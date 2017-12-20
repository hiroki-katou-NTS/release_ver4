package nts.uk.ctx.sys.portal.app.find.webmenu;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.arc.enums.EnumAdaptor;
import nts.arc.enums.EnumConstant;
import nts.arc.error.BusinessException;
import nts.arc.error.RawErrorMessage;
import nts.arc.i18n.I18NResources;
import nts.arc.scoped.request.RequestContextProvider;
import nts.arc.time.GeneralDate;
import nts.uk.ctx.sys.portal.app.find.standardmenu.StandardMenuDto;
import nts.uk.ctx.sys.portal.app.find.webmenu.detail.MenuBarDetailDto;
import nts.uk.ctx.sys.portal.app.find.webmenu.detail.TitleBarDetailDto;
import nts.uk.ctx.sys.portal.app.find.webmenu.detail.TreeMenuDetailDto;
import nts.uk.ctx.sys.portal.app.find.webmenu.detail.WebMenuDetailDto;
import nts.uk.ctx.sys.portal.dom.adapter.role.AffJobHistoryDto;
import nts.uk.ctx.sys.portal.dom.adapter.role.EmployeeDto;
import nts.uk.ctx.sys.portal.dom.adapter.role.RoleGrantAdapter;
import nts.uk.ctx.sys.portal.dom.adapter.role.RoleSetGrantedJobTitleDetailDto;
import nts.uk.ctx.sys.portal.dom.adapter.role.RoleSetGrantedPersonDto;
import nts.uk.ctx.sys.portal.dom.adapter.role.UserDto;
import nts.uk.ctx.sys.portal.dom.enums.MenuClassification;
import nts.uk.ctx.sys.portal.dom.enums.System;
import nts.uk.ctx.sys.portal.dom.standardmenu.StandardMenu;
import nts.uk.ctx.sys.portal.dom.standardmenu.StandardMenuRepository;
import nts.uk.ctx.sys.portal.dom.webmenu.MenuBar;
import nts.uk.ctx.sys.portal.dom.webmenu.SelectedAtr;
import nts.uk.ctx.sys.portal.dom.webmenu.TitleBar;
import nts.uk.ctx.sys.portal.dom.webmenu.WebMenu;
import nts.uk.ctx.sys.portal.dom.webmenu.WebMenuRepository;
import nts.uk.ctx.sys.portal.dom.webmenu.personaltying.PersonalTying;
import nts.uk.ctx.sys.portal.dom.webmenu.personaltying.PersonalTyingRepository;
import nts.uk.ctx.sys.portal.dom.webmenu.webmenulinking.RoleByRoleTies;
import nts.uk.ctx.sys.portal.dom.webmenu.webmenulinking.RoleByRoleTiesRepository;
import nts.uk.ctx.sys.portal.dom.webmenu.webmenulinking.RoleSetLinkWebMenu;
import nts.uk.ctx.sys.portal.dom.webmenu.webmenulinking.RoleSetLinkWebMenuRepository;
import nts.uk.shr.com.context.AppContexts;
import nts.uk.shr.com.context.AppContextsConfig;
import nts.uk.shr.com.context.LoginUserContext;
import nts.uk.shr.com.program.Program;
import nts.uk.shr.com.program.ProgramsManager;
import nts.uk.shr.com.program.WebAppId;

@Stateless
public class WebMenuFinder {

	@Inject
	private WebMenuRepository webMenuRepository;
	
	@Inject
	private StandardMenuRepository standardMenuRepository;
	
	@Inject
	private I18NResources internationalization;
	
	@Inject
	private PersonalTyingRepository personalTyingRepository;
	
	@Inject
	private RoleByRoleTiesRepository roleTiesRepository;
	
	@Inject
	private RoleSetLinkWebMenuRepository roleSetLinkMenuRepo;
	
	@Inject
	private RoleGrantAdapter roleAdapter;

	/**
	 * Find a web menu by code
	 * @param webMenuCode
	 * @return
	 */
	public WebMenuDto find(String webMenuCode) {
		String companyId = AppContexts.user().companyId();
		Optional<WebMenu> webMenuOpt = webMenuRepository.find(companyId, webMenuCode);
		if (!webMenuOpt.isPresent()) {
			return null;
		}
		
		return webMenuOpt.map(webMenuItem -> {
			List<MenuBarDto> menuBars = toMenuBar(webMenuItem);
			
			return new WebMenuDto(
					companyId, 
					webMenuItem.getWebMenuCode().v(), 
					webMenuItem.getWebMenuName().v(), 
					webMenuItem.getDefaultMenu().value,
					menuBars);
		}).get();
	}
	
	/**
	 * Find menu list.
	 * @param codes codes
	 * @return menu list
	 */
	public List<WebMenuDetailDto> find(List<String> codes) {
		String companyId = AppContexts.user().companyId();
		List<WebMenu> webMenus = webMenuRepository.find(companyId, codes);
		return webMenus.stream().map(m -> toMenuDetails(m, companyId)).collect(Collectors.toList());
	}
	
	/**
	 * Find all item web menu
	 * @return
	 */
	public List<WebMenuDto> findAll() {

		String companyId = AppContexts.user().companyId();

		List<WebMenu> webMenuList = webMenuRepository.findAll(companyId);

		List<WebMenuDto> result = webMenuList.stream().map(webMenuItem -> {
			List<MenuBarDto> menuBars = toMenuBar(webMenuItem);
			
			return new WebMenuDto(
					companyId, 
					webMenuItem.getWebMenuCode().v(), 
					webMenuItem.getWebMenuName().v(), 
					webMenuItem.getDefaultMenu().value,
					menuBars);
		}).collect(Collectors.toList());
		
		return result;
	}
	
	/**
	 * Find all menu details.
	 * @return menu details
	 */
	public List<WebMenuDetailDto> findAllDetails() {
		LoginUserContext userCtx = AppContexts.user();
		String companyId = userCtx.companyId();
		List<String> menus = getMenuSet(companyId, userCtx.userId());
		return menus != null && !menus.isEmpty() ? find(menus) : Arrays.asList(findDefault());
	}
	
	/**
	 * Get menu set.
	 * @param companyId companyId
	 * @param userId userId
	 * @return list of menu codes
	 */
	private List<String> getMenuSet(String companyId, String userId) {
		if (companyId == null || userId == null) return null;
		// Get role ties
		Optional<String> roleId = roleAdapter.getRoleId(userId);
		Optional<RoleByRoleTies> roleTies = Optional.empty();
		if (roleId.isPresent()) {
			roleTies = roleTiesRepository.getRoleByRoleTiesById(roleId.get());
		}
		
		// Get role set
		UserDto user = roleAdapter.getUserInfo(userId)
				.orElseThrow(() -> new BusinessException(new RawErrorMessage("User not found.")));
		if (user.getAssociatedPersonID() == null) throw new BusinessException(new RawErrorMessage("Personal ID not existed."));
		EmployeeDto employee = roleAdapter.getEmployee(companyId, user.getAssociatedPersonID())
				.orElseThrow(() -> new BusinessException(new RawErrorMessage("Employee not found.")));
		Optional<RoleSetGrantedPersonDto> roleSetGrantedPerson = roleAdapter.getRoleSetPersonGrant(employee.getEmployeeId());
		
		String roleSetCd = null;
		if (roleSetGrantedPerson.isPresent()) {
			roleSetCd = roleSetGrantedPerson.get().getRoleSetCd();
		} else {
			Optional<AffJobHistoryDto> jobHistory = roleAdapter.getAffJobHist(employee.getEmployeeId(), GeneralDate.today());
			if (jobHistory.isPresent()) {
				Optional<RoleSetGrantedJobTitleDetailDto> roleSetGrantedJobTitle = roleAdapter.getRoleSetJobTitleGrant(companyId, jobHistory.get().getJobTitleId());
				if (roleSetGrantedJobTitle.isPresent()) {
					roleSetCd = roleSetGrantedJobTitle.get().getRoleSetCd();
				} else {
					// TODO: Get 兼務職位履歴 and check if not present, then get default role set 
					// otherwise, get RoleSetGrantedJobTitleDetailDto with retrieved job title Id
					// and applyToConcurrentPerson = true
					roleSetCd = getDefaultRoleSet(companyId);
				}
			} else {
				roleSetCd = getDefaultRoleSet(companyId);
			}
		}
		
		List<RoleSetLinkWebMenu> menus = roleSetLinkMenuRepo.findByRoleSetCd(companyId, roleSetCd);
		List<String> menuCodes = menus.stream().map(m -> m.getWebMenuCd().v()).collect(Collectors.toList());
		if (roleTies.isPresent()) {
			String menuCode = roleTies.get().getWebMenuCd().v();
			if (menuCodes.stream().noneMatch(m -> menuCode.equals(m))) {
				menuCodes.add(menuCode);
			}
		}
		return menuCodes;
	}
	
	/**
	 * Get default role set.
	 * @param companyId company Id
	 * @return default role set
	 */
	private String getDefaultRoleSet(String companyId) {
		return roleAdapter.getDefaultRoleSet(companyId)
				.orElseThrow(() -> new RuntimeException("Default role set not found."))
				.getRoleSetCd();
	}
	
	/**
	 * Find default menu.
	 * @return default menu
	 */
	public WebMenuDetailDto findDefault() {
		String companyId = AppContexts.user().companyId();
		Optional<WebMenu> menuOpt = webMenuRepository.findDefault(companyId);
		if (!menuOpt.isPresent()) return null;
		WebMenu menu = menuOpt.get();
		return toMenuDetails(menu, companyId);
	}
	
	/**
	 * To menu details.
	 * @param menu menu
	 * @param companyId company Id
	 * @return web menu details
	 */
	private WebMenuDetailDto toMenuDetails(WebMenu menu, String companyId) {
		List<MenuBarDetailDto> menuBar = menu.getMenuBars().stream().map(m -> {
			String link = null;
			if (m.getSelectedAtr() == SelectedAtr.DirectActivation) {
				StandardMenu sMenu = findStandardMenu(companyId, m.getCode().v(), m.getSystem().value, m.getMenuCls().value);
				link = getProgramPath(sMenu);
			}
			
			List<TitleBarDetailDto> titleBars = m.getTitleMenu().stream().map(t -> {
				List<TreeMenuDetailDto> treeMenus = t.getTreeMenu().stream().map(tm -> {
					String menuCode = tm.getCode().v();
					int system = tm.getSystem().value;
					int classification = tm.getClassification().value;
					StandardMenu stdMenu = findStandardMenu(companyId, menuCode, system, classification);
					String path = getProgramPath(stdMenu);
					return new TreeMenuDetailDto(menuCode, stdMenu.getTargetItems(), stdMenu.getDisplayName().v(),
							path, tm.getDisplayOrder(), system, stdMenu.getMenuAtr().value, classification, 
							stdMenu.getAfterLoginDisplay(), stdMenu.getProgramId(), stdMenu.getScreenId(), stdMenu.getQueryString());
				}).sorted((tm1, tm2) -> tm1.getDisplayOrder() - tm2.getDisplayOrder()).collect(Collectors.toList());
				
				return new TitleBarDetailDto(t.getTitleMenuId().toString(), 
						t.getTitleMenuName().v(), t.getBackgroundColor().v(), t.getImageFile(),
						t.getTextColor().v(), t.getTitleMenuAtr().value, t.getTitleMenuCode().v(),
						t.getDisplayOrder(), treeMenus);
			}).sorted((t1, t2) -> t1.getDisplayOrder() - t2.getDisplayOrder()).collect(Collectors.toList());
			
			return new MenuBarDetailDto(m.getMenuBarId().toString(),
					m.getCode().v(), m.getMenuBarName().v(), m.getSelectedAtr().value, link,
					m.getSystem().value, m.getMenuCls().value, m.getBackgroundColor().v(),
					m.getTextColor().v(), m.getDisplayOrder().intValue(), titleBars);
		}).sorted((m1, m2) -> m1.getDisplayOrder() - m2.getDisplayOrder()).collect(Collectors.toList());
		return new WebMenuDetailDto(companyId, menu.getWebMenuCode().v(),
				menu.getWebMenuName().v(), menu.getDefaultMenu().value, menuBar);
	}
	
	/**
	 * Find standard menu.
	 * @param companyId companyId
	 * @param menuCode menuCode
	 * @param system system
	 * @param classification classification
	 * @return standard menu
	 */
	private StandardMenu findStandardMenu(String companyId, String menuCode, int system, int classification) {
		Optional<StandardMenu> standardMenu = standardMenuRepository.getStandardMenubyCode(companyId, menuCode, system, classification);
		return standardMenu.orElseThrow(() -> new RuntimeException("Menu not found."));
	}
	
	/**
	 * Find program path.
	 * @param companyId companyId
	 * @param menuCode menuCode
	 * @param system system
	 * @param classification classification
	 * @return program path
	 */
	private String getProgramPath(StandardMenu stdMenu) {
		// Get system
		System sys = stdMenu.getSystem();
		WebAppId appId = null;
		if (sys == System.COMMON) {
			appId = WebAppId.COM;
		} else if (sys == System.TIME_SHEET) {
			appId = WebAppId.AT;
		} else if (sys == System.KYUYOU) {
			appId = WebAppId.PR;
		}
		
		String url = stdMenu.getUrl();
		if (url == null || "".equals(url)) {
			Optional<Program> programOpt = ProgramsManager.findById(appId, 
					stdMenu.getProgramId() + (stdMenu.getScreenId() == null ? "" : stdMenu.getScreenId()));
			if (programOpt.isPresent()) {
				url = programOpt.get().getPPath();
			}
		}
		return url;
	}
	
	/**
	 * Get program string.
	 * @return program string
	 */
	public String getProgram() {
		String companyId = AppContexts.user().companyId();
		String pgId = RequestContextProvider.get().get(AppContextsConfig.KEY_PROGRAM_ID);
		if (pgId == null) return "";
		String programId = pgId, screenId = null;
		if (pgId.length() > 6) {
			 programId = pgId.substring(0, 6);
			 screenId = pgId.substring(6);
		}
		Optional<StandardMenu> menuOpt = standardMenuRepository.getProgram(companyId, programId, screenId);
		if (menuOpt.isPresent()) {
			StandardMenu menu = menuOpt.get(); 
			return pgId + " " + menu.getDisplayName().v();
		}
		return "";
	}
	
	
	/**
	 * 
	 * @return
	 */
	public EditMenuBarDto getEditMenuBarDto() {
		List<EnumConstant> listSelectedAtr = EnumAdaptor.convertToValueNameList(SelectedAtr.class, internationalization);
		List<EnumConstant> listSystem = EnumAdaptor.convertToValueNameList(nts.uk.ctx.sys.portal.dom.enums.System.class, internationalization);
		List<EnumConstant> listMenuClassification = EnumAdaptor.convertToValueNameList(MenuClassification.class);
		String companyID = AppContexts.user().companyId();
		int webMenuSetting = 1;
		int menuAtr = 0;
		//List<StandardMenuDto> listStandardMenu = standardMenuRepository.findByAtr(companyID, WebMenuSetting.Display.value, MenuAtr.Menu.value)
		List<StandardMenuDto> listStandardMenu = standardMenuRepository.findByAtr(companyID, webMenuSetting, menuAtr)
				.stream().map(item -> StandardMenuDto.fromDomain(item))
				.collect(Collectors.toList());
		return new EditMenuBarDto(listSelectedAtr, listSystem, listMenuClassification, listStandardMenu);
	}

	/**
	 * convert to  MenuBar
	 * 
	 * @param domain
	 * @return
	 */
	private static List<MenuBarDto> toMenuBar(WebMenu domain) {
		List<MenuBarDto> menuBars = domain.getMenuBars().stream().map(mn -> {
			List<TitleBarDto> titleMenus = toTitleMenu(domain, mn);

			return new MenuBarDto(mn.getMenuBarId().toString(), mn.getCode().v(), mn.getMenuBarName().v(),
					mn.getSelectedAtr().value, mn.getSystem().value, mn.getMenuCls().value,
					mn.getBackgroundColor().v(), mn.getTextColor().v(), mn.getDisplayOrder(), titleMenus);
		}).collect(Collectors.toList());
		return menuBars;
	}

	/**
	 * convert to  TitleMenu
	 * 
	 * @param domain
	 * @param mn
	 * @return
	 */
	private static List<TitleBarDto> toTitleMenu(WebMenu domain, MenuBar mn) {
		List<TitleBarDto> titleMenus = mn.getTitleMenu().stream().map(tm -> {
			List<TreeMenuDto> treeMenus = toTreeMenu(domain, tm);

			return new TitleBarDto(tm.getTitleMenuId().toString(), tm.getTitleMenuName().v(), tm.getBackgroundColor().v(),
					tm.getImageFile(), tm.getTextColor().v(), tm.getTitleMenuAtr().value, tm.getTitleMenuCode().v(),
					tm.getDisplayOrder(), treeMenus);
		}).collect(Collectors.toList());
		
		return titleMenus;
	}

	/**
	 * convert to  TreeMenu
	 * 
	 * @param domain
	 * @param tm
	 * @return
	 */
	private static List<TreeMenuDto> toTreeMenu(WebMenu domain, TitleBar tm) {
		List<TreeMenuDto> treeMenus = tm.getTreeMenu().stream().map(trm -> {
			return new TreeMenuDto(trm.getCode().v(), trm.getDisplayOrder(), trm.getClassification().value,
					trm.getSystem().value);
		}).collect(Collectors.toList());
		return treeMenus;
	}
	
	/**
	 * Find all Person
	 * @param employeeId
	 * @return
	 */
	public List<PersonTypeDto> findAllPerson(String employeeId) {
		String companyId = AppContexts.user().companyId();
		return personalTyingRepository.findAll(companyId, employeeId).stream().map(e -> {
			return convertToDbType(e);
		}).collect(Collectors.toList()); 
	}

	/**
	 * Convert to Database
	 * @param personalTying
	 * @return
	 */
	private PersonTypeDto convertToDbType(PersonalTying personalTying) {
		PersonTypeDto personTypeDto = new PersonTypeDto();
		personTypeDto.setWebMenuCode(personalTying.getWebMenuCode());
		personTypeDto.setEmployeeId(personalTying.getEmployeeId());
		
		return personTypeDto;
	}

}
