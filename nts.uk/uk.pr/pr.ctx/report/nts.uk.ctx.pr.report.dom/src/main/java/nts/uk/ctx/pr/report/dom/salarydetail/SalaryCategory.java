/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.report.dom.salarydetail;

/**
 * The Enum SalaryCategory.
 */
public enum SalaryCategory {

	/** The Payment. */
	Payment(0),

	/** The Deduction. */
	Deduction(1),

	/** The Attendance. */
	Attendance(2),

	/** The Article others. */
	ArticleOthers(3);

	/** The value. */
	public final Integer value;

	/**
	 * Instantiates a new salary category.
	 *
	 * @param value the value
	 */
	private SalaryCategory(Integer value) {
		this.value = value;
	}

	/**
	 * Value of.
	 *
	 * @param value the value
	 * @return the salary category
	 */
	public static SalaryCategory valueOf(Integer value) {
		// Invalid object.
		if (value == null) {
			return null;
		}

		// Find value.
		for (SalaryCategory val : SalaryCategory.values()) {
			if (val.value == value) {
				return val;
			}
		}

		// Not found.
		return null;
	}
}
