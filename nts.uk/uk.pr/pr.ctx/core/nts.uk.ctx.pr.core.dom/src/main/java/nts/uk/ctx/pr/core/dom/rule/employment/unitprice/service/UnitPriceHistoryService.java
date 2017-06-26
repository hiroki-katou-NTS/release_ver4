/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.core.dom.rule.employment.unitprice.service;

import nts.uk.ctx.pr.core.dom.base.simplehistory.SimpleHistoryBaseService;
import nts.uk.ctx.pr.core.dom.rule.employment.unitprice.UnitPrice;
import nts.uk.ctx.pr.core.dom.rule.employment.unitprice.UnitPriceHistory;

/**
 * The Interface UnitPriceHistoryService.
 */
public abstract class UnitPriceHistoryService extends SimpleHistoryBaseService<UnitPrice, UnitPriceHistory> {

	/**
	 * Validate required item.
	 *
	 * @param unitPriceHistory the unit price history
	 */
	public abstract void validateRequiredItem(UnitPriceHistory unitPriceHistory);

	/**
	 * Validate date range.
	 *
	 * @param unitPriceHistory the unit price history
	 */
	public abstract void validateDateRange(UnitPriceHistory unitPriceHistory);
}
