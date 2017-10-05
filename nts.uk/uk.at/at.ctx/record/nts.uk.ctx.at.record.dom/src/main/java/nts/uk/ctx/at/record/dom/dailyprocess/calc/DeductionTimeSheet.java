package nts.uk.ctx.at.record.dom.dailyprocess.calc;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.val;
import nts.gul.util.value.Finally;
import nts.uk.ctx.at.record.dom.daily.AttendanceLeavingWork;
import nts.uk.ctx.at.record.dom.daily.AttendanceLeavingWorkOfDaily;
import nts.uk.ctx.at.record.dom.daily.DeductionTotalTime;
import nts.uk.ctx.at.record.dom.daily.TimeWithCalculation;
import nts.uk.ctx.at.record.dom.daily.breaktimegoout.BreakTimeSheet;
import nts.uk.ctx.at.record.dom.daily.breaktimegoout.BreakTimeSheetOfDaily;
import nts.uk.ctx.at.record.dom.daily.breaktimegoout.GoOutTimeSheetOfDailyWork;
import nts.uk.ctx.at.record.dom.daily.calcset.SetForNoStamp;
import nts.uk.ctx.at.record.dom.dailyprocess.calc.withinstatutory.WithinWorkTimeFrame;
import nts.uk.ctx.at.shared.dom.common.time.AttendanceTime;
import nts.uk.ctx.at.shared.dom.common.time.TimeSpanForCalc;
import nts.uk.ctx.at.shared.dom.worktime.WorkTimeDivision;
import nts.uk.ctx.at.shared.dom.worktime.WorkTimeMethodSet;
import nts.uk.ctx.at.shared.dom.worktime.CommomSetting.BreakSetOfCommon;
import nts.uk.ctx.at.shared.dom.worktime.CommomSetting.CalcMethodIfLeaveWorkDuringBreakTime;
import nts.uk.ctx.at.shared.dom.worktime.CommonSetting.lateleaveearly.LateLeaveEarlyClassification;
import nts.uk.ctx.at.shared.dom.worktime.fixedworkset.set.FixRestCalcMethod;
import nts.uk.ctx.at.shared.dom.worktime.fixedworkset.timespan.TimeSpanWithRounding;
import nts.uk.ctx.at.shared.dom.worktime.fluidworkset.FluRestTimeSetting;
import nts.uk.ctx.at.shared.dom.worktime.fluidworkset.FluidWorkSetting;
import nts.uk.ctx.at.shared.dom.worktime.fluidworkset.fluidbreaktimeset.BreakClockOfManageAtr;
import nts.uk.ctx.at.shared.dom.worktime.fluidworkset.fluidbreaktimeset.FluidBreakTimeOfCalcMethod;
import nts.uk.shr.com.time.TimeWithDayAttr;

/**
 * 控除時間帯
 * @author keisuke_hoshina
 *
 */
@RequiredArgsConstructor
public class DeductionTimeSheet {
	@Getter
	private final List<TimeSheetOfDeductionItem> forDeductionTimeZoneList;
	private final List<TimeSheetOfDeductionItem> forRecordTimeZoneList;
	@Getter
	private final BreakManagement breakTimeSheet;
	
	public void devideDeductionsBy(ActualWorkingTimeSheet actualWorkingTimeSheet,BreakManagement breakTimeSheet) {

		devide(this.forDeductionTimeZoneList, actualWorkingTimeSheet);
		devide(this.forRecordTimeZoneList, actualWorkingTimeSheet);
	}
	
	private static void devide(
			List<TimeSheetOfDeductionItem> source,
			ActualWorkingTimeSheet devider) {
		
		val devided = new ArrayList<TimeSheetOfDeductionItem>();
		source.forEach(deductionTimeZone -> {
			devided.addAll(deductionTimeZone.devideIfContains(devider.start()));
			devided.addAll(deductionTimeZone.devideIfContains(devider.end()));
		});
		
		source.clear();
		source.addAll(devided);
	}
	
	public DeductionTimeSheet createDedctionTimeSheet(AcquisitionConditionsAtr acqAtr,WorkTimeMethodSet setMethod,BreakClockOfManageAtr clockManage,
								GoOutTimeSheetOfDailyWork dailyGoOutSheet,CalculationRangeOfOneDay oneDayRange,BreakSetOfCommon CommonSet, AttendanceLeavingWorkOfDaily attendanceLeaveWork
								,FixRestCalcMethod fixedCalc,WorkTimeDivision workTimeDivision,SetForNoStamp noStampSet, FluidBreakTimeOfCalcMethod fluidSet){
		/*控除時間帯取得　控除時間帯リストへコピー*/
		List<TimeSheetOfDeductionItem> useDedTimeSheet = collectDeductionTimes(dailyGoOutSheet,oneDayRange,CommonSet
				,attendanceLeaveWork,fixedCalc,workTimeDivision,noStampSet,fluidSet,acqAtr );
		/*重複部分補正処理*/
		useDedTimeSheet = new DeductionTimeSheetAdjustDuplicationTime(useDedTimeSheet).reCreate(setMethod, clockManage);
		/*計上用↓*/
		List<TimeSheetOfDeductionItem> recordDedTimeSheet = useDedTimeSheet;
		
		
		/*控除でない外出削除*/
		List<TimeSheetOfDeductionItem> goOutDeletedList = new ArrayList<TimeSheetOfDeductionItem>();
		for(TimeSheetOfDeductionItem timesheet: useDedTimeSheet) {
			if(!(timesheet.getDeductionAtr().isGoOut() && timesheet.getGoOutReason().get().isPublicOrCmpensation())) {
				goOutDeletedList.add(timesheet);
			}
		}
		
		/*ここに丸め設定系の処理を置く*/
		
		return new DeductionTimeSheet(goOutDeletedList, recordDedTimeSheet, breakTimeSheet);
	}

	
	/**
	 * 控除時間に該当する項目を集め控除時間帯を作成する
	 */
	public static List<TimeSheetOfDeductionItem> collectDeductionTimes(GoOutTimeSheetOfDailyWork dailyGoOutSheet,CalculationRangeOfOneDay oneDayRange,BreakSetOfCommon CommonSet
										, AttendanceLeavingWorkOfDaily attendanceLeaveWork,FixRestCalcMethod fixedCalc
										,WorkTimeDivision workTimeDivision,SetForNoStamp noStampSet, FluidBreakTimeOfCalcMethod fluidSet,AcquisitionConditionsAtr acqAtr ) {
		List<TimeSheetOfDeductionItem> sheetList = new ArrayList<TimeSheetOfDeductionItem>(); 
		/*休憩時間帯取得*/
		breakTimeSheet.getBreakTimeSheet(workTimeDivision, fixedCalc, noStampSet, fluidSet);
		/*外出時間帯取得*/
		dailyGoOutSheet.RemoveUnuseItemBaseOnAtr(acqAtr)
								.forEach(tc ->{
									sheetList.add(TimeSheetOfDeductionItem.reateBreakTimeSheetAsFixed(new TimeSpanForCalc(tc.getGoOut().getEngrave().getTimesOfDay(),tc.getComeBack().getEngrave().getTimesOfDay())
																									,Finally.of(tc.getGoOutReason())
																									,null
																									,DeductionClassification.GO_OUT
																									,WithinStatutoryAtr.WithinStatury));	
								});
		/*育児時間帯を取得*/
		/*ソート処理*/
		sheetList.stream().sorted((first,second) -> first.calculationTimeSheet.getStart().compareTo(second.calculationTimeSheet.getStart()));
		/*計算範囲による絞り込み*/
		List<TimeSheetOfDeductionItem> reNewSheetList = refineCalcRange(sheetList,oneDayRange.getOneDayOfRange(),CommonSet.getLeaveWorkDuringBreakTime()
				, attendanceLeaveWork);
		return reNewSheetList;
		
	}
	

	/**
	 * 計算範囲による絞り込みを行うためのループ
	 * @param dedTimeSheets 控除項目の時間帯
	 * @param oneDayRange　1日の範囲
	 * @return 控除項目の時間帯リスト
	 */
	public static List<TimeSheetOfDeductionItem> refineCalcRange(List<TimeSheetOfDeductionItem> dedTimeSheets,TimeSpanForCalc oneDayRange,CalcMethodIfLeaveWorkDuringBreakTime calcMethod
												,AttendanceLeavingWorkOfDaily attendanceLeaveWork) {
		List<TimeSheetOfDeductionItem> sheetList = new ArrayList<TimeSheetOfDeductionItem>(); 
		for(TimeSheetOfDeductionItem timeSheet: dedTimeSheets){
			switch(timeSheet.getDeductionAtr()) {
			case CHILD_CARE:
				/*要確認*/
			case GO_OUT:
				Optional<TimeSpanForCalc> duplicateGoOutSheet = getGoOutCalcRange(timeSheet,oneDayRange);
				if(duplicateGoOutSheet.isPresent()) {
						sheetList.add(TimeSheetOfDeductionItem.reateBreakTimeSheetAsFixed( duplicateGoOutSheet.get()
																						, timeSheet.getGoOutReason()
																						, timeSheet.getBreakAtr()
																						, timeSheet.getDeductionAtr()
																						, timeSheet.getWithinStatutoryAtr()));
				}
			case BREAK:
			
				List<TimeSpanForCalc> duplicateBreakSheet = getBreakCalcRange(attendanceLeaveWork.getAttendanceLeavingWorkTime(),calcMethod,oneDayRange.getDuplicatedWith(timeSheet.calculationTimeSheet));
				if(!duplicateBreakSheet.isEmpty())
				{
					duplicateBreakSheet.forEach(tc -> {
						sheetList.add(TimeSheetOfDeductionItem.reateBreakTimeSheetAsFixed( tc
							, timeSheet.getGoOutReason()
							, timeSheet.getBreakAtr()
							, timeSheet.getDeductionAtr()
							, timeSheet.getWithinStatutoryAtr()));	
					});
				}
			default:
				throw new RuntimeException("unknown deductionAtr:" + timeSheet.getDeductionAtr());
			}
		}
		return sheetList;
	}
	
	
	//外出時間帯の計算範囲の取得(親(控除時間帯？)でやれる)
	public static Optional<TimeSpanForCalc> getGoOutCalcRange(TimeSheetOfDeductionItem shet,TimeSpanForCalc oneDayRange) {
		
		return oneDayRange.getDuplicatedWith(shet.calculationTimeSheet); 
	
	}

	/**
	 * 休憩時間帯の計算範囲の取得 
	 * @param timeList 出勤退勤の時間リスト
	 * @param calcMethod　休憩時間中に退勤した場合の計算方法
	 * @param deplicateoneTimeRange 1日の範囲と控除時間帯の重複部分
	 * @return
	 */
	public static List<TimeSpanForCalc> getBreakCalcRange(List<AttendanceLeavingWork> timeList,CalcMethodIfLeaveWorkDuringBreakTime calcMethod,Optional<TimeSpanForCalc> deplicateOneTimeRange) {
		if(deplicateOneTimeRange.isPresent()) {
			return null;
		}
		List<TimeSpanForCalc> timesheets = new ArrayList<TimeSpanForCalc>();
		for(AttendanceLeavingWork time : timeList) {
			timesheets.add(getIncludeAttendanceOrLeaveDuplicateTimeSheet(time, calcMethod, deplicateOneTimeRange.get()));
		}
		return timesheets;
	}
	
	

	/**
	 * 休憩時間帯に出勤、退勤が含まれているかの判定ののち重複時間帯の取得
	 * @param time
	 * @param calcMethod
	 * @param oneDayRange
	 * @return
	 */
	public static TimeSpanForCalc getIncludeAttendanceOrLeaveDuplicateTimeSheet(AttendanceLeavingWork time,CalcMethodIfLeaveWorkDuringBreakTime calcMethod,TimeSpanForCalc oneDayRange) {
		TimeWithDayAttr newStart = oneDayRange.getStart();
		TimeWithDayAttr newEnd = oneDayRange.getEnd();
		if(oneDayRange.contains(time.getLeaveWork().getActualEngrave().getTimesOfDay())) {
			if(oneDayRange.contains(time.getAttendance().getActualEngrave().getTimesOfDay())){
				newStart = time.getAttendance().getActualEngrave().getTimesOfDay();
			}
		
			switch(calcMethod) {
			case NotRecordAll:
				return null;
			case RecordAll:
				return new TimeSpanForCalc(newStart,newEnd);
			case RecordUntilLeaveWork:
				return new TimeSpanForCalc(newStart,time.getLeaveWork().getEngrave().getTimesOfDay());
			default:
				throw new RuntimeException("unknown CalcMethodIfLeaveWorkDuringBreakTime:" + calcMethod);
			}
		}
		else
		{
			return oneDayRange.getDuplicatedWith(new TimeSpanForCalc(time.getAttendance().getEngrave().getTimesOfDay(),time.getLeaveWork().getEngrave().getTimesOfDay())).get();
		}
	}
	
	/**
	 * 全控除項目の時間帯の合計を算出する
	 * @return 控除時間
	 */
	public int calcDeductionAllTimeSheet(DeductionAtr dedAtr,TimeSpanForCalc workTimeSpan) {
		List<TimeSheetOfDeductionItem> duplicatitedworkTime = getCalcRange(workTimeSpan);
		int sumTime = 0;
		for(TimeSheetOfDeductionItem dedItem :duplicatitedworkTime) {
			sumTime += dedItem.calculationTimeSheet.lengthAsMinutes();
		}
		return sumTime;
	}
	
	/**
	 * 休憩時間帯の合計時間を算出する
	 * @param deductionTimeSheetList
	 * @return
	 */
	public int calcDeductionTotalTime(List<TimeSheetOfDeductionItem> deductionItemTimeSheetList) {
		int totalTime = 0;
		for(TimeSheetOfDeductionItem deductionItemTimeSheet: deductionItemTimeSheetList) {
			totalTime += deductionItemTimeSheet.calcTotalTime();
		}
		return totalTime;
	}
	
	/**
	 * 引数の時間帯に重複する休憩時間帯の合計時間（分）を返す
	 * @param baseTimeSheet
	 * @return
	 */
	public int sumBreakTimeIn(TimeSpanForCalc baseTimeSheet) {
		return this.breakTimeSheet.sumBreakTimeIn(baseTimeSheet);
	}
	
	/**
	 * 算出された休憩時間の合計を取得する
	 * @param dedAtr
	 * @return
	 */
	public DeductionTotalTime getTotalBreakTime(DeductionAtr dedAtr) {
		DeductionTotalTime dedTotalTime;
		switch(dedAtr) {
		case Appropriate:
			dedTotalTime = getDeductionTotalTime(forDeductionTimeZoneList.stream().filter(tc -> tc.getDeductionAtr().isBreak()).collect(Collectors.toList()));
		case Deduction:
			dedTotalTime = getDeductionTotalTime(forRecordTimeZoneList.stream().filter(tc -> tc.getDeductionAtr().isBreak()).collect(Collectors.toList()));
		default:
			throw new RuntimeException("unknown DeductionAtr" + dedAtr);
		}
		return dedTotalTime;
	}
	
	/**
	 * 算出された外出時間の合計を取得する
	 * @param dedAtr
	 * @return
	 */
	public List<DeductionTotalTime> getTotalGoOutTime(DeductionAtr dedAtr) {
		List<DeductionTotalTime> dedTotalTimeList;
		switch(dedAtr) {
		case Appropriate:
			dedTotalTimeList.add(getDeductionTotalTime(forDeductionTimeZoneList.stream().filter(tc -> tc.getDeductionAtr().isBreak())
																						.filter(tc -> tc.getGoOutReason().get().isPrivate())
																						.collect(Collectors.toList())));
			dedTotalTimeList.add(getDeductionTotalTime(forDeductionTimeZoneList.stream().filter(tc -> tc.getDeductionAtr().isBreak())
																						.filter(tc -> tc.getGoOutReason().get().isCompensation())
																						.collect(Collectors.toList())));
			dedTotalTimeList.add(getDeductionTotalTime(forDeductionTimeZoneList.stream().filter(tc -> tc.getDeductionAtr().isBreak())
																						.filter(tc -> tc.getGoOutReason().get().isPublic())
																						.collect(Collectors.toList())));
			dedTotalTimeList.add(getDeductionTotalTime(forDeductionTimeZoneList.stream().filter(tc -> tc.getDeductionAtr().isBreak())
																						.filter(tc -> tc.getGoOutReason().get().isUnion())
																						.collect(Collectors.toList())));
		case Deduction:
			dedTotalTimeList.add(getDeductionTotalTime(forRecordTimeZoneList.stream().filter(tc -> tc.getDeductionAtr().isBreak())
																						.filter(tc -> tc.getGoOutReason().get().isPrivate())
																						.collect(Collectors.toList())));
			dedTotalTimeList.add(getDeductionTotalTime(forRecordTimeZoneList.stream().filter(tc -> tc.getDeductionAtr().isBreak())
																						.filter(tc -> tc.getGoOutReason().get().isCompensation())
																						.collect(Collectors.toList())));
			dedTotalTimeList.add(getDeductionTotalTime(forRecordTimeZoneList.stream().filter(tc -> tc.getDeductionAtr().isBreak())
																						.filter(tc -> tc.getGoOutReason().get().isPublic())
																						.collect(Collectors.toList())));
			dedTotalTimeList.add(getDeductionTotalTime(forRecordTimeZoneList.stream().filter(tc -> tc.getDeductionAtr().isBreak())
																						.filter(tc -> tc.getGoOutReason().get().isUnion())
																						.collect(Collectors.toList())));
		default:
			throw new RuntimeException("unknown DeductionAtr" + dedAtr);
		}
		return dedTotalTimeList;
	}
	

	/**
	 * 法定内・外、総裁時間から合計時間算出
	 * @param deductionTimeSheetList　
	 * @return
	 */
	public DeductionTotalTime getDeductionTotalTime(List<TimeSheetOfDeductionItem> deductionTimeSheetList) {
		int statutoryTotalTime         = calcDeductionTotalTime(deductionTimeSheetList.stream()
																					  .filter(tc -> tc.getWithinStatutoryAtr().isWithinStatutory())
																					  .collect(Collectors.toList()));
		int excessOfStatutoryTotalTime = calcDeductionTotalTime(deductionTimeSheetList.stream()
																					  .filter(tc -> tc.getWithinStatutoryAtr().isExcessOfStatutory())
																					  .collect(Collectors.toList()));
		return DeductionTotalTime.of(TimeWithCalculation.of(new AttendanceTime(statutoryTotalTime+excessOfStatutoryTotalTime)
															,new AttendanceTime(statutoryTotalTime)
															,new AttendanceTime(excessOfStatutoryTotalTime)));
	}
	
	/**
	 * 計算を行う範囲に存在する控除時間帯の抽出
	 * @param workTimeSpan 計算範囲
	 * @return　計算範囲内に存在する控除時間帯
	 */
	public List<TimeSheetOfDeductionItem> getCalcRange(TimeSpanForCalc workTimeSpan){
		return forDeductionTimeZoneList.stream().filter(tc -> workTimeSpan.contains(tc.calculationTimeSheet.getSpan())).collect(Collectors.toList());
	}	
	
	
	//＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊
	
	/**
	 * 控除時間帯の仮確定(流動用) 
	 */
	public void provisionalDecisionOfDeductionTimeSheet(FluidWorkSetting fluidWorkSetting) {
		//固定休憩か流動休憩か確認する
		if(fluidWorkSetting.getWeekdayWorkTime().getRestTime().getUseFixedRestTime()) {//固定休憩の場合
			switch(fluidWorkSetting.getRestSetting().getFluidWorkBreakSettingDetail().getFluidPrefixBreakTimeSet().getCalcMethod()) {
				//マスタを参照する
				case ReferToMaster:
				this.createDedctionTimeSheet(acqAtr, setMethod, clockManage, dailyGoOutSheet, oneDayRange, CommonSet, attendanceLeaveWork, fixedCalc, workTimeDivision, noStampSet, fluidSet);
				//予定を参照する
				case ReferToSchedule:
				this.createDedctionTimeSheet(acqAtr, setMethod, clockManage, dailyGoOutSheet, oneDayRange, CommonSet, attendanceLeaveWork, fixedCalc, workTimeDivision, noStampSet, fluidSet);
				//参照せずに打刻する
				case StampWithoutReference:
				this.createDedctionTimeSheet(acqAtr, setMethod, clockManage, dailyGoOutSheet, oneDayRange, CommonSet, attendanceLeaveWork, fixedCalc, workTimeDivision, noStampSet, fluidSet);
			}
		}else{//流動休憩の場合
			switch(fluidWorkSetting.getRestSetting().getFluidWorkBreakSettingDetail().getFluidBreakTimeSet().getCalcMethod()) {
				//マスタを参照する
				case ReferToMaster:
				
				//マスタと打刻を併用する	
				case ConbineMasterWithStamp:
				
				//参照せずに打刻する	
				case StampWithoutReference:
			
			}
		}
		
	}
	
	
	/**
	 * 控除時間帯の作成   流動勤務で固定休憩の場合にシフトから計算する場合の処理の事
	 */
	public void createLateTimeSheetForFluid(
			WithinWorkTimeFrame withinWorkTimeFrame,
			FluidWorkSetting fluidWorkSetting,
			CalculationRangeOfOneDay oneDayRange) {
		
		//計算範囲の取得
		oneDayRange.
		//控除時間帯の取得　・・・保科君が作成済みの処理を呼ぶ
		List<TimeSheetOfDeductionItem> deductionTimeSheet = this.collectDeductionTimes(
				dailyGoOutSheet, 
				oneDayRange, 
				CommonSet, 
				attendanceLeaveWork, 
				fixedCalc, 
				workTimeDivision, 
				noStampSet, 
				fluidSet, 
				acqAtr);
		//控除時間帯同士の重複部分を補正
		deductionTimeSheet = new DeductionTimeSheetAdjustDuplicationTime(deductionTimeSheet).reCreate(setMethod, clockManage);
		//控除合計時間クラスを作成　　不要な可能性あり
		DeductionTotalTimeForFluidCalc deductionTotalTime = new DeductionTotalTimeForFluidCalc();
		//流動休憩時間帯を取得する
		List<FluRestTimeSetting> fluRestTimeSheetList = 
				fluidWorkSetting.getWeekdayWorkTime().getRestTime().getFluidRestTime().getFluidRestTimes();
		//外出取得開始時刻を作成する
		AttendanceTime getGoOutStartClock = new AttendanceTime(withinWorkTimeFrame.getCalcrange().getStart().valueAsMinutes());
		//一時的に作成
		int roopNo = 0;
		//一時的に作成
		List<TimeSheetOfDeductionItem> restTimeSheetList = new ArrayList<>();
		//流動休憩時間帯分ループ
		for(FluRestTimeSetting fluRestTimeSetting : fluRestTimeSheetList) {
			roopNo++;
			//外出のみの控除時間帯リストを作成する
			List<TimeSheetOfDeductionItem> goOutDeductionTimelist = 
					this.forDeductionTimeZoneList.stream().filter(ts -> ts.getGoOutReason().isPresent()).collect(Collectors.toList());		
			//流動休憩時間帯の作成（引数にgetGoOutStartClockを渡す
			TimeSheetOfDeductionItem restTimeSheet = deductionTotalTime.createDeductionFluidRestTime(
					fluRestTimeSetting, 
					getGoOutStartClock, 
					ｔimeSheetOfDeductionItem, 
					roopNo, 
					fluidWorkSetting, 
					deductionTimeSheet, 
					goOutDeductionTimelist);
			//作成した時間帯を休憩時間帯リストに格納
			restTimeSheetList.add(restTimeSheet);
		}
		//退勤時刻までの外出の処理
		deductionTotalTime.collectDeductionTotalTime(list, getGoOutStartClock, fluidWorkSetting, roopNo);
		//控除時間帯をソート
		
	}
	
}
