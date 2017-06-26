/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.report.dom.salarydetail.outputsetting;

import nts.uk.ctx.pr.report.dom.salarydetail.SalaryItemType;

/**
 * The Interface SalaryOutputItemSetMemento.
 */
public interface SalaryOutputItemSetMemento {

	/**
	 * Sets the linkage code.
	 *
	 * @param linkageCode the new linkage code
	 */
	 void setLinkageCode(String linkageCode);

	/**
	 * Sets the type.
	 *
	 * @param salaryItemType the new type
	 */
	void setType(SalaryItemType salaryItemType);

	/**
	 * Sets the order number.
	 *
	 * @param orderNumber the new order number
	 */
	void setOrderNumber(int orderNumber);
}
