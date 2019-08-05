package nts.uk.ctx.at.record.dom.monthly.verticaltotal.workclock.pclogon;

import java.util.Optional;

import lombok.Getter;
import nts.uk.ctx.at.record.dom.actualworkinghours.AttendanceTimeOfDailyPerformance;
import nts.uk.ctx.at.record.dom.daily.attendanceleavinggate.PCLogOnInfoOfDaily;
import nts.uk.ctx.at.record.dom.dailyprocess.calc.PredetermineTimeSetForCalc;
import nts.uk.ctx.at.record.dom.worktime.TimeLeavingOfDailyPerformance;
import nts.uk.ctx.at.shared.dom.worktype.WorkType;

/**
 * 月別実績のPCログオン情報
 * @author shuichi_ishida
 */
@Getter
public class PCLogonOfMonthly {

	/** PCログオン時刻 */
	private PCLogonClockOfMonthly logonClock;
	/** PCログオン乖離 */
	private PCLogonDivergenceOfMonthly logonDivergence;
	
	/**
	 * コンストラクタ
	 */
	public PCLogonOfMonthly(){
		
		this.logonClock = new PCLogonClockOfMonthly();
		this.logonDivergence = new PCLogonDivergenceOfMonthly();
	}
	
	/**
	 * ファクトリー
	 * @param logonClock PCログオン時刻
	 * @param logonDivergence PCログオン乖離
	 * @return 月別実績のPCログオン情報
	 */
	public static PCLogonOfMonthly of(
			PCLogonClockOfMonthly logonClock,
			PCLogonDivergenceOfMonthly logonDivergence){
		
		PCLogonOfMonthly domain = new PCLogonOfMonthly();
		domain.logonClock = logonClock;
		domain.logonDivergence = logonDivergence;
		return domain;
	}
	
	/**
	 * 集計
	 * @param pcLogonInfoOpt 日別実績のPCログオン情報 
	 * @param attendanceTimeOfDaily 日別実績の勤怠時間
	 * @param timeLeavingOfDaily 日別実績の出退勤
	 * @param isWeekday 平日かどうか
	 * @param workType 勤務種類
	 * @param predTimeSetForCalc 計算用所定時間設定
	 */
	public void aggregate(
			Optional<PCLogOnInfoOfDaily> pcLogonInfoOpt,
			AttendanceTimeOfDailyPerformance attendanceTimeOfDaily,
			TimeLeavingOfDailyPerformance timeLeavingOfDaily,
			boolean isWeekday,
			WorkType workType,
			PredetermineTimeSetForCalc predTimeSetForCalc){
		
		// PCログオン時刻
		this.logonClock.aggregate(pcLogonInfoOpt, timeLeavingOfDaily, workType, predTimeSetForCalc);
		
		// PCログオン乖離
		this.logonDivergence.aggregate(pcLogonInfoOpt, attendanceTimeOfDaily, timeLeavingOfDaily, isWeekday,
				workType, predTimeSetForCalc);
	}
	
	/**
	 * 合算する
	 * @param target 加算対象
	 */
	public void sum(PCLogonOfMonthly target){
		
		this.logonClock.sum(target.logonClock);
		this.logonDivergence.sum(target.logonDivergence);
	}
}
