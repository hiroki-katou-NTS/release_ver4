/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.core.dom.base.simplehistory;

import nts.arc.primitive.PrimitiveValue;
import nts.arc.time.YearMonth;

/**
 * The Interface History.
 */
public interface History<T> {
	/**
	 * Get uuid.
	 * @return
	 */
	String getUuid();

	/**
	 * Gets the master code.
	 *
	 * @return the master code
	 */
	PrimitiveValue<String> getMasterCode();

	/**
	 * Gets the company code.
	 *
	 * @return the company code
	 */
	String getCompanyCode();

	/**
	 * Gets the start.
	 *
	 * @return the start
	 */
	YearMonth getStart();

	/**
	 * Gets the end.
	 *
	 * @return the end
	 */
	YearMonth getEnd();

	/**
	 * Sets the start.
	 *
	 * @param yearMonth the new start
	 */
	void setStart(YearMonth yearMonth);

	/**
	 * Sets the end.
	 *
	 * @param yearMonth the new end
	 */
	void setEnd(YearMonth yearMonth);

	/**
	 * Copy with date.
	 *
	 * @param start the start
	 * @return the t
	 */
	T copyWithDate(YearMonth start);
}
