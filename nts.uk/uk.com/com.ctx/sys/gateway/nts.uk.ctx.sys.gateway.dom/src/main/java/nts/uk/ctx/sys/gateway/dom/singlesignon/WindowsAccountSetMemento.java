/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.sys.gateway.dom.singlesignon;

import java.util.List;

/**
 * The Interface WindowAccountSetMemento.
 */
public interface WindowsAccountSetMemento {

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
	 * Sets the hot name.
	 *
	 * @param accountInfos the new account infos
	 */
	void setAccountInfos(List<WindowsAccountInfo> accountInfos);
	
}
