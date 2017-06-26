/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.report.dom.salarydetail.aggregate;

/**
 * The Interface SalaryAggregateItemHeaderSetMemento.
 */
public interface SalaryAggregateItemHeaderSetMemento {

	/**
	 * Sets the company code.
	 *
	 * @param companyCode the new company code
	 */
	 void setCompanyCode(String companyCode);

	/**
	 * Sets the salary aggregate item code.
	 *
	 * @param salaryAggregateItemCode the new salary aggregate item code
	 */
	void setSalaryAggregateItemCode(SalaryAggregateItemCode salaryAggregateItemCode);

	/**
	 * Sets the tax division.
	 *
	 * @param taxDivision the new tax division
	 */
	void setTaxDivision(TaxDivision taxDivision);
}
