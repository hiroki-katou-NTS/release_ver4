/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.sys.gateway.dom.singlesignon;

import java.util.List;
import java.util.Optional;

/**
 * The Interface OtherSysAccountRepository.
 */
public interface OtherSysAccountRepository {

	/**
	 * Removes the.
	 *
	 * @param userId the user id
	 */
	void remove(String cid,String userId);

	/**
	 * Adds the.
	 *
	 * @param otherSysAccount the other sys account
	 */
	void add(OtherSysAccount otherSysAccount);

	/**
	 * Find by company code and user name.
	 *
	 * @param companyCode the company code
	 * @param userName the user name
	 * @return the optional
	 */
	List<OtherSysAccount> findByCompanyCodeAndUserName(String companyCode, String userName);

	/**
	 * Find by employee id.
	 *
	 * @param cid the cid
	 * @param employeeId the employee id
	 * @return the optional
	 */
	Optional<OtherSysAccount> findByEmployeeId(String cid, String employeeId);

	/**
	 * Update.
	 *
	 * @param otherSysAccCommand the other sys acc command
	 * @param otherSysAccDB the other sys acc DB
	 */
	void update(OtherSysAccount otherSysAccCommand, OtherSysAccount otherSysAccDB);

	/**
	 * Find all other sys account.
	 *
	 * @param listUserId the list user id
	 * @return the list
	 */
	List<OtherSysAccount> findAllOtherSysAccount(List<String> listUserId);

}
