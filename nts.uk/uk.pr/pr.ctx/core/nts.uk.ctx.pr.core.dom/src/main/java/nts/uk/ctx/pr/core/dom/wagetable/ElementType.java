/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/

package nts.uk.ctx.pr.core.dom.wagetable;

/**
 * The Enum ElementType.
 */
public enum ElementType {

	/** The master ref. */
	MASTER_REF(0, true, false, "マスタ参照"),

	/** The code ref. */
	CODE_REF(1, true, false, "コード名称参照"),

	/** The item data ref. */
	ITEM_DATA_REF(2, false, true, "明細データ参照"),

	/** The experience fix. */
	EXPERIENCE_FIX(3, false, true, "勤続年数"),

	/** The age fix. */
	AGE_FIX(4, false, true, "年齢"),

	/** The family mem fix. */
	FAMILY_MEM_FIX(5, false, true, "家族人数"),

	// Extend element type
	/** The certification. */
	CERTIFICATION(6, true, false, "資格"),

	/** The working day. */
	WITHOUT_WORKING_DAY(7, false, true, "欠勤日数"),

	/** The come late. */
	COME_LATE(8, false, true, "遅刻早退"),

	/** The level. */
	LEVEL(9, true, false, "精皆勤レベル");

	/** The value. */
	public int value;

	/** The is code mode. */
	public boolean isCodeMode;

	/** The is range mode. */
	public boolean isRangeMode;

	/** The display name. */
	public String displayName;

	/** The Constant values. */
	private final static ElementType[] values = ElementType.values();

	/**
	 * Instantiates a new element type.
	 *
	 * @param value
	 *            the value
	 * @param isCodeMode
	 *            the is code mode
	 * @param isRangeMode
	 *            the is range mode
	 */
	private ElementType(int value, boolean isCodeMode, boolean isRangeMode, String displayName) {
		this.value = value;
		this.isCodeMode = isCodeMode;
		this.isRangeMode = isRangeMode;
		this.displayName = displayName;
	}

	/**
	 * Value of.
	 *
	 * @param value
	 *            the value
	 * @return the element type
	 */
	public static ElementType valueOf(Integer value) {
		// Invalid object.
		if (value == null) {
			return null;
		}

		// Find value.
		for (ElementType val : ElementType.values) {
			if (val.value == value) {
				return val;
			}
		}

		// Not found.
		return null;
	}
}
