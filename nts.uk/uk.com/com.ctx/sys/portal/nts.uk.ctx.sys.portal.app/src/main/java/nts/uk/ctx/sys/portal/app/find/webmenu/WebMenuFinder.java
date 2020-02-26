package nts.uk.ctx.sys.portal.app.find.webmenu;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.arc.enums.EnumAdaptor;
import nts.arc.enums.EnumConstant;
import nts.arc.i18n.I18NResources;
import nts.uk.ctx.sys.portal.app.find.roleset.RoleSetPortalFinder;
import nts.uk.ctx.sys.portal.app.find.standardmenu.StandardMenuDto;
import nts.uk.ctx.sys.portal.app.find.webmenu.detail.MenuBarDetailDto;
import nts.uk.ctx.sys.portal.app.find.webmenu.detail.TitleBarDetailDto;
import nts.uk.ctx.sys.portal.app.find.webmenu.detail.TreeMenuDetailDto;
import nts.uk.ctx.sys.portal.app.find.webmenu.detail.WebMenuDetailDto;
import nts.uk.ctx.sys.portal.dom.adapter.role.RoleGrantAdapter;
import nts.uk.ctx.sys.portal.dom.enums.MenuClassification;
import nts.uk.ctx.sys.portal.dom.enums.System;
import nts.uk.ctx.sys.portal.dom.standardmenu.StandardMenu;
import nts.uk.ctx.sys.portal.dom.standardmenu.StandardMenuKey;
import nts.uk.ctx.sys.portal.dom.standardmenu.StandardMenuRepository;
import nts.uk.ctx.sys.portal.dom.webmenu.MenuBar;
import nts.uk.ctx.sys.portal.dom.webmenu.SelectedAtr;
import nts.uk.ctx.sys.portal.dom.webmenu.TitleBar;
import nts.uk.ctx.sys.portal.dom.webmenu.WebMenu;
import nts.uk.ctx.sys.portal.dom.webmenu.WebMenuRepository;
import nts.uk.ctx.sys.portal.dom.webmenu.personaltying.PersonalTying;
import nts.uk.ctx.sys.portal.dom.webmenu.personaltying.PersonalTyingRepository;
import nts.uk.ctx.sys.portal.dom.webmenu.webmenulinking.RoleByRoleTiesRepository;
import nts.uk.ctx.sys.portal.dom.webmenu.webmenulinking.RoleSetLinkWebMenu;
import nts.uk.ctx.sys.portal.dom.webmenu.webmenulinking.RoleSetLinkWebMenuRepository;
import nts.uk.shr.com.context.AppContexts;
import nts.uk.shr.com.context.LoginUserContext;
import nts.uk.shr.com.menu.adapter.ProgramNameDto;
import nts.uk.shr.com.menu.adapter.ShareMenuAdapter;
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
	
	@Inject
	private RoleSetPortalFinder roleSetFinder;
	
	@Inject
	private ShareMenuAdapter menuAdapter;

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
	public List<WebMenuDetailDto> find(List<MenuCodeDto> codes) {
		List<WebMenuDetailDto> results = new ArrayList<>();
		Map<String, List<MenuCodeDto>> companyMap = codes.stream().collect(Collectors.groupingBy(c -> c.getCompanyId()));
		companyMap.forEach((companyId, codeDtos) -> {
			List<String> menuCodes = codeDtos.stream().map(MenuCodeDto::getMenuCode).collect(Collectors.toList());
			List<WebMenu> webMenus = webMenuRepository.find(companyId, menuCodes);
			List<WebMenuDetailDto> menuDtos = webMenus.stream().map(m -> toMenuDetails(m, companyId))
															.collect(Collectors.toList());
			results.addAll(menuDtos);
		});
		return results;
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
	 * RequestList No.95「すべてのWebメニューを取得する」
	 * Get all simple web menu info
	 * @return
	 */
	public List<WebMenuSimpleDto> findAllWithNoMenuBar() {
		String companyId = AppContexts.user().companyId();
		return webMenuRepository.findAllSimpleValue(companyId).stream().map(webMenuItem -> {
			return new WebMenuSimpleDto(webMenuItem.getCode(), webMenuItem.getName());
		}).collect(Collectors.toList());
	}
	
	/**
	 * Find all menu details.
	 * @return menu details
	 */
	public List<WebMenuDetailDto> findAllDetails() {
		LoginUserContext userCtx = AppContexts.user();
		String companyId = userCtx.companyId();
		List<MenuCodeDto> menus = getMenuSet(companyId, userCtx.userId());
		return menus != null && !menus.isEmpty() ? find(menus) : Arrays.asList(findDefault());
	}
	
	/**
	 * Get menu set.
	 * @param companyId companyId
	 * @param userId userId
	 * @return list of menu codes
	 */
	private List<MenuCodeDto> getMenuSet(String companyId, String userId) {
		if (companyId == null || userId == null) return null;
		// Get role ties
		List<String> roleIds = roleAdapter.getRoleId(userId);
		List<MenuCodeDto> roleMenuCodes = new ArrayList<>();
		roleIds.stream().forEach(r -> {
			roleTiesRepository.getByRoleIdAndCompanyId(r, companyId)
							.ifPresent(t -> roleMenuCodes.add(new MenuCodeDto(t.getCompanyId(), t.getWebMenuCd().v())));
		});
		
		List<MenuCodeDto> menuCodes = new ArrayList<>();
		// Get role set
		Optional<String> roleSetCdOpt = roleSetFinder.getRoleSetCode(companyId, userId);
		if (roleSetCdOpt.isPresent()) {
			List<RoleSetLinkWebMenu> menus = roleSetLinkMenuRepo.findByRoleSetCd(companyId, roleSetCdOpt.get());
			menuCodes = menus.stream().map(m -> new MenuCodeDto(m.getCompanyId(), m.getWebMenuCd().v())).collect(Collectors.toList());
		}
		
		for (MenuCodeDto menuCode : roleMenuCodes) {
			if (menuCodes.stream().noneMatch(m -> menuCode.equals(m))) {
				menuCodes.add(menuCode);
			}
		}
		return menuCodes;
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
		List<StandardMenuKey> keys = new ArrayList<>();
		menu.getMenuBars().stream().forEach(m -> {
			keys.add(new StandardMenuKey(companyId, m.getCode().v(), m.getSystem().value, m.getMenuCls().value));
			m.getTitleMenu().stream().forEach(t -> {
				t.getTreeMenu().stream().forEach(tm -> {
					keys.add(new StandardMenuKey(companyId, tm.getCode().v(), tm.getSystem().value, tm.getClassification().value));
				});
			});
		});
		
		List<StandardMenu> standardMenus = standardMenuRepository.find(keys);
		List<MenuBarDetailDto> menuBar = menu.getMenuBars().stream().map(m -> {
			String link = null;
			if (m.getSelectedAtr() == SelectedAtr.DirectActivation) {
				StandardMenu sMenu = standardMenus.stream()
						.filter(sm -> sm.getCompanyId().equals(companyId) 
								&& sm.getCode().v().equals(m.getCode().v())
								&& sm.getSystem() == m.getSystem()
								&& sm.getClassification() == m.getMenuCls()).findFirst()
						.orElseThrow(() -> new RuntimeException("Menu not found."));
				link = getProgramPath(sMenu);
				
				if (sMenu.getQueryString() != null) {
					link += "?" + sMenu.getQueryString();
				}
			}
			
			List<TitleBarDetailDto> titleBars = m.getTitleMenu().stream().map(t -> {
				List<TreeMenuDetailDto> treeMenus = t.getTreeMenu().stream().map(tm -> {
					String menuCode = tm.getCode().v();
					int system = tm.getSystem().value;
					int classification = tm.getClassification().value;
					StandardMenu stdMenu = standardMenus.stream()
							.filter(sm -> sm.getCompanyId().equals(companyId)
									&& sm.getCode().v().equals(menuCode)
									&& sm.getSystem() == tm.getSystem()
									&& sm.getClassification() == tm.getClassification()).findFirst()
							.orElseThrow(() -> new RuntimeException("Menu not found."));
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
//	public List<ProgramNameDto> getProgram() {
//		
//		return menuAdapter.getProgramName();
//	}
	
	
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
