/******************************************************************
 * Copyright (c) 2015 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/

package nts.uk.ctx.pr.core.dom.insurance;

/**
 * The Enum FundInputApply.
 */
public enum FundInputApply {

	/** The No. */
	No(0),

	/** The Yes. */
	Yes(1);

	/** The value. */
	public int value;

	/** The Constant values. */
	private final static FundInputApply[] values = FundInputApply.values();

	/**
	 * Instantiates a new fund input apply.
	 *
	 * @param value the value
	 */
	private FundInputApply(int value) {
		this.value = value;
	}

	/**
	 * Value of.
	 *
	 * @param value the value
	 * @return the fund input apply
	 */
	public static FundInputApply valueOf(Integer value) {
		// Invalid object.
		if (value == null) {
			return null;
		}

		// Find value.
		for (FundInputApply val : FundInputApply.values) {
			if (val.value == value) {
				return val;
			}
		}
		// Not found.
		return null;
	}
}
