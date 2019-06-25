package nts.uk.ctx.at.record.dom.monthly.verticaltotal.workclock.pclogon;

import java.util.Optional;

import lombok.Getter;
import lombok.val;
import nts.uk.ctx.at.record.dom.actualworkinghours.AttendanceTimeOfDailyPerformance;
import nts.uk.ctx.at.record.dom.daily.attendanceleavinggate.LogOnInfo;
import nts.uk.ctx.at.record.dom.daily.attendanceleavinggate.PCLogOnInfoOfDaily;
import nts.uk.ctx.at.record.dom.daily.attendanceleavinggate.PCLogOnNo;
import nts.uk.ctx.at.record.dom.daily.optionalitemtime.AnyItemValue;
import nts.uk.ctx.at.record.dom.daily.optionalitemtime.AnyItemValueOfDaily;
import nts.uk.ctx.at.record.dom.dailyprocess.calc.PredetermineTimeSetForCalc;
import nts.uk.ctx.at.record.dom.worktime.TimeActualStamp;
import nts.uk.ctx.at.record.dom.worktime.TimeLeavingOfDailyPerformance;
import nts.uk.ctx.at.record.dom.worktime.TimeLeavingWork;
import nts.uk.ctx.at.shared.dom.common.days.AttendanceDaysMonth;
import nts.uk.ctx.at.shared.dom.common.time.AttendanceTimeMonth;
import nts.uk.ctx.at.shared.dom.worktime.predset.UseSetting;
import nts.uk.ctx.at.shared.dom.worktype.WorkType;
import nts.uk.shr.com.time.TimeWithDayAttr;

/**
 * 集計PCログオン乖離
 * @author shuichi_ishida
 */
@Getter
public class AggrPCLogonDivergence {

	/** 日数 */
	private AttendanceDaysMonth days;
	/** 合計時間 */
	private AttendanceTimeMonth totalTime;
	/** 平均時間 */
	private AttendanceTimeMonth averageTime;
	
	/**
	 * コンストラクタ
	 */
	public AggrPCLogonDivergence(){
		
		this.days = new AttendanceDaysMonth(0.0);
		this.totalTime = new AttendanceTimeMonth(0);
		this.averageTime = new AttendanceTimeMonth(0);
	}

	/**
	 * ファクトリー
	 * @param days 日数
	 * @param totalTime 合計時間
	 * @param averageTime 平均時間
	 * @return 集計PCログオン乖離
	 */
	public static AggrPCLogonDivergence of(
			AttendanceDaysMonth days,
			AttendanceTimeMonth totalTime,
			AttendanceTimeMonth averageTime){
		
		AggrPCLogonDivergence domain = new AggrPCLogonDivergence();
		domain.days = days;
		domain.totalTime = totalTime;
		domain.averageTime = averageTime;
		return domain;
	}
	
	/**
	 * 集計PCログオン乖離
	 * @param pcLogonInfoOpt 日別実績のPCログオン情報 
	 * @param attendanceTimeOfDaily 日別実績の勤怠時間
	 * @param timeLeavingOfDaily 日別実績の出退勤
	 * @param anyItemValueOpt 日別実績の任意項目
	 */
	public void aggregateLogon(
			Optional<PCLogOnInfoOfDaily> pcLogonInfoOpt,
			AttendanceTimeOfDailyPerformance attendanceTimeOfDaily,
			TimeLeavingOfDailyPerformance timeLeavingOfDaily,
			Optional<AnyItemValueOfDaily> anyItemValueOpt){

		// 対象とするかどうかの判断
		if (attendanceTimeOfDaily == null) return;
		val stayingTime =  attendanceTimeOfDaily.getStayingTime();		// 日別実績の滞在時間
		{
			// ログオン時刻<>NULLを判断
			if (!pcLogonInfoOpt.isPresent()) return;
			PCLogOnInfoOfDaily pcLogonInfo = pcLogonInfoOpt.get();
			Optional<LogOnInfo> logonInfoOpt = pcLogonInfo.getLogOnInfo(new PCLogOnNo(1));	// 勤務No=1
			if (!logonInfoOpt.isPresent()) return;
			if (!logonInfoOpt.get().getLogOn().isPresent()) return;
			
			// ログオン乖離時間の計算対象かどうか判断
			if (timeLeavingOfDaily == null) return;
			Optional<TimeLeavingWork> timeLeavingWorkOpt = timeLeavingOfDaily.getAttendanceLeavingWork(1);	// 勤務No=1
			if (timeLeavingWorkOpt.isPresent()) {
				TimeLeavingWork timeLeavingWork = timeLeavingWorkOpt.get();
				if (timeLeavingWork.getAttendanceStamp().isPresent() &&
					timeLeavingWork.getLeaveStamp().isPresent()) {
					TimeActualStamp attendanceStamp = timeLeavingWork.getAttendanceStamp().get();
					TimeActualStamp leaveStamp = timeLeavingWork.getLeaveStamp().get();
					if (attendanceStamp.getStamp().isPresent() &&
						leaveStamp.getStamp().isPresent()) {
						// 出勤＝退勤なら、対象外
						int attendanceMinutes = attendanceStamp.getStamp().get().getTimeWithDay().valueAsMinutes();
						int leaveMinutes = leaveStamp.getStamp().get().getTimeWithDay().valueAsMinutes();
						if (attendanceMinutes == leaveMinutes) {
							return;
						}
					}
				}
			}
			
			// 平日の判断
			boolean isWeekday = false;
			if (anyItemValueOpt.isPresent()) {
				AnyItemValueOfDaily anyItemValue = anyItemValueOpt.get();
				for (AnyItemValue item : anyItemValue.getItems()) {
					if (item.getItemNo().v().intValue() != 12) continue;	// 任意項目12以外は無視
					if (item.getTimes().isPresent()) {						// 回数=1 なら平日
						if (item.getTimes().get().v().doubleValue() == 1.0) isWeekday = true; 
					}
				}
			}
			if (isWeekday == false) return;
		}

		// 合計時間を集計
		int logonMinutes = stayingTime.getBeforePCLogOnTime().valueAsMinutes();
		if (logonMinutes > 0) this.totalTime = this.totalTime.addMinutes(logonMinutes);
		
		// 日数を集計する
		this.days = this.days.addDays(1.0);
		
		// 平均時間を計算する
		this.averageTime = new AttendanceTimeMonth(0);
		if (this.days.v().doubleValue() != 0.0){
			this.averageTime = new AttendanceTimeMonth(this.totalTime.valueAsMinutes() / this.days.v().intValue());
		}
	}
	
	/**
	 * 集計PCログオフ乖離
	 * @param pcLogonInfoOpt 日別実績のPCログオン情報 
	 * @param attendanceTimeOfDaily 日別実績の勤怠時間
	 * @param timeLeavingOfDaily 日別実績の出退勤
	 * @param workType 勤務種類
	 * @param predTimeSetForCalc 計算用所定時間設定
	 */
	public void aggregateLogoff(
			Optional<PCLogOnInfoOfDaily> pcLogonInfoOpt,
			AttendanceTimeOfDailyPerformance attendanceTimeOfDaily,
			TimeLeavingOfDailyPerformance timeLeavingOfDaily,
			WorkType workType,
			PredetermineTimeSetForCalc predTimeSetForCalc){

		// 対象とするかどうかの判断
		if (attendanceTimeOfDaily == null) return;
		if (predTimeSetForCalc == null) return;
		val stayingTime =  attendanceTimeOfDaily.getStayingTime();
		{
			// 退勤時刻<>NULL
			if (timeLeavingOfDaily == null) return;
			TimeWithDayAttr leaveStamp = null;
			Integer targetWorkNo = timeLeavingOfDaily.getWorkTimes().v();	// 勤務No ← 勤務回数
			Optional<TimeActualStamp> leavingWorkOpt = timeLeavingOfDaily.getLeavingWork();		// 2回勤務があれば2回目
			if (leavingWorkOpt.isPresent()) {
				if (leavingWorkOpt.get().getStamp().isPresent()) {
					leaveStamp = leavingWorkOpt.get().getStamp().get().getTimeWithDay();
				}
			}
			if (leaveStamp == null) return;
			
			// ログオフ時刻<>NULL
			if (!pcLogonInfoOpt.isPresent()) return;
			PCLogOnInfoOfDaily pcLogonInfo = pcLogonInfoOpt.get();
			Optional<LogOnInfo> logonInfoOpt = pcLogonInfo.getLogOnInfo(new PCLogOnNo(targetWorkNo));
			if (!logonInfoOpt.isPresent()) return;
			if (!logonInfoOpt.get().getLogOff().isPresent()) return;
			TimeWithDayAttr logoffStamp = logonInfoOpt.get().getLogOff().get();
			
			// 退勤時刻>=ログオフ時刻　なら対象外
			int leaveMinutes = leaveStamp.valueAsMinutes();
			int logoffMinutes = logoffStamp.valueAsMinutes();
			if (leaveMinutes >= logoffMinutes) return;

			// 時間帯　確認
			val timezoneUseOpt = predTimeSetForCalc.getTimeSheets(workType.getAttendanceHolidayAttr(), targetWorkNo);
			if (!timezoneUseOpt.isPresent()) return;
			val timezoneUse = timezoneUseOpt.get();
			if (timezoneUse.getUseAtr() == UseSetting.NOT_USE) return;
			
			// 所定時間内の退勤の場合、対象外
			int timezoneUseEndMinutes = timezoneUse.getEnd().valueAsMinutes();
			if (leaveMinutes < timezoneUseEndMinutes) return;
		}

		// 合計時間を集計
		int logoffMinutes = stayingTime.getAfterPCLogOffTime().valueAsMinutes();
		if (logoffMinutes > 0) this.totalTime = this.totalTime.addMinutes(logoffMinutes);
		
		// 日数を集計する
		if (stayingTime.getAfterPCLogOffTime().valueAsMinutes() > 0) this.days = this.days.addDays(1.0);
		
		// 平均時間を計算する
		this.averageTime = new AttendanceTimeMonth(0);
		if (this.days.v().doubleValue() != 0.0){
			this.averageTime = new AttendanceTimeMonth(this.totalTime.valueAsMinutes() / this.days.v().intValue());
		}
	}
	
	/**
	 * 合算する
	 * @param target 加算対象
	 */
	public void sum(AggrPCLogonDivergence target){
		
		this.days = this.days.addDays(target.days.v());
		this.totalTime = this.totalTime.addMinutes(target.totalTime.v());
		
		this.averageTime = new AttendanceTimeMonth(0);
		if (this.days.v() != 0.0){
			this.averageTime = new AttendanceTimeMonth(this.totalTime.v() / this.days.v().intValue());
		}
	}
}
