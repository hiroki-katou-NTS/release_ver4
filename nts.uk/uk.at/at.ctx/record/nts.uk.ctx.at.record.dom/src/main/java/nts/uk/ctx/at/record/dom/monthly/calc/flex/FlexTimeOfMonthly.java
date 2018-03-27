package nts.uk.ctx.at.record.dom.monthly.calc.flex;

import java.util.Map;
import java.util.Optional;

import lombok.Getter;
import lombok.val;
import nts.arc.time.GeneralDate;
import nts.arc.time.YearMonth;
import nts.uk.ctx.at.record.dom.actualworkinghours.AttendanceTimeOfDailyPerformance;
import nts.uk.ctx.at.record.dom.monthly.TimeMonthWithCalculationAndMinus;
import nts.uk.ctx.at.record.dom.monthly.calc.AggregateMonthlyValue;
import nts.uk.ctx.at.record.dom.monthly.calc.MonthlyAggregateAtr;
import nts.uk.ctx.at.record.dom.monthly.calc.totalworkingtime.AggregateTotalWorkingTime;
import nts.uk.ctx.at.record.dom.monthlyaggrmethod.flex.AggrSettingMonthlyOfFlx;
import nts.uk.ctx.at.record.dom.monthlyaggrmethod.flex.AggregateSetting;
import nts.uk.ctx.at.record.dom.monthlyaggrmethod.flex.CarryforwardSetInShortageFlex;
import nts.uk.ctx.at.record.dom.monthlyaggrmethod.flex.FlexAggregateMethod;
import nts.uk.ctx.at.record.dom.monthlyprocess.aggr.export.DeductDaysAndTime;
import nts.uk.ctx.at.record.dom.monthlyprocess.aggr.work.RepositoriesRequiredByMonthlyAggr;
import nts.uk.ctx.at.record.dom.monthlyprocess.aggr.work.excessoutside.ExcessOutsideWorkMng;
import nts.uk.ctx.at.record.dom.monthlyprocess.aggr.work.premiumtarget.AddedVacationUseTime;
import nts.uk.ctx.at.record.dom.monthlyprocess.aggr.work.premiumtarget.getvacationaddtime.AddSet;
import nts.uk.ctx.at.record.dom.monthlyprocess.aggr.work.premiumtarget.getvacationaddtime.GetAddSet;
import nts.uk.ctx.at.record.dom.monthlyprocess.aggr.work.premiumtarget.getvacationaddtime.GetVacationAddTime;
import nts.uk.ctx.at.record.dom.monthlyprocess.aggr.work.premiumtarget.getvacationaddtime.PremiumAtr;
import nts.uk.ctx.at.shared.dom.calculation.holiday.FlexWork;
import nts.uk.ctx.at.shared.dom.calculation.holiday.HolidayAddtion;
import nts.uk.ctx.at.shared.dom.common.time.AttendanceTimeMonth;
import nts.uk.ctx.at.shared.dom.common.time.AttendanceTimeMonthWithMinus;
import nts.uk.ctx.at.shared.dom.workingcondition.WorkingConditionItem;
import nts.uk.ctx.at.shared.dom.workingcondition.WorkingSystem;
import nts.uk.shr.com.time.calendar.period.DatePeriod;

/**
 * 月別実績のフレックス時間
 * @author shuichi_ishida
 */
@Getter
public class FlexTimeOfMonthly {

	/** フレックス時間 */
	private FlexTime flexTime;
	/** フレックス超過時間 */
	private AttendanceTimeMonth flexExcessTime;
	/** フレックス不足時間 */
	private AttendanceTimeMonth flexShortageTime;
	/** フレックス繰越時間 */
	private FlexCarryforwardTime flexCarryforwardTime;
	/** 時間外超過のフレックス時間 */
	private FlexTimeOfExcessOutsideTime flexTimeOfExcessOutsideTime;
	/** フレックス不足控除時間 */
	private FlexShortDeductTime flexShortDeductTime;
	
	/** 加算した休暇使用時間 */
	private AddedVacationUseTime addedVacationUseTime;
	/** 控除の日数と時間 */
	private DeductDaysAndTime deductDaysAndTime;
	
	/**
	 * コンストラクタ
	 */
	public FlexTimeOfMonthly(){
		
		this.flexTime = new FlexTime();
		this.flexExcessTime = new AttendanceTimeMonth(0);
		this.flexShortageTime = new AttendanceTimeMonth(0);
		this.flexCarryforwardTime = new FlexCarryforwardTime();
		this.flexTimeOfExcessOutsideTime = new FlexTimeOfExcessOutsideTime();
		this.flexShortDeductTime = new FlexShortDeductTime();
		
		this.addedVacationUseTime = new AddedVacationUseTime();
	}

	/**
	 * ファクトリー
	 * @param flexTime フレックス時間
	 * @param flexExcessTime フレックス超過時間
	 * @param flexShortageTime フレックス不足時間
	 * @param flexCarryforwardTime フレックス繰越時間
	 * @param flexTimeOfExcessOutsideWork 時間外超過のフレックス時間
	 * @param flexShortDeductTime フレックス不足控除時間
	 * @return 月別実績のフレックス時間
	 */
	public static FlexTimeOfMonthly of(
			FlexTime flexTime,
			AttendanceTimeMonth flexExcessTime,
			AttendanceTimeMonth flexShortageTime,
			FlexCarryforwardTime flexCarryforwardTime,
			FlexTimeOfExcessOutsideTime flexTimeOfExcessOutsideWork,
			FlexShortDeductTime flexShortDeductTime){
		
		val domain = new FlexTimeOfMonthly();
		domain.flexTime = flexTime;
		domain.flexExcessTime = flexExcessTime;
		domain.flexShortageTime = flexShortageTime;
		domain.flexCarryforwardTime = flexCarryforwardTime;
		domain.flexTimeOfExcessOutsideTime = flexTimeOfExcessOutsideWork;
		return domain;
	}
	
	/**
	 * 月別実績を集計する
	 * @param companyId 会社ID
	 * @param employeeId 社員ID
	 * @param yearMonth 年月（度）
	 * @param datePeriod 期間
	 * @param workingSystem 労働制
	 * @param aggregateAtr 集計区分
	 * @param flexAggregateMethod フレックス集計方法
	 * @param aggrSetOfFlex フレックス時間勤務の月の集計設定
	 * @param attendanceTimeOfDailyMap 日別実績の勤怠時間リスト
	 * @param aggregateTotalWorkingTime 集計総労働時間
	 * @param excessOutsideWorkMng 時間外超過管理
	 * @param prescribedWorkingTimeMonth 月間所定労働時間
	 * @param statutoryWorkingTimeMonth 月間法定労働時間
	 * @param repositories 月次集計が必要とするリポジトリ
	 * @return 戻り値：月別実績を集計する
	 */
	public AggregateMonthlyValue aggregateMonthly(
			String companyId,
			String employeeId,
			YearMonth yearMonth,
			DatePeriod datePeriod,
			WorkingSystem workingSystem,
			MonthlyAggregateAtr aggregateAtr,
			FlexAggregateMethod flexAggregateMethod,
			AggrSettingMonthlyOfFlx aggrSetOfFlex,
			Map<GeneralDate, AttendanceTimeOfDailyPerformance> attendanceTimeOfDailyMap,
			AggregateTotalWorkingTime aggregateTotalWorkingTime,
			ExcessOutsideWorkMng excessOutsideWorkMng,
			AttendanceTimeMonth prescribedWorkingTimeMonth,
			AttendanceTimeMonth statutoryWorkingTimeMonth,
			RepositoriesRequiredByMonthlyAggr repositories){
		
		// 期間．開始日を処理日にする
		GeneralDate procDate = datePeriod.start();
		
		// 処理をする期間の日数分ループ
		while (procDate.beforeOrEquals(datePeriod.end())){
			
			if (attendanceTimeOfDailyMap.containsKey(procDate)){
				val attendanceTimeOfDaily = attendanceTimeOfDailyMap.get(procDate);
			
				// 処理日の職場コードを取得する
				String workplaceId = "empty";
				val affWorkplaceOpt = repositories.getAffWorkplace().findBySid(employeeId, procDate);
				if (affWorkplaceOpt.isPresent()){
					workplaceId = affWorkplaceOpt.get().getWorkplaceId();
				}
				
				// 処理日の雇用コードを取得する
				String employmentCd = "empty";
				val syEmploymentOpt = repositories.getSyEmployment().findByEmployeeId(companyId, employeeId, procDate);
				if (syEmploymentOpt.isPresent()){
					employmentCd = syEmploymentOpt.get().getEmploymentCode();
				}
			
				// 日別実績を集計する　（フレックス時間勤務用）
				val flexTimeDaily = aggregateTotalWorkingTime.aggregateDailyForFlex(attendanceTimeOfDaily, companyId,
						workplaceId, employmentCd, workingSystem, aggregateAtr, aggrSetOfFlex, repositories);
				
				// フレックス時間への集計結果を取得する
				for (val timeSeriesWork : flexTimeDaily.getTimeSeriesWorks().values()){
					this.flexTime.getTimeSeriesWorks().put(timeSeriesWork.getYmd(), timeSeriesWork);
				}
				
				// フレックス時間を集計する
				this.flexTime.aggregate(attendanceTimeOfDaily);
			}
			
			if (aggregateAtr == MonthlyAggregateAtr.EXCESS_OUTSIDE_WORK && excessOutsideWorkMng != null){
			
				// 時間外超過の時、フレックス超過時間を割り当てる
				excessOutsideWorkMng.assignFlexExcessTime(datePeriod, flexAggregateMethod,
						procDate, aggrSetOfFlex, aggregateTotalWorkingTime, this.flexTime,
						prescribedWorkingTimeMonth, statutoryWorkingTimeMonth, repositories);
			}
			
			procDate = procDate.addDays(1);
		}
		
		return AggregateMonthlyValue.of(aggregateTotalWorkingTime, excessOutsideWorkMng);
	}
	
	/**
	 * 月単位の時間を集計する
	 * @param companyId 会社ID
	 * @param employeeId 社員ID
	 * @param yearMonth 年月（度）
	 * @param datePeriod 期間
	 * @param flexAggregateMethod フレックス集計方法
	 * @param workingConditionItem 労働条件項目
	 * @param workplaceId 職場ID
	 * @param employmentCd 雇用コード
	 * @param aggrSetOfFlex フレックス時間勤務の月の集計設定
	 * @param holidayAdditionOpt 休暇加算時間設定
	 * @param aggregateTotalWorkingTime 集計総労働時間
	 * @param prescribedWorkingTimeMonth 月間所定労働時間
	 * @param statutoryWorkingTimeMonth 月間法定労働時間
	 * @param repositories 月次集計が必要とするリポジトリ
	 */
	public void aggregateMonthlyHours(String companyId, String employeeId,
			YearMonth yearMonth, DatePeriod datePeriod,
			FlexAggregateMethod flexAggregateMethod,
			WorkingConditionItem workingConditionItem,
			String workplaceId, String employmentCd,
			AggrSettingMonthlyOfFlx aggrSetOfFlex,
			Optional<HolidayAddtion> holidayAdditionOpt,
			AggregateTotalWorkingTime aggregateTotalWorkingTime,
			AttendanceTimeMonth prescribedWorkingTimeMonth,
			AttendanceTimeMonth statutoryWorkingTimeMonth,
			RepositoriesRequiredByMonthlyAggr repositories){

		// 翌月繰越の時
		if (aggrSetOfFlex.getShortageSet().getCarryforwardSet() == CarryforwardSetInShortageFlex.NEXT_MONTH_CARRYFORWARD){
		
			// 前月の「月別実績の勤怠時間」を取得する
			val prevYearMonth = yearMonth.previousMonth();
			val prevAttendanceTimeList =
					repositories.getAttendanceTimeOfMonthly().findByYearMonthOrderByStartYmd(employeeId, prevYearMonth);
			
			// 前月のフレックス不足時間を取得する　（開始日が最も大きい日のデータ）
			AttendanceTimeMonth prevFlexShortageTime = new AttendanceTimeMonth(0);
			if (!prevAttendanceTimeList.isEmpty()){
				val prevAttendanceTime = prevAttendanceTimeList.get(prevAttendanceTimeList.size() - 1);
				val prevFlexTime = prevAttendanceTime.getMonthlyCalculation().getFlexTime();
				prevFlexShortageTime = new AttendanceTimeMonth(prevFlexTime.getFlexShortageTime().v());
			}
			
			// 前月のフレックス不足時間を当月のフレックス繰越時間にコピーする
			this.flexCarryforwardTime.setFlexCarryforwardTime(
					new AttendanceTimeMonth(prevFlexShortageTime.v()));
		}
		
		if (flexAggregateMethod == FlexAggregateMethod.FOR_CONVENIENCE){
		
			// 便宜上集計をする
			this.aggregateForConvenience(datePeriod);
		}

		if (flexAggregateMethod == FlexAggregateMethod.PRINCIPLE){
			
			// 原則集計をする
			this.aggregatePrinciple(
					companyId, employeeId, yearMonth, datePeriod, workplaceId, employmentCd,
					aggregateTotalWorkingTime, aggrSetOfFlex, holidayAdditionOpt,
					prescribedWorkingTimeMonth, statutoryWorkingTimeMonth,
					repositories);
		}
		
		// 年休・欠勤控除　準備
		this.deductDaysAndTime = new DeductDaysAndTime(
				this.flexShortDeductTime.getAnnualLeaveDeductDays(),
				this.flexShortDeductTime.getAbsenceDeductTime());
		
		// 年休控除する
		this.deductAnnualLeave(companyId, employeeId, datePeriod, flexAggregateMethod,
				workingConditionItem, aggrSetOfFlex, repositories);
		
		// 欠勤控除する
		this.deductAbsence();
	}
	
	/**
	 * 便宜上集計をする
	 * @param datePeriod 期間
	 */
	private void aggregateForConvenience(DatePeriod datePeriod){
		
		// フレックス時間を取得する　→　繰越時間相殺前に入れる
		val carryforwardTimeBeforeOffset = this.flexTime.getTimeSeriesTotalFlexTime(datePeriod, false);
		
		if (carryforwardTimeBeforeOffset.greaterThan(0)){
			
			// フレックス超過の処理をする　（便宜上）
			this.flexExcessForConvenience(carryforwardTimeBeforeOffset);
		}
		else {
			
			// フレックス不足の処理をする　（便宜上）
			this.flexShortageForConvenience(carryforwardTimeBeforeOffset);
		}
	}

	/**
	 * フレックス超過の処理をする　（便宜上）
	 * @param carryforwardTimeBeforeOffset 繰越時間相殺前
	 */
	private void flexExcessForConvenience(AttendanceTimeMonthWithMinus carryforwardTimeBeforeOffset){

		// フレックス繰越時間を取得する
		val carryforwardTime = this.flexCarryforwardTime.getFlexCarryforwardTime();
		val carryforwardWorkTime = this.flexCarryforwardTime.getFlexCarryforwardWorkTime();
		val carryforwardShortageTime = this.flexCarryforwardTime.getFlexCarryforwardShortageTime();
		
		if (carryforwardTime.lessThan(carryforwardTimeBeforeOffset.v())){
			
			// フレックス繰越時間をフレックス繰越勤務時間に加算する
			this.flexCarryforwardTime.setFlexCarryforwardTime(
					carryforwardWorkTime.addMinutes(carryforwardTime.v()));
			
			// 繰越時間相殺前とフレックス繰越時間の差分をフレックス超過時間・フレックス時間に加算する
			val difference = carryforwardTimeBeforeOffset.minusMinutes(carryforwardTime.v());
			this.flexExcessTime = this.flexExcessTime.addMinutes(difference.v());
			this.flexTime.setFlexTime(this.flexTime.getFlexTime().addMinutes(difference.v(), 0));
		}
		else {
			
			// 繰越時間相殺前をフレックス繰越勤務時間に加算する
			this.flexCarryforwardTime.setFlexCarryforwardWorkTime(
					carryforwardWorkTime.addMinutes(carryforwardTimeBeforeOffset.v()));
			
			// フレックス繰越時間と繰越時間相殺前の差分をフレックス繰越不足時間に加算する
			val difference = carryforwardTime.minusMinutes(carryforwardTimeBeforeOffset.v());
			this.flexCarryforwardTime.setFlexCarryforwardShortageTime(
					carryforwardShortageTime.addMinutes(difference.v()));
			
			// フレックス時間を 0 にする
			this.flexTime.setFlexTime(
					TimeMonthWithCalculationAndMinus.ofSameTime(0));
		}
	}
	
	/**
	 * フレックス不足の処理をする　（便宜上）
	 * @param carryforwardTimeBeforeOffset 繰越時間相殺前
	 */
	private void flexShortageForConvenience(AttendanceTimeMonthWithMinus carryforwardTimeBeforeOffset){
		
		// フレックス繰越時間を取得する
		val carryforwardTime = this.flexCarryforwardTime.getFlexCarryforwardTime();
		val carryforwardShortageTime = this.flexCarryforwardTime.getFlexCarryforwardShortageTime();
		
		// フレックス繰越時間をフレックス繰越不足時間に加算する
		this.flexCarryforwardTime.setFlexCarryforwardShortageTime(
				carryforwardShortageTime.addMinutes(carryforwardTime.v()));
		
		// 繰越時間相殺前をフレックス不足時間・フレックス時間に加算する
		this.flexShortageTime = this.flexShortageTime.addMinutes(carryforwardTimeBeforeOffset.v());
		this.flexTime.setFlexTime(this.flexTime.getFlexTime().addMinutes(
				carryforwardTimeBeforeOffset.v(), 0));
	}
	
	/**
	 * 原則集計をする
	 * @param companyId 会社ID
	 * @param employeeId 社員ID
	 * @param yearMonth 年月（度）
	 * @param datePeriod 期間
	 * @param workplaceId 職場ID
	 * @param employmentCd 雇用コード
	 * @param aggregateTotalWorkingTime 総労働時間
	 * @param aggrSetOfFlex フレックス時間勤務の月の集計設定
	 * @param holidayAdditionOpt 休暇加算時間設定
	 * @param prescribedWorkingTimeMonth 月間所定労働時間
	 * @param statutoryWorkingTimeMonth 月間法定労働時間
	 * @param repositories 月次集計が必要とするリポジトリ
	 */
	private void aggregatePrinciple(
			String companyId,
			String employeeId,
			YearMonth yearMonth,
			DatePeriod datePeriod,
			String workplaceId,
			String employmentCd,
			AggregateTotalWorkingTime aggregateTotalWorkingTime,
			AggrSettingMonthlyOfFlx aggrSetOfFlex,
			Optional<HolidayAddtion> holidayAdditionOpt,
			AttendanceTimeMonth prescribedWorkingTimeMonth,
			AttendanceTimeMonth statutoryWorkingTimeMonth,
			RepositoriesRequiredByMonthlyAggr repositories){
		
		// フレックス対象時間を集計する
		val flexTargetTime = this.aggregateFlexTargetTime(datePeriod, aggregateTotalWorkingTime);
		
		// 所定労働時間（代休控除後）を求める
		val compensatoryLeaveAfterDudection =
				this.askCompensatoryLeaveAfterDeduction(companyId, employeeId, yearMonth, datePeriod,
						aggregateTotalWorkingTime, prescribedWorkingTimeMonth, repositories);

		// 加算設定　取得　（所定労働時間計算用）
		val addSet = GetAddSet.get(WorkingSystem.FLEX_TIME_WORK, PremiumAtr.ONLY_LEGAL, holidayAdditionOpt);
		
		// フレックス勤務の加算設定　取得
		Optional<FlexWork> flexWorkSetOpt = Optional.empty();
		if (holidayAdditionOpt.isPresent()){
			flexWorkSetOpt = Optional.of(holidayAdditionOpt.get().getFlexWork());
		}
		
		// フレックス対象時間と所定労働時間（代休控除後）を比較する
		if (flexTargetTime.greaterThanOrEqualTo(compensatoryLeaveAfterDudection.v())){
			
			// 所定以上の処理をする
			this.addedVacationUseTime = this.greaterThanEqualPrescribedProcess(
					datePeriod, flexTargetTime, compensatoryLeaveAfterDudection,
					aggregateTotalWorkingTime, aggrSetOfFlex, addSet, flexWorkSetOpt,
					statutoryWorkingTimeMonth);
		}
		else {
			
			// 所定未満の処理をする
			this.addedVacationUseTime = this.lessThanPrescribedProcess(
					datePeriod, flexTargetTime, compensatoryLeaveAfterDudection,
					aggregateTotalWorkingTime, addSet, flexWorkSetOpt);
		}
		
		// 繰越時間相殺前を求める
		val carryforwardTimeBeforeOffset = flexTargetTime.minusMinutes(compensatoryLeaveAfterDudection.v());
		
		if (carryforwardTimeBeforeOffset.greaterThan(0)){
			
			// フレックス超過の処理をする
			this.flexExcessPrinciple(carryforwardTimeBeforeOffset);
		}
		else {
			
			// 加算設定　取得　（不足時計算用）
			val addSetForShortage = GetAddSet.get(
					WorkingSystem.FLEX_TIME_WORK, PremiumAtr.WHEN_SHORTAGE, holidayAdditionOpt);
			
			// フレックス不足の処理をする
			val addedTimeForShortage = this.flexShortagePrinciple(
					datePeriod, carryforwardTimeBeforeOffset, aggregateTotalWorkingTime, addSetForShortage);
			this.addedVacationUseTime.addMinutesToAddTimePerMonth(addedTimeForShortage.getAddTimePerMonth().v());
		}
	}
	
	/**
	 * フレックス対象時間を集計する
	 * @param datePeriod 期間
	 * @param aggregateTotalWorkingTime 総労働時間
	 * @return フレックス対象時間
	 */
	private AttendanceTimeMonthWithMinus aggregateFlexTargetTime(
			DatePeriod datePeriod,
			AggregateTotalWorkingTime aggregateTotalWorkingTime){
		
		AttendanceTimeMonthWithMinus flexTargetTime = new AttendanceTimeMonthWithMinus(0);
		
		// 合計法定内時間を取得する
		val totalLegalTime = aggregateTotalWorkingTime.getWorkTime().getTimeSeriesTotalLegalTime(datePeriod);
		
		// フレックス対象時間に合計法定内時間（就業時間）を加算する
		flexTargetTime = flexTargetTime.addMinutes(totalLegalTime.v());
				
		// 合計フレックス時間を取得する
		val totalFlexTime = this.flexTime.getTimeSeriesTotalFlexTime(datePeriod, true);
		
		// フレックス対象時間にフレックス時間を加算する
		flexTargetTime = flexTargetTime.addMinutes(totalFlexTime.v());
		
		return flexTargetTime;
	}
	
	/**
	 * 所定労働時間（代休控除後）を求める
	 * @param companyId 会社ID
	 * @param employeeId 社員ID
	 * @param yearMonth 年月（度）
	 * @param datePeriod 期間
	 * @param aggregateTotalWorkingTime 総労働時間
	 * @param prescribedWorkingTimeMonth 月間所定労働時間
	 * @param repositories 月次集計が必要とするリポジトリ
	 * @return 所定労働時間（代休控除後）
	 */
	private AttendanceTimeMonthWithMinus askCompensatoryLeaveAfterDeduction(
			String companyId,
			String employeeId,
			YearMonth yearMonth,
			DatePeriod datePeriod,
			AggregateTotalWorkingTime aggregateTotalWorkingTime,
			AttendanceTimeMonth prescribedWorkingTimeMonth,
			RepositoriesRequiredByMonthlyAggr repositories){
		
		AttendanceTimeMonthWithMinus compensatoryLeaveAfterDeduction = new AttendanceTimeMonthWithMinus(0);
		
		// 所定労働時間を取得する　→　所定労働時間（代休控除後）
		compensatoryLeaveAfterDeduction = new AttendanceTimeMonthWithMinus(prescribedWorkingTimeMonth.v());
		
		// 所定労働時間から代休分を引く
		val compensatoryLeave = aggregateTotalWorkingTime.getVacationUseTime().getCompensatoryLeave();
		compensatoryLeave.aggregate(datePeriod);
		compensatoryLeaveAfterDeduction = compensatoryLeaveAfterDeduction.minusMinutes(compensatoryLeave.getUseTime().v());
		
		return compensatoryLeaveAfterDeduction;
	}
	
	/**
	 * 所定以上の処理をする
	 * @param datePeriod 期間
	 * @param flexTargetTime フレックス対象時間
	 * @param compensatoryLeaveAfterDudection 所定労働時間（代休控除後）
	 * @param aggregateTotalWorkingTime 総労働時間
	 * @param aggrSetOfFlex フレックス時間勤務の月の集計設定
	 * @param addSet 加算設定
	 * @param addSetOfFlexOpt フレックス勤務の加算設定
	 * @param statutoryWorkingTimeMonth 月間法定労働時間
	 * @return 加算した休暇使用時間
	 */
	private AddedVacationUseTime greaterThanEqualPrescribedProcess(
			DatePeriod datePeriod,
			AttendanceTimeMonthWithMinus flexTargetTime,
			AttendanceTimeMonthWithMinus compensatoryLeaveAfterDudection,
			AggregateTotalWorkingTime aggregateTotalWorkingTime,
			AggrSettingMonthlyOfFlx aggrSetOfFlex,
			AddSet addSet,
			Optional<FlexWork> addSetOfFlexOpt,
			AttendanceTimeMonth statutoryWorkingTimeMonth){

		AddedVacationUseTime addedVacationUseTime = new AddedVacationUseTime();
		
		// 集計設定を確認する
		val aggregateSet = aggrSetOfFlex.getLegalAggregateSet().getAggregateTimeSet().getAggregateSet();
		
		if (aggregateSet == AggregateSetting.INCLUDE_ALL_OUTSIDE_TIME_IN_FLEX_TIME){
			
			// （差分を）全て法定外フレックス時間として計算する
			// ※　差分　＝　フレックス対象時間　－　所定労働時間（代休控除後）
			val difference = flexTargetTime.minusMinutes(compensatoryLeaveAfterDudection.v());
			this.flexTime.setIllegalFlexTime(
					this.flexTime.getIllegalFlexTime().addMinutes(difference.v()));
		}
		if (aggregateSet == AggregateSetting.MANAGE_DETAIL){
			
			// 法定内・法定外フレックス時間に分けて計算する
			this.divideLegalAndIllegal(flexTargetTime, compensatoryLeaveAfterDudection,
					statutoryWorkingTimeMonth);
		}
		
		// 「フレックス勤務の加算設定．月次法定内のみ加算」を確認する
		boolean isAdd = false;
		//*****（未）　対象メンバがない。漏れ？
		//addSetOfFlexOpt...
		if (isAdd){
			
			// 休暇加算時間を取得する
			val vacationAddTime = GetVacationAddTime.getTime(datePeriod,
					aggregateTotalWorkingTime.getVacationUseTime(), addSet);
		
			// 休暇加算時間を法定内フレックス時間に加算する
			this.flexTime.setLegalFlexTime(
					this.flexTime.getLegalFlexTime().addMinutes(vacationAddTime.v()));
		
			// 加算した休暇加算時間を「加算した休暇使用時間」に退避しておく
			addedVacationUseTime = AddedVacationUseTime.of(vacationAddTime);
		}
		
		return addedVacationUseTime;
	}
	
	/**
	 * 法定内・法定外フレックス時間に分けて計算する
	 * @param flexTargetTime フレックス対象時間
	 * @param compensatoryLeaveAfterDudection 所定労働時間（代休控除後）
	 * @param statutoryWorkingTimeMonth 月間法定労働時間
	 */
	private void divideLegalAndIllegal(
			AttendanceTimeMonthWithMinus flexTargetTime,
			AttendanceTimeMonthWithMinus compensatoryLeaveAfterDudection,
			AttendanceTimeMonth statutoryWorkingTimeMonth){
		
		// 法定労働時間を取得する
		val statutoryWorkingtime = new AttendanceTimeMonthWithMinus(statutoryWorkingTimeMonth.v());
		
		if (flexTargetTime.greaterThan(statutoryWorkingtime.v())){
			
			// フレックス対象時間と法定労働時間の差分を法定外フレックス時間に加算する
			val diffIllegal = flexTargetTime.minusMinutes(statutoryWorkingtime.v());
			this.flexTime.setIllegalFlexTime(
					this.flexTime.getIllegalFlexTime().addMinutes(diffIllegal.v()));
			
			// 所定労働時間（代休控除後）と法定労働時間の差分を法定内フレックス時間に加算する
			val diffLegal = statutoryWorkingtime.minusMinutes(compensatoryLeaveAfterDudection.v());
			this.flexTime.setLegalFlexTime(
					this.flexTime.getLegalFlexTime().addMinutes(diffLegal.v()));
		}
		else {
			
			// フレックス対象時間と所定労働時間（代休控除後）の差分を法定内フレックス時間に加算する
			val diffLegal = flexTargetTime.minusMinutes(compensatoryLeaveAfterDudection.v());
			this.flexTime.setLegalFlexTime(
					this.flexTime.getLegalFlexTime().addMinutes(diffLegal.v()));
		}
	}
	
	/**
	 * 所定未満の処理をする
	 * @param datePeriod 期間
	 * @param flexTargetTime フレックス対象時間
	 * @param compensatoryLeaveAfterDudection 所定労働時間（代休控除後）
	 * @param aggregateTotalWorkingTime 総労働時間
	 * @param addSet 加算設定
	 * @param addSetOfFlexOpt フレックス勤務の加算設定
	 * @return 加算した休暇使用時間
	 */
	private AddedVacationUseTime lessThanPrescribedProcess(
			DatePeriod datePeriod,
			AttendanceTimeMonthWithMinus flexTargetTime,
			AttendanceTimeMonthWithMinus compensatoryLeaveAfterDudection,
			AggregateTotalWorkingTime aggregateTotalWorkingTime,
			AddSet addSet,
			Optional<FlexWork> addSetOfFlexOpt){
		
		AddedVacationUseTime addedVacationUseTime = new AddedVacationUseTime();
		
		// 「フレックス勤務の加算設定．月次法定内のみ加算」を確認する
		boolean isAdd = false;
		//*****（未）　対象メンバがない。漏れ？
		//addSetOfFlexOpt...
		if (isAdd){
			
			// 休暇加算時間を取得する
			val vacationAddTime = GetVacationAddTime.getTime(datePeriod,
					aggregateTotalWorkingTime.getVacationUseTime(), addSet);
		
			// フレックス対象時間と休暇加算時間を合計する　→　休暇使用時間含
			val includeVacationUseTime = flexTargetTime.addMinutes(vacationAddTime.v());
		
			// 休暇使用時間含と所定労働時間（代休控除後）を比較する
			if (includeVacationUseTime.greaterThan(compensatoryLeaveAfterDudection.v())){
				
				// 所定労働時間（代休控除後）とフレックス対象時間の差分を加算した休暇使用時間に退避しておく
				val diffFlex = compensatoryLeaveAfterDudection.minusMinutes(flexTargetTime.v());
				addedVacationUseTime = AddedVacationUseTime.of(new AttendanceTimeMonth(diffFlex.v()));
				
				// 休暇使用時間含と所定労働時間（代休控除後）の差分を法定内フレックス時間に加算する
				val diffLegalFlex = includeVacationUseTime.minusMinutes(compensatoryLeaveAfterDudection.v());
				this.flexTime.setLegalFlexTime(
						this.flexTime.getLegalFlexTime().addMinutes(diffLegalFlex.v()));
			}
			else {
				
				// 休暇加算時間を加算した休暇使用時間に退避しておく
				addedVacationUseTime = AddedVacationUseTime.of(vacationAddTime);
			}
		}
		
		return addedVacationUseTime;
	}

	/**
	 * フレックス超過の処理をする　（原則）
	 * @param carryforwardTimeBeforeOffset 繰越時間相殺前
	 */
	private void flexExcessPrinciple(AttendanceTimeMonthWithMinus carryforwardTimeBeforeOffset){

		// フレックス繰越時間を取得する
		val carryforwardTime = this.flexCarryforwardTime.getFlexCarryforwardTime();
		val carryforwardWorkTime = this.flexCarryforwardTime.getFlexCarryforwardWorkTime();
		val carryforwardShortageTime = this.flexCarryforwardTime.getFlexCarryforwardShortageTime();
		
		if (carryforwardTime.lessThan(carryforwardTimeBeforeOffset.v())){
			
			// フレックス繰越時間をフレックス繰越勤務時間に加算する
			this.flexCarryforwardTime.setFlexCarryforwardTime(
					carryforwardWorkTime.addMinutes(carryforwardTime.v()));
			
			// 繰越時間相殺前とフレックス繰越時間の差分をフレックス超過時間・フレックス時間に加算する
			val difference = carryforwardTimeBeforeOffset.minusMinutes(carryforwardTime.v());
			this.flexExcessTime = this.flexExcessTime.addMinutes(difference.v());
			this.flexTime.setFlexTime(this.flexTime.getFlexTime().addMinutes(difference.v(), 0));
		}
		else {
			
			// 繰越時間相殺前をフレックス繰越勤務時間に加算する
			this.flexCarryforwardTime.setFlexCarryforwardWorkTime(
					carryforwardWorkTime.addMinutes(carryforwardTimeBeforeOffset.v()));
			
			// フレックス繰越時間と繰越時間相殺前の差分をフレックス繰越不足時間に加算する
			val difference = carryforwardTime.minusMinutes(carryforwardTimeBeforeOffset.v());
			this.flexCarryforwardTime.setFlexCarryforwardShortageTime(
					carryforwardShortageTime.addMinutes(difference.v()));
			
			// フレックス時間を 0 にする
			this.flexTime.setFlexTime(TimeMonthWithCalculationAndMinus.ofSameTime(0));
		}
	}
	
	/**
	 * フレックス不足の処理をする　（原則）
	 * @param datePeriod 期間
	 * @param carryforwardTimeBeforeOffset 繰越時間相殺前
	 * @param aggregateTotalWorkingTime 総労働時間
	 * @param addSet 加算設定
	 * @return 加算した休暇使用時間
	 */
	private AddedVacationUseTime flexShortagePrinciple(
			DatePeriod datePeriod,
			AttendanceTimeMonthWithMinus carryforwardTimeBeforeOffset,
			AggregateTotalWorkingTime aggregateTotalWorkingTime,
			AddSet addSet){
		
		AddedVacationUseTime addedVacationUseTime = new AddedVacationUseTime();
		
		// フレックス繰越時間を取得する
		val carryforwardTime = this.flexCarryforwardTime.getFlexCarryforwardTime();
		val carryforwardShortageTime = this.flexCarryforwardTime.getFlexCarryforwardShortageTime();
		
		// フレックス繰越時間をフレックス繰越不足時間に加算する
		this.flexCarryforwardTime.setFlexCarryforwardShortageTime(
				carryforwardShortageTime.addMinutes(carryforwardTime.v()));
		
		// 繰越時間相殺前を休暇加算後にコピーする
		AttendanceTimeMonthWithMinus afterAddVacation =
				new AttendanceTimeMonthWithMinus(carryforwardTimeBeforeOffset.v());
		
		// 休暇加算後＜ 0 なら、不足分を加算する
		if (afterAddVacation.lessThan(0)){
			
			// 休暇加算時間を取得する
			val vacationAddTime = GetVacationAddTime.getTime(datePeriod,
					aggregateTotalWorkingTime.getVacationUseTime(), addSet);
			
			// 休暇加算後に休暇加算時間を加算する
			afterAddVacation = afterAddVacation.addMinutes(vacationAddTime.v());
			if (afterAddVacation.lessThanOrEqualTo(0)){
				
				// 休暇加算後が 0 以下なら、休暇加算時間全体を「加算した休暇使用時間」とする
				addedVacationUseTime = AddedVacationUseTime.of(vacationAddTime);
			}
			else {
				
				// 休暇加算後が0を超える時、休暇加算後を 0 とし、休暇加算後と相殺前の差分を「加算した休暇使用時間」とする
				afterAddVacation = new AttendanceTimeMonthWithMinus(0);
				val difference = afterAddVacation.minusMinutes(carryforwardTimeBeforeOffset.v());
				addedVacationUseTime = AddedVacationUseTime.of(new AttendanceTimeMonth(difference.v()));
			}
		}
		
		// 休暇加算後をフレックス不足時間に加算する
		// ※　休暇加算後はマイナス値なので、減算により、加算扱いにする。
		this.flexShortageTime = this.flexShortageTime.minusMinutes(afterAddVacation.v());

		// 繰越時間相殺前をフレックス時間に加算する
		this.flexTime.setFlexTime(this.flexTime.getFlexTime().addMinutes(
				carryforwardTimeBeforeOffset.v(), 0));
		
		return addedVacationUseTime;
	}
	
	/**
	 * 年休控除する
	 * @param companyId 会社ID
	 * @param employeeId 社員ID
	 * @param period 期間
	 * @param flexAggregateMethod フレックス集計方法
	 * @param workingConditionItem 労働条件項目
	 * @param aggrSetOfFlex フレックス時間勤務の月の集計設定
	 * @param repositories 月次集計が必要とするリポジトリ
	 */
	private void deductAnnualLeave(
			String companyId,
			String employeeId,
			DatePeriod period,
			FlexAggregateMethod flexAggregateMethod,
			WorkingConditionItem workingConditionItem,
			AggrSettingMonthlyOfFlx aggrSetOfFlex,
			RepositoriesRequiredByMonthlyAggr repositories){
		
		// 「控除前のフレックス不足時間」を入れておく
		this.flexShortDeductTime.setFlexShortTimeBeforeDeduct(this.flexShortageTime);
		
		// 年休控除日数を時間換算する
		this.deductDaysAndTime.timeConversionOfDeductAnnualLeaveDays(
				companyId, employeeId, period, workingConditionItem, repositories);
		if (this.deductDaysAndTime.getErrorMessageIds().size() > 0){
			return;
		}
		
		// フレックス不足時間を年休控除する
		if (this.flexShortageTime.greaterThan(0)){
			AttendanceTimeMonth subtractTime = this.deductDaysAndTime.getAnnualLeaveDeductTime();
			if (subtractTime.greaterThan(this.flexShortageTime.v())){
				subtractTime = this.flexShortageTime;
			}
			// フレックス不足時間から年休控除時間を引く
			this.flexShortageTime = this.flexShortageTime.minusMinutes(subtractTime.v());
			// 引いた分を年休控除時間から引く
			this.deductDaysAndTime.minusMinutesToAnnualLeaveDeductTime(subtractTime.v());
			// 引いた分をフレックス時間に足す
			int flexMinutes = this.flexTime.getFlexTime().getTime().v();
			if (flexMinutes < 0){
				int addMinutes = subtractTime.v();
				if (addMinutes > -flexMinutes) addMinutes = -flexMinutes;
				this.flexTime.getFlexTime().setTime(new AttendanceTimeMonthWithMinus(flexMinutes + addMinutes));
			}
		}
		if (this.deductDaysAndTime.getAnnualLeaveDeductTime().lessThanOrEqualTo(0)) return;
		
		// 「フレックス繰越不足時間」を年休控除する
		val flexCarryforwardShortTime = this.flexCarryforwardTime.getFlexCarryforwardShortageTime();
		if (flexCarryforwardShortTime.greaterThan(0)){
			AttendanceTimeMonth subtractTime = this.deductDaysAndTime.getAnnualLeaveDeductTime();
			if (subtractTime.greaterThan(flexCarryforwardShortTime.v())){
				subtractTime = flexCarryforwardShortTime;
			}
			// フレックス繰越不足時間から年休控除時間を引く
			this.flexCarryforwardTime.setFlexCarryforwardShortageTime(
					flexCarryforwardShortTime.minusMinutes(subtractTime.v()));
			// 引いた分を年休控除時間から引く
			this.deductDaysAndTime.minusMinutesToAnnualLeaveDeductTime(subtractTime.v());
		}
		if (this.deductDaysAndTime.getAnnualLeaveDeductTime().lessThanOrEqualTo(0)) return;
		
		// 法定内・法定外フレックス時間に加算する
		if (flexAggregateMethod == FlexAggregateMethod.PRINCIPLE){
			val aggrSet = aggrSetOfFlex.getLegalAggregateSet().getAggregateTimeSet().getAggregateSet();
			if (aggrSet == AggregateSetting.INCLUDE_ALL_OUTSIDE_TIME_IN_FLEX_TIME){
				// 年休控除時間を法定外フレックス時間に加算する
				this.flexTime.setIllegalFlexTime(this.flexTime.getIllegalFlexTime().addMinutes(
						this.deductDaysAndTime.getAnnualLeaveDeductTime().v()));
			}
			if (aggrSet == AggregateSetting.MANAGE_DETAIL){
				// 年休控除時間を法定内フレックス時間に加算する
				this.flexTime.setLegalFlexTime(this.flexTime.getLegalFlexTime().addMinutes(
						this.deductDaysAndTime.getAnnualLeaveDeductTime().v()));
			}
		}
		// 年休控除時間をフレックス時間に加算する
		int flexMinutes = this.flexTime.getFlexTime().getTime().v();
		if (flexMinutes < 0){
			int addMinutes = this.deductDaysAndTime.getAnnualLeaveDeductTime().v();
			if (addMinutes > -flexMinutes) addMinutes = -flexMinutes;
			this.flexTime.getFlexTime().setTime(new AttendanceTimeMonthWithMinus(flexMinutes + addMinutes));
		}
	}
	
	/**
	 * 欠勤控除する
	 */
	private void deductAbsence(){
		
		// フレックス不足時間を欠勤控除する
		if (this.flexShortageTime.greaterThan(0)){
			AttendanceTimeMonth subtractTime = this.deductDaysAndTime.getAbsenceDeductTime();
			if (subtractTime.greaterThan(this.flexShortageTime.v())){
				subtractTime = this.flexShortageTime;
			}
			// フレックス不足時間から欠勤控除時間を引く
			this.flexShortageTime = this.flexShortageTime.minusMinutes(subtractTime.v());
			// 引いた分を欠勤控除時間から引く
			this.deductDaysAndTime.minusMinutesToAbsenceDeductTime(subtractTime.v());
			// 引いた分をフレックス時間に足す
			int flexMinutes = this.flexTime.getFlexTime().getTime().v();
			if (flexMinutes < 0){
				int addMinutes = subtractTime.v();
				if (addMinutes > -flexMinutes) addMinutes = -flexMinutes;
				this.flexTime.getFlexTime().setTime(new AttendanceTimeMonthWithMinus(flexMinutes + addMinutes));
			}
		}
		if (this.deductDaysAndTime.getAbsenceDeductTime().lessThanOrEqualTo(0)) return;
		
		// フレックス繰越不足時間を欠勤控除する
		val flexCarryforwardShortTime = this.flexCarryforwardTime.getFlexCarryforwardShortageTime();
		if (flexCarryforwardShortTime.greaterThan(0)){
			AttendanceTimeMonth subtractTime = this.deductDaysAndTime.getAbsenceDeductTime();
			if (subtractTime.greaterThan(flexCarryforwardShortTime.v())){
				subtractTime = flexCarryforwardShortTime;
			}
			// フレックス繰越不足時間から欠勤控除時間を引く
			this.flexCarryforwardTime.setFlexCarryforwardShortageTime(
					flexCarryforwardShortTime.minusMinutes(subtractTime.v()));
			// 引いた分を欠勤控除時間から引く
			this.deductDaysAndTime.minusMinutesToAbsenceDeductTime(subtractTime.v());
		}
	}
}
