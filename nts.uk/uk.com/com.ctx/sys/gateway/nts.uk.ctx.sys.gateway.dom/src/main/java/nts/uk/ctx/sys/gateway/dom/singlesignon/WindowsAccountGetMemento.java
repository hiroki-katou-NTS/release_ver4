/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.sys.gateway.dom.singlesignon;

import java.util.List;

/**
 * The Interface WindowAccountGetMemento.
 */
public interface WindowsAccountGetMemento {
	

	/**
	 * Gets the company id.
	 *
	 * @return the company id
	 */
	String getCompanyId();
	
	/**
	 * Gets the employee id.
	 *
	 * @return the employee id
	 */
	String getEmployeeId();
	
	/**
	 * Gets the hot name.
	 *
	 * @return the hot name
	 */
	List<WindowsAccountInfo> getAccountInfos();
	
}
