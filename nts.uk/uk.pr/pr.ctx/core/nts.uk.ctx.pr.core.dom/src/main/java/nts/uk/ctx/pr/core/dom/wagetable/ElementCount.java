/******************************************************************
 * Copyright (c) 2015 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/

package nts.uk.ctx.pr.core.dom.wagetable;

/**
 * The Enum MultipleTargetSetting.
 */
public enum ElementCount {

	/** The Bigest method. */
	One(0),

	/** The Total method. */
	Two(1),

	/** The Three. */
	Three(2),

	/** The Eligibility. */
	Qualification(3),

	/** The Finework. */
	Finework(4);

	/** The value. */
	public int value;

	/** The Constant values. */
	private final static ElementCount[] values = ElementCount.values();

	/**
	 * Instantiates a new multiple target setting.
	 *
	 * @param value
	 *            the value
	 * @param description
	 *            the description
	 */
	private ElementCount(int value) {
		this.value = value;
	}

	/**
	 * Value of.
	 *
	 * @param value
	 *            the value
	 * @return the multiple target setting
	 */
	public static ElementCount valueOf(Integer value) {
		// Invalid object.
		if (value == null) {
			return null;
		}

		// Find value.
		for (ElementCount val : ElementCount.values) {
			if (val.value == value) {
				return val;
			}
		}

		// Not found.
		return null;
	}
}
