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
	 * added by sonnh1
	 * find by COMPANYID and SYSTEM and MENU_CLASSIFICATION and AFTER_LOGIN_DISPLAY
	 * @param companyId
	 * @param afterLoginDisplay
	 * @param system
	 * @param menu_classification
	 * @return
	 */
	List<StandardMenu> findDataForAfterLoginDis(String companyId, int afterLoginDisplay, int system,
			int menu_classification);

	/**
	 * find by COMPANYID and AFTER_LOGIN_DISPLAY
	 * 
	 * @param companyId
	 * @param afterLoginDisplay
	 * @return
	 */
	List<StandardMenu> findByAfterLoginDisplay(String companyId, int afterLoginDisplay);

	/**
	 * Find all.
	 *
	 * @param companyId
	 *            the company id
	 * @param webMenuSetting
	 * @param menuAtr
	 * @return the list
	 */
	List<StandardMenu> findByAtr(String companyId, int webMenuSetting, int menuAtr);

	/**
	 * hoatt get standard menu
	 * 
	 * @param companyId
	 * @param code
	 * @param system
	 * @param classification
	 * @return
	 */
	Optional<StandardMenu> getStandardMenubyCode(String companyId, String code, int system, int classification);

	/**
	 * yennth
	 * 
	 * @param List
	 *            StandardMenu
	 */
	void changeName(List<StandardMenu> StandardMenu);

	/**
	 * Find all by system.
	 *
	 * @param companyId
	 *            the company id
	 * @param system
	 * @return the list
	 */
	List<StandardMenu> findBySystem(String companyId, int system);
	/**
	 * yennth
	 * @param companyId
	 * @param code
	 * @param system
	 * @param classification
	 * @param displayName
	 * @return
	 */
	boolean isExistDisplayName(List<StandardMenu> StandardMenu);

	/**
	 * Find all menu with condition=DISPLAY
	 * @param companyID
	 * @return
	 */
	List<StandardMenu> findAllDisplay(String companyID);
}
