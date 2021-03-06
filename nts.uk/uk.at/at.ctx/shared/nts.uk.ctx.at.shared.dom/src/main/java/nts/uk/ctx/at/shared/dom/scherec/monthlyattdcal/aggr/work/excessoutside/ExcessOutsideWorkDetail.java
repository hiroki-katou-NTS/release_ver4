package nts.uk.ctx.at.shared.dom.scherec.monthlyattdcal.aggr.work.excessoutside;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.Getter;
import lombok.val;
import nts.arc.time.GeneralDate;
import nts.arc.time.calendar.period.DatePeriod;
import nts.uk.ctx.at.shared.dom.common.time.AttendanceTimeMonth;
import nts.uk.ctx.at.shared.dom.common.time.AttendanceTimeMonthWithMinus;
import nts.uk.ctx.at.shared.dom.scherec.monthlyattdcal.aggr.calcmethod.calcmethod.flex.FlexMonthWorkTimeAggrSet;
import nts.uk.ctx.at.shared.dom.scherec.monthlyattdcal.aggr.roundingset.RoundingSetOfMonthly;
import nts.uk.ctx.at.shared.dom.scherec.monthlyattdcal.aggr.work.timeseries.FlexTimeOfTimeSeries;
import nts.uk.ctx.at.shared.dom.scherec.monthlyattdcal.aggr.work.timeseries.MonthlyPremiumTimeOfTimeSeries;
import nts.uk.ctx.at.shared.dom.scherec.monthlyattdcal.aggr.work.timeseries.WeeklyPremiumTimeOfTimeSeries;
import nts.uk.ctx.at.shared.dom.scherec.monthlyattdcal.aggr.work.timeseries.WorkTimeOfTimeSeries;
import nts.uk.ctx.at.shared.dom.scherec.monthlyattdcal.monthly.AttendanceItemOfMonthly;
import nts.uk.ctx.at.shared.dom.scherec.monthlyattdcal.monthly.TimeMonthWithCalculation;
import nts.uk.ctx.at.shared.dom.scherec.monthlyattdcal.monthly.calc.actualworkingtime.RegularAndIrregularTimeOfMonthly;
import nts.uk.ctx.at.shared.dom.scherec.monthlyattdcal.monthly.calc.flex.FlexTimeOfMonthly;
import nts.uk.ctx.at.shared.dom.scherec.monthlyattdcal.monthly.calc.totalworkingtime.AggregateTotalWorkingTime;
import nts.uk.ctx.at.shared.dom.scherec.monthlyattdcal.monthly.calc.totalworkingtime.hdwkandcompleave.AggregateHolidayWorkTime;
import nts.uk.ctx.at.shared.dom.scherec.monthlyattdcal.monthly.calc.totalworkingtime.overtime.AggregateOverTime;
import nts.uk.ctx.at.shared.dom.scherec.monthlyattdcal.outsideot.breakdown.OutsideOTBRDItem;
import nts.uk.ctx.at.shared.dom.workingcondition.WorkingSystem;
import nts.uk.ctx.at.shared.dom.workrule.outsideworktime.holidaywork.HolidayWorkFrameNo;
import nts.uk.ctx.at.shared.dom.workrule.outsideworktime.overtime.overtimeframe.OverTimeFrameNo;

/**
 * ?????????????????????
 * @author shuichi_ishida
 */
@Getter
public class ExcessOutsideWorkDetail {

	/** ???????????? */
	private Map<GeneralDate, WorkTimeOfTimeSeries> workTime;
	/** ???????????? */
	private Map<OverTimeFrameNo, AggregateOverTime> overTime;
	/** ???????????? */
	private Map<HolidayWorkFrameNo, AggregateHolidayWorkTime> holidayWorkTime;
	/** ??????????????????????????? */
	private Map<GeneralDate, FlexTimeOfTimeSeries> flexExcessTime;
	/** ??????????????? */
	private Map<GeneralDate, WeeklyPremiumTimeOfTimeSeries> weeklyPremiumTime;
	/** ??????????????? */
	private Map<GeneralDate, MonthlyPremiumTimeOfTimeSeries> monthlyPremiumTime;
	/** ????????????????????? */
	private TotalTime totalTimeAfterRound;
	/** ?????????????????? */
	private TotalTime roundDiffTime;
	
	/**
	 * ?????????????????????
	 */
	public ExcessOutsideWorkDetail(){

		this.workTime = new HashMap<>();
		this.overTime = new HashMap<>();
		this.holidayWorkTime = new HashMap<>();
		this.flexExcessTime = new HashMap<>();
		this.weeklyPremiumTime = new HashMap<>();
		this.monthlyPremiumTime = new HashMap<>();
		this.totalTimeAfterRound = new TotalTime();
		this.roundDiffTime = new TotalTime();
	}
	
	/**
	 * ????????????????????????????????????????????????
	 * @param datePeriod ??????
	 * @return ?????????????????????????????????
	 */
	public AttendanceTimeMonthWithMinus getTotalFlexExcessTime(DatePeriod datePeriod){
		
		AttendanceTimeMonthWithMinus totalTime = new AttendanceTimeMonthWithMinus(0);
		for (val timeSeriesWork : this.flexExcessTime.values()){
			if (!datePeriod.contains(timeSeriesWork.getYmd())) continue;
			val flexTime = timeSeriesWork.getFlexTime().getFlexTime().getTime();
			totalTime = totalTime.addMinutes(flexTime.v());
		}
		return totalTime;
	}
	
	/**
	 * ????????????????????????????????????
	 * @param aggregateTotalWorkingTime ?????????????????????
	 * @param regAndIrgTimeOfMonthly ?????????????????????????????????
	 * @param flexTimeOfMonthly ????????????????????????????????????
	 * @param flexAggrSet ????????????????????????????????????????????????
	 * @param outsideOTBDItems ???????????????????????????????????????????????????????????????
	 * @param roundingSet ???????????????????????????
	 * @param isMultiMonth ???????????????????????????
	 */
	public void setTotalTimeAfterRound(AggregateTotalWorkingTime aggregateTotalWorkingTime,
			RegularAndIrregularTimeOfMonthly regAndIrgTimeOfMonthly, FlexTimeOfMonthly flexTimeOfMonthly,
			FlexMonthWorkTimeAggrSet flexAggrSet, List<OutsideOTBRDItem> outsideOTBDItems,
			RoundingSetOfMonthly roundingSet, boolean isMultiMonth) {
		
		// ???????????????????????????????????????
		TotalTimeBeforeRound totalTimeBeforeRound = new TotalTimeBeforeRound();
		totalTimeBeforeRound.copyValues(aggregateTotalWorkingTime, regAndIrgTimeOfMonthly, 
				flexTimeOfMonthly, flexAggrSet, outsideOTBDItems, isMultiMonth);
		
		// ???????????????????????????
		this.totalTimeAfterRound.setTotalTimeAfterRound(totalTimeBeforeRound, roundingSet);
	}

	/**
	 * ???????????????????????????????????????????????????????????????
	 * @param procDate ?????????
	 * @param flexExcessTargetTime ?????????????????????????????????
	 * @param flexTimeOfMonthly ????????????????????????????????????
	 * @return ?????????????????????????????????????????????????????????
	 */
	public AttendanceTimeMonthWithMinus assignFlexExcessTargetTimeByDayUnit(
			GeneralDate procDate,
			AttendanceTimeMonthWithMinus flexExcessTargetTime,
			FlexTimeOfMonthly flexTimeOfMonthly){
		
		AttendanceTimeMonthWithMinus timeAfterAssign = flexExcessTargetTime; 
		
		// ????????????????????????????????????
		val timeSeriesWorks = flexTimeOfMonthly.getFlexTime().getTimeSeriesWorks();
		if (!timeSeriesWorks.containsKey(procDate)) return timeAfterAssign;
		val flexMinutes = timeSeriesWorks.get(procDate).getFlexTime().getFlexTime().getTime().v();
		if (flexMinutes <= 0) return timeAfterAssign;
		
		// ??????????????????????????????????????????????????????????????????????????????
		int assignMinutes = timeAfterAssign.v();
		if (assignMinutes > flexMinutes) assignMinutes = flexMinutes;
		this.flexExcessTime.putIfAbsent(procDate, new FlexTimeOfTimeSeries(procDate));
		this.flexExcessTime.get(procDate).addMinutesToFlexTimeInFlexTime(assignMinutes);
		
		// ??????????????????????????????????????????????????????????????????????????????????????????
		timeAfterAssign = timeAfterAssign.minusMinutes(assignMinutes);
		
		return timeAfterAssign;
	}
	
	/**
	 * ??????????????????????????????
	 * @param monthlyDetail ????????????
	 * @param datePeriod ??????
	 * @param roundingSet ???????????????????????????
	 * @param workingSystem ?????????
	 */
	public void assignRoundTime(MonthlyDetail monthlyDetail, DatePeriod datePeriod,
			RoundingSetOfMonthly roundingSet, WorkingSystem workingSystem){
		
		// ???????????????????????????????????????????????????????????????
		this.workTime = monthlyDetail.getWorkTime();
		this.overTime = monthlyDetail.getOverTime();
		this.holidayWorkTime = monthlyDetail.getHolidayWorkTime();
		
		// ????????????????????????????????????
		TotalTimeBeforeRound totalTimeBeforeRound = new TotalTimeBeforeRound();
		totalTimeBeforeRound.aggregateValues(this, datePeriod, workingSystem);
		
		// ???????????????????????????
		this.totalTimeAfterRound.setTotalTimeAfterRound(totalTimeBeforeRound, roundingSet);
		
		// ??????????????????????????????
		this.askRoundDiffTime(totalTimeBeforeRound);
	}
	
	/**
	 * ??????????????????????????????
	 * @param totalTimeBeforeRound ?????????????????????
	 */
	private void askRoundDiffTime(TotalTimeBeforeRound totalTimeBeforeRound){
		
		// ????????????
		int diffWorkMinutes = this.totalTimeAfterRound.getWorkTime().v()
				- totalTimeBeforeRound.getWorkTime().v();
		int diffWithinPrescribedMinutes = this.totalTimeAfterRound.getWithinPrescribedPremiumTime().v()
				- totalTimeBeforeRound.getWithinPrescribedPremiumTime().v();
		this.roundDiffTime.setWorkTime(new AttendanceTimeMonth(diffWorkMinutes));
		this.roundDiffTime.setWithinPrescribedPremiumTime(new AttendanceTimeMonth(diffWithinPrescribedMinutes));
		
		// ????????????
		for (val overTimeEachFrameNo : this.totalTimeAfterRound.getOverTime().values()){
			val overTimeFrameNo = overTimeEachFrameNo.getOverTimeFrameNo();
			if (!totalTimeBeforeRound.getOverTime().containsKey(overTimeFrameNo)) continue;
			val overTimeBeforeRound = totalTimeBeforeRound.getOverTime().get(overTimeFrameNo);
			
			int diffOverMinutes = overTimeEachFrameNo.getOverTime().getTime().v()
					- overTimeBeforeRound.getOverTime().getTime().v();
			
			int diffCalcOverMinutes = overTimeEachFrameNo.getOverTime().getCalcTime().v()
					- overTimeBeforeRound.getOverTime().getCalcTime().v();
			
			int diffTransOverMinutes = overTimeEachFrameNo.getTransferOverTime().getTime().v()
					- overTimeBeforeRound.getTransferOverTime().getTime().v();
			
			int diffCalcTransOverMinutes = overTimeEachFrameNo.getTransferOverTime().getCalcTime().v()
					- overTimeBeforeRound.getTransferOverTime().getCalcTime().v();
			
			this.roundDiffTime.getOverTime().putIfAbsent(overTimeFrameNo,
					OverTimeFrameTotalTime.of(overTimeFrameNo,
							new TimeMonthWithCalculation(
									new AttendanceTimeMonth(diffOverMinutes),
									new AttendanceTimeMonth(diffCalcOverMinutes)),
							new TimeMonthWithCalculation(
									new AttendanceTimeMonth(diffTransOverMinutes),
									new AttendanceTimeMonth(diffCalcTransOverMinutes))));
		}
		
		// ????????????
		for (val holidayWorkTimeEachFrameNo : this.totalTimeAfterRound.getHolidayWorkTime().values()){
			val holidayWorkTimeFrameNo = holidayWorkTimeEachFrameNo.getHolidayWorkFrameNo();
			if (!totalTimeBeforeRound.getHolidayWorkTime().containsKey(holidayWorkTimeFrameNo)) continue;
			val holidayWorkTimeBeforeRound = totalTimeBeforeRound.getHolidayWorkTime().get(holidayWorkTimeFrameNo);
			
			int diffHolidayWorkMinutes = holidayWorkTimeEachFrameNo.getHolidayWorkTime().getTime().v()
					- holidayWorkTimeBeforeRound.getHolidayWorkTime().getTime().v();
			
			int diffCalcHolidayWorkMinutes = holidayWorkTimeEachFrameNo.getHolidayWorkTime().getCalcTime().v()
					- holidayWorkTimeBeforeRound.getHolidayWorkTime().getCalcTime().v();
			
			int diffTransMinutes = holidayWorkTimeEachFrameNo.getTransferTime().getTime().v()
					- holidayWorkTimeBeforeRound.getTransferTime().getTime().v();
			
			int diffCalcTransMinutes = holidayWorkTimeEachFrameNo.getTransferTime().getCalcTime().v()
					- holidayWorkTimeBeforeRound.getTransferTime().getCalcTime().v();
			
			this.roundDiffTime.getHolidayWorkTime().putIfAbsent(holidayWorkTimeFrameNo,
					HolidayWorkFrameTotalTime.of(holidayWorkTimeFrameNo,
							new TimeMonthWithCalculation(
									new AttendanceTimeMonth(diffHolidayWorkMinutes),
									new AttendanceTimeMonth(diffCalcHolidayWorkMinutes)),
							new TimeMonthWithCalculation(
									new AttendanceTimeMonth(diffTransMinutes),
									new AttendanceTimeMonth(diffCalcTransMinutes))));
		}
		
		// ???????????????????????????
		int diffFlexExcessMinutes = this.totalTimeAfterRound.getFlexExcessTime().v()
				- totalTimeBeforeRound.getFlexExcessTime().v();
		this.roundDiffTime.setFlexExcessTime(new AttendanceTimeMonth(diffFlexExcessMinutes));
		
		// ?????????????????????
		int diffWeekPremiumMinutes = this.totalTimeAfterRound.getWeeklyTotalPremiumTime().v()
				- totalTimeBeforeRound.getWeeklyTotalPremiumTime().v();
		this.roundDiffTime.setWeeklyTotalPremiumTime(new AttendanceTimeMonth(diffWeekPremiumMinutes));
		
		// ?????????????????????
		int diffMonthPremiumMinutes = this.totalTimeAfterRound.getMonthlyTotalPremiumTime().v()
				- totalTimeBeforeRound.getMonthlyTotalPremiumTime().v();
		this.roundDiffTime.setMonthlyTotalPremiumTime(new AttendanceTimeMonth(diffMonthPremiumMinutes));
	}
	
	/**
	 * ????????????ID????????????????????????????????????
	 * @param attendanceItemId ????????????ID
	 * @param procDate ?????????
	 * @return ??????????????????
	 */
	public AttendanceTimeMonth getTimeOfAttendanceItemId(int attendanceItemId, GeneralDate procDate){

		AttendanceTimeMonth notExistTime = new AttendanceTimeMonth(0);
		
		// ????????????
		if (attendanceItemId == AttendanceItemOfMonthly.WORK_TIME.value) {
			if (!this.workTime.containsKey(procDate)) return notExistTime;
			return new AttendanceTimeMonth(this.workTime.get(procDate).getLegalTime().getWorkTime().v());
		}
		
		// ????????????
		if (attendanceItemId >= AttendanceItemOfMonthly.OVER_TIME_01.value &&
			attendanceItemId <= AttendanceItemOfMonthly.OVER_TIME_10.value){
			val overTimeFrameNo = new OverTimeFrameNo(
					attendanceItemId - AttendanceItemOfMonthly.OVER_TIME_01.value + 1);
			if (!this.overTime.containsKey(overTimeFrameNo)) return notExistTime;
			val overTimeDetails = this.overTime.get(overTimeFrameNo).getTimeSeriesWorks();
			if (!overTimeDetails.containsKey(procDate)) return notExistTime;
			val overTimeDetail = overTimeDetails.get(procDate).getOverTime();
			return new AttendanceTimeMonth(overTimeDetail.getOverTimeWork().getTime().v());
		}
		
		// ??????????????????
		if (attendanceItemId >= AttendanceItemOfMonthly.CALC_OVER_TIME_01.value &&
			attendanceItemId <= AttendanceItemOfMonthly.CALC_OVER_TIME_10.value){
			val overTimeFrameNo = new OverTimeFrameNo(
					attendanceItemId - AttendanceItemOfMonthly.CALC_OVER_TIME_01.value + 1);
			if (!this.overTime.containsKey(overTimeFrameNo)) return notExistTime;
			val overTimeDetails = this.overTime.get(overTimeFrameNo).getTimeSeriesWorks();
			if (!overTimeDetails.containsKey(procDate)) return notExistTime;
			val overTimeDetail = overTimeDetails.get(procDate).getOverTime();
			return new AttendanceTimeMonth(overTimeDetail.getOverTimeWork().getCalcTime().v());
		}
		
		// ??????????????????
		if (attendanceItemId >= AttendanceItemOfMonthly.TRANSFER_OVER_TIME_01.value &&
			attendanceItemId <= AttendanceItemOfMonthly.TRANSFER_OVER_TIME_10.value){
			val overTimeFrameNo = new OverTimeFrameNo(
					attendanceItemId - AttendanceItemOfMonthly.TRANSFER_OVER_TIME_01.value + 1);
			if (!this.overTime.containsKey(overTimeFrameNo)) return notExistTime;
			val overTimeDetails = this.overTime.get(overTimeFrameNo).getTimeSeriesWorks();
			if (!overTimeDetails.containsKey(procDate)) return notExistTime;
			val overTimeDetail = overTimeDetails.get(procDate).getOverTime();
			return new AttendanceTimeMonth(overTimeDetail.getTransferTime().getTime().v());
		}
		
		// ????????????????????????
		if (attendanceItemId >= AttendanceItemOfMonthly.CALC_TRANSFER_OVER_TIME_01.value &&
			attendanceItemId <= AttendanceItemOfMonthly.CALC_TRANSFER_OVER_TIME_10.value){
			val overTimeFrameNo = new OverTimeFrameNo(
					attendanceItemId - AttendanceItemOfMonthly.CALC_TRANSFER_OVER_TIME_01.value + 1);
			if (!this.overTime.containsKey(overTimeFrameNo)) return notExistTime;
			val overTimeDetails = this.overTime.get(overTimeFrameNo).getTimeSeriesWorks();
			if (!overTimeDetails.containsKey(procDate)) return notExistTime;
			val overTimeDetail = overTimeDetails.get(procDate).getOverTime();
			return new AttendanceTimeMonth(overTimeDetail.getTransferTime().getCalcTime().v());
		}
		
		// ????????????
		if (attendanceItemId >= AttendanceItemOfMonthly.HOLIDAY_WORK_TIME_01.value &&
			attendanceItemId <= AttendanceItemOfMonthly.HOLIDAY_WORK_TIME_10.value){
			val holidayWorkTimeFrameNo = new HolidayWorkFrameNo(
					attendanceItemId - AttendanceItemOfMonthly.HOLIDAY_WORK_TIME_01.value + 1);
			if (!this.holidayWorkTime.containsKey(holidayWorkTimeFrameNo)) return notExistTime;
			val holidayWorkTimeDetails = this.holidayWorkTime.get(holidayWorkTimeFrameNo).getTimeSeriesWorks();
			if (!holidayWorkTimeDetails.containsKey(procDate)) return notExistTime;
			val holidayWorkTimeDetail = holidayWorkTimeDetails.get(procDate).getHolidayWorkTime();
			return new AttendanceTimeMonth(holidayWorkTimeDetail.getHolidayWorkTime().get().getTime().v());
		}
		
		// ??????????????????
		if (attendanceItemId >= AttendanceItemOfMonthly.CALC_HOLIDAY_WORK_TIME_01.value &&
			attendanceItemId <= AttendanceItemOfMonthly.CALC_HOLIDAY_WORK_TIME_10.value){
			val holidayWorkTimeFrameNo = new HolidayWorkFrameNo(
					attendanceItemId - AttendanceItemOfMonthly.CALC_HOLIDAY_WORK_TIME_01.value + 1);
			if (!this.holidayWorkTime.containsKey(holidayWorkTimeFrameNo)) return notExistTime;
			val holidayWorkTimeDetails = this.holidayWorkTime.get(holidayWorkTimeFrameNo).getTimeSeriesWorks();
			if (!holidayWorkTimeDetails.containsKey(procDate)) return notExistTime;
			val holidayWorkTimeDetail = holidayWorkTimeDetails.get(procDate).getHolidayWorkTime();
			return new AttendanceTimeMonth(holidayWorkTimeDetail.getHolidayWorkTime().get().getCalcTime().v());
		}
		
		// ????????????
		if (attendanceItemId >= AttendanceItemOfMonthly.TRANSFER_TIME_01.value &&
			attendanceItemId <= AttendanceItemOfMonthly.TRANSFER_TIME_10.value){
			val holidayWorkTimeFrameNo = new HolidayWorkFrameNo(
					attendanceItemId - AttendanceItemOfMonthly.TRANSFER_TIME_01.value + 1);
			if (!this.holidayWorkTime.containsKey(holidayWorkTimeFrameNo)) return notExistTime;
			val holidayWorkTimeDetails = this.holidayWorkTime.get(holidayWorkTimeFrameNo).getTimeSeriesWorks();
			if (!holidayWorkTimeDetails.containsKey(procDate)) return notExistTime;
			val holidayWorkTimeDetail = holidayWorkTimeDetails.get(procDate).getHolidayWorkTime();
			return new AttendanceTimeMonth(holidayWorkTimeDetail.getTransferTime().get().getTime().v());
		}
		
		// ??????????????????
		if (attendanceItemId >= AttendanceItemOfMonthly.CALC_TRANSFER_TIME_01.value &&
			attendanceItemId <= AttendanceItemOfMonthly.CALC_TRANSFER_TIME_10.value){
			val holidayWorkTimeFrameNo = new HolidayWorkFrameNo(
					attendanceItemId - AttendanceItemOfMonthly.CALC_TRANSFER_TIME_01.value + 1);
			if (!this.holidayWorkTime.containsKey(holidayWorkTimeFrameNo)) return notExistTime;
			val holidayWorkTimeDetails = this.holidayWorkTime.get(holidayWorkTimeFrameNo).getTimeSeriesWorks();
			if (!holidayWorkTimeDetails.containsKey(procDate)) return notExistTime;
			val holidayWorkTimeDetail = holidayWorkTimeDetails.get(procDate).getHolidayWorkTime();
			return new AttendanceTimeMonth(holidayWorkTimeDetail.getTransferTime().get().getCalcTime().v());
		}
		
		// ????????????????????????????????????????????????????????????????????????
		if (attendanceItemId == AttendanceItemOfMonthly.FLEX_EXCESS_TIME.value){
			if (!this.flexExcessTime.containsKey(procDate)) return notExistTime;
			Integer flexExcessMinutes = this.flexExcessTime.get(procDate).getFlexTime().getFlexTime().getTime().v();
			if (flexExcessMinutes <= 0) flexExcessMinutes = 0;
			return new AttendanceTimeMonth(flexExcessMinutes);
		}
		
		// ?????????????????????
		if (attendanceItemId == AttendanceItemOfMonthly.WITHIN_PRESCRIBED_PREMIUM_TIME.value){
			if (!this.workTime.containsKey(procDate)) return notExistTime;
			return new AttendanceTimeMonth(this.workTime.get(procDate).getLegalTime().getWithinPrescribedPremiumTime().v());
		}
		
		// ?????????????????????
		if (attendanceItemId == AttendanceItemOfMonthly.WEEKLY_TOTAL_PREMIUM_TIME.value){
			if (!this.weeklyPremiumTime.containsKey(procDate)) return notExistTime;
			return this.weeklyPremiumTime.get(procDate).getWeeklyPremiumTime();
		}
		
		// ?????????????????????
		if (attendanceItemId == AttendanceItemOfMonthly.MONTHLY_TOTAL_PREMIUM_TIME.value){
			if (!this.monthlyPremiumTime.containsKey(procDate)) return notExistTime;
			return this.monthlyPremiumTime.get(procDate).getMonthlyTotalPremiumTime();
		}
		
		return notExistTime;
	}
}
