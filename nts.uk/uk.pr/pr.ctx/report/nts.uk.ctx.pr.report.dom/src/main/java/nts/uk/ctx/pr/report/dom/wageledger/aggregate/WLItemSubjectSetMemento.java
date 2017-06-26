/******************************************************************
 * Copyright (c) 2015 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.report.dom.wageledger.aggregate;

import nts.uk.ctx.pr.report.dom.wageledger.PaymentType;
import nts.uk.ctx.pr.report.dom.wageledger.WLCategory;

/**
 * The Interface WLItemSubjectSetMemento.
 */
public interface WLItemSubjectSetMemento {
	 
 	/**
 	 * Sets the cateory.
 	 *
 	 * @param category the new cateory
 	 */
 	void setCateory(WLCategory category);
	
	 /**
 	 * Sets the payment type.
 	 *
 	 * @param paymentType the new payment type
 	 */
 	void setPaymentType(PaymentType paymentType);
	
	 /**
 	 * Sets the company code.
 	 *
 	 * @param companyCode the new company code
 	 */
 	void setCompanyCode(String companyCode);
	 
 	/**
 	 * Sets the code.
 	 *
 	 * @param code the new code
 	 */
 	void setCode(WLAggregateItemCode code);
}
