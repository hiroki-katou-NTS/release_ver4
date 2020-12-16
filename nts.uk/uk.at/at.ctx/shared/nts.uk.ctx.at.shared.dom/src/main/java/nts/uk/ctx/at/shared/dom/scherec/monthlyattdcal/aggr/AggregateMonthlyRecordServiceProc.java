package nts.uk.ctx.at.shared.dom.scherec.monthlyattdcal.aggr;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CountDownLatch;

import javax.enterprise.concurrent.ManagedExecutorService;

import lombok.Getter;
import lombok.Setter;
import lombok.val;
//import lombok.extern.slf4j.Slf4j;
import nts.arc.diagnose.stopwatch.concurrent.ConcurrentStopwatches;
import nts.arc.layer.app.cache.CacheCarrier;
import nts.arc.task.AsyncTask;
import nts.arc.time.GeneralDate;
import nts.arc.time.YearMonth;
import nts.arc.time.calendar.period.DatePeriod;
import nts.gul.util.value.MutableValue;
import nts.uk.ctx.at.shared.dom.adapter.employee.EmployeeImport;
import nts.uk.ctx.at.shared.dom.common.WorkplaceId;
import nts.uk.ctx.at.shared.dom.common.anyitem.AnyTimesMonth;
import nts.uk.ctx.at.shared.dom.common.days.AttendanceDaysMonth;
import nts.uk.ctx.at.shared.dom.common.days.MonthlyDays;
import nts.uk.ctx.at.shared.dom.remainingnumber.absencerecruitment.export.query.AbsRecMngInPeriodParamInput;
import nts.uk.ctx.at.shared.dom.remainingnumber.absencerecruitment.export.query.AbsRecRemainMngOfInPeriod;
import nts.uk.ctx.at.shared.dom.remainingnumber.absencerecruitment.export.query.AbsenceReruitmentMngInPeriodQuery;
import nts.uk.ctx.at.shared.dom.remainingnumber.absencerecruitment.interim.InterimAbsMng;
import nts.uk.ctx.at.shared.dom.remainingnumber.absencerecruitment.interim.InterimRecMng;
import nts.uk.ctx.at.shared.dom.remainingnumber.algorithm.DailyInterimRemainMngData;
import nts.uk.ctx.at.shared.dom.remainingnumber.algorithm.InterimRemainCreateDataInputPara;
import nts.uk.ctx.at.shared.dom.remainingnumber.algorithm.InterimRemainOffPeriodCreateData;
import nts.uk.ctx.at.shared.dom.remainingnumber.algorithm.RecordRemainCreateInfor;
import nts.uk.ctx.at.shared.dom.remainingnumber.annualleave.GetDaysForCalcAttdRate;
import nts.uk.ctx.at.shared.dom.remainingnumber.annualleave.empinfo.maxdata.RemainingMinutes;
import nts.uk.ctx.at.shared.dom.remainingnumber.annualleave.export.CreateInterimAnnualMngData;
import nts.uk.ctx.at.shared.dom.remainingnumber.annualleave.export.GetAnnAndRsvRemNumWithinPeriod;
import nts.uk.ctx.at.shared.dom.remainingnumber.annualleave.export.InterimRemainMngMode;
import nts.uk.ctx.at.shared.dom.remainingnumber.annualleave.export.param.CalYearOffWorkAttendRate;
import nts.uk.ctx.at.shared.dom.remainingnumber.annualleave.interim.TmpAnnualLeaveMngWork;
import nts.uk.ctx.at.shared.dom.remainingnumber.breakdayoffmng.export.query.BreakDayOffMngInPeriodQuery;
import nts.uk.ctx.at.shared.dom.remainingnumber.breakdayoffmng.export.query.BreakDayOffRemainMngOfInPeriod;
import nts.uk.ctx.at.shared.dom.remainingnumber.breakdayoffmng.export.query.BreakDayOffRemainMngParam;
import nts.uk.ctx.at.shared.dom.remainingnumber.breakdayoffmng.interim.InterimBreakMng;
import nts.uk.ctx.at.shared.dom.remainingnumber.breakdayoffmng.interim.InterimDayOffMng;
import nts.uk.ctx.at.shared.dom.remainingnumber.export.param.AggrResultOfAnnAndRsvLeave;
import nts.uk.ctx.at.shared.dom.remainingnumber.interimremain.InterimRemain;
import nts.uk.ctx.at.shared.dom.remainingnumber.reserveleave.interim.TmpReserveLeaveMngWork;
import nts.uk.ctx.at.shared.dom.remainingnumber.service.RemainNumberCreateInformation;
import nts.uk.ctx.at.shared.dom.remainingnumber.specialholidaymng.interim.InterimSpecialHolidayMng;
import nts.uk.ctx.at.shared.dom.remainingnumber.specialleave.service.ComplileInPeriodOfSpecialLeaveParam;
import nts.uk.ctx.at.shared.dom.remainingnumber.specialleave.service.InPeriodOfSpecialLeave;
import nts.uk.ctx.at.shared.dom.remainingnumber.specialleave.service.InPeriodOfSpecialLeaveResultInfor;
import nts.uk.ctx.at.shared.dom.remainingnumber.specialleave.service.RemainDaysOfSpecialHoliday;
import nts.uk.ctx.at.shared.dom.remainingnumber.specialleave.service.SpecialLeaveManagementService;
import nts.uk.ctx.at.shared.dom.remainingnumber.specialleave.service.SpecialLeaveRemainNoMinus;
import nts.uk.ctx.at.shared.dom.remainingnumber.work.CompanyHolidayMngSetting;
import nts.uk.ctx.at.shared.dom.scherec.dailyattdcal.autocalsetting.JobTitleId;
import nts.uk.ctx.at.shared.dom.scherec.dailyattdcal.dailyattendance.dailyattendancework.IntegrationOfDaily;
import nts.uk.ctx.at.shared.dom.scherec.dailyattdcal.dailyattendance.timesheet.ouen.OuenWorkTimeOfDailyAttendance;
import nts.uk.ctx.at.shared.dom.scherec.dailyattdcal.dailyattendance.timesheet.ouen.OuenWorkTimeSheetOfDailyAttendance;
import nts.uk.ctx.at.shared.dom.scherec.monthlyattdcal.aggr.converter.MonthlyRecordToAttendanceItemConverter;
import nts.uk.ctx.at.shared.dom.scherec.monthlyattdcal.aggr.export.pererror.CreatePerErrorsFromLeaveErrors;
import nts.uk.ctx.at.shared.dom.scherec.monthlyattdcal.aggr.work.AggregateAttendanceTimeValue;
import nts.uk.ctx.at.shared.dom.scherec.monthlyattdcal.aggr.work.MonAggrCompanySettings;
import nts.uk.ctx.at.shared.dom.scherec.monthlyattdcal.aggr.work.MonAggrEmployeeSettings;
import nts.uk.ctx.at.shared.dom.scherec.monthlyattdcal.aggr.work.MonthlyCalculatingDailys;
import nts.uk.ctx.at.shared.dom.scherec.monthlyattdcal.aggr.work.MonthlyOldDatas;
import nts.uk.ctx.at.shared.dom.scherec.monthlyattdcal.aggr.work.anyitem.AnyItemAggrResult;
import nts.uk.ctx.at.shared.dom.scherec.monthlyattdcal.aggr.work.excessoutside.ExcessOutsideWorkMng;
import nts.uk.ctx.at.shared.dom.scherec.monthlyattdcal.agreement.management.setting.AgreementOperationSetting;
import nts.uk.ctx.at.shared.dom.scherec.monthlyattdcal.monthly.AttendanceTimeOfMonthly;
import nts.uk.ctx.at.shared.dom.scherec.monthlyattdcal.monthly.IntegrationOfMonthly;
import nts.uk.ctx.at.shared.dom.scherec.monthlyattdcal.monthly.affiliation.AffiliationInfoOfMonthly;
import nts.uk.ctx.at.shared.dom.scherec.monthlyattdcal.monthly.affiliation.AggregateAffiliationInfo;
import nts.uk.ctx.at.shared.dom.scherec.monthlyattdcal.monthly.anyitem.AggregateAnyItem;
import nts.uk.ctx.at.shared.dom.scherec.monthlyattdcal.monthly.anyitem.AnyItemOfMonthly;
import nts.uk.ctx.at.shared.dom.scherec.monthlyattdcal.monthly.calc.MonthlyAggregateAtr;
import nts.uk.ctx.at.shared.dom.scherec.monthlyattdcal.monthly.calc.MonthlyCalculation;
import nts.uk.ctx.at.shared.dom.scherec.monthlyattdcal.monthly.editstate.EditStateOfMonthlyPerformance;
import nts.uk.ctx.at.shared.dom.scherec.monthlyattdcal.monthly.erroralarm.EmployeeMonthlyPerError;
import nts.uk.ctx.at.shared.dom.scherec.monthlyattdcal.monthly.erroralarm.ErrorType;
import nts.uk.ctx.at.shared.dom.scherec.monthlyattdcal.monthly.erroralarm.Flex;
import nts.uk.ctx.at.shared.dom.scherec.monthlyattdcal.monthly.ouen.aggframe.OuenAggregateFrameSetOfMonthly;
import nts.uk.ctx.at.shared.dom.scherec.monthlyattdcal.monthly.totalcount.TotalCountByPeriod;
import nts.uk.ctx.at.shared.dom.scherec.monthlyattdcal.monthly.vacation.ClosureStatus;
import nts.uk.ctx.at.shared.dom.scherec.monthlyattdcal.monthly.vacation.absenceleave.AbsenceLeaveRemainData;
import nts.uk.ctx.at.shared.dom.scherec.monthlyattdcal.monthly.vacation.absenceleave.AttendanceDaysMonthToTal;
import nts.uk.ctx.at.shared.dom.scherec.monthlyattdcal.monthly.vacation.absenceleave.RemainDataDaysMonth;
import nts.uk.ctx.at.shared.dom.scherec.monthlyattdcal.monthly.vacation.annualleave.AnnLeaRemNumEachMonth;
import nts.uk.ctx.at.shared.dom.scherec.monthlyattdcal.monthly.vacation.annualleave.AnnualLeaveAttdRateDays;
import nts.uk.ctx.at.shared.dom.scherec.monthlyattdcal.monthly.vacation.dayoff.DayOffDayAndTimes;
import nts.uk.ctx.at.shared.dom.scherec.monthlyattdcal.monthly.vacation.dayoff.DayOffRemainDayAndTimes;
import nts.uk.ctx.at.shared.dom.scherec.monthlyattdcal.monthly.vacation.dayoff.MonthlyDayoffRemainData;
import nts.uk.ctx.at.shared.dom.scherec.monthlyattdcal.monthly.vacation.dayoff.RemainDataTimesMonth;
import nts.uk.ctx.at.shared.dom.scherec.monthlyattdcal.monthly.vacation.reserveleave.ReserveLeaveGrant;
import nts.uk.ctx.at.shared.dom.scherec.monthlyattdcal.monthly.vacation.reserveleave.RsvLeaRemNumEachMonth;
import nts.uk.ctx.at.shared.dom.scherec.monthlyattdcal.monthly.vacation.specialholiday.SpecialHolidayRemainData;
import nts.uk.ctx.at.shared.dom.scherec.monthlyattdcal.monthly.verticaltotal.VerticalTotalOfMonthly;
import nts.uk.ctx.at.shared.dom.scherec.monthlyattdcal.weekly.AttendanceTimeOfWeekly;
import nts.uk.ctx.at.shared.dom.scherec.optitem.PerformanceAtr;
import nts.uk.ctx.at.shared.dom.scherec.optitem.applicable.EmpCondition;
import nts.uk.ctx.at.shared.dom.specialholiday.SpecialHoliday;
import nts.uk.ctx.at.shared.dom.workingcondition.WorkingCondition;
import nts.uk.ctx.at.shared.dom.workingcondition.WorkingConditionItem;
import nts.uk.ctx.at.shared.dom.workrecord.workperfor.dailymonthlyprocessing.ErrMessageContent;
import nts.uk.ctx.at.shared.dom.workrule.closure.Closure;
import nts.uk.ctx.at.shared.dom.workrule.closure.ClosureId;
import nts.uk.ctx.at.shared.dom.worktype.WorkType;
import nts.uk.shr.com.i18n.TextResource;
import nts.uk.shr.com.time.calendar.date.ClosureDate;

/**
 * 処理：ドメインサービス：月別実績を集計する
 * 
 * @author shuichi_ishida
 */
@Getter
/* @Slf4j */
public class AggregateMonthlyRecordServiceProc {

	/** 集計結果 */
	private AggregateMonthlyRecordValue aggregateResult;
	/** エラー情報 */
	private Map<String, MonthlyAggregationErrorInfo> errorInfos;
	/** 編集状態リスト */
	private List<EditStateOfMonthlyPerformance> editStates;

	/** 会社ID */
	@Setter
	private String companyId;
	/** 社員ID */
	@Setter
	private String employeeId;
	/** 年月 */
	private YearMonth yearMonth;
	/** 締めID */
	private ClosureId closureId;
	/** 締め日 */
	private ClosureDate closureDate;

	/** 月別集計で必要な会社別設定 */
	@Setter
	private MonAggrCompanySettings companySets;
	/** 月別集計で必要な社員別設定 */
	private MonAggrEmployeeSettings employeeSets;
	/** 月の計算中の日別実績データ */
	@Setter
	private MonthlyCalculatingDailys monthlyCalculatingDailys;
	/** 集計前の月別実績データ */
	private MonthlyOldDatas monthlyOldDatas;
	/** 労働条件項目 */
	private List<WorkingConditionItem> workingConditionItems;
	/** 労働条件 */
	private Map<String, DatePeriod> workingConditions;
	/** 前回集計結果 （年休積立年休の集計結果） */
	private AggrResultOfAnnAndRsvLeave prevAggrResult;
	/** 前回集計結果 （振休振出の集計結果） */
	private Optional<AbsRecRemainMngOfInPeriod> prevAbsRecResultOpt;
	/** 前回集計結果 （代休の集計結果） */
	private Optional<BreakDayOffRemainMngOfInPeriod> prevBreakDayOffResultOpt;
	/** 前回集計結果 （特別休暇の集計結果） */
	private Map<Integer, InPeriodOfSpecialLeaveResultInfor> prevSpecialLeaveResultMap;
	/** 週NO管理 */
	private Map<YearMonth, Integer> weekNoMap;
	/** 手修正あり */
	private boolean isRetouch;
	/** 暫定残数データ */
	private Map<GeneralDate, DailyInterimRemainMngData> dailyInterimRemainMngs;
	/** 暫定残数データ上書きフラグ */
	private boolean isOverWriteRemain;
	/** 並列処理用 */
//	private ManagedExecutorService executerService;

	public AggregateMonthlyRecordServiceProc() {
	}

	/**
	 * 集計処理 
	 * @param companyId 会社ID
	 * @param employeeId 社員ID
	 * @param yearMonth 年月
	 * @param closureId 締めID
	 * @param closureDate 締め日付
	 * @param datePeriod 期間
	 * @param prevAggrResult 前回集計結果 （年休積立年休の集計結果）
	 * @param prevAbsRecResultOpt 前回集計結果 （振休振出の集計結果）
	 * @param prevBreakDayOffResultOpt 前回集計結果 （代休の集計結果）
	 * @param prevSpecialLeaveResultMap 前回集計結果 （特別休暇の集計結果）
	 * @param companySets 月別集計で必要な会社別設定
	 * @param employeeSets 月別集計で必要な社員別設定
	 * @param dailyWorksOpt 日別実績(WORK)List
	 * @param monthlyWorkOpt 月別実績(WORK)
	 * @param remainingProcAtr 残数処理フラグ
	 * @return 集計結果
	 */
	public AggregateMonthlyRecordValue aggregate(RequireM15 require, CacheCarrier cacheCarrier, String companyId,
			String employeeId, YearMonth yearMonth, ClosureId closureId, ClosureDate closureDate, DatePeriod datePeriod,
			AggrResultOfAnnAndRsvLeave prevAggrResult, Optional<AbsRecRemainMngOfInPeriod> prevAbsRecResultOpt,
			Optional<BreakDayOffRemainMngOfInPeriod> prevBreakDayOffResultOpt,
			Map<Integer, InPeriodOfSpecialLeaveResultInfor> prevSpecialLeaveResultMap,
			MonAggrCompanySettings companySets, MonAggrEmployeeSettings employeeSets,
			Optional<List<IntegrationOfDaily>> dailyWorksOpt, Optional<IntegrationOfMonthly> monthlyWorkOpt,
			Boolean remainingProcAtr) {

		this.aggregateResult = new AggregateMonthlyRecordValue();
		this.errorInfos = new HashMap<>();
		this.editStates = new ArrayList<>();

		this.companyId = companyId;
		this.employeeId = employeeId;
		this.yearMonth = yearMonth;
		this.closureId = closureId;
		this.closureDate = closureDate;
		this.prevAggrResult = prevAggrResult;
		this.prevAbsRecResultOpt = prevAbsRecResultOpt;
		this.prevBreakDayOffResultOpt = prevBreakDayOffResultOpt;
		this.prevSpecialLeaveResultMap = prevSpecialLeaveResultMap;
		this.weekNoMap = new HashMap<>();
		this.isRetouch = false;
		this.dailyInterimRemainMngs = new HashMap<>();
		this.isOverWriteRemain = false;

		ConcurrentStopwatches.start("12100:集計期間ごと準備：");

		this.companySets = companySets;
		this.employeeSets = employeeSets;

		// 社員を取得する
		EmployeeImport employee = this.employeeSets.getEmployee();

		// 入社前、退職後を期間から除く → 一か月の集計期間
		val termInOffice = new DatePeriod(employee.getEntryDate(), employee.getRetiredDate());
		DatePeriod monthPeriod = this.confirmProcPeriod(datePeriod, termInOffice);
		if (monthPeriod == null) {
			// 処理期間全体が、入社前または退職後の時
			return this.aggregateResult;
		}

		// 前月の36協定の集計がありえるため、前月分のデータも読み込んでおく （Redmine#107701）
		DatePeriod loadPeriod = new DatePeriod(monthPeriod.start(), monthPeriod.end());
		if (monthPeriod.start().after(employee.getEntryDate())) { // 開始日が入社日より後の時のみ、前月を読み込む
			loadPeriod = new DatePeriod(monthPeriod.start().addMonths(-1), monthPeriod.end());
		}

		// 計算に必要なデータを準備する
		this.monthlyCalculatingDailys = MonthlyCalculatingDailys.loadData(require, 
				employeeId, loadPeriod, dailyWorksOpt, employeeSets);
		
		// 集計前の月別実績データを確認する
		this.monthlyOldDatas = MonthlyOldDatas.loadData(require, employeeId, yearMonth, 
				closureId, closureDate, monthlyWorkOpt);

		// 「労働条件項目」を取得
		List<WorkingConditionItem> workingConditionItems = require.workingConditionItem(employeeId, monthPeriod);
		if (workingConditionItems.isEmpty()) {
			this.aggregateResult.addErrorInfos("001", new ErrMessageContent(TextResource.localize("Msg_430")));
			return this.aggregateResult;
		}

		// 同じ労働制の履歴を統合
		this.IntegrateHistoryOfSameWorkSys(require, workingConditionItems);

		// 所属情報の作成
		val affiliationInfo = this.createAffiliationInfo(monthPeriod);
		
		if (affiliationInfo == null) {
			for (val errorInfo : this.errorInfos.values()) {
				this.aggregateResult.getErrorInfos().putIfAbsent(errorInfo.getResourceId(), errorInfo);
			}
			return this.aggregateResult;
		}
		this.aggregateResult.setAffiliationInfo(Optional.of(affiliationInfo));

		// 残数と集計の処理を並列で実行
		{
			CountDownLatch cdlAggregation = new CountDownLatch(1);
			MutableValue<RuntimeException> excepAggregation = new MutableValue<>();

			// 日別修正等の画面から実行されている場合、集計処理を非同期で実行
			Runnable asyncAggregation = () -> {
				try {
					this.aggregationProcess(require, cacheCarrier, monthPeriod);
				} catch (RuntimeException ex) {
					excepAggregation.set(ex);
				} finally {
					cdlAggregation.countDown();
				}
			};

			if (Thread.currentThread().getName().indexOf("REQUEST:") == 0) {
				require.getExecutorService().submit(AsyncTask.builder().withContexts()
						.threadName("Aggregation").build(asyncAggregation));
			} else {
				// バッチなどの場合は非同期にせずそのまま実行
				asyncAggregation.run();
			}

			// 残数処理
			// こちらはDB書き込みをしているので非同期化できない
			this.remainingProcess(require, cacheCarrier, monthPeriod, datePeriod, remainingProcAtr);

			// 非同期実行中の集計処理と待ち合わせ
			try {
				cdlAggregation.await();
			} catch (InterruptedException e) {
				throw new RuntimeException(e);
			}

			// 集計処理で例外が起きていたらここでthrow
			if (excepAggregation.optional().isPresent()) {
				throw excepAggregation.get();
			}
		}
		
		if (this.aggregateResult.getAttendanceTime().isPresent()) {

			/** 応援作業の集計 */
			this.aggregateResult.getAttendanceTime().get().aggregateOuen(
					createAttendanceTimeOfMonthlyRequire(require, dailyWorksOpt), 
					employeeId, loadPeriod);	
		}

		// 月別実績の編集状態 取得
		this.editStates = require.monthEditStates(this.employeeId, this.yearMonth, this.closureId, this.closureDate);

		if (this.aggregateResult.getAttendanceTime().isPresent()) {
			AttendanceTimeOfMonthly attendanceTime = this.aggregateResult.getAttendanceTime().get();

			// 手修正された項目を元に戻す （勤怠時間用）
			attendanceTime = this.undoRetouchValuesForAttendanceTime(require, attendanceTime, this.monthlyOldDatas);

			// 手修正を戻してから計算必要な項目を再度計算
			if (this.isRetouch) {
				this.aggregateResult.setAttendanceTime(Optional.of(this.recalcAttendanceTime(attendanceTime)));
			}
		}

		ConcurrentStopwatches.start("12300:36協定時間：");

		// 基本計算結果を確認する
		Optional<MonthlyCalculation> basicCalced = Optional.empty();
		if (this.aggregateResult.getAttendanceTime().isPresent()) {
			basicCalced = Optional.of(this.aggregateResult.getAttendanceTime().get().getMonthlyCalculation());
		}

		// 36協定時間の集計
		{
			// 36協定時間の集計
			MonthlyCalculation monthlyCalculationForAgreement = new MonthlyCalculation();
			
			val agreementTime = monthlyCalculationForAgreement.aggregateAgreementTime(require, cacheCarrier,
					this.companyId, this.employeeId, this.yearMonth, this.closureId, this.closureDate, monthPeriod,
					Optional.empty(), Optional.empty(), Optional.empty(), this.companySets, this.employeeSets,
					this.monthlyCalculatingDailys, this.monthlyOldDatas, basicCalced);
			
			if (agreementTime.getAgreementTime().isPresent()) {
				
				this.aggregateResult.getAgreementTimeList().add(agreementTime.getAgreementTime().get());
			} else {
				if (!agreementTime.getError().isEmpty()) {
					val error = agreementTime.getError().get(0);
					this.aggregateResult.addErrorInfos(error.getResourceId(), error.getMessage());
				}
			}

			// 36協定運用設定を取得
			if (this.companySets.getAgreementOperationSet().isPresent()) {
				AgreementOperationSetting agreementOpeSet = this.companySets.getAgreementOperationSet().get();

				if (!this.closureDate.equals(agreementOpeSet.getClosureDate()) && // 締めが異なる
																						// かつ
						monthPeriod.start().after(employee.getEntryDate())) { // 開始日が入社日より後の時

					// 集計期間を一ヶ月手前にずらす
					YearMonth prevYM = this.yearMonth.addMonths(-1);
					GeneralDate prevEnd = monthPeriod.start().addDays(-1);
					GeneralDate prevStart = prevEnd.addMonths(-1).addDays(1);
					DatePeriod prevPeriod = new DatePeriod(prevStart, prevEnd);

					// 36協定時間の集計
					MonthlyOldDatas prevOldDatas = MonthlyOldDatas.loadData(require, employeeId, prevYM, closureId, closureDate,
							Optional.empty());
					MonthlyCalculation prevCalculationForAgreement = new MonthlyCalculation();
					
					val prevAgreTime = prevCalculationForAgreement.aggregateAgreementTime(require,
							cacheCarrier, this.companyId, this.employeeId, prevYM, this.closureId, this.closureDate,
							prevPeriod, Optional.empty(), Optional.empty(), Optional.empty(), this.companySets,
							this.employeeSets, this.monthlyCalculatingDailys, prevOldDatas, Optional.empty());
					
					if (prevAgreTime.getAgreementTime().isPresent()) {
						
						this.aggregateResult.getAgreementTimeList().add(prevAgreTime.getAgreementTime().get());
					} else {
						if (!prevAgreTime.getError().isEmpty()) {
							val error = prevAgreTime.getError().get(0);
							this.aggregateResult.addErrorInfos(error.getResourceId(), error.getMessage());
						}
					}
				}
			}
		}

		ConcurrentStopwatches.stop("12300:36協定時間：");
		ConcurrentStopwatches.start("12500:任意項目：");

		// 大塚カスタマイズの集計
		Map<Integer, Map<Integer, AnyItemAggrResult>> anyItemCustomizeValue = this
				.aggregateCustomizeForOtsuka(require, cacheCarrier, this.yearMonth, this.closureId, this.companySets);

		// 月別実績の任意項目を集計
		this.aggregateAnyItem(require, monthPeriod, anyItemCustomizeValue);

		// 手修正された項目を元に戻す （任意項目用）
		this.undoRetouchValuesForAnyItems(require, this.monthlyOldDatas);

		ConcurrentStopwatches.stop("12500:任意項目：");
		ConcurrentStopwatches.start("12600:大塚カスタマイズ：");

		// 大塚カスタマイズ
		this.customizeForOtsuka();

		ConcurrentStopwatches.stop("12600:大塚カスタマイズ：");

		// 戻り値にエラー情報を移送
		for (val errorInfo : this.errorInfos.values()) {
			this.aggregateResult.getErrorInfos().putIfAbsent(errorInfo.getResourceId(), errorInfo);
		}

		return this.aggregateResult;
	}

	/**
	 * 集計処理
	 * 
	 * @param monthPeriod
	 */
	private void aggregationProcess(RequireM13 require, CacheCarrier cacheCarrier, DatePeriod monthPeriod) {
		ConcurrentStopwatches.stop("12100:集計期間ごと準備：");

		// 項目の数だけループ
		List<Flex> perErrorsForFlex = new ArrayList<>();
		for (val workingConditionItem : this.workingConditionItems) {

			ConcurrentStopwatches.start("12200:労働条件ごと：");

			// 「労働条件」の該当履歴から期間を取得
			val historyId = workingConditionItem.getHistoryId();
			if (!this.workingConditions.containsKey(historyId))
				continue;

			// 処理期間を計算 （一か月の集計期間と労働条件履歴期間の重複を確認する）
			val term = this.workingConditions.get(historyId);
			DatePeriod aggrPeriod = this.confirmProcPeriod(monthPeriod, term);
			if (aggrPeriod == null) {
				// 履歴の期間と重複がない時
				continue;
			}

			// 月別実績の勤怠時間を集計
			val aggregateResult = this.aggregateAttendanceTime(require, cacheCarrier, aggrPeriod, workingConditionItem);
			val attendanceTime = aggregateResult.getAttendanceTime();
			if (attendanceTime == null)
				continue;

			// 社員の月別実績のエラー（フレックス不足補填）を確認する
			val resultPerErrorsForFlex = attendanceTime.getMonthlyCalculation().getFlexTime().getPerErrors();
			for (val resultPerError : resultPerErrorsForFlex) {
				if (!perErrorsForFlex.contains(resultPerError))
					perErrorsForFlex.add(resultPerError);
			}

			// データを合算する
			if (this.aggregateResult.getAttendanceTime().isPresent()) {
				val calcedAttendanceTime = this.aggregateResult.getAttendanceTime().get();
				attendanceTime.sum(calcedAttendanceTime);
			}

			// 計算中のエラー情報の取得
			val monthlyCalculation = attendanceTime.getMonthlyCalculation();
			for (val errorInfo : monthlyCalculation.getErrorInfos()) {
				this.errorInfos.putIfAbsent(errorInfo.getResourceId(), errorInfo);
			}

			// 計算結果を戻り値に蓄積
			this.aggregateResult.setAttendanceTime(Optional.of(attendanceTime));
			this.aggregateResult.getAttendanceTimeWeeks().addAll(aggregateResult.getAttendanceTimeWeeks());

			ConcurrentStopwatches.stop("12200:労働条件ごと：");
		}

		// 社員の月別実績のエラー（フレックス不足補填）を出力する
		for (val perError : perErrorsForFlex) {
			this.aggregateResult.getPerErrors().add(new EmployeeMonthlyPerError(ErrorType.FLEX, this.yearMonth,
					this.employeeId, this.closureId, this.closureDate, perError, null, null));
		}

		// 合算後のチェック処理
		this.checkAfterSum(monthPeriod);
	}

	/**
	 * 残数処理
	 * 
	 * @param monthPeriod
	 *            1か月の集計期間
	 * @param datePeriod
	 *            期間（実行期間）
	 * @param remainingProcAtr
	 *            残数処理フラグ
	 */
	private void remainingProcess(RequireM8 require, CacheCarrier cacheCarrier, DatePeriod monthPeriod,
			DatePeriod datePeriod, Boolean remainingProcAtr) {

		ConcurrentStopwatches.start("12400:残数処理：");

		// 当月の判断
		boolean isCurrentMonth = false;
		if (this.companySets.getCurrentMonthPeriodMap().containsKey(this.closureId.value)) {
			DatePeriod currentPeriod = this.companySets.getCurrentMonthPeriodMap().get(this.closureId.value);
			// 当月の締め期間と実行期間を比較し、重複した期間を取り出す
			DatePeriod confPeriod = this.confirmProcPeriod(currentPeriod, datePeriod);
			// 重複期間があれば、当月
			if (confPeriod != null)
				isCurrentMonth = true;
		}
		if (remainingProcAtr == true)
			isCurrentMonth = true;

		// 2019.4.25 UPD shuichi_ishida Redmine#107271(1)(EA.3359)
		// 残数処理を実行する当月判断方法を変更
		// 集計開始日を締め開始日をする時だけ、残数処理を実行する （集計期間の初月（＝締めの当月）だけ実行する）
		// if (this.employeeSets.isNoCheckStartDate()){
		// 実行期間が当月の締め期間に含まれる時だけ、残数処理を実行する
		if (isCurrentMonth) {

			// 残数処理
			this.remainingProcess(require, cacheCarrier, monthPeriod, InterimRemainMngMode.MONTHLY, true);
		}

		ConcurrentStopwatches.stop("12400:残数処理：");
	}

	/**
	 * 同じ労働制の履歴を統合
	 * 
	 * @param target
	 *            労働条件項目リスト （統合前）
	 * @return 労働条件項目リスト （統合後）
	 */
	private void IntegrateHistoryOfSameWorkSys(RequireM14 require, List<WorkingConditionItem> target) {

		this.workingConditionItems = new ArrayList<>();
		this.workingConditions = new HashMap<>();

		val itrTarget = target.listIterator();
		while (itrTarget.hasNext()) {

			// 要素[n]を取得
			WorkingConditionItem startItem = itrTarget.next();
			val startHistoryId = startItem.getHistoryId();
			val startConditionOpt = require.workingCondition(startHistoryId);
			if (!startConditionOpt.isPresent())
				continue;
			val startCondition = startConditionOpt.get();
			if (startCondition.getDateHistoryItem().isEmpty())
				continue;
			DatePeriod startPeriod = startCondition.getDateHistoryItem().get(0).span();

			// 要素[n]と要素[n+1]以降を順次比較
			WorkingConditionItem endItem = null;
			while (itrTarget.hasNext()) {
				WorkingConditionItem nextItem = target.get(itrTarget.nextIndex());
				if (startItem.getLaborSystem() != nextItem.getLaborSystem()
						|| startItem.getHourlyPaymentAtr() != nextItem.getHourlyPaymentAtr()) {

					// 労働制または時給者区分が異なる履歴が見つかった時点で、労働条件の統合をやめる
					break;
				}

				// 労働制と時給者区分が同じ履歴の要素を順次取得
				endItem = itrTarget.next();
			}

			// 次の要素がなくなった、または、異なる履歴が見つかれば、集計要素を確定する
			if (endItem == null) {
				this.workingConditionItems.add(startItem);
				this.workingConditions.putIfAbsent(startHistoryId, startPeriod);
				continue;
			}
			val endHistoryId = endItem.getHistoryId();
			val endConditionOpt = require.workingCondition(endHistoryId);
			if (!endConditionOpt.isPresent())
				continue;
			;
			val endCondition = endConditionOpt.get();
			if (endCondition.getDateHistoryItem().isEmpty())
				continue;
			this.workingConditionItems.add(endItem);
			this.workingConditions.putIfAbsent(endHistoryId,
					new DatePeriod(startPeriod.start(), endCondition.getDateHistoryItem().get(0).end()));
		}
	}

	/**
	 * 月別実績の勤怠時間を集計
	 * 
	 * @param datePeriod 期間
	 * @param workingConditionItem 労働条件項目
	 * @return 月別実績の勤怠時間
	 */
	private AggregateAttendanceTimeValue aggregateAttendanceTime(RequireM13 require, CacheCarrier cacheCarrier,
			DatePeriod datePeriod, WorkingConditionItem workingConditionItem) {

		AggregateAttendanceTimeValue result = new AggregateAttendanceTimeValue();

		// 週Noを確認する
		this.weekNoMap.putIfAbsent(this.yearMonth, 0);
		val startWeekNo = this.weekNoMap.get(this.yearMonth) + 1;

		// 労働制を確認する
		val workingSystem = workingConditionItem.getLaborSystem();

		ConcurrentStopwatches.start("12210:集計準備：");

		// 月別実績の勤怠時間 初期設定
		val attendanceTime = new AttendanceTimeOfMonthly(this.employeeId, this.yearMonth, this.closureId,
				this.closureDate, datePeriod);
		attendanceTime.prepareAggregation(require, cacheCarrier, this.companyId, datePeriod,
				workingConditionItem, startWeekNo, this.companySets, this.employeeSets, this.monthlyCalculatingDailys,
				this.monthlyOldDatas);
		val monthlyCalculation = attendanceTime.getMonthlyCalculation();
		if (monthlyCalculation.getErrorInfos().size() > 0) {
			for (val errorInfo : monthlyCalculation.getErrorInfos()) {
				this.errorInfos.putIfAbsent(errorInfo.getResourceId(), errorInfo);
			}
			return result;
		}

		ConcurrentStopwatches.stop("12210:集計準備：");
		ConcurrentStopwatches.start("12220:月の計算：");

		// 月の計算
		monthlyCalculation.aggregate(require, cacheCarrier, datePeriod, MonthlyAggregateAtr.MONTHLY, Optional.empty(),
				Optional.empty(), Optional.empty());

		ConcurrentStopwatches.stop("12220:月の計算：");
		ConcurrentStopwatches.start("12230:縦計：");

		// 縦計
		{
			// 週単位の期間を取得
			for (val attendanceTimeWeek : attendanceTime.getMonthlyCalculation().getAttendanceTimeWeeks()) {
				DatePeriod weekPeriod = attendanceTimeWeek.getPeriod();

				// 週の縦計
				val verticalTotalWeek = attendanceTimeWeek.getVerticalTotal();
				verticalTotalWeek.verticalTotal(require, this.companyId, this.employeeId, weekPeriod,
						workingSystem, this.companySets, this.employeeSets, this.monthlyCalculatingDailys);
			}

			// 月の縦計
			val verticalTotal = attendanceTime.getVerticalTotal();
			verticalTotal.verticalTotal(require, this.companyId, this.employeeId, datePeriod, workingSystem,
					this.companySets, this.employeeSets, this.monthlyCalculatingDailys);
		}

		ConcurrentStopwatches.stop("12230:縦計：");
		ConcurrentStopwatches.start("12240:時間外超過：");

		// 時間外超過
		ExcessOutsideWorkMng excessOutsideWorkMng = new ExcessOutsideWorkMng(monthlyCalculation);
		excessOutsideWorkMng.aggregate(require, cacheCarrier);
		if (excessOutsideWorkMng.getErrorInfos().size() > 0) {
			for (val errorInfo : excessOutsideWorkMng.getErrorInfos()) {
				this.errorInfos.putIfAbsent(errorInfo.getResourceId(), errorInfo);
			}
		}
		attendanceTime.setExcessOutsideWork(excessOutsideWorkMng.getExcessOutsideWork());

		ConcurrentStopwatches.stop("12240:時間外超過：");
		ConcurrentStopwatches.start("12250:回数集計：");

		// 回数集計
		{
			// 週単位の期間を取得
			for (val attendanceTimeWeek : attendanceTime.getMonthlyCalculation().getAttendanceTimeWeeks()) {
				DatePeriod weekPeriod = attendanceTimeWeek.getPeriod();

				// 週の回数集計
				val totalCountWeek = attendanceTimeWeek.getTotalCount();
				totalCountWeek.totalize(require, this.companyId, this.employeeId, weekPeriod, this.companySets,
						this.monthlyCalculatingDailys);
				if (totalCountWeek.getErrorInfos().size() > 0) {
					for (val errorInfo : totalCountWeek.getErrorInfos()) {
						this.errorInfos.putIfAbsent(errorInfo.getResourceId(), errorInfo);
					}
				}
			}

			// 月の回数集計
			val totalCount = attendanceTime.getTotalCount();
			totalCount.totalize(require, this.companyId, this.employeeId, datePeriod, this.companySets,
					this.monthlyCalculatingDailys);
			if (totalCount.getErrorInfos().size() > 0) {
				for (val errorInfo : totalCount.getErrorInfos()) {
					this.errorInfos.putIfAbsent(errorInfo.getResourceId(), errorInfo);
				}
			}
		}

		ConcurrentStopwatches.stop("12250:回数集計：");

		// 集計結果を返す
		result.setAttendanceTime(attendanceTime);
		for (val attendanceTimeWeek : attendanceTime.getMonthlyCalculation().getAttendanceTimeWeeks()) {
			val nowWeekNo = this.weekNoMap.get(this.yearMonth);
			if (nowWeekNo < attendanceTimeWeek.getWeekNo()) {
				this.weekNoMap.put(this.yearMonth, attendanceTimeWeek.getWeekNo());
			}
			result.getAttendanceTimeWeeks().add(attendanceTimeWeek);
		}
		return result;
	}

	/**
	 * 月別実績の任意項目を集計
	 * 
	 * @param monthPeriod
	 *            月の期間
	 * @param anyItemCustomizeValue
	 *            任意項目カスタマイズ値
	 */
	private void aggregateAnyItem(RequireM12 require, DatePeriod monthPeriod,
			Map<Integer, Map<Integer, AnyItemAggrResult>> anyItemCustomizeValue) {

		// 週単位の期間を取得
		ListIterator<AttendanceTimeOfWeekly> itrWeeks = this.aggregateResult.getAttendanceTimeWeeks().listIterator();
		while (itrWeeks.hasNext()) {
			AttendanceTimeOfWeekly attendanceTimeWeek = itrWeeks.next();

			// 週ごとの集計
			val weekResults = this.aggregateAnyItemPeriod(require, attendanceTimeWeek.getPeriod(), true,
					anyItemCustomizeValue.get(attendanceTimeWeek.getWeekNo()));
			for (val weekResult : weekResults.values()) {
				attendanceTimeWeek.getAnyItem().getAnyItemValues().put(weekResult.getOptionalItemNo(),
						AggregateAnyItem.of(weekResult.getOptionalItemNo(), weekResult.getAnyTime(),
								weekResult.getAnyTimes(), weekResult.getAnyAmount()));
			}
			itrWeeks.set(attendanceTimeWeek);
		}

		// 月ごとの集計
		val monthResults = this.aggregateAnyItemPeriod(require, monthPeriod, false, anyItemCustomizeValue.get(0));
		for (val monthResult : monthResults.values()) {
			this.aggregateResult.putAnyItemOrUpdate(AnyItemOfMonthly.of(this.employeeId, this.yearMonth, this.closureId,
					this.closureDate, monthResult));
		}
	}

	/**
	 * 大塚カスタマイズ （任意項目集計）
	 * 
	 * @param yearMonth
	 *            年月
	 * @param closureId
	 *            締めID
	 * @param companySets
	 *            月別集計で必要な会社別設定
	 * @return 任意項目カスタマイズ値
	 */
	private Map<Integer, Map<Integer, AnyItemAggrResult>> aggregateCustomizeForOtsuka(RequireM11 require, CacheCarrier cacheCarrier,
			YearMonth yearMonth, ClosureId closureId, MonAggrCompanySettings companySets) {

		// 任意項目カスタマイズ値 ※ 最初のInteger=0（月結果）、1～（各週結果（週No））
		Map<Integer, Map<Integer, AnyItemAggrResult>> results = new HashMap<>();
		results.put(0, new HashMap<>()); // 月結果

		// 月ごとの集計
		AnyItemAggrResult monthResult = this.getPredWorkingDays(require, cacheCarrier, yearMonth, closureId, companySets);
		results.get(0).putIfAbsent(monthResult.getOptionalItemNo(), monthResult);

		// 任意項目カスタマイズ値を返す
		return results;
	}

	/**
	 * 任意項目期間集計
	 * 
	 * @param period
	 *            期間
	 * @param isWeek
	 *            週間集計
	 * @param anyItemCustomizeValue
	 *            任意項目カスタマイズ値
	 * @return 任意項目集計結果
	 */
	private Map<Integer, AnyItemAggrResult> aggregateAnyItemPeriod(RequireM12 require, DatePeriod period, boolean isWeek,
			Map<Integer, AnyItemAggrResult> anyItemCustomizeValue) {

		Map<Integer, AnyItemAggrResult> results = new HashMap<>();
		List<AnyItemOfMonthly> anyItems = new ArrayList<>();

		// 任意項目ごとに集計する
		Map<Integer, AggregateAnyItem> anyItemTotals = new HashMap<>();
		for (val anyItemValueOfDaily : this.monthlyCalculatingDailys.getAnyItemValueOfDailyList().entrySet()){
			if (!period.contains(anyItemValueOfDaily.getKey())) continue;
			if (anyItemValueOfDaily.getValue().getItems() == null) continue;
			val ymd = anyItemValueOfDaily.getKey();
			for (val item : anyItemValueOfDaily.getValue().getItems()){
				if (item.getItemNo() == null) continue;
				Integer itemNo = item.getItemNo().v();

				if (period.contains(ymd)) {
					anyItemTotals.putIfAbsent(itemNo, new AggregateAnyItem(itemNo));
					anyItemTotals.get(itemNo).addFromDaily(item);
				}
			}
		}

		// 任意項目を取得
		for (val optionalItem : this.companySets.getOptionalItemMap().values()) {
			Integer optionalItemNo = optionalItem.getOptionalItemNo().v();

			// 大塚カスタマイズ （月別実績の任意項目←任意項目カスタマイズ値）
			if (anyItemCustomizeValue != null) {
				if (anyItemCustomizeValue.containsKey(optionalItemNo)) {
					results.put(optionalItemNo, anyItemCustomizeValue.get(optionalItemNo));
					anyItems.add(AnyItemOfMonthly.of(this.employeeId, this.yearMonth, this.closureId, this.closureDate,
							anyItemCustomizeValue.get(optionalItemNo)));
					continue;
				}
			}

			// 利用条件の判定
			Optional<EmpCondition> empCondition = Optional.empty();
			if (this.companySets.getEmpConditionMap().containsKey(optionalItemNo)) {
				empCondition = Optional.of(this.companySets.getEmpConditionMap().get(optionalItemNo));
			}
			val bsEmploymentHistOpt = this.employeeSets.getEmployment(period.end());
			if (optionalItem.checkTermsOfUse(empCondition, bsEmploymentHistOpt)) {
				// 利用する

				// 初期化
				AnyItemAggrResult result = AnyItemAggrResult.of(optionalItemNo, optionalItem);

				// 「実績区分」を判断
				if (optionalItem.getPerformanceAtr() == PerformanceAtr.DAILY_PERFORMANCE || isWeek) {

					// 日別実績 縦計処理
					result = AnyItemAggrResult.calcFromDailys(optionalItemNo, optionalItem, anyItemTotals);
				} else if (this.aggregateResult.getAttendanceTime().isPresent()) {
					val attendanceTime = this.aggregateResult.getAttendanceTime().get();

					// 月別実績 計算処理
					result = AnyItemAggrResult.calcFromMonthly(require, optionalItemNo, optionalItem, attendanceTime, anyItems,
							this.companySets);
				}
				results.put(optionalItemNo, result);
				anyItems.add(
						AnyItemOfMonthly.of(this.employeeId, this.yearMonth, this.closureId, this.closureDate, result));
			} else {
				// 利用しない

				// 日別実績 縦計処理
				AnyItemAggrResult result = AnyItemAggrResult.calcFromDailys(optionalItemNo, optionalItem,
						anyItemTotals);
				results.put(optionalItemNo, result);
				anyItems.add(
						AnyItemOfMonthly.of(this.employeeId, this.yearMonth, this.closureId, this.closureDate, result));
			}
		}

		return results;
	}

	/**
	 * 計画所定労働日数
	 * 
	 * @param yearMonth
	 *            年月
	 * @param closureId
	 *            締めID
	 * @param companySets
	 *            月別集計で必要な会社別設定
	 * @return 任意項目集計結果
	 */
	private AnyItemAggrResult getPredWorkingDays(RequireM11 require, CacheCarrier cacheCarrier,
			YearMonth yearMonth, ClosureId closureId, MonAggrCompanySettings companySets) {

		AnyItemAggrResult emptyResult = AnyItemAggrResult.of(69, null, new AnyTimesMonth(0.0), null);

		// 指定した年月の締め期間を取得する
		DatePeriod period = null;
		{
			// 対象の締めを取得する
			if (!companySets.getClosureMap().containsKey(closureId.value))
				return emptyResult;
			Closure closure = companySets.getClosureMap().get(closureId.value);

			// 指定した年月の期間をすべて取得する
			List<DatePeriod> periods = closure.getPeriodByYearMonth(yearMonth);
			if (periods.size() == 0)
				return emptyResult;

			// 期間を合算する
			GeneralDate startDate = periods.get(0).start();
			GeneralDate endDate = periods.get(0).end();
			if (periods.size() == 2) {
				if (startDate.after(periods.get(1).start()))
					startDate = periods.get(1).start();
				if (endDate.before(periods.get(1).end()))
					endDate = periods.get(1).end();
			}
			period = new DatePeriod(startDate, endDate);
		}

		// RQ608：指定期間の所定労働日数を取得する(大塚用)
		double predWorkingDays = require.monthAttendanceDays(cacheCarrier, period, this.companySets.getAllWorkTypeMap()).v();

		// 任意項目69へ格納
		return AnyItemAggrResult.of(69, null, new AnyTimesMonth(predWorkingDays), null);
	}

	/**
	 * 合算後のチェック処理
	 * 
	 * @param period
	 *            期間
	 */
	private void checkAfterSum(DatePeriod period) {

		// 「日別実績の勤怠時間」を取得する
		if (!this.monthlyCalculatingDailys.isExistDailyRecord(period)) {
			val errorInfo = new MonthlyAggregationErrorInfo("018",
					new ErrMessageContent(TextResource.localize("Msg_1376")));
			this.errorInfos.putIfAbsent(errorInfo.getResourceId(), errorInfo);
			return;
		}

		if (this.aggregateResult.getAttendanceTime().isPresent()) {
			val attendanceTime = this.aggregateResult.getAttendanceTime().get();

			// フレックス時間を確認する
			val flexTime = attendanceTime.getMonthlyCalculation().getFlexTime();
			int flexMinutes = flexTime.getFlexTime().getFlexTime().getTime().v();
			if (flexMinutes < 0) {
				this.aggregateResult.getPerErrors().add(new EmployeeMonthlyPerError(ErrorType.FLEX_SUPP, this.yearMonth,
						this.employeeId, this.closureId, this.closureDate, null, null, null));
			}
		}

		// ※ 乖離フラグは、縦計の合算と同時に再チェックされるため、ここでは不要。
		// ※ 平均時間関連は、縦計の合算と同時に再計算されるため、ここでは不要。
	}

	/**
	 * 手修正された項目を元に戻す （勤怠時間用）
	 * 
	 * @param attendanceTime
	 *            月別実績の勤怠時間
	 * @param monthlyOldDatas
	 *            集計前の月別実績データ
	 * @return 月別実績の勤怠時間
	 */
	private AttendanceTimeOfMonthly undoRetouchValuesForAttendanceTime(RequireM10 require, 
			AttendanceTimeOfMonthly attendanceTime, MonthlyOldDatas monthlyOldDatas) {

		this.isRetouch = false;

		// 既存データを確認する
		val oldDataOpt = monthlyOldDatas.getAttendanceTime();
		if (!oldDataOpt.isPresent())
			return attendanceTime;
		val oldConverter = require.createMonthlyConverter();
		MonthlyRecordToAttendanceItemConverter oldItemConvert = oldConverter.withAttendanceTime(oldDataOpt.get());
		if (monthlyOldDatas.getAnnualLeaveRemain().isPresent()) {
			oldItemConvert = oldItemConvert.withAnnLeave(monthlyOldDatas.getAnnualLeaveRemain().get());
		}
		if (monthlyOldDatas.getReserveLeaveRemain().isPresent()) {
			oldItemConvert = oldItemConvert.withRsvLeave(monthlyOldDatas.getReserveLeaveRemain().get());
		}
		if (monthlyOldDatas.getAbsenceLeaveRemain().isPresent()) {
			oldItemConvert = oldItemConvert.withAbsenceLeave(monthlyOldDatas.getAbsenceLeaveRemain().get());
		}
		if (monthlyOldDatas.getMonthlyDayoffRemain().isPresent()) {
			oldItemConvert = oldItemConvert.withDayOff(monthlyOldDatas.getMonthlyDayoffRemain().get());
		}
		oldItemConvert = oldItemConvert.withSpecialLeave(monthlyOldDatas.getSpecialLeaveRemainList());

		// 計算後データを確認
		val monthlyConverter = require.createMonthlyConverter();
		MonthlyRecordToAttendanceItemConverter convert = monthlyConverter.withAttendanceTime(attendanceTime);
		if (this.aggregateResult.getAnnLeaRemNumEachMonthList().size() > 0) {
			convert = convert.withAnnLeave(this.aggregateResult.getAnnLeaRemNumEachMonthList().get(0));
		}
		if (this.aggregateResult.getRsvLeaRemNumEachMonthList().size() > 0) {
			convert = convert.withRsvLeave(this.aggregateResult.getRsvLeaRemNumEachMonthList().get(0));
		}
		if (this.aggregateResult.getAbsenceLeaveRemainList().size() > 0) {
			convert = convert.withAbsenceLeave(this.aggregateResult.getAbsenceLeaveRemainList().get(0));
		}
		if (this.aggregateResult.getMonthlyDayoffRemainList().size() > 0) {
			convert = convert.withDayOff(this.aggregateResult.getMonthlyDayoffRemainList().get(0));
		}
		if (this.aggregateResult.getSpecialLeaveRemainList().size() > 0) {
			convert = convert.withSpecialLeave(this.aggregateResult.getSpecialLeaveRemainList());
		}

		// 月別実績の編集状態を取得
		for (val editState : this.editStates) {

			// 勤怠項目IDから項目を判断
			val itemValueOpt = oldItemConvert.convert(editState.getAttendanceItemId());
			if (!itemValueOpt.isPresent())
				continue;
			val itemValue = itemValueOpt.get();
			if (itemValue.value() == null)
				continue;

			// 該当する勤怠項目IDの値を計算前に戻す
			convert.merge(itemValue);
			this.isRetouch = true;
		}

		// いずれかの手修正値を戻した時、戻した後の勤怠時間を返す
		if (this.isRetouch) {
			val convertedOpt = convert.toAttendanceTime();
			if (convertedOpt.isPresent()) {
				val retouchedTime = convertedOpt.get();
				retouchedTime.getMonthlyCalculation().copySettings(attendanceTime.getMonthlyCalculation());

				val convertedAnnLeaveOpt = convert.toAnnLeave();
				if (convertedAnnLeaveOpt.isPresent()) {
					this.aggregateResult.getAnnLeaRemNumEachMonthList().clear();
					this.aggregateResult.getAnnLeaRemNumEachMonthList().add(convertedAnnLeaveOpt.get());
				}
				val convertedRsvLeaveOpt = convert.toRsvLeave();
				if (convertedRsvLeaveOpt.isPresent()) {
					this.aggregateResult.getRsvLeaRemNumEachMonthList().clear();
					this.aggregateResult.getRsvLeaRemNumEachMonthList().add(convertedRsvLeaveOpt.get());
				}
				val convertedAbsLeaveOpt = convert.toAbsenceLeave();
				if (convertedAbsLeaveOpt.isPresent()) {
					this.aggregateResult.getAbsenceLeaveRemainList().clear();
					this.aggregateResult.getAbsenceLeaveRemainList().add(convertedAbsLeaveOpt.get());
				}
				val convertedDayOffOpt = convert.toDayOff();
				if (convertedDayOffOpt.isPresent()) {
					this.aggregateResult.getMonthlyDayoffRemainList().clear();
					this.aggregateResult.getMonthlyDayoffRemainList().add(convertedDayOffOpt.get());
				}
				val convertedSpcLeaveList = convert.toSpecialHoliday();
				if (convertedSpcLeaveList.size() > 0) {
					this.aggregateResult.getSpecialLeaveRemainList().clear();
					this.aggregateResult.getSpecialLeaveRemainList().addAll(convertedSpcLeaveList);
				}
				return retouchedTime;
			}
		}
		return attendanceTime;
	}

	/**
	 * 手修正を戻してから計算必要な項目を再度計算
	 * 
	 * @param attendanceTime
	 *            月別実績の勤怠時間
	 * @return 月別実績の勤怠時間
	 */
	private AttendanceTimeOfMonthly recalcAttendanceTime(AttendanceTimeOfMonthly attendanceTime) {

		val monthlyCalculation = attendanceTime.getMonthlyCalculation();

		// 残業合計時間を集計する
		monthlyCalculation.getAggregateTime().getOverTime().recalcTotal();

		// 休出合計時間を集計する
		monthlyCalculation.getAggregateTime().getHolidayWorkTime().recalcTotal();

		// 総労働時間と36協定時間の再計算
		monthlyCalculation.recalcTotal();

		return attendanceTime;
	}

	/**
	 * 手修正された項目を元に戻す （任意項目用）
	 * 
	 * @param monthlyOldDatas
	 *            集計前の月別実績データ
	 */
	private void undoRetouchValuesForAnyItems(RequireM10 require, MonthlyOldDatas monthlyOldDatas) {

		this.isRetouch = false;

		// 既存データを確認する
		val oldDataList = monthlyOldDatas.getAnyItemList();
		if (oldDataList.size() == 0)
			return;
		val oldConverter = require.createMonthlyConverter();
		val oldItemConvert = oldConverter.withAnyItem(oldDataList);

		// 計算後データを確認
		val monthlyConverter = require.createMonthlyConverter();
		MonthlyRecordToAttendanceItemConverter convert = monthlyConverter
				.withAttendanceTime(this.aggregateResult.getAttendanceTime().get());
		convert = convert.withAnyItem(this.aggregateResult.getAnyItemList());

		// 月別実績の編集状態を取得
		for (val editState : this.editStates) {

			// 勤怠項目IDから項目を判断
			val itemValueOpt = oldItemConvert.convert(editState.getAttendanceItemId());
			if (!itemValueOpt.isPresent())
				continue;
			val itemValue = itemValueOpt.get();
			if (itemValue.value() == null)
				continue;

			// 該当する勤怠項目IDの値を計算前に戻す
			convert.merge(itemValue);
			this.isRetouch = true;
		}

		// いずれかの手修正値を戻した時、戻した後の任意項目を返す
		if (this.isRetouch) {
			val convertedList = convert.toAnyItems();
			this.aggregateResult.setAnyItemList(convertedList);
		}
	}

	/**
	 * 残数処理
	 * 
	 * @param period
	 *            期間
	 * @param interimRemainMngMode
	 *            暫定残数データ管理モード
	 * @param isCalcAttendanceRate
	 *            出勤率計算フラグ
	 */
	private void remainingProcess(RequireM8 require, CacheCarrier cacheCarrier, DatePeriod period,
			InterimRemainMngMode interimRemainMngMode, boolean isCalcAttendanceRate) {

		ConcurrentStopwatches.start("12405:暫定データ作成：");

		// Workを考慮した月次処理用の暫定残数管理データを作成する
		this.createDailyInterimRemainMngs(require, cacheCarrier, period);

		ConcurrentStopwatches.stop("12405:暫定データ作成：");
		ConcurrentStopwatches.start("12410:年休積休：");

		// 年休、積休
		this.annualAndReserveLeaveRemain(require, cacheCarrier, period, interimRemainMngMode, isCalcAttendanceRate);

		ConcurrentStopwatches.stop("12410:年休積休：");
		ConcurrentStopwatches.start("12420:振休：");

		// 振休
		this.absenceLeaveRemain(require, cacheCarrier, period, interimRemainMngMode);

		ConcurrentStopwatches.stop("12420:振休：");
		ConcurrentStopwatches.start("12430:代休：");

		// 代休
		this.dayoffRemain(require, cacheCarrier, period, interimRemainMngMode);

		ConcurrentStopwatches.stop("12430:代休：");
		ConcurrentStopwatches.start("12440:特別休暇：");

		// 特別休暇
		this.specialLeaveRemain(require, cacheCarrier, period, interimRemainMngMode);

		ConcurrentStopwatches.stop("12440:特別休暇：");
	}

	/**
	 * Workを考慮した月次処理用の暫定残数管理データを作成する
	 * 
	 * @param period
	 *            期間
	 */
	public void createDailyInterimRemainMngs(RequireM7 require, CacheCarrier cacheCarrier, DatePeriod period) {

		// 【参考：旧処理】 月次処理用の暫定残数管理データを作成する
		// this.dailyInterimRemainMngs =
		// this.interimRemOffMonth.monthInterimRemainData(
		// this.companyId, this.employeeId, period);

		// 残数作成元情報(実績)を作成する
		List<RecordRemainCreateInfor> recordRemains = RemainNumberCreateInformation.createRemainInfor(
				this.employeeId,
				this.monthlyCalculatingDailys.getAttendanceTimeOfDailyMap(),
				this.monthlyCalculatingDailys.getWorkInfoOfDailyMap());

		// 指定期間の暫定残数管理データを作成する
		InterimRemainCreateDataInputPara inputPara = new InterimRemainCreateDataInputPara(this.companyId,
				this.employeeId, period, recordRemains, Collections.emptyList(), Collections.emptyList(), false);
		CompanyHolidayMngSetting comHolidaySetting = new CompanyHolidayMngSetting(this.companyId,
				this.companySets.getAbsSettingOpt(), this.companySets.getDayOffSetting());
		this.dailyInterimRemainMngs = InterimRemainOffPeriodCreateData.createInterimRemainDataMng(require, cacheCarrier,
				inputPara, comHolidaySetting);

		this.isOverWriteRemain = (this.dailyInterimRemainMngs.size() > 0);
	}

	/**
	 * 年休、積休
	 * 
	 * @param period 期間
	 * @param interimRemainMngMode 暫定残数データ管理モード
	 * @param isCalcAttendanceRate 出勤率計算フラグ
	 */
	private void annualAndReserveLeaveRemain(RequireM6 require, CacheCarrier cacheCarrier, DatePeriod period,
			InterimRemainMngMode interimRemainMngMode, boolean isCalcAttendanceRate) {

		// 暫定残数データを年休・積立年休に絞り込む
		List<TmpAnnualLeaveMngWork> tmpAnnualLeaveMngs = new ArrayList<>();
		List<TmpReserveLeaveMngWork> tmpReserveLeaveMngs = new ArrayList<>();
		for (val dailyInterimRemainMng : this.dailyInterimRemainMngs.values()) {
			if (dailyInterimRemainMng.getRecAbsData().size() <= 0)
				continue;
			val master = dailyInterimRemainMng.getRecAbsData().get(0);

			// 年休
			if (dailyInterimRemainMng.getAnnualHolidayData().isPresent()) {
				val data = dailyInterimRemainMng.getAnnualHolidayData().get();
				tmpAnnualLeaveMngs.add(TmpAnnualLeaveMngWork.of(master, data));
			}

			// 積立年休
			if (dailyInterimRemainMng.getResereData().isPresent()) {
				val data = dailyInterimRemainMng.getResereData().get();
				tmpReserveLeaveMngs.add(TmpReserveLeaveMngWork.of(master, data));
			}
		}

		// 月別実績の計算結果が存在するかチェック
		boolean isOverWriteAnnual = this.isOverWriteRemain;
		if (this.aggregateResult.getAttendanceTime().isPresent()) {

			// 年休控除日数分の年休暫定残数データを作成する
			val compensFlexWorkOpt = CreateInterimAnnualMngData.ofCompensFlexToWork(
					this.aggregateResult.getAttendanceTime().get(), period.end());
			if (compensFlexWorkOpt.isPresent()) {
				tmpAnnualLeaveMngs.add(compensFlexWorkOpt.get());
				isOverWriteAnnual = true;
			}
		}

		// 「モード」をチェック
		CalYearOffWorkAttendRate daysForCalcAttdRate = new CalYearOffWorkAttendRate();
		if (interimRemainMngMode == InterimRemainMngMode.MONTHLY) {

			// 日別実績から出勤率計算用日数を取得 （月別集計用）
			daysForCalcAttdRate = GetDaysForCalcAttdRate.algorithm(require, this.companyId,
					this.employeeId, period, this.companySets, this.monthlyCalculatingDailys);
		}

		// 期間中の年休積休残数を取得
		val aggrResult = GetAnnAndRsvRemNumWithinPeriod.algorithm(require, cacheCarrier, 
				this.companyId, this.employeeId, period, interimRemainMngMode,
				// period.end(), true, isCalcAttendanceRate,
				period.end(), false, isCalcAttendanceRate, Optional.of(isOverWriteAnnual),
				Optional.of(tmpAnnualLeaveMngs), Optional.of(tmpReserveLeaveMngs), Optional.of(false),
				Optional.of(this.employeeSets.isNoCheckStartDate()), this.prevAggrResult.getAnnualLeave(),
				this.prevAggrResult.getReserveLeave(), Optional.of(this.companySets), Optional.of(this.employeeSets),
				Optional.of(this.monthlyCalculatingDailys));

		// 2回目の取得以降は、締め開始日を確認させる
		this.employeeSets.setNoCheckStartDate(false);

		if (aggrResult.getAnnualLeave().isPresent()) {
			val asOfPeriodEnd = aggrResult.getAnnualLeave().get().getAsOfPeriodEnd();
			val asOfStartNextDayOfPeriodEnd = aggrResult.getAnnualLeave().get().getAsOfStartNextDayOfPeriodEnd();
			val remainingNumber = asOfPeriodEnd.getRemainingNumber();

			// 年休月別残数データを更新
			AnnLeaRemNumEachMonth annLeaRemNum = AnnLeaRemNumEachMonth.of(this.employeeId, this.yearMonth,
					this.closureId, this.closureDate, period, ClosureStatus.UNTREATED,
					remainingNumber.getAnnualLeaveNoMinus(), remainingNumber.getAnnualLeaveWithMinus(),
					remainingNumber.getHalfDayAnnualLeaveNoMinus(), remainingNumber.getHalfDayAnnualLeaveWithMinus(),
					asOfStartNextDayOfPeriodEnd.getGrantInfo(), remainingNumber.getTimeAnnualLeaveNoMinus(),
					remainingNumber.getTimeAnnualLeaveWithMinus(),
					AnnualLeaveAttdRateDays.of(new MonthlyDays(daysForCalcAttdRate.getWorkingDays()),
							new MonthlyDays(daysForCalcAttdRate.getPrescribedDays()),
							new MonthlyDays(daysForCalcAttdRate.getDeductedDays())),
					asOfStartNextDayOfPeriodEnd.isAfterGrantAtr());
			this.aggregateResult.getAnnLeaRemNumEachMonthList().add(annLeaRemNum);

			// 年休エラーから月別残数エラー一覧を作成する
			this.aggregateResult.getPerErrors()
					.addAll(CreatePerErrorsFromLeaveErrors.fromAnnualLeave(this.employeeId, this.yearMonth,
							this.closureId, this.closureDate,
							aggrResult.getAnnualLeave().get().getAnnualLeaveErrors()));
		}

		if (aggrResult.getReserveLeave().isPresent()) {
			val asOfPeriodEnd = aggrResult.getReserveLeave().get().getAsOfPeriodEnd();
			val asOfStartNextDayOfPeriodEnd = aggrResult.getReserveLeave().get().getAsOfStartNextDayOfPeriodEnd();
			val remainingNumber = asOfPeriodEnd.getRemainingNumber();

			// 積立年休月別残数データを更新
			ReserveLeaveGrant reserveLeaveGrant = null;
			if (asOfStartNextDayOfPeriodEnd.getGrantInfo().isPresent()) {
				reserveLeaveGrant = ReserveLeaveGrant
						.of(asOfStartNextDayOfPeriodEnd.getGrantInfo().get().getGrantDays());
			}
			RsvLeaRemNumEachMonth rsvLeaRemNum = RsvLeaRemNumEachMonth.of(this.employeeId, this.yearMonth,
					this.closureId, this.closureDate, period, ClosureStatus.UNTREATED,
					remainingNumber.getReserveLeaveNoMinus(), remainingNumber.getReserveLeaveWithMinus(),
					Optional.ofNullable(reserveLeaveGrant), asOfStartNextDayOfPeriodEnd.isAfterGrantAtr());
			this.aggregateResult.getRsvLeaRemNumEachMonthList().add(rsvLeaRemNum);

			// 積立年休エラーから月別残数エラー一覧を作成する
			this.aggregateResult.getPerErrors()
					.addAll(CreatePerErrorsFromLeaveErrors.fromReserveLeave(this.employeeId, this.yearMonth,
							this.closureId, this.closureDate,
							aggrResult.getReserveLeave().get().getReserveLeaveErrors()));
		}

		// 集計結果を前回集計結果に引き継ぐ
		this.aggregateResult.setAggrResultOfAnnAndRsvLeave(aggrResult);
	}

	/**
	 * 振休
	 * 
	 * @param period 期間
	 * @param interimRemainMngMode 暫定残数データ管理モード
	 */
	private void absenceLeaveRemain(RequireM5 require, CacheCarrier cacheCarrier, DatePeriod period,
			InterimRemainMngMode interimRemainMngMode) {

		// 暫定残数データを振休・振出に絞り込む
		List<InterimRemain> interimMng = new ArrayList<>();
		List<InterimAbsMng> useAbsMng = new ArrayList<>();
		List<InterimRecMng> useRecMng = new ArrayList<>();
		for (val dailyInterimRemainMng : this.dailyInterimRemainMngs.values()) {
			if (dailyInterimRemainMng.getRecAbsData().size() <= 0)
				continue;
			interimMng.addAll(dailyInterimRemainMng.getRecAbsData());

			// 振休
			if (dailyInterimRemainMng.getInterimAbsData().isPresent()) {
				useAbsMng.add(dailyInterimRemainMng.getInterimAbsData().get());
			}

			// 振出
			if (dailyInterimRemainMng.getRecData().isPresent()) {
				useRecMng.add(dailyInterimRemainMng.getRecData().get());
			}
		}

		// 期間内の振休振出残数を取得する
		AbsRecMngInPeriodParamInput paramInput = new AbsRecMngInPeriodParamInput(this.companyId, this.employeeId,
				period, period.end(), (interimRemainMngMode == InterimRemainMngMode.MONTHLY), this.isOverWriteRemain,
				useAbsMng, interimMng, useRecMng, this.prevAbsRecResultOpt, Optional.empty(), Optional.empty());
		val aggrResult = AbsenceReruitmentMngInPeriodQuery.getAbsRecMngInPeriod(require, cacheCarrier, paramInput);
		if (aggrResult != null) {

			// 振休月別残数データを更新
			AbsenceLeaveRemainData absLeaRemNum = new AbsenceLeaveRemainData(this.employeeId, this.yearMonth,
					this.closureId.value, this.closureDate.getClosureDay().v(), this.closureDate.getLastDayOfMonth(),
					ClosureStatus.UNTREATED, period.start(), period.end(),
					new RemainDataDaysMonth(aggrResult.getOccurrenceDays()),
					new RemainDataDaysMonth(aggrResult.getUseDays()),
					new AttendanceDaysMonthToTal(aggrResult.getRemainDays()),
					new AttendanceDaysMonthToTal(aggrResult.getCarryForwardDays()),
					new RemainDataDaysMonth(aggrResult.getUnDigestedDays()));
			this.aggregateResult.getAbsenceLeaveRemainList().add(absLeaRemNum);

			// 振休エラーから月別残数エラー一覧を作成する
			this.aggregateResult.getPerErrors().addAll(CreatePerErrorsFromLeaveErrors.fromPause(this.employeeId,
					this.yearMonth, this.closureId, this.closureDate, aggrResult.getPError()));

			// 集計結果を前回集計結果に引き継ぐ
			this.aggregateResult.setAbsRecRemainMngOfInPeriodOpt(Optional.of(aggrResult));
		}
	}

	/**
	 * 代休
	 * 
	 * @param period 期間
	 * @param interimRemainMngMode 暫定残数データ管理モード
	 */
	private void dayoffRemain(RequireM4 require, CacheCarrier cacheCarrier, DatePeriod period,
			InterimRemainMngMode interimRemainMngMode) {

		// 暫定残数データを休出・代休に絞り込む
		List<InterimRemain> interimMng = new ArrayList<>();
		List<InterimBreakMng> breakMng = new ArrayList<>();
		List<InterimDayOffMng> dayOffMng = new ArrayList<>();
		for (val dailyInterimRemainMng : this.dailyInterimRemainMngs.values()) {
			if (dailyInterimRemainMng.getRecAbsData().size() <= 0)
				continue;
			interimMng.addAll(dailyInterimRemainMng.getRecAbsData());

			// 休出
			if (dailyInterimRemainMng.getBreakData().isPresent()) {
				breakMng.add(dailyInterimRemainMng.getBreakData().get());
			}

			// 代休
			if (dailyInterimRemainMng.getDayOffData().isPresent()) {
				dayOffMng.add(dailyInterimRemainMng.getDayOffData().get());
			}
		}

		// 期間内の休出代休残数を取得する
		BreakDayOffRemainMngParam inputParam = new BreakDayOffRemainMngParam(this.companyId, this.employeeId, period,
				(interimRemainMngMode == InterimRemainMngMode.MONTHLY), period.end(), this.isOverWriteRemain,
				interimMng, breakMng, dayOffMng, this.prevBreakDayOffResultOpt, Optional.empty(), Optional.empty());
		val aggrResult = BreakDayOffMngInPeriodQuery.getBreakDayOffMngInPeriod(require, cacheCarrier, inputParam);
		if (aggrResult != null) {

			// 代休月別残数データを更新
			MonthlyDayoffRemainData monDayRemNum = new MonthlyDayoffRemainData(this.employeeId, this.yearMonth,
					this.closureId.value, this.closureDate.getClosureDay().v(), this.closureDate.getLastDayOfMonth(),
					ClosureStatus.UNTREATED, period.start(), period.end(),
					new DayOffDayAndTimes(new RemainDataDaysMonth(aggrResult.getOccurrenceDays()),
							Optional.of(new RemainDataTimesMonth(aggrResult.getOccurrenceTimes()))),
					new DayOffDayAndTimes(new RemainDataDaysMonth(aggrResult.getUseDays()),
							Optional.of(new RemainDataTimesMonth(aggrResult.getUseTimes()))),
					new DayOffRemainDayAndTimes(new AttendanceDaysMonthToTal(aggrResult.getRemainDays()),
							Optional.of(new RemainingMinutes(aggrResult.getRemainTimes()))),
					new DayOffRemainDayAndTimes(new AttendanceDaysMonthToTal(aggrResult.getCarryForwardDays()),
							Optional.of(new RemainingMinutes(aggrResult.getRemainTimes()))),
					new DayOffDayAndTimes(new RemainDataDaysMonth(aggrResult.getUnDigestedDays()),
							Optional.of(new RemainDataTimesMonth(aggrResult.getUnDigestedTimes()))));
			this.aggregateResult.getMonthlyDayoffRemainList().add(monDayRemNum);

			// 代休エラーから月別残数エラー一覧を作成する
			this.aggregateResult.getPerErrors().addAll(CreatePerErrorsFromLeaveErrors.fromDayOff(this.employeeId,
					this.yearMonth, this.closureId, this.closureDate, aggrResult.getLstError()));

			// 集計結果を前回集計結果に引き継ぐ
			this.aggregateResult.setBreakDayOffRemainMngOfInPeriodOpt(Optional.of(aggrResult));
		}
	}

	/**
	 * 特別休暇
	 * 
	 * @param period 期間
	 * @param interimRemainMngMode 暫定残数データ管理モード
	 */
	private void specialLeaveRemain(RequireM3 require, CacheCarrier cacheCarrier, DatePeriod period,
			InterimRemainMngMode interimRemainMngMode) {

		// 暫定残数データを特別休暇に絞り込む
		List<InterimRemain> interimMng = new ArrayList<>();
		List<InterimSpecialHolidayMng> interimSpecialData = new ArrayList<>();
		for (val dailyInterimRemainMng : this.dailyInterimRemainMngs.values()) {
			if (dailyInterimRemainMng.getRecAbsData().size() <= 0)
				continue;
			if (dailyInterimRemainMng.getSpecialHolidayData().size() <= 0)
				continue;
			interimMng.addAll(dailyInterimRemainMng.getRecAbsData());
			interimSpecialData.addAll(dailyInterimRemainMng.getSpecialHolidayData());
		}

		// 「特別休暇」を取得する
		val specialHolidays = require.specialHoliday(this.companyId);
		for (val specialHoliday : specialHolidays) {
			Integer specialLeaveCode = specialHoliday.getSpecialHolidayCode().v();

			// 前回集計結果を確認する
			Optional<InPeriodOfSpecialLeaveResultInfor> prevSpecialLeaveResult = Optional.empty();
			if (this.prevSpecialLeaveResultMap.containsKey(specialLeaveCode)) {
				prevSpecialLeaveResult = Optional.of(this.prevSpecialLeaveResultMap.get(specialLeaveCode));
			}

			// マイナスなしを含めた期間内の特別休暇残を集計する
			// 期間内の特別休暇残を集計する
			ComplileInPeriodOfSpecialLeaveParam param = new ComplileInPeriodOfSpecialLeaveParam(this.companyId,
					this.employeeId, period,
					// (interimRemainMngMode == InterimRemainMngMode.MONTHLY),
					// period.end(), specialLeaveCode, true,
					(interimRemainMngMode == InterimRemainMngMode.MONTHLY), period.end(), specialLeaveCode, false,
					this.isOverWriteRemain, interimMng, interimSpecialData, prevSpecialLeaveResult);
			InPeriodOfSpecialLeaveResultInfor aggrResult = SpecialLeaveManagementService
					.complileInPeriodOfSpecialLeave(require, cacheCarrier, param);
			InPeriodOfSpecialLeave inPeriod = aggrResult.getAggSpecialLeaveResult();

			// マイナスなしの残数・使用数を計算
			RemainDaysOfSpecialHoliday remainDays = inPeriod.getRemainDays();
			SpecialLeaveRemainNoMinus remainNoMinus = new SpecialLeaveRemainNoMinus(remainDays);

			// 特別休暇月別残数データを更新
			SpecialHolidayRemainData speLeaRemNum = SpecialHolidayRemainData.of(this.employeeId, this.yearMonth,
					this.closureId, this.closureDate, period, specialLeaveCode, inPeriod, remainNoMinus);
			this.aggregateResult.getSpecialLeaveRemainList().add(speLeaRemNum);

			// 特別休暇エラーから月別残数エラー一覧を作成する
			this.aggregateResult.getPerErrors()
					.addAll(CreatePerErrorsFromLeaveErrors.fromSpecialLeave(this.employeeId, this.yearMonth,
							this.closureId, this.closureDate, specialLeaveCode, inPeriod.getLstError()));

			// 集計結果を前回集計結果に引き継ぐ
			this.aggregateResult.getInPeriodOfSpecialLeaveResultInforMap().put(specialLeaveCode, aggrResult);
		}
	}

	/**
	 * 大塚カスタマイズ
	 */
	private void customizeForOtsuka() {

		// 2018.01.21 DEL shuichi_ishida Redmine#105681
		// 時短日割適用日数
		// this.TimeSavingDailyRateApplyDays();
	}

	/**
	 * 処理期間との重複を確認する （重複期間を取り出す）
	 * 
	 * @param target 処理期間
	 * @param comparison 比較対象期間
	 * @return 重複期間 （null = 重複なし）
	 */
	private DatePeriod confirmProcPeriod(DatePeriod target, DatePeriod comparison) {

		DatePeriod overlap = null; // 重複期間

		// 開始前
		if (target.isBefore(comparison))
			return overlap;

		// 終了後
		if (target.isAfter(comparison))
			return overlap;

		// 重複あり
		overlap = target;

		// 開始日より前を除外
		if (overlap.contains(comparison.start())) {
			overlap = overlap.cutOffWithNewStart(comparison.start());
		}

		// 終了日より後を除外
		if (overlap.contains(comparison.end())) {
			overlap = overlap.cutOffWithNewEnd(comparison.end());
		}

		return overlap;
	}

	/**
	 * 所属情報の作成
	 * 
	 * @param datePeriod
	 *            期間
	 * @return 月別実績の所属情報
	 */
	private AffiliationInfoOfMonthly createAffiliationInfo(DatePeriod datePeriod) {

		List<String> employeeIds = new ArrayList<>();
		employeeIds.add(this.employeeId);

		// 月初の所属情報を取得
		boolean isExistStartWorkInfo = false;
		if (this.monthlyCalculatingDailys.getWorkInfoOfDailyMap().containsKey(datePeriod.start())) {
			isExistStartWorkInfo = true;
		}
		val workInfoOfDailyList = monthlyCalculatingDailys.getAffiInfoOfDailyMap();
		if (workInfoOfDailyList.isEmpty()) {
			if (isExistStartWorkInfo) {
				val errorInfo = new MonthlyAggregationErrorInfo("003",
						new ErrMessageContent(TextResource.localize("Msg_1157")));
				this.errorInfos.putIfAbsent(errorInfo.getResourceId(), errorInfo);
			}
			return null;
		}
		val firstInfoOfDaily = workInfoOfDailyList.get(datePeriod.start());
		if (firstInfoOfDaily == null) {
			if (isExistStartWorkInfo) {
				val errorInfo = new MonthlyAggregationErrorInfo("003",
						new ErrMessageContent(TextResource.localize("Msg_1157")));
				this.errorInfos.putIfAbsent(errorInfo.getResourceId(), errorInfo);
			}
			return null;
		}
		// 月初の情報を作成
		val firstInfo = AggregateAffiliationInfo.of(
				firstInfoOfDaily.getEmploymentCode(),
				new WorkplaceId(firstInfoOfDaily.getWplID()),
				new JobTitleId(firstInfoOfDaily.getJobTitleID()),
				firstInfoOfDaily.getClsCode(),
				firstInfoOfDaily.getBusinessTypeCode());

		// 月末がシステム日付以降の場合、月初の情報を月末の情報とする
		if (datePeriod.end().after(GeneralDate.today())) {

			// 月別実績の所属情報を返す
			return AffiliationInfoOfMonthly.of(this.employeeId, this.yearMonth, this.closureId, this.closureDate,
					firstInfo, firstInfo);
		}

		// 月末の所属情報を取得
		val lastInfoOfDaily = workInfoOfDailyList.get(datePeriod.end());
		if (lastInfoOfDaily == null) {

			// 月別実績の所属情報を返す （エラーにせず、月末に月初の情報を入れる）
			return AffiliationInfoOfMonthly.of(this.employeeId, this.yearMonth, this.closureId, this.closureDate,
					firstInfo, firstInfo);
		}

		// 月末の情報を作成
		val lastInfo = AggregateAffiliationInfo.of(
				lastInfoOfDaily.getEmploymentCode(),
				new WorkplaceId(lastInfoOfDaily.getWplID()),
				new JobTitleId(lastInfoOfDaily.getJobTitleID()),
				lastInfoOfDaily.getClsCode(),
				lastInfoOfDaily.getBusinessTypeCode());
		
		// 月別実績の所属情報を返す
		return AffiliationInfoOfMonthly.of(this.employeeId, this.yearMonth, this.closureId, this.closureDate, firstInfo,
				lastInfo);
	}

	/**
	 * 時短日割適用日数
	 */
	private void TimeSavingDailyRateApplyDays() {

		// 月別実績の所属情報を取得
		val affiliationInfoOpt = this.aggregateResult.getAffiliationInfo();
		if (!affiliationInfoOpt.isPresent())
			return;

		// 月末の勤務情報を判断
		val lastInfo = affiliationInfoOpt.get().getLastInfo();
		if (lastInfo.getBusinessTypeCd().map(c -> c.v()).orElse("").compareTo("0000002030") == 0) {

			// 任意項目50にセット
			this.aggregateResult.putAnyItemOrUpdate(AnyItemOfMonthly.of(this.employeeId, this.yearMonth, this.closureId,
					this.closureDate, 50, Optional.empty(), Optional.of(new AnyTimesMonth(20.67)), Optional.empty()));
		}
	}

	public static interface RequireM13 extends AttendanceTimeOfMonthly.RequireM1, TotalCountByPeriod.RequireM1,
		MonthlyCalculation.RequireM4, VerticalTotalOfMonthly.RequireM1, ExcessOutsideWorkMng.RequireM5 {
		
	}

	public static interface RequireM11 {
		
		AttendanceDaysMonth monthAttendanceDays(CacheCarrier cacheCarrier, DatePeriod period, Map<String, WorkType> workTypeMap);
	}
	
	public static interface RequireM12 extends AnyItemAggrResult.RequireM1 {
		
	}
	
	public static interface RequireM10 {
		
		MonthlyRecordToAttendanceItemConverter createMonthlyConverter();
	}
	
	public static interface RequireM8 extends RequireM7, RequireM6, RequireM5, RequireM4, RequireM3 {
	}
	
	public static interface RequireM7 extends InterimRemainOffPeriodCreateData.RequireM4 {
	}
	
	public static interface RequireM6 extends GetDaysForCalcAttdRate.RequireM2,
		GetAnnAndRsvRemNumWithinPeriod.RequireM2 {
	}
	
	public static interface RequireM5 extends AbsenceReruitmentMngInPeriodQuery.RequireM10 {
	}
	
	public static interface RequireM4 extends BreakDayOffMngInPeriodQuery.RequireM10 {
	}
	
	public static interface RequireM3 extends SpecialLeaveManagementService.RequireM5 {
		
		List<SpecialHoliday> specialHoliday(String companyId);
	}
	
	public static interface RequireM14 {
		
		Optional<WorkingCondition> workingCondition(String historyId);
	}
	
	public static interface RequireM15 extends MonthlyCalculatingDailys.RequireM4, RequireM13,
		MonthlyOldDatas.RequireM1, RequireM14, RequireM8, RequireM10,
		MonthlyCalculation.RequireM2, AttendanceTimeOfMonthly.RequireM2, RequireM11, RequireM12 {

		List<WorkingConditionItem> workingConditionItem(String employeeId, DatePeriod datePeriod);

		List<EditStateOfMonthlyPerformance> monthEditStates(String employeeId, YearMonth yearMonth, ClosureId closureId,
				ClosureDate closureDate);
		
		ManagedExecutorService getExecutorService();
	}
	
	private AttendanceTimeOfMonthly.RequireM2 createAttendanceTimeOfMonthlyRequire(
			RequireM15 require, Optional<List<IntegrationOfDaily>> dailyWorksOpt) {
		
		return new AttendanceTimeOfMonthly.RequireM2() {
			
			private Optional<IntegrationOfDaily> findDaily(String empId, GeneralDate ymd) {
				
				return dailyWorksOpt.flatMap(dws -> {
					return dws.stream().filter(dw -> empId.equals(dw.getEmployeeId())
							&& ymd.equals(dw.getYmd())).findFirst();
				});
			}
			
			@Override
			public boolean isUseWorkLayer(String companyId) {
				return require.isUseWorkLayer(companyId);
			}
			
			@Override
			public List<OuenWorkTimeSheetOfDailyAttendance> ouenWorkTimeSheetOfDailyAttendance(String empId, GeneralDate ymd) {
				val daily = findDaily(empId, ymd);
				
				return daily.map(dw -> dw.getOuenTimeSheet())
						.orElseGet(() -> require.ouenWorkTimeSheetOfDailyAttendance(empId, ymd));
			}
			
			@Override
			public List<OuenWorkTimeOfDailyAttendance> ouenWorkTimeOfDailyAttendance(String empId, GeneralDate ymd) {
				val daily = findDaily(empId, ymd);
				
				return daily.map(dw -> dw.getOuenTime())
						.orElseGet(() -> require.ouenWorkTimeOfDailyAttendance(empId, ymd));
			}
			
			@Override
			public Optional<OuenAggregateFrameSetOfMonthly> ouenAggregateFrameSetOfMonthly(String companyId) {
				return require.ouenAggregateFrameSetOfMonthly(companyId);
			}
		};
	}
}