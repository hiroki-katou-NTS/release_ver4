/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.at.shared.dom.worktime.common;

/**
 * The Enum SubHolTransferSetAtr.
 */
// 代休振替設定区分
public enum SubHolTransferSetAtr {

	/** The specified time sub hol. */
	// 指定した時間を代休とする
	SPECIFIED_TIME_SUB_HOL(0, "Enum_SubHolTransferSetAtr_specifiedTimeSubHol", "指定した時間を代休とする"),

	/** The certain time exc sub hol. */
	// 一定時間を超えたら代休とする
	CERTAIN_TIME_EXC_SUB_HOL(1, "Enum_SubHolTransferSetAtr_certainTimeExcSubHol", "一定時間を超えたら代休とする");

	/** The value. */
	public final int value;

	/** The name id. */
	public final String nameId;

	/** The description. */
	public final String description;

	/** The Constant values. */
	private final static SubHolTransferSetAtr[] values = SubHolTransferSetAtr.values();

	/**
	 * Instantiates a new sub hol transfer set atr.
	 *
	 * @param value the value
	 * @param nameId the name id
	 * @param description the description
	 */
	private SubHolTransferSetAtr(int value, String nameId, String description) {
		this.value = value;
		this.nameId = nameId;
		this.description = description;
	}

	/**
	 * Value of.
	 *
	 * @param value the value
	 * @return the sub hol transfer set atr
	 */
	public static SubHolTransferSetAtr valueOf(Integer value) {
		// Invalid object.
		if (value == null) {
			return null;
		}

		// Find value.
		for (SubHolTransferSetAtr val : SubHolTransferSetAtr.values) {
			if (val.value == value) {
				return val;
			}
		}

		// Not found.
		return null;
	}
	
	/**
	 * 指定した時間を代休とするか判定する
	 * @return　指定した時間を代休とする
	 */
	public boolean isSpecifiedTimeSubHol() {
		return this.equals(SPECIFIED_TIME_SUB_HOL);
	}
}
