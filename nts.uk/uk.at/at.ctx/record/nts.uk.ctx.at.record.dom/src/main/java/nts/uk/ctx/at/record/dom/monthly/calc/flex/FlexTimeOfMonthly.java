package nts.uk.ctx.at.record.dom.monthly.calc.flex;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import lombok.Getter;
import lombok.val;
import nts.arc.diagnose.stopwatch.concurrent.ConcurrentStopwatches;
import nts.arc.layer.dom.AggregateRoot;
import nts.arc.time.GeneralDate;
import nts.arc.time.YearMonth;
import nts.uk.ctx.at.record.dom.monthly.TimeMonthWithCalculationAndMinus;
import nts.uk.ctx.at.record.dom.monthly.calc.AggregateMonthlyValue;
import nts.uk.ctx.at.record.dom.monthly.calc.MonthlyAggregateAtr;
import nts.uk.ctx.at.record.dom.monthly.calc.MonthlyCalculation;
import nts.uk.ctx.at.record.dom.monthly.calc.totalworkingtime.AggregateTotalWorkingTime;
import nts.uk.ctx.at.record.dom.monthly.erroralarm.Flex;
import nts.uk.ctx.at.record.dom.monthly.workform.flex.MonthlyAggrSetOfFlex;
import nts.uk.ctx.at.record.dom.monthlyaggrmethod.flex.AggregateSetting;
import nts.uk.ctx.at.record.dom.monthlyaggrmethod.flex.CarryforwardSetInShortageFlex;
import nts.uk.ctx.at.record.dom.monthlyaggrmethod.flex.FlexAggregateMethod;
import nts.uk.ctx.at.record.dom.monthlyprocess.aggr.MonthlyAggregationErrorInfo;
import nts.uk.ctx.at.record.dom.monthlyprocess.aggr.export.DeductDaysAndTime;
import nts.uk.ctx.at.record.dom.monthlyprocess.aggr.work.MonAggrCompanySettings;
import nts.uk.ctx.at.record.dom.monthlyprocess.aggr.work.MonAggrEmployeeSettings;
import nts.uk.ctx.at.record.dom.monthlyprocess.aggr.work.MonthlyCalculatingDailys;
import nts.uk.ctx.at.record.dom.monthlyprocess.aggr.work.RepositoriesRequiredByMonthlyAggr;
import nts.uk.ctx.at.record.dom.monthlyprocess.aggr.work.SettingRequiredByFlex;
import nts.uk.ctx.at.record.dom.monthlyprocess.aggr.work.excessoutside.ExcessOutsideWorkMng;
import nts.uk.ctx.at.record.dom.monthlyprocess.aggr.work.premiumtarget.AddedVacationUseTime;
import nts.uk.ctx.at.record.dom.monthlyprocess.aggr.work.premiumtarget.getvacationaddtime.AddSet;
import nts.uk.ctx.at.record.dom.monthlyprocess.aggr.work.premiumtarget.getvacationaddtime.GetAddSet;
import nts.uk.ctx.at.record.dom.monthlyprocess.aggr.work.premiumtarget.getvacationaddtime.GetVacationAddTime;
import nts.uk.ctx.at.record.dom.monthlyprocess.aggr.work.premiumtarget.getvacationaddtime.PremiumAtr;
import nts.uk.ctx.at.record.dom.weekly.AttendanceTimeOfWeekly;
import nts.uk.ctx.at.record.dom.workrecord.monthcal.FlexMonthWorkTimeAggrSet;
import nts.uk.ctx.at.record.dom.workrecord.workperfor.dailymonthlyprocessing.ErrMessageContent;
import nts.uk.ctx.at.shared.dom.calculation.holiday.WorkFlexAdditionSet;
import nts.uk.ctx.at.shared.dom.common.days.AttendanceDaysMonth;
import nts.uk.ctx.at.shared.dom.common.time.AttendanceTimeMonth;
import nts.uk.ctx.at.shared.dom.common.time.AttendanceTimeMonthWithMinus;
import nts.uk.ctx.at.shared.dom.statutory.worktime.shared.WeekStart;
import nts.uk.ctx.at.shared.dom.workingcondition.WorkingConditionItem;
import nts.uk.ctx.at.shared.dom.workingcondition.WorkingSystem;
import nts.uk.ctx.at.shared.dom.workrule.closure.Closure;
import nts.uk.ctx.at.shared.dom.workrule.closure.ClosureId;
import nts.uk.ctx.at.shared.dom.workrule.statutoryworktime.flex.GetFlexPredWorkTime;
import nts.uk.ctx.at.shared.dom.workrule.statutoryworktime.flex.ReferencePredTimeOfFlex;
import nts.uk.shr.com.enumcommon.NotUseAtr;
import nts.uk.shr.com.i18n.TextResource;
import nts.uk.shr.com.time.calendar.date.ClosureDate;
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
	
	/** フレックス時間勤務の月の集計設定 */
	private FlexMonthWorkTimeAggrSet flexAggrSet;
	/** 休暇加算時間設定 */
	private Map<String, AggregateRoot> holidayAdditionMap;
	/** 月次法定内のみ加算 */
	private boolean addMonthlyWithinStatutory;
	/** フレックス勤務の月別集計設定 */
	private Optional<MonthlyAggrSetOfFlex> monthlyAggrSetOfFlexOpt;
	/** フレックス勤務所定労働時間取得 */
	private Optional<GetFlexPredWorkTime> getFlexPredWorkTimeOpt; 
	/** 加算した休暇使用時間 */
	private AddedVacationUseTime addedVacationUseTime;
	/** 控除の日数と時間 */
	private DeductDaysAndTime deductDaysAndTime;
	/** 控除前の年休控除時間 */
	private AttendanceTimeMonth annualLeaveTimeBeforeDeduct;
	/** エラー情報リスト */
	private List<MonthlyAggregationErrorInfo> errorInfos;
	/** 社員の月別実績のエラー詳細リスト */
	private List<Flex> perErrors; 
	
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
		
		this.flexAggrSet = null;
		this.holidayAdditionMap = new HashMap<>();
		this.addMonthlyWithinStatutory = false;
		this.monthlyAggrSetOfFlexOpt = Optional.empty();
		this.getFlexPredWorkTimeOpt = Optional.empty();
		this.addedVacationUseTime = new AddedVacationUseTime();
		this.deductDaysAndTime = new DeductDaysAndTime(
				new AttendanceDaysMonth(0.0), new AttendanceTimeMonth(0));
		this.annualLeaveTimeBeforeDeduct = new AttendanceTimeMonth(0);
		this.errorInfos = new ArrayList<>();
		this.perErrors = new ArrayList<>();
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
		domain.flexShortDeductTime = flexShortDeductTime;
		return domain;
	}
	
	/**
	 * 月別実績を集計する
	 * @param companyId 会社ID
	 * @param employeeId 社員ID
	 * @param yearMonth 年月（度）
	 * @param closureId 締めID
	 * @param closureDate 締め日付
	 * @param datePeriod 期間
	 * @param workingSystem 労働制
	 * @param aggregateAtr 集計区分
	 * @param closureOpt 締め
	 * @param flexAggregateMethod フレックス集計方法
	 * @param settingsByFlex フレックス勤務が必要とする設定
	 * @param aggregateTotalWorkingTime 集計総労働時間
	 * @param excessOutsideWorkMng 時間外超過管理
	 * @param startWeekNo 開始週NO
	 * @param companySets 月別集計で必要な会社別設定
	 * @param employeeSets 月別集計で必要な社員別設定
	 * @param monthlyCalcDailys 月の計算中の日別実績データ
	 * @param repositories 月次集計が必要とするリポジトリ
	 * @return 戻り値：月別実績を集計する
	 */
	public AggregateMonthlyValue aggregateMonthly(
			String companyId,
			String employeeId,
			YearMonth yearMonth,
			ClosureId closureId,
			ClosureDate closureDate,
			DatePeriod datePeriod,
			WorkingSystem workingSystem,
			MonthlyAggregateAtr aggregateAtr,
			Optional<Closure> closureOpt,
			FlexAggregateMethod flexAggregateMethod,
			SettingRequiredByFlex settingsByFlex,
			AggregateTotalWorkingTime aggregateTotalWorkingTime,
			ExcessOutsideWorkMng excessOutsideWorkMng,
			int startWeekNo,
			MonAggrCompanySettings companySets,
			MonAggrEmployeeSettings employeeSets,
			MonthlyCalculatingDailys monthlyCalcDailys,
			RepositoriesRequiredByMonthlyAggr repositories){
		
		this.flexAggrSet = settingsByFlex.getFlexAggrSet();
		this.monthlyAggrSetOfFlexOpt = settingsByFlex.getMonthlyAggrSetOfFlexOpt();
		this.getFlexPredWorkTimeOpt = settingsByFlex.getGetFlexPredWorkTimeOpt();
		
		List<AttendanceTimeOfWeekly> resultWeeks = new ArrayList<>();
		
		// 期間．開始日を処理日にする
		GeneralDate procDate = datePeriod.start();
		int procWeekNo = startWeekNo;
		
		// 「処理中の週開始日」を期間．開始日にする
		GeneralDate procWeekStartDate = datePeriod.start();
		
		// 処理をする期間の日数分ループ
		val attendanceTimeOfDailyMap = monthlyCalcDailys.getAttendanceTimeOfDailyMap();
		while (procDate.beforeOrEquals(datePeriod.end())){
			
			if (attendanceTimeOfDailyMap.containsKey(procDate)){
				val attendanceTimeOfDaily = attendanceTimeOfDailyMap.get(procDate);
			
				// 処理日の職場コードを取得する
				String workplaceId = "empty";
				val affWorkplaceOpt = employeeSets.getWorkplace(procDate);
				if (affWorkplaceOpt.isPresent()){
					workplaceId = affWorkplaceOpt.get().getWorkplaceId();
				}
				
				// 処理日の雇用コードを取得する
				String employmentCd = "empty";
				val employmentOpt = employeeSets.getEmployment(procDate);
				if (employmentOpt.isPresent()){
					employmentCd = employmentOpt.get().getEmploymentCode();
				}
			
				ConcurrentStopwatches.start("12222.3:日別実績の集計：");
				
				// 日別実績を集計する　（フレックス時間勤務用）
				val flexTimeDaily = aggregateTotalWorkingTime.aggregateDailyForFlex(attendanceTimeOfDaily,
						companyId, workplaceId, employmentCd, workingSystem, aggregateAtr, settingsByFlex);

				ConcurrentStopwatches.stop("12222.3:日別実績の集計：");
				
				// フレックス時間への集計結果を取得する
				for (val timeSeriesWork : flexTimeDaily.getTimeSeriesWorks().values()){
					this.flexTime.getTimeSeriesWorks().put(timeSeriesWork.getYmd(), timeSeriesWork);
				}
				
				// フレックス時間を集計する
				this.flexTime.aggregate(attendanceTimeOfDaily);
			}

			// 週の集計をする日か確認する
			if (MonthlyCalculation.isAggregateWeek(procDate, WeekStart.TighteningStartDate, datePeriod, closureOpt)){
			
				// 週の期間を計算
				DatePeriod weekAggrPeriod = new DatePeriod(procWeekStartDate, procDate);
				
				// 翌週の開始日を設定しておく
				procWeekStartDate = procDate.addDays(1);
				
				// 対象の「週別実績の勤怠時間」を作成する
				val newWeek = new AttendanceTimeOfWeekly(employeeId, yearMonth, closureId, closureDate,
						procWeekNo, weekAggrPeriod);
				procWeekNo += 1;
				
				// 週別実績を集計する
				{
					ConcurrentStopwatches.start("12222.4:週別実績の集計：");
					
					// 週の計算
					val weekCalc = newWeek.getWeeklyCalculation();
					weekCalc.aggregate(companyId, employeeId, yearMonth, weekAggrPeriod,
							workingSystem, aggregateAtr,
							null, null, aggregateTotalWorkingTime,
							WeekStart.TighteningStartDate, new AttendanceTimeMonth(0),
							attendanceTimeOfDailyMap, companySets, repositories);
					resultWeeks.add(newWeek);
					if (weekCalc.getErrorInfos().size() > 0) this.errorInfos.addAll(weekCalc.getErrorInfos());

					ConcurrentStopwatches.stop("12222.4:週別実績の集計：");
					
					// 集計区分を確認する
					if (aggregateAtr == MonthlyAggregateAtr.EXCESS_OUTSIDE_WORK && excessOutsideWorkMng != null){
					
						ConcurrentStopwatches.start("12222.5:時間外超過の集計：");
						
						// 時間外超過の集計
						newWeek.getExcessOutside().aggregate(weekCalc, companySets);
						
						ConcurrentStopwatches.stop("12222.5:時間外超過の集計：");
					}
				}
			}
			
			if (aggregateAtr == MonthlyAggregateAtr.EXCESS_OUTSIDE_WORK && excessOutsideWorkMng != null){

				ConcurrentStopwatches.start("12222.6:超過時間割り当て：");
				
				// 時間外超過の時、フレックス超過時間を割り当てる
				excessOutsideWorkMng.assignFlexExcessTime(datePeriod, flexAggregateMethod,
						procDate, this.flexAggrSet, aggregateTotalWorkingTime, this.flexTime,
						settingsByFlex.getPrescribedWorkingTimeMonth(),
						settingsByFlex.getStatutoryWorkingTimeMonth(),
						repositories);
				
				ConcurrentStopwatches.stop("12222.6:超過時間割り当て：");
			}
			
			procDate = procDate.addDays(1);
		}
		
		return AggregateMonthlyValue.of(aggregateTotalWorkingTime, excessOutsideWorkMng, new ArrayList<>());
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
	 * @param settingsByFlex フレックス勤務が必要とする設定
	 * @param aggregateTotalWorkingTime 集計総労働時間
	 * @param repositories 月次集計が必要とするリポジトリ
	 */
	public void aggregateMonthlyHours(String companyId, String employeeId,
			YearMonth yearMonth, DatePeriod datePeriod,
			FlexAggregateMethod flexAggregateMethod,
			WorkingConditionItem workingConditionItem,
			String workplaceId, String employmentCd,
			SettingRequiredByFlex settingsByFlex,
			AggregateTotalWorkingTime aggregateTotalWorkingTime,
			RepositoriesRequiredByMonthlyAggr repositories){

		this.flexAggrSet = settingsByFlex.getFlexAggrSet();
		this.holidayAdditionMap = settingsByFlex.getHolidayAdditionMap();
		this.monthlyAggrSetOfFlexOpt = settingsByFlex.getMonthlyAggrSetOfFlexOpt();
		
		// 「月次法定内のみ加算」を確認する
		this.addMonthlyWithinStatutory = false;
		if (this.holidayAdditionMap.containsKey("flexWork")){
			val setOfFlex = (WorkFlexAdditionSet)this.holidayAdditionMap.get("flexWork");
			val workTimeCalcMethod = setOfFlex.getVacationCalcMethodSet().getWorkTimeCalcMethodOfHoliday();
			if (workTimeCalcMethod.getAdvancedSet().isPresent()){
				val includeVacationSet = workTimeCalcMethod.getAdvancedSet().get().getIncludeVacationSet();
				if (includeVacationSet.getAdditionWithinMonthlyStatutory().isPresent()){
					if (includeVacationSet.getAdditionWithinMonthlyStatutory().get() == NotUseAtr.USE){
						this.addMonthlyWithinStatutory = true;
					}
				}
			}
		}
		else {
			this.errorInfos.add(new MonthlyAggregationErrorInfo(
					"012", new ErrMessageContent(TextResource.localize("Msg_1233"))));
			return;
		}
		
		// 翌月繰越の時
		if (this.flexAggrSet.getInsufficSet().getCarryforwardSet() == CarryforwardSetInShortageFlex.NEXT_MONTH_CARRYFORWARD){
		
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
					aggregateTotalWorkingTime,
					settingsByFlex.getPrescribedWorkingTimeMonth(),
					settingsByFlex.getStatutoryWorkingTimeMonth());
		}
		
		// 年休・欠勤控除　準備
		this.deductDaysAndTime = new DeductDaysAndTime(
				this.flexShortDeductTime.getAnnualLeaveDeductDays(),
				this.flexShortDeductTime.getAbsenceDeductTime());
		
		// 年休控除する
		this.deductAnnualLeave(companyId, employeeId, datePeriod, flexAggregateMethod,
				workingConditionItem, repositories);
		
		// 欠勤控除する
		this.deductAbsence();
		
		// フレックス補填のエラーチェック
		this.checkErrorForInsufficientFlex(settingsByFlex);
	}
	
	/**
	 * 便宜上集計をする
	 * @param datePeriod 期間
	 */
	private void aggregateForConvenience(DatePeriod datePeriod){
		
		// フレックス時間の計算
		{
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
		
		// 計算フレックス時間の計算
		{
			// 計算フレックス時間を取得する　→　繰越時間相殺前
			val totalCalcFlexTime = this.flexTime.getTimeSeriesTotalCalcFlexTime(datePeriod, false);
			if (totalCalcFlexTime.greaterThan(0)){
				
				// 計算フレックス超過の処理をする　（便宜上）
				this.calcFlexExcessForConvenience(totalCalcFlexTime);
			}
			else {
				
				// 計算フレックス不足の処理をする　（便宜上）
				this.calcFlexShortageForConvenience(totalCalcFlexTime);
			}
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
			this.flexCarryforwardTime.setFlexCarryforwardWorkTime(
					carryforwardWorkTime.addMinutes(carryforwardTime.v()));
			
			// 繰越時間相殺前とフレックス繰越時間の差分をフレックス超過時間・フレックス時間に加算する
			val difference = carryforwardTimeBeforeOffset.minusMinutes(carryforwardTime.v());
			this.flexExcessTime = this.flexExcessTime.addMinutes(difference.v());
			this.flexTime.setFlexTime(this.flexTime.getFlexTime().addMinutes(difference.v(), 0));
			
			// 法定外フレックス時間にフレックス時間をセットする
			this.flexTime.setIllegalFlexTime(this.flexTime.getFlexTime().getTime());
			this.flexTime.setLegalFlexTime(new AttendanceTimeMonthWithMinus(0));
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
			this.flexTime.setFlexTime(new TimeMonthWithCalculationAndMinus(
					new AttendanceTimeMonthWithMinus(0),
					this.flexTime.getFlexTime().getCalcTime()));
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
		
		// 繰越時間相殺前をフレックス不足時間に加算する
		// ※　繰越時間相殺前はマイナス値なので、減算により、加算扱いにする。
		this.flexShortageTime = this.flexShortageTime.minusMinutes(carryforwardTimeBeforeOffset.v());
		
		// 繰越時間相殺前をフレックス時間に加算する
		this.flexTime.setFlexTime(this.flexTime.getFlexTime().addMinutes(
				carryforwardTimeBeforeOffset.v(), 0));
	}

	/**
	 * 計算フレックス超過の処理をする　（便宜上）
	 * @param carryforwardTimeBeforeOffset 繰越時間相殺前
	 */
	private void calcFlexExcessForConvenience(AttendanceTimeMonthWithMinus carryforwardTimeBeforeOffset){

		// フレックス繰越時間を取得する
		val carryforwardTime = this.flexCarryforwardTime.getFlexCarryforwardTime();
		
		if (carryforwardTime.lessThan(carryforwardTimeBeforeOffset.v())){
			
			// 繰越時間相殺前とフレックス繰越時間の差分を計算フレックス時間に加算する
			int difference = carryforwardTimeBeforeOffset.v() - carryforwardTime.v();
			this.flexTime.setFlexTime(this.flexTime.getFlexTime().addMinutes(0, difference));
		}
		else {
			
			// 計算フレックス時間を 0 にする
			this.flexTime.setFlexTime(new TimeMonthWithCalculationAndMinus(
					this.flexTime.getFlexTime().getTime(),
					new AttendanceTimeMonthWithMinus(0)));
		}
	}
	
	/**
	 * 計算フレックス不足の処理をする　（便宜上）
	 * @param carryforwardTimeBeforeOffset 繰越時間相殺前
	 */
	private void calcFlexShortageForConvenience(AttendanceTimeMonthWithMinus carryforwardTimeBeforeOffset){
		
		// 繰越時間相殺前を計算フレックス時間に加算する
		this.flexTime.setFlexTime(this.flexTime.getFlexTime().addMinutes(
				0, carryforwardTimeBeforeOffset.v()));
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
	 * @param prescribedWorkingTimeMonth 月間所定労働時間
	 * @param statutoryWorkingTimeMonth 月間法定労働時間
	 */
	private void aggregatePrinciple(
			String companyId,
			String employeeId,
			YearMonth yearMonth,
			DatePeriod datePeriod,
			String workplaceId,
			String employmentCd,
			AggregateTotalWorkingTime aggregateTotalWorkingTime,
			AttendanceTimeMonth prescribedWorkingTimeMonth,
			AttendanceTimeMonth statutoryWorkingTimeMonth){
		
		// フレックス時間の計算
		{
			// フレックス対象時間を集計する
			val flexTargetTime = this.aggregateFlexTargetTime(datePeriod, aggregateTotalWorkingTime);
			
			// 所定労働時間（代休控除後）を求める
			val compensatoryLeaveAfterDudection =
					this.askCompensatoryLeaveAfterDeduction(companyId, employeeId, yearMonth, datePeriod,
							aggregateTotalWorkingTime, prescribedWorkingTimeMonth);

			// 繰越時間相殺前を求める
			val carryforwardTimeBeforeOffset = flexTargetTime.minusMinutes(compensatoryLeaveAfterDudection.v());
			
			if (carryforwardTimeBeforeOffset.greaterThan(0)){
				
				// フレックス超過の処理をする
				this.flexExcessPrinciple(carryforwardTimeBeforeOffset, compensatoryLeaveAfterDudection,
						datePeriod, aggregateTotalWorkingTime, statutoryWorkingTimeMonth);
			}
			else {
				
				// 加算設定　取得　（不足時計算用）
				val addSetForShortage = GetAddSet.get(
						WorkingSystem.FLEX_TIME_WORK, PremiumAtr.WHEN_SHORTAGE, this.holidayAdditionMap);
				if (addSetForShortage.getErrorInfo().isPresent()){
					this.errorInfos.add(addSetForShortage.getErrorInfo().get());
				}
				
				// フレックス不足の処理をする
				val addedTimeForShortage = this.flexShortagePrinciple(
						datePeriod, carryforwardTimeBeforeOffset, aggregateTotalWorkingTime, addSetForShortage);
				this.addedVacationUseTime.addMinutesToAddTimePerMonth(addedTimeForShortage.getAddTimePerMonth().v());
			}
		}
		
		// 計算フレックス時間の計算
		{
			// 計算フレックス対象時間を求める
			int calcflexTargetMinutes = this.aggregateCalcFlexTargetTime(datePeriod, aggregateTotalWorkingTime).v();
			
			// 所定労働時間（代休控除後）を求める
			int compensatoryLeaveAfterDudection =
					this.askCompensatoryLeaveAfterDeduction(companyId, employeeId, yearMonth, datePeriod,
							aggregateTotalWorkingTime, prescribedWorkingTimeMonth).v();

			// 繰越時間相殺前を求める
			int carryforwardTimeBeforeOffset = calcflexTargetMinutes - compensatoryLeaveAfterDudection;
			
			if (carryforwardTimeBeforeOffset > 0){
				
				// フレックス超過の処理をする
				this.calcFlexExcessPrinciple(new AttendanceTimeMonthWithMinus(carryforwardTimeBeforeOffset));
			}
			else {
				
				// フレックス不足の処理をする
				this.calcFlexShortagePrinciple(datePeriod,
						new AttendanceTimeMonthWithMinus(carryforwardTimeBeforeOffset), aggregateTotalWorkingTime);
			}
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
		
		// 合計法定内実働時間を取得する
		val totalLegalTime = aggregateTotalWorkingTime.getWorkTime().getTimeSeriesTotalLegalActualTime(datePeriod);
		
		// フレックス対象時間に合計法定内時間（就業時間）を加算する
		flexTargetTime = flexTargetTime.addMinutes(totalLegalTime.v());
				
		// 合計フレックス時間を取得する
		val totalFlexTime = this.flexTime.getTimeSeriesTotalFlexTime(datePeriod, true);
		
		// フレックス対象時間にフレックス時間を加算する
		flexTargetTime = flexTargetTime.addMinutes(totalFlexTime.v());
		
		// 「月次法定内のみ加算」を確認する
		AttendanceTimeMonth vacationAddTime = new AttendanceTimeMonth(0);
		if (!this.addMonthlyWithinStatutory){
			// 加算しない
			
			// 加算設定　取得　（割増用）
			val addSetWhenPremium = GetAddSet.get(
					WorkingSystem.FLEX_TIME_WORK, PremiumAtr.PREMIUM, this.holidayAdditionMap);
			if (addSetWhenPremium.getErrorInfo().isPresent()){
				this.errorInfos.add(addSetWhenPremium.getErrorInfo().get());
			}
			
			// 加算する休暇時間を取得する
			vacationAddTime = GetVacationAddTime.getTime(
					datePeriod, aggregateTotalWorkingTime.getVacationUseTime(), addSetWhenPremium);
		}
		else {
			// 加算する
			
			// 加算設定　取得　（法定内のみ用）
			val addSetWhenOnlyLegal = GetAddSet.get(
					WorkingSystem.FLEX_TIME_WORK, PremiumAtr.ONLY_LEGAL, this.holidayAdditionMap);
			if (addSetWhenOnlyLegal.getErrorInfo().isPresent()){
				this.errorInfos.add(addSetWhenOnlyLegal.getErrorInfo().get());
			}
			
			// 加算する休暇時間を取得する
			vacationAddTime = GetVacationAddTime.getTime(
					datePeriod, aggregateTotalWorkingTime.getVacationUseTime(), addSetWhenOnlyLegal);
		}

		// フレックス対象時間に休暇加算時間を加算する
		flexTargetTime = flexTargetTime.addMinutes(vacationAddTime.v());

		// 「休暇加算時間」を「加算した休暇使用時間」に退避しておく
		this.addedVacationUseTime.addMinutesToAddTimePerMonth(vacationAddTime.v());
		
		return flexTargetTime;
	}
	
	/**
	 * 計算フレックス対象時間を求める
	 * @param datePeriod 期間
	 * @param aggregateTotalWorkingTime 総労働時間
	 * @return フレックス対象時間
	 */
	private AttendanceTimeMonthWithMinus aggregateCalcFlexTargetTime(
			DatePeriod datePeriod,
			AggregateTotalWorkingTime aggregateTotalWorkingTime){
		
		int calcFlexTargetMinutes = 0;
		
		// 合計法定内実働時間を取得する
		val totalLegalTime = aggregateTotalWorkingTime.getWorkTime().getTimeSeriesTotalLegalActualTime(datePeriod);
		
		// 計算フレックス対象時間に合計法定内時間（就業時間）を加算する
		calcFlexTargetMinutes += totalLegalTime.v();
				
		// 合計計算フレックス時間を取得する
		int totalCalcFlexMinutes = this.flexTime.getTimeSeriesTotalCalcFlexTime(datePeriod, true).v();
		
		// 計算フレックス対象時間に合計計算フレックス時間を加算する
		calcFlexTargetMinutes += totalCalcFlexMinutes;
		
		// 「月次法定内のみ加算」を確認する
		int vacationAddMinutes = 0;
		if (!this.addMonthlyWithinStatutory){
			// 加算しない
			
			// 加算設定　取得　（割増用）
			val addSetWhenPremium = GetAddSet.get(
					WorkingSystem.FLEX_TIME_WORK, PremiumAtr.PREMIUM, this.holidayAdditionMap);
			if (addSetWhenPremium.getErrorInfo().isPresent()){
				this.errorInfos.add(addSetWhenPremium.getErrorInfo().get());
			}
			
			// 加算する休暇時間を取得する
			vacationAddMinutes = GetVacationAddTime.getTime(
					datePeriod, aggregateTotalWorkingTime.getVacationUseTime(), addSetWhenPremium).v();
		}
		else {
			// 加算する
			
			// 加算設定　取得　（法定内のみ用）
			val addSetWhenOnlyLegal = GetAddSet.get(
					WorkingSystem.FLEX_TIME_WORK, PremiumAtr.ONLY_LEGAL, this.holidayAdditionMap);
			if (addSetWhenOnlyLegal.getErrorInfo().isPresent()){
				this.errorInfos.add(addSetWhenOnlyLegal.getErrorInfo().get());
			}
			
			// 加算する休暇時間を取得する
			vacationAddMinutes = GetVacationAddTime.getTime(
					datePeriod, aggregateTotalWorkingTime.getVacationUseTime(), addSetWhenOnlyLegal).v();
		}

		// 計算フレックス対象時間に休暇加算時間を加算する
		calcFlexTargetMinutes += vacationAddMinutes;
		
		return new AttendanceTimeMonthWithMinus(calcFlexTargetMinutes);
	}
	
	/**
	 * 所定労働時間（代休控除後）を求める
	 * @param companyId 会社ID
	 * @param employeeId 社員ID
	 * @param yearMonth 年月（度）
	 * @param datePeriod 期間
	 * @param aggregateTotalWorkingTime 総労働時間
	 * @param prescribedWorkingTimeMonth 月間所定労働時間
	 * @return 所定労働時間（代休控除後）
	 */
	private AttendanceTimeMonthWithMinus askCompensatoryLeaveAfterDeduction(
			String companyId,
			String employeeId,
			YearMonth yearMonth,
			DatePeriod datePeriod,
			AggregateTotalWorkingTime aggregateTotalWorkingTime,
			AttendanceTimeMonth prescribedWorkingTimeMonth){
		
		AttendanceTimeMonthWithMinus compensatoryLeaveAfterDeduction = new AttendanceTimeMonthWithMinus(0);
	
		// 「フレックス勤務所定労働時間取得」を取得する
		if (!this.getFlexPredWorkTimeOpt.isPresent()){
			
			// エラー処理　（計算準備での読み込みでエラー発生するので、このタイミングでは発生しない）
			this.getFlexPredWorkTimeOpt = Optional.of(new GetFlexPredWorkTime(companyId));
		}
		val getFlexPredWorkTime = this.getFlexPredWorkTimeOpt.get();
			
		// 「フレックス勤務所定労働時間取得」を確認する
		if (getFlexPredWorkTime.getReference() == ReferencePredTimeOfFlex.FROM_MASTER){
			// マスタから参照
			
			// 所定労働時間を取得する
			compensatoryLeaveAfterDeduction = new AttendanceTimeMonthWithMinus(prescribedWorkingTimeMonth.v());
		}
		else {
			// 実績から参照
			
			// 実績から計画所定労働時間を取得する
			compensatoryLeaveAfterDeduction = new AttendanceTimeMonthWithMinus(
					aggregateTotalWorkingTime.getPrescribedWorkingTime()
							.getTotalSchedulePrescribedWorkingTime(datePeriod).v());
		}
		
		// 休出をフレックスに含めるか確認する
		// ※　本来は、「法定外休出時間をフレックス時間に含める」を確認する仕様だが、クラス属性が未実装のため、仮対応　2018.11.17 shuichi_ishida
		val compensatoryLeave = aggregateTotalWorkingTime.getVacationUseTime().getCompensatoryLeave();
		compensatoryLeave.aggregate(datePeriod);
		int cmpLeaveUseMinutes = compensatoryLeave.getUseTime().v();
		if (this.flexAggrSet.getIncludeOverTime() == NotUseAtr.USE){
			
			// 法定内代休時間の計算　（含める場合、引く代休時間は法定内のみにする）
			cmpLeaveUseMinutes = compensatoryLeave.calcLegalTime(datePeriod).v();
		}
		
		// 所定労働時間から代休分を引く
		compensatoryLeaveAfterDeduction = compensatoryLeaveAfterDeduction.minusMinutes(cmpLeaveUseMinutes);
		if (compensatoryLeaveAfterDeduction.lessThan(0)){
			compensatoryLeaveAfterDeduction = new AttendanceTimeMonthWithMinus(0);
		}
		
		return compensatoryLeaveAfterDeduction;
	}

	/**
	 * フレックス超過の処理をする　（原則）
	 * @param carryforwardTimeBeforeOffset 繰越時間相殺前
	 * @param compensatoryLeaveAfterDudection 所定労働時間（代休控除後）
	 * @param datePeriod 期間
	 * @param aggregateTotalWorkingTime 総労働時間
	 * @param statutoryWorkingTimeMonth 月間法定労働時間
	 */
	private void flexExcessPrinciple(
			AttendanceTimeMonthWithMinus carryforwardTimeBeforeOffset,
			AttendanceTimeMonthWithMinus compensatoryLeaveAfterDudection,
			DatePeriod datePeriod,
			AggregateTotalWorkingTime aggregateTotalWorkingTime,
			AttendanceTimeMonth statutoryWorkingTimeMonth){

		// フレックス繰越時間を取得する
		val carryforwardTime = this.flexCarryforwardTime.getFlexCarryforwardTime();
		val carryforwardWorkTime = this.flexCarryforwardTime.getFlexCarryforwardWorkTime();
		val carryforwardShortageTime = this.flexCarryforwardTime.getFlexCarryforwardShortageTime();
		
		if (carryforwardTime.lessThan(carryforwardTimeBeforeOffset.v())){
			
			// フレックス繰越時間をフレックス繰越勤務時間に加算する
			this.flexCarryforwardTime.setFlexCarryforwardWorkTime(
					carryforwardWorkTime.addMinutes(carryforwardTime.v()));
			
			// 繰越時間相殺前とフレックス繰越時間の差分をフレックス超過時間・フレックス時間に加算する
			val difference = carryforwardTimeBeforeOffset.minusMinutes(carryforwardTime.v());
			this.flexExcessTime = this.flexExcessTime.addMinutes(difference.v());
			this.flexTime.setFlexTime(this.flexTime.getFlexTime().addMinutes(difference.v(), 0));
			
			// 法定内・法定外フレックス時間を求める
			val aggrSet = this.flexAggrSet.getLegalAggrSet().getAggregateSet();
			if (aggrSet == AggregateSetting.INCLUDE_ALL_OUTSIDE_TIME_IN_FLEX_TIME){
				// 全て法定外フレックス時間として計算する
				
				// 「フレックス時間」を「法定外フレックス時間」に入れる
				this.flexTime.setIllegalFlexTime(new AttendanceTimeMonthWithMinus(
						this.flexTime.getFlexTime().getTime().v()));
			}
			else {
				// 法定内・法定外フレックス時間に分けて計算する
				
				// 法定労働時間から代休分を引く
				val compensatoryLeave = aggregateTotalWorkingTime.getVacationUseTime().getCompensatoryLeave();
				compensatoryLeave.aggregate(datePeriod);
				int statutoryAfterDeduct = statutoryWorkingTimeMonth.v() - compensatoryLeave.getUseTime().v();
				if (statutoryAfterDeduct < 0) statutoryAfterDeduct = 0;
				
				// 法定内として扱う時間を求める
				int treatLegal = statutoryAfterDeduct - compensatoryLeaveAfterDudection.v() - this.flexCarryforwardTime.getFlexCarryforwardWorkTime().v();
				if (treatLegal < 0) treatLegal = 0;
				
				// 「月次法定内のみ加算」を確認する
				if (this.addMonthlyWithinStatutory){
					// 大塚モードで処理をする
					
					// 加算設定　取得　（法定内のみ用）
					val addSetWhenOnlyLegal = GetAddSet.get(
							WorkingSystem.FLEX_TIME_WORK, PremiumAtr.ONLY_LEGAL, this.holidayAdditionMap);
					if (addSetWhenOnlyLegal.getErrorInfo().isPresent()){
						this.errorInfos.add(addSetWhenOnlyLegal.getErrorInfo().get());
					}
					
					// 休暇加算時間を取得する
					val vacationAddTime = GetVacationAddTime.getTime(
							datePeriod, aggregateTotalWorkingTime.getVacationUseTime(), addSetWhenOnlyLegal);
					
					// 「フレックス時間」と「休暇加算時間」を比較する
					if (this.flexTime.getFlexTime().getTime().greaterThan(vacationAddTime.v())){
						
						// 「休暇加算時間」を「法定内フレックス時間」に入れる
						this.flexTime.setLegalFlexTime(new AttendanceTimeMonthWithMinus(
								this.flexTime.getLegalFlexTime().v() + vacationAddTime.v()));
						
						// 「フレックス時間（休暇加算前）」を求める
						int beforeAddVacation = this.flexTime.getFlexTime().getTime().v() - vacationAddTime.v();
						
						// 「フレックス時間（休暇加算前）」と「法定内として扱う時間」を比較する
						if (beforeAddVacation <= treatLegal){
							
							// 「フレックス時間（休暇加算前）」を「法定内フレックス時間」に加算する
							this.flexTime.setLegalFlexTime(new AttendanceTimeMonthWithMinus(
									this.flexTime.getLegalFlexTime().v() + beforeAddVacation));
						}
						else {
							
							// 「法定内として扱う時間」を「法定内フレックス時間」に加算する
							this.flexTime.setLegalFlexTime(new AttendanceTimeMonthWithMinus(
									this.flexTime.getLegalFlexTime().v() + treatLegal));
							
							// 「法定外フレックス時間」を求める
							this.flexTime.setIllegalFlexTime(new AttendanceTimeMonthWithMinus(
									beforeAddVacation - treatLegal));
						}
					}
					else {
						
						// フレックス時間を「法定内フレックス時間」に入れる
						this.flexTime.setLegalFlexTime(new AttendanceTimeMonthWithMinus(
								this.flexTime.getLegalFlexTime().v() + this.flexTime.getFlexTime().getTime().v()));
					}
				}
				else {
					// 標準モードで処理をする
					
					// 「フレックス時間」と「法定内として扱う時間」を比較する
					if (this.flexTime.getFlexTime().getTime().lessThanOrEqualTo(treatLegal)){

						// 「フレックス時間」を「法定内フレックス時間」に入れる
						this.flexTime.setLegalFlexTime(this.flexTime.getFlexTime().getTime());
					}
					else {

						// 「法定内として扱う時間」を「法定内フレックス時間」に入れる
						this.flexTime.setLegalFlexTime(new AttendanceTimeMonthWithMinus(treatLegal));
						
						// 「法定外フレックス時間」を求める
						this.flexTime.setIllegalFlexTime(new AttendanceTimeMonthWithMinus(
								this.flexTime.getFlexTime().getTime().v() - treatLegal));
					}
				}
			}
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
			this.flexTime.setFlexTime(new TimeMonthWithCalculationAndMinus(
					new AttendanceTimeMonthWithMinus(0),
					this.flexTime.getFlexTime().getCalcTime()));
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
			
			// 「月次法定内のみ加算」を確認する
			if (!this.addMonthlyWithinStatutory){
				// 「加算しない」時、不足分を加算する

				// 加算設定　取得　（不足時用）
				val addSetWhenShortage = GetAddSet.get(
						WorkingSystem.FLEX_TIME_WORK, PremiumAtr.WHEN_SHORTAGE, this.holidayAdditionMap);
				if (addSetWhenShortage.getErrorInfo().isPresent()){
					this.errorInfos.add(addSetWhenShortage.getErrorInfo().get());
				}
				
				// 休暇加算時間を取得する
				val vacationAddTime = GetVacationAddTime.getTime(
						datePeriod, aggregateTotalWorkingTime.getVacationUseTime(), addSetWhenShortage);
				
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
		}
		
		// 休暇加算後をフレックス不足時間に加算する
		// ※　休暇加算後はマイナス値なので、減算により、加算扱いにする。
		this.flexShortageTime = this.flexShortageTime.minusMinutes(afterAddVacation.v());

		// 繰越時間相殺前をフレックス時間に加算する
		this.flexTime.setFlexTime(this.flexTime.getFlexTime().addMinutes(
                carryforwardTimeBeforeOffset.v(), 0));
//				afterAddVacation.v(), 0));
		
		return addedVacationUseTime;
	}

	/**
	 * 計算フレックス超過の処理をする　（原則）
	 * @param carryforwardTimeBeforeOffset 繰越時間相殺前
	 */
	private void calcFlexExcessPrinciple(
			AttendanceTimeMonthWithMinus carryforwardTimeBeforeOffset){

		// フレックス繰越時間を取得する
		int carryforwardMinutes = this.flexCarryforwardTime.getFlexCarryforwardTime().v();
		
		if (carryforwardMinutes < carryforwardTimeBeforeOffset.v()){
			
			// 繰越時間相殺前とフレックス繰越時間の差分を計算フレックス時間に加算する
			int difference = carryforwardTimeBeforeOffset.v() - carryforwardMinutes;
			this.flexTime.setFlexTime(this.flexTime.getFlexTime().addMinutes(0, difference));
		}
		else {
			
			// 計算フレックス時間を 0 にする
			this.flexTime.setFlexTime(new TimeMonthWithCalculationAndMinus(
					this.flexTime.getFlexTime().getTime(),
					new AttendanceTimeMonthWithMinus(0)));
		}
	}
	
	/**
	 * 計算フレックス不足の処理をする　（原則）
	 * @param datePeriod 期間
	 * @param carryforwardTimeBeforeOffset 繰越時間相殺前
	 * @param aggregateTotalWorkingTime 総労働時間
	 * @return 加算した休暇使用時間
	 */
	private AddedVacationUseTime calcFlexShortagePrinciple(
			DatePeriod datePeriod,
			AttendanceTimeMonthWithMinus carryforwardTimeBeforeOffset,
			AggregateTotalWorkingTime aggregateTotalWorkingTime){
		
		AddedVacationUseTime addedVacationUseTime = new AddedVacationUseTime();
		
		// 休暇加算前を確認する　←　繰越時間相殺前
		int beforeAddVacation = carryforwardTimeBeforeOffset.v();
		
		// 休暇加算前＜ 0 なら、不足分を加算する
		if (beforeAddVacation < 0){
			
			// 「月次法定内のみ加算」を確認する
			if (!this.addMonthlyWithinStatutory){
				// 「加算しない」時、不足分を加算する

				// 加算設定　取得　（不足時用）
				val addSetWhenShortage = GetAddSet.get(
						WorkingSystem.FLEX_TIME_WORK, PremiumAtr.WHEN_SHORTAGE, this.holidayAdditionMap);
				if (addSetWhenShortage.getErrorInfo().isPresent()){
					this.errorInfos.add(addSetWhenShortage.getErrorInfo().get());
				}
				
				// 休暇加算時間を取得する
				int vacationAddMinutes = GetVacationAddTime.getTime(
						datePeriod, aggregateTotalWorkingTime.getVacationUseTime(), addSetWhenShortage).v();
				
				// 休暇加算前に休暇加算時間を加算する　→　休暇加算後
				// ※　使わないので不要
				//int afterAddVacation = beforeAddVacation + vacationAddMinutes;
				
				// 加算した「休暇加算時間」を「加算した休暇使用時間」に退避しておく
				addedVacationUseTime = AddedVacationUseTime.of(new AttendanceTimeMonth(vacationAddMinutes));
			}
		}
		
		// 繰越時間相殺前を計算フレックス時間に加算する
		this.flexTime.setFlexTime(this.flexTime.getFlexTime().addMinutes(
				0, carryforwardTimeBeforeOffset.v()));
		
		return addedVacationUseTime;
	}
	
	/**
	 * フレックス勤務の就業時間を求める
	 * @param companyId 会社ID
	 * @param employeeId 社員ID
	 * @param yearMonth 年月（度）
	 * @param datePeriod 期間
	 * @param flexAggregateMethod フレックス集計方法
	 * @param settingsByFlex フレックス勤務が必要とする設定
	 * @param aggregateTotalWorkingTime 集計総労働時間
	 * @return 就業時間
	 */
	public Optional<AttendanceTimeMonth> askWorkTimeOfFlex(
			String companyId,
			String employeeId,
			YearMonth yearMonth,
			DatePeriod datePeriod,
			FlexAggregateMethod flexAggregateMethod,
			SettingRequiredByFlex settingsByFlex,
			AggregateTotalWorkingTime aggregateTotalWorkingTime){
		
		// 「フレックス集計方法」を確認する　（原則集計かどうか）
		if (flexAggregateMethod == FlexAggregateMethod.PRINCIPLE){
			
			// 「フレ超過時間」OR「フレ繰越勤務時間」に値が入っているか確認する
			if (this.flexExcessTime.greaterThan(0) ||
				this.flexCarryforwardTime.getFlexCarryforwardWorkTime().greaterThan(0)){
				
				// 設定上の所定労働時間を確認する
				val prescribedWorkingTimeSet = settingsByFlex.getPrescribedWorkingTimeMonth();
				
				// 所定労働時間を求める
				val prescribedWorkingTime =
						this.askCompensatoryLeaveAfterDeduction(companyId, employeeId, yearMonth, datePeriod,
								aggregateTotalWorkingTime, prescribedWorkingTimeSet);
				int predMinutes = prescribedWorkingTime.v();
				if (predMinutes < 0) predMinutes = 0;
				
				// 所定労働時間を返す　（呼び出し元で就業時間に入れる）
				return Optional.of(new AttendanceTimeMonth(predMinutes));
			}
		}
		
		return Optional.empty();
	}
	
	/**
	 * 年休控除する
	 * @param companyId 会社ID
	 * @param employeeId 社員ID
	 * @param period 期間
	 * @param flexAggregateMethod フレックス集計方法
	 * @param workingConditionItem 労働条件項目
	 * @param flexAggrSet フレックス時間勤務の月の集計設定
	 * @param repositories 月次集計が必要とするリポジトリ
	 */
	private void deductAnnualLeave(
			String companyId,
			String employeeId,
			DatePeriod period,
			FlexAggregateMethod flexAggregateMethod,
			WorkingConditionItem workingConditionItem,
			RepositoriesRequiredByMonthlyAggr repositories){
		
		// 「控除前のフレックス不足時間」を入れておく
		this.flexShortDeductTime.setFlexShortTimeBeforeDeduct(this.flexShortageTime);
		
		// 年休控除日数を時間換算する
		this.deductDaysAndTime.timeConversionOfDeductAnnualLeaveDays(
				companyId, employeeId, period, workingConditionItem, repositories);
		if (this.deductDaysAndTime.getErrorInfos().size() > 0){
			this.errorInfos.addAll(this.deductDaysAndTime.getErrorInfos());
			return;
		}
		
		// 年休控除日数に値が入っているか確認する
		if (this.flexShortDeductTime.getAnnualLeaveDeductDays().lessThanOrEqualTo(0.0)) return;
		
		// 控除前の年休控除時間を保存する
		this.annualLeaveTimeBeforeDeduct = this.deductDaysAndTime.getAnnualLeaveDeductTime();
		
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
			// 引いた分をフレックス繰越勤務時間に足す
			this.flexCarryforwardTime.setFlexCarryforwardWorkTime(new AttendanceTimeMonth(
					this.flexCarryforwardTime.getFlexCarryforwardWorkTime().v() + subtractTime.v()));
		}
		
		// 年休控除時間が残っているか確認する
		if (this.deductDaysAndTime.getAnnualLeaveDeductTime().lessThanOrEqualTo(0)) return;
		
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
		
		// 年休控除時間が残っているか確認する
		if (this.deductDaysAndTime.getAnnualLeaveDeductTime().lessThanOrEqualTo(0)) return;
		
		// 法定内・法定外フレックス時間に加算する
		{
			// 「フレックス集計方法」を確認すす
			if (flexAggregateMethod == FlexAggregateMethod.PRINCIPLE){
				val aggrSet = this.flexAggrSet.getLegalAggrSet().getAggregateSet();
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
			int addMinutes = this.deductDaysAndTime.getAnnualLeaveDeductTime().v();
			this.flexTime.getFlexTime().setTime(new AttendanceTimeMonthWithMinus(flexMinutes + addMinutes));
		}
		
		int flexMinutes = this.flexTime.getFlexTime().getTime().v();
		if (flexMinutes > 0){
			
			// フレックス時間をフレックス超過時間へ移送
			this.flexExcessTime = new AttendanceTimeMonth(flexMinutes);
		}
	}
	
	/**
	 * 欠勤控除する
	 */
	private void deductAbsence(){
		
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
		
		// 欠勤控除時間が残っているか確認する
		if (this.deductDaysAndTime.getAbsenceDeductTime().lessThanOrEqualTo(0)) return;
		
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
	}
	
	/**
	 * フレックス補填のエラーチェック
	 * @param settingsByFlex フレックス勤務が必要とする設定
	 */
	private void checkErrorForInsufficientFlex(SettingRequiredByFlex settingsByFlex){
		
		// フレックス不足の年休補填管理を取得
		val insufficientFlexOpt = settingsByFlex.getInsufficientFlexOpt();
		if (insufficientFlexOpt.isPresent()){
			val insufficientFlex = insufficientFlexOpt.get();
			
			// 年休補填時間のエラーチェック
			val deductDays = new nts.uk.ctx.at.shared.dom.common.days.AttendanceDaysMonth(
					this.flexShortDeductTime.getAnnualLeaveDeductDays().v());
			if (insufficientFlex.checkErrorForSupplementableDays(deductDays)){
				
				// 社員の月別実績のエラーを作成する
				if (!this.perErrors.contains(Flex.FLEX_YEAR_HOLIDAY_DEDUCTIBLE_DAYS)){
					this.perErrors.add(Flex.FLEX_YEAR_HOLIDAY_DEDUCTIBLE_DAYS);
				}
			}
		}
		
		// フレックス不足時間のエラーチェック
		boolean shortageError = false;
		{
			// 社員のフレックス繰越上限時間を求める
			int limitTime = 15 * 60;		// 繰越上限時間
			{
				val flexShortageLimitOpt = settingsByFlex.getFlexShortageLimitOpt();
				if (flexShortageLimitOpt.isPresent()){
					limitTime = flexShortageLimitOpt.get().getLimitTime().v();
				}
				int possibleTime = settingsByFlex.getCanNextCarryforwardTimeMonth().v();	// 翌月繰越可能時間
				if (limitTime > possibleTime) limitTime = possibleTime;
			}
			
			// 「フレックス不足時間」と繰越上限時間を比較
			if (this.flexShortageTime.v() > limitTime) shortageError = true;
			
			if (shortageError){
				
				// 社員の月別実績のエラーを作成する
				if (!this.perErrors.contains(Flex.FLEX_EXCESS_CARRYOVER_TIME)){
					this.perErrors.add(Flex.FLEX_EXCESS_CARRYOVER_TIME);
				}
			}
		}
		
		// 前月繰越の補填未完了のエラーチェック
		val flexCarryforwardShortTime = this.flexCarryforwardTime.getFlexCarryforwardShortageTime();
		if (flexCarryforwardShortTime.v() > 0){
			
			// 社員の月別実績のエラーを作成する
			if (!this.perErrors.contains(Flex.FLEX_INCOMPLETE_OF_CARRYFORWARD)){
				this.perErrors.add(Flex.FLEX_INCOMPLETE_OF_CARRYFORWARD);
			}
		}
	}
	
	/**
	 * 総労働対象時間の取得
	 * @return 総労働対象時間
	 */
	public AttendanceTimeMonth getTotalWorkingTargetTime(){
		
		return new AttendanceTimeMonth(this.flexExcessTime.v() +
				this.flexCarryforwardTime.getFlexCarryforwardWorkTime().v());
	}
	
	/**
	 * 合算する
	 * @param target 加算対象
	 */
	public void sum(FlexTimeOfMonthly target){
		
		this.flexTime.sum(target.flexTime);
		this.flexExcessTime = this.flexExcessTime.addMinutes(target.flexExcessTime.v());
		this.flexShortageTime = this.flexShortageTime.addMinutes(target.flexShortageTime.v());
		this.flexCarryforwardTime.sum(target.flexCarryforwardTime);
		this.flexTimeOfExcessOutsideTime.sum(target.flexTimeOfExcessOutsideTime);
		this.flexShortDeductTime.sum(target.flexShortDeductTime);
	}
}
