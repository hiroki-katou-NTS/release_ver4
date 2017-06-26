/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.report.infra.repository.salarydetail.memento;

import nts.uk.ctx.pr.report.dom.salarydetail.aggregate.SalaryAggregateItemCode;
import nts.uk.ctx.pr.report.dom.salarydetail.aggregate.SalaryAggregateItemHeaderGetMemento;
import nts.uk.ctx.pr.report.dom.salarydetail.aggregate.TaxDivision;
import nts.uk.ctx.pr.report.infra.entity.salarydetail.QlsptPaylstAggreHeadPK;

/**
 * The Class JpaSalaryAggregateItemHeaderGetMemento.
 */
public class JpaSalaryAggregateItemHeaderGetMemento implements SalaryAggregateItemHeaderGetMemento {

	/** The head pk. */
	private QlsptPaylstAggreHeadPK headPk;

	/**
	 * Instantiates a new jpa salary aggregate item header get memento.
	 *
	 * @param headPk
	 *            the head pk
	 */
	public JpaSalaryAggregateItemHeaderGetMemento(QlsptPaylstAggreHeadPK headPk) {
		this.headPk = headPk;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.pr.report.dom.salarydetail.aggregate.
	 * SalaryAggregateItemHeaderGetMemento#getCompanyCode()
	 */
	@Override
	public String getCompanyCode() {
		return this.headPk.getCcd();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.pr.report.dom.salarydetail.aggregate.
	 * SalaryAggregateItemHeaderGetMemento#getSalaryAggregateItemCode()
	 */
	@Override
	public SalaryAggregateItemCode getSalaryAggregateItemCode() {
		return new SalaryAggregateItemCode(this.headPk.getAggregateCd());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.pr.report.dom.salarydetail.aggregate.
	 * SalaryAggregateItemHeaderGetMemento#getTaxDivision()
	 */
	@Override
	public TaxDivision getTaxDivision() {
		return TaxDivision.valueOf(this.headPk.getCtgAtr());
	}

}
