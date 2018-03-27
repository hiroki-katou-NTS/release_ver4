package nts.uk.ctx.at.record.dom.dailyprocess.calc;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import lombok.Getter;
import nts.gul.util.value.Finally;
import nts.uk.ctx.at.record.dom.daily.ExcessOverTimeWorkMidNightTime;
import nts.uk.ctx.at.record.dom.daily.overtimework.OverTimeOfDaily;
import nts.uk.ctx.at.shared.dom.common.time.AttendanceTime;
import nts.uk.ctx.at.shared.dom.ot.autocalsetting.AutoCalOvertimeSetting;
import nts.uk.ctx.at.shared.dom.worktime.flowset.FlowOTTimezone;
import nts.uk.ctx.at.shared.dom.worktime.flowset.FlowWorkTimezoneSetting;
import nts.uk.ctx.at.shared.dom.worktype.WorkType;

/**
 * 残業時間帯
 * @author keisuke_hoshina
 *
 */

public class OverTimeWorkSheet {
	
	@Getter
	private static OverTimeOfDaily overWorkTimeOfDaily; 
	
	public  OverTimeWorkSheet(OverTimeOfDaily dailyOverWorkTime) {
		this.overWorkTimeOfDaily = dailyOverWorkTime;
	}
	
	/**
	 * 分割後の残業時間枠時間帯を受け取り
	 * @param insertList　補正した時間帯
	 * @param originList　補正する前の時間帯
	 * @return　
	 */
	public static List<OverTimeFrameTimeSheet> correctTimeSpan(List<OverTimeFrameTimeSheet> insertList,List<OverTimeFrameTimeSheet> originList,int nowNumber){
		originList.remove(nowNumber);
		originList.addAll(insertList);
		return originList;
	}
	
	
	/**
	 * 残業時間の計算(残業時間帯の合計の時間を取得し1日の範囲に返す)
	 * @return
	 */
	public static OverTimeOfDaily calcOverTimeWork(AutoCalOvertimeSetting autoCalcSet) {
		//ControlOverFrameTime returnClass = new ControlOverFrameTime(overWorkTimeOfDaily.collectOverTimeWorkTime(autoCalcSet));
		
		//overWorkTimeOfDaily.addToList(returnClass);
		
		return  overWorkTimeOfDaily;
	}
	
//	/**
//	 * 残業時間枠時間帯をループさせ時間を計算する
//	 * @param autoCalcSet 時間外時間の自動計算設定
//	 */
//	public List<OverTimeFrameTime> collectOverTimeWorkTime(AutoCalculationOfOverTimeWork autoCalcSet) {
//		List<OverTimeFrameTime> calcOverTimeWorkTimeList = new ArrayList<>();
//		for(OverTimeFrameTimeSheetWork overTimeWorkFrameTime : overTimeWorkFrameTimeSheet) {
//			calcOverTimeWorkTimeList.add(overTimeWorkFrameTime.calcOverTimeWorkTime(autoCalcSet));
//			//calcOverTimeWorkTimeList.add();
//		}
//		return calcOverTimeWorkTimeList;
//	}
	
	/**
	 * 深夜時間計算後の時間帯再作成
	 * @return
	 */
	public OverTimeWorkSheet reCreateToCalcExcessWork(OverTimeWorkSheet overTimeWorkSheet,AutoCalOvertimeSetting autoCalcSet) {
		ExcessOverTimeWorkMidNightTime midNightTime = overTimeWorkSheet.overWorkTimeOfDaily.calcMidNightTimeIncludeOverTimeWork(autoCalcSet);
		OverTimeOfDaily overTimeWorkOfDaily = new OverTimeOfDaily(overTimeWorkSheet.overWorkTimeOfDaily.getOverTimeWorkFrameTimeSheet(),
																		  overTimeWorkSheet.overWorkTimeOfDaily.getOverTimeWorkFrameTime(),
																		  Finally.of(midNightTime));
		return new OverTimeWorkSheet(overTimeWorkOfDaily);
	}
	
	//＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊

//	/**
//	 * 流動勤務（就外、平日）
//	 * @return
//	 */
//	public OverTimeWorkSheet createOverTimeWorkSheet(
//			TimeSpanForCalc calcRange,/*1日の計算範囲の計算範囲*/
//			WithinWorkTimeSheet withinWorkTimeSheet,/*流動勤務（平日・就内）で作成した就業時間内時間帯*/
//			DeductionTimeSheet deductionTimeSheet,
//			FluidWorkTimeSetting fluidWorkTimeSetting
//			) {
//		
//		//計算範囲の取得
//		TimeSpanForCalc timeSpan = new TimeSpanForCalc(
//				withinWorkTimeSheet.getFrameAt(0).getTimeSheet().getEnd(),
//				calcRange.getEnd());
//		
//		//控除時間帯を取得　（保科くんが作ってくれた処理を呼ぶ）
//		
//		//残業枠の開始時刻
//		TimeWithDayAttr startClock = calcRange.getStart();
//		//残業枠設定分ループ
//		for(FluidOverTimeWorkSheet fluidOverTimeWorkSheet: fluidWorkTimeSetting.overTimeWorkSheet) {
//			//残業枠n+1の経過時間を取得
////			AttendanceTime nextElapsedTime = getnextElapsedTime(
////					fluidOverTimeWorkSheet,
////					fluidWorkTimeSetting,
////					new AttendanceTime(calcRange.lengthAsMinutes()));
//			//控除時間から残業時間帯を作成
//			OverTimeWorkFrameTimeSheet overTimeWorkFrameTimeSheet;
//			
//			
//			//次の残業枠の開始時刻に終了時刻を入れる。
//			startClock = overTimeWorkFrameTimeSheet.getTimeSheet().getEnd();
//		}
//		//時間休暇溢れ分の割り当て
//			
//		
//	}
	
	
	/**
	 * 残業枠ｎ+1．経過時間を取得する
	 * @param fluidOverTimeWorkSheet
	 * @param fluidWorkTimeSetting
	 * @param timeOfCalcRange
	 * @return
	 */
	public AttendanceTime getnextElapsedTime(
			FlowOTTimezone fluidOverTimeWorkSheet,
			FlowWorkTimezoneSetting fluidWorkTimeSetting,
			AttendanceTime timeOfCalcRange) {
		int nextOverWorkTimeNo = fluidOverTimeWorkSheet.getWorktimeNo() + 1;
		AttendanceTime nextlapsedTime;
		Optional<FlowOTTimezone> nextFluidOverTimeWorkSheet = 
				fluidWorkTimeSetting.getMatchWorkNoOverTimeWorkSheet(nextOverWorkTimeNo);
		if(nextFluidOverTimeWorkSheet==null) {
			nextlapsedTime = timeOfCalcRange;
			return nextlapsedTime;
		}
		nextlapsedTime = nextFluidOverTimeWorkSheet.get().getFlowTimeSetting().getElapsedTime();
		return nextlapsedTime;
	}
	
	/**
	 * 代休の振替処理(残業用)
	 * @param workType　当日の勤務種類
	 */
	public void modifyHandTransfer(WorkType workType) {
		//平日の場合のみ振替処理実行
//		if(workType.isWeekDayAttendance()) {
//			//if(/*代休発生設定を取得(設計中のようす)*/) {
//			if(false) {
//				if(/*代休振替設定区分使用：一定時間を超えたら代休とする*/) {
//					periodOfTimeTransfer();
//				}
//				else if(/*指定した時間を代休とする*/) {
//					specifiedTimeTransfer();
//				}
//				else {
//					throw new Exception("unknown daikyuSet:");
//				}
//			}
//		}
	}
	
	/**
	 * 一定時間の振替処理
	 * @param 一定時間
	 */
	public void periodOfTimeTransfer(AttendanceTime periodTime) {
		/*振替可能時間の計算*/
		AttendanceTime transAbleTime = calcTransferTimeOfPeriodTime(periodTime);
		/*振り替える*/
		
	}
	
	/**
	 * 代休の振替可能時間の計算
	 * @param periodTime 一定時間
	 * @return 振替可能時間
	 */
	private AttendanceTime calcTransferTimeOfPeriodTime(AttendanceTime periodTime) {
		int totalFrameTime = this.getOverWorkTimeOfDaily().calcTotalFrameTime();
		if(periodTime.greaterThanOrEqualTo(new AttendanceTime(totalFrameTime))) {
			return new AttendanceTime(totalFrameTime).minusMinutes(periodTime.valueAsMinutes());
		}
		else {
			return new AttendanceTime(0);
		}
	}
	
//	/**
//	 * 指定時間の振替処理
//	 * @param prioritySet 優先設定
//	 */
//	public void specifiedTransfer(/*指定時間クラス*/,StatutoryPrioritySet prioritySet) {
//		AttendanceTime transAbleTime = calcSpecifiedTimeTransfer(/*指定時間クラス*/);
//		overWorkTimeOfDaily.hurikakesyori(transAbleTime, prioritySet);
//	}
//	
//	/**
//	 * 指定合計時間の計算
//	 * @param 指定時間クラス 
//	 */
//	private AttendanceTime calcSpecifiedTimeTransfer(/*指定時間クラス*/) {
//		int totalFrameTime = this.getOverWorkTimeOfDaily().calcTotalFrameTime();
//		if(totalFrameTime >= 指定時間クラス.１日の指定時間) {
//			return  指定時間クラス.１日の指定時間
//		}
//		else {
//			if(totalFrameTime >= 指定時間クラス.半日の指定時間) {
//				return 指定時間クラス.半日の指定時間
//			}
//			else {
//				return new AttendanceTime(0);
//			}
//		}
//	}
	
}
