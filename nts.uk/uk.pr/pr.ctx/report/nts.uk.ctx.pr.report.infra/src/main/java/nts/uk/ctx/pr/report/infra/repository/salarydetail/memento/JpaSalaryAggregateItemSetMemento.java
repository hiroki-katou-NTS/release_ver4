/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.report.infra.repository.salarydetail.memento;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import nts.uk.ctx.pr.report.dom.salarydetail.aggregate.SalaryAggregateItemHeader;
import nts.uk.ctx.pr.report.dom.salarydetail.aggregate.SalaryAggregateItemName;
import nts.uk.ctx.pr.report.dom.salarydetail.aggregate.SalaryAggregateItemSetMemento;
import nts.uk.ctx.pr.report.dom.salarydetail.item.SalaryItem;
import nts.uk.ctx.pr.report.infra.entity.salarydetail.QlsptPaylstAggreDetail;
import nts.uk.ctx.pr.report.infra.entity.salarydetail.QlsptPaylstAggreDetailPK;
import nts.uk.ctx.pr.report.infra.entity.salarydetail.QlsptPaylstAggreHead;
import nts.uk.ctx.pr.report.infra.entity.salarydetail.QlsptPaylstAggreHeadPK;

/**
 * The Class JpaSalaryAggregateItemSetMemento.
 */
public class JpaSalaryAggregateItemSetMemento implements SalaryAggregateItemSetMemento {

	/** The aggregate head. */
	private QlsptPaylstAggreHead aggregateHead;

	/**
	 * Instantiates a new jpa salary aggregate item set memento.
	 *
	 * @param aggregateHead
	 *            the aggregate head
	 */
	public JpaSalaryAggregateItemSetMemento(QlsptPaylstAggreHead aggregateHead) {
		this.aggregateHead = aggregateHead;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.pr.report.dom.salarydetail.aggregate.
	 * SalaryAggregateItemSetMemento#setSalaryAggregateItemHeader(nts.uk.ctx.pr.
	 * report.dom.salarydetail.aggregate.SalaryAggregateItemHeader)
	 */
	@Override
	public void setSalaryAggregateItemHeader(SalaryAggregateItemHeader header) {
		QlsptPaylstAggreHeadPK headPk = new QlsptPaylstAggreHeadPK();
		header.saveToMemento(new JpaSalaryAggregateItemHeaderSetMemento(headPk));
		this.aggregateHead.setQlsptPaylstAggreHeadPK(headPk);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.pr.report.dom.salarydetail.aggregate.
	 * SalaryAggregateItemSetMemento#setSalaryAggregateItemName(nts.uk.ctx.pr.
	 * report.dom.salarydetail.aggregate.SalaryAggregateItemName)
	 */
	@Override
	public void setSalaryAggregateItemName(SalaryAggregateItemName salaryAggregateItemName) {
		this.aggregateHead.setAggregateName(salaryAggregateItemName.v());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.pr.report.dom.salarydetail.aggregate.
	 * SalaryAggregateItemSetMemento#setSubItemCodes(java.util.Set)
	 */
	@Override
	public void setSubItemCodes(Set<SalaryItem> subItemCodes) {
		List<QlsptPaylstAggreDetail> listItem = new ArrayList<>();
		QlsptPaylstAggreHeadPK headPk = this.aggregateHead.getQlsptPaylstAggreHeadPK();
		subItemCodes.stream().forEach(item -> {
			QlsptPaylstAggreDetail aggreDetail = new QlsptPaylstAggreDetail();
			aggreDetail.setQlsptPaylstAggreDetailPK(new QlsptPaylstAggreDetailPK(headPk.getCcd(),
					headPk.getAggregateCd(), headPk.getCtgAtr(), item.getSalaryItemCode()));
			listItem.add(aggreDetail);
		});
		this.aggregateHead.setQlsptPaylstAggreDetailList(listItem);
	}

}
