package nts.uk.ctx.at.record.dom.daily.holidaywork;

import java.util.List;
import java.util.Optional;

import lombok.Getter;
import nts.gul.util.value.Finally;
import nts.uk.ctx.at.record.dom.MidNightTimeSheetForCalc;
import nts.uk.ctx.at.record.dom.daily.TimeWithCalculation;
import nts.uk.ctx.at.record.dom.daily.holidayworktime.HolidayWorkFrameTime;
import nts.uk.ctx.at.record.dom.dailyprocess.calc.BonusPayTimeSheetForCalc;
import nts.uk.ctx.at.record.dom.dailyprocess.calc.CalculationTimeSheet;
import nts.uk.ctx.at.record.dom.dailyprocess.calc.DeductionTimeSheet;
import nts.uk.ctx.at.record.dom.dailyprocess.calc.OverTimeWorkFrameTimeSheet;
import nts.uk.ctx.at.record.dom.dailyprocess.calc.SpecBonusPayTimeSheetForCalc;
import nts.uk.ctx.at.record.dom.dailyprocess.calc.TimeSheetOfDeductionItem;
import nts.uk.ctx.at.shared.dom.bonuspay.setting.BonusPayTimesheet;
import nts.uk.ctx.at.shared.dom.bonuspay.setting.SpecBonusPayTimesheet;
import nts.uk.ctx.at.shared.dom.common.time.AttendanceTime;
import nts.uk.ctx.at.shared.dom.common.time.TimeSpanForCalc;
import nts.uk.ctx.at.shared.dom.workrule.outsideworktime.holidaywork.HolidayWorkFrameNo;
import nts.uk.ctx.at.shared.dom.workrule.outsideworktime.holidaywork.StaturoryAtrOfHolidayWork;
import nts.uk.ctx.at.shared.dom.workrule.outsideworktime.AutoCalcSetOfHolidayWorkTime;
import nts.uk.ctx.at.shared.dom.worktime.common.TimeZoneRounding;
import nts.uk.ctx.at.shared.dom.worktime.flowset.FlowWorkHolidayTimeZone;
import nts.uk.ctx.at.shared.dom.worktype.WorkType;
import nts.uk.shr.com.time.TimeWithDayAttr;

/**
 * 休出枠時間帯
 * @author keisuke_hoshina
 *
 */
@Getter
public class HolidayWorkFrameTimeSheet extends CalculationTimeSheet{
	
	private HolidayWorkFrameTime frameTime;
	
	private boolean TreatAsTimeSpentAtWork;
	
	private HolidayWorkFrameNo HolidayWorkTimeSheetNo; 
	
	private StaturoryAtrOfHolidayWork statutoryAtr;
	
	/**
	 * constructor
	 * @param timeSheet 時間帯(丸め付き)
	 * @param calculationTimeSheet 計算範囲
	 * @param deductionTimeSheets 控除項目の時間帯
	 * @param bonusPayTimeSheet 加給時間帯
	 * @param midNighttimeSheet 
	 * @param frameTime
	 * @param treatAsTimeSpentAtWork
	 * @param holidayWorkTimeSheetNo
	 */
	public HolidayWorkFrameTimeSheet(
			TimeZoneRounding timeSheet,
			TimeSpanForCalc calculationTimeSheet,
			List<TimeSheetOfDeductionItem> recorddeductionTimeSheets,
			List<TimeSheetOfDeductionItem> deductionTimeSheets,
			List<BonusPayTimeSheetForCalc> bonusPayTimeSheet, 
			List<SpecBonusPayTimeSheetForCalc> specifiedbonusPayTimeSheet,
			Optional<MidNightTimeSheetForCalc> midNighttimeSheet, 
			HolidayWorkFrameTime frameTime,
			boolean treatAsTimeSpentAtWork,
			HolidayWorkFrameNo holidayWorkTimeSheetNo) {
		super(timeSheet, calculationTimeSheet, recorddeductionTimeSheets, deductionTimeSheets, bonusPayTimeSheet,specifiedbonusPayTimeSheet, midNighttimeSheet);
		this.frameTime = frameTime;
		this.TreatAsTimeSpentAtWork = treatAsTimeSpentAtWork;
		this.HolidayWorkTimeSheetNo = holidayWorkTimeSheetNo;
	}
	
	/**
	 * 残業時間帯時間枠に残業時間を埋める
	 * @param autoCalcSet 時間外の自動計算区分
	 * @return 残業時間枠時間帯クラス
	 */
	public HolidayWorkFrameTime calcOverTimeWorkTime(AutoCalcSetOfHolidayWorkTime autoCalcSet) {
		AttendanceTime holidayWorkTime;
		if(autoCalcSet.getLateNightTime().getCalculationClassification().isCalculateEmbossing()) {
			holidayWorkTime = new AttendanceTime(0);
		}
		else {
			holidayWorkTime = this.calcTotalTime();
		}
		return  new HolidayWorkFrameTime(this.frameTime.getHolidayFrameNo()
				,this.frameTime.getTransferTime()
				,Finally.of(TimeWithCalculation.sameTime(holidayWorkTime))
				,this.frameTime.getBeforeApplicationTime());
	}
	
	
	//＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊
	
	/**
	 * 控除時間を考慮して終了時刻を求める
	 * @return
	 */
	public HolidayWorkFrameTimeSheet collectHolidayWorkFrameTimeSheet(
			FlowWorkHolidayTimeZone fluWorkHolidayTimeSheet,
			WorkType workType,
			DeductionTimeSheet deductionTimeSheet,/*事前処理で作成した控除時間帯で良い？*/
			TimeSpanForCalc collectCalcRange,/*計算範囲*/
			AttendanceTime previousElapsedTime/*前回の経過時間*/
			) {
		//今回の処理の経過時間
		AttendanceTime elapsedTime = fluWorkHolidayTimeSheet.getFlowTimeSetting().getElapsedTime();
		//休出枠の時間を計算する (今回処理する経過時間-前回の経過時間)
		AttendanceTime holidayWorkFrameTime =  new AttendanceTime(elapsedTime.valueAsMinutes()-previousElapsedTime.valueAsMinutes());
		//休出枠時間から終了時刻を計算する
		TimeWithDayAttr endClock = collectCalcRange.getStart().backByMinutes(elapsedTime.valueAsMinutes());
		//休出枠時間帯　（一時的に作成）
		TimeSpanForCalc holidayWorkFrameTimeSheet = new TimeSpanForCalc(collectCalcRange.getStart(),endClock);
		//控除時間帯分ループ
		for(TimeSheetOfDeductionItem timeSheetOfDeductionItem : deductionTimeSheet.getForDeductionTimeZoneList()) {
			TimeSpanForCalc duplicateTime = holidayWorkFrameTimeSheet.getDuplicatedWith(timeSheetOfDeductionItem.getTimeSheet().timeSpan()).orElse(null);
			if(duplicateTime!=null) {//重複している場合の処理
				//控除項目の時間帯に法定内区分をセット
				//TODO: createBreakTimeSheetAsFixed
//				timeSheetOfDeductionItem = timeSheetOfDeductionItem.createBreakTimeSheetAsFixed(
//						timeSheetOfDeductionItem.getTimeSheet().getSpan(),
//						timeSheetOfDeductionItem.getGoOutReason(),
//						timeSheetOfDeductionItem.getBreakAtr(),
//						timeSheetOfDeductionItem.getDeductionAtr(),
//						WithinStatutoryAtr.WithinStatutory);
				//控除時間分、終了時刻を遅くする
				TimeSpanForCalc collectTimeSheet = this.timeSheet.timeSpan().shiftEndBack(duplicateTime.lengthAsMinutes());
				TimeZoneRounding newTimeSheet = this.timeSheet;
				// ここはベトナムへ連絡後コメントアウトを外すnewTimeSheet.newTimeSpan(collectTimeSheet);
			}	
		}
		
		//休出枠NOを判断
		
		//控除時間帯に丸め設定を付与

		//加給時間帯を作成
		
		//深夜時間帯を作成
		
		//TODO: return HolidayWorkFrameTimeSheet
//		HolidayWorkFrameTimeSheet holidayWorkFrameTimeSheet2 =  new HolidayWorkFrameTimeSheet();
		return null;
	}
	
	
	
}