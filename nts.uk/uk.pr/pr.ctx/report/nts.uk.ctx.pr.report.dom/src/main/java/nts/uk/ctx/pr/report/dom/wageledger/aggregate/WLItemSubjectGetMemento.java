/******************************************************************
 * Copyright (c) 2015 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.report.dom.wageledger.aggregate;

import nts.uk.ctx.pr.report.dom.wageledger.PaymentType;
import nts.uk.ctx.pr.report.dom.wageledger.WLCategory;

/**
 * The Interface WLItemSubjectGetMemento.
 */
public interface WLItemSubjectGetMemento {
	 
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
 	 * Gets the company code.
 	 *
 	 * @return the company code
 	 */
 	String getCompanyCode();
	 
 	/**
 	 * Gets the code.
 	 *
 	 * @return the code
 	 */
 	WLAggregateItemCode getCode();
}
