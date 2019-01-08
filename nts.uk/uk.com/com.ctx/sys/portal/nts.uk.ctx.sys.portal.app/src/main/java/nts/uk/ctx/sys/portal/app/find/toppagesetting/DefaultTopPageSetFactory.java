package nts.uk.ctx.sys.portal.app.find.toppagesetting;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.arc.layer.app.file.storage.FileStorage;
import nts.arc.layer.app.file.storage.StoredFileInfo;
import nts.gul.text.StringUtil;
import nts.uk.ctx.sys.portal.app.find.placement.PlacementDto;
import nts.uk.ctx.sys.portal.app.find.placement.PlacementPartDto;
import nts.uk.ctx.sys.portal.app.find.toppage.TopPageDto;
import nts.uk.ctx.sys.portal.app.find.toppage.TopPageFinder;
import nts.uk.ctx.sys.portal.dom.enums.MenuClassification;
import nts.uk.ctx.sys.portal.dom.enums.System;
import nts.uk.ctx.sys.portal.dom.enums.TopPagePartType;
import nts.uk.ctx.sys.portal.dom.enums.UseDivision;
import nts.uk.ctx.sys.portal.dom.flowmenu.FlowMenu;
import nts.uk.ctx.sys.portal.dom.flowmenu.FlowMenuRepository;
import nts.uk.ctx.sys.portal.dom.layout.Layout;
import nts.uk.ctx.sys.portal.dom.layout.PGType;
import nts.uk.ctx.sys.portal.dom.mypage.MyPage;
import nts.uk.ctx.sys.portal.dom.mypage.MyPageRepository;
import nts.uk.ctx.sys.portal.dom.mypage.setting.MyPageSetting;
import nts.uk.ctx.sys.portal.dom.mypage.setting.MyPageSettingRepository;
import nts.uk.ctx.sys.portal.dom.placement.Placement;
import nts.uk.ctx.sys.portal.dom.placement.PlacementRepository;
import nts.uk.ctx.sys.portal.dom.placement.externalurl.ExternalUrl;
import nts.uk.ctx.sys.portal.dom.standardmenu.StandardMenu;
import nts.uk.ctx.sys.portal.dom.standardmenu.StandardMenuRepository;
import nts.uk.ctx.sys.portal.dom.toppagepart.TopPagePart;
import nts.uk.ctx.sys.portal.dom.toppagepart.service.TopPagePartService;
import nts.uk.ctx.sys.portal.dom.toppagesetting.PersonPermissionSetting;
import nts.uk.ctx.sys.portal.dom.toppagesetting.PortalJobTitleAdapter;
import nts.uk.ctx.sys.portal.dom.toppagesetting.PortalJobTitleImport;
import nts.uk.ctx.sys.portal.dom.toppagesetting.TopMenuCode;
import nts.uk.ctx.sys.portal.dom.toppagesetting.TopPageJobSet;
import nts.uk.ctx.sys.portal.dom.toppagesetting.TopPageJobSetRepository;
import nts.uk.ctx.sys.portal.dom.toppagesetting.TopPagePersonSet;
import nts.uk.ctx.sys.portal.dom.toppagesetting.TopPagePersonSetRepository;
import nts.uk.ctx.sys.portal.dom.toppagesetting.TopPageSelfSetRepository;
import nts.uk.shr.com.context.AppContexts;

/**
 * 
 * @author hoatt
 *
 */
@Stateless
public class DefaultTopPageSetFactory implements TopPageSetFactory {

	@Inject
	private TopPagePartService topPagePartService;
	@Inject
	private TopPageSelfSettingFinder topPageSelfSet;
	@Inject
	private TopPagePersonSetRepository topPagePerson;
	@Inject
	private StandardMenuRepository standardMenu;
	@Inject
	private TopPageFinder toppageFinder;
	@Inject
	private MyPageSettingRepository myPageSet;
	@Inject
	private TopPageSelfSetRepository toppageRepository;
	@Inject
	private PlacementRepository placementRepository;
	@Inject
	private MyPageRepository mypage;
	@Inject
	private TopPageJobSetRepository topPageJobSet;
	@Inject
	private FlowMenuRepository flowMenuRepository;
	@Inject
	private FileStorage fileStorage;
	@Inject
	private PortalJobTitleAdapter jobTitleAdapter;

	private static final String LOGIN_SCREEN = "login";

	/**
	 * display my page
	 */
	@Override
	public LayoutForMyPageDto buildLayoutMyPage(Layout layout, List<Placement> placements) {
		String companyId = AppContexts.user().companyId();
		String employeeId = AppContexts.user().employeeId();

		// get my page setting
		Optional<MyPageSetting> myPageS = myPageSet.findMyPageSet(companyId);
		if (!myPageS.isPresent()) {
			return new LayoutForMyPageDto(employeeId, "", PGType.MYPAGE.value, true, null, null);
		}
		MyPageSetting myPage = myPageS.get();
		// get placement
		List<PlacementDto> placementDtos = buildPlacementDto(layout, placements, myPage, false);
		// get flow menu
		List<FlowMenuPlusDto> flowmenuNew = buildFlowMenu(companyId, placementDtos);

		// process data with not flow menu
		List<PlacementDto> placementNew = placementDtos.stream()
				.filter(placementDto -> !(placementDto.getPlacementPartDto().getUrl() == null
						&& placementDto.getPlacementPartDto().getType().intValue() == TopPagePartType.FlowMenu.value))
				.collect(Collectors.toList());

		// return
		return new LayoutForMyPageDto(employeeId, layout.getLayoutID(), PGType.MYPAGE.value, false, flowmenuNew, placementNew);
	}

	/**
	 * display top page
	 */
	@Override
	public LayoutForTopPageDto buildLayoutTopPage(Layout layout, List<Placement> placements) {
		String companyId = AppContexts.user().companyId();
		// get placement
		List<PlacementDto> placementDtos = buildPlacementDto(layout, placements, null, true);

		// process data with flow menu
		List<FlowMenuPlusDto> flowmenuNew = buildFlowMenu(companyId, placementDtos);
		
		// process data with not flow menu
		List<PlacementDto> placementNew = placementDtos.stream()
				.filter(placementDto -> !(placementDto.getPlacementPartDto().getUrl() == null
						&& placementDto.getPlacementPartDto().getType().intValue() == TopPagePartType.FlowMenu.value))
				.collect(Collectors.toList());

		return new LayoutForTopPageDto(layout.getCompanyID(), layout.getLayoutID(), layout.getPgType().value,
				flowmenuNew, placementNew, null);
	}

	/**
	 * get by position
	 */
	@Override
	public LayoutAllDto getTopPageForPosition(String fromScreen, PortalJobTitleImport jobPosition,TopPageJobSet topPageJob) {
		/**
		 * check = true (When start: display top page) check = false (When
		 * start: display my page)
		 */
		boolean check = false;
		// companyId
		String companyId = AppContexts.user().companyId();
		// employeeId
		String employeeId = AppContexts.user().employeeId();
		//ログインユーザーの社員IDをもとに、ドメインモデル「本人トップページ設定」を取得する
		TopPageSelfSettingDto tpSelfSet = topPageSelfSet.getTopPageSelfSet();
		LayoutForTopPageDto layoutTopPage = null;
		// check my page: use or not use
		boolean checkMyPage = checkMyPageSet();
		// check top page: setting or not setting
		boolean checkTopPage = checkTopPageSet();
		// check login screen
		boolean isLoginScreen = LOGIN_SCREEN.equals(fromScreen);
		// check top page job setting or not setting
		if (topPageJob.getPersonPermissionSet() == PersonPermissionSetting.SET && tpSelfSet != null && !isLoginScreen) {
			// display top page self set (本人トップページ設定)-C
			check = false;
			layoutTopPage = getTopPageByCode(companyId, tpSelfSet.getCode(), System.COMMON.value,
					MenuClassification.TopPage.value, true);
			if (!checkMyPage) {// not use my page
				check = true;
				return new LayoutAllDto(null, layoutTopPage, check, checkMyPage, checkTopPage);
			}
			LayoutForMyPageDto layoutMypage = findLayoutMyPage();
			if(layoutMypage.isNotActiveMyPage()==true && layoutTopPage!=null){
					check = true;
			}
			if(layoutMypage.getLayoutID() == null){//Khong hien thi myPage
				checkMyPage = false;
				check = true;
			}
			return new LayoutAllDto(layoutMypage, layoutTopPage, check, checkMyPage, checkTopPage);
		}
		// topPageJob: setting
		Optional<TopPagePersonSet> topPPerson = topPagePerson.getbyCode(companyId, employeeId);
		//String code = getMenuCode(fromScreen, topPageJob.getTopMenuCode(), topPageJob.getLoginMenuCode());
		// login menu code & top page person
		if (topPPerson.isPresent()) {
			// display top page person set (個人別トップページ設定)-B
			TopPagePersonSet tpPerson = topPPerson.get();
			check = false;
			String menuCode = getMenuCode(fromScreen, tpPerson.getTopMenuCode(), tpPerson.getLoginMenuCode());
			if (!StringUtil.isNullOrEmpty(menuCode, true)) {
				layoutTopPage = getTopPageByCode(companyId, menuCode, tpPerson.getLoginSystem().value,
						MenuClassification.TopPage.value, isLoginScreen);
				if(layoutTopPage == null) {
					layoutTopPage = getTopPageByCode(companyId, menuCode, tpPerson.getLoginSystem().value,
							MenuClassification.Standard.value, isLoginScreen);
				}
				// case not use my page
				if (!checkMyPage) {
					check = true;
					return new LayoutAllDto(null, layoutTopPage, check, false, checkTopPage);
				}
				LayoutForMyPageDto layoutMypage = findLayoutMyPage();
				if(layoutMypage.isNotActiveMyPage()==true && layoutTopPage!=null){
					check = true;
				}
				if(layoutMypage.getLayoutID() == null){//Khong hien thi myPage
					checkMyPage = false;
					check = true;
				}
				return new LayoutAllDto(layoutMypage, layoutTopPage, check, true, checkTopPage);
			}
		}
		String code = getMenuCode(fromScreen, topPageJob.getTopMenuCode(), topPageJob.getLoginMenuCode());
		// not top page person or not login menu code
		if (StringUtil.isNullOrEmpty(code, true) && tpSelfSet != null) {// login menu code: empty
			// display my page
			check = false;
			layoutTopPage = getTopPageByCode(companyId, tpSelfSet.getCode(), System.COMMON.value,
					MenuClassification.TopPage.value, true);
			if (!checkMyPage) {// not use my page
				check = true;
				return new LayoutAllDto(null, layoutTopPage, check, checkMyPage, checkTopPage);
			}
			// use my page
			LayoutForMyPageDto layoutMypage = findLayoutMyPage();
			if(layoutMypage.getLayoutID() == null){//Khong hien thi myPage
				checkMyPage = false;
				check = true;
			}
			return new LayoutAllDto(layoutMypage, layoutTopPage, check, checkMyPage, checkTopPage);
		}
		//// display top page job title set (職位別トップページ設定)-A
		check = false;
		String menuCode = getMenuCode(fromScreen, topPageJob.getTopMenuCode(), topPageJob.getLoginMenuCode());
		if(isLoginScreen) {
			layoutTopPage = getTopPageByCode(companyId, menuCode, topPageJob.getLoginSystem().value,
					topPageJob.getMenuClassification().value, isLoginScreen);
		}else {
			layoutTopPage = getTopPageByCode(companyId, menuCode, topPageJob.getLoginSystem().value,
					MenuClassification.TopPage.value, isLoginScreen);
		}
		if (!checkMyPage) {// not use my page
			check = true;
			return new LayoutAllDto(null, layoutTopPage, check, checkMyPage, checkTopPage);
		}
		LayoutForMyPageDto layoutMypage = findLayoutMyPage();
		if(layoutMypage.isNotActiveMyPage()==true && layoutTopPage!=null){
			check = true;
		}
		if(layoutMypage.getLayoutID() == null){//Khong hien thi myPage
			checkMyPage = false;
			check = true;
		}
		return new LayoutAllDto(layoutMypage, layoutTopPage, check, checkMyPage, checkTopPage);
	}

	/**
	 * get by not position
	 */
	@Override
	public LayoutAllDto getTopPageNotPosition(String fromScreen) {
		/**
		 * check = true (When start: display top page) check = false (When
		 * start: display my page)
		 */
		boolean check = false;
		String companyId = AppContexts.user().companyId();
		String employeeId = AppContexts.user().employeeId();
		
		// get data from domain person set (個人別トップページ設定)
		Optional<TopPagePersonSet> topPPerson = topPagePerson.getbyCode(companyId, employeeId);
		LayoutForTopPageDto layoutToppage = null;
		// check my page: use or not use
		boolean checkMyPage = checkMyPageSet();
		// check top page: setting or not setting
		boolean checkTopPage = checkTopPageSet();
		if (topPPerson.isPresent()) {// co tpPerson
			TopPagePersonSet tpPerson = topPPerson.get();
			String code = getMenuCode(fromScreen, tpPerson.getTopMenuCode(), tpPerson.getLoginMenuCode());
			if (!StringUtil.isNullOrEmpty(code, true)) {// login menu code not
														// empty
				// display top page person set (個人別トップページ設定)-B
				check = false;
				layoutToppage = getTopPageByCode(companyId, code, tpPerson.getLoginSystem().value,
						tpPerson.getMenuClassification().value, LOGIN_SCREEN.equals(fromScreen));
				if (!checkMyPage) {// not use my page
					check = true;
					return new LayoutAllDto(null, layoutToppage, true, checkMyPage, checkTopPage);
				}
				LayoutForMyPageDto layoutMypage = findLayoutMyPage();
				if(layoutMypage.isNotActiveMyPage()==true && layoutToppage!=null){
					check = true;
				}
				if(layoutMypage.getLayoutID() == null){//Khong hien thi myPage
					checkMyPage = false;
					check = true;
				}
				return new LayoutAllDto(layoutMypage, layoutToppage, check, checkMyPage, checkTopPage);
			}
		}
		check = false;
		// display only my page
		if (!checkMyPage) {// not use my page
			check = true;
			return new LayoutAllDto(null, null, check, checkMyPage, checkTopPage);
		}
		LayoutForMyPageDto layoutMypage = findLayoutMyPage();
		if(layoutMypage.getLayoutID() == null){//Khong hien thi myPage
			checkMyPage = false;
			check = true;
		}
		return new LayoutAllDto(layoutMypage, null, check, checkMyPage, checkTopPage);
	}

	/**
	 * check my page: setting or not setting
	 */
	@Override
	public boolean checkMyPageSet() {
		// companyId
		String companyId = AppContexts.user().companyId();
		Optional<MyPageSetting> myPage = myPageSet.findMyPageSet(companyId);
		if (!myPage.isPresent()) {
			return false;
		}
		
		return UseDivision.Use.equals(myPage.get().getUseMyPage());
	}

	/**
	 * check top page: use or not use
	 * 
	 * @return
	 */
	@Override
	public boolean checkTopPageSet() {
		// companyId
		String companyId = AppContexts.user().companyId();
		List<String> lst = new ArrayList<>();
		// lay job position
		Optional<PortalJobTitleImport> jobPosition = jobTitleAdapter.getJobPosition(AppContexts.user().employeeId());
		if (!jobPosition.isPresent()) {
			return false;
		}

		lst.add(jobPosition.get().getJobTitleID());
		// lay top page job title set
		List<TopPageJobSet> lstTpJobSet = topPageJobSet.findByListJobId(companyId, lst);
		if (lstTpJobSet.isEmpty()) {
			return false;
		}

		TopPageJobSet tpJobSet = lstTpJobSet.get(0);
		return (tpJobSet != null && tpJobSet.getPersonPermissionSet().value == PersonPermissionSetting.SET.value);
	}

	/**
	 * find lay out my page
	 * 
	 * @return
	 */
	@Override
	public LayoutForMyPageDto findLayoutMyPage() {
		// employeeId
		String employeeId = AppContexts.user().employeeId();
		//hoatt 2019.01.07
		//EA修正履歴 No.3053
		if(employeeId == null){//ログインユーザコンテキストの社員IDをチェックする
			//ログインユーザコンテキスト．社員IDがNullの場合
			return new LayoutForMyPageDto(null, null, PGType.MYPAGE.value, false, null, null);
		}
		Optional<MyPage> mPage = mypage.getMyPage(employeeId);
		if (!mPage.isPresent()) {// register my page
			MyPage mypageNew = MyPage.createNew(employeeId);
			mypage.addMyPage(mypageNew);
			// check = true
			return new LayoutForMyPageDto(employeeId, mypageNew.getLayoutId(), PGType.MYPAGE.value, true, null, null);
		}
		
		Optional<Layout> layout = toppageRepository.find(mPage.get().getLayoutId(), PGType.MYPAGE.value);
		if (!layout.isPresent()) {
			// check = false
			return new LayoutForMyPageDto(employeeId, mPage.get().getLayoutId(), PGType.MYPAGE.value, false, null, null);	
		}
		
		List<Placement> placements = placementRepository.findByLayout(mPage.get().getLayoutId());
		if (!placements.isEmpty()) {
			// check = false
			return buildLayoutMyPage(layout.get(), placements);
		}
		
		return new LayoutForMyPageDto(employeeId, mPage.get().getLayoutId(), PGType.MYPAGE.value, false, null, null);
	}

	/**
	 * build placement my page
	 * 
	 * @param layout
	 * @param placements
	 * @param myPage
	 * @return
	 */
	private List<PlacementDto> buildPlacementDto(Layout layout, List<Placement> placements, MyPageSetting myPage, boolean topOrMy) {
		List<TopPagePart> activeTopPageParts = topPagePartService.getAllActiveTopPagePart(layout.getCompanyID(), layout.getPgType());
		List<PlacementDto> placementDtos = new ArrayList<PlacementDto>();
		for (Placement placement : placements) {
			if (placement.isExternalUrl()) {
				// Case top page
				if (topOrMy) {
					ExternalUrl externalUrl = placement.getExternalUrl().get();
					placementDtos.add(new PlacementDto(placement.getPlacementID(), placement.getLayoutID(),
							placement.getColumn().v(), placement.getRow().v(), PlacementPartDto.createExternalUrl(externalUrl)));
					continue;
				}
				
				// Case my page
				if (UseDivision.Use.equals(myPage.getUseMyPage())) { //todo myPage.getExternalUrlPermission().value == UseDivision.Use.value
					ExternalUrl externalUrl = placement.getExternalUrl().get();
					placementDtos.add(new PlacementDto(placement.getPlacementID(), placement.getLayoutID(),
							placement.getColumn().v(), placement.getRow().v(), PlacementPartDto.createExternalUrl(externalUrl)));
				}
				continue;
			}
			
			Optional<TopPagePart> topPagePart = activeTopPageParts.stream().filter(c -> c.getToppagePartID().equals(placement.getToppagePartID())).findFirst();
			if (topPagePart.isPresent()) {
				placementDtos.add(new PlacementDto(placement.getPlacementID(), placement.getLayoutID(),
						placement.getColumn().v(), placement.getRow().v(), PlacementPartDto.createFromTopPagePart(topPagePart.get())));
			}
		}
		return placementDtos;
	}
	
	/**
	 * Build layout with flow menu
	 * @param companyId
	 * @param placementDtos
	 * @return
	 */
	private List<FlowMenuPlusDto> buildFlowMenu(String companyId, List<PlacementDto> placementDtos) {
		// get top page part id by flow menu
		List<String> listTopPagePartIdTypeFlow = placementDtos.stream()
				.filter(placementDto -> placementDto.getPlacementPartDto().getUrl() == null
						&& placementDto.getPlacementPartDto().getType().intValue() == TopPagePartType.FlowMenu.value)
				.map(x -> x.getPlacementPartDto().getTopPagePartID()).distinct().collect(Collectors.toList());

		// get placement by flow menu
		List<PlacementDto> placementFlows = placementDtos.stream()
				.filter(placementDto -> placementDto.getPlacementPartDto().getUrl() == null
						&& placementDto.getPlacementPartDto().getType().intValue() == TopPagePartType.FlowMenu.value)
				.collect(Collectors.toList());

		// process data with flow menu
		List<FlowMenuPlusDto> flowmenuNew = new ArrayList<FlowMenuPlusDto>();
		if (!listTopPagePartIdTypeFlow.isEmpty()) {
			// get all flow menu by top page part id
			List<FlowMenu> flowmenus = flowMenuRepository.findByCodes(companyId, listTopPagePartIdTypeFlow);
			Map<String, FlowMenu> flowMenuMap = flowmenus.stream()
					.collect(Collectors.toMap(FlowMenu::getToppagePartID, x -> x));

			for (PlacementDto placementDto : placementFlows) {
				FlowMenu flowMenu = flowMenuMap.get(placementDto.getPlacementPartDto().getTopPagePartID());
				if (flowMenu == null) {
					continue;
				}

				// get file infe
				Optional<StoredFileInfo> fileInfo = fileStorage.getInfo(flowMenu.getFileID());

				FlowMenuPlusDto fMenu = new FlowMenuPlusDto(flowMenu.getToppagePartID(), flowMenu.getCode().v(),
						flowMenu.getName().v(), flowMenu.getType().value, flowMenu.getWidth().v(),
						flowMenu.getHeight().v(), flowMenu.getFileID(),
						fileInfo != null ? fileInfo.get().getOriginalName() : "", flowMenu.getDefClassAtr().value,
						placementDto.getRow(), placementDto.getColumn());

				flowmenuNew.add(fMenu);
			}
		}
		return flowmenuNew;
	}
	
	/**
	 * get info of top page or standard menu
	 */
	private LayoutForTopPageDto getTopPageByCode(String companyId, String code, int system, int classification, boolean isLoginScreen) {
		if (classification == MenuClassification.TopPage.value) {// topPage
			TopPageDto topPage = toppageFinder.findByCode(companyId, code, "0");
			if(topPage==null){
				return null;
			}
			Optional<Layout> layout = toppageRepository.find(topPage.getLayoutId(), PGType.TOPPAGE.value);
			if (!layout.isPresent()) {
				return new LayoutForTopPageDto(companyId, null, PGType.TOPPAGE.value,null, null, null);
			}
			List<Placement> placements = placementRepository.findByLayout(topPage.getLayoutId());
			return buildLayoutTopPage(layout.get(), placements);
		}
		
		// standard menu
		Optional<StandardMenu> sMenu = standardMenu.getStandardMenubyCode(companyId, code, system, classification);
		if (!sMenu.isPresent()) {
			return null;
		}
		
		List<FlowMenuPlusDto> flowMenu = null;
		List<PlacementDto> placements = null;
		return new LayoutForTopPageDto(companyId, "", PGType.TOPPAGE.value, flowMenu, placements,
				sMenu.get().getUrl());	
	}
	

	/**
	 * Get menu code
	 * 
	 * @param fromScreen
	 * @param topMenuCode
	 *            top page code
	 * @param loginMenuCode
	 *            top page code or standard menu code
	 * @return login menu code if from screen login else other screen
	 */
	private String getMenuCode(String fromScreen, TopMenuCode topMenuCode, TopMenuCode loginMenuCode) {
		return LOGIN_SCREEN.equals(fromScreen) ? loginMenuCode.v() : topMenuCode.v();
	}
}
