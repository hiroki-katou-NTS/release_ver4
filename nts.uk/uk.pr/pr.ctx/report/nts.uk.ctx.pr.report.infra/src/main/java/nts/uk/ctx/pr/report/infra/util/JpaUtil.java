/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.report.infra.util;

/**
 * The Class JpaUtil.
 */
public class JpaUtil {

	/**
	 * Boolean 2 short.
	 *
	 * @param input
	 *            the input
	 * @return the short
	 */
	public static Short boolean2Short(Boolean input) {
		return (short) (input ? 1 : 0);
	}

	/**
	 * Short 2 boolean.
	 *
	 * @param input
	 *            the input
	 * @return the boolean
	 */
	public static Boolean short2Boolean(Short input) {
		return input.intValue() == 1 ? true : false;
	}
}
