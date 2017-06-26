/******************************************************************
 * Copyright (c) 2015 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/

package nts.uk.ctx.pr.core.dom.insurance;

/**
 * The Enum RoundingMethod.
 */
public enum CalculateMethod {

	/** The Round up. */
	Auto(0),

	/** The Truncation. */
	Manual(1);

	/** The value. */
	public int value;

	/** The Constant values. */
	private final static CalculateMethod[] values = CalculateMethod.values();

	/**
	 * Instantiates a new rounding method.
	 *
	 * @param value
	 *            the value
	 * @param nameId
	 *            the name id
	 * @param description
	 *            the description
	 */
	private CalculateMethod(int value) {
		this.value = value;
	}

	/**
	 * Value of.
	 *
	 * @param value
	 *            the value
	 * @return the rounding method
	 */
	public static CalculateMethod valueOf(Integer value) {
		// Invalid object.
		if (value == null) {
			return null;
		}

		// Find value.
		for (CalculateMethod val : CalculateMethod.values) {
			if (val.value == value) {
				return val;
			}
		}
		// Not found.
		return null;
	}
}
