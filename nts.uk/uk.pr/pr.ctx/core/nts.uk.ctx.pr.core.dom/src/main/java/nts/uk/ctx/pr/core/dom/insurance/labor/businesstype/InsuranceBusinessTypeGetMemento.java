/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.core.dom.insurance.labor.businesstype;

import nts.uk.ctx.pr.core.dom.insurance.BusinessName;

/**
 * The Interface InsuranceBusinessTypeGetMemento.
 */
public interface InsuranceBusinessTypeGetMemento {

	/**
	 * Gets the company code.
	 *
	 * @return the company code
	 */
	String getCompanyCode();

	/**
	 * Gets the biz order.
	 *
	 * @return the biz order
	 */
	BusinessTypeEnum getBizOrder();

	/**
	 * Gets the biz name.
	 *
	 * @return the biz name
	 */
	BusinessName getBizName();

}
