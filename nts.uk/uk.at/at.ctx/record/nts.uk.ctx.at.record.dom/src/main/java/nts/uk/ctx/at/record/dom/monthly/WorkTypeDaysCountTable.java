package nts.uk.ctx.at.record.dom.monthly;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import lombok.Getter;
import lombok.val;
import nts.uk.ctx.at.record.dom.monthly.verticaltotal.VacationAddSet;
import nts.uk.ctx.at.record.dom.monthly.verticaltotal.workdays.workdays.AggregateAbsenceDays;
import nts.uk.ctx.at.record.dom.monthly.verticaltotal.workdays.workdays.AggregateSpcVacationDays;
import nts.uk.ctx.at.record.dom.monthly.vtotalmethod.VerticalTotalMethodOfMonthly;
import nts.uk.ctx.at.shared.dom.common.days.AttendanceDaysMonth;
import nts.uk.ctx.at.shared.dom.worktype.CloseAtr;
import nts.uk.ctx.at.shared.dom.worktype.WorkAtr;
import nts.uk.ctx.at.shared.dom.worktype.WorkType;
import nts.uk.ctx.at.shared.dom.worktype.WorkTypeClassification;
import nts.uk.ctx.at.shared.dom.worktype.WorkTypeSet;
import nts.uk.ctx.at.shared.dom.worktype.WorkTypeSetCheck;

/**
 * 勤務種類の日数カウント表
 * @author shuichi_ishida
 */
@Getter
public class WorkTypeDaysCountTable {

	/** 出勤日数 */
	private AttendanceDaysMonth attendanceDays;
	/** 休日日数 */
	private AttendanceDaysMonth holidayDays;
	/** 公休日数 */
	private AttendanceDaysMonth publicHolidayDays;
	/** 休出日数 */
	private AttendanceDaysMonth holidayWorkDays;
	/** 年休日数 */
	private AttendanceDaysMonth annualLeaveDays;
	/** 積立年休日数 */
	private AttendanceDaysMonth retentionYearlyDays;
	/** 特休日数 */
	private Map<Integer, AggregateSpcVacationDays> spcVacationDaysMap;
	/** 特休単位 */
	private Map<Integer, WorkAtr> spcVacationWorkAtrMap;
	/** 欠勤日数 */
	private Map<Integer, AggregateAbsenceDays> absenceDaysMap;
	/** 欠勤単位 */
	private Map<Integer, WorkAtr> absenceWorkAtrMap;
	/** 代休日数 */
	private AttendanceDaysMonth compensatoryLeaveDays;
	/** 振出日数 */
	private AttendanceDaysMonth transferAttendanceDays;
	/** 振休発生日数 */
	private AttendanceDaysMonth transferHolidayGenerateDays;
	/** 振休使用日数 */
	private AttendanceDaysMonth transferHolidayUseDays;
	/** 休業日数 */
	private Map<CloseAtr, AttendanceDaysMonth> leaveDays;
	/** 時消日数 */
	private AttendanceDaysMonth timeDigestionDays;
	/** 所定日数 */
	private AttendanceDaysMonth predetermineDays;
	/** 勤務日数 */
	private AttendanceDaysMonth workDays;
	/** 年休出勤率用労働日数 */
	private AttendanceDaysMonth workingDaysForAttendanceRate;

	/** 年休を加算する */
	private boolean addAnnualLeave;
	/** 積立年休を加算する */
	private boolean addRetentionYearly;
	/** 特別休暇を加算する */
	private boolean addSpecialHoliday;
	
	/** 月別実績の縦計方法 */
	private Optional<VerticalTotalMethodOfMonthly> verticalTotalMethodOpt;
	
	/**
	 * コンストラクタ
	 * @param workType 勤務種類
	 * @param vacationAddSet 休暇加算設定
	 * @param verticalTotalMethod 月別実績の縦計方法　（特定日日数の振分用）
	 */
	public WorkTypeDaysCountTable(WorkType workType,
			VacationAddSet vacationAddSet, Optional<VerticalTotalMethodOfMonthly> verticalTotalMethod){
		
		// init
		this.attendanceDays = new AttendanceDaysMonth(0.0);
		this.holidayDays = new AttendanceDaysMonth(0.0);
		this.publicHolidayDays = new AttendanceDaysMonth(0.0);
		this.holidayWorkDays = new AttendanceDaysMonth(0.0);
		this.annualLeaveDays = new AttendanceDaysMonth(0.0);
		this.retentionYearlyDays = new AttendanceDaysMonth(0.0);
		this.spcVacationDaysMap = new HashMap<>();
		this.spcVacationWorkAtrMap = new HashMap<>();
		this.absenceDaysMap = new HashMap<>();
		this.absenceWorkAtrMap = new HashMap<>();
		this.compensatoryLeaveDays = new AttendanceDaysMonth(0.0);
		this.transferAttendanceDays = new AttendanceDaysMonth(0.0);
		this.transferHolidayGenerateDays = new AttendanceDaysMonth(0.0);
		this.transferHolidayUseDays = new AttendanceDaysMonth(0.0);
		this.leaveDays = new HashMap<>();
		this.timeDigestionDays = new AttendanceDaysMonth(0.0);
		this.predetermineDays = new AttendanceDaysMonth(0.0);
		this.workDays = new AttendanceDaysMonth(0.0);
		this.workingDaysForAttendanceRate = new AttendanceDaysMonth(0.0);
		
		this.addAnnualLeave = vacationAddSet.isAnnualLeave();
		this.addRetentionYearly = vacationAddSet.isRetentionYearly();
		//*****（未）　特別休暇の判定方法について、設計確認要。
		this.addSpecialHoliday = false;
		
		this.verticalTotalMethodOpt = verticalTotalMethod;
		
		this.confirmCount(workType);
	}
	
	/**
	 * カウント日数確認
	 * @param workType 勤務種類
	 */
	private void confirmCount(WorkType workType){
		
		val dailyWork = workType.getDailyWork();
		
		if (workType.isOneDay()){

			// 勤務単位＝1日　の時
			val workTypeClass = dailyWork.getOneDay();
			val workTypeSet = workType.getWorkTypeSetByAtr(WorkAtr.OneDay);
			this.confirmCountByWorkTypeUnit(workTypeClass, workTypeSet, 1.0);
			
			// 勤務種類が勤務日数カウント対象なら、勤務日数に加算
			if (this.isCountWorkDays(workTypeClass)) {
				this.workDays = this.workDays.addDays(1.0);
			}
			
			// 勤務種類が年休出勤率用労働日数カウント対象なら、年休出勤率用労働日数に加算
			if (this.isCountWorkingDaysForAttendanceRate(workTypeClass)){
				this.workingDaysForAttendanceRate = this.workingDaysForAttendanceRate.addDays(1.0);
			}
		}
		else {
			
			// 勤務単位＝午前と午後　の時
			val workTypeClassAM = dailyWork.getMorning();
			val workTypeClassPM = dailyWork.getAfternoon();
			val workTypeSetAM = workType.getWorkTypeSetByAtr(WorkAtr.Monring);
			val workTypeSetPM = workType.getWorkTypeSetByAtr(WorkAtr.Afternoon);
			this.confirmCountByWorkTypeUnit(workTypeClassAM, workTypeSetAM, 0.5);
			this.confirmCountByWorkTypeUnit(workTypeClassPM, workTypeSetPM, 0.5);
			
			// 午前か午後の勤務種類が勤務日数カウント対象なら、勤務日数に加算
			if (this.isCountWorkDays(workTypeClassAM) || this.isCountWorkDays(workTypeClassPM)){
				this.workDays = this.workDays.addDays(1.0);
			}
			
			// 午前か午後の勤務種類が年休出勤率用労働日数カウント対象なら、年休出勤率用労働日数に加算
			if (this.isCountWorkingDaysForAttendanceRate(workTypeClassAM) ||
				this.isCountWorkingDaysForAttendanceRate(workTypeClassPM)){
				this.workingDaysForAttendanceRate = this.workingDaysForAttendanceRate.addDays(1.0);
			}
		}
	}
	
	/**
	 * カウント日数確認　（勤務種類単位）
	 * @param workTypeClass 勤務種類の分類
	 * @param workTypeSet 勤務種類設定 
	 * @param addDays 加算する日数
	 */
	private void confirmCountByWorkTypeUnit(
			WorkTypeClassification workTypeClass, Optional<WorkTypeSet> workTypeSet, double addDays){
		
		// 勤務種類設定を確認する
		boolean publicHoliday = false;
		boolean notCountForHolidayDays = false;
		//boolean generateCompensatoryLeave = false;
		int sumAbsenceNo = -1;
		int sumSpHolidayNo = -1;
		CloseAtr closeAtr = null;
		WorkAtr workAtr = null;
		if (workTypeSet != null && workTypeSet.isPresent()) {
			publicHoliday = (workTypeSet.get().getDigestPublicHd() == WorkTypeSetCheck.CHECK); 
			notCountForHolidayDays = (workTypeSet.get().getCountHodiday() != WorkTypeSetCheck.CHECK);
			//generateCompensatoryLeave = (workTypeSet.getGenSubHodiday() == WorkTypeSetCheck.CHECK);
			sumAbsenceNo = workTypeSet.get().getSumAbsenseNo();
			sumSpHolidayNo = workTypeSet.get().getSumSpHodidayNo();
			closeAtr = workTypeSet.get().getCloseAtr();
			workAtr = workTypeSet.get().getWorkAtr();
		}
		
		switch (workTypeClass){
		case Attendance:
			this.attendanceDays = this.attendanceDays.addDays(addDays);
			this.predetermineDays = this.predetermineDays.addDays(addDays);
			break;
		case Holiday:
			if (!notCountForHolidayDays) {
				this.holidayDays = this.holidayDays.addDays(addDays);
				if (publicHoliday) this.publicHolidayDays = this.publicHolidayDays.addDays(addDays);
			}
			break;
		case AnnualHoliday:
			if (this.addAnnualLeave) this.attendanceDays = this.attendanceDays.addDays(addDays);
			this.annualLeaveDays = this.annualLeaveDays.addDays(addDays);
			this.predetermineDays = this.predetermineDays.addDays(addDays);
			break;
		case YearlyReserved:
			if (this.addRetentionYearly) this.attendanceDays = this.attendanceDays.addDays(addDays);
			this.retentionYearlyDays = this.retentionYearlyDays.addDays(addDays);
			this.predetermineDays = this.predetermineDays.addDays(addDays);
			break;
		case SpecialHoliday:
			if (sumSpHolidayNo >= 0){
				val spcVacationFrameNo = Integer.valueOf(sumSpHolidayNo);
				this.spcVacationDaysMap.putIfAbsent(spcVacationFrameNo, new AggregateSpcVacationDays(spcVacationFrameNo));
				val targetSpcVacationDays = this.spcVacationDaysMap.get(spcVacationFrameNo);
				targetSpcVacationDays.addDays(addDays);
				if (workAtr != null) {
					this.spcVacationWorkAtrMap.putIfAbsent(spcVacationFrameNo, workAtr);
				}
			}
			if (this.addSpecialHoliday) this.attendanceDays = this.attendanceDays.addDays(addDays);
			this.predetermineDays = this.predetermineDays.addDays(addDays);
			break;
		case Absence:
			if (sumAbsenceNo >= 0){
				val absenceFrameNo = Integer.valueOf(sumAbsenceNo);
				this.absenceDaysMap.putIfAbsent(absenceFrameNo, new AggregateAbsenceDays(absenceFrameNo));
				val targetAbsenceDays = this.absenceDaysMap.get(absenceFrameNo);
				targetAbsenceDays.addDays(addDays);
				if (workAtr != null) {
					this.absenceWorkAtrMap.putIfAbsent(absenceFrameNo, workAtr);
				}
			}
			this.predetermineDays = this.predetermineDays.addDays(addDays);
			break;
		case SubstituteHoliday:
			this.compensatoryLeaveDays = this.compensatoryLeaveDays.addDays(addDays);
			this.predetermineDays = this.predetermineDays.addDays(addDays);
			break;
		case Shooting:
			this.transferAttendanceDays = this.transferAttendanceDays.addDays(addDays);
			this.transferHolidayGenerateDays = this.transferHolidayGenerateDays.addDays(addDays);
			this.predetermineDays = this.predetermineDays.addDays(addDays);
			break;
		case Pause:
			this.transferHolidayUseDays = this.transferHolidayUseDays.addDays(addDays);
			break;
		case TimeDigestVacation:
			if (this.addAnnualLeave) this.attendanceDays = this.attendanceDays.addDays(addDays);
			this.timeDigestionDays = this.timeDigestionDays.addDays(addDays);
			this.predetermineDays = this.predetermineDays.addDays(addDays);
			break;
		case HolidayWork:
			this.holidayWorkDays = this.holidayWorkDays.addDays(addDays);
			break;
		case ContinuousWork:
			this.attendanceDays = this.attendanceDays.addDays(addDays);
			this.predetermineDays = this.predetermineDays.addDays(addDays);
			break;
		case Closure:
			if (closeAtr != null){
				this.leaveDays.putIfAbsent(closeAtr, new AttendanceDaysMonth(0.0));
				this.leaveDays.compute(closeAtr, (k, v) -> v.addDays(addDays));
			}
			this.predetermineDays = this.predetermineDays.addDays(addDays);
			break;
		case LeaveOfAbsence:
			break;
		default:
			break;
		}
	}
	
	/**
	 * 勤務日数カウントする勤務種類かどうか
	 * @param workTypeClass 勤務種類の分類
	 * @return true：カウントする、false：カウントしない
	 */
	private boolean isCountWorkDays(WorkTypeClassification workTypeClass){
		
		switch (workTypeClass){
		case Attendance:
		case Shooting:
		case HolidayWork:
			return true;
		default:
			break;
		}
		return false;
	}
	
	/**
	 * 年休出勤率用労働日数カウントする勤務種類かどうか
	 * @param workTypeClass 勤務種類の分類
	 * @return true：カウントする、false：カウントしない
	 */
	private boolean isCountWorkingDaysForAttendanceRate(WorkTypeClassification workTypeClass){
		
		switch (workTypeClass){
		case Attendance:
		case AnnualHoliday:
		case YearlyReserved:
		case SpecialHoliday:
		case SubstituteHoliday:
		case Shooting:
		case TimeDigestVacation:
		case ContinuousWork:
		case Closure:
			return true;
		default:
			break;
		}
		return false;
	}
}
