package nts.uk.screen.at.app.monthlyperformance.correction.dto;

import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum PrimitiveValueMonthly {
	
	RecordRemarks(13, "実績の備考", "RecordRemarks"),
	
	AttendanceTimeMonth(16, "勤怠月間時間", "AttendanceTimeMonth"),
	
	AttendanceTimeMonthWithMinus(17, "勤怠月間時間(マイナス有り)", "AttendanceTimeMonthWithMinus"),
	
	AttendanceDaysMonth(18, "勤怠月間日数", "AttendanceDaysMonth"),
	
	AttendanceTimesMonth(19, "勤怠月間回数", "AttendanceTimesMonth"),
	
	Agree36OverTime(20, "３６協定1ヵ月時間", "OverTime"),
	
	MonthlyDays(23, "月間日数", "MonthlyDays"),
	
	YearlyDays(27, "年間日数", "YearlyDays"),
	
	YearlyGrantDays(28, "年休付与日数", "GrantDays"),
	
	AnnualLeaveUsedDayNumber(29, "年休使用日数", "AnnualLeaveUsedDayNumber"),
	
	AnnualLeaveRemainingDayNumber(30, "年休残日数", "AnnualLeaveRemainingDayNumber"),
	
	AnnualLeaveRemainingTime(31, "年休残時間", "AnnualLeaveRemainingTime"),
	
	HalfDayHolidayUsedTimes(32, "半日年休使用回数", "UsedTimes"),
	
	HalfDayHolidayRemainingTimes(33, "半日年休残回数", "RemainingTimes"),
	
	HourHolidayUsedTimes(34, "時間年休使用回数", "UsedTimes"),
	
	HourHolidayUsedMinutes(35, "時間年休使用時間", "UsedMinutes"),
	
	HourHolidayRemainingMinutes(36, "時間年休残時間", "RemainingMinutes"),
	
	YearlyReservedGrantDays(37, "積立年休付与日数", "GrantDays"),
	
	ReserveLeaveUsedDayNumber(38, "積立年休使用日数", "ReserveLeaveUsedDayNumber"),
	
	ReserveLeaveRemainingDayNumber(39, "積立年休残日数", "ReserveLeaveRemainingDayNumber"),
	
	SpecialTimeOfRemain(40, "特別休暇残時間", "TimeOfRemain"),
	
	SpecialDayNumberOfRemain(41, "特別休暇残数", "DayNumberOfRemain"),
	
	DayNumberOfGrant(42, "特別休暇残数用付与日数", "DayNumberOfGrant"),
	
	SPECIAL_HOLIDAY_USE_NUM(43, "特別休暇使用回数", "UsedTimes"),// not found
	
	SpecialTimeOfUse(44, "特別休暇使用時間", "TimeOfUse"),
	
	SpecialDayNumberOfUse(45, "特別休暇使用日数", "DayNumberOfUse"),
	
	SUBSTITUTE_HOLIDAY_DAYS(46, "代休日数", "ReserveLeaveUsedDayNumber"),// not found
	
	SUBSTITUTE_HOLIDAY_REMAINING_DAYS(47, "代休残日数", "ReserveLeaveRemainingDayNumber"), // not found
	
	SUBSTITUTE_HOLIDAY_TIME(48, "代休時間", "UsedMinutes"),// not found
	
	SUBSTITUTE_HOLIDAY_REMAINING_TIME(49, "代休残時間", "RemainingMinutes"), // not found
	
	PAUSE_REMAINING_DAYS(50, "振休残日数", "ReserveLeaveRemainingDayNumber"),// not found
	
	PAUSE_USED_DAYS(51, "振休日数", "ReserveLeaveUsedDayNumber"),// not found
	
	MoneyDay(54, "日次任意金額", "AnyItemAmount"),
	
	MoneyMonth(55, "月次任意金額", "AnyAmountMonth"),
	
	TimeDay(56, "日次任意時間", "AnyItemTime"),
	
	TimeMonth(57, "月次任意時間", "AnyTimeMonth"), 
	
	CountDay(58, "日次任意回数", "AnyItemTimes"),
	
	CountMonth(59, "月次任意回数", "AnyTimesMonth"),
	
	ReasonCode(60, "乖離理由コード", "DiverdenceReasonCode"),
	
	EmploymentCode(7, "雇用区分コード", "EmploymentCode"),
	
	ClassificationCode(8, "分類コード", "ClassificationCode"),
	
	JobTitleCode(9, "職位コード", "JobTitleCode"),
	
	WorkplaceCode(10, "職場コード", "WorkplaceCode"),
	
	Business(21, "勤務種別", "BusinessTypeCode");
	
	
	public final int value;
	
	public final String name;
	
	public final String primitive;
	
	public final static PrimitiveValueMonthly[] values = PrimitiveValueMonthly.values();
	
	public final static Map<Integer, String> mapValuePrimitive =  IntStream.range(0, values.length).boxed().collect(Collectors.toMap(x -> values[x].value, x -> values[x].primitive));
	
}
