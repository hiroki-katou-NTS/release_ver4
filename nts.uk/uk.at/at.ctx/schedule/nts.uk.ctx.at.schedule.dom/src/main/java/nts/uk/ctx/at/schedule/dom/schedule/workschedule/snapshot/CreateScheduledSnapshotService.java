package nts.uk.ctx.at.schedule.dom.schedule.workschedule.snapshot;

import java.util.Optional;

import lombok.val;
import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.schedule.dom.schedule.workschedule.WorkSchedule;
import nts.uk.ctx.at.shared.dom.common.time.AttendanceTime;
import nts.uk.ctx.at.shared.dom.scherec.dailyattdcal.dailyattendance.snapshot.SnapShot;
import nts.uk.ctx.at.shared.dom.scherec.dailyattdcal.dailyattendance.vacationusetime.HolidayOfDaily;
import nts.uk.ctx.at.shared.dom.scherec.dailyattdcal.dailyattendance.worktime.TotalWorkingTime;

/** スナップショットを作成する */
public class CreateScheduledSnapshotService {

	/** スケジュール管理する場合の作成  */
	public static Optional<SnapShot> createForScheduleManaged(RequireM1 require, String sid, GeneralDate ymd) {
		
		val workSchedule = require.workSchedule(sid, ymd).orElse(null);
		if (workSchedule == null) {
			
			return Optional.empty();
		}
		
		/** スナップショットの所定時間を計算 */
		val predetermineTime = workSchedule.getOptAttendanceTime()
											.map(at -> calcPredetermineTime(at.getActualWorkingTimeOfDaily().getTotalWorkingTime()))
											 .orElseGet(() -> new AttendanceTime(0));
		
		/** 日別勤怠の勤務情報をスナップショットの勤務情報にコピー */
		return Optional.of(SnapShot.of(workSchedule.getWorkInfo().getRecordInfo(), predetermineTime));
	}
	
	/** スナップショットの所定時間を計算 */
	private static AttendanceTime calcPredetermineTime(TotalWorkingTime totalWorkTime) {
		
		/** 時間休暇使用時間の合計を計算 */
		val timeBreakUse = calcTimeBreakUsage(totalWorkTime);
		
		/** 使用時間の合計を計算 */
		val breakTimeUse = calcBreakUsage(totalWorkTime.getHolidayOfDaily());
		
		/** 所定時間を計算 */
		return totalWorkTime.getWithinStatutoryTimeOfDaily().getActualWorkTime()
				.addMinutes(breakTimeUse.valueAsMinutes())
				.addMinutes(timeBreakUse)
				.addMinutes(totalWorkTime.getWithinStatutoryTimeOfDaily().getWithinPrescribedPremiumTime().v().intValue());
	}
	
	/** 使用時間の合計を計算 */
	private static AttendanceTime calcBreakUsage(HolidayOfDaily holiday) {
		/** TODO: 子の看護介護を足す*/
		/** 休暇使用時間を合計する */
		return holiday.getAnnual().getUseTime()
				.addMinutes(holiday.getSubstitute().getUseTime().valueAsMinutes())
				.addMinutes(holiday.getOverSalary().getUseTime().valueAsMinutes())
				.addMinutes(holiday.getSpecialHoliday().getUseTime().valueAsMinutes())
				.addMinutes(holiday.getYearlyReserved().getUseTime().valueAsMinutes())
				.addMinutes(holiday.getTimeDigest().getUseTime().valueAsMinutes())
				.addMinutes(holiday.getAbsence().getUseTime().valueAsMinutes());
	}
	
	/** 時間休暇使用時間の合計を計算 */
	private static int calcTimeBreakUsage(TotalWorkingTime totalWorkTime) {
		
		/** 遅刻時間の時間休暇使用時間の合計 */
		val lateTime = totalWorkTime.getLateTimeOfDaily().stream()
				.mapToInt(c -> c.getTimePaidUseTime().totalVacationAddTime())
				.sum();
		
		/** 早退時間の時間休暇使用時間の合計 */
		val leaveEarlyTime = totalWorkTime.getLeaveEarlyTimeOfDaily().stream()
				.mapToInt(c -> c.getTimePaidUseTime().totalVacationAddTime())
				.sum();
		
		/** 外出時間の時間休暇使用時間の取得 */
		val outTime = totalWorkTime.getOutingTimeOfDailyPerformance().stream()
				.mapToInt(c -> c.getTimeVacationUseOfDaily().totalVacationAddTime())
				.sum();
		
		/** 合計して返す */
		return lateTime + leaveEarlyTime + outTime;
	}
	
	public static interface RequireM1 {
			
		Optional<WorkSchedule> workSchedule(String sid, GeneralDate ymd);
		
	}
}
