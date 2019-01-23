/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.sys.gateway.dom.singlesignon;

import java.util.List;
import java.util.Optional;

/**
 * The Interface WindowAccountRepository.
 */
public interface WindowsAccountRepository {
	

	/**
	 * Find list window account by user id.
	 *
	 * @param companyId the company id
	 * @param employeeId the employee id
	 * @return the list
	 */
	Optional<WindowsAccount> findListWindowAccountByEmployeeId(String companyId, String employeeId);

	/**
	 * Removes the.
	 *
	 * @param cid the cid
	 * @param sid the sid
	 * @param no the no
	 */
	void remove(String cid, String sid, Integer no);

	/**
	 * Adds the.
	 *
	 * @param windowAccount the window account
	 */
	void add(String cid, String employeeId, WindowsAccountInfo windowAccountInfo);
	
	/**
	 * Findby user name and host name.
	 *
	 * @param userName the user name
	 * @param hostName the host name
	 * @return the optional
	 */
	Optional<WindowsAccount> findbyUserNameAndHostName(String userName, String hostName);

	/**
	 * Findby user name and host name and is used.
	 *
	 * @param userName the user name
	 * @param hostName the host name
	 * @return the optional
	 */
	Optional<WindowsAccount> findbyUserNameAndHostNameAndIsUsed(String userName, String hostName);
	
		
	/**
	 * Find by user id.
	 *
	 * @param userId the user id
	 * @return the list
	 */
	Optional<WindowsAccount> findByEmployeeId(String cid,String empployeeId);

	/**
	 * Update.
	 *
	 * @param winAccCommand the win acc command
	 * @param winAccDB the win acc DB
	 */
	void update(String companyId, String employeeId, WindowsAccountInfo winAccCommand, WindowsAccountInfo winAccDB);
	
	
	/**
	 * Find by list user id.
	 *
	 * @param ltsUserId the lts user id
	 * @return the list
	 */
	List<WindowsAccount> findByListEmployeeId(List<String> lstEmployeeId);
	
}
