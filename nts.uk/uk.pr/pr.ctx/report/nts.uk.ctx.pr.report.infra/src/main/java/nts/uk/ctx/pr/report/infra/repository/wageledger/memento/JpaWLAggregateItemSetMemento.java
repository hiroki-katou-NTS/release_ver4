/******************************************************************
 * Copyright (c) 2015 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.report.infra.repository.wageledger.memento;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import nts.uk.ctx.pr.report.dom.wageledger.PaymentType;
import nts.uk.ctx.pr.report.dom.wageledger.WLCategory;
import nts.uk.ctx.pr.report.dom.wageledger.aggregate.WLAggregateItemCode;
import nts.uk.ctx.pr.report.dom.wageledger.aggregate.WLAggregateItemName;
import nts.uk.ctx.pr.report.dom.wageledger.aggregate.WLAggregateItemSetMemento;
import nts.uk.ctx.pr.report.dom.wageledger.aggregate.WLItemSubject;
import nts.uk.ctx.pr.report.infra.entity.wageledger.QlsptLedgerAggreDetail;
import nts.uk.ctx.pr.report.infra.entity.wageledger.QlsptLedgerAggreDetailPK;
import nts.uk.ctx.pr.report.infra.entity.wageledger.QlsptLedgerAggreHead;
import nts.uk.ctx.pr.report.infra.entity.wageledger.QlsptLedgerAggreHeadPK;

/**
 * The Class JpaWLAggregateItemSetMemento.
 */
public class JpaWLAggregateItemSetMemento implements WLAggregateItemSetMemento {
	
	/** The entity. */
	private QlsptLedgerAggreHead entity;
	
	/** The company code. */
	private String companyCode;
	
	/** The code. */
	private WLAggregateItemCode code;
	
	/** The category. */
	private WLCategory category;
	
	/** The payment type. */
	private PaymentType paymentType;
	
	/**
	 * Instantiates a new jpa WL aggregate item set memento.
	 *
	 * @param entity the entity
	 */
	public JpaWLAggregateItemSetMemento(QlsptLedgerAggreHead entity) {
		super();
		this.entity = entity;
		if (this.entity.getQlsptLedgerAggreHeadPK() == null) {
			this.entity.setQlsptLedgerAggreHeadPK(new QlsptLedgerAggreHeadPK());
		}
	}
	
	/* (non-Javadoc)
	 * @see nts.uk.ctx.pr.report.dom.wageledger.aggregate.WLAggregateItemSetMemento
	 * #setName(nts.uk.ctx.pr.report.dom.wageledger.aggregate.WLAggregateItemName)
	 */
	@Override
	public void setName(WLAggregateItemName name) {
		this.entity.setAggregateName(name.v());
	}
	
	/* (non-Javadoc)
	 * @see nts.uk.ctx.pr.report.dom.wageledger.aggregate.WLAggregateItemSetMemento
	 * #setShowNameZeroValue(java.lang.Boolean)
	 */
	@Override
	public void setShowNameZeroValue(Boolean showNameZeroValue) {
		this.entity.setDispNameZeroAtr(showNameZeroValue ? 1 : 0);
	}
	
	/* (non-Javadoc)
	 * @see nts.uk.ctx.pr.report.dom.wageledger.aggregate.WLAggregateItemSetMemento
	 * #setShowValueZeroValue(boolean)
	 */
	@Override
	public void setShowValueZeroValue(boolean showValueZeroValue) {
		this.entity.setDispZeroAtr(showValueZeroValue ? 1 : 0);
	}
	
	/* (non-Javadoc)
	 * @see nts.uk.ctx.pr.report.dom.wageledger.aggregate.WLAggregateItemSetMemento
	 * #setSubItems(java.util.Set)
	 */
	@Override
	public void setSubItems(Set<String> subItems) {
		List<QlsptLedgerAggreDetail> itemList = new ArrayList<>();
		subItems.parallelStream().forEach(item -> {
			QlsptLedgerAggreDetail itemEntity = new QlsptLedgerAggreDetail();
			itemEntity.setQlsptLedgerAggreDetailPK(new QlsptLedgerAggreDetailPK(
					this.companyCode, this.paymentType.value,
					this.code.v(), this.category.value, item));
			itemList.add(itemEntity);
		});
		this.entity.setQlsptLedgerAggreDetailList(itemList);
	}
	
	/* (non-Javadoc)
	 * @see nts.uk.ctx.pr.report.dom.wageledger.aggregate.WLAggregateItemSetMemento
	 * #setSubject(nts.uk.ctx.pr.report.dom.wageledger.aggregate.WLItemSubject)
	 */
	@Override
	public void setSubject(WLItemSubject itemSubject) {
		itemSubject.saveToMemento(
				new JpaWLItemSubjectSetMemento(this.entity.getQlsptLedgerAggreHeadPK()));
		this.companyCode = itemSubject.getCompanyCode();
		this.category = itemSubject.getCategory();
		this.paymentType = itemSubject.getPaymentType();
		this.code = itemSubject.getCode();
	}

}
