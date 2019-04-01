package nts.uk.ctx.at.record.dom.dailyprocess.calc.withinstatutory;

import java.util.List;
import java.util.Optional;

import lombok.Value;
import lombok.val;
import nts.uk.ctx.at.record.dom.dailyprocess.calc.DeductionAtr;
import nts.uk.ctx.at.record.dom.dailyprocess.calc.DeductionTimeSheet;
import nts.uk.ctx.at.record.dom.dailyprocess.calc.PredetermineTimeSetForCalc;
import nts.uk.ctx.at.record.dom.dailyprocess.calc.TimeSheetOfDeductionItem;
import nts.uk.ctx.at.record.dom.worktime.TimeLeavingWork;
import nts.uk.ctx.at.shared.dom.common.time.TimeSpanForCalc;
import nts.uk.ctx.at.shared.dom.worktime.common.GraceTimeSetting;
import nts.uk.ctx.at.shared.dom.worktime.flexset.CoreTimeSetting;
import nts.uk.ctx.at.shared.dom.worktime.predset.TimezoneUse;
import nts.uk.ctx.at.shared.dom.worktype.AttendanceHolidayAttr;
import nts.uk.ctx.at.shared.dom.worktype.WorkType;
import nts.uk.shr.com.time.TimeWithDayAttr;

/**
 * 遅刻判断時刻
 * 
 * @author ken_takasu
 *
 */
@Value
public class LateDecisionClock {
	//遅刻判断時刻
	private TimeWithDayAttr lateDecisionClock;
	//勤務No
	private int workNo;

	
	/**
	 * 遅刻判断時刻を作成する
	 * @param workNo 勤務No
	 * @param predetermineTimeSet 所定時間帯
	 * @param deductionTimeSheet　控除時間帯
	 * @param lateGraceTime　遅刻猶予時間
	 * @param breakTimeList 
	 * @return
	 */
	public static Optional<LateDecisionClock> create(
			int workNo,
			PredetermineTimeSetForCalc predetermineTimeSet,
			DeductionTimeSheet deductionTimeSheet,
			GraceTimeSetting lateGraceTime,
			TimeLeavingWork timeLeavingWork,
			Optional<CoreTimeSetting> coreTimeSetting,WorkType workType, 
			List<TimeSheetOfDeductionItem> breakTimeList) {

		Optional<TimezoneUse> predetermineTimeSheet = predetermineTimeSet.getTimeSheets(workType.getDailyWork().decisionNeedPredTime(),workNo);
		if(!predetermineTimeSheet.isPresent())
			return Optional.empty();
		TimeWithDayAttr decisionClock = new TimeWithDayAttr(0);

		//計算範囲取得
		Optional<TimeSpanForCalc> calｃRange = getCalcRange(predetermineTimeSheet.get(),timeLeavingWork,coreTimeSetting,predetermineTimeSet,workType.getDailyWork().decisionNeedPredTime());
		if(calｃRange.isPresent()) {
			if (lateGraceTime.isZero()) {
				// 猶予時間が0：00の場合、所定時間の開始時刻を判断時刻にする
				decisionClock = calｃRange.get().getStart();
			} else {
				// 猶予時間帯の作成                                                                                                   
				TimeSpanForCalc graceTimeSheet = new TimeSpanForCalc(calｃRange.get().getStart(),
																	 calｃRange.get().getStart().forwardByMinutes(lateGraceTime.getGraceTime().valueAsMinutes()));
				// 重複している控除分をずらす(休憩)
				List<TimeSheetOfDeductionItem> breakTimeSheetList = breakTimeList;
				//EnterPriseでは短時間系との重複はとっていないためコメントアウト
//				List<TimeZoneRounding> breakTimeSheetList = deductionTimeSheet.getForDeductionTimeZoneList().stream().filter(tc -> tc.getDeductionAtr().isBreak() || tc.getDeductionAtr().isChildCare()).map(t -> t.getTimeSheet()).collect(Collectors.toList());
				
				//控除時間帯(休憩＆短時間)と猶予時間帯の重複を調べ猶予時間帯の調整
				for(TimeSheetOfDeductionItem breakTime:breakTimeSheetList) {
					TimeSpanForCalc deductTime = new TimeSpanForCalc(breakTime.getTimeSheet().getStart(),breakTime.getTimeSheet().getEnd());
					val dupRange = deductTime.getDuplicatedWith(graceTimeSheet);
						if(dupRange.isPresent()) {
							graceTimeSheet = new TimeSpanForCalc(graceTimeSheet.getStart(), 
																 graceTimeSheet.getEnd().forwardByMinutes(breakTime.calcTotalTime(DeductionAtr.Deduction).valueAsMinutes()));
						}
				}
				decisionClock = graceTimeSheet.getEnd();
			}
			//補正後の猶予時間帯の開始時刻を判断時刻とする
			return Optional.of(new LateDecisionClock(decisionClock, workNo));
		}
		return Optional.empty();
	}
	
	/**
	 * 遅刻時間の計算範囲の取得
	 * @param predetermineTimeSet
	 * @param timeLeavingWork
	 * @return
	 */
	static public Optional<TimeSpanForCalc> getCalcRange(TimezoneUse predetermineTimeSet,
														 TimeLeavingWork timeLeavingWork,
														 Optional<CoreTimeSetting> coreTimeSetting,
														 PredetermineTimeSetForCalc predetermineTimeSetForCalc,AttendanceHolidayAttr attr)
	{
		//出勤時刻
		TimeWithDayAttr attendance = null;
		if(timeLeavingWork.getAttendanceStamp().isPresent()) {
			if(timeLeavingWork.getAttendanceStamp().get().getStamp().isPresent()) {
				if(timeLeavingWork.getAttendanceStamp().get().getStamp().get().getTimeWithDay()!=null) {
					attendance =  timeLeavingWork.getAttendanceStamp().get().getStamp().get().getTimeWithDay();
				}
			}
		}
		//フレックス勤務では無い場合の計算範囲
		Optional<TimeSpanForCalc> result = Optional.empty();
		if(attendance!=null) {
			result = Optional.of(new TimeSpanForCalc(predetermineTimeSet.getStart(), attendance));
			//フレ勤務かどうか判断
			if(coreTimeSetting.isPresent()) {
				//コアタイム使用するかどうか
				if(coreTimeSetting.get().getTimesheet().isNOT_USE()) {
					return Optional.empty();
				}
				val coreTime = coreTimeSetting.get().getDecisionCoreTimeSheet(attr, predetermineTimeSetForCalc.getAMEndTime(),predetermineTimeSetForCalc.getPMStartTime());
				if(attendance.greaterThanOrEqualTo(coreTime.getEndTime())) {
					return Optional.of(new TimeSpanForCalc(coreTime.getStartTime(), coreTime.getEndTime()));
				}
				return Optional.of(new TimeSpanForCalc(coreTime.getStartTime(), attendance));
			}
			if(attendance.greaterThanOrEqualTo(predetermineTimeSet.getEnd())) {
				result = Optional.of(new TimeSpanForCalc(predetermineTimeSet.getStart(), predetermineTimeSet.getEnd()));
			}
		}
		return result;
	}
	
	/**
	 * 遅刻しているか判断する
	 * @param time
	 * @return
	 */
	public boolean isLate(TimeWithDayAttr time) {
		return time.greaterThan(this.lateDecisionClock);
	}
}
