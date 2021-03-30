package nts.uk.ctx.at.shared.dom.scherec.monthlyattdcal.monthly.calc.actualworkingtime;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import lombok.Getter;
import lombok.Setter;
import lombok.val;
import nts.arc.diagnose.stopwatch.concurrent.ConcurrentStopwatches;
import nts.arc.layer.app.cache.CacheCarrier;
import nts.arc.time.GeneralDate;
import nts.arc.time.YearMonth;
import nts.arc.time.calendar.period.DatePeriod;
import nts.uk.ctx.at.shared.dom.common.time.AttendanceTimeMonth;
import nts.uk.ctx.at.shared.dom.common.time.AttendanceTimeMonthWithMinus;
import nts.uk.ctx.at.shared.dom.scherec.monthlyattdcal.aggr.MonthlyAggregationErrorInfo;
import nts.uk.ctx.at.shared.dom.scherec.monthlyattdcal.aggr.calcmethod.export.GetSettlementPeriodOfDefor;
import nts.uk.ctx.at.shared.dom.scherec.monthlyattdcal.aggr.vtotalmethod.AggregateMethodOfMonthly;
import nts.uk.ctx.at.shared.dom.scherec.monthlyattdcal.aggr.work.MonAggrCompanySettings;
import nts.uk.ctx.at.shared.dom.scherec.monthlyattdcal.aggr.work.MonAggrEmployeeSettings;
import nts.uk.ctx.at.shared.dom.scherec.monthlyattdcal.aggr.work.MonthlyCalculatingDailys;
import nts.uk.ctx.at.shared.dom.scherec.monthlyattdcal.aggr.work.SettingRequiredByDefo;
import nts.uk.ctx.at.shared.dom.scherec.monthlyattdcal.aggr.work.SettingRequiredByReg;
import nts.uk.ctx.at.shared.dom.scherec.monthlyattdcal.aggr.work.excessoutside.ExcessOutsideWorkMng;
import nts.uk.ctx.at.shared.dom.scherec.monthlyattdcal.aggr.work.premiumtarget.AddedVacationUseTime;
import nts.uk.ctx.at.shared.dom.scherec.monthlyattdcal.aggr.work.premiumtarget.IrregularPeriodCarryforwardsTimeOfCurrent;
import nts.uk.ctx.at.shared.dom.scherec.monthlyattdcal.aggr.work.premiumtarget.TargetPremiumTimeMonthOfRegular;
import nts.uk.ctx.at.shared.dom.scherec.monthlyattdcal.aggr.work.premiumtarget.TargetPrmTimeWeekOfPrevMonLast;
import nts.uk.ctx.at.shared.dom.scherec.monthlyattdcal.aggr.work.premiumtarget.getvacationaddtime.AddSet;
import nts.uk.ctx.at.shared.dom.scherec.monthlyattdcal.aggr.work.premiumtarget.getvacationaddtime.GetAddSet;
import nts.uk.ctx.at.shared.dom.scherec.monthlyattdcal.aggr.work.premiumtarget.getvacationaddtime.PremiumAtr;
import nts.uk.ctx.at.shared.dom.scherec.monthlyattdcal.monthly.AttendanceTimeOfMonthly;
import nts.uk.ctx.at.shared.dom.scherec.monthlyattdcal.monthly.calc.AggregateMonthlyValue;
import nts.uk.ctx.at.shared.dom.scherec.monthlyattdcal.monthly.calc.MonthlyAggregateAtr;
import nts.uk.ctx.at.shared.dom.scherec.monthlyattdcal.monthly.calc.MonthlyCalculation;
import nts.uk.ctx.at.shared.dom.scherec.monthlyattdcal.monthly.calc.totalworkingtime.AggregateTotalWorkingTime;
import nts.uk.ctx.at.shared.dom.scherec.monthlyattdcal.weekly.AttendanceTimeOfWeekly;
import nts.uk.ctx.at.shared.dom.workingcondition.WorkingConditionItem;
import nts.uk.ctx.at.shared.dom.workingcondition.WorkingSystem;
import nts.uk.ctx.at.shared.dom.workrecord.workperfor.dailymonthlyprocessing.ErrMessageContent;
import nts.uk.ctx.at.shared.dom.workrule.closure.Closure;
import nts.uk.ctx.at.shared.dom.workrule.closure.ClosureId;
import nts.uk.ctx.at.shared.dom.workrule.weekmanage.WeekRuleManagement;
import nts.uk.ctx.at.shared.dom.workrule.weekmanage.WeekStart;
import nts.uk.shr.com.i18n.TextResource;
import nts.uk.shr.com.time.calendar.date.ClosureDate;

/**
 * 月別実績の通常変形時間
 * @author shuichi_ishida
 */
@Getter
public class RegularAndIrregularTimeOfMonthly implements Serializable{

	/** Serializable */
	private static final long serialVersionUID = 1L;
	
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
	private DatePeriod weekAggrPeriod;
	/** 前月の最終週の週割増対象時間 */
	private AttendanceTimeMonth weekPremiumTimeOfPrevMonth;
	/** エラー情報 */
	private List<MonthlyAggregationErrorInfo> errorInfos;
	
	/**
	 * コンストラクタ
	 */
	public RegularAndIrregularTimeOfMonthly(){
		
		this.weeklyTotalPremiumTime = new AttendanceTimeMonth(0);
		this.monthlyTotalPremiumTime = new AttendanceTimeMonth(0);
		this.irregularWorkingTime = new IrregularWorkingTimeOfMonthly();
		
		this.irregularPeriodCarryforwardsTime = new IrregularPeriodCarryforwardsTimeOfCurrent();
		this.addedVacationUseTime = new AddedVacationUseTime();
		this.weekAggrPeriod = new DatePeriod(GeneralDate.today(), GeneralDate.today());
		this.weekPremiumTimeOfPrevMonth = new AttendanceTimeMonth(0);
		this.errorInfos = new ArrayList<>();
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
	 * @param closureId 締めID
	 * @param closureDate 締め日付
	 * @param datePeriod 期間
	 * @param workingSystem 労働制
	 * @param closureOpt 締め
	 * @param aggregateAtr 集計区分
	 * @param employmentCd 雇用コード
	 * @param settingsByReg 通常勤務が必要とする設定
	 * @param settingsByDefo 変形労働勤務が必要とする設定
	 * @param aggregateTotalWorkingTime 集計総労働時間
	 * @param excessOutsideWorkMng 時間外超過管理
	 * @param startWeekNo 開始週NO
	 * @param companySets 月別集計で必要な会社別設定
	 * @param employeeSets 月別集計で必要な社員別設定
	 * @param monthlyCalcDailys 月の計算中の日別実績データ
	 * @return 戻り値：月別実績を集計する
	 */
	public AggregateMonthlyValue aggregateMonthly(RequireM3 require, String companyId, String employeeId,
			YearMonth yearMonth, ClosureId closureId, ClosureDate closureDate, DatePeriod datePeriod,
			WorkingSystem workingSystem, Optional<Closure> closureOpt, MonthlyAggregateAtr aggregateAtr,
			String employmentCd, SettingRequiredByReg settingsByReg, SettingRequiredByDefo settingsByDefo,
			AggregateTotalWorkingTime aggregateTotalWorkingTime, ExcessOutsideWorkMng excessOutsideWorkMng,
			int startWeekNo, MonAggrCompanySettings companySets, MonAggrEmployeeSettings employeeSets,
			MonthlyCalculatingDailys monthlyCalcDailys) {
		
		List<AttendanceTimeOfWeekly> resultWeeks = new ArrayList<>();
		
		ConcurrentStopwatches.start("12222.1:週開始の取得：");
		
		// 週開始を取得する
		val workTimeSetOpt = require.weekRuleManagement(companyId);
		if (!workTimeSetOpt.isPresent()){
			this.errorInfos.add(new MonthlyAggregationErrorInfo(
					"005", new ErrMessageContent(TextResource.localize("Msg_1171"))));
			return AggregateMonthlyValue.of(aggregateTotalWorkingTime, excessOutsideWorkMng, resultWeeks);
		}
		
		WeekStart weekStart = workTimeSetOpt.get().getWeekStart();

		ConcurrentStopwatches.stop("12222.1:週開始の取得：");
		ConcurrentStopwatches.start("12222.2:前月の最終週：");
		
		this.weekPremiumTimeOfPrevMonth = new AttendanceTimeMonth(0);
		if (weekStart != WeekStart.TighteningStartDate){
			
			// 前月の最終週を集計する
			this.aggregateLastWeekOfPrevMonth(require, companyId, employeeId, datePeriod, workingSystem,
					aggregateAtr, settingsByReg, settingsByDefo,
					weekStart, startWeekNo, companySets, employeeSets, monthlyCalcDailys);
		}

		ConcurrentStopwatches.stop("12222.2:前月の最終週：");
		
		// 期間．開始日を処理日にする
		GeneralDate procDate = datePeriod.start();
		int procWeekNo = startWeekNo;
		
		// 「処理中の週開始日」を期間．開始日にする
		GeneralDate procWeekStartDate = datePeriod.start();
		
		// 処理をする期間の日数分ループ
		val attendanceTimeOfDailyMap = monthlyCalcDailys.getAttendanceTimeOfDailyMap();
		val workInformationOfDailyMap = monthlyCalcDailys.getWorkInfoOfDailyMap();
		while (procDate.beforeOrEquals(datePeriod.end())){
			
			if (attendanceTimeOfDailyMap.containsKey(procDate)){
				val attendanceTimeOfDaily = attendanceTimeOfDailyMap.get(procDate);
				
				// 処理日の職場コードを取得する
				String procWorkplaceId = "empty";
				val workplaceOpt = employeeSets.getWorkplace(procDate);
				if (workplaceOpt.isPresent()){
					procWorkplaceId = workplaceOpt.get().getWorkplaceId();
				}
				
				// 処理日の雇用コードを取得する
				String procEmploymentCd = "empty";
				val employmentOpt = employeeSets.getEmployment(procDate);
				if (employmentOpt.isPresent()){
					procEmploymentCd = employmentOpt.get().getEmploymentCode();
				}
				
				ConcurrentStopwatches.start("12222.3:日別実績の集計：");
				
				// 処理日の勤務情報を取得する
				if (workInformationOfDailyMap.containsKey(procDate)) {
					val workInfo = workInformationOfDailyMap.get(procDate).getRecordInfo();
					
					// 日別実績を集計する　（通常・変形労働時間勤務用）
					aggregateTotalWorkingTime.aggregateDailyForRegAndIrreg(require, procDate, attendanceTimeOfDaily,
							companyId, procWorkplaceId, procEmploymentCd, workingSystem, aggregateAtr,
							workInfo, settingsByReg, settingsByDefo, companySets, employeeSets);
				}
				
				ConcurrentStopwatches.stop("12222.3:日別実績の集計：");
			}
			
			// 週の集計をする日か確認する
			if (MonthlyCalculation.isAggregateWeek(procDate, weekStart, datePeriod, closureOpt)){
			
				// 週の期間を計算
				this.weekAggrPeriod = new DatePeriod(procWeekStartDate, procDate);
				
				// 翌週の開始日を設定しておく
				procWeekStartDate = procDate.addDays(1);
				
				// 対象の「週別実績の勤怠時間」を作成する
				val newWeek = new AttendanceTimeOfWeekly(employeeId, yearMonth, closureId, closureDate,
						procWeekNo, this.weekAggrPeriod);
				procWeekNo += 1;
				
				// 週別実績を集計する
				{
					ConcurrentStopwatches.start("12222.4:週別実績の集計：");
					
					// 週の計算
					val weekCalc = newWeek.getWeeklyCalculation();
					weekCalc.aggregate(companyId, employeeId, yearMonth, this.weekAggrPeriod,
							datePeriod, workingSystem, aggregateAtr, settingsByReg, settingsByDefo,
							aggregateTotalWorkingTime, weekStart, this.weekPremiumTimeOfPrevMonth,
							attendanceTimeOfDailyMap, companySets);
					resultWeeks.add(newWeek);
					if (weekCalc.getErrorInfos().size() > 0) this.errorInfos.addAll(weekCalc.getErrorInfos());

					ConcurrentStopwatches.stop("12222.4:週別実績の集計：");
					
					// 「前月の最終週の週割増対象時間 」を0にする　（前月の最終週の～は、1回目の週の計算だけで使う）
					this.weekPremiumTimeOfPrevMonth = new AttendanceTimeMonth(0);
					
					// 月の週割増合計へ
					val weekTime = weekCalc.getRegAndIrgTime();
					this.weeklyTotalPremiumTime = this.weeklyTotalPremiumTime.addMinutes(
							weekTime.getWeeklyTotalPremiumTime().v());
					
					// 集計区分を確認する
					if (aggregateAtr == MonthlyAggregateAtr.EXCESS_OUTSIDE_WORK && excessOutsideWorkMng != null){

						ConcurrentStopwatches.start("12222.5:時間外超過の集計：");
						
						// 時間外超過の集計
						newWeek.getExcessOutside().aggregate(weekCalc, companySets);
						
						ConcurrentStopwatches.stop("12222.5:時間外超過の集計：");
						ConcurrentStopwatches.start("12222.6:逆時系列割り当て：");
						
						// 時間外超過の時、週割増時間を逆時系列で割り当てる
						excessOutsideWorkMng.assignWeeklyPremiumTimeByReverseTimeSeries(require, 
								weekTime.getWeekPremiumProcPeriod(), weekTime.getWeeklyTotalPremiumTime(),
								aggregateTotalWorkingTime);
						
						ConcurrentStopwatches.stop("12222.6:逆時系列割り当て：");
					}
				}
			}
			
			procDate = procDate.addDays(1);
		}
		
		return AggregateMonthlyValue.of(aggregateTotalWorkingTime, excessOutsideWorkMng, resultWeeks);
	}
	
	/**
	 * 前月の最終週を集計する
	 * @param companyId 会社ID
	 * @param employeeId 社員ID
	 * @param datePeriod 期間
	 * @param workingSystem 労働制
	 * @param aggregateAtr 集計区分
	 * @param settingsByReg 通常勤務が必要とする設定
	 * @param settingsByDefo 変形労働勤務が必要とする設定
	 * @param weekStart 週開始
	 * @param startWeekNo 開始週NO
	 * @param companySets 月別集計で必要な会社別設定
	 * @param employeeSets 月別集計で必要な社員別設定
	 * @param monthlyCalcDailys 月の計算中の日別実績データ
	 */
	private void aggregateLastWeekOfPrevMonth(RequireM2 require, String companyId, String employeeId,
			DatePeriod datePeriod, WorkingSystem workingSystem, MonthlyAggregateAtr aggregateAtr,
			SettingRequiredByReg settingsByReg, SettingRequiredByDefo settingsByDefo, WeekStart weekStart,
			int startWeekNo, MonAggrCompanySettings companySets, MonAggrEmployeeSettings employeeSets,
			MonthlyCalculatingDailys monthlyCalcDailys){
		
		// 前月の最終週を集計するか判断する
		if(!shouldCalcPrevMonthLastWeek(require, employeeId, datePeriod, workingSystem, 
				startWeekNo, companySets.getVerticalTotalMethod()))
			return;
		
		// 前月の最終週の期間を求める
		DatePeriod lastWeekPeriod = getPrevMonthLastWeek(datePeriod, weekStart, employeeSets);
		if (lastWeekPeriod == null) return;
		
		// 最終週用の集計総労働時間を用意する
		AggregateTotalWorkingTime prevTotalWorkingTime = new AggregateTotalWorkingTime();
		
		val attendanceTimeOfDailyMap = monthlyCalcDailys.getAttendanceTimeOfDailyMap();
		val workInformationOfDailyMap = monthlyCalcDailys.getWorkInfoOfDailyMap();
		
		// 共有項目を集計する
		prevTotalWorkingTime.aggregateSharedItem(require, lastWeekPeriod, attendanceTimeOfDailyMap,
				workInformationOfDailyMap, monthlyCalcDailys.getSnapshots());

		GeneralDate procDate = lastWeekPeriod.start();
		while (procDate.beforeOrEquals(lastWeekPeriod.end())){
			
			if (attendanceTimeOfDailyMap.containsKey(procDate)){
				val attendanceTimeOfDaily = attendanceTimeOfDailyMap.get(procDate);
				
				// 処理日の職場コードを取得する
				String procWorkplaceId = "empty";
				val affWorkplaceOpt = employeeSets.getWorkplace(procDate);
				if (affWorkplaceOpt.isPresent()){
					procWorkplaceId = affWorkplaceOpt.get().getWorkplaceId();
				}
				
				// 処理日の雇用コードを取得する
				String procEmploymentCd = "empty";
				val employmentOpt = employeeSets.getEmployment(procDate);
				if (employmentOpt.isPresent()){
					procEmploymentCd = employmentOpt.get().getEmploymentCode();
				}
				
				// 処理日の勤務情報を取得する
				if (workInformationOfDailyMap.containsKey(procDate)) {
					val workInfo = workInformationOfDailyMap.get(procDate).getRecordInfo();
					
					// 日別実績を集計する　（通常・変形労働時間勤務用）
					prevTotalWorkingTime.aggregateDailyForRegAndIrreg(require, procDate, attendanceTimeOfDaily,
							companyId, procWorkplaceId, procEmploymentCd, workingSystem, aggregateAtr,
							workInfo, settingsByReg, settingsByDefo, companySets, employeeSets);
				}
			}
			
			// 処理日を更新する
			procDate = procDate.addDays(1);
		}

		// 加算設定取得　（割増用）
		AddSet addSet = new AddSet();
		if (workingSystem == WorkingSystem.REGULAR_WORK){
			addSet = GetAddSet.get(workingSystem, PremiumAtr.PREMIUM, settingsByReg.getHolidayAdditionMap());
		}
		if (workingSystem == WorkingSystem.VARIABLE_WORKING_TIME_WORK){
			addSet = GetAddSet.get(workingSystem, PremiumAtr.PREMIUM, settingsByDefo.getHolidayAdditionMap());
		}
		if (addSet.getErrorInfo().isPresent()){
			this.errorInfos.add(addSet.getErrorInfo().get());
		}
		
		// 前月の最終週の週割増時間を求める
		val weekPremiumTime = TargetPrmTimeWeekOfPrevMonLast.askPremiumTimeWeek(
				companyId, employeeId, lastWeekPeriod, addSet, prevTotalWorkingTime);
		this.weekPremiumTimeOfPrevMonth = new AttendanceTimeMonth(weekPremiumTime.getTargetTime().v());
	}

	/** ○前月の最終週の期間を求める */
	private DatePeriod getPrevMonthLastWeek(DatePeriod datePeriod, WeekStart weekStart,
			MonAggrEmployeeSettings employeeSets) {
		val employee = employeeSets.getEmployee();
		DatePeriod lastWeekPeriod = null;
		if (!MonthlyCalculation.isWeekStart(datePeriod.start(), weekStart)){
			GeneralDate startDate = datePeriod.start().addDays(-1);
			for (int i = 0; i < 6; i++){
				if (MonthlyCalculation.isWeekStart(startDate, weekStart)) break;
				startDate = startDate.addDays(-1);
			}
			GeneralDate endDate = datePeriod.start().addDays(-1);
			lastWeekPeriod = MonthlyCalculation.confirmProcPeriod(
					new DatePeriod(startDate, endDate),
					new DatePeriod(employee.getEntryDate(), employee.getRetiredDate()));
		}
		return lastWeekPeriod;
	}

	/** ○前月の最終週を集計するか判断する */
	private boolean shouldCalcPrevMonthLastWeek(RequireM2 require, String employeeId, DatePeriod datePeriod,
			WorkingSystem workingSystem, int startWeekNo, AggregateMethodOfMonthly verticalTotalMethod) {
		
		/** 「月別実績の縦計方法。前月の最終週を含めて計算するか」を確認する*/
		if (!verticalTotalMethod.isCalcWithPreviousMonthLastWeek()) return false;
		
		// 労働条件のループの1回目の処理か確認する　（週NOが1以外なら、その月度の1回目ではない）
		if (startWeekNo != 1) return false;
		
		// 前月の最終日の労働制を確認する
		val workingConditionItemOpt = require.workingConditionItem(employeeId, datePeriod.start().addDays(-1));
		if (!workingConditionItemOpt.isPresent()) return false;
		val prevWorkingSystem = workingConditionItemOpt.get().getLaborSystem();
		
		if (workingSystem == WorkingSystem.REGULAR_WORK){
			if (prevWorkingSystem != WorkingSystem.REGULAR_WORK) return false;
		}
		if (workingSystem == WorkingSystem.VARIABLE_WORKING_TIME_WORK){
			if (prevWorkingSystem == WorkingSystem.FLEX_TIME_WORK) return false;
		}
		
		return true;
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
	 * @param settingsByReg 通常勤務が必要とする設定
	 * @param settingsByDefo 変形労働勤務が必要とする設定
	 * @param aggregateTotalWorkingTime 総労働時間
	 */
	public void aggregateMonthlyHours(RequireM1 require, CacheCarrier cacheCarrier, String companyId, String employeeId, YearMonth yearMonth,
			ClosureId closureId, ClosureDate closureDate, DatePeriod datePeriod, WorkingSystem workingSystem,
			MonthlyAggregateAtr aggregateAtr, boolean isRetireMonth, String workplaceId, String employmentCd,
			SettingRequiredByReg settingsByReg, SettingRequiredByDefo settingsByDefo, 
			AggregateTotalWorkingTime aggregateTotalWorkingTime){
		
		// 通常勤務の時
		if (workingSystem == WorkingSystem.REGULAR_WORK){
			
			// 加算設定　取得　（割増用）
			val addSet = GetAddSet.get(workingSystem, PremiumAtr.PREMIUM, settingsByReg.getHolidayAdditionMap());
			if (addSet.getErrorInfo().isPresent()){
				this.errorInfos.add(addSet.getErrorInfo().get());
			}
			
			// 「割増を求める」がtrueの時
			val aggregateTimeSet = settingsByReg.getRegularAggrSet().getAggregateTimeSet();
			if (aggregateTimeSet.isSurchargeWeekMonth()){
			
				// 通常勤務の月単位の時間を集計する
				this.aggregateTimePerMonthOfRegular(companyId, employeeId, yearMonth, datePeriod,
						workplaceId, employmentCd, addSet, aggregateTotalWorkingTime,
						settingsByReg.getStatutoryWorkingTimeMonth());
			}
		}
		
		// 変形労働時間勤務の時
		if (workingSystem == WorkingSystem.VARIABLE_WORKING_TIME_WORK){
			
			// 加算設定　取得　（割増用）
			val addSet = GetAddSet.get(workingSystem, PremiumAtr.PREMIUM, settingsByDefo.getHolidayAdditionMap());
			if (addSet.getErrorInfo().isPresent()){
				this.errorInfos.add(addSet.getErrorInfo().get());
			}
			
			// 変形労働勤務の月単位の時間を集計する
			this.aggregateTimePerMonthOfIrregular(require, cacheCarrier, companyId, employeeId, yearMonth, closureId, 
													closureDate, datePeriod, employmentCd, isRetireMonth,
													settingsByDefo, addSet, aggregateTotalWorkingTime);
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
	 */
	private void aggregateTimePerMonthOfRegular(String companyId, String employeeId, YearMonth yearMonth, 
			DatePeriod datePeriod, String workplaceId, String employmentCd, AddSet addSet,
			AggregateTotalWorkingTime aggregateTotalWorkingTime, AttendanceTimeMonth statutoryWorkingTimeMonth){
		
		// 通常勤務の月割増時間の対象となる時間を求める
		val targetPremiumTimeMonthOfRegular = new TargetPremiumTimeMonthOfRegular();
		this.addedVacationUseTime = targetPremiumTimeMonthOfRegular.askPremiumTimeMonth(
				companyId, employeeId, datePeriod, addSet, aggregateTotalWorkingTime);
		val targetPremiumTimeMonth = targetPremiumTimeMonthOfRegular.getTargetPremiumTimeMonth();
		
		// 通常勤務の月割増対象時間　≦　法定労働時間　なら、処理終了
		if (targetPremiumTimeMonth.lessThanOrEqualTo(statutoryWorkingTimeMonth.v())) return;
		
		// 通常勤務の月割増対象時間が法定労働時間を超えた分を「月割増対象時間超過分」とする
		int excessTargetPremiumMinutes = targetPremiumTimeMonth.v();
		excessTargetPremiumMinutes -= statutoryWorkingTimeMonth.v();
		
		// 月割増対象時間超過分－週割増合計時間を月割増合計時間とする
		this.monthlyTotalPremiumTime = new AttendanceTimeMonth(
				excessTargetPremiumMinutes - this.weeklyTotalPremiumTime.v());
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
	 * @param settingsByDefo 変形労働勤務が必要とする設定
	 * @param addSet 加算設定
	 * @param aggregateTotalWorkingTime 集計総労働時間
	 */
	private void aggregateTimePerMonthOfIrregular(RequireM1 require, CacheCarrier cacheCarrier, String companyId, String employeeId,
			YearMonth yearMonth, ClosureId closureId, ClosureDate closureDate, DatePeriod datePeriod, String employmentCode,
			boolean isRetireMonth, SettingRequiredByDefo settingsByDefo,
			AddSet addSet, AggregateTotalWorkingTime aggregateTotalWorkingTime){
		
		// 当月の変形期間繰越時間を集計する
		this.irregularPeriodCarryforwardsTime = new IrregularPeriodCarryforwardsTimeOfCurrent();
		this.irregularPeriodCarryforwardsTime.aggregate(require, cacheCarrier, companyId, employeeId, datePeriod,
				yearMonth, datePeriod.end(), employmentCode, closureId,
				this.weeklyTotalPremiumTime, settingsByDefo.getHolidayAdditionMap(),
				aggregateTotalWorkingTime, settingsByDefo.getDefoAggregateMethod());
		this.addedVacationUseTime.addMinutesToAddTimePerMonth(
				this.irregularPeriodCarryforwardsTime.getAddedVacationUseTime().v());
		
		// 「変形労働時間勤務の法定内集計設定」を取得
		val deforAggrSet = settingsByDefo.getDeforAggrSet();
		
		// 変形労働精算期間の取得
		val setlPeriod = GetSettlementPeriodOfDefor.createFromDeforAggrSet(deforAggrSet);
		
		// 該当精算期間の開始月～前月の変形期間繰越時間を集計する
		val pastIrregularPeriodCarryforwardsTime = this.aggregatePastIrregularPeriodCarryforwardsTime(
				require, employeeId, yearMonth, closureId, closureDate, setlPeriod);
		
		// 開始月～当月の変形期間繰越時間を求める
		AttendanceTimeMonthWithMinus totalIrregularPeriodCarryforwardsTime = new AttendanceTimeMonthWithMinus(
				pastIrregularPeriodCarryforwardsTime.v());
		totalIrregularPeriodCarryforwardsTime = totalIrregularPeriodCarryforwardsTime.addMinutes(
				this.irregularPeriodCarryforwardsTime.getTime().v());

		// 精算月か確認する
		if (setlPeriod.isSettlementMonth(require, employeeId, datePeriod, yearMonth, isRetireMonth)) {

			// 精算月の時、月割増合計時間に集計結果を入れる
			/** ○月割増合計時間を月割増時間に入れる */
			if(totalIrregularPeriodCarryforwardsTime.isNegative()) {
				this.monthlyTotalPremiumTime = new AttendanceTimeMonth(0);
				this.irregularWorkingTime.setIrregularWorkingShortageTime(new AttendanceTimeMonth(Math.abs(totalIrregularPeriodCarryforwardsTime.valueAsMinutes())));
			} else {
				this.monthlyTotalPremiumTime = new AttendanceTimeMonth(totalIrregularPeriodCarryforwardsTime.v());
				this.irregularWorkingTime.setIrregularWorkingShortageTime(new AttendanceTimeMonth(0));
			}
			/** ○変形期間繰越時間を0にする */
			this.irregularWorkingTime.setIrregularPeriodCarryforwardTime(new AttendanceTimeMonthWithMinus(0));
		}
		else{
			
			// 精算月でない時、複数月変形途中時間・変形期間繰越時間に集計結果を入れる
			this.irregularWorkingTime.setMultiMonthIrregularMiddleTime(totalIrregularPeriodCarryforwardsTime);
			this.irregularWorkingTime.setIrregularPeriodCarryforwardTime(new AttendanceTimeMonthWithMinus(this.irregularPeriodCarryforwardsTime.getTime().v()));
		}
	}
	
	/**
	 * 精算期間．開始月～前月までの変形期間繰越時間を集計する
	 * @param employeeId 社員ID
	 * @param yearMonth 年月（度）
	 * @param closureId 締めID
	 * @param closureDate 締め日付
	 * @param setlPeriod 変形労働精算期間
	 * @return 過去の変形期間繰越時間
	 */
	private AttendanceTimeMonth aggregatePastIrregularPeriodCarryforwardsTime(RequireM1 require, String employeeId, 
			YearMonth yearMonth, ClosureId closureId, ClosureDate closureDate, GetSettlementPeriodOfDefor setlPeriod){
		
		AttendanceTimeMonth irregularPeriodCarryforwardsTime = new AttendanceTimeMonth(0);
		
		// 精算期間を取得する
		val pastYearMonths = setlPeriod.getPastSettlementYearMonths(yearMonth);
		
		// 開始月～前月までの変形期間繰越時間を集計する
		for (val pastYearMonth : pastYearMonths){
			val attendanceTimeList = require.attendanceTimeOfMonthly(employeeId, pastYearMonth);
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
	
	/**
	 * 総労働対象時間の取得
	 * @return 総労働対象時間
	 */
	public AttendanceTimeMonth getTotalWorkingTargetTime(){
		
		return new AttendanceTimeMonth(this.weeklyTotalPremiumTime.v() + this.monthlyTotalPremiumTime.v() +
				this.irregularWorkingTime.getTotalWorkingTargetTime().v());
	}
	
	/**
	 * 合算する
	 * @param target 加算対象
	 */
	public void sum(RegularAndIrregularTimeOfMonthly target){
		
		this.weeklyTotalPremiumTime = this.weeklyTotalPremiumTime.addMinutes(target.weeklyTotalPremiumTime.v());
		this.monthlyTotalPremiumTime = this.monthlyTotalPremiumTime.addMinutes(target.monthlyTotalPremiumTime.v());
		this.irregularWorkingTime.sum(target.irregularWorkingTime);
	}
	
	
	public static interface RequireM1 extends IrregularPeriodCarryforwardsTimeOfCurrent.Require, GetSettlementPeriodOfDefor.Require {

		List<AttendanceTimeOfMonthly> attendanceTimeOfMonthly(String employeeId, YearMonth yearMonth);
	}

	public static interface RequireM2 extends AggregateTotalWorkingTime.RequireM3, 
												AggregateTotalWorkingTime.RequireM2 {
		
		Optional<WorkingConditionItem> workingConditionItem(String employeeId, GeneralDate baseDate);
	}

	public static interface RequireM3 extends MonAggrCompanySettings.RequireM1, RequireM2, 
												ExcessOutsideWorkMng.RequireM4 {
		
		Optional<WeekRuleManagement> weekRuleManagement(String cid);
	}
}