/******************************************************************
 * Copyright (c) 2016 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.at.shared.dom.vacation.setting.compensatoryleave;

/**
 * The Enum CompensatoryOccurrenceDivision.
 */
// 代休発生元区分
public enum CompensatoryOccurrenceDivision {
	
	/** The From over time. */
	FromOverTime(0,"代休の発生に必要な残業時間","代休の発生に必要な残業時間"),
	
	/** The Work day off time. */
	WorkDayOffTime(1,"代休の発生に必要な休日出勤時間","代休の発生に必要な休日出勤時間");
	
	/** The value. */
	public int value;

	/** The name id. */
	public String nameId;

	/** The description. */
	public String description;

	/** The Constant values. */
	private final static CompensatoryOccurrenceDivision[] values = CompensatoryOccurrenceDivision.values();

	/**
	 * Instantiates a new manage distinct.
	 *
	 * @param value
	 *            the value
	 * @param description
	 *            the description
	 */
	private CompensatoryOccurrenceDivision(int value, String nameId, String description) {
		this.value = value;
		this.nameId = nameId;
		this.description = description;
	}

	/**
	 * Value of.
	 *
	 * @param value
	 *            the value
	 * @return the manage distinct
	 */
	public static CompensatoryOccurrenceDivision valueOf(Integer value) {
		// Invalid object.
		if (value == null) {
			return null;
		}

		// Find value.
		for (CompensatoryOccurrenceDivision val : CompensatoryOccurrenceDivision.values) {
			if (val.value == value) {
				return val;
			}
		}

		// Not found.
		return null;
	}
	
	/**
	 * 残業であるか判定する
	 * @return　残業である
	 */
	public boolean isOverTime() {
		return FromOverTime.equals(this);
	}
}
