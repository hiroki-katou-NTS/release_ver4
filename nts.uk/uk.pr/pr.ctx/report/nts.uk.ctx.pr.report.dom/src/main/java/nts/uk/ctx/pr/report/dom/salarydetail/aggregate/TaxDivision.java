/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.report.dom.salarydetail.aggregate;

/**
 * The Enum TaxDivision.
 */
public enum TaxDivision {

	/** The Payment. */
	Payment(0),

	/** The Deduction. */
	Deduction(1);

	/** The value. */
	public final Integer value;

	/**
	 * Instantiates a new tax division.
	 *
	 * @param value the value
	 */
	private TaxDivision(Integer value) {
		this.value = value;
	}

	/**
	 * Value of.
	 *
	 * @param value the value
	 * @return the tax division
	 */
	public static TaxDivision valueOf(Integer value) {
		// Invalid object.
		if (value == null) {
			return null;
		}

		// Find value.
		for (TaxDivision val : TaxDivision.values()) {
			if (val.value == value) {
				return val;
			}
		}

		// Not found.
		return null;
	}
}
