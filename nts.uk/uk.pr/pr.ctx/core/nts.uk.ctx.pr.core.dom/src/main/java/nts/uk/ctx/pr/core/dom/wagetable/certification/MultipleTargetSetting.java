/******************************************************************
 * Copyright (c) 2015 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/

package nts.uk.ctx.pr.core.dom.wagetable.certification;

/**
 * The Enum MultipleTargetSetting.
 */
public enum MultipleTargetSetting {

	/** The Bigest method. */
	BigestMethod(0, "BigestAmount"),

	/** The Total method. */
	TotalMethod(1, "TotalAmount");

	/** The value. */
	public int value;

	/** The description. */
	public String description;

	/** The Constant values. */
	private final static MultipleTargetSetting[] values = MultipleTargetSetting.values();

	/**
	 * Instantiates a new multiple target setting.
	 *
	 * @param value
	 *            the value
	 * @param description
	 *            the description
	 */
	private MultipleTargetSetting(int value, String description) {
		this.value = value;
		this.description = description;
	}

	/**
	 * Value of.
	 *
	 * @param value
	 *            the value
	 * @return the multiple target setting
	 */
	public static MultipleTargetSetting valueOf(Integer value) {
		// Invalid object.
		if (value == null) {
			return null;
		}

		// Find value.
		for (MultipleTargetSetting val : MultipleTargetSetting.values) {
			if (val.value == value) {
				return val;
			}
		}

		// Not found.
		return null;
	}
}
