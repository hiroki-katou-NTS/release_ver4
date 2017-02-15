/******************************************************************
 * Copyright (c) 2015 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.core.dom.insurance.social.healthavgearn;

/**
 * The Interface HealthInsuranceAvgearnSetMemento.
 */
public interface HealthInsuranceAvgearnSetMemento {

	/**
	 * Sets the history id.
	 *
	 * @param historyId the new history id
	 */
	void setHistoryId(String historyId);

	/**
	 * Sets the level code.
	 *
	 * @param levelCode the new level code
	 */
	void setLevelCode(Integer levelCode);

	/**
	 * Sets the company avg.
	 *
	 * @param companyAvg the new company avg
	 */
	void setCompanyAvg(HealthInsuranceAvgearnValue companyAvg);

	/**
	 * Sets the personal avg.
	 *
	 * @param personalAvg the new personal avg
	 */
	void setPersonalAvg(HealthInsuranceAvgearnValue personalAvg);

}
