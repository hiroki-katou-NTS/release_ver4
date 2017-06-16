package nts.uk.ctx.sys.portal.infra.repository.webmenu;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.ejb.Stateless;

import nts.arc.layer.infra.data.JpaRepository;
import nts.uk.ctx.sys.portal.dom.webmenu.MenuBar;
import nts.uk.ctx.sys.portal.dom.webmenu.TitleMenu;
import nts.uk.ctx.sys.portal.dom.webmenu.TreeMenu;
import nts.uk.ctx.sys.portal.dom.webmenu.WebMenu;
import nts.uk.ctx.sys.portal.dom.webmenu.WebMenuRepository;
import nts.uk.ctx.sys.portal.infra.entity.webmenu.CcgstMenuBar;
import nts.uk.ctx.sys.portal.infra.entity.webmenu.CcgstMenuBarPK;
import nts.uk.ctx.sys.portal.infra.entity.webmenu.CcgstTitleMenu;
import nts.uk.ctx.sys.portal.infra.entity.webmenu.CcgstTitleMenuPK;
import nts.uk.ctx.sys.portal.infra.entity.webmenu.CcgstTreeMenu;
import nts.uk.ctx.sys.portal.infra.entity.webmenu.CcgstTreeMenuPK;
import nts.uk.ctx.sys.portal.infra.entity.webmenu.CcgstWebMenu;
import nts.uk.ctx.sys.portal.infra.entity.webmenu.CcgstWebMenuPK;

/**
 * 
 * @author sonnh
 *
 */
@Stateless
public class JpaWebMenuRepository extends JpaRepository implements WebMenuRepository {

	private final String SEL_1 = "SELECT a FROM CcgstWebMenu a WHERE a.ccgstWebMenuPK.companyId = :companyId";

	@Override
	public List<WebMenu> findAll(String companyId) {
		this.queryProxy().query(SEL_1, CcgstWebMenu.class).setParameter("companyId", companyId).getList(w -> {
			return toDomain(companyId, w);
		});

		return null;
	}
	
	@Override
	public Optional<WebMenu> find(String companyId, String webMenuCode) {
		CcgstWebMenuPK key = new CcgstWebMenuPK(companyId, webMenuCode);
		return this.queryProxy().find(key, CcgstWebMenu.class)
				.map(wm -> toDomain(companyId, wm));
	}

	@Override
	public void add(WebMenu webMenu) {
		this.commandProxy().insert(toEntity(webMenu));
	}

	@Override
	public void update(WebMenu webMenu) {
		this.commandProxy().update(toEntity(webMenu));
	}

	@Override
	public void remove(String companyId, String webMenuCode) {
		CcgstWebMenuPK key = new CcgstWebMenuPK(companyId, webMenuCode);
		this.commandProxy().remove(CcgstWebMenu.class, key);
	}

	/**
	 * convert to domain WebMenu
	 * 
	 * @param companyId
	 * @param w
	 * @return
	 */
	private WebMenu toDomain(String companyId, CcgstWebMenu w) {
		List<MenuBar> menuBars = (List<MenuBar>) w.menuBars.stream().map(mb -> {

			return toDomainMenuBar(mb);
		}).collect(Collectors.toList());

		return WebMenu.createFromJavaType(companyId, w.ccgstWebMenuPK.webMenuCd, w.webMenuName, w.defaultMenu,
				menuBars);
	}

	/**
	 * convert to domain MenuBar
	 * 
	 * @param mb
	 * @return
	 */
	private MenuBar toDomainMenuBar(CcgstMenuBar mb) {
		List<TitleMenu> titleMenus = mb.titleMenus.stream().map(tm -> {
			return toDomainTitleMenu(tm);
		}).collect(Collectors.toList());

		return MenuBar.createFromJavaType(mb.ccgstMenuBarPK.menuBarId, mb.menuBarName, mb.selectedAtr, mb.system, mb.menuCls, mb.code,
				mb.backgroundColor, mb.textColor, mb.displayOrder, titleMenus);
	}

	/**
	 * convert to domain TitleMenu
	 * 
	 * @param tm
	 * @return
	 */
	private TitleMenu toDomainTitleMenu(CcgstTitleMenu tm) {
		List<TreeMenu> treeMenus = tm.treeMenus.stream().map(trm -> {
			return TreeMenu.createFromJavaType(trm.ccgstTreeMenuPK.titleMenuId, trm.code,
					trm.ccgstTreeMenuPK.displayOrder, trm.classification, trm.system);
		}).collect(Collectors.toList());

		return TitleMenu.createFromJavaType(tm.ccgstTitleMenuPK.menuBarId, tm.ccgstTitleMenuPK.titleMenuId,
				tm.titleMenuName, tm.backgroundColor, tm.imageFile, tm.textColor, tm.titleMenuAtr, tm.titleMenuCD,
				tm.displayOrder, treeMenus);
	}

	/**
	 * convert to entity CcgstWebMenu
	 * @param domain
	 * @return
	 */
	private static CcgstWebMenu toEntity(WebMenu domain) {
		CcgstWebMenuPK key = new CcgstWebMenuPK(domain.getCompanyId(), domain.getWebMenuCode().v());
		
		List<CcgstMenuBar> menuBars = toEntityMenuBar(domain);
		
		return new CcgstWebMenu(key, domain.getWebMenuName().v(), domain.getDefaultMenu().value, menuBars);
	}

	/**
	 * convert to entity CcgstMenuBar
	 * @param domain
	 * @return
	 */
	private static List<CcgstMenuBar> toEntityMenuBar(WebMenu domain) {
		List<CcgstMenuBar> menuBars = domain.getMenuBars().stream()
				.map(mn -> {
					List<CcgstTitleMenu> titleMenus = toEntityTitleMenu(domain, mn);
					
				    CcgstMenuBarPK ccgstMenuBarPK = new CcgstMenuBarPK(domain.getCompanyId(), domain.getWebMenuCode().v(), mn.getMenuBarId().toString());
					return new CcgstMenuBar(ccgstMenuBarPK,  mn.getMenuBarName().v(), mn.getSelectedAtr().value, mn.getSystem().value, mn.getMenuCls().value, mn.getCode().v(), mn.getBackgroundColor().v(), mn.getTextColor().v(), mn.getDisplayOrder(), titleMenus);
				}).collect(Collectors.toList());
		return menuBars;
	}

	/**
	 * convert to entity CcgstTitleMenu
	 * @param domain
	 * @param mn
	 * @return
	 */
	private static List<CcgstTitleMenu> toEntityTitleMenu(WebMenu domain, MenuBar mn) {
		List<CcgstTitleMenu> titleMenus = mn.getTitleMenu().stream()
				.map(tm -> {
					List<CcgstTreeMenu> treeMenus = toEntityTreeMenu(domain, tm);
					
					CcgstTitleMenuPK ccgstTitleMenuPK = new CcgstTitleMenuPK(domain.getCompanyId(), domain.getWebMenuCode().v(), mn.getMenuBarId().toString(), tm.getTitleMenuId().toString());
					return new CcgstTitleMenu(ccgstTitleMenuPK, tm.getTitleMenuName().v(), tm.getBackgroundColor().v(), tm.getImageFile(), tm.getTextColor().v(), tm.getTitleMenuAtr().value, tm.getTitleMenuCode().v(), tm.getDisplayOrder(), treeMenus);
				}).collect(Collectors.toList());;
		return titleMenus;
	}

	/**
	 * convert to entity CcgstTreeMenu
	 * @param domain
	 * @param tm
	 * @return
	 */
	private static List<CcgstTreeMenu> toEntityTreeMenu(WebMenu domain, TitleMenu tm) {
		List<CcgstTreeMenu> treeMenus = tm.getTreeMenu().stream()
				.map(trm -> {
					CcgstTreeMenuPK ccgstTreeMenuPK = new CcgstTreeMenuPK(domain.getCompanyId(), domain.getWebMenuCode().v(), tm.getTitleMenuId().toString(), trm.getTitleMenuId().toString(), trm.getDisplayOrder());
					return new CcgstTreeMenu(ccgstTreeMenuPK, trm.getCode().v(), trm.getClassification().value, trm.getSystem().value);
				}).collect(Collectors.toList());
		return treeMenus;
	}

}
