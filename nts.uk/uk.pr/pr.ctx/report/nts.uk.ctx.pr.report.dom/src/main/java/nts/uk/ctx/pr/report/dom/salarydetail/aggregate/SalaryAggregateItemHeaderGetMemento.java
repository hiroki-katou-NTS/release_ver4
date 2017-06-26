/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.report.dom.salarydetail.aggregate;

/**
 * The Interface SalaryAggregateItemHeaderGetMemento.
 */
public interface SalaryAggregateItemHeaderGetMemento {

	/**
	 * Gets the company code.
	 *
	 * @return the company code
	 */
	String getCompanyCode();

	/**
	 * Gets the salary aggregate item code.
	 *
	 * @return the salary aggregate item code
	 */
	SalaryAggregateItemCode getSalaryAggregateItemCode();

	/**
	 * Gets the tax division.
	 *
	 * @return the tax division
	 */
	TaxDivision getTaxDivision();
}
