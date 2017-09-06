/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.at.shared.dom.worktype;

import lombok.AllArgsConstructor;

/**
 * The Enum WorkTypeClassification.
 */
@AllArgsConstructor
// 勤務種類の分類
public enum WorkTypeClassification {
	Absence(0), // 欠勤
	AnnualHoliday(1), // 年休
	Attendance(2), // 出勤
	Closure(3), // 休業
	ContinuousWork(4), // 連続勤務
	Holiday(5), // 休日
	HolidayWork(6), // 休日出勤
	LeaveOfAbsence(7), // 休職
	Pause(8), // 振休
	Shooting(9), // 振出
	SpecialHoliday(10), // 特別休暇
	SubstituteHoliday(11), // 代休
	TimeDigestVacation(12), // 時間消化休暇
	YearlyReserved(13); // 積立年休

	/** The value. */
	public final int value;
	
	public boolean isHoliday() {
		switch (this) {
		case Absence:
		case AnnualHoliday:
		case Closure:
		case Holiday:
		case LeaveOfAbsence:
		case Pause:
		case SpecialHoliday:
		case SubstituteHoliday:
		case TimeDigestVacation:
		case YearlyReserved:
		case ContinuousWork:
			return true;
			
		case HolidayWork:
		case Attendance:
		case Shooting:
			return false;
			
		default:
			throw new RuntimeException("invalid value: " + this);
		}
	}
	
	public boolean isAttendance() {
		return !this.isHoliday();
	}
	
	public boolean isWeekDayAttendance() {
		switch (this) {
		case Attendance:
		case Shooting:
			return true;
		case Absence:
		case AnnualHoliday:
		case Closure:
		case ContinuousWork:
		case Holiday:
		case LeaveOfAbsence:
		case Pause:
		case SpecialHoliday:
		case SubstituteHoliday:
		case TimeDigestVacation:
		case YearlyReserved:
			return false;
		default:
			throw new RuntimeException("invalid value: " + this);
		}
	}
}
