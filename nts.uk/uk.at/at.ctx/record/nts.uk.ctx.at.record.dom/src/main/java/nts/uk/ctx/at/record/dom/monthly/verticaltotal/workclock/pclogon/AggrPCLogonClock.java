package nts.uk.ctx.at.record.dom.monthly.verticaltotal.workclock.pclogon;

import java.util.Optional;

import lombok.Getter;
import lombok.val;
import nts.gul.util.value.MutableValue;
import nts.uk.ctx.at.record.dom.daily.attendanceleavinggate.PCLogOnInfoOfDaily;
import nts.uk.ctx.at.record.dom.dailyprocess.calc.PredetermineTimeSetForCalc;
import nts.uk.ctx.at.record.dom.worktime.TimeLeavingOfDailyPerformance;
import nts.uk.ctx.at.shared.dom.common.days.AttendanceDaysMonth;
import nts.uk.ctx.at.shared.dom.common.time.AttendanceTimeMonth;
import nts.uk.ctx.at.shared.dom.worktime.predset.UseSetting;
import nts.uk.ctx.at.shared.dom.worktype.WorkType;
import nts.uk.ctx.at.shared.dom.worktype.WorkTypeCode;

/**
 * 集計PCログオン時刻
 * @author shuichi_ishida
 */
@Getter
public class AggrPCLogonClock {

	/** 105：プレミアムデー */
	public static final WorkTypeCode PREMIUM_DAY = new WorkTypeCode("105");
	/** 002：半出　+　プレミアム */
	public static final WorkTypeCode HALF_WORK_PREMIUM = new WorkTypeCode("002");
	
	/** 合計日数 */
	private AttendanceDaysMonth totalDays;
	/** 合計時刻 */
	private AttendanceTimeMonth totalClock;
	/** 平均時刻 */
	private AttendanceTimeMonth averageClock;
	
	/**
	 * コンストラクタ
	 */
	public AggrPCLogonClock(){
		
		this.totalDays = new AttendanceDaysMonth(0.0);
		this.totalClock = new AttendanceTimeMonth(0);
		this.averageClock = new AttendanceTimeMonth(0);
	}

	/**
	 * ファクトリー
	 * @param totalDays 合計日数
	 * @param totalClock 合計時刻
	 * @param averageClock 平均時刻
	 * @return 集計PCログオン時刻
	 */
	public static AggrPCLogonClock of(
			AttendanceDaysMonth totalDays,
			AttendanceTimeMonth totalClock,
			AttendanceTimeMonth averageClock){
		
		AggrPCLogonClock domain = new AggrPCLogonClock();
		domain.totalDays = totalDays;
		domain.totalClock = totalClock;
		domain.averageClock = averageClock;
		return domain;
	}
	
	/**
	 * 集計PCログオン時刻
	 * @param pcLogonInfoOpt 日別実績のPCログオン情報 
	 * @param isWeekday 平日かどうか
	 * @param workType 勤務種類
	 * @param timeLeavingOfDaily 日別実績の出退勤
	 */
	public void aggregateLogOn(
			Optional<PCLogOnInfoOfDaily> pcLogonInfoOpt,
			boolean isWeekday,
			WorkType workType,
			TimeLeavingOfDailyPerformance timeLeavingOfDaily){
		
		if (!pcLogonInfoOpt.isPresent()) return;
		val pcLogonInfo = pcLogonInfoOpt.get();
		
		// 出勤時刻<>NULL
		Integer timeAttendance = this.getTimeAttendance(timeLeavingOfDaily);
		if (timeAttendance == null) return;

		// 平日の判断
		if (isWeekday == false) return;
		
		// Web終業時刻計算対象の判断
		if (workType.isCalcTargetForEndClock() == true) {
			boolean isExistLogon = false;
			
			// ログオン時刻を合計
			for (val logonInfo : pcLogonInfo.getLogOnInfo()){
				
				// ログ時刻<>NULL
				if (!logonInfo.getLogOn().isPresent()) continue;
				
				// ログオン時刻を合計
				this.totalClock = this.totalClock.addMinutes(logonInfo.getLogOn().get().v());
				isExistLogon = true;
			}
			
			// 合計日数を計算
			if (isExistLogon) this.totalDays = this.totalDays.addDays(1.0);
		}
		
		// 平均時刻を計算
		this.calcAverageClock();
	}
	
	/**
	 * 集計PCログオフ時刻
	 * @param pcLogonInfoOpt 日別実績のPCログオン情報 
	 * @param timeLeavingOfDaily 日別実績の出退勤
	 * @param isWeekday 平日かどうか
	 * @param workType 勤務種類
	 * @param predTimeSetForCalc 計算用所定時間設定
	 */
	public void aggregateLogOff(
			Optional<PCLogOnInfoOfDaily> pcLogonInfoOpt,
			TimeLeavingOfDailyPerformance timeLeavingOfDaily,
			boolean isWeekday,
			WorkType workType,
			PredetermineTimeSetForCalc predTimeSetForCalc) {
		
		if (!pcLogonInfoOpt.isPresent()) return;
		val pcLogonInfo = pcLogonInfoOpt.get();
		
		// 退勤時刻<>NULL
		Integer timeLeave = this.getTimeLeave(timeLeavingOfDaily);
		if (timeLeave == null) return;

		// 平日の判断
		if (isWeekday == false) return;
		
		if (predTimeSetForCalc == null) return;
		
		// Web終業時刻計算対象の判断
		if (workType.isCalcTargetForEndClock() == true) {
			boolean isExistLogoff = false;
			
			// ログオフ時刻を合計
			for (val logonInfo : pcLogonInfo.getLogOnInfo()){
				
				// ログ時刻<>NULL
				if (!logonInfo.getLogOff().isPresent()) continue;
				
				// 補正後PCログオフ時刻を計算（集計）
				this.totalClock = this.totalClock.addMinutes(AggrPCLogonClock.getLogOffClock(
						logonInfo.getLogOff().get().valueAsMinutes(), timeLeave, predTimeSetForCalc));
				isExistLogoff = true;
			}
			
			// 合計日数を計算
			if (isExistLogoff) this.totalDays = this.totalDays.addDays(1.0);
		}
		
		// 平均時刻を計算
		this.calcAverageClock();
	}

	/**
	 * 平均時刻を計算
	 */
	private void calcAverageClock() {
		this.averageClock = new AttendanceTimeMonth(0);
		if (this.totalDays.v() != 0.0){
			this.averageClock = new AttendanceTimeMonth(this.totalClock.v() / this.totalDays.v().intValue());
		}
	}

	/**
	 * 補正後PCログオフ時刻を計算
	 * @param logOff ログオフ時刻
	 * @param timeLeave 退勤時刻
	 * @param predTimeSetForCalc 計算用所定時間設定
	 * @return 補正後PCログオフ時刻
	 */
	public static int getLogOffClock(int logOff, Integer timeLeave, PredetermineTimeSetForCalc predTimeSetForCalc) {
		
		// 指定した時刻が所定内に含まれているかどうか確認
		boolean isLogOffInPredTimeSet = predTimeSetForCalc.getTimeSheets().stream().anyMatch(ts ->
				ts.getUseAtr() == UseSetting.USE
				&& ts.getStart().valueAsMinutes() <= logOff && ts.getEnd().valueAsMinutes() >= logOff);
		
		// 所定内に含まれている時、退勤時刻を返す
		if (isLogOffInPredTimeSet) return timeLeave;
		
		// 退勤時刻とログオフ時刻の大きい方を返す
		if (logOff > timeLeave) return logOff;
		return timeLeave;
	}
	
	/**
	 * 出勤時刻の取得
	 * @param timeLeavingOfDaily 日別実績の出退勤
	 * @return　出勤時刻
	 */
	private Integer getTimeAttendance(TimeLeavingOfDailyPerformance timeLeavingOfDaily) {
		if(timeLeavingOfDaily == null) return null;
		
		MutableValue<Integer> timeAttendance = new MutableValue<>(null);
		
		timeLeavingOfDaily.getAttendanceLeavingWork(1).ifPresent(tl -> {
			tl.getAttendanceStamp().ifPresent(leave -> {
				leave.getStamp().ifPresent(ls -> {
					if (ls.getTimeWithDay() != null) timeAttendance.set(ls.getTimeWithDay().valueAsMinutes());
				});
			});
		});
		
		return timeAttendance.optional().isPresent() ? timeAttendance.get() : null;
	}

	/**
	 * 退勤時刻の取得
	 * @param timeLeavingOfDaily 日別実績の出退勤
	 * @return 退勤時刻
	 */
	private Integer getTimeLeave(TimeLeavingOfDailyPerformance timeLeavingOfDaily) {
		if(timeLeavingOfDaily == null) return null;
		
		MutableValue<Integer> timeLeave = new MutableValue<>(null);
		
		timeLeavingOfDaily.getAttendanceLeavingWork(1).ifPresent(tl -> {
			tl.getLeaveStamp().ifPresent(leave -> {
				leave.getStamp().ifPresent(ls -> {
					if (ls.getTimeWithDay() != null) timeLeave.set(ls.getTimeWithDay().valueAsMinutes());
				});
			});
		});
		
		return timeLeave.optional().isPresent() ? timeLeave.get() : null;
	}
	
	/**
	 * 合算する
	 * @param target 加算対象
	 */
	public void sum(AggrPCLogonClock target){
		
		this.totalDays = this.totalDays.addDays(target.totalDays.v());
		this.totalClock = this.totalClock.addMinutes(target.totalClock.v());
		
		calcAverageClock();
	}
}
