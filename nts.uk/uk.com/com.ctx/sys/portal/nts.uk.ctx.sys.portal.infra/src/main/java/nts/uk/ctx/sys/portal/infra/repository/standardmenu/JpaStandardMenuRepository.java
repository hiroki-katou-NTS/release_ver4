package nts.uk.ctx.sys.portal.infra.repository.standardmenu;

import java.util.List;
import java.util.Optional;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;

import lombok.val;
import nts.arc.layer.infra.data.JpaRepository;
import nts.uk.ctx.sys.portal.dom.standardmenu.StandardMenu;
import nts.uk.ctx.sys.portal.dom.standardmenu.StandardMenuRepository;
import nts.uk.ctx.sys.portal.infra.entity.standardmenu.CcgstStandardMenu;
import nts.uk.ctx.sys.portal.infra.entity.standardmenu.CcgstStandardMenuPK;

/**
 * The Class JpaStandardMenuRepository.
 */
@Stateless
public class JpaStandardMenuRepository extends JpaRepository implements StandardMenuRepository {
	private final String SEL = "SELECT s FROM CcgstStandardMenu s ";
	private final String GET_ALL_STANDARD_MENU = "SELECT s FROM CcgstStandardMenu s WHERE s.ccgmtStandardMenuPK.companyId = :companyId";
	private final String GET_ALL_STANDARD_MENU_BY_SYSTEM = "SELECT s FROM CcgstStandardMenu s WHERE s.ccgmtStandardMenuPK.companyId = :companyId "
			+ "AND s.ccgmtStandardMenuPK.system = :system AND s.menuAtr = 1";	
	private final String GET_ALL_STANDARD_MENU_DISPLAY = "SELECT s FROM CcgstStandardMenu s WHERE s.ccgmtStandardMenuPK.companyId = :companyId "
			+ "AND s.webMenuSetting = 1 ORDER BY s.ccgmtStandardMenuPK.classification ASC,s.ccgmtStandardMenuPK.code ASC";	
	private final String FIND_BY_AFTER_LOGIN_DISPLAY = SEL + "WHERE s.ccgmtStandardMenuPK.companyId = :companyId "
			+ "AND s.afterLoginDisplay = :afterLoginDisplay ";
	private final String FIND_BY_SYSTEM_MENUCLASSIFICATION = SEL + "WHERE s.ccgmtStandardMenuPK.companyId = :companyId "
			+ "AND s.ccgmtStandardMenuPK.system = :system "
			+ "AND s.ccgmtStandardMenuPK.classification = :menu_classification ORDER BY s.ccgmtStandardMenuPK.code ASC";
	private final String FIND_BY_SYSTEM_MENUCLASSIFICATION_AND_AFTER_LOGIN_DIS = SEL
			+ "WHERE s.ccgmtStandardMenuPK.companyId = :companyId " + "AND s.ccgmtStandardMenuPK.system = :system "
			+ "AND s.ccgmtStandardMenuPK.classification = :menu_classification OR s.afterLoginDisplay = :afterLoginDisplay "
			+ "ORDER BY s.ccgmtStandardMenuPK.classification ASC,s.ccgmtStandardMenuPK.code ASC";

	private final String GET_ALL_STANDARD_MENU_BY_ATR = "SELECT s FROM CcgstStandardMenu s WHERE s.ccgmtStandardMenuPK.companyId = :companyId "
			+ "AND s.webMenuSetting = :webMenuSetting " + "AND s.menuAtr = :menuAtr";
	// hoatt
	private final String SELECT_STANDARD_MENU_BY_CODE = "SELECT c FROM CcgstStandardMenu c WHERE c.ccgmtStandardMenuPK.companyId = :companyId "
			+ " AND c.ccgmtStandardMenuPK.code = :code" + " AND c.ccgmtStandardMenuPK.system = :system"
			+ " AND c.ccgmtStandardMenuPK.classification = :classification";
	private final String GET_PG = "SELECT a FROM CcgstStandardMenu a WHERE a.ccgmtStandardMenuPK.companyId = :companyId"
			+ " AND a.programId = :programId AND a.screenID = :screenId";

	private CcgstStandardMenu toEntity(StandardMenu domain) {
		val entity = new CcgstStandardMenu();

		entity.ccgmtStandardMenuPK = new CcgstStandardMenuPK();
		entity.ccgmtStandardMenuPK.companyId = domain.getCompanyId();
		entity.ccgmtStandardMenuPK.code = domain.getCode().v();
		entity.ccgmtStandardMenuPK.system = domain.getSystem().value;
		entity.ccgmtStandardMenuPK.classification = domain.getClassification().value;
		entity.afterLoginDisplay = domain.getAfterLoginDisplay();
		entity.displayName = domain.getDisplayName().v();
		entity.displayOrder = domain.getDisplayOrder();
		entity.logSettingDisplay = domain.getLogSettingDisplay();
		entity.menuAtr = domain.getMenuAtr().value;
		entity.targetItems = domain.getTargetItems();
		entity.url = domain.getUrl();
		entity.webMenuSetting = domain.getWebMenuSetting().value;
		return entity;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nts.uk.ctx.sys.portal.dom.standardmenu.StandardMenuRepository#findAll(
	 * java.lang. String)
	 */
	@Override
	public List<StandardMenu> findAll(String companyId) {
		return this.queryProxy().query(GET_ALL_STANDARD_MENU, CcgstStandardMenu.class)
				.setParameter("companyId", companyId).getList(t -> toDomain(t));
	}

	@Override
	public List<StandardMenu> findByAfterLoginDisplay(String companyId, int afterLoginDisplay) {
		return this.queryProxy().query(FIND_BY_AFTER_LOGIN_DISPLAY, CcgstStandardMenu.class)
				.setParameter("companyId", companyId).setParameter("afterLoginDisplay", afterLoginDisplay)
				.getList(t -> toDomain(t));
	}

	/**
	 * added by sonnh1
	 * 
	 * find by COMPANYID and SYSTEM and MENU_CLASSIFICATION
	 */
	@Override
	public List<StandardMenu> findBySystemMenuClassification(String companyId, int system, int menu_classification) {
		return this.queryProxy().query(FIND_BY_SYSTEM_MENUCLASSIFICATION, CcgstStandardMenu.class)
				.setParameter("companyId", companyId).setParameter("system", system)
				.setParameter("menu_classification", menu_classification).getList(t -> toDomain(t));
	}
	
	/**
	 * added by sonnh1
	 * 
	 * find by COMPANYID and SYSTEM and MENU_CLASSIFICATION and AFTER_LOGIN_DISPLAY
	 */
	@Override
	public List<StandardMenu> findDataForAfterLoginDis(String companyId, int afterLoginDisplay, int system,
			int menu_classification) {
		return this.queryProxy().query(FIND_BY_SYSTEM_MENUCLASSIFICATION_AND_AFTER_LOGIN_DIS, CcgstStandardMenu.class)
				.setParameter("companyId", companyId).setParameter("system", system)
				.setParameter("menu_classification", menu_classification)
				.setParameter("afterLoginDisplay", afterLoginDisplay).getList(t -> toDomain(t));
	}

	@Override
	public List<StandardMenu> findByAtr(String companyId, int webMenuSetting, int menuAtr) {
		return this.queryProxy().query(GET_ALL_STANDARD_MENU_BY_ATR, CcgstStandardMenu.class)
				.setParameter("companyId", companyId).setParameter("webMenuSetting", webMenuSetting)
				.setParameter("menuAtr", menuAtr).getList(t -> toDomain(t));
	}

	/**
	 * To domain.
	 *
	 * @param s
	 *            the s
	 * @return the top page
	 */
	private StandardMenu toDomain(CcgstStandardMenu s) {
		return StandardMenu.createFromJavaType(s.ccgmtStandardMenuPK.companyId, s.ccgmtStandardMenuPK.code,
				s.targetItems, s.displayName, s.displayOrder, s.menuAtr, s.url, s.ccgmtStandardMenuPK.system,
				s.ccgmtStandardMenuPK.classification, s.webMenuSetting, s.afterLoginDisplay, s.logSettingDisplay,
				s.programId, s.screenID, s.queryString);
	}

	/**
	 * hoatt get standard menu
	 * 
	 * @param companyId
	 * @param code
	 * @param system
	 * @param classification
	 * @return
	 */
	@Override
	public Optional<StandardMenu> getStandardMenubyCode(String companyId, String code, int system, int classification) {
		return this.queryProxy().query(SELECT_STANDARD_MENU_BY_CODE, CcgstStandardMenu.class)
				.setParameter("companyId", companyId).setParameter("code", code).setParameter("system", system)
				.setParameter("classification", classification).getSingle(c -> toDomain(c));
	}

	/**
	 * yennth update list standard menu
	 * 
	 * @param list
	 *            standard menu
	 */
	@Override
	public void changeName(List<StandardMenu> StandardMenu) {
		EntityManager manager = this.getEntityManager();
		CcgstStandardMenuPK pk;
		for (StandardMenu obj : StandardMenu) {
			pk = new CcgstStandardMenuPK(obj.getCompanyId(), obj.getCode().v(), obj.getSystem().value,
					obj.getClassification().value);
			CcgstStandardMenu o = manager.find(CcgstStandardMenu.class, pk);
			o.setDisplayName(obj.getDisplayName().v());
		}
	}

	@Override
	public List<StandardMenu> findBySystem(String companyId, int system) {
		return this.queryProxy().query(GET_ALL_STANDARD_MENU_BY_SYSTEM, CcgstStandardMenu.class)
				.setParameter("companyId", companyId).setParameter("system", system).getList(t -> toDomain(t));
	}

	/**
	 * yennth
	 * @param list standardMenu
	 */
	@Override
	public boolean isExistDisplayName(List<StandardMenu> StandardMenu) {
		boolean isExist = false;
		for (StandardMenu obj : StandardMenu) {
			if (obj.getDisplayName() != null || !obj.getDisplayName().v().equals(""))
				isExist = true;
			break;
		}
		return isExist;
	}

	/**
	 * Get all display standard menu
	 */
	@Override
	public List<StandardMenu> findAllDisplay(String companyId) {
		return this.queryProxy().query(GET_ALL_STANDARD_MENU_DISPLAY, CcgstStandardMenu.class)
				.setParameter("companyId", companyId).getList(t -> toDomain(t));
	}
	
	/* (non-Javadoc)
	 * @see nts.uk.ctx.sys.portal.dom.standardmenu.StandardMenuRepository
	 * #getProgram(java.lang.String, java.lang.String)
	 */
	@Override
	public Optional<StandardMenu> getProgram(String companyId, String programId, String screenId) {
		return this.queryProxy().query(GET_PG, CcgstStandardMenu.class)
			.setParameter("companyId", companyId).setParameter("programId", programId)
			.setParameter("screenId", screenId).getSingle(m -> toDomain(m));
	}
}
