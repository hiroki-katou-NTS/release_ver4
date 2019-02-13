package nts.uk.ctx.at.record.dom.monthly.verticaltotal;

import java.util.ArrayList;
import java.util.Map;
import java.util.Optional;

import lombok.Getter;
import lombok.val;
import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.record.dom.dailyprocess.calc.PredetermineTimeSetForCalc;
import nts.uk.ctx.at.record.dom.monthly.WorkTypeDaysCountTable;
import nts.uk.ctx.at.record.dom.monthly.verticaltotal.workclock.WorkClockOfMonthly;
import nts.uk.ctx.at.record.dom.monthly.verticaltotal.workdays.WorkDaysOfMonthly;
import nts.uk.ctx.at.record.dom.monthly.verticaltotal.worktime.WorkTimeOfMonthlyVT;
import nts.uk.ctx.at.record.dom.monthlyprocess.aggr.export.attdstatus.AttendanceStatusList;
import nts.uk.ctx.at.record.dom.monthlyprocess.aggr.work.MonAggrCompanySettings;
import nts.uk.ctx.at.record.dom.monthlyprocess.aggr.work.MonAggrEmployeeSettings;
import nts.uk.ctx.at.record.dom.monthlyprocess.aggr.work.MonthlyCalculatingDailys;
import nts.uk.ctx.at.record.dom.monthlyprocess.aggr.work.RepositoriesRequiredByMonthlyAggr;
import nts.uk.ctx.at.record.dom.workinformation.WorkInfoOfDailyPerformance;
import nts.uk.ctx.at.shared.dom.workingcondition.WorkingSystem;
import nts.uk.ctx.at.shared.dom.worktime.predset.PredetemineTimeSetting;
import nts.uk.ctx.at.shared.dom.worktype.WorkType;
import nts.uk.shr.com.time.calendar.period.DatePeriod;

/**
 * 期間別の縦計
 * @author shuichi_ishida
 */
@Getter
public class VerticalTotalOfMonthly {
	
	/** 勤務日数 */
	private WorkDaysOfMonthly workDays;
	/** 勤務時間 */
	private WorkTimeOfMonthlyVT workTime;
	/** 勤務時刻 */
	private WorkClockOfMonthly workClock;
	
	/**
	 * コンストラクタ
	 */
	public VerticalTotalOfMonthly(){
		
		this.workDays = new WorkDaysOfMonthly();
		this.workTime = new WorkTimeOfMonthlyVT();
		this.workClock = new WorkClockOfMonthly();
	}
	
	/**
	 * ファクトリー
	 * @param workDays 勤務日数
	 * @param workTime 勤務時間
	 * @param workClock 勤務時刻
	 * @return 月別実績の縦計
	 */
	public static VerticalTotalOfMonthly of(
			WorkDaysOfMonthly workDays,
			WorkTimeOfMonthlyVT workTime,
			WorkClockOfMonthly workClock){
		
		val domain = new VerticalTotalOfMonthly();
		domain.workDays = workDays;
		domain.workTime = workTime;
		domain.workClock = workClock;
		return domain;
	}
	
	/**
	 * 縦計
	 * @param companyId 会社ID
	 * @param employeeId 社員ID
	 * @param datePeriod 期間
	 * @param workingSystem 労働制
	 * @param companySets 月別集計で必要な会社別設定
	 * @param employeeSets 月別集計で必要な社員別設定
	 * @param monthlyCalcDailys 月の計算中の日別実績データ
	 * @param repositories 月次集計が必要とするリポジトリ
	 */
	public void verticalTotal(
			String companyId,
			String employeeId,
			DatePeriod datePeriod,
			WorkingSystem workingSystem,
			MonAggrCompanySettings companySets,
			MonAggrEmployeeSettings employeeSets,
			MonthlyCalculatingDailys monthlyCalcDailys,
			RepositoriesRequiredByMonthlyAggr repositories){
		
		// 集計結果の初期化
		this.workTime = new WorkTimeOfMonthlyVT();
		this.workDays = new WorkDaysOfMonthly();
		this.workClock = new WorkClockOfMonthly();
		
		// 日別実績の勤務情報　取得
		Map<GeneralDate, WorkInfoOfDailyPerformance> workInfoOfDailyMap =
				monthlyCalcDailys.getWorkInfoOfDailyMap();
		
		// 出勤状態を取得する
		AttendanceStatusList attendanceStatusList = new AttendanceStatusList(
				new ArrayList<>(monthlyCalcDailys.getAttendanceTimeOfDailyMap().values()),
				new ArrayList<>(monthlyCalcDailys.getTimeLeaveOfDailyMap().values()));
		
		// 乖離フラグの集計
		this.workTime.aggregateDivergenceAtr(monthlyCalcDailys.getEmployeeDailyPerErrorList());
		
		// 日ごとのループ
		GeneralDate procYmd = datePeriod.start();
		while (procYmd.beforeOrEquals(datePeriod.end())){
			
			// 勤務情報・勤務種類・日数カウント表・所定時間設定を確認する
			WorkInfoOfDailyPerformance workInfoOfDaily = workInfoOfDailyMap.get(procYmd);
			WorkType workType = null;
			WorkTypeDaysCountTable workTypeDaysCountTable = null;
			PredetemineTimeSetting predetermineTimeSet = null;
			PredetermineTimeSetForCalc predTimeSetForCalc = null;
			if (workInfoOfDaily != null){
				val recordWorkInfo = workInfoOfDaily.getRecordInfo();
				val workTypeCode = recordWorkInfo.getWorkTypeCode();
				val workTimeCode = recordWorkInfo.getWorkTimeCode();
				if (workTypeCode != null){
					workType = companySets.getWorkTypeMap(workTypeCode.v(), repositories);
					if (workType != null){
						
						// 勤務種類を判断しカウント数を取得する
						workTypeDaysCountTable = new WorkTypeDaysCountTable(workType,
								companySets.getVacationAddSet(),
								Optional.of(companySets.getVerticalTotalMethod()));
					}
					else {
						workInfoOfDaily = null;
					}
				}
				else {
					workInfoOfDaily = null;
				}
				
				// 計算用所定時間設定を確認する
				if (workTimeCode != null){
					predetermineTimeSet = companySets.getPredetemineTimeSetMap(workTimeCode.v(), repositories);
					if (predetermineTimeSet != null){
						predTimeSetForCalc = PredetermineTimeSetForCalc.convertMastarToCalc(predetermineTimeSet);
					}
				}
			}
			
			// 平日時就業時間帯を取得
			PredetemineTimeSetting predTimeSetOnWeekday = null;
			val workConditionItemOpt = employeeSets.getWorkingConditionItem(procYmd);
			if (workConditionItemOpt.isPresent()) {
				val workCategory = workConditionItemOpt.get().getWorkCategory();
				if (workCategory != null) {
					val weekdayTime = workCategory.getWeekdayTime();
					if (weekdayTime != null) {
						if (weekdayTime.getWorkTimeCode().isPresent()) {
							predTimeSetOnWeekday = companySets.getPredetemineTimeSetMap(
									weekdayTime.getWorkTimeCode().get().v(), repositories);
						}
					}
				}
			}
			
			// 必要なマスタがない時、その日をスキップする
			boolean isSkip = false;
			if (workType == null) isSkip = true;
			if (isSkip){
				procYmd = procYmd.addDays(1);
				continue;
			}
			
			// 勤怠時間を確認する
			val attendanceTimeOfDaily = monthlyCalcDailys.getAttendanceTimeOfDailyMap().get(procYmd);
			
			// 出退勤を確認する
			val timeLeavingOfDaily = monthlyCalcDailys.getTimeLeaveOfDailyMap().get(procYmd);
			
			// 臨時出退勤を確認する
			val temporaryTimeOfDaily = monthlyCalcDailys.getTemporaryTimeOfDailyMap().get(procYmd);
			
			// 特定日区分を確認する
			val specificDateAttrOfDaily = monthlyCalcDailys.getSpecificDateAttrOfDailyMap().get(procYmd);
			
			// 出勤状態・2回目打刻有無を確認する
			val isAttendanceDay = attendanceStatusList.isAttendanceDay(procYmd);
			val isTwoTimesStampExists = attendanceStatusList.isTwoTimesStampExists(procYmd);
			
			// PCログオン情報　取得
			val pcLogonInfoOpt = Optional.ofNullable(monthlyCalcDailys.getPcLogonInfoMap().get(procYmd));
		
			// 勤務日数集計
			this.workDays.aggregate(workingSystem, workType, attendanceTimeOfDaily, temporaryTimeOfDaily,
					specificDateAttrOfDaily,
					workTypeDaysCountTable, companySets.getPayItemCount(), predetermineTimeSet,
					isAttendanceDay, isTwoTimesStampExists, predTimeSetOnWeekday);
			
			// 勤務時間集計
			this.workTime.aggregate(workType, attendanceTimeOfDaily);
			
			// 勤務時刻集計
			this.workClock.aggregate(workType, pcLogonInfoOpt, attendanceTimeOfDaily, timeLeavingOfDaily, predTimeSetForCalc);
			
			procYmd = procYmd.addDays(1);
		}
	}
	
	/**
	 * 合算する
	 * @param target 加算対象
	 */
	public void sum(VerticalTotalOfMonthly target){

		this.workDays.sum(target.workDays);
		this.workTime.sum(target.workTime);
		this.workClock.sum(target.workClock);
	}
}
