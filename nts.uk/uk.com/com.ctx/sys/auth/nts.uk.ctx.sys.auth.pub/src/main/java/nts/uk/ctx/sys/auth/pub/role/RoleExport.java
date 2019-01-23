/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.sys.auth.pub.role;

import lombok.Getter;

/**
 * The Class RoleExport.
 */
@Getter
public class RoleExport {
	
	/** The company Id. */
	public String companyId;

	/** The role id. */
	public String roleId;

	/** The role code. */
	public String roleCode;

	/** The role name. */
	public String roleName;
	
	/** The assign atr. */
	private Integer assignAtr; 
	
	private Integer employeeReferenceRange;

	/**
	 * Instantiates a new role export.
	 *
	 * @param companyId the company id
	 * @param roleId the role id
	 * @param roleCode the role code
	 * @param roleName the role name
	 * @param assignAtr the assign atr
	 */
	public RoleExport(String companyId, String roleId, String roleCode, String roleName,
			Integer assignAtr, Integer employeeReferenceRange) {
		super();
		this.companyId = companyId;
		this.roleId = roleId;
		this.roleCode = roleCode;
		this.roleName = roleName;
		this.assignAtr = assignAtr;
		this.employeeReferenceRange = employeeReferenceRange;
	}
	
}
