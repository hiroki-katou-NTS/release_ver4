/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.sys.gateway.dom.singlesignon;

/**
 * The Interface OtherSysAccountGetMemento.
 */
public interface OtherSysAccountGetMemento {


	/**
	 * Company id.
	 *
	 * @return the string
	 */
	String getCompanyId();
	/**
	 * Gets the employee id.
	 *
	 * @return the employee id
	 */
	String getEmployeeId();
	
	/**
	 * Gets the company code.
	 *
	 * @return the company code
	 */
	CompanyCode getCompanyCode();
	
	/**
	 * Gets the user name.
	 *
	 * @return the user name
	 */
	UserName getUserName();
	

	/**
	 * Gets the use atr.
	 *
	 * @return the use atr
	 */
	UseAtr getUseAtr();
	
	
}
