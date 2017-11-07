/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.at.shared.dom.worktime.fixedworkset;

/**
 * The Enum LegalOTSetting.
 */
public enum LegalOTSetting {

	/** The legal internal time. */
	// 法定内の残業時間を法定内時間として扱う
	LEGAL_INTERNAL_TIME(0, "Enum_LegalOTSetting_allEmployee", "法定内の残業時間を法定内時間として扱う"),

	/** The outside legal time. */
	// 法定内の残業時間を法定外時間として扱う
	OUTSIDE_LEGAL_TIME(1, "Enum_LegalOTSetting_departmentAndChild", "法定内の残業時間を法定外時間として扱う");

	/** The value. */
	public final int value;

	/** The name id. */
	public final String nameId;

	/** The description. */
	public final String description;

	/** The Constant values. */
	private final static LegalOTSetting[] values = LegalOTSetting.values();

	/**
	 * Instantiates a new legal OT setting.
	 *
	 * @param value the value
	 * @param nameId the name id
	 * @param description the description
	 */
	private LegalOTSetting(int value, String nameId, String description) {
		this.value = value;
		this.nameId = nameId;
		this.description = description;
	}

	/**
	 * Value of.
	 *
	 * @param value the value
	 * @return the legal OT setting
	 */
	public static LegalOTSetting valueOf(Integer value) {
		// Invalid object.
		if (value == null) {
			return null;
		}

		// Find value.
		for (LegalOTSetting val : LegalOTSetting.values) {
			if (val.value == value) {
				return val;
			}
		}

		// Not found.
		return null;
	}
	
	
}
