/******************************************************************
 * Copyright (c) 2015 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.report.infra.repository.wageledger.memento;

import java.util.List;
import java.util.stream.Collectors;

import nts.gul.collection.LazyList;
import nts.uk.ctx.pr.report.dom.wageledger.PaymentType;
import nts.uk.ctx.pr.report.dom.wageledger.WLCategory;
import nts.uk.ctx.pr.report.dom.wageledger.outputsetting.WLCategorySettingGetMemento;
import nts.uk.ctx.pr.report.dom.wageledger.outputsetting.WLSettingItem;
import nts.uk.ctx.pr.report.infra.entity.wageledger.QlsptLedgerFormHead;

/**
 * The Class JpaWLCategorySettingGetMemento.
 */
public class JpaWLCategorySettingGetMemento implements WLCategorySettingGetMemento {
	
	/** The details. */
	private QlsptLedgerFormHead entity;
	
	/** The category. */
	private WLCategory category;
	
	/** The payment type. */
	private PaymentType paymentType;
	/**
	 * Instantiates a new jpa WL category setting get memento.
	 *
	 * @param deatails the deatails
	 * @param category the category
	 * @param paymentType the payment type
	 */
	public JpaWLCategorySettingGetMemento(QlsptLedgerFormHead entity, WLCategory category,
			PaymentType paymentType) {
		super();
		this.entity = entity;
		this.category = category;
		this.paymentType = paymentType;
	}

	/* (non-Javadoc)
	 * @see nts.uk.ctx.pr.report.dom.wageledger.outputsetting.WLCategorySettingGetMemento
	 * #getCategory()
	 */
	@Override
	public WLCategory getCategory() {
		return this.category;
	}

	/* (non-Javadoc)
	 * @see nts.uk.ctx.pr.report.dom.wageledger.outputsetting.WLCategorySettingGetMemento
	 * #getPaymentType()
	 */
	@Override
	public PaymentType getPaymentType() {
		return this.paymentType;
	}

	/* (non-Javadoc)
	 * @see nts.uk.ctx.pr.report.dom.wageledger.outputsetting.WLCategorySettingGetMemento
	 * #getOutputItems()
	 */
	@Override
	public List<WLSettingItem> getOutputItems() {
		return LazyList.withMap(
				() -> this.entity.getQlsptLedgerFormDetailList().stream()
						.filter(data -> data.getQlsptLedgerFormDetailPK().getCtgAtr() == this.category.value
								&& data.getQlsptLedgerFormDetailPK().getPayBonusAtr() == this.paymentType.value)
						.sorted((data1, data2) -> {
							return data2.getDispOrder() - data1.getDispOrder();
						})
						.collect(Collectors.toList()),
				detail -> new WLSettingItem(new JpaWLSettingItemGetMemento(detail)));
	}

}
