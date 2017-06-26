/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.report.infra.repository.salarydetail.memento;

import nts.uk.ctx.pr.report.dom.salarydetail.SalaryItemType;
import nts.uk.ctx.pr.report.dom.salarydetail.outputsetting.SalaryOutputItemSetMemento;
import nts.uk.ctx.pr.report.dom.salarydetail.outputsetting.SalaryOutputSettingCode;
import nts.uk.ctx.pr.report.infra.entity.salarydetail.QlsptPaylstFormDetail;
import nts.uk.ctx.pr.report.infra.entity.salarydetail.QlsptPaylstFormDetailPK;

/**
 * The Class JpaSalaryOutputItemSetMemento.
 */
public class JpaSalaryOutputItemSetMemento implements SalaryOutputItemSetMemento {

	/** The entity. */
	protected QlsptPaylstFormDetail entity;

	/**
	 * Instantiates a new jpa salary output item set memento.
	 *
	 * @param entity the entity
	 * @param companyCode the company code
	 * @param code the code
	 */
	public JpaSalaryOutputItemSetMemento(QlsptPaylstFormDetail entity, String companyCode,
			SalaryOutputSettingCode code) {
		this.entity = entity;
		if (this.entity.getQlsptPaylstFormDetailPK() == null) {
			this.entity.setQlsptPaylstFormDetailPK(new QlsptPaylstFormDetailPK());
		}
		this.entity.getQlsptPaylstFormDetailPK().setCcd(companyCode);
		this.entity.getQlsptPaylstFormDetailPK().setFormCd(code.v());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.pr.report.dom.salarydetail.outputsetting.
	 * SalaryOutputItemSetMemento#setLinkageCode(java.lang.String)
	 */
	@Override
	public void setLinkageCode(String linkageCode) {
		this.entity.getQlsptPaylstFormDetailPK().setItemAgreCd(linkageCode);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.pr.report.dom.salarydetail.outputsetting.
	 * SalaryOutputItemSetMemento#setType(nts.uk.ctx.pr.report.dom.salarydetail.
	 * SalaryItemType)
	 */
	@Override
	public void setType(SalaryItemType salaryItemType) {
		this.entity.getQlsptPaylstFormDetailPK().setAggregateAtr(salaryItemType.value);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.pr.report.dom.salarydetail.outputsetting.
	 * SalaryOutputItemSetMemento#setOrderNumber(int)
	 */
	@Override
	public void setOrderNumber(int orderNumber) {
		this.entity.setDispOrder(orderNumber);
	}

}
