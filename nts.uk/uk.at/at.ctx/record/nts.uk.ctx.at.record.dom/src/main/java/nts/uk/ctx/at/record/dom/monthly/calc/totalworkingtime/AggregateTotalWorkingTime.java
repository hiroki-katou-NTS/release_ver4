package nts.uk.ctx.at.record.dom.monthly.calc.totalworkingtime;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import lombok.Getter;
import lombok.val;
import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.record.dom.actualworkinghours.AttendanceTimeOfDailyPerformance;
import nts.uk.ctx.at.record.dom.monthly.calc.MonthlyAggregateAtr;
import nts.uk.ctx.at.record.dom.monthly.calc.actualworkingtime.RegularAndIrregularTimeOfMonthly;
import nts.uk.ctx.at.record.dom.monthly.calc.flex.FlexTime;
import nts.uk.ctx.at.record.dom.monthly.calc.flex.FlexTimeOfMonthly;
import nts.uk.ctx.at.record.dom.monthly.calc.totalworkingtime.hdwkandcompleave.HolidayWorkTimeOfMonthly;
import nts.uk.ctx.at.record.dom.monthly.calc.totalworkingtime.overtime.OverTimeOfMonthly;
import nts.uk.ctx.at.record.dom.monthly.calc.totalworkingtime.vacationusetime.VacationUseTimeOfMonthly;
import nts.uk.ctx.at.record.dom.monthly.workform.flex.MonthlyAggrSetOfFlex;
import nts.uk.ctx.at.record.dom.monthlyaggrmethod.legaltransferorder.LegalTransferOrderSetOfAggrMonthly;
import nts.uk.ctx.at.record.dom.monthlyprocess.aggr.work.RepositoriesRequiredByMonthlyAggr;
import nts.uk.ctx.at.record.dom.monthlyprocess.aggr.work.SettingRequiredByDefo;
import nts.uk.ctx.at.record.dom.monthlyprocess.aggr.work.SettingRequiredByReg;
import nts.uk.ctx.at.record.dom.workrecord.monthcal.ExcessOutsideTimeSetReg;
import nts.uk.ctx.at.record.dom.workrecord.monthcal.FlexMonthWorkTimeAggrSet;
import nts.uk.ctx.at.shared.dom.WorkInformation;
import nts.uk.ctx.at.shared.dom.common.time.AttendanceTimeMonth;
import nts.uk.ctx.at.shared.dom.workingcondition.WorkingSystem;
import nts.uk.ctx.at.shared.dom.workrecord.monthlyresults.roleofovertimework.RoleOvertimeWork;
import nts.uk.ctx.at.shared.dom.workrecord.monthlyresults.roleopenperiod.RoleOfOpenPeriod;
import nts.uk.shr.com.time.calendar.period.DatePeriod;

/**
 * 集計総労働時間
 * @author shuichi_ishida
 */
@Getter
public class AggregateTotalWorkingTime {

	/** 就業時間 */
	private WorkTimeOfMonthly workTime;
	/** 残業時間 */
	private OverTimeOfMonthly overTime;
	/** 休出時間 */
	private HolidayWorkTimeOfMonthly holidayWorkTime;
	/** 臨時時間 */
	//temporaryTime
	/** 休暇使用時間 */
	private VacationUseTimeOfMonthly vacationUseTime;
	/** 所定労働時間 */
	private PrescribedWorkingTimeOfMonthly prescribedWorkingTime;
	
	/**
	 * コンストラクタ
	 */
	public AggregateTotalWorkingTime(){
		
		this.workTime = new WorkTimeOfMonthly();
		this.overTime = new OverTimeOfMonthly();
		this.holidayWorkTime = new HolidayWorkTimeOfMonthly();
		this.vacationUseTime = new VacationUseTimeOfMonthly();
		this.prescribedWorkingTime = new PrescribedWorkingTimeOfMonthly();
	}

	/**
	 * ファクトリー
	 * @param workTime 就業時間
	 * @param overTime 残業時間
	 * @param holidayWorkTime 休出時間
	 * @param vacationUseTime 休暇使用時間
	 * @param prescribedWorkingTime 所定労働時間
	 * @return 集計総労働時間
	 */
	public static AggregateTotalWorkingTime of(
			WorkTimeOfMonthly workTime,
			OverTimeOfMonthly overTime,
			HolidayWorkTimeOfMonthly holidayWorkTime,
			VacationUseTimeOfMonthly vacationUseTime,
			PrescribedWorkingTimeOfMonthly prescribedWorkingTime){
		
		val domain = new AggregateTotalWorkingTime();
		domain.workTime = workTime;
		domain.overTime = overTime;
		domain.holidayWorkTime = holidayWorkTime;
		domain.vacationUseTime = vacationUseTime;
		domain.prescribedWorkingTime = prescribedWorkingTime;
		return domain;
	}
	
	/**
	 * 共有項目を集計する
	 * @param datePeriod 期間
	 * @param attendanceTimeOfDailyMap 日別実績の勤怠時間リスト
	 */
	public void aggregateSharedItem(DatePeriod datePeriod,
			Map<GeneralDate, AttendanceTimeOfDailyPerformance> attendanceTimeOfDailyMap){

		// 就業時間を集計する
		this.workTime.confirm(datePeriod, attendanceTimeOfDailyMap);
	
		// 休暇使用時間を集計する
		this.vacationUseTime.confirm(datePeriod, attendanceTimeOfDailyMap);
		
		// 所定労働時間を集計する
		this.prescribedWorkingTime.confirm(datePeriod, attendanceTimeOfDailyMap);
	}
	
	/**
	 * 共有項目をコピーする
	 * @param aggregateTime 集計時間
	 */
	public void copySharedItem(AggregateTotalWorkingTime aggregateTime){
		
		this.workTime = aggregateTime.workTime.clone();
		this.vacationUseTime = aggregateTime.vacationUseTime.clone();
		this.prescribedWorkingTime = aggregateTime.prescribedWorkingTime.clone();
	}
	
	/**
	 * 日別実績を集計する　（通常・変形労働時間勤務用）
	 * @param attendanceTimeOfDaily 日別実績の勤怠時間
	 * @param companyId 会社ID
	 * @param workplaceId 職場ID
	 * @param employmentCd 雇用コード
	 * @param workingSystem 労働制
	 * @param aggregateAtr 集計区分
	 * @param workInfo 勤務情報
	 * @param settingsByReg 通常勤務が必要とする設定
	 * @param settingsByDefo 変形労働勤務が必要とする設定
	 * @param repositories 月次集計が必要とするリポジトリ
	 */
	public void aggregateDailyForRegAndIrreg(
			AttendanceTimeOfDailyPerformance attendanceTimeOfDaily,
			String companyId, String workplaceId, String employmentCd,
			WorkingSystem workingSystem, MonthlyAggregateAtr aggregateAtr,
			WorkInformation workInfo,
			SettingRequiredByReg settingsByReg,
			SettingRequiredByDefo settingsByDefo,
			RepositoriesRequiredByMonthlyAggr repositories){

		// 労働制を元に、該当する設定を取得する
		LegalTransferOrderSetOfAggrMonthly legalTransferOrderSet = new LegalTransferOrderSetOfAggrMonthly(companyId);
		Map<Integer, RoleOvertimeWork> roleOverTimeFrameMap = new HashMap<>();
		Map<Integer, RoleOfOpenPeriod> roleHolidayWorkFrameMap = new HashMap<>();
		List<RoleOvertimeWork> autoExceptOverTimeFrames = new ArrayList<>();
		List<RoleOfOpenPeriod> autoExceptHolidayWorkFrames = new ArrayList<>();
		ExcessOutsideTimeSetReg excessOutsideTimeSet = new ExcessOutsideTimeSetReg(false, false, false);
		if (workingSystem == WorkingSystem.VARIABLE_WORKING_TIME_WORK) {
			// 変形労働の時
			legalTransferOrderSet = settingsByDefo.getLegalTransferOrderSet();
			roleOverTimeFrameMap = settingsByDefo.getRoleOverTimeFrameMap();
			roleHolidayWorkFrameMap = settingsByDefo.getRoleHolidayWorkFrameMap();
			autoExceptOverTimeFrames = settingsByDefo.getAutoExceptOverTimeFrames();
			autoExceptHolidayWorkFrames = settingsByDefo.getAutoExceptHolidayWorkFrames();
			excessOutsideTimeSet = settingsByDefo.getDeforAggrSet().getExcessOutsideTimeSet();
		}
		else {
			// 通常勤務の時
			legalTransferOrderSet = settingsByReg.getLegalTransferOrderSet();
			roleOverTimeFrameMap = settingsByReg.getRoleOverTimeFrameMap();
			roleHolidayWorkFrameMap = settingsByReg.getRoleHolidayWorkFrameMap();
			autoExceptOverTimeFrames = settingsByReg.getAutoExceptOverTimeFrames();
			autoExceptHolidayWorkFrames = settingsByReg.getAutoExceptHolidayWorkFrames();
			excessOutsideTimeSet = settingsByReg.getRegularAggrSet().getExcessOutsideTimeSet();
		}
		
		// 残業時間を集計する　（通常・変形労働時間勤務用）
		this.overTime.aggregateForRegAndIrreg(attendanceTimeOfDaily, companyId, workplaceId, employmentCd,
				workingSystem, workInfo, legalTransferOrderSet.getLegalOverTimeTransferOrder(),
				roleOverTimeFrameMap, autoExceptOverTimeFrames, repositories);
		
		// 休出時間を集計する　（通常・変形労働時間勤務用）
		this.holidayWorkTime.aggregateForRegAndIrreg(attendanceTimeOfDaily, companyId, workplaceId, employmentCd,
				workingSystem, aggregateAtr, workInfo, legalTransferOrderSet.getLegalHolidayWorkTransferOrder(),
				excessOutsideTimeSet, roleHolidayWorkFrameMap, autoExceptHolidayWorkFrames, repositories);
	}
	
	/**
	 * 日別実績を集計する　（フレックス時間勤務用）
	 * @param attendanceTimeOfDaily 日別実績の勤怠時間
	 * @param companyId 会社ID
	 * @param workplaceId 職場ID
	 * @param employmentCd 雇用コード
	 * @param workingSystem 労働制
	 * @param aggregateAtr 集計区分
	 * @param flexAggrSet フレックス時間勤務の月の集計設定
	 * @param monthlyAggrSetOfFlexOpt フレックス勤務の月別集計設定
	 * @return フレックス時間　（当日分のみ）
	 */
	public FlexTime aggregateDailyForFlex(
			AttendanceTimeOfDailyPerformance attendanceTimeOfDaily,
			String companyId, String workplaceId, String employmentCd,
			WorkingSystem workingSystem, MonthlyAggregateAtr aggregateAtr,
			FlexMonthWorkTimeAggrSet flexAggrSet,
			Optional<MonthlyAggrSetOfFlex> monthlyAggrSetOfFlexOpt){
		
		FlexTime flexTime = new FlexTime();
		
		// 残業時間を集計する　（フレックス時間勤務用）
		flexTime = this.overTime.aggregateForFlex(attendanceTimeOfDaily, companyId, aggregateAtr,
				flexAggrSet, monthlyAggrSetOfFlexOpt, flexTime);
		
		// 休出時間を集計する　（フレックス時間勤務用）
		flexTime = this.holidayWorkTime.aggregateForFlex(attendanceTimeOfDaily, companyId, aggregateAtr,
				flexAggrSet, monthlyAggrSetOfFlexOpt, flexTime);
		
		return flexTime;
	}
	
	/**
	 * 実働時間の集計
	 * @param datePeriod 期間
	 * @param workingSystem 労働制
	 * @param actualWorkingTime 実働時間
	 * @param flexTime フレックス時間
	 */
	public void aggregateActualWorkingTime(
			DatePeriod datePeriod,
			WorkingSystem workingSystem,
			RegularAndIrregularTimeOfMonthly actualWorkingTime,
			FlexTimeOfMonthly flexTime){
		
		// 残業合計時間を集計する
		this.overTime.aggregateTotal(datePeriod);
		
		// 休出合計時間を集計する
		this.holidayWorkTime.aggregateTotal(datePeriod);
		
		// 休暇使用時間を集計する
		this.vacationUseTime.aggregate(datePeriod);
		
		// 所定労働時間を集計する
		this.prescribedWorkingTime.aggregate(datePeriod);
		
		// 就業時間を集計する
		this.workTime.aggregate(datePeriod, workingSystem, actualWorkingTime, flexTime,
				this.overTime, this.holidayWorkTime);
	}
	
	/**
	 * 総労働対象時間の取得
	 * @return 総労働対象時間
	 */
	public AttendanceTimeMonth getTotalWorkingTargetTime(){
		
		return new AttendanceTimeMonth(this.workTime.getTotalWorkingTargetTime().v() +
				this.overTime.getTotalWorkingTargetTime().v() +
				this.holidayWorkTime.getTotalWorkingTargetTime().v());
	}
	
	/**
	 * 合算する
	 * @param target 加算対象
	 */
	public void sum(AggregateTotalWorkingTime target){
		
		this.workTime.sum(target.workTime);
		this.overTime.sum(target.overTime);
		this.holidayWorkTime.sum(target.holidayWorkTime);
		this.vacationUseTime.sum(target.vacationUseTime);
		this.prescribedWorkingTime.sum(target.prescribedWorkingTime);
	}
}
