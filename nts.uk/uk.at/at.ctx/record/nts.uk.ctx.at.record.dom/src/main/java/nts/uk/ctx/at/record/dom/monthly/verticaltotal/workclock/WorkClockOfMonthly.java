package nts.uk.ctx.at.record.dom.monthly.verticaltotal.workclock;

import java.util.Optional;

import lombok.Getter;
import nts.uk.ctx.at.record.dom.actualworkinghours.AttendanceTimeOfDailyPerformance;
import nts.uk.ctx.at.record.dom.daily.attendanceleavinggate.PCLogOnInfoOfDaily;
import nts.uk.ctx.at.record.dom.monthly.verticaltotal.workclock.pclogon.PCLogonOfMonthly;

/**
 * 月別実績の勤務時刻
 * @author shuichu_ishida
 */
@Getter
public class WorkClockOfMonthly {

	/** 終業時刻 */
	private EndClockOfMonthly endClock;
	/** PCログオン情報 */
	private PCLogonOfMonthly logonInfo;
	
	/**
	 * コンストラクタ
	 */
	public WorkClockOfMonthly(){
		
		this.endClock = new EndClockOfMonthly();
		this.logonInfo = new PCLogonOfMonthly();
	}
	
	/**
	 * ファクトリー
	 * @param endClock 終業時刻
	 * @param logonInfo PCログオン情報
	 * @return 月別実績の勤務時刻
	 */
	public static WorkClockOfMonthly of(
			EndClockOfMonthly endClock,
			PCLogonOfMonthly logonInfo){
		
		WorkClockOfMonthly domain = new WorkClockOfMonthly();
		domain.endClock = endClock;
		domain.logonInfo = logonInfo;
		return domain;
	}
	
	/**
	 * 集計
	 * @param pcLogonInfoOpt 日別実績のPCログオン情報 
	 * @param attendanceTimeOfDaily 日別実績の勤怠時間
	 */
	public void aggregate(
			Optional<PCLogOnInfoOfDaily> pcLogonInfoOpt,
			AttendanceTimeOfDailyPerformance attendanceTimeOfDaily){
		
		// 終業時刻の集計
		this.endClock.aggregate();
		
		// PCログオン情報の集計
		this.logonInfo.aggregate(pcLogonInfoOpt, attendanceTimeOfDaily);
	}
	
	/**
	 * 合算する
	 * @param target 加算対象
	 */
	public void sum(WorkClockOfMonthly target){
		
		this.endClock.sum(target.endClock);
		this.logonInfo.sum(target.logonInfo);
	}
}
