/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.sys.gateway.dom.login.adapter;

import java.util.List;

import nts.uk.ctx.sys.gateway.dom.login.dto.RoleImport;

public interface RoleAdapter {
	
	/**
	 * Gets the all by id.
	 *
	 * @param roleId the role id
	 * @return the all by id
	 */
	List<RoleImport> getAllById(String roleId);
	
	public boolean isEmpWhetherLoginerCharge();
}
