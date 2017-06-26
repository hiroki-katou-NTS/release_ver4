/******************************************************************
 * Copyright (c) 2015 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/

package nts.uk.ctx.pr.core.dom.insurance.social.pensionrate;

/**
 * The Enum InsuranceGender.
 */
public enum InsuranceGender {

	/** The Male. */
	Male(1, "Male"),

	/** The Female. */
	Female(2, "Female"),

	/** The Unknow. */
	Unknow(3, "Unknow");

	/** The value. */
	public int value;

	/** The description. */
	public String description;

	/** The Constant values. */
	private final static InsuranceGender[] values = InsuranceGender.values();

	/**
	 * Instantiates a new insurance gender.
	 *
	 * @param value
	 *            the value
	 * @param description
	 *            the description
	 */
	private InsuranceGender(int value, String description) {
		this.value = value;
		this.description = description;
	}

	/**
	 * Value of.
	 *
	 * @param value
	 *            the value
	 * @return the business type enum
	 */
	public static InsuranceGender valueOf(Integer value) {
		// Invalid object.
		if (value == null) {
			return null;
		}

		// Find value.
		for (InsuranceGender val : InsuranceGender.values) {
			if (val.value == value) {
				return val;
			}
		}

		// Not found.
		return null;
	}
}
