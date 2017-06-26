/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.core.dom.insurance.social.pensionavgearn;

import nts.uk.ctx.pr.core.dom.insurance.CommonAmount;

/**
 * The Interface PensionAvgearnGetMemento.
 */
public interface PensionAvgearnGetMemento {

	/**
	 * Gets the history id.
	 *
	 * @return the history id
	 */
	String getHistoryId();

	/**
	 * Gets the grade.
	 *
	 * @return the grade
	 */
	Integer getGrade();

	/**
 	 * Gets the avg earn.
 	 *
 	 * @return the avg earn
 	 */
 	Long getAvgEarn();

	/**
 	 * Gets the upper limit.
 	 *
 	 * @return the upper limit
 	 */
 	Long getUpperLimit();

	/**
	 * Gets the child contribution amount.
	 *
	 * @return the child contribution amount
	 */
	CommonAmount getChildContributionAmount();

	/**
	 * Gets the company fund.
	 *
	 * @return the company fund
	 */
	PensionAvgearnValue getCompanyFund();

	/**
	 * Gets the company fund exemption.
	 *
	 * @return the company fund exemption
	 */
	PensionAvgearnValue getCompanyFundExemption();

	/**
	 * Gets the company pension.
	 *
	 * @return the company pension
	 */
	PensionAvgearnValue getCompanyPension();

	/**
	 * Gets the personal fund.
	 *
	 * @return the personal fund
	 */
	PensionAvgearnValue getPersonalFund();

	/**
	 * Gets the personal fund exemption.
	 *
	 * @return the personal fund exemption
	 */
	PensionAvgearnValue getPersonalFundExemption();

	/**
	 * Gets the personal pension.
	 *
	 * @return the personal pension
	 */
	PensionAvgearnValue getPersonalPension();

}
