/******************************************************************
 * Copyright (c) 2015 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.report.infra.repository.wageledger.memento;

import nts.uk.ctx.pr.report.dom.wageledger.PaymentType;
import nts.uk.ctx.pr.report.dom.wageledger.WLCategory;
import nts.uk.ctx.pr.report.dom.wageledger.aggregate.WLAggregateItemCode;
import nts.uk.ctx.pr.report.dom.wageledger.aggregate.WLItemSubjectGetMemento;
import nts.uk.ctx.pr.report.infra.entity.wageledger.QlsptLedgerAggreHeadPK;

/**
 * The Class JpaWLItemSubjectGetMemento.
 */
public class JpaWLItemSubjectGetMemento implements WLItemSubjectGetMemento {
	
	/** The entity. */
	private QlsptLedgerAggreHeadPK entity;
	
	/**
	 * Instantiates a new jpa WL item subject get memento.
	 *
	 * @param entity the entity
	 */
	public JpaWLItemSubjectGetMemento(QlsptLedgerAggreHeadPK entity) {
		super();
		this.entity = entity;
	}

	/* (non-Javadoc)
	 * @see nts.uk.ctx.pr.report.dom.wageledger.aggregate.WLItemSubjectGetMemento#getCategory()
	 */
	@Override
	public WLCategory getCategory() {
		return WLCategory.valueOf(this.entity.getCtgAtr());
	}

	/* (non-Javadoc)
	 * @see nts.uk.ctx.pr.report.dom.wageledger.aggregate.WLItemSubjectGetMemento#getPaymentType()
	 */
	@Override
	public PaymentType getPaymentType() {
		return PaymentType.valueOf(this.entity.getPayBonusAtr());
	}

	/* (non-Javadoc)
	 * @see nts.uk.ctx.pr.report.dom.wageledger.aggregate.WLItemSubjectGetMemento#getCompanyCode()
	 */
	@Override
	public String getCompanyCode() {
		return this.entity.getCcd();
	}

	/* (non-Javadoc)
	 * @see nts.uk.ctx.pr.report.dom.wageledger.aggregate.WLItemSubjectGetMemento#getCode()
	 */
	@Override
	public WLAggregateItemCode getCode() {
		return new WLAggregateItemCode(this.entity.getAggregateCd());
	}

}
