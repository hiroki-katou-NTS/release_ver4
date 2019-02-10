package nts.uk.ctx.at.record.dom.monthly.verticaltotal.workdays;

import lombok.Getter;
import lombok.val;
import nts.uk.ctx.at.record.dom.actualworkinghours.AttendanceTimeOfDailyPerformance;
import nts.uk.ctx.at.record.dom.monthly.WorkTypeDaysCountTable;
import nts.uk.ctx.at.record.dom.monthly.verticaltotal.workdays.leave.LeaveOfMonthly;
import nts.uk.ctx.at.record.dom.monthly.verticaltotal.workdays.paydays.PayDaysOfMonthly;
import nts.uk.ctx.at.record.dom.monthly.verticaltotal.workdays.specificdays.SpecificDaysOfMonthly;
import nts.uk.ctx.at.record.dom.monthly.verticaltotal.workdays.workdays.AbsenceDaysOfMonthly;
import nts.uk.ctx.at.record.dom.monthly.verticaltotal.workdays.workdays.AttendanceDaysOfMonthly;
import nts.uk.ctx.at.record.dom.monthly.verticaltotal.workdays.workdays.HolidayDaysOfMonthly;
import nts.uk.ctx.at.record.dom.monthly.verticaltotal.workdays.workdays.HolidayWorkDaysOfMonthly;
import nts.uk.ctx.at.record.dom.monthly.verticaltotal.workdays.workdays.PredeterminedDaysOfMonthly;
import nts.uk.ctx.at.record.dom.monthly.verticaltotal.workdays.workdays.RecruitmentDaysOfMonthly;
import nts.uk.ctx.at.record.dom.monthly.verticaltotal.workdays.workdays.SpcVacationDaysOfMonthly;
import nts.uk.ctx.at.record.dom.monthly.verticaltotal.workdays.workdays.TemporaryWorkTimesOfMonthly;
import nts.uk.ctx.at.record.dom.monthly.verticaltotal.workdays.workdays.TwoTimesWorkTimesOfMonthly;
import nts.uk.ctx.at.record.dom.monthly.verticaltotal.workdays.workdays.WorkDaysDetailOfMonthly;
import nts.uk.ctx.at.record.dom.monthly.verticaltotal.workdays.workdays.WorkTimesOfMonthly;
import nts.uk.ctx.at.record.dom.monthly.vtotalmethod.PayItemCountOfMonthly;
import nts.uk.ctx.at.record.dom.raisesalarytime.SpecificDateAttrOfDailyPerfor;
import nts.uk.ctx.at.record.dom.worktime.TemporaryTimeOfDailyPerformance;
import nts.uk.ctx.at.shared.dom.workingcondition.WorkingSystem;
import nts.uk.ctx.at.shared.dom.worktime.predset.PredetemineTimeSetting;
import nts.uk.ctx.at.shared.dom.worktype.WorkType;

/**
 * 月別実績の勤務日数
 * @author shuichi_ishida
 */
@Getter
public class WorkDaysOfMonthly {

	/** 出勤日数 */
	private AttendanceDaysOfMonthly attendanceDays;
	/** 欠勤日数 */
	private AbsenceDaysOfMonthly absenceDays;
	/** 所定日数 */
	private PredeterminedDaysOfMonthly predetermineDays;
	/** 勤務日数 */
	private WorkDaysDetailOfMonthly workDays;
	/** 休日日数 */
	private HolidayDaysOfMonthly holidayDays;
	/** 特定日数 */
	private SpecificDaysOfMonthly specificDays;
	/** 休出日数 */
	private HolidayWorkDaysOfMonthly holidayWorkDays;
	/** 給与用日数 */
	private PayDaysOfMonthly payDays;
	/** 勤務回数 */
	private WorkTimesOfMonthly workTimes;
	/** 二回勤務回数 */
	private TwoTimesWorkTimesOfMonthly twoTimesWorkTimes;
	/** 臨時勤務回数 */
	private TemporaryWorkTimesOfMonthly temporaryWorkTimes;
	/** 休業 */
	private LeaveOfMonthly leave;
	/** 振出日数 */
	private RecruitmentDaysOfMonthly recruitmentDays;
	/** 特別休暇日数 */
	private SpcVacationDaysOfMonthly specialVacationDays;
	
	/**
	 * コンストラクタ
	 */
	public WorkDaysOfMonthly(){
		
		this.attendanceDays = new AttendanceDaysOfMonthly();
		this.absenceDays = new AbsenceDaysOfMonthly();
		this.predetermineDays = new PredeterminedDaysOfMonthly();
		this.workDays = new WorkDaysDetailOfMonthly();
		this.holidayDays = new HolidayDaysOfMonthly();
		this.specificDays = new SpecificDaysOfMonthly();
		this.holidayWorkDays = new HolidayWorkDaysOfMonthly();
		this.payDays = new PayDaysOfMonthly();
		this.workTimes = new WorkTimesOfMonthly();
		this.twoTimesWorkTimes = new TwoTimesWorkTimesOfMonthly();
		this.temporaryWorkTimes = new TemporaryWorkTimesOfMonthly();
		this.leave = new LeaveOfMonthly();
		this.recruitmentDays = new RecruitmentDaysOfMonthly();
		this.specialVacationDays = new SpcVacationDaysOfMonthly();
	}

	/**
	 * ファクトリー
	 * @param attendanceDays 出勤日数
	 * @param absenceDays 欠勤日数
	 * @param predetermineDays 所定日数
	 * @param workDays 勤務日数
	 * @param holidayDays 休日日数
	 * @param specficDays 特定日数
	 * @param holidayWorkDays 休出日数
	 * @param payDays 給与用日数
	 * @param workTimes 勤務回数
	 * @param twoTimesWorkTimes 二回勤務回数
	 * @param temporaryWorkTimes 臨時勤務回数
	 * @param leave 休業
	 * @param recruitmentDays 振出日数
	 * @param specialVacationDays 特別休暇日数
	 * @return 月別実績の勤務日数
	 */
	public static WorkDaysOfMonthly of(
			AttendanceDaysOfMonthly attendanceDays,
			AbsenceDaysOfMonthly absenceDays,
			PredeterminedDaysOfMonthly predetermineDays,
			WorkDaysDetailOfMonthly workDays,
			HolidayDaysOfMonthly holidayDays,
			SpecificDaysOfMonthly specficDays,
			HolidayWorkDaysOfMonthly holidayWorkDays,
			PayDaysOfMonthly payDays,
			WorkTimesOfMonthly workTimes,
			TwoTimesWorkTimesOfMonthly twoTimesWorkTimes,
			TemporaryWorkTimesOfMonthly temporaryWorkTimes,
			LeaveOfMonthly leave,
			RecruitmentDaysOfMonthly recruitmentDays,
			SpcVacationDaysOfMonthly specialVacationDays){
		
		val domain = new WorkDaysOfMonthly();
		domain.attendanceDays = attendanceDays;
		domain.absenceDays = absenceDays;
		domain.predetermineDays = predetermineDays;
		domain.workDays = workDays;
		domain.holidayDays = holidayDays;
		domain.specificDays = specficDays;
		domain.holidayWorkDays = holidayWorkDays;
		domain.payDays = payDays;
		domain.workTimes = workTimes;
		domain.twoTimesWorkTimes = twoTimesWorkTimes;
		domain.temporaryWorkTimes = temporaryWorkTimes;
		domain.leave = leave;
		domain.recruitmentDays = recruitmentDays;
		domain.specialVacationDays = specialVacationDays;
		return domain;
	}
	
	/**
	 * 集計
	 * @param workingSystem 労働制
	 * @param workType 勤務種類
	 * @param attendanceTimeOfDaily 日別実績の勤怠時間
	 * @param temporaryTimeOfDaily 日別実績の臨時出退勤
	 * @param specificDateAttrOfDaily 日別実績の特定日区分
	 * @param workTypeDaysCountTable 勤務種類の日数カウント表
	 * @param payItemCount 月別実績の給与項目カウント
	 * @param predetermineTimeSet 所定時間設定
	 * @param isAttendanceDay 出勤しているかどうか
	 * @param isTwoTimesStampExists 2回目の打刻が存在するかどうか
	 * @param predTimeSetOnWeekday 所定時間設定（平日時）
	 */
	public void aggregate(
			WorkingSystem workingSystem,
			WorkType workType,
			AttendanceTimeOfDailyPerformance attendanceTimeOfDaily,
			TemporaryTimeOfDailyPerformance temporaryTimeOfDaily,
			SpecificDateAttrOfDailyPerfor specificDateAttrOfDaily,
			WorkTypeDaysCountTable workTypeDaysCountTable,
			PayItemCountOfMonthly payItemCount,
			PredetemineTimeSetting predetermineTimeSet,
			boolean isAttendanceDay,
			boolean isTwoTimesStampExists,
			PredetemineTimeSetting predTimeSetOnWeekday){
		
		// 出勤日数の集計
		this.attendanceDays.aggregate(workingSystem, workTypeDaysCountTable, isAttendanceDay);
		
		// 欠勤日数の集計
		this.absenceDays.aggregate(workingSystem, workType, workTypeDaysCountTable, isAttendanceDay,
				predetermineTimeSet, predTimeSetOnWeekday);
		
		// 所定日数の集計
		this.predetermineDays.aggregate(workTypeDaysCountTable);
		
		// 勤務日数の集計
		this.workDays.aggregate(workingSystem, workTypeDaysCountTable, isAttendanceDay);
		
		// 休日日数の集計
		this.holidayDays.aggregate(workTypeDaysCountTable);
		
		// 特定日日数の集計
		//*****（未）　月別実績の縦計方法の設計待ち。特定日集計に該当する勤務種類かを、方法から判定する必要がある。
		this.specificDays.aggregate(workingSystem, workType, specificDateAttrOfDaily, isAttendanceDay);
		
		// 休出日数の集計
		this.holidayWorkDays.aggregate(workingSystem, workTypeDaysCountTable, isAttendanceDay);
		
		// 給与支払い基礎日数の集計
		this.payDays.aggregate(workType, payItemCount);
		
		// 勤務回数の集計
		this.workTimes.aggregate(attendanceTimeOfDaily);
		
		// 二回勤務回数の集計
		this.twoTimesWorkTimes.aggregate(predetermineTimeSet, isTwoTimesStampExists);
		
		// 臨時勤務回数の集計
		//*****（未）　回数計算が独自に必要か、回数参照だけで良いか確認要。
		this.temporaryWorkTimes.aggregate(temporaryTimeOfDaily);
		
		// 休業日数の集計
		this.leave.aggregate(workTypeDaysCountTable);
		
		// 振出日数の集計
		this.recruitmentDays.aggregate(workingSystem, workTypeDaysCountTable, isAttendanceDay);
		
		// 特別休暇日数の集計
		this.specialVacationDays.aggregate(workingSystem, workType, workTypeDaysCountTable, isAttendanceDay,
				predetermineTimeSet, predTimeSetOnWeekday);
	}
	
	/**
	 * 合算する
	 * @param target 加算対象
	 */
	public void sum(WorkDaysOfMonthly target){
		
		this.attendanceDays.sum(target.attendanceDays);
		this.absenceDays.sum(target.absenceDays);
		this.predetermineDays.sum(target.predetermineDays);
		this.workDays.sum(target.workDays);
		this.holidayDays.sum(target.holidayDays);
		this.specificDays.sum(target.specificDays);
		this.holidayWorkDays.sum(target.holidayWorkDays);
		this.payDays.sum(target.payDays);
		this.workTimes.sum(target.workTimes);
		this.twoTimesWorkTimes.sum(target.twoTimesWorkTimes);
		this.temporaryWorkTimes.sum(target.temporaryWorkTimes);
		this.leave.sum(target.leave);
		this.recruitmentDays.sum(target.recruitmentDays);
		this.specialVacationDays.sum(target.specialVacationDays);
	}
}
