package nts.uk.ctx.at.record.dom.dailyprocess.calc;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.util.Comparator.*;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.val;
import nts.gul.util.value.Finally;
import nts.uk.ctx.at.record.dom.daily.LateTimeOfDaily;
import nts.uk.ctx.at.record.dom.daily.TimeWithCalculation;
import nts.uk.ctx.at.record.dom.dailyprocess.calc.withinstatutory.LateDecisionClock;
import nts.uk.ctx.at.record.dom.dailyprocess.calc.withinstatutory.WithinWorkTimeFrame;
import nts.uk.ctx.at.record.dom.dailyprocess.calc.withinstatutory.WithinWorkTimeSheet;
import nts.uk.ctx.at.record.dom.worktime.TimeLeavingWork;
import nts.uk.ctx.at.shared.dom.common.time.AttendanceTime;
import nts.uk.ctx.at.shared.dom.common.time.TimeSpanForCalc;
import nts.uk.ctx.at.shared.dom.common.timerounding.Rounding;
import nts.uk.ctx.at.shared.dom.common.timerounding.TimeRoundingSetting;
import nts.uk.ctx.at.shared.dom.common.timerounding.Unit;
import nts.uk.ctx.at.shared.dom.vacation.setting.addsettingofworktime.HolidayCalcMethodSet;
import nts.uk.ctx.at.shared.dom.worktime.common.EmTimeZoneSet;
import nts.uk.ctx.at.shared.dom.worktime.common.GraceTimeSetting;
import nts.uk.ctx.at.shared.dom.worktime.common.OtherEmTimezoneLateEarlySet;
import nts.uk.ctx.at.shared.dom.worktime.common.TimeZoneRounding;
import nts.uk.ctx.at.shared.dom.worktime.flexset.CoreTimeSetting;
import nts.uk.ctx.at.shared.dom.worktime.predset.TimezoneUse;
import nts.uk.shr.com.time.TimeWithDayAttr;

/**
 * 遅刻時間帯
 * @author keisuke_hoshina
 *
 */
public class LateTimeSheet{
	
	// 遅刻していない場合はempty
	@Getter
	private Optional<LateLeaveEarlyTimeSheet> forRecordTimeSheet;
	
	@Getter
	private Optional<LateLeaveEarlyTimeSheet> forDeducationTimeSheet;
	
	@Getter
	//今は一時的にint型で作成しているが、本来はworkNo型
	private int workNo;
	
	private Optional<DeductionOffSetTime> OffsetTime;
	
	
	public LateTimeSheet(
			Optional<LateLeaveEarlyTimeSheet> recordTimeSheet,
			Optional<LateLeaveEarlyTimeSheet> deductionTimeSheet,
			int workNo,
			Optional<DeductionOffSetTime> OffsetTime) {
		
		this.forRecordTimeSheet = recordTimeSheet;
		this.forDeducationTimeSheet = deductionTimeSheet;
		this.workNo = workNo;
		this.OffsetTime = OffsetTime;
	}
	
	public static LateTimeSheet createAsLate(LateLeaveEarlyTimeSheet recordTimeSheet,LateLeaveEarlyTimeSheet deductionTimeSheet,int workNo,Optional<LateTimeOfDaily> lateTime,Optional<DeductionOffSetTime> OffsetTime) {
		return new LateTimeSheet(Optional.of(recordTimeSheet), Optional.of(deductionTimeSheet),workNo,OffsetTime);
	}
	
	public static LateTimeSheet createAsNotLate() {
		return new LateTimeSheet(Optional.empty(), Optional.empty(),1,Optional.empty());
	}
	
	
	/**
	 * 遅刻時間帯の作成
	 * @param specifiedTimeSheet
	 * @param goWorkTime
	 * @param workNo
	 * @param classification
	 * @param lateDecisionClock
	 * @return
	 */
	public static LateTimeSheet createLateTimeSheet(
			LateDecisionClock lateDecisionClock,
			TimeWithDayAttr attendance
			,GraceTimeSetting graceTimeSetting
			,EmTimeZoneSet duplicateTimeSheet
			,DeductionTimeSheet deductionTimeSheet
			,Optional<CoreTimeSetting> coreTimeSetting
			,TimezoneUse predetermineTimeSet
			,int workNo) {

		if(attendance!=null) {
			//出勤時刻と遅刻判断時刻を比較	
			if(lateDecisionClock.getLateDecisionClock().greaterThan(attendance)
					||!graceTimeSetting.isIncludeWorkingHour()){//猶予時間を加算しない場合
				
				//遅刻控除時間帯の作成
				Optional<LateLeaveEarlyTimeSheet> lateDeductTimeSheet = createLateLeaveEarlyTimeSheet(DeductionAtr.Deduction,attendance,coreTimeSetting,predetermineTimeSet,duplicateTimeSheet,deductionTimeSheet);
				//遅刻時間帯の作成
				Optional<LateLeaveEarlyTimeSheet> lateAppTimeSheet = createLateLeaveEarlyTimeSheet(DeductionAtr.Appropriate,attendance,coreTimeSetting,predetermineTimeSet,duplicateTimeSheet,deductionTimeSheet);
				
				LateTimeSheet lateTimeSheet = new LateTimeSheet(lateAppTimeSheet,lateDeductTimeSheet, workNo, Optional.empty());
				
				return lateTimeSheet;
			}	
		}
		return LateTimeSheet.createAsNotLate();//遅刻していない
	}
	
	/**
	 * 遅刻早退時間帯作成
	 * @param goWorkTime
	 * @param workTime
	 * @param deductionTimeSheet
	 * @param workNo
	 * @param deductionAtr
	 * @return 控除用または計上用の遅刻早退時間帯
	 */
	private static Optional<LateLeaveEarlyTimeSheet> createLateLeaveEarlyTimeSheet(
			DeductionAtr deductionAtr,
			TimeWithDayAttr attendance
			,Optional<CoreTimeSetting> coreTimeSetting
			,TimezoneUse predetermineTimeSet
			,EmTimeZoneSet duplicateTimeSheet
			,DeductionTimeSheet deductionTimeSheet){

		//遅刻時間帯の作成
		Optional<LateLeaveEarlyTimeSheet> instance = createLateTimeSheetInstance(deductionAtr,
				attendance
				,coreTimeSetting
				,predetermineTimeSet
				,duplicateTimeSheet
				,deductionTimeSheet);
			
		//遅刻時間を計算

		//遅刻時間帯を再度補正
		
		return instance;
	}
	
	private static Optional<LateLeaveEarlyTimeSheet> createLateTimeSheetInstance(
			DeductionAtr deductionAtr,
			TimeWithDayAttr attendance
			,Optional<CoreTimeSetting> coreTimeSetting
			,TimezoneUse predetermineTimeSet
			,EmTimeZoneSet duplicateTimeSheet
			,DeductionTimeSheet deductionTimeSheet){
		//控除区分を基に丸め設定を取得しておく
		//TimeRoundingSetting timeRoundingSetting = lateLeaveEarlySettingOfWorkTime.getTimeRoundingSetting(deductionAtr);

		//計算範囲の取得
		TimeSpanForCalc calcRange = LateDecisionClock.getCalcRange(predetermineTimeSet, attendance, coreTimeSetting);
		//遅刻時間帯の作成
		TimeWithDayAttr start = calcRange.getStart();
		TimeWithDayAttr end = duplicateTimeSheet.getTimezone().getEnd().greaterThan(attendance)?duplicateTimeSheet.getTimezone().getEnd():attendance;
		
		LateLeaveEarlyTimeSheet timeSheet = new LateLeaveEarlyTimeSheet(
								new TimeZoneRounding(start,end,new TimeRoundingSetting(Unit.ROUNDING_TIME_1MIN,Rounding.ROUNDING_DOWN)),
								new TimeSpanForCalc(start,end));
		
		List<TimeSheetOfDeductionItem> dudctionList = deductionTimeSheet.getDupliRangeTimeSheet(new TimeSpanForCalc(start,end), deductionAtr);
		timeSheet.setDeductionTimeSheet(dudctionList);
		return Optional.of(timeSheet);
	}
	
	
	/**
	 * 遅刻計上時間の計算
	 * @return
	 */
	public TimeWithCalculation calcForRecordTime(
			boolean late //日別実績の計算区分.遅刻早退の自動計算設定.遅刻
			) {
		//遅刻時間の計算
		AttendanceTime calcforRecordTime = this.forRecordTimeSheet.get().calcTotalTime();
		//インターバル免除時間を控除する
		
		//遅刻計上時間の作成
		TimeWithCalculation lateTime = late?TimeWithCalculation.sameTime(new AttendanceTime(calcforRecordTime.minute())):TimeWithCalculation.createTimeWithCalculation(new AttendanceTime(0),new AttendanceTime(calcforRecordTime.minute()));	
		return lateTime;
	}
	
	/**
	 * 遅刻控除時間の計算
	 * @return
	 */
	public TimeWithCalculation calcDedctionTime(
			boolean late, //日別実績の計算区分.遅刻早退の自動計算設定.遅刻
			HolidayCalcMethodSet holidayCalcMethodSet
			) {
		TimeWithCalculation lateDeductionTime = TimeWithCalculation.sameTime(new AttendanceTime(0));
		if(holidayCalcMethodSet.getWorkTimeCalcMethodOfHoliday().getDetailSet().getDeductLateLeaveEarly().isUse()) {//控除する場合
			AttendanceTime calcDeductionTime = this.forDeducationTimeSheet.get().calcTotalTime();
			lateDeductionTime =  late?TimeWithCalculation.sameTime(new AttendanceTime(calcDeductionTime.minute())):TimeWithCalculation.createTimeWithCalculation(new AttendanceTime(0),new AttendanceTime(calcDeductionTime.minute()));
		}
		return lateDeductionTime;
	}
	
	
	
//	/**
//	 * 控除項目の時間帯（List）を取得する
//	 * @return
//	 */
//	public List<TimeSheetOfDeductionItem> getTimeSheetOfDeductionItem(DeductionTimeSheet deductionTimeSheet,TimeSpanForCalc initiaLlateTimeSheet){
//		//遅刻時間帯に重複する控除時間帯格納用リスト
//		List<TimeSheetOfDeductionItem> duplicateTimeSheetList = new ArrayList<>();
//		//控除用時間帯リスト分ループ
//		for(TimeSheetOfDeductionItem timeSheetOfDeductionItem : deductionTimeSheet.getForDeductionTimeZoneList()) {
//			TimeSpanForCalc duplicateTimeSheet = initiaLlateTimeSheet.getDuplicatedWith(timeSheetOfDeductionItem.getTimeSheet()).orElse(null);
//			if(duplicateTimeSheet != null) {
//				duplicateTimeSheetList.add(timeSheetOfDeductionItem);
//			}
//		}
//		return duplicateTimeSheetList;
//	}
//	
//	/**
//	 * 遅刻時間の計算
//	 * @param lateTimeSpan
//	 * @param deductionTimeSheet
//	 * @return　遅刻時間
//	 */
//	public int getLateTime(LateLeaveEarlyTimeSheet lateLeaveEarlyTimeSheet) {
//		//計算範囲を取得
//		TimeSpanForCalc calcRange = lateLeaveEarlyTimeSheet.getCalcrange();
//		//遅刻時間を計算
//		int lateTime = calcRange.lengthAsMinutes();
//		//控除時間帯を取得	
//		List<TimeSheetOfDeductionItem> deductionTimeSheet = lateLeaveEarlyTimeSheet.getDeductionTimeSheets();
//		//控除時間の計算
//		int deductionTime = calcDeductionTime(deductionTimeSheet);
//		//遅刻時間から控除時間を控除する	
//		lateTime -= deductionTime;
//		//丸め処理
//		
//		return lateTime;
//	}
//	
//	/**
//	 * 控除時間の計算
//	 * @return
//	 */
//	public int calcDeductionTime(List<TimeSheetOfDeductionItem> deductionTimeSheet) {
//		int totalDeductionTime = 0;
//		//控除時間帯分ループ
//		for(TimeSheetOfDeductionItem timeSheetOfDeductionItem : deductionTimeSheet) {
//			//控除時間の計算
//			int deductionTime = timeSheetOfDeductionItem.getTimeSheet().lengthAsMinutes();
//			//丸め処理
//			
//			//丸め後の値をtotalDeductionTimeに加算
//			totalDeductionTime += deductionTime;
//		}
//		return totalDeductionTime;
//	}
//	
//	
//	/**
//	 * 遅刻時間帯を再度補正
//	 * @param lateTimeSheet
//	 * @param lateTime
//	 * @param deductionTimeSheet
//	 * @return 補正後の遅刻時間帯
//	 */
//	public LateLeaveEarlyTimeSheet getCorrectedLateTimeSheet(
//			LateLeaveEarlyTimeSheet lateLeaveEarlyTimeSheet,
//			int lateTime,
//			DeductionTimeSheet deductionTimeSheet) {
//		//開始から丸め後の遅刻時間分を加算した時間帯を作成
//		TimeSpanForCalc lateTimeSheet = new TimeSpanForCalc(
//				lateLeaveEarlyTimeSheet.getTimeSheet().getStart(),
//				lateLeaveEarlyTimeSheet.getTimeSheet().getStart().backByMinutes(lateTime));
//		//全ての控除時間帯を取得しソート
//		List<TimeSheetOfDeductionItem> deduTimeSheetList = deductionTimeSheet.getForDeductionTimeZoneList().stream()
//				.sorted(comparing(e -> e.getTimeSheet().getStart()))
//				.collect(Collectors.toList());
//		//入れ物だけ作成
//		List<TimeSheetOfDeductionItem> newdeduTimeSheetList = new ArrayList<>();
//		//控除時間帯分ループ
//		for(TimeSheetOfDeductionItem deduTimeSheet : deduTimeSheetList) {
//			//計算範囲の時間帯を作成
//			TimeSpanForCalc deductionTimeSpan = deduTimeSheet.getTimeSheet().getSpan().getDuplicatedWith(lateTimeSheet).orElse(null);
//			//控除時間の計算
//			int deductionTime = 0;
//			if(deductionTimeSpan != null) {
//				newdeduTimeSheetList.add(deduTimeSheet);
//				deductionTime += deductionTimeSpan.lengthAsMinutes();
//				//丸め
//				
//			}
//			lateTimeSheet.shiftEndAhead(deductionTime);
//		}
//		LateLeaveEarlyTimeSheet collectLateLeaveEarlyTimeSheet = new LateLeaveEarlyTimeSheet(
//				lateLeaveEarlyTimeSheet.getTimeSheet().newTimeSpan(lateTimeSheet),
//				lateLeaveEarlyTimeSheet.getCalcrange(),
//				newdeduTimeSheetList,
//				lateLeaveEarlyTimeSheet.getBonusPayTimeSheet(),
//				lateLeaveEarlyTimeSheet.getMidNightTimeSheet());
//		return collectLateLeaveEarlyTimeSheet;
//	}
//	
//	public int getLateDeductionTime() {
//		return this.forDeducationTimeSheet.map(ts -> ts.lengthAsMinutes()).orElse(0);
//	}
//	
//	public boolean isLate() {
//		return this.forDeducationTimeSheet.isPresent();
//	}
//	
//	public TimeSpanWithRounding deductForm(TimeSpanWithRounding source) {
//		
//		if (!this.isLate()) {
//			return source;
//		}
//
//		//遅刻時間帯の終了時刻を開始時刻にする
//		return source.newTimeSpan(
//				source.shiftOnlyStart(this.forDeducationTimeSheet.get().getEnd()));
//	}
//	
//	
//	/**
//	 * 流動勤務の場合の遅刻控除時間の計算
//	 * @return
//	 */
//	public LateTimeSheet lateTimeCalcForFluid(
//			WithinWorkTimeFrame withinWorkTimeFrame,
//			TimeSpanForCalc lateRangeForCalc,
//			WorkTimeCommonSet workTimeCommonSet,
//			LateDecisionClock lateDecisionClock,
//			DeductionTimeSheet deductionTimeSheet) {
//		
//		TimeWithDayAttr goWorkTime = lateRangeForCalc.getEnd();
//		
//		if(lateDecisionClock.isLate(goWorkTime)){
//			
//			return withinWorkTimeFrame.getTimeSheet().getDuplicatedWith(lateRangeForCalc)
//					.map(initialLateTimeSheet -> {
//						val revisedLateTimeSheet = reviceLateTimeSheetForFluid(initialLateTimeSheet, deductionTimeSheet);						
//						return LateTimeSheet.createAsLate(revisedLateTimeSheet);
//					})
//					.orElse(LateTimeSheet.createAsNotLate());
//		}
//		return LateTimeSheet.createAsNotLate();//遅刻していない場合
//	}
//	
//	/**
//	 * 遅刻時間帯作成(流動勤務)
//	 * @param goWorkTime
//	 * @param workTime
//	 * @param deductionTimeSheet
//	 * @param workNo
//	 * @param deductionAtr
//	 * @return 控除用または計上用の早退時間帯
//	 */
//	private TimeSpanForCalc reviceLateTimeSheetForFluid(
//			TimeSpanForCalc lateTimeSheet,/*遅刻時間帯の作成*/
//			DeductionTimeSheet deductionTimeSheet) {
//		
//		
//		//遅刻時間を計算
//		int lateTime = getLateTimeForFluid(lateTimeSheet, deductionTimeSheet);
//		//遅刻時間帯を再度補正
//		lateTimeSheet = getCorrectedLateTimeSheet(lateTimeSheet, lateTime, deductionTimeSheet);		
//		//丸め設定を保持（未作成）
//		
//		return lateTimeSheet;
//	}
//
//	/**
//	 * 遅刻時間の計算(流動勤務)
//	 * @param lateTimeSpan
//	 * @param deductionTimeSheet
//	 * @return　遅刻時間
//	 */
//	public int getLateTimeForFluid(
//			TimeSpanForCalc lateTimeSpan,
//			DeductionTimeSheet deductionTimeSheet) {
//		//遅刻時間を計算
//		int lateTime = lateTimeSpan.lengthAsMinutes();
//
//		//遅刻時間の取得（未作成）	
//		
//		//控除時間の計算（未作成）	
//		
//		//遅刻時間から控除時間を控除する（未作成）	
//		//lateTime -= deductionTime;
//
//		//丸め処理（未作成）		
//		
//		return lateTime;
//	}
//	
//	
//	/**
//	 * 遅刻時間の計算　（時間帯作成後の処理で使用する）
//	 * @return
//	 */
//	public LateTimeSheet createLateTimeSheet(LateTimeSheet baseLateTimeSheet) {
//		//遅刻時間の計算
//		
//		
//		
//		
//		
//	}
//	
}
