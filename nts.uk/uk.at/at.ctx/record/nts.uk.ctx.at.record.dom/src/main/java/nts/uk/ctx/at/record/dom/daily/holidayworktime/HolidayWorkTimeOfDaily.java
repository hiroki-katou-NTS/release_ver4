package nts.uk.ctx.at.record.dom.daily.holidayworktime;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import lombok.Value;
import lombok.val;
import nts.gul.util.value.Finally;
import nts.uk.ctx.at.record.dom.bonuspay.autocalc.BonusPayAutoCalcSet;
import nts.uk.ctx.at.record.dom.calculationattribute.CalAttrOfDailyPerformance;
import nts.uk.ctx.at.record.dom.daily.TimeWithCalculation;
import nts.uk.ctx.at.record.dom.daily.bonuspaytime.BonusPayTime;
import nts.uk.ctx.at.record.dom.dailyprocess.calc.ActualWorkTimeSheetAtr;
import nts.uk.ctx.at.record.dom.dailyprocess.calc.BonusPayAtr;
import nts.uk.ctx.at.record.dom.dailyprocess.calc.ControlHolidayWorkTime;
import nts.uk.ctx.at.record.dom.dailyprocess.calc.OverTimeFrameTimeSheetWork;
import nts.uk.ctx.at.record.dom.dailyprocess.calc.withinstatutory.LateDecisionClock;
import nts.uk.ctx.at.shared.dom.bonuspay.BonusPayAutoCalcSet;
import nts.uk.ctx.at.shared.dom.common.time.AttendanceTime;
import nts.uk.ctx.at.shared.dom.workrule.outsideworktime.AutoCalcSetOfHolidayWorkTime;
import nts.uk.ctx.at.shared.dom.workrule.outsideworktime.holidaywork.HolidayWorkFrameNo;
import nts.uk.ctx.at.shared.dom.workrule.outsideworktime.holidaywork.StaturoryAtrOfHolidayWork;
import nts.uk.ctx.at.shared.dom.workrule.overtime.StatutoryPrioritySet;

/**
 * 日別実績の休出時間
 * @author keisuke_hoshina
 *
 */
@Value
public class HolidayWorkTimeOfDaily {
	private List<HolidayWorkFrameTimeSheet> holidayWorkFrameTimeSheet;
	private List<HolidayWorkFrameTime> holidayWorkFrameTime;
	private Finally<HolidayMidnightWork> holidayMidNightWork;
	private AttendanceTime holidayTimeSpentAtWork = new AttendanceTime(0);
	
	public HolidayWorkTimeOfDaily(List<HolidayWorkFrameTimeSheet> holidayWorkFrameTimeSheet,List<HolidayWorkFrameTime> holidayWorkFrameTime,
								   Finally<HolidayMidnightWork> holidayMidNightWork,AttendanceTime holidayTimeSpentAtWork) {
		this.holidayWorkFrameTimeSheet = holidayWorkFrameTimeSheet;
		this.holidayWorkFrameTime = holidayWorkFrameTime;
		this.holidayMidNightWork = holidayMidNightWork;
		this.holidayTimeSpentAtWork.addMinutes(holidayTimeSpentAtWork.valueAsMinutes());
	}
	

	/**
	 * 休出時間枠時間帯をループさせ時間を計算する
	 */
	public List<HolidayWorkFrameTime> collectHolidayWorkTime(AutoCalcSetOfHolidayWorkTime autoCalcSet) {
		List<HolidayWorkFrameTime> calcHolidayWorkTimeList = new ArrayList<>();
//		for(HolidayWorkFrameTimeSheetWORK holidyWorkFrameTimeSheet : holidayWorkFrameTimeSheet) {
//			calcHolidayWorkTimeList.add(holidyWorkFrameTimeSheet.calcOverTimeWorkTime(autoCalcSet));
//		}
		return calcHolidayWorkTimeList;
	}
	
	/**
	 * 休出枠時間へ休出時間の集計結果を追加する
	 * @param hasAddListClass 休出時間帯の集計を行った後の休出枠時間クラス
	 */
	public void addToList(ControlHolidayWorkTime hasAddListClass) {
		this.holidayWorkFrameTime.addAll(hasAddListClass.getHolidayWorkFrame());
	}
	
	/**
	 * 休出時間に含まれている加給時間帯を計算する
	 * @return　加給時間クラス
	 */
	public List<BonusPayTime> calcBonusPay(BonusPayAutoCalcSet bonusPayAutoCalcSet,BonusPayAtr bonusPayAtr,CalAttrOfDailyPerformance calcAtrOfDaily){
		List<BonusPayTime> bonusPayList = new ArrayList<>();
//		for(HolidayWorkFrameTimeSheet frameTimeSheet: holidayWorkFrameTimeSheet) {
//			bonusPayList.addAll(frameTimeSheet.calcBonusPay(ActualWorkTimeSheetAtr.HolidayWork,bonusPayAutoCalcSet,calcAtrOfDaily));
//		}
		return bonusPayList;
	}
	
	/**
	 * 休出時間に含まれている特定日加給時間帯を計算する
	 * @return　加給時間クラス
	 */
	public List<BonusPayTime> calcSpecifiedBonusPay(BonusPayAutoCalcSet bonusPayAutoCalcSet,BonusPayAtr bonusPayAtr,CalAttrOfDailyPerformance calcAtrOfDaily){
		List<BonusPayTime> bonusPayList = new ArrayList<>();
//		for(HolidayWorkFrameTimeSheet frameTimeSheet: holidayWorkFrameTimeSheet) {
//			bonusPayList.addAll(frameTimeSheet.calcSpacifiedBonusPay(ActualWorkTimeSheetAtr.HolidayWork,bonusPayAutoCalcSet,calcAtrOfDaily));
//		}
		return bonusPayList;
	}
	/**
	 * 休出時間が含んでいる深夜時間の算出
	 * @return
	 */
	public HolidayMidnightWork calcMidNightTimeIncludeHolidayWorkTime(AutoCalcSetOfHolidayWorkTime autoCalcSet) {
		EachStatutoryHolidayWorkTime eachTime = new EachStatutoryHolidayWorkTime();
//		for(HolidayWorkFrameTimeSheet  frameTime : holidayWorkFrameTimeSheet) {
//			eachTime.addTime(frameTime.getStatutoryAtr(), frameTime.calcMidNight(autoCalcSet.getLateNightTime().getCalculationClassification()));
//		}
		List<HolidayWorkMidNightTime> holidayWorkList = new ArrayList<>();
		holidayWorkList.add(new HolidayWorkMidNightTime(TimeWithCalculation.sameTime(new AttendanceTime(eachTime.getStatutory())),StaturoryAtrOfHolidayWork.WithinPrescribedHolidayWork));
		holidayWorkList.add(new HolidayWorkMidNightTime(TimeWithCalculation.sameTime(new AttendanceTime(eachTime.getExcess())),StaturoryAtrOfHolidayWork.ExcessOfStatutoryHolidayWork));
		holidayWorkList.add(new HolidayWorkMidNightTime(TimeWithCalculation.sameTime(new AttendanceTime(eachTime.getPublicholiday())),StaturoryAtrOfHolidayWork.PublicHolidayWork));
		return new HolidayMidnightWork(holidayWorkList);
	}
	
	/**
	 * 全枠の休出時間の合計の算出
	 * @return　休出時間
	 */
	public int calcTotalFrameTime() {
		int totalTime = 0;
		for(HolidayWorkFrameTime holidayWorkFrameTime : holidayWorkFrameTime) {
			totalTime += holidayWorkFrameTime.getHolidayWorkTime().get().getTime().valueAsMinutes();
		}
		return totalTime;
	}
	
	/**
	 * 早出・普通の設定(優先順位)を見て並び替える
	 * @param overTimeWorkFrameTimeSheetList
	 * @param prioritySet
	 * @return
	 */
	public static List<HolidayWorkFrameTimeSheetWORK> sortedByPriority(List<HolidayWorkFrameTimeSheetWORK> overTimeWorkFrameTimeSheetList,StatutoryPrioritySet prioritySet){
		List<HolidayWorkFrameTimeSheetWORK> copyList = new ArrayList<>();
//		if(prioritySet.isPriorityNormal()) {
//			/*普通を優先*/
//			copyList.addAll(overTimeWorkFrameTimeSheetList.stream().filter(tc -> !tc.isGoEarly()).collect(Collectors.toList()));
//			copyList.addAll(overTimeWorkFrameTimeSheetList.stream().filter(tc -> tc.isGoEarly()).collect(Collectors.toList()));
//		}else {
//			/*早出を優先*/
//			copyList.addAll(overTimeWorkFrameTimeSheetList.stream().filter(tc -> tc.isGoEarly()).collect(Collectors.toList()));
//			copyList.addAll(overTimeWorkFrameTimeSheetList.stream().filter(tc -> !tc.isGoEarly()).collect(Collectors.toList()));
//		}
		return copyList;
	}
	
	
	/**
	 * 指定時間の振替処理から呼ばれた振替処理
	 * @param hurikaeAbleTime 振替可能時間
	 * @param prioritySet 振替可能時間
	 */
	public void hurikakesyori(AttendanceTime hurikaeAbleTime,StatutoryPrioritySet prioritySet) {
//		List<HolidayWorkFrameTimeSheetWORK> hurikae = sortedByPriority(holidayWorkFrameTimeSheet,prioritySet);
//		AttendanceTime ableTransTime = new AttendanceTime(0);
//		for(HolidayWorkFrameTimeSheetWORK holidayWorkFrameTimeSheet : hurikae) {
////			if(/*Not 振替大将*/) {
////				continue;
////			}
//			//残業時間 >= 振替可能時間
//			if(holidayWorkFrameTimeSheet.getFrameTime().getHolidayWorkTime().get().getCalcTime().greaterThanOrEqualTo(hurikaeAbleTime.valueAsMinutes())) {
//				ableTransTime = hurikaeAbleTime;
//			}
//			//残業時間 < 振替可能時間
//			else {
//				ableTransTime = holidayWorkFrameTimeSheet.getFrameTime().getHolidayWorkTime().get().getCalcTime(); 
//			}
//			holidayWorkFrameTime.stream().sorted((first,second) -> first.getHolidayFrameNo().compareTo(second.getHolidayFrameNo()));
//			//残業枠時間帯に対する加算
//			//holidayWorkFrameTimeSheet.getOverWorkFrameTime().getOverTimeWork().addMinutes(ableTransTime, ableTransTime);
//			//overTimeFrameTimeSheet.getOverWorkFrameTime().getTransferTime().addMinutes(ableTransTime, ableTransTime);
//			//日別実績の～～が持ってる枠に対する加算
//			//overTimeWorkFrameTime.get(overTimeFrameTimeSheet.getFrameNo().v()).getOverTimeWork().addMinutes(ableTransTime, ableTransTime);
//			//overTimeWorkFrameTime.get(overTimeFrameTimeSheet.getFrameNo().v()).getTransferTime().addMinutes(ableTransTime, ableTransTime);
//			
//			hurikaeAbleTime.minusMinutes(ableTransTime.valueAsMinutes());
//		}
	}
	
	
}
