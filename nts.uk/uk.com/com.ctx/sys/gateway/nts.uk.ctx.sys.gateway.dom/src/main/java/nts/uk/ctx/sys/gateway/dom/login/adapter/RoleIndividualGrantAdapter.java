/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.sys.gateway.dom.login.adapter;

import nts.arc.time.GeneralDate;
import nts.uk.ctx.sys.gateway.dom.login.dto.RoleIndividualGrantImport;

/**
 * The Interface RoleIndividualGrantAdapter.
 */
public interface RoleIndividualGrantAdapter {
	
	/**
	 * Gets the by user and role.
	 *
	 * @param userId the user id
	 * @param roleType the role type
	 * @return the by user and role
	 */
	RoleIndividualGrantImport getByUserAndRole (String userId,RoleType roleType);
	
	/**
	 * Gets the by user.
	 *
	 * @param userId the user id
	 * @param date the date
	 * @return the by user
	 */
	RoleIndividualGrantImport getByUser (String userId,GeneralDate date);
	
	RoleIndividualGrantImport getByUser(String userId);
}
