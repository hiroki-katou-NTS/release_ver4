/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.sys.auth.pub.role;

import java.util.List;

/**
 * The Interface RoleExportRepo.
 */
public interface RoleExportRepo {
	
	/**
	 * Find by id.
	 *
	 * @param roleId the role id
	 * @return the list
	 */
	List<RoleExport> findById(String roleId);
	
	/**
	 * Find by list role id.
	 *
	 * @param companyId the company id
	 * @param lstRoleId the lst role id
	 * @return the list
	 */
	List<RoleExport> findByListRoleId(String companyId,List<String> lstRoleId);
}
