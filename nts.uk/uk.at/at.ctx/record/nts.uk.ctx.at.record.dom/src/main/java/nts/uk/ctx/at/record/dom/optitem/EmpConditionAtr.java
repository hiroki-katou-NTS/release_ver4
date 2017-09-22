/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.at.record.dom.optitem;

/**
 * The Enum EmpConditionAtr.
 */
// 雇用条件区分
public enum EmpConditionAtr {

	/** The no condition. */
	// 条件なし
	NO_CONDITION(0, "Enum_EmpConditionAtr_NO_CONDITION", "条件なし"),

	/** The with condition. */
	// 条件あり
	WITH_CONDITION(1, "Enum_EmpConditionAtr_WITH_CONDITION", "条件あり");

	/** The value. */
	public int value;

	/** The name id. */
	public String nameId;

	/** The description. */
	public String description;

	/** The Constant values. */
	private final static EmpConditionAtr[] values = EmpConditionAtr.values();

	/**
	 * Instantiates a new emp condition atr.
	 *
	 * @param value the value
	 * @param nameId the name id
	 * @param description the description
	 */
	private EmpConditionAtr(int value, String nameId, String description) {
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
	public static EmpConditionAtr valueOf(Integer value) {
		// Invalid object.
		if (value == null) {
			return null;
		}

		// Find value.
		for (EmpConditionAtr val : EmpConditionAtr.values) {
			if (val.value == value) {
				return val;
			}
		}

		// Not found.
		return null;
	}

}
