package nts.uk.ctx.at.record.dom.monthlyprocess.aggr.work.excessoutside;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import lombok.Getter;
import lombok.val;
import nts.arc.time.GeneralDate;
import nts.arc.time.YearMonth;
import nts.uk.ctx.at.record.dom.actualworkinghours.AttendanceTimeOfDailyPerformance;
import nts.uk.ctx.at.record.dom.monthly.AttendanceItemOfMonthly;
import nts.uk.ctx.at.record.dom.monthly.AttendanceTimeOfMonthly;
import nts.uk.ctx.at.record.dom.monthly.agreement.AgreementTimeOfManagePeriod;
import nts.uk.ctx.at.record.dom.monthly.calc.AggregateMonthlyValue;
import nts.uk.ctx.at.record.dom.monthly.calc.MonthlyAggregateAtr;
import nts.uk.ctx.at.record.dom.monthly.calc.MonthlyCalculation;
import nts.uk.ctx.at.record.dom.monthly.calc.actualworkingtime.RegularAndIrregularTimeOfMonthly;
import nts.uk.ctx.at.record.dom.monthly.calc.flex.FlexTime;
import nts.uk.ctx.at.record.dom.monthly.calc.flex.FlexTimeOfMonthly;
import nts.uk.ctx.at.record.dom.monthly.calc.totalworkingtime.AggregateTotalWorkingTime;
import nts.uk.ctx.at.record.dom.monthly.excessoutside.ExcessOutsideWorkOfMonthly;
import nts.uk.ctx.at.record.dom.monthly.roundingset.RoundingSetOfMonthly;
import nts.uk.ctx.at.record.dom.monthlyaggrmethod.AggrSettingMonthly;
import nts.uk.ctx.at.record.dom.monthlyaggrmethod.flex.AggrSettingMonthlyOfFlx;
import nts.uk.ctx.at.record.dom.monthlyaggrmethod.flex.FlexAggregateMethod;
import nts.uk.ctx.at.record.dom.monthlyaggrmethod.legaltransferorder.LegalTransferOrderSetOfAggrMonthly;
import nts.uk.ctx.at.record.dom.monthlyaggrmethod.regularandirregular.ExcessOutsideTimeSet;
import nts.uk.ctx.at.record.dom.monthlyprocess.aggr.work.RepositoriesRequiredByMonthlyAggr;
import nts.uk.ctx.at.record.dom.monthlyprocess.aggr.work.timeseries.FlexTimeOfTimeSeries;
import nts.uk.ctx.at.shared.dom.WorkInformation;
import nts.uk.ctx.at.shared.dom.calculation.holiday.HolidayAddtion;
import nts.uk.ctx.at.shared.dom.common.Year;
import nts.uk.ctx.at.shared.dom.common.time.AttendanceTimeMonth;
import nts.uk.ctx.at.shared.dom.common.time.AttendanceTimeMonthWithMinus;
import nts.uk.ctx.at.shared.dom.outsideot.OutsideOTCalMed;
import nts.uk.ctx.at.shared.dom.outsideot.OutsideOTSetting;
import nts.uk.ctx.at.shared.dom.outsideot.UseClassification;
import nts.uk.ctx.at.shared.dom.outsideot.breakdown.OutsideOTBRDItem;
import nts.uk.ctx.at.shared.dom.outsideot.overtime.Overtime;
import nts.uk.ctx.at.shared.dom.workingcondition.WorkingConditionItem;
import nts.uk.ctx.at.shared.dom.workingcondition.WorkingSystem;
import nts.uk.ctx.at.shared.dom.workrule.closure.ClosureDate;
import nts.uk.ctx.at.shared.dom.workrule.closure.ClosureId;
import nts.uk.shr.com.time.calendar.period.DatePeriod;

/**
 * 時間外超過管理
 * @author shuichu_ishida
 */
@Getter
public class ExcessOutsideWorkMng {

	/** 月次明細 */
	private MonthlyDetail monthlyDetail;
	/** 時間外超過明細 */
	private ExcessOutsideWorkDetail excessOutsideWorkDetail;
	/** 時間外超過 */
	private ExcessOutsideWorkOfMonthly excessOutsideWork;
	
	/** 月別実績の月の計算 */
	private MonthlyCalculation monthlyCalculation;
	/** 会社ID */
	private final String companyId;
	/** 社員ID */
	private final String employeeId;
	/** 年月 */
	private final YearMonth yearMonth;
	/** 締めID */
	private final ClosureId closureId;
	/** 締め日付 */
	private final ClosureDate closureDate;
	/** 期間 */
	private final DatePeriod procPeriod;
	/** 労働条件項目 */
	private final WorkingConditionItem workingConditionItem;
	/** 労働制 */
	private final WorkingSystem workingSystem;
	/** 職場ID */
	private String workplaceId;
	/** 雇用コード */
	private String employmentCd;
	/** 退職月度がどうか */
	private boolean isRetireMonth;
	/** 月別実績集計設定 */
	private AggrSettingMonthly aggrSettingMonthly;
	/** 月次集計の法定内振替順設定 */
	private LegalTransferOrderSetOfAggrMonthly legalTransferOrderSet;
	/** 休暇加算時間設定 */
	private Optional<HolidayAddtion> holidayAdditionOpt;
	/** 日別実績の勤怠時間リスト */
	private Map<GeneralDate, AttendanceTimeOfDailyPerformance> attendanceTimeOfDailyMap;
	/** 日別実績の勤務情報リスト */
	private Map<GeneralDate, WorkInformation> workInformationOfDailyMap;
	/** 週間法定労働時間 */
	private AttendanceTimeMonth statutoryWorkingTimeWeek;
	/** 月間法定労働時間 */
	private AttendanceTimeMonth statutoryWorkingTimeMonth;
	/** 週間所定労働時間 */
	private AttendanceTimeMonth prescribedWorkingTimeWeek;
	/** 月間所定労働時間 */
	private AttendanceTimeMonth prescribedWorkingTimeMonth;
	
	/** 年度 */
	private Year year;
	/** 管理期間の36協定時間 */
	private AgreementTimeOfManagePeriod agreementTimeOfManagePeriod;
	
	/** 時間外超過設定 */
	private Optional<OutsideOTSetting> outsideOTSetOpt;
	/** 月別実績の丸め設定 */
	private RoundingSetOfMonthly roundingSet;
	
	/**
	 * コンストラクタ
	 * @param companyId 会社ID
	 * @param employeeId 社員ID
	 * @param yearMonth 年月
	 * @param closureId 締めID
	 * @param closureDate 締め日
	 * @param procPeriod 期間
	 * @param workingSystem 労働制
	 * @param monthlyCalculation 月別実績の月の計算
	 */
	public ExcessOutsideWorkMng(MonthlyCalculation monthlyCalculation){
		
		this.monthlyDetail = new MonthlyDetail();
		this.excessOutsideWorkDetail = new ExcessOutsideWorkDetail();
		this.excessOutsideWork = new ExcessOutsideWorkOfMonthly();
		
		this.monthlyCalculation = monthlyCalculation;
		this.companyId = monthlyCalculation.getCompanyId();
		this.employeeId = monthlyCalculation.getEmployeeId();
		this.yearMonth = monthlyCalculation.getYearMonth();
		this.closureId = monthlyCalculation.getClosureId();
		this.closureDate = monthlyCalculation.getClosureDate();
		this.procPeriod = monthlyCalculation.getProcPeriod();
		this.workingConditionItem = monthlyCalculation.getWorkingConditionItem();
		this.workingSystem = monthlyCalculation.getWorkingSystem();
		this.workplaceId = monthlyCalculation.getWorkplaceId();
		this.employmentCd = monthlyCalculation.getEmploymentCd();
		this.isRetireMonth = monthlyCalculation.isRetireMonth();
		this.aggrSettingMonthly = monthlyCalculation.getAggrSettingMonthly();
		this.legalTransferOrderSet = monthlyCalculation.getLegalTransferOrderSet();
		this.holidayAdditionOpt = monthlyCalculation.getHolidayAdditionOpt();
		this.attendanceTimeOfDailyMap = monthlyCalculation.getAttendanceTimeOfDailyMap();
		this.workInformationOfDailyMap = monthlyCalculation.getWorkInformationOfDailyMap();
		this.statutoryWorkingTimeWeek = monthlyCalculation.getStatutoryWorkingTimeWeek();
		this.statutoryWorkingTimeMonth = monthlyCalculation.getStatutoryWorkingTimeMonth();
		this.prescribedWorkingTimeWeek = monthlyCalculation.getPrescribedWorkingTimeWeek();
		this.prescribedWorkingTimeMonth = monthlyCalculation.getPrescribedWorkingTimeMonth();
		
		this.year = monthlyCalculation.getYear();
		this.agreementTimeOfManagePeriod = new AgreementTimeOfManagePeriod(this.employeeId, this.yearMonth);
		
		this.outsideOTSetOpt = Optional.empty();
		this.roundingSet = new RoundingSetOfMonthly(this.companyId);
	}
	
	/**
	 * 集計
	 * @param repositories 月次集計が必要とするリポジトリ
	 */
	public void aggregate(RepositoriesRequiredByMonthlyAggr repositories){

		// 時間外超過設定　取得
		this.outsideOTSetOpt = repositories.getOutsideOTSet().findById(this.companyId);
		
		// 丸め設定取得
		val roundingSetOpt = repositories.getRoundingSetOfMonthly().find(this.companyId);
		if (roundingSetOpt.isPresent()) this.roundingSet = roundingSetOpt.get();
		
		// 労働制を確認する
		if (this.workingSystem == WorkingSystem.REGULAR_WORK || this.workingSystem == WorkingSystem.VARIABLE_WORKING_TIME_WORK){
			
			// 通常・変形労働時間勤務の時間外超過を集計する
			this.aggregateExcessOutsideWork(null, repositories);
		}
		if (this.workingSystem == WorkingSystem.FLEX_TIME_WORK){
			
			// フレックス集計方法を確認する
			val flexAggregateMethod = this.aggrSettingMonthly.getFlexWork().getAggregateMethod();
			if (flexAggregateMethod == FlexAggregateMethod.PRINCIPLE){
				
				// 原則集計で時間外超過を集計する
				this.aggregateExcessOutsideWork(FlexAggregateMethod.PRINCIPLE, repositories);
			}
			if (flexAggregateMethod == FlexAggregateMethod.FOR_CONVENIENCE){
				
				// 便宜上集計で時間外超過を集計する
				this.aggregateExcessOutsideWork(FlexAggregateMethod.FOR_CONVENIENCE, repositories);

				// 原則集計で時間外超過を集計する
				//this.aggregateExcessOutsideWork(FlexAggregateMethod.PRINCIPLE, repositories);
				
				// 原則集計と便宜上集計の結果を比較する
				//*****（未）　設計が保留中。比較する場合、どのクラスを比較するかで、原則・便宜上の各計算結果の保持方法の検討要。
			}
		}
		
		// 超休精算
	}
	
	/**
	 * 時間外超過を集計する
	 * @param flexAggregateMethod フレックス集計方法
	 * @param repositories 月次集計が必要とするリポジトリ
	 */
	private void aggregateExcessOutsideWork(
			FlexAggregateMethod flexAggregateMethod,
			RepositoriesRequiredByMonthlyAggr repositories){
		
		// 時間外超過設定を確認する
		OutsideOTCalMed calcMethod = OutsideOTCalMed.DECISION_AFTER;
		if (this.outsideOTSetOpt.isPresent()) calcMethod = outsideOTSetOpt.get().getCalculationMethod();
		if (calcMethod == OutsideOTCalMed.DECISION_AFTER){
			
			// 集計後に求める
			this.askAfterAggregate(flexAggregateMethod, repositories);
		}
		if (calcMethod == OutsideOTCalMed.TIME_SERIES){
			
			// 時系列で求める
			this.askbyTimeSeries(flexAggregateMethod, repositories);
		}
	}
	
	/**
	 * 集計後に求める
	 * @param flexAggregateMethod フレックス集計方法
	 * @param repositories 月次集計が必要とするリポジトリ
	 */
	private void askAfterAggregate(
			FlexAggregateMethod flexAggregateMethod,
			RepositoriesRequiredByMonthlyAggr repositories){
		
		// 集計結果　初期化
		val regAndIrgTime = new RegularAndIrregularTimeOfMonthly();
		val flexTime = new FlexTimeOfMonthly();
		val aggregateTotalWorkingTime = new AggregateTotalWorkingTime();
		aggregateTotalWorkingTime.copySharedItem(this.monthlyCalculation.getTotalWorkingTime());
		AggregateMonthlyValue aggrValue = null;
		
		// 労働制を確認する
		if (workingSystem == WorkingSystem.REGULAR_WORK || workingSystem == WorkingSystem.VARIABLE_WORKING_TIME_WORK){
			
			// 通常・変形労働時間勤務の月別実績を集計する
			aggrValue = regAndIrgTime.aggregateMonthly(this.companyId, this.employeeId,
					this.yearMonth, this.procPeriod, this.workingSystem, MonthlyAggregateAtr.EXCESS_OUTSIDE_WORK,
					this.aggrSettingMonthly, this.legalTransferOrderSet, this.holidayAdditionOpt,
					this.attendanceTimeOfDailyMap, this.workInformationOfDailyMap, this.statutoryWorkingTimeWeek,
					aggregateTotalWorkingTime, this, repositories);
			
			// 通常・変形労働時間勤務の月単位の時間を集計する
			regAndIrgTime.aggregateMonthlyHours(
					this.companyId, this.employeeId, this.yearMonth, this.closureId, this.closureDate,
					this.procPeriod, this.workingSystem, MonthlyAggregateAtr.EXCESS_OUTSIDE_WORK,
					this.isRetireMonth, this.workplaceId, this.employmentCd,
					this.aggrSettingMonthly, this.holidayAdditionOpt,
					aggrValue.getAggregateTotalWorkingTime(),
					this.statutoryWorkingTimeMonth, repositories);
		}
		if (workingSystem == WorkingSystem.FLEX_TIME_WORK){
			val aggrSetOfFlex = this.aggrSettingMonthly.getFlexWork();
			
			// フレックス勤務の月別実績を集計する
			aggrValue = flexTime.aggregateMonthly(this.companyId, this.employeeId,
					this.yearMonth, this.procPeriod, this.workingSystem, MonthlyAggregateAtr.EXCESS_OUTSIDE_WORK,
					flexAggregateMethod, aggrSetOfFlex, this.attendanceTimeOfDailyMap,
					aggregateTotalWorkingTime, this,
					this.prescribedWorkingTimeMonth, this.statutoryWorkingTimeMonth, repositories);
			
			// フレックス勤務の月単位の時間を集計する
			flexTime.aggregateMonthlyHours(this.companyId, this.employeeId, this.yearMonth, this.procPeriod,
					flexAggregateMethod, this.workingConditionItem, this.workplaceId, this.employmentCd,
					aggrSetOfFlex, this.holidayAdditionOpt, aggrValue.getAggregateTotalWorkingTime(),
					this.prescribedWorkingTimeMonth, this.statutoryWorkingTimeMonth, repositories);
		}
		
		if (aggrValue != null){
			
			// 実働時間の集計
			aggrValue.getAggregateTotalWorkingTime().aggregateActualWorkingTime(
					this.procPeriod, this.workingSystem, regAndIrgTime, flexTime);
			
			// 時間外超過明細．丸め後合計時間に移送する
			this.excessOutsideWorkDetail.setTotalTimeAfterRound(
					aggrValue.getAggregateTotalWorkingTime(), regAndIrgTime, flexTime,
					this.aggrSettingMonthly.getFlexWork(), this.roundingSet);
			
			// 月別実績の時間外超過に移送する
			this.excessOutsideWork.setFromAggregateTime(
					this.excessOutsideWorkDetail.getTotalTimeAfterRound(), regAndIrgTime.getIrregularWorkingTime());
			
			// 時間外超過内訳に割り当てる　（集計後）
			this.assignExcessOutsideWorkBreakdownForAfterAggregate();
		}
	}
	
	/**
	 * 時系列で求める
	 * @param flexAggregateMethod フレックス集計方法
	 * @param repositories 月次集計が必要とするリポジトリ
	 */
	private void askbyTimeSeries(
			FlexAggregateMethod flexAggregateMethod,
			RepositoriesRequiredByMonthlyAggr repositories){
		
		// 集計結果　初期化
		val regAndIrgTime = new RegularAndIrregularTimeOfMonthly();
		val flexTime = new FlexTimeOfMonthly();
		val aggregateTotalWorkingTime = new AggregateTotalWorkingTime();
		aggregateTotalWorkingTime.copySharedItem(this.monthlyCalculation.getTotalWorkingTime());
		AggregateMonthlyValue aggrValue = null;
		
		// 労働制を確認する
		if (this.workingSystem == WorkingSystem.REGULAR_WORK ||
			this.workingSystem == WorkingSystem.VARIABLE_WORKING_TIME_WORK){
			
			// 通常・変形労働時間勤務の月別実績を集計する
			aggrValue = regAndIrgTime.aggregateMonthly(this.companyId, this.employeeId,
					this.yearMonth, this.procPeriod, this.workingSystem, MonthlyAggregateAtr.EXCESS_OUTSIDE_WORK,
					this.aggrSettingMonthly, this.legalTransferOrderSet, this.holidayAdditionOpt,
					this.attendanceTimeOfDailyMap, this.workInformationOfDailyMap, this.statutoryWorkingTimeWeek,
					aggregateTotalWorkingTime, this, repositories);
			
			// 通常・変形労働時間勤務の月単位の時間を集計する
			regAndIrgTime.aggregateMonthlyHours(
					this.companyId, this.employeeId, this.yearMonth, this.closureId, this.closureDate,
					this.procPeriod, this.workingSystem, MonthlyAggregateAtr.EXCESS_OUTSIDE_WORK,
					this.isRetireMonth, this.workplaceId, this.employmentCd,
					this.aggrSettingMonthly, this.holidayAdditionOpt,
					aggrValue.getAggregateTotalWorkingTime(),
					this.statutoryWorkingTimeMonth, repositories);
			
			// 通常・変形労働勤務の逆時系列割り当て
			this.assignReverseTimeSeriesOfRegAndIrg(
					regAndIrgTime, aggrValue.getAggregateTotalWorkingTime(), repositories);
		}
		if (this.workingSystem == WorkingSystem.FLEX_TIME_WORK){
			val aggrSetOfFlex = this.aggrSettingMonthly.getFlexWork();
			
			// フレックス勤務の月別実績を集計する
			aggrValue = flexTime.aggregateMonthly(this.companyId, this.employeeId,
					this.yearMonth, this.procPeriod, this.workingSystem, MonthlyAggregateAtr.EXCESS_OUTSIDE_WORK,
					flexAggregateMethod, aggrSetOfFlex, this.attendanceTimeOfDailyMap,
					aggregateTotalWorkingTime, this,
					this.prescribedWorkingTimeMonth, this.statutoryWorkingTimeMonth, repositories);
			
			// フレックス勤務の月単位の時間を集計する
			flexTime.aggregateMonthlyHours(this.companyId, this.employeeId, this.yearMonth, this.procPeriod,
					flexAggregateMethod, this.workingConditionItem, this.workplaceId, this.employmentCd,
					aggrSetOfFlex, this.holidayAdditionOpt, aggrValue.getAggregateTotalWorkingTime(),
					this.prescribedWorkingTimeMonth, this.statutoryWorkingTimeMonth, repositories);
			
			// フレックス時間勤務の逆時系列割り当て
			if (flexAggregateMethod == FlexAggregateMethod.FOR_CONVENIENCE){
				
				// 便宜上集計の逆時系列割り当て
				this.assignReverseTimeSeriesForConvenience(flexTime);
			}
		}
		
		if (aggrValue != null){
		
			// 丸め時間を割り当てる
			this.excessOutsideWorkDetail.assignRoundTime(this.monthlyDetail, this.procPeriod, this.roundingSet);
			
			// 時間外超過内訳に割り当てる
			this.assignExcessOutsideWorkBreakdownForReverseTimeSeries();
		}
	}
	
	/**
	 * 週割増時間を逆時系列で割り当てる
	 * @param weekPermiumProcPeriod 週割増処理期間
	 * @param weekPremiumTime 週単位の週割増時間
	 * @param aggregateTotalWorkingTime 集計総労働時間
	 * @param repositories 月次集計が必要とするリポジトリ
	 */
	public void assignWeeklyPremiumTimeByReverseTimeSeries(
			DatePeriod weekPermiumProcPeriod,
			AttendanceTimeMonth weekPremiumTime,
			AggregateTotalWorkingTime aggregateTotalWorkingTime,
			RepositoriesRequiredByMonthlyAggr repositories){

		// 「週割増・月割増を求める」を取得する
		ExcessOutsideTimeSet excessOutsideTimeSet = new ExcessOutsideTimeSet();
		if (this.workingSystem == WorkingSystem.REGULAR_WORK){
			val aggrSetOfReg = this.aggrSettingMonthly.getRegularWork();
			excessOutsideTimeSet = aggrSetOfReg.getExcessOutsideTimeSet();
		}
		if (this.workingSystem == WorkingSystem.VARIABLE_WORKING_TIME_WORK){
			val aggrSetOfIrg = this.aggrSettingMonthly.getIrregularWork();
			excessOutsideTimeSet = aggrSetOfIrg.getExcessOutsideTimeSet();
		}
		if (!excessOutsideTimeSet.isAskPremium()) return;
		
		// 「時間外超過設定」を確認する　（計算方法が「時系列」以外なら割り当てない）
		if (!this.outsideOTSetOpt.isPresent()) return;
		if (this.outsideOTSetOpt.get().getCalculationMethod() != OutsideOTCalMed.TIME_SERIES) return;
		
		// 「週単位の週割増時間」を「逆時系列割り当て用の週割増時間」にコピーする
		AttendanceTimeMonthWithMinus weeklyPTForAssign = new AttendanceTimeMonthWithMinus(weekPremiumTime.v());
		
		// 「週単位の週割増時間」を「月別実績の時間外超過」に加算する
		this.excessOutsideWork.addMinutesToWeeklyTotalPremiumTime(weekPremiumTime.v());
		
		// 「期間．終了日」を処理日にする
		GeneralDate procDate = weekPermiumProcPeriod.end();
		while (procDate.afterOrEquals(weekPermiumProcPeriod.start())){
			
			// 週割増時間を日単位で割り当てる
			weeklyPTForAssign = this.monthlyDetail.assignWeeklyPremiumTimeByDayUnit(procDate, weeklyPTForAssign,
					aggregateTotalWorkingTime, this.workInformationOfDailyMap, this, repositories);
			
			// 時間外超過明細の更新
			this.excessOutsideWorkDetail = this.monthlyDetail.getExcessOutsideWorkMng().getExcessOutsideWorkDetail();
			
			if (weeklyPTForAssign.lessThanOrEqualTo(0)) break;
			
			// 処理日を更新する
			procDate = procDate.addDays(-1);
		}
	}
	
	/**
	 * フレックス超過時間を割り当てる
	 * @param datePeriod 期間
	 * @param flexAggregateMethod フレックス集計方法
	 * @param procDate 処理日
	 * @param aggrSetOfFlex フレックス時間勤務の月の集計設定
	 * @param aggregateTotalWorkingTime 集計総労働時間
	 * @param flexTime フレックス時間
	 * @param prescribedWorkingTimeMonth 月間所定労働時間
	 * @param statutoryWorkingTimeMonth 月間法定労働時間
	 * @param repositories 月次集計が必要とするリポジトリ
	 */
	public void assignFlexExcessTime(
			DatePeriod datePeriod,
			FlexAggregateMethod flexAggregateMethod,
			GeneralDate procDate,
			AggrSettingMonthlyOfFlx aggrSetOfFlex,
			AggregateTotalWorkingTime aggregateTotalWorkingTime,
			FlexTime flexTime,
			AttendanceTimeMonth prescribedWorkingTimeMonth,
			AttendanceTimeMonth statutoryWorkingTimeMonth,
			RepositoriesRequiredByMonthlyAggr repositories){
		
		// 「フレックス集計方法」を確認する
		if (flexAggregateMethod == FlexAggregateMethod.FOR_CONVENIENCE) return;
		
		// フレックス超過対象時間を求める
		val targetFlexExcessTime = this.askTargetFlexExcessTime(
				datePeriod, procDate, aggregateTotalWorkingTime, flexTime);
		
		// 「時間外超過対象設定」を確認する
		val excessOutsideTimeTargetSet =
				aggrSetOfFlex.getLegalAggregateSet().getExcessOutsideTimeSet().getExcessOutsideTimeTargetSet();
		AttendanceTimeMonthWithMinus excessTimeUntilDay = new AttendanceTimeMonthWithMinus(0);
		switch (excessOutsideTimeTargetSet){
		case ONLY_ILLEGAL_FLEX:
			
			// 法定外フレックスのみで当日までの超過時間を求める
			excessTimeUntilDay = targetFlexExcessTime.minusMinutes(statutoryWorkingTimeMonth.v());
			break;
			
		case INCLUDE_LEGAL_FLEX:
			
			// 法定内フレックスを含んで当日までの超過時間を求める
			excessTimeUntilDay = this.askExcessTimeUntilDayIncludeLegalFlex(
					targetFlexExcessTime, datePeriod, procDate, aggregateTotalWorkingTime, prescribedWorkingTimeMonth);
			break;
		}

		// 前日までの超過時間を求める
		AttendanceTimeMonthWithMinus excessTimeUntilPrevDay = new AttendanceTimeMonthWithMinus(0);
		DatePeriod periodUntilPrevDay = new DatePeriod(datePeriod.start(), procDate.addDays(-1)); 
		if (!periodUntilPrevDay.isReversed()){
			excessTimeUntilPrevDay = this.excessOutsideWorkDetail.getTotalFlexExcessTime(periodUntilPrevDay);
		}
		
		// 当日の超過時間を計算する
		val excessTimeOfDay = excessTimeUntilDay.minusMinutes(excessTimeUntilPrevDay.v());
		if (excessTimeOfDay.greaterThan(0)){
			
			// 当日の超過時間＞0 の時、当日の超過時間を時間外超過明細に加算する
			val flexExcessTime = this.excessOutsideWorkDetail.getFlexExcessTime();
			flexExcessTime.putIfAbsent(procDate, new FlexTimeOfTimeSeries(procDate));
			val targetTimeSeries = flexExcessTime.get(procDate);
			targetTimeSeries.addMinutesToFlexTimeInFlexTime(excessTimeOfDay.v());
		}
	}
	
	/**
	 * フレックス超過対象時間を求める
	 * @param datePeriod 期間
	 * @param procDate 処理日
	 * @param aggregateTotalWorkingTime 集計総労働時間
	 * @param flexTime フレックス時間
	 * @return フレックス超過対象時間
	 */
	private AttendanceTimeMonthWithMinus askTargetFlexExcessTime(
			DatePeriod datePeriod,
			GeneralDate procDate,
			AggregateTotalWorkingTime aggregateTotalWorkingTime,
			FlexTime flexTime){
		
		AttendanceTimeMonthWithMinus targetFlexExcessTime = new AttendanceTimeMonthWithMinus(0);

		// 「月別実績の就業時間」を取得する
		val workTime = aggregateTotalWorkingTime.getWorkTime();
		
		DatePeriod targetPeriod = new DatePeriod(datePeriod.start(), procDate);
		
		// 累計就業時間を集計する
		AttendanceTimeMonthWithMinus totalWorkTime =
				new AttendanceTimeMonthWithMinus(workTime.getTimeSeriesTotalLegalTime(targetPeriod).v());
		
		// 累計フレックス時間を集計する
		AttendanceTimeMonthWithMinus totalFlexTime =
				new AttendanceTimeMonthWithMinus(flexTime.getTimeSeriesTotalFlexTime(targetPeriod, false).v());
		
		// 「フレックス超過対象時間」を計算する
		targetFlexExcessTime = totalWorkTime.addMinutes(totalFlexTime.v());
		
		return targetFlexExcessTime;
	}
	
	/**
	 * 法定内フレックスを含んで当日までの超過時間を求める
	 * @param targetFlexExcessTime フレックス超過対象時間
	 * @param datePeriod 期間
	 * @param procDate 処理日
	 * @param aggregateTotalWorkingTime 集計総労働時間
	 * @param prescribedWorkingTimeMonth 月間所定労働時間
	 * @return 当日までの超過時間
	 */
	private AttendanceTimeMonthWithMinus askExcessTimeUntilDayIncludeLegalFlex(
			AttendanceTimeMonthWithMinus targetFlexExcessTime,
			DatePeriod datePeriod,
			GeneralDate procDate,
			AggregateTotalWorkingTime aggregateTotalWorkingTime,
			AttendanceTimeMonth prescribedWorkingTimeMonth){
		
		AttendanceTimeMonthWithMinus excessTimeUntilDay = new AttendanceTimeMonthWithMinus(0);

		// 代休使用時間を求める
		val compensatoryLeave = aggregateTotalWorkingTime.getVacationUseTime().getCompensatoryLeave();
		DatePeriod targetPeriod = new DatePeriod(datePeriod.start(), procDate);
		compensatoryLeave.aggregate(targetPeriod);
		val compensatoryLeaveUseTime = compensatoryLeave.getUseTime();
		
		// 所定労働時間から代休分を引く
		val diffMinutes = prescribedWorkingTimeMonth.v() - compensatoryLeaveUseTime.v();
		
		// 当日までの超過時間を求める
		excessTimeUntilDay = targetFlexExcessTime.minusMinutes(diffMinutes);
		
		return excessTimeUntilDay;
	}
	
	/**
	 * 通常・変形労働勤務の逆時系列割り当て
	 * @param regAndIrgTime 月別実績の通常変形時間
	 * @param aggregateTotalWorkingTime 総労働時間
	 * @param repositories 月次集計が必要とするリポジトリ
	 */
	private void assignReverseTimeSeriesOfRegAndIrg(
			RegularAndIrregularTimeOfMonthly regAndIrgTime,
			AggregateTotalWorkingTime aggregateTotalWorkingTime,
			RepositoriesRequiredByMonthlyAggr repositories){
		
		// 内訳項目一覧に週割増合計時間が設定されているか確認する
		if (!this.outsideOTSetOpt.isPresent()) return;
		val outsideOTSet = this.outsideOTSetOpt.get();
		boolean isExistWeekTotalPT = false;
		for (val outsideOTBDItem : outsideOTSet.getBreakdownItems()){
			for (val attendanceItemId : outsideOTBDItem.getAttendanceItemIds()){
				if (attendanceItemId == AttendanceItemOfMonthly.WEEKLY_TOTAL_PREMIUM_TIME.value){
					isExistWeekTotalPT = true;
					break;
				}
			}
		}
		if (!isExistWeekTotalPT) return;
		
		// 「労働制」を確認する
		if (this.workingSystem == WorkingSystem.REGULAR_WORK){
			
			// 通常勤務の月割増時間を逆時系列で割り当てる
			this.assignMonthlyPremiumTimeByReverseTimeSeriesOfReg(
					regAndIrgTime, aggregateTotalWorkingTime, repositories);
		}
		if (this.workingSystem == WorkingSystem.VARIABLE_WORKING_TIME_WORK){
			
			// 変形労働勤務の月割増時間を逆時系列で割り当てる
			this.assignMonthlyPremiumTimeByReverseTimeSeriesOfIrg(
					regAndIrgTime, aggregateTotalWorkingTime, repositories);
		}
	}
	
	/**
	 * 通常勤務の月割増時間を逆時系列で割り当てる
	 * @param regAndIrgTime 月別実績の通常変形時間
	 * @param aggregateTotalWorkingTime 総労働時間
	 * @param repositories 月次集計が必要とするリポジトリ
	 */
	private void assignMonthlyPremiumTimeByReverseTimeSeriesOfReg(
			RegularAndIrregularTimeOfMonthly regAndIrgTime,
			AggregateTotalWorkingTime aggregateTotalWorkingTime,
			RepositoriesRequiredByMonthlyAggr repositories){
		
		// 「週割増・月割増を求める」を取得する
		ExcessOutsideTimeSet excessOutsideTimeSet = new ExcessOutsideTimeSet();
		if (this.workingSystem == WorkingSystem.REGULAR_WORK){
			val aggrSetOfReg = this.aggrSettingMonthly.getRegularWork();
			excessOutsideTimeSet = aggrSetOfReg.getExcessOutsideTimeSet();
		}
		if (this.workingSystem == WorkingSystem.VARIABLE_WORKING_TIME_WORK){
			val aggrSetOfIrg = this.aggrSettingMonthly.getIrregularWork();
			excessOutsideTimeSet = aggrSetOfIrg.getExcessOutsideTimeSet();
		}
		if (!excessOutsideTimeSet.isAskPremium()) return;
		
		// 「月別実績の通常変形時間」から「逆時系列割り当て用の月割増時間」にコピーする
		val monthPremiumTime = regAndIrgTime.getMonthlyTotalPremiumTime();
		AttendanceTimeMonthWithMinus monthlyPTForAssign = new AttendanceTimeMonthWithMinus(monthPremiumTime.v());
		
		// 「月別実績の通常変形時間」から「月別実績の時間外超過」にコピーする
		this.excessOutsideWork.addMinutesToMonthlyTotalPremiumTime(monthPremiumTime.v());
		
		// 「期間．終了日」を処理日にする
		GeneralDate procDate = this.procPeriod.end();
		while (procDate.afterOrEquals(this.procPeriod.start())){
			
			// 月割増時間を日単位で割り当てる
			monthlyPTForAssign = this.monthlyDetail.assignMonthlyPremiumTimeByDayUnit(procDate, monthlyPTForAssign,
					aggregateTotalWorkingTime, this.workInformationOfDailyMap, this, repositories);
			
			// 時間外超過明細の更新
			this.excessOutsideWorkDetail = this.monthlyDetail.getExcessOutsideWorkMng().getExcessOutsideWorkDetail();
			
			if (monthlyPTForAssign.lessThanOrEqualTo(0)) break;
			
			// 処理日を更新する
			procDate = procDate.addDays(-1);
		}
	}
	
	/**
	 * 変形労働勤務の月割増時間を逆時系列で割り当てる
	 * @param regAndIrgTime 月別実績の通常変形時間
	 * @param aggregateTotalWorkingTime 総労働時間
	 * @param repositories 月次集計が必要とするリポジトリ
	 */
	private void assignMonthlyPremiumTimeByReverseTimeSeriesOfIrg(
			RegularAndIrregularTimeOfMonthly regAndIrgTime,
			AggregateTotalWorkingTime aggregateTotalWorkingTime,
			RepositoriesRequiredByMonthlyAggr repositories){
		
		// 処理月が含まれる精算期間が単月か複数月か確認する
		val settlementPeriod = this.aggrSettingMonthly.getIrregularWork().getSettlementPeriod();
		if (settlementPeriod.isSingleMonth(this.yearMonth)){
			
			// 単月の時、変形繰越時間を 0 にする
			this.excessOutsideWork.setDeformationCarryforwardTime(new AttendanceTimeMonthWithMinus(0));
			
			// 精算月の月割増時間を逆時系列で割り当てる　（単月）　※　通常勤務の方式と同じ
			this.assignMonthlyPremiumTimeByReverseTimeSeriesOfReg(
					regAndIrgTime, aggregateTotalWorkingTime, repositories);
		}
		else {
			
			// 複数月の時、変形繰越時間に月割増時間の値をコピーする
			val monthPremiumTime = regAndIrgTime.getMonthlyTotalPremiumTime();
			this.excessOutsideWork.setDeformationCarryforwardTime(new AttendanceTimeMonthWithMinus(monthPremiumTime.v()));
			
			// 精算月か確認する
			if (settlementPeriod.isSettlementMonth(this.yearMonth, this.isRetireMonth)){
				
				// 精算月の時、精算月の月割増時間を逆時系列で割り当てる　（複数月）
				this.assignMonthlyPremiumTimeByReverseTimeSeriesForMultiMonth(
						regAndIrgTime, aggregateTotalWorkingTime, repositories);
			}
			else {
				
				// 精算月でない時、月割増合計時間を 0 にする
				this.excessOutsideWork.setMonthlyTotalPremiumTime(new AttendanceTimeMonth(0));
			}
		}
	}
	
	/**
	 * 精算月の月割増時間を逆時系列で割り当てる　（複数月）
	 * @param regAndIrgTime 月別実績の通常変形時間
	 * @param aggregateTotalWorkingTime 総労働時間
	 * @param repositories 月次集計が必要とするリポジトリ
	 */
	private void assignMonthlyPremiumTimeByReverseTimeSeriesForMultiMonth(
			RegularAndIrregularTimeOfMonthly regAndIrgTime,
			AggregateTotalWorkingTime aggregateTotalWorkingTime,
			RepositoriesRequiredByMonthlyAggr repositories){
		
		// 精算期間を取得する
		val settlementPeriod = this.aggrSettingMonthly.getIrregularWork().getSettlementPeriod();
		val settlementMonths = settlementPeriod.getPastSettlementYearMonths(this.yearMonth);
		settlementMonths.add(this.yearMonth);
		int totalStatutoryWorkingMinutes = 0;
		int totalRecordMinutes = 0;
		for (val settlementMonth : settlementMonths){
			boolean isCurrentMonth = settlementMonth.equals(this.yearMonth);
			
			// 法定労働時間を取得する　（月間法定労働時間）
			//*****（未）　日次での実装位置を確認して、合わせて実装する。
			//*****（未）　参考（日次用）。このクラスか、別のクラスに、月・週用のメソッドを追加。仮に0設定。
			/*
			repositories.getGetOfStatutoryWorkTime().getDailyTimeFromStaturoyWorkTime(WorkingSystem.RegularWork,
					companyId, workplaceId, employmentCd, employeeId, datePeriod.end());
			*/
			val statutoryWorkingTimeMonth = new AttendanceTimeMonth(0);
			
			// 確認中年月の月別実績を確認する　（当月除く）
			List<AttendanceTimeOfMonthly> attendanceTimes = new ArrayList<>();
			if (!isCurrentMonth){
				attendanceTimes = repositories.getAttendanceTimeOfMonthly().findByYearMonthOrderByStartYmd(
						this.employeeId, settlementMonth);
			}
			
			// 取得した法令労働時間を法定労働時間累計に加算する
			totalStatutoryWorkingMinutes += statutoryWorkingTimeMonth.v();
			
			// 当月以外の過去月の時間を元に実績累計に加算する
			if (!isCurrentMonth){
				
				// 取得した法定労働時間を確認する
				if (statutoryWorkingTimeMonth.lessThanOrEqualTo(0)){
					
					// 就業時間＋変形期間繰越時間を実績累計に加算する
					for (val attendanceTime : attendanceTimes){
						val monthlyCalculation = attendanceTime.getMonthlyCalculation();
						val workTime = monthlyCalculation.getTotalWorkingTime().getWorkTime();
						val irregTime = monthlyCalculation.getActualWorkingTime().getIrregularWorkingTime();
						val irgPeriodCryfwdMinutes = irregTime.getIrregularPeriodCarryforwardTime().v();
						totalRecordMinutes += workTime.getWorkTime().v();
						if (irgPeriodCryfwdMinutes > 0) totalRecordMinutes += irgPeriodCryfwdMinutes;
					}
				}
				else {
					
					// 法定労働時間＋変形期間繰越時間を実績累計に加算する
					totalRecordMinutes += statutoryWorkingTimeMonth.v();
					for (val attendanceTime : attendanceTimes){
						val monthlyCalculation = attendanceTime.getMonthlyCalculation();
						val irregTime = monthlyCalculation.getActualWorkingTime().getIrregularWorkingTime();
						totalRecordMinutes += irregTime.getIrregularPeriodCarryforwardTime().v();
					}
				}
			}
		}
		
		// 境界時間を求める
		int boundaryMinutes = totalStatutoryWorkingMinutes - totalRecordMinutes;
		
		// 月割増合計時間を求める
		val monthlyPTTargetMinutes = regAndIrgTime.getIrregularPeriodCarryforwardsTime().getTime().v();
		int monthlyTotalPremiumMinutes = monthlyPTTargetMinutes - boundaryMinutes;
		if (monthlyTotalPremiumMinutes < 0) monthlyTotalPremiumMinutes = 0;
		this.excessOutsideWork.setMonthlyTotalPremiumTime(new AttendanceTimeMonth(monthlyTotalPremiumMinutes));
		
		// 月割増時間を逆時系列で割り当てる　※　通常勤務と同じ方式
		AttendanceTimeMonthWithMinus monthlyPTForAssign = new AttendanceTimeMonthWithMinus(monthlyTotalPremiumMinutes);
		this.assignMonthlyPremiumTimeByReverseTimeSeriesOfIrg(
				monthlyPTForAssign, aggregateTotalWorkingTime, repositories);
	}
	
	/**
	 * 変形労働勤務の月割増時間を逆時系列で割り当てる
	 * @param monthlyPTForAssign 逆時系列割り当て用の月割増時間
	 * @param aggregateTotalWorkingTime 総労働時間
	 * @param repositories 月次集計が必要とするリポジトリ
	 */
	private void assignMonthlyPremiumTimeByReverseTimeSeriesOfIrg(
			AttendanceTimeMonthWithMinus monthlyPTForAssign,
			AggregateTotalWorkingTime aggregateTotalWorkingTime,
			RepositoriesRequiredByMonthlyAggr repositories){
		
		// 「期間．終了日」を処理日にする
		GeneralDate procDate = this.procPeriod.end();
		while (procDate.afterOrEquals(this.procPeriod.start())){
			
			// 月割増時間を日単位で割り当てる
			monthlyPTForAssign = this.monthlyDetail.assignMonthlyPremiumTimeByDayUnit(procDate, monthlyPTForAssign,
					aggregateTotalWorkingTime, this.workInformationOfDailyMap, this, repositories);
			
			// 時間外超過明細の更新
			this.excessOutsideWorkDetail = this.monthlyDetail.getExcessOutsideWorkMng().getExcessOutsideWorkDetail();
			
			if (monthlyPTForAssign.lessThanOrEqualTo(0)) break;
			
			// 処理日を更新する
			procDate = procDate.addDays(-1);
		}
	}
	
	/**
	 * 便宜上集計の逆時系列割り当て　（フレックス）
	 * @param flexTimeOfMonthly 月別実績のフレックス時間
	 */
	private void assignReverseTimeSeriesForConvenience(
			FlexTimeOfMonthly flexTimeOfMonthly){

		// フレックス超過対象時間を求める
		int flexExcessTargetMinutes = flexTimeOfMonthly.getFlexExcessTime().v();
		flexExcessTargetMinutes += flexTimeOfMonthly.getFlexCarryforwardTime().getFlexCarryforwardWorkTime().v();
		AttendanceTimeMonthWithMinus flexExcessTargetTime = new AttendanceTimeMonthWithMinus(flexExcessTargetMinutes);
		
		// 「期間．終了日」を処理日にする
		GeneralDate procDate = this.procPeriod.end();
		while (procDate.afterOrEquals(this.procPeriod.start())){
			
			// フレックス超過対象時間を日単位で割り当てる
			flexExcessTargetTime = this.excessOutsideWorkDetail.assignFlexExcessTargetTimeByDayUnit(
					procDate, flexExcessTargetTime, flexTimeOfMonthly);
			
			if (flexExcessTargetTime.lessThanOrEqualTo(0)) break;
			
			// 処理日を更新する
			procDate = procDate.addDays(-1);
		}
	}
	
	/**
	 * 時間外超過内訳に割り当てる　（集計後）
	 */
	private void assignExcessOutsideWorkBreakdownForAfterAggregate(){
		
		// 「時間外超過の内訳項目」を取得する
		List<OutsideOTBRDItem> outsideOTBDItems = new ArrayList<>();
		if (this.outsideOTSetOpt.isPresent()){
			outsideOTBDItems = this.outsideOTSetOpt.get().getBreakdownItems();
		}
		outsideOTBDItems.removeIf(a -> { return a.getUseClassification() != UseClassification.UseClass_Use; });
		outsideOTBDItems.sort((a, b) -> a.getProductNumber().value - b.getProductNumber().value);
		for (val outsideOTBDItem : outsideOTBDItems){
			
			// 内訳項目に設定されている項目の値を取得する
			val totalTime = this.excessOutsideWorkDetail.getTotalTimeAfterRound();
			AttendanceTimeMonth breakdownItemTime = new AttendanceTimeMonth(0);
			for (val attendanceItemId : outsideOTBDItem.getAttendanceItemIds()){
				breakdownItemTime = breakdownItemTime.addMinutes(
						totalTime.getTimeOfAttendanceItemId(attendanceItemId).v());
			}
			if (breakdownItemTime.greaterThan(0)){
			
				// 内訳項目ごとに時間外超過の値を求める
				val breakdownItemNo =  outsideOTBDItem.getBreakdownItemNo().value;
				this.askExcessOutsideWorkEachBreakdown(breakdownItemTime, breakdownItemNo);
			}
		}
	}
	
	/**
	 * 時間外超過内訳に割り当てる　（逆時系列）
	 */
	private void assignExcessOutsideWorkBreakdownForReverseTimeSeries(){
		
		// 「期間．開始日」を処理日にする
		GeneralDate procDate = this.procPeriod.start();
		while (procDate.beforeOrEquals(this.procPeriod.end())){
			
			// 日ごとに時間外超過の値を求める
			this.askExcessOutsideWorkEachDay(procDate);
			
			// 処理日を更新する
			procDate = procDate.addDays(1);
		}
	}
	
	/**
	 * 日ごとに時間外超過の値を求める
	 * @param procDate 処理日
	 */
	private void askExcessOutsideWorkEachDay(GeneralDate procDate){
		
		// 「時間外超過の内訳項目」を取得する
		List<OutsideOTBRDItem> outsideOTBDItems = new ArrayList<>();
		if (this.outsideOTSetOpt.isPresent()){
			outsideOTBDItems = this.outsideOTSetOpt.get().getBreakdownItems();
		}
		outsideOTBDItems.removeIf(a -> { return a.getUseClassification() != UseClassification.UseClass_Use; });
		outsideOTBDItems.sort((a, b) -> a.getProductNumber().value - b.getProductNumber().value);
		for (val outsideOTBDItem : outsideOTBDItems){
			
			// 内訳項目に設定されている項目の値を取得する
			AttendanceTimeMonth breakdownItemTime = new AttendanceTimeMonth(0);
			for (val attendanceItemId : outsideOTBDItem.getAttendanceItemIds()){
				breakdownItemTime = breakdownItemTime.addMinutes(
						this.excessOutsideWorkDetail.getTimeOfAttendanceItemId(attendanceItemId, procDate).v());
			}
			if (breakdownItemTime.greaterThan(0)){
			
				// 内訳項目ごとに時間外超過の値を求める
				val breakdownItemNo =  outsideOTBDItem.getBreakdownItemNo().value;
				this.askExcessOutsideWorkEachBreakdown(breakdownItemTime, breakdownItemNo);
			}
		}
	}
	
	/**
	 * 内訳項目ごとに時間外超過の値を求める
	 * @param breakdownItemTime 内訳項目時間
	 * @param breakdownItemNo 内訳項目NO
	 */
	private void askExcessOutsideWorkEachBreakdown(AttendanceTimeMonth breakdownItemTime, int breakdownItemNo){
		
		// 「超過時間一覧」を取得する
		List<Overtime> overTimes = new ArrayList<>();
		if (this.outsideOTSetOpt.isPresent()){
			overTimes = this.outsideOTSetOpt.get().getOvertimes();
		}
		overTimes.removeIf(a -> { return a.getUseClassification() != UseClassification.UseClass_Use; });
		overTimes.sort((a, b) -> a.getOvertime().v() - b.getOvertime().v());
		
		// 時間外超過の時間を合計する　→　時間外超過累積時間
		val totalExcessOutside = this.excessOutsideWork.getTotalBreakdownTime();
		
		for (int ixOverTime = 0; ixOverTime < overTimes.size(); ixOverTime++){
			val overTime = overTimes.get(ixOverTime);
			
			// 時間外超過累積時間＋内訳項目時間と超過時間を比較する
			if (totalExcessOutside.v() + breakdownItemTime.v() <= overTime.getOvertime().v()) break;
			
			// 次の超過時間を取得する
			Overtime nextOverTime = null;
			if (ixOverTime + 1 < overTimes.size()) nextOverTime = overTimes.get(ixOverTime + 1);
			
			// 時間外超過の時間に加算する
			this.excessOutsideWork.addTimeFromBreakdownItemTime(
					totalExcessOutside, breakdownItemTime, overTime, nextOverTime, breakdownItemNo);
		}
	}
}
