package nts.uk.ctx.at.record.dom.daily;

import java.util.ArrayList;
import java.util.List;

import lombok.Value;
import nts.uk.ctx.at.record.dom.bonuspay.autocalc.BonusPayAutoCalcSet;
import nts.uk.ctx.at.record.dom.calculationattribute.CalAttrOfDailyPerformance;
import nts.uk.ctx.at.record.dom.daily.bonuspaytime.BonusPayTime;
import nts.uk.ctx.at.record.dom.dailyprocess.calc.ActualWorkTimeSheetAtr;
import nts.uk.ctx.at.record.dom.dailyprocess.calc.BonusPayAtr;
import nts.uk.ctx.at.record.dom.dailyprocess.calc.ControlOverFrameTime;
import nts.uk.ctx.at.record.dom.dailyprocess.calc.OverTimeFrameTime;
import nts.uk.ctx.at.record.dom.dailyprocess.calc.OverTimeFrameTimeSheet;
import nts.uk.ctx.at.shared.dom.common.time.AttendanceTime;
import nts.uk.ctx.at.shared.dom.ot.autocalsetting.AutoCalOvertimeSetting;
import nts.uk.ctx.at.shared.dom.workrule.outsideworktime.AutoCalcSet;
import nts.uk.shr.com.time.TimeWithDayAttr;

/**
 * 日別実績の残業時間
 * @author keisuke_hoshina
 *
 */
@Value
public class OverTimeWorkOfDaily {
	
	private List<OverTimeFrameTimeSheet> OverTimeFrameTimeSheet;
	
	private List<OverTimeFrameTime> OverTimeFrameTime;
	
	private OverTimeWorkOfDaily(List<OverTimeFrameTimeSheet> frameTimeList) {
		this.OverTimeFrameTimeSheet = frameTimeList;
		//TODO: init OverTimeFrameTimeSheet list
		this.OverTimeFrameTime = new ArrayList<>();
	}
	
	/**
	 * 勤務回数を見て開始時刻が正しいか判定する
	 * @param startTime
	 * @param workNo
	 * @param attendanceTime
	 * @return
	 */
	public static boolean startDicision(TimeWithDayAttr startTime, int workNo, TimeWithDayAttr attendanceTime) {
		if(workNo == 0) {
			return (startTime.v() < attendanceTime.v());
		}
		else{
			return (startTime.v() >= attendanceTime.v());
		}
	}
	

	
	/**
	 * 残業枠時間へ残業時間の集計結果を追加する
	 * @param hasAddListClass 残業時間帯の集計を行った後の残業枠時間クラス
	 */
	public void addToList(ControlOverFrameTime hasAddListClass) {
		this.OverTimeFrameTime.addAll(hasAddListClass.getOverTimeWorkFrameTime());
	}
	
	/**
	 * 残業時間が含んでいる加給時間の計算
	 * @return 加給時間リスト
	 */
	public List<BonusPayTime> calcBonusPay(BonusPayAutoCalcSet bonusPayAutoCalcSet,BonusPayAtr bonusPayAtr,CalAttrOfDailyPerformance calcAtrOfDaily){
		List<BonusPayTime> bonusPayList = new ArrayList<>();
		for(OverTimeFrameTimeSheet frameTimeSheet : OverTimeFrameTimeSheet) {
			//bonusPayList.addAll(frameTimeSheet.calcBonusPay(ActualWorkTimeSheetAtr.OverTimeWork,bonusPayAutoCalcSet, calcAtrOfDaily));
		}
		return bonusPayList;
	}
	
	/**
	 * 残業時間が含んでいる特定日加給時間の計算
	 * @return 加給時間リスト
	 */
	public List<BonusPayTime> calcSpecifiedBonusPay(BonusPayAutoCalcSet bonusPayAutoCalcSet,BonusPayAtr bonusPayAtr,CalAttrOfDailyPerformance calcAtrOfDaily){
		List<BonusPayTime> bonusPayList = new ArrayList<>();
		for(OverTimeFrameTimeSheet frameTimeSheet : OverTimeFrameTimeSheet) {
			//bonusPayList.addAll(frameTimeSheet.calcSpacifiedBonusPay(ActualWorkTimeSheetAtr.OverTimeWork,bonusPayAutoCalcSet, calcAtrOfDaily));
		}
		return bonusPayList;
	}
	/**
	 * 残業時間が含んでいる深夜時間の算出
	 * @return 日別実績の深夜時間帯クラス
	 */
	public ExcessOfStatutoryMidNightTime calcMidNightTimeIncludeOverTimeWork(AutoCalOvertimeSetting autoCalcSet) {
		int totalTime = 0;
		for(OverTimeFrameTimeSheet frameTime : OverTimeFrameTimeSheet) {
			/*↓分岐の条件が明確になったら記述*/
//			AutoCalcSet setting;
//			if(frameTime.getWithinStatutoryAtr().isStatutory()) {
//				setting = autoCalcSet.getLegalOvertimeHours();
//			}
//			else if(frameTime.isGoEarly()) {
//				setting = autoCalcSet.getEarlyOvertimeHours();
//			}
//			else {
//				setting = autoCalcSet.getNormalOvertimeHours();
//			}
//			totalTime += frameTime.calcMidNight(setting.getCalculationClassification());
		}
		return new ExcessOfStatutoryMidNightTime(TimeWithCalculation.sameTime(new AttendanceTime(totalTime)),new AttendanceTime(0));
	}
	
	/**
	 * 全枠の残業時間の合計の算出
	 * @return　残業時間
	 */
	public int calcTotalFrameTime() {
		int totalTime = 0;
		for(OverTimeFrameTime OverTimeFrameTime :OverTimeFrameTime) {
			totalTime += OverTimeFrameTime.getOverTimeWork().getTime().valueAsMinutes();
		}
		return totalTime;
	}
}
