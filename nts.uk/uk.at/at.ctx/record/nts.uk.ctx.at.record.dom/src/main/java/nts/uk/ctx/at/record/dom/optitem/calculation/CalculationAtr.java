/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.at.record.dom.optitem.calculation;

/**
 * The Enum CalculationClassification.
 */
// 計算区分
public enum CalculationAtr {

	/** The item selection. */
	// 計算項目選択
	ITEM_SELECTION(0, "KMK002_43", "計算項目選択"),

	/** The formula setting. */
	// 計算式設定
	FORMULA_SETTING(1, "KMK002_42", "計算式設定");

	/** The value. */
	public int value;

	/** The name id. */
	public String nameId;

	/** The description. */
	public String description;

	/** The Constant values. */
	private final static CalculationAtr[] values = CalculationAtr.values();

	/**
	 * Instantiates a new calculation atr.
	 *
	 * @param value the value
	 * @param nameId the name id
	 * @param description the description
	 */
	private CalculationAtr(int value, String nameId, String description) {
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
	public static CalculationAtr valueOf(Integer value) {
		// Invalid object.
		if (value == null) {
			return null;
		}

		// Find value.
		for (CalculationAtr val : CalculationAtr.values) {
			if (val.value == value) {
				return val;
			}
		}

		// Not found.
		return null;
	}
}
