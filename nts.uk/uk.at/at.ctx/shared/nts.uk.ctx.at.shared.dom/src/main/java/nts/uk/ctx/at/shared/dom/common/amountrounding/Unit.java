/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.at.shared.dom.common.amountrounding;

/**
 * The Enum Unit.
 */
// 端数処理位置
public enum Unit {

	/** The one yen. */
	// 1円
	ONE_YEN(1, "ENUM_UNIT_ONE_YEN"),

	/** The ten yen. */
	// 10円
	TEN_YEN(10, "ENUM_UNIT_TEN_YEN"),

	/** The one hundred yen. */
	// 100円
	ONE_HUNDRED_YEN(100, "ENUM_UNIT_ONE_HUNDRED_YEN"),

	/** The one thousand yen. */
	// 1000円
	ONE_THOUSAND_YEN(1000, "ENUM_UNIT_ONE_THOUSAND_YEN"),;

	/** The value. */
	public final int value;

	/** The name id. */
	public final String nameId;

	/** The Constant values. */
	private final static Unit[] values = Unit.values();

	/**
	 * Instantiates a new unit.
	 *
	 * @param value
	 *            the value
	 * @param nameId
	 *            the name id
	 */
	private Unit(int value, String nameId) {
		this.value = value;
		this.nameId = nameId;
	}

	/**
	 * Value of.
	 *
	 * @param value
	 *            the value
	 * @return the unit
	 */
	public static Unit valueOf(Integer value) {
		// Invalid object.
		if (value == null) {
			return null;
		}

		// Find value.
		for (Unit val : Unit.values) {
			if (val.value == value) {
				return val;
			}
		}

		// Not found.
		return null;
	}
}
