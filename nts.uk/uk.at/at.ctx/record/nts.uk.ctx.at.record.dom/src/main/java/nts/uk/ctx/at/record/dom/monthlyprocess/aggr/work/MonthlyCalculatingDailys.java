package nts.uk.ctx.at.record.dom.monthlyprocess.aggr.work;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.stream.Collectors;

import lombok.Getter;
import lombok.Setter;
import lombok.val;
import nts.arc.time.GeneralDate;
import nts.arc.time.calendar.period.DatePeriod;
import nts.uk.ctx.at.record.dom.actualworkinghours.ActualWorkingTimeOfDaily;
import nts.uk.ctx.at.record.dom.actualworkinghours.AttendanceTimeOfDailyPerformance;
import nts.uk.ctx.at.record.dom.actualworkinghours.TotalWorkingTime;
import nts.uk.ctx.at.record.dom.actualworkinghours.daily.workschedule.WorkScheduleTimeOfDaily;
import nts.uk.ctx.at.record.dom.affiliationinformation.WorkTypeOfDailyPerformance;
import nts.uk.ctx.at.record.dom.daily.attendanceleavinggate.PCLogOnInfoOfDaily;
import nts.uk.ctx.at.record.dom.daily.optionalitemtime.AnyItemValueOfDaily;
import nts.uk.ctx.at.record.dom.daily.vacationusetime.AnnualOfDaily;
import nts.uk.ctx.at.record.dom.daily.vacationusetime.HolidayOfDaily;
import nts.uk.ctx.at.record.dom.daily.vacationusetime.SpecialHolidayOfDaily;
import nts.uk.ctx.at.record.dom.daily.vacationusetime.SubstituteHolidayOfDaily;
import nts.uk.ctx.at.record.dom.daily.vacationusetime.YearlyReservedOfDaily;
import nts.uk.ctx.at.record.dom.daily.withinworktime.WithinStatutoryTimeOfDaily;
import nts.uk.ctx.at.record.dom.dailyprocess.calc.IntegrationOfDaily;
import nts.uk.ctx.at.record.dom.raisesalarytime.SpecificDateAttrOfDailyPerfor;
import nts.uk.ctx.at.record.dom.remainingnumber.annualleave.export.param.AnnualLeaveGrantRemaining;
import nts.uk.ctx.at.record.dom.remainingnumber.reserveleave.export.param.ReserveLeaveGrantRemaining;
import nts.uk.ctx.at.record.dom.workinformation.WorkInfoOfDailyPerformance;
import nts.uk.ctx.at.record.dom.workrecord.erroralarm.EmployeeDailyPerError;
import nts.uk.ctx.at.record.dom.worktime.TemporaryTimeOfDailyPerformance;
import nts.uk.ctx.at.record.dom.worktime.TimeLeavingOfDailyPerformance;
import nts.uk.ctx.at.shared.dom.common.time.AttendanceTime;
import nts.uk.ctx.at.shared.dom.remainingnumber.annualleave.empinfo.grantremainingdata.AnnualLeaveGrantRemainingData;
import nts.uk.ctx.at.shared.dom.remainingnumber.reserveleave.empinfo.grantremainingdata.ReserveLeaveGrantRemainingData;
import nts.uk.ctx.at.shared.dom.workingcondition.WorkingConditionItem;
import nts.uk.ctx.at.shared.dom.workingcondition.WorkingSystem;

/**
 * 月の計算中の日別実績データ
 * @author shuichi_ishida
 */
@Getter
@Setter
public class MonthlyCalculatingDailys {

	/** 日別実績の勤怠時間リスト */
	private Map<GeneralDate, AttendanceTimeOfDailyPerformance> attendanceTimeOfDailyMap;
	/** 日別実績の勤務情報リスト */
	private Map<GeneralDate, WorkInfoOfDailyPerformance> workInfoOfDailyMap;
	/** 日別実績の出退勤リスト */
	private Map<GeneralDate, TimeLeavingOfDailyPerformance> timeLeaveOfDailyMap; 
	/** 日別実績の臨時出退勤リスト */
	private Map<GeneralDate, TemporaryTimeOfDailyPerformance> temporaryTimeOfDailyMap;
	/** 日別実績の特定日区分リスト */
	private Map<GeneralDate, SpecificDateAttrOfDailyPerfor> specificDateAttrOfDailyMap;
	/** 社員の日別実績エラー一覧リスト */
	private List<EmployeeDailyPerError> employeeDailyPerErrorList;
	/** 日別実績の任意項目リスト */
	private List<AnyItemValueOfDaily> anyItemValueOfDailyList;
	/** 日別実績のPCログオン情報リスト */
	private Map<GeneralDate, PCLogOnInfoOfDaily> pcLogonInfoMap;
	/** 年休付与残数データリスト */
	private List<AnnualLeaveGrantRemaining> grantRemainingDatas;
	/** 積立年休付与残数データリスト */
	private List<ReserveLeaveGrantRemaining> rsvGrantRemainingDatas;
	/** 日別実績の勤務種別 */
	private Map<GeneralDate, WorkTypeOfDailyPerformance> workTypeOfDailyMap;
	
	public MonthlyCalculatingDailys(){
		this.attendanceTimeOfDailyMap = new HashMap<>();
		this.workInfoOfDailyMap = new HashMap<>();
		this.timeLeaveOfDailyMap = new HashMap<>();
		this.temporaryTimeOfDailyMap = new HashMap<>();
		this.specificDateAttrOfDailyMap = new HashMap<>();
		this.employeeDailyPerErrorList = new ArrayList<>();
		this.anyItemValueOfDailyList = new ArrayList<>();
		this.pcLogonInfoMap = new HashMap<>();
		this.grantRemainingDatas = new ArrayList<>();
		this.rsvGrantRemainingDatas = new ArrayList<>();
		this.workTypeOfDailyMap = new HashMap<>();
	}
	
	/**
	 * データ取得
	 * @param employeeId 社員ID
	 * @param period 期間
	 * @param repositories 月別集計が必要とするリポジトリ
	 * @return 月の計算中の日別実績データ
	 */
	public static MonthlyCalculatingDailys loadData(
			String employeeId,
			DatePeriod period,
			RepositoriesRequiredByMonthlyAggr repositories){
		
		return loadDataRequire(createRequireImpl(repositories), employeeId, period, repositories);
	}
	
	public static MonthlyCalculatingDailys loadDataRequire(
			Require require, 
			String employeeId,
			DatePeriod period,
			RepositoriesRequiredByMonthlyAggr repositories){
		MonthlyCalculatingDailys result = new MonthlyCalculatingDailys();
		
		// 取得期間を　開始日-1月～終了日+1月　とする　（前月の最終週、36協定締め日違いの集計のため）
		DatePeriod findPeriod = new DatePeriod(period.start().addMonths(-1), period.end().addDays(31));
		
		// 日別実績の勤怠時間
		val attendanceTimeOfDailyList =
				repositories.getAttendanceTimeOfDaily().findByPeriodOrderByYmd(employeeId, findPeriod);
		for (val attendanceTimeOfDaily : attendanceTimeOfDailyList){
			result.attendanceTimeOfDailyMap.putIfAbsent(attendanceTimeOfDaily.getYmd(), attendanceTimeOfDaily);
		}
		
		// 共通処理
		result.loadDataCommon(require, employeeId, period, repositories);

		return correctExamDayTimeRequire(require, result, repositories);
	}
	
	/**
	 * データ取得
	 * @param employeeId 社員ID
	 * @param period 期間
	 * @param attendanceTimeOfDailys 日別実績の勤怠時間リスト
	 * @param repositories 月別集計が必要とするリポジトリ
	 * @return 月の計算中の日別実績データ
	 */
	public static MonthlyCalculatingDailys loadData(
			String employeeId,
			DatePeriod period,
			List<AttendanceTimeOfDailyPerformance> attendanceTimeOfDailys,
			RepositoriesRequiredByMonthlyAggr repositories){
		
		// 期間内の全データ読み込み
		MonthlyCalculatingDailys result = MonthlyCalculatingDailys.loadData(employeeId, period, repositories);
		
		// 日別実績の勤怠時間リストの指定がない時、終了
		if (attendanceTimeOfDailys.size() <= 0) return result;
		
		// 日別実績の勤怠時間を反映する
		for (val attendanceTimeOfDaily : attendanceTimeOfDailys){
			val ymd = attendanceTimeOfDaily.getYmd();
			result.attendanceTimeOfDailyMap.put(ymd, attendanceTimeOfDaily);
		}
		
		return correctExamDayTime(result, repositories);
	}
	
	/**
	 * データ取得
	 * @param employeeId 社員ID
	 * @param period 期間
	 * @param dailyWorksOpt 日別実績(WORK)リスト
	 * @param repositories 月別集計が必要とするリポジトリ
	 * @return 月の計算中の日別実績データ
	 */
	public static MonthlyCalculatingDailys loadData(
			Require require,
			String employeeId,
			DatePeriod period,
			Optional<List<IntegrationOfDaily>> dailyWorksOpt,
			RepositoriesRequiredByMonthlyAggr repositories){
		
		// 期間内の全データ読み込み
		MonthlyCalculatingDailys result = MonthlyCalculatingDailys.loadData(employeeId, period, repositories);

		// 日別実績(WORK)指定がない時、終了
		if (!dailyWorksOpt.isPresent()) return result;
		
		for (val dailyWork : dailyWorksOpt.get()){
			
			// 日別実績の勤怠時間がない日は、反映しない
			if (!dailyWork.getAttendanceTimeOfDailyPerformance().isPresent()) continue;
			
			// 日別実績の勤怠時間
			val attendanceTimeOfDaily = dailyWork.getAttendanceTimeOfDailyPerformance().get();
			val ymd = attendanceTimeOfDaily.getYmd();
			result.attendanceTimeOfDailyMap.put(ymd, attendanceTimeOfDaily);
			
			// 日別実績の出退勤
			if (dailyWork.getAttendanceLeave().isPresent()){
				val timeLeaveOfDaily = dailyWork.getAttendanceLeave().get();
				result.timeLeaveOfDailyMap.put(ymd, timeLeaveOfDaily);
			}
			
			// 日別実績の勤務情報
			if (dailyWork.getWorkInformation() != null){
				val workInfoOfDaily = dailyWork.getWorkInformation();
				result.workInfoOfDailyMap.put(ymd, workInfoOfDaily);
			}
			
			// 日別実績の臨時出退勤
			if (dailyWork.getTempTime().isPresent()){
				val temporaryTimeOfDaily = dailyWork.getTempTime().get();
				result.temporaryTimeOfDailyMap.put(ymd, temporaryTimeOfDaily);
			}
			
			// 日別実績の特定日区分
			if (dailyWork.getSpecDateAttr().isPresent()){
				val specificDateAttrOfDaily = dailyWork.getSpecDateAttr().get();
				result.specificDateAttrOfDailyMap.put(ymd, specificDateAttrOfDaily);
			}
			
			// 社員の日別実績エラー一覧
			if (dailyWork.getEmployeeError() != null){
				val itrPerError = result.employeeDailyPerErrorList.listIterator();
				while (itrPerError.hasNext()){
					val perError = itrPerError.next();
					if (perError == null){
						itrPerError.remove();
						continue;
					}
					if (perError.getDate() == null) continue;
					if (perError.getDate().compareTo(ymd) != 0) continue;
					itrPerError.remove();
				}
				result.employeeDailyPerErrorList.addAll(dailyWork.getEmployeeError());
			}
			
			// 日別実績の任意項目
			if (dailyWork.getAnyItemValue().isPresent()){
				val itrAnyItem = result.anyItemValueOfDailyList.listIterator();
				while (itrAnyItem.hasNext()){
					val anyItem = itrAnyItem.next();
					if (anyItem.getYmd().compareTo(ymd) != 0) continue;
					itrAnyItem.remove();
				}
				result.anyItemValueOfDailyList.add(dailyWork.getAnyItemValue().get());
			}
			
			// PCログオン情報
			if (dailyWork.getPcLogOnInfo().isPresent()){
				val pcLogonInfo = dailyWork.getPcLogOnInfo().get();
				result.pcLogonInfoMap.put(pcLogonInfo.getYmd(), pcLogonInfo);
			}
			
			// 日別実績の勤務種別
			if (dailyWork.getBusinessType().isPresent()) {
				val workType = dailyWork.getBusinessType().get();
				result.workTypeOfDailyMap.put(workType.getDate(), workType);
			}
		}
		
		return correctExamDayTimeRequire(require, result, repositories);

	}
	
	private static MonthlyCalculatingDailys correctExamDayTime(MonthlyCalculatingDailys calcDaily, RepositoriesRequiredByMonthlyAggr repositories){
		return correctExamDayTimeRequire(createRequireImpl(repositories), calcDaily, repositories);
	}
	private static MonthlyCalculatingDailys correctExamDayTimeRequire(Require require, MonthlyCalculatingDailys calcDaily, RepositoriesRequiredByMonthlyAggr repositories){
		for (Entry<GeneralDate, AttendanceTimeOfDailyPerformance> dailyAttenTime : calcDaily.attendanceTimeOfDailyMap.entrySet()) {
			GeneralDate currentDate = dailyAttenTime.getKey();
			if (calcDaily.workInfoOfDailyMap.containsKey(currentDate)) {
				WorkInfoOfDailyPerformance wi = calcDaily.workInfoOfDailyMap.get(currentDate);
				val workCondition = require.getBySidAndStandardDate(wi.getEmployeeId(), currentDate);
				workCondition.ifPresent(wc -> {
					if (wc.getLaborSystem() == WorkingSystem.FLEX_TIME_WORK) {
						calcDaily.attendanceTimeOfDailyMap.put(currentDate, examDayTimeCorrect(dailyAttenTime.getValue(), wi));
					}
				});
			}
		}
		return correctExamDayTime(result, repositories);
	}
	
	private static MonthlyCalculatingDailys correctExamDayTime(MonthlyCalculatingDailys calcDaily, RepositoriesRequiredByMonthlyAggr repositories){
		
		
		for (Entry<GeneralDate, AttendanceTimeOfDailyPerformance> dailyAttenTime : calcDaily.attendanceTimeOfDailyMap.entrySet()) {
			
			GeneralDate currentDate = dailyAttenTime.getKey();
			
			if (calcDaily.workInfoOfDailyMap.containsKey(currentDate)) {
				WorkInfoOfDailyPerformance wi = calcDaily.workInfoOfDailyMap.get(currentDate);
				
				val workCondition = repositories.getWorkingConditionItem().getBySidAndStandardDate(wi.getEmployeeId(), currentDate);
				
				workCondition.ifPresent(wc -> {
					if (wc.getLaborSystem() == WorkingSystem.FLEX_TIME_WORK) {
						calcDaily.attendanceTimeOfDailyMap.put(currentDate, examDayTimeCorrect(dailyAttenTime.getValue(), wi));
					}
				});
			}
		}
		
		return calcDaily;
	}
	
	/**
	 * 大塚カスタマイズ（試験日対応）
	 */
	private static AttendanceTimeOfDailyPerformance examDayTimeCorrect(AttendanceTimeOfDailyPerformance atTime, WorkInfoOfDailyPerformance workInfo) {
		
		if (workInfo.getRecordInfo().isExamWorkTime()) {
			
			WorkScheduleTimeOfDaily correctedSche = new WorkScheduleTimeOfDaily(atTime.getWorkScheduleTimeOfDaily().getWorkScheduleTime(),
																				new AttendanceTime(0), 
																				atTime.getWorkScheduleTimeOfDaily().getRecordPrescribedLaborTime());
			
			ActualWorkingTimeOfDaily beforeActualWork = atTime.getActualWorkingTimeOfDaily();
			ActualWorkingTimeOfDaily correctedActualWork = ActualWorkingTimeOfDaily.of(
										beforeActualWork.getConstraintDifferenceTime(), 
										beforeActualWork.getConstraintTime(), 
										beforeActualWork.getTimeDifferenceWorkingHours(), 
										new TotalWorkingTime(beforeActualWork.getTotalWorkingTime().getTotalTime(), 
												beforeActualWork.getTotalWorkingTime().getTotalCalcTime(), 
												beforeActualWork.getTotalWorkingTime().getActualTime(),
												WithinStatutoryTimeOfDaily.createWithinStatutoryTimeOfDaily(
														new AttendanceTime(0), 
														new AttendanceTime(0), 
														beforeActualWork.getTotalWorkingTime().getWithinStatutoryTimeOfDaily().getWithinPrescribedPremiumTime(), 
														beforeActualWork.getTotalWorkingTime().getWithinStatutoryTimeOfDaily().getWithinStatutoryMidNightTime(), 
														beforeActualWork.getTotalWorkingTime().getWithinStatutoryTimeOfDaily().getVacationAddTime()), 
												beforeActualWork.getTotalWorkingTime().getExcessOfStatutoryTimeOfDaily(), 
												beforeActualWork.getTotalWorkingTime().getLateTimeOfDaily(),
												beforeActualWork.getTotalWorkingTime().getLeaveEarlyTimeOfDaily(),
												beforeActualWork.getTotalWorkingTime().getBreakTimeOfDaily(), 
												beforeActualWork.getTotalWorkingTime().getOutingTimeOfDailyPerformance(),
												beforeActualWork.getTotalWorkingTime().getRaiseSalaryTimeOfDailyPerfor(),
												beforeActualWork.getTotalWorkingTime().getWorkTimes(),
												beforeActualWork.getTotalWorkingTime().getTemporaryTime(),
												beforeActualWork.getTotalWorkingTime().getShotrTimeOfDaily(),
												new HolidayOfDaily(
														beforeActualWork.getTotalWorkingTime().getHolidayOfDaily().getAbsence(), 
														beforeActualWork.getTotalWorkingTime().getHolidayOfDaily().getTimeDigest(), 
														new YearlyReservedOfDaily(new AttendanceTime(0)), 
														new SubstituteHolidayOfDaily(new AttendanceTime(0), 
																beforeActualWork.getTotalWorkingTime().getHolidayOfDaily().getSubstitute().getDigestionUseTime()), 
														beforeActualWork.getTotalWorkingTime().getHolidayOfDaily().getOverSalary(), 
														new SpecialHolidayOfDaily(new AttendanceTime(0), 
																beforeActualWork.getTotalWorkingTime().getHolidayOfDaily().getSpecialHoliday().getDigestionUseTime()), 
														new AnnualOfDaily(new AttendanceTime(0), 
																beforeActualWork.getTotalWorkingTime().getHolidayOfDaily().getAnnual().getDigestionUseTime()))),
										beforeActualWork.getDivTime(),
										beforeActualWork.getPremiumTimeOfDailyPerformance());
			
			return new AttendanceTimeOfDailyPerformance(atTime.getEmployeeId(), atTime.getYmd(), correctedSche, correctedActualWork, 
														atTime.getStayingTime(), atTime.getBudgetTimeVariance(), atTime.getUnEmployedTime()); 
		}
		
		return atTime;
	} 
	
	/**
	 * 大塚カスタマイズ（試験日対応）
	 */
	private static AttendanceTimeOfDailyPerformance examDayTimeCorrect(AttendanceTimeOfDailyPerformance atTime, WorkInfoOfDailyPerformance workInfo) {
		
		if (workInfo.getRecordInfo().isExamWorkTime()) {
			
			WorkScheduleTimeOfDaily correctedSche = new WorkScheduleTimeOfDaily(atTime.getWorkScheduleTimeOfDaily().getWorkScheduleTime(),
																				new AttendanceTime(0), 
																				atTime.getWorkScheduleTimeOfDaily().getRecordPrescribedLaborTime());
			
			ActualWorkingTimeOfDaily beforeActualWork = atTime.getActualWorkingTimeOfDaily();
			ActualWorkingTimeOfDaily correctedActualWork = ActualWorkingTimeOfDaily.of(
										beforeActualWork.getConstraintDifferenceTime(), 
										beforeActualWork.getConstraintTime(), 
										beforeActualWork.getTimeDifferenceWorkingHours(), 
										new TotalWorkingTime(beforeActualWork.getTotalWorkingTime().getTotalTime(), 
												beforeActualWork.getTotalWorkingTime().getTotalCalcTime(), 
												beforeActualWork.getTotalWorkingTime().getActualTime(),
												WithinStatutoryTimeOfDaily.createWithinStatutoryTimeOfDaily(
														new AttendanceTime(0), 
														new AttendanceTime(0), 
														beforeActualWork.getTotalWorkingTime().getWithinStatutoryTimeOfDaily().getWithinPrescribedPremiumTime(), 
														beforeActualWork.getTotalWorkingTime().getWithinStatutoryTimeOfDaily().getWithinStatutoryMidNightTime(), 
														beforeActualWork.getTotalWorkingTime().getWithinStatutoryTimeOfDaily().getVacationAddTime()), 
												beforeActualWork.getTotalWorkingTime().getExcessOfStatutoryTimeOfDaily(), 
												beforeActualWork.getTotalWorkingTime().getLateTimeOfDaily(),
												beforeActualWork.getTotalWorkingTime().getLeaveEarlyTimeOfDaily(),
												beforeActualWork.getTotalWorkingTime().getBreakTimeOfDaily(), 
												beforeActualWork.getTotalWorkingTime().getOutingTimeOfDailyPerformance(),
												beforeActualWork.getTotalWorkingTime().getRaiseSalaryTimeOfDailyPerfor(),
												beforeActualWork.getTotalWorkingTime().getWorkTimes(),
												beforeActualWork.getTotalWorkingTime().getTemporaryTime(),
												beforeActualWork.getTotalWorkingTime().getShotrTimeOfDaily(),
												new HolidayOfDaily(
														beforeActualWork.getTotalWorkingTime().getHolidayOfDaily().getAbsence(), 
														beforeActualWork.getTotalWorkingTime().getHolidayOfDaily().getTimeDigest(), 
														new YearlyReservedOfDaily(new AttendanceTime(0)), 
														new SubstituteHolidayOfDaily(new AttendanceTime(0), 
																beforeActualWork.getTotalWorkingTime().getHolidayOfDaily().getSubstitute().getDigestionUseTime()), 
														beforeActualWork.getTotalWorkingTime().getHolidayOfDaily().getOverSalary(), 
														new SpecialHolidayOfDaily(new AttendanceTime(0), 
																beforeActualWork.getTotalWorkingTime().getHolidayOfDaily().getSpecialHoliday().getDigestionUseTime()), 
														new AnnualOfDaily(new AttendanceTime(0), 
																beforeActualWork.getTotalWorkingTime().getHolidayOfDaily().getAnnual().getDigestionUseTime()))),
										beforeActualWork.getDivTime(),
										beforeActualWork.getPremiumTimeOfDailyPerformance());
			
			return new AttendanceTimeOfDailyPerformance(atTime.getEmployeeId(), atTime.getYmd(), correctedSche, correctedActualWork, 
														atTime.getStayingTime(), atTime.getBudgetTimeVariance(), atTime.getUnEmployedTime()); 
		}
		
		return atTime;
	} 
	
	/**
	 * データ取得共通処理
	 * @param employeeId 社員ID
	 * @param period 期間
	 * @param repositories 月別集計が必要とするリポジトリ
	 */
	public void loadDataCommon(
			Require require,
			String employeeId,
			DatePeriod period,
			RepositoriesRequiredByMonthlyAggr repositories){
		
		List<String> employeeIds = new ArrayList<>();
		employeeIds.add(employeeId);
		
		// データ取得共通処理　（36協定時間用）
		this.loadDataCommonForAgreementRequire(require, employeeId, period, repositories);
		
		// 取得期間を　開始日-1月～終了日+1月　とする　（前月の最終週、36協定締め日違いの集計のため）
		DatePeriod findPeriod = new DatePeriod(period.start().addMonths(-1), period.end().addDays(31));
		
		// 日別実績の出退勤
		val timeLeaveOfDailyList = require.findTimeLeavingbyPeriodOrderByYmd(employeeId, findPeriod);
		for (val timeLeaveOfDaily : timeLeaveOfDailyList){
			this.timeLeaveOfDailyMap.putIfAbsent(timeLeaveOfDaily.getYmd(), timeLeaveOfDaily);
		}
		
		// ※　以下は、期間外の配慮不要。

		// 日別実績の臨時出退勤
		val temporaryTimeOfDailys = require.findTemporarybyPeriodOrderByYmd(employeeId, period);
		for (val temporaryTimeOfDaily : temporaryTimeOfDailys){
			val ymd = temporaryTimeOfDaily.getYmd();
			this.temporaryTimeOfDailyMap.putIfAbsent(ymd, temporaryTimeOfDaily);
		}
		
		// 日別実績の特定日区分
		val specificDateAttrOfDailys = require.findSpecificDateByPeriodOrderByYmd(employeeId, period);
		for (val specificDateAttrOfDaily : specificDateAttrOfDailys){
			val ymd = specificDateAttrOfDaily.getYmd();
			this.specificDateAttrOfDailyMap.putIfAbsent(ymd, specificDateAttrOfDaily);
		}
		
		// 社員の日別実績エラー一覧
		this.employeeDailyPerErrorList = require.findEmployeeDailyPerErrorByPeriodOrderByYmd(employeeId, period);
		val itrPerError = this.employeeDailyPerErrorList.listIterator();
		while (itrPerError.hasNext()){
			val perError = itrPerError.next();
			if (perError == null) itrPerError.remove();
		}
		
		// 日別実績の任意項目
		this.anyItemValueOfDailyList = require.findsAnyItemValue(employeeIds, period);
		
		// PCログオン情報
		val pcLogonInfos = require.findsPcLongOnInfo(employeeIds, period);
		for (val pcLogonInfo : pcLogonInfos){
			val ymd = pcLogonInfo.getYmd();
			this.pcLogonInfoMap.putIfAbsent(ymd, pcLogonInfo);
		}
		
		// 年休付与残数データリスト
		this.grantRemainingDatas = require.findNotExp(employeeId).stream()
						.map(c -> new AnnualLeaveGrantRemaining(c)).collect(Collectors.toList());
		
		// 積立年休付与残数データリスト
		this.rsvGrantRemainingDatas = require.findNotExp(employeeId, null).stream()
						.map(c -> new ReserveLeaveGrantRemaining(c)).collect(Collectors.toList());
		
		// 日別実績の勤務種別
		val workTypes = require.finds(employeeIds, period);
		for (val workType : workTypes){
			val ymd = workType.getDate();
			this.workTypeOfDailyMap.putIfAbsent(ymd, workType);
		}
	}
	
	/**
	 * データ取得　（36協定時間用）
	 * @param employeeId 社員ID
	 * @param period 期間
	 * @param repositories 月別集計が必要とするリポジトリ
	 * @return 月の計算中の日別実績データ
	 */
	public static MonthlyCalculatingDailys loadDataForAgreement(
			String employeeId,
			DatePeriod period,
			RepositoriesRequiredByMonthlyAggr repositories){
		
		MonthlyCalculatingDailys result = new MonthlyCalculatingDailys();
		
		// 取得期間を　開始日-1月～終了日+1月　とする　（前月の最終週、36協定締め日違いの集計のため）
		DatePeriod findPeriod = new DatePeriod(period.start().addMonths(-1), period.end().addDays(31));
		
		// 日別実績の勤怠時間
		val attendanceTimeOfDailyList =
				repositories.getAttendanceTimeOfDaily().findByPeriodOrderByYmd(employeeId, findPeriod);
		for (val attendanceTimeOfDaily : attendanceTimeOfDailyList){
			result.attendanceTimeOfDailyMap.putIfAbsent(attendanceTimeOfDaily.getYmd(), attendanceTimeOfDaily);
		}
		
		// データ取得共通処理　（36協定時間用）
		result.loadDataCommonForAgreement(employeeId, period, repositories);
		
		return correctExamDayTime(result, repositories);
	}
	
	/**
	 * データ取得　（36協定時間用）
	 * @param employeeId 社員ID
	 * @param period 期間
	 * @param attendanceTimeOfDailys 日別実績の勤怠時間リスト
	 * @param repositories 月別集計が必要とするリポジトリ
	 * @return 月の計算中の日別実績データ
	 */
	public static MonthlyCalculatingDailys loadDataForAgreement(
			String employeeId,
			DatePeriod period,
			List<AttendanceTimeOfDailyPerformance> attendanceTimeOfDailys,
			RepositoriesRequiredByMonthlyAggr repositories){
		
		MonthlyCalculatingDailys result = new MonthlyCalculatingDailys();
		
		// 取得期間を　開始日-1月～終了日+1月　とする　（前月の最終週、36協定締め日違いの集計のため）
		DatePeriod findPeriod = new DatePeriod(period.start().addMonths(-1), period.end().addDays(31));
		
		// 日別実績の勤怠時間
		val attendanceTimeOfDailyList =
				repositories.getAttendanceTimeOfDaily().findByPeriodOrderByYmd(employeeId, findPeriod);
		for (val attendanceTimeOfDaily : attendanceTimeOfDailyList){
			result.attendanceTimeOfDailyMap.putIfAbsent(attendanceTimeOfDaily.getYmd(), attendanceTimeOfDaily);
		}

		// 指定の日別実績の勤怠時間リストを上書き反映する
		for (val attendanceTimeOfDaily : attendanceTimeOfDailys){
			result.attendanceTimeOfDailyMap.put(attendanceTimeOfDaily.getYmd(), attendanceTimeOfDaily);
		}
		
		// データ取得共通処理　（36協定時間用）
		result.loadDataCommonForAgreement(employeeId, period, repositories);
		
		return correctExamDayTime(result, repositories);
	}
	
	/**
	 * データ取得共通処理　（36協定時間用）
	 * @param employeeId 社員ID
	 * @param period 期間
	 * @param repositories 月別集計が必要とするリポジトリ
	 */
	public void loadDataCommonForAgreement(
			String employeeId,
			DatePeriod period,
			RepositoriesRequiredByMonthlyAggr repositories){
		loadDataCommonForAgreementRequire(createRequireImpl(repositories), employeeId, period, repositories);
	}
	
	private void loadDataCommonForAgreementRequire(
			Require require,
			String employeeId,
			DatePeriod period,
			RepositoriesRequiredByMonthlyAggr repositories){
		
		// 取得期間を　開始日-1月～終了日+1月　とする　（前月の最終週、36協定締め日違いの集計のため）
		DatePeriod findPeriod = new DatePeriod(period.start().addMonths(-1), period.end().addDays(31));
		
		// 日別実績の勤務情報
		val workInfoOfDailyList = require.findWorkInfoByPeriodOrderByYmd(employeeId, findPeriod);
		for (val workInfoOfDaily : workInfoOfDailyList){
			this.workInfoOfDailyMap.putIfAbsent(workInfoOfDaily.getYmd(), workInfoOfDaily);
		}
	}
	
	/**
	 * 期間内の日別実績の勤怠時間が存在するか
	 * @param period 期間
	 * @return true：存在する、false：存在しない
	 */
	public boolean isExistDailyRecord(DatePeriod period){
	
		for (val attendanceTimeOfDaily : this.attendanceTimeOfDailyMap.values()){
			val ymd = attendanceTimeOfDaily.getYmd();
			if (!period.contains(ymd)) continue;
			return true;
		}
		return false;
	}
	
	public static interface Require{
//		repositories.getAttendanceTimeOfDaily().findByPeriodOrderByYmd(employeeId, findPeriod);
		List<AttendanceTimeOfDailyPerformance> findAttendanceTimeByPeriodOrderByYmd(String employeeId, DatePeriod datePeriod);
//		repositories.getWorkInformationOfDaily().findByPeriodOrderByYmd(employeeId, findPeriod);
		List<WorkInfoOfDailyPerformance> findWorkInfoByPeriodOrderByYmd(String employeeId, DatePeriod datePeriod);
//		repositories.getWorkingConditionItem().getBySidAndStandardDate(wi.getEmployeeId(), currentDate);
		Optional<WorkingConditionItem> getBySidAndStandardDate(String employeeId, GeneralDate baseDate);
//		repositories.getTimeLeavingOfDaily().findbyPeriodOrderByYmd(employeeId, findPeriod);
		List<TimeLeavingOfDailyPerformance> findTimeLeavingbyPeriodOrderByYmd(String employeeId, DatePeriod datePeriod);
//		repositories.getTemporaryTimeOfDaily().findbyPeriodOrderByYmd(employeeId, period);
		List<TemporaryTimeOfDailyPerformance> findTemporarybyPeriodOrderByYmd(String employeeId, DatePeriod datePeriod);
//		repositories.getSpecificDateAttrOfDaily().findByPeriodOrderByYmd(employeeId, period);
		List<SpecificDateAttrOfDailyPerfor> findSpecificDateByPeriodOrderByYmd(String employeeId, DatePeriod datePeriod);
//		repositories.getEmployeeDailyError().findByPeriodOrderByYmd(employeeId, period);
		List<EmployeeDailyPerError> findEmployeeDailyPerErrorByPeriodOrderByYmd(String employeeId, DatePeriod datePeriod);
//		repositories.getAnyItemValueOfDaily().finds(employeeIds, period);
		List<AnyItemValueOfDaily> findsAnyItemValue(List<String> employeeId, DatePeriod baseDate);
//		repositories.getPCLogonInfoOfDaily().finds(employeeIds, period);
		List<PCLogOnInfoOfDaily> findsPcLongOnInfo(List<String> employeeId, DatePeriod baseDate);
//		repositories.getAnnLeaGrantRemData().findNotExp(employeeId).stream()
		List<AnnualLeaveGrantRemainingData> findNotExp(String employeeId);
//		repositories.getRsvLeaGrantRemData().findNotExp(employeeId, null)
		List<ReserveLeaveGrantRemainingData> findNotExp(String employeeId, String cId);
//		repositories.getWorkTypeOfDaily().finds(employeeIds, period);
		List<WorkTypeOfDailyPerformance> finds(List<String> employeeId, DatePeriod baseDate);
	}

	private static Require createRequireImpl(RepositoriesRequiredByMonthlyAggr repositories) {
		return new MonthlyCalculatingDailys.Require() {
			@Override
			public Optional<WorkingConditionItem> getBySidAndStandardDate(String employeeId, GeneralDate baseDate) {
				return repositories.getWorkingConditionItem().getBySidAndStandardDate(employeeId, baseDate);
			}
			@Override
			public List<PCLogOnInfoOfDaily> findsPcLongOnInfo(List<String> employeeIds, DatePeriod baseDate) {
				return repositories.getPCLogonInfoOfDaily().finds(employeeIds, baseDate);
			}
			@Override
			public List<AnyItemValueOfDaily> findsAnyItemValue(List<String> employeeIds, DatePeriod baseDate) {
				return repositories.getAnyItemValueOfDaily().finds(employeeIds, baseDate);
			}
			@Override
			public List<WorkTypeOfDailyPerformance> finds(List<String> employeeIds, DatePeriod baseDate) {
				return repositories.getWorkTypeOfDaily().finds(employeeIds, baseDate);
			}
			@Override
			public List<WorkInfoOfDailyPerformance> findWorkInfoByPeriodOrderByYmd(String employeeId, DatePeriod datePeriod) {
				return repositories.getWorkInformationOfDaily().findByPeriodOrderByYmd(employeeId, datePeriod);
			}
			@Override
			public List<TimeLeavingOfDailyPerformance> findTimeLeavingbyPeriodOrderByYmd(String employeeId,
					DatePeriod datePeriod) {
				return repositories.getTimeLeavingOfDaily().findbyPeriodOrderByYmd(employeeId, datePeriod);
			}
			@Override
			public List<TemporaryTimeOfDailyPerformance> findTemporarybyPeriodOrderByYmd(String employeeId,DatePeriod datePeriod) {
				return repositories.getTemporaryTimeOfDaily().findbyPeriodOrderByYmd(employeeId, datePeriod);
			}
			@Override
			public List<SpecificDateAttrOfDailyPerfor> findSpecificDateByPeriodOrderByYmd(String employeeId,DatePeriod datePeriod) {
				return repositories.getSpecificDateAttrOfDaily().findByPeriodOrderByYmd(employeeId, datePeriod);
			}
			@Override
			public List<ReserveLeaveGrantRemainingData> findNotExp(String employeeId, String cId) {
				return repositories.getRsvLeaGrantRemData().findNotExp(employeeId, null);
			}
			@Override
			public List<AnnualLeaveGrantRemainingData> findNotExp(String employeeId) {
				return repositories.getAnnLeaGrantRemData().findNotExp(employeeId);
			}
			@Override
			public List<EmployeeDailyPerError> findEmployeeDailyPerErrorByPeriodOrderByYmd(String employeeId, DatePeriod datePeriod) {
				return repositories.getEmployeeDailyError().findByPeriodOrderByYmd(employeeId, datePeriod);
			}
			@Override
			public List<AttendanceTimeOfDailyPerformance> findAttendanceTimeByPeriodOrderByYmd(String employeeId,DatePeriod datePeriod) {
				return repositories.getAttendanceTimeOfDaily().findByPeriodOrderByYmd(employeeId, datePeriod);
			}
		};
	}
}