/******************************************************************
 * Copyright (c) 2015 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.core.dom.insurance.social.pensionrate;

import java.util.Set;

import nts.uk.ctx.pr.core.dom.insurance.CalculateMethod;
import nts.uk.ctx.pr.core.dom.insurance.CommonAmount;
import nts.uk.ctx.pr.core.dom.insurance.FundInputApply;
import nts.uk.ctx.pr.core.dom.insurance.Ins2Rate;
import nts.uk.ctx.pr.core.dom.insurance.MonthRange;
import nts.uk.ctx.pr.core.dom.insurance.OfficeCode;

/**
 * The Interface PensionRateGetMemento.
 */
public interface PensionRateGetMemento {

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
	 * Gets the office code.
	 *
	 * @return the office code
	 */
	OfficeCode getOfficeCode();

	/**
	 * Gets the apply range.
	 *
	 * @return the apply range
	 */
	MonthRange getApplyRange();

	/**
	 * Gets the fund input apply.
	 *
	 * @return the fund input apply
	 */
	FundInputApply getFundInputApply();

	/**
	 * Gets the auto calculate.
	 *
	 * @return the auto calculate
	 */
	CalculateMethod getAutoCalculate();

	/**
	 * Gets the max amount.
	 *
	 * @return the max amount
	 */
	CommonAmount getMaxAmount();

	/**
	 * Gets the fund rate items.
	 *
	 * @return the fund rate items
	 */
	Set<FundRateItem> getFundRateItems();

	/**
	 * Gets the premium rate items.
	 *
	 * @return the premium rate items
	 */
	Set<PensionPremiumRateItem> getPremiumRateItems();

	/**
	 * Gets the child contribution rate.
	 *
	 * @return the child contribution rate
	 */
	Ins2Rate getChildContributionRate();

	/**
	 * Gets the rounding methods.
	 *
	 * @return the rounding methods
	 */
	Set<PensionRateRounding> getRoundingMethods();

}
