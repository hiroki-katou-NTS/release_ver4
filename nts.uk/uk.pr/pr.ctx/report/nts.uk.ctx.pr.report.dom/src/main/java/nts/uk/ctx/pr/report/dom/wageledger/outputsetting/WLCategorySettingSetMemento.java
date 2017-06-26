/******************************************************************
 * Copyright (c) 2015 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.report.dom.wageledger.outputsetting;

import java.util.List;

import nts.uk.ctx.pr.report.dom.wageledger.PaymentType;
import nts.uk.ctx.pr.report.dom.wageledger.WLCategory;

/**
 * The Interface WageLedgerCategorySettingSetMemento.
 */
public interface WLCategorySettingSetMemento {
	
	/**
	 * Sets the category.
	 *
	 * @param category the new category
	 */
	void setCategory(WLCategory category);
	
	/**
	 * Sets the payment type.
	 *
	 * @param paymentType the new payment type
	 */
	void setPaymentType(PaymentType paymentType);
	
	/**
	 * Sets the output items.
	 *
	 * @param outputItems the new output items
	 */
	void setOutputItems(List<WLSettingItem> outputItems);
}
