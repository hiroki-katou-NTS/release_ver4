package nts.uk.ctx.at.function.app.command.processexecution;

import java.time.DateTimeException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

import lombok.val;
import nts.arc.layer.app.command.AsyncCommandHandler;
import nts.arc.layer.app.command.CommandHandlerContext;
import nts.arc.time.GeneralDate;
import nts.arc.time.GeneralDateTime;
import nts.arc.time.YearMonth;
import nts.gul.collection.CollectionUtil;
import nts.gul.text.IdentifierUtil;
import nts.uk.ctx.at.function.dom.adapter.AffCompanyHistImport;
import nts.uk.ctx.at.function.dom.adapter.EmployeeHistWorkRecordAdapter;
import nts.uk.ctx.at.function.dom.adapter.WorkPlaceHistImport;
import nts.uk.ctx.at.function.dom.adapter.WorkPlaceIdAndPeriodImport;
import nts.uk.ctx.at.function.dom.adapter.WorkplaceWorkRecordAdapter;
import nts.uk.ctx.at.function.dom.processexecution.ExecutionCode;
import nts.uk.ctx.at.function.dom.processexecution.LastExecDateTime;
import nts.uk.ctx.at.function.dom.processexecution.ProcessExecution;
import nts.uk.ctx.at.function.dom.processexecution.executionlog.CurrentExecutionStatus;
import nts.uk.ctx.at.function.dom.processexecution.executionlog.EndStatus;
import nts.uk.ctx.at.function.dom.processexecution.executionlog.ExecutionTaskLog;
import nts.uk.ctx.at.function.dom.processexecution.executionlog.ProcessExecutionLog;
import nts.uk.ctx.at.function.dom.processexecution.executionlog.ProcessExecutionLogHistory;
import nts.uk.ctx.at.function.dom.processexecution.executionlog.ProcessExecutionTask;
import nts.uk.ctx.at.function.dom.processexecution.personalschedule.TargetClassification;
import nts.uk.ctx.at.function.dom.processexecution.personalschedule.TargetSetting;
import nts.uk.ctx.at.function.dom.processexecution.repository.ExecutionTaskLogRepository;
import nts.uk.ctx.at.function.dom.processexecution.repository.ExecutionTaskSettingRepository;
import nts.uk.ctx.at.function.dom.processexecution.repository.LastExecDateTimeRepository;
import nts.uk.ctx.at.function.dom.processexecution.repository.ProcessExecutionLogHistRepository;
import nts.uk.ctx.at.function.dom.processexecution.repository.ProcessExecutionLogRepository;
import nts.uk.ctx.at.function.dom.processexecution.repository.ProcessExecutionRepository;
import nts.uk.ctx.at.function.dom.processexecution.tasksetting.ExecutionTaskSetting;
import nts.uk.ctx.at.record.dom.dailyperformanceformat.businesstype.BusinessTypeOfEmployeeHistory;
import nts.uk.ctx.at.record.dom.dailyperformanceformat.businesstype.repository.BusinessTypeEmpOfHistoryRepository;
import nts.uk.ctx.at.record.dom.dailyperformanceprocessing.output.ExecutionAttr;
import nts.uk.ctx.at.record.dom.dailyperformanceprocessing.repository.CreateDailyResultDomainService;
import nts.uk.ctx.at.record.dom.dailyperformanceprocessing.repository.CreateDailyResultDomainServiceImpl.ProcessState;
import nts.uk.ctx.at.record.dom.dailyperformanceprocessing.repository.ProcessFlowOfDailyCreationDomainService;
import nts.uk.ctx.at.record.dom.dailyprocess.calc.DailyCalculationService;
import nts.uk.ctx.at.record.dom.workrecord.workperfor.dailymonthlyprocessing.CalExeSettingInfor;
import nts.uk.ctx.at.record.dom.workrecord.workperfor.dailymonthlyprocessing.EmpCalAndSumExeLog;
import nts.uk.ctx.at.record.dom.workrecord.workperfor.dailymonthlyprocessing.EmpCalAndSumExeLogRepository;
import nts.uk.ctx.at.record.dom.workrecord.workperfor.dailymonthlyprocessing.ErrMessageInfo;
import nts.uk.ctx.at.record.dom.workrecord.workperfor.dailymonthlyprocessing.ErrMessageInfoRepository;
import nts.uk.ctx.at.record.dom.workrecord.workperfor.dailymonthlyprocessing.ExecutionLog;
import nts.uk.ctx.at.record.dom.workrecord.workperfor.dailymonthlyprocessing.ExecutionLogRepository;
import nts.uk.ctx.at.record.dom.workrecord.workperfor.dailymonthlyprocessing.ExecutionTime;
import nts.uk.ctx.at.record.dom.workrecord.workperfor.dailymonthlyprocessing.ObjectPeriod;
import nts.uk.ctx.at.record.dom.workrecord.workperfor.dailymonthlyprocessing.SettingInforForDailyCreation;
import nts.uk.ctx.at.record.dom.workrecord.workperfor.dailymonthlyprocessing.TargetPersonRepository;
import nts.uk.ctx.at.record.dom.workrecord.workperfor.dailymonthlyprocessing.enums.DailyRecreateClassification;
import nts.uk.ctx.at.record.dom.workrecord.workperfor.dailymonthlyprocessing.enums.ErrorPresent;
import nts.uk.ctx.at.record.dom.workrecord.workperfor.dailymonthlyprocessing.enums.ExeStateOfCalAndSum;
import nts.uk.ctx.at.record.dom.workrecord.workperfor.dailymonthlyprocessing.enums.ExecutedMenu;
import nts.uk.ctx.at.record.dom.workrecord.workperfor.dailymonthlyprocessing.enums.ExecutionContent;
import nts.uk.ctx.at.record.dom.workrecord.workperfor.dailymonthlyprocessing.enums.ExecutionStatus;
import nts.uk.ctx.at.record.dom.workrecord.workperfor.dailymonthlyprocessing.enums.ExecutionType;
import nts.uk.ctx.at.schedule.app.command.executionlog.ScheduleCreatorExecutionCommand;
import nts.uk.ctx.at.schedule.app.command.executionlog.ScheduleCreatorExecutionCommandHandler;
import nts.uk.ctx.at.schedule.dom.executionlog.CompletionStatus;
import nts.uk.ctx.at.schedule.dom.executionlog.ScheduleExecutionLog;
import nts.uk.ctx.at.schedule.dom.executionlog.ScheduleExecutionLogRepository;
//import nts.uk.ctx.at.record.dom.dailyperformanceprocessing.output.ExecutionAttr;
//import nts.uk.ctx.at.record.dom.dailyperformanceprocessing.repository.ProcessFlowOfDailyCreationDomainService;
//import nts.uk.ctx.at.record.dom.workrecord.log.EmpCalAndSumExeLogRepository;
//import nts.uk.ctx.at.schedule.app.command.executionlog.ScheduleCreatorExecutionCommand;
//import nts.uk.ctx.at.schedule.app.command.executionlog.ScheduleCreatorExecutionCommandHandler;
//import nts.uk.ctx.at.schedule.dom.executionlog.CompletionStatus;
//import nts.uk.ctx.at.schedule.dom.executionlog.ScheduleExecutionLog;
//import nts.uk.ctx.at.schedule.dom.executionlog.ScheduleExecutionLogRepository;
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
import nts.uk.shr.com.time.calendar.period.DatePeriod;

@Stateless
public class ExecuteProcessExecutionCommandHandler extends AsyncCommandHandler<ExecuteProcessExecutionCommand> {

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
	private ProcessFlowOfDailyCreationDomainService processFlowOfDailyCreationDomainService;
	
	@Inject
	private ScheduleExecutionLogRepository scheduleExecutionLogRepository;
	
	@Inject
	private EmpCalAndSumExeLogRepository empCalAndSumExeLogRepository;

	@Inject
	private WorkplaceWorkRecordAdapter workplaceAdapter;
	
	@Inject
	private EmployeeHistWorkRecordAdapter employeeHistAdapter;
	
	@Inject
	private BusinessTypeEmpOfHistoryRepository typeEmployeeOfHistoryRepos;
	
	@Inject
	private TargetPersonRepository targetPersonRepository;
	
	@Inject
	private CreateDailyResultDomainService createDailyResultDomainService;
	
	@Inject
	private DailyCalculationService dailyCalculationService;
	
	@Inject
	private ExecutionLogRepository executionLogRepository;
	
	@Inject
	private ErrMessageInfoRepository errMessageInfoRepository;
	/**
	 * 更新処理を開始する
	 * 
	 * @param execType 実行タイプ
	 * @param execId 実行ID
	 * @param execItemCd 更新処理自動実行項目コード
	 * @param companyId 会社ID
	 */
	@Override
	protected void handle(CommandHandlerContext<ExecuteProcessExecutionCommand> context) {
		ExecuteProcessExecutionCommand command = context.getCommand();
		String execItemCd = command.getExecItemCd();
		String companyId = command.getCompanyId();
		String execId = command.getExecId();
		int execType = command.getExecType();
		// ドメインモデル「更新処理自動実行」を取得する
		ProcessExecution procExec = null;
		Optional<ProcessExecution> procExecOpt = this.procExecRepo.getProcessExecutionByCidAndExecCd(companyId, execItemCd);
		if (procExecOpt.isPresent()) {
			procExec = procExecOpt.get();
		}
		
		// ドメインモデル「実行タスク設定」を取得する
		ExecutionTaskSetting execSetting = null;
		Optional<ExecutionTaskSetting> execSettingOpt = this.execSettingRepo.getByCidAndExecCd(companyId, execItemCd);
		if (execSettingOpt.isPresent()) {
			execSetting = execSettingOpt.get();
		}
		
		// ドメインモデル「更新処理自動実行ログ」を取得する
		ProcessExecutionLog procExecLog = null;
		Optional<ProcessExecutionLog> procExecLogOpt = this.procExecLogRepo.getLogByCIdAndExecCd(companyId, execItemCd, execId);
		if (procExecLogOpt.isPresent()) {
			procExecLog = procExecLogOpt.get();
		}

		// ドメインモデル「更新処理前回実行日時」を取得する
		LastExecDateTime lastExecDateTime = null;
		Optional<LastExecDateTime> lastDateTimeOpt =
									lastExecDateTimeRepo.get(procExec.getCompanyId(), procExec.getExecItemCd().v());
		if (lastDateTimeOpt.isPresent()) {
			lastExecDateTime = lastDateTimeOpt.get();
		}
		
		/*
		 *  ドメインモデル「更新処理自動実行ログ」を更新する
		 *  現在の実行状態　＝　実行
		 *  全体の終了状態　＝　NULL
		 */
		procExecLog.setCurrentStatus(CurrentExecutionStatus.RUNNING);
		procExecLog.setOverallStatus(null);
		this.procExecLogRepo.update(procExecLog);
		
		/*
		 *  各処理を実行する
		 *  【パラメータ】
		 *  実行ID　＝　取得した実行ID
		 *  取得したドメインモデル「更新処理自動実行」、「実行タスク設定」、「更新処理自動実行ログ」の情報
		 */
		this.doProcesses(execId, procExec, procExecLog, context);
		
		// アルゴリズム「自動実行登録処理」を実行する
		this.updateDomains(execItemCd, execType, companyId, execId, execSetting, procExecLog, lastExecDateTime);
	}

	private void updateDomains(String execItemCd, int execType, String companyId, String execId,
			ExecutionTaskSetting execSetting, ProcessExecutionLog procExecLog, LastExecDateTime lastExecDateTime) {
		/*
		 * ドメインモデル「更新処理自動実行ログ」を更新する
		 * 
		 * 【更新処理自動実行ログ.各処理の終了状態に「異常終了」がない＆全体の終了状態がNULLの場合】
		 * 実行ID　＝　取得した実行ID
		 * 現在の実行状態　＝　待機
		 * 全体の終了状態　＝　正常終了
		 * 
		 * 【更新処理自動実行ログ.各処理の終了状態に「異常終了」がある場合】
		 * 実行ID　＝　取得した実行ID
		 * 現在の実行状態　＝　待機
		 * 全体の終了状態　＝　異常終了
		 */
		if (!this.isAbnormalTermEachTask(procExecLog) && procExecLog.getOverallStatus() == null) {
			procExecLog.setExecId(execId);
			procExecLog.setCurrentStatus(CurrentExecutionStatus.WAITING);
			procExecLog.setOverallStatus(EndStatus.SUCCESS);
		} else if (this.isAbnormalTermEachTask(procExecLog)) {
			procExecLog.setExecId(execId);
			procExecLog.setCurrentStatus(CurrentExecutionStatus.WAITING);
			procExecLog.setOverallStatus(EndStatus.ABNORMAL_END);
		}
		List<ExecutionTaskLog> taskLogList = this.execTaskLogRepo.getAllByCidExecCdExecId(companyId, execItemCd, execId);
		if (CollectionUtil.isEmpty(taskLogList)) {
			this.execTaskLogRepo.insertAll(companyId, execItemCd, execId, procExecLog.getTaskLogList());
		} else {
			this.execTaskLogRepo.updateAll(companyId, execItemCd, execId, procExecLog.getTaskLogList());
		}
		
		/*
		 * ドメインモデル「更新処理自動実行ログ」を更新する
		 * 
		 * 【実行タイプ　＝　1（即時実行）の場合】
		 * 前回実行日時　＝　システム日時
		 * 前回実行日時（即時実行を含めない）　＝　システム日時
		 * 
		 * 【実行タイプ　＝　0（開始時刻）の場合】
		 * 前回実行日時　＝　システム日時
		 */
		if (execType == 1) {
			procExecLog.setLastExecDateTime(GeneralDateTime.now());
			procExecLog.setLastExecDateTimeEx(GeneralDateTime.now());
		} else if (execType == 0) {
			procExecLog.setLastExecDateTime(GeneralDateTime.now());
		}
		this.procExecLogRepo.update(procExecLog);
		
		/*
		 * ドメインモデル「更新処理自動実行ログ履歴」を新規登録する
		 * 
		 * 会社ID　＝　更新処理自動実行ログ.会社ID
		 * 実行ID　＝　取得した実行ID
		 * コード　＝　更新処理自動実行ログ.コード
		 * 前回実行日時　＝　更新処理自動実行ログ.前回実行日時
		 * 各処理の終了状態(List)　＝　更新処理自動実行ログ.各処理の終了状態(List)
		 * 全体の終了状態　＝　更新処理自動実行ログ.全体の終了状態
		 * 全体のエラー詳細　＝　更新処理自動実行ログ.全体のエラー詳細
		 * 各処理の期間　＝　更新処理自動実行ログ.各処理の期間
		 */
		this.procExecLogHistRepo.insert(
					new ProcessExecutionLogHistory(new ExecutionCode(execItemCd),
													companyId,
													procExecLog.getOverallError(),
													procExecLog.getOverallStatus(),
													procExecLog.getLastExecDateTime(),
													procExecLog.getEachProcPeriod(),
													procExecLog.getTaskLogList(),
													execId));
		
		/*
		 *  ドメインモデル「実行タスク設定」を更新する
		 *  
		 *  次回実行日時　＝　次回実行日時を作成する。　※補足資料⑤参照
		 */
		if (execSetting != null) {
			execSetting.setNextExecDateTime();
			this.execSettingRepo.update(execSetting);
		}
		
		/*
		 * ドメインモデル「更新処理前回実行日時」を更新する
		 * 前回実行日時　＝　システム日時
		 */
		lastExecDateTime.setLastExecDateTime(GeneralDateTime.now());
		this.lastExecDateTimeRepo.update(lastExecDateTime);
	}

	/**
	 * 各処理を実行する
	 * @param execId 実行ID
	 * @param procExec 更新処理自動実行
	 * @param execSetting 実行タスク設定
	 * @param procExecLog 更新処理自動実行ログ
	 */
	private void doProcesses(String execId, ProcessExecution procExec, ProcessExecutionLog procExecLog, CommandHandlerContext<ExecuteProcessExecutionCommand> context) {
		// Initialize status [未実施] for each task 
		initAllTaskStatus(procExecLog, EndStatus.NOT_IMPLEMENT);
		
		/*
		 *  スケジュールの作成
		 *  【パラメータ】
		 *  実行ID
		 *  取得したドメインモデル「更新処理自動実行」、「実行タスク設定」、「更新処理自動実行ログ」の情報
		 */
		if (!this.createSchedule(execId, procExec, procExecLog)) {
			// ドメインモデル「更新処理自動実行ログ」を更新する
			this.updateEachTaskStatus(procExecLog, ProcessExecutionTask.SCH_CREATION, EndStatus.FORCE_END);
			return;
		} else if (!this.createDailyData(execId, procExec, procExecLog, context)) {
			// ドメインモデル「更新処理自動実行ログ」を更新する
			this.updateEachTaskStatus(procExecLog, ProcessExecutionTask.DAILY_CREATION, EndStatus.FORCE_END);
			this.updateEachTaskStatus(procExecLog, ProcessExecutionTask.DAILY_CALCULATION, EndStatus.FORCE_END);
			return;
		}
	}
	
	/**
	 * スケジュールの作成
	 * @param execId 実行ID
	 * @param procExec 更新処理自動実行
	 * @param execSetting 実行タスク設定
	 * @param procExecLog 更新処理自動実行ログ
	 */
	private boolean createSchedule(String execId, ProcessExecution procExec, ProcessExecutionLog procExecLog) {
		// Login user context
		LoginUserContext loginContext = AppContexts.user();
		// ドメインモデル「更新処理自動実行ログ」を更新する
		this.updateEachTaskStatus(procExecLog, ProcessExecutionTask.SCH_CREATION, null);
		try {
			// 個人スケジュール作成区分の判定
			if (!procExec.getExecSetting().getPerSchedule().isPerSchedule()) {
				this.updateEachTaskStatus(procExecLog, ProcessExecutionTask.SCH_CREATION, EndStatus.NOT_IMPLEMENT);
				return true;
			}
			
			// 期間の計算
			this.calculateSchedulePeriod(procExec, procExecLog);
			
			/*
			 * 対象社員を取得 TODO
			 */
			List<String> sidList = new ArrayList<>();
			sidList.add(loginContext.employeeId()); // Add login SID to test, remove when implement this algorithm
			
			/*
			 *  作成対象の判定
			 */
			// 全員の場合
			if (procExec.getExecSetting().getPerSchedule().getTarget().getCreationTarget().value == TargetClassification.ALL.value) {
				// 対象社員を取得 - TODO
				
				this.executeScheduleCreation(execId);
				
				// find execution log by id
				Optional<ScheduleExecutionLog> domainOpt =
						this.scheduleExecutionLogRepository.findById(loginContext.companyId(), execId);
				if (domainOpt.isPresent()) {
					if (domainOpt.get().getCompletionStatus().value == CompletionStatus.COMPLETION_ERROR.value) {
						return false;
					}
				}
				
				// ドメインモデル「更新処理自動実行ログ」を更新する
				this.updateEachTaskStatus(procExecLog, ProcessExecutionTask.SCH_CREATION, EndStatus.SUCCESS);
			}
			// 異動者・新入社員のみ作成の場合
			else {
				DatePeriod period = procExecLog.getEachProcPeriod().getScheduleCreationPeriod();
				List<String> reEmployeeList = new ArrayList<>();
				List<String> newEmployeeList = new ArrayList<>();
				// 対象社員を絞り込み
				this.filterEmployeeList(procExec, sidList, period, reEmployeeList, newEmployeeList);
				// 社員ID（新入社員）（List）のみ
				if (!CollectionUtil.isEmpty(reEmployeeList)) {
					this.executeScheduleCreation(execId);
				}
				
				// 社員ID（異動者、勤務種別変更者）（List）のみ
				if (!CollectionUtil.isEmpty(newEmployeeList)) {
					this.executeScheduleCreation(execId);
				}
			}
		} catch (Exception e) {
			// ドメインモデル「更新処理自動実行ログ」を更新する
			this.updateEachTaskStatus(procExecLog, ProcessExecutionTask.SCH_CREATION, EndStatus.ABNORMAL_END);
		}
		return true;
	}

	/**
	 * 実行
	 * @param execId
	 */
	private void executeScheduleCreation(String execId) {
		ScheduleCreatorExecutionCommand scheduleCommand = new ScheduleCreatorExecutionCommand();
		scheduleCommand.setExecutionId(execId);
		this.scheduleExecution.handle(scheduleCommand);
	}

	/**
	 * 日別作成
	 * @param execId 実行ID
	 * @param procExec 更新処理自動実行
	 * @param execSetting 実行タスク設定
	 * @param procExecLog 更新処理自動実行ログ
	 */
	private boolean createDailyData(String execId, ProcessExecution procExec, ProcessExecutionLog procExecLog, CommandHandlerContext<ExecuteProcessExecutionCommand> context) {
			ExecuteProcessExecutionCommand command = context.getCommand();
			String companyId = command.getCompanyId();
			// ドメインモデル「更新処理自動実行ログ」を更新する
			this.updateEachTaskStatus(procExecLog, ProcessExecutionTask.DAILY_CREATION, null);
			this.updateEachTaskStatus(procExecLog, ProcessExecutionTask.DAILY_CALCULATION, null);
			
			// 日別実績の作成・計算区分の判定
			if (!procExec.getExecSetting().getDailyPerf().isDailyPerfCls()) {
				this.updateEachTaskStatus(procExecLog, ProcessExecutionTask.DAILY_CREATION, EndStatus.NOT_IMPLEMENT);
				this.updateEachTaskStatus(procExecLog, ProcessExecutionTask.DAILY_CALCULATION, EndStatus.NOT_IMPLEMENT);
				return true;
			}
			
			// ドメインモデル「就業締め日」を取得する
			List<Closure> closureList = this.closureRepo.findAllActive(companyId, UseClassification.UseClass_Use);
			DatePeriod period = this.findClosurePeriod(companyId, closureList);
			
			// ドメインモデル「就業計算と集計実行ログ」を新規登録する
			EmpCalAndSumExeLog empCalAndSumExeLog = null;
			List<ExecutionLog> lstExecutionLog = null;
			Optional<EmpCalAndSumExeLog> empExeLogOpt = 
					this.empCalAndSumExeLogRepository.getByEmpCalAndSumExecLogID(execId);
			if (empExeLogOpt.isPresent()) {
				empCalAndSumExeLog = empExeLogOpt.get();
				lstExecutionLog = this.executionLogRepository.getExecutionLogs(empCalAndSumExeLog.getEmpCalAndSumExecLogID());
			} else {
				 EmpCalAndSumExeLogOutput registerEmpCalAndSumExeLog = this.registerEmpCalAndSumExeLog(execId, procExecLog, command, period.start(), period.end());
				 empCalAndSumExeLog =  registerEmpCalAndSumExeLog.getEmpCalAndSumExeLog();
				 lstExecutionLog =  registerEmpCalAndSumExeLog.getExecutionLogs();
			}
			
			val asyncContext = context.asAsync();
			for (Closure closure : closureList) {
				// 期間の計算
				this.calculateDailyPeriod(procExec,
										procExecLog,
										closure.getCompanyId().v(),
										closure.getClosureId().value,
										closure.getClosureMonth());
				
				// 雇用コードを取得する
				List<ClosureEmployment> employmentList =
									this.closureEmpRepo.findByClosureId(companyId, closure.getClosureId().value);
				
				// 対象社員を取得 - TODO
				List<String> sidList = new ArrayList<>();
				sidList.add(AppContexts.user().employeeId()); // Add login sid to test, remove when implement [対象社員を取得]
				
//				// Insert all TargetPersons
//				List<TargetPerson> lstTargetPerson = new ArrayList<TargetPerson>();
//				for (String employeeID : sidList) {
//					List<ComplStateOfExeContents> lstState = new ArrayList<ComplStateOfExeContents>();
//					for (ExecutionLog executionLog : empCalAndSumExeLog.getExecutionLogs()) {
//						ComplStateOfExeContents state = new ComplStateOfExeContents(executionLog.getExecutionContent(), EmployeeExecutionStatus.INCOMPLETE);
//						lstState.add(state);
//					}
//					TargetPerson targetPerson = new TargetPerson(employeeID, empCalAndSumExeLog.getEmpCalAndSumExecLogID(), lstState);
//					lstTargetPerson.add(targetPerson);
//				}
//				targetPersonRepository.addAll(lstTargetPerson);
				// 取得した社員IDを1つずつ順番に回す（取得した社員ID　＝　回数）
				
				ProcessState finalStatus = ProcessState.SUCCESS;
				// 実行ログを確認する
			//	val executionLogs = empCalAndSumExeLog.getExecutionLogs();
				Map<ExecutionContent, ExecutionLog> logsMap = new HashMap<>();
				for (val executionLog : lstExecutionLog){
					logsMap.put(executionLog.getExecutionContent(), executionLog);
				}
				
				DatePeriod createPeriod = procExecLog.getEachProcPeriod().getDailyCreationPeriod();
				// 日別実績の作成　実行
				if (logsMap.containsKey(ExecutionContent.DAILY_CREATION)
						&& finalStatus == ProcessState.SUCCESS) {
					try {
						Optional<ExecutionLog> dailyCreationLog =
								Optional.of(logsMap.get(ExecutionContent.DAILY_CREATION));
						finalStatus = this.createDailyResultDomainService.createDailyResult(asyncContext, sidList,
								createPeriod, ExecutionAttr.MANUAL, companyId, execId, dailyCreationLog);
						if (finalStatus == ProcessState.SUCCESS) {
							// ドメインモデル「更新処理自動実行ログ」を更新する
							this.updateEachTaskStatus(procExecLog, ProcessExecutionTask.DAILY_CREATION, EndStatus.SUCCESS);
						}
					} catch (Exception e) {
						// ドメインモデル「更新処理自動実行ログ」を更新する
						this.updateEachTaskStatus(procExecLog, ProcessExecutionTask.DAILY_CREATION, EndStatus.ABNORMAL_END);
					}
				}
				
				if (logsMap.containsKey(ExecutionContent.DAILY_CALCULATION)
						&& finalStatus == ProcessState.SUCCESS) {
					try {
						Optional<ExecutionLog> dailyCalculationLog =
								Optional.of(logsMap.get(ExecutionContent.DAILY_CALCULATION));
						finalStatus = this.dailyCalculationService.manager(asyncContext, sidList,
								createPeriod, ExecutionAttr.MANUAL, execId, dailyCalculationLog);
						
						if (finalStatus == ProcessState.SUCCESS) {
							// ドメインモデル「更新処理自動実行ログ」を更新する
							this.updateEachTaskStatus(procExecLog, ProcessExecutionTask.DAILY_CALCULATION, EndStatus.SUCCESS);
						}
					} catch (Exception e) {
						// ドメインモデル「更新処理自動実行ログ」を更新する
						this.updateEachTaskStatus(procExecLog, ProcessExecutionTask.DAILY_CALCULATION, EndStatus.ABNORMAL_END);
					}
				}
			
				// 終了状態　＝　中断
				if (finalStatus == ProcessState.INTERRUPTION) {
					this.empCalAndSumExeLogRepository.updateStatus(execId, ExeStateOfCalAndSum.STOPPING.value);
					return false;
				} else {
					ExeStateOfCalAndSum executionStatus = this.updateExecutionState(execId);
					this.empCalAndSumExeLogRepository.updateStatus(execId, executionStatus.value);
				}
//				try {
//					
////					for (ClosureEmployment employee : employmentList) {
//					DatePeriod createPeriod = procExecLog.getEachProcPeriod().getDailyCreationPeriod();
//					
//					// 実行
//					processFlowOfDailyCreationDomainService.processFlowOfDailyCreation(asyncContext, ExecutionAttr.MANUAL, createPeriod, execId);
//					// 実行処理からパラメータで「終了状態=中断」がかえってきているか確認する
//					Optional<EmpCalAndSumExeLog> empExeLogOpt = 
//							this.empCalAndSumExeLogRepository.getByEmpCalAndSumExecLogID(execId);
//					if (empExeLogOpt.isPresent()) {
//						// 終了状態　＝　中断
//						if (empExeLogOpt.get().getExecutionStatus().value == ExeStateOfCalAndSum.STOPPING.value) {
//							return false;
//						}
//					}
////					}
//					// ドメインモデル「更新処理自動実行ログ」を更新する
//					this.updateEachTaskStatus(procExecLog, ProcessExecutionTask.DAILY_CREATION, EndStatus.SUCCESS);
//					this.updateEachTaskStatus(procExecLog, ProcessExecutionTask.DAILY_CALCULATION, EndStatus.SUCCESS);
//				} catch (Exception e) {
//					// ドメインモデル「更新処理自動実行ログ」を更新する
//					this.updateEachTaskStatus(procExecLog, ProcessExecutionTask.DAILY_CREATION, EndStatus.ABNORMAL_END);
//					this.updateEachTaskStatus(procExecLog, ProcessExecutionTask.DAILY_CALCULATION, EndStatus.ABNORMAL_END);
//				}
			}
		return true;
	}

	private DatePeriod findClosurePeriod(String companyId, List<Closure> closureList) {
		YearMonth startYearMonth = null;
		YearMonth endYearMonth = null;
		for (Closure closure : closureList) {
			Optional<ClosureHistory> firstHist =
					this.closureRepo.findByHistoryBegin(companyId, closure.getClosureId().value);
			if (firstHist.isPresent()) {
				if (startYearMonth == null || firstHist.get().getStartYearMonth().lessThan(startYearMonth)) {
					startYearMonth = firstHist.get().getStartYearMonth();
				}
			}
			Optional<ClosureHistory> lastHist =
					this.closureRepo.findByHistoryLast(companyId, closure.getClosureId().value);
			if (lastHist.isPresent()) {
				if (endYearMonth == null || lastHist.get().getEndYearMonth().greaterThan(endYearMonth)) {
					endYearMonth = lastHist.get().getEndYearMonth();
				}
			}
		}
		GeneralDate startClosingDate = GeneralDate.ymd(startYearMonth.year(), startYearMonth.month(), 1);
		GeneralDate endClosingDate = GeneralDate.ymd(endYearMonth.year(), endYearMonth.month(), 1);
		endClosingDate = GeneralDate.ymd(endClosingDate.year(), endClosingDate.month(), endClosingDate.localDate().lengthOfMonth());
		return new DatePeriod(startClosingDate, endClosingDate);
	}

	private EmpCalAndSumExeLogOutput registerEmpCalAndSumExeLog(String execId, ProcessExecutionLog procExecLog,
			ExecuteProcessExecutionCommand command, GeneralDate startClosingDate, GeneralDate endClosingDate) {
		GeneralDateTime now = GeneralDateTime.now();
		Integer processingMonth = Integer.valueOf(GeneralDate.today().toString("YYYYMM"));
			EmpCalAndSumExeLogOutput empCalAndSumExeLogOutput = new EmpCalAndSumExeLogOutput();
		// ドメインモデル「就業計算と集計実行ログ」を新規登録する
		EmpCalAndSumExeLog empCalAndSumExeLog = new EmpCalAndSumExeLog(
													execId,
													command.getCompanyId(),
													new YearMonth(processingMonth),
													ExecutedMenu.SELECT_AND_RUN,
													GeneralDate.today(),
													ExeStateOfCalAndSum.PROCESSING,
													AppContexts.user().employeeId(),
													1,
													IdentifierUtil.randomUniqueId());
		empCalAndSumExeLogOutput.setEmpCalAndSumExeLog(empCalAndSumExeLog);
		// ドメインモデル「実行ログ」を新規登録する
		ExecutionLog dailyCreateLog = new ExecutionLog(
				execId,
				ExecutionContent.DAILY_CREATION,
				ErrorPresent.NO_ERROR,
				new ExecutionTime(now, now),
				ExecutionStatus.INCOMPLETE,
				new ObjectPeriod(startClosingDate, endClosingDate)
				);
		dailyCreateLog.setDailyCreationSetInfo(
				new SettingInforForDailyCreation(ExecutionContent.DAILY_CREATION,
						ExecutionType.NORMAL_EXECUTION,
						IdentifierUtil.randomUniqueId(),
						DailyRecreateClassification.REBUILD, Optional.empty()));
		empCalAndSumExeLogOutput.addExecutionLog(dailyCreateLog);
		
		ExecutionLog dailyCalLog = new ExecutionLog(
				execId,
				ExecutionContent.DAILY_CALCULATION,
				ErrorPresent.NO_ERROR,
				new ExecutionTime(now, now),
				ExecutionStatus.INCOMPLETE,
				new ObjectPeriod(startClosingDate, endClosingDate)
				);
		dailyCalLog.setDailyCalSetInfo(new CalExeSettingInfor(ExecutionContent.DAILY_CALCULATION,
				ExecutionType.NORMAL_EXECUTION,
				IdentifierUtil.randomUniqueId()));
		empCalAndSumExeLogOutput.addExecutionLog(dailyCalLog);
		this.empCalSumRepo.add(empCalAndSumExeLog);
		this.executionLogRepository.addAllExecutionLog(empCalAndSumExeLogOutput.getExecutionLogs());
//		this.empCalSumRepo.addFromUpdateProcessing(empCalAndSumExeLog);
		
		return empCalAndSumExeLogOutput;
	}
	
	private void updateEachTaskStatus(ProcessExecutionLog procExecLog, ProcessExecutionTask execTask, EndStatus status) {
		procExecLog.getTaskLogList().forEach(task -> {
			if(execTask.value == task.getProcExecTask().value){
				task.setStatus(status);
			}
		});
	}
	
	private boolean isAbnormalTermEachTask(ProcessExecutionLog procExecLog) {
		for (ExecutionTaskLog task : procExecLog.getTaskLogList()) {
			if (EndStatus.ABNORMAL_END.value == task.getStatus().value) {
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
			task.setStatus(status);
		});
	}
	
	/**
	 * 個人スケジュール作成期間を計算する
	 * @param procExec
	 * @return 期間
	 */
	private void calculateSchedulePeriod(ProcessExecution procExec, ProcessExecutionLog procExecLog) {
		GeneralDate today = GeneralDate.today();
		// Add month
		GeneralDate startDate = today.addMonths(procExec.getExecSetting().getPerSchedule().getPeriod().getTargetMonth().value);
		
		// Add day
		int targetDate = procExec.getExecSetting().getPerSchedule().getPeriod().getTargetDate().v();
		// ※2　システム日付の日　＞　対象日　であった場合、システム日付の日とする。
		if (today.day() > targetDate) {
			targetDate = today.day();
		}
		try {
			startDate = GeneralDate.ymd(startDate.year(),
										startDate.month(),
										targetDate);
		} catch (DateTimeException dte) {
			// ※1　対象月の末日を超える対象日だった場合、対象月の末日とする。
			startDate = GeneralDate.ymd(startDate.year(),
										startDate.month(),
										startDate.localDate().lengthOfMonth());
		}
		
		// Add month
		GeneralDate endDate = startDate.addMonths(procExec.getExecSetting().getPerSchedule().getPeriod().getCreationPeriod().v());
		try {
			endDate = GeneralDate.ymd(endDate.year(),
									  endDate.month(),
									  procExec.getExecSetting().getPerSchedule().getPeriod().getTargetDate().v() - 1);
		} catch (DateTimeException dte) {
			endDate = GeneralDate.ymd(endDate.year(),
									  endDate.month(),
									  endDate.localDate().lengthOfMonth());
		}
		procExecLog.getEachProcPeriod().setScheduleCreationPeriod(new DatePeriod(startDate, endDate));
	}
	
	
	/**
	 * 日別作成.計算の期間を作成する
	 * @param procExec
	 * @return 期間
	 */
	private void calculateDailyPeriod(ProcessExecution procExec, ProcessExecutionLog procExecLog, String companyId, int closureId, CurrentMonth currentMonth) {
		DatePeriod closurePeriod = this.closureService.getClosurePeriod(closureId, currentMonth.getProcessingYm());
		
		// ドメインモデル「更新処理自動実行.実行設定.日別実績の作成・計算.作成・計算項目」を元に日別作成の期間を作成する
		GeneralDate crtStartDate = null;
		GeneralDate crtEndDate = null;
		// ドメインモデル「更新処理自動実行.実行設定.日別実績の作成・計算.作成・計算項目」を元に日別計算の期間を作成する
		GeneralDate calStartDate = null;
		GeneralDate calEndDate = null;
		
		GeneralDate lastExecDate = GeneralDate.today();
		GeneralDate today = GeneralDate.today();
		Optional<LastExecDateTime> lastDateTimeOpt =
				lastExecDateTimeRepo.get(procExec.getCompanyId(), procExec.getExecItemCd().v());
		if (lastDateTimeOpt.isPresent()) {
			GeneralDateTime lastExecDateTime = lastDateTimeOpt.get().getLastExecDateTime();
			if (lastExecDateTime != null) {
				lastExecDate = GeneralDate.ymd(lastExecDateTime.year(),
						lastExecDateTime.month(),
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
		}
		
		procExecLog.getEachProcPeriod().setDailyCreationPeriod(new DatePeriod(crtStartDate, crtEndDate));
		procExecLog.getEachProcPeriod().setDailyCalcPeriod(new DatePeriod(calStartDate, calEndDate));
	}
	/**
	 * 対象社員を絞り込み
	 * @param procExec
	 * @param employeeIdList
	 * @param period
	 */
	private void filterEmployeeList(ProcessExecution procExec, List<String> employeeIdList, DatePeriod datePeriod,
			List<String> reEmployeeList, List<String> newEmployeeList) {
		if (procExec.getExecSetting().getPerSchedule().getTarget().getCreationTarget().value == TargetClassification.ALL.value) {
			return;
		} else {
			String companyId = AppContexts.user().companyId();
			
			// ドメインモデル「就業締め日」を取得する
			List<Closure> closureList = this.closureRepo.findAllActive(companyId, UseClassification.UseClass_Use);
			DatePeriod closurePeriod = this.findClosurePeriod(companyId, closureList);
			TargetSetting setting = procExec.getExecSetting().getPerSchedule().getTarget().getTargetSetting();
			// 異動者を再作成するか判定
			if (setting.isRecreateTransfer()) {
				// Imported(勤務実績)「所属職場履歴」を取得する
				List<WorkPlaceHistImport> wkpImportList = this.workplaceAdapter.getWplByListSidAndPeriod(employeeIdList, datePeriod);
				
				// 「全締めの期間.開始日年月日」以降に「所属職場履歴.期間.開始日」が存在する
				wkpImportList.forEach(emp-> {
					final List<WorkPlaceIdAndPeriodImport> subList = emp.getLstWkpIdAndPeriod();
					for (WorkPlaceIdAndPeriodImport period : subList) {
						if (period.getDatePeriod().start().afterOrEquals(closurePeriod.start())) {
							// 取得したImported（勤務実績）「所属職場履歴」.社員IDを異動者とする
							reEmployeeList.add(emp.getEmployeeId());
							break;
						}
					}
				});
			}
			
			// 勤務種別変更者を再作成するか判定
			if (setting.isRecreateWorkType()) {
				employeeIdList.forEach(x-> {
					// ドメインモデル「社員の勤務種別の履歴」を取得する
					Optional<BusinessTypeOfEmployeeHistory> optional = this.typeEmployeeOfHistoryRepos
							.findByEmployeeDesc(AppContexts.user().companyId(), x);
					if (optional.isPresent()) {
						for (DateHistoryItem history : optional.get().getHistory()) {
							// 「全締めの期間.開始日年月日」以降に「社員の勤務種別の履歴.履歴.期間.開始日」が存在する
							if (history.start().afterOrEquals(closurePeriod.start())) {
								// 取得したImported（勤務実績）「所属職場履歴」.社員IDを異動者とする
								reEmployeeList.add(optional.get().getEmployeeId());
								break;
							}
						}
					}
				});
			}
			
			// 新入社員を作成するか判定
			if (setting.isCreateEmployee()) {
				// Imported「所属会社履歴（社員別）」を取得する
				List<AffCompanyHistImport> employeeHistList = this.employeeHistAdapter.getWplByListSidAndPeriod(employeeIdList, datePeriod);
				
				// 取得したドメインモデル「所属開始履歴（社員別）」.社員IDを新入社員とする
				employeeHistList.forEach(x->newEmployeeList.add(x.getEmployeeId()));
			}
		}
	}
	
private ExeStateOfCalAndSum updateExecutionState(String empCalAndSumExecLogID){
		
		// 0 : 完了
		// 1 : 完了（エラーあり）
		ExeStateOfCalAndSum executionStatus = ExeStateOfCalAndSum.DONE;
		
		List<ErrMessageInfo> errMessageInfos = this.errMessageInfoRepository.getAllErrMessageInfoByEmpID(empCalAndSumExecLogID);
		List<String> errorMessage = errMessageInfos.stream().map(error -> {
			return error.getMessageError().v();
		}).collect(Collectors.toList());
		if (errorMessage.isEmpty()) {
			executionStatus = ExeStateOfCalAndSum.DONE;

		} else {
			executionStatus = ExeStateOfCalAndSum.DONE_WITH_ERROR;
		}
		
		return executionStatus;
	}
}
