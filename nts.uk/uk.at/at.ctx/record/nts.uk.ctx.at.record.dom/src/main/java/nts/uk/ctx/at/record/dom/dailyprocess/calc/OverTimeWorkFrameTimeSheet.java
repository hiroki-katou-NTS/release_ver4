package nts.uk.ctx.at.record.dom.dailyprocess.calc;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import lombok.Getter;
import nts.uk.ctx.at.record.dom.MidNightTimeSheetForCalc;
import nts.uk.ctx.at.record.dom.daily.TimeWithCalculation;
import nts.uk.ctx.at.shared.dom.bonuspay.setting.BonusPayTimesheet;
import nts.uk.ctx.at.shared.dom.bonuspay.setting.SpecBonusPayTimesheet;
import nts.uk.ctx.at.shared.dom.common.time.AttendanceTime;
import nts.uk.ctx.at.shared.dom.common.time.TimeSpanForCalc;
import nts.uk.ctx.at.shared.dom.ot.autocalsetting.AutoCalOvertimeSetting;
import nts.uk.ctx.at.shared.dom.workrule.outsideworktime.StatutoryAtr;
import nts.uk.ctx.at.shared.dom.workrule.outsideworktime.overtime.overtimeframe.OverTimeFrameNo;
import nts.uk.ctx.at.shared.dom.worktime.common.OverTimeOfTimeZoneSet;
import nts.uk.ctx.at.shared.dom.worktime.common.TimeZoneRounding;
import nts.uk.shr.com.time.TimeWithDayAttr;

/**
 * 残業枠時間帯
 * @author keisuke_hoshina
 *
 */
@Getter
public class OverTimeWorkFrameTimeSheet extends CalculationTimeSheet{
	/**残業時間帯NO**/
	private final int frameNo;
	private final OverTimeFrameTime overWorkFrameTime;
	
	public OverTimeWorkFrameTimeSheet(
			TimeZoneRounding timesheet,
			TimeSpanForCalc calculationTimeSheet,
			List<TimeSheetOfDeductionItem> recorddeductionTimeSheets,
			List<TimeSheetOfDeductionItem> deductionTimeSheets,
			List<BonusPayTimeSheetForCalc> bonusPayTimeSheet,
			List<SpecBonusPayTimeSheetForCalc> specifiedBonusPayTimeSheet,
			Optional<MidNightTimeSheetForCalc> midNighttimeSheet
			,int frameNo
			,OverTimeFrameTime overTimeWorkFrameTime
) {
		super(timesheet,calculationTimeSheet,recorddeductionTimeSheets,deductionTimeSheets,bonusPayTimeSheet,specifiedBonusPayTimeSheet,midNighttimeSheet);
		this.frameNo = frameNo;
		this.overWorkFrameTime = overTimeWorkFrameTime;
	}
	
	/**
	 * 振替処理において残業時間帯を分割するための再作成処理
	 * @param statutoryAtr 	法定内区分
	 * @param newTimeSpan　　  重複している時間帯
	 * @return
	 */
	public OverTimeWorkFrameTimeSheet reCreate(StatutoryAtr statutoryAtr, TimeSpanForCalc newTimeSpan) {
		return new OverTimeWorkFrameTimeSheet(this.getTimeSheet(),
				  							  newTimeSpan,
				  							  this.recordedTimeSheet,
											  this.deductionTimeSheet,
											  this.bonusPayTimeSheet,
											  this.specBonusPayTimesheet,
											  this.midNightTimeSheet,
											  this.getFrameNo(),
											  this.overWorkFrameTime);
	}
	
	/**
	 *残業枠時間帯の作成
	 * @param frameNo
	 * @param timeSpanWithRounding
	 * @return
	 */
	public static OverTimeWorkFrameTimeSheet createOverWorkFramTimeSheet(OverTimeOfTimeZoneSet overTimeHourSet,TimeSpanForCalc timeSpan) {
		
		//TODO: get DeductionTimeSheet
		DeductionTimeSheet deductionTimeSheet = null;/*実働時間の時間帯を跨いだ控除時間帯を分割する*/;
//		deductionTimeSheet.getForRecordTimeZoneList();/*法定内区分の置き換え*/
//		deductionTimeSheet.getForDeductionTimeZoneList();/*法定内区分の置き換え*/
		
		
		return new OverTimeWorkFrameTimeSheet(overTimeHourSet.getTimezone(),
											  timeSpan,
											  deductionTimeSheet.getForRecordTimeZoneList().stream().map(tc ->tc.createWithExcessAtr()).collect(Collectors.toList()),
											  deductionTimeSheet.getForDeductionTimeZoneList().stream().map(tc ->tc.createWithExcessAtr()).collect(Collectors.toList()),
											  Collections.emptyList(),
											  Collections.emptyList(),
											  Optional.empty(),
											  overTimeHourSet.getLegalOTframeNo().v(),
											  new OverTimeFrameTime(new OverTimeFrameNo(overTimeHourSet.getLegalOTframeNo().v()),
													  					TimeWithCalculation.sameTime(new AttendanceTime(0)),
													  					TimeWithCalculation.sameTime(new AttendanceTime(0)),
													  					new AttendanceTime(0),
													  					new AttendanceTime(0)));
	}
	

	/**
	 * 残業時間帯時間枠に残業時間を埋める
	 * @param autoCalcSet 時間外の自動計算区分
	 * @return 残業時間枠時間帯クラス
	 */
	public OverTimeFrameTime calcOverTimeWorkTime(AutoCalOvertimeSetting autoCalcSet) {
		AttendanceTime overTimeWorkTime;
//		if(getExcessTimeAutoCalcAtr(autoCalcSet).isCalculateEmbossing()) {
//			overTimeWorkTime = new AttendanceTime(0);
//		}
//		else {
			overTimeWorkTime = this.calcTotalTime();
//		}
		return new OverTimeFrameTime(this.overWorkFrameTime.getOverWorkFrameNo()
				,this.overWorkFrameTime.getTransferTime()
				,TimeWithCalculation.sameTime(overTimeWorkTime)
				,this.overWorkFrameTime.getBeforeApplicationTime(),
					new AttendanceTime(0));
	}
	
	
//	/**
//	 * 計算区分の判断処理
//	 * @param overTimeWorkFrameTime　残業時間枠時間帯クラス
//	 * @param autoCalcSet　残業時間の自動計算設定
//	 * @return　時間外の自動計算区分
//	 */
//	public AutoCalculationCategoryOutsideHours getExcessTimeAutoCalcAtr(AutoCalculationOfOverTimeWork autoCalcSet) {
//		switch(withinStatutoryAtr) {
//		case DeformationCriterion:
//		case Excess:
//			if(goEarly) {
//				return autoCalcSet.getEarlyOvertimeHours().getCalculationClassification();
//			}
//			else {
//				return autoCalcSet.getNormalOvertimeHours().getCalculationClassification();
//			}
//		case Statutory:
//			return autoCalcSet.getLegalOvertimeHours().getCalculationClassification();
//		default:
//			throw new RuntimeException("unkwon WithinStatutory Atr:" + withinStatutoryAtr);
//	
//		}
//	}
	
	/**
	 * 時間帯の分割
	 * @return
	 */
	public List<OverTimeWorkFrameTimeSheet> gege(TimeWithDayAttr baseTime){
		List<OverTimeWorkFrameTimeSheet> returnList = new ArrayList<>();
		if(this.calcrange.getEnd().equals(baseTime)) {
			returnList.add(this);
			return returnList;
		}
		else {
			returnList.add(new OverTimeWorkFrameTimeSheet(this.timeSheet
														 ,new TimeSpanForCalc(this.calcrange.getStart(), baseTime)
														 ,this.recordedTimeSheet
														 ,this.deductionTimeSheet
														 ,this.bonusPayTimeSheet
														 ,this.specBonusPayTimesheet
														 ,this.midNightTimeSheet
														 ,this.frameNo
														 ,this.getOverWorkFrameTime()));
			//TODO: add ??
//			returnList.add();
			return returnList;
		}
	}
	
	//＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊
	
//	//控除時間から残業時間帯を作成
//	public OverTimeWorkFrameTimeSheet createOverTimeWorkFrameTimeSheet(
//			FluidOverTimeWorkSheet fluidOverTimeWorkSheet,
//			DeductionTimeSheet deductionTimeSheet,
//			TimeWithDayAttr startClock,/*残業枠の開始時刻*/
//			AttendanceTime nextElapsedTime/*残業枠ｎ+1．経過時間*/
//			) {
//		//今回のループで処理する経過時間
//		AttendanceTime elapsedTime = fluidOverTimeWorkSheet.getFluidWorkTimeSetting().getElapsedTime();
//		//残業枠の時間を計算する  (残業枠ｎ+1．経過時間　-　残業枠n．経過時間)
//		AttendanceTime overTimeWorkFrameTime = new AttendanceTime(nextElapsedTime.valueAsMinutes()-elapsedTime.valueAsMinutes());
//		//残業枠時間から終了時刻を計算する
//		TimeWithDayAttr endClock = startClock.backByMinutes(overTimeWorkFrameTime.valueAsMinutes());
//		//残業枠時間帯を作成する
//		TimeSpanForCalc overTimeWorkFrameTimeSheet = new TimeSpanForCalc(startClock,endClock);
//		//控除時間帯分ループ
//		for(TimeSheetOfDeductionItem timeSheetOfDeductionItem : deductionTimeSheet.getForDeductionTimeZoneList()) {
//			TimeSpanForCalc duplicateTime = overTimeWorkFrameTimeSheet.getDuplicatedWith(timeSheetOfDeductionItem.getTimeSheet().getSpan()).orElse(null);
//			if(duplicateTime!=null) {
//				//控除項目の時間帯に法定内区分をセット
//				timeSheetOfDeductionItem = timeSheetOfDeductionItem.reateBreakTimeSheetAsFixed(
//						timeSheetOfDeductionItem.getTimeSheet().getSpan(),
//						timeSheetOfDeductionItem.getGoOutReason(),
//						timeSheetOfDeductionItem.getBreakAtr(),
//						timeSheetOfDeductionItem.getDeductionAtr(),
//						WithinStatutoryAtr.WithinStatutory);
//				//控除時間分、終了時刻を遅くする
//				TimeSpanForCalc collectTimeSheet = this.timeSheet.shiftEndBack(duplicateTime.lengthAsMinutes());
//				TimeSpanWithRounding newTimeSheet = this.timeSheet;
//				newTimeSheet.newTimeSpan(collectTimeSheet);
//			}
//		}
//		//控除時間帯に丸め設定を付与
//		
//		//加給時間帯を作成
//		
//		//深夜時間帯を作成
//		
//		
//		OverTimeWorkFrameTimeSheet overTimeWorkFrameTimeSheet = new OverTimeWorkFrameTimeSheet();
//		return overTimeWorkFrameTimeSheet;
//	}

}
