/******************************************************************
 * Copyright (c) 2015 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.report.infra.repository.wageledger.memento;

import nts.uk.ctx.pr.report.dom.wageledger.PaymentType;
import nts.uk.ctx.pr.report.dom.wageledger.WLCategory;
import nts.uk.ctx.pr.report.dom.wageledger.aggregate.WLAggregateItemCode;
import nts.uk.ctx.pr.report.dom.wageledger.aggregate.WLItemSubjectSetMemento;
import nts.uk.ctx.pr.report.infra.entity.wageledger.QlsptLedgerAggreHeadPK;

/**
 * The Class JpaWLItemSubjectSetMemento.
 */
public class JpaWLItemSubjectSetMemento implements WLItemSubjectSetMemento {
	
	/** The entity. */
	private QlsptLedgerAggreHeadPK entity;
	
	/**
	 * Instantiates a new jpa WL item subject set memento.
	 *
	 * @param entity the entity
	 */
	public JpaWLItemSubjectSetMemento(QlsptLedgerAggreHeadPK entity) {
		super();
		this.entity = entity;
	}

	/* (non-Javadoc)
	 * @see nts.uk.ctx.pr.report.dom.wageledger.aggregate.WLItemSubjectSetMemento
	 * #setCateory(nts.uk.ctx.pr.report.dom.wageledger.WLCategory)
	 */
	@Override
	public void setCateory(WLCategory category) {
		this.entity.setCtgAtr(category.value);
	}

	/* (non-Javadoc)
	 * @see nts.uk.ctx.pr.report.dom.wageledger.aggregate.WLItemSubjectSetMemento
	 * #setPaymentType(nts.uk.ctx.pr.report.dom.wageledger.PaymentType)
	 */
	@Override
	public void setPaymentType(PaymentType paymentType) {
		this.entity.setPayBonusAtr(paymentType.value);
	}

	/* (non-Javadoc)
	 * @see nts.uk.ctx.pr.report.dom.wageledger.aggregate.WLItemSubjectSetMemento
	 * #setCompanyCode(nts.uk.ctx.pr.report.dom.company.CompanyCode)
	 */
	@Override
	public void setCompanyCode(String companyCode) {
		this.entity.setCcd(companyCode);
	}

	/* (non-Javadoc)
	 * @see nts.uk.ctx.pr.report.dom.wageledger.aggregate.WLItemSubjectSetMemento
	 * #setCode(nts.uk.ctx.pr.report.dom.wageledger.aggregate.WLAggregateItemCode)
	 */
	@Override
	public void setCode(WLAggregateItemCode code) {
		this.entity.setAggregateCd(code.v());
	}

}
