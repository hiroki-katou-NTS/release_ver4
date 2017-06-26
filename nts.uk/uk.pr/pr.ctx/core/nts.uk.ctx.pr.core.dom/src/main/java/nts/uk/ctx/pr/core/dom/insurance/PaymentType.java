/******************************************************************
 * Copyright (c) 2015 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/

package nts.uk.ctx.pr.core.dom.insurance;

/**
 * The Enum PaymentType.
 */
public enum PaymentType {

	/** The Salary. */
	Salary(0, "Salary"),

	/** The Bonus. */
	Bonus(1, "Bonus");

	/** The value. */
	public int value;

	/** The description. */
	public String description;

	/** The Constant values. */
	private final static PaymentType[] values = PaymentType.values();

	/**
	 * Instantiates a new payment type.
	 *
	 * @param value the value
	 * @param description the description
	 */
	private PaymentType(int value, String description) {
		this.value = value;
		this.description = description;
	}

	/**
	 * Value of.
	 *
	 * @param value the value
	 * @return the payment type
	 */
	public static PaymentType valueOf(Integer value) {
		// Invalid object.
		if (value == null) {
			return null;
		}

		// Find value.
		for (PaymentType val : PaymentType.values) {
			if (val.value == value) {
				return val;
			}
		}

		// Not found.
		return null;
	}
}
