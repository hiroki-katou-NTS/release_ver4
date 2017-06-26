/******************************************************************
 * Copyright (c) 2016 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.core.dom.rule.employment.unitprice.service;

import nts.uk.ctx.pr.core.dom.rule.employment.unitprice.UnitPrice;

/**
 * The Interface UnitPriceService.
 */
public interface UnitPriceService {

	/**
	 * Validate required item.
	 *
	 * @param unitPrice the unit price
	 */
	void validateRequiredItem(UnitPrice unitPrice);

	/**
	 * Check duplicate code.
	 *
	 * @param unitPrice the unit price
	 */
	void checkDuplicateCode(UnitPrice unitPrice);
}
