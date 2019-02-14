package nts.uk.ctx.at.function.app.command.processexecution;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

//import lombok.val;
import nts.arc.layer.app.command.AsyncCommandHandler;
import nts.arc.layer.app.command.AsyncCommandHandlerContext;
import nts.arc.layer.app.command.CommandHandlerContext;
import nts.arc.task.parallel.ManagedParallelWithContext;
import nts.arc.task.parallel.ManagedParallelWithContext.ControlOption;
//import nts.arc.task.AsyncTaskInfo;
import nts.arc.time.GeneralDate;
import nts.arc.time.GeneralDateTime;
import nts.arc.time.YearMonth;
import nts.gul.collection.CollectionUtil;
import nts.gul.text.IdentifierUtil;
import nts.uk.ctx.at.function.app.command.processexecution.approuteupdatedaily.AppRouteUpdateDailyService;
import nts.uk.ctx.at.function.app.command.processexecution.approuteupdatemonthly.AppRouteUpdateMonthlyService;
import nts.uk.ctx.at.function.dom.adapter.RegulationInfoEmployeeAdapter;
import nts.uk.ctx.at.function.dom.adapter.RegulationInfoEmployeeAdapterDto;
import nts.uk.ctx.at.function.dom.adapter.RegulationInfoEmployeeAdapterImport;
import nts.uk.ctx.at.function.dom.adapter.WorkPlaceHistImport;
import nts.uk.ctx.at.function.dom.adapter.WorkplaceWorkRecordAdapter;
import nts.uk.ctx.at.function.dom.adapter.appreflectmanager.AppReflectManagerAdapter;
import nts.uk.ctx.at.function.dom.adapter.appreflectmanager.ProcessStateReflectImport;
import nts.uk.ctx.at.function.dom.adapter.employeemanage.EmployeeManageAdapter;
import nts.uk.ctx.at.function.dom.adapter.toppagealarmpub.AlarmCategoryFn;
import nts.uk.ctx.at.function.dom.adapter.toppagealarmpub.ExecutionLogAdapterFn;
import nts.uk.ctx.at.function.dom.adapter.toppagealarmpub.ExecutionLogErrorDetailFn;
import nts.uk.ctx.at.function.dom.adapter.toppagealarmpub.ExecutionLogImportFn;
import nts.uk.ctx.at.function.dom.adapter.worklocation.RecordWorkInfoFunAdapter;
import nts.uk.ctx.at.function.dom.adapter.worklocation.WorkInfoOfDailyPerFnImport;
import nts.uk.ctx.at.function.dom.alarm.alarmlist.createextractionprocess.CreateExtraProcessService;
import nts.uk.ctx.at.function.dom.alarm.alarmlist.execalarmlistprocessing.ExecAlarmListProcessingService;
import nts.uk.ctx.at.function.dom.alarm.alarmlist.execalarmlistprocessing.OutputExecAlarmListPro;
import nts.uk.ctx.at.function.dom.processexecution.ExecutionCode;
import nts.uk.ctx.at.function.dom.processexecution.ExecutionScopeClassification;
import nts.uk.ctx.at.function.dom.processexecution.LastExecDateTime;
import nts.uk.ctx.at.function.dom.processexecution.ProcessExecType;
import nts.uk.ctx.at.function.dom.processexecution.ProcessExecution;
import nts.uk.ctx.at.function.dom.processexecution.ProcessExecutionScopeItem;
import nts.uk.ctx.at.function.dom.processexecution.executionlog.CurrentExecutionStatus;
import nts.uk.ctx.at.function.dom.processexecution.executionlog.EachProcessPeriod;
import nts.uk.ctx.at.function.dom.processexecution.executionlog.EndStatus;
import nts.uk.ctx.at.function.dom.processexecution.executionlog.ExecutionTaskLog;
import nts.uk.ctx.at.function.dom.processexecution.executionlog.ProcessExecutionLog;
import nts.uk.ctx.at.function.dom.processexecution.executionlog.ProcessExecutionLogHistory;
import nts.uk.ctx.at.function.dom.processexecution.executionlog.ProcessExecutionLogManage;
import nts.uk.ctx.at.function.dom.processexecution.executionlog.ProcessExecutionTask;
import nts.uk.ctx.at.function.dom.processexecution.personalschedule.CreateScheduleYear;
import nts.uk.ctx.at.function.dom.processexecution.personalschedule.PersonalScheduleCreationPeriod;
import nts.uk.ctx.at.function.dom.processexecution.personalschedule.TargetClassification;
import nts.uk.ctx.at.function.dom.processexecution.personalschedule.TargetMonth;
import nts.uk.ctx.at.function.dom.processexecution.personalschedule.TargetSetting;
import nts.uk.ctx.at.function.dom.processexecution.repository.ExecutionTaskLogRepository;
import nts.uk.ctx.at.function.dom.processexecution.repository.ExecutionTaskSettingRepository;
import nts.uk.ctx.at.function.dom.processexecution.repository.LastExecDateTimeRepository;
import nts.uk.ctx.at.function.dom.processexecution.repository.ProcessExecutionLogHistRepository;
import nts.uk.ctx.at.function.dom.processexecution.repository.ProcessExecutionLogManageRepository;
import nts.uk.ctx.at.function.dom.processexecution.repository.ProcessExecutionLogRepository;
import nts.uk.ctx.at.function.dom.processexecution.repository.ProcessExecutionRepository;
import nts.uk.ctx.at.function.dom.processexecution.tasksetting.ExecutionTaskSetting;
import nts.uk.ctx.at.record.dom.adapter.company.AffComHistItemImport;
import nts.uk.ctx.at.record.dom.adapter.company.SyCompanyRecordAdapter;
import nts.uk.ctx.at.record.dom.dailyperformanceformat.businesstype.BusinessTypeOfEmployeeHistory;
import nts.uk.ctx.at.record.dom.dailyperformanceformat.businesstype.repository.BusinessTypeEmpOfHistoryRepository;
import nts.uk.ctx.at.record.dom.dailyperformanceprocessing.repository.CreateDailyResultDomainServiceImpl.ProcessState;
import nts.uk.ctx.at.record.dom.dailyperformanceprocessing.repository.CreateDailyResultEmployeeDomainService;
import nts.uk.ctx.at.record.dom.dailyprocess.calc.DailyCalculationEmployeeService;
import nts.uk.ctx.at.record.dom.monthlyprocess.aggr.MonthlyAggregationEmployeeService;
import nts.uk.ctx.at.record.dom.workrecord.workperfor.dailymonthlyprocessing.CalExeSettingInfor;
import nts.uk.ctx.at.record.dom.workrecord.workperfor.dailymonthlyprocessing.EmpCalAndSumExeLog;
import nts.uk.ctx.at.record.dom.workrecord.workperfor.dailymonthlyprocessing.EmpCalAndSumExeLogRepository;
import nts.uk.ctx.at.record.dom.workrecord.workperfor.dailymonthlyprocessing.ErrMessageContent;
import nts.uk.ctx.at.record.dom.workrecord.workperfor.dailymonthlyprocessing.ErrMessageInfo;
import nts.uk.ctx.at.record.dom.workrecord.workperfor.dailymonthlyprocessing.ErrMessageInfoRepository;
import nts.uk.ctx.at.record.dom.workrecord.workperfor.dailymonthlyprocessing.ErrMessageResource;
import nts.uk.ctx.at.record.dom.workrecord.workperfor.dailymonthlyprocessing.ExecutionLog;
import nts.uk.ctx.at.record.dom.workrecord.workperfor.dailymonthlyprocessing.ExecutionLogRepository;
import nts.uk.ctx.at.record.dom.workrecord.workperfor.dailymonthlyprocessing.ExecutionTime;
import nts.uk.ctx.at.record.dom.workrecord.workperfor.dailymonthlyprocessing.ObjectPeriod;
import nts.uk.ctx.at.record.dom.workrecord.workperfor.dailymonthlyprocessing.SetInforReflAprResult;
import nts.uk.ctx.at.record.dom.workrecord.workperfor.dailymonthlyprocessing.SettingInforForDailyCreation;
import nts.uk.ctx.at.record.dom.workrecord.workperfor.dailymonthlyprocessing.enums.CalAndAggClassification;
import nts.uk.ctx.at.record.dom.workrecord.workperfor.dailymonthlyprocessing.enums.DailyRecreateClassification;
import nts.uk.ctx.at.record.dom.workrecord.workperfor.dailymonthlyprocessing.enums.ErrorPresent;
import nts.uk.ctx.at.record.dom.workrecord.workperfor.dailymonthlyprocessing.enums.ExeStateOfCalAndSum;
import nts.uk.ctx.at.record.dom.workrecord.workperfor.dailymonthlyprocessing.enums.ExecutedMenu;
import nts.uk.ctx.at.record.dom.workrecord.workperfor.dailymonthlyprocessing.enums.ExecutionContent;
import nts.uk.ctx.at.record.dom.workrecord.workperfor.dailymonthlyprocessing.enums.ExecutionStatus;
import nts.uk.ctx.at.record.dom.workrecord.workperfor.dailymonthlyprocessing.enums.ExecutionType;
import nts.uk.ctx.at.schedule.app.command.executionlog.ScheduleCreatorExecutionCommand;
import nts.uk.ctx.at.schedule.app.command.executionlog.ScheduleCreatorExecutionCommandHandler;
import nts.uk.ctx.at.schedule.app.command.executionlog.internal.ScheCreExeErrorLogHandler;
import nts.uk.ctx.at.schedule.app.command.executionlog.internal.ScheduleErrorLogGeterCommand;
import nts.uk.ctx.at.schedule.dom.executionlog.CompletionStatus;
import nts.uk.ctx.at.schedule.dom.executionlog.CreateMethodAtr;
import nts.uk.ctx.at.schedule.dom.executionlog.ExecutionAtr;
import nts.uk.ctx.at.schedule.dom.executionlog.ImplementAtr;
import nts.uk.ctx.at.schedule.dom.executionlog.ProcessExecutionAtr;
import nts.uk.ctx.at.schedule.dom.executionlog.ReCreateAtr;
import nts.uk.ctx.at.schedule.dom.executionlog.ReCreateContent;
import nts.uk.ctx.at.schedule.dom.executionlog.RebuildTargetAtr;
import nts.uk.ctx.at.schedule.dom.executionlog.RebuildTargetDetailsAtr;
import nts.uk.ctx.at.schedule.dom.executionlog.ResetAtr;
import nts.uk.ctx.at.schedule.dom.executionlog.ScheduleCreateContent;
import nts.uk.ctx.at.schedule.dom.executionlog.ScheduleErrorLog;
import nts.uk.ctx.at.schedule.dom.executionlog.ScheduleExecutionLog;
import nts.uk.ctx.at.schedule.dom.executionlog.ScheduleExecutionLogRepository;
import nts.uk.ctx.at.schedule.dom.schedule.basicschedule.BasicScheduleRepository;
import nts.uk.ctx.at.shared.app.service.workrule.closure.ClosureEmploymentService;
import nts.uk.ctx.at.shared.dom.common.CompanyId;
import nts.uk.ctx.at.shared.dom.workrule.closure.Closure;
import nts.uk.ctx.at.shared.dom.workrule.closure.ClosureEmployment;
import nts.uk.ctx.at.shared.dom.workrule.closure.ClosureEmploymentRepository;
import nts.uk.ctx.at.shared.dom.workrule.closure.ClosureHistory;
import nts.uk.ctx.at.shared.dom.workrule.closure.ClosureRepository;
import nts.uk.ctx.at.shared.dom.workrule.closure.CurrentMonth;
import nts.uk.ctx.at.shared.dom.workrule.closure.UseClassification;
import nts.uk.ctx.at.shared.dom.workrule.closure.service.ClosureService;
import nts.uk.shr.com.context.AppContexts;
import nts.uk.shr.com.context.LoginUserContext;
import nts.uk.shr.com.history.DateHistoryItem;
import nts.uk.shr.com.i18n.TextResource;
import nts.uk.shr.com.task.schedule.UkJobScheduler;
import nts.uk.shr.com.time.calendar.period.DatePeriod;

@Stateless
public class ExecuteProcessExecutionAutoCommandHandler extends AsyncCommandHandler<ExecuteProcessExecutionCommand> {

	@Inject
	private ProcessExecutionRepository procExecRepo;

	@Inject
	private ExecutionTaskSettingRepository execSettingRepo;

	@Inject
	private ProcessExecutionLogRepository procExecLogRepo;

	@Inject
	private ProcessExecutionLogHistRepository procExecLogHistRepo;

	@Inject
	private ExecutionTaskLogRepository execTaskLogRepo;

	@Inject
	private LastExecDateTimeRepository lastExecDateTimeRepo;

	@Inject
	private EmpCalAndSumExeLogRepository empCalSumRepo;

	@Inject
	private ClosureRepository closureRepo;

	/** The Closure service. */
	@Inject
	private ClosureService closureService;

	@Inject
	private ClosureEmploymentRepository closureEmpRepo;

	@Inject
	private ScheduleCreatorExecutionCommandHandler scheduleExecution;

	@Inject
	private ScheduleExecutionLogRepository scheduleExecutionLogRepository;

	@Inject
	private WorkplaceWorkRecordAdapter workplaceAdapter;

	@Inject
	private BusinessTypeEmpOfHistoryRepository typeEmployeeOfHistoryRepos;

	@Inject
	private ExecutionLogRepository executionLogRepository;

	@Inject
	private ErrMessageInfoRepository errMessageInfoRepository;
	@Inject
	private RegulationInfoEmployeeAdapter regulationInfoEmployeeAdapter;
	@Inject
	private MonthlyAggregationEmployeeService monthlyService;
	@Inject
	private ClosureEmploymentService closureEmploymentService;
	@Inject
	private ProcessExecutionLogManageRepository processExecLogManaRepo;
	@Inject
	private CreateDailyResultEmployeeDomainService createDailyService;
	@Inject
	private DailyCalculationEmployeeService dailyCalculationEmployeeService;
	@Inject
	private SyCompanyRecordAdapter syCompanyRecordAdapter;
	@Inject
	private UkJobScheduler scheduler;
	@Inject
	private WorkplaceWorkRecordAdapter workplaceWorkRecordAdapter;

	@Inject
	private ScheCreExeErrorLogHandler scheCreExeErrorLogHandler;

	// requestList477
	@Inject
	private ExecutionLogAdapterFn executionLogAdapterFn;

	// request list 526
	@Inject
	private EmployeeManageAdapter employeeManageAdapter;

	@Inject
	private AppReflectManagerAdapter appReflectManagerAdapter;

	@Inject
	private ManagedParallelWithContext managedParallelWithContext;
	
	public static int MAX_DELAY_PARALLEL = 0;

	/**
	 * 更新処理を開始する
	 * 
	 * @param execType
	 *            実行タイプ
	 * @param execId
	 *            実行ID
	 * @param execItemCd
	 *            更新処理自動実行項目コード
	 * @param companyId
	 *            会社ID
	 */
	// 実行処理
	@Override
	public void handle(CommandHandlerContext<ExecuteProcessExecutionCommand> context) {
		//val asyncContext = context.asAsync();
		ExecuteProcessExecutionCommand command = context.getCommand();
		String execItemCd = command.getExecItemCd();
		String companyId = command.getCompanyId();
		// String execId = command.getExecId();
		// vi ExecuteProcessExecCommandHandler dang loi nen dung tam random execId
		String execId = IdentifierUtil.randomUniqueId();
		int execType = command.getExecType();
		// ドメインモデル「更新処理自動実行」を取得する
		ProcessExecution procExec = null;
		Optional<ProcessExecution> procExecOpt = this.procExecRepo.getProcessExecutionByCidAndExecCd(companyId,
				execItemCd);
		if (procExecOpt.isPresent()) {
			procExec = procExecOpt.get();
		}
		// ドメインモデル「実行タスク設定」を取得する
		ExecutionTaskSetting execSetting = null;
		if (execType == 0) {
			Optional<ExecutionTaskSetting> execSettingOpt = this.execSettingRepo.getByCidAndExecCd(companyId,
					execItemCd);
			if (execSettingOpt.isPresent()) {
				execSetting = execSettingOpt.get();
			}
		}

		// ドメインモデル「更新処理自動実行管理」を取得する NO.4
		ProcessExecutionLogManage processExecutionLogManage = null;
		Optional<ProcessExecutionLogManage> logByCIdAndExecCdOpt = this.processExecLogManaRepo
				.getLogByCIdAndExecCd(companyId, execItemCd);
		if (logByCIdAndExecCdOpt.isPresent()) {
			processExecutionLogManage = logByCIdAndExecCdOpt.get();
		}

		/*
		 * // ドメインモデル「更新処理自動実行ログ」を取得する ProcessExecutionLog procExecLog = null;
		 * Optional<ProcessExecutionLog> procExecLogOpt =
		 * this.procExecLogRepo.getLogByCIdAndExecCd(companyId, execItemCd, execId); if
		 * (procExecLogOpt.isPresent()) { procExecLog = procExecLogOpt.get(); }
		 * 
		 */

		// ドメインモデル「更新処理前回実行日時」を取得する
		LastExecDateTime lastExecDateTime = null;
		Optional<LastExecDateTime> lastDateTimeOpt = lastExecDateTimeRepo.get(procExec.getCompanyId(),
				procExec.getExecItemCd().v());
		if (lastDateTimeOpt.isPresent()) {
			lastExecDateTime = lastDateTimeOpt.get();
		}
		if (execType == 0) {
			// ドメインモデルの取得結果をチェックする
			if (procExec == null || execSetting == null || processExecutionLogManage == null
					|| lastExecDateTime == null) {
				return;
			}
		} else {
			if (procExec == null || processExecutionLogManage == null || lastExecDateTime == null) {
				return;
			}
		}
		// アルゴリズム「就業計算と集計実行ログ作成判定処理」を実行する
		// ・ドメインモデル「更新処理自動実行.実行設定.日別実績の作成・計算.日別実績の作成・計算区分」
		//boolean dailyPerfCls = procExec.getExecSetting().getDailyPerf().isDailyPerfCls();
		// ・ドメインモデル「更新処理自動実行.実行設定.承認結果反映」
		//boolean reflectResultCls = procExec.getExecSetting().isReflectResultCls();
		// ・ドメインモデル「更新処理自動実行.実行設定.承認結果反映」
		//boolean monthlyAggCls = procExec.getExecSetting().isMonthlyAggCls();
		EmpCalAndSumExeLog empCalAndSumExeLog = null;
		// if (dailyPerfCls || reflectResultCls || monthlyAggCls) {
		// ドメインモデル「就業計算と集計実行ログ」を追加する
		empCalAndSumExeLog = new EmpCalAndSumExeLog(execId, command.getCompanyId(),
				new YearMonth(GeneralDate.today().year() * 100 + 1), ExecutedMenu.SELECT_AND_RUN, GeneralDateTime.now(),
				null, AppContexts.user().employeeId(), 1, IdentifierUtil.randomUniqueId(),
				CalAndAggClassification.AUTOMATIC_EXECUTION);
		this.empCalSumRepo.add(empCalAndSumExeLog);
		// }

		// アルゴリズム「実行前登録処理」を実行する
		// 実行前登録処理
		ProcessExecutionLog procExecLog = this.preExecutionRegistrationProcessing(companyId, execItemCd, execId,
				processExecutionLogManage, execType);

		/*
		 * /* ドメインモデル「更新処理自動実行ログ」を更新する 現在の実行状態 ＝ 実行 全体の終了状態 ＝ NULL
		 * 
		 * procExecLog.setCurrentStatus(CurrentExecutionStatus.RUNNING);
		 * procExecLog.setOverallStatus(null); this.procExecLogRepo.update(procExecLog);
		 */

		/*
		 * 各処理を実行する 【パラメータ】 実行ID ＝ 取得した実行ID
		 * 取得したドメインモデル「更新処理自動実行」、「実行タスク設定」、「更新処理自動実行ログ」の情報
		 */
		this.doProcesses(context, empCalAndSumExeLog, execId, procExec, procExecLog, companyId);

		processExecutionLogManage = this.processExecLogManaRepo.getLogByCIdAndExecCd(companyId, execItemCd).get();
		// アルゴリズム「自動実行登録処理」を実行する
		this.updateDomains(execItemCd, execType, companyId, execId, execSetting, procExecLog, lastExecDateTime,
				processExecutionLogManage);
	}

	private void updateDomains(String execItemCd, int execType, String companyId, String execId,
			ExecutionTaskSetting execSetting, ProcessExecutionLog procExecLog, LastExecDateTime lastExecDateTime,
			ProcessExecutionLogManage processExecutionLogManage) {

		// ドメインモデル「更新処理自動実行ログ履歴」を取得する
		Optional<ProcessExecutionLogHistory> processExecutionLogHistory = procExecLogHistRepo.getByExecId(companyId,
				execItemCd, execId);
		if (!processExecutionLogHistory.isPresent()) {
			// ドメインモデル「更新処理自動実行管理」を更新する
			if (!this.isAbnormalTermEachTask(procExecLog) && (processExecutionLogManage.getOverallStatus() == null
					|| !processExecutionLogManage.getOverallStatus().isPresent())) {
				processExecutionLogManage.setOverallStatus(EndStatus.SUCCESS);
			} else if (this.isAbnormalTermEachTask(procExecLog)) {
				processExecutionLogManage.setOverallStatus(EndStatus.ABNORMAL_END);
			}
			processExecutionLogManage.setCurrentStatus(CurrentExecutionStatus.WAITING);
			this.processExecLogManaRepo.update(processExecutionLogManage);

			List<ExecutionTaskLog> taskLogList = this.execTaskLogRepo.getAllByCidExecCdExecId(companyId, execItemCd,
					execId);
			if (CollectionUtil.isEmpty(taskLogList)) {
				this.execTaskLogRepo.insertAll(companyId, execItemCd, execId, procExecLog.getTaskLogList());
			} else {
				this.execTaskLogRepo.updateAll(companyId, execItemCd, execId, procExecLog.getTaskLogList());
			}

			/*
			 * ドメインモデル「更新処理自動実行ログ」を更新する
			 * 
			 * 【実行タイプ ＝ 1（即時実行）の場合】 前回実行日時 ＝ システム日時 前回実行日時（即時実行を含めない） ＝ システム日時
			 * 
			 * 【実行タイプ ＝ 0（開始時刻）の場合】 前回実行日時 ＝ システム日時
			 * 
			 * if (execType == 1) { procExecLog.setLastExecDateTime(GeneralDateTime.now());
			 * procExecLog.setLastExecDateTimeEx(GeneralDateTime.now()); } else if (execType
			 * == 0) { procExecLog.setLastExecDateTime(GeneralDateTime.now()); }
			 * this.procExecLogRepo.update(procExecLog);
			 * 
			 * 
			 * ドメインモデル「更新処理自動実行ログ履歴」を新規登録する
			 * 
			 * 会社ID ＝ 更新処理自動実行ログ.会社ID 実行ID ＝ 取得した実行ID コード ＝ 更新処理自動実行ログ.コード 前回実行日時 ＝
			 * 更新処理自動実行ログ.前回実行日時 各処理の終了状態(List) ＝ 更新処理自動実行ログ.各処理の終了状態(List) 全体の終了状態 ＝
			 * 更新処理自動実行ログ.全体の終了状態 全体のエラー詳細 ＝ 更新処理自動実行ログ.全体のエラー詳細 各処理の期間 ＝ 更新処理自動実行ログ.各処理の期間
			 */

			this.procExecLogHistRepo.insert(new ProcessExecutionLogHistory(new ExecutionCode(execItemCd), companyId,
					processExecutionLogManage.getOverallError(),
					(processExecutionLogManage.getOverallStatus() != null
							&& processExecutionLogManage.getOverallStatus().isPresent())
									? processExecutionLogManage.getOverallStatus().get()
									: null,
					processExecutionLogManage.getLastExecDateTime(),
					(procExecLog.getEachProcPeriod() != null && procExecLog.getEachProcPeriod().isPresent())
							? procExecLog.getEachProcPeriod().get()
							: null,
					procExecLog.getTaskLogList(), execId));
		} else {
			// Optional<EmpCalAndSumExeLog> empCalAndSumExeLog =
			// this.empCalSumRepo.getByEmpCalAndSumExecLogID(execId);

			// ドメインモデル「就業計算と集計実行ログ」を更新する
			this.empCalSumRepo.updateStatus(execId, ExeStateOfCalAndSum.STOPPING.value);

		}
		// パラメータ.実行タイプのチェック
		if (execType == 1) {
			return;
		}

		/*
		 * ドメインモデル「実行タスク設定」を更新する
		 * 
		 * 次回実行日時 ＝ 次回実行日時を作成する。 ※補足資料⑤参照
		 */
		if (execSetting != null) {
			// execSetting.setNextExecDateTime();
			String scheduleId = execSetting.getScheduleId();
			Optional<GeneralDateTime> nextFireTime = this.scheduler.getNextFireTime(scheduleId);
			execSetting.setNextExecDateTime(nextFireTime);
			this.execSettingRepo.update(execSetting);
		}

		/*
		 * ドメインモデル「更新処理前回実行日時」を更新する 前回実行日時 ＝ システム日時
		 */
		lastExecDateTime.setLastExecDateTime(GeneralDateTime.now());
		this.lastExecDateTimeRepo.update(lastExecDateTime);
	}

	@Inject
	private AppRouteUpdateDailyService appRouteUpdateDailyService;

	@Inject
	private AppRouteUpdateMonthlyService appRouteUpdateMonthlyService;

	/**
	 * 各処理を実行する
	 * 
	 * @param execId
	 *            実行ID
	 * @param procExec
	 *            更新処理自動実行
	 * @param execSetting
	 *            実行タスク設定
	 * @param procExecLog
	 *            更新処理自動実行ログ
	 */
	// true interrupt false non interrupt
	// 各処理の分岐
	private boolean doProcesses(CommandHandlerContext<ExecuteProcessExecutionCommand> context,
			EmpCalAndSumExeLog empCalAndSumExeLog, String execId, ProcessExecution procExec,
			ProcessExecutionLog procExecLog, String companyId) {
		// Initialize status [未実施] for each task
		initAllTaskStatus(procExecLog, EndStatus.NOT_IMPLEMENT);
		/*
		 * スケジュールの作成 【パラメータ】 実行ID 取得したドメインモデル「更新処理自動実行」、「実行タスク設定」、「更新処理自動実行ログ」の情報
		 */
		if (!this.createSchedule(context, execId, procExec, procExecLog)) {
			// ドメインモデル「更新処理自動実行ログ」を更新する
			// 各処理の終了状態 ＝ [スケジュールの作成、強制終了]
			if(procExec.getProcessExecType() == ProcessExecType.RE_CREATE) {
				this.updateEachTaskStatus(procExecLog, ProcessExecutionTask.SCH_CREATION, EndStatus.ABNORMAL_END);
			}else {
				this.updateEachTaskStatus(procExecLog, ProcessExecutionTask.SCH_CREATION, EndStatus.FORCE_END);
			}
			// 各処理の終了状態 ＝ [日別作成、未実施]
			this.updateEachTaskStatus(procExecLog, ProcessExecutionTask.DAILY_CREATION, EndStatus.NOT_IMPLEMENT);
			// 各処理の終了状態 ＝ [日別計算、未実施]
			this.updateEachTaskStatus(procExecLog, ProcessExecutionTask.DAILY_CALCULATION, EndStatus.NOT_IMPLEMENT);
			// 各処理の終了状態 ＝ [承認結果反映、未実施]
			this.updateEachTaskStatus(procExecLog, ProcessExecutionTask.RFL_APR_RESULT, EndStatus.NOT_IMPLEMENT);
			// 各処理の終了状態 ＝ [月別集計、未実施]
			this.updateEachTaskStatus(procExecLog, ProcessExecutionTask.MONTHLY_AGGR, EndStatus.NOT_IMPLEMENT);
			// 各処理の終了状態 ＝ [アラーム抽出、未実施]
			this.updateEachTaskStatus(procExecLog, ProcessExecutionTask.AL_EXTRACTION, EndStatus.NOT_IMPLEMENT);
			// 各処理の終了状態 ＝ [承認ルート更新（日次）、未実施]
			this.updateEachTaskStatus(procExecLog, ProcessExecutionTask.APP_ROUTE_U_DAI, EndStatus.NOT_IMPLEMENT);
			// 各処理の終了状態 ＝ [承認ルート更新（月次）、未実施]
			this.updateEachTaskStatus(procExecLog, ProcessExecutionTask.APP_ROUTE_U_MON, EndStatus.NOT_IMPLEMENT);
			return true;
		} else if (!this.createDailyData(context, empCalAndSumExeLog, execId, procExec, procExecLog)) {
			// ドメインモデル「更新処理自動実行ログ」を更新する
			// 各処理の終了状態 ＝ [日別作成、強制終了]
			this.updateEachTaskStatus(procExecLog, ProcessExecutionTask.DAILY_CREATION, EndStatus.FORCE_END);
			// 各処理の終了状態 ＝ [日別計算、強制終了]
			this.updateEachTaskStatus(procExecLog, ProcessExecutionTask.DAILY_CALCULATION, EndStatus.FORCE_END);
			// 各処理の終了状態 ＝ [承認結果反映、未実施]
			this.updateEachTaskStatus(procExecLog, ProcessExecutionTask.RFL_APR_RESULT, EndStatus.NOT_IMPLEMENT);
			// 各処理の終了状態 ＝ [月別集計、未実施]
			this.updateEachTaskStatus(procExecLog, ProcessExecutionTask.MONTHLY_AGGR, EndStatus.NOT_IMPLEMENT);
			// 各処理の終了状態 ＝ [アラーム抽出、未実施]
			this.updateEachTaskStatus(procExecLog, ProcessExecutionTask.AL_EXTRACTION, EndStatus.NOT_IMPLEMENT);
			// 各処理の終了状態 ＝ [承認ルート更新（日次）、未実施]
			this.updateEachTaskStatus(procExecLog, ProcessExecutionTask.APP_ROUTE_U_DAI, EndStatus.NOT_IMPLEMENT);
			// 各処理の終了状態 ＝ [承認ルート更新（月次）、未実施]
			this.updateEachTaskStatus(procExecLog, ProcessExecutionTask.APP_ROUTE_U_MON, EndStatus.NOT_IMPLEMENT);
			return true;
		} else if (this.reflectApprovalResult(execId, procExec, procExecLog, companyId)) {
			// ドメインモデル「更新処理自動実行ログ」を更新する
			// 各処理の終了状態 ＝ [承認結果反映、強制終了]
			this.updateEachTaskStatus(procExecLog, ProcessExecutionTask.RFL_APR_RESULT, EndStatus.FORCE_END);
			// 各処理の終了状態 ＝ [月別集計、未実施]
			this.updateEachTaskStatus(procExecLog, ProcessExecutionTask.MONTHLY_AGGR, EndStatus.NOT_IMPLEMENT);
			// 各処理の終了状態 ＝ [アラーム抽出、未実施]
			this.updateEachTaskStatus(procExecLog, ProcessExecutionTask.AL_EXTRACTION, EndStatus.NOT_IMPLEMENT);
			// 各処理の終了状態 ＝ [承認ルート更新（日次）、未実施]
			this.updateEachTaskStatus(procExecLog, ProcessExecutionTask.APP_ROUTE_U_DAI, EndStatus.NOT_IMPLEMENT);
			// 各処理の終了状態 ＝ [承認ルート更新（月次）、未実施]
			this.updateEachTaskStatus(procExecLog, ProcessExecutionTask.APP_ROUTE_U_MON, EndStatus.NOT_IMPLEMENT);
			return true;
		} else if (this.monthlyAggregation(execId, procExec, procExecLog, companyId, context)) {
			// 各処理の終了状態 ＝ [月別集計、強制終了]
			this.updateEachTaskStatus(procExecLog, ProcessExecutionTask.MONTHLY_AGGR, EndStatus.FORCE_END);
			// 各処理の終了状態 ＝ [アラーム抽出、未実施]
			this.updateEachTaskStatus(procExecLog, ProcessExecutionTask.AL_EXTRACTION, EndStatus.NOT_IMPLEMENT);
			// 各処理の終了状態 ＝ [承認ルート更新（日次）、未実施]
			this.updateEachTaskStatus(procExecLog, ProcessExecutionTask.APP_ROUTE_U_DAI, EndStatus.NOT_IMPLEMENT);
			// 各処理の終了状態 ＝ [承認ルート更新（月次）、未実施]
			this.updateEachTaskStatus(procExecLog, ProcessExecutionTask.APP_ROUTE_U_MON, EndStatus.NOT_IMPLEMENT);
			return true;
		} else if (this.alarmExtraction(execId, procExec, procExecLog, companyId, context)) {
			// 各処理の終了状態 ＝ [アラーム抽出、未実施]
			this.updateEachTaskStatus(procExecLog, ProcessExecutionTask.AL_EXTRACTION, EndStatus.FORCE_END);
			// 各処理の終了状態 ＝ [承認ルート更新（日次）、未実施]
			this.updateEachTaskStatus(procExecLog, ProcessExecutionTask.APP_ROUTE_U_DAI, EndStatus.NOT_IMPLEMENT);
			// 各処理の終了状態 ＝ [承認ルート更新（月次）、未実施]
			this.updateEachTaskStatus(procExecLog, ProcessExecutionTask.APP_ROUTE_U_MON, EndStatus.NOT_IMPLEMENT);
			return true;
		}

		// 承認ルート更新（日次）
		this.appRouteUpdateDailyService.checkAppRouteUpdateDaily(execId, procExec, procExecLog);
		// 承認ルート更新（月次）
		this.appRouteUpdateMonthlyService.checkAppRouteUpdateMonthly(execId, procExec, procExecLog);
		return false;
	}

	@Inject
	private BasicScheduleRepository basicScheduleRepository;

	/**
	 * スケジュールの作成
	 * 
	 * @param execId
	 *            実行ID
	 * @param procExec
	 *            更新処理自動実行
	 * @param execSetting
	 *            実行タスク設定
	 * @param procExecLog
	 *            更新処理自動実行ログ
	 */
	private boolean createSchedule(CommandHandlerContext<ExecuteProcessExecutionCommand> context, String execId,
			ProcessExecution procExec, ProcessExecutionLog procExecLog) {

		// Login user context
		LoginUserContext loginContext = AppContexts.user();
		// ドメインモデル「更新処理自動実行ログ」を更新する
		this.updateEachTaskStatus(procExecLog, ProcessExecutionTask.SCH_CREATION, null);
		String companyId = context.getCommand().getCompanyId();
		String execItemCd = context.getCommand().getExecItemCd();
		List<ExecutionTaskLog> taskLogList = this.execTaskLogRepo.getAllByCidExecCdExecId(companyId, execItemCd,
				execId);
		if (CollectionUtil.isEmpty(taskLogList)) {
			this.execTaskLogRepo.insertAll(companyId, execItemCd, execId, procExecLog.getTaskLogList());
		} else {
			this.execTaskLogRepo.updateAll(companyId, execItemCd, execId, procExecLog.getTaskLogList());
		}
		this.procExecLogRepo.update(procExecLog);
		//AsyncTaskInfo handle = null;
		boolean isException = true;
		// 就業担当者の社員ID（List）を取得する : RQ526
		List<String> listManagementId = employeeManageAdapter.getListEmpID(companyId, GeneralDate.today());
		try {
			// 個人スケジュール作成区分の判定
			if (!procExec.getExecSetting().getPerSchedule().isPerSchedule()) {
				this.updateEachTaskStatus(procExecLog, ProcessExecutionTask.SCH_CREATION, EndStatus.NOT_IMPLEMENT);
				this.procExecLogRepo.update(procExecLog);
				return true;
			}
			//新入社員作成区分（Boolean）←属性「新入社員を作成」
			boolean checkCreateEmployee = procExec.getExecSetting().getPerSchedule().getTarget().getTargetSetting().isCreateEmployee();
			// 期間の計算
			DatePeriod calculateSchedulePeriod = this.calculateSchedulePeriod(procExec, procExecLog,checkCreateEmployee);

			/*
			 * 対象社員を取得 TODO
			 */
			// List<String> sidList = new ArrayList<>();
			// sidList.add(loginContext.employeeId()); // Add login SID to test, remove when
			// implement this algorithm

			// <<Public>> 就業条件で社員を検索して並び替える
			RegulationInfoEmployeeAdapterImport regulationInfoEmployeeAdapterImport = new RegulationInfoEmployeeAdapterImport();
			if (procExec.getExecScope().getExecScopeCls().value == 1) {
				// 【更新処理自動実行.実行範囲.実行範囲区分 ＝ 職場 の場合】
				// 基準日 → システム日付
				regulationInfoEmployeeAdapterImport.setBaseDate(GeneralDateTime.now());
				// 検索参照範囲 → 参照範囲を考慮しない
				regulationInfoEmployeeAdapterImport.setReferenceRange(3);
				// 雇用で絞り込む → FAlSE
				regulationInfoEmployeeAdapterImport.setFilterByEmployment(false);
				// 雇用コード一覧 → なし
				regulationInfoEmployeeAdapterImport.setEmploymentCodes(null);
				// 部門で絞り込む → FALSE
				regulationInfoEmployeeAdapterImport.setFilterByDepartment(false);
				// 部門ID一覧 → なし
				regulationInfoEmployeeAdapterImport.setDepartmentCodes(null);
				// 職場で絞り込む → TRUE
				regulationInfoEmployeeAdapterImport.setFilterByWorkplace(true);

				List<ProcessExecutionScopeItem> workplaceIdList = procExec.getExecScope().getWorkplaceIdList();
				List<String> workplaceIds = new ArrayList<String>();
				workplaceIdList.forEach(x -> {
					workplaceIds.add(x.getWkpId());
				});
				// 職場ID一覧 → ドメインモデル「更新処理自動実行」に登録されている職場ID
				regulationInfoEmployeeAdapterImport.setWorkplaceCodes(workplaceIds);
				// 分類で絞り込む → FALSE
				regulationInfoEmployeeAdapterImport.setFilterByClassification(false);
				// 分類コード一覧 → なし
				regulationInfoEmployeeAdapterImport.setClassificationCodes(null);
				// 職位で絞り込む → FALSE
				regulationInfoEmployeeAdapterImport.setFilterByJobTitle(false);
				// 職位ID一覧 → なし
				regulationInfoEmployeeAdapterImport.setJobTitleCodes(null);
				// 在職・休職・休業のチェック期間 → 作成した期間
				regulationInfoEmployeeAdapterImport.setPeriodStart(calculateSchedulePeriod.start());
				regulationInfoEmployeeAdapterImport.setPeriodEnd(calculateSchedulePeriod.end());
				// 在職者を含める → TRUE
				regulationInfoEmployeeAdapterImport.setIncludeIncumbents(true);
				// 休職者を含める → FALSE
				regulationInfoEmployeeAdapterImport.setIncludeWorkersOnLeave(false);
				// 休業者を含める → FALSE
				regulationInfoEmployeeAdapterImport.setIncludeOccupancy(false);
				// 出向に来ている社員を含める → TRUE
				regulationInfoEmployeeAdapterImport.setIncludeAreOnLoan(true);
				// 出向に行っている社員を含める → FALSE
				regulationInfoEmployeeAdapterImport.setIncludeGoingOnLoan(false);
				// 退職者を含める → FALSE
				regulationInfoEmployeeAdapterImport.setIncludeRetirees(false);
				// 退職日のチェック期間 → 作成した期間
				regulationInfoEmployeeAdapterImport.setRetireStart(calculateSchedulePeriod.start());
				regulationInfoEmployeeAdapterImport.setRetireEnd(calculateSchedulePeriod.end());
				// 並び順NO → 1
				regulationInfoEmployeeAdapterImport.setSortOrderNo(1);
				// 氏名の種類 → ビジネスネーム日本語
				regulationInfoEmployeeAdapterImport.setNameType("ビジネスネーム日本語");

				regulationInfoEmployeeAdapterImport.setSystemType(2);
				// 勤務種別で絞り込む → FALSE
				regulationInfoEmployeeAdapterImport.setFilterByWorktype(false);
				// 勤務種別コード一覧 → 空
				regulationInfoEmployeeAdapterImport.setWorktypeCodes(new ArrayList<String>());

				// 就業締めで絞り込む → FALSE
				regulationInfoEmployeeAdapterImport.setFilterByClosure(false);

			} else {
				// 【更新処理自動実行.実行範囲.実行範囲区分 ＝ 会社 の場合】
				// 基準日 → システム日付
				regulationInfoEmployeeAdapterImport.setBaseDate(GeneralDateTime.now());
				// 検索参照範囲 → 参照範囲を考慮しない
				regulationInfoEmployeeAdapterImport.setReferenceRange(3);
				// 雇用で絞り込む → FALSE
				regulationInfoEmployeeAdapterImport.setFilterByEmployment(false);
				// 雇用コード一覧 → なし
				regulationInfoEmployeeAdapterImport.setEmploymentCodes(null);
				// 部門で絞り込む → FALSE
				regulationInfoEmployeeAdapterImport.setFilterByDepartment(false);
				// 部門ID一覧 → なし
				regulationInfoEmployeeAdapterImport.setDepartmentCodes(null);
				// 職場で絞り込む → FALSE
				regulationInfoEmployeeAdapterImport.setFilterByWorkplace(false);
				// 職場ID一覧 → なし
				regulationInfoEmployeeAdapterImport.setWorkplaceCodes(null);
				// 分類で絞り込む → FALSE
				regulationInfoEmployeeAdapterImport.setFilterByClassification(false);
				// 分類コード一覧 → なし
				regulationInfoEmployeeAdapterImport.setClassificationCodes(null);
				// 職位で絞り込む → FALSE
				regulationInfoEmployeeAdapterImport.setFilterByJobTitle(false);
				// 職位ID一覧 → なし
				regulationInfoEmployeeAdapterImport.setJobTitleCodes(null);
				// 在職・休職・休業のチェック期間 → 作成した期間
				regulationInfoEmployeeAdapterImport.setPeriodStart(calculateSchedulePeriod.start());
				regulationInfoEmployeeAdapterImport.setPeriodEnd(calculateSchedulePeriod.end());
				// 在職者を含める → TRUE
				regulationInfoEmployeeAdapterImport.setIncludeIncumbents(true);
				// 休職者を含める → FALSE
				regulationInfoEmployeeAdapterImport.setIncludeWorkersOnLeave(false);
				// 休業者を含める → FALSE
				regulationInfoEmployeeAdapterImport.setIncludeOccupancy(false);
				// 出向に来ている社員を含める → TRUE
				regulationInfoEmployeeAdapterImport.setIncludeAreOnLoan(true);
				// 出向に行っている社員を含める → FALSE
				regulationInfoEmployeeAdapterImport.setIncludeGoingOnLoan(false);
				// 退職者を含める → FALSE
				regulationInfoEmployeeAdapterImport.setIncludeRetirees(false);
				// 退職日のチェック期間 → 作成した期間
				regulationInfoEmployeeAdapterImport.setRetireStart(calculateSchedulePeriod.start());
				regulationInfoEmployeeAdapterImport.setRetireEnd(calculateSchedulePeriod.end());
				// 並び順NO → 1
				regulationInfoEmployeeAdapterImport.setSortOrderNo(1);
				// 氏名の種類 → ビジネスネーム日本語
				regulationInfoEmployeeAdapterImport.setNameType("ビジネスネーム日本語");

				regulationInfoEmployeeAdapterImport.setSystemType(2);
				// 勤務種別で絞り込む → FALSE
				regulationInfoEmployeeAdapterImport.setFilterByWorktype(false);
				// 勤務種別コード一覧 → 空
				regulationInfoEmployeeAdapterImport.setWorktypeCodes(new ArrayList<String>());

				// 就業締めで絞り込む → FALSE
				regulationInfoEmployeeAdapterImport.setFilterByClosure(false);
			}
			// <<Public>> 就業条件で社員を検索して並び替える
			List<RegulationInfoEmployeeAdapterDto> lstRegulationInfoEmployee = this.regulationInfoEmployeeAdapter
					.find(regulationInfoEmployeeAdapterImport);

			// 5-社員ID（List
			List<String> empIds = new ArrayList<>();
			lstRegulationInfoEmployee.forEach(x -> {
				empIds.add(x.getEmployeeId());
			});

			/*
			 * 作成対象の判定
			 */
			// 全員の場合
			if (procExec.getExecSetting().getPerSchedule().getTarget()
					.getCreationTarget().value == TargetClassification.ALL.value) {
				// 対象社員を取得 - TODO

				ScheduleCreatorExecutionCommand scheduleCommand = getScheduleCreatorExecutionAllEmp(execId, procExec,
						loginContext, calculateSchedulePeriod, empIds);
				//handle = 
						this.scheduleExecution.handle(scheduleCommand);
			}
			// 異動者・新入社員のみ作成の場合
			else {
				DatePeriod period = procExecLog.getEachProcPeriod().get().getScheduleCreationPeriod().get();
				// ・社員ID（異動者、勤務種別変更者、休職者・休業者）（List）
				List<String> reEmployeeList = new ArrayList<>();
				// 社員ID（新入社員）（List）
				List<String> newEmployeeList = new ArrayList<>();
				// 社員ID（休職者・休業者）（List）
				List<String> temporaryEmployeeList = new ArrayList<>();
				// 対象社員を絞り込み
				this.filterEmployeeList(procExec, empIds, period, reEmployeeList, newEmployeeList,
						temporaryEmployeeList);
				if (!CollectionUtil.isEmpty(reEmployeeList) && !CollectionUtil.isEmpty(newEmployeeList)) {
				} else {
					// 社員ID（新入社員）（List）のみ
					if (!CollectionUtil.isEmpty(newEmployeeList) && !CollectionUtil.isEmpty(temporaryEmployeeList)) {
						ScheduleCreatorExecutionCommand scheduleCreatorExecutionOneEmp = this
								.getScheduleCreatorExecutionOneEmp(execId, procExec, loginContext,
										calculateSchedulePeriod, newEmployeeList);
						//handle = 
								this.scheduleExecution.handle(scheduleCreatorExecutionOneEmp);
						ScheduleCreatorExecutionCommand scheduleCreatorExecutionOneEmp3 = this
								.getScheduleCreatorExecutionOneEmp(execId, procExec, loginContext,
										calculateSchedulePeriod, temporaryEmployeeList);
						//handle = 
								this.scheduleExecution.handle(scheduleCreatorExecutionOneEmp3);
					}
					// 社員ID（異動者、勤務種別変更者）（List）のみ
					if (!CollectionUtil.isEmpty(reEmployeeList)) {
						// 異動者、勤務種別変更者、休職者・休業者の期間の計算
						GeneralDate endDate = basicScheduleRepository.findMaxDateByListSid(reEmployeeList);
						DatePeriod periodDate = this.getMinPeriodFromStartDate(companyId);
						ScheduleCreatorExecutionCommand scheduleCreatorExecutionOneEmp1 = this
								.getScheduleCreatorExecutionOneEmp(execId, procExec, loginContext,
										calculateSchedulePeriod, reEmployeeList);
						scheduleCreatorExecutionOneEmp1.getScheduleExecutionLog()
								.setPeriod(new DatePeriod(periodDate.start(), endDate));
						//handle = 
								this.scheduleExecution.handle(scheduleCreatorExecutionOneEmp1);
					}

				}
			}
		} catch (Exception e) {
			isException = false;
			// ドメインモデル「更新処理自動実行ログ」を更新する
			this.updateEachTaskStatus(procExecLog, ProcessExecutionTask.SCH_CREATION, EndStatus.ABNORMAL_END);
			ScheduleErrorLogGeterCommand scheduleErrorLogGeterCommand = new ScheduleErrorLogGeterCommand();
			scheduleErrorLogGeterCommand.setCompanyId(companyId);
			scheduleErrorLogGeterCommand.setExecutionId(execId);
			scheduleErrorLogGeterCommand.setToDate(GeneralDate.today());
			// ドメインモデル「スケジュール作成エラーログ」を取得する
			List<ScheduleErrorLog> listError = this.scheCreExeErrorLogHandler
					.getListError(scheduleErrorLogGeterCommand, AppContexts.user().employeeId());

			ExecutionLogImportFn param = new ExecutionLogImportFn();
			List<ExecutionLogErrorDetailFn> listErrorAndEmpId = new ArrayList<>();

			// 会社ID ＝ パラメータ.更新処理自動実行.会社ID
			param.setCompanyId(companyId);
			// 管理社員ID ＝
			param.setManagerId(listManagementId);
			// 実行完了日時 ＝ システム日時
			param.setFinishDateTime(GeneralDateTime.now());
			// エラーの有無 ＝ エラーあり
			param.setExistenceError(1);
			// 実行内容 ＝ スケジュール作成
			param.setExecutionContent(AlarmCategoryFn.CREATE_SCHEDULE);
			// 実行ログエラー詳細 ＝ 取得したエラーメッセージ情報（社員ID, エラーメッセージ ）（List）
			if (listError.isEmpty()) {
				this.scheCreExeErrorLogHandler.addError(scheduleErrorLogGeterCommand, "System", "Msg_1339");
				for (String managementId : listManagementId) {
					listErrorAndEmpId
							.add(new ExecutionLogErrorDetailFn(TextResource.localize("Msg_1339"), managementId));
				}
			} else {
				for (ScheduleErrorLog scheduleErrorLog : listError) {
					listErrorAndEmpId.add(new ExecutionLogErrorDetailFn(scheduleErrorLog.getErrorContent(),
							scheduleErrorLog.getEmployeeId()));
				}
			}
			param.setTargerEmployee(listErrorAndEmpId);
			// アルゴリズム「実行ログ登録」を実行する 2290 Done
			executionLogAdapterFn.updateExecuteLog(param);
		}
		int timeOut = 1;
		boolean isInterruption = false;
		if (isException) {
			while (true) {
				// find execution log by id
				Optional<ScheduleExecutionLog> domainOpt = this.scheduleExecutionLogRepository
						.findById(loginContext.companyId(), execId);
				if (domainOpt.isPresent()) {
					if (domainOpt.get().getCompletionStatus().value == CompletionStatus.COMPLETION_ERROR.value) {
						this.updateEachTaskStatus(procExecLog, ProcessExecutionTask.SCH_CREATION,
								EndStatus.ABNORMAL_END);
						break;
					}
					if (domainOpt.get().getCompletionStatus().value == CompletionStatus.INTERRUPTION.value) {
						isInterruption = true;
						break;
					}
					if (domainOpt.get().getCompletionStatus().value == CompletionStatus.DONE.value) {
						this.updateEachTaskStatus(procExecLog, ProcessExecutionTask.SCH_CREATION, EndStatus.SUCCESS);
						break;
					}

				}
				if (timeOut == 24) {
					this.updateEachTaskStatus(procExecLog, ProcessExecutionTask.SCH_CREATION, EndStatus.ABNORMAL_END);
					break;
				}
				timeOut++;
				try {
					Thread.sleep(10000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
		this.procExecLogRepo.update(procExecLog);
		ExecutionLogImportFn param = new ExecutionLogImportFn();
		List<ExecutionLogErrorDetailFn> listErrorAndEmpId = new ArrayList<>();
		// 会社ID ＝ パラメータ.更新処理自動実行.会社ID
		param.setCompanyId(companyId);
		// 管理社員ID ＝
		param.setManagerId(listManagementId);
		// 実行完了日時 ＝ システム日時
		param.setFinishDateTime(GeneralDateTime.now());
		// 実行内容 ＝ スケジュール作成
		param.setExecutionContent(AlarmCategoryFn.CREATE_SCHEDULE);
		// エラーの有無 ＝ エラーなし
		param.setExistenceError(0);
		// 実行ログエラー詳細 ＝ NULL
		param.setTargerEmployee(listErrorAndEmpId);
		// アルゴリズム「実行ログ登録」を実行する 2290 Done
		executionLogAdapterFn.updateExecuteLog(param);

		if (isInterruption) {
			return false;
		}
		
		boolean checkError = this.scheCreExeErrorLogHandler
				.checkExistErrorByExeId(execId);
		//再実行の場合にExceptionが発生したかどうかを確認する。
		if(checkError == true && procExec.getProcessExecType() == ProcessExecType.RE_CREATE) {
			return false;
		}
		return true;
	}

	private ScheduleCreatorExecutionCommand getScheduleCreatorExecutionAllEmp(String execId, ProcessExecution procExec,
			LoginUserContext loginContext, DatePeriod calculateSchedulePeriod, List<String> empIds) {
		ScheduleCreatorExecutionCommand scheduleCommand = new ScheduleCreatorExecutionCommand();
		scheduleCommand.setConfirm(false);
		scheduleCommand.setExecutionId(execId);
		scheduleCommand.setAutomatic(true);
		scheduleCommand.setEmployeeIds(empIds);
		// 2-対象開始日 ＝ 「期間の計算」で作成した開始日とする
		// companyId
		scheduleCommand.setCompanyId(loginContext.companyId());
		// 3-対象開始日 ＝ 「期間の計算」で作成した開始日とする
		// 4-対象終了日 ＝ 「期間の計算」で作成した終了日とする
		// calculateSchedulePeriod
		ScheduleExecutionLog scheduleExecutionLog = new ScheduleExecutionLog();
		scheduleExecutionLog.setPeriod(new DatePeriod(calculateSchedulePeriod.start(), calculateSchedulePeriod.end()));
		scheduleExecutionLog.setCompanyId(new CompanyId(loginContext.companyId()));
		scheduleExecutionLog.setExecutionId(execId);
		// 【ドメインモデル「作成対象詳細設定」．異動者を再作成する = "する" or ドメインモデル「作成対象詳細設定」．勤務種別変更者を再作成 = "する"
		// の場合】
		boolean recreateTransfer = procExec.getExecSetting().getPerSchedule().getTarget().getTargetSetting()
				.isRecreateTransfer();
		boolean recreateWorkType = procExec.getExecSetting().getPerSchedule().getTarget().getTargetSetting()
				.isRecreateWorkType();
		ScheduleCreateContent s = new ScheduleCreateContent();
		// 1-実行ID ＝ 取得した実行ID
		// execId
		s.setExecutionId(execId);
		ReCreateContent reCreateContent = new ReCreateContent();
		if (recreateTransfer || recreateWorkType) {
			// 6-実施区分 → 再作成 とする
			s.setImplementAtr(ImplementAtr.RECREATE);
			// 7-再作成区分 → 未確定データのみ とする

			reCreateContent.setReCreateAtr(ReCreateAtr.ONLY_UNCONFIRM);
			// 8-処理実行区分 → もう一度作り直す とする
			reCreateContent.setProcessExecutionAtr(ProcessExecutionAtr.REBUILD);
		} else {
			// ・実施区分 → 再作成 とする
			s.setImplementAtr(ImplementAtr.RECREATE);
			// ・再作成区分 → 全件 とする
			reCreateContent.setReCreateAtr(ReCreateAtr.ALL_CASE);
			// ・処理実行区分 → もう一度作り直す とする
			reCreateContent.setProcessExecutionAtr(ProcessExecutionAtr.REBUILD);
		}
		// ・9-マスタ情報再設定 → falseとする
		ResetAtr r = new ResetAtr();
		r.setResetMasterInfo(false);
		// 10-申し送り時間再設定 → falseとする
		r.setResetTimeAssignment(false);
		// ・11-作成時に確定済みにする → falseとする
		s.setConfirm(false);
		// ・12-作成方法区分 → 個人情報とする
		s.setCreateMethodAtr(CreateMethodAtr.PERSONAL_INFO);
		// 13-コピー開始日 → nullとする

		// 14-パターンコード → nullとする

		// 15-休日反映方法 → nullとする

		// 16-パターン開始日 → nullとする

		// 17-法内休日利用区分 → nullとする

		// 18-法内休日勤務種類 → nullとする

		// 19-法外休日利用区分 → nullとする

		// 20-法外休日勤務種類 → nullとする

		// 21-祝日利用区分 → nullとする

		// 22-祝日勤務種類 → nullとする

		// 23-実行区分 ＝ 自動
		scheduleExecutionLog.setExeAtr(ExecutionAtr.AUTOMATIC);
		RebuildTargetDetailsAtr rebuildTargetDetailsAtr = new RebuildTargetDetailsAtr();
		if (recreateTransfer) {
			// 24-異動者を再作成 → true
			rebuildTargetDetailsAtr.setRecreateConverter(true);

		} else {
			// 異動者を再作成 → false
			rebuildTargetDetailsAtr.setRecreateConverter(false);
		}
		if (recreateWorkType) {
			// 25-・勤務種別変更者を再作成 → true
			rebuildTargetDetailsAtr.setRecreateWorkTypeChange(true);
		} else {
			// ・勤務種別変更者を再作成 → false
			rebuildTargetDetailsAtr.setRecreateWorkTypeChange(false);
		}
		// 【ドメインモデル「作成対象詳細設定」．手修正を保護する = "する" 】
		boolean manualCorrection = procExec.getExecSetting().getPerSchedule().getTarget().getTargetSetting()
				.isManualCorrection();
		if (manualCorrection) {
			// 26-・手修正を保護 → true
			rebuildTargetDetailsAtr.setProtectHandCorrection(true);
		} else {
			// 手修正を保護 → false
			rebuildTargetDetailsAtr.setProtectHandCorrection(false);
		}

		// 27-再作成対象区分 → 対象者のみ
		reCreateContent.setRebuildTargetAtr(RebuildTargetAtr.TARGET_ONLY);
		// 28-休職休業者を再作成 → falseとする
		rebuildTargetDetailsAtr.setRecreateEmployeeOffWork(false);
		// 29-・直行直帰者を再作成 → falseとする
		rebuildTargetDetailsAtr.setRecreateDirectBouncer(false);
		// 30短時間勤務者を再作成 → falseとする
		rebuildTargetDetailsAtr.setRecreateShortTermEmployee(false);
		// 31勤務開始・終了時刻を再設定 → falseとする
		r.setResetWorkingHours(false);
		// 32休憩開始・終了時刻を再設定 → falseとする
		r.setResetStartEndTime(false);

		reCreateContent.setRebuildTargetDetailsAtr(rebuildTargetDetailsAtr);
		reCreateContent.setResetAtr(r);
		;
		s.setReCreateContent(reCreateContent);
		scheduleCommand.setScheduleExecutionLog(scheduleExecutionLog);
		scheduleCommand.setContent(s);
		return scheduleCommand;
	}

	private ScheduleCreatorExecutionCommand getScheduleCreatorExecutionOneEmp(String execId, ProcessExecution procExec,
			LoginUserContext loginContext, DatePeriod calculateSchedulePeriod, List<String> empIds) {
		ScheduleCreatorExecutionCommand scheduleCommand = new ScheduleCreatorExecutionCommand();
		scheduleCommand.setConfirm(false);
		scheduleCommand.setAutomatic(true);
		scheduleCommand.setEmployeeIds(empIds);
		// 1-実行ID ＝ 取得した実行ID
		// execId
		scheduleCommand.setExecutionId(execId);
		// 2-対象開始日 ＝ 「期間の計算」で作成した開始日とする
		// companyId
		scheduleCommand.setCompanyId(loginContext.companyId());
		// 3-対象開始日 ＝ 「期間の計算」で作成した開始日とする
		// 4-対象終了日 ＝ 「期間の計算」で作成した終了日とする
		// calculateSchedulePeriod
		ScheduleExecutionLog scheduleExecutionLog = new ScheduleExecutionLog();
		scheduleExecutionLog.setPeriod(new DatePeriod(calculateSchedulePeriod.start(), calculateSchedulePeriod.end()));
		scheduleExecutionLog.setCompanyId(new CompanyId(loginContext.companyId()));
		scheduleExecutionLog.setExecutionId(execId);
		// 【ドメインモデル「作成対象詳細設定」．異動者を再作成する = "する" or ドメインモデル「作成対象詳細設定」．勤務種別変更者を再作成 = "する"
		// の場合】
		boolean recreateTransfer = procExec.getExecSetting().getPerSchedule().getTarget().getTargetSetting()
				.isRecreateTransfer();
		boolean recreateWorkType = procExec.getExecSetting().getPerSchedule().getTarget().getTargetSetting()
				.isRecreateWorkType();
		ScheduleCreateContent s = new ScheduleCreateContent();
		s.setExecutionId(execId);
		ReCreateContent reCreateContent = new ReCreateContent();
		if (recreateTransfer || recreateWorkType) {
			// 6-実施区分 → 再作成 とする
			s.setImplementAtr(ImplementAtr.RECREATE);
			// 7-再作成区分 → 未確定データのみ とする

			reCreateContent.setReCreateAtr(ReCreateAtr.ONLY_UNCONFIRM);
			// 8-処理実行区分 → もう一度作り直す とする
			reCreateContent.setProcessExecutionAtr(ProcessExecutionAtr.REBUILD);
		} else {
			// ・実施区分 → 再作成 とする
			s.setImplementAtr(ImplementAtr.RECREATE);
			// ・再作成区分 → 全件 とする
			reCreateContent.setReCreateAtr(ReCreateAtr.ALL_CASE);
			// ・処理実行区分 → もう一度作り直す とする
			reCreateContent.setProcessExecutionAtr(ProcessExecutionAtr.REBUILD);
		}
		// ・9-マスタ情報再設定 → falseとする
		ResetAtr r = new ResetAtr();
		r.setResetMasterInfo(false);
		// 10-申し送り時間再設定 → falseとする
		r.setResetTimeAssignment(false);
		// ・11-作成時に確定済みにする → falseとする
		s.setConfirm(false);
		// ・12-作成方法区分 → 個人情報とする
		s.setCreateMethodAtr(CreateMethodAtr.PERSONAL_INFO);
		// 13-コピー開始日 → nullとする

		// 14-パターンコード → nullとする

		// 15-休日反映方法 → nullとする

		// 16-パターン開始日 → nullとする

		// 17-法内休日利用区分 → nullとする

		// 18-法内休日勤務種類 → nullとする

		// 19-法外休日利用区分 → nullとする

		// 20-法外休日勤務種類 → nullとする

		// 21-祝日利用区分 → nullとする

		// 22-祝日勤務種類 → nullとする

		// 23-実行区分 ＝ 自動
		scheduleExecutionLog.setExeAtr(ExecutionAtr.AUTOMATIC);

		RebuildTargetDetailsAtr rebuildTargetDetailsAtr = new RebuildTargetDetailsAtr();
		if (recreateTransfer) {
			// 24-異動者を再作成 → true
			rebuildTargetDetailsAtr.setRecreateConverter(true);

		} else {
			// 異動者を再作成 → false
			rebuildTargetDetailsAtr.setRecreateConverter(false);
		}
		if (recreateWorkType) {
			// 25-・勤務種別変更者を再作成 → true
			rebuildTargetDetailsAtr.setRecreateWorkTypeChange(true);
		} else {
			// ・勤務種別変更者を再作成 → false
			rebuildTargetDetailsAtr.setRecreateWorkTypeChange(false);
		}
		// 【ドメインモデル「作成対象詳細設定」．手修正を保護する = "する" 】
		boolean manualCorrection = procExec.getExecSetting().getPerSchedule().getTarget().getTargetSetting()
				.isManualCorrection();
		if (manualCorrection) {
			// 26-・手修正を保護 → true
			rebuildTargetDetailsAtr.setProtectHandCorrection(true);
		} else {
			// 手修正を保護 → false
			rebuildTargetDetailsAtr.setProtectHandCorrection(false);
		}

		// 27-再作成対象区分 → 対象者のみ
		reCreateContent.setRebuildTargetAtr(RebuildTargetAtr.TARGET_ONLY);
		// 28-休職休業者を再作成 → falseとする
		rebuildTargetDetailsAtr.setRecreateEmployeeOffWork(false);
		// 29-・直行直帰者を再作成 → falseとする
		rebuildTargetDetailsAtr.setRecreateDirectBouncer(false);
		// 30短時間勤務者を再作成 → falseとする
		rebuildTargetDetailsAtr.setRecreateShortTermEmployee(false);
		// 31勤務開始・終了時刻を再設定 → falseとする
		r.setResetWorkingHours(false);
		// 32休憩開始・終了時刻を再設定 → falseとする
		r.setResetStartEndTime(false);

		reCreateContent.setRebuildTargetDetailsAtr(rebuildTargetDetailsAtr);
		reCreateContent.setResetAtr(r);
		;
		s.setReCreateContent(reCreateContent);
		scheduleCommand.setScheduleExecutionLog(scheduleExecutionLog);
		scheduleCommand.setContent(s);
		return scheduleCommand;
	}

	/**
	 * 日別作成
	 * 
	 * @param execId
	 *            実行ID
	 * @param procExec
	 *            更新処理自動実行
	 * @param execSetting
	 *            実行タスク設定
	 * @param procExecLog
	 *            更新処理自動実行ログ
	 */
	private boolean createDailyData(CommandHandlerContext<ExecuteProcessExecutionCommand> context,
			EmpCalAndSumExeLog empCalAndSumExeLog, String execId, ProcessExecution procExec,
			ProcessExecutionLog procExecLog) {
		ExecuteProcessExecutionCommand command = context.getCommand();
		String companyId = command.getCompanyId();
		// ドメインモデル「更新処理自動実行ログ」を更新する
		this.updateEachTaskStatus(procExecLog, ProcessExecutionTask.DAILY_CREATION, null);
		this.updateEachTaskStatus(procExecLog, ProcessExecutionTask.DAILY_CALCULATION, null);
		this.procExecLogRepo.update(procExecLog);

		// 日別実績の作成・計算区分の判定
		if (!procExec.getExecSetting().getDailyPerf().isDailyPerfCls()) {
			this.updateEachTaskStatus(procExecLog, ProcessExecutionTask.DAILY_CREATION, EndStatus.NOT_IMPLEMENT);
			this.updateEachTaskStatus(procExecLog, ProcessExecutionTask.DAILY_CALCULATION, EndStatus.NOT_IMPLEMENT);
			this.procExecLogRepo.update(procExecLog);
			return true;
		}
		String execItemCd = context.getCommand().getExecItemCd();
		List<ExecutionTaskLog> taskLogList = this.execTaskLogRepo.getAllByCidExecCdExecId(companyId, execItemCd,
				execId);
		if (CollectionUtil.isEmpty(taskLogList)) {
			this.execTaskLogRepo.insertAll(companyId, execItemCd, execId, procExecLog.getTaskLogList());
		} else {
			this.execTaskLogRepo.updateAll(companyId, execItemCd, execId, procExecLog.getTaskLogList());
		}

		// ドメインモデル「就業締め日」を取得する
		List<Closure> closureList = this.closureRepo.findAllActive(companyId, UseClassification.UseClass_Use);
		DatePeriod period = this.findClosureMinMaxPeriod(companyId, closureList);

		// ドメインモデル「実行ログ」を新規登録する
		ExecutionLog dailyCreateLog = new ExecutionLog(execId, ExecutionContent.DAILY_CREATION, ErrorPresent.NO_ERROR,
				new ExecutionTime(GeneralDateTime.now(), GeneralDateTime.now()), ExecutionStatus.INCOMPLETE,
				new ObjectPeriod(period.end(), period.end()));
		dailyCreateLog.setDailyCreationSetInfo(
				new SettingInforForDailyCreation(ExecutionContent.DAILY_CREATION, ExecutionType.NORMAL_EXECUTION,
						IdentifierUtil.randomUniqueId(), DailyRecreateClassification.REBUILD, Optional.empty()));
		ExecutionLog dailyCalLog = new ExecutionLog(execId, ExecutionContent.DAILY_CALCULATION, ErrorPresent.NO_ERROR,
				new ExecutionTime(GeneralDateTime.now(), GeneralDateTime.now()), ExecutionStatus.INCOMPLETE,
				new ObjectPeriod(period.start(), period.end()));
		dailyCalLog.setDailyCalSetInfo(new CalExeSettingInfor(ExecutionContent.DAILY_CALCULATION,
				ExecutionType.NORMAL_EXECUTION, IdentifierUtil.randomUniqueId()));
		this.executionLogRepository.addExecutionLog(dailyCreateLog);
		this.executionLogRepository.addExecutionLog(dailyCalLog);

		// ドメインモデル「更新処理自動実行ログ」を更新する
		if (procExecLog.getEachProcPeriod() != null && procExecLog.getEachProcPeriod().isPresent()) {
			EachProcessPeriod eachProcessPeriod = procExecLog.getEachProcPeriod().get();
			DatePeriod scheduleCreationPeriod = (eachProcessPeriod.getScheduleCreationPeriod() != null
					&& eachProcessPeriod.getScheduleCreationPeriod().isPresent())
							? eachProcessPeriod.getScheduleCreationPeriod().get()
							: null;
			DatePeriod reflectApprovalResult = (eachProcessPeriod.getReflectApprovalResult() != null
					&& eachProcessPeriod.getReflectApprovalResult().isPresent())
							? eachProcessPeriod.getReflectApprovalResult().get()
							: null;
			procExecLog.setEachProcPeriod(
					new EachProcessPeriod(scheduleCreationPeriod, period, period, reflectApprovalResult));
		} else {
			procExecLog.setEachProcPeriod(new EachProcessPeriod(null, period, period, null));
		}

		boolean isHasCreateDailyException = false;
		boolean isHasDailyCalculateException = false;
		// 就業担当者の社員ID（List）を取得する : RQ526
		List<String> listManagementId = employeeManageAdapter.getListEmpID(companyId, GeneralDate.today());
		try {
			for (Closure closure : closureList) {
				// ドメインモデル「更新処理前回実行日時」を更新する
				// LastExecDateTime lastExecDateTime = new LastExecDateTime(companyId,
				// procExecLog.getExecItemCd(),
				// GeneralDateTime.now());

				// 雇用コードを取得する
				List<ClosureEmployment> employmentList = this.closureEmpRepo.findByClosureId(companyId,
						closure.getClosureId().value);
				List<String> lstEmploymentCode = new ArrayList<String>();
				employmentList.forEach(x -> {
					lstEmploymentCode.add(x.getEmploymentCD());
				});
				List<ProcessExecutionScopeItem> workplaceIdList = procExec.getExecScope().getWorkplaceIdList();
				List<String> workPlaceIds = new ArrayList<String>();
				workplaceIdList.forEach(x -> {
					workPlaceIds.add(x.getWkpId());
				});
				if (procExec.getProcessExecType() == ProcessExecType.NORMAL_EXECUTION) {
					// 実行呼び出し処理
					// 期間の計算
					DailyCreatAndCalOutput calculateDailyPeriod = this.calculateDailyPeriod(procExec,
							closure.getClosureId().value, closure.getClosureMonth());
					if (calculateDailyPeriod == null)
						continue;
					// <<Public>> 就業条件で社員を検索して並び替える
					RegulationInfoEmployeeAdapterImport regulationInfoEmployeeAdapterImport = new RegulationInfoEmployeeAdapterImport();
					if (procExec.getExecScope().getExecScopeCls().value == 1) {
						// 【更新処理自動実行.実行範囲.実行範囲区分 ＝ 職場 の場合】
						// 基準日 → システム日付
						regulationInfoEmployeeAdapterImport.setBaseDate(GeneralDateTime.now());
						// 検索参照範囲 → 参照範囲を考慮しない
						regulationInfoEmployeeAdapterImport.setReferenceRange(3);
						// 雇用で絞り込む → TRUE
						regulationInfoEmployeeAdapterImport.setFilterByEmployment(true);
						// 雇用コード一覧 → 取得した雇用コード（List)
						regulationInfoEmployeeAdapterImport.setEmploymentCodes(lstEmploymentCode);
						// 部門で絞り込む → FALSE
						regulationInfoEmployeeAdapterImport.setFilterByDepartment(false);
						// 部門ID一覧 → なし
						regulationInfoEmployeeAdapterImport.setDepartmentCodes(null);
						// 職場で絞り込む → TRUE
						regulationInfoEmployeeAdapterImport.setFilterByWorkplace(true);

						// 職場ID一覧 → ドメインモデル「更新処理自動実行」に登録されている職場ID
						regulationInfoEmployeeAdapterImport.setWorkplaceCodes(workPlaceIds);
						// 分類で絞り込む → FALSE
						regulationInfoEmployeeAdapterImport.setFilterByClassification(false);
						// 分類コード一覧 → なし
						regulationInfoEmployeeAdapterImport.setClassificationCodes(null);
						// 職位で絞り込む → FALSE
						regulationInfoEmployeeAdapterImport.setFilterByJobTitle(false);
						// 職位ID一覧 → なし
						regulationInfoEmployeeAdapterImport.setJobTitleCodes(null);
						// 在職・休職・休業のチェック期間 → 作成した期間
						regulationInfoEmployeeAdapterImport
								.setPeriodStart(calculateDailyPeriod.getDailyCreationPeriod().start());
						regulationInfoEmployeeAdapterImport
								.setPeriodEnd(calculateDailyPeriod.getDailyCreationPeriod().end());
						// 在職者を含める → TRUE
						regulationInfoEmployeeAdapterImport.setIncludeIncumbents(true);
						// 休職者を含める → FALSE
						regulationInfoEmployeeAdapterImport.setIncludeWorkersOnLeave(false);
						// 休業者を含める → FALSE
						regulationInfoEmployeeAdapterImport.setIncludeOccupancy(false);
						// 出向に来ている社員を含める → TRUE
						regulationInfoEmployeeAdapterImport.setIncludeAreOnLoan(true);
						// 出向に行っている社員を含める → FALSE
						regulationInfoEmployeeAdapterImport.setIncludeGoingOnLoan(false);
						// 退職者を含める → FALSE
						regulationInfoEmployeeAdapterImport.setIncludeRetirees(false);
						// 退職日のチェック期間 → 作成した期間
						regulationInfoEmployeeAdapterImport
								.setRetireStart(calculateDailyPeriod.getDailyCreationPeriod().start());
						regulationInfoEmployeeAdapterImport
								.setRetireEnd(calculateDailyPeriod.getDailyCreationPeriod().end());
						// 並び順NO → 1
						regulationInfoEmployeeAdapterImport.setSortOrderNo(1);
						regulationInfoEmployeeAdapterImport.setSystemType(2);
						// 勤務種別で絞り込む → FALSE
						regulationInfoEmployeeAdapterImport.setFilterByWorktype(false);

						// 氏名の種類 → ビジネスネーム日本語
						regulationInfoEmployeeAdapterImport.setNameType("ビジネスネーム日本語");

					} else {
						// 【更新処理自動実行.実行範囲.実行範囲区分 ＝ 会社 の場合】
						// 基準日 → システム日付
						regulationInfoEmployeeAdapterImport.setBaseDate(GeneralDateTime.now());
						// 検索参照範囲 → 参照範囲を考慮しない
						regulationInfoEmployeeAdapterImport.setReferenceRange(3);
						// 雇用で絞り込む → TRUE
						regulationInfoEmployeeAdapterImport.setFilterByEmployment(true);
						// 雇用コード一覧 → 取得した雇用コード（List)
						regulationInfoEmployeeAdapterImport.setEmploymentCodes(lstEmploymentCode);
						// 部門で絞り込む → FALSE
						regulationInfoEmployeeAdapterImport.setFilterByDepartment(false);
						// 部門ID一覧 → なし
						regulationInfoEmployeeAdapterImport.setDepartmentCodes(null);
						// 職場で絞り込む → FALSE
						regulationInfoEmployeeAdapterImport.setFilterByWorkplace(false);
						// 職場ID一覧 → なし
						regulationInfoEmployeeAdapterImport.setWorkplaceCodes(null);
						// 分類で絞り込む → FALSE
						regulationInfoEmployeeAdapterImport.setFilterByClassification(false);
						// 分類コード一覧 → なし
						regulationInfoEmployeeAdapterImport.setClassificationCodes(null);
						// 職位で絞り込む → FALSE
						regulationInfoEmployeeAdapterImport.setFilterByJobTitle(false);
						// 職位ID一覧 → なし
						regulationInfoEmployeeAdapterImport.setJobTitleCodes(null);
						// 在職・休職・休業のチェック期間 → 作成した期間
						regulationInfoEmployeeAdapterImport
								.setPeriodStart(calculateDailyPeriod.getDailyCreationPeriod().start());
						regulationInfoEmployeeAdapterImport
								.setPeriodEnd(calculateDailyPeriod.getDailyCreationPeriod().end());
						// 在職者を含める → TRUE
						regulationInfoEmployeeAdapterImport.setIncludeIncumbents(true);
						// 休職者を含める → FALSE
						regulationInfoEmployeeAdapterImport.setIncludeWorkersOnLeave(false);
						// 休業者を含める → FALSE
						regulationInfoEmployeeAdapterImport.setIncludeOccupancy(false);
						// 出向に来ている社員を含める → TRUE
						regulationInfoEmployeeAdapterImport.setIncludeAreOnLoan(true);
						// 出向に行っている社員を含める → FALSE
						regulationInfoEmployeeAdapterImport.setIncludeGoingOnLoan(false);
						// 退職者を含める → FALSE
						regulationInfoEmployeeAdapterImport.setIncludeRetirees(false);
						// 退職日のチェック期間 → 作成した期間
						regulationInfoEmployeeAdapterImport
								.setRetireStart(calculateDailyPeriod.getDailyCreationPeriod().start());
						regulationInfoEmployeeAdapterImport
								.setRetireEnd(calculateDailyPeriod.getDailyCreationPeriod().end());
						// 並び順NO → 1
						regulationInfoEmployeeAdapterImport.setSortOrderNo(1);
						regulationInfoEmployeeAdapterImport.setSystemType(2);
						// 勤務種別で絞り込む → FALSE
						regulationInfoEmployeeAdapterImport.setFilterByWorktype(false);
						// 氏名の種類 → ビジネスネーム日本語
						regulationInfoEmployeeAdapterImport.setNameType("ビジネスネーム日本語");

					}
					// <<Public>> 就業条件で社員を検索して並び替える
					List<RegulationInfoEmployeeAdapterDto> lstRegulationInfoEmployee = this.regulationInfoEmployeeAdapter
							.find(regulationInfoEmployeeAdapterImport);
					List<String> empIds = new ArrayList<>();
					lstRegulationInfoEmployee.forEach(x -> {
						empIds.add(x.getEmployeeId());
					});

					String typeExecution = "日別作成";
					// 日別実績の作成
					// boolean dailyPerformanceCreation = this.dailyPerformanceCreation(
					// companyId,context, procExec, empCalAndSumExeLog,
					// createProcessForChangePerOrWorktype.getNoLeaderEmpIdList(),
					// calculateDailyPeriod.getDailyCreationPeriod(), workPlaceIds,
					// typeExecution,dailyCreateLog);
					try {
						boolean dailyPerformanceCreation = this.dailyPerformanceCreation(companyId, context, procExec,
								empCalAndSumExeLog, empIds, calculateDailyPeriod.getDailyCreationPeriod(), workPlaceIds,
								typeExecution, dailyCreateLog);

						if (dailyPerformanceCreation) {
							return false;
						}
					} catch (CreateDailyException ex) {
						isHasCreateDailyException = true;
					}
					// boolean dailyPerformanceCreation = this.dailyPerformanceCreation(
					// companyId,context, procExec, empCalAndSumExeLog, empIds,
					// calculateDailyPeriod.getDailyCreationPeriod(), workPlaceIds,
					// typeExecution,dailyCreateLog);
					//
					// if(dailyPerformanceCreation){
					// return false;
					// }

					typeExecution = "日別計算";
					// 日別実績の計算
					try {
						boolean dailyPerformanceCreation2 = this.dailyPerformanceCreation(companyId, context, procExec,
								empCalAndSumExeLog, empIds, calculateDailyPeriod.getDailyCalcPeriod(), workPlaceIds,
								typeExecution, dailyCalLog);
						if (dailyPerformanceCreation2) {
							return false;
						}
					} catch (DailyCalculateException ex) {
						isHasDailyCalculateException = true;
					}

				} else {
					GeneralDate calculateDate = this.calculatePeriod(closure.getClosureId().value, period, companyId);

					// <<Public>> 就業条件で社員を検索して並び替える
					RegulationInfoEmployeeAdapterImport regulationInfoEmployeeAdapterImport = new RegulationInfoEmployeeAdapterImport();
					if (procExec.getExecScope().getExecScopeCls().value == 1) {
						// 【更新処理自動実行.実行範囲.実行範囲区分 ＝ 職場 の場合】
						// 基準日 → システム日付
						regulationInfoEmployeeAdapterImport.setBaseDate(GeneralDateTime.now());
						// 検索参照範囲 → 参照範囲を考慮しない
						regulationInfoEmployeeAdapterImport.setReferenceRange(3);
						// 雇用で絞り込む → TRUE
						regulationInfoEmployeeAdapterImport.setFilterByEmployment(true);
						// 雇用コード一覧 → 取得した雇用コード（List)
						regulationInfoEmployeeAdapterImport.setEmploymentCodes(lstEmploymentCode);
						// 部門で絞り込む → FALSE
						regulationInfoEmployeeAdapterImport.setFilterByDepartment(false);
						// 部門ID一覧 → なし
						regulationInfoEmployeeAdapterImport.setDepartmentCodes(null);
						// 職場で絞り込む → TRUE
						regulationInfoEmployeeAdapterImport.setFilterByWorkplace(true);

						// 職場ID一覧 → 職場ID（List）←ドメインモデル「更新処理自動実行」．実行範囲．職場実行範囲
						regulationInfoEmployeeAdapterImport.setWorkplaceCodes(workPlaceIds);
						// 分類で絞り込む → FALSE
						regulationInfoEmployeeAdapterImport.setFilterByClassification(false);
						// 分類コード一覧 → なし
						regulationInfoEmployeeAdapterImport.setClassificationCodes(null);
						// 職位で絞り込む → FALSE
						regulationInfoEmployeeAdapterImport.setFilterByJobTitle(false);
						// 職位ID一覧 → なし
						regulationInfoEmployeeAdapterImport.setJobTitleCodes(null);
						// 在職・休職・休業のチェック期間 → 作成した開始日～9999/12/31
						regulationInfoEmployeeAdapterImport.setPeriodStart(calculateDate);
						regulationInfoEmployeeAdapterImport.setPeriodEnd(GeneralDate.ymd(9999, 12, 31));
						// 在職者を含める → TRUE
						regulationInfoEmployeeAdapterImport.setIncludeIncumbents(true);
						// 休職者を含める → FALSE
						regulationInfoEmployeeAdapterImport.setIncludeWorkersOnLeave(false);
						// 休業者を含める → FALSE
						regulationInfoEmployeeAdapterImport.setIncludeOccupancy(false);
						// 出向に来ている社員を含める → TRUE
						regulationInfoEmployeeAdapterImport.setIncludeAreOnLoan(true);
						// 出向に行っている社員を含める → FALSE
						regulationInfoEmployeeAdapterImport.setIncludeGoingOnLoan(false);
						// 退職者を含める → FALSE
						regulationInfoEmployeeAdapterImport.setIncludeRetirees(false);
						// 退職日のチェック期間 → 作成した開始日～9999/12/31
						regulationInfoEmployeeAdapterImport.setRetireStart(calculateDate);
						regulationInfoEmployeeAdapterImport.setRetireEnd(GeneralDate.ymd(9999, 12, 31));
						// 並び順NO → 1
						regulationInfoEmployeeAdapterImport.setSortOrderNo(1);
						regulationInfoEmployeeAdapterImport.setSystemType(2);
						// 勤務種別で絞り込む → FALSE
						regulationInfoEmployeeAdapterImport.setFilterByWorktype(false);

						// 氏名の種類 → ビジネスネーム日本語
						regulationInfoEmployeeAdapterImport.setNameType("ビジネスネーム日本語");

					} else {
						// 【更新処理自動実行.実行範囲.実行範囲区分 ＝ 会社 の場合】
						// 基準日 → システム日付
						regulationInfoEmployeeAdapterImport.setBaseDate(GeneralDateTime.now());
						// 検索参照範囲 → 参照範囲を考慮しない
						regulationInfoEmployeeAdapterImport.setReferenceRange(3);
						// 雇用で絞り込む → TRUE
						regulationInfoEmployeeAdapterImport.setFilterByEmployment(true);
						// 雇用コード一覧 → 取得した雇用コード（List)
						regulationInfoEmployeeAdapterImport.setEmploymentCodes(lstEmploymentCode);
						// 部門で絞り込む → FALSE
						regulationInfoEmployeeAdapterImport.setFilterByDepartment(false);
						// 部門ID一覧 → なし
						regulationInfoEmployeeAdapterImport.setDepartmentCodes(null);
						// 職場で絞り込む → FALSE
						regulationInfoEmployeeAdapterImport.setFilterByWorkplace(false);
						// 職場ID一覧 → なし
						regulationInfoEmployeeAdapterImport.setWorkplaceCodes(null);
						// 分類で絞り込む → FALSE
						regulationInfoEmployeeAdapterImport.setFilterByClassification(false);
						// 分類コード一覧 → なし
						regulationInfoEmployeeAdapterImport.setClassificationCodes(null);
						// 職位で絞り込む → FALSE
						regulationInfoEmployeeAdapterImport.setFilterByJobTitle(false);
						// 職位ID一覧 → なし
						regulationInfoEmployeeAdapterImport.setJobTitleCodes(null);
						// 在職・休職・休業のチェック期間 → 作成した開始日～9999/12/31
						regulationInfoEmployeeAdapterImport.setPeriodStart(calculateDate);
						regulationInfoEmployeeAdapterImport.setPeriodEnd(GeneralDate.ymd(9999, 12, 31));
						// 在職者を含める → TRUE
						regulationInfoEmployeeAdapterImport.setIncludeIncumbents(true);
						// 休職者を含める → FALSE
						regulationInfoEmployeeAdapterImport.setIncludeWorkersOnLeave(false);
						// 休業者を含める → FALSE
						regulationInfoEmployeeAdapterImport.setIncludeOccupancy(false);
						// 出向に来ている社員を含める → TRUE
						regulationInfoEmployeeAdapterImport.setIncludeAreOnLoan(true);
						// 出向に行っている社員を含める → FALSE
						regulationInfoEmployeeAdapterImport.setIncludeGoingOnLoan(false);
						// 退職者を含める → FALSE
						regulationInfoEmployeeAdapterImport.setIncludeRetirees(false);
						// 退職日のチェック期間 → 作成した期間
						regulationInfoEmployeeAdapterImport.setRetireStart(calculateDate);
						regulationInfoEmployeeAdapterImport.setRetireEnd(GeneralDate.ymd(9999, 12, 31));
						// 並び順NO → 1
						regulationInfoEmployeeAdapterImport.setSortOrderNo(1);
						regulationInfoEmployeeAdapterImport.setSystemType(2);
						// 勤務種別で絞り込む → FALSE
						regulationInfoEmployeeAdapterImport.setFilterByWorktype(false);
						// 氏名の種類 → ビジネスネーム日本語
						regulationInfoEmployeeAdapterImport.setNameType("ビジネスネーム日本語");

					}
					// <<Public>> 就業条件で社員を検索して並び替える
					List<RegulationInfoEmployeeAdapterDto> lstRegulationInfoEmployee = this.regulationInfoEmployeeAdapter
							.find(regulationInfoEmployeeAdapterImport);
					List<String> empIds = new ArrayList<>();
					lstRegulationInfoEmployee.forEach(x -> {
						empIds.add(x.getEmployeeId());
					});
					// 異動者・勤務種別変更者リスト作成処理
					ListLeaderOrNotEmpOutput createProcessForChangePerOrWorktype = this
							.createProcessForChangePerOrWorktype(closure.getClosureId().value, companyId, empIds,
									calculateDate, procExec);

					boolean isHasInterrupt = false;
					for (String empLeader : createProcessForChangePerOrWorktype.getLeaderEmpIdList()) {
						// ドメインモデル「日別実績の勤務情報」を取得する
						List<WorkInfoOfDailyPerFnImport> listWorkInfo = recordWorkInfoFunAdapter
								.findByPeriodOrderByYmd(empLeader);
						if (listWorkInfo.isEmpty())
							continue;
						// 再作成処理
						// 日別実績処理の再実行
						// 「作成した開始日」～「取得した日別実績の勤務情報.年月日」を対象期間とする
						GeneralDate maxDate = listWorkInfo.stream().map(u -> u.getYmd()).max(GeneralDate::compareTo)
								.get();
						isHasInterrupt = this.RedoDailyPerformanceProcessing(context, companyId, empLeader,
								new DatePeriod(calculateDate, maxDate), empCalAndSumExeLog.getEmpCalAndSumExecLogID(),
								dailyCreateLog, procExec);
						if (isHasInterrupt) {
							break;
						}
					}

				}

				// throw new CreateDailyException();
			}
			// } catch (CreateDailyException ex) {
			// isHasCreateDailyException = true;
			// } catch (DailyCalculateException ex) {
			// isHasDailyCalculateException = true;
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		// exceptionがあるか確認する（日別作成）
		if (isHasCreateDailyException) {
			this.updateEachTaskStatus(procExecLog, ProcessExecutionTask.DAILY_CREATION, EndStatus.ABNORMAL_END);
			// ドメインモデル「エラーメッセージ情報」を取得する
			List<ErrMessageInfo> listErrMessageInfo = errMessageInfoRepository.getAllErrMessageInfoByID(execId,
					ExecutionContent.DAILY_CREATION.value);

			ExecutionLogImportFn param = new ExecutionLogImportFn();
			List<ExecutionLogErrorDetailFn> listErrorAndEmpId = new ArrayList<>();

			// 会社ID ＝ パラメータ.更新処理自動実行.会社ID
			param.setCompanyId(companyId);
			// 管理社員ID ＝
			param.setManagerId(listManagementId);
			// 実行完了日時 ＝ システム日時
			param.setFinishDateTime(GeneralDateTime.now());
			// エラーの有無 ＝ エラーあり
			param.setExistenceError(1);
			// 実行内容 ＝ 日別実績の作成
			param.setExecutionContent(AlarmCategoryFn.CREATE_DAILY_REPORT);

			if (listErrMessageInfo.isEmpty()) {
				// アルゴリズム「エラーログ書き込み」を実行する
				this.errMessageInfoRepository.add(new ErrMessageInfo("System", execId, new ErrMessageResource("18"),
						ExecutionContent.DAILY_CREATION, GeneralDate.today(),
						new ErrMessageContent(TextResource.localize("Msg_1339"))));
				for (String managementId : listManagementId) {
					listErrorAndEmpId
							.add(new ExecutionLogErrorDetailFn(TextResource.localize("Msg_1339"), managementId));
				}

			} else {
				for (ErrMessageInfo errMessageInfo : listErrMessageInfo) {
					listErrorAndEmpId.add(new ExecutionLogErrorDetailFn(errMessageInfo.getMessageError().v(),
							errMessageInfo.getEmployeeID()));
				}
			}
			param.setTargerEmployee(listErrorAndEmpId);
			// アルゴリズム「実行ログ登録」を実行する 2290 Done
			executionLogAdapterFn.updateExecuteLog(param);
		} else {
			// ドメインモデル「更新処理自動実行ログ」を更新する
			this.updateEachTaskStatus(procExecLog, ProcessExecutionTask.DAILY_CREATION, EndStatus.SUCCESS);
			ExecutionLogImportFn param = new ExecutionLogImportFn();
			List<ExecutionLogErrorDetailFn> listErrorAndEmpId = new ArrayList<>();
			// 会社ID ＝ パラメータ.更新処理自動実行.会社ID
			param.setCompanyId(companyId);
			// 管理社員ID ＝
			param.setManagerId(listManagementId);
			// 実行完了日時 ＝ システム日時
			param.setFinishDateTime(GeneralDateTime.now());
			// 実行内容 ＝ 日別実績の作成
			param.setExecutionContent(AlarmCategoryFn.CREATE_DAILY_REPORT);
			// エラーの有無 ＝ エラーなし
			param.setExistenceError(0);
			// 実行ログエラー詳細 ＝ NULL
			param.setTargerEmployee(listErrorAndEmpId);
			// アルゴリズム「実行ログ登録」を実行する 2290 Done
			executionLogAdapterFn.updateExecuteLog(param);
		}
		if (isHasDailyCalculateException) {
			this.updateEachTaskStatus(procExecLog, ProcessExecutionTask.DAILY_CALCULATION, EndStatus.ABNORMAL_END);
			// ドメインモデル「エラーメッセージ情報」を取得する
			List<ErrMessageInfo> listErrMessageInfo = errMessageInfoRepository.getAllErrMessageInfoByID(execId,
					ExecutionContent.DAILY_CALCULATION.value);
			ExecutionLogImportFn param = new ExecutionLogImportFn();
			List<ExecutionLogErrorDetailFn> listErrorAndEmpId = new ArrayList<>();
			// 会社ID ＝ パラメータ.更新処理自動実行.会社ID
			param.setCompanyId(companyId);
			// 管理社員ID ＝
			param.setManagerId(listManagementId);
			// 実行完了日時 ＝ システム日時
			param.setFinishDateTime(GeneralDateTime.now());
			// エラーの有無 ＝ エラーあり
			param.setExistenceError(1);
			// 実行内容 ＝ スケジュール作成
			param.setExecutionContent(AlarmCategoryFn.CALCULATION_DAILY_REPORT);
			if (listErrMessageInfo.isEmpty()) {
				this.errMessageInfoRepository.add(new ErrMessageInfo("System", execId, new ErrMessageResource("18"),
						ExecutionContent.DAILY_CALCULATION, GeneralDate.today(),
						new ErrMessageContent(TextResource.localize("Msg_1339"))));
				for (String managementId : listManagementId) {
					listErrorAndEmpId
							.add(new ExecutionLogErrorDetailFn(TextResource.localize("Msg_1339"), managementId));
				}
			} else {
				for (ErrMessageInfo errMessageInfo : listErrMessageInfo) {
					listErrorAndEmpId.add(new ExecutionLogErrorDetailFn(errMessageInfo.getMessageError().v(),
							errMessageInfo.getEmployeeID()));
				}
			}
			param.setTargerEmployee(listErrorAndEmpId);
			// アルゴリズム「実行ログ登録」を実行する 2290 Done
			executionLogAdapterFn.updateExecuteLog(param);

		} else {
			// ドメインモデル「更新処理自動実行ログ」を更新する
			this.updateEachTaskStatus(procExecLog, ProcessExecutionTask.DAILY_CALCULATION, EndStatus.SUCCESS);
			ExecutionLogImportFn param = new ExecutionLogImportFn();
			List<ExecutionLogErrorDetailFn> listErrorAndEmpId = new ArrayList<>();
			// 会社ID ＝ パラメータ.更新処理自動実行.会社ID
			param.setCompanyId(companyId);
			// 管理社員ID ＝
			param.setManagerId(listManagementId);
			// 実行完了日時 ＝ システム日時
			param.setFinishDateTime(GeneralDateTime.now());
			// 実行内容 ＝ 日別実績の計算
			param.setExecutionContent(AlarmCategoryFn.CALCULATION_DAILY_REPORT);
			// エラーの有無 ＝ エラーなし
			param.setExistenceError(0);
			// 実行ログエラー詳細 ＝ NULL
			param.setTargerEmployee(listErrorAndEmpId);
			// アルゴリズム「実行ログ登録」を実行する 2290 Done
			executionLogAdapterFn.updateExecuteLog(param);

		}
		this.procExecLogRepo.update(procExecLog);

		return true;
	}

//	private DatePeriod getMaxPeriodBetweenCalAndCreate(DatePeriod dailyCreate, DatePeriod dailyCalculate) {
//		GeneralDate startDate;
//		GeneralDate endDate;
//		if (dailyCreate.start().compareTo(dailyCalculate.start()) == -1) {
//			startDate = dailyCreate.start();
//		} else {
//			startDate = dailyCalculate.start();
//		}
//		if (dailyCreate.end().compareTo(dailyCalculate.end()) == 1) {
//			endDate = dailyCreate.end();
//		} else {
//			endDate = dailyCalculate.end();
//		}
//		return new DatePeriod(startDate, endDate);
//	}

	// 異動者・勤務種別変更者リスト作成処理
	private ListLeaderOrNotEmpOutput createProcessForChangePerOrWorktype(int closureId, String companyId,
			List<String> empIds, GeneralDate startDate, ProcessExecution procExec) {
		// 期間を計算
		// GeneralDate p = this.calculatePeriod(closureId, period, companyId);
		List<String> newEmpIdList = new ArrayList<>();
		// ・社員ID（異動者、勤務種別変更者のみ）（List）
		Set<String> setEmpIds = new HashSet<String>();
		// ・社員ID（異動者、勤務種別変更者のみ）（List）
		List<String> noLeaderEmpIdList = empIds;
		// check 異動者を再作成する
		if (procExec.getExecSetting().getDailyPerf().getTargetGroupClassification().isRecreateTransfer()) {
			// 異動者の絞り込み todo request list 189
			List<WorkPlaceHistImport> wplByListSidAndPeriod = this.workplaceWorkRecordAdapter
					.getWplByListSidAndPeriod(empIds, new DatePeriod(startDate, GeneralDate.ymd(9999, 12, 31)));
			wplByListSidAndPeriod.forEach(x -> {
				setEmpIds.add(x.getEmployeeId());
			});
		}
		if (procExec.getExecSetting().getDailyPerf().getTargetGroupClassification().isRecreateTypeChangePerson()) {
			// 勤務種別の絞り込み
			newEmpIdList = this.refineWorkType(companyId, empIds, startDate);
		}
		setEmpIds.addAll(newEmpIdList);
		noLeaderEmpIdList.removeAll(new ArrayList<>(newEmpIdList));
		return new ListLeaderOrNotEmpOutput(new ArrayList<>(setEmpIds), noLeaderEmpIdList);
	}

	private DatePeriod findClosureMinMaxPeriod(String companyId, List<Closure> closureList) {
		GeneralDate startYearMonth = null;
		GeneralDate endYearMonth = null;
		for (Closure closure : closureList) {
			DatePeriod datePeriod = this.closureService.getClosurePeriod(closure.getClosureId().value,
					closure.getClosureMonth().getProcessingYm());

			if (startYearMonth == null || datePeriod.start().before(startYearMonth)) {
				startYearMonth = datePeriod.start();
			}

			if (endYearMonth == null || datePeriod.end().after(endYearMonth)) {
				endYearMonth = datePeriod.end();
			}
		}

		return new DatePeriod(startYearMonth, endYearMonth);
	}

	private void updateEachTaskStatus(ProcessExecutionLog procExecLog, ProcessExecutionTask execTask,
			EndStatus status) {
		procExecLog.getTaskLogList().forEach(task -> {
			if (execTask.value == task.getProcExecTask().value) {
				task.setStatus(Optional.ofNullable(status));
			}
		});
	}

	private boolean isAbnormalTermEachTask(ProcessExecutionLog procExecLog) {
		for (ExecutionTaskLog task : procExecLog.getTaskLogList()) {
			if (task.getStatus() != null && task.getStatus().isPresent()
					&& EndStatus.ABNORMAL_END.value == task.getStatus().get().value) {
				return true;
			}
		}
		return false;
	}

	private void initAllTaskStatus(ProcessExecutionLog procExecLog, EndStatus status) {
		if (CollectionUtil.isEmpty(procExecLog.getTaskLogList())) {
			procExecLog.initTaskLogList();
		}
		procExecLog.getTaskLogList().forEach(task -> {
			task.setStatus(Optional.ofNullable(status));
		});
	}

	/**
	 * 個人スケジュール作成期間を計算する
	 * 
	 * @param procExec
	 * @return 期間
	 */
	private DatePeriod calculateSchedulePeriod(ProcessExecution procExec, ProcessExecutionLog procExecLog,boolean checkCreateEmployee) {

		GeneralDate today = GeneralDate.today();
		int targetMonth = procExec.getExecSetting().getPerSchedule().getPeriod().getTargetMonth().value;
		int targetDate = procExec.getExecSetting().getPerSchedule().getPeriod().getTargetDate().v().intValue();
		int startMonth = today.month();
		GeneralDate startDate;
		switch (targetMonth) {
		case 0:
			startMonth = today.month();
			startDate = GeneralDate.ymd(today.year(), startMonth, targetDate);
			break;
		case 1:
			startMonth = today.month();
			startDate = GeneralDate.ymd(today.year(), startMonth, targetDate).addMonths(1);
			break;
		case 2:
			startMonth = today.month();
			startDate = GeneralDate.ymd(today.year(), startMonth, targetDate).addMonths(2);
			break;
		//開始月を指定する の場合
		case 3: 
			PersonalScheduleCreationPeriod creationPeriod =  procExec.getExecSetting().getPerSchedule().getPeriod();
			//個人スケジュール作成期間の年を計算する
			int year = GeneralDate.today().year();
			if(creationPeriod.getDesignatedYear().get() == CreateScheduleYear.FOLLOWING_YEAR ) {
				year = year + 1;
			}
			//個人スケジュール作成期間の月日を計算する
			startDate = GeneralDate.ymd(year, creationPeriod.getStartMonthDay().get().getMonth(), creationPeriod.getStartMonthDay().get().getDay());
			break;
		default:
			startDate = GeneralDate.ymd(today.year(), startMonth, targetDate);
			break;
		}
		int createPeriod = procExec.getExecSetting().getPerSchedule().getPeriod().getCreationPeriod().v().intValue();
		GeneralDate endDate;
		if(targetMonth == TargetMonth.DESIGNATE_START_MONTH.value) {
			PersonalScheduleCreationPeriod creationPeriod =  procExec.getExecSetting().getPerSchedule().getPeriod();
			//個人スケジュール作成期間の年を計算する
			int year = GeneralDate.today().year();
			if(creationPeriod.getDesignatedYear().get() == CreateScheduleYear.FOLLOWING_YEAR ) {
				year = year + 1;
			}
			//個人スケジュール作成期間の月日を計算する
			endDate = GeneralDate.ymd(year, creationPeriod.getStartMonthDay().get().getMonth(), creationPeriod.getStartMonthDay().get().getDay());
		}else {
			if (targetDate == 1) {
				endDate = GeneralDate.ymd(startDate.year(), startDate.month(), 1);
				endDate = GeneralDate.ymd(startDate.year(), startDate.month(), endDate.lastDateInMonth()).addMonths(createPeriod);
			} else {
				GeneralDate dateTest = GeneralDate.ymd(startDate.year(), startDate.month() , 1).addMonths(createPeriod);
				int maxdate = dateTest.lastDateInMonth();
				if(maxdate<(targetDate-1)) {
					targetDate = maxdate;
				}
				endDate = GeneralDate.ymd(startDate.year(), startDate.month() , targetDate).addMonths(createPeriod).addDays(-1);
			}
		}
		//パラメータ「新入社員作成区分」を判断 : true
		if(checkCreateEmployee) {
			//全締めから一番早い期間.開始日を取得する
			//開始日を取得した開始日に置き換える
			startDate = getMinPeriodFromStartDate(AppContexts.user().companyId()).start();
		}

		if (procExecLog.getEachProcPeriod() == null || procExecLog.getEachProcPeriod().isPresent()) {
			procExecLog.setEachProcPeriod(new EachProcessPeriod(new DatePeriod(startDate, endDate), null, null, null));
		} else {
			procExecLog.getEachProcPeriod().get().setScheduleCreationPeriod(new DatePeriod(startDate, endDate));
		}
		// ドメインモデル「更新処理自動実行ログ」を更新する
		// this.procExecLogRepo.update(procExecLog);
		return new DatePeriod(startDate, endDate);
	}

	// 期間を求める
	private DailyCreatAndCalOutput calculateDailyPeriod(ProcessExecution procExec, int closureId,
			CurrentMonth currentMonth) {
		DatePeriod closurePeriod = this.closureService.getClosurePeriod(closureId, currentMonth.getProcessingYm());

		// ドメインモデル「更新処理自動実行.実行設定.日別実績の作成・計算.作成・計算項目」を元に日別作成の期間を作成する
		GeneralDate crtStartDate = null;
		GeneralDate crtEndDate = null;
		// ドメインモデル「更新処理自動実行.実行設定.日別実績の作成・計算.作成・計算項目」を元に日別計算の期間を作成する
		GeneralDate calStartDate = null;
		GeneralDate calEndDate = null;

		GeneralDate lastExecDate = GeneralDate.today();
		GeneralDate today = GeneralDate.today();
		// ドメインモデル「更新処理前回実行日時」を取得する
		Optional<LastExecDateTime> lastDateTimeOpt = lastExecDateTimeRepo.get(procExec.getCompanyId(),
				procExec.getExecItemCd().v());
		if (lastDateTimeOpt.isPresent()) {
			GeneralDateTime lastExecDateTime = lastDateTimeOpt.get().getLastExecDateTime();
			if (lastExecDateTime != null) {
				lastExecDate = GeneralDate.ymd(lastExecDateTime.year(), lastExecDateTime.month(),
						lastExecDateTime.day());
			}
		}

		switch (procExec.getExecSetting().getDailyPerf().getDailyPerfItem()) {
		case FIRST_OPT:
			crtStartDate = lastExecDate;
			crtEndDate = today;
			calStartDate = lastExecDate;
			calEndDate = today;
			break;
		case SECOND_OPT:
			crtStartDate = lastExecDate;
			crtEndDate = today;
			calStartDate = closurePeriod.start();
			calEndDate = today;
			break;
		case THIRD_OPT:
			crtStartDate = closurePeriod.start();
			crtEndDate = today;
			calStartDate = closurePeriod.start();
			calEndDate = today;
			break;
		case FOURTH_OPT:
			crtStartDate = closurePeriod.start();
			crtEndDate = closurePeriod.end();
			calStartDate = closurePeriod.start();
			calEndDate = closurePeriod.end();
			break;
		case FIFTH_OPT:
			crtStartDate = closurePeriod.start();
			crtEndDate = closurePeriod.end().addMonths(1);
			calStartDate = closurePeriod.start();
			calEndDate = closurePeriod.end().addMonths(1);
			break;
		case SIXTH_OPT:
			crtStartDate = closurePeriod.start().addMonths(1);
			crtEndDate = closurePeriod.end().addMonths(1);
			calStartDate = closurePeriod.start().addMonths(1);
			calEndDate = closurePeriod.end().addMonths(1);
			break;
		default:
			break;
		}
		DailyCreatAndCalOutput dailyCreatAndCalOutput = new DailyCreatAndCalOutput();
		dailyCreatAndCalOutput.setDailyCreationPeriod(new DatePeriod(crtStartDate, crtEndDate));
		dailyCreatAndCalOutput.setDailyCalcPeriod(new DatePeriod(calStartDate, calEndDate));
		return dailyCreatAndCalOutput;
	}

	/**
	 * 対象社員を絞り込み
	 * 
	 * @param procExec
	 * @param employeeIdList
	 * @param period
	 */
	private DatePeriod filterEmployeeList(ProcessExecution procExec, List<String> employeeIdList, DatePeriod datePeriod,
			List<String> reEmployeeList, List<String> newEmployeeList, List<String> temporaryEmployeeList) {

		String companyId = AppContexts.user().companyId();
		/** 作成対象の判定 */
		if (procExec.getExecSetting().getPerSchedule().getTarget()
				.getCreationTarget().value == TargetClassification.ALL.value) {
			return null;
		} else {
			Set<String> listSetReEmployeeList = new HashSet<>();
			// ドメインモデル「就業締め日」を取得する
			List<Closure> closureList = this.closureRepo.findAllActive(companyId, UseClassification.UseClass_Use);
			DatePeriod closurePeriod = this.findClosureMinMaxPeriod(companyId, closureList);

			DatePeriod newClosurePeriod = new DatePeriod(closurePeriod.start(), GeneralDate.ymd(9999, 12, 31));

			TargetSetting setting = procExec.getExecSetting().getPerSchedule().getTarget().getTargetSetting();
			// 異動者を再作成するか判定
			if (setting.isRecreateTransfer()) {
				// Imported(勤務実績)「所属職場履歴」を取得する : 異動者の絞り込み
				List<WorkPlaceHistImport> wkpImportList = this.workplaceAdapter.getWplByListSidAndPeriod(employeeIdList,
						newClosurePeriod);

				// 「全締めの期間.開始日年月日」以降に「所属職場履歴.期間.開始日」が存在する
				wkpImportList.forEach(emp -> {
					for (String empId : employeeIdList) {
						if (empId.equals(emp.getEmployeeId())) {
							listSetReEmployeeList.add(emp.getEmployeeId());
							break;
						}
					}
					// final List<WorkPlaceIdAndPeriodImport> subList = emp.getLstWkpIdAndPeriod();
					// for (WorkPlaceIdAndPeriodImport period : subList) {
					// if (period.getDatePeriod().start().afterOrEquals(closurePeriod.start())) {
					// // 取得したImported（勤務実績）「所属職場履歴」.社員IDを異動者とする
					// reEmployeeList.add(emp.getEmployeeId());
					// break;
					// }
					// }
				});
			}

			// 勤務種別変更者を再作成するか判定
			if (setting.isRecreateWorkType()) {
				employeeIdList.forEach(x -> {
					// ドメインモデル「社員の勤務種別の履歴」を取得する
					Optional<BusinessTypeOfEmployeeHistory> optional = this.typeEmployeeOfHistoryRepos
							.findByEmployeeDesc(AppContexts.user().companyId(), x);
					if (optional.isPresent()) {
						for (DateHistoryItem history : optional.get().getHistory()) {
							// 「全締めの期間.開始日年月日」以降に「社員の勤務種別の履歴.履歴.期間.開始日」が存在する
							if (history.start().beforeOrEquals(closurePeriod.start())) {
								// 取得したImported（勤務実績）「所属職場履歴」.社員IDを異動者とする
								listSetReEmployeeList.add(optional.get().getEmployeeId());
								break;
							}
						}
					}
				});
			}
			// 休職者・休業者を再作成するか判定
			// TODO:chua lam
			// 新入社員を作成するか判定
			if (setting.isCreateEmployee()) {
				// request list 211
				// Imported「所属会社履歴（社員別）」を取得する
				List<nts.uk.ctx.at.record.dom.adapter.company.AffCompanyHistImport> employeeHistList = this.syCompanyRecordAdapter
						.getAffCompanyHistByEmployee(employeeIdList, newClosurePeriod);
				// 取得したドメインモデル「所属開始履歴（社員別）」.社員IDを新入社員とする
				employeeHistList.forEach(x -> newEmployeeList.add(x.getEmployeeId()));

				// 社員ID（新入社員以外）（List）
				for (String empID : employeeIdList) {
					boolean checkNotExist = true;
					for (String newEmpID : newEmployeeList) {
						if (empID.equals(newEmpID)) {
							checkNotExist = false;
							break;
						}
					}
					if (checkNotExist) {
						temporaryEmployeeList.add(empID);
					}
				}

			}
			// 社員ID（異動者、勤務種別変更者、休職者・休業者）（List）から重複している社員IDを1つになるよう削除する
			List<String> temp = new ArrayList<String>(listSetReEmployeeList);
			reEmployeeList.addAll(temp);
			return closurePeriod;
		}

	}

	// 承認結果反映
	private boolean reflectApprovalResult(String execId, ProcessExecution processExecution,
			ProcessExecutionLog ProcessExecutionLog, String companyId) {
		// ドメインモデル「更新処理自動実行ログ」を更新する
		List<ExecutionTaskLog> taskLogLists = ProcessExecutionLog.getTaskLogList();
		int size = taskLogLists.size();
		boolean existExecutionTaskLog = false;
		for (int i = 0; i < size; i++) {
			ExecutionTaskLog executionTaskLog = taskLogLists.get(i);
			// 承認結果反映
			if (executionTaskLog.getProcExecTask().value == 3) {
				executionTaskLog.setStatus(null);
				existExecutionTaskLog = true;
			}
		}
		if (!existExecutionTaskLog) {
			taskLogLists.add(new ExecutionTaskLog(ProcessExecutionTask.RFL_APR_RESULT, null));
		}
		this.procExecLogRepo.update(ProcessExecutionLog);

		boolean reflectResultCls = processExecution.getExecSetting().isReflectResultCls();
		if (!reflectResultCls) {
			// ドメインモデル「更新処理自動実行ログ」を更新する
			for (int i = 0; i < size; i++) {
				ExecutionTaskLog executionTaskLog = taskLogLists.get(i);
				// 承認結果反映
				if (executionTaskLog.getProcExecTask().value == 3) {
					// 未実施
					executionTaskLog.setStatus(Optional.ofNullable(EndStatus.NOT_IMPLEMENT));
				}
			}
			this.procExecLogRepo.update(ProcessExecutionLog);
			return false; // chua confirm
		}
		// ドメインモデル「就業締め日」を取得する
		List<Closure> lstClosure = this.closureRepo.findAllUse(companyId);

		// ドメインモデル「実行ログ」を新規登録する
		DatePeriod period = this.findClosurePeriodMinDate(companyId, lstClosure);
		GeneralDateTime now = GeneralDateTime.now();
		ExecutionLog executionLog = new ExecutionLog(execId, ExecutionContent.REFLRCT_APPROVAL_RESULT,
				ErrorPresent.NO_ERROR, new ExecutionTime(now, now), ExecutionStatus.INCOMPLETE,
				new ObjectPeriod(period.start(), period.end()));
		executionLog.setReflectApprovalSetInfo(new SetInforReflAprResult(ExecutionContent.REFLRCT_APPROVAL_RESULT,
				ExecutionType.NORMAL_EXECUTION, IdentifierUtil.randomUniqueId(), false));
		this.executionLogRepository.addExecutionLog(executionLog);
		// ドメインモデル「更新処理自動実行ログ」を更新する
		if (ProcessExecutionLog.getEachProcPeriod() != null && ProcessExecutionLog.getEachProcPeriod().isPresent()) {
			EachProcessPeriod eachProcessPeriod = ProcessExecutionLog.getEachProcPeriod().get();
			DatePeriod scheduleCreationPeriod = (eachProcessPeriod.getScheduleCreationPeriod() != null
					&& eachProcessPeriod.getScheduleCreationPeriod().isPresent())
							? eachProcessPeriod.getScheduleCreationPeriod().get()
							: null;
			DatePeriod dailyCreationPeriod = (eachProcessPeriod.getDailyCreationPeriod() != null
					&& eachProcessPeriod.getDailyCreationPeriod().isPresent())
							? eachProcessPeriod.getDailyCreationPeriod().get()
							: null;
			DatePeriod dailyCalcPeriod = (eachProcessPeriod.getDailyCalcPeriod() != null
					&& eachProcessPeriod.getDailyCalcPeriod().isPresent())
							? eachProcessPeriod.getDailyCalcPeriod().get()
							: null;
			ProcessExecutionLog.setEachProcPeriod(
					new EachProcessPeriod(scheduleCreationPeriod, dailyCreationPeriod, dailyCalcPeriod, period));

		} else {
			ProcessExecutionLog.setEachProcPeriod(new EachProcessPeriod(null, null, null, period));
		}

		boolean isHasException = false;
		boolean endStatusIsInterrupt = false;
		// 就業担当者の社員ID（List）を取得する : RQ526
		List<String> listManagementId = employeeManageAdapter.getListEmpID(companyId, GeneralDate.today());
		try {
			int sizeClosure = lstClosure.size();
			for (int i = 0; i < sizeClosure; i++) {
				Closure closure = lstClosure.get(i);
				// 雇用コードを取得する ~ 締めに紐付く雇用コード一覧を取得
				List<ClosureEmployment> employmentList = this.closureEmpRepo.findByClosureId(companyId,
						closure.getClosureId().value);
				List<String> lstEmploymentCode = new ArrayList<String>();
				employmentList.forEach(x -> {
					lstEmploymentCode.add(x.getEmploymentCD());
				});

				// <<Public>> 就業条件で社員を検索して並び替える
				RegulationInfoEmployeeAdapterImport regulationInfoEmployeeAdapterImport = new RegulationInfoEmployeeAdapterImport();
				if (processExecution.getExecScope().getExecScopeCls().value == 1) {
					// 【更新処理自動実行.実行範囲.実行範囲区分 ＝ 職場 の場合】
					// 基準日 → システム日付
					regulationInfoEmployeeAdapterImport.setBaseDate(now);
					// 検索参照範囲 → 参照範囲を考慮しない
					regulationInfoEmployeeAdapterImport.setReferenceRange(3);
					// 雇用で絞り込む → TRUE
					regulationInfoEmployeeAdapterImport.setFilterByEmployment(true);
					// 雇用コード一覧 → 取得した雇用コード（List)
					regulationInfoEmployeeAdapterImport.setEmploymentCodes(lstEmploymentCode);
					// 部門で絞り込む → FALSE
					regulationInfoEmployeeAdapterImport.setFilterByDepartment(false);
					// 部門ID一覧 → なし
					regulationInfoEmployeeAdapterImport.setDepartmentCodes(null);
					// 職場で絞り込む → TRUE
					regulationInfoEmployeeAdapterImport.setFilterByWorkplace(true);

					List<ProcessExecutionScopeItem> workplaceIdList = processExecution.getExecScope()
							.getWorkplaceIdList();
					List<String> workplaceIds = new ArrayList<String>();
					workplaceIdList.forEach(x -> {
						workplaceIds.add(x.getWkpId());
					});
					// 職場ID一覧 → ドメインモデル「更新処理自動実行」に登録されている職場ID
					regulationInfoEmployeeAdapterImport.setWorkplaceCodes(workplaceIds);
					// 分類で絞り込む → FALSE
					regulationInfoEmployeeAdapterImport.setFilterByClassification(false);
					// 分類コード一覧 → なし
					regulationInfoEmployeeAdapterImport.setClassificationCodes(null);
					// 職位で絞り込む → FALSE
					regulationInfoEmployeeAdapterImport.setFilterByJobTitle(false);
					// 職位ID一覧 → なし
					regulationInfoEmployeeAdapterImport.setJobTitleCodes(null);
					// 在職・休職・休業のチェック期間 → 作成した期間
					regulationInfoEmployeeAdapterImport.setPeriodStart(period.start());
					regulationInfoEmployeeAdapterImport.setPeriodEnd(period.end());
					// 在職者を含める → TRUE
					regulationInfoEmployeeAdapterImport.setIncludeIncumbents(true);
					// 休職者を含める → FALSE
					regulationInfoEmployeeAdapterImport.setIncludeWorkersOnLeave(false);
					// 休業者を含める → FALSE
					regulationInfoEmployeeAdapterImport.setIncludeOccupancy(false);
					// 出向に来ている社員を含める → TRUE
					regulationInfoEmployeeAdapterImport.setIncludeAreOnLoan(true);
					// 出向に行っている社員を含める → FALSE
					regulationInfoEmployeeAdapterImport.setIncludeGoingOnLoan(false);
					// 退職者を含める → FALSE
					regulationInfoEmployeeAdapterImport.setIncludeRetirees(false);
					// 退職日のチェック期間 → 作成した期間
					regulationInfoEmployeeAdapterImport.setRetireStart(period.start());
					regulationInfoEmployeeAdapterImport.setRetireEnd(period.end());
					// 並び順NO → 1
					regulationInfoEmployeeAdapterImport.setSortOrderNo(1);
					regulationInfoEmployeeAdapterImport.setSystemType(2);
					// 勤務種別で絞り込む → FALSE
					regulationInfoEmployeeAdapterImport.setFilterByWorktype(false);

					// 氏名の種類 → ビジネスネーム日本語
					regulationInfoEmployeeAdapterImport.setNameType("ビジネスネーム日本語");

				} else {
					// 【更新処理自動実行.実行範囲.実行範囲区分 ＝ 会社 の場合】
					// 基準日 → システム日付
					regulationInfoEmployeeAdapterImport.setBaseDate(now);
					// 検索参照範囲 → 参照範囲を考慮しない
					regulationInfoEmployeeAdapterImport.setReferenceRange(3);
					// 雇用で絞り込む → TRUE
					regulationInfoEmployeeAdapterImport.setFilterByEmployment(true);
					// 雇用コード一覧 → 取得した雇用コード（List)
					regulationInfoEmployeeAdapterImport.setEmploymentCodes(lstEmploymentCode);
					// 部門で絞り込む → FALSE
					regulationInfoEmployeeAdapterImport.setFilterByDepartment(false);
					// 部門ID一覧 → なし
					regulationInfoEmployeeAdapterImport.setDepartmentCodes(null);
					// 職場で絞り込む → FALSE
					regulationInfoEmployeeAdapterImport.setFilterByWorkplace(false);
					// 職場ID一覧 → なし
					regulationInfoEmployeeAdapterImport.setWorkplaceCodes(null);
					// 分類で絞り込む → FALSE
					regulationInfoEmployeeAdapterImport.setFilterByClassification(false);
					// 分類コード一覧 → なし
					regulationInfoEmployeeAdapterImport.setClassificationCodes(null);
					// 職位で絞り込む → FALSE
					regulationInfoEmployeeAdapterImport.setFilterByJobTitle(false);
					// 職位ID一覧 → なし
					regulationInfoEmployeeAdapterImport.setJobTitleCodes(null);
					// 在職・休職・休業のチェック期間 → 作成した期間
					regulationInfoEmployeeAdapterImport.setPeriodStart(period.start());
					regulationInfoEmployeeAdapterImport.setPeriodEnd(period.end());
					// 在職者を含める → TRUE
					regulationInfoEmployeeAdapterImport.setIncludeIncumbents(true);
					// 休職者を含める → FALSE
					regulationInfoEmployeeAdapterImport.setIncludeWorkersOnLeave(false);
					// 休業者を含める → FALSE
					regulationInfoEmployeeAdapterImport.setIncludeOccupancy(false);
					// 出向に来ている社員を含める → TRUE
					regulationInfoEmployeeAdapterImport.setIncludeAreOnLoan(true);
					// 出向に行っている社員を含める → FALSE
					regulationInfoEmployeeAdapterImport.setIncludeGoingOnLoan(false);
					// 退職者を含める → FALSE
					regulationInfoEmployeeAdapterImport.setIncludeRetirees(false);
					// 退職日のチェック期間 → 作成した期間
					regulationInfoEmployeeAdapterImport.setRetireStart(period.start());
					regulationInfoEmployeeAdapterImport.setRetireEnd(period.end());
					// 並び順NO → 1
					regulationInfoEmployeeAdapterImport.setSortOrderNo(1);
					regulationInfoEmployeeAdapterImport.setSystemType(2);
					// 勤務種別で絞り込む → FALSE
					regulationInfoEmployeeAdapterImport.setFilterByWorktype(false);
					// 氏名の種類 → ビジネスネーム日本語
					regulationInfoEmployeeAdapterImport.setNameType("ビジネスネーム日本語");

				}
				// <<Public>> 就業条件で社員を検索して並び替える
				List<RegulationInfoEmployeeAdapterDto> lstRegulationInfoEmployee = this.regulationInfoEmployeeAdapter
						.find(regulationInfoEmployeeAdapterImport);

				int sizeEmployee = lstRegulationInfoEmployee.size();
				for (int j = 0; j < sizeEmployee; j++) {
					RegulationInfoEmployeeAdapterDto regulationInfoEmployeeAdapterDto = lstRegulationInfoEmployee
							.get(j);
					// 期間を作成する
					// 社員に対応する締め期間を取得す
					DatePeriod datePeriod = this.closureEmploymentService
							.findClosurePeriod(regulationInfoEmployeeAdapterDto.getEmployeeId(), GeneralDate.today());
					// outputされた期間の終了日を「9999/12/31」に変更する
					DatePeriod newDatePeriod = new DatePeriod(datePeriod.start(), GeneralDate.ymd(9999, 12, 31));

					// 社員の申請を反映 cua chi du
					// AppReflectManager.reflectEmployeeOfApp
					ProcessStateReflectImport processStateReflectImport = appReflectManagerAdapter
							.reflectAppOfEmployeeTotal(execId, regulationInfoEmployeeAdapterDto.getEmployeeId(),
									newDatePeriod);
					// fixed endStatusIsInterrupt =true (終了状態 ＝ 中断)
					if (processStateReflectImport == ProcessStateReflectImport.INTERRUPTION) {
						endStatusIsInterrupt = true;
					}
					if (endStatusIsInterrupt) {
						break;
					}
				}
				if (endStatusIsInterrupt) {
					break;
				}
			}
		} catch (Exception e) {
			isHasException = true;
		}

		if (endStatusIsInterrupt) {
			return true; // 終了状態 ＝ 中断
		}
		if (isHasException) {
			// ドメインモデル「更新処理自動実行ログ」を更新する
			for (int i = 0; i < size; i++) {
				ExecutionTaskLog executionTaskLog = taskLogLists.get(i);
				// 承認結果反映
				if (executionTaskLog.getProcExecTask().value == 3) {
					// 異常終了
					executionTaskLog.setStatus(Optional.ofNullable(EndStatus.ABNORMAL_END));
				}
			}
			this.procExecLogRepo.update(ProcessExecutionLog);
			// ドメインモデル「エラーメッセージ情報」を取得する
			List<ErrMessageInfo> listErrMessageInfo = errMessageInfoRepository.getAllErrMessageInfoByID(execId,
					ExecutionContent.REFLRCT_APPROVAL_RESULT.value);
			ExecutionLogImportFn param = new ExecutionLogImportFn();
			List<ExecutionLogErrorDetailFn> listErrorAndEmpId = new ArrayList<>();

			// 会社ID ＝ パラメータ.更新処理自動実行.会社ID
			param.setCompanyId(companyId);
			// 管理社員ID ＝
			param.setManagerId(listManagementId);
			// 実行完了日時 ＝ システム日時
			param.setFinishDateTime(GeneralDateTime.now());
			// エラーの有無 ＝ エラーあり
			param.setExistenceError(1);
			// 実行内容 ＝ 承認結果の反映
			param.setExecutionContent(AlarmCategoryFn.REFLECT_APPROVAL_RESULT);

			if (listErrMessageInfo.isEmpty()) {
				this.errMessageInfoRepository.add(new ErrMessageInfo("System", execId, new ErrMessageResource("18"),
						ExecutionContent.REFLRCT_APPROVAL_RESULT, GeneralDate.today(),
						new ErrMessageContent(TextResource.localize("Msg_1339"))));
				for (String managementId : listManagementId) {
					listErrorAndEmpId
							.add(new ExecutionLogErrorDetailFn(TextResource.localize("Msg_1339"), managementId));
				}
			} else {
				for (ErrMessageInfo errMessageInfo : listErrMessageInfo) {
					listErrorAndEmpId.add(new ExecutionLogErrorDetailFn(errMessageInfo.getMessageError().v(),
							errMessageInfo.getEmployeeID()));
				}

			}

			param.setTargerEmployee(listErrorAndEmpId);
			// アルゴリズム「実行ログ登録」を実行する 2290 Done
			executionLogAdapterFn.updateExecuteLog(param);

		} else {
			// ドメインモデル「更新処理自動実行ログ」を更新する
			for (int i = 0; i < size; i++) {
				ExecutionTaskLog executionTaskLog = taskLogLists.get(i);
				// 承認結果反映
				if (executionTaskLog.getProcExecTask().value == 3) {
					// 正常終了
					executionTaskLog.setStatus(Optional.ofNullable(EndStatus.SUCCESS));
				}
			}
			this.procExecLogRepo.update(ProcessExecutionLog);
			ExecutionLogImportFn param = new ExecutionLogImportFn();
			List<ExecutionLogErrorDetailFn> listErrorAndEmpId = new ArrayList<>();
			// 会社ID ＝ パラメータ.更新処理自動実行.会社ID
			param.setCompanyId(companyId);
			// 管理社員ID ＝
			param.setManagerId(listManagementId);
			// 実行完了日時 ＝ システム日時
			param.setFinishDateTime(GeneralDateTime.now());
			// 実行内容 ＝ 承認結果の反映
			param.setExecutionContent(AlarmCategoryFn.REFLECT_APPROVAL_RESULT);
			// エラーの有無 ＝ エラーなし
			param.setExistenceError(0);
			// 実行ログエラー詳細 ＝ NULL
			param.setTargerEmployee(listErrorAndEmpId);
			// アルゴリズム「実行ログ登録」を実行する 2290 Done
			executionLogAdapterFn.updateExecuteLog(param);

		}

		return false; // 終了状態 !＝ 中断
	}

	// 月別集計
	private boolean monthlyAggregation(String execId, ProcessExecution processExecution,
			ProcessExecutionLog ProcessExecutionLog, String companyId,
			CommandHandlerContext<ExecuteProcessExecutionCommand> context) {
		// ドメインモデル「更新処理自動実行ログ」を更新する
		List<ExecutionTaskLog> taskLogLists = ProcessExecutionLog.getTaskLogList();
		int size = taskLogLists.size();
		boolean existExecutionTaskLog = false;
		for (int i = 0; i < size; i++) {
			ExecutionTaskLog executionTaskLog = taskLogLists.get(i);
			// 月別集計
			if (executionTaskLog.getProcExecTask().value == 4) {
				executionTaskLog.setStatus(null);
				existExecutionTaskLog = true;
			}
		}
		if (!existExecutionTaskLog) {
			taskLogLists.add(new ExecutionTaskLog(ProcessExecutionTask.MONTHLY_AGGR, null));
		}
		String execItemCd = context.getCommand().getExecItemCd();
		List<ExecutionTaskLog> taskLogList = this.execTaskLogRepo.getAllByCidExecCdExecId(companyId, execItemCd,
				execId);
		if (CollectionUtil.isEmpty(taskLogList)) {
			this.execTaskLogRepo.insertAll(companyId, execItemCd, execId, ProcessExecutionLog.getTaskLogList());
		} else {
			this.execTaskLogRepo.updateAll(companyId, execItemCd, execId, ProcessExecutionLog.getTaskLogList());
		}

		this.procExecLogRepo.update(ProcessExecutionLog);
		// 月別集計の判定
		boolean reflectResultCls = processExecution.getExecSetting().isMonthlyAggCls();
		if (!reflectResultCls) {
			// ドメインモデル「更新処理自動実行ログ」を更新する
			for (int i = 0; i < size; i++) {
				ExecutionTaskLog executionTaskLog = taskLogLists.get(i);
				// 月別集計
				if (executionTaskLog.getProcExecTask().value == 4) {
					// 未実施
					executionTaskLog.setStatus(Optional.ofNullable(EndStatus.NOT_IMPLEMENT));
				}
			}
			this.procExecLogRepo.update(ProcessExecutionLog);
			return false; // chua confirm
		}
		// ドメインモデル「就業締め日」を取得する
		List<Closure> lstClosure = this.closureRepo.findAllUse(companyId);

		// ドメインモデル「実行ログ」を新規登録する
		DatePeriod period = this.findClosureMinMaxPeriod(companyId, lstClosure);
		GeneralDateTime now = GeneralDateTime.now();
		ExecutionLog executionLog = new ExecutionLog(execId, ExecutionContent.MONTHLY_AGGREGATION,
				ErrorPresent.NO_ERROR, new ExecutionTime(now, now), ExecutionStatus.INCOMPLETE,
				new ObjectPeriod(period.start(), period.end()));
		executionLog.setMonlyAggregationSetInfo(new CalExeSettingInfor(ExecutionContent.MONTHLY_AGGREGATION,
				ExecutionType.NORMAL_EXECUTION, IdentifierUtil.randomUniqueId()));
		this.executionLogRepository.addExecutionLog(executionLog);

		boolean isHasException = false;
		//boolean endStatusIsInterrupt = false;
		List<Boolean> listCheck = new ArrayList<>();
		// 就業担当者の社員ID（List）を取得する : RQ526
		List<String> listManagementId = employeeManageAdapter.getListEmpID(companyId, GeneralDate.today());
		try {
			int sizeClosure = lstClosure.size();
			for (int i = 0; i < sizeClosure; i++) {
				Closure closure = lstClosure.get(i);
				// 雇用コードを取得する ~ 締めに紐付く雇用コード一覧を取得
				List<ClosureEmployment> employmentList = this.closureEmpRepo.findByClosureId(companyId,
						closure.getClosureId().value);
				List<String> lstEmploymentCode = new ArrayList<String>();
				employmentList.forEach(x -> {
					lstEmploymentCode.add(x.getEmploymentCD());
				});

				// <<Public>> 就業条件で社員を検索して並び替える
				RegulationInfoEmployeeAdapterImport regulationInfoEmployeeAdapterImport = new RegulationInfoEmployeeAdapterImport();
				if (processExecution.getExecScope().getExecScopeCls().value == 1) {
					// 【更新処理自動実行.実行範囲.実行範囲区分 ＝ 職場 の場合】
					// 基準日 → システム日付
					regulationInfoEmployeeAdapterImport.setBaseDate(now);
					// 検索参照範囲 → 参照範囲を考慮しない
					regulationInfoEmployeeAdapterImport.setReferenceRange(3);
					// 雇用で絞り込む → TRUE
					regulationInfoEmployeeAdapterImport.setFilterByEmployment(true);
					// 雇用コード一覧 → 取得した雇用コード（List)
					regulationInfoEmployeeAdapterImport.setEmploymentCodes(lstEmploymentCode);
					// 部門で絞り込む → FALSE
					regulationInfoEmployeeAdapterImport.setFilterByDepartment(false);
					// 部門ID一覧 → なし
					regulationInfoEmployeeAdapterImport.setDepartmentCodes(null);
					// 職場で絞り込む → TRUE
					regulationInfoEmployeeAdapterImport.setFilterByWorkplace(true);

					List<ProcessExecutionScopeItem> workplaceIdList = processExecution.getExecScope()
							.getWorkplaceIdList();
					List<String> workplaceIds = new ArrayList<String>();
					workplaceIdList.forEach(x -> {
						workplaceIds.add(x.getWkpId());
					});
					// 職場ID一覧 → ドメインモデル「更新処理自動実行」に登録されている職場ID
					regulationInfoEmployeeAdapterImport.setWorkplaceCodes(workplaceIds);
					// 分類で絞り込む → FALSE
					regulationInfoEmployeeAdapterImport.setFilterByClassification(false);
					// 分類コード一覧 → なし
					regulationInfoEmployeeAdapterImport.setClassificationCodes(null);
					// 職位で絞り込む → FALSE
					regulationInfoEmployeeAdapterImport.setFilterByJobTitle(false);
					// 職位ID一覧 → なし
					regulationInfoEmployeeAdapterImport.setJobTitleCodes(null);
					// 在職・休職・休業のチェック期間 → 作成した期間
					regulationInfoEmployeeAdapterImport.setPeriodStart(period.start());
					regulationInfoEmployeeAdapterImport.setPeriodEnd(period.end());
					// 在職者を含める → TRUE
					regulationInfoEmployeeAdapterImport.setIncludeIncumbents(true);
					// 休職者を含める → FALSE
					regulationInfoEmployeeAdapterImport.setIncludeWorkersOnLeave(false);
					// 休業者を含める → FALSE
					regulationInfoEmployeeAdapterImport.setIncludeOccupancy(false);
					// 出向に来ている社員を含める → TRUE
					regulationInfoEmployeeAdapterImport.setIncludeAreOnLoan(true);
					// 出向に行っている社員を含める → FALSE
					regulationInfoEmployeeAdapterImport.setIncludeGoingOnLoan(false);
					// 退職者を含める → FALSE
					regulationInfoEmployeeAdapterImport.setIncludeRetirees(false);
					// 退職日のチェック期間 → 作成した期間
					regulationInfoEmployeeAdapterImport.setRetireStart(period.start());
					regulationInfoEmployeeAdapterImport.setRetireEnd(period.end());
					// 並び順NO → 1
					regulationInfoEmployeeAdapterImport.setSortOrderNo(1);
					regulationInfoEmployeeAdapterImport.setSystemType(2);
					// 勤務種別で絞り込む → FALSE
					regulationInfoEmployeeAdapterImport.setFilterByWorktype(false);
					// 氏名の種類 → ビジネスネーム日本語
					regulationInfoEmployeeAdapterImport.setNameType("ビジネスネーム日本語");

				} else {
					// 【更新処理自動実行.実行範囲.実行範囲区分 ＝ 会社 の場合】
					// 基準日 → システム日付
					regulationInfoEmployeeAdapterImport.setBaseDate(now);
					// 検索参照範囲 → 参照範囲を考慮しない
					regulationInfoEmployeeAdapterImport.setReferenceRange(3);
					// 雇用で絞り込む → TRUE
					regulationInfoEmployeeAdapterImport.setFilterByEmployment(true);
					// 雇用コード一覧 → 取得した雇用コード（List)
					regulationInfoEmployeeAdapterImport.setEmploymentCodes(lstEmploymentCode);
					// 部門で絞り込む → FALSE
					regulationInfoEmployeeAdapterImport.setFilterByDepartment(false);
					// 部門ID一覧 → なし
					regulationInfoEmployeeAdapterImport.setDepartmentCodes(null);
					// 職場で絞り込む → FALSE
					regulationInfoEmployeeAdapterImport.setFilterByWorkplace(false);
					// 職場ID一覧 → なし
					regulationInfoEmployeeAdapterImport.setWorkplaceCodes(null);
					// 分類で絞り込む → FALSE
					regulationInfoEmployeeAdapterImport.setFilterByClassification(false);
					// 分類コード一覧 → なし
					regulationInfoEmployeeAdapterImport.setClassificationCodes(null);
					// 職位で絞り込む → FALSE
					regulationInfoEmployeeAdapterImport.setFilterByJobTitle(false);
					// 職位ID一覧 → なし
					regulationInfoEmployeeAdapterImport.setJobTitleCodes(null);
					// 在職・休職・休業のチェック期間 → 作成した期間
					regulationInfoEmployeeAdapterImport.setPeriodStart(period.start());
					regulationInfoEmployeeAdapterImport.setPeriodEnd(period.end());
					// 在職者を含める → TRUE
					regulationInfoEmployeeAdapterImport.setIncludeIncumbents(true);
					// 休職者を含める → FALSE
					regulationInfoEmployeeAdapterImport.setIncludeWorkersOnLeave(false);
					// 休業者を含める → FALSE
					regulationInfoEmployeeAdapterImport.setIncludeOccupancy(false);
					// 出向に来ている社員を含める → TRUE
					regulationInfoEmployeeAdapterImport.setIncludeAreOnLoan(true);
					// 出向に行っている社員を含める → FALSE
					regulationInfoEmployeeAdapterImport.setIncludeGoingOnLoan(false);
					// 退職者を含める → FALSE
					regulationInfoEmployeeAdapterImport.setIncludeRetirees(false);
					// 退職日のチェック期間 → 作成した期間
					regulationInfoEmployeeAdapterImport.setRetireStart(period.start());
					regulationInfoEmployeeAdapterImport.setRetireEnd(period.end());
					// 並び順NO → 1
					regulationInfoEmployeeAdapterImport.setSortOrderNo(1);
					regulationInfoEmployeeAdapterImport.setSystemType(2);
					// 勤務種別で絞り込む → FALSE
					regulationInfoEmployeeAdapterImport.setFilterByWorktype(false);
					// 氏名の種類 → ビジネスネーム日本語
					regulationInfoEmployeeAdapterImport.setNameType("ビジネスネーム日本語");

				}

				// <<Public>> 就業条件で社員を検索して並び替える
				List<RegulationInfoEmployeeAdapterDto> lstRegulationInfoEmployee = this.regulationInfoEmployeeAdapter
						.find(regulationInfoEmployeeAdapterImport);

				// int sizeEmployee = lstRegulationInfoEmployee.size();
				this.managedParallelWithContext.forEach(
						ControlOption.custom().millisRandomDelay(MAX_DELAY_PARALLEL),
						lstRegulationInfoEmployee,
						item -> {
							RegulationInfoEmployeeAdapterDto regulationInfoEmployeeAdapterDto = item;
							AsyncCommandHandlerContext<ExecuteProcessExecutionCommand> asyContext = (AsyncCommandHandlerContext<ExecuteProcessExecutionCommand>) context;
							ProcessState aggregate = monthlyService.aggregate(asyContext, companyId,
									regulationInfoEmployeeAdapterDto.getEmployeeId(), GeneralDate.legacyDate(now.date()),
									execId, ExecutionType.NORMAL_EXECUTION);
							// 中断
							if (aggregate.value == 0) {
								//endStatusIsInterrupt = true;
								listCheck.add(true);
								//break;
								return;
							}
							
						});
//				for (int j = 0; j < sizeEmployee; j++) {
//					RegulationInfoEmployeeAdapterDto regulationInfoEmployeeAdapterDto = lstRegulationInfoEmployee
//							.get(j);
//					AsyncCommandHandlerContext<ExecuteProcessExecutionCommand> asyContext = (AsyncCommandHandlerContext<ExecuteProcessExecutionCommand>) context;
//					ProcessState aggregate = monthlyService.aggregate(asyContext, companyId,
//							regulationInfoEmployeeAdapterDto.getEmployeeId(), GeneralDate.legacyDate(now.date()),
//							execId, ExecutionType.NORMAL_EXECUTION);
//					// 中断
//					if (aggregate.value == 0) {
//						endStatusIsInterrupt = true;
//						break;
//					}
//				}
//				if (endStatusIsInterrupt) {
//					break;
//				}
				if(!listCheck.isEmpty()) {
					if (listCheck.get(0)) {
						break;
					}
				}
			}
		} catch (Exception e) {
			isHasException = true;
		}
		if(!listCheck.isEmpty()) {
			if (listCheck.get(0)) {
				return true; // 終了状態 ＝ 中断
			}
		}
//		if (listCheck.isEmpty()) {
//			return true; // 終了状態 ＝ 中断
//		}
		if (isHasException) {
			// ドメインモデル「更新処理自動実行ログ」を更新する
			for (int i = 0; i < size; i++) {
				ExecutionTaskLog executionTaskLog = taskLogLists.get(i);
				// 月別集計
				if (executionTaskLog.getProcExecTask().value == 4) {
					// 異常終了
					executionTaskLog.setStatus(Optional.ofNullable(EndStatus.ABNORMAL_END));
				}
			}
			this.procExecLogRepo.update(ProcessExecutionLog);
			// ドメインモデル「エラーメッセージ情報」を取得する
			List<ErrMessageInfo> listErrMessageInfo = errMessageInfoRepository.getAllErrMessageInfoByID(execId,
					ExecutionContent.MONTHLY_AGGREGATION.value);

			ExecutionLogImportFn param = new ExecutionLogImportFn();
			List<ExecutionLogErrorDetailFn> listErrorAndEmpId = new ArrayList<>();

			// 会社ID ＝ パラメータ.更新処理自動実行.会社ID
			param.setCompanyId(companyId);
			// 管理社員ID ＝
			param.setManagerId(listManagementId);
			// 実行完了日時 ＝ システム日時
			param.setFinishDateTime(GeneralDateTime.now());
			// エラーの有無 ＝ エラーあり
			param.setExistenceError(1);
			// 実行内容 ＝ 月別実績の集計
			param.setExecutionContent(AlarmCategoryFn.AGGREGATE_RESULT_MONTH);

			if (listErrMessageInfo.isEmpty()) {
				this.errMessageInfoRepository.add(new ErrMessageInfo("System", execId, new ErrMessageResource("18"),
						ExecutionContent.MONTHLY_AGGREGATION, GeneralDate.today(),
						new ErrMessageContent(TextResource.localize("Msg_1339"))));
				for (String managementId : listManagementId) {
					listErrorAndEmpId
							.add(new ExecutionLogErrorDetailFn(TextResource.localize("Msg_1339"), managementId));
				}
			} else {
				for (ErrMessageInfo errMessageInfo : listErrMessageInfo) {
					listErrorAndEmpId.add(new ExecutionLogErrorDetailFn(errMessageInfo.getMessageError().v(),
							errMessageInfo.getEmployeeID()));
				}
			}
			param.setTargerEmployee(listErrorAndEmpId);
			// アルゴリズム「実行ログ登録」を実行する 2290 Done
			executionLogAdapterFn.updateExecuteLog(param);
		} else {
			// ドメインモデル「更新処理自動実行ログ」を更新する
			for (int i = 0; i < size; i++) {
				ExecutionTaskLog executionTaskLog = taskLogLists.get(i);
				// 月別集計
				if (executionTaskLog.getProcExecTask().value == 4) {
					// 正常終了
					executionTaskLog.setStatus(Optional.ofNullable(EndStatus.SUCCESS));
				}
			}
			this.procExecLogRepo.update(ProcessExecutionLog);
			ExecutionLogImportFn param = new ExecutionLogImportFn();
			List<ExecutionLogErrorDetailFn> listErrorAndEmpId = new ArrayList<>();
			// 会社ID ＝ パラメータ.更新処理自動実行.会社ID
			param.setCompanyId(companyId);
			// 管理社員ID ＝
			param.setManagerId(listManagementId);
			// 実行完了日時 ＝ システム日時
			param.setFinishDateTime(GeneralDateTime.now());
			// 実行内容 ＝ 月別実績の集計
			param.setExecutionContent(AlarmCategoryFn.AGGREGATE_RESULT_MONTH);
			// エラーの有無 ＝ エラーなし
			param.setExistenceError(0);
			// 実行ログエラー詳細 ＝ NULL
			param.setTargerEmployee(listErrorAndEmpId);
			// アルゴリズム「実行ログ登録」を実行する 2290 Done
			executionLogAdapterFn.updateExecuteLog(param);

		}

		return false; // 終了状態 !＝ 中断
	}

	@Inject
	private CreateExtraProcessService createExtraProcessService;
	@Inject
	private ExecAlarmListProcessingService execAlarmListProcessingService;

	// アラーム抽出
	private boolean alarmExtraction(String execId, ProcessExecution processExecution,
			ProcessExecutionLog ProcessExecutionLog, String companyId,
			CommandHandlerContext<ExecuteProcessExecutionCommand> context) {
		context.asAsync().getDataSetter().updateData("taskId", context.asAsync().getTaskId());
		// ドメインモデル「更新処理自動実行ログ」を更新する
		List<ExecutionTaskLog> taskLogLists = ProcessExecutionLog.getTaskLogList();
		int size = taskLogLists.size();
		boolean existExecutionTaskLog = false;
		for (int i = 0; i < size; i++) {
			ExecutionTaskLog executionTaskLog = taskLogLists.get(i);
			if (executionTaskLog.getProcExecTask().value == ProcessExecutionTask.AL_EXTRACTION.value) {
				executionTaskLog.setStatus(null);
				existExecutionTaskLog = true;
			}
		}
		if (!existExecutionTaskLog) {
			taskLogLists.add(new ExecutionTaskLog(ProcessExecutionTask.AL_EXTRACTION, null));
		}
		String execItemCd = context.getCommand().getExecItemCd();
		List<ExecutionTaskLog> taskLogList = this.execTaskLogRepo.getAllByCidExecCdExecId(companyId, execItemCd,
				execId);
		if (CollectionUtil.isEmpty(taskLogList)) {
			this.execTaskLogRepo.insertAll(companyId, execItemCd, execId, ProcessExecutionLog.getTaskLogList());
		} else {
			this.execTaskLogRepo.updateAll(companyId, execItemCd, execId, ProcessExecutionLog.getTaskLogList());
		}
		this.procExecLogRepo.update(ProcessExecutionLog);

		// アラーム抽出区分の判定
		boolean alarmAtr = processExecution.getExecSetting().getAlarmExtraction().isAlarmAtr();
		if (!alarmAtr) {
			// ドメインモデル「更新処理自動実行ログ」を更新する
			for (int i = 0; i < size; i++) {
				ExecutionTaskLog executionTaskLog = taskLogLists.get(i);
				if (executionTaskLog.getProcExecTask().value == ProcessExecutionTask.AL_EXTRACTION.value) {
					executionTaskLog.setStatus(Optional.ofNullable(EndStatus.NOT_IMPLEMENT));
				}
			}
			this.procExecLogRepo.update(ProcessExecutionLog);
			return false;
		}
		// アルゴリズム「抽出処理状況を作成する」を実行する
		String extraProcessStatusID = createExtraProcessService.createExtraProcess(companyId);
		// 実行 :
		// List<職場コード>
		List<String> workplaceIdList = new ArrayList<>();
		if (processExecution.getExecScope().getExecScopeCls() == ExecutionScopeClassification.COMPANY) {
			workplaceIdList = workplaceAdapter.findListWorkplaceIdByBaseDate(GeneralDate.today());
		} else {
			workplaceIdList = processExecution.getExecScope().getWorkplaceIdList().stream().map(c -> c.getWkpId())
					.collect(Collectors.toList());
		}
		// List<パターンコード>
		List<String> listPatternCode = new ArrayList<>();
		listPatternCode.add(processExecution.getExecSetting().getAlarmExtraction().getAlarmCode().get().v());
		boolean sendMailPerson = false;
		if (processExecution.getExecSetting().getAlarmExtraction().getMailPrincipal().isPresent()) {
			if (processExecution.getExecSetting().getAlarmExtraction().getMailPrincipal().get().booleanValue())
				sendMailPerson = true;
		}
		boolean sendMailAdmin = false;
		if (processExecution.getExecSetting().getAlarmExtraction().getMailAdministrator().isPresent()) {
			if (processExecution.getExecSetting().getAlarmExtraction().getMailAdministrator().get().booleanValue())
				sendMailAdmin = true;
		}
		// アラームリスト自動実行処理を実行する
		OutputExecAlarmListPro outputExecAlarmListPro = this.execAlarmListProcessingService.execAlarmListProcessing(
				extraProcessStatusID, companyId, workplaceIdList, listPatternCode, GeneralDateTime.now(),
				sendMailPerson, sendMailAdmin);

		// ドメインモデル「更新処理自動実行ログ」を取得しチェックする（中断されている場合は更新されているため、最新の情報を取得する）
		Optional<ProcessExecutionLog> processExecutionLog = procExecLogRepo.getLogByCIdAndExecCd(companyId, execItemCd,
				execId);
		// if optional
		if (!processExecutionLog.isPresent())
			return false;

		// 就業担当者の社員ID（List）を取得する : RQ526
		List<String> listManagementId = employeeManageAdapter.getListEmpID(companyId, GeneralDate.today());

		ExecutionLogImportFn param = new ExecutionLogImportFn();
		List<ExecutionLogErrorDetailFn> listErrorAndEmpId = new ArrayList<>();
		// 会社ID ＝ パラメータ.更新処理自動実行.会社ID
		param.setCompanyId(companyId);
		// 管理社員ID ＝
		param.setManagerId(listManagementId);
		// 実行完了日時 ＝ システム日時
		param.setFinishDateTime(GeneralDateTime.now());

		// 実行内容 ＝ スケジュール作成
		param.setExecutionContent(AlarmCategoryFn.ALARM_LIST_PERSONAL);
		// IF :TRUE
		if (outputExecAlarmListPro.isCheckExecAlarmListPro()) {
			// ドメインモデル「更新処理自動実行ログ」を更新する
			for (int i = 0; i < processExecutionLog.get().getTaskLogList().size(); i++) {
				ExecutionTaskLog executionTaskLog = taskLogLists.get(i);
				if (executionTaskLog.getProcExecTask().value == ProcessExecutionTask.AL_EXTRACTION.value) {
					executionTaskLog.setStatus(Optional.ofNullable(EndStatus.SUCCESS));
					this.procExecLogRepo.update(ProcessExecutionLog);
				}
			}
			param.setTargerEmployee(Collections.emptyList());
			param.setExistenceError(0);
			// アルゴリズム「実行ログ登録」を実行する 2290
			executionLogAdapterFn.updateExecuteLog(param);
			return true;
		}
		// IF :FALSE
		// ドメインモデル「更新処理自動実行ログ」を更新する
		for (int i = 0; i < processExecutionLog.get().getTaskLogList().size(); i++) {
			ExecutionTaskLog executionTaskLog = taskLogLists.get(i);
			if (executionTaskLog.getProcExecTask().value == ProcessExecutionTask.AL_EXTRACTION.value) {
				executionTaskLog.setStatus(Optional.ofNullable(EndStatus.FORCE_END));
				this.procExecLogRepo.update(ProcessExecutionLog);
			}
		}
		for (String managementId : listManagementId) {
			listErrorAndEmpId.add(new ExecutionLogErrorDetailFn(TextResource.localize("Msg_1339"), managementId));
		}
		param.setTargerEmployee(listErrorAndEmpId);
		param.setExistenceError(1);
		// アルゴリズム「実行ログ登録」を実行する 2290
		executionLogAdapterFn.updateExecuteLog(param);
		return false;
	}

	private DatePeriod findClosurePeriodMinDate(String companyId, List<Closure> closureList) {
		YearMonth startYearMonth = null;
		YearMonth endYearMonth = null;
		for (Closure closure : closureList) {
			Optional<ClosureHistory> firstHist = this.closureRepo.findByHistoryBegin(companyId,
					closure.getClosureId().value);
			if (firstHist.isPresent()) {
				if (startYearMonth == null || firstHist.get().getStartYearMonth().lessThan(startYearMonth)) {
					startYearMonth = firstHist.get().getStartYearMonth();
				}
			}
			Optional<ClosureHistory> lastHist = this.closureRepo.findByHistoryLast(companyId,
					closure.getClosureId().value);
			if (lastHist.isPresent()) {
				if (endYearMonth == null || lastHist.get().getEndYearMonth().greaterThan(endYearMonth)) {
					endYearMonth = lastHist.get().getEndYearMonth();
				}
			}
		}
		GeneralDate startClosingDate = GeneralDate.ymd(startYearMonth.year(), startYearMonth.month(), 1);
		GeneralDate endClosingDate = GeneralDate.ymd(9999, 12, 31);
		return new DatePeriod(startClosingDate, endClosingDate);
	}

	// 実行前登録処理
	private ProcessExecutionLog preExecutionRegistrationProcessing(String companyId, String execItemCd, String execId,
			ProcessExecutionLogManage processExecutionLogManage, int execType) {
		Optional<ProcessExecutionLog> procExecLogOpt = this.procExecLogRepo.getLog(companyId, execItemCd);
		ProcessExecutionLog procExecLog = null;
		if (procExecLogOpt.isPresent()) {
			procExecLog = procExecLogOpt.get();
			// アルゴリズム「更新処理自動実行ログ新規登録処理」を実行する
			// 現在の実行状態 ＝ 実行
			processExecutionLogManage.setCurrentStatus(CurrentExecutionStatus.RUNNING);
			// 全体の終了状態 ＝ NULL
			processExecutionLogManage.setOverallStatus(null);
			if (execType == 1) {
				processExecutionLogManage.setLastExecDateTime(GeneralDateTime.now());
			} else {
				processExecutionLogManage.setLastExecDateTime(GeneralDateTime.now());
				processExecutionLogManage.setLastExecDateTimeEx(GeneralDateTime.now());
			}
			this.processExecLogManaRepo.update(processExecutionLogManage);

			// ドメインモデル「更新処理自動実行ログ」を削除する
			this.procExecLogRepo.remove(companyId, execItemCd, procExecLogOpt.get().getExecId());

			// [更新処理：スケジュールの作成、終了状態 ＝ 未実施]
			this.updateEachTaskStatus(procExecLog, ProcessExecutionTask.SCH_CREATION, EndStatus.FORCE_END);
			// [更新処理：日別作成、終了状態 ＝ 未実施]
			this.updateEachTaskStatus(procExecLog, ProcessExecutionTask.DAILY_CREATION, EndStatus.NOT_IMPLEMENT);
			// [更新処理：日別計算、終了状態 ＝ 未実施]
			this.updateEachTaskStatus(procExecLog, ProcessExecutionTask.DAILY_CALCULATION, EndStatus.NOT_IMPLEMENT);
			// [更新処理：承認結果反映、終了状態 ＝ 未実施]
			this.updateEachTaskStatus(procExecLog, ProcessExecutionTask.RFL_APR_RESULT, EndStatus.NOT_IMPLEMENT);
			// [更新処理：月別集計、終了状態 ＝ 未実施]
			this.updateEachTaskStatus(procExecLog, ProcessExecutionTask.MONTHLY_AGGR, EndStatus.NOT_IMPLEMENT);
			// [更新処理：アラーム抽出、終了状態 ＝ 未実施]
			this.updateEachTaskStatus(procExecLog, ProcessExecutionTask.AL_EXTRACTION, EndStatus.NOT_IMPLEMENT);
			// [更新処理：承認ルート更新（日次、終了状態 ＝ 未実施]
			this.updateEachTaskStatus(procExecLog, ProcessExecutionTask.APP_ROUTE_U_DAI, EndStatus.NOT_IMPLEMENT);
			// [更新処理：承認ルート更新（月次）、終了状態 ＝ 未実施]
			this.updateEachTaskStatus(procExecLog, ProcessExecutionTask.APP_ROUTE_U_MON, EndStatus.NOT_IMPLEMENT);

			procExecLog.setExecId(execId);
			this.procExecLogRepo.insert(procExecLog);

		} else {
			// アルゴリズム「更新処理自動実行ログ新規登録処理」を実行する
			// 現在の実行状態 ＝ 実行
			processExecutionLogManage.setCurrentStatus(CurrentExecutionStatus.RUNNING);
			// 全体の終了状態 ＝ NULL
			processExecutionLogManage.setOverallStatus(null);
			if (execType == 1) {
				processExecutionLogManage.setLastExecDateTime(GeneralDateTime.now());
			} else {
				processExecutionLogManage.setLastExecDateTime(GeneralDateTime.now());
				processExecutionLogManage.setLastExecDateTimeEx(GeneralDateTime.now());
			}
			this.processExecLogManaRepo.update(processExecutionLogManage);
			List<ExecutionTaskLog> taskLogList = new ArrayList<ExecutionTaskLog>();
			taskLogList.add(new ExecutionTaskLog(ProcessExecutionTask.SCH_CREATION,
					Optional.ofNullable(EndStatus.NOT_IMPLEMENT)));
			taskLogList.add(new ExecutionTaskLog(ProcessExecutionTask.DAILY_CREATION,
					Optional.ofNullable(EndStatus.NOT_IMPLEMENT)));
			taskLogList.add(new ExecutionTaskLog(ProcessExecutionTask.DAILY_CALCULATION,
					Optional.ofNullable(EndStatus.NOT_IMPLEMENT)));
			taskLogList.add(new ExecutionTaskLog(ProcessExecutionTask.RFL_APR_RESULT,
					Optional.ofNullable(EndStatus.NOT_IMPLEMENT)));
			taskLogList.add(new ExecutionTaskLog(ProcessExecutionTask.MONTHLY_AGGR,
					Optional.ofNullable(EndStatus.NOT_IMPLEMENT)));
			taskLogList.add(new ExecutionTaskLog(ProcessExecutionTask.AL_EXTRACTION,
					Optional.ofNullable(EndStatus.NOT_IMPLEMENT)));
			taskLogList.add(new ExecutionTaskLog(ProcessExecutionTask.APP_ROUTE_U_DAI,
					Optional.ofNullable(EndStatus.NOT_IMPLEMENT)));
			taskLogList.add(new ExecutionTaskLog(ProcessExecutionTask.APP_ROUTE_U_MON,
					Optional.ofNullable(EndStatus.NOT_IMPLEMENT)));
			procExecLog = new ProcessExecutionLog(new ExecutionCode(execItemCd), companyId, null, taskLogList, execId);
			this.procExecLogRepo.insert(procExecLog);
		}
		return procExecLog;

	}

	// true is interrupt
	// 日別実績の作成 ~ 日別実績の計算
	private boolean dailyPerformanceCreation(String companyId,
			CommandHandlerContext<ExecuteProcessExecutionCommand> context, ProcessExecution processExecution,
			EmpCalAndSumExeLog empCalAndSumExeLog, List<String> lstEmpId, DatePeriod period, List<String> workPlaceIds,
			String typeExecution, ExecutionLog dailyCreateLog) throws CreateDailyException, DailyCalculateException {
		/*
		 * // <<Public>> 就業条件で社員を検索して並び替える RegulationInfoEmployeeAdapterImport
		 * regulationInfoEmployeeAdapterImport = new
		 * RegulationInfoEmployeeAdapterImport(); if
		 * (processExecution.getExecScope().getExecScopeCls().value == 1) { //
		 * 【更新処理自動実行.実行範囲.実行範囲区分 ＝ 職場 の場合】 // 基準日 → システム日付
		 * regulationInfoEmployeeAdapterImport.setBaseDate(GeneralDateTime.now()); //
		 * 検索参照範囲 → 参照範囲を考慮しない regulationInfoEmployeeAdapterImport.setReferenceRange(3);
		 * // 雇用で絞り込む → TRUE
		 * regulationInfoEmployeeAdapterImport.setFilterByEmployment(true); // 雇用コード一覧 →
		 * 取得した雇用コード（List)
		 * regulationInfoEmployeeAdapterImport.setEmploymentCodes(lstEmploymentCode); //
		 * 部門で絞り込む → FALSE
		 * regulationInfoEmployeeAdapterImport.setFilterByDepartment(false); // 部門ID一覧 →
		 * なし regulationInfoEmployeeAdapterImport.setDepartmentCodes(null); // 職場で絞り込む →
		 * TRUE regulationInfoEmployeeAdapterImport.setFilterByWorkplace(true);
		 * 
		 * if (workPlaceIds == null || workPlaceIds.isEmpty()) {
		 * regulationInfoEmployeeAdapterImport.setWorkplaceCodes(null); } else { //
		 * 職場ID一覧 → パラメータ「職場ID（List）」
		 * regulationInfoEmployeeAdapterImport.setWorkplaceCodes(workPlaceIds); }
		 * 
		 * // 分類で絞り込む → FALSE
		 * regulationInfoEmployeeAdapterImport.setFilterByClassification(false); //
		 * 分類コード一覧 → なし
		 * regulationInfoEmployeeAdapterImport.setClassificationCodes(null); // 職位で絞り込む
		 * → FALSE regulationInfoEmployeeAdapterImport.setFilterByJobTitle(false); //
		 * 職位ID一覧 → なし regulationInfoEmployeeAdapterImport.setJobTitleCodes(null); //
		 * 在職・休職・休業のチェック期間 → 作成した期間（日別作成）※パラメータで取得
		 * regulationInfoEmployeeAdapterImport.setPeriodStart(period.start());
		 * regulationInfoEmployeeAdapterImport.setPeriodEnd(period.end()); // 在職者を含める →
		 * TRUE regulationInfoEmployeeAdapterImport.setIncludeIncumbents(true); //
		 * 休職者を含める → FALSE
		 * regulationInfoEmployeeAdapterImport.setIncludeWorkersOnLeave(false); //
		 * 休業者を含める → FALSE
		 * regulationInfoEmployeeAdapterImport.setIncludeOccupancy(false); //
		 * 出向に来ている社員を含める → TRUE
		 * regulationInfoEmployeeAdapterImport.setIncludeAreOnLoan(true); //
		 * 出向に行っている社員を含める → FALSE
		 * regulationInfoEmployeeAdapterImport.setIncludeGoingOnLoan(false); // 退職者を含める
		 * → FALSE regulationInfoEmployeeAdapterImport.setIncludeRetirees(false); //
		 * 退職日のチェック期間 → 作成した期間（日別作成） ※パラメータで取得
		 * regulationInfoEmployeeAdapterImport.setRetireStart(period.start());
		 * regulationInfoEmployeeAdapterImport.setRetireEnd(period.end()); // 並び順NO → 1
		 * regulationInfoEmployeeAdapterImport.setSortOrderNo(1);
		 * regulationInfoEmployeeAdapterImport.setSystemType(2); //勤務種別で絞り込む → FALSE
		 * regulationInfoEmployeeAdapterImport.setFilterByWorktype(false); // 氏名の種類 →
		 * ビジネスネーム日本語 regulationInfoEmployeeAdapterImport.setNameType("ビジネスネーム日本語");
		 * 
		 * } else { // 【更新処理自動実行.実行範囲.実行範囲区分 ＝ 会社 の場合】 // 基準日 → システム日付
		 * regulationInfoEmployeeAdapterImport.setBaseDate(GeneralDateTime.now()); //
		 * 検索参照範囲 → 参照範囲を考慮しない regulationInfoEmployeeAdapterImport.setReferenceRange(3);
		 * // 雇用で絞り込む → TRUE
		 * regulationInfoEmployeeAdapterImport.setFilterByEmployment(true); // 雇用コード一覧 →
		 * 取得した雇用コード（List)
		 * regulationInfoEmployeeAdapterImport.setEmploymentCodes(lstEmploymentCode); //
		 * 部門で絞り込む → FALSE
		 * regulationInfoEmployeeAdapterImport.setFilterByDepartment(false); // 部門ID一覧 →
		 * なし regulationInfoEmployeeAdapterImport.setDepartmentCodes(null); // 職場で絞り込む →
		 * FALSE regulationInfoEmployeeAdapterImport.setFilterByWorkplace(false); //
		 * 職場ID一覧 → なし regulationInfoEmployeeAdapterImport.setWorkplaceCodes(null); //
		 * 分類で絞り込む → FALSE
		 * regulationInfoEmployeeAdapterImport.setFilterByClassification(false); //
		 * 分類コード一覧 → なし
		 * regulationInfoEmployeeAdapterImport.setClassificationCodes(null); // 職位で絞り込む
		 * → FALSE regulationInfoEmployeeAdapterImport.setFilterByJobTitle(false); //
		 * 職位ID一覧 → なし regulationInfoEmployeeAdapterImport.setJobTitleCodes(null); //
		 * 在職・休職・休業のチェック期間 → 作成した期間
		 * regulationInfoEmployeeAdapterImport.setPeriodStart(period.start());
		 * regulationInfoEmployeeAdapterImport.setPeriodEnd(period.end()); // 在職者を含める →
		 * TRUE regulationInfoEmployeeAdapterImport.setIncludeIncumbents(true); //
		 * 休職者を含める → FALSE
		 * regulationInfoEmployeeAdapterImport.setIncludeWorkersOnLeave(false); //
		 * 休業者を含める → FALSE
		 * regulationInfoEmployeeAdapterImport.setIncludeOccupancy(false); //
		 * 出向に来ている社員を含める → TRUE
		 * regulationInfoEmployeeAdapterImport.setIncludeAreOnLoan(true); //
		 * 出向に行っている社員を含める → FALSE
		 * regulationInfoEmployeeAdapterImport.setIncludeGoingOnLoan(false); // 退職者を含める
		 * → FALSE regulationInfoEmployeeAdapterImport.setIncludeRetirees(false); //
		 * 退職日のチェック期間 → 作成した期間（日別作成） ※パラメータで取得
		 * regulationInfoEmployeeAdapterImport.setRetireStart(period.start());
		 * regulationInfoEmployeeAdapterImport.setRetireEnd(period.end()); // 並び順NO → 1
		 * regulationInfoEmployeeAdapterImport.setSortOrderNo(1);
		 * regulationInfoEmployeeAdapterImport.setSystemType(2); //勤務種別で絞り込む → FALSE
		 * regulationInfoEmployeeAdapterImport.setFilterByWorktype(false); // 氏名の種類 →
		 * ビジネスネーム日本語 regulationInfoEmployeeAdapterImport.setNameType("ビジネスネーム日本語");
		 * 
		 * } // <<Public>> 就業条件で社員を検索して並び替える List<RegulationInfoEmployeeAdapterDto>
		 * lstRegulationInfoEmployee = this.regulationInfoEmployeeAdapter
		 * .find(regulationInfoEmployeeAdapterImport);
		 */
		boolean isInterrupt = false;
		List<Boolean> listIsInterrupt = new ArrayList<>();
		List<String> listErrorTryCatch = new ArrayList<>();
		//int size = lstEmpId.size();
				this.managedParallelWithContext.forEach(
						ControlOption.custom().millisRandomDelay(MAX_DELAY_PARALLEL),
						lstEmpId,
						empId -> {
							// アルゴリズム「開始日を入社日にする」を実行する
							try {
								DatePeriod employeeDatePeriod = this.makeStartDateForHiringDate(processExecution, empId, period);
								if (employeeDatePeriod == null && processExecution.getExecSetting().getDailyPerf()
										.getTargetGroupClassification().isMidJoinEmployee()) {
									
								}else {
									boolean executionDaily = this.executionDaily(companyId, context, processExecution, empId,
											empCalAndSumExeLog, employeeDatePeriod, typeExecution, dailyCreateLog);
									if (executionDaily) {
										listIsInterrupt.add(true);
										return;
									}
								}
							} catch (CreateDailyException ex) {
								listErrorTryCatch.add("errCreateDailyException");
							} catch (DailyCalculateException ex) {
								listErrorTryCatch.add("errDailyCalculateException");
							}
						});
				if(!listErrorTryCatch.isEmpty()) {
					if(listErrorTryCatch.get(0).equals("errCreateDailyException")) {
						throw new CreateDailyException();
					}
					if(listErrorTryCatch.get(0).equals("errDailyCalculateException")) {
						throw new DailyCalculateException();
					}
				}
				if(!listIsInterrupt.isEmpty()) {
					isInterrupt = true;
				}
//		for (int i = 0; i < size; i++) {
//			// アルゴリズム「開始日を入社日にする」を実行する
//			DatePeriod employeeDatePeriod = this.makeStartDateForHiringDate(processExecution, lstEmpId.get(i), period);
//			if (employeeDatePeriod == null && processExecution.getExecSetting().getDailyPerf()
//					.getTargetGroupClassification().isMidJoinEmployee()) {
//				continue;
//			}
//			boolean executionDaily = this.executionDaily(companyId, context, processExecution, lstEmpId.get(i),
//					empCalAndSumExeLog, employeeDatePeriod, typeExecution, dailyCreateLog);
//			if (executionDaily) {
//				isInterrupt = true;
//				break;
//			}
//		}
		List<ErrMessageInfo> errMessageInfos = this.errMessageInfoRepository
				.getAllErrMessageInfoByEmpID(empCalAndSumExeLog.getEmpCalAndSumExecLogID());
		List<String> errorMessage = errMessageInfos.stream().map(error -> {
			return error.getMessageError().v();
		}).collect(Collectors.toList());

		if ("日別作成".equals(typeExecution)) {
			if (!errorMessage.isEmpty()) {
				throw new CreateDailyException();
			}
		} else {
			if (isInterrupt) {
				throw new DailyCalculateException();
			}
		}

		if (isInterrupt) {
			return true;
		}
		return false;

	}

	// 開始日を入社日にする
	private DatePeriod makeStartDateForHiringDate(ProcessExecution processExecution, String employeeId,
			DatePeriod period) {
		List<String> lstEmployeeId = new ArrayList<String>();
		lstEmployeeId.add(employeeId);
		// ドメインモデル「更新処理自動実行.実行設定.日別実績の作成・計算.途中入社は入社日からにする」の判定
		if (processExecution.getExecSetting().getDailyPerf().getTargetGroupClassification().isMidJoinEmployee()) {
			// request list 211
			List<nts.uk.ctx.at.record.dom.adapter.company.AffCompanyHistImport> affCompanyHistByEmployee = this.syCompanyRecordAdapter
					.getAffCompanyHistByEmployee(lstEmployeeId, period);
			if (affCompanyHistByEmployee != null && !affCompanyHistByEmployee.isEmpty()) {
				List<AffComHistItemImport> lstAffComHistItem = affCompanyHistByEmployee.get(0).getLstAffComHistItem();
				int size = lstAffComHistItem.size();
				GeneralDate startDate = GeneralDate.ymd(9999, 12, 31);
				for (int i = 0; i < size; i++) {
					AffComHistItemImport affComHistItemImport = lstAffComHistItem.get(i);
					if (affComHistItemImport.getDatePeriod() != null
							&& affComHistItemImport.getDatePeriod().start().compareTo(startDate) == -1) {
						startDate = affComHistItemImport.getDatePeriod().start();
					}
				}
				return new DatePeriod(startDate, period.end());
			}
			return null;
		}
		return period;
	}

	// true is interrupt
	private boolean executionDaily(String companyId, CommandHandlerContext<ExecuteProcessExecutionCommand> context,
			ProcessExecution processExecution, String employeeId, EmpCalAndSumExeLog empCalAndSumExeLog,
			DatePeriod period, String typeExecution, ExecutionLog dailyCreateLog)
			throws CreateDailyException, DailyCalculateException {
		AsyncCommandHandlerContext<ExecuteProcessExecutionCommand> asyContext = (AsyncCommandHandlerContext<ExecuteProcessExecutionCommand>) context;
		ProcessState processState;
		// 受け取った期間が「作成した期間（日別作成）」の場合
		if ("日別作成".equals(typeExecution)) {
			try {
				// ⑤社員の日別実績を作成する
				processState = this.createDailyService.createDailyResultEmployeeWithNoInfoImport(asyContext, employeeId,
						period, empCalAndSumExeLog.getCompanyID(), empCalAndSumExeLog.getEmpCalAndSumExecLogID(),
						Optional.ofNullable(dailyCreateLog), processExecution.getExecSetting().getDailyPerf()
								.getTargetGroupClassification().isRecreateTypeChangePerson() ? true : false,
						false, false, null);
			} catch (Exception e) {
				throw new CreateDailyException();
			}
		} else {
			try {
				processState = this.dailyCalculationEmployeeService.calculateForOnePerson(asyContext, employeeId,
						period, Optional.empty());
			} catch (Exception e) {
				throw new DailyCalculateException();
			}

		}
		// fixed
		return processState.value == 0 ? true : false;
	}

//	private DatePeriod getMaxDatePeriod(DatePeriod dailyCreation, DatePeriod dailyCalculation) {
//		GeneralDate start;
//		GeneralDate end;
//		if (dailyCreation.start().compareTo(dailyCalculation.start()) <= 0) {
//			start = dailyCreation.start();
//		} else {
//			start = dailyCalculation.start();
//		}
//		if (dailyCreation.end().compareTo(dailyCalculation.end()) >= 0) {
//			end = dailyCreation.end();
//		} else {
//			end = dailyCalculation.end();
//		}
//		return new DatePeriod(start, end);
//	}

	@Inject
	private RecordWorkInfoFunAdapter recordWorkInfoFunAdapter;

	// 再作成処理
//	private boolean recreateProcess(CommandHandlerContext<ExecuteProcessExecutionCommand> context, int closureId,
//			EmpCalAndSumExeLog empCalAndSumExeLog, DatePeriod period, List<String> workPlaceIds, List<String> empIdList,
//			String companyId, ProcessExecutionLog procExecLog, ProcessExecution processExecution,
//			ExecutionLog dailyCreateLog) throws CreateDailyException, DailyCalculateException {
//		// 承認結果の反映の実行ログを作成
//		// this.createExecLogReflecAppResult(empCalAndSumExeLog.getCaseSpecExeContentID(),
//		// companyId, procExecLog);
//		// 期間を計算
//		GeneralDate calculateDate = this.calculatePeriod(closureId, period, companyId);
//
//		//// 勤務種別の絞り込み
//		List<String> newEmpIdList = this.refineWorkType(companyId, empIdList, calculateDate);
//
//		boolean isHasInterrupt = false;
//		// 日別実績処理の再実行
//		int size = newEmpIdList.size();
//		for (int i = 0; i < size; i++) {
//			String empId = newEmpIdList.get(i);
//			// ドメインモデル「日別実績の勤務情報」を取得する
//			// 「作成した開始日」～「取得した日別実績の勤務情報.年月日」を対象期間とする
//			List<WorkInfoOfDailyPerFnImport> listWorkInfo = recordWorkInfoFunAdapter.findByPeriodOrderByYmd(empId);
//			if (listWorkInfo.isEmpty())
//				continue;
//			GeneralDate maxDate = listWorkInfo.stream().map(u -> u.getYmd()).max(GeneralDate::compareTo).get();
//			isHasInterrupt = this.RedoDailyPerformanceProcessing(context, companyId, empId,
//					new DatePeriod(calculateDate, maxDate), empCalAndSumExeLog.getEmpCalAndSumExecLogID(),
//					dailyCreateLog, processExecution);
//			if (isHasInterrupt) {
//				break;
//			}
//		}
//		return isHasInterrupt;
//	}

	// 期間を計算
	private GeneralDate calculatePeriod(int closureId, DatePeriod period, String companyId) {
		Optional<Closure> closureOpt = this.closureRepo.findById(companyId, closureId);
		if (closureOpt.isPresent()) {
			Closure closure = closureOpt.get();
			YearMonth processingYm = closure.getClosureMonth().getProcessingYm();
			DatePeriod closurePeriod = this.closureService.getClosurePeriod(closureId, processingYm);
			return closurePeriod.start();
		}
		return period.start();
	}

	// 勤務種別の絞り込み
	private List<String> refineWorkType(String companyId, List<String> empIdList, GeneralDate startDate) {
		List<String> newEmpIdList = new ArrayList<String>();
		for (String empId : empIdList) {
			// ドメインモデル「社員の勤務種別の履歴」を取得する
			Optional<BusinessTypeOfEmployeeHistory> businessTypeOpt = this.typeEmployeeOfHistoryRepos
					.findByEmployeeDesc(AppContexts.user().companyId(), empId);
			if (businessTypeOpt.isPresent()) {
				BusinessTypeOfEmployeeHistory businessTypeOfEmployeeHistory = businessTypeOpt.get();
				List<DateHistoryItem> lstDate = businessTypeOfEmployeeHistory.getHistory();
				int size = lstDate.size();
				for (int i = 0; i < size; i++) {
					DateHistoryItem dateHistoryItem = lstDate.get(i);
					if (dateHistoryItem.start().compareTo(startDate) >= 0) {
						newEmpIdList.add(empId);
						break;
					}
				}
			}
		}
		return newEmpIdList;
	}

	private boolean RedoDailyPerformanceProcessing(CommandHandlerContext<ExecuteProcessExecutionCommand> context,
			String companyId, String empId, DatePeriod period, String empCalAndSumExeLogId, ExecutionLog dailyCreateLog,
			ProcessExecution procExec) throws CreateDailyException, DailyCalculateException {
		AsyncCommandHandlerContext<ExecuteProcessExecutionCommand> asyncContext = (AsyncCommandHandlerContext<ExecuteProcessExecutionCommand>) context;
		ProcessState processState1;
		try {
			// 実行設定.日別実績の作成・計算.対象者区分.勤務種別者を再作成
			boolean reCreateWorkType = procExec.getExecSetting().getDailyPerf().getTargetGroupClassification()
					.isRecreateTypeChangePerson();
			// 実行設定.日別実績の作成・計算.対象者区分.異動者を再作成する
			boolean reCreateWorkPlace = procExec.getExecSetting().getDailyPerf().getTargetGroupClassification()
					.isRecreateTransfer();
			// 実行設定.日別実績の作成・計算.対象者区分.休職者・休業者を再作成
			boolean reCreateRestTime = false; // TODO : chua lam
			// ⑤社員の日別実績を作成する
			processState1 = this.createDailyService.createDailyResultEmployeeWithNoInfoImport(asyncContext, empId,
					period, companyId, empCalAndSumExeLogId, Optional.ofNullable(dailyCreateLog), reCreateWorkType,
					reCreateWorkPlace, reCreateRestTime, null);
		} catch (Exception e) {
			throw new CreateDailyException();
		}
		ProcessState ProcessState2;

		try {
			// 社員の日別実績を計算
			ProcessState2 = this.dailyCalculationEmployeeService.calculateForOnePerson(asyncContext, empId, period,
					Optional.empty());
		} catch (Exception e) {
			throw new DailyCalculateException();
		}

		// 社員の申請を反映 cua chi du
		// AppReflectManager.reflectEmployeeOfApp
		// fixed endStatusIsInterrupt =true (終了状態 ＝ 中断)
		// boolean endStatusIsInterrupt = true;

		// 中断
		if (processState1.value == 0 || ProcessState2.value == 0) {
			return true;
		}
		return false;
	}

	// 全締めから一番早い期間.開始日を取得する
	private DatePeriod getMinPeriodFromStartDate(String companyId) {
		// ドメインモデル「就業締め日」を取得する
		List<Closure> closureList = this.closureRepo.findAllActive(companyId, UseClassification.UseClass_Use);
		// 全締めから一番早い期間.開始日を取得する
		return this.findClosureMinMaxPeriod(companyId, closureList);
	}

}
