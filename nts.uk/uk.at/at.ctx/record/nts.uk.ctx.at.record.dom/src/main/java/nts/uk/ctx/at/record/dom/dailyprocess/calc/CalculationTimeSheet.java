package nts.uk.ctx.at.record.dom.dailyprocess.calc;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import lombok.Getter;
import lombok.Setter;
import lombok.val;
import nts.uk.ctx.at.record.dom.MidNightTimeSheetForCalc;
import nts.uk.ctx.at.record.dom.bonuspay.autocalc.BonusPayAutoCalcSet;
import nts.uk.ctx.at.record.dom.calculationattribute.CalAttrOfDailyPerformance;
import nts.uk.ctx.at.record.dom.daily.TimeWithCalculation;
import nts.uk.ctx.at.record.dom.daily.bonuspaytime.BonusPayTime;
import nts.uk.ctx.at.record.dom.daily.midnight.MidNightTimeSheet;
import nts.uk.ctx.at.shared.dom.bonuspay.setting.BonusPayTimesheet;
import nts.uk.ctx.at.shared.dom.bonuspay.setting.SpecBonusPayTimesheet;
import nts.uk.ctx.at.shared.dom.common.time.AttendanceTime;
import nts.uk.ctx.at.shared.dom.common.time.TimeSpanForCalc;
import nts.uk.ctx.at.shared.dom.ot.autocalsetting.AutoCalAtrOvertime;
import nts.uk.ctx.at.shared.dom.workrule.outsideworktime.RaisingSalaryCalcAtr;
import nts.uk.ctx.at.shared.dom.worktime.common.EmTimeZoneSet;
import nts.uk.ctx.at.shared.dom.worktime.common.TimeZoneRounding;
import nts.uk.shr.com.time.TimeWithDayAttr;

/**
 * 計算時間帯
 * @author keisuke_hoshina
 *
 */
@Getter
public abstract class CalculationTimeSheet {
	protected TimeZoneRounding timeSheet;
	protected final TimeSpanForCalc calcrange;
	@Setter
	//計上用
	protected List<TimeSheetOfDeductionItem> recordedTimeSheet= new ArrayList<>();
	@Setter
	//控除用
	protected List<TimeSheetOfDeductionItem> deductionTimeSheet = new ArrayList<>();
	@Setter
	protected List<BonusPayTimeSheetForCalc> bonusPayTimeSheet = new ArrayList<>();
	@Setter
	protected List<SpecBonusPayTimeSheetForCalc> specBonusPayTimesheet = new ArrayList<>();
	@Setter
	protected Optional<MidNightTimeSheetForCalc> midNightTimeSheet = Optional.empty();

	/**
	 * @param 控除用
	 * @param 計上用
	 */
	public CalculationTimeSheet(TimeZoneRounding timeSheet,
								TimeSpanForCalc calcrange) {
		this.timeSheet = timeSheet;
		this.calcrange = calcrange;
	}

	
	/**
	 * Constructor
	 * @param timeSheet 時間帯(丸め付き)
	 * @param calcrange 計算範囲
	 * @param midNighttimeSheet 深夜時間帯
	 */
	public CalculationTimeSheet(TimeZoneRounding timeSheet,
								TimeSpanForCalc calcrange,
								List<TimeSheetOfDeductionItem> recorddeductionTimeSheets,
								List<TimeSheetOfDeductionItem> deductionTimeSheets,
								
								List<BonusPayTimeSheetForCalc> bonusPayTimeSheet,
								List<SpecBonusPayTimeSheetForCalc> specifiedBonusPayTimeSheet,
								Optional<MidNightTimeSheetForCalc> midNighttimeSheet
								) {
		this.timeSheet = timeSheet;
		this.calcrange = calcrange;
		
		this.recordedTimeSheet = recorddeductionTimeSheets;
		this.deductionTimeSheet = deductionTimeSheets;
		
		this.bonusPayTimeSheet = bonusPayTimeSheet;
		this.specBonusPayTimesheet = specifiedBonusPayTimeSheet;
		this.midNightTimeSheet = midNighttimeSheet;		
	}
	
	
	/**
	 * 控除時間帯を入れ替える(この処理は「残業枠時間帯作成」で使用する想定で作成)
	 * @param deductionList 入れ替える控除時間帯(List)
	 */
	public void replaceDeductionTimeSheet(List<DeductionTimeSheet> deductionList) {
		this.deductionTimeSheet.clear();
		this.deductionTimeSheet.addAll(deductionTimeSheet);
	}
	
	
	/**
	 * 指定時間を終了とする時間帯作成
	 * @return
	 */
	public TimeSpanForCalc reCreateTreatAsSiteiTimeEnd(AttendanceTime transTime,OverTimeFrameTimeSheetForCalc overTimeWork) {
		TimeSpanForCalc copySpan = calcrange;
		return overTimeWork.reduceUntilSpecifiedTime(new AttendanceTime(copySpan.lengthAsMinutes() - transTime.valueAsMinutes()));
		//return copySpan;
	}
	
	/**
	 * 指定時間帯を指定時間に従って縮小
	 * @param assingnTime 指定時間
	 * @return 縮小後の時間帯
	 */
	public TimeSpanForCalc reduceUntilSpecifiedTime(AttendanceTime assignTime) {
		AttendanceTime shortened = calcTotalTime().minusMinutes(assignTime.valueAsMinutes());
		
		AttendanceTime newEnd = new AttendanceTime(timeSheet.getStart().forwardByMinutes(shortened.valueAsMinutes()).valueAsMinutes());
		
		TimeZoneRounding newTimeSpan = new TimeZoneRounding(new TimeWithDayAttr(shortened.valueAsMinutes()),new TimeWithDayAttr(newEnd.valueAsMinutes()),this.timeSheet.getRounding());
		List<TimeSheetOfDeductionItem> refineList = duplicateNewTimeSpan(newTimeSpan.timeSpan());
		
		while(true) {
			AttendanceTime deductionTime = new AttendanceTime(0);
			newTimeSpan = new TimeZoneRounding(timeSheet.getStart(),new TimeWithDayAttr(newEnd.valueAsMinutes()), this.timeSheet.getRounding());
			for(TimeSheetOfDeductionItem deductionItem : refineList) {
				deductionTime = deductionItem.calcTotalTime();
				newTimeSpan = new TimeZoneRounding(timeSheet.getStart(),newTimeSpan.getEnd().forwardByMinutes(deductionTime.valueAsMinutes()), this.timeSheet.getRounding());
			}
			
			List<TimeSheetOfDeductionItem> moveSpanDuplicateDeductionTimeSheet = duplicateNewTimeSpan(new TimeSpanForCalc(timeSheet.getEnd(), newTimeSpan.getEnd()));
			if(moveSpanDuplicateDeductionTimeSheet.size()>0) {
				refineList.addAll(moveSpanDuplicateDeductionTimeSheet);
			}
			else {
				break; 
			}
		}
		
		return newTimeSpan.timeSpan();
	}
	
	/**
	 *　時間帯と重複している控除時間帯のみを抽出する
	 * @param newTimeSpan 時間帯
	 * @return　控除時間帯リスト
	 */
	public List<TimeSheetOfDeductionItem> duplicateNewTimeSpan(TimeSpanForCalc newTimeSpan){
		return deductionTimeSheet.stream().filter(tc -> newTimeSpan.contains(tc.calcrange)).collect(Collectors.toList());
	}
	
	
	/**
	 * 控除時間の合計を算出する
	 * @param dedAtr
	 * @param conditionAtr
	 * @return
	 */
	public AttendanceTime calcDedTimeByAtr(DeductionAtr dedAtr,ConditionAtr conditionAtr) {
		val forCalcList = getDedTimeSheetByAtr(dedAtr,conditionAtr);
		return new AttendanceTime(forCalcList.stream().map(tc -> tc.calcTotalTime().valueAsMinutes()).collect(Collectors.summingInt(tc -> tc)));
	}
	
	/**
	 * 条件、控除区分に従って控除項目の時間帯取得
	 * @param dedAtr 控除区分
	 * @param conditionAtr　条件
	 * @return　控除項目の時間帯
	 */
	public List<TimeSheetOfDeductionItem> getDedTimeSheetByAtr(DeductionAtr dedAtr, ConditionAtr conditionAtr) {
		val returnList = (dedAtr.isDeduction())?this.deductionTimeSheet:this.recordedTimeSheet;
		switch(conditionAtr) {
			case BREAK:
				return returnList.stream().filter(tc -> tc.getDeductionAtr().isBreak()).collect(Collectors.toList());
			case Care:
			case Child:
				return returnList.stream().filter(tc -> tc.getDeductionAtr().isChildCare()).collect(Collectors.toList());
			case CompesationGoOut:
				return returnList.stream().filter(tc -> tc.getDeductionAtr().isGoOut() 
													 && tc.getGoOutReason().get().isCompensation()).collect(Collectors.toList());
			case PrivateGoOut:
				return returnList.stream().filter(tc -> tc.getDeductionAtr().isGoOut() 
													 && tc.getGoOutReason().get().isPrivate()).collect(Collectors.toList());
			case PublicGoOut:
				return returnList.stream().filter(tc -> tc.getDeductionAtr().isGoOut()).collect(Collectors.toList());
			case UnionGoOut:
				return returnList.stream().filter(tc -> tc.getDeductionAtr().isGoOut()).collect(Collectors.toList());
			default:
				throw new RuntimeException("unknown condition Atr");
		}
	}


	/**
	 * 時間の計算
	 * @return 
	 */
	public AttendanceTime calcTotalTime() {
		//int calcTime = timeSheet.getSpan().lengthAsMinutes() - minusDeductionTime();
		AttendanceTime calcTime = deductionLengthMinutes();
		return calcTime;
	}
	
	/**
	 * 控除時間の計算
	 * @return 控除時間
	 */
	public AttendanceTime calcTotalDeductionTime() {
		return this.calcTotalTime().minusMinutes(deductionLengthMinutes().valueAsMinutes());
	}
	
	/**
	 * 時間帯に含んでいる控除時間を差し引いた時間を計算する(メモ：トリガー)
	 * @return 時間
	 */
	public AttendanceTime deductionLengthMinutes() {
		if(deductionTimeSheet.isEmpty()) return new AttendanceTime(this.calcrange.lengthAsMinutes()) ;
		return recursiveTotalTime() ;
	}
	
	/**
	 * 控除時間の合計を求める(メモ：再帰)
	 * @return　控除の合計時間
	 */
	public AttendanceTime recursiveTotalTime() {
		if(deductionTimeSheet.isEmpty()) return new AttendanceTime(0) ;
		AttendanceTime totalDedTime = new AttendanceTime(0);
		for(TimeSheetOfDeductionItem dedTimeSheet : deductionTimeSheet) {
			totalDedTime.addMinutes(dedTimeSheet.recursiveTotalTime().valueAsMinutes());
		}
		//return 丸め処理(calcrange.lengthAsMinutes() - totalDedTime); ←丸め処理実装後こちらに変える
		return new AttendanceTime(calcrange.lengthAsMinutes() - totalDedTime.valueAsMinutes());
	}

	/**
	 * 指定時間に従って時間帯の縮小
	 * @return 縮小後の時間帯
	 */
	public Optional<TimeSpanForCalc> contractTimeSheet(TimeWithDayAttr timeWithDayAttr) {
		/*ここのcalcTotalTImeは残業時間帯の時間*/
		int afterShort = calcTotalTime().valueAsMinutes() - timeWithDayAttr.valueAsMinutes();
		if(afterShort <= 0) return Optional.empty();
		int newEnd = 0;
		TimeSpanForCalc newSpan = new TimeSpanForCalc(timeSheet.getStart(), new TimeWithDayAttr(newEnd + afterShort));
		List<TimeSheetOfDeductionItem> copyList = getNewSpanIncludeCalcrange(deductionTimeSheet,newSpan);
		for(int listn = 0 ; listn < copyList.size() ; listn++){
				/*ここのcalcTotalTimeは残業時間帯が持ってる控除時間帯の時間*/
				int differTime = copyList.get(listn).calcTotalTime().valueAsMinutes();
				newSpan = newSpan.shiftEndAhead(differTime);
				/*ずらす前に範囲内に入っている時間帯の数を保持*/
				int beforeincludeSpan = getNewSpanIncludeCalcrange(copyList,newSpan).size();
				newSpan = newSpan.shiftEndAhead(copyList.stream().map(ts -> ts.calcrange.lengthAsMinutes()).collect(Collectors.summingInt(tc -> tc)));
				int afterincludeSpan = getNewSpanIncludeCalcrange(copyList,newSpan).size();
				/*ずらした後の範囲に入っている時間帯の数とずらす前のかずを比較し増えていた場合、控除時間帯を保持してる変数に追加する*/
				if(afterincludeSpan > beforeincludeSpan) {
					copyList = Collections.emptyList();
					copyList = getNewSpanIncludeCalcrange(deductionTimeSheet,newSpan);
				}
		}
		return Optional.of(newSpan);
	}
	
	
	/**
	 * 受け取った時間帯に含まれている控除項目の時間帯をリストにする
	 * @param　控除項目の時間帯(List)
	 */
	private List<TimeSheetOfDeductionItem> getNewSpanIncludeCalcrange(List<TimeSheetOfDeductionItem> copyList , TimeSpanForCalc newSpan){
		return copyList.stream().filter(tc -> newSpan.contains(tc.calcrange)).collect(Collectors.toList());
	}
	
	/**
	 * 
	 * @param basePoint 
	 * @return 
	 */
	public int calcIncludeTimeSheet(int basePoint, List<TimeSheetOfDeductionItem> deductionItemList){
		return deductionItemList.stream().map(ts -> ts.calcrange.lengthAsMinutes()).collect(Collectors.summingInt(tc -> tc));
	}
	
	/**
	 * 開始から指定時間経過後の終了時刻を取得
	 * @param timeSpan　時間帯
	 * @param time　指定時間
	 * @return 指定時間経過後の終了時間
	 */
	public Optional<TimeWithDayAttr> getNewEndTime(TimeSpanForCalc timeSpan, TimeWithDayAttr time) {
		Optional<TimeSpanForCalc> newEnd = createTimeSpan(timeSpan,time);
		if(newEnd.isPresent()) {
			return Optional.of(newEnd.get().getEnd());
		}
		else {
			return  Optional.empty();
		}
	}
	
	/**
	 * 開始から指定時間を終了とする時間帯作成
	 * @param timeSpan 時間帯
	 * @param time　指定時間
	 * @return
	 */
	public Optional<TimeSpanForCalc> createTimeSpan(TimeSpanForCalc timeSpan, TimeWithDayAttr time) {
		return contractTimeSheet(time);
	}
	
	/**
	 * 加給時間帯のリストを作り直す
	 * @param baseTime 基準時間
	 * @param isDateBefore 基準時間より早い時間を切り出す
	 * @return 切り出した加給時間帯
	 */
	public List<BonusPayTimeSheetForCalc> recreateBonusPayListBeforeBase(TimeWithDayAttr baseTime,boolean isDateBefore){
		List<BonusPayTimeSheetForCalc> bonusPayList = new ArrayList<>();
		for(BonusPayTimeSheetForCalc bonusPay : bonusPayList) {
//			if(bonusPay..contains(baseTime)) {
//				bonusPayList.add(bonusPay.reCreateOwn(baseTime,isDateBefore));
//			}
//			else if(bonusPay.calcrange.getEnd().lessThan(baseTime) && isDateBefore) {
//				bonusPayList.add(bonusPay);
//			}
//			else if(bonusPay.calcrange.getStart().greaterThan(baseTime) && !isDateBefore) {
//				bonusPayList.add(bonusPay);
//			}
		}
		return bonusPayList; 
	}
	
	/**
	 * 特定日加給時間帯のリストを作り直す
	 * @param baseTime 基準時間
	 * @param isDateBefore 基準時間より早い時間を切り出す
	 * @return 切り出した加給時間帯
	 */
	public List<SpecBonusPayTimeSheetForCalc> recreateSpecifiedBonusPayListBeforeBase(TimeWithDayAttr baseTime,boolean isDateBefore){
		List<SpecBonusPayTimeSheetForCalc> specifiedBonusPayList = new ArrayList<>();
		for(SpecBonusPayTimeSheetForCalc specifiedBonusPay : specifiedBonusPayList) {
//			if(specifiedBonusPay.contains(baseTime)) {
//				specifiedBonusPayList.add(specifiedBonusPay.reCreateOwn(baseTime,isDateBefore));
//			}
//			else if(specifiedBonusPay.calcrange.getEnd().lessThan(baseTime) && isDateBefore) {
//				specifiedBonusPayList.add(specifiedBonusPay);
//			}
//			else if(specifiedBonusPay.calcrange.getStart().greaterThan(baseTime) && !isDateBefore) {
//				specifiedBonusPayList.add(specifiedBonusPay);
//			}
		}
		return specifiedBonusPayList; 
	}
	
	/**
	 * 控除時間帯のリストを作り直す
	 * @param baseTime 基準時間
	 * @param isDateBefore 基準時間より早い時間を切り出す
	 * @return 切り出したl控除時間帯
	 */
	public List<TimeSheetOfDeductionItem> recreateDeductionItemBeforeBase(TimeWithDayAttr baseTime,boolean isDateBefore, DeductionAtr dedAtr){
		
		List<TimeSheetOfDeductionItem> deductionList = new ArrayList<>();
		if(dedAtr.isDeduction()) {
			
			deductionList = this.deductionTimeSheet;
		}
		else {
			
			deductionList = this.recordedTimeSheet;
		}
		List<TimeSheetOfDeductionItem> returnList = new ArrayList<>();
		
		for(TimeSheetOfDeductionItem deductionItem : deductionList) {
			
			if(deductionItem.contains(baseTime)) {
				returnList.add(deductionItem.reCreateOwn(baseTime,isDateBefore));
				returnList.add(deductionItem);
			}
			else if(deductionItem.calcrange.getEnd().lessThan(baseTime) && isDateBefore) {
				returnList.add(deductionItem);
			}
			else if(deductionItem.calcrange.getStart().greaterThan(baseTime) && !isDateBefore) {
				returnList.add(deductionItem);
			}
		}
		
		return returnList;
	}
	
	/**
	 * 深夜時間帯のリストを作り直す
	 * @param baseTime 基準時間
	 * @param isDateBefore 基準時間より早い時間を切り出す
	 * @return 切り出した深夜時間帯
	 */
	public Optional<MidNightTimeSheetForCalc> recreateMidNightTimeSheetBeforeBase(TimeWithDayAttr baseTime,boolean isDateBefore){
		if(this.midNightTimeSheet.isPresent()) {
			if(midNightTimeSheet.get().calcrange.contains(baseTime)) {
				//return midNightTimeSheet.get().midNightTimeSheet.get().reCreateOwn(baseTime,isDateBefore);
				return midNightTimeSheet;
			}
			else if(midNightTimeSheet.get().calcrange.getEnd().lessThan(baseTime) && isDateBefore) {
				return midNightTimeSheet;
			}
			else if(midNightTimeSheet.get().calcrange.getStart().greaterThan(baseTime) && !isDateBefore) {
				return midNightTimeSheet;
			}
		}
		return Optional.empty();
	}
	
	/**
	 * 深夜時間帯の作成(トリガー)
	 * @param midnightTimeSheet
	 */
	public Optional<MidNightTimeSheetForCalc> createMidNightTimeSheet() {
		if(midNightTimeSheet.isPresent()) {
			if(calcrange.checkDuplication(midNightTimeSheet.get().calcrange).isDuplicated()) { 
				return Optional.of(new MidNightTimeSheetForCalc(timeSheet,
										 calcrange.getDuplicatedWith(midNightTimeSheet.get().calcrange).get(),
										 duplicateTimeSpan(midNightTimeSheet.get().calcrange),
										 duplicateTimeSpan(midNightTimeSheet.get().calcrange),
										 bonusPayTimeSheet,
										 specBonusPayTimesheet,
										 midNightTimeSheet
										 ));
			}
		}
		return Optional.empty();
	}
	
	/**
	 * 深夜時間帯の作成(再帰)
	 * @param timeSpan
	 * @return
	 */
	public List<TimeSheetOfDeductionItem> duplicateTimeSpan(TimeSpanForCalc timeSpan) {
		List<TimeSheetOfDeductionItem> returnList = new ArrayList<>();
		for(TimeSheetOfDeductionItem deductionTimeSheet : deductionTimeSheet) {
			if(midNightTimeSheet.isPresent()) {
				Optional<TimeSpanForCalc> duplicateSpan = midNightTimeSheet.get().calcrange.getDuplicatedWith(deductionTimeSheet.calcrange);
				if(duplicateSpan.isPresent()) {
					returnList.add(TimeSheetOfDeductionItem.createTimeSheetOfDeductionItemAsFixed(
																								deductionTimeSheet.timeSheet
																							   ,deductionTimeSheet.calcrange
																							   ,deductionTimeSheet.recordedTimeSheet
																							   ,deductionTimeSheet.deductionTimeSheet
																							   ,deductionTimeSheet.bonusPayTimeSheet
																							   ,deductionTimeSheet.specBonusPayTimesheet
																							   ,deductionTimeSheet.midNightTimeSheet
																							   ,deductionTimeSheet.getGoOutReason()
																							   ,deductionTimeSheet.getBreakAtr()
																							   ,deductionTimeSheet.getDeductionAtr()
																							   ));
				}
			}
		}
		return returnList;
	}
	
	/**
	 * 加給時間の計算
	 * @param actualWorkAtr 実働区分
	 * @param bonusPayCalcSet　加給自動計算設定
	 * @param calcAtrOfDaily　日別実績の計算区分
	 * @return 加給時間クラス(List)
	 */
	public List<BonusPayTime> calcBonusPay(ActualWorkTimeSheetAtr actualWorkAtr, RaisingSalaryCalcAtr raisingAutoCalcSet,BonusPayAutoCalcSet bonusPayAutoCalcSet, CalAttrOfDailyPerformance calcAtrOfDaily,BonusPayAtr bonusPayAtr) {
		List<BonusPayTime> bonusPayTimeList = new ArrayList<>();
		for(BonusPayTimeSheetForCalc bonusPaySheet : this.bonusPayTimeSheet){
			AttendanceTime calcTime = bonusPaySheet.calcTotalTime();
			bonusPayTimeList.add(new BonusPayTime(bonusPayAutoCalcSet.getBonusPayItemNo()
												 ,calcTime
												 ,TimeWithCalculation.sameTime(calcTime)
												 ,TimeWithCalculation.sameTime(calcTime)));
		}
		if(!GetCalcAtr.isCalc(calcAtrOfDaily.getRasingSalarySetting().getSalaryCalSetting().isUse(), calcAtrOfDaily, bonusPayAutoCalcSet, actualWorkAtr)) {
			bonusPayTimeList.forEach(tc ->{tc.getWithinBonusPay().setTime(new AttendanceTime(0));
										   tc.getExcessBonusPayTime().setTime(new AttendanceTime(0));});
		}
		return bonusPayTimeList;
	}
	
	/**
	 * 特定加給時間の計算
	 * @param actualWorkAtr 実働区分
	 * @param bonusPayCalcSet　加給自動計算設定
	 * @param calcAtrOfDaily　日別実績の計算区分
	 * @return 加給時間クラス(List)
	 */
	public List<BonusPayTime> calcSpacifiedBonusPay(ActualWorkTimeSheetAtr actualWorkAtr, RaisingSalaryCalcAtr raisingAutoCalcSet,BonusPayAutoCalcSet bonusPayAutoCalcSet, CalAttrOfDailyPerformance calcAtrOfDaily,BonusPayAtr bonusPayAtr){
		List<BonusPayTime> bonusPayTimeList = new ArrayList<>();
		for(SpecBonusPayTimeSheetForCalc bonusPaySheet : this.specBonusPayTimesheet){
			AttendanceTime calcTime = bonusPaySheet.calcTotalTime();
			bonusPayTimeList.add(new BonusPayTime(bonusPayAutoCalcSet.getBonusPayItemNo()
					 ,calcTime
					 ,TimeWithCalculation.sameTime(calcTime)
					 ,TimeWithCalculation.sameTime(calcTime)));
		}
		if(!GetCalcAtr.isCalc(calcAtrOfDaily.getRasingSalarySetting().getSpecificSalaryCalSetting().isUse(), calcAtrOfDaily, bonusPayAutoCalcSet, actualWorkAtr)) {
			bonusPayTimeList.forEach(tc ->{tc.getWithinBonusPay().setTime(new AttendanceTime(0));
										   tc.getExcessBonusPayTime().setTime(new AttendanceTime(0));});
		}
		return bonusPayTimeList;
	}
	
	/**
	 * 特定日加給時間帯の作成
	 * @param specifiedDayList　特定日
	 * @return 加給設定
	 */
	public List<SpecBonusPayTimeSheetForCalc> createSpecifiedBonusPayTimeSheet(List<Integer> specifiedDayList,List<SpecBonusPayTimeSheetForCalc> specifiedBonusPayTimeSheetList){
		if(specifiedDayList.size() == 0) return Collections.emptyList();
		for(SpecBonusPayTimeSheetForCalc specifiedBonusPayTimeSheet : specifiedBonusPayTimeSheetList) {
			if(specifiedDayList.contains(specifiedBonusPayTimeSheet.getSpecBonusPayNumber().v().intValue())) {
				Optional<TimeSpanForCalc> newSpan = this.calcrange.getDuplicatedWith(
														new TimeSpanForCalc(new TimeWithDayAttr(specifiedBonusPayTimeSheet.getCalcrange().getStart().valueAsMinutes())
																		   ,new TimeWithDayAttr(specifiedBonusPayTimeSheet.getCalcrange().getEnd().valueAsMinutes())));
				if(newSpan.isPresent()) {
					this.specBonusPayTimesheet.add(specifiedBonusPayTimeSheet.convertForCalcCorrectRange(newSpan.get()));
				}
			}
		}
		return this.specBonusPayTimesheet;
	}
	
	/**
	 * 深夜時間の計算
	 * @return 深夜時間
	 */
	public AttendanceTime calcMidNight(AutoCalAtrOvertime autoCalcSet) {
		if(autoCalcSet.isCalculateEmbossing())
		{
			if(this.midNightTimeSheet.isPresent()) {
				return this.midNightTimeSheet.get().calcTotalTime();
			}
			else {
				return new AttendanceTime(0);
			}
		}
		else {
			return new AttendanceTime(0);
		}
	}
	
//	/**
//	 * 控除区分に従って該当のリストを取得(現時点では休憩のみしか取得できない)
//	 * @param dedAtr
//	 * @param conAtr
//	 * @return
//	 */
//	public List<TimeSheetOfDeductionItem> getDedTimeSheetByDedAtr(DeductionAtr dedAtr,ConditionAtr conAtr){
//		switch(dedAtr) {
//		case Appropriate:
//			return this.recordedTimeSheet.stream().filter(tc -> tc.getBreakAtr().get().isBreak()).collect(Collectors.toList());
//		case Deduction:
//			return this.deductionTimeSheet.stream().filter(tc -> tc.getBreakAtr().get().isBreak()).collect(Collectors.toList());
//		default:
//			throw new RuntimeException("unknown DedAtr:" + dedAtr);
//		}
//		
//	}
	//---------------------------実働時間帯へ持っていきたい-------------------------------↓
	/**
	 * 指定された時間帯と重複している加給時間帯を取得
	 * @param bonusPayTimeSheet
	 * @param duplicateTimeSheet
	 * @return
	 */
	public static List<BonusPayTimeSheetForCalc> getDuplicatedBonusPay(List<BonusPayTimeSheetForCalc> bonusPayTimeSheet,TimeSpanForCalc timeSpan){
		return bonusPayTimeSheet.stream()
								.filter(tc -> tc.getCalcrange().checkDuplication(timeSpan).isDuplicated())
								.map(tc -> tc.convertForCalcCorrectRange(tc.getCalcrange().getDuplicatedWith(timeSpan).get()))
								.collect(Collectors.toList());
	}
	
	/**
	 * 指定された時間帯と重複している特定加給時間帯を取得
	 * @param bonusPayTimeSheet
	 * @param duplicateTimeSheet
	 * @return
	 */
	public static List<SpecBonusPayTimeSheetForCalc> getDuplicatedSpecBonusPay(List<SpecBonusPayTimeSheetForCalc> bonusPayTimeSheet,TimeSpanForCalc timeSpan){
		return bonusPayTimeSheet.stream()
								.filter(tc -> tc.getCalcrange().checkDuplication(timeSpan).isDuplicated())
								.map(tc -> tc.convertForCalcCorrectRange(tc.getCalcrange().getDuplicatedWith(timeSpan).get()))
								.collect(Collectors.toList());
	}
	
	/**
	 * 指定された時間帯と重複している深夜時間帯を取得
	 * @param midNightTimeSheet
	 * @param duplicateTimeSheet
	 * @return
	 */
	public static Optional<MidNightTimeSheetForCalc> getDuplicateMidNight(MidNightTimeSheet midNightTimeSheet, TimeSpanForCalc timeSpan){ 
		val duplicateMidNightSpan = timeSpan.getDuplicatedWith(midNightTimeSheet.getTimeSpan());
		if(duplicateMidNightSpan.isPresent()) {
			return Optional.of(MidNightTimeSheetForCalc.convertForCalc(midNightTimeSheet).getDuplicateRangeTimeSheet(duplicateMidNightSpan.get()));
		}
		return Optional.empty();
	}
	//---------------------------実働時間帯へ持っていきたい-------------------------------↑
}
