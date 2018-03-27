package nts.uk.ctx.at.record.dom.monthly.calc.actualworkingtime;

import java.util.Map;
import java.util.Optional;

import lombok.Getter;
import lombok.Setter;
import lombok.val;
import nts.arc.time.GeneralDate;
import nts.arc.time.YearMonth;
import nts.uk.ctx.at.record.dom.actualworkinghours.AttendanceTimeOfDailyPerformance;
import nts.uk.ctx.at.record.dom.monthly.calc.AggregateMonthlyValue;
import nts.uk.ctx.at.record.dom.monthly.calc.MonthlyAggregateAtr;
import nts.uk.ctx.at.record.dom.monthly.calc.totalworkingtime.AggregateTotalWorkingTime;
import nts.uk.ctx.at.record.dom.monthlyaggrmethod.AggrSettingMonthly;
import nts.uk.ctx.at.record.dom.monthlyaggrmethod.legaltransferorder.LegalTransferOrderSetOfAggrMonthly;
import nts.uk.ctx.at.record.dom.monthlyaggrmethod.regularandirregular.LegalAggrSetOfIrg;
import nts.uk.ctx.at.record.dom.monthlyprocess.aggr.work.RepositoriesRequiredByMonthlyAggr;
import nts.uk.ctx.at.record.dom.monthlyprocess.aggr.work.excessoutside.ExcessOutsideWorkMng;
import nts.uk.ctx.at.record.dom.monthlyprocess.aggr.work.premiumtarget.AddedVacationUseTime;
import nts.uk.ctx.at.record.dom.monthlyprocess.aggr.work.premiumtarget.IrregularPeriodCarryforwardsTimeOfCurrent;
import nts.uk.ctx.at.record.dom.monthlyprocess.aggr.work.premiumtarget.TargetPremiumTimeMonthOfIrregular;
import nts.uk.ctx.at.record.dom.monthlyprocess.aggr.work.premiumtarget.TargetPremiumTimeMonthOfRegular;
import nts.uk.ctx.at.record.dom.monthlyprocess.aggr.work.premiumtarget.getvacationaddtime.AddSet;
import nts.uk.ctx.at.record.dom.monthlyprocess.aggr.work.premiumtarget.getvacationaddtime.GetAddSet;
import nts.uk.ctx.at.record.dom.monthlyprocess.aggr.work.premiumtarget.getvacationaddtime.PremiumAtr;
import nts.uk.ctx.at.shared.dom.WorkInformation;
import nts.uk.ctx.at.shared.dom.calculation.holiday.HolidayAddtion;
import nts.uk.ctx.at.shared.dom.common.time.AttendanceTimeMonth;
import nts.uk.ctx.at.shared.dom.common.time.AttendanceTimeMonthWithMinus;
import nts.uk.ctx.at.shared.dom.statutory.worktime.shared.WeekStart;
import nts.uk.ctx.at.shared.dom.workingcondition.WorkingSystem;
import nts.uk.ctx.at.shared.dom.workrule.closure.ClosureDate;
import nts.uk.ctx.at.shared.dom.workrule.closure.ClosureId;
import nts.uk.shr.com.time.calendar.period.DatePeriod;

/**
 * 月別実績の通常変形時間
 * @author shuichi_ishida
 */
@Getter
public class RegularAndIrregularTimeOfMonthly {
	
	/** 週割増合計時間 */
	@Setter
	private AttendanceTimeMonth weeklyTotalPremiumTime;
	/** 月割増合計時間 */
	@Setter
	private AttendanceTimeMonth monthlyTotalPremiumTime;
	/** 変形労働時間 */
	private IrregularWorkingTimeOfMonthly irregularWorkingTime;
	
	/** 当月の変形期間繰越時間 */
	private IrregularPeriodCarryforwardsTimeOfCurrent irregularPeriodCarryforwardsTime;
	/** 加算した休暇使用時間 */
	private AddedVacationUseTime addedVacationUseTime;
	/** 週割増処理期間 */
	private DatePeriod weekPermiumProcPeriod;
	/** 週単位の週割増時間 */
	private AttendanceTimeMonth weekPremiumTime;
	
	/**
	 * コンストラクタ
	 */
	public RegularAndIrregularTimeOfMonthly(){
		
		this.weeklyTotalPremiumTime = new AttendanceTimeMonth(0);
		this.monthlyTotalPremiumTime = new AttendanceTimeMonth(0);
		this.irregularWorkingTime = new IrregularWorkingTimeOfMonthly();
		
		this.irregularPeriodCarryforwardsTime = new IrregularPeriodCarryforwardsTimeOfCurrent();
		this.addedVacationUseTime = new AddedVacationUseTime();
		this.weekPermiumProcPeriod = new DatePeriod(GeneralDate.today(), GeneralDate.today());
		this.weekPremiumTime = new AttendanceTimeMonth(0);
	}

	/**
	 * ファクトリー
	 * @param weeklyTotalPremiumTime 週割増合計時間
	 * @param monthlyTotalPremiumTime 月割増合計時間
	 * @param irregularWorkingTime 変形労働時間
	 * @return 日別実績の通常変形時間
	 */
	public static RegularAndIrregularTimeOfMonthly of(
			AttendanceTimeMonth weeklyTotalPremiumTime,
			AttendanceTimeMonth monthlyTotalPremiumTime,
			IrregularWorkingTimeOfMonthly irregularWorkingTime){
		
		val domain = new RegularAndIrregularTimeOfMonthly();
		domain.weeklyTotalPremiumTime = weeklyTotalPremiumTime;
		domain.monthlyTotalPremiumTime = monthlyTotalPremiumTime;
		domain.irregularWorkingTime = irregularWorkingTime;
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
	 * @param aggrSettingMonthly 月別実績集計設定
	 * @param legalTransferOrderSet 法定内振替順設定
	 * @param holidayAdditionOpt 休暇加算時間設定
	 * @param attendanceTimeOfDailyMap 日別実績の勤怠時間リスト
	 * @param workInformationOfDailyMap 日別実績の勤務情報リスト
	 * @param statutoryWorkingTimeWeek 週間法定労働時間
	 * @param aggregateTotalWorkingTime 集計総労働時間
	 * @param excessOutsideWorkMng 時間外超過管理
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
			AggrSettingMonthly aggrSettingMonthly,
			LegalTransferOrderSetOfAggrMonthly legalTransferOrderSet,
			Optional<HolidayAddtion> holidayAdditionOpt,
			Map<GeneralDate, AttendanceTimeOfDailyPerformance> attendanceTimeOfDailyMap,
			Map<GeneralDate, WorkInformation> workInformationOfDailyMap,
			AttendanceTimeMonth statutoryWorkingTimeWeek,
			AggregateTotalWorkingTime aggregateTotalWorkingTime,
			ExcessOutsideWorkMng excessOutsideWorkMng,
			RepositoriesRequiredByMonthlyAggr repositories){
		
		// 週開始を取得する
		val weekStartOpt = repositories.getGetWeekStart().get(workingSystem);
		WeekStart weekStart = WeekStart.TighteningStartDate;
		if (weekStartOpt.isPresent()) weekStart = weekStartOpt.get();
		
		// 前月の最終週のループ
		//*****（保留中）

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
				val syEmploymentOpt =
						repositories.getSyEmployment().findByEmployeeId(companyId, employeeId, procDate);
				if (syEmploymentOpt.isPresent()){
					employmentCd = syEmploymentOpt.get().getEmploymentCode();
				}
				
				// 処理日の勤務情報を取得する
				if (workInformationOfDailyMap.containsKey(procDate)) {
					val workInfo = workInformationOfDailyMap.get(procDate);
					
					// 日別実績を集計する　（通常・変形労働時間勤務用）
					aggregateTotalWorkingTime.aggregateDailyForRegAndIrreg(attendanceTimeOfDaily,
							companyId, workplaceId, employmentCd, workingSystem, aggregateAtr,
							workInfo, aggrSettingMonthly, legalTransferOrderSet, repositories);
				}
			}
			
			// 週の集計をする日か確認する
			if (this.isAggregateDayOfWeek(procDate, weekStart, datePeriod)){
			
				// 週別実績を集計する
				this.aggregateOfWeekly(companyId, employeeId, datePeriod, workingSystem, aggregateAtr, procDate,
						aggrSettingMonthly, holidayAdditionOpt, aggregateTotalWorkingTime, statutoryWorkingTimeWeek,
						weekStart);

				// 集計区分を確認する
				if (aggregateAtr == MonthlyAggregateAtr.EXCESS_OUTSIDE_WORK && excessOutsideWorkMng != null){
				
					// 時間外超過の時、週割増時間を逆時系列で割り当てる
					excessOutsideWorkMng.assignWeeklyPremiumTimeByReverseTimeSeries(
							this.weekPermiumProcPeriod, this.weekPremiumTime, aggregateTotalWorkingTime, repositories);
				}
			}
			
			procDate = procDate.addDays(1);
		}
		
		return AggregateMonthlyValue.of(aggregateTotalWorkingTime, excessOutsideWorkMng);
	}
	
	/**
	 * 週の集計をする日か確認する
	 * @param procYmd 処理日
	 * @param weekStart 週開始
	 * @param datePeriod 期間（月別集計用）
	 * @return true：集計する、false：集計しない
	 */
	private boolean isAggregateDayOfWeek(GeneralDate procYmd, WeekStart weekStart, DatePeriod datePeriod){
		
		// 週開始から集計する曜日を求める　（週開始の曜日の前日の曜日が「集計する曜日」）
		int aggregateWeek = 0;
		switch (weekStart){
		case Monday:
			aggregateWeek = 7;
			break;
		case Tuesday:
			aggregateWeek = 1;
			break;
		case Wednesday:
			aggregateWeek = 2;
			break;
		case Thursday:
			aggregateWeek = 3;
			break;
		case Friday:
			aggregateWeek = 4;
			break;
		case Saturday:
			aggregateWeek = 5;
			break;
		case Sunday:
			aggregateWeek = 6;
			break;
		case TighteningStartDate:
			
			// 締め開始日を取得する
			//*****（未）　取得方法の確認要。仮に、期間開始日の曜日とする。
			val closureDate = datePeriod.start();
			
			// 締め開始日の曜日から集計する曜日を求める
			aggregateWeek = closureDate.dayOfWeek() - 1;
			if (aggregateWeek == 0) aggregateWeek = 7;
			break;
		}
		
		// 集計する曜日を処理日の曜日を比較する
		val procWeek = procYmd.dayOfWeek();
		if (procWeek != aggregateWeek){
			if (!procYmd.equals(datePeriod.end())) return false;
		}
		return true;
	}
	
	/**
	 * 週別実績を集計する
	 * @param companyId 会社ID
	 * @param employeeId 社員ID
	 * @param datePeriod 期間(月別集計用)
	 * @param workingSystem 労働制
	 * @param aggregateAtr 集計区分
	 * @param procYmd 処理日
	 * @param aggrSettingMonthly 月別実績集計設定
	 * @param holidayAdditionOpt 休暇加算時間設定
	 * @param aggregateTotalWorkingTime 総労働時間
	 * @param statutoryWorkingTimeWeek 週間法定労働時間
	 * @param weekStart 週開始
	 */
	private void aggregateOfWeekly(
			String companyId,
			String employeeId,
			DatePeriod datePeriod,
			WorkingSystem workingSystem,
			MonthlyAggregateAtr aggregateAtr,
			GeneralDate procYmd,
			AggrSettingMonthly aggrSettingMonthly,
			Optional<HolidayAddtion> holidayAdditionOpt,
			AggregateTotalWorkingTime aggregateTotalWorkingTime,
			AttendanceTimeMonth statutoryWorkingTimeWeek,
			WeekStart weekStart){

		// 週集計期間を求める
		this.weekPermiumProcPeriod = new DatePeriod(procYmd.addDays(-6), procYmd);
		if (this.weekPermiumProcPeriod.start().before(datePeriod.start())) {
			this.weekPermiumProcPeriod = new DatePeriod(datePeriod.start(), procYmd);
		}

		// 加算設定　取得　（割増用）
		val addSet = GetAddSet.get(workingSystem, PremiumAtr.PREMIUM, holidayAdditionOpt);
		
		// 労働制を確認する
		if (workingSystem == WorkingSystem.REGULAR_WORK){
			val regularWorkSet = aggrSettingMonthly.getRegularWork();
			
			// 「週割増・月割増を求める」を取得する
			boolean isAskPremium = false;
			if (aggregateAtr == MonthlyAggregateAtr.MONTHLY){
				isAskPremium = regularWorkSet.getAggregateTimeSet().isAskPremium();
			}
			if (aggregateAtr == MonthlyAggregateAtr.EXCESS_OUTSIDE_WORK){
				isAskPremium = regularWorkSet.getExcessOutsideTimeSet().isAskPremium();
			}
			if (isAskPremium){
				
				// 通常勤務の週割増時間を集計する
				this.aggregateWeeklyPremiumTimeOfRegular(companyId, employeeId, this.weekPermiumProcPeriod,
						addSet, aggregateTotalWorkingTime, statutoryWorkingTimeWeek, weekStart);
			}
		}
		if (workingSystem == WorkingSystem.VARIABLE_WORKING_TIME_WORK){

			// 変形労働勤務の週割増時間を集計する
			this.aggregateWeeklyPremiumTimeOfIrregular(companyId, employeeId, this.weekPermiumProcPeriod,
					addSet, aggregateTotalWorkingTime, statutoryWorkingTimeWeek, weekStart);
		}
	}

	/**
	 * 通常勤務の週割増時間を集計する
	 * @param companyId 会社ID
	 * @param employeeId 社員ID
	 * @param weekPeriod 週期間
	 * @param addSet 加算設定
	 * @param aggregateTotalWorkingTime 総労働時間
	 * @param statutoryWorkingTimeWeek 週間法定労働時間
	 * @param weekStart 週開始
	 */
	private void aggregateWeeklyPremiumTimeOfRegular(
			String companyId,
			String employeeId,
			DatePeriod weekPeriod,
			AddSet addSet,
			AggregateTotalWorkingTime aggregateTotalWorkingTime,
			AttendanceTimeMonth statutoryWorkingTimeWeek,
			WeekStart weekStart){
		
		// 通常勤務の週割増時間の対象となる時間を求める
		val targetPremiumTimeMonthOfRegular = new TargetPremiumTimeMonthOfRegular();
		this.addedVacationUseTime = targetPremiumTimeMonthOfRegular.askPremiumTimeMonth(
				companyId, employeeId, weekPeriod, addSet, aggregateTotalWorkingTime);
		val targetPremiumTimeWeek = targetPremiumTimeMonthOfRegular.getTargetPremiumTimeMonth();
		
		// 按分するか確認する　（週開始＝締め開始日　かつ　期間が7日未満　の時、按分する）
		boolean isDistribute = false;
		val periodDays = weekPeriod.start().daysTo(weekPeriod.end()) + 1;
		if (weekStart == WeekStart.TighteningStartDate){
			if (periodDays < 7) isDistribute = true;
		}
		AttendanceTimeMonth targetStatutoryWorkingTime = new AttendanceTimeMonth(statutoryWorkingTimeWeek.v());
		if (isDistribute){
			
			// 法定労働時間を按分する
			int statutoryWorkingMinutesDay = statutoryWorkingTimeWeek.v() / 7;
			targetStatutoryWorkingTime = new AttendanceTimeMonth(statutoryWorkingMinutesDay * periodDays);
		}
		
		// 週割増対象時間と法定労働時間を比較する
		if (targetPremiumTimeWeek.lessThanOrEqualTo(targetStatutoryWorkingTime)) return;
		
		// 週割増対象時間が法定労働時間を超えた分だけ週単位の週割増時間に入れる
		this.weekPremiumTime = targetPremiumTimeWeek.minusMinutes(targetStatutoryWorkingTime.v());
		
		// 超えた時間を週割増合計時間に加算する
		this.weeklyTotalPremiumTime = this.weeklyTotalPremiumTime.addMinutes(this.weekPremiumTime.v());
	}

	/**
	 * 変形労働勤務の週割増時間を集計する
	 * @param companyId 会社ID
	 * @param employeeId 社員ID
	 * @param weekPeriod 週期間
	 * @param addSet 加算設定
	 * @param aggregateTotalWorkingTime 総労働時間
	 * @param statutoryWorkingTimeWeek 週間法定労働時間
	 * @param weekStart 週開始
	 */
	private void aggregateWeeklyPremiumTimeOfIrregular(
			String companyId,
			String employeeId,
			DatePeriod weekPeriod,
			AddSet addSet,
			AggregateTotalWorkingTime aggregateTotalWorkingTime,
			AttendanceTimeMonth statutoryWorkingTimeWeek,
			WeekStart weekStart){
		
		// 変形労働勤務の週割増時間の対象となる時間を求める
		val targetPremiumTimeMonthOfIrregular = new TargetPremiumTimeMonthOfIrregular();
		targetPremiumTimeMonthOfIrregular.askPremiumTimeMonth(
				companyId, employeeId, weekPeriod, addSet, aggregateTotalWorkingTime, true);
		val targetPremiumTimeWeek = targetPremiumTimeMonthOfIrregular.getTargetPremiumTimeMonth();

		// （実績）所定労働時間を取得する
		val prescribedWorkingTime = aggregateTotalWorkingTime.getPrescribedWorkingTime();
		val recordPresctibedWorkingTime = prescribedWorkingTime.getTotalRecordPrescribedWorkingTime(weekPeriod);
		
		// 按分するか確認する　（週開始＝締め開始日　かつ　期間が7日未満　の時、按分する）
		boolean isDistribute = false;
		val periodDays = weekPeriod.start().daysTo(weekPeriod.end()) + 1;
		if (weekStart == WeekStart.TighteningStartDate){
			if (periodDays < 7) isDistribute = true;
		}
		AttendanceTimeMonth targetStatutoryWorkingTime = new AttendanceTimeMonth(statutoryWorkingTimeWeek.v());
		if (isDistribute){
			
			// 法定労働時間を按分する
			int statutoryWorkingMinutesDay = statutoryWorkingTimeWeek.v() / 7;
			targetStatutoryWorkingTime = new AttendanceTimeMonth(statutoryWorkingMinutesDay * periodDays);
		}
		
		// 法定労働時間と所定労働時間を比較する
		if (targetStatutoryWorkingTime.greaterThanOrEqualTo(recordPresctibedWorkingTime.v())){
			
			// 週割増対象時間と法定労働時間を比較する
			if (targetPremiumTimeWeek.lessThanOrEqualTo(targetStatutoryWorkingTime.v())) return;
			
			// 週割増対象時間が法定労働時間を超えた分だけ週単位の週割増時間に入れる
			this.weekPremiumTime = targetPremiumTimeWeek.minusMinutes(targetStatutoryWorkingTime.v());
			
			// 超えた時間を週割増合計時間に加算する
			this.weeklyTotalPremiumTime = this.weeklyTotalPremiumTime.addMinutes(this.weekPremiumTime.v());
		}
		else {
			
			// 週割増対象時間と所定労働時間を比較する
			if (targetPremiumTimeWeek.lessThanOrEqualTo(recordPresctibedWorkingTime.v())) return;
			
			// 週割増対象時間が所定労働時間を超えた分だけ週単位の週割増時間に入れる
			this.weekPremiumTime = targetPremiumTimeWeek.minusMinutes(recordPresctibedWorkingTime.v());
			
			// 超えた時間を週割増合計時間に加算する
			this.weeklyTotalPremiumTime = this.weeklyTotalPremiumTime.addMinutes(this.weekPremiumTime.v());
		}
	}
	
	/**
	 * 月単位の時間を集計する
	 * @param companyId 会社ID
	 * @param employeeId 社員ID
	 * @param yearMonth 年月（度）
	 * @param closureId 締めID
	 * @param closureDate 締め日付
	 * @param datePeriod 期間
	 * @param workingSystem 労働制
	 * @param aggregateAtr 集計区分
	 * @param isRetireMonth 退職月かどうか
	 * @param workplaceId 職場ID
	 * @param employmentCd 雇用コード
	 * @param aggrSettingMonthly 月別実績集計設定
	 * @param holidayAdditionOpt 休暇加算時間設定
	 * @param aggregateTotalWorkingTime 総労働時間
	 * @param statutoryWorkingTimeMonth 月間法定労働時間
	 * @param repositories 月次集計が必要とするリポジトリ
	 */
	public void aggregateMonthlyHours(
			String companyId,
			String employeeId,
			YearMonth yearMonth,
			ClosureId closureId,
			ClosureDate closureDate,
			DatePeriod datePeriod,
			WorkingSystem workingSystem,
			MonthlyAggregateAtr aggregateAtr,
			boolean isRetireMonth,
			String workplaceId,
			String employmentCd,
			AggrSettingMonthly aggrSettingMonthly,
			Optional<HolidayAddtion> holidayAdditionOpt,
			AggregateTotalWorkingTime aggregateTotalWorkingTime,
			AttendanceTimeMonth statutoryWorkingTimeMonth,
			RepositoriesRequiredByMonthlyAggr repositories){

		// 加算設定　取得　（割増用）
		val addSet = GetAddSet.get(workingSystem, PremiumAtr.PREMIUM, holidayAdditionOpt);
		
		// 通常勤務の時
		if (workingSystem == WorkingSystem.REGULAR_WORK){
			
			// 「割増を求める」がtrueの時
			val legalAggrSetOfReg = aggrSettingMonthly.getRegularWork();
			val aggregateTimeSet = legalAggrSetOfReg.getAggregateTimeSet();
			if (aggregateTimeSet.isAskPremium()){
			
				// 通常勤務の月単位の時間を集計する
				this.aggregateTimePerMonthOfRegular(companyId, employeeId, yearMonth, datePeriod,
						workplaceId, employmentCd, addSet, aggregateTotalWorkingTime,
						statutoryWorkingTimeMonth, repositories);
			}
		}
		
		// 変形労働時間勤務の時
		if (workingSystem == WorkingSystem.VARIABLE_WORKING_TIME_WORK){
			
			// 変形労働勤務の月単位の時間を集計する
			this.aggregateTimePerMonthOfIrregular(companyId, employeeId,
					yearMonth, closureId, closureDate, datePeriod, isRetireMonth,
					aggrSettingMonthly.getIrregularWork(), holidayAdditionOpt,
					aggregateTotalWorkingTime, statutoryWorkingTimeMonth, repositories);
		}
	}
	
	/**
	 * 通常勤務の月単位の時間を集計する
	 * @param companyId 会社ID
	 * @param employeeId 社員ID
	 * @param yearMonth 年月（度）
	 * @param datePeriod 期間
	 * @param workplaceId 職場ID
	 * @param employmentCd 雇用コード
	 * @param addSet 加算設定
	 * @param aggregateTotalWorkingTime 集計総労働時間
	 * @param statutoryWorkingTimeMonth 月間法定労働時間
	 * @param repositories 月次集計が必要とするリポジトリ
	 */
	private void aggregateTimePerMonthOfRegular(
			String companyId,
			String employeeId,
			YearMonth yearMonth,
			DatePeriod datePeriod,
			String workplaceId,
			String employmentCd,
			AddSet addSet,
			AggregateTotalWorkingTime aggregateTotalWorkingTime,
			AttendanceTimeMonth statutoryWorkingTimeMonth,
			RepositoriesRequiredByMonthlyAggr repositories){
		
		// 通常勤務の月割増時間の対象となる時間を求める
		val targetPremiumTimeMonthOfRegular = new TargetPremiumTimeMonthOfRegular();
		this.addedVacationUseTime = targetPremiumTimeMonthOfRegular.askPremiumTimeMonth(
				companyId, employeeId, datePeriod, addSet, aggregateTotalWorkingTime);
		val targetPremiumTimeMonth = targetPremiumTimeMonthOfRegular.getTargetPremiumTimeMonth();
		
		// 通常勤務の月割増対象時間　≦　法定労働時間　なら、処理終了
		if (targetPremiumTimeMonth.lessThanOrEqualTo(statutoryWorkingTimeMonth.v())) return;
		
		// 通常勤務の月割増対象時間が法定労働時間を超えた分を「月割増対象時間超過分」とする
		val excessTargetPremiumTimeMonth = new AttendanceTimeMonth(targetPremiumTimeMonth.v());
		excessTargetPremiumTimeMonth.minusMinutes(statutoryWorkingTimeMonth.v());
		
		// 月割増対象時間超過分－週割増合計時間を月割増合計時間とする
		this.monthlyTotalPremiumTime = new AttendanceTimeMonth(excessTargetPremiumTimeMonth.v());
		this.monthlyTotalPremiumTime.minusMinutes(this.weeklyTotalPremiumTime.v());
		if (this.monthlyTotalPremiumTime.lessThan(0)) {
			this.monthlyTotalPremiumTime = new AttendanceTimeMonth(0);
		}
	}
	
	
	/**
	 * 変形労働勤務の月単位の時間を集計する
	 * @param companyId 会社ID
	 * @param employeeId 社員ID
	 * @param yearMonth 年月（度）
	 * @param closureId 締めID
	 * @param closureDate 締め日付
	 * @param datePeriod 期間
	 * @param isRetireMonth 退職月かどうか
	 * @param legalAggrSetOfIrg 変形労働時間勤務の法定内集計設定
	 * @param holidayAdditionOpt 休暇加算時間設定
	 * @param aggregateTotalWorkingTime 集計総労働時間
	 * @param statutoryWorkingTimeMonth 月間法定労働時間
	 * @param repositories 月次集計が必要とするリポジトリ
	 */
	private void aggregateTimePerMonthOfIrregular(
			String companyId,
			String employeeId,
			YearMonth yearMonth,
			ClosureId closureId,
			ClosureDate closureDate,
			DatePeriod datePeriod,
			boolean isRetireMonth,
			LegalAggrSetOfIrg legalAggrSetOfIrg,
			Optional<HolidayAddtion> holidayAdditionOpt,
			AggregateTotalWorkingTime aggregateTotalWorkingTime,
			AttendanceTimeMonth statutoryWorkingTimeMonth,
			RepositoriesRequiredByMonthlyAggr repositories){
		
		// 当月の変形期間繰越時間を集計する
		this.irregularPeriodCarryforwardsTime = new IrregularPeriodCarryforwardsTimeOfCurrent();
		this.irregularPeriodCarryforwardsTime.aggregate(companyId, employeeId, datePeriod,
				this.weeklyTotalPremiumTime, holidayAdditionOpt,
				aggregateTotalWorkingTime, statutoryWorkingTimeMonth);
		this.addedVacationUseTime.addMinutesToAddTimePerMonth(
				this.irregularPeriodCarryforwardsTime.getAddedVacationUseTime().v());
		
		// 該当精算期間の開始月～前月の変形期間繰越時間を集計する
		val pastIrregularPeriodCarryforwardsTime = this.aggregatePastIrregularPeriodCarryforwardsTime(
				employeeId, yearMonth, closureId, closureDate, legalAggrSetOfIrg, repositories);
		
		// 開始月～当月の変形期間繰越時間を求める
		AttendanceTimeMonthWithMinus totalIrregularPeriodCarryforwardsTime = new AttendanceTimeMonthWithMinus(
				pastIrregularPeriodCarryforwardsTime.v());
		totalIrregularPeriodCarryforwardsTime = totalIrregularPeriodCarryforwardsTime.addMinutes(
				this.irregularPeriodCarryforwardsTime.getTime().v());

		// 精算月か確認する
		if (legalAggrSetOfIrg.getSettlementPeriod().isSettlementMonth(yearMonth, isRetireMonth)){
			
			// 精算月の時、月割増合計時間に集計結果を入れる
			this.monthlyTotalPremiumTime = new AttendanceTimeMonth(totalIrregularPeriodCarryforwardsTime.v());
			
			// 変形期間繰越時間を 0 にする
			this.irregularWorkingTime.setIrregularPeriodCarryforwardTime(new AttendanceTimeMonthWithMinus(0));
		}
		else{
			
			// 精算月でない時、複数月変形途中時間・変形期間繰越時間に集計結果を入れる
			this.irregularWorkingTime.setMultiMonthIrregularMiddleTime(totalIrregularPeriodCarryforwardsTime);
			this.irregularWorkingTime.setIrregularPeriodCarryforwardTime(
					new AttendanceTimeMonthWithMinus(this.irregularPeriodCarryforwardsTime.getTime().v()));
		}
	}
	
	/**
	 * 精算期間．開始月～前月までの変形期間繰越時間を集計する
	 * @param employeeId 社員ID
	 * @param yearMonth 年月（度）
	 * @param closureId 締めID
	 * @param closureDate 締め日付
	 * @param legalAggrSetOfIrg 変形労働時間勤務の法定内集計設定
	 * @param repositories 月次集計が必要とするリポジトリ
	 * @return 過去の変形期間繰越時間
	 */
	private AttendanceTimeMonth aggregatePastIrregularPeriodCarryforwardsTime(
			String employeeId, YearMonth yearMonth,
			ClosureId closureId, ClosureDate closureDate,
			LegalAggrSetOfIrg legalAggrSetOfIrg,
			RepositoriesRequiredByMonthlyAggr repositories){
		
		AttendanceTimeMonth irregularPeriodCarryforwardsTime = new AttendanceTimeMonth(0);
		
		// 精算期間を取得する
		val pastYearMonths = legalAggrSetOfIrg.getSettlementPeriod().getPastSettlementYearMonths(yearMonth);
		
		// 開始月～前月までの変形期間繰越時間を集計する
		for (val pastYearMonth : pastYearMonths){
			val attendanceTimeList = repositories.getAttendanceTimeOfMonthly().findByYearMonthOrderByStartYmd(
					employeeId, pastYearMonth);
			for (val attendanceTime : attendanceTimeList){
				val actualWorkingTime = attendanceTime.getMonthlyCalculation().getActualWorkingTime();
				val irregularWorkingTime = actualWorkingTime.getIrregularWorkingTime();
				val carryforwardTime = irregularWorkingTime.getIrregularPeriodCarryforwardTime();
				
				irregularPeriodCarryforwardsTime =
						irregularPeriodCarryforwardsTime.addMinutes(carryforwardTime.v());
			}
		}
		
		return irregularPeriodCarryforwardsTime;
	}
}
