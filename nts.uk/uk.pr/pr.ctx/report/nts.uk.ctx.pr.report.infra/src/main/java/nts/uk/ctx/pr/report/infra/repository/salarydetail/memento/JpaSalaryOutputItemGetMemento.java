/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.report.infra.repository.salarydetail.memento;

import nts.uk.ctx.pr.report.dom.salarydetail.SalaryItemType;
import nts.uk.ctx.pr.report.dom.salarydetail.outputsetting.SalaryOutputItemGetMemento;
import nts.uk.ctx.pr.report.infra.entity.salarydetail.QlsptPaylstFormDetail;

/**
 * The Class JpaSalaryOutputItemGetMemento.
 */
public class JpaSalaryOutputItemGetMemento implements SalaryOutputItemGetMemento {

	/** The entity. */
	private QlsptPaylstFormDetail entity;

	/**
	 * Instantiates a new jpa salary output item get memento.
	 *
	 * @param entity the entity
	 */
	public JpaSalaryOutputItemGetMemento(QlsptPaylstFormDetail entity) {
		super();
		this.entity = entity;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nts.uk.ctx.pr.report.dom.wageledger.outputsetting.WLSettingItemGetMemento
	 * #getLinkageCode()
	 */
	@Override
	public String getLinkageCode() {
		return this.entity.getQlsptPaylstFormDetailPK().getItemAgreCd();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nts.uk.ctx.pr.report.dom.wageledger.outputsetting.WLSettingItemGetMemento
	 * #getType()
	 */
	@Override
	public SalaryItemType getType() {
		return SalaryItemType.valueOf(this.entity.getQlsptPaylstFormDetailPK().getAggregateAtr());
	}

	/* (non-Javadoc)
	 * @see nts.uk.ctx.pr.report.dom.salarydetail.outputsetting.SalaryOutputItemGetMemento#getOrderNumber()
	 */
	@Override
	public int getOrderNumber() {
		return this.entity.getDispOrder();
	}

}
