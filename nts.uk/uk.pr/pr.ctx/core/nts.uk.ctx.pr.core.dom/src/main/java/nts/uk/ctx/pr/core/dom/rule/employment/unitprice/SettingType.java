/******************************************************************
 * Copyright (c) 2015 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/

package nts.uk.ctx.pr.core.dom.rule.employment.unitprice;

/**
 * The Enum ApplySetting.
 */
public enum SettingType {

	/** The Apply. */
	Company(0, "Company"),

	/** The Not apply. */
	Contract(1, "Contract");

	/** The value. */
	public int value;

	/** The description. */
	public String description;

	/** The Constant values. */
	private final static SettingType[] values = SettingType.values();

	/**
	 * Instantiates a new apply setting.
	 *
	 * @param value
	 *            the value
	 * @param description
	 *            the description
	 */
	private SettingType(int value, String description) {
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
	public static SettingType valueOf(Integer value) {
		// Invalid object.
		if (value == null) {
			return null;
		}

		// Find value.
		for (SettingType val : SettingType.values) {
			if (val.value == value) {
				return val;
			}
		}

		// Not found.
		return null;
	}
}
