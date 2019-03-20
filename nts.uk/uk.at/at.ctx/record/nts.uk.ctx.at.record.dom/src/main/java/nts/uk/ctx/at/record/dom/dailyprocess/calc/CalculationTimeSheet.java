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
import nts.uk.ctx.at.record.dom.calculationattribute.BonusPayAutoCalcSet;
import nts.uk.ctx.at.record.dom.calculationattribute.CalAttrOfDailyPerformance;
import nts.uk.ctx.at.record.dom.daily.TimeWithCalculation;
import nts.uk.ctx.at.record.dom.daily.bonuspaytime.BonusPayTime;
import nts.uk.ctx.at.record.dom.daily.midnight.MidNightTimeSheet;
import nts.uk.ctx.at.record.dom.raisesalarytime.SpecificDateAttrOfDailyPerfor;
//import nts.uk.ctx.at.record.dom.raisesalarytime.SpecificDateAttrSheet;
import nts.uk.ctx.at.record.dom.raisesalarytime.primitivevalue.SpecificDateItemNo;
import nts.uk.ctx.at.shared.dom.bonuspay.setting.BonusPaySetting;
//import nts.uk.ctx.at.shared.dom.bonuspay.setting.BonusPayTimesheet;
import nts.uk.ctx.at.shared.dom.bonuspay.setting.SpecBonusPayTimesheet;
import nts.uk.ctx.at.shared.dom.common.time.AttendanceTime;
import nts.uk.ctx.at.shared.dom.common.time.TimeSpanForCalc;
import nts.uk.ctx.at.shared.dom.common.timerounding.Rounding;
import nts.uk.ctx.at.shared.dom.common.timerounding.TimeRoundingSetting;
import nts.uk.ctx.at.shared.dom.common.timerounding.Unit;
//import nts.uk.ctx.at.shared.dom.ot.autocalsetting.AutoCalAtrOvertime;
import nts.uk.ctx.at.shared.dom.workrule.outsideworktime.AutoCalRaisingSalarySetting;
import nts.uk.ctx.at.shared.dom.worktime.common.TimeZoneRounding;
import nts.uk.ctx.at.shared.dom.worktime.common.WorkTimezoneCommonSet;
import nts.uk.shr.com.time.TimeWithDayAttr;

/**
 * 計算時間帯
 * @author keisuke_hoshina
 *
 */
@Getter
public abstract class CalculationTimeSheet {
	//時間帯(丸め付き)
	protected TimeZoneRounding timeSheet;
	//計算範囲
	protected TimeSpanForCalc calcrange;
	@Setter
	//計上用
	protected List<TimeSheetOfDeductionItem> recordedTimeSheet= new ArrayList<>();
	@Setter
	//控除用
	protected List<TimeSheetOfDeductionItem> deductionTimeSheet = new ArrayList<>();
	@Setter
	//加給
	protected List<BonusPayTimeSheetForCalc> bonusPayTimeSheet = new ArrayList<>();
	@Setter
	//特定日加給
	protected List<SpecBonusPayTimeSheetForCalc> specBonusPayTimesheet = new ArrayList<>();
	@Setter
	//深夜
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
	
	public void replaceTimeSheet(TimeSpanForCalc calcRange) {
		this.timeSheet = new TimeZoneRounding(calcRange.getStart(), calcRange.getEnd(), this.timeSheet.getRounding());
		this.calcrange = calcRange;

	}
	
	public void replaceOwnDedTimeSheet() {
		List<TimeSheetOfDeductionItem> ded = new ArrayList<>(this.deductionTimeSheet);
		this.deductionTimeSheet.clear();
		this.addDuplicatedDeductionTimeSheet(ded, DeductionAtr.Deduction, Optional.empty());
		List<TimeSheetOfDeductionItem> rec = new ArrayList<>(this.recordedTimeSheet);
		this.recordedTimeSheet.clear();
		this.addDuplicatedDeductionTimeSheet(rec, DeductionAtr.Appropriate, Optional.empty());
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
	public Optional<TimeSpanForCalc> reCreateTreatAsSiteiTimeEnd(AttendanceTime transTime,OverTimeFrameTimeSheetForCalc overTimeWork) {
		return overTimeWork.contractTimeSheet(new TimeWithDayAttr(this.calcTotalTime(DeductionAtr.Deduction).valueAsMinutes() - transTime.valueAsMinutes()));
	}
	
//	/**
//	 * 指定時間帯を指定時間に従って縮小
//	 * @param assingnTime 指定時間
//	 * @return 縮小後の時間帯
//	 */
//	public TimeSpanForCalc reduceUntilSpecifiedTime(AttendanceTime assignTime) {
//		//開始時間からの経過時間を求める
//		AttendanceTime shortened = calcTotalTime(DeductionAtr.Deduction).minusMinutes(assignTime.valueAsMinutes());
//		//開始時間と経過時間から新しいEnd時刻を求める
//		AttendanceTime newEnd = new AttendanceTime(timeSheet.getStart().forwardByMinutes(shortened.valueAsMinutes()).valueAsMinutes());
//		TimeZoneRounding newTimeSpan = new TimeZoneRounding(new TimeWithDayAttr(shortened.valueAsMinutes()),new TimeWithDayAttr(newEnd.valueAsMinutes()),this.timeSheet.getRounding());
//		//自身の計算範囲と被っている場所にある控除時間帯を求める
//		List<TimeSheetOfDeductionItem> refineList = getNewSpanIncludeCalcrange(this.deductionTimeSheet,newTimeSpan.timeSpan());
//		
//		while(true) {
//			//控除時間
//			AttendanceTime deductionTime = new AttendanceTime(0);
//			//算出した計算範囲(初期化)
//			newTimeSpan = new TimeZoneRounding(timeSheet.getStart(),new TimeWithDayAttr(newEnd.valueAsMinutes()), this.timeSheet.getRounding());
//			//含んでいる控除時間経過した分だけ終了を未来へずらす
//			for(TimeSheetOfDeductionItem deductionItem : refineList) {
//				deductionTime = deductionItem.calcTotalTime(DeductionAtr.Deduction);
//				newTimeSpan = new TimeZoneRounding(timeSheet.getStart(),newTimeSpan.getEnd().forwardByMinutes(deductionTime.valueAsMinutes()), this.timeSheet.getRounding());
//			}
//			
//			List<TimeSheetOfDeductionItem> moveSpanDuplicateDeductionTimeSheet = duplicateNewTimeSpan(new TimeSpanForCalc(timeSheet.getEnd(), newTimeSpan.getEnd()));
//			if(moveSpanDuplicateDeductionTimeSheet.size()>0) {
//				refineList.addAll(moveSpanDuplicateDeductionTimeSheet);
//			}
//			else {
//				break; 
//			}
//		}
//		
//		return newTimeSpan.timeSpan();
//	}
	
	/**
	 * 指定時間に従って時間帯の縮小
	 * @return 縮小後の時間帯
	 */
	public Optional<TimeSpanForCalc> contractTimeSheet(TimeWithDayAttr timeWithDayAttr) {
		/*ここのcalcTotalTImeは残業時間帯の時間*/
		int afterShort = calcTotalTime(DeductionAtr.Deduction).valueAsMinutes() - timeWithDayAttr.valueAsMinutes();
		if(afterShort <= 0) return Optional.empty();
		TimeSpanForCalc newSpan = new TimeSpanForCalc(timeSheet.getStart(), timeSheet.getStart().forwardByMinutes(afterShort));
		List<TimeSheetOfDeductionItem> copyList = getNewSpanIncludeCalcrange(deductionTimeSheet,newSpan);
		for(int listn = 0 ; listn < copyList.size() ; listn++){
				/*ここのcalcTotalTimeは残業時間帯が持ってる控除時間帯の時間*/
				int differTime = copyList.get(listn).calcTotalTime(DeductionAtr.Deduction).valueAsMinutes();
				/*ずらす前に範囲内に入っている時間帯の数を保持*/
				int beforeincludeSpan = copyList.size();//getNewSpanIncludeCalcrange(copyList,newSpan).size();
				//含んでいる控除時間帯分未来へずらす
				newSpan = newSpan.shiftEndAhead(differTime);
//				//
//				val moveAfterNewSpan = newSpan.shiftEndAhead(copyList.stream().map(ts -> ts.calcrange.lengthAsMinutes()).collect(Collectors.summingInt(tc -> tc)));
				//増やしたことで、含む控除時間が増えるかどうかをチェック(含んでいる控除時間帯の数を数えなおす)
				int afterincludeSpan = getNewSpanIncludeCalcrange(deductionTimeSheet,newSpan).size();
				/*ずらした後の範囲に入っている時間帯の数とずらす前のかずを比較し増えていた場合、控除時間帯を保持してる変数に追加する*/
				if(afterincludeSpan > beforeincludeSpan) {
					copyList = Collections.emptyList();
					copyList = getNewSpanIncludeCalcrange(deductionTimeSheet,newSpan);
				}
		}
		return Optional.of(newSpan);
	}
	
	/**
	 *受け取った時間帯に含まれている控除項目の時間帯をリストにする
	 * @param newTimeSpan 時間帯
	 * @return　控除時間帯リスト
	 */
	public List<TimeSheetOfDeductionItem> duplicateNewTimeSpan(TimeSpanForCalc newTimeSpan){
		return deductionTimeSheet.stream().filter(tc -> newTimeSpan.contains(tc.timeSheet.getTimeSpan())).collect(Collectors.toList());
	}
	
	/**
	 * 
	 * 　時間帯と重複している控除時間帯のみを抽出する
	 * @param　控除項目の時間帯(List)
	 */
	private List<TimeSheetOfDeductionItem> getNewSpanIncludeCalcrange(List<TimeSheetOfDeductionItem> copyList , TimeSpanForCalc newSpan){
		return copyList.stream().filter(tc -> newSpan.checkDuplication(tc.timeSheet.getTimeSpan()).isDuplicated()).collect(Collectors.toList());
	}
	
	/**
	 * 控除時間の合計を算出する
	 * @param dedAtr
	 * @param conditionAtr
	 * @return
	 */
	public AttendanceTime calcDedTimeByAtr(DeductionAtr dedAtr,ConditionAtr conditionAtr) {
		val forCalcList = getDedTimeSheetByAtr(dedAtr,conditionAtr);
		return new AttendanceTime(forCalcList.stream().map(tc -> tc.calcTotalTime(dedAtr).valueAsMinutes()).collect(Collectors.summingInt(tc -> tc)));
	}
		
	/**
	 * 控除時間の合計を算出する（指定なし)
	 * @param dedAtr
	 * @param conditionAtr
	 * @return
	 */
	public AttendanceTime calcDedTimeByAtr(DeductionAtr dedAtr) {
		val forCalcList = (dedAtr.isDeduction())?this.deductionTimeSheet:this.recordedTimeSheet;
		return new AttendanceTime(forCalcList.stream().map(tc -> tc.calcTotalTime(dedAtr).valueAsMinutes()).collect(Collectors.summingInt(tc -> tc)));
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
				val list = returnList.stream().filter(tc -> tc.getDeductionAtr().isChildCare()).collect(Collectors.toList());	
				List<TimeSheetOfDeductionItem> list2 = new ArrayList<>();
				for(TimeSheetOfDeductionItem timeSheetOfDeductionItem:list) {
					if(timeSheetOfDeductionItem.getChildCareAtr().isPresent()&&timeSheetOfDeductionItem.getChildCareAtr().get().isCare()) {
						list2.add(timeSheetOfDeductionItem);
					}
				}
				return list2;
			case Child:
				val list3 = returnList.stream().filter(tc -> tc.getDeductionAtr().isChildCare()).collect(Collectors.toList());
				List<TimeSheetOfDeductionItem> list4 = new ArrayList<>();
				for(TimeSheetOfDeductionItem timeSheetOfDeductionItem:list3) {
					if(timeSheetOfDeductionItem.getChildCareAtr().isPresent()&&timeSheetOfDeductionItem.getChildCareAtr().get().isChildCare()) {
						list4.add(timeSheetOfDeductionItem);
					}
				}
				 return list4;					  
			case CompesationGoOut:
				return returnList.stream().filter(tc -> tc.getDeductionAtr().isGoOut() 
													 && tc.getGoOutReason().get().isCompensation()).collect(Collectors.toList());
			case PrivateGoOut:
				return returnList.stream().filter(tc -> tc.getDeductionAtr().isGoOut() 
													 && tc.getGoOutReason().get().isPrivate()).collect(Collectors.toList());
			case PublicGoOut:
				return returnList.stream().filter(tc -> tc.getDeductionAtr().isGoOut()
													&& tc.getGoOutReason().get().isPublic()).collect(Collectors.toList());
			case UnionGoOut:
				return returnList.stream().filter(tc -> tc.getDeductionAtr().isGoOut()
													&& tc.getGoOutReason().get().isUnion()).collect(Collectors.toList());
			default:
				throw new RuntimeException("unknown condition Atr");
		}
	}


	/**
	 * 時間の計算(自分自身ー自分が持つ控除時間帯.length)
	 * @return 
	 */
	public AttendanceTime calcTotalTime(DeductionAtr dedAtr) {
		return deductionLengthMinutes(dedAtr);
	}
	
	/**
	 * 控除時間の計算
	 * @return 控除時間
	 */
	public AttendanceTime calcTotalDeductionTime() {
		return new AttendanceTime(this.getDeductionTimeSheet().stream().map(tc-> tc.calcTotalTime(DeductionAtr.Deduction).valueAsMinutes()).collect(Collectors.summingInt(tc -> tc)));
	}
	
	/**
	 * 時間帯に含んでいる控除時間を差し引いた時間を計算する(メモ：トリガー)
	 * @return 時間
	 */
	private AttendanceTime deductionLengthMinutes(DeductionAtr dedAtr) {
		
		if(deductionTimeSheet.isEmpty()) {
			//丸め設定の取得
			TimeRoundingSetting rounding = this.timeSheet.getRounding();
			if(rounding != null) {
				return new AttendanceTime(rounding.round(this.timeSheet.getTimeSpan().lengthAsMinutes()));
			}
			else {
				return new AttendanceTime(this.timeSheet.getTimeSpan().lengthAsMinutes());
			}
		}
		return recursiveTotalTime(dedAtr) ;
	}
	
	/**
	 * 控除時間の合計を求める(メモ：再帰)
	 * @return　控除の合計時間
	 */
	protected AttendanceTime recursiveTotalTime(DeductionAtr dedAtr) {
		List<TimeSheetOfDeductionItem> prodcessList = dedAtr.isAppropriate()?this.getRecordedTimeSheet():this.getDeductionTimeSheet();
		if(prodcessList.isEmpty()) return new AttendanceTime(timeSheet.getTimeSpan().lengthAsMinutes());
		AttendanceTime totalDedTime = new AttendanceTime(0);
		for(TimeSheetOfDeductionItem dedTimeSheet : prodcessList) {
			totalDedTime = new AttendanceTime(totalDedTime.valueAsMinutes()+dedTimeSheet.recursiveTotalTime(dedAtr).valueAsMinutes());
		}
		//丸め設定の取得
		TimeRoundingSetting rounding = this.timeSheet.getRounding();
		//丸め処理
		return new AttendanceTime(rounding.round(timeSheet.getTimeSpan().lengthAsMinutes() - totalDedTime.valueAsMinutes()));	
	}

	

//	/**
//	 * 
//	 * @param basePoint 
//	 * @return 
//	 */
//	public int calcIncludeTimeSheet(int basePoint, List<TimeSheetOfDeductionItem> deductionItemList){
//		return deductionItemList.stream().map(ts -> ts.timeSheet.getTimeSpan().lengthAsMinutes()).collect(Collectors.summingInt(tc -> tc));
//	}
//	
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
	//	for(BonusPayTimeSheetForCalc bonusPay : bonusPayList) {
//			if(bonusPay..contains(baseTime)) {
//				bonusPayList.add(bonusPay.reCreateOwn(baseTime,isDateBefore));
//			}
//			else if(bonusPay.calcrange.getEnd().lessThan(baseTime) && isDateBefore) {
//				bonusPayList.add(bonusPay);
//			}
//			else if(bonusPay.calcrange.getStart().greaterThan(baseTime) && !isDateBefore) {
//				bonusPayList.add(bonusPay);
//			}
		//}
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
//		for(SpecBonusPayTimeSheetForCalc specifiedBonusPay : specifiedBonusPayList) {
//			if(specifiedBonusPay.contains(baseTime)) {
//				specifiedBonusPayList.add(specifiedBonusPay.reCreateOwn(baseTime,isDateBefore));
//			}
//			else if(specifiedBonusPay.calcrange.getEnd().lessThan(baseTime) && isDateBefore) {
//				specifiedBonusPayList.add(specifiedBonusPay);
//			}
//			else if(specifiedBonusPay.calcrange.getStart().greaterThan(baseTime) && !isDateBefore) {
//				specifiedBonusPayList.add(specifiedBonusPay);
//			}
//		}
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
				//returnList.add(deductionItem);
			}
			else if(deductionItem.timeSheet.getTimeSpan().getEnd().lessThan(baseTime) && isDateBefore) {
				returnList.add(deductionItem);
			}
			else if(deductionItem.timeSheet.getTimeSpan().getStart().greaterThan(baseTime) && !isDateBefore) {
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
			if(midNightTimeSheet.get().timeSheet.getTimeSpan().contains(baseTime)) {
				return midNightTimeSheet.get().reCreateOwn(baseTime,isDateBefore);
				//return midNightTimeSheet;
			}
			else if(midNightTimeSheet.get().timeSheet.getTimeSpan().getEnd().lessThan(baseTime) && isDateBefore) {
				return midNightTimeSheet;
			}
			else if(midNightTimeSheet.get().timeSheet.getTimeSpan().getStart().greaterThan(baseTime) && !isDateBefore) {
				return midNightTimeSheet;
			}
		}
		return Optional.empty();
	}
	
//	/**
//	 * 深夜時間帯の作成(トリガー)
//	 * @param midnightTimeSheet
//	 */
//	public Optional<MidNightTimeSheetForCalc> createMidNightTimeSheet() {
//		if(midNightTimeSheet.isPresent()) {
//			if(calcrange.checkDuplication(midNightTimeSheet.get().calcrange).isDuplicated()) { 
//				return Optional.of(new MidNightTimeSheetForCalc(timeSheet,
//										 calcrange.getDuplicatedWith(midNightTimeSheet.get().calcrange).get(),
//										 duplicateTimeSpan(midNightTimeSheet.get().calcrange),
//										 duplicateTimeSpan(midNightTimeSheet.get().calcrange),
//										 bonusPayTimeSheet,
//										 specBonusPayTimesheet,
//										 midNightTimeSheet
//										 ));
//			}
//		}
//		return Optional.empty();
//	}
	
	/**
	 * 深夜時間帯の作成(再帰)
	 * @param timeSpan
	 * @return
	 */
	public List<TimeSheetOfDeductionItem> duplicateTimeSpan(TimeSpanForCalc timeSpan) {
		List<TimeSheetOfDeductionItem> returnList = new ArrayList<>();
		for(TimeSheetOfDeductionItem deductionTimeSheet : deductionTimeSheet) {
			if(midNightTimeSheet.isPresent()) {
				Optional<TimeSpanForCalc> duplicateSpan = midNightTimeSheet.get().timeSheet.getTimeSpan().getDuplicatedWith(deductionTimeSheet.timeSheet.getTimeSpan());
				if(duplicateSpan.isPresent()) {
					returnList.add(TimeSheetOfDeductionItem.createTimeSheetOfDeductionItemAsFixed(
																								deductionTimeSheet.timeSheet
																							   ,deductionTimeSheet.calcrange
																							   ,deductionTimeSheet.recordedTimeSheet
																							   ,deductionTimeSheet.deductionTimeSheet
																							   ,deductionTimeSheet.bonusPayTimeSheet
																							   ,deductionTimeSheet.specBonusPayTimesheet
																							   ,deductionTimeSheet.midNightTimeSheet
																							   ,deductionTimeSheet.getWorkingBreakAtr()
																							   ,deductionTimeSheet.getGoOutReason()
																							   ,deductionTimeSheet.getBreakAtr()
																							   ,deductionTimeSheet.getShortTimeSheetAtr()
																							   ,deductionTimeSheet.getDeductionAtr()
																							   ,deductionTimeSheet.getChildCareAtr()));
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
	public List<BonusPayTime> calcBonusPay(ActualWorkTimeSheetAtr actualWorkAtr, AutoCalRaisingSalarySetting raisingAutoCalcSet,BonusPayAutoCalcSet bonusPayAutoCalcSet, CalAttrOfDailyPerformance calcAtrOfDaily,BonusPayAtr bonusPayAtr) {
		List<BonusPayTime> bonusPayTimeList = new ArrayList<>();
		for(BonusPayTimeSheetForCalc bonusPaySheet : this.bonusPayTimeSheet){
			AttendanceTime calcTime = raisingAutoCalcSet.isRaisingSalaryCalcAtr()?bonusPaySheet.calcTotalTime(DeductionAtr.Deduction): new AttendanceTime(0);
			AttendanceTime time = raisingAutoCalcSet.isRaisingSalaryCalcAtr()?bonusPaySheet.calcTotalTime(DeductionAtr.Appropriate): new AttendanceTime(0);
			
			bonusPayTimeList.add(new BonusPayTime(bonusPaySheet.getRaiseSalaryTimeItemNo().v().intValue()
												 ,calcTime
												 ,TimeWithCalculation.createTimeWithCalculation(actualWorkAtr.isWithinWorkTime()?time:new AttendanceTime(0),actualWorkAtr.isWithinWorkTime()?calcTime:new AttendanceTime(0))
												 ,TimeWithCalculation.createTimeWithCalculation(actualWorkAtr.isWithinWorkTime()?new AttendanceTime(0):time,actualWorkAtr.isWithinWorkTime()?new AttendanceTime(0):calcTime)));
		}
		if(!GetCalcAtr.isCalc(calcAtrOfDaily.getRasingSalarySetting().isRaisingSalaryCalcAtr(), calcAtrOfDaily, bonusPayAutoCalcSet, actualWorkAtr)) {
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
	public List<BonusPayTime> calcSpacifiedBonusPay(ActualWorkTimeSheetAtr actualWorkAtr, AutoCalRaisingSalarySetting raisingAutoCalcSet,BonusPayAutoCalcSet bonusPayAutoCalcSet, CalAttrOfDailyPerformance calcAtrOfDaily,BonusPayAtr bonusPayAtr){
		List<BonusPayTime> bonusPayTimeList = new ArrayList<>();
		for(SpecBonusPayTimeSheetForCalc bonusPaySheet : this.specBonusPayTimesheet){
			AttendanceTime calcTime = raisingAutoCalcSet.isSpecificRaisingSalaryCalcAtr()?bonusPaySheet.calcTotalTime(DeductionAtr.Deduction):new AttendanceTime(0);
			AttendanceTime time = raisingAutoCalcSet.isSpecificRaisingSalaryCalcAtr()?bonusPaySheet.calcTotalTime(DeductionAtr.Appropriate):new AttendanceTime(0);
			bonusPayTimeList.add(new BonusPayTime(bonusPaySheet.getSpecBonusPayNumber().v().intValue()
					 ,calcTime
					 ,TimeWithCalculation.createTimeWithCalculation(actualWorkAtr.isWithinWorkTime()?time:new AttendanceTime(0),actualWorkAtr.isWithinWorkTime()?calcTime:new AttendanceTime(0))
					 ,TimeWithCalculation.createTimeWithCalculation(actualWorkAtr.isWithinWorkTime()?new AttendanceTime(0):time,actualWorkAtr.isWithinWorkTime()?new AttendanceTime(0):calcTime)));
		}
		if(!GetCalcAtr.isCalc(calcAtrOfDaily.getRasingSalarySetting().isSpecificRaisingSalaryCalcAtr(), calcAtrOfDaily, bonusPayAutoCalcSet, actualWorkAtr)) {
			bonusPayTimeList.forEach(tc ->{tc.getWithinBonusPay().setTime(new AttendanceTime(0));
										   tc.getExcessBonusPayTime().setTime(new AttendanceTime(0));});
		}
		return bonusPayTimeList;
	}
	
//	/**
//	 * 特定日加給時間帯の作成
//	 * @param specifiedDayList　特定日
//	 * @return 加給設定
//	 */
//	public List<SpecBonusPayTimeSheetForCalc> createSpecifiedBonusPayTimeSheet(List<Integer> specifiedDayList,List<SpecBonusPayTimeSheetForCalc> specifiedBonusPayTimeSheetList){
//		if(specifiedDayList.size() == 0) return Collections.emptyList();
//		for(SpecBonusPayTimeSheetForCalc specifiedBonusPayTimeSheet : specifiedBonusPayTimeSheetList) {
//			if(specifiedDayList.contains(specifiedBonusPayTimeSheet.getSpecBonusPayNumber().v().intValue())) {
//				Optional<TimeSpanForCalc> newSpan = this.timeSheet.getTimeSpan().getDuplicatedWith(
//														new TimeSpanForCalc(new TimeWithDayAttr(specifiedBonusPayTimeSheet.getCalcrange().getStart().valueAsMinutes())
//																		   ,new TimeWithDayAttr(specifiedBonusPayTimeSheet.getCalcrange().getEnd().valueAsMinutes())));
//				if(newSpan.isPresent()) {
//					this.specBonusPayTimesheet.add(specifiedBonusPayTimeSheet.convertForCalcCorrectRange(newSpan.get()));
//				}
//			}
//		}
//		return this.specBonusPayTimesheet;
//	}
//	
//	/**
//	 * 深夜時間の計算
//	 * @return 深夜時間
//	 */
//	public AttendanceTime calcMidNight(AutoCalAtrOvertime autoCalcSet) {
//		if(autoCalcSet.isCalculateEmbossing())
//		{
//			if(this.midNightTimeSheet.isPresent()) {
//				return this.midNightTimeSheet.get().calcTotalTime(DeductionAtr.Deduction);
//			}
//			else {
//				return new AttendanceTime(0);
//			}
//		}
//		else {
//			return new AttendanceTime(0);
//		}
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
								.filter(tc -> tc.getCalcrange().getDuplicatedWith(timeSpan).isPresent())
								.map(tc -> tc.convertForCalcCorrectRange(tc.getCalcrange().getDuplicatedWith(timeSpan).get()))
								.collect(Collectors.toList());
	}
	/**
	 * 指定された時間帯と重複している加給時間帯を取得
	 * @param bonusPayTimeSheet
	 * @param duplicateTimeSheet
	 * @return
	 */
	public List<BonusPayTimeSheetForCalc> getDuplicatedBonusPayNotStatic(List<BonusPayTimeSheetForCalc> bonusPayTimeSheet,TimeSpanForCalc timeSpan){
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
	 * 指定された時間帯と重複している特定加給時間帯を取得
	 * @param bonusPayTimeSheet
	 * @param duplicateTimeSheet
	 * @return
	 */
	public List<SpecBonusPayTimeSheetForCalc> getDuplicatedSpecBonusPayzNotStatic(List<SpecBonusPayTimeSheetForCalc> bonusPayTimeSheet,TimeSpanForCalc timeSpan){
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
	public static Optional<MidNightTimeSheetForCalc> getDuplicateMidNight(MidNightTimeSheetForCalc midNightTimeSheet, TimeSpanForCalc timeSpan){ 
		val duplicateMidNightSpan = timeSpan.getDuplicatedWith(midNightTimeSheet.getTimeSheet().getTimeSpan());
		if(duplicateMidNightSpan.isPresent()) {
			return Optional.of(midNightTimeSheet.replaceTime(duplicateMidNightSpan.get()));
		}
		return Optional.empty();
	}
	
	/**
	 * 指定された時間帯と重複している深夜時間帯を取得
	 * @param midNightTimeSheet
	 * @param duplicateTimeSheet
	 * @return
	 */
	public Optional<MidNightTimeSheetForCalc> getDuplicateMidNightNotStatic(MidNightTimeSheetForCalc midNightTimeSheet, TimeSpanForCalc timeSpan){ 
		val duplicateMidNightSpan = timeSpan.getDuplicatedWith(midNightTimeSheet.getTimeSheet().getTimeSpan());
		if(duplicateMidNightSpan.isPresent()) {
			return Optional.of(midNightTimeSheet.replaceTime(duplicateMidNightSpan.get()));
		}
		return Optional.empty();
	}
	
	/**
	 * 自身と重複している控除時間帯に絞り込む
	 * @param deductionTimeSheet
	 * @return
	 */
	private List<TimeSheetOfDeductionItem> getDuplicatedDeductionTimeSheet(List<TimeSheetOfDeductionItem> deductionTimeSheet) {
		if(deductionTimeSheet == null) return deductionTimeSheet;
		return deductionTimeSheet.stream()
								 .filter(tc -> tc != null)
						  		 .filter(tc -> this.getTimeSheet().getTimeSpan().getDuplicatedWith(tc.getTimeSheet().getTimeSpan()).isPresent())
						  		 .map(tc -> tc.createDuplicateRange(this.getTimeSheet().getTimeSpan().getDuplicatedWith(tc.getTimeSheet().getTimeSpan()).get()).get())
						  		 .collect(Collectors.toList());
	}
	
	/**
	 * 自分に重複している控除時間帯になるように補正して保持する
	 * @param dedAtr 渡すリストが控除か計上用か
	 * @param roundingSet 変更したい丸め設定(そのままでいい場合、emptyで)
	 */
	public void addDuplicatedDeductionTimeSheet(List<TimeSheetOfDeductionItem> deductionTimeSheet,DeductionAtr dedAtr,Optional<TimeRoundingSetting> roundingSet) {
		if(roundingSet.isPresent()) {
			deductionTimeSheet = deductionTimeSheet.stream().map(tc -> TimeSheetOfDeductionItem.createTimeSheetOfDeductionItemAsFixed(new TimeZoneRounding(tc.getTimeSheet().getStart() ,tc.getTimeSheet().getEnd(), roundingSet.get()), 
																																	  tc.getCalcrange(), 
																																	  tc.getRecordedTimeSheet(), 
																																	  tc.getDeductionTimeSheet(), 
																																	  tc.getBonusPayTimeSheet(), 
																																	  tc.getSpecBonusPayTimesheet(), 
																																	  tc.getMidNightTimeSheet(),
																																	  tc.getWorkingBreakAtr(),
																																	  tc.getGoOutReason(), 
																																	  tc.getBreakAtr(), 
																																	  tc.getShortTimeSheetAtr(), 
																																	  tc.getDeductionAtr(),
																																	  tc.getChildCareAtr()))
															.collect(Collectors.toList());
		}
		if(dedAtr.isAppropriate()) {
			if(this.recordedTimeSheet != null && !this.recordedTimeSheet.isEmpty()) {
				val test = getDuplicatedDeductionTimeSheet(deductionTimeSheet);
				if(test != null)
					this.recordedTimeSheet.addAll(test);
			}
			else {
				val test = getDuplicatedDeductionTimeSheet(deductionTimeSheet);
				if(test != null)
					this.recordedTimeSheet = test;
			}
				
		}
		else {
			if(this.deductionTimeSheet != null && !this.deductionTimeSheet.isEmpty()) {
				val test = getDuplicatedDeductionTimeSheet(deductionTimeSheet);
				if(test != null)
					this.deductionTimeSheet.addAll(test);
			}
			else {
				val test = getDuplicatedDeductionTimeSheet(deductionTimeSheet);
				if(test != null)
					this.deductionTimeSheet = test;

			}
		}
	}
	
	/*実働時間帯へジェネリクスに変えて飛ばしたい*/
	//重複している控除を入れたい
	public static List<BonusPayTimeSheetForCalc> bonusPay(List<BonusPayTimeSheetForCalc> calcTimeSheetList,List<TimeSheetOfDeductionItem> dedSheetList,List<TimeSheetOfDeductionItem> recordSheetList){
		calcTimeSheetList.forEach(tc -> tc.addDuplicatedDeductionTimeSheet(dedSheetList, DeductionAtr.Deduction,Optional.empty()));
		calcTimeSheetList.forEach(tc -> tc.addDuplicatedDeductionTimeSheet(recordSheetList, DeductionAtr.Appropriate,Optional.empty()));
		return calcTimeSheetList;
	}
	/*実働時間帯へジェネリクスに変えて飛ばしたい*/
	//重複している控除を入れたい
	public static List<SpecBonusPayTimeSheetForCalc> specBonusPay(List<SpecBonusPayTimeSheetForCalc> calcTimeSheetList,List<TimeSheetOfDeductionItem> dedSheetList,List<TimeSheetOfDeductionItem> recordSheetList){
		calcTimeSheetList.forEach(tc -> tc.addDuplicatedDeductionTimeSheet(dedSheetList, DeductionAtr.Deduction,Optional.empty()));
		calcTimeSheetList.forEach(tc -> tc.addDuplicatedDeductionTimeSheet(recordSheetList, DeductionAtr.Appropriate,Optional.empty()));
		return calcTimeSheetList;
	}
	
	/**
	 * 加給時間帯と重複している控除項目時間帯を加給時間帯へ保持させる 
	 * (実働時間帯へ持っていきたい)
	 */
	public static List<BonusPayTimeSheetForCalc> getBonusPayTimeSheetIncludeDedTimeSheet(Optional<BonusPaySetting> bonuspaySetting,TimeSpanForCalc duplicateTimeSheet,
															   							  List<TimeSheetOfDeductionItem> dedTimeSheet,
															   							  List<TimeSheetOfDeductionItem> recordTimeSheet){
		List<BonusPayTimeSheetForCalc> duplicatedBonusPay = new ArrayList<>();
		if(bonuspaySetting.isPresent()) {
			val bpTimeSheet = bonuspaySetting.get().getLstBonusPayTimesheet().stream()
			   											  					 .filter(tc -> tc.getUseAtr().isUse())
			   											  					 .map(tc ->BonusPayTimeSheetForCalc.convertForCalc(tc))
			   											  					 .collect(Collectors.toList());
			duplicatedBonusPay = getDuplicatedBonusPay(bpTimeSheet,
					  								   duplicateTimeSheet);
		}
		return bonusPay(duplicatedBonusPay,dedTimeSheet,recordTimeSheet);
	}

	/**
	 * 特定加給時間帯と重複している控除項目時間帯を加給時間帯へ保持させる 
	 * (実働時間帯へ持っていきたい)
	 */
	public static List<SpecBonusPayTimeSheetForCalc> getSpecBonusPayTimeSheetIncludeDedTimeSheet(Optional<BonusPaySetting> bonuspaySetting,TimeSpanForCalc duplicateTimeSheet,
															   		   						  List<TimeSheetOfDeductionItem> dedTimeSheet,
															   		   						  List<TimeSheetOfDeductionItem> recordTimeSheet,
															   		   						  Optional<SpecificDateAttrOfDailyPerfor> specificDateAttrSheets){
		List<SpecBonusPayTimeSheetForCalc> duplicatedSpecBonusPay = new ArrayList<>();
		if(bonuspaySetting.isPresent()) {
			val specBpTimeSheet = bonuspaySetting.get().getLstSpecBonusPayTimesheet().stream()
			   												   					 .filter(tc -> tc.getUseAtr().isUse())
			   												   					 .collect(Collectors.toList());
			if(specificDateAttrSheets.isPresent()) {
				val useSpecTimeSheet = getUseSpecTimeSheet(specBpTimeSheet,specificDateAttrSheets.get().getUseNo());
				duplicatedSpecBonusPay = getDuplicatedSpecBonusPay(useSpecTimeSheet.stream().map(tc ->SpecBonusPayTimeSheetForCalc.convertForCalc(tc)).collect(Collectors.toList()),
						   											duplicateTimeSheet);
			}
		}
		return specBonusPay(duplicatedSpecBonusPay,dedTimeSheet,recordTimeSheet);
	}
	
	/**
	 * 日別実績の特定日区分を基に特定日として利用するか判定する
	 * @param specBpTimeSheets　特定日時間帯
	 * @param specNoList　特定日NOリスト
	 * @return 使用する特定日時間帯
	 */
	private static List<SpecBonusPayTimesheet> getUseSpecTimeSheet(List<SpecBonusPayTimesheet> specBpTimeSheets,List<SpecificDateItemNo> specNoList){
		List<SpecBonusPayTimesheet> returnList = new ArrayList<>();
		for(SpecBonusPayTimesheet specTimeSheet:specBpTimeSheets) {
			if(specNoList.contains(new SpecificDateItemNo(specTimeSheet.getDateCode())))
				returnList.add(specTimeSheet);
		}
		return returnList;
	}
	

//	/**
//	 * 日別実績の特定日区分を基に加給として使用する日を判定する
//	 * @param bpTimeSheets　加給時間帯
//	 * @param specNoList　特定日NOリスト
//	 * @return 使用する加給時間帯
//	 */
//	private static List<BonusPayTimeSheetForCalc> getUseBpTimeSheet(List<BonusPayTimeSheetForCalc> bpTimeSheets,List<SpecificDateItemNo> specNoList){
//		List<BonusPayTimeSheetForCalc> returnList = new ArrayList<>();
//		for(BonusPayTimeSheetForCalc bpTimeSheet:bpTimeSheets) {
//			if(!specNoList.contains(new SpecificDateItemNo(bpTimeSheet.getRaiseSalaryTimeItemNo().v().intValue())))
//				returnList.add(bpTimeSheet);
//		}
//		return returnList;
//	}
	
	/**
	 * 深夜時間帯と重複している控除項目時間帯を深夜時間帯へ保持させる 
	 * (実働時間帯へ持っていきたい)
	 */
	@SuppressWarnings("static-access")
	public static Optional<MidNightTimeSheetForCalc> getMidNightTimeSheetIncludeDedTimeSheet(MidNightTimeSheet midNightTimeSheet,TimeSpanForCalc duplicateTimeSheet,
															   List<TimeSheetOfDeductionItem> dedTimeSheet,
															   List<TimeSheetOfDeductionItem> recordTimeSheet,Optional<WorkTimezoneCommonSet> commonSetting){
		val midNightTimeForCalc = MidNightTimeSheetForCalc.convertForCalc(midNightTimeSheet,commonSetting);
		val duplicatedMidNight = midNightTimeForCalc.getDuplicateMidNight(midNightTimeForCalc,duplicateTimeSheet);
		if(duplicatedMidNight.isPresent()) {
			duplicatedMidNight.get().addDuplicatedDeductionTimeSheet(dedTimeSheet, DeductionAtr.Deduction,Optional.of(new TimeRoundingSetting(Unit.ROUNDING_TIME_1MIN, Rounding.ROUNDING_DOWN)));
			duplicatedMidNight.get().addDuplicatedDeductionTimeSheet(recordTimeSheet, DeductionAtr.Appropriate,Optional.of(new TimeRoundingSetting(Unit.ROUNDING_TIME_1MIN, Rounding.ROUNDING_DOWN)));
		}
		return duplicatedMidNight;
	}
	
	//---------------------------実働時間帯へ持っていきたい-------------------------------↑
	
	/**
	 *　指定条件の控除項目だけの控除時間(再起のトリガー)
	 * @param forcsList
	 * @param atr
	 * @return
	 */
	public AttendanceTime forcs(ConditionAtr atr,DeductionAtr dedAtr){
		AttendanceTime dedTotalTime = new AttendanceTime(0);
		dedTotalTime = new AttendanceTime(getDedTimeSheetByAtr(dedAtr, atr).stream().map(tc -> tc.calcTotalTime(dedAtr).valueAsMinutes()).collect(Collectors.summingInt(tc -> tc)));
		return dedTotalTime;
	}
	
	/**
	 * 短時間勤務時間帯の収集
	 * @return　短時間勤務時間帯List
	 */
	public List<TimeSheetOfDeductionItem> collectShortTimeSheet() {
		List<TimeSheetOfDeductionItem> returnList = new ArrayList<>();
		List<TimeSheetOfDeductionItem> shortTimeSheet = new ArrayList<>();
		//控除
		if(this.getDeductionTimeSheet() != null) {
			shortTimeSheet = this.getRecordedTimeSheet().stream().map(tc -> tc.collectShortTime()).flatMap(List::stream).collect(Collectors.toList());
			returnList.addAll(shortTimeSheet);
		}
		//加給
		if(this.getBonusPayTimeSheet() != null) {
			shortTimeSheet = this.getBonusPayTimeSheet().stream().map(tc -> tc.collectShortTimeSheet()).flatMap(List::stream).collect(Collectors.toList());
			returnList.addAll(shortTimeSheet);
		}
		//特定日
		if(this.getSpecBonusPayTimesheet() != null) {
			shortTimeSheet = this.getSpecBonusPayTimesheet().stream().map(tc -> tc.collectShortTimeSheet()).flatMap(List::stream).collect(Collectors.toList());
			returnList.addAll(shortTimeSheet);
		}
		//深夜
		if(this.getMidNightTimeSheet() != null && this.getMidNightTimeSheet().isPresent())
			returnList.addAll(this.getMidNightTimeSheet().get().collectShortTimeSheet());
		return returnList;
	}
}
