/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.at.shared.dom.scherec.totaltimes;

/**
 * The Enum SummaryAtr.
 */
// 回数集計区分
public enum SummaryAtr {

	/** The dutytype. */
	DUTYTYPE(0, "勤務種類" ,"Enum_SummaryAtr_DutyType" ),

	/** The workingtime. */
	WORKINGTIME(1,  "就業時間帯", "Enum_SummaryAtr_WorkingTime"),

	/** The combination. */
	COMBINATION(2, "組合せ" ,"Enum_SummaryAtr_Combination");

	/** The value. */
	public final int value;

	/** The name id. */
	public final String nameId;

	/** The description. */
	public final String description;

	/** The Constant values. */
	private final static SummaryAtr[] values = SummaryAtr.values();

	/**
	 * Instantiates a new summary atr.
	 *
	 * @param value
	 *            the value
	 * @param nameId
	 *            the name id
	 * @param description
	 *            the description
	 */
	private SummaryAtr(int value, String nameId, String description) {
		this.value = value;
		this.nameId = nameId;
		this.description = description;
	}

	/**
	 * Value of.
	 *
	 * @param value
	 *            the value
	 * @return the summary atr
	 */
	public static SummaryAtr valueOf(Integer value) {
		// Invalid object.
		if (value == null) {
			return null;
		}

		// Find value.
		for (SummaryAtr val : SummaryAtr.values) {
			if (val.value == value) {
				return val;
			}
		}

		// Not found.
		return null;
	}

}
