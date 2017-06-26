/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.report.dom.salarydetail.aggregate;

import java.util.Set;

import nts.uk.ctx.pr.report.dom.salarydetail.item.SalaryItem;

/**
 * The Interface SalaryAggregateItemSetMemento.
 */
public interface SalaryAggregateItemSetMemento {

	/**
	 * Sets the salary aggregate item header.
	 *
	 * @param header the new salary aggregate item header
	 */
	void setSalaryAggregateItemHeader(SalaryAggregateItemHeader header);

	/**
	 * Sets the salary aggregate item name.
	 *
	 * @param salaryAggregateItemName the new salary aggregate item name
	 */
	void setSalaryAggregateItemName(SalaryAggregateItemName salaryAggregateItemName);

	/**
	 * Sets the sub item codes.
	 *
	 * @param subItemCodes the new sub item codes
	 */
	void setSubItemCodes(Set<SalaryItem> subItemCodes);

}
