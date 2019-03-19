package nts.uk.ctx.at.record.dom.monthlyprocess.aggr.work.excessoutside;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import lombok.Getter;
import lombok.val;
import nts.arc.time.GeneralDate;
import nts.arc.time.YearMonth;
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
import nts.uk.ctx.at.record.dom.monthlyaggrmethod.flex.FlexAggregateMethod;
import nts.uk.ctx.at.record.dom.monthlyprocess.aggr.MonthlyAggregationErrorInfo;
import nts.uk.ctx.at.record.dom.monthlyprocess.aggr.work.MonAggrCompanySettings;
import nts.uk.ctx.at.record.dom.monthlyprocess.aggr.work.MonAggrEmployeeSettings;
import nts.uk.ctx.at.record.dom.monthlyprocess.aggr.work.MonthlyCalculatingDailys;
import nts.uk.ctx.at.record.dom.monthlyprocess.aggr.work.RepositoriesRequiredByMonthlyAggr;
import nts.uk.ctx.at.record.dom.monthlyprocess.aggr.work.SettingRequiredByDefo;
import nts.uk.ctx.at.record.dom.monthlyprocess.aggr.work.SettingRequiredByFlex;
import nts.uk.ctx.at.record.dom.monthlyprocess.aggr.work.SettingRequiredByReg;
import nts.uk.ctx.at.record.dom.monthlyprocess.aggr.work.premiumtarget.getvacationaddtime.AddSet;
import nts.uk.ctx.at.record.dom.monthlyprocess.aggr.work.premiumtarget.getvacationaddtime.GetAddSet;
import nts.uk.ctx.at.record.dom.monthlyprocess.aggr.work.premiumtarget.getvacationaddtime.PremiumAtr;
import nts.uk.ctx.at.record.dom.monthlyprocess.aggr.work.timeseries.FlexTimeOfTimeSeries;
import nts.uk.ctx.at.record.dom.workrecord.monthcal.FlexMonthWorkTimeAggrSet;
import nts.uk.ctx.at.record.dom.workrecord.monthcal.export.GetSettlementPeriodOfDefor;
import nts.uk.ctx.at.shared.dom.WorkInformation;
import nts.uk.ctx.at.shared.dom.adapter.employee.EmployeeImport;
import nts.uk.ctx.at.shared.dom.common.Year;
import nts.uk.ctx.at.shared.dom.common.time.AttendanceTimeMonth;
import nts.uk.ctx.at.shared.dom.common.time.AttendanceTimeMonthWithMinus;
import nts.uk.ctx.at.shared.dom.outsideot.OutsideOTCalMed;
import nts.uk.ctx.at.shared.dom.outsideot.breakdown.OutsideOTBRDItem;
import nts.uk.ctx.at.shared.dom.outsideot.overtime.Overtime;
import nts.uk.ctx.at.shared.dom.workingcondition.WorkingConditionItem;
import nts.uk.ctx.at.shared.dom.workingcondition.WorkingSystem;
import nts.uk.ctx.at.shared.dom.workrule.closure.Closure;
import nts.uk.ctx.at.shared.dom.workrule.closure.ClosureId;
import nts.uk.shr.com.time.calendar.date.ClosureDate;
import nts.uk.shr.com.time.calendar.period.DatePeriod;

/**
 * 時間外超過管理
 * @author shuichi_ishida
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
	/** 社員 */
	private EmployeeImport employee;
	/** 職場ID */
	private String workplaceId;
	/** 雇用コード */
	private String employmentCd;
	/** 退職月度がどうか */
	private boolean isRetireMonth;
	/** 締め */
	private Optional<Closure> closureOpt;
	
	/** 通常勤務が必要とする設定 */
	private SettingRequiredByReg settingsByReg;
	/** 変形労働勤務が必要とする設定 */
	private SettingRequiredByDefo settingsByDefo;
	/** フレックス勤務が必要とする設定 */
	private SettingRequiredByFlex settingsByFlex;
	/** 月別集計で必要な会社別設定 */
	private MonAggrCompanySettings companySets;
	/** 月別集計で必要な社員別設定 */
	private MonAggrEmployeeSettings employeeSets;

	/** 月の計算中の日別実績データ */
	private MonthlyCalculatingDailys monthlyCalculatingDailys;
	/** 実績の勤務情報リスト */
	private Map<GeneralDate, WorkInformation> workInfoOfRecordMap;
	
	/** 開始週NO */
	private int startWeekNo;
	/** 年度 */
	private Year year;
	/** 管理期間の36協定時間 */
	private AgreementTimeOfManagePeriod agreementTimeOfManagePeriod;
	
	/** 時間外超過累積時間 */
	private AttendanceTimeMonth totalExcessOutside;
	/** エラー情報 */
	private List<MonthlyAggregationErrorInfo> errorInfos;
	
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
		this.employee = monthlyCalculation.getEmployee();
		this.workplaceId = monthlyCalculation.getWorkplaceId();
		this.employmentCd = monthlyCalculation.getEmploymentCd();
		this.isRetireMonth = monthlyCalculation.isRetireMonth();
		this.closureOpt = monthlyCalculation.getClosureOpt();
		
		this.settingsByReg = monthlyCalculation.getSettingsByReg();
		this.settingsByDefo = monthlyCalculation.getSettingsByDefo();
		this.settingsByFlex = monthlyCalculation.getSettingsByFlex();
		this.companySets = monthlyCalculation.getCompanySets();
		this.employeeSets = monthlyCalculation.getEmployeeSets();
		
		this.monthlyCalculatingDailys = monthlyCalculation.getMonthlyCalculatingDailys();
		this.workInfoOfRecordMap = monthlyCalculation.getWorkInfoOfRecordMap();
		
		this.startWeekNo = monthlyCalculation.getStartWeekNo();
		this.year = monthlyCalculation.getYear();
		this.agreementTimeOfManagePeriod = new AgreementTimeOfManagePeriod(this.employeeId, this.yearMonth);
		
		this.totalExcessOutside = new AttendanceTimeMonth(0);
		this.errorInfos = new ArrayList<>();
	}
	
	/**
	 * 集計
	 * @param repositories 月次集計が必要とするリポジトリ
	 */
	public void aggregate(RepositoriesRequiredByMonthlyAggr repositories){

		// 労働制を確認する
		if (this.workingSystem == WorkingSystem.REGULAR_WORK || this.workingSystem == WorkingSystem.VARIABLE_WORKING_TIME_WORK){
			
			// 通常・変形労働時間勤務の時間外超過を集計する
			this.aggregateExcessOutsideWork(null, repositories);
		}
		if (this.workingSystem == WorkingSystem.FLEX_TIME_WORK){
			
			// フレックス集計方法を確認する
			val flexAggregateMethod = this.settingsByFlex.getFlexAggrSet().getAggrMethod();
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
		OutsideOTCalMed calcMethod = this.companySets.getOutsideOverTimeSet().getCalculationMethod();
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
		aggregateTotalWorkingTime.copySharedItem(this.monthlyCalculation.getAggregateTime());
		AggregateMonthlyValue aggrValue = null;
		
		// 労働制を確認する
		if (workingSystem == WorkingSystem.REGULAR_WORK || workingSystem == WorkingSystem.VARIABLE_WORKING_TIME_WORK){
			
			// 通常・変形労働時間勤務の月別実績を集計する
			aggrValue = regAndIrgTime.aggregateMonthly(
					this.companyId, this.employeeId, this.yearMonth, this.closureId, this.closureDate,
					this.procPeriod, this.workingSystem, this.closureOpt,
					MonthlyAggregateAtr.EXCESS_OUTSIDE_WORK,
					this.employmentCd, this.settingsByReg, this.settingsByDefo,
					aggregateTotalWorkingTime, this, this.startWeekNo,
					this.companySets, this.employeeSets, this.monthlyCalculatingDailys, repositories);
			
			// 通常・変形労働時間勤務の月単位の時間を集計する
			regAndIrgTime.aggregateMonthlyHours(
					this.companyId, this.employeeId, this.yearMonth, this.closureId, this.closureDate,
					this.procPeriod, this.workingSystem, MonthlyAggregateAtr.EXCESS_OUTSIDE_WORK,
					this.isRetireMonth, this.workplaceId, this.employmentCd,
					this.settingsByReg, this.settingsByDefo,
					aggrValue.getAggregateTotalWorkingTime(), repositories);
		}
		if (workingSystem == WorkingSystem.FLEX_TIME_WORK){
			
			// フレックス勤務の月別実績を集計する
			aggrValue = flexTime.aggregateMonthly(
					this.companyId, this.employeeId, this.yearMonth, this.closureId, this.closureDate,
					this.procPeriod, this.workingSystem, MonthlyAggregateAtr.EXCESS_OUTSIDE_WORK,
					this.closureOpt, flexAggregateMethod, this.settingsByFlex,
					aggregateTotalWorkingTime, this, this.startWeekNo,
					this.companySets, this.employeeSets, this.monthlyCalculatingDailys, repositories);
			
			// フレックス勤務の月単位の時間を集計する
			flexTime.aggregateMonthlyHours(this.companyId, this.employeeId, this.yearMonth, this.procPeriod,
					flexAggregateMethod, this.workingConditionItem, this.workplaceId, this.employmentCd,
					this.settingsByFlex, aggrValue.getAggregateTotalWorkingTime(), repositories);
		}
		
		if (aggrValue != null){
			
			// 実働時間の集計
			aggrValue.getAggregateTotalWorkingTime().aggregateActualWorkingTime(
					this.procPeriod, this.workingSystem, regAndIrgTime, flexTime);
			
			// 時間外超過明細．丸め後合計時間に移送する
			this.excessOutsideWorkDetail.setTotalTimeAfterRound(
					aggrValue.getAggregateTotalWorkingTime(), regAndIrgTime, flexTime,
					this.settingsByFlex.getFlexAggrSet(), this.companySets.getOutsideOTBDItems(),
					this.companySets.getRoundingSet());
			
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
		aggregateTotalWorkingTime.copySharedItem(this.monthlyCalculation.getAggregateTime());
		AggregateMonthlyValue aggrValue = null;
		
		// 労働制を確認する
		if (this.workingSystem == WorkingSystem.REGULAR_WORK ||
			this.workingSystem == WorkingSystem.VARIABLE_WORKING_TIME_WORK){
			
			// 通常・変形労働時間勤務の月別実績を集計する
			aggrValue = regAndIrgTime.aggregateMonthly(
					this.companyId, this.employeeId, this.yearMonth, this.closureId, this.closureDate,
					this.procPeriod, this.workingSystem, this.closureOpt,
					MonthlyAggregateAtr.EXCESS_OUTSIDE_WORK,
					this.employmentCd, this.settingsByReg, this.settingsByDefo,
					aggregateTotalWorkingTime, this, this.startWeekNo,
					this.companySets, this.employeeSets, this.monthlyCalculatingDailys, repositories);
			
			// 通常・変形労働時間勤務の月単位の時間を集計する
			regAndIrgTime.aggregateMonthlyHours(
					this.companyId, this.employeeId, this.yearMonth, this.closureId, this.closureDate,
					this.procPeriod, this.workingSystem, MonthlyAggregateAtr.EXCESS_OUTSIDE_WORK,
					this.isRetireMonth, this.workplaceId, this.employmentCd,
					this.settingsByReg, this.settingsByDefo,
					aggrValue.getAggregateTotalWorkingTime(), repositories);
			
			// 月次明細に計算結果をコピーする
			this.monthlyDetail.setFromAggregateTotalWorkingTime(aggrValue.getAggregateTotalWorkingTime());
			
			// 通常・変形労働勤務の逆時系列割り当て
			this.assignReverseTimeSeriesOfRegAndIrg(
					regAndIrgTime, aggrValue.getAggregateTotalWorkingTime(), repositories);
		}
		if (this.workingSystem == WorkingSystem.FLEX_TIME_WORK){
			
			// フレックス勤務の月別実績を集計する
			aggrValue = flexTime.aggregateMonthly(
					this.companyId, this.employeeId, this.yearMonth, this.closureId, this.closureDate,
					this.procPeriod, this.workingSystem, MonthlyAggregateAtr.EXCESS_OUTSIDE_WORK,
					this.closureOpt, flexAggregateMethod, this.settingsByFlex,
					aggregateTotalWorkingTime, this, this.startWeekNo,
					this.companySets, this.employeeSets, this.monthlyCalculatingDailys, repositories);
			
			// フレックス勤務の月単位の時間を集計する
			flexTime.aggregateMonthlyHours(this.companyId, this.employeeId, this.yearMonth, this.procPeriod,
					flexAggregateMethod, this.workingConditionItem, this.workplaceId, this.employmentCd,
					this.settingsByFlex, aggrValue.getAggregateTotalWorkingTime(), repositories);
			
			// 月次明細に計算結果をコピーする
			this.monthlyDetail.setFromAggregateTotalWorkingTime(aggrValue.getAggregateTotalWorkingTime());
			
			// フレックス時間勤務の逆時系列割り当て
			if (flexAggregateMethod == FlexAggregateMethod.FOR_CONVENIENCE){
				
				// 便宜上集計の逆時系列割り当て
				this.assignReverseTimeSeriesForConvenience(flexTime);
			}
		}
		
		if (aggrValue != null){
		
			// 丸め時間を割り当てる
			this.excessOutsideWorkDetail.assignRoundTime(
					this.monthlyDetail, this.procPeriod, this.companySets.getRoundingSet());
			
			// 時間外超過内訳に割り当てる
			this.assignExcessOutsideWorkBreakdownForTimeSeries();
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
		boolean isAskPremium = false;
		if (this.workingSystem == WorkingSystem.REGULAR_WORK){
			isAskPremium = this.settingsByReg.getRegularAggrSet().getExcessOutsideTimeSet().getSurchargeWeekMonth();
		}
		if (this.workingSystem == WorkingSystem.VARIABLE_WORKING_TIME_WORK){
			isAskPremium = this.settingsByDefo.getDeforAggrSet().getExcessOutsideTimeSet().getSurchargeWeekMonth();
		}
		if (!isAskPremium) return;
		
		// 「時間外超過設定」を確認する　（計算方法が「時系列」以外なら割り当てない）
		if (this.companySets.getOutsideOverTimeSet().getCalculationMethod() != OutsideOTCalMed.TIME_SERIES) return;
		
		// 「週単位の週割増時間」を「逆時系列割り当て用の週割増時間」にコピーする
		AttendanceTimeMonthWithMinus weeklyPTForAssign = new AttendanceTimeMonthWithMinus(weekPremiumTime.v());
		
		// 「週単位の週割増時間」を「月別実績の時間外超過」に加算する
		this.excessOutsideWork.addMinutesToWeeklyTotalPremiumTime(weekPremiumTime.v());
		
		// 「期間．終了日」を処理日にする
		GeneralDate procDate = weekPermiumProcPeriod.end();
		while (procDate.afterOrEquals(weekPermiumProcPeriod.start())){
			
			// 週割増時間を日単位で割り当てる
			weeklyPTForAssign = this.monthlyDetail.assignWeeklyPremiumTimeByDayUnit(procDate, weeklyPTForAssign,
					aggregateTotalWorkingTime, this.workInfoOfRecordMap, this, repositories);
			
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
	 * @param flexAggrSet フレックス時間勤務の月の集計設定
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
			FlexMonthWorkTimeAggrSet flexAggrSet,
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
		
		// 法定内フレックス時間を含めるか判断する
		AttendanceTimeMonthWithMinus excessTimeUntilDay = new AttendanceTimeMonthWithMinus(0);
		if (ExcessOutsideWorkMng.isIncludeLegalFlexTime(this.companySets.getOutsideOTBDItems())){
			
			// 法定内フレックスを含んで当日までの超過時間を求める
			excessTimeUntilDay = this.askExcessTimeUntilDayIncludeLegalFlex(
					targetFlexExcessTime, procDate, prescribedWorkingTimeMonth);
		}
		else {
			
			// 法定外フレックスのみで当日までの超過時間を求める
			excessTimeUntilDay = this.askExcessTimeUntilDayOnlyillegalFlex(
					targetFlexExcessTime, procDate, statutoryWorkingTimeMonth);
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
	 * 法定内フレックス時間を含めるか判断する
	 * @return true:含める（法定内フレックスを含む）、false:含めない（法定外フレックスのみ）
	 */
	public static boolean isIncludeLegalFlexTime(List<OutsideOTBRDItem> outsideOTBDItems){

		// 内訳項目一覧を取得
		boolean isExistLegalFlex = false;
		boolean isExistIllegalFlex = false;
		for (val outsideOTBDItem : outsideOTBDItems){
			for (val attendanceItemId : outsideOTBDItem.getAttendanceItemIds()){
				if (attendanceItemId == AttendanceItemOfMonthly.FLEX_LEGAL_TIME.value) isExistLegalFlex = true;
				if (attendanceItemId == AttendanceItemOfMonthly.FLEX_ILLEGAL_TIME.value) isExistIllegalFlex = true;
			}
		}
		
		// 法定内フレックス時間を含めるかどうか判断
		if (isExistIllegalFlex == true && isExistLegalFlex == false) return false;
		return true;
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
		
		// 集計対象時間を集計する
		AttendanceTimeMonthWithMinus totalWorkTime =
				new AttendanceTimeMonthWithMinus(workTime.getAggregateTargetTime(targetPeriod).v());
		
		// 累計フレックス時間を集計する
		AttendanceTimeMonthWithMinus totalFlexTime =
				new AttendanceTimeMonthWithMinus(flexTime.getTimeSeriesTotalFlexTime(targetPeriod, true).v());
		
		// 「フレックス超過対象時間」を計算する
		targetFlexExcessTime = totalWorkTime.addMinutes(totalFlexTime.v());
		
		return targetFlexExcessTime;
	}
	
	/**
	 * 法定外フレックスのみで当日までの超過時間を求める
	 * @param targetFlexExcessTime フレックス超過対象時間
	 * @param procDate 処理日
	 * @param statutoryWorkingTimeMonth 月間法定労働時間
	 * @return 当日までの超過時間
	 */
	private AttendanceTimeMonthWithMinus askExcessTimeUntilDayOnlyillegalFlex(
			AttendanceTimeMonthWithMinus targetFlexExcessTime,
			GeneralDate procDate,
			AttendanceTimeMonth statutoryWorkingTimeMonth){
		
		AttendanceTimeMonthWithMinus excessTimeUntilDay = new AttendanceTimeMonthWithMinus(0);

		// 代休使用時間を求める
		val vacationUseTime = this.monthlyCalculation.getAggregateTime().getVacationUseTime();
		val compensatoryLeave = vacationUseTime.getCompensatoryLeave();
		DatePeriod targetPeriod = new DatePeriod(this.procPeriod.start(), procDate);
		val compensatoryLeaveTime = compensatoryLeave.getTotalUseTime(targetPeriod);
		
		// 法定労働時間から代休分を引く
		int afterDeduction = statutoryWorkingTimeMonth.v() - compensatoryLeaveTime.v();
		if (afterDeduction < 0) afterDeduction = 0;
		
		// 当日までの超過時間を求める
		excessTimeUntilDay = new AttendanceTimeMonthWithMinus(targetFlexExcessTime.v() - afterDeduction);
		
		return excessTimeUntilDay;
	}
	
	/**
	 * 法定内フレックスを含んで当日までの超過時間を求める
	 * @param targetFlexExcessTime フレックス超過対象時間
	 * @param procDate 処理日
	 * @param prescribedWorkingTimeMonth 月間所定労働時間
	 * @return 当日までの超過時間
	 */
	private AttendanceTimeMonthWithMinus askExcessTimeUntilDayIncludeLegalFlex(
			AttendanceTimeMonthWithMinus targetFlexExcessTime,
			GeneralDate procDate,
			AttendanceTimeMonth prescribedWorkingTimeMonth){
		
		AttendanceTimeMonthWithMinus excessTimeUntilDay = new AttendanceTimeMonthWithMinus(0);

		// 代休使用時間を求める
		val vacationUseTime = this.monthlyCalculation.getAggregateTime().getVacationUseTime();
		val compensatoryLeave = vacationUseTime.getCompensatoryLeave();
		DatePeriod targetPeriod = new DatePeriod(this.procPeriod.start(), procDate);
		val compensatoryLeaveTime = compensatoryLeave.getTotalUseTime(targetPeriod);
		
		// 所定労働時間から代休分を引く
		int afterDeduction = prescribedWorkingTimeMonth.v() - compensatoryLeaveTime.v();
		if (afterDeduction < 0) afterDeduction = 0;
		
		// 当日までの超過時間を求める
		excessTimeUntilDay = new AttendanceTimeMonthWithMinus(targetFlexExcessTime.v() - afterDeduction);
		
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
		val outsideOTSet = this.companySets.getOutsideOverTimeSet();
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
		boolean isAskPremium = false;
		if (this.workingSystem == WorkingSystem.REGULAR_WORK){
			isAskPremium = this.settingsByReg.getRegularAggrSet().getExcessOutsideTimeSet().getSurchargeWeekMonth();
		}
		if (this.workingSystem == WorkingSystem.VARIABLE_WORKING_TIME_WORK){
			isAskPremium = this.settingsByDefo.getDeforAggrSet().getExcessOutsideTimeSet().getSurchargeWeekMonth();
		}
		if (!isAskPremium) return;
		
		// 「月別実績の通常変形時間」から「逆時系列割り当て用の月割増時間」にコピーする
		val monthPremiumTime = regAndIrgTime.getMonthlyTotalPremiumTime();
		AttendanceTimeMonthWithMinus monthlyPTForAssign = new AttendanceTimeMonthWithMinus(monthPremiumTime.v());
		
		// 「月別実績の通常変形時間」から「月別実績の時間外超過」にコピーする
		this.excessOutsideWork.addMinutesToMonthlyTotalPremiumTime(monthPremiumTime.v());
		
		// 加算設定を取得する　（割増）
		AddSet addSet = new AddSet();
		if (this.workingSystem == WorkingSystem.REGULAR_WORK){
			addSet = GetAddSet.get(this.workingSystem, PremiumAtr.PREMIUM, this.settingsByReg.getHolidayAdditionMap());
		}
		if (this.workingSystem == WorkingSystem.VARIABLE_WORKING_TIME_WORK){
			addSet = GetAddSet.get(this.workingSystem, PremiumAtr.PREMIUM, this.settingsByDefo.getHolidayAdditionMap());
		}
		if (addSet.getErrorInfo().isPresent()){
			this.errorInfos.add(addSet.getErrorInfo().get());
		}
		
		// 「期間．終了日」を処理日にする
		GeneralDate procDate = this.procPeriod.end();
		while (procDate.afterOrEquals(this.procPeriod.start())){
			
			// 月割増時間を日単位で割り当てる
			monthlyPTForAssign = this.monthlyDetail.assignMonthlyPremiumTimeByDayUnit(procDate, monthlyPTForAssign,
					addSet, aggregateTotalWorkingTime, this.workInfoOfRecordMap, this, repositories);
			
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
		val settlementPeriod = GetSettlementPeriodOfDefor.createFromDeforAggrSet(this.settingsByDefo.getDeforAggrSet());
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
		
		// 精算期間を取得する　（当月除く過去分の期間内年月リスト）
		val settlementPeriod = GetSettlementPeriodOfDefor.createFromDeforAggrSet(this.settingsByDefo.getDeforAggrSet());
		val settlementMonths = settlementPeriod.getPastSettlementYearMonths(this.yearMonth);
		int totalStatutoryWorkingMinutes = 0;
		int totalRecordMinutes = 0;
		for (val settlementMonth : settlementMonths){
			
			// 法定労働時間を取得する
			val monAndWeekStatTimeOpt = repositories.getMonthlyStatutoryWorkingHours().getMonAndWeekStatutoryTime(
					this.companyId, this.employmentCd, this.employeeId, this.procPeriod.end(),
					settlementMonth, WorkingSystem.VARIABLE_WORKING_TIME_WORK);
			if (!monAndWeekStatTimeOpt.isPresent()) continue;
			int statutoryWorkingTimeMonth = monAndWeekStatTimeOpt.get().getMonthlyEstimateTime().v();
			
			// 確認中年月の月別実績を確認する
			List<AttendanceTimeOfMonthly> attendanceTimes = new ArrayList<>();
			attendanceTimes = repositories.getAttendanceTimeOfMonthly().findByYearMonthOrderByStartYmd(
					this.employeeId, settlementMonth);
			
			// 取得した法令労働時間を法定労働時間累計に加算する
			totalStatutoryWorkingMinutes += statutoryWorkingTimeMonth;
			
			// 取得した法定労働時間を確認する
			if (statutoryWorkingTimeMonth <= 0){
				
				// 就業時間＋変形期間繰越時間を実績累計に加算する
				for (val attendanceTime : attendanceTimes){
					val monthlyCalculation = attendanceTime.getMonthlyCalculation();
					val workTime = monthlyCalculation.getAggregateTime().getWorkTime();
					val irregTime = monthlyCalculation.getActualWorkingTime().getIrregularWorkingTime();
					val irgPeriodCryfwdMinutes = irregTime.getIrregularPeriodCarryforwardTime().v();
					totalRecordMinutes += workTime.getWorkTime().v();
					if (irgPeriodCryfwdMinutes > 0) totalRecordMinutes += irgPeriodCryfwdMinutes;
				}
			}
			else {
				
				// 法定労働時間＋変形期間繰越時間を実績累計に加算する
				totalRecordMinutes += statutoryWorkingTimeMonth;
				for (val attendanceTime : attendanceTimes){
					val monthlyCalculation = attendanceTime.getMonthlyCalculation();
					val irregTime = monthlyCalculation.getActualWorkingTime().getIrregularWorkingTime();
					totalRecordMinutes += irregTime.getIrregularPeriodCarryforwardTime().v();
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
		
		// 加算設定を取得する　（割増）
		val addSet = GetAddSet.get(this.workingSystem, PremiumAtr.PREMIUM, this.settingsByDefo.getHolidayAdditionMap());
		if (addSet.getErrorInfo().isPresent()){
			this.errorInfos.add(addSet.getErrorInfo().get());
		}
		
		// 「期間．終了日」を処理日にする
		GeneralDate procDate = this.procPeriod.end();
		while (procDate.afterOrEquals(this.procPeriod.start())){
			
			// 月割増時間を日単位で割り当てる
			monthlyPTForAssign = this.monthlyDetail.assignMonthlyPremiumTimeByDayUnit(procDate, monthlyPTForAssign,
					addSet, aggregateTotalWorkingTime, this.workInfoOfRecordMap, this, repositories);
			
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

		// 法定内フレックス時間を含めるか判断する
		AttendanceTimeMonthWithMinus flexExcessTargetTime = new AttendanceTimeMonthWithMinus(0);
		if (ExcessOutsideWorkMng.isIncludeLegalFlexTime(this.companySets.getOutsideOTBDItems())){
			
			// フレックス超過対象時間を求める（法定内フレックスを含む）
			int flexExcessTargetMinutes = flexTimeOfMonthly.getFlexExcessTime().v();
			flexExcessTargetMinutes += flexTimeOfMonthly.getFlexCarryforwardTime().getFlexCarryforwardWorkTime().v();
			flexExcessTargetTime = new AttendanceTimeMonthWithMinus(flexExcessTargetMinutes);
		}
		else {
			
			// フレックス超過対象時間を求める（法定外フレックスのみ）
			int flexExcessTargetMinutes = flexTimeOfMonthly.getFlexTime().getIllegalFlexTime().v();
			flexExcessTargetMinutes += flexTimeOfMonthly.getFlexCarryforwardTime().getFlexCarryforwardWorkTime().v();
			flexExcessTargetTime = new AttendanceTimeMonthWithMinus(flexExcessTargetMinutes);
		}
		
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
		
		// 「時間外超過累積時間」を作成する
		this.totalExcessOutside = new AttendanceTimeMonth(0);
		
		// 「時間外超過の内訳項目」を取得する
		for (val outsideOTBDItem : this.companySets.getOutsideOTBDItems()){
			
			// 内訳項目に設定されている項目の値を取得する
			val totalTime = this.excessOutsideWorkDetail.getTotalTimeAfterRound();
			AttendanceTimeMonth breakdownItemTime = new AttendanceTimeMonth(0);
			boolean addedFlexExcessTime = false;	// フレックス超過時間を足したか
			for (val attendanceItemId : outsideOTBDItem.getAttendanceItemIds()){
				
				// 法定内フレックス時間・法定外フレックス時間がある時、フレックス超過時間を1回だけ足す
				if (attendanceItemId == AttendanceItemOfMonthly.FLEX_LEGAL_TIME.value ||
					attendanceItemId == AttendanceItemOfMonthly.FLEX_ILLEGAL_TIME.value){
					if (addedFlexExcessTime) continue;
					breakdownItemTime = breakdownItemTime.addMinutes(
							totalTime.getTimeOfAttendanceItemId(AttendanceItemOfMonthly.FLEX_EXCESS_TIME.value).v());
					addedFlexExcessTime = true;
					continue;
				}
				
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
	 * 時間外超過内訳に割り当てる　（時系列用）
	 */
	private void assignExcessOutsideWorkBreakdownForTimeSeries(){
		
		// 「時間外超過累積時間」を作成する
		this.totalExcessOutside = new AttendanceTimeMonth(0);
		
		// 「期間．開始日」を処理日にする
		GeneralDate procDate = this.procPeriod.start();
		while (procDate.beforeOrEquals(this.procPeriod.end())){
			
			// 日ごとに時間外超過の値を求める
			this.askExcessOutsideWorkEachDay(procDate);
			
			// 処理日を更新する
			procDate = procDate.addDays(1);
		}
		
		// 丸め差分時間を時間外超過の値に割り当てる
		{
			// 「時間外超過の内訳項目」を取得する
			for (val outsideOTBDItem : this.companySets.getOutsideOTBDItems()){
				
				// 内訳項目に設定されている項目の値を取得する
				val roundDiffTime = this.excessOutsideWorkDetail.getRoundDiffTime();
				AttendanceTimeMonth breakdownItemTime = new AttendanceTimeMonth(0);
				boolean addedFlexExcessTime = false;	// フレックス超過時間を足したか
				for (val attendanceItemId : outsideOTBDItem.getAttendanceItemIds()){
					
					// 法定内フレックス時間・法定外フレックス時間がある時、フレックス超過時間を1回だけ足す
					if (attendanceItemId == AttendanceItemOfMonthly.FLEX_LEGAL_TIME.value ||
						attendanceItemId == AttendanceItemOfMonthly.FLEX_ILLEGAL_TIME.value){
						if (addedFlexExcessTime) continue;
						breakdownItemTime = breakdownItemTime.addMinutes(
								roundDiffTime.getTimeOfAttendanceItemId(AttendanceItemOfMonthly.FLEX_EXCESS_TIME.value).v());
						addedFlexExcessTime = true;
						continue;
					}
					
					breakdownItemTime = breakdownItemTime.addMinutes(
							roundDiffTime.getTimeOfAttendanceItemId(attendanceItemId).v());
				}
				if (breakdownItemTime.greaterThan(0)){
				
					// 内訳項目ごとに時間外超過の値を求める
					val breakdownItemNo =  outsideOTBDItem.getBreakdownItemNo().value;
					this.askExcessOutsideWorkEachBreakdown(breakdownItemTime, breakdownItemNo);
				}
			}
		}
	}
	
	/**
	 * 日ごとに時間外超過の値を求める
	 * @param procDate 処理日
	 */
	private void askExcessOutsideWorkEachDay(GeneralDate procDate){
		
		// 「時間外超過の内訳項目」を取得する
		for (val outsideOTBDItem : this.companySets.getOutsideOTBDItems()){
			
			// 内訳項目に設定されている項目の値を取得する
			AttendanceTimeMonth breakdownItemTime = new AttendanceTimeMonth(0);
			boolean addedFlexExcessTime = false;	// フレックス超過時間を足したか
			for (val attendanceItemId : outsideOTBDItem.getAttendanceItemIds()){
				
				// 法定内フレックス時間・法定外フレックス時間がある時、フレックス超過時間を1回だけ足す
				if (attendanceItemId == AttendanceItemOfMonthly.FLEX_LEGAL_TIME.value ||
					attendanceItemId == AttendanceItemOfMonthly.FLEX_ILLEGAL_TIME.value){
					if (addedFlexExcessTime) continue;
					breakdownItemTime = breakdownItemTime.addMinutes(
							this.excessOutsideWorkDetail.getTimeOfAttendanceItemId(
									AttendanceItemOfMonthly.FLEX_EXCESS_TIME.value, procDate).v());
					addedFlexExcessTime = true;
					continue;
				}
				
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
		val overTimes = this.companySets.getOutsideOTOverTimes();
		
		for (int ixOverTime = 0; ixOverTime < overTimes.size(); ixOverTime++){
			val overTime = overTimes.get(ixOverTime);
			
			// 時間外超過累積時間＋内訳項目時間と超過時間を比較する
			if (this.totalExcessOutside.v() + breakdownItemTime.v() <= overTime.getOvertime().v()) break;
			
			// 次の超過時間を取得する
			Overtime nextOverTime = null;
			if (ixOverTime + 1 < overTimes.size()) nextOverTime = overTimes.get(ixOverTime + 1);
			
			// 時間外超過の時間に加算する
			this.excessOutsideWork.addTimeFromBreakdownItemTime(
					this.totalExcessOutside, breakdownItemTime, overTime, nextOverTime, breakdownItemNo);
		}
		
		// 「内訳項目時間」を「時間外超過累積時間」に加算する
		this.totalExcessOutside = this.totalExcessOutside.addMinutes(breakdownItemTime.v());
	}
	
	/**
	 * エラー情報の取得
	 * @return エラー情報リスト
	 */
	public List<MonthlyAggregationErrorInfo> getErrorInfos(){
		
		List<MonthlyAggregationErrorInfo> results = new ArrayList<>();
		results.addAll(this.errorInfos);
		results.addAll(this.monthlyDetail.getErrorInfos());
		return results;
	}
}
