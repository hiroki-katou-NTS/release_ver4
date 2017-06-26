/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.report.infra.repository.salarydetail.memento;

import java.util.Set;

import nts.gul.collection.LazySet;
import nts.uk.ctx.pr.report.dom.salarydetail.aggregate.SalaryAggregateItemGetMemento;
import nts.uk.ctx.pr.report.dom.salarydetail.aggregate.SalaryAggregateItemHeader;
import nts.uk.ctx.pr.report.dom.salarydetail.aggregate.SalaryAggregateItemName;
import nts.uk.ctx.pr.report.dom.salarydetail.item.SalaryItem;
import nts.uk.ctx.pr.report.infra.entity.salarydetail.QlsptPaylstAggreHead;

/**
 * The Class JpaSalaryAggregateItemGetMemento.
 */
public class JpaSalaryAggregateItemGetMemento implements SalaryAggregateItemGetMemento {

	/** The aggregate head. */
	private QlsptPaylstAggreHead aggregateHead;

	/**
	 * Instantiates a new jpa salary aggregate item get memento.
	 *
	 * @param aggregateHead the aggregate head
	 */
	public JpaSalaryAggregateItemGetMemento(QlsptPaylstAggreHead aggregateHead) {
		this.aggregateHead = aggregateHead;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.pr.report.dom.salarydetail.aggregate.
	 * SalaryAggregateItemGetMemento#getSalaryAggregateItemName()
	 */
	@Override
	public SalaryAggregateItemName getSalaryAggregateItemName() {
		return new SalaryAggregateItemName(this.aggregateHead.getAggregateName());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.pr.report.dom.salarydetail.aggregate.
	 * SalaryAggregateItemGetMemento#getSubItemCodes()
	 */
	@Override
	public Set<SalaryItem> getSubItemCodes() {
		return LazySet.withMap(() -> this.aggregateHead.getQlsptPaylstAggreDetailList(), item -> {
			SalaryItem salaryItem = new SalaryItem();
			salaryItem.setSalaryItemCode(item.getQlsptPaylstAggreDetailPK().getItemCd());
			salaryItem.setSalaryItemName("基本給 " + item.getQlsptPaylstAggreDetailPK().getItemCd());
			return salaryItem;
		});
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.pr.report.dom.salarydetail.aggregate.
	 * SalaryAggregateItemGetMemento#getSalaryAggregateItem()
	 */
	@Override
	public SalaryAggregateItemHeader getSalaryAggregateItemHeader() {
		return new SalaryAggregateItemHeader(
				new JpaSalaryAggregateItemHeaderGetMemento(this.aggregateHead.getQlsptPaylstAggreHeadPK()));
	}
}
