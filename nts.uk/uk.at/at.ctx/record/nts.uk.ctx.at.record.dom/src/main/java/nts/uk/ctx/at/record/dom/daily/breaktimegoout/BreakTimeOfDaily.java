package nts.uk.ctx.at.record.dom.daily.breaktimegoout;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import lombok.Getter;
import lombok.val;
import nts.uk.ctx.at.record.dom.breakorgoout.BreakTimeSheet;
import nts.uk.ctx.at.record.dom.daily.DeductionTotalTime;
import nts.uk.ctx.at.record.dom.dailyprocess.calc.CalculationRangeOfOneDay;
import nts.uk.ctx.at.record.dom.dailyprocess.calc.ConditionAtr;
import nts.uk.ctx.at.record.dom.dailyprocess.calc.DeductionAtr;
import nts.uk.ctx.at.record.dom.dailyprocess.calc.TimeSheetRoundingAtr;
import nts.uk.ctx.at.shared.dom.common.time.AttendanceTime;
import nts.uk.ctx.at.shared.dom.workrule.outsideworktime.StatutoryAtr;

/**
 * 日別実績の休憩時間
 * @author keisuke_hoshina
 *
 */
@Getter
public class BreakTimeOfDaily {
	/** 計上用合計時間: 控除合計時間 */
	private DeductionTotalTime toRecordTotalTime;
	/** 控除用合計時間: 控除合計時間 */
	private DeductionTotalTime deductionTotalTime;
	/** 休憩回数: 休憩外出回数 */
	private BreakTimeGoOutTimes gooutTimes;
	/** 勤務間時間: 勤怠時間 */
	private AttendanceTime workTime;
	/** 補正後時間帯: 休憩時間帯 */
	private List<BreakTimeSheet> breakTimeSheet;
	
	
	/**
	 * Constructor
	 * @param record 計上用
	 * @param deduction 控除用
	 */
	private BreakTimeOfDaily(DeductionTotalTime record,DeductionTotalTime deduction) {
		this.toRecordTotalTime = record;
		this.deductionTotalTime = deduction;
	}
	
	public BreakTimeOfDaily(DeductionTotalTime toRecordTotalTime, DeductionTotalTime deductionTotalTime,
			BreakTimeGoOutTimes gooutTimes, AttendanceTime workTime, List<BreakTimeSheet> breakTimeSheet) {
		super();
		this.toRecordTotalTime = toRecordTotalTime;
		this.deductionTotalTime = deductionTotalTime;
		this.gooutTimes = gooutTimes;
		this.workTime = workTime;
		this.breakTimeSheet = breakTimeSheet;
	}
	
	/**
	 * 控除、計上用両方を受け取った時間に入れ替える
	 * @return 日別実績の休憩時間
	 */
	public static BreakTimeOfDaily sameTotalTime(DeductionTotalTime deductionTime) {
		return new BreakTimeOfDaily(deductionTime,deductionTime);
	}
	
	/**
	 * 全ての休憩時間を算出する指示クラス
	 * @param oneDay 1日の計算範囲
	 * @return 日別実績の休憩時間
	 */
	public static BreakTimeOfDaily calcTotalBreakTime(CalculationRangeOfOneDay oneDay) {
		//計上用計算時間
		val recordCalcTime = calculationDedBreakTime(DeductionAtr.Appropriate,oneDay);
		//控除用計算時間
		val dedCalcTime = calculationDedBreakTime(DeductionAtr.Deduction,oneDay);
		//休憩回数
		BreakTimeGoOutTimes goOutTimes = new BreakTimeGoOutTimes(1);
		//勤務間時間
		AttendanceTime duringTime = new AttendanceTime(0);
		//補正後時間帯
		List<BreakTimeSheet> breakTimeSheets = new ArrayList<>();
		
		return new BreakTimeOfDaily(recordCalcTime,dedCalcTime,goOutTimes,duringTime,breakTimeSheets);
	}

	/**
	 *　合計時間算出
	 * @param oneDay 
	 * @return
	 */
	private static DeductionTotalTime calculationDedBreakTime(DeductionAtr dedAtr, CalculationRangeOfOneDay oneDay) {
		return createDudAllTime(ConditionAtr.BREAK,dedAtr,TimeSheetRoundingAtr.PerTimeSheet,oneDay);
	}
	
	private static DeductionTotalTime createDudAllTime(ConditionAtr conditionAtr, DeductionAtr dedAtr,
			TimeSheetRoundingAtr pertimesheet, CalculationRangeOfOneDay oneDay) {
		val withinDedTime = oneDay.calcWithinTotalTime(conditionAtr,dedAtr,StatutoryAtr.Statutory,pertimesheet);
		val excessDedTime = oneDay.calcWithinTotalTime(conditionAtr,dedAtr,StatutoryAtr.Excess,pertimesheet);
		return DeductionTotalTime.of(withinDedTime.addMinutes(excessDedTime.getTime(), excessDedTime.getCalcTime()),
									  withinDedTime,
									  excessDedTime);
	}
	
//	private static getBreakTimeSheet(CalculationRangeOfOneDay oneDay) {
//		List<BreakTimeSheet> totalList = new ArrayList<>();
//		DeductionAtr dedAtr = DeductionAtr.Appropriate;
//		ConditionAtr conAtr = ConditionAtr.BREAK;
//		if(oneDay.getWithinWorkingTimeSheet().isPresent()) {
//			totalList.addAll(oneDay.getWithinWorkingTimeSheet().get().getDedTimeSheetByDedAtr(dedAtr, conAtr));
//		}
//		val overTime;
//		val holidayWork;
//		
//		return totalList;
//	}


	
	
	


	
}
