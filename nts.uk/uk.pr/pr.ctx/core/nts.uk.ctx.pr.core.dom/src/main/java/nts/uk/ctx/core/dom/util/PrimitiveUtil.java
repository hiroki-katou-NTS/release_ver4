/******************************************************************
 * Copyright (c) 2016 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.core.dom.util;

import nts.arc.time.YearMonth;

/**
 * The Class PrimitiveUtil.
 */
public class PrimitiveUtil {
	
	public static final String DEFAULT_YM_SEPARATOR_CHAR = "/";
	
	/**
	 * To year month.
	 *
	 * @param ymStr the ym str
	 * @param separatorChar the separator char
	 * @return the year month
	 */
	public static YearMonth toYearMonth(String ymStr, String separatorChar) {
		String outmonthyear[] = ymStr.split(separatorChar);
		return YearMonth.of(Integer.parseInt(outmonthyear[0]), Integer.parseInt(outmonthyear[1]));
	}
}
