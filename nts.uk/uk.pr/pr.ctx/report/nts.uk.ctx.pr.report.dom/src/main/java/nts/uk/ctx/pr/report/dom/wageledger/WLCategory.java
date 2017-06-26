/******************************************************************
 * Copyright (c) 2015 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.report.dom.wageledger;

/**
 * The Enum Category.
 */
public enum WLCategory {
	
	/** The Salary payment. */
	Payment(0),
	
	/** The Salary deduction. */
	Deduction(1),
	
	/** The Salary attendance. */
	Attendance(2);
	
	/** The value. */
	public final int value;

	/**
	 * Instantiates a new category.
	 *
	 * @param value the value
	 * @param description the description
	 */
	private WLCategory(int value) {
		this.value = value;
	}
	
	/**
	 * Value of.
	 *
	 * @param value
	 *            the value
	 * @return the element type
	 */
	public static WLCategory valueOf(Integer value) {
		// Invalid object.
		if (value == null) {
			return null;
		}

		// Find value.
		for (WLCategory val : WLCategory.values()) {
			if (val.value == value) {
				return val;
			}
		}

		// Not found.
		return null;
	}
}
