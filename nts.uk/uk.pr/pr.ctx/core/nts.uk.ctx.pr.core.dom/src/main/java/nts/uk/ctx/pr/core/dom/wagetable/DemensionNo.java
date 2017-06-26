/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/

package nts.uk.ctx.pr.core.dom.wagetable;

/**
 * The Enum DemensionNo.
 */
public enum DemensionNo {

	/** The demension 1st. */
	DEMENSION_1ST(1),

	/** The demension 2nd. */
	DEMENSION_2ND(2),

	/** The demension 3rd. */
	DEMENSION_3RD(3);

	/** The value. */
	public int value;

	/** The Constant values. */
	private final static DemensionNo[] values = DemensionNo.values();

	/**
	 * Instantiates a new demension no.
	 *
	 * @param value
	 *            the value
	 */
	private DemensionNo(int value) {
		this.value = value;
	}

	/**
	 * Value of.
	 *
	 * @param value
	 *            the value
	 * @return the demension no
	 */
	public static DemensionNo valueOf(Integer value) {
		// Invalid object.
		if (value == null) {
			return null;
		}

		// Find value.
		for (DemensionNo val : DemensionNo.values) {
			if (val.value == value) {
				return val;
			}
		}

		// Not found.
		return null;
	}
}
