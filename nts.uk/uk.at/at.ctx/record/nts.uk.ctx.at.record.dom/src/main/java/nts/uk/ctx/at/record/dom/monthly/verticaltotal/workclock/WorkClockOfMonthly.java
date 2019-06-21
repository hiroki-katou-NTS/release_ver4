package nts.uk.ctx.at.record.dom.monthly.verticaltotal.workclock;

import java.util.Optional;

import lombok.Getter;
import nts.uk.ctx.at.record.dom.actualworkinghours.AttendanceTimeOfDailyPerformance;
import nts.uk.ctx.at.record.dom.daily.attendanceleavinggate.PCLogOnInfoOfDaily;
import nts.uk.ctx.at.record.dom.daily.optionalitemtime.AnyItemValueOfDaily;
import nts.uk.ctx.at.record.dom.dailyprocess.calc.PredetermineTimeSetForCalc;
import nts.uk.ctx.at.record.dom.monthly.verticaltotal.workclock.pclogon.PCLogonOfMonthly;
import nts.uk.ctx.at.record.dom.worktime.TimeLeavingOfDailyPerformance;
import nts.uk.ctx.at.shared.dom.worktype.WorkType;

/**
 * 月別実績の勤務時刻
 * @author shuichi_ishida
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
	 * @param workType 勤務種類
	 * @param pcLogonInfoOpt 日別実績のPCログオン情報 
	 * @param attendanceTimeOfDaily 日別実績の勤怠時間
	 * @param timeLeavingOfDaily 日別実績の出退勤
	 * @param predTimeSetForCalc 計算用所定時間設定
	 * @param anyItemValueOpt 日別実績の任意項目
	 */
	public void aggregate(
			WorkType workType,
			Optional<PCLogOnInfoOfDaily> pcLogonInfoOpt,
			AttendanceTimeOfDailyPerformance attendanceTimeOfDaily,
			TimeLeavingOfDailyPerformance timeLeavingOfDaily,
			PredetermineTimeSetForCalc predTimeSetForCalc,
			Optional<AnyItemValueOfDaily> anyItemValueOpt){
		
		// 終業時刻の集計
		this.endClock.aggregate(workType, timeLeavingOfDaily, predTimeSetForCalc);
		
		// PCログオン情報の集計
		this.logonInfo.aggregate(pcLogonInfoOpt, attendanceTimeOfDaily, timeLeavingOfDaily, anyItemValueOpt);
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
