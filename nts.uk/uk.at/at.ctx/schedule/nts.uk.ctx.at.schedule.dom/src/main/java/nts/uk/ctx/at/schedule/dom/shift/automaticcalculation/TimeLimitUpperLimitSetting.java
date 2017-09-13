/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.at.schedule.dom.shift.automaticcalculation;

/**
 * The Enum TimeLimitUpperLimitSetting.
 */
//時間外の上限設定
public enum TimeLimitUpperLimitSetting {

	/** The noupperlimit. */
	NOUPPERLIMIT(0, "上限なし" ,"Enum_TimeLimitUpperLimitSetting_NoUpperLimit" ),
	
	/** The limitnumberapplication. */
	LIMITNUMBERAPPLICATION(1, "事前申請を上限とする" ,"Enum_TimeLimitUpperLimitSetting_LimitNumberApplications" ),
	
	/** The indicatedyimeupperlimit. */
	INDICATEDYIMEUPPERLIMIT(2, "指示時間を上限とする" ,"Enum_TimeLimitUpperLimitSetting_IndicatedTimeUpperLimit" );
	
	/** The value. */
	public final int value;

	/** The name id. */
	public final String nameId;

	/** The description. */
	public final String description;

	/** The Constant values. */
	private final static TimeLimitUpperLimitSetting[] values = TimeLimitUpperLimitSetting.values();


	/**
	 * Instantiates a new time limit upper limit setting.
	 *
	 * @param value the value
	 * @param nameId the name id
	 * @param description the description
	 */
	private TimeLimitUpperLimitSetting(int value, String nameId, String description) {
		this.value = value;
		this.nameId = nameId;
		this.description = description;
	}

	/**
	 * Value of.
	 *
	 * @param value the value
	 * @return the time limit upper limit setting
	 */
	public static TimeLimitUpperLimitSetting valueOf(Integer value) {
		// Invalid object.
		if (value == null) {
			return null;
		}

		// Find value.
		for (TimeLimitUpperLimitSetting val : TimeLimitUpperLimitSetting.values) {
			if (val.value == value) {
				return val;
			}
		}

		// Not found.
		return null;
	}

}
