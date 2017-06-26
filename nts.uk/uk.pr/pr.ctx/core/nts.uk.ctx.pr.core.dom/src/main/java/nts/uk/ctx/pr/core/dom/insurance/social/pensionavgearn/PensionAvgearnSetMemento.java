/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.core.dom.insurance.social.pensionavgearn;

import nts.uk.ctx.pr.core.dom.insurance.CommonAmount;

/**
 * The Interface PensionRateMemento.
 */
public interface PensionAvgearnSetMemento {

	/**
	 * Sets the history id.
	 *
	 * @param historyId the new history id
	 */
	void setHistoryId(String historyId);

	/**
	 * Gets the level code.
	 *
	 * @return the level code
	 */
	void setGrade(Integer grade);
	
	/**
 	 * Gets the avg earn.
 	 *
 	 * @return the avg earn
 	 */
 	void setAvgEarn(Long avgEarn);

	/**
 	 * Gets the upper limit.
 	 *
 	 * @return the upper limit
 	 */
 	void setUpperLimit(Long upperLimit);

	/**
	 * Gets the child contribution amount.
	 *
	 * @return the child contribution amount
	 */
	void setChildContributionAmount(CommonAmount childContributionAmount);

	/**
	 * Gets the company fund.
	 *
	 * @return the company fund
	 */
	void setCompanyFund(PensionAvgearnValue companyFund);

	/**
	 * Gets the company fund exemption.
	 *
	 * @return the company fund exemption
	 */
	void setCompanyFundExemption(PensionAvgearnValue companyFundExemption);

	/**
	 * Gets the company pension.
	 *
	 * @return the company pension
	 */
	void setCompanyPension(PensionAvgearnValue companyPension);

	/**
	 * Gets the personal fund.
	 *
	 * @return the personal fund
	 */
	void setPersonalFund(PensionAvgearnValue personalFund);

	/**
	 * Gets the personal fund exemption.
	 *
	 * @return the personal fund exemption
	 */
	void setPersonalFundExemption(PensionAvgearnValue personalFundExemption);

	/**
	 * Gets the personal pension.
	 *
	 * @return the personal pension
	 */
	void setPersonalPension(PensionAvgearnValue personalPension);

}
