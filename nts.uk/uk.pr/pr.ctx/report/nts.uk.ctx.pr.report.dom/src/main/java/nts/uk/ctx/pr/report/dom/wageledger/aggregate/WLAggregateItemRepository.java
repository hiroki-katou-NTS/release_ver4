/******************************************************************
 * Copyright (c) 2015 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.report.dom.wageledger.aggregate;

import java.util.List;

import nts.uk.ctx.pr.report.dom.wageledger.PaymentType;
import nts.uk.ctx.pr.report.dom.wageledger.WLCategory;

/**
 * The Interface WageLedgerAggregateItemRepository.
 */
public interface WLAggregateItemRepository {
	
	/**
	 * Save.
	 *
	 * @param aggregateItem the aggregate item
	 */
	void create(WLAggregateItem aggregateItem);
	
	/**
	 * Update.
	 *
	 * @param aggregateItem the aggregate item
	 */
	void update(WLAggregateItem aggregateItem);
	
	/**
	 * Removes the.
	 *
	 * @param aggregateItem the aggregate item
	 */
	void remove(WLItemSubject subject);
	
	/**
	 * Find.
	 *
	 * @param code the code
	 * @param companyCode the company code
	 * @return the wage ledger aggregate item
	 */
	WLAggregateItem findByCode(WLItemSubject subject);
	
	/**
	 * Find by category and payment type.
	 *
	 * @param companyCode the company code
	 * @param category the category
	 * @param paymentType the payment type
	 * @return the list
	 */
	List<WLAggregateItem> findByCategoryAndPaymentType(String companyCode,
			WLCategory category, PaymentType paymentType);
	
	/**
	 * Find by codes.
	 *
	 * @param companyCode the company code
	 * @param itemCode the item code
	 * @return the list
	 */
	List<WLAggregateItem> findByCodes(String companyCode, List<String> itemCode);
	
	/**
	 * Checks if is exist.
	 *
	 * @param code the code
	 * @return true, if is exist
	 */
	boolean isExist(WLItemSubject subject);
	
	/**
	 * Find all.
	 *
	 * @param companyCode the company code
	 * @return the list
	 */
	default List<WLAggregateItem> findAll(String companyCode) {
		return this.findByCategoryAndPaymentType(companyCode, null, null);
	}
}
