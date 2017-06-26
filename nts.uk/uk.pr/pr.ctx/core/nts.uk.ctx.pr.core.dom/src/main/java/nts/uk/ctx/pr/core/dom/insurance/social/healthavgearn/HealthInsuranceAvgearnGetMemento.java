/******************************************************************
 * Copyright (c) 2015 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.core.dom.insurance.social.healthavgearn;

/**
 * The Interface HealthInsuranceAvgearnGetMemento.
 */
public interface HealthInsuranceAvgearnGetMemento {

	/**
	 * Gets the history id.
	 *
	 * @return the history id
	 */
	String getHistoryId();

	/**
	 * Gets the level code.
	 *
	 * @return the level code
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
	 * Gets the company avg.
	 *
	 * @return the company avg
	 */
	HealthInsuranceAvgearnValue  getCompanyAvg();

	/**
	 * Gets the personal avg.
	 *
	 * @return the personal avg
	 */
	HealthInsuranceAvgearnValue  getPersonalAvg();

}
