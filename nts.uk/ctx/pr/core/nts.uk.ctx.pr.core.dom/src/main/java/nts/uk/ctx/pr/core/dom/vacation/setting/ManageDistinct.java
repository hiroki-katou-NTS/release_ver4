/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.core.dom.vacation.setting;

	/**
 * The Enum ManageDistinct.
 */
public enum ManageDistinct {

	/** The yes. */
	YES(1, "管理する"),

	/** The no. */
	NO(0, "管理しない");

	/** The value. */
	public int value;

	/** The description. */
	public String description;

	/** The Constant values. */
	private final static ManageDistinct[] values = ManageDistinct.values();

	/**
	 * Instantiates a new manage distinct.
	 *
	 * @param value
	 *            the value
	 * @param description
	 *            the description
	 */
	private ManageDistinct(int value, String description) {
		this.value = value;
		this.description = description;
	}

	/**
	 * Value of.
	 *
	 * @param value
	 *            the value
	 * @return the manage distinct
	 */
	public static ManageDistinct valueOf(Integer value) {
		// Invalid object.
		if (value == null) {
			return null;
		}

		// Find value.
		for (ManageDistinct val : ManageDistinct.values) {
			if (val.value == value) {
				return val;
			}
		}

		// Not found.
		return null;
	}
}
