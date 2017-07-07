package nts.uk.ctx.sys.portal.app.find.toppagesetting;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.uk.ctx.sys.portal.app.find.toppage.TopPageDto;
import nts.uk.ctx.sys.portal.app.find.toppage.TopPageFinder;
import nts.uk.ctx.sys.portal.dom.layout.Layout;
import nts.uk.ctx.sys.portal.dom.layout.PGType;
import nts.uk.ctx.sys.portal.dom.placement.Placement;
import nts.uk.ctx.sys.portal.dom.placement.PlacementRepository;
import nts.uk.ctx.sys.portal.dom.toppagesetting.TopPageJobSet;
import nts.uk.ctx.sys.portal.dom.toppagesetting.TopPageJobSetRepository;
import nts.uk.ctx.sys.portal.dom.toppagesetting.TopPageSelfSetRepository;
import nts.uk.shr.com.context.AppContexts;
/**
 * 
 * @author hoatt
 *
 */
@Stateless
public class DisplayMyPageFinder {
	@Inject
	private TopPageSelfSetRepository toppageRepository;
	@Inject
	private PlacementRepository placementRepository;
	@Inject
	private TopPageSetFactory topPageSet;
	@Inject
	private TopPageFinder toppageFinder;
	@Inject
	private TopPageJobSetRepository topPageJobSet;
	@Inject
	private TopPageSelfSettingFinder topPageSelfSet;
	/**
	 * find layout (top page)
	 * @param topPageCode
	 * @return
	 */
	public LayoutAllDto findLayoutTopPage(String topPageCode){
		//companyId
		String companyId = AppContexts.user().companyId();
		if(topPageCode != null && topPageCode != ""){//co top page code
			LayoutForMyPageDto layoutMypage = topPageSet.findLayoutMyPage();
			//check my page: use or not use
			boolean checkMyPage = topPageSet.checkMyPageSet();
			//check top page: setting or not setting
			boolean checkTopPage = topPageSet.checkTopPageSet();
			TopPageDto topPage = toppageFinder.findByCode(companyId, topPageCode, "0");
			if(topPage==null){//data is empty
				return new LayoutAllDto(layoutMypage,null,true,checkMyPage,checkTopPage);
			}
			Optional<Layout> layout = toppageRepository.find(topPage.getLayoutId(),PGType.TOPPAGE.value);
			if (layout.isPresent()) {//co du lieu
				List<Placement> placements = placementRepository.findByLayout(topPage.getLayoutId());
				LayoutForTopPageDto layoutTopPage = topPageSet.buildLayoutTopPage(layout.get(), placements);
				return new LayoutAllDto(layoutMypage,layoutTopPage,true,checkMyPage,checkTopPage);
			}
			return new LayoutAllDto(layoutMypage,null,true,checkMyPage,checkTopPage);
		}
		//top page code is empty
		//get position(所属職位履歴)
		JobPositionDto jobPosition = topPageSelfSet.getJobPosition(AppContexts.user().employeeId());
		List<String> lstJobId = new ArrayList<>();
		if (jobPosition == null) {
			return topPageSet.getTopPageNotPosition();
		}
			  
		lstJobId.add(jobPosition.getJobId());
			  
		//lay top page job title set
		List<TopPageJobSet>lstTpJobSet = topPageJobSet.findByListJobId(companyId, lstJobId);
		if(lstTpJobSet.isEmpty()){//position and job setting
			return topPageSet.getTopPageNotPosition();
		}
		TopPageJobSet tpJobSet = lstTpJobSet.get(0);
			  
		return topPageSet.getTopPageForPosition(jobPosition, tpJobSet);
		
	}
}
