/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.core.dom.insurance.social.pensionavgearn.limit;

/**
 * The Interface PensionAvgEarnLimitSetMemento.
 */
public interface PensionAvgEarnLimitSetMemento {

	/**
	 * Sets the grade.
	 *
	 * @param grade the new grade
	 */
	void setGrade(Integer grade);

	/**
	 * Sets the avg earn.
	 *
	 * @param avgEarn the new avg earn
	 */
	void setAvgEarn(Long avgEarn);

	/**
	 * Sets the sal limit.
	 *
	 * @param salLimit the new sal limit
	 */
	void setSalLimit(Long salLimit);

}
