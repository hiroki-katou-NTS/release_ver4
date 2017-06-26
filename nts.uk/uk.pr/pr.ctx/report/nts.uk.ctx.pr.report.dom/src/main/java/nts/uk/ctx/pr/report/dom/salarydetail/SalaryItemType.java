/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.report.dom.salarydetail;

/**
 * The Enum SalaryItemType.
 */
public enum SalaryItemType {

	/** The Master. */
	Master(0),

	/** The Aggregate. */
	Aggregate(1);

	/** The value. */
	public final Integer value;

	/**
	 * Instantiates a new salary item type.
	 *
	 * @param value the value
	 */
	private SalaryItemType(Integer value) {
		this.value = value;
	}

	/**
	 * Value of.
	 *
	 * @param value the value
	 * @return the salary item type
	 */
	public static SalaryItemType valueOf(Integer value) {
		// Invalid object.
		if (value == null) {
			return null;
		}

		// Find value.
		for (SalaryItemType val : SalaryItemType.values()) {
			if (val.value == value) {
				return val;
			}
		}

		// Not found.
		return null;
	}
}
