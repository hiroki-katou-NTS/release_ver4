package nts.uk.ctx.at.record.dom.divergence.time;

/**
 * The Enum DivergenceType.
 */
// 乖離の種類
public enum DivergenceType {

	/** The arbitrary divergence time. */
	ARBITRARY_DIVERGENCE_TIME(0, "任意乖離時間"),

	/** The holidays departure time. */
	HOLIDAYS_DEPARTURE_TIME(1, "休日出勤乖離時間"),

	/** The entry divergence time. */
	ENTRY_DIVERGENCE_TIME(2, "入館乖離時間"),

	/** The evacuation departure time. */
	EVACUATION_DEPARTURE_TIME(3, "退館乖離時間"),

	/** The logon pc divergence time. */
	LOGON_PC_DIVERGENCE_TIME(4, "PCログオン乖離時間"),

	/** The logoff pc divergence time. */
	LOGOFF_PC_DIVERGENCE_TIME(5, "PCログオフ乖離時間"),

	/** The predetermined break time divergence time. */
	PREDETERMINED_BREAK_TIME_DIVERGENCE_TIME(6, "所定内休憩乖離時間"),

	/** The non scheduled divergence time. */
	NON_SCHEDULED_DIVERGENCE_TIME(7, "所定外休憩乖離時間"),

	/** The premature overtime departure time. */
	PREMATURE_OVERTIME_DEPARTURE_TIME(8, "早出残業乖離時間"),

	/** The normal overtime divergence time. */
	NORMAL_OVERTIME_DIVERGENCE_TIME(9, "普通残業乖離時間");

	/** The value. */
	public final int value;

	/** The name id. */
	public final String display;

	/** The Constant values. */
	private final static DivergenceType[] values = DivergenceType.values();

	/**
	 * Instantiates a new rounding.
	 *
	 * @param value            the value
	 * @param display the display
	 */
	private DivergenceType(int value, String display) {
		this.value = value;
		this.display = display;
	}

	/**
	 * Value of.
	 *
	 * @param value
	 *            the value
	 * @return the rounding
	 */
	public static DivergenceType valueOf(Integer value) {
		// Invalid object.
		if (value == null) {
			return null;
		}

		// Find value.
		for (DivergenceType val : DivergenceType.values) {
			if (val.value == value) {
				return val;
			}
		}

		// Not found.
		return null;
	}
}
