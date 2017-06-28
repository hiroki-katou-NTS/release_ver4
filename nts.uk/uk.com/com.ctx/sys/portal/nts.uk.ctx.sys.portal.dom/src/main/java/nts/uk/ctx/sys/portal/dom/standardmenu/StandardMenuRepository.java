package nts.uk.ctx.sys.portal.dom.standardmenu;

import java.util.List;
import java.util.Optional;

/**
 * The Interface StandardMenuRepository.
 */
public interface StandardMenuRepository {
	/**
	 * Find all.
	 *
	 * @param companyId
	 *            the company id
	 * @return the list
	 */
	List<StandardMenu> findAll(String companyId);

	/**
	 * added by sonnh1
	 * 
	 * find by COMPANYID and SYSTEM and MENU_CLASSIFICATION
	 * 
	 * @param companyId
	 * @param system
	 * @param menu_classification
	 * @return
	 */
	List<StandardMenu> findBySystemMenuClassification(String companyId, int system, int menu_classification);

	/**
	 * find by COMPANYID and AFTER_LOGIN_DISPLAY
	 * 
	 * @param companyId
	 * @param afterLoginDisplay
	 * @return
	 */
	List<StandardMenu> findByAfterLoginDisplay(String companyId, int afterLoginDisplay);

	/**
	 * added by sonnh1
	 * 
	 * find by COMPANYID and AFTER_LOGIN_DISPLAY and SYSTEM and
	 * MENU_CLASSIFICATION
	 * 
	 * @param companyId
	 * @param afterLoginDisplay
	 * @param system
	 * @param menu_classification
	 * @return
	 */
//	List<StandardMenu> findByAfterLgDisSysMenuCls(String companyId, int afterLoginDisplay, int system,
//			int menu_classification);
	
	/**
	 * Find all.
	 *
	 * @param companyId the company id
	 * @param webMenuSetting
	 * @param menuAtr
	 * @return the list 
	 */ 
	List<StandardMenu> findByAtr(String companyId, int webMenuSetting, int menuAtr);
	/**
	 * hoatt
	 * get standard menu
	 * @param companyId
	 * @param code
	 * @param system
	 * @param classification
	 * @return
	 */
	Optional<StandardMenu> getStandardMenubyCode(String companyId,String code, int system,int classification);
}
