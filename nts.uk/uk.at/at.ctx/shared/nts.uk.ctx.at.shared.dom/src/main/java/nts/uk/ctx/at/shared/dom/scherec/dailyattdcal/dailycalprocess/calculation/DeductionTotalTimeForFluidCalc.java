package nts.uk.ctx.at.shared.dom.scherec.dailyattdcal.dailycalprocess.calculation;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.val;
import nts.gul.util.value.Finally;
import nts.uk.ctx.at.shared.dom.common.time.AttendanceTime;
import nts.uk.ctx.at.shared.dom.common.time.TimeSpanDuplication;
import nts.uk.ctx.at.shared.dom.common.time.TimeSpanForCalc;
import nts.uk.ctx.at.shared.dom.common.timerounding.TimeRoundingSetting;
import nts.uk.ctx.at.shared.dom.scherec.dailyattdcal.autocalsetting.FluidFixedAtr;
import nts.uk.ctx.at.shared.dom.scherec.dailyattdcal.dailyattendance.attendancetime.TimeLeavingOfDailyAttd;
import nts.uk.ctx.at.shared.dom.scherec.dailyattdcal.dailyattendance.attendancetime.TimeLeavingWork;
import nts.uk.ctx.at.shared.dom.scherec.dailyattdcal.dailycalprocess.calculation.timezone.deductiontime.BreakClassification;
import nts.uk.ctx.at.shared.dom.scherec.dailyattdcal.dailycalprocess.calculation.timezone.deductiontime.DeductionAtr;
import nts.uk.ctx.at.shared.dom.scherec.dailyattdcal.dailycalprocess.calculation.timezone.deductiontime.DeductionClassification;
import nts.uk.ctx.at.shared.dom.scherec.dailyattdcal.dailycalprocess.calculation.timezone.deductiontime.TimeSheetOfDeductionItem;
import nts.uk.ctx.at.shared.dom.scherec.dailyattdcal.dailycalprocess.calculation.timezone.deductiontime.WorkingBreakTimeAtr;
import nts.uk.ctx.at.shared.dom.worktime.IntegrationOfWorkTime;
import nts.uk.ctx.at.shared.dom.worktime.flowset.FlowRestClockCalcMethod;
import nts.uk.ctx.at.shared.dom.worktime.flowset.FlowRestSetting;
import nts.uk.ctx.at.shared.dom.worktime.worktimeset.WorkTimeMethodSet;
import nts.uk.ctx.at.shared.dom.worktype.WorkType;
import nts.uk.shr.com.time.TimeWithDayAttr;

/**
 * 流動休憩作成情報
 *
 */
@Getter
@AllArgsConstructor
public class DeductionTotalTimeForFluidCalc {
	
	/** 休憩開始時刻 */
	private TimeWithDayAttr breakStartTime;
	
	/** 控除合計時間 */
	private DeductionTotalTimeLocal deductionTotal;
	
	public DeductionTotalTimeForFluidCalc(TimeWithDayAttr breakStartTime) {
		this.breakStartTime = breakStartTime;
		this.deductionTotal = new DeductionTotalTimeLocal(new AttendanceTime(0), new AttendanceTime(0));
	}
	
	/** 控除合計時間　（local） */
	@Getter
	@AllArgsConstructor
	public static class DeductionTotalTimeLocal {

		/** 合計時間 */
		private AttendanceTime totalTime;
		/** 外出相殺残時間 */
		private AttendanceTime goOutTimeOffsetRemainingTime;
		
		/** ○外出控除時間を控除合計時間に加算 */
		public void add(AttendanceTime goOutTime) {
			totalTime = totalTime.addMinutes(goOutTime.valueAsMinutes());
			goOutTimeOffsetRemainingTime = goOutTimeOffsetRemainingTime.addMinutes(goOutTime.valueAsMinutes());
		}
		
		/** ○外出相殺残時間から相殺時間を減算 */
		public void subGoOutOffset(int time) {
			goOutTimeOffsetRemainingTime = goOutTimeOffsetRemainingTime.minusMinutes(time);
		}
	}
	
	
	/**
	 * 流動休憩時間帯の作成
	 */
	public List<TimeSheetOfDeductionItem> createDeductionFluidRestTime(DeductionAtr deductionAtr,
			TimeLeavingOfDailyAttd timeLeave, TimeWithDayAttr goOutGetStartTime, FlowRestSetting flowBreakSet, 
			DeductionTotalTimeLocal deductionTotal, List<TimeSheetOfDeductionItem> deductTimeSheet,
			IntegrationOfWorkTime workTime, WorkType workType, TimeWithDayAttr dayStart, boolean correctWithEndTime) {
		
		/** △流動休憩開始までの間にある外出分、休憩をズラす */
		val breakTime = reassignBreakTime(dayStart, goOutGetStartTime, flowBreakSet, deductionTotal, deductTimeSheet);
		
		/** 丸め←後で入れる */
		val rounding = new TimeRoundingSetting(1, 1);
		/** 休憩時間帯クラスを作成 */
		val breakTimeSheet = TimeSheetOfDeductionItem.createTimeSheetOfDeductionItemAsFixed(
															new TimeSpanForDailyCalc(breakTime.getBreakStartTime(), 
																	breakTime.getBreakStartTime().forwardByMinutes(flowBreakSet.getFlowRestTime().valueAsMinutes())),
															rounding, new ArrayList<>(), new ArrayList<>(), 
															WorkingBreakTimeAtr.NOTWORKING, Finally.empty(), 
															Finally.of(BreakClassification.BREAK), 
															Optional.empty(), DeductionClassification.BREAK, Optional.empty());
		
		/** 休憩と外出の重複処理 */
		duplicationProcessingOfFluidBreakTime(breakTimeSheet, deductTimeSheet, workTime, workType);
		
		/** ○流動休憩設定を取得する */
		if(workTime.getFlowWorkRestSettingDetail().get().getFlowRestSetting().isUseStamp()) {
			
			/** △外出と休憩の合計時間相殺処理 */
			subtractionGoOutTimeOffsetRemainingTime(breakTimeSheet, breakTime, 
												workTime.getFlowWorkRestSettingDetail().get().getFlowRestSetting().getUseStampCalcMethod(),
												goOutGetStartTime, deductTimeSheet);
		}
		List<TimeSheetOfDeductionItem> newTimeSpan = new ArrayList<>();
		if (correctWithEndTime) {
			/** ○退勤が含まれている場合の補正 */
			newTimeSpan.addAll(getLastLeave(timeLeave)
					.map(tl -> breakTimeSheet.getIncludeAttendanceOrLeaveDuplicateTimeSheet(
																				tl, workTime.getCommonRestSetting().getCalculateMethod(),
																				deductionAtr, breakTimeSheet.getTimeSheet()))
					.orElse(new ArrayList<>()));	
		} else {
			newTimeSpan.add(breakTimeSheet);
		}
		
//		/** ○休憩時間帯を返す */
//		val corrected =	newTimeSpan.map(c -> TimeSheetOfDeductionItem.createTimeSheetOfDeductionItemAsFixed(c, rounding, 
//												breakTimeSheet.getRecordedTimeSheet(), breakTimeSheet.getDeductionTimeSheet(),
//												breakTimeSheet.getWorkingBreakAtr(), breakTimeSheet.getGoOutReason(), breakTimeSheet.getBreakAtr(), 
//												breakTimeSheet.getShortTimeSheetAtr(), breakTimeSheet.getDeductionAtr(), breakTimeSheet.getChildCareAtr()))
//									.orElse(breakTimeSheet);

		/** ○休憩時間帯クラスの開始時刻を退避 */
		this.breakStartTime = breakTimeSheet.getTimeSheet().getStart();
		this.deductionTotal = breakTime.deductionTotal;
				
		return newTimeSpan;
	}
	
	private Optional<TimeLeavingWork> getLastLeave(TimeLeavingOfDailyAttd timeLeave) {
		if (timeLeave.getTimeLeavingWorks().size() == 2) {
			return timeLeave.getAttendanceLeavingWork(2);
		}
		
		return timeLeave.getAttendanceLeavingWork(1);
	}
	
	/**
	 * 流動休憩開始までの間にある外出分、休憩をズラす (この処理は合計時間をズラすだけ)
	 */
	private DeductionTotalTimeForFluidCalc reassignBreakTime(
			TimeWithDayAttr dayStart,
			TimeWithDayAttr goOutGetStartTime, FlowRestSetting flowBreakSet, 
			DeductionTotalTimeLocal deductionTotal,
			List<TimeSheetOfDeductionItem> deductTimeSheet) {
		
		/** ○流動休憩開始時刻を計算 */
		val breakPassageTime = flowBreakSet.getFlowPassageTime().addMinutes(deductionTotal.totalTime.valueAsMinutes());
		TimeWithDayAttr flowStartTime = dayStart.forwardByMinutes(breakPassageTime.valueAsMinutes());
		
		/** ○ズラした事によって間に外出が増えた場合は追加 */
		flowStartTime = reassignFlowBreakTime(new TimeSpanForCalc(goOutGetStartTime, flowStartTime), 
												flowStartTime, deductTimeSheet, deductionTotal);
		
		/** 休憩時間分を合計時間に加算する */
		deductionTotal.totalTime = deductionTotal.totalTime.addMinutes(flowBreakSet.getFlowRestTime().valueAsMinutes());
		
		/** ○流動休憩作成情報を返す */
		return new DeductionTotalTimeForFluidCalc(flowStartTime, deductionTotal);
	}

	/** △流動休憩開始時刻までの間にある外出を取得 */
	private List<TimeSheetOfDeductionItem> getGoOutBeforeStartTime(List<TimeSheetOfDeductionItem> goOutTimeSheet, 
			TimeSpanForCalc goOutGetRange) {
		
		return goOutTimeSheet.stream().filter(c -> c.getDeductionAtr() == DeductionClassification.GO_OUT
				&& c.getTimeSheet().getStart().greaterThan(goOutGetRange.getStart())
				&& c.getTimeSheet().getEnd().lessThan(goOutGetRange.getEnd()))
				.collect(Collectors.toList());
	}

	/** ○ズラした事によって間に外出が増えた場合は追加 */
	private TimeWithDayAttr reassignFlowBreakTime(TimeSpanForCalc goOutGetRange, TimeWithDayAttr flowStartTime,
			List<TimeSheetOfDeductionItem> deductTimeSheet, DeductionTotalTimeLocal deductionTotal) {

		/** △流動休憩開始時刻までの間にある外出を取得 */
		val goOut = getGoOutBeforeStartTime(deductTimeSheet, goOutGetRange);
		
		for(val c : goOut) {
			
			/** △外出時間を計算 */
			val goOutTime = c.calcTotalTime();
			
			/** ○控除時間分、流動休憩時間の開始をズラす */
			flowStartTime = flowStartTime.forwardByMinutes(goOutTime.valueAsMinutes());
			
			/** ○外出控除時間を控除合計時間に加算 */
			deductionTotal.add(goOutTime);
			
			if (goOutTime.greaterThan(0)) {
				
				return reassignFlowBreakTime(new TimeSpanForCalc(c.getTimeSheet().getEnd(), flowStartTime), 
											flowStartTime, deductTimeSheet, deductionTotal);
			}
		}
		return flowStartTime;
	}
	
	/**
	 * 流動休憩の重複処理
	 * @param ｔimeSheetOfDeductionItem 「休憩時間帯クラスを作成」で作成した休憩
	 * @param deductionTimeList 外出時間帯のみの控除時間帯
	 * @return
	 */
	private void duplicationProcessingOfFluidBreakTime(TimeSheetOfDeductionItem targetDeductSheet,
														List<TimeSheetOfDeductionItem> deductionTimeList,
														IntegrationOfWorkTime workTime, WorkType workType) {
		
		/** ○休憩時間帯と重複している控除時間帯を取得 */
		List<TimeSheetOfDeductionItem> duplicateList = deductionTimeList.stream().filter(ts -> ts.getDeductionAtr() == DeductionClassification.GO_OUT && 
																								targetDeductSheet.getTimeSheet().checkDuplication(ts.getTimeSheet()) != TimeSpanDuplication.NOT_DUPLICATE )
																					.collect(Collectors.toList());
		/** 〇控除時間帯の重複処理 */
		for(val dts : duplicateList) { 
			
			/** 〇控除時間帯の重複処理 */
			val result = dts.deplicateBreakGoOut(targetDeductSheet, WorkTimeMethodSet.FLOW_WORK, workTime.getRestClockManageAtr(), 
												workTime.isFixBreak(workType), FluidFixedAtr.FluidWork, 
												workTime.getWorkTimeSetting().getWorkTimeDivision().getWorkTimeDailyAtr());
			
			if (!result.isEmpty()) {
				/** 重複時間帯を置き換え*/
				val correctedBreak = result.stream().filter(c -> c.getDeductionAtr() == DeductionClassification.BREAK)
													.findFirst().get();
				targetDeductSheet.replaceTimeSheet(correctedBreak.getTimeSheet());
				
				/** ○ループ最初からやり直し */
				duplicationProcessingOfFluidBreakTime(targetDeductSheet, deductionTimeList, workTime, workType);
				return;
			}
		}
	}
	
	/**
	 *  外出と休憩の相殺処理　　（外出相殺残時間）
	 *  外出相殺残時間がメンバーになっているため、ここはvoidでOK
	 * @param ｔimeSheetOfDeductionItem
	 * @param restTime 親ロジックで作成した休憩時間
	 */
	private void subtractionGoOutTimeOffsetRemainingTime(
			TimeSheetOfDeductionItem timeSheetOfDeductionItem,
			DeductionTotalTimeForFluidCalc flowBreakDeductInfo,
			FlowRestClockCalcMethod clockCalcMethod,
			TimeWithDayAttr goOutGetStartTime, 
			List<TimeSheetOfDeductionItem> deductTimeSheet) {
		
		/** ○休憩時間から相殺できる時間を計算 */
		val breakTime = timeSheetOfDeductionItem.calcTotalTime();
		val offsetTime = flowBreakDeductInfo.deductionTotal.goOutTimeOffsetRemainingTime.lessThan(breakTime) 
							? flowBreakDeductInfo.deductionTotal.goOutTimeOffsetRemainingTime : breakTime;
		
		if(offsetTime.greaterThan(0)) {
			/** △休憩時間帯を相殺時間分縮める */
			Optional<TimeSpanForDailyCalc> afterShrinkingTimeSheet = timeSheetOfDeductionItem.contractTimeSheet(new TimeWithDayAttr(offsetTime.valueAsMinutes()));
			
			/** ○外出相殺残時間から相殺時間を減算 */
			flowBreakDeductInfo.deductionTotal.subGoOutOffset(afterShrinkingTimeSheet.map(c -> c.lengthAsMinutes()).orElse(0));
		}
		
		/** ○外出と休憩の相殺処理設定を取得 */
		if(clockCalcMethod.isRestTimeToCalculate()) {
			val toChangeToBreak = deductTimeSheet.stream().filter(c -> c.getTimeSheet().getEnd().greaterThanOrEqualTo(goOutGetStartTime)
																	&& c.getTimeSheet().getStart().lessThanOrEqualTo(flowBreakDeductInfo.breakStartTime))
												.collect(Collectors.toList());
			
			changeGoOutToBreak(toChangeToBreak, offsetTime, deductTimeSheet);
		}
	}
	
	/**
	 * △相殺した分の外出を休憩として扱う
	 * @param goOutTimeSheetList 控除用時間帯リストから取得できるすべての外出時間帯
	 * @param offSetTime 相殺時間
	 */
	private void changeGoOutToBreak(List<TimeSheetOfDeductionItem> goOutTimeSheetList, AttendanceTime offSetTime,
			List<TimeSheetOfDeductionItem> deductTimeSheet) {
		
		for (val dts : goOutTimeSheetList) {
			
			/** △外出時間を計算 */
			val goOutTime = dts.calcTotalTime();
			
			/** ○外出から休憩に振り替える時間を計算 */
			val toBreakTime = offSetTime.greaterThan(goOutTime) ? goOutTime : goOutTime.minusHours(offSetTime.valueAsMinutes());
			
			/** △控除時間帯を考慮して終了時刻を判断 */
			val endTime = getEndTime(dts, new TimeWithDayAttr(toBreakTime.valueAsMinutes()));
			
			/** ○終了時刻に従って、外出時間帯を分割 */
			if (endTime.isPresent()) {
				
				val breakTime = TimeSheetOfDeductionItem.createTimeSheetOfDeductionItemAsFixed(new TimeSpanForDailyCalc(dts.getTimeSheet().getStart(), endTime.get()),
											dts.getRounding(), new ArrayList<>(), new ArrayList<>(), WorkingBreakTimeAtr.NOTWORKING, Finally.empty(), 
											Finally.of(BreakClassification.BREAK_STAMP), Optional.empty(), DeductionClassification.BREAK, Optional.empty());
				deductTimeSheet.add(breakTime);
				dts.replaceTimeSheet(new TimeSpanForDailyCalc(endTime.get(), dts.getTimeSheet().getEnd()));
			} else {
				dts.changeToBreak();
			}
			
			/** ○相殺残時間から振替時間を減算 */
			offSetTime = offSetTime.minusMinutes(toBreakTime.valueAsMinutes());
		}
	}
	
	/** △控除時間帯を考慮して終了時刻を判断 */
	private Optional<TimeWithDayAttr> getEndTime(TimeSheetOfDeductionItem timeSheet, TimeWithDayAttr time) {
		
		/** △開始から指定時間を終了とする時間帯を作成 */
		/** ○パラメータ「時間帯」をコピー */
		/** △コピーした時間帯が指定時間になるように縮小 */
		val reductedRange  = timeSheet.contractTimeSheet(time);
		
		/** ○作成した時間帯の終了時刻を返す */
		return reductedRange.map(c -> c.getTimeSpan().getEnd());
	}
	
}



