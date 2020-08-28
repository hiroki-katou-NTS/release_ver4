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
import nts.uk.ctx.at.record.dom.actualworkinghours.AttendanceTimeOfDailyPerformance;
import nts.uk.ctx.at.record.dom.daily.attendanceleavinggate.PCLogOnInfoOfDaily;
import nts.uk.ctx.at.record.dom.daily.optionalitemtime.AnyItemValueOfDaily;
import nts.uk.ctx.at.record.dom.raisesalarytime.SpecificDateAttrOfDailyPerfor;
import nts.uk.ctx.at.record.dom.remainingnumber.annualleave.export.param.AnnualLeaveGrantRemaining;
import nts.uk.ctx.at.record.dom.remainingnumber.reserveleave.export.param.ReserveLeaveGrantRemaining;
import nts.uk.ctx.at.record.dom.workinformation.WorkInfoOfDailyPerformance;
import nts.uk.ctx.at.record.dom.worktime.TemporaryTimeOfDailyPerformance;
import nts.uk.ctx.at.record.dom.worktime.TimeLeavingOfDailyPerformance;
import nts.uk.ctx.at.shared.dom.affiliationinformation.WorkTypeOfDailyPerformance;
import nts.uk.ctx.at.shared.dom.common.time.AttendanceTime;
import nts.uk.ctx.at.shared.dom.dailyattdcal.dailyattendance.dailyattendancework.IntegrationOfDaily;
import nts.uk.ctx.at.shared.dom.dailyattdcal.dailyattendance.erroralarm.EmployeeDailyPerError;
import nts.uk.ctx.at.shared.dom.dailyattdcal.dailyattendance.workschedule.WorkScheduleTimeOfDaily;
import nts.uk.ctx.at.shared.dom.dailyattdcal.dailyattendance.worktime.ActualWorkingTimeOfDaily;
import nts.uk.ctx.at.shared.dom.dailyattdcal.dailyattendance.worktime.TotalWorkingTime;
import nts.uk.ctx.at.shared.dom.dailyattdcal.dailycalprocess.calculation.other.WithinStatutoryTimeOfDaily;
import nts.uk.ctx.at.shared.dom.dailyattdcal.dailycalprocess.calculation.other.vacationusetime.AnnualOfDaily;
import nts.uk.ctx.at.shared.dom.dailyattdcal.dailycalprocess.calculation.other.vacationusetime.HolidayOfDaily;
import nts.uk.ctx.at.shared.dom.dailyattdcal.dailycalprocess.calculation.other.vacationusetime.SpecialHolidayOfDaily;
import nts.uk.ctx.at.shared.dom.dailyattdcal.dailycalprocess.calculation.other.vacationusetime.SubstituteHolidayOfDaily;
import nts.uk.ctx.at.shared.dom.dailyattdcal.dailycalprocess.calculation.other.vacationusetime.YearlyReservedOfDaily;
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
	
	private Map<GeneralDate, WorkingConditionItem> workConditions;
	
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
		this.workConditions = new HashMap<>();
	}
	
	/**
	 * データ取得
	 * @param employeeId 社員ID
	 * @param period 期間
	 * @param repositories 月別集計が必要とするリポジトリ
	 * @return 月の計算中の日別実績データ
	 */
	public static MonthlyCalculatingDailys loadData(RequireM4 require, String employeeId,
			DatePeriod period, MonAggrEmployeeSettings settings){
		
		MonthlyCalculatingDailys result = new MonthlyCalculatingDailys();
		
		// 取得期間を　開始日-1月～終了日+1月　とする　（前月の最終週、36協定締め日違いの集計のため）
		DatePeriod findPeriod = new DatePeriod(period.start().addMonths(-1), period.end().addDays(31));
		
		// 日別実績の勤怠時間
		val attendanceTimeOfDailyList = require.dailyAttendanceTimes(employeeId, findPeriod);
		for (val attendanceTimeOfDaily : attendanceTimeOfDailyList){
			result.attendanceTimeOfDailyMap.putIfAbsent(attendanceTimeOfDaily.getYmd(), attendanceTimeOfDaily);
		}
		
		// 共通処理
		result.loadDataCommon(require, employeeId, period, settings);
		
		return correctExamDayTime(result, settings);
	}
	
	/**
	 * データ取得
	 * @param employeeId 社員ID
	 * @param period 期間
	 * @param attendanceTimeOfDailys 日別実績の勤怠時間リスト
	 * @return 月の計算中の日別実績データ
	 */
	public static MonthlyCalculatingDailys loadData(RequireM4 require, String employeeId, DatePeriod period,
			List<AttendanceTimeOfDailyPerformance> attendanceTimeOfDailys, MonAggrEmployeeSettings settings){
		
		// 期間内の全データ読み込み
		MonthlyCalculatingDailys result = MonthlyCalculatingDailys.loadData(require, employeeId, period, settings);
		
		// 日別実績の勤怠時間リストの指定がない時、終了
		if (attendanceTimeOfDailys.size() <= 0) return result;
		
		// 日別実績の勤怠時間を反映する
		for (val attendanceTimeOfDaily : attendanceTimeOfDailys){
			val ymd = attendanceTimeOfDaily.getYmd();
			result.attendanceTimeOfDailyMap.put(ymd, attendanceTimeOfDaily);
		}
		
		return correctExamDayTime(result, settings);
	}
	
	/**
	 * データ取得
	 * @param employeeId 社員ID
	 * @param period 期間
	 * @param dailyWorksOpt 日別実績(WORK)リスト
	 * @return 月の計算中の日別実績データ
	 */
	public static MonthlyCalculatingDailys loadData(RequireM4 require, String employeeId, DatePeriod period,
			Optional<List<IntegrationOfDaily>> dailyWorksOpt, MonAggrEmployeeSettings settings){
		
		// 期間内の全データ読み込み
		MonthlyCalculatingDailys result = MonthlyCalculatingDailys.loadData(require, employeeId, period, settings);

		// 日別実績(WORK)指定がない時、終了
		if (!dailyWorksOpt.isPresent()) return result;
		
		for (val dailyWork : dailyWorksOpt.get()){
			
			// 日別実績の勤怠時間がない日は、反映しない
			if (!dailyWork.getAttendanceTimeOfDailyPerformance().isPresent()) continue;
			
			// 日別実績の勤怠時間
			val attendanceTimeOfDaily = dailyWork.getAttendanceTimeOfDailyPerformance().get();
			val ymd = dailyWork.getYmd();
			AttendanceTimeOfDailyPerformance dailyPerformance = new AttendanceTimeOfDailyPerformance(employeeId, ymd, attendanceTimeOfDaily);
			result.attendanceTimeOfDailyMap.put(ymd, dailyPerformance);
			
			// 日別実績の出退勤
			if (dailyWork.getAttendanceLeave().isPresent()){
				val timeLeaveOfDaily = dailyWork.getAttendanceLeave().get();
				TimeLeavingOfDailyPerformance performance = new TimeLeavingOfDailyPerformance(employeeId, ymd, timeLeaveOfDaily);
				result.timeLeaveOfDailyMap.put(ymd, performance);
			}
			
			// 日別実績の勤務情報
			if (dailyWork.getWorkInformation() != null){
				val workInfoOfDaily = dailyWork.getWorkInformation();
				WorkInfoOfDailyPerformance performance = new WorkInfoOfDailyPerformance(employeeId, ymd, workInfoOfDaily);
				result.workInfoOfDailyMap.put(ymd, performance);
			}
			
			// 日別実績の臨時出退勤
			if (dailyWork.getTempTime().isPresent()){
				val temporaryTimeOfDaily = dailyWork.getTempTime().get();
				TemporaryTimeOfDailyPerformance performance = new TemporaryTimeOfDailyPerformance(employeeId, ymd, temporaryTimeOfDaily);
				result.temporaryTimeOfDailyMap.put(ymd, performance);
			}
			
			// 日別実績の特定日区分
			if (dailyWork.getSpecDateAttr().isPresent()){
				val specificDateAttrOfDaily = dailyWork.getSpecDateAttr().get();
				SpecificDateAttrOfDailyPerfor ofDailyPerfor = new SpecificDateAttrOfDailyPerfor(employeeId, ymd, specificDateAttrOfDaily);
				result.specificDateAttrOfDailyMap.put(ymd, ofDailyPerfor);
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
				AnyItemValueOfDaily daily = new AnyItemValueOfDaily(employeeId, ymd, dailyWork.getAnyItemValue().get());
				result.anyItemValueOfDailyList.add(daily);
			}
			
			// PCログオン情報
			if (dailyWork.getPcLogOnInfo().isPresent()){
				val pcLogonInfo = dailyWork.getPcLogOnInfo().get();
				PCLogOnInfoOfDaily infoOfDaily = new PCLogOnInfoOfDaily(employeeId, ymd, pcLogonInfo);
				result.pcLogonInfoMap.put(ymd, infoOfDaily);
			}
			
			// 日別実績の勤務種別
			// Không còn domain này trong EA
//			if (dailyWork.getBusinessType().isPresent()) {
//				val workType = dailyWork.getBusinessType().get();
//				result.workTypeOfDailyMap.put(workType.getDate(), workType);
//			}
		}
		
		return correctExamDayTime(result, settings);

	}
	
	private static MonthlyCalculatingDailys correctExamDayTime(MonthlyCalculatingDailys calcDaily, 
			MonAggrEmployeeSettings settings){
		
		
		for (Entry<GeneralDate, AttendanceTimeOfDailyPerformance> dailyAttenTime : calcDaily.attendanceTimeOfDailyMap.entrySet()) {
			
			GeneralDate currentDate = dailyAttenTime.getKey();
			
			if (calcDaily.workInfoOfDailyMap.containsKey(currentDate)) {
				WorkInfoOfDailyPerformance wi = calcDaily.workInfoOfDailyMap.get(currentDate);
				
				settings.getWorkingConditions().entrySet().stream()
				.filter(wc -> wc.getValue().contains(wi.getYmd()))
				.findFirst().flatMap(wc -> settings.getWorkingConditionItems().stream()
														.filter(wci -> wci.getHistoryId().equals(wc))
														.map(wci -> wci.getLaborSystem())
														.findFirst())
				.ifPresent(laborSystem -> {
					if (laborSystem == WorkingSystem.FLEX_TIME_WORK) {
						calcDaily.attendanceTimeOfDailyMap.put(currentDate, 
								examDayTimeCorrect(dailyAttenTime.getValue(), wi));
					}
				});
			}
		}
		
		return calcDaily;
	}
	
	/**
	 * 大塚カスタマイズ（試験日対応）
	 */
	private static AttendanceTimeOfDailyPerformance examDayTimeCorrect(AttendanceTimeOfDailyPerformance atTime,
			WorkInfoOfDailyPerformance workInfo) {
		
		if (workInfo.getWorkInformation().getRecordInfo().isExamWorkTime()) {
			
			WorkScheduleTimeOfDaily correctedSche = new WorkScheduleTimeOfDaily(atTime.getTime().getWorkScheduleTimeOfDaily().getWorkScheduleTime(),
																				new AttendanceTime(0), 
																				atTime.getTime().getWorkScheduleTimeOfDaily().getRecordPrescribedLaborTime());
			
			ActualWorkingTimeOfDaily beforeActualWork = atTime.getTime().getActualWorkingTimeOfDaily();
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
														atTime.getTime().getStayingTime(), atTime.getTime().getBudgetTimeVariance(), 
														atTime.getTime().getUnEmployedTime()); 
		}
		
		return atTime;
	} 
	
	/**
	 * データ取得共通処理
	 * @param employeeId 社員ID
	 * @param period 期間
	 */
	public void loadDataCommon(RequireM1 require, String employeeId, DatePeriod period, MonAggrEmployeeSettings settings){
		
		List<String> employeeIds = new ArrayList<>();
		employeeIds.add(employeeId);
		
		// データ取得共通処理　（36協定時間用）
		this.loadDataCommonForAgreement(require, employeeId, period);
		
		// 取得期間を　開始日-1月～終了日+1月　とする　（前月の最終週、36協定締め日違いの集計のため）
		DatePeriod findPeriod = new DatePeriod(period.start().addMonths(-1), period.end().addDays(31));
		
		// 日別実績の出退勤
		val timeLeaveOfDailyList = require.dailyTimeLeavings(employeeId, findPeriod);
		for (val timeLeaveOfDaily : timeLeaveOfDailyList){
			this.timeLeaveOfDailyMap.putIfAbsent(timeLeaveOfDaily.getYmd(), timeLeaveOfDaily);
		}
		
		// ※　以下は、期間外の配慮不要。

		// 日別実績の臨時出退勤
		val temporaryTimeOfDailys = require.dailyTemporaryTimes(employeeId, period);
		for (val temporaryTimeOfDaily : temporaryTimeOfDailys){
			val ymd = temporaryTimeOfDaily.getYmd();
			this.temporaryTimeOfDailyMap.putIfAbsent(ymd, temporaryTimeOfDaily);
		}
		
		// 日別実績の特定日区分
		val specificDateAttrOfDailys = require.dailySpecificDates(employeeId, period);
		for (val specificDateAttrOfDaily : specificDateAttrOfDailys){
			val ymd = specificDateAttrOfDaily.getYmd();
			this.specificDateAttrOfDailyMap.putIfAbsent(ymd, specificDateAttrOfDaily);
		}
		
		// 社員の日別実績エラー一覧
		this.employeeDailyPerErrorList = require.dailyEmpErrors(employeeId, period);
		val itrPerError = this.employeeDailyPerErrorList.listIterator();
		while (itrPerError.hasNext()){
			val perError = itrPerError.next();
			if (perError == null) itrPerError.remove();
		}
		
		// 日別実績の任意項目
		this.anyItemValueOfDailyList = require.dailyAnyItems(employeeIds, period);
		
		// PCログオン情報
		val pcLogonInfos = require.dailyPcLogons(employeeIds, period);
		for (val pcLogonInfo : pcLogonInfos){
			val ymd = pcLogonInfo.getYmd();
			this.pcLogonInfoMap.putIfAbsent(ymd, pcLogonInfo);
		}
		
		// 年休付与残数データリスト
		this.grantRemainingDatas = require.annualLeaveGrantRemainingData(employeeId).stream()
						.map(c -> new AnnualLeaveGrantRemaining(c)).collect(Collectors.toList());
		
		// 積立年休付与残数データリスト
		this.rsvGrantRemainingDatas = require.reserveLeaveGrantRemainingData(employeeId, null).stream()
						.map(c -> new ReserveLeaveGrantRemaining(c)).collect(Collectors.toList());
		
		// 日別実績の勤務種別
		val workTypes = require.dailyWorkTypes(employeeIds, period);
		for (val workType : workTypes){
			val ymd = workType.getDate();
			this.workTypeOfDailyMap.putIfAbsent(ymd, workType);
		}
	}
	
	/**
	 * データ取得　（36協定時間用）
	 * @param employeeId 社員ID
	 * @param period 期間
	 * @return 月の計算中の日別実績データ
	 */
	public static MonthlyCalculatingDailys loadDataForAgreement(RequireM3 require,
			String employeeId, DatePeriod period, MonAggrEmployeeSettings settings){
		
		MonthlyCalculatingDailys result = new MonthlyCalculatingDailys();
		
		// 取得期間を　開始日-1月～終了日+1月　とする　（前月の最終週、36協定締め日違いの集計のため）
		DatePeriod findPeriod = new DatePeriod(period.start().addMonths(-1), period.end().addDays(31));
		
		// 日別実績の勤怠時間
		val attendanceTimeOfDailyList = require.dailyAttendanceTimes(employeeId, findPeriod);
		for (val attendanceTimeOfDaily : attendanceTimeOfDailyList){
			result.attendanceTimeOfDailyMap.putIfAbsent(attendanceTimeOfDaily.getYmd(), attendanceTimeOfDaily);
		}
		
		// データ取得共通処理　（36協定時間用）
		result.loadDataCommonForAgreement(require, employeeId, period);
		
		return correctExamDayTime(result, settings);
	}
	
	/**
	 * データ取得　（36協定時間用）
	 * @param employeeId 社員ID
	 * @param period 期間
	 * @param attendanceTimeOfDailys 日別実績の勤怠時間リスト
	 * @return 月の計算中の日別実績データ
	 */
	public static MonthlyCalculatingDailys loadDataForAgreement(RequireM3 require, String employeeId,
			DatePeriod period, List<AttendanceTimeOfDailyPerformance> attendanceTimeOfDailys,
			MonAggrEmployeeSettings settings){
		
		MonthlyCalculatingDailys result = new MonthlyCalculatingDailys();
		
		// 取得期間を　開始日-1月～終了日+1月　とする　（前月の最終週、36協定締め日違いの集計のため）
		DatePeriod findPeriod = new DatePeriod(period.start().addMonths(-1), period.end().addDays(31));
		
		// 日別実績の勤怠時間
		val attendanceTimeOfDailyList = require.dailyAttendanceTimes(employeeId, findPeriod);
		for (val attendanceTimeOfDaily : attendanceTimeOfDailyList){
			result.attendanceTimeOfDailyMap.putIfAbsent(attendanceTimeOfDaily.getYmd(), attendanceTimeOfDaily);
		}

		// 指定の日別実績の勤怠時間リストを上書き反映する
		for (val attendanceTimeOfDaily : attendanceTimeOfDailys){
			result.attendanceTimeOfDailyMap.put(attendanceTimeOfDaily.getYmd(), attendanceTimeOfDaily);
		}
		
		// データ取得共通処理　（36協定時間用）
		result.loadDataCommonForAgreement(require, employeeId, period);
		
		return correctExamDayTime(result, settings);
	}
	
	/**
	 * データ取得共通処理　（36協定時間用）
	 * @param employeeId 社員ID
	 * @param period 期間
	 */
	public void loadDataCommonForAgreement(RequireM2 require, String employeeId, DatePeriod period){
		
		// 取得期間を　開始日-1月～終了日+1月　とする　（前月の最終週、36協定締め日違いの集計のため）
		DatePeriod findPeriod = new DatePeriod(period.start().addMonths(-1), period.end().addDays(31));
		
		// 日別実績の勤務情報
		val workInfoOfDailyList = require.dailyWorkInfos(employeeId, findPeriod);
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

	public static interface RequireM3 extends RequireM2 {

		List<AttendanceTimeOfDailyPerformance> dailyAttendanceTimes(String employeeId, DatePeriod datePeriod);
	}
	
	public static interface RequireM2 {
		
		List<WorkInfoOfDailyPerformance> dailyWorkInfos(String employeeId, DatePeriod datePeriod);
	}
	
	public static interface RequireM4 extends RequireM1, RequireM3 {}
	
	public static interface RequireM1 extends RequireM2 {
		
		List<TimeLeavingOfDailyPerformance> dailyTimeLeavings(String employeeId, DatePeriod datePeriod);
		
		List<TemporaryTimeOfDailyPerformance> dailyTemporaryTimes(String employeeId, DatePeriod datePeriod);
		
		List<SpecificDateAttrOfDailyPerfor> dailySpecificDates(String employeeId, DatePeriod datePeriod);
		
		List<EmployeeDailyPerError> dailyEmpErrors(String employeeId, DatePeriod datePeriod);
		
		List<AnyItemValueOfDaily> dailyAnyItems(List<String> employeeId, DatePeriod baseDate);
		
		List<PCLogOnInfoOfDaily> dailyPcLogons(List<String> employeeId, DatePeriod baseDate);
		
		List<AnnualLeaveGrantRemainingData> annualLeaveGrantRemainingData(String employeeId);
		
		List<ReserveLeaveGrantRemainingData> reserveLeaveGrantRemainingData(String employeeId, String cId);
		
		List<WorkTypeOfDailyPerformance> dailyWorkTypes(List<String> employeeId, DatePeriod baseDate);
	}
}