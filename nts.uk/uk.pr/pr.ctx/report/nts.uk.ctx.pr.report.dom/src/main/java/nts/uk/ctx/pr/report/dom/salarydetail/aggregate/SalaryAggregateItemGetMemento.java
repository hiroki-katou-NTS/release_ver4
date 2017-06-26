/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.report.dom.salarydetail.aggregate;

import java.util.Set;

import nts.uk.ctx.pr.report.dom.salarydetail.item.SalaryItem;

/**
 * The Interface SalaryAggregateItemGetMemento.
 */
public interface SalaryAggregateItemGetMemento {

	/**
	 * Gets the salary aggregate item header.
	 *
	 * @return the salary aggregate item header
	 */
	SalaryAggregateItemHeader getSalaryAggregateItemHeader();

	/**
	 * Gets the salary aggregate item name.
	 *
	 * @return the salary aggregate item name
	 */
	SalaryAggregateItemName getSalaryAggregateItemName();

	/**
	 * Gets the sub item codes.
	 *
	 * @return the sub item codes
	 */
	Set<SalaryItem> getSubItemCodes();

}
