package nts.uk.ctx.at.record.dom.monthlyprocess.aggr.work;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CountDownLatch;
import java.util.stream.Collectors;

import javax.enterprise.concurrent.ManagedExecutorService;

import lombok.Getter;
import lombok.val;
//import lombok.extern.slf4j.Slf4j;
import nts.arc.diagnose.stopwatch.concurrent.ConcurrentStopwatches;
import nts.arc.task.AsyncTask;
import nts.arc.time.GeneralDate;
import nts.arc.time.YearMonth;
import nts.gul.util.value.MutableValue;
import nts.uk.ctx.at.record.dom.dailyprocess.calc.IntegrationOfDaily;
import nts.uk.ctx.at.record.dom.monthly.AttendanceTimeOfMonthly;
import nts.uk.ctx.at.record.dom.monthly.affiliation.AffiliationInfoOfMonthly;
import nts.uk.ctx.at.record.dom.monthly.affiliation.AggregateAffiliationInfo;
import nts.uk.ctx.at.record.dom.monthly.anyitem.AggregateAnyItem;
import nts.uk.ctx.at.record.dom.monthly.anyitem.AnyItemOfMonthly;
import nts.uk.ctx.at.record.dom.monthly.calc.MonthlyAggregateAtr;
import nts.uk.ctx.at.record.dom.monthly.calc.MonthlyCalculation;
import nts.uk.ctx.at.record.dom.monthly.erroralarm.EmployeeMonthlyPerError;
import nts.uk.ctx.at.record.dom.monthly.erroralarm.ErrorType;
import nts.uk.ctx.at.record.dom.monthly.erroralarm.Flex;
import nts.uk.ctx.at.record.dom.monthly.performance.EditStateOfMonthlyPerRepository;
import nts.uk.ctx.at.record.dom.monthly.performance.EditStateOfMonthlyPerformance;
import nts.uk.ctx.at.record.dom.monthly.vacation.ClosureStatus;
import nts.uk.ctx.at.record.dom.monthly.vacation.absenceleave.monthremaindata.AbsenceLeaveRemainData;
import nts.uk.ctx.at.record.dom.monthly.vacation.absenceleave.monthremaindata.AttendanceDaysMonthToTal;
import nts.uk.ctx.at.record.dom.monthly.vacation.absenceleave.monthremaindata.RemainDataDaysMonth;
import nts.uk.ctx.at.record.dom.monthly.vacation.annualleave.AnnLeaRemNumEachMonth;
import nts.uk.ctx.at.record.dom.monthly.vacation.annualleave.AnnualLeaveAttdRateDays;
import nts.uk.ctx.at.record.dom.monthly.vacation.dayoff.monthremaindata.DayOffDayAndTimes;
import nts.uk.ctx.at.record.dom.monthly.vacation.dayoff.monthremaindata.DayOffRemainDayAndTimes;
import nts.uk.ctx.at.record.dom.monthly.vacation.dayoff.monthremaindata.MonthlyDayoffRemainData;
import nts.uk.ctx.at.record.dom.monthly.vacation.dayoff.monthremaindata.RemainDataTimesMonth;
import nts.uk.ctx.at.record.dom.monthly.vacation.reserveleave.ReserveLeaveGrant;
import nts.uk.ctx.at.record.dom.monthly.vacation.reserveleave.RsvLeaRemNumEachMonth;
import nts.uk.ctx.at.record.dom.monthly.vacation.specialholiday.monthremaindata.SpecialHolidayRemainData;
import nts.uk.ctx.at.record.dom.monthlyprocess.aggr.IntegrationOfMonthly;
import nts.uk.ctx.at.record.dom.monthlyprocess.aggr.MonthlyAggregationErrorInfo;
import nts.uk.ctx.at.record.dom.monthlyprocess.aggr.converter.MonthlyRecordToAttendanceItemConverter;
import nts.uk.ctx.at.record.dom.monthlyprocess.aggr.export.pererror.CreatePerErrorsFromLeaveErrors;
import nts.uk.ctx.at.record.dom.monthlyprocess.aggr.work.anyitem.AnyItemAggrResult;
import nts.uk.ctx.at.record.dom.monthlyprocess.aggr.work.excessoutside.ExcessOutsideWorkMng;
import nts.uk.ctx.at.record.dom.optitem.PerformanceAtr;
import nts.uk.ctx.at.record.dom.optitem.applicable.EmpCondition;
import nts.uk.ctx.at.record.dom.optitem.calculation.Formula;
import nts.uk.ctx.at.record.dom.remainingnumber.annualleave.GetDaysForCalcAttdRate;
import nts.uk.ctx.at.record.dom.remainingnumber.annualleave.export.CreateInterimAnnualMngData;
import nts.uk.ctx.at.record.dom.remainingnumber.annualleave.export.GetAnnAndRsvRemNumWithinPeriod;
import nts.uk.ctx.at.record.dom.remainingnumber.annualleave.export.InterimRemainMngMode;
import nts.uk.ctx.at.record.dom.remainingnumber.annualleave.export.param.AggrResultOfAnnAndRsvLeave;
import nts.uk.ctx.at.record.dom.remainingnumber.annualleave.export.param.CalYearOffWorkAttendRate;
import nts.uk.ctx.at.record.dom.service.RemainNumberCreateInformation;
import nts.uk.ctx.at.record.dom.weekly.AttendanceTimeOfWeekly;
import nts.uk.ctx.at.record.dom.workrecord.workperfor.dailymonthlyprocessing.ErrMessageContent;
import nts.uk.ctx.at.shared.dom.adapter.employee.EmployeeImport;
import nts.uk.ctx.at.shared.dom.common.WorkplaceId;
import nts.uk.ctx.at.shared.dom.common.anyitem.AnyAmountMonth;
import nts.uk.ctx.at.shared.dom.common.anyitem.AnyTimeMonth;
import nts.uk.ctx.at.shared.dom.common.anyitem.AnyTimesMonth;
import nts.uk.ctx.at.shared.dom.common.days.MonthlyDays;
import nts.uk.ctx.at.shared.dom.ot.autocalsetting.JobTitleId;
import nts.uk.ctx.at.shared.dom.remainingnumber.absencerecruitment.export.query.AbsRecMngInPeriodParamInput;
import nts.uk.ctx.at.shared.dom.remainingnumber.absencerecruitment.export.query.AbsenceReruitmentMngInPeriodQuery;
import nts.uk.ctx.at.shared.dom.remainingnumber.absencerecruitment.interim.InterimAbsMng;
import nts.uk.ctx.at.shared.dom.remainingnumber.absencerecruitment.interim.InterimRecMng;
import nts.uk.ctx.at.shared.dom.remainingnumber.algorithm.DailyInterimRemainMngData;
import nts.uk.ctx.at.shared.dom.remainingnumber.algorithm.InterimRemainCreateDataInputPara;
import nts.uk.ctx.at.shared.dom.remainingnumber.algorithm.InterimRemainOffMonthProcess;
import nts.uk.ctx.at.shared.dom.remainingnumber.algorithm.InterimRemainOffPeriodCreateData;
import nts.uk.ctx.at.shared.dom.remainingnumber.algorithm.RecordRemainCreateInfor;
import nts.uk.ctx.at.shared.dom.remainingnumber.annualleave.empinfo.maxdata.RemainingMinutes;
import nts.uk.ctx.at.shared.dom.remainingnumber.annualleave.interim.TmpAnnualLeaveMngWork;
import nts.uk.ctx.at.shared.dom.remainingnumber.breakdayoffmng.export.query.BreakDayOffMngInPeriodQuery;
import nts.uk.ctx.at.shared.dom.remainingnumber.breakdayoffmng.export.query.BreakDayOffRemainMngParam;
import nts.uk.ctx.at.shared.dom.remainingnumber.breakdayoffmng.interim.InterimBreakMng;
import nts.uk.ctx.at.shared.dom.remainingnumber.breakdayoffmng.interim.InterimDayOffMng;
import nts.uk.ctx.at.shared.dom.remainingnumber.interimremain.InterimRemain;
import nts.uk.ctx.at.shared.dom.remainingnumber.reserveleave.interim.TmpReserveLeaveMngWork;
import nts.uk.ctx.at.shared.dom.remainingnumber.specialholidaymng.interim.InterimSpecialHolidayMng;
import nts.uk.ctx.at.shared.dom.remainingnumber.specialleave.service.ComplileInPeriodOfSpecialLeaveParam;
import nts.uk.ctx.at.shared.dom.remainingnumber.specialleave.service.InPeriodOfSpecialLeave;
import nts.uk.ctx.at.shared.dom.remainingnumber.specialleave.service.RemainDaysOfSpecialHoliday;
import nts.uk.ctx.at.shared.dom.remainingnumber.specialleave.service.SpecialLeaveManagementService;
import nts.uk.ctx.at.shared.dom.remainingnumber.specialleave.service.SpecialLeaveRemainNoMinus;
import nts.uk.ctx.at.shared.dom.remainingnumber.work.CompanyHolidayMngSetting;
import nts.uk.ctx.at.shared.dom.specialholiday.SpecialHolidayRepository;
import nts.uk.ctx.at.shared.dom.workingcondition.WorkingConditionItem;
import nts.uk.ctx.at.shared.dom.workrule.closure.ClosureId;
import nts.uk.shr.com.i18n.TextResource;
import nts.uk.shr.com.time.calendar.date.ClosureDate;
import nts.uk.shr.com.time.calendar.period.DatePeriod;

/**
 * 処理：ドメインサービス：月別実績を集計する
 * @author shuichi_ishida
 */
@Getter
/*@Slf4j*/
public class AggregateMonthlyRecordServiceProc {
	
	/** 月別集計が必要とするリポジトリ */
	private RepositoriesRequiredByMonthlyAggr repositories;
	/** 月次処理用の暫定残数管理データを作成する */
	private InterimRemainOffMonthProcess interimRemOffMonth;
	/** 残数作成元情報を作成する */
	private RemainNumberCreateInformation remNumCreateInfo;
	/** 指定期間の暫定残数管理データを作成する */
	private InterimRemainOffPeriodCreateData periodCreateData;
	/** 暫定年休管理データを作成する */
	private CreateInterimAnnualMngData createInterimAnnual;
	/** 期間中の年休積休残数を取得 */
	private GetAnnAndRsvRemNumWithinPeriod getAnnAndRsvRemNumWithinPeriod;
	/** 出勤率計算用日数を取得する */
	private GetDaysForCalcAttdRate getDaysForCalcAttdRate;
	/** 期間内の振出振休残数を取得する */
	private AbsenceReruitmentMngInPeriodQuery absenceRecruitMng;
	/** 期間内の休出代休残数を取得する */
	private BreakDayOffMngInPeriodQuery breakDayoffMng;
	/** 特別休暇 */
	private SpecialHolidayRepository specialHolidayRepo;
	/** 期間内の特別休暇残を集計する */
	private SpecialLeaveManagementService specialLeaveMng;
	/** 休暇残数エラーから月別残数エラー一覧を作成する */
	private CreatePerErrorsFromLeaveErrors createPerErrorFromLeaveErrors;
	/** 月別実績の編集状態 */
	private EditStateOfMonthlyPerRepository editStateRepo;
	
	/** 集計結果 */
	private AggregateMonthlyRecordValue aggregateResult;
	/** エラー情報 */
	private Map<String, MonthlyAggregationErrorInfo> errorInfos;
	/** 編集状態リスト */
	private List<EditStateOfMonthlyPerformance> editStates;

	/** 会社ID */
	private String companyId;
	/** 社員ID */
	private String employeeId;
	/** 年月 */
	private YearMonth yearMonth;
	/** 締めID */
	private ClosureId closureId;
	/** 締め日 */
	private ClosureDate closureDate;
	
	/** 月別集計で必要な会社別設定 */
	private MonAggrCompanySettings companySets;
	/** 月別集計で必要な社員別設定 */
	private MonAggrEmployeeSettings employeeSets;
	/** 月の計算中の日別実績データ */
	private MonthlyCalculatingDailys monthlyCalculatingDailys;
	/** 集計前の月別実績データ */
	private MonthlyOldDatas monthlyOldDatas;
	/** 労働条件項目 */
	private List<WorkingConditionItem> workingConditionItems;
	/** 労働条件 */
	private Map<String, DatePeriod> workingConditions;
	/** 前回集計結果　（年休積立年休の集計結果） */
	private AggrResultOfAnnAndRsvLeave prevAggrResult;
	/** 週NO管理 */
	private Map<YearMonth, Integer> weekNoMap;
	/** 手修正あり */
	private boolean isRetouch;
	/** 暫定残数データ */
	private Map<GeneralDate, DailyInterimRemainMngData> dailyInterimRemainMngs;
	/** 暫定残数データ上書きフラグ */
	private boolean isOverWriteRemain;
	/** 並列処理用 */
	private ManagedExecutorService executerService;
	
	public AggregateMonthlyRecordServiceProc(
			RepositoriesRequiredByMonthlyAggr repositories,
			InterimRemainOffMonthProcess interimRemOffMonth,
			RemainNumberCreateInformation remNumCreateInfo,
			InterimRemainOffPeriodCreateData periodCreateData,
			CreateInterimAnnualMngData createInterimAnnual,
			GetAnnAndRsvRemNumWithinPeriod getAnnAndRsvRemNumWithinPeriod,
			AbsenceReruitmentMngInPeriodQuery absenceRecruitMng,
			BreakDayOffMngInPeriodQuery breakDayoffMng,
			GetDaysForCalcAttdRate getDaysForCalcAttdRate,
			SpecialHolidayRepository specialHolidayRepo,
			SpecialLeaveManagementService specialLeaveMng,
			CreatePerErrorsFromLeaveErrors createPerErrorFromLeaveErrors,
			EditStateOfMonthlyPerRepository editStateRepo,
			ManagedExecutorService executerService){

		this.repositories = repositories;
		this.interimRemOffMonth = interimRemOffMonth;
		this.remNumCreateInfo = remNumCreateInfo;
		this.periodCreateData = periodCreateData;
		this.createInterimAnnual = createInterimAnnual;
		this.getAnnAndRsvRemNumWithinPeriod = getAnnAndRsvRemNumWithinPeriod;
		this.absenceRecruitMng = absenceRecruitMng;
		this.breakDayoffMng = breakDayoffMng;
		this.getDaysForCalcAttdRate = getDaysForCalcAttdRate;
		this.specialHolidayRepo = specialHolidayRepo;
		this.specialLeaveMng = specialLeaveMng;
		this.createPerErrorFromLeaveErrors = createPerErrorFromLeaveErrors;
		this.editStateRepo = editStateRepo;
		this.executerService = executerService;
	}
	
	/**
	 * 集計処理
	 * @param companyId 会社ID
	 * @param employeeId 社員ID
	 * @param yearMonth 年月
	 * @param closureId 締めID
	 * @param closureDate 締め日付
	 * @param datePeriod 期間
	 * @param prevAggrResult 前回集計結果　（年休積立年休の集計結果）
	 * @param companySets 月別集計で必要な会社別設定
	 * @param employeeSets 月別集計で必要な社員別設定
	 * @param dailyWorksOpt 日別実績(WORK)List
	 * @param monthlyWorkOpt 月別実績(WORK)
	 * @return 集計結果
	 */
	public AggregateMonthlyRecordValue aggregate(
			String companyId, String employeeId, YearMonth yearMonth,
			ClosureId closureId, ClosureDate closureDate, DatePeriod datePeriod,
			AggrResultOfAnnAndRsvLeave prevAggrResult,
			MonAggrCompanySettings companySets,
			MonAggrEmployeeSettings employeeSets,
			Optional<List<IntegrationOfDaily>> dailyWorksOpt,
			Optional<IntegrationOfMonthly> monthlyWorkOpt) {
		
		this.aggregateResult = new AggregateMonthlyRecordValue();
		this.errorInfos = new HashMap<>();
		this.editStates = new ArrayList<>();

		this.companyId = companyId;
		this.employeeId = employeeId;
		this.yearMonth = yearMonth;
		this.closureId = closureId;
		this.closureDate = closureDate;
		this.prevAggrResult = prevAggrResult;
		this.weekNoMap = new HashMap<>();
		this.isRetouch = false;
		this.dailyInterimRemainMngs = new HashMap<>();
		this.isOverWriteRemain = false;
		
		ConcurrentStopwatches.start("12100:集計期間ごと準備：");

		this.companySets = companySets;
		this.employeeSets = employeeSets;
		
		// 社員を取得する
		EmployeeImport employee = this.employeeSets.getEmployee();
		
		// 入社前、退職後を期間から除く　→　一か月の集計期間
		val termInOffice = new DatePeriod(employee.getEntryDate(), employee.getRetiredDate());
		DatePeriod monthPeriod = this.confirmProcPeriod(datePeriod, termInOffice);
		if (monthPeriod == null) {
			// 処理期間全体が、入社前または退職後の時
			return this.aggregateResult;
		}
		
		// 計算に必要なデータを準備する
		this.monthlyCalculatingDailys = MonthlyCalculatingDailys.loadData(
				employeeId, monthPeriod, dailyWorksOpt, this.repositories);
		
		// 集計前の月別実績データを確認する
		this.monthlyOldDatas = MonthlyOldDatas.loadData(
				employeeId, yearMonth, closureId, closureDate, monthlyWorkOpt, this.repositories);
		
		// 「労働条件項目」を取得
		List<WorkingConditionItem> workingConditionItems = this.repositories.getWorkingConditionItem()
				.getBySidAndPeriodOrderByStrD(employeeId, monthPeriod);
		if (workingConditionItems.isEmpty()){
			this.aggregateResult.addErrorInfos("001", new ErrMessageContent(TextResource.localize("Msg_430")));
			return this.aggregateResult;
		}
		
		// 同じ労働制の履歴を統合
		this.IntegrateHistoryOfSameWorkSys(workingConditionItems);
		
		// 所属情報の作成
		val affiliationInfo = this.createAffiliationInfo(monthPeriod);
		if (affiliationInfo == null) {
			for (val errorInfo : this.errorInfos.values()){
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
					this.aggregationProcess(monthPeriod);
				} catch (RuntimeException ex) {
					excepAggregation.set(ex);
				} finally {
					cdlAggregation.countDown();
				}
			};
			
			if (Thread.currentThread().getName().indexOf("REQUEST:") == 0) {
				this.executerService.submit(AsyncTask.builder()
						.withContexts()
						.threadName("Aggregation")
						.build(asyncAggregation));
			} else {
				// バッチなどの場合は非同期にせずそのまま実行
				asyncAggregation.run();
			}
			
			// 残数処理
			// こちらはDB書き込みをしているので非同期化できない
			this.remainingProcess(monthPeriod);
	
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
		
		// 月別実績の編集状態　取得
		this.editStates = this.editStateRepo.findByClosure(
				this.employeeId, this.yearMonth, this.closureId, this.closureDate);
		
		if (this.aggregateResult.getAttendanceTime().isPresent()){
			AttendanceTimeOfMonthly attendanceTime = this.aggregateResult.getAttendanceTime().get();
			
			// 手修正された項目を元に戻す　（勤怠時間用）
			attendanceTime = this.undoRetouchValuesForAttendanceTime(attendanceTime, this.monthlyOldDatas);
				
			// 手修正を戻してから計算必要な項目を再度計算
			if (this.isRetouch){
				this.aggregateResult.setAttendanceTime(Optional.of(this.recalcAttendanceTime(attendanceTime)));
			}
		}
		
		ConcurrentStopwatches.start("12300:36協定時間：");
		
		// 基本計算結果を確認する
		Optional<MonthlyCalculation> basicCalced = Optional.empty();
		if (this.aggregateResult.getAttendanceTime().isPresent()){
			basicCalced = Optional.of(this.aggregateResult.getAttendanceTime().get().getMonthlyCalculation());
		}
		
		// 36協定時間の集計
		MonthlyCalculation monthlyCalculationForAgreement = new MonthlyCalculation();
		val agreementTimeOpt = monthlyCalculationForAgreement.aggregateAgreementTime(
				this.companyId, this.employeeId, this.yearMonth, this.closureId, this.closureDate,
				monthPeriod, Optional.empty(), Optional.empty(), this.companySets, this.employeeSets,
				this.monthlyCalculatingDailys, this.monthlyOldDatas, basicCalced, this.repositories);
		if (agreementTimeOpt.isPresent()){
			val agreementTime = agreementTimeOpt.get();
			this.aggregateResult.setAgreementTime(Optional.of(agreementTime));
		}

		ConcurrentStopwatches.stop("12300:36協定時間：");
		ConcurrentStopwatches.start("12500:任意項目：");
		
		// 月別実績の任意項目を集計
		this.aggregateAnyItem(monthPeriod);
		
		// 手修正された項目を元に戻す　（任意項目用）
		this.undoRetouchValuesForAnyItems(this.monthlyOldDatas);

		ConcurrentStopwatches.stop("12500:任意項目：");
		ConcurrentStopwatches.start("12600:大塚カスタマイズ：");
		
		// 大塚カスタマイズ
		this.customizeForOtsuka();

		ConcurrentStopwatches.stop("12600:大塚カスタマイズ：");
		
		// 戻り値にエラー情報を移送
		for (val errorInfo : this.errorInfos.values()){
			this.aggregateResult.getErrorInfos().putIfAbsent(errorInfo.getResourceId(), errorInfo);
		}
		
		return this.aggregateResult;
	}

	/**
	 * 集計処理
	 * @param monthPeriod
	 */
	private void aggregationProcess(DatePeriod monthPeriod) {
		ConcurrentStopwatches.stop("12100:集計期間ごと準備：");
		
		// 項目の数だけループ
		List<Flex> perErrorsForFlex = new ArrayList<>();
		for (val workingConditionItem : this.workingConditionItems){

			ConcurrentStopwatches.start("12200:労働条件ごと：");
			
			// 「労働条件」の該当履歴から期間を取得
			val historyId = workingConditionItem.getHistoryId();
			if (!this.workingConditions.containsKey(historyId)) continue;

			// 処理期間を計算　（一か月の集計期間と労働条件履歴期間の重複を確認する）
			val term = this.workingConditions.get(historyId);
			DatePeriod aggrPeriod = this.confirmProcPeriod(monthPeriod, term);
			if (aggrPeriod == null) {
				// 履歴の期間と重複がない時
				continue;
			}
			
			// 月別実績の勤怠時間を集計
			val aggregateResult = this.aggregateAttendanceTime(aggrPeriod, workingConditionItem);
			val attendanceTime = aggregateResult.getAttendanceTime();
			if (attendanceTime == null) continue;
			
			// 社員の月別実績のエラー（フレックス不足補填）を確認する
			val resultPerErrorsForFlex = attendanceTime.getMonthlyCalculation().getFlexTime().getPerErrors();
			for (val resultPerError : resultPerErrorsForFlex){
				if (!perErrorsForFlex.contains(resultPerError)) perErrorsForFlex.add(resultPerError);
			}
			
			// データを合算する
			if (this.aggregateResult.getAttendanceTime().isPresent()){
				val calcedAttendanceTime = this.aggregateResult.getAttendanceTime().get();
				attendanceTime.sum(calcedAttendanceTime);
			}

			// 計算中のエラー情報の取得
			val monthlyCalculation = attendanceTime.getMonthlyCalculation();
			for (val errorInfo : monthlyCalculation.getErrorInfos()){
				this.errorInfos.putIfAbsent(errorInfo.getResourceId(), errorInfo);
			}
			
			// 計算結果を戻り値に蓄積
			this.aggregateResult.setAttendanceTime(Optional.of(attendanceTime));
			this.aggregateResult.getAttendanceTimeWeeks().addAll(aggregateResult.getAttendanceTimeWeeks());

			ConcurrentStopwatches.stop("12200:労働条件ごと：");
		}
		
		// 社員の月別実績のエラー（フレックス不足補填）を出力する
		for (val perError : perErrorsForFlex){
			this.aggregateResult.getPerErrors().add(new EmployeeMonthlyPerError(
					ErrorType.FLEX,
					this.yearMonth,
					this.employeeId,
					this.closureId,
					this.closureDate,
					perError,
					null,
					null));
		}

		// 合算後のチェック処理
		this.checkAfterSum(monthPeriod);
	}

	/**
	 * 残数処理
	 * @param monthPeriod
	 */
	private void remainingProcess(DatePeriod monthPeriod) {
		
		ConcurrentStopwatches.start("12400:残数処理：");
		
		// 集計開始日を締め開始日をする時だけ、残数処理を実行する　（集計期間の初月（＝締めの当月）だけ実行する）
		if (this.employeeSets.isNoCheckStartDate()){
			
			// 残数処理
			this.remainingProcess(monthPeriod, InterimRemainMngMode.MONTHLY, true);
		}
		
		ConcurrentStopwatches.stop("12400:残数処理：");
	}
	
	/**
	 * 同じ労働制の履歴を統合
	 * @param target 労働条件項目リスト　（統合前）
	 * @return 労働条件項目リスト　（統合後）
	 */
	private void IntegrateHistoryOfSameWorkSys(List<WorkingConditionItem> target){

		this.workingConditionItems = new ArrayList<>();
		this.workingConditions = new HashMap<>();
		
		val itrTarget = target.listIterator();
		while (itrTarget.hasNext()){
			
			// 要素[n]を取得
			WorkingConditionItem startItem = itrTarget.next();
			val startHistoryId = startItem.getHistoryId();
			val startConditionOpt = this.repositories.getWorkingCondition().getByHistoryId(startHistoryId);
			if (!startConditionOpt.isPresent()) continue;
			val startCondition = startConditionOpt.get();
			if (startCondition.getDateHistoryItem().isEmpty()) continue;
			DatePeriod startPeriod = startCondition.getDateHistoryItem().get(0).span();
			
			// 要素[n]と要素[n+1]以降を順次比較
			WorkingConditionItem endItem = null;
			while (itrTarget.hasNext()){
				WorkingConditionItem nextItem = target.get(itrTarget.nextIndex());
				if (startItem.getLaborSystem() != nextItem.getLaborSystem() ||
					startItem.getHourlyPaymentAtr() != nextItem.getHourlyPaymentAtr()){
					
					// 労働制または時給者区分が異なる履歴が見つかった時点で、労働条件の統合をやめる
					break;
				}
			
				// 労働制と時給者区分が同じ履歴の要素を順次取得
				endItem = itrTarget.next();
			}
			
			// 次の要素がなくなった、または、異なる履歴が見つかれば、集計要素を確定する
			if (endItem == null){
				this.workingConditionItems.add(startItem);
				this.workingConditions.putIfAbsent(startHistoryId, startPeriod);
				continue;
			}
			val endHistoryId = endItem.getHistoryId();
			val endConditionOpt = this.repositories.getWorkingCondition().getByHistoryId(endHistoryId);
			if (!endConditionOpt.isPresent()) continue;;
			val endCondition = endConditionOpt.get();
			if (endCondition.getDateHistoryItem().isEmpty()) continue;
			this.workingConditionItems.add(endItem);
			this.workingConditions.putIfAbsent(endHistoryId,
					new DatePeriod(startPeriod.start(), endCondition.getDateHistoryItem().get(0).end()));
		}
	}
	
	/**
	 * 月別実績の勤怠時間を集計
	 * @param datePeriod 期間
	 * @param workingConditionItem 労働条件項目
	 * @return 月別実績の勤怠時間
	 */
	private AggregateAttendanceTimeValue aggregateAttendanceTime(
			DatePeriod datePeriod,
			WorkingConditionItem workingConditionItem){
		
		AggregateAttendanceTimeValue result = new AggregateAttendanceTimeValue();
		
		// 週Noを確認する
		this.weekNoMap.putIfAbsent(this.yearMonth, 0);
		val startWeekNo = this.weekNoMap.get(this.yearMonth) + 1;
		
		// 労働制を確認する
		val workingSystem = workingConditionItem.getLaborSystem();
		
		ConcurrentStopwatches.start("12210:集計準備：");
		
		// 月別実績の勤怠時間　初期設定
		val attendanceTime = new AttendanceTimeOfMonthly(
				this.employeeId, this.yearMonth, this.closureId, this.closureDate, datePeriod);
		attendanceTime.prepareAggregation(this.companyId, datePeriod, workingConditionItem,
				startWeekNo, this.companySets, this.employeeSets,
				this.monthlyCalculatingDailys, this.monthlyOldDatas, this.repositories);
		val monthlyCalculation = attendanceTime.getMonthlyCalculation();
		if (monthlyCalculation.getErrorInfos().size() > 0) {
			for (val errorInfo : monthlyCalculation.getErrorInfos()){
				this.errorInfos.putIfAbsent(errorInfo.getResourceId(), errorInfo);
			}
			return result;
		}
		
		ConcurrentStopwatches.stop("12210:集計準備：");
		ConcurrentStopwatches.start("12220:月の計算：");
		
		// 月の計算
		monthlyCalculation.aggregate(datePeriod, MonthlyAggregateAtr.MONTHLY,
				Optional.empty(), Optional.empty(), this.repositories);
		
		ConcurrentStopwatches.stop("12220:月の計算：");
		ConcurrentStopwatches.start("12230:縦計：");
		
		// 縦計
		{
			// 週単位の期間を取得
			for (val attendanceTimeWeek : attendanceTime.getMonthlyCalculation().getAttendanceTimeWeeks()){
				DatePeriod weekPeriod = attendanceTimeWeek.getPeriod();
				
				// 週の縦計
				val verticalTotalWeek = attendanceTimeWeek.getVerticalTotal();
				verticalTotalWeek.verticalTotal(this.companyId, this.employeeId, weekPeriod, workingSystem,
						this.companySets, this.employeeSets, this.monthlyCalculatingDailys, this.repositories);
			}
			
			// 月の縦計
			val verticalTotal = attendanceTime.getVerticalTotal();
			verticalTotal.verticalTotal(this.companyId, this.employeeId, datePeriod, workingSystem,
					this.companySets, this.employeeSets, this.monthlyCalculatingDailys, this.repositories);
		}
		
		ConcurrentStopwatches.stop("12230:縦計：");
		ConcurrentStopwatches.start("12240:時間外超過：");
		
		// 時間外超過
		ExcessOutsideWorkMng excessOutsideWorkMng = new ExcessOutsideWorkMng(monthlyCalculation);
		excessOutsideWorkMng.aggregate(this.repositories);
		if (excessOutsideWorkMng.getErrorInfos().size() > 0) {
			for (val errorInfo : excessOutsideWorkMng.getErrorInfos()){
				this.errorInfos.putIfAbsent(errorInfo.getResourceId(), errorInfo);
			}
		}
		attendanceTime.setExcessOutsideWork(excessOutsideWorkMng.getExcessOutsideWork());

		ConcurrentStopwatches.stop("12240:時間外超過：");
		ConcurrentStopwatches.start("12250:回数集計：");
		
		// 回数集計
		{
			// 週単位の期間を取得
			for (val attendanceTimeWeek : attendanceTime.getMonthlyCalculation().getAttendanceTimeWeeks()){
				DatePeriod weekPeriod = attendanceTimeWeek.getPeriod();
				
				// 週の回数集計
				val totalCountWeek = attendanceTimeWeek.getTotalCount();
				totalCountWeek.totalize(this.companyId, this.employeeId, weekPeriod,
						this.companySets, this.monthlyCalculatingDailys, this.repositories);
				if (totalCountWeek.getErrorInfos().size() > 0){
					for (val errorInfo : totalCountWeek.getErrorInfos()){
						this.errorInfos.putIfAbsent(errorInfo.getResourceId(), errorInfo);
					}
				}
			}
			
			// 月の回数集計
			val totalCount = attendanceTime.getTotalCount();
			totalCount.totalize(this.companyId, this.employeeId, datePeriod,
					this.companySets, this.monthlyCalculatingDailys, this.repositories);
			if (totalCount.getErrorInfos().size() > 0){
				for (val errorInfo : totalCount.getErrorInfos()){
					this.errorInfos.putIfAbsent(errorInfo.getResourceId(), errorInfo);
				}
			}
		}
		
		ConcurrentStopwatches.stop("12250:回数集計：");
		
		// 集計結果を返す
		result.setAttendanceTime(attendanceTime);
		for (val attendanceTimeWeek : attendanceTime.getMonthlyCalculation().getAttendanceTimeWeeks()){
			val nowWeekNo = this.weekNoMap.get(this.yearMonth);
			if (nowWeekNo < attendanceTimeWeek.getWeekNo()){
				this.weekNoMap.put(this.yearMonth, attendanceTimeWeek.getWeekNo());
			}
			result.getAttendanceTimeWeeks().add(attendanceTimeWeek);
		}
		return result;
	}

	/**
	 * 月別実績の任意項目を集計
	 * @param monthPeriod 月の期間
	 */
	private void aggregateAnyItem(DatePeriod monthPeriod){

		// 週単位の期間を取得
		ListIterator<AttendanceTimeOfWeekly> itrWeeks =
				this.aggregateResult.getAttendanceTimeWeeks().listIterator();
		while (itrWeeks.hasNext()){
			AttendanceTimeOfWeekly attendanceTimeWeek = itrWeeks.next();
			
			// 週ごとの集計
			val weekResults = this.aggregateAnyItemPeriod(attendanceTimeWeek.getPeriod(), true);
			for (val weekResult : weekResults.values()){
				attendanceTimeWeek.getAnyItem().getAnyItemValues().put(
						weekResult.getOptionalItemNo(),
						AggregateAnyItem.of(
								weekResult.getOptionalItemNo(),
								weekResult.getAnyTime(),
								weekResult.getAnyTimes(),
								weekResult.getAnyAmount()));
			}
			itrWeeks.set(attendanceTimeWeek);
		}
		
		// 月ごとの集計
		val monthResults = this.aggregateAnyItemPeriod(monthPeriod, false);
		for (val monthResult : monthResults.values()){
			this.aggregateResult.putAnyItemOrUpdate(AnyItemOfMonthly.of(
					this.employeeId, this.yearMonth, this.closureId, this.closureDate, monthResult));
		}
	}
	
	/**
	 * 任意項目期間集計
	 * @param period 期間
	 * @param isWeek 週間集計
	 * @return 任意項目集計結果
	 */
	private Map<Integer, AnyItemAggrResult> aggregateAnyItemPeriod(DatePeriod period, boolean isWeek){
		
		Map<Integer, AnyItemAggrResult> results = new HashMap<>();
		List<AnyItemOfMonthly> anyItems = new ArrayList<>();
		
		// 任意項目ごとに集計する
		Map<Integer, AggregateAnyItem> anyItemTotals = new HashMap<>();
		for (val anyItemValueOfDaily : this.monthlyCalculatingDailys.getAnyItemValueOfDailyList()){
			if (!period.contains(anyItemValueOfDaily.getYmd())) continue;
			if (anyItemValueOfDaily.getItems() == null) continue;
			val ymd = anyItemValueOfDaily.getYmd();
			for (val item : anyItemValueOfDaily.getItems()){
				if (item.getItemNo() == null) continue;
				Integer itemNo = item.getItemNo().v();
				
				if (period.contains(ymd)){
					anyItemTotals.putIfAbsent(itemNo, new AggregateAnyItem(itemNo));
					anyItemTotals.get(itemNo).addFromDaily(item);
				}
			}
		}
		
		// 任意項目を取得
		for (val optionalItem : this.companySets.getOptionalItemMap().values()){
			Integer optionalItemNo = optionalItem.getOptionalItemNo().v();

			// 利用条件の判定
			Optional<EmpCondition> empCondition = Optional.empty();
			if (this.companySets.getEmpConditionMap().containsKey(optionalItemNo)){
				empCondition = Optional.of(this.companySets.getEmpConditionMap().get(optionalItemNo));
			}
			val bsEmploymentHistOpt = this.employeeSets.getEmployment(period.end());
			if (optionalItem.checkTermsOfUse(empCondition, bsEmploymentHistOpt))
			{
				
				// 属性に応じて初期化
				AnyTimeMonth anyTime = null;
				AnyTimesMonth anyTimes = null;
				AnyAmountMonth anyAmount = null;
				switch (optionalItem.getOptionalItemAtr()){
				case TIME:
					anyTime = new AnyTimeMonth(0);
					break;
				case NUMBER:
					anyTimes = new AnyTimesMonth(0.0);
					break;
				case AMOUNT:
					anyAmount = new AnyAmountMonth(0);
					break;
				}
				
				// 「実績区分」を判断
				if (optionalItem.getPerformanceAtr() == PerformanceAtr.DAILY_PERFORMANCE || isWeek){

					// 日別実績　縦計処理
					if (anyItemTotals.containsKey(optionalItemNo)){
						val anyItemTotal = anyItemTotals.get(optionalItemNo);
						if (anyItemTotal.getTime().isPresent()){
							if (anyTime == null) anyTime = new AnyTimeMonth(0);
							anyTime = anyTime.addMinutes(anyItemTotal.getTime().get().v());
						}
						if (anyItemTotal.getTimes().isPresent()){
							if (anyTimes == null) anyTimes = new AnyTimesMonth(0.0);
							anyTimes = anyTimes.addTimes(anyItemTotal.getTimes().get().v().doubleValue());
						}
						if (anyItemTotal.getAmount().isPresent()){
							if (anyAmount == null) anyAmount = new AnyAmountMonth(0);
							anyAmount = anyAmount.addAmount(anyItemTotal.getAmount().get().v());
						}
					}
				}
				else if (this.aggregateResult.getAttendanceTime().isPresent()){
					val attendanceTime = this.aggregateResult.getAttendanceTime().get();
					
					// 月別実績　計算処理
					List<Formula> targetFormulas = this.companySets.getFormulaList().stream()
							.filter(c -> c.getOptionalItemNo().equals(optionalItem.getOptionalItemNo()))
							.collect(Collectors.toList());
					val monthlyConverter = this.repositories.getAttendanceItemConverter().createMonthlyConverter();
					MonthlyRecordToAttendanceItemConverter monthlyRecordDto = monthlyConverter.withAttendanceTime(attendanceTime);
					monthlyRecordDto = monthlyRecordDto.withAnyItem(anyItems);
					val calcResult = optionalItem.caluculationFormula(
							this.companyId, optionalItem, targetFormulas,
							Optional.empty(), Optional.of(monthlyRecordDto));
					if (calcResult != null){
						if (calcResult.getTime().isPresent()){
							if (anyTime == null) anyTime = new AnyTimeMonth(0);
							anyTime = anyTime.addMinutes(calcResult.getTime().get().intValue());
						}
						if (calcResult.getCount().isPresent()){
							if (anyTimes == null) anyTimes = new AnyTimesMonth(0.0);
							anyTimes = anyTimes.addTimes(calcResult.getCount().get().doubleValue());
						}
						if (calcResult.getMoney().isPresent()){
							if (anyAmount == null) anyAmount = new AnyAmountMonth(0);
							anyAmount = anyAmount.addAmount(calcResult.getMoney().get().intValue());
						}
					}
				}
				
				// 任意項目集計結果を返す
				AnyItemAggrResult result = AnyItemAggrResult.of(optionalItemNo, anyTime, anyTimes, anyAmount);
				results.put(optionalItemNo, result);
				anyItems.add(AnyItemOfMonthly.of(
						this.employeeId, this.yearMonth, this.closureId, this.closureDate, result));
			}
		}
		
		return results;
	}
	
	/**
	 * 合算後のチェック処理
	 * @param period 期間
	 */
	private void checkAfterSum(DatePeriod period){
		
		// 「日別実績の勤怠時間」を取得する
		if (!this.monthlyCalculatingDailys.isExistDailyRecord(period)){
			val errorInfo = new MonthlyAggregationErrorInfo(
					"018", new ErrMessageContent(TextResource.localize("Msg_1376")));
			this.errorInfos.putIfAbsent(errorInfo.getResourceId(), errorInfo);
			return;
		}
		
		if (this.aggregateResult.getAttendanceTime().isPresent()){
			val attendanceTime = this.aggregateResult.getAttendanceTime().get();
			
			// フレックス時間を確認する
			val flexTime = attendanceTime.getMonthlyCalculation().getFlexTime();
			int flexMinutes = flexTime.getFlexTime().getFlexTime().getTime().v();
			if (flexMinutes < 0){
				this.aggregateResult.getPerErrors().add(new EmployeeMonthlyPerError(
						ErrorType.FLEX_SUPP,
						this.yearMonth,
						this.employeeId,
						this.closureId,
						this.closureDate,
						null,
						null,
						null));
			}
		}
		
		// ※　乖離フラグは、縦計の合算と同時に再チェックされるため、ここでは不要。
		// ※　平均時間関連は、縦計の合算と同時に再計算されるため、ここでは不要。
	}
	
	/**
	 * 手修正された項目を元に戻す　（勤怠時間用）
	 * @param attendanceTime 月別実績の勤怠時間
	 * @param monthlyOldDatas 集計前の月別実績データ
	 * @return 月別実績の勤怠時間
	 */
	private AttendanceTimeOfMonthly undoRetouchValuesForAttendanceTime(
			AttendanceTimeOfMonthly attendanceTime,
			MonthlyOldDatas monthlyOldDatas){

		this.isRetouch = false;
		
		// 既存データを確認する
		val oldDataOpt = monthlyOldDatas.getAttendanceTime();
		if (!oldDataOpt.isPresent()) return attendanceTime;
		val oldConverter = this.repositories.getAttendanceItemConverter().createMonthlyConverter();
		val oldItemConvert = oldConverter.withAttendanceTime(oldDataOpt.get());

		// 計算後データを確認
		val monthlyConverter = this.repositories.getAttendanceItemConverter().createMonthlyConverter();
		val convert = monthlyConverter.withAttendanceTime(attendanceTime);
		
		// 月別実績の編集状態を取得
		for (val editState : this.editStates){
			
			// 勤怠項目IDから項目を判断
			val itemValueOpt = oldItemConvert.convert(editState.getAttendanceItemId());
			if (!itemValueOpt.isPresent()) continue;
			val itemValue = itemValueOpt.get();
			if (itemValue.value() == null) continue;
			
			// 該当する勤怠項目IDの値を計算前に戻す
			convert.merge(itemValue);
			this.isRetouch = true;
		}

		// いずれかの手修正値を戻した時、戻した後の勤怠時間を返す
		if (this.isRetouch){
			val convertedOpt = convert.toAttendanceTime();
			if (convertedOpt.isPresent()) {
				val retouchedTime = convertedOpt.get();
				retouchedTime.getMonthlyCalculation().copySettings(attendanceTime.getMonthlyCalculation());
				return retouchedTime;
			}
		}
		return attendanceTime;
	}

	/**
	 * 手修正を戻してから計算必要な項目を再度計算
	 * @param attendanceTime 月別実績の勤怠時間
	 * @return 月別実績の勤怠時間
	 */
	private AttendanceTimeOfMonthly recalcAttendanceTime(AttendanceTimeOfMonthly attendanceTime){
	
		val monthlyCalculation = attendanceTime.getMonthlyCalculation();
		
		// 残業合計時間を集計する
		monthlyCalculation.getAggregateTime().getOverTime().recalcTotal();
		
		// 休出合計時間を集計する
		monthlyCalculation.getAggregateTime().getHolidayWorkTime().recalcTotal();

		// 総労働時間と36協定時間の再計算
		monthlyCalculation.recalcTotalAndAgreement(attendanceTime.getDatePeriod(),
				MonthlyAggregateAtr.MONTHLY, this.repositories);
		
		return attendanceTime;
	}
	
	
	/**
	 * 手修正された項目を元に戻す　（任意項目用）
	 * @param monthlyOldDatas 集計前の月別実績データ
	 */
	private void undoRetouchValuesForAnyItems(MonthlyOldDatas monthlyOldDatas){

		this.isRetouch = false;
		
		// 既存データを確認する
		val oldDataList = monthlyOldDatas.getAnyItemList();
		if (oldDataList.size() == 0) return;
		val oldConverter = this.repositories.getAttendanceItemConverter().createMonthlyConverter();
		val oldItemConvert = oldConverter.withAnyItem(oldDataList);

		// 計算後データを確認
		val monthlyConverter = this.repositories.getAttendanceItemConverter().createMonthlyConverter();
		MonthlyRecordToAttendanceItemConverter convert =
				monthlyConverter.withAttendanceTime(this.aggregateResult.getAttendanceTime().get());
		convert = convert.withAnyItem(this.aggregateResult.getAnyItemList());
		
		// 月別実績の編集状態を取得
		for (val editState : this.editStates){
			
			// 勤怠項目IDから項目を判断
			val itemValueOpt = oldItemConvert.convert(editState.getAttendanceItemId());
			if (!itemValueOpt.isPresent()) continue;
			val itemValue = itemValueOpt.get();
			if (itemValue.value() == null) continue;
			
			// 該当する勤怠項目IDの値を計算前に戻す
			convert.merge(itemValue);
			this.isRetouch = true;
		}

		// いずれかの手修正値を戻した時、戻した後の任意項目を返す
		if (this.isRetouch){
			val convertedList = convert.toAnyItems();
			this.aggregateResult.setAnyItemList(convertedList);
		}
	}

	/**
	 * 残数処理
	 * @param period 期間
	 * @param interimRemainMngMode 暫定残数データ管理モード
	 * @param isCalcAttendanceRate 出勤率計算フラグ
	 */
	private void remainingProcess(
			DatePeriod period,
			InterimRemainMngMode interimRemainMngMode,
			boolean isCalcAttendanceRate){
		
		ConcurrentStopwatches.start("12405:暫定データ作成：");

		// Workを考慮した月次処理用の暫定残数管理データを作成する
		this.createDailyInterimRemainMngs(period);
		
		ConcurrentStopwatches.stop("12405:暫定データ作成：");
		ConcurrentStopwatches.start("12410:年休積休：");
		
		// 年休、積休
		this.annualAndReserveLeaveRemain(period, interimRemainMngMode, isCalcAttendanceRate);

		ConcurrentStopwatches.stop("12410:年休積休：");
		ConcurrentStopwatches.start("12420:振休：");
		
		// 振休
		this.absenceLeaveRemain(period, interimRemainMngMode);

		ConcurrentStopwatches.stop("12420:振休：");
		ConcurrentStopwatches.start("12430:代休：");
		
		// 代休
		this.dayoffRemain(period, interimRemainMngMode);
		
		ConcurrentStopwatches.stop("12430:代休：");
		ConcurrentStopwatches.start("12440:特別休暇：");
		
		// 特別休暇
		this.specialLeaveRemain(period, interimRemainMngMode);
		
		ConcurrentStopwatches.stop("12440:特別休暇：");
	}
	
	/**
	 * Workを考慮した月次処理用の暫定残数管理データを作成する
	 * @param period 期間
	 */
	private void createDailyInterimRemainMngs(DatePeriod period){
		
		// 【参考：旧処理】　月次処理用の暫定残数管理データを作成する
		//this.dailyInterimRemainMngs = this.interimRemOffMonth.monthInterimRemainData(
		//		this.companyId, this.employeeId, period);
		
		// 残数作成元情報(実績)を作成する
		List<RecordRemainCreateInfor> recordRemains = this.remNumCreateInfo.createRemainInfor(
				new ArrayList<>(this.monthlyCalculatingDailys.getAttendanceTimeOfDailyMap().values()),
				new ArrayList<>(this.monthlyCalculatingDailys.getWorkInfoOfDailyMap().values()));
		
		// 指定期間の暫定残数管理データを作成する
		InterimRemainCreateDataInputPara inputPara = new InterimRemainCreateDataInputPara(
				this.companyId, this.employeeId, period,
				recordRemains, Collections.emptyList(), Collections.emptyList(), false);
		CompanyHolidayMngSetting comHolidaySetting = new CompanyHolidayMngSetting(
				this.companyId, this.companySets.getAbsSettingOpt(), this.companySets.getDayOffSetting());
		this.dailyInterimRemainMngs = this.periodCreateData.createInterimRemainDataMng(inputPara, comHolidaySetting);
		
		this.isOverWriteRemain = (this.dailyInterimRemainMngs.size() > 0);
	}
	
	/**
	 * 年休、積休
	 * @param period 期間
	 * @param interimRemainMngMode 暫定残数データ管理モード
	 * @param isCalcAttendanceRate 出勤率計算フラグ
	 */
	private void annualAndReserveLeaveRemain(
			DatePeriod period,
			InterimRemainMngMode interimRemainMngMode,
			boolean isCalcAttendanceRate){
		
		// 暫定残数データを年休・積立年休に絞り込む
		List<TmpAnnualLeaveMngWork> tmpAnnualLeaveMngs = new ArrayList<>();
		List<TmpReserveLeaveMngWork> tmpReserveLeaveMngs = new ArrayList<>();
		for (val dailyInterimRemainMng : this.dailyInterimRemainMngs.values()){
			if (dailyInterimRemainMng.getRecAbsData().size() <= 0) continue;
			val master = dailyInterimRemainMng.getRecAbsData().get(0);
			
			// 年休
			if (dailyInterimRemainMng.getAnnualHolidayData().isPresent()){
				val data = dailyInterimRemainMng.getAnnualHolidayData().get();
				tmpAnnualLeaveMngs.add(TmpAnnualLeaveMngWork.of(master, data));
			}
			
			// 積立年休
			if (dailyInterimRemainMng.getResereData().isPresent()){
				val data = dailyInterimRemainMng.getResereData().get();
				tmpReserveLeaveMngs.add(TmpReserveLeaveMngWork.of(master, data));
			}
		}

		// 月別実績の計算結果が存在するかチェック
		boolean isOverWriteAnnual = this.isOverWriteRemain;
		if (this.aggregateResult.getAttendanceTime().isPresent()){
			
			// 年休控除日数分の年休暫定残数データを作成する
			val compensFlexWorkOpt = this.createInterimAnnual.ofCompensFlexToWork(
					this.aggregateResult.getAttendanceTime().get(), period.end());
			if (compensFlexWorkOpt.isPresent()){
				tmpAnnualLeaveMngs.add(compensFlexWorkOpt.get());
				isOverWriteAnnual = true;
			}
		}

		// 「モード」をチェック
		CalYearOffWorkAttendRate daysForCalcAttdRate = new CalYearOffWorkAttendRate();
		if (interimRemainMngMode == InterimRemainMngMode.MONTHLY){
			
			// 日別実績から出勤率計算用日数を取得　（月別集計用）
			daysForCalcAttdRate = this.getDaysForCalcAttdRate.algorithm(
					this.companyId, this.employeeId, period,
					this.companySets, this.monthlyCalculatingDailys, this.repositories);
		}
		
		// 期間中の年休積休残数を取得
		val aggrResult = this.getAnnAndRsvRemNumWithinPeriod.algorithm(
				this.companyId, this.employeeId, period, interimRemainMngMode,
				period.end(), true, isCalcAttendanceRate,
				Optional.of(isOverWriteAnnual), Optional.of(tmpAnnualLeaveMngs), Optional.of(tmpReserveLeaveMngs),
				Optional.of(false),
				Optional.of(this.employeeSets.isNoCheckStartDate()),
				this.prevAggrResult.getAnnualLeave(), this.prevAggrResult.getReserveLeave(),
				Optional.of(this.companySets), Optional.of(this.employeeSets),
				Optional.of(this.monthlyCalculatingDailys));
		
		// 2回目の取得以降は、締め開始日を確認させる
		this.employeeSets.setNoCheckStartDate(false);
		
		if (aggrResult.getAnnualLeave().isPresent()){
			val asOfPeriodEnd = aggrResult.getAnnualLeave().get().getAsOfPeriodEnd();
			val remainingNumber = asOfPeriodEnd.getRemainingNumber();
			
			// 年休月別残数データを更新
			AnnLeaRemNumEachMonth annLeaRemNum = AnnLeaRemNumEachMonth.of(
					this.employeeId,
					this.yearMonth,
					this.closureId,
					this.closureDate,
					period,
					ClosureStatus.UNTREATED,
					remainingNumber.getAnnualLeaveNoMinus(),
					remainingNumber.getAnnualLeaveWithMinus(),
					remainingNumber.getHalfDayAnnualLeaveNoMinus(),
					remainingNumber.getHalfDayAnnualLeaveWithMinus(),
					asOfPeriodEnd.getGrantInfo(),
					remainingNumber.getTimeAnnualLeaveNoMinus(),
					remainingNumber.getTimeAnnualLeaveWithMinus(),
					AnnualLeaveAttdRateDays.of(
							new MonthlyDays(daysForCalcAttdRate.getWorkingDays()),
							new MonthlyDays(daysForCalcAttdRate.getPrescribedDays()),
							new MonthlyDays(daysForCalcAttdRate.getDeductedDays())),
					asOfPeriodEnd.isAfterGrantAtr());
			this.aggregateResult.getAnnLeaRemNumEachMonthList().add(annLeaRemNum);
			
			// 年休エラーから月別残数エラー一覧を作成する
			this.aggregateResult.getPerErrors().addAll(this.createPerErrorFromLeaveErrors.fromAnnualLeave(
					this.employeeId, this.yearMonth, this.closureId, this.closureDate,
					aggrResult.getAnnualLeave().get().getAnnualLeaveErrors()));
		}
		
		if (aggrResult.getReserveLeave().isPresent()){
			val asOfPeriodEnd = aggrResult.getReserveLeave().get().getAsOfPeriodEnd();
			val remainingNumber = asOfPeriodEnd.getRemainingNumber();
			
			// 積立年休月別残数データを更新
			ReserveLeaveGrant reserveLeaveGrant = null;
			if (asOfPeriodEnd.getGrantInfo().isPresent()){
				reserveLeaveGrant = ReserveLeaveGrant.of(asOfPeriodEnd.getGrantInfo().get().getGrantDays());
			}
			RsvLeaRemNumEachMonth rsvLeaRemNum = RsvLeaRemNumEachMonth.of(
					this.employeeId,
					this.yearMonth,
					this.closureId,
					this.closureDate,
					period,
					ClosureStatus.UNTREATED,
					remainingNumber.getReserveLeaveNoMinus(),
					remainingNumber.getReserveLeaveWithMinus(),
					Optional.ofNullable(reserveLeaveGrant),
					asOfPeriodEnd.isAfterGrantAtr());
			this.aggregateResult.getRsvLeaRemNumEachMonthList().add(rsvLeaRemNum);
			
			// 積立年休エラーから月別残数エラー一覧を作成する
			this.aggregateResult.getPerErrors().addAll(this.createPerErrorFromLeaveErrors.fromReserveLeave(
					this.employeeId, this.yearMonth, this.closureId, this.closureDate,
					aggrResult.getReserveLeave().get().getReserveLeaveErrors()));
		}
		
		// 集計結果を前回集計結果に引き継ぐ
		this.aggregateResult.setAggrResultOfAnnAndRsvLeave(aggrResult);
	}
	
	/**
	 * 振休
	 * @param period 期間
	 * @param interimRemainMngMode 暫定残数データ管理モード
	 */
	private void absenceLeaveRemain(
			DatePeriod period,
			InterimRemainMngMode interimRemainMngMode){

		// 暫定残数データを振休・振出に絞り込む
		List<InterimRemain> interimMng = new ArrayList<>();
		List<InterimAbsMng> useAbsMng = new ArrayList<>();
		List<InterimRecMng> useRecMng = new ArrayList<>();
		for (val dailyInterimRemainMng : this.dailyInterimRemainMngs.values()){
			if (dailyInterimRemainMng.getRecAbsData().size() <= 0) continue;
			interimMng.addAll(dailyInterimRemainMng.getRecAbsData());

			// 振休
			if (dailyInterimRemainMng.getInterimAbsData().isPresent()){
				useAbsMng.add(dailyInterimRemainMng.getInterimAbsData().get());
			}
			
			// 振出
			if (dailyInterimRemainMng.getRecData().isPresent()){
				useRecMng.add(dailyInterimRemainMng.getRecData().get());
			}
		}
		
		// 期間内の振休振出残数を取得する
		AbsRecMngInPeriodParamInput paramInput = new AbsRecMngInPeriodParamInput(
				this.companyId, this.employeeId, period, period.end(),
				(interimRemainMngMode == InterimRemainMngMode.MONTHLY),
				this.isOverWriteRemain, useAbsMng, interimMng, useRecMng);
		val aggrResult = this.absenceRecruitMng.getAbsRecMngInPeriod(paramInput);
		if (aggrResult != null){
			
			// 振休月別残数データを更新
			AbsenceLeaveRemainData absLeaRemNum = new AbsenceLeaveRemainData(
					this.employeeId,
					this.yearMonth,
					this.closureId.value,
					this.closureDate.getClosureDay().v(),
					this.closureDate.getLastDayOfMonth(),
					ClosureStatus.UNTREATED,
					period.start(),
					period.end(),
					new RemainDataDaysMonth(aggrResult.getOccurrenceDays()),
					new RemainDataDaysMonth(aggrResult.getUseDays()),
					new AttendanceDaysMonthToTal(aggrResult.getRemainDays()),
					new AttendanceDaysMonthToTal(aggrResult.getCarryForwardDays()),
					new RemainDataDaysMonth(aggrResult.getUnDigestedDays()));
			this.aggregateResult.getAbsenceLeaveRemainList().add(absLeaRemNum);
			
			// 振休エラーから月別残数エラー一覧を作成する
			this.aggregateResult.getPerErrors().addAll(this.createPerErrorFromLeaveErrors.fromPause(
					this.employeeId, this.yearMonth, this.closureId, this.closureDate, aggrResult.getPError()));
		}
	}
	
	/**
	 * 代休
	 * @param period 期間
	 * @param interimRemainMngMode 暫定残数データ管理モード
	 */
	private void dayoffRemain(
			DatePeriod period,
			InterimRemainMngMode interimRemainMngMode){

		// 暫定残数データを休出・代休に絞り込む
		List<InterimRemain> interimMng = new ArrayList<>();
		List<InterimBreakMng> breakMng = new ArrayList<>();
		List<InterimDayOffMng> dayOffMng = new ArrayList<>();
		for (val dailyInterimRemainMng : this.dailyInterimRemainMngs.values()){
			if (dailyInterimRemainMng.getRecAbsData().size() <= 0) continue;
			interimMng.addAll(dailyInterimRemainMng.getRecAbsData());

			// 休出
			if (dailyInterimRemainMng.getBreakData().isPresent()){
				breakMng.add(dailyInterimRemainMng.getBreakData().get());
			}
			
			// 代休
			if (dailyInterimRemainMng.getDayOffData().isPresent()){
				dayOffMng.add(dailyInterimRemainMng.getDayOffData().get());
			}
		}
		
		// 期間内の休出代休残数を取得する
		BreakDayOffRemainMngParam inputParam = new BreakDayOffRemainMngParam(
				this.companyId, this.employeeId, period,
				(interimRemainMngMode == InterimRemainMngMode.MONTHLY), period.end(),
				this.isOverWriteRemain, interimMng, breakMng, dayOffMng);
		val aggrResult = this.breakDayoffMng.getBreakDayOffMngInPeriod(inputParam);
		if (aggrResult != null){
			
			// 代休月別残数データを更新
			MonthlyDayoffRemainData monDayRemNum = new MonthlyDayoffRemainData(
					this.employeeId,
					this.yearMonth,
					this.closureId.value,
					this.closureDate.getClosureDay().v(),
					this.closureDate.getLastDayOfMonth(),
					ClosureStatus.UNTREATED,
					period.start(),
					period.end(),
					new DayOffDayAndTimes(
							new RemainDataDaysMonth(aggrResult.getOccurrenceDays()),
							Optional.of(new RemainDataTimesMonth(aggrResult.getOccurrenceTimes()))),
					new DayOffDayAndTimes(
							new RemainDataDaysMonth(aggrResult.getUseDays()),
							Optional.of(new RemainDataTimesMonth(aggrResult.getUseTimes()))),
					new DayOffRemainDayAndTimes(
							new AttendanceDaysMonthToTal(aggrResult.getRemainDays()),
							Optional.of(new RemainingMinutes(aggrResult.getRemainTimes()))),
					new DayOffRemainDayAndTimes(
							new AttendanceDaysMonthToTal(aggrResult.getCarryForwardDays()),
							Optional.of(new RemainingMinutes(aggrResult.getRemainTimes()))),
					new DayOffDayAndTimes(
							new RemainDataDaysMonth(aggrResult.getUnDigestedDays()),
							Optional.of(new RemainDataTimesMonth(aggrResult.getUnDigestedTimes()))));
			this.aggregateResult.getMonthlyDayoffRemainList().add(monDayRemNum);
			
			// 代休エラーから月別残数エラー一覧を作成する
			this.aggregateResult.getPerErrors().addAll(this.createPerErrorFromLeaveErrors.fromDayOff(
					this.employeeId, this.yearMonth, this.closureId, this.closureDate, aggrResult.getLstError()));
		}
	}
	
	/**
	 * 特別休暇
	 * @param period 期間
	 * @param interimRemainMngMode 暫定残数データ管理モード
	 */
	private void specialLeaveRemain(
			DatePeriod period,
			InterimRemainMngMode interimRemainMngMode){

		// 暫定残数データを特別休暇に絞り込む
		List<InterimRemain> interimMng = new ArrayList<>();
		List<InterimSpecialHolidayMng> interimSpecialData = new ArrayList<>();
		for (val dailyInterimRemainMng : this.dailyInterimRemainMngs.values()){
			if (dailyInterimRemainMng.getRecAbsData().size() <= 0) continue;
			if (dailyInterimRemainMng.getSpecialHolidayData().size() <= 0) continue;
			interimMng.addAll(dailyInterimRemainMng.getRecAbsData());
			interimSpecialData.addAll(dailyInterimRemainMng.getSpecialHolidayData());
		}
		
		// 「特別休暇」を取得する
		val specialHolidays = this.specialHolidayRepo.findByCompanyId(this.companyId);
		for (val specialHoliday : specialHolidays){
			int specialLeaveCode = specialHoliday.getSpecialHolidayCode().v();

			// マイナスなしを含めた期間内の特別休暇残を集計する
			// 期間内の特別休暇残を集計する
			ComplileInPeriodOfSpecialLeaveParam param = new ComplileInPeriodOfSpecialLeaveParam(
					this.companyId, this.employeeId, period,
					(interimRemainMngMode == InterimRemainMngMode.MONTHLY), period.end(), specialLeaveCode, true,
					this.isOverWriteRemain, interimMng, interimSpecialData);
			InPeriodOfSpecialLeave inPeriod = this.specialLeaveMng.complileInPeriodOfSpecialLeave(param);
			
			// マイナスなしの残数・使用数を計算
			RemainDaysOfSpecialHoliday remainDays = inPeriod.getRemainDays();
			SpecialLeaveRemainNoMinus remainNoMinus = new SpecialLeaveRemainNoMinus(remainDays);
			
			// 特別休暇月別残数データを更新
			SpecialHolidayRemainData speLeaRemNum = SpecialHolidayRemainData.of(
					this.employeeId,
					this.yearMonth,
					this.closureId,
					this.closureDate,
					period,
					specialLeaveCode,
					inPeriod,
					remainNoMinus);
			this.aggregateResult.getSpecialLeaveRemainList().add(speLeaRemNum);
			
			// 特別休暇エラーから月別残数エラー一覧を作成する
			this.aggregateResult.getPerErrors().addAll(this.createPerErrorFromLeaveErrors.fromSpecialLeave(
					this.employeeId, this.yearMonth, this.closureId, this.closureDate, specialLeaveCode,
					inPeriod.getLstError()));
		}
	}
	
	/**
	 * 大塚カスタマイズ
	 */
	private void customizeForOtsuka(){
		
		//2018.01.21 DEL shuichi_ishida Redmine#105681
		// 時短日割適用日数
		//this.TimeSavingDailyRateApplyDays();
	}
	
	/**
	 * 処理期間との重複を確認する　（重複期間を取り出す）
	 * @param target 処理期間
	 * @param comparison 比較対象期間
	 * @return 重複期間　（null = 重複なし）
	 */
	private DatePeriod confirmProcPeriod(DatePeriod target, DatePeriod comparison){

		DatePeriod overlap = null;		// 重複期間
		
		// 開始前
		if (target.isBefore(comparison)) return overlap;
		
		// 終了後
		if (target.isAfter(comparison)) return overlap;
		
		// 重複あり
		overlap = target;
		
		// 開始日より前を除外
		if (overlap.contains(comparison.start())){
			overlap = overlap.cutOffWithNewStart(comparison.start());
		}
		
		// 終了日より後を除外
		if (overlap.contains(comparison.end())){
			overlap = overlap.cutOffWithNewEnd(comparison.end());
		}

		return overlap;
	}
	
	/**
	 * 所属情報の作成
	 * @param datePeriod 期間
	 * @return 月別実績の所属情報
	 */
	private AffiliationInfoOfMonthly createAffiliationInfo(DatePeriod datePeriod){
		
		// 月初の所属情報を取得
		boolean isExistStartWorkInfo = false;
		if (this.monthlyCalculatingDailys.getWorkInfoOfDailyMap().containsKey(datePeriod.start())){
			isExistStartWorkInfo = true;
		}
		val firstInfoOfDailyOpt = this.repositories.getAffiliationInfoOfDaily().findByKey(
				this.employeeId, datePeriod.start());
		if (!firstInfoOfDailyOpt.isPresent()){
			if (isExistStartWorkInfo){
				val errorInfo = new MonthlyAggregationErrorInfo(
						"003", new ErrMessageContent(TextResource.localize("Msg_1157")));
				this.errorInfos.putIfAbsent(errorInfo.getResourceId(), errorInfo);
			}
			return null;
		}
		val firstInfoOfDaily = firstInfoOfDailyOpt.get();
		val firstWorkTypeOfDailyOpt = this.repositories.getWorkTypeOfDaily().findByKey(
				this.employeeId, datePeriod.start());
		if (!firstWorkTypeOfDailyOpt.isPresent()){
			if (isExistStartWorkInfo){
				val errorInfo = new MonthlyAggregationErrorInfo(
						"003", new ErrMessageContent(TextResource.localize("Msg_1157")));
				this.errorInfos.putIfAbsent(errorInfo.getResourceId(), errorInfo);
			}
			return null;
		}
		val firstWorkTypeOfDaily = firstWorkTypeOfDailyOpt.get();
		
		// 月初の情報を作成
		val firstInfo = AggregateAffiliationInfo.of(
				firstInfoOfDaily.getEmploymentCode(),
				new WorkplaceId(firstInfoOfDaily.getWplID()),
				new JobTitleId(firstInfoOfDaily.getJobTitleID()),
				firstInfoOfDaily.getClsCode(),
				firstWorkTypeOfDaily.getWorkTypeCode());

		// 月末がシステム日付以降の場合、月初の情報を月末の情報とする
		if (datePeriod.end().after(GeneralDate.today())){

			// 月別実績の所属情報を返す
			return AffiliationInfoOfMonthly.of(this.employeeId, this.yearMonth, this.closureId, this.closureDate,
					firstInfo, firstInfo);
		}
		
		// 月末の所属情報を取得
		val lastInfoOfDailyOpt = this.repositories.getAffiliationInfoOfDaily().findByKey(
				this.employeeId, datePeriod.end());
		if (!lastInfoOfDailyOpt.isPresent()){
			//val errorInfo = new MonthlyAggregationErrorInfo(
			//		"004", new ErrMessageContent(TextResource.localize("Msg_1157")));
			//this.errorInfos.putIfAbsent(errorInfo.getResourceId(), errorInfo);
			//return null;
			
			// 月別実績の所属情報を返す　（エラーにせず、月末に月初の情報を入れる）
			return AffiliationInfoOfMonthly.of(this.employeeId, this.yearMonth, this.closureId, this.closureDate,
					firstInfo, firstInfo);
		}
		val lastInfoOfDaily = lastInfoOfDailyOpt.get();
		val lastWorkTypeOfDailyOpt = this.repositories.getWorkTypeOfDaily().findByKey(
				this.employeeId, datePeriod.end());
		if (!lastWorkTypeOfDailyOpt.isPresent()){
			//val errorInfo = new MonthlyAggregationErrorInfo(
			//		"004", new ErrMessageContent(TextResource.localize("Msg_1157")));
			//this.errorInfos.putIfAbsent(errorInfo.getResourceId(), errorInfo);
			//return null;
			
			// 月別実績の所属情報を返す　（エラーにせず、月末に月初の情報を入れる）
			return AffiliationInfoOfMonthly.of(this.employeeId, this.yearMonth, this.closureId, this.closureDate,
					firstInfo, firstInfo);
		}
		val lastWorkTypeOfDaily = lastWorkTypeOfDailyOpt.get();

		// 月末の情報を作成
		val lastInfo = AggregateAffiliationInfo.of(
				lastInfoOfDaily.getEmploymentCode(),
				new WorkplaceId(lastInfoOfDaily.getWplID()),
				new JobTitleId(lastInfoOfDaily.getJobTitleID()),
				lastInfoOfDaily.getClsCode(),
				lastWorkTypeOfDaily.getWorkTypeCode());
		
		// 月別実績の所属情報を返す
		return AffiliationInfoOfMonthly.of(this.employeeId, this.yearMonth, this.closureId, this.closureDate,
				firstInfo, lastInfo);
	}
	
	/**
	 * 時短日割適用日数
	 */
	private void TimeSavingDailyRateApplyDays(){
		
		// 月別実績の所属情報を取得
		val affiliationInfoOpt = this.aggregateResult.getAffiliationInfo();
		if (!affiliationInfoOpt.isPresent()) return;
		
		// 月末の勤務情報を判断
		val lastInfo = affiliationInfoOpt.get().getLastInfo();
		if (lastInfo.getBusinessTypeCd().v().compareTo("0000002030") == 0){
			
			// 任意項目50にセット
			this.aggregateResult.putAnyItemOrUpdate(AnyItemOfMonthly.of(
					this.employeeId, this.yearMonth, this.closureId, this.closureDate,
					50, Optional.empty(), Optional.of(new AnyTimesMonth(20.67)), Optional.empty()));
		}
	}
}
