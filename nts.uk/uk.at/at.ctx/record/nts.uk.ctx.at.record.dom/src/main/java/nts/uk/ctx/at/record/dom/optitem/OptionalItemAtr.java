/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.at.record.dom.optitem;

/**
 * The Enum OptionalItemAttribute.
 */
// 任意項目の属性
public enum OptionalItemAtr {

	/** The times. */
	// 回数
	TIMES(0, "Enum_OptionalItemAtr_TIMES", "回数"),

	/** The amount. */
	// 金額
	AMOUNT(1, "Enum_OptionalItemAtr_AMOUNT", "金額"),

	/** The time. */
	// 時間
	TIME(2, "Enum_OptionalItemAtr_TIME", "時間");

	/** The value. */
	public int value;

	/** The name id. */
	public String nameId;

	/** The description. */
	public String description;

	/** The Constant values. */
	private final static OptionalItemAtr[] values = OptionalItemAtr.values();

	/**
	 * Instantiates a new optional item atr.
	 *
	 * @param value the value
	 * @param nameId the name id
	 * @param description the description
	 */
	private OptionalItemAtr(int value, String nameId, String description) {
		this.value = value;
		this.nameId = nameId;
		this.description = description;
	}

	/**
	 * Value of.
	 *
	 * @param value the value
	 * @return the emp condition atr
	 */
	public static OptionalItemAtr valueOf(Integer value) {
		// Invalid object.
		if (value == null) {
			return null;
		}

		// Find value.
		for (OptionalItemAtr val : OptionalItemAtr.values) {
			if (val.value == value) {
				return val;
			}
		}

		// Not found.
		return null;
	}
}
