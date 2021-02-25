package nts.uk.ctx.at.function.dom.attendancetype;

/**
 * The Enum ScreenUseAtr.
 */
// 勤怠項目を利用する画面
public enum ScreenUseAtr {
	/** The personnel expenses. */
	// 人件費
	PERSONNEL_EXPENSES(0, "人件費"),

	/** The divergence time. */
	// ★乖離時間
	DIVERGENCE_TIME(1, "★乖離時間"),

	/** The over time. */
	// 時間外超過
	OVER_TIME(2, "時間外超過"),

	/** The entry divergence time. */
	// 入館乖離時間
	ENTRY_DIVERGENCE_TIME(3, "入館乖離時間"),

	/** The evacuation departure timr. */
	// 退館乖離時間
	EVACUATION_DEPARTURE_TIMR(4, "退館乖離時間"),

	/** The pclogon divergence time. */
	// PCログオン乖離時間
	PCLOGON_DIVERGENCE_TIME(5, "PCログオン乖離時間"),

	/** The pclogoff divergence time. */
	// PCログオフ乖離時間
	PCLOGOFF_DIVERGENCE_TIME(6, "PCログオフ乖離時間"),

	/** The predetermined break time divergence. */
	// 所定内休憩乖離時間
	PREDETERMINED_BREAK_TIME_DIVERGENCE(7, "所定内休憩乖離時間"),

	/** The non scheduled divergence time. */
	// 所定外休憩乖離時間
	NON_SCHEDULED_DIVERGENCE_TIME(8, "所定外休憩乖離時間"),

	/** The premature overtime departure time. */
	// 早出残業乖離時間
	PREMATURE_OVERTIME_DEPARTURE_TIME(9, "早出残業乖離時間"),

	/** The normal overtime deviation time. */
	// 普通残業乖離時間
	NORMAL_OVERTIME_DEVIATION_TIME(10, "普通残業乖離時間"),

	/** The holidays departure time. */
	// 休日出勤乖離時間
	HOLIDAYS_DEPARTURE_TIME(11, "休日出勤乖離時間"),

	/** The arbitrary divergence time. */
	// 任意乖離時間
	ARBITRARY_DIVERGENCE_TIME(12, "任意乖離時間"),

	/** The attendance type of dervicetype. */
	// 出勤簿勤務種類
	ATTENDANCE_TYPE_OF_DERVICETYPE(13, "出勤簿勤務種類"),

	/** The employee booking hours. */
	// 出勤簿就業時間帯
	EMPLOYEE_BOOKING_HOURS(14, "出勤簿就業時間帯"),

	/** The time of cumulative arrival. */
	// 出勤簿時刻
	TIME_OF_CUMULATIVE_ARRIVAL(15, "出勤簿時刻"),

	/** The work time. */
	// 出勤簿時間
	WORK_TIME(16, "出勤簿時間"),

	/** The attendance times. */
	// 出勤簿回数
	ATTENDANCE_TIMES(17, "出勤簿回数"),

	/** The total commuting amount. */
	// 出勤簿金額
	TOTAL_COMMUTING_AMOUNT(18, "出勤簿金額"),

	/** The daily work schedule. */
	// 日別勤務表
	DAILY_WORK_SCHEDULE(19, "日別勤務表"),
		
	/** The monthly work schedule. */
	// 月別勤務表
	MONTHLY_WORK_SCHEDULE(20, "月別勤務表"),
	
	// 出勤簿日数
	DAILY_WORK_TIME(22, "出勤簿日数");

	/** The value. */
	public final int value;

	/** The name id. */
	public final String name;

	/** The Constant values. */
	private final static ScreenUseAtr[] values = ScreenUseAtr.values();

	/**
	 * Instantiates a new rounding.
	 *
	 * @param value
	 *            the value
	 * @param nameId
	 *            the name id
	 */
	private ScreenUseAtr(int value, String name) {
		this.value = value;
		this.name = name;
	}

	/**
	 * Value of.
	 *
	 * @param value
	 *            the value
	 * @return the rounding
	 */
	public static ScreenUseAtr valueOf(Integer value) {
		// Invalid object.
		if (value == null) {
			return null;
		}

		// Find value.
		for (ScreenUseAtr val : ScreenUseAtr.values) {
			if (val.value == value) {

				return val;
			}
		}

		// Not found.
		return null;
	}

}
