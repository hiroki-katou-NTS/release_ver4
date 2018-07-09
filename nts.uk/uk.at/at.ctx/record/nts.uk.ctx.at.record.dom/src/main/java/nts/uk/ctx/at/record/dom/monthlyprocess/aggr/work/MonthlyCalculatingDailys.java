package nts.uk.ctx.at.record.dom.monthlyprocess.aggr.work;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import lombok.Getter;
import lombok.Setter;
import lombok.val;
import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.record.dom.actualworkinghours.AttendanceTimeOfDailyPerformance;
import nts.uk.ctx.at.record.dom.daily.attendanceleavinggate.PCLogOnInfoOfDaily;
import nts.uk.ctx.at.record.dom.daily.optionalitemtime.AnyItemValueOfDaily;
import nts.uk.ctx.at.record.dom.raisesalarytime.SpecificDateAttrOfDailyPerfor;
import nts.uk.ctx.at.record.dom.remainingnumber.annualleave.export.param.AnnualLeaveGrantRemaining;
import nts.uk.ctx.at.record.dom.workinformation.WorkInfoOfDailyPerformance;
import nts.uk.ctx.at.record.dom.workrecord.erroralarm.EmployeeDailyPerError;
import nts.uk.ctx.at.record.dom.worktime.TemporaryTimeOfDailyPerformance;
import nts.uk.ctx.at.record.dom.worktime.TimeLeavingOfDailyPerformance;
import nts.uk.shr.com.time.calendar.period.DatePeriod;

/**
 * 月の計算中の日別実績データ
 * @author shuichu_ishida
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
		
		MonthlyCalculatingDailys result = new MonthlyCalculatingDailys();
		
		// 取得期間を　開始日-6日～終了日　とする　（前月の最終週の集計のため）
		DatePeriod findPeriod = new DatePeriod(period.start().addDays(-6), period.end());
		
		// 日別実績の勤怠時間
		val attendanceTimeOfDailyList =
				repositories.getAttendanceTimeOfDaily().findByPeriodOrderByYmd(employeeId, findPeriod);
		for (val attendanceTimeOfDaily : attendanceTimeOfDailyList){
			result.attendanceTimeOfDailyMap.putIfAbsent(attendanceTimeOfDaily.getYmd(), attendanceTimeOfDaily);
		}
		
		// 共通処理
		result.loadDataCommon(employeeId, period, repositories);
		
		return result;
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
		
		MonthlyCalculatingDailys result = new MonthlyCalculatingDailys();
		
		// 取得期間を　開始日-6日～開始日-1日　とする　（前月の最終週の集計のため）
		DatePeriod findPeriod = new DatePeriod(period.start().addDays(-6), period.start().addDays(-1));
		
		// 日別実績の勤怠時間　（前月最終週分）
		val findAttendanceTimeOfDailyList =
				repositories.getAttendanceTimeOfDaily().findByPeriodOrderByYmd(employeeId, findPeriod);
		for (val attendanceTimeOfDaily : findAttendanceTimeOfDailyList){
			result.attendanceTimeOfDailyMap.putIfAbsent(attendanceTimeOfDaily.getYmd(), attendanceTimeOfDaily);
		}
		
		// 日別実績の勤怠時間
		val attendanceTimeOfDailyList = attendanceTimeOfDailys;
		for (val attendanceTimeOfDaily : attendanceTimeOfDailyList){
			result.attendanceTimeOfDailyMap.putIfAbsent(attendanceTimeOfDaily.getYmd(), attendanceTimeOfDaily);
		}
		
		// 共通処理
		result.loadDataCommon(employeeId, period, repositories);
		
		return result;
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
		
		// 取得期間を　開始日-6日～終了日　とする　（前月の最終週の集計のため）
		DatePeriod findPeriod = new DatePeriod(period.start().addDays(-6), period.end());
		
		// 日別実績の勤怠時間
		val attendanceTimeOfDailyList =
				repositories.getAttendanceTimeOfDaily().findByPeriodOrderByYmd(employeeId, findPeriod);
		for (val attendanceTimeOfDaily : attendanceTimeOfDailyList){
			result.attendanceTimeOfDailyMap.putIfAbsent(attendanceTimeOfDaily.getYmd(), attendanceTimeOfDaily);
		}
		
		// データ取得共通処理　（36協定時間用）
		result.loadDataCommonForAgreement(employeeId, period, repositories);
		
		return result;
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
		
		// 日別実績の勤怠時間
		val attendanceTimeOfDailyList = attendanceTimeOfDailys;
		for (val attendanceTimeOfDaily : attendanceTimeOfDailyList){
			result.attendanceTimeOfDailyMap.putIfAbsent(attendanceTimeOfDaily.getYmd(), attendanceTimeOfDaily);
		}
		
		// データ取得共通処理　（36協定時間用）
		result.loadDataCommonForAgreement(employeeId, period, repositories);
		
		return result;
	}
	
	/**
	 * データ取得共通処理
	 * @param employeeId 社員ID
	 * @param period 期間
	 * @param repositories 月別集計が必要とするリポジトリ
	 */
	public void loadDataCommon(
			String employeeId,
			DatePeriod period,
			RepositoriesRequiredByMonthlyAggr repositories){
		
		List<String> employeeIds = new ArrayList<>();
		employeeIds.add(employeeId);
		
		// データ取得共通処理　（36協定時間用）
		this.loadDataCommonForAgreement(employeeId, period, repositories);
		
		// 取得期間を　開始日-6日～終了日　とする　（前月の最終週の集計のため）
		DatePeriod findPeriod = new DatePeriod(period.start().addDays(-6), period.end());
		
		// 日別実績の出退勤
		val timeLeaveOfDailyList =
				repositories.getTimeLeavingOfDaily().findbyPeriodOrderByYmd(employeeId, findPeriod);
		for (val timeLeaveOfDaily : timeLeaveOfDailyList){
			this.timeLeaveOfDailyMap.putIfAbsent(timeLeaveOfDaily.getYmd(), timeLeaveOfDaily);
		}
		
		// ※　以下は、期間の前月最終週の配慮不要。

		// 日別実績の臨時出退勤
		val temporaryTimeOfDailys =
				repositories.getTemporaryTimeOfDaily().findbyPeriodOrderByYmd(employeeId, period);
		for (val temporaryTimeOfDaily : temporaryTimeOfDailys){
			val ymd = temporaryTimeOfDaily.getYmd();
			this.temporaryTimeOfDailyMap.putIfAbsent(ymd, temporaryTimeOfDaily);
		}
		
		// 日別実績の特定日区分
		val specificDateAttrOfDailys =
				repositories.getSpecificDateAttrOfDaily().findByPeriodOrderByYmd(employeeId, period);
		for (val specificDateAttrOfDaily : specificDateAttrOfDailys){
			val ymd = specificDateAttrOfDaily.getYmd();
			this.specificDateAttrOfDailyMap.putIfAbsent(ymd, specificDateAttrOfDaily);
		}
		
		// 社員の日別実績エラー一覧
		this.employeeDailyPerErrorList =
				repositories.getEmployeeDailyError().findByPeriodOrderByYmd(employeeId, period);
		
		// 日別実績の任意項目
		this.anyItemValueOfDailyList = repositories.getAnyItemValueOfDaily().finds(employeeIds, period);
		
		// PCログオン情報
		val pcLogonInfos = repositories.getPCLogonInfoOfDaily().finds(employeeIds, period);
		for (val pcLogonInfo : pcLogonInfos){
			val ymd = pcLogonInfo.getYmd();
			this.pcLogonInfoMap.putIfAbsent(ymd, pcLogonInfo);
		}
		
		// 年休付与残数データリスト
		this.grantRemainingDatas =
				repositories.getAnnLeaGrantRemData().findNotExp(employeeId).stream()
						.map(c -> new AnnualLeaveGrantRemaining(c)).collect(Collectors.toList());
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
		
		// 取得期間を　開始日-6日～終了日　とする　（前月の最終週の集計のため）
		DatePeriod findPeriod = new DatePeriod(period.start().addDays(-6), period.end());
		
		// 日別実績の勤務情報
		val workInfoOfDailyList =
				repositories.getWorkInformationOfDaily().findByPeriodOrderByYmd(employeeId, findPeriod);
		for (val workInfoOfDaily : workInfoOfDailyList){
			this.workInfoOfDailyMap.putIfAbsent(workInfoOfDaily.getYmd(), workInfoOfDaily);
		}
	}
}
