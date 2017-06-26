/******************************************************************
 * Copyright (c) 2015 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/

package nts.uk.ctx.pr.core.dom.rule.employment.unitprice;

/**
 * The Enum ApplySetting.
 */
public enum ApplySetting {

	/** The Apply. */
	Apply(1, "Apply"),

	/** The Not apply. */
	NotApply(0, "NotApply");

	/** The value. */
	public int value;

	/** The description. */
	public String description;

	/** The Constant values. */
	private final static ApplySetting[] values = ApplySetting.values();

	/**
	 * Instantiates a new apply setting.
	 *
	 * @param value
	 *            the value
	 * @param description
	 *            the description
	 */
	private ApplySetting(int value, String description) {
		this.value = value;
		this.description = description;
	}

	/**
	 * Value of.
	 *
	 * @param value
	 *            the value
	 * @return the apply setting
	 */
	public static ApplySetting valueOf(Integer value) {
		// Invalid object.
		if (value == null) {
			return null;
		}

		// Find value.
		for (ApplySetting val : ApplySetting.values) {
			if (val.value == value) {
				return val;
			}
		}

		// Not found.
		return null;
	}
}
