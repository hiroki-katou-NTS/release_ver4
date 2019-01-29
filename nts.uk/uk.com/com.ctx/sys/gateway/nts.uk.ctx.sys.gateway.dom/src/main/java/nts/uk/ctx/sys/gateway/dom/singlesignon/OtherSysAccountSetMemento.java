/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.sys.gateway.dom.singlesignon;

/**
 * The Interface OtherSysAccountSetMemento.
 */
public interface OtherSysAccountSetMemento {
	
	/**
	 * Sets the company id.
	 *
	 * @param companyId the new company id
	 */
	void setCompanyId(String companyId);
	
	/**
	 * Sets the employee id.
	 *
	 * @param employeeId the new employee id
	 */
	void setEmployeeId(String employeeId);
	
	/**
	 * Sets the company code.
	 *
	 * @param companyCode the new company code
	 */
	void setCompanyCode(CompanyCode companyCode);
	
	
	/**
	 * Sets the user name.
	 *
	 * @param userName the new user name
	 */
	void setUserName(UserName userName);
	
	
	/**
	 * Sets the use division.
	 *
	 * @param useDivision the new use division
	 */
	void setUseAtr(UseAtr useAtr);
	
}
