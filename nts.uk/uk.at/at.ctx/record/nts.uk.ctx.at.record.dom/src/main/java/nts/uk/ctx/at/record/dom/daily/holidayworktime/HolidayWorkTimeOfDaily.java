package nts.uk.ctx.at.record.dom.daily.holidayworktime;

import java.util.ArrayList;
import java.util.Collections;
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
import nts.uk.ctx.at.record.dom.dailyprocess.calc.ConditionAtr;
import nts.uk.ctx.at.record.dom.dailyprocess.calc.ControlHolidayWorkTime;
import nts.uk.ctx.at.record.dom.dailyprocess.calc.DeductionAtr;
import nts.uk.ctx.at.record.dom.dailyprocess.calc.HolidayWorkFrameTimeSheetForCalc;
import nts.uk.ctx.at.record.dom.dailyprocess.calc.HolidayWorkTimeSheet;
import nts.uk.ctx.at.record.dom.dailyprocess.calc.OverTimeFrameTimeSheet;
import nts.uk.ctx.at.shared.dom.common.time.AttendanceTime;
import nts.uk.ctx.at.shared.dom.ot.autocalsetting.AutoCalSetting;
import nts.uk.ctx.at.shared.dom.vacation.setting.compensatoryleave.CompensatoryOccurrenceSetting;
import nts.uk.ctx.at.shared.dom.workrule.outsideworktime.AutoCalcSetOfHolidayWorkTime;
import nts.uk.ctx.at.shared.dom.workrule.outsideworktime.holidaywork.StaturoryAtrOfHolidayWork;
import nts.uk.ctx.at.shared.dom.workrule.overtime.StatutoryPrioritySet;
import nts.uk.ctx.at.shared.dom.worktime.common.WorkTimezoneOtherSubHolTimeSet;
import nts.uk.ctx.at.shared.dom.worktype.WorkType;

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
	private AttendanceTime holidayTimeSpentAtWork;
	
	/**
	 * Constructor 
	 */
	public HolidayWorkTimeOfDaily(List<HolidayWorkFrameTimeSheet> holidayWorkFrameTimeSheet,List<HolidayWorkFrameTime> holidayWorkFrameTime,
								   Finally<HolidayMidnightWork> holidayMidNightWork, AttendanceTime holidayTimeSpentAtWork) {
		this.holidayWorkFrameTimeSheet = holidayWorkFrameTimeSheet;
		this.holidayWorkFrameTime = holidayWorkFrameTime;
		this.holidayMidNightWork = holidayMidNightWork;
		this.holidayTimeSpentAtWork = holidayTimeSpentAtWork;
	}
	

	/**
	 * メンバー変数の時間計算を指示するクラス
	 * @param holidayTimeSheet
	 * @return
	 */
	public static HolidayWorkTimeOfDaily calculationTime(HolidayWorkTimeSheet holidayWorkTimeSheet,
														 AutoCalSetting holidayAutoCalcSetting,
														 WorkType workType,
														 Optional<WorkTimezoneOtherSubHolTimeSet> eachWorkTimeSet,
														 Optional<CompensatoryOccurrenceSetting> eachCompanyTimeSet) {
		//時間帯
		val holidayWorkFrameTimeSheet = holidayWorkTimeSheet.changeHolidayWorkTimeFrameTimeSheet();
		//枠時間
		val holidayWorkFrameTime = holidayWorkTimeSheet.collectHolidayWorkTime(holidayAutoCalcSetting,
				 															   workType,
				 															   eachWorkTimeSet,
				 															   eachCompanyTimeSet);
		//深夜
		//holMidNightTime.add(new HolidayWorkMidNightTime(TimeWithCalculation.sameTime(new AttendanceTime(0)), StaturoryAtrOfHolidayWork.WithinPrescribedHolidayWork));
		val holidayMidnightWork = Finally.of(calcMidNightTimeIncludeHolidayWorkTime(holidayWorkTimeSheet));
		//使用時間
		val holidayTimeSpentTime = new AttendanceTime(0);
		return new HolidayWorkTimeOfDaily(holidayWorkFrameTimeSheet,
										  holidayWorkFrameTime,
										  holidayMidnightWork,
										  holidayTimeSpentTime);
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
		for(HolidayWorkFrameTimeSheet frameTimeSheet: holidayWorkFrameTimeSheet) {
			//bonusPayList.addAll(frameTimeSheet.calcBonusPay(ActualWorkTimeSheetAtr.HolidayWork,bonusPayAutoCalcSet,calcAtrOfDaily));
		}
		return bonusPayList;
	}
	
	/**
	 * 休出時間に含まれている特定日加給時間帯を計算する
	 * @return　加給時間クラス
	 */
	public List<BonusPayTime> calcSpecifiedBonusPay(BonusPayAutoCalcSet bonusPayAutoCalcSet,BonusPayAtr bonusPayAtr,CalAttrOfDailyPerformance calcAtrOfDaily){
		List<BonusPayTime> bonusPayList = new ArrayList<>();
		for(HolidayWorkFrameTimeSheet frameTimeSheet: holidayWorkFrameTimeSheet) {
			//bonusPayList.addAll(frameTimeSheet.calcSpacifiedBonusPay(ActualWorkTimeSheetAtr.HolidayWork,bonusPayAutoCalcSet,calcAtrOfDaily));
		}
		return bonusPayList;
	}
	/**
	 * 休出時間が含んでいる深夜時間の算出
	 * @return
	 */
	public static HolidayMidnightWork calcMidNightTimeIncludeHolidayWorkTime(HolidayWorkTimeSheet holidayWorkTimeSheet) {
		EachStatutoryHolidayWorkTime eachTime = new EachStatutoryHolidayWorkTime();
		for(HolidayWorkFrameTimeSheetForCalc  frameTime : holidayWorkTimeSheet.getWorkHolidayTime()) {
			if(frameTime.getMidNightTimeSheet().isPresent()) {
				int dedTime = frameTime.getDedTimeSheetByAtr(DeductionAtr.Appropriate, ConditionAtr.BREAK).stream()
																							.map(tc -> tc.getCalcrange().lengthAsMinutes())
																							.collect(Collectors.summingInt(tc -> tc));
				eachTime.addTime(frameTime.getStatutoryAtr().get(), frameTime.getMidNightTimeSheet().get().calcTotalTime().minusMinutes(dedTime));
			}
		}
		List<HolidayWorkMidNightTime> holidayWorkList = new ArrayList<>();
		holidayWorkList.add(new HolidayWorkMidNightTime(TimeWithCalculation.sameTime(eachTime.getStatutory()),StaturoryAtrOfHolidayWork.WithinPrescribedHolidayWork));
		holidayWorkList.add(new HolidayWorkMidNightTime(TimeWithCalculation.sameTime(eachTime.getExcess()),StaturoryAtrOfHolidayWork.ExcessOfStatutoryHolidayWork));
		holidayWorkList.add(new HolidayWorkMidNightTime(TimeWithCalculation.sameTime(eachTime.getPublicholiday()),StaturoryAtrOfHolidayWork.PublicHolidayWork));
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
	 * 全枠の振替休出時間の合計の算出
	 * @return　休出時間
	 */
	public int calcTransTotalFrameTime() {
		int transTotalTime = 0;
		for(HolidayWorkFrameTime holidayWorkFrameTime : holidayWorkFrameTime) {
			transTotalTime += holidayWorkFrameTime.getTransferTime().get().getTime().valueAsMinutes();
		}
		return transTotalTime ;
	}
	
	/**
	 * 早出・普通の設定(優先順位)を見て並び替える
	 * @param overTimeWorkFrameTimeSheetList
	 * @param prioritySet
	 * @return
	 */
	public static List<HolidayWorkFrameTimeSheet> sortedByPriority(List<HolidayWorkFrameTimeSheet> overTimeWorkFrameTimeSheetList,StatutoryPrioritySet prioritySet){
		List<HolidayWorkFrameTimeSheet> copyList = new ArrayList<>();
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
		List<HolidayWorkFrameTimeSheet> hurikae = sortedByPriority(holidayWorkFrameTimeSheet,prioritySet);
		AttendanceTime ableTransTime = new AttendanceTime(0);
		for(HolidayWorkFrameTimeSheet holidayWorkFrameTimeSheet : hurikae) {
//			if(/*Not 振替大将*/) {
//				continue;
//			}
			//残業時間 >= 振替可能時間
//			if(holidayWorkFrameTimeSheet.getFrameTime().getHolidayWorkTime().get().getCalcTime().greaterThanOrEqualTo(hurikaeAbleTime.valueAsMinutes())) {
//				ableTransTime = hurikaeAbleTime;
//			}
//			//残業時間 < 振替可能時間
//			else {
//				ableTransTime = holidayWorkFrameTimeSheet.getFrameTime().getHolidayWorkTime().get().getCalcTime(); 
//			}
			holidayWorkFrameTime.stream().sorted((first,second) -> first.getHolidayFrameNo().compareTo(second.getHolidayFrameNo()));
			//残業枠時間帯に対する加算
			//holidayWorkFrameTimeSheet.getOverWorkFrameTime().getOverTimeWork().addMinutes(ableTransTime, ableTransTime);
			//overTimeFrameTimeSheet.getOverWorkFrameTime().getTransferTime().addMinutes(ableTransTime, ableTransTime);
			//日別実績の～～が持ってる枠に対する加算
			//overTimeWorkFrameTime.get(overTimeFrameTimeSheet.getFrameNo().v()).getOverTimeWork().addMinutes(ableTransTime, ableTransTime);
			//overTimeWorkFrameTime.get(overTimeFrameTimeSheet.getFrameNo().v()).getTransferTime().addMinutes(ableTransTime, ableTransTime);
			
			hurikaeAbleTime.minusMinutes(ableTransTime.valueAsMinutes());
		}
	}
}
