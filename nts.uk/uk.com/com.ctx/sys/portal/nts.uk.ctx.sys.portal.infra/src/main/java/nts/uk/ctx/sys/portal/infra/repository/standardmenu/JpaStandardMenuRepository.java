package nts.uk.ctx.sys.portal.infra.repository.standardmenu;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;

import nts.arc.layer.infra.data.JpaRepository;
import nts.arc.layer.infra.data.jdbc.NtsStatement;
import nts.uk.ctx.sys.portal.dom.standardmenu.StandardMenu;
import nts.uk.ctx.sys.portal.dom.standardmenu.StandardMenuKey;
import nts.uk.ctx.sys.portal.dom.standardmenu.StandardMenuRepository;
import nts.uk.ctx.sys.portal.infra.entity.standardmenu.CcgstStandardMenu;
import nts.uk.ctx.sys.portal.infra.entity.standardmenu.CcgstStandardMenuPK;
import nts.uk.shr.com.context.AppContexts;
import nts.uk.shr.com.menu.ShareStandardMenuAdapter;

/**
 * The Class JpaStandardMenuRepository.
 */
@Stateless
public class JpaStandardMenuRepository extends JpaRepository implements StandardMenuRepository, ShareStandardMenuAdapter {
	private static final String SEL = "SELECT s FROM CcgstStandardMenu s ";
	private static final String GET_ALL_STANDARD_MENU = "SELECT s FROM CcgstStandardMenu s WHERE s.ccgmtStandardMenuPK.companyId = :companyId and s.queryString NOT LIKE CONCAT('%',:toppagecode,'%')";
	private static final String GET_ALL_STANDARD_MENU_BY_SYSTEM = "SELECT s FROM CcgstStandardMenu s WHERE s.ccgmtStandardMenuPK.companyId = :companyId "
			+ "AND s.ccgmtStandardMenuPK.system = :system AND s.menuAtr = 1";
	private static final String GET_ALL_STANDARD_MENU_DISPLAY = "SELECT s FROM CcgstStandardMenu s WHERE s.ccgmtStandardMenuPK.companyId = :companyId "
			+ "AND s.webMenuSetting = 1 ORDER BY s.ccgmtStandardMenuPK.classification ASC,s.ccgmtStandardMenuPK.code ASC";
	private static final String FIND_BY_AFTER_LOGIN_DISPLAY = SEL + "WHERE s.ccgmtStandardMenuPK.companyId = :companyId "
			+ "AND s.afterLoginDisplay = :afterLoginDisplay ";
	private static final String FIND_BY_SYSTEM_MENUCLASSIFICATION = SEL + "WHERE s.ccgmtStandardMenuPK.companyId = :companyId "
			+ "AND s.ccgmtStandardMenuPK.system = :system "
			+ "AND s.ccgmtStandardMenuPK.classification = :menu_classification ORDER BY s.ccgmtStandardMenuPK.code ASC";
	private static final String FIND_BY_MENUCLASSIFICATION_OR_AFTER_LOGIN_DIS = SEL
			+ "WHERE s.ccgmtStandardMenuPK.companyId = :companyId "
			+ "AND (s.ccgmtStandardMenuPK.classification = :menu_classification OR s.afterLoginDisplay = :afterLoginDisplay) "
			+ "ORDER BY s.ccgmtStandardMenuPK.classification ASC,s.ccgmtStandardMenuPK.code ASC";

	private static final String GET_ALL_STANDARD_MENU_BY_ATR = "SELECT s FROM CcgstStandardMenu s WHERE s.ccgmtStandardMenuPK.companyId = :companyId "
			+ "AND s.webMenuSetting = :webMenuSetting " + "AND s.menuAtr = :menuAtr";
	// hoatt
	private static final String SELECT_STANDARD_MENU_BY_CODE = "SELECT c FROM CcgstStandardMenu c WHERE c.ccgmtStandardMenuPK.companyId = :companyId "
			+ " AND c.ccgmtStandardMenuPK.code = :code" + " AND c.ccgmtStandardMenuPK.system = :system"
			+ " AND c.ccgmtStandardMenuPK.classification = :classification";
	
	private static final String GET_PG = "SELECT a FROM CcgstStandardMenu a WHERE a.ccgmtStandardMenuPK.companyId = :companyId"
			+ " AND a.programId = :programId AND a.screenID = :screenId";
	private static final String GET_PG_BYQRY = "SELECT a FROM CcgstStandardMenu a WHERE a.ccgmtStandardMenuPK.companyId = :companyId"
			+ " AND a.programId = :programId AND a.screenID = :screenId"
			+ " AND a.queryString = :queryString";

	public CcgstStandardMenu insertToEntity(StandardMenu domain) {
		 CcgstStandardMenuPK ccgstStandardMenuPK = new CcgstStandardMenuPK(domain.getCompanyId(), domain.getCode().v(), domain.getSystem().value, domain.getClassification().value);
		 int maxDisplayOrder = this.getMaxDisplayOrder() + 1;
	return new CcgstStandardMenu(
			 ccgstStandardMenuPK, 
			 domain.getTargetItems(), 
			 domain.getDisplayName().v(), 
			 maxDisplayOrder, 
			 domain.getMenuAtr().value, 
			 domain.getUrl(), 
			 domain.getWebMenuSetting().value, 
			 domain.getAfterLoginDisplay(), 
			 domain.getLogSettingDisplay(), 
			 domain.getProgramId(), 
			 domain.getScreenId(), 
			 domain.getQueryString()
			 );
	}
	
	public CcgstStandardMenu toEntity(StandardMenu domain) {
		 CcgstStandardMenuPK ccgstStandardMenuPK = new CcgstStandardMenuPK(domain.getCompanyId(), domain.getCode().v(), domain.getSystem().value, domain.getClassification().value);
	return new CcgstStandardMenu(
			 ccgstStandardMenuPK, 
			 domain.getTargetItems(), 
			 domain.getDisplayName().v(), 
			 domain.getDisplayOrder(), 
			 domain.getMenuAtr().value, 
			 domain.getUrl(), 
			 domain.getWebMenuSetting().value, 
			 domain.getAfterLoginDisplay(), 
			 domain.getLogSettingDisplay(), 
			 domain.getProgramId(), 
			 domain.getScreenId(), 
			 domain.getQueryString()
			 );
	 }

	 private static final String GET_MAX = "SELECT MAX(a.displayOrder) FROM CcgstStandardMenu a WHERE a.ccgmtStandardMenuPK.companyId = :companyId";
	 
	 public int getMaxDisplayOrder() {
		 String cid = AppContexts.user().companyId();
		 Object max = this.queryProxy().query(GET_MAX, Object.class)
					.setParameter("companyId", cid).getSingleOrNull();
		 if(max.equals(null)) {
			 return 0;
		 }else {
			 return (int)max;
		 }
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
				.setParameter("companyId", companyId).setParameter("toppagecode", "toppagecode").getList(t -> toDomain(t));
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
	 * find by COMPANYID and MENU_CLASSIFICATION or AFTER_LOGIN_DISPLAY
	 */
	@Override
	public List<StandardMenu> findDataForAfterLoginDis(String companyId, int afterLoginDisplay,
			int menu_classification) {
		return this.queryProxy().query(FIND_BY_MENUCLASSIFICATION_OR_AFTER_LOGIN_DIS, CcgstStandardMenu.class)
				.setParameter("companyId", companyId).setParameter("menu_classification", menu_classification)
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
	

	/* (non-Javadoc)
	 * @see nts.uk.ctx.sys.portal.dom.standardmenu.StandardMenuRepository#find(java.util.List)
	 */
	@Override
	public List<StandardMenu> find(List<StandardMenuKey> keys) {
		if (keys == null || keys.isEmpty()) return Collections.emptyList();
		
		StringBuilder queryStr = new StringBuilder(SEL);
		queryStr.append("WHERE");
		keys.stream().map(k -> " (s.ccgmtStandardMenuPK.companyId = '" + k.getCompanyId() + "' AND "
					+ "s.ccgmtStandardMenuPK.code = '" + k.getCode() + "' AND "
					+ "s.ccgmtStandardMenuPK.system = " + k.getSystem() + " AND "
					+ "s.ccgmtStandardMenuPK.classification = " + k.getClassification() + ") "
		).reduce((a, b) -> a + "OR" + b).ifPresent(queryStr::append);
		
		return this.queryProxy().query(queryStr.toString(), CcgstStandardMenu.class).getList(m -> toDomain(m));
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
	 * 
	 * @param list
	 *            standardMenu
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.sys.portal.dom.standardmenu.StandardMenuRepository
	 * #getProgram(java.lang.String, java.lang.String)
	 */
	@Override
	public List<StandardMenu> getProgram(String companyId, String programId, String screenId) {
		return this.queryProxy().query(GET_PG, CcgstStandardMenu.class).setParameter("companyId", companyId)
				.setParameter("programId", programId).setParameter("screenId", screenId).getList(m -> toDomain(m));
	}

	@Override
	public void insertStandardMenu(StandardMenu standardMenu) {
		this.commandProxy().insert(insertToEntity(standardMenu));
	}

	@Override
	public void updateStandardMenu(StandardMenu standardMenu) {
		Optional<CcgstStandardMenu> entity =
				this.queryProxy().query(SELECT_STANDARD_MENU_BY_CODE, CcgstStandardMenu.class)
				.setParameter("companyId", standardMenu.getCompanyId())
				.setParameter("code", standardMenu.getCode())
				.setParameter("system", standardMenu.getSystem().value)
				.setParameter("classification", standardMenu.getClassification().value)
				.getSingle();
		if(entity.isPresent()) {
			entity.get().setDisplayName(standardMenu.getDisplayName().v());
			entity.get().setTargetItems(standardMenu.getDisplayName().v());
			this.commandProxy().update(entity.get());
		}
	}

	private static final String DELETE_STANDARD_MENU = "DELETE FROM CcgstStandardMenu t "
			+ "WHERE t.ccgmtStandardMenuPK.companyId = :companyId "
			+ "AND t.ccgmtStandardMenuPK.code = :code "
			+ "AND t.ccgmtStandardMenuPK.system = :system "
			+ "AND t.ccgmtStandardMenuPK.classification = :classification ";
	@Override
	public void deleteStandardMenu(String companyId, String code, int system, int classification) {
		this.getEntityManager().createQuery(DELETE_STANDARD_MENU, CcgstStandardMenu.class)
		.setParameter("companyId", companyId)
		.setParameter("code", code)
		.setParameter("system",  system)
		.setParameter("classification",  classification)
		.executeUpdate();
	}

	@Override
	public boolean isEsistMenuWith(String comId, String screenId, String programId, String queryString) {
		String sql = "select COUNT(*)"
		        + " from CCGST_STANDARD_MENU"
		        + " where CID = @cid AND PROGRAM_ID = @programId AND SCREEN_ID = @screenId AND QUERY_STRING = @queryString";
		         int check = new NtsStatement(sql, this.jdbcProxy())
						.paramString("cid", comId)
						.paramString("programId", programId)
						.paramString("screenId", screenId)
						.paramString("queryString", queryString)
						.getSingle(rec -> {
								return rec.getInt(1);	
						}).orElse(0);
		         return check >0;
	}

	@Override
	public Optional<StandardMenu> getPgName(String companyId, String programId, String screenId, String queryString) {
		return this.queryProxy().query(GET_PG_BYQRY, CcgstStandardMenu.class)
				.setParameter("companyId", companyId)
				.setParameter("programId", programId)
				.setParameter("screenId", screenId)
				.setParameter("queryString", queryString)
				.getSingle(c -> toDomain(c));
	}

	private static final String GET_MAX_ORDER = "SELECT MAX(t.displayOrder) FROM CcgstStandardMenu t "
			+ "WHERE t.ccgmtStandardMenuPK.companyId = :companyId "
			+ "AND t.ccgmtStandardMenuPK.system = :system "
			+ "AND t.ccgmtStandardMenuPK.classification = :classification ";
	
	@Override
	public int maxOrderStandardMenu(String companyId, int system, int classification) {
		return this.queryProxy().query(GET_MAX_ORDER, int.class)
		.setParameter("companyId", companyId)
		.setParameter("system",  system)
		.setParameter("classification",  classification)
		.getSingle().orElse(0);
	}
}
