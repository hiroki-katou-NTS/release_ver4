/******************************************************************
 * Copyright (c) 2015 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.core.dom.insurance.labor.unemployeerate;

import java.util.Set;

import nts.uk.ctx.pr.core.dom.insurance.MonthRange;

/**
 * The Interface UnemployeeInsuranceRateMemento.
 */
public interface UnemployeeInsuranceRateGetMemento {

	/**
	 * Gets the history id.
	 *
	 * @return the history id
	 */
	String getHistoryId();

	/**
	 * Gets the company code.
	 *
	 * @return the company code
	 */
	String getCompanyCode();

	/**
	 * Gets the apply range.
	 *
	 * @return the apply range
	 */
	MonthRange getApplyRange();

	/**
	 * Gets the rate items.
	 *
	 * @return the rate items
	 */
	Set<UnemployeeInsuranceRateItem> getRateItems();

}
