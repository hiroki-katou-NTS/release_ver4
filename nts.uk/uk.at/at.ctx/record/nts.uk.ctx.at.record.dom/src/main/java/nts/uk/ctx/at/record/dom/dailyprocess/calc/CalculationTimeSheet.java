package nts.uk.ctx.at.record.dom.dailyprocess.calc;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import lombok.Getter;
import lombok.Setter;
import nts.uk.ctx.at.record.dom.MidNightTimeSheet;
import nts.uk.ctx.at.record.dom.bonuspay.BonusPayAutoCalcSet;
import nts.uk.ctx.at.record.dom.bonuspay.setting.BonusPayTimesheet;
import nts.uk.ctx.at.record.dom.bonuspay.setting.SpecBonusPayTimesheet;
import nts.uk.ctx.at.record.dom.calculationattribute.CalAttrOfDailyPerformance;
import nts.uk.ctx.at.record.dom.daily.BonusPayTime;
import nts.uk.ctx.at.record.dom.daily.TimeWithCalculation;
import nts.uk.ctx.at.shared.dom.common.time.AttendanceTime;
import nts.uk.ctx.at.shared.dom.common.time.TimeSpanForCalc;
import nts.uk.ctx.at.shared.dom.workrule.outsideworktime.AutoCalculationCategoryOutsideHours;
import nts.uk.ctx.at.shared.dom.worktime.fixedworkset.timespan.TimeSpanWithRounding;
import nts.uk.shr.com.time.TimeWithDayAttr;

/**
 * 計算時間帯
 * @author keisuke_hoshina
 *
 */
@Getter
public abstract class CalculationTimeSheet {
	protected TimeSpanWithRounding timeSheet;
	protected final TimeSpanForCalc calcrange;
	@Setter
	//計上用
	protected List<TimeSheetOfDeductionItem> recordedTimeSheet= new ArrayList<>();
	@Setter
	//控除用
	protected List<TimeSheetOfDeductionItem> deductionTimeSheet = new ArrayList<>();
	@Setter
	protected List<BonusPayTimesheet> bonusPayTimeSheet = new ArrayList<>();
	@Setter
	protected List<SpecBonusPayTimesheet> specBonusPayTimesheet = new ArrayList<>();
	@Setter
	protected Optional<MidNightTimeSheet> midNightTimeSheet = Optional.empty();

	/**
	 * @param 控除用
	 * @param 計上用
	 */
	public CalculationTimeSheet(TimeSpanWithRounding timeSheet,
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
	public CalculationTimeSheet(TimeSpanWithRounding timeSheet,
								TimeSpanForCalc calcrange,
								List<TimeSheetOfDeductionItem> deductionTimeSheets,
								List<BonusPayTimesheet> bonusPayTimeSheet,
								List<SpecBonusPayTimesheet> specifiedBonusPayTimeSheet,
								Optional<MidNightTimeSheet> midNighttimeSheet
								) {
		this.timeSheet = timeSheet;
		this.calcrange = calcrange;
		
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
	public TimeSpanForCalc reCreateTreatAsSiteiTimeEnd(AttendanceTime transTime,OverTimeFrameTimeSheet overTimeWork) {
		TimeSpanForCalc copySpan = calcrange;
		return overTimeWork.reduceUntilSpecifiedTime(new AttendanceTime(copySpan.lengthAsMinutes() - transTime.valueAsMinutes()));
	}
	
	/**
	 * 指定時間帯を指定時間に従って縮小
	 * @param assingnTime 指定時間
	 * @return 縮小後の時間帯
	 */
	public TimeSpanWithRounding reduceUntilSpecifiedTime(AttendanceTime assignTime) {
		AttendanceTime shortened = calcTotalTime().minusMinutes(assignTime.valueAsMinutes());
		
		AttendanceTime newEnd = new AttendanceTime(timeSheet.getStart().forwardByMinutes(shortened.valueAsMinutes()).valueAsMinutes());
		
		TimeSpanWithRounding newTimeSpan = new TimeSpanWithRounding(new TimeWithDayAttr(shortened.valueAsMinutes()),new TimeWithDayAttr(newEnd.valueAsMinutes()),this.timeSheet.getRounding());
		List<TimeSheetOfDeductionItem> refineList = duplicateNewTimeSpan(newTimeSpan);
		
		while(true) {
			AttendanceTime deductionTime = new AttendanceTime(0);
			newTimeSpan = new TimeSpanWithRounding(timeSheet.getStart(),new TimeWithDayAttr(newEnd.valueAsMinutes()), this.timeSheet.getRounding());
			for(TimeSheetOfDeductionItem deductionItem : refineList) {
				deductionTime = deductionItem.calcTotalTime();
				newTimeSpan = new TimeSpanWithRounding(timeSheet.getStart(),newTimeSpan.getEnd().forwardByMinutes(deductionTime.valueAsMinutes()), this.timeSheet.getRounding());
			}
			
			List<TimeSheetOfDeductionItem> moveSpanDuplicateDeductionTimeSheet = duplicateNewTimeSpan(new TimeSpanForCalc(timeSheet.getEnd(), newTimeSpan.getEnd()));
			if(moveSpanDuplicateDeductionTimeSheet.size()>0) {
				refineList.addAll(moveSpanDuplicateDeductionTimeSheet);
			}
			else {
				break; 
			}
		}
		
		return newTimeSpan;
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
	 * 時間の計算
	 * @return 
	 */
	public AttendanceTime calcTotalTime() {
		//int calcTime = timeSheet.getSpan().lengthAsMinutes() - minusDeductionTime();
		AttendanceTime calcTime = deductionLengthMinutes();
		return calcTime;
	}
	
	/**
	 * 時間帯に含んでいる控除時間を差し引いた時間を計算する(メモ：トリガー)
	 * @return 時間
	 */
	public AttendanceTime deductionLengthMinutes() {
		if(deductionTimeSheet.isEmpty()) return new AttendanceTime(0) ;
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
	public List<BonusPayTimesheet> recreateBonusPayListBeforeBase(TimeWithDayAttr baseTime,boolean isDateBefore){
		List<BonusPayTimesheet> bonusPayList = new ArrayList<>();
		for(BonusPayTimesheet bonusPay : bonusPayList) {
			if(bonusPay.contains(baseTime)) {
				bonusPayList.add(bonusPay.reCreateOwn(baseTime,isDateBefore));
			}
			else if(bonusPay.calcrange.getEnd().lessThan(baseTime) && isDateBefore) {
				bonusPayList.add(bonusPay);
			}
			else if(bonusPay.calcrange.getStart().greaterThan(baseTime) && !isDateBefore) {
				bonusPayList.add(bonusPay);
			}
		}
		return bonusPayList; 
	}
	
	/**
	 * 特定日加給時間帯のリストを作り直す
	 * @param baseTime 基準時間
	 * @param isDateBefore 基準時間より早い時間を切り出す
	 * @return 切り出した加給時間帯
	 */
	public List<SpecBonusPayTimesheet> recreateSpecifiedBonusPayListBeforeBase(TimeWithDayAttr baseTime,boolean isDateBefore){
		List<SpecBonusPayTimesheet> specifiedBonusPayList = new ArrayList<>();
		for(SpecBonusPayTimesheet specifiedBonusPay : specifiedBonusPayList) {
			if(specifiedBonusPay.contains(baseTime)) {
				specifiedBonusPayList.add(specifiedBonusPay.reCreateOwn(baseTime,isDateBefore));
			}
			else if(specifiedBonusPay.calcrange.getEnd().lessThan(baseTime) && isDateBefore) {
				specifiedBonusPayList.add(specifiedBonusPay);
			}
			else if(specifiedBonusPay.calcrange.getStart().greaterThan(baseTime) && !isDateBefore) {
				specifiedBonusPayList.add(specifiedBonusPay);
			}
		}
		return specifiedBonusPayList; 
	}
	
	/**
	 * 控除時間帯のリストを作り直す
	 * @param baseTime 基準時間
	 * @param isDateBefore 基準時間より早い時間を切り出す
	 * @return 切り出したl控除時間帯
	 */
	public List<TimeSheetOfDeductionItem> recreateDeductionItemBeforeBase(TimeWithDayAttr baseTime,boolean isDateBefore){
		List<TimeSheetOfDeductionItem> deductionList = new ArrayList<>();
		
		for(TimeSheetOfDeductionItem deductionItem : this.deductionTimeSheet) {
			
			if(deductionItem.contains(baseTime)) {
					deductionList.add(deductionItem.reCreateOwn(baseTime,isDateBefore));
			}
			else if(deductionItem.calcrange.getEnd().lessThan(baseTime) && isDateBefore) {
				deductionList.add(deductionItem);
			}
			else if(deductionItem.calcrange.getStart().greaterThan(baseTime) && !isDateBefore) {
				deductionList.add(deductionItem);
			}
		}
		
		return deductionList;
	}
	
	/**
	 * 深夜時間帯のリストを作り直す
	 * @param baseTime 基準時間
	 * @param isDateBefore 基準時間より早い時間を切り出す
	 * @return 切り出した深夜時間帯
	 */
	public Optional<MidNightTimeSheet> recreateMidNightTimeSheetBeforeBase(TimeWithDayAttr baseTime,boolean isDateBefore){
		if(this.midNightTimeSheet.isPresent()) {
			if(midNightTimeSheet.get().calcrange.contains(baseTime)) {
				return midNightTimeSheet.get().midNightTimeSheet.get().reCreateOwn(baseTime,isDateBefore);
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
	public Optional<MidNightTimeSheet> createMidNightTimeSheet() {
		if(midNightTimeSheet.isPresent()) {
			if(calcrange.checkDuplication(midNightTimeSheet.get().calcrange).isDuplicated()) { 
				return Optional.of(new MidNightTimeSheet(timeSheet,
										 calcrange.getDuplicatedWith(midNightTimeSheet.get().calcrange).get(),
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
	public List<BonusPayTime> calcBonusPay(ActualWorkTimeSheetAtr actualWorkAtr, BonusPayAutoCalcSet bonusPayCalcSet, CalAttrOfDailyPerformance calcAtrOfDaily) {
		List<BonusPayTime> bonusPayTimeList = new ArrayList<>();
		for(BonusPayTimesheet bonusPaySheet : this.bonusPayTimeSheet){
			int calcTime = bonusPaySheet.calcTotalTime().valueAsMinutes();
			bonusPayTimeList.add(new BonusPayTime(bonusPayCalcSet.getBonusPayItemNo()
												 ,TimeWithCalculation.sameTime(new AttendanceTime(calcTime))
												 ,TimeWithCalculation.sameTime(new AttendanceTime(calcTime))
												 ,TimeWithCalculation.sameTime(new AttendanceTime(calcTime))));
		}
		if(!GetCalcAtr.isCalc(calcAtrOfDaily.getRasingSalarySetting().getSalaryCalSetting().isUse(), calcAtrOfDaily, bonusPayCalcSet, actualWorkAtr)) {
			bonusPayTimeList.forEach(tc ->{tc.getBonusPay().setTime(new AttendanceTime(0));});
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
	public List<BonusPayTime> calcSpacifiedBonusPay(ActualWorkTimeSheetAtr actualWorkAtr, BonusPayAutoCalcSet bonusPayCalcSet, CalAttrOfDailyPerformance calcAtrOfDaily){
		List<BonusPayTime> bonusPayTimeList = new ArrayList<>();
		for(SpecBonusPayTimesheet bonusPaySheet : this.specBonusPayTimesheet){
			int calcTime = bonusPaySheet.calcTotalTime().valueAsMinutes();
			bonusPayTimeList.add(new BonusPayTime(bonusPayCalcSet.getBonusPayItemNo()
												 ,TimeWithCalculation.sameTime(new AttendanceTime(calcTime))
												 ,TimeWithCalculation.sameTime(new AttendanceTime(calcTime))
												 ,TimeWithCalculation.sameTime(new AttendanceTime(calcTime))));
		}
		if(!GetCalcAtr.isCalc(calcAtrOfDaily.getRasingSalarySetting().getSpecificSalaryCalSetting().isUse(), calcAtrOfDaily, bonusPayCalcSet, actualWorkAtr)) {
			bonusPayTimeList.forEach(tc ->{tc.getBonusPay().setTime(new AttendanceTime(0));});
		}
		return bonusPayTimeList;
	}
	
	/**
	 * 特定日加給時間帯の作成
	 * @param specifiedDayList　特定日
	 * @return 加給設定
	 */
	public List<SpecBonusPayTimesheet> createSpecifiedBonusPayTimeSheet(List<Integer> specifiedDayList,List<SpecBonusPayTimesheet> specifiedBonusPayTimeSheetList){
		if(specifiedDayList.size() == 0) return Collections.emptyList();
		for(SpecBonusPayTimesheet specifiedBonusPayTimeSheet : specifiedBonusPayTimeSheetList) {
			if(specifiedDayList.contains(specifiedBonusPayTimeSheet.getSpecBonusPayNumber().v().intValue())) {
				Optional<TimeSpanForCalc> newSpan = this.calcrange.getDuplicatedWith(
														new TimeSpanForCalc(new TimeWithDayAttr(specifiedBonusPayTimeSheet.getStartTime().valueAsMinutes())
																		   ,new TimeWithDayAttr(specifiedBonusPayTimeSheet.getEndTime().valueAsMinutes())));
				if(newSpan.isPresent()) {
					this.specBonusPayTimesheet.add(specifiedBonusPayTimeSheet.reCreateCalcRange(newSpan.get()));
				}
			}
		}
		return this.specBonusPayTimesheet;
	}
	
	/**
	 * 深夜時間の計算
	 * @return 深夜時間
	 */
	public int calcMidNight(AutoCalculationCategoryOutsideHours autoCalcSet) {
		if(autoCalcSet.isCalculateEmbossing())
		{
			if(this.midNightTimeSheet.isPresent()) {
				return this.midNightTimeSheet.get().calcTotalTime().valueAsMinutes();
			}
			else {
				return 0;
			}
		}
		else {
			return 0;
		}
	}
}
