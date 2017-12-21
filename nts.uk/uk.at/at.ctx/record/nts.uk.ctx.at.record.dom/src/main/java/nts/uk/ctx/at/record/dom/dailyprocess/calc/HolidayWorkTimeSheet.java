package nts.uk.ctx.at.record.dom.dailyprocess.calc;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import lombok.Getter;
import nts.uk.ctx.at.record.dom.MidNightTimeSheet;
import nts.uk.ctx.at.record.dom.daily.holidayworktime.HolidayMidnightWork;
import nts.uk.ctx.at.record.dom.daily.holidayworktime.HolidayWorkFrameTimeSheet;
import nts.uk.ctx.at.record.dom.daily.holidayworktime.HolidayWorkMidNightTime;
import nts.uk.ctx.at.record.dom.daily.holidayworktime.HolidayWorkTimeOfDaily;
import nts.uk.ctx.at.shared.dom.bonuspay.setting.BonusPayTimesheet;
import nts.uk.ctx.at.shared.dom.bonuspay.setting.SpecBonusPayTimesheet;
import nts.uk.ctx.at.shared.dom.common.time.AttendanceTime;
import nts.uk.ctx.at.shared.dom.common.time.TimeSpanForCalc;
import nts.uk.ctx.at.shared.dom.workrule.outsideworktime.AutoCalculationOfOverTimeWork;
import nts.uk.ctx.at.shared.dom.workrule.overtime.StatutoryPrioritySet;
import nts.uk.ctx.at.shared.dom.workrule.outsideworktime.AutoCalcSetOfHolidayWorkTime;
import nts.uk.ctx.at.shared.dom.worktime.fixedworkset.timespan.TimeSpanWithRounding;
import nts.uk.ctx.at.shared.dom.worktime.fluidworkset.FluOffdayWorkTime;
import nts.uk.ctx.at.shared.dom.worktime.fluidworkset.FluidTimeSetting;
import nts.uk.ctx.at.shared.dom.worktime.fluidworkset.fluidworktimesheet.FluWorkHolidayTimeSheet;
import nts.uk.ctx.at.shared.dom.worktype.WorkType;
import nts.uk.shr.com.time.TimeWithDayAttr;

/**
 * 休日出勤時間帯
 * @author keisuke_hoshina
 *
 */
public class HolidayWorkTimeSheet{
	@Getter
	private HolidayWorkTimeOfDaily workHolidayTime;
	
	/**
	 * Constructor
	 */
	public HolidayWorkTimeSheet(HolidayWorkTimeOfDaily holidayWorkOfDaily) {
		this.workHolidayTime = holidayWorkOfDaily;
	}
	
	
	/**
	 * 全ての休日出勤時間帯から休日出勤時間を算出する(休日出勤時間帯の合計の時間を取得し1日の範囲に返す)
	 */
	public HolidayWorkTimeOfDaily calcHolidayWorkTime(AutoCalcSetOfHolidayWorkTime autoCalcSet) {
		ControlHolidayWorkTime returnClass = new ControlHolidayWorkTime(workHolidayTime.collectHolidayWorkTime(autoCalcSet));
		workHolidayTime.addToList(returnClass);
		return workHolidayTime;
	}
	
//	/**
//	 * 代休の振替処理(残業用)
//	 * @param workType　当日の勤務種類
//	 */
//	public void modifyHandTransfer(WorkType workType) {
//		//平日の場合のみ振替処理実行
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
//	}
//	
//	/**
//	 * 
//	 * @param 一定時間
//	 */
//	public void periodOfTime(AttendanceTime periodTime) {
//		/*振替可能時間の計算*/
//		AttendanceTime transAbleTime = calcTransferTime(periodTime);
//		/*振り替える*/
//
//	}
//	
//	/**
//	 * 代休の振替可能時間の計算
//	 * @param periodTime 一定時間
//	 * @return 振替可能時間
//	 */
//	private AttendanceTime calcTransferTime(AttendanceTime periodTime) {
//		int totalFrameTime = this.getWorkHolidayTime().calcTotalFrameTime();
//		if(periodTime.greaterThanOrEqualTo(new AttendanceTime(totalFrameTime))) {
//			return new AttendanceTime(totalFrameTime).minusMinutes(periodTime.valueAsMinutes());
//		}
//		else {
//			return new AttendanceTime(0);
//		}
//	}
//	
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
	
	
	
	/**
	 * 深夜時間計算後の時間帯再作成
	 * @param HolidayWorkTimeSheet 休日出勤時間帯
	 * @param autoCalcSet 休出時間の自動計算設定
	 * @return
	 */
	//public HolidayWorkTimeSheet reCreateToCalcExcessWork(HolidayWorkTimeSheet holidayWorkSheet,AutoCalcSetOfHolidayWorkTime autoCalcSet) {
	public void reCreateToCalcExcessWork(HolidayWorkTimeSheet holidayWorkSheet,AutoCalcSetOfHolidayWorkTime autoCalcSet) {
//		HolidayMidnightWork holidayWorkMidNightTime = holidayWorkSheet.getWorkHolidayTime().calcMidNightTimeIncludeHolidayWorkTime(autoCalcSet);
//		↓コード値を変えつつ作り直すというおかしなことが起きてしまっている。値の変更(Entity)になるように全体的に修正をする
//		HolidayWorkTimeOfDaily  holidayWorkTimeOfDaily  = new HolidayWorkTimeOfDaily(holidayWorkMidNightTime);
//		
//		return new HolidayWorkTimeSheet(holidayWorkTimeOfDaily);
	}
		
	
	//＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊
	
//	/**
//	 * 流動勤務（休日出勤）
//	 */
//	public HolidayWorkTimeSheet createholidayWorkTimeSheet(
//			AttendanceLeavingWorkOfDaily attendanceLeavingWork,
//			List<FluWorkHolidayTimeSheet> workingTimes,
//			DeductionTimeSheet deductionTimeSheet,
//			WorkType workType,
//			HolidayWorkTimeOfDaily holidayWorkTimeOfDaily,
//			TimeSpanForCalc calcRange/*1日の計算範囲*/
//			) {
//		//出退勤時間帯を作成
//		TimeSpanForCalc attendanceLeavingWorkTimeSpan = new TimeSpanForCalc(
//				attendanceLeavingWork.getAttendanceLeavingWork(1).getAttendance().getEngrave().getTimesOfDay(),
//				attendanceLeavingWork.getLeavingWork().getEngrave().getTimesOfDay());
//		//出退勤時間帯と１日の計算範囲の重複部分を計算範囲とする
//		TimeSpanForCalc collectCalcRange = calcRange.getDuplicatedWith(attendanceLeavingWorkTimeSpan).orElse(null);
//		//休出枠時間帯を入れるListを作成
//		List<HolidayWorkFrameTimeSheet> holidayWorkFrameTimeSheetList = new ArrayList<>();
//		//前回のループの経過時間保持用(時間は0：00で作成)
//		AttendanceTime previousElapsedTime = new AttendanceTime(0);
//		//全ての休出枠設定分ループ
//		for(FluWorkHolidayTimeSheet fluWorkHolidayTimeSheet : workingTimes) {
//			//控除時間から休出時間帯を作成
//			HolidayWorkFrameTimeSheet holidayWorkFrameTimeSheet;
//			holidayWorkFrameTimeSheet.collectHolidayWorkFrameTimeSheet(fluWorkHolidayTimeSheet, workType, deductionTimeSheet, collectCalcRange, previousElapsedTime);
//			holidayWorkFrameTimeSheetList.add(holidayWorkFrameTimeSheet);
//			//次のループでの休出枠時間計算用に今回の経過時間を退避
//			previousElapsedTime = fluWorkHolidayTimeSheet.getFluidTimeSetting().getElapsedTime();
//		}
//		HolidayWorkTimeSheet holidayWorkTimeSheet = new HolidayWorkTimeSheet();
//		return  holidayWorkTimeSheet;
//	}
	
	
}
