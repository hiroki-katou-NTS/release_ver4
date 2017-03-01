/******************************************************************
 * Copyright (c) 2015 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.report.dom.wageledger.outputsetting;


import java.util.List;

import lombok.Getter;
import nts.uk.ctx.pr.report.dom.wageledger.PaymentType;
import nts.uk.ctx.pr.report.dom.wageledger.WLCategory;

/**
 * The Class WageLedgerCategorySetting.
 */
@Getter
public class WLCategorySetting {
	
	/** The category. */
	private WLCategory category;
	
	/** The payment type. */
	private PaymentType paymentType;
	
	/** The output items. */
	private List<WLSettingItem> outputItems;

	/**
	 * Instantiates a new wage ledger category setting.
	 *
	 * @param category the category
	 * @param paymentType the payment type
	 * @param outputItems the output items
	 */
	public WLCategorySetting(WLCategorySettingGetMemento memento) {
		super();
		this.outputItems = memento.getOutputItems();
		this.category = memento.getCategory();
		this.paymentType = memento.getPaymentType();
	}
	
	/**
	 * Save to memento.
	 *
	 * @param memento the memento
	 */
	public void saveToMemento(WLCategorySettingSetMemento memento) {
		memento.setOutputItems(this.outputItems);
		memento.setCategory(this.category);
		memento.setPaymentType(this.paymentType);
	}
}
