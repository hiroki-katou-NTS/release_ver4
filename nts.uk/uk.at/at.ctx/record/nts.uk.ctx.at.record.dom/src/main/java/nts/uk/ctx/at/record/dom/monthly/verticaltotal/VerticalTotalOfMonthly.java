package nts.uk.ctx.at.record.dom.monthly.verticaltotal;

import java.util.HashMap;
import java.util.Map;

import lombok.Getter;
import lombok.val;
import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.record.dom.actualworkinghours.AttendanceTimeOfDailyPerformance;
import nts.uk.ctx.at.record.dom.monthly.WorkTypeDaysCountTable;
import nts.uk.ctx.at.record.dom.monthly.verticaltotal.workdays.WorkDaysOfMonthly;
import nts.uk.ctx.at.record.dom.monthly.verticaltotal.worktime.WorkTimeOfMonthly;
import nts.uk.ctx.at.record.dom.monthly.vtotalmethod.PayItemCountOfMonthly;
import nts.uk.ctx.at.record.dom.monthly.vtotalmethod.VerticalTotalMethodOfMonthly;
import nts.uk.ctx.at.record.dom.monthlyprocess.aggr.vtotalwork.AttendanceStatusMap;
import nts.uk.ctx.at.record.dom.monthlyprocess.aggr.work.RepositoriesRequiredByMonthlyAggr;
import nts.uk.ctx.at.record.dom.raisesalarytime.SpecificDateAttrOfDailyPerfor;
import nts.uk.ctx.at.record.dom.workinformation.WorkInfoOfDailyPerformance;
import nts.uk.ctx.at.record.dom.worktime.TemporaryTimeOfDailyPerformance;
import nts.uk.ctx.at.shared.dom.workingcondition.WorkingSystem;
import nts.uk.ctx.at.shared.dom.worktime.common.WorkTimeCode;
import nts.uk.ctx.at.shared.dom.worktime.predset.PredetemineTimeSetting;
import nts.uk.ctx.at.shared.dom.worktype.WorkType;
import nts.uk.ctx.at.shared.dom.worktype.WorkTypeCode;
import nts.uk.shr.com.time.calendar.period.DatePeriod;

/**
 * 月別実績の縦計
 * @author shuichu_ishida
 */
@Getter
public class VerticalTotalOfMonthly {
	
	/** 勤務日数 */
	private WorkDaysOfMonthly workDays;
	/** 勤務時間 */
	private WorkTimeOfMonthly workTime;
	
	/**
	 * コンストラクタ
	 */
	public VerticalTotalOfMonthly(){
		
		this.workDays = new WorkDaysOfMonthly();
		this.workTime = new WorkTimeOfMonthly();
	}
	
	/**
	 * ファクトリー
	 * @param workDays 勤務日数
	 * @param workTime 勤務時間
	 * @return 月別実績の縦計
	 */
	public static VerticalTotalOfMonthly of(
			WorkDaysOfMonthly workDays,
			WorkTimeOfMonthly workTime){
		
		val domain = new VerticalTotalOfMonthly();
		domain.workDays = workDays;
		domain.workTime = workTime;
		return domain;
	}
	
	/**
	 * 縦計
	 * @param companyId 会社ID
	 * @param employeeId 社員ID
	 * @param datePeriod 期間
	 * @param workingSystem 労働制
	 * @param repositories 月次集計が必要とするリポジトリ
	 */
	public void verticalTotal(
			String companyId,
			String employeeId,
			DatePeriod datePeriod,
			WorkingSystem workingSystem,
			RepositoriesRequiredByMonthlyAggr repositories){
		
		// 集計結果の初期化
		this.workTime = new WorkTimeOfMonthly();
		this.workDays = new WorkDaysOfMonthly();
		
		// 日別実績の勤務情報　取得
		val workInfoOfDailys =
				repositories.getWorkInformationOfDaily().findByPeriodOrderByYmd(employeeId, datePeriod);
		Map<GeneralDate, WorkInfoOfDailyPerformance> workInfoOfDailyMap = new HashMap<>();
		for (val workInfoOfDaily : workInfoOfDailys){
			val ymd = workInfoOfDaily.getYmd();
			workInfoOfDailyMap.putIfAbsent(ymd, workInfoOfDaily);
		}
		
		// 日別実績の勤怠時間　取得
		val attendanceTimeOfDailys =
				repositories.getAttendanceTimeOfDaily().findByPeriodOrderByYmd(employeeId, datePeriod);
		Map<GeneralDate, AttendanceTimeOfDailyPerformance> attendanceTimeOfDailyMap = new HashMap<>();
		for (val attendanceTimeOfDaily : attendanceTimeOfDailys){
			val ymd = attendanceTimeOfDaily.getYmd();
			attendanceTimeOfDailyMap.putIfAbsent(ymd, attendanceTimeOfDaily);
		}
		
		// 日別実績の出退勤　取得
		val timeLeaveingOfDailys =
				repositories.getTimeLeavingOfDaily().findbyPeriodOrderByYmd(employeeId, datePeriod);
		
		// 日別実績の臨時出退勤　取得
		val temporaryTimeOfDailys =
				repositories.getTemporaryTimeOfDaily().findbyPeriodOrderByYmd(employeeId, datePeriod);
		Map<GeneralDate, TemporaryTimeOfDailyPerformance> temporaryTimeOfDailyMap = new HashMap<>();
		for (val temporaryTimeOfDaily : temporaryTimeOfDailys){
			val ymd = temporaryTimeOfDaily.getYmd();
			temporaryTimeOfDailyMap.putIfAbsent(ymd, temporaryTimeOfDaily);
		}
		
		// 日別実績の特定日区分　取得
		val specificDateAttrOfDailys =
				repositories.getSpecificDateAttrOfDaily().findByPeriodOrderByYmd(employeeId, datePeriod);
		Map<GeneralDate, SpecificDateAttrOfDailyPerfor> specificDateAttrOfDailyMap = new HashMap<>();
		for (val specificDateAttrOfDaily : specificDateAttrOfDailys){
			val ymd = specificDateAttrOfDaily.getYmd();
			specificDateAttrOfDailyMap.putIfAbsent(ymd, specificDateAttrOfDaily);
		}
		
		// 勤務情報　取得
		val workTypes = repositories.getWorkType().findByCompanyId(companyId);
		Map<WorkTypeCode, WorkType> workTypeMap = new HashMap<>();
		for (val workType : workTypes){
			val workTypeCode = workType.getWorkTypeCode();
			workTypeMap.putIfAbsent(workTypeCode, workType);
		}
		
		// 所定時間設定　取得
		val predetermineTimeSets = repositories.getPredetermineTimeSet().findByCompanyID(companyId);
		Map<WorkTimeCode, PredetemineTimeSetting> predetermineTimeSetMap = new HashMap<>();
		for (val predetermineTimeSet : predetermineTimeSets){
			val workTimeCode = predetermineTimeSet.getWorkTimeCode();
			predetermineTimeSetMap.putIfAbsent(workTimeCode, predetermineTimeSet);
		}
		
		// 月別実績の縦計方法　取得
		//*****（未）　設計待ち。特定日設定部分が保留中。仮に空条件で。
		VerticalTotalMethodOfMonthly verticalTotalMethod = new VerticalTotalMethodOfMonthly(companyId);
		
		// 月別実績の給与項目カウント　取得
		PayItemCountOfMonthly payItemCount = new PayItemCountOfMonthly(companyId);
		val payItemCountOpt = repositories.getPayItemCountOfMonthly().find(companyId);
		if (payItemCountOpt.isPresent()) payItemCount = payItemCountOpt.get();
		
		// 休暇加算設定　取得
		val vacationAddSet = new VacationAddSet(companyId, repositories);
		
		// 出勤状態クラスの作成
		AttendanceStatusMap attendanceStatusMap = new AttendanceStatusMap(
				attendanceTimeOfDailys, timeLeaveingOfDailys);
		
		// 乖離フラグの集計
		//*****（未）　ドメインの構成、DB設計、リポジトリでのfindの管理単位が不整合かもしれない。確認要。
		//val employeeDailyPerErrors =
		//		repositories.getEmployeeDailyError().findByPeriodOrderByYmd(employeeId, datePeriod);
		//this.workTime.aggregateDivergenceAtr(employeeDailyPerErrors);
		
		// 日ごとのループ
		GeneralDate procYmd = datePeriod.start();
		while (procYmd.beforeOrEquals(datePeriod.end())){
			
			// 勤務情報・勤務種類・日数カウント表・所定時間設定を確認する
			WorkInfoOfDailyPerformance workInfoOfDaily = workInfoOfDailyMap.get(procYmd);
			WorkType workType = null;
			WorkTypeDaysCountTable workTypeDaysCountTable = null;
			PredetemineTimeSetting predetermineTimeSet = null;
			if (workInfoOfDaily != null){
				val recordWorkInfo = workInfoOfDaily.getRecordInfo();
				val workTypeCode = recordWorkInfo.getWorkTypeCode();
				val workTimeCode = recordWorkInfo.getWorkTimeCode();
				workType = workTypeMap.get(workTypeCode);
				if (workType != null){
					
					// 勤務種類を判断しカウント数を取得する
					workTypeDaysCountTable = new WorkTypeDaysCountTable(workType, vacationAddSet, verticalTotalMethod);
				}
				else {
					workInfoOfDaily = null;
				}
				
				// 所定時間設定の有無を確認する
				predetermineTimeSet = predetermineTimeSetMap.get(workTimeCode);
			}
			
			// 勤怠時間を確認する
			val attendanceTimeOfDaily = attendanceTimeOfDailyMap.get(procYmd);
			
			// 臨時出退勤を確認する
			val temporaryTimeOfDaily = temporaryTimeOfDailyMap.get(procYmd);
			
			// 特定日区分を確認する
			val specificDateAttrOfDaily = specificDateAttrOfDailyMap.get(procYmd);
			
			// 出勤状態・2回目打刻有無を確認する
			val isAttendanceDay = attendanceStatusMap.isAttendanceDay(procYmd);
			val isTwoTimesStampExists = attendanceStatusMap.isTwoTimesStampExists(procYmd);
		
			// 勤務日数集計
			this.workDays.aggregate(workingSystem, workType, attendanceTimeOfDaily, temporaryTimeOfDaily,
					specificDateAttrOfDaily,
					workTypeDaysCountTable, payItemCount, predetermineTimeSet,
					isAttendanceDay, isTwoTimesStampExists);
			
			// 勤務時間集計
			this.workTime.aggregate(workType, attendanceTimeOfDaily);
			
			// 処理期間の各週の開始日・終了日を判断
			
			// 週の回数分ループ
			
			// 週の集計
			
			
			procYmd = procYmd.addDays(1);
		}
	}
}
