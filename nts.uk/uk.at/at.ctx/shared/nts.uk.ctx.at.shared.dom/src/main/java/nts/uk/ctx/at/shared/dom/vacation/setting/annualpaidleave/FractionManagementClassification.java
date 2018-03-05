/******************************************************************
 * Copyright (c) 2018 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.at.shared.dom.vacation.setting.annualpaidleave;

/**
 * 年休端数処理区分
 */
public enum FractionManagementClassification {
	 
	/** Truncate on day 0. */
	TruncateOnDay0(0, "1日に切り上げる", "1日に切り上げる"),

	/** Round up to the day. */
	RoundUpToTheDay(1, "0日に切り捨てる", "0日に切り捨てる"),

	/** Do not perform fractional processing. */
	FractionManagementNo(2, "端数処理をしない", "端数処理をしない");

	/** The value. */
	public int value;
	
	/** The name id. */
	public String nameId;
	
	/** The description. */
	public String description;

	/** The Constant values. */
	private final static FractionManagementClassification [] values = FractionManagementClassification.values();

	/**
	 * Instantiates a new max day reference.
	 *
	 * @param value the value
	 * @param nameId the name id
	 * @param description the description
	 */
	private FractionManagementClassification(int value, String nameId, String description) {
		this.value = value;
		this.nameId = nameId;
		this.description = description;
	}

	/**
	 * Value of.
	 *
	 * @param value the value
	 * @return the max day reference
	 */
	public static FractionManagementClassification valueOf(Integer value) {
		// Invalid object.
		if (value == null) {
			return null;
		}

		// Find value.
		for (FractionManagementClassification val : FractionManagementClassification.values) {
			if (val.value == value) {
				return val;
			}
		}
		// Not found.
		return null;
	}
}
