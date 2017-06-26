/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.core.dom.wagetable.history.service;

import nts.uk.ctx.pr.core.dom.base.simplehistory.SimpleHistoryBaseService;
import nts.uk.ctx.pr.core.dom.wagetable.WtHead;
import nts.uk.ctx.pr.core.dom.wagetable.history.WtHistory;

/**
 * The Class WageTableHistoryService.
 */
public abstract class WtHistoryService
		extends SimpleHistoryBaseService<WtHead, WtHistory> {

	/**
	 * Validate required item.
	 *
	 * @param unitPriceHistory the unit price history
	 */
	public abstract void validateRequiredItem(WtHistory unitPriceHistory);

	/**
	 * Validate date range.
	 *
	 * @param unitPriceHistory the unit price history
	 */
	public abstract void validateDateRange(WtHistory unitPriceHistory);

}
