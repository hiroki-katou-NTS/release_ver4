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
 * The Interface PensionRateSetMemento.
 */
public interface PensionRateSetMemento {

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
	 * Sets the fund input apply.
	 *
	 * @param fundInputApply the new fund input apply
	 */
	void setFundInputApply(FundInputApply fundInputApply);

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
	 * Sets the fund rate items.
	 *
	 * @param fundRateItems the new fund rate items
	 */
	void setFundRateItems(Set<FundRateItem> fundRateItems);

	/**
	 * Sets the premium rate items.
	 *
	 * @param premiumRateItems the new premium rate items
	 */
	void setPremiumRateItems(Set<PensionPremiumRateItem> premiumRateItems);

	/**
	 * Sets the child contribution rate.
	 *
	 * @param childContributionRate the new child contribution rate
	 */
	void setChildContributionRate(Ins2Rate childContributionRate);

	/**
	 * Sets the rounding methods.
	 *
	 * @param roundingMethods the new rounding methods
	 */
	void setRoundingMethods(Set<PensionRateRounding> roundingMethods);

}
