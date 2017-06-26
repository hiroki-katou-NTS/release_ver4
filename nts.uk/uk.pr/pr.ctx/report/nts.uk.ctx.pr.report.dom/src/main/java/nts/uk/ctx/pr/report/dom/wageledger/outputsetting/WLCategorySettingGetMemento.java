/******************************************************************
 * Copyright (c) 2015 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.report.dom.wageledger.outputsetting;

import java.util.List;

import nts.uk.ctx.pr.report.dom.wageledger.PaymentType;
import nts.uk.ctx.pr.report.dom.wageledger.WLCategory;

/**
 * The Interface WLCategorySettingGetMemento.
 */
public interface WLCategorySettingGetMemento {
	
	/**
	 * Gets the category.
	 *
	 * @return the category
	 */
	WLCategory getCategory();
	
	/**
	 * Gets the payment type.
	 *
	 * @return the payment type
	 */
	PaymentType getPaymentType();
	
	/**
	 * Gets the output items.
	 *
	 * @return the output items
	 */
	List<WLSettingItem> getOutputItems();
}
