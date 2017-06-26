/******************************************************************
 * Copyright (c) 2015 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.report.infra.repository.wageledger.memento;

import java.util.Set;

import nts.gul.collection.LazySet;
import nts.uk.ctx.pr.report.dom.wageledger.aggregate.WLAggregateItemGetMemento;
import nts.uk.ctx.pr.report.dom.wageledger.aggregate.WLAggregateItemName;
import nts.uk.ctx.pr.report.dom.wageledger.aggregate.WLItemSubject;
import nts.uk.ctx.pr.report.infra.entity.wageledger.QlsptLedgerAggreHead;

/**
 * The Class JpaWLAggregateItemGetMemento.
 */
public class JpaWLAggregateItemGetMemento implements WLAggregateItemGetMemento {

	/** The entity. */
	private QlsptLedgerAggreHead entity;

	/**
	 * Instantiates a new jpa WL aggregate item get memento.
	 *
	 * @param entity the entity
	 */
	public JpaWLAggregateItemGetMemento(QlsptLedgerAggreHead entity) {
		super();
		this.entity = entity;
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * nts.uk.ctx.pr.report.dom.wageledger.aggregate.WLAggregateItemGetMemento#
	 * getName()
	 */
	@Override
	public WLAggregateItemName getName() {
		return new WLAggregateItemName(this.entity.getAggregateName());
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * nts.uk.ctx.pr.report.dom.wageledger.aggregate.WLAggregateItemGetMemento#
	 * getShowNameZeroValue()
	 */
	@Override
	public Boolean getShowNameZeroValue() {
		return this.entity.getDispNameZeroAtr() == 1;
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * nts.uk.ctx.pr.report.dom.wageledger.aggregate.WLAggregateItemGetMemento#
	 * getShowValueZeroValue()
	 */
	@Override
	public Boolean getShowValueZeroValue() {
		return this.entity.getDispZeroAtr() == 1;
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * nts.uk.ctx.pr.report.dom.wageledger.aggregate.WLAggregateItemGetMemento#
	 * getSubItems()
	 */
	@Override
	public Set<String> getSubItems() {
		return LazySet.withMap(() -> this.entity.getQlsptLedgerAggreDetailList(),
				item -> item.getQlsptLedgerAggreDetailPK().getItemCd());
	}

	/* (non-Javadoc)
	 * @see nts.uk.ctx.pr.report.dom.wageledger.aggregate.WLAggregateItemGetMemento#
	 * getSubject()
	 */
	@Override
	public WLItemSubject getSubject() {
		return new WLItemSubject(
				new JpaWLItemSubjectGetMemento(this.entity.getQlsptLedgerAggreHeadPK()));
	}

}
