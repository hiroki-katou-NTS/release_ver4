/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/

package nts.uk.ctx.pr.core.dom.insurance.labor.businesstype;

/**
 * The Enum BusinessTypeEnum.
 */
public enum BusinessTypeEnum {

	/** The Biz 1 st. */
	Biz1St(0, 1, "Biz1St"),

	/** The Biz 2 nd. */
	Biz2Nd(1, 2, "Biz2Nd"),

	/** The Biz 3 rd. */
	Biz3Rd(2, 3, "Biz3Rd"),

	/** The Biz 4 th. */
	Biz4Th(3, 4, "Biz4Th"),

	/** The Biz 5 th. */
	Biz5Th(4, 5, "Biz5Th"),

	/** The Biz 6 th. */
	Biz6Th(5, 6, "Biz6Th"),

	/** The Biz 7 th. */
	Biz7Th(6, 7, "Biz7Th"),

	/** The Biz 8 th. */
	Biz8Th(7, 8, "Biz8Th"),

	/** The Biz 9 th. */
	Biz9Th(8, 9, "Biz9Th"),

	/** The Biz 10 th. */
	Biz10Th(9, 10, "Biz10Th");

	/** The index. */
	public int index;

	/** The value. */
	public int value;

	/** The description. */
	public String description;

	/** The Constant values. */
	private final static BusinessTypeEnum[] values = BusinessTypeEnum.values();

	/**
	 * Instantiates a new business type enum.
	 *
	 * @param value
	 *            the value
	 * @param description
	 *            the description
	 */
	private BusinessTypeEnum(int index, int value, String description) {
		this.index = index;
		this.value = value;
		this.description = description;
	}

	/**
	 * Value of.
	 *
	 * @param value
	 *            the value
	 * @return the business type enum
	 */
	public static BusinessTypeEnum valueOf(Integer value) {
		// Invalid object.
		if (value == null) {
			return null;
		}

		// Find value.
		for (BusinessTypeEnum val : BusinessTypeEnum.values) {
			if (val.value == value) {
				return val;
			}
		}

		// Not found.
		return null;
	}
}
