package nts.uk.ctx.at.shared.dom.dailyattdcal.dailyattendance.worktime;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import nts.arc.layer.dom.objecttype.DomainObject;
import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.shared.dom.common.time.AttendanceTimeOfExistMinus;
import nts.uk.ctx.at.shared.dom.dailyattdcal.dailyattendance.enums.CheckExcessAtr;
import nts.uk.ctx.at.shared.dom.dailyattdcal.dailyattendance.enums.SystemFixedErrorAlarm;
import nts.uk.ctx.at.shared.dom.dailyattdcal.dailyattendance.erroralarm.EmployeeDailyPerError;
import nts.uk.ctx.at.shared.dom.dailyattdcal.dailyattendance.secondorder.medical.MedicalCareTimeOfDaily;
import nts.uk.ctx.at.shared.dom.dailyattdcal.dailyattendance.workschedule.WorkScheduleTimeOfDaily;
/**
 * 日別勤怠の勤怠時間 (new)
 * UKDesign.ドメインモデル.NittsuSystem.UniversalK.就業.shared(勤務予定、勤務実績).日の勤怠計算.日別勤怠.実働時間.日別勤怠の勤怠時間
 * @author tutk
 *
 */
@Getter
public class AttendanceTimeOfDailyAttendance implements DomainObject {
		//勤務予定時間 - 日別実績の勤務予定時間
		private WorkScheduleTimeOfDaily workScheduleTimeOfDaily;
		
		//実働時間/実績時間  - 日別実績の勤務実績時間 - 勤務時間 /日別勤怠の勤務時間 (new)
		private ActualWorkingTimeOfDaily actualWorkingTimeOfDaily;
		
		//滞在時間 - 日別実績の滞在時間 change tyle
		private StayingTimeOfDaily stayingTime;
		
		//不就労時間 - 勤怠時間
		private AttendanceTimeOfExistMinus unEmployedTime;
		
		//予実差異時間 - 勤怠時間
		private AttendanceTimeOfExistMinus budgetTimeVariance;
		
		//医療時間 - 日別実績の医療時間
		private MedicalCareTimeOfDaily medicalCareTime;

		
		public AttendanceTimeOfDailyAttendance (
				 WorkScheduleTimeOfDaily schedule,
				 ActualWorkingTimeOfDaily actual,
				 StayingTimeOfDaily stay,
				 AttendanceTimeOfExistMinus budget,
				 AttendanceTimeOfExistMinus unEmploy) {
				this.workScheduleTimeOfDaily = schedule;
				this.actualWorkingTimeOfDaily = actual;
				this.stayingTime = stay;
				this.budgetTimeVariance = budget;
				this.unEmployedTime = unEmploy;
				}
				
	public AttendanceTimeOfDailyAttendance(WorkScheduleTimeOfDaily workScheduleTimeOfDaily,
			ActualWorkingTimeOfDaily actualWorkingTimeOfDaily, StayingTimeOfDaily stayingTime,
			AttendanceTimeOfExistMinus unEmployedTime, AttendanceTimeOfExistMinus budgetTimeVariance,
			MedicalCareTimeOfDaily medicalCareTime) {
		super();
		this.workScheduleTimeOfDaily = workScheduleTimeOfDaily;
		this.actualWorkingTimeOfDaily = actualWorkingTimeOfDaily;
		this.stayingTime = stayingTime;
		this.unEmployedTime = unEmployedTime;
		this.budgetTimeVariance = budgetTimeVariance;
		this.medicalCareTime = medicalCareTime;
	}
	/**
	 * エラーチェックの指示メソッド 
	 * @param attendanceItemConverter 
	 * @return 社員のエラーチェック一覧
	 */
	public List<EmployeeDailyPerError> getErrorList(String employeeId,GeneralDate targetDate,
			   										SystemFixedErrorAlarm fixedErrorAlarmCode, CheckExcessAtr checkAtr) {
		List<EmployeeDailyPerError> returnErrorItem = new ArrayList<>();
		if(this.getActualWorkingTimeOfDaily() != null) {
			return this.getActualWorkingTimeOfDaily().requestCheckError(employeeId, targetDate, fixedErrorAlarmCode, checkAtr);
		}
		return returnErrorItem;
	}
	
	public AttendanceTimeOfDailyAttendance inssertActualWorkingTimeOfDaily(ActualWorkingTimeOfDaily time) {
		return new AttendanceTimeOfDailyAttendance (
						this.workScheduleTimeOfDaily, time, this.stayingTime, this.budgetTimeVariance,
						this.unEmployedTime
				); 
	}
}
