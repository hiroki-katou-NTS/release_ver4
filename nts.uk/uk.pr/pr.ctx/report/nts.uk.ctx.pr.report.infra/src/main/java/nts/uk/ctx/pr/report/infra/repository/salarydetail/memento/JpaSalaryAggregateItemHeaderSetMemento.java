/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.report.infra.repository.salarydetail.memento;

import nts.uk.ctx.pr.report.dom.salarydetail.aggregate.SalaryAggregateItemCode;
import nts.uk.ctx.pr.report.dom.salarydetail.aggregate.SalaryAggregateItemHeaderSetMemento;
import nts.uk.ctx.pr.report.dom.salarydetail.aggregate.TaxDivision;
import nts.uk.ctx.pr.report.infra.entity.salarydetail.QlsptPaylstAggreHeadPK;

/**
 * The Class JpaSalaryAggregateItemHeaderSetMemento.
 */
public class JpaSalaryAggregateItemHeaderSetMemento implements SalaryAggregateItemHeaderSetMemento {

	/** The head pk. */
	private QlsptPaylstAggreHeadPK headPk;

	/**
	 * Instantiates a new jpa salary aggregate item header set memento.
	 *
	 * @param headPk the head pk
	 */
	public JpaSalaryAggregateItemHeaderSetMemento(QlsptPaylstAggreHeadPK headPk) {
		this.headPk = headPk;
	}

	/**
	 * Sets the company code.
	 *
	 * @param companyCode the new company code
	 */
	@Override
	public void setCompanyCode(String companyCode) {
		this.headPk.setCcd(companyCode);
	}

	/**
	 * Sets the salary aggregate item code.
	 *
	 * @param salaryAggregateItemCode the new salary aggregate item code
	 */
	@Override
	public void setSalaryAggregateItemCode(SalaryAggregateItemCode salaryAggregateItemCode) {
		this.headPk.setAggregateCd(salaryAggregateItemCode.v());
	}

	/**
	 * Sets the tax division.
	 *
	 * @param taxDivision the new tax division
	 */
	@Override
	public void setTaxDivision(TaxDivision taxDivision) {
		this.headPk.setCtgAtr(taxDivision.value);
	}

}
