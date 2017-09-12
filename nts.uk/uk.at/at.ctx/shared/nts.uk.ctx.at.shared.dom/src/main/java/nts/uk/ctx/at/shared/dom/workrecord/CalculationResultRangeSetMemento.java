/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.at.shared.dom.workrecord;

/**
 * The Interface CalculationResultRangeSetMemento.
 */
public interface CalculationResultRangeSetMemento {

	/**
	 * Sets the upper limit.
	 *
	 * @param upper the new upper limit
	 */
	void setUpperLimit(CalculationRangeCheck upper);

	/**
	 * Sets the lower limit.
	 *
	 * @param lower the new lower limit
	 */
	void setLowerLimit(CalculationRangeCheck lower);

	/**
	 * Sets the number range.
	 *
	 * @param range the new number range
	 */
	void setNumberRange(NumberRange range);

	/**
	 * Sets the time range.
	 *
	 * @param range the new time range
	 */
	void setTimeRange(TimeRange range);

	/**
	 * Sets the amount range.
	 *
	 * @param range the new amount range
	 */
	void setAmountRange(AmountRange range);
}
