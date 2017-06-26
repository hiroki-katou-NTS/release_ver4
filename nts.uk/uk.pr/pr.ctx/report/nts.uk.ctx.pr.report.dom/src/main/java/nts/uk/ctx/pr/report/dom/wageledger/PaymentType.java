/******************************************************************
 * Copyright (c) 2015 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.report.dom.wageledger;

/**
 * The Enum PaymentType.
 */
public enum PaymentType {
	
	/** The Salary. */
	Salary(0),
	
	/** The Bonus. */
	Bonus(1);
	
	/** The value. */
	public final int value;
	
	/**
	 * Instantiates a new payment type.
	 *
	 * @param value the value
	 */
	private PaymentType(int value) {
		this.value = value;
	}
	
	/**
	 * Value of.
	 *
	 * @param value
	 *            the value
	 * @return the element type
	 */
	public static PaymentType valueOf(Integer value) {
		// Invalid object.
		if (value == null) {
			return null;
		}

		// Find value.
		for (PaymentType val : PaymentType.values()) {
			if (val.value == value) {
				return val;
			}
		}

		// Not found.
		return null;
	}
}
