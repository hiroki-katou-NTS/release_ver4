package nts.uk.ctx.at.shared.dom.scherec.dailyattendanceitem.enums;

public enum PrimitiveValueOfAttendanceItem {
	ATTENDANCE_TIME(1, "勤怠時間"),
	ATTENDANCE_TIME_WITH_MINUS(2, "勤怠時間（マイナス有り）"),
	WORK_NUM(3, "勤務回数"),
	WORK_TYPE_CD(4, "勤務種類コード"),
	WORK_HOURS_CD(5, "就業時間帯コード"),
	WORK_LOCATION_CD(6, "勤務場所コード"),
	EMP_CTG_CD(7, "雇用区分コード"),
	CLASSIFICATION_CD(8, "分類コード"),
	POSITION_CD(9, "職位コード"),
	WORKPLACE_CD(10, "職場コード"),
	REASON_FOR_DISCREPANCY(11, "乖離理由"),
	BREAK_OUT_NUM(12, "休憩外出回数"),
	REMARKS_ACHIEVEMENTS(13, "実績の備考"),
	CHOICE_CD(14, "選択肢コード"),
	TIME_WITH_DAY_ATR(15, "時刻（日区分付き）"),
	ATTENDANCE_MONTHLY_TIME(16, "勤怠月間時間"),
	ATTENDANCE_MONTHLY_TIME_WITH_MINUS(17, "勤怠月間時間（マイナス有り）"),
	ATTENDANCE_MONTHLY_DAYS(18, "勤怠月間日数"),
	ATTENDANCE_MONTHLY_NUM(19, "勤怠月間回数"),
	AGREEMENT36_ONE_MONTH(20, "３６協定1ヵ月時間"),
	WORK_TYPE_DIFFERENT_CD(21, "勤務種別コード"),
	CALCULATION_TIME(22, "計算時間"),
	MONTHLY_DAYS(23, "月間日数"),
	TIME(24, "時間"),
	DATE(25, "年月日"),
	OUT_WORK_DATE(26, "出勤率"),
	YEARLY_DAYS(27, "年間日数"),
	YEARLY_HOLIDAY_GRANT_DAYS(28, "年休付与日数"),
	YEARLY_HOLIDAY_USE_DAYS(29, "年休使用日数"),
	YEARLY_HOLIDAY_REMAINING_DAYS(30, "年休残日数"),
	YEARLY_HOLIDAY_REMAINING_TIME(31, "年休残時間"),
	HALF_DAY_HOLIDAY_USE_NUM(32, "半日年休使用回数"),
	HALF_DAY_HOLIDAY_REMAINING_NUM(33, "半日年休残回数"),
	HOUR_HOLIDAY_USE_NUM(34, "時間年休使用回数"),
	HOUR_HOLIDAY_USE_TIME(35, "時間年休使用時間"),
	HOUR_HOLIDAY_REMAINING_TIME(36, "時間年休残時間"),
	YEARLY_RESERVED_GRANT_DAYS(37, "積立年休付与日数"),
	YEARLY_RESERVED_USE_DAYS(38, "積立年休使用日数"),
	YEARLY_RESERVED_REMAINING_DAYS(39, "積立年休残日数"),
	SPECIAL_HOLIDAY_REMAINING_TIME(40, "特別休暇残時間"),
	SPECIAL_HOLIDAY_REMAINING_NUM(41, "特別休暇残数"),
	SPECIAL_HOLIDAY_REMAINING_GRANT_DAYS(42, "特別休暇残数用付与日数"),
	SPECIAL_HOLIDAY_USE_NUM(43, "特別休暇使用回数"),
	SPECIAL_HOLIDAY_USE_TIME(44, "特別休暇使用時間"),
	SPECIAL_HOLIDAY_USE_DAYS(45, "特別休暇使用日数"),
	SUBSTITUTE_HOLIDAY_DAYS(46, "代休日数"),
	SUBSTITUTE_HOLIDAY_REMAINING_DAYS(47, "代休残日数"),
	SUBSTITUTE_HOLIDAY_TIME(48, "代休時間"),
	SUBSTITUTE_HOLIDAY_REMAINING_TIME(49, "代休残時間"),
	PAUSE_REMAINING_DAYS(50, "振休残日数"),
	PAUSE_TOTAL_USE_DAYS(51, "振休使用日数合計"),
	PAUSE_TOTAL_OCCURRENCE_DAYS(52, "振休発生日数合計"),
	PAUSE_TOTAL_UNDIGESTED_DAYS(53, "振休未消化日数"),
	DAILY_ANY_AMOUNT(54, "日次任意金額"),
	MONTHLY_ANY_AMOUNT(55, "月次任意金額"),
	DAILY_ANY_TIME(56, "日次任意時間"),
	MONTHLY_ANY_TIME(57, "月次任意時間"),
	DAILY_ANY_NUM(58, "日次任意回数"),
	MONTHLY_ANY_NUM(59, "月次任意回数"),
	DIVERGENCE_REASON_CD(60, "乖離理由コード"),
	APPLICATION_REASON(61, "申請理由"),
	APPLICATION_FIXED_REASON(62, "申請定型理由コード"),
	WORD_CD(63, "作業コード"),
	COMPANY_ID(64, "会社ID"),
	MOUNTHLY_AMOUNT(65, "勤怠月間金額"),
	RESERVATION_NUM(66, "注文数"),
	RESERVATION_AMOUNT(67, "注文金額"),
	ADDITION_SETTING_CODE(68, "加給設定コード");

	public final int value;
	public final String name;

	private PrimitiveValueOfAttendanceItem(int value, String name) {
		this.value = value;
		this.name = name;
	}
}
