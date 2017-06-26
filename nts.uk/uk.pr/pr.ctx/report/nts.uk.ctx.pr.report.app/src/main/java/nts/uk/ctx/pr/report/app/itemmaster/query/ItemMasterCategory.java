/******************************************************************
 * Copyright (c) 2015 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.report.app.itemmaster.query;

/**
 * The Enum ItemMasterCategory.
 */
public enum ItemMasterCategory {
	
	/** The Payment. */
	Payment(0),

	/** The Deduction. */
	Deduction(1),

	/** The Attendance. */
	Attendance(2),
	
	/** The Articles. */
	Articles(3),
	
	/** The Other. */
	Other(9);
	

	/** The value. */
	public final int value;

	/**
	 * Instantiates a new item master category.
	 *
	 * @param value the value
	 */
	private ItemMasterCategory(int value) {
		this.value = value;
	}

	/**
	 * Value of.
	 *
	 * @param value the value
	 * @return the item master category
	 */
	public static ItemMasterCategory valueOf(Integer value) {
		// Invalid object.
		if (value == null) {
			return null;
		}

		// Find value.
		for (ItemMasterCategory val : ItemMasterCategory.values()) {
			if (val.value == value) {
				return val;
			}
		}

		// Not found.
		return null;
	}
}
