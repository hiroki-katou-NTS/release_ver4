/******************************************************************
 * Copyright (c) 2015 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.core.dom.insurance.social.healthrate;

import java.util.Set;

import nts.uk.ctx.pr.core.dom.insurance.CalculateMethod;
import nts.uk.ctx.pr.core.dom.insurance.CommonAmount;
import nts.uk.ctx.pr.core.dom.insurance.MonthRange;
import nts.uk.ctx.pr.core.dom.insurance.OfficeCode;

/**
 * The Interface HealthInsuranceRateMemento.
 */
public interface HealthInsuranceRateSetMemento {

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
	 * Sets the office code.
	 *
	 * @param officeCode the new office code
	 */
	void setOfficeCode(OfficeCode officeCode);

	/**
	 * Sets the apply range.
	 *
	 * @param applyRange the new apply range
	 */
	void setApplyRange(MonthRange applyRange);

	/**
	 * Sets the auto calculate.
	 *
	 * @param autoCalculate the new auto calculate
	 */
	void setAutoCalculate(CalculateMethod autoCalculate);

	/**
	 * Sets the max amount.
	 *
	 * @param maxAmount the new max amount
	 */
	void setMaxAmount(CommonAmount maxAmount);

	/**
	 * Sets the rate items.
	 *
	 * @param rateItems the new rate items
	 */
	void setRateItems(Set<InsuranceRateItem> rateItems);

	/**
	 * Sets the rounding methods.
	 *
	 * @param roundingMethods the new rounding methods
	 */
	void setRoundingMethods(Set<HealthInsuranceRounding> roundingMethods);

}
