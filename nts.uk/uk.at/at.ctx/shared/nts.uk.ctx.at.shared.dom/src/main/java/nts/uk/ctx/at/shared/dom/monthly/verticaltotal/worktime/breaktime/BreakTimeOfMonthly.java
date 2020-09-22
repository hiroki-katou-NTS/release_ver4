package nts.uk.ctx.at.shared.dom.monthly.verticaltotal.worktime.breaktime;

import java.io.Serializable;

import lombok.Getter;
import lombok.val;
import nts.uk.ctx.at.shared.dom.common.time.AttendanceTimeMonth;
import nts.uk.ctx.at.shared.dom.scherec.dailyattdcal.dailyattendance.worktime.AttendanceTimeOfDailyAttendance;

/**
 * 月別実績の休憩時間
 * @author shuichi_ishida
 */
@Getter
public class BreakTimeOfMonthly implements Serializable{

	/** Serializable */
	private static final long serialVersionUID = 1L;

	/** 休憩時間 */
	private AttendanceTimeMonth breakTime;
	
	/**
	 * コンストラクタ
	 */
	public BreakTimeOfMonthly(){
		
		this.breakTime = new AttendanceTimeMonth(0);
	}
	
	/**
	 * ファクトリー
	 * @param breakTime 休憩時間
	 * @return 月別実績の休憩時間
	 */
	public static BreakTimeOfMonthly of(AttendanceTimeMonth breakTime){
		
		val domain = new BreakTimeOfMonthly();
		domain.breakTime = breakTime;
		return domain;
	}
	
	/**
	 * 集計
	 * @param attendanceTimeOfDaily 日別実績の勤怠時間
	 */
	public void aggregate(AttendanceTimeOfDailyAttendance attendanceTimeOfDaily){

		if (attendanceTimeOfDaily == null) return;
		
		val totalWorkingTime = attendanceTimeOfDaily.getActualWorkingTimeOfDaily().getTotalWorkingTime();
		val breakTimeOfDaily = totalWorkingTime.getBreakTimeOfDaily();
		
		this.breakTime = this.breakTime.addMinutes(
				breakTimeOfDaily.getToRecordTotalTime().getTotalTime().getTime().v());
	}

	/**
	 * 合算する
	 * @param target 加算対象
	 */
	public void sum(BreakTimeOfMonthly target){
		
		this.breakTime = this.breakTime.addMinutes(target.breakTime.v());
	}
}
