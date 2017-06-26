/******************************************************************
 * Copyright (c) 2015 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.core.dom.insurance.labor.accidentrate;

import java.util.Set;

import nts.uk.ctx.pr.core.dom.insurance.MonthRange;

/**
 * The Interface AccidentInsuranceRateMemento.
 */
public interface AccidentInsuranceRateSetMemento {

	/**
	 * Sets the history id.
	 *
	 * @param historyId the new history id
	 */
	void setHistoryId(String historyId);

	/**
	 * Sets the company code.
	 *
	 * @param companyCode the new company code
	 */
	void setCompanyCode(String companyCode);

	/**
	 * Sets the apply range.
	 *
	 * @param applyRange the new apply range
	 */
	void setApplyRange(MonthRange applyRange);

	/**
	 * Sets the rate items.
	 *
	 * @param items the new rate items
	 */
	void setRateItems(Set<InsuBizRateItem> items);

}
