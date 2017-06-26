/******************************************************************
 * Copyright (c) 2015 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/

package nts.uk.ctx.pr.core.dom.insurance.social.healthrate;

/**
 * The Enum HealthInsuranceType.
 */
public enum HealthInsuranceType {

	/** The Basic. */
	Basic(1, "Basic"),

	/** The Nursing. */
	Nursing(2, "Nursing"),

	/** The Special. */
	Special(3, "Special"),

	/** The General. */
	General(4, "General");

	/** The value. */
	public int value;

	/** The description. */
	public String description;

	/** The Constant values. */
	private final static HealthInsuranceType[] values = HealthInsuranceType.values();

	/**
	 * Instantiates a new health insurance type.
	 *
	 * @param value
	 *            the value
	 * @param description
	 *            the description
	 */
	private HealthInsuranceType(int value, String description) {
		this.value = value;
		this.description = description;
	}

	/**
	 * Value of.
	 *
	 * @param value
	 *            the value
	 * @return the health insurance type
	 */
	public static HealthInsuranceType valueOf(Integer value) {
		// Invalid object.
		if (value == null) {
			return null;
		}

		// Find value.
		for (HealthInsuranceType val : HealthInsuranceType.values) {
			if (val.value == value) {
				return val;
			}
		}

		// Not found.
		return null;
	}
}
