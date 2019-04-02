package nts.uk.ctx.at.record.dom.dailyprocess.calc;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

//import nts.uk.ctx.at.record.dom.breakorgoout.BreakTimeOfDailyPerformance;
import nts.uk.ctx.at.shared.dom.common.time.TimeSpanForCalc;
import nts.uk.ctx.at.shared.dom.worktime.common.TimeZoneRounding;
import nts.uk.shr.com.time.TimeWithDayAttr;

/**
 * 遅刻早退時間帯
 * @author ken_takasu
 *
 */
public class LateLeaveEarlyTimeSheet extends CalculationTimeSheet{

	public LateLeaveEarlyTimeSheet(TimeZoneRounding timeSheet, TimeSpanForCalc calcrange) {
		super(timeSheet, calcrange);
	}
	
	//遅刻再度補正
	public LateLeaveEarlyTimeSheet collectionAgainOfLate(LateLeaveEarlyTimeSheet calcTime, List<TimeSheetOfDeductionItem> breakTimeList) {
		//休憩取得範囲分の時間帯
		//TimeSpanForCalc calcTimeSheet = new TimeSpanForCalc(this.getTimeSheet().getStart(), this.getTimeSheet().getStart().forwardByMinutes(calcTime.calcTotalTime(DeductionAtr.Deduction).valueAsMinutes()+calcTime.calcDedTimeByAtr(DeductionAtr.Deduction).valueAsMinutes()));
		//丸め後の遅刻時間帯
		TimeSpanForCalc lateTimeSheet = new TimeSpanForCalc(this.getTimeSheet().getStart(), this.getTimeSheet().getStart().forwardByMinutes(calcTime.calcTotalTime(DeductionAtr.Deduction).valueAsMinutes()));
		List<TimeSheetOfDeductionItem> hasList = new ArrayList<>();
				
		List<TimeSheetOfDeductionItem> deductionTimeSheetList = breakTimeList.stream().sorted((time1,time2) -> time1.getTimeSheet().getStart().compareTo(time2.getTimeSheet().getStart())).collect(Collectors.toList());
		//控除時間帯分ループ
		for(TimeSheetOfDeductionItem deduTimeSheet:deductionTimeSheetList) {
			//int deductTime = lateTimeSheet.getDuplicatedWith(deductionTimeSheet.getTimeSheet().getTimeSpan()).isPresent()?lateTimeSheet.getDuplicatedWith(deductionTimeSheet.getTimeSheet().getTimeSpan()).get().lengthAsMinutes():0;
			int deductTime = lateTimeSheet.getDuplicatedWith(deduTimeSheet.getTimeSheet().getTimeSpan()).isPresent()?deduTimeSheet.calcTotalTime(DeductionAtr.Deduction).valueAsMinutes():0;
			//控除時間分、終了時刻を後ろにズラす
			lateTimeSheet = new TimeSpanForCalc(lateTimeSheet.getStart(), lateTimeSheet.getEnd().forwardByMinutes(deductTime));
			
			hasList.add(deduTimeSheet);
		}
		LateLeaveEarlyTimeSheet result = new LateLeaveEarlyTimeSheet(new TimeZoneRounding(lateTimeSheet.getStart(), lateTimeSheet.getEnd(), this.getTimeSheet().getRounding()),
																	 lateTimeSheet);
		
		//控除時間帯を保持
		result.addDuplicatedDeductionTimeSheet(hasList,DeductionAtr.Deduction,Optional.empty());
		result.addDuplicatedDeductionTimeSheet(hasList,DeductionAtr.Appropriate,Optional.empty());

		return result;
	}
	
	//再度補正
	public LateLeaveEarlyTimeSheet collectionAgainOfEarly(LateLeaveEarlyTimeSheet calcTime) {
		//休憩取得範囲分の時間帯
//		TimeSpanForCalc calcTimeSheet = new TimeSpanForCalc(this.getTimeSheet().getStart(), this.getTimeSheet().getStart().backByMinutes(calcTime.calcTotalTime().valueAsMinutes()+calcTime.calcDedTimeByAtr(DeductionAtr.Deduction).valueAsMinutes()));
		TimeSpanForCalc calcTimeSheet = new TimeSpanForCalc(new TimeWithDayAttr(this.getTimeSheet().getEnd().valueAsMinutes()-(calcTime.calcTotalTime(DeductionAtr.Deduction).valueAsMinutes()+calcTime.calcDedTimeByAtr(DeductionAtr.Deduction).valueAsMinutes()))
															,this.getTimeSheet().getEnd());
		//丸め後の早退時間帯
		TimeSpanForCalc EarlyTimeSheet = new TimeSpanForCalc(new TimeWithDayAttr(this.getTimeSheet().getEnd().valueAsMinutes()-calcTime.calcTotalTime(DeductionAtr.Deduction).valueAsMinutes())
															 ,this.getTimeSheet().getEnd());
				
		List<TimeZoneRounding> deductionTimeSheetList = this.getDeductionTimeSheet().stream().map(t -> t.getTimeSheet()).sorted((time1,time2) -> time1.getStart().compareTo(time2.getStart())).collect(Collectors.toList());
		//控除時間帯分ループ
		for(TimeZoneRounding deductionTimeSheet:deductionTimeSheetList) {
			TimeSpanForCalc timeSheet = new TimeSpanForCalc(deductionTimeSheet.getStart(), deductionTimeSheet.getEnd());
			int deductTime = calcTimeSheet.getDuplicatedWith(timeSheet).isPresent()?calcTimeSheet.getDuplicatedWith(timeSheet).get().lengthAsMinutes():0;
			//控除時間分、終了時刻を後ろにズラす
			EarlyTimeSheet = new TimeSpanForCalc(new TimeWithDayAttr(EarlyTimeSheet.getStart().valueAsMinutes()-deductTime)
											     ,EarlyTimeSheet.getEnd());
		}
		LateLeaveEarlyTimeSheet result = new LateLeaveEarlyTimeSheet(new TimeZoneRounding(EarlyTimeSheet.getStart(), EarlyTimeSheet.getEnd(), this.getTimeSheet().getRounding()),
																	 EarlyTimeSheet);
		//控除時間帯を保持
		result.addDuplicatedDeductionTimeSheet(this.getDeductionTimeSheet(),DeductionAtr.Deduction,Optional.empty());
		result.addDuplicatedDeductionTimeSheet(this.getDeductionTimeSheet(),DeductionAtr.Appropriate,Optional.empty());

		return result;
	}
}
