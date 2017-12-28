package nts.uk.ctx.at.record.dom.dailyprocess.calc;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;
import nts.gul.util.value.Finally;
import nts.uk.ctx.at.record.dom.breakorgoout.BreakTimeOfDailyPerformance;
import nts.uk.ctx.at.record.dom.breakorgoout.BreakTimeSheet;
import nts.uk.ctx.at.record.dom.daily.breaktimegoout.BreakTimeOfDaily;
import nts.uk.ctx.at.shared.dom.common.time.TimeSpanForCalc;
import nts.uk.ctx.at.shared.dom.worktime.fixedworkset.set.FixRestCalcMethod;
import nts.uk.ctx.at.shared.dom.worktime.fixedworkset.timespan.TimeSpanWithRounding;
import nts.uk.ctx.at.shared.dom.worktime.fluidworkset.FluidPrefixBreakTimeSet;
import nts.uk.ctx.at.shared.dom.worktime.fluidworkset.fluidbreaktimeset.FlowRestCalcMethod;
import nts.uk.ctx.at.shared.dom.worktime.worktimeset.WorkTimeDivision;


/**
 * 休憩管理
 * @author keisuke_hoshina
 *
 */
@RequiredArgsConstructor
public class BreakTimeManagement {
	private final BreakTimeOfDaily breakTimeOfDaily;
	private final List<BreakTimeOfDailyPerformance> breakTimeSheetOfDaily;
	
	/**
	 * 休憩時間帯を作成する
	 * @return 休憩時間帯
	 */
	
	public List<TimeSheetOfDeductionItem> getBreakTimeSheet(WorkTimeDivision workTimeDivision,FixRestCalcMethod calcRest,FluidPrefixBreakTimeSet noStampSet
															,FlowRestCalcMethod calcMethod) {
		List<Optional<BreakTimeOfDailyPerformance>> timeSheets = new ArrayList<Optional<BreakTimeOfDailyPerformance>>();
		/**/
		if(!workTimeDivision.isfluidorFlex()) {
			timeSheets.add(getFixedBreakTimeSheet(calcRest)); 
		}
		else {
			timeSheets.addAll(getFluidBreakTimeSheet(calcMethod,true,noStampSet));/*流動　の　休　*/;
		}
		
		
		List<TimeSheetOfDeductionItem> dedTimeSheet = new ArrayList<TimeSheetOfDeductionItem>();
		//
		for(Optional<BreakTimeOfDailyPerformance> OptionalTimeSheet : timeSheets) {
			
			for(BreakTimeSheet timeSheet : OptionalTimeSheet.get().getBreakTimeSheets())
				dedTimeSheet.add(TimeSheetOfDeductionItem.createTimeSheetOfDeductionItemAsFixed(new TimeSpanWithRounding(timeSheet.getStartTime().getTimeWithDay(),timeSheet.getEndTime().getTimeWithDay(),Finally.empty())
																			, new TimeSpanForCalc(timeSheet.getStartTime().getTimeWithDay(),timeSheet.getEndTime().getTimeWithDay())
																			, Collections.emptyList()
																			, Collections.emptyList()
																			, Collections.emptyList()
																			, Optional.empty()
																			, Finally.empty()
																			, Finally.of(BreakClassification.BREAK)
																			, DeductionClassification.BREAK));
		}
		return dedTimeSheet;
	}
	
	/**
	 * 固定勤務 時に休 時間帯を取得する
	 * @param restCalc 固定給系の計算方法
	 * @return 休  時間帯

 */
	public Optional<BreakTimeOfDailyPerformance> getFixedBreakTimeSheet(FixRestCalcMethod calcRest) {
		if(calcRest.isReferToMaster()) {
			return breakTimeSheetOfDaily.stream()
										.filter(tc -> tc.getBreakType().isReferWorkTime())
										.findFirst();
		}
		else {
			return breakTimeSheetOfDaily.stream()
										.filter(tc -> tc.getBreakType().isReferSchedule())
										.findFirst();
		}
	}
	

	/**
	 * 流動勤務 休 設定取得
	 * @param calcMethod 流動休 の計算方
	 * @param isFixedBreakTime 流動固定休 を使用する区分
	 * @param noStampSet 休 未打刻時 休設定
	 * @return 休 時間帯
	 */
	public List<Optional<BreakTimeOfDailyPerformance>> getFluidBreakTimeSheet(FlowRestCalcMethod calcMethod,boolean isFixedBreakTime,FluidPrefixBreakTimeSet noStampSet) {
		List<Optional<BreakTimeOfDailyPerformance>> fluidBreakTimeSheet = new ArrayList<Optional<BreakTimeOfDailyPerformance>>();
		if(isFixedBreakTime) {
			switch(noStampSet.getCalcMethod()) {
				//予定を参照する
				case ReferToSchedule:
					fluidBreakTimeSheet.add(getReferenceTimeSheetFromSchedule());
				//マスタを参照
				case ReferToMaster:
					fluidBreakTimeSheet.add(getReferenceTimeSheetFromWorkTime());
				//参照せずに打刻をする
				case StampWithoutReference:
					fluidBreakTimeSheet.add(getReferenceTimeSheetFromBreakStamp());
					
					if(fluidBreakTimeSheet.isEmpty() && noStampSet.isReferToBreakClockFromMaster()) {
						fluidBreakTimeSheet.add(getReferenceTimeSheetFromWorkTime());
						
				}
			default:
				throw new RuntimeException("unKnown calcMethod" + calcMethod);
			}
		}
		return fluidBreakTimeSheet;
	}
	
	/**
	 * 流動固定休  の計算方法がマスタ参 の日別計算 休 時間帯クラスを取得する
	 * @return 日別実績の休 時間帯クラス
	 */
	public Optional<BreakTimeOfDailyPerformance> getReferenceTimeSheetFromWorkTime(){
		return breakTimeSheetOfDaily.stream().filter(tc -> tc.getBreakType().isReferWorkTime()).findFirst();
	}
	/**
	 * 流動固定休　の計算方法が打刻参　の日別計算　休　時間帯クラスを取得す
	 * @return 日別実績の休　時間帯クラス
	 */
	public Optional<BreakTimeOfDailyPerformance> getReferenceTimeSheetFromBreakStamp(){
		return breakTimeSheetOfDaily.stream().filter(tc -> tc.getBreakType().isReferWorkTime()).findFirst();
	}
	
	/**
	 * 流動固定休　の計算方法がスケジュール参　の日別計算　休　時間帯クラスを取得す　
	 * @return 日別実績の休　時間帯クラス
	 */
	public Optional<BreakTimeOfDailyPerformance> getReferenceTimeSheetFromSchedule(){
		return breakTimeSheetOfDaily.stream().filter(tc -> tc.getBreakType().isReferSchedule()).findFirst();
	}
	
	/**
	 * 指定した時間帯に含まれる休憩時間の合計値を返す
	 * @param baseTimeSheet
	 * @return
	 */
	public int sumBreakTimeIn(TimeSpanForCalc baseTimeSheet) {
		return breakTimeSheetOfDaily.stream()
				.collect(Collectors.summingInt(b -> b.sumBreakTimeIn(baseTimeSheet)));
	}
}
