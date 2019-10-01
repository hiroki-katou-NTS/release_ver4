/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.at.schedule.app.command.executionlog;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;

import lombok.val;
import nts.arc.layer.app.command.AsyncCommandHandler;
import nts.arc.layer.app.command.CommandHandlerContext;
import nts.arc.task.parallel.ManagedParallelWithContext;
import nts.gul.collection.CollectionUtil;
import nts.uk.ctx.at.schedule.dom.adapter.ScTimeAdapter;
import nts.uk.ctx.at.schedule.dom.adapter.dailymonthlyprocessing.DailyMonthlyprocessAdapterSch;
import nts.uk.ctx.at.schedule.dom.adapter.dailymonthlyprocessing.ExeStateOfCalAndSumImportSch;
import nts.uk.ctx.at.schedule.dom.adapter.employmentstatus.EmploymentInfoImported;
import nts.uk.ctx.at.schedule.dom.adapter.employmentstatus.EmploymentStatusAdapter;
import nts.uk.ctx.at.schedule.dom.adapter.employmentstatus.EmploymentStatusImported;
import nts.uk.ctx.at.schedule.dom.adapter.executionlog.ScShortWorkTimeAdapter;
import nts.uk.ctx.at.schedule.dom.adapter.executionlog.dto.ShortWorkTimeDto;
import nts.uk.ctx.at.schedule.dom.adapter.generalinfo.EmployeeGeneralInfoImported;
import nts.uk.ctx.at.schedule.dom.adapter.generalinfo.ScEmployeeGeneralInfoAdapter;
import nts.uk.ctx.at.schedule.dom.adapter.generalinfo.employment.ExEmploymentHistItemImported;
import nts.uk.ctx.at.schedule.dom.adapter.generalinfo.employment.ExEmploymentHistoryImported;
import nts.uk.ctx.at.schedule.dom.executionlog.CompletionStatus;
import nts.uk.ctx.at.schedule.dom.executionlog.ExecutionAtr;
import nts.uk.ctx.at.schedule.dom.executionlog.ExecutionStatus;
import nts.uk.ctx.at.schedule.dom.executionlog.ScheduleCreateContent;
import nts.uk.ctx.at.schedule.dom.executionlog.ScheduleCreateContentRepository;
import nts.uk.ctx.at.schedule.dom.executionlog.ScheduleCreator;
import nts.uk.ctx.at.schedule.dom.executionlog.ScheduleCreatorRepository;
import nts.uk.ctx.at.schedule.dom.executionlog.ScheduleErrorLog;
import nts.uk.ctx.at.schedule.dom.executionlog.ScheduleErrorLogRepository;
import nts.uk.ctx.at.schedule.dom.executionlog.ScheduleExecutionLog;
import nts.uk.ctx.at.schedule.dom.executionlog.ScheduleExecutionLogRepository;
import nts.uk.ctx.at.schedule.dom.schedule.algorithm.WorkRestTimeZoneDto;
import nts.uk.ctx.at.schedule.dom.schedule.basicschedule.BasicSchedule;
import nts.uk.ctx.at.schedule.dom.schedule.basicschedule.BasicScheduleRepository;
import nts.uk.ctx.at.shared.dom.dailyperformanceformat.businesstype.BusinessTypeOfEmpDto;
import nts.uk.ctx.at.shared.dom.dailyperformanceformat.businesstype.BusinessTypeOfEmpHisAdaptor;
import nts.uk.ctx.at.shared.dom.workingcondition.WorkingCondition;
import nts.uk.ctx.at.shared.dom.workingcondition.WorkingConditionItem;
import nts.uk.ctx.at.shared.dom.workingcondition.WorkingConditionItemRepository;
import nts.uk.ctx.at.shared.dom.workingcondition.WorkingConditionRepository;
import nts.uk.ctx.at.shared.dom.workrule.closure.Closure;
import nts.uk.ctx.at.shared.dom.workrule.closure.ClosureEmployment;
import nts.uk.ctx.at.shared.dom.workrule.closure.ClosureEmploymentRepository;
import nts.uk.ctx.at.shared.dom.workrule.closure.ClosureRepository;
import nts.uk.ctx.at.shared.dom.workrule.closure.service.ClosureService;
import nts.uk.ctx.at.shared.dom.worktime.common.WorkTimeCode;
import nts.uk.ctx.at.shared.dom.worktime.difftimeset.DiffTimeWorkSettingRepository;
import nts.uk.ctx.at.shared.dom.worktime.fixedset.FixedWorkSettingRepository;
import nts.uk.ctx.at.shared.dom.worktime.flowset.FlowWorkSettingRepository;
import nts.uk.ctx.at.shared.dom.worktime.perfomance.AmPmWorkTimezone;
import nts.uk.ctx.at.shared.dom.worktime.worktimeset.WorkTimeDailyAtr;
import nts.uk.ctx.at.shared.dom.worktime.worktimeset.WorkTimeDivision;
import nts.uk.ctx.at.shared.dom.worktime.worktimeset.WorkTimeMethodSet;
import nts.uk.ctx.at.shared.dom.worktime.worktimeset.WorkTimeSettingRepository;
import nts.uk.ctx.at.shared.dom.worktype.WorkTypeRepository;
import nts.uk.shr.com.context.AppContexts;
import nts.uk.shr.com.context.LoginUserContext;
import nts.uk.shr.com.time.calendar.period.DatePeriod;
import nts.uk.shr.infra.i18n.resource.I18NResourcesForUK;

/**
 * The Class ScheduleCreatorExecutionCommandHandler.
 *///
@TransactionAttribute(TransactionAttributeType.SUPPORTS)
@Stateless
public class ScheduleCreatorExecutionCommandHandler extends AsyncCommandHandler<ScheduleCreatorExecutionCommand> {

	@Inject
	private ManagedParallelWithContext parallel;
	
	/** The basic schedule repository. */
	@Inject
	private BasicScheduleRepository basicScheduleRepository;

	/** The schedule execution log repository. */
	@Inject
	private ScheduleExecutionLogRepository scheduleExecutionLogRepository;

	/** The schedule creator repository. */
	@Inject
	private ScheduleCreatorRepository scheduleCreatorRepository;

	/** The schedule error log repository. */
	@Inject
	private ScheduleErrorLogRepository scheduleErrorLogRepository;

	/** The content repository. */
	@Inject
	private ScheduleCreateContentRepository contentRepository;

	/** The create content repository. */
	@Inject
	private ScheduleCreateContentRepository scheduleCreateContentRepository;

	@Inject
	private ScEmployeeGeneralInfoAdapter scEmpGeneralInfoAdapter;

	@Inject
	private EmploymentStatusAdapter employmentStatusAdapter;

	@Inject
	private WorkingConditionRepository workingConditionRepository;

	@Inject
	private WorkingConditionItemRepository workingConditionItemRepository;

	@Inject
	private WorkTypeRepository workTypeRepository;

	@Inject
	private WorkTimeSettingRepository workTimeSettingRepository;

	@Inject
	private BusinessTypeOfEmpHisAdaptor businessTypeOfEmpHisAdaptor;

	@Inject
	private FixedWorkSettingRepository fixedWorkSettingRepository;

	@Inject
	private FlowWorkSettingRepository flowWorkSettingRepository;

	@Inject
	private DiffTimeWorkSettingRepository diffTimeWorkSettingRepository;

	@Inject
	private ScShortWorkTimeAdapter scShortWorkTimeAdapter;
	
	@Inject
	private ScTimeAdapter scTimeAdapter;
	
	@Inject
	private ScheduleCreatorExecutionTransaction transaction;

	@Inject
	private ClosureEmploymentRepository closureEmployment;

	@Inject
	private ClosureRepository closureRepository;

	@Inject
	private ClosureService closureService;
	
	@Inject
	private I18NResourcesForUK internationalization;

	/** The Constant DEFAULT_CODE. */
	public static final String DEFAULT_CODE = "000";

	/** The Constant NEXT_DAY_MONTH. */
	public static final int NEXT_DAY_MONTH = 1;

	/** The Constant ZERO_DAY_MONTH. */
	public static final int ZERO_DAY_MONTH = 0;

	/** The Constant MUL_YEAR. */
	public static final int MUL_YEAR = 10000;

	/** The Constant MUL_MONTH. */
	public static final int MUL_MONTH = 100;

	/** The Constant SHIFT1. */
	public static final int SHIFT1 = 1;

	/** The Constant SHIFT2. */
	public static final int SHIFT2 = 2;

	/** The Constant BEFORE_JOINING. */
	// 入社前
	public static final int BEFORE_JOINING = 4;

	/** The Constant RETIREMENT. */
	// 退職
	public static final int RETIREMENT = 6;

	/** The Constant INCUMBENT. */
	// 在職
	public static final int INCUMBENT = 1;

	/** The Constant LEAVE_OF_ABSENCE. */
	// 休職
	public static final int LEAVE_OF_ABSENCE = 2;

	/** The Constant HOLIDAY. */
	// 休業
	public static final int HOLIDAY = 3;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nts.arc.layer.app.command.AsyncCommandHandler#handle(nts.arc.layer.app.
	 * command.CommandHandlerContext)
	 */

	@Override
	public void handle(CommandHandlerContext<ScheduleCreatorExecutionCommand> context) {
		System.out.println("Run batch service !");
		LoginUserContext loginUserContext = AppContexts.user();

		// get company id
		String companyId = loginUserContext.companyId();

		// get command
		ScheduleCreatorExecutionCommand command = context.getCommand();

		if (!command.isAutomatic()) {
			ScheduleExecutionLog scheduleExecutionLog = new ScheduleExecutionLog();

			// update command
			command.setCompanyId(companyId);
//			command.setIsDeleteBeforInsert(false);

			// find execution log by id
			scheduleExecutionLog = this.scheduleExecutionLogRepository.findById(companyId, command.getExecutionId())
					.get();

			// update execution time to now
			scheduleExecutionLog.setExecutionTimeToNow();

			// set exeAtr is manual
			scheduleExecutionLog.setExeAtrIsManual();

			// update domain execution log
			this.scheduleExecutionLogRepository.update(scheduleExecutionLog);

			// find execution content by id
			ScheduleCreateContent scheCreContent = this.contentRepository.findByExecutionId(command.getExecutionId())
					.get();
			command.setContent(scheCreContent);

			command.setConfirm(scheCreContent.getConfirm());
			// register personal schedule

			this.registerPersonalSchedule(command, scheduleExecutionLog, context, companyId);
			return;
		}

		ScheduleExecutionLog scheduleExecutionLogAuto = ScheduleExecutionLog.creator(companyId,
				command.getScheduleExecutionLog().getExecutionId(), loginUserContext.employeeId(),
				command.getScheduleExecutionLog().getPeriod(), command.getScheduleExecutionLog().getExeAtr());
		try {
		this.registerPersonalSchedule(command, scheduleExecutionLogAuto, context, companyId);
		} catch(Exception ex) {
			command.setIsExForKBT(true);
			if(command.getCountDownLatch() != null)
				command.getCountDownLatch().countDown();
			throw ex;
		}
		if(command.getCountDownLatch() != null)
			command.getCountDownLatch().countDown();
	}

	@Inject
	private DailyMonthlyprocessAdapterSch dailyMonthlyprocessAdapterSch;
	
	/**
	 * 個人スケジュールを登録する: register Personal Schedule
	 * 
	 * @param command
	 * @param scheduleExecutionLog
	 * @param context
	 */
	private void registerPersonalSchedule(
			ScheduleCreatorExecutionCommand command,
			ScheduleExecutionLog scheduleExecutionLog,
			CommandHandlerContext<ScheduleCreatorExecutionCommand> context,
			String companyId) {

		String exeId = command.getExecutionId();
		DatePeriod period = scheduleExecutionLog.getPeriod();

		// パラメータ実施区分を判定
		if (scheduleExecutionLog.getExeAtr() == ExecutionAtr.AUTOMATIC) {
			createExcutionLog(command, scheduleExecutionLog);
		}

		// get all data creator
		List<ScheduleCreator> scheduleCreators = this.scheduleCreatorRepository.findAll(exeId);
		List<String> employeeIds = scheduleCreators.stream().map(item -> item.getEmployeeId())
				.collect(Collectors.toList());

		// EA No2017
		// マスタ情報を取得する
		CreateScheduleMasterCache masterCache = this.acquireData(companyId, employeeIds, period);
		
		// get info by context
		val asyncTask = context.asAsync();
		
		// at.recordの計算処理で使用する共通の会社設定は、ここで取得しキャッシュしておく
		Object companySetting = scTimeAdapter.getCompanySettingForCalculation();
		AtomicBoolean checkStop = new AtomicBoolean(false);
		this.parallel.forEach(
				scheduleCreators.stream().sorted((a,b) -> a.getEmployeeId().compareTo(b.getEmployeeId())).collect(Collectors.toList()),
				scheduleCreator -> {
			if (scheduleExecutionLog.getExeAtr() == ExecutionAtr.AUTOMATIC) {
				if(checkStop.get()) {
					return;
				}
				Optional<ExeStateOfCalAndSumImportSch> exeStateOfCalAndSumImportSch = dailyMonthlyprocessAdapterSch.executionStatus(exeId);
				if(exeStateOfCalAndSumImportSch.isPresent())
					if(exeStateOfCalAndSumImportSch.get() == ExeStateOfCalAndSumImportSch.START_INTERRUPTION) {
						checkStop.set(true);
						this.updateStatusScheduleExecutionLog(scheduleExecutionLog, CompletionStatus.INTERRUPTION);
						return;
					}
			}else {
				// check is client submit cancel
				if (asyncTask.hasBeenRequestedToCancel()) {
					// ドメインモデル「スケジュール作成実行ログ」を更新する(update domain 「スケジュール作成実行ログ」)
					this.updateStatusScheduleExecutionLog(scheduleExecutionLog, CompletionStatus.INTERRUPTION);
					return;
				}
			}

			// アルゴリズム「対象期間を締め開始日以降に補正する」を実行する
			StateAndValueDatePeriod stateAndValueDatePeriod = this.correctTargetPeriodAfterClosingStartDate(
					command.getCompanyId(), scheduleCreator.getEmployeeId(), period,
					masterCache.getEmpGeneralInfo());

			if (stateAndValueDatePeriod.state) {
				DatePeriod dateAfterCorrection = stateAndValueDatePeriod.getValue();
			
				// process each by 2 months to make transaction small for performance
				final int unitMonthsOfTransaction = 2;
				dateAfterCorrection.forEachByMonths(unitMonthsOfTransaction, subPeriod -> {

					List<BasicSchedule> listBasicSchedule = this.basicScheduleRepository.findSomePropertyWithJDBC(
							Arrays.asList(scheduleCreator.getEmployeeId()), subPeriod);
					
					this.transaction.execute(
							command,
							scheduleExecutionLog,
							context,
							companyId,
							exeId,
							subPeriod,
							masterCache,
							listBasicSchedule,
							asyncTask,
							companySetting,
							scheduleCreator);
				});
			} else {
				String errorContent = this.internationalization.localize("Msg_1509").get();
				// ドメインモデル「スケジュール作成エラーログ」を登録する
				ScheduleErrorLog scheduleErrorLog = new ScheduleErrorLog(errorContent, command.getExecutionId(),
						stateAndValueDatePeriod.value.end(), scheduleCreator.getEmployeeId());
				this.scheduleErrorLogRepository.add(scheduleErrorLog);
				
				scheduleCreator.updateToCreated();
				this.scheduleCreatorRepository.update(scheduleCreator);
			}
		});
		scTimeAdapter.clearCompanySettingShareContainer(companySetting);
		
		if (scheduleExecutionLog.getExeAtr() == ExecutionAtr.AUTOMATIC) {
			Optional<ExeStateOfCalAndSumImportSch> exeStateOfCalAndSumImportSch = dailyMonthlyprocessAdapterSch.executionStatus(exeId);
			if(exeStateOfCalAndSumImportSch.isPresent())
				if(exeStateOfCalAndSumImportSch.get() == ExeStateOfCalAndSumImportSch.START_INTERRUPTION) {
					return;
				}
			// EA修正履歴　No2378
			// ドメインモデル「スケジュール作成実行ログ」を取得する find execution log by id
			ScheduleExecutionLog scheExeLog = this.scheduleExecutionLogRepository
					.findById(command.getCompanyId(), scheduleExecutionLog.getExecutionId()).get();
			if (scheExeLog.getCompletionStatus() != CompletionStatus.INTERRUPTION) {
				this.updateStatusScheduleExecutionLog(scheduleExecutionLog);
			}
		}else {
			if (asyncTask.hasBeenRequestedToCancel()) {
				asyncTask.finishedAsCancelled();
			}
			ScheduleExecutionLog scheExeLog = this.scheduleExecutionLogRepository
					.findById(command.getCompanyId(), scheduleExecutionLog.getExecutionId()).get();
			if (scheExeLog.getCompletionStatus() != CompletionStatus.INTERRUPTION) {
				System.out.println("not hasBeenRequestedToCancel: " + asyncTask.hasBeenRequestedToCancel() + "&exeid="
						+ scheduleExecutionLog.getExecutionId());
				this.updateStatusScheduleExecutionLog(scheduleExecutionLog);
			}
		}
		
	}
	
	/**
	 * 実行ログ作成処理
	 * 
	 * @author danpv
	 */
	private void createExcutionLog(ScheduleCreatorExecutionCommand command, ScheduleExecutionLog scheduleExecutionLog) {
		ScheduleCreateContent scheduleCreateContent = command.getContent();
		List<ScheduleCreator> scheduleCreators = command.getEmployeeIds().stream()
				.map(sId -> new ScheduleCreator(command.getExecutionId(), ExecutionStatus.NOT_CREATED, sId))
				.collect(Collectors.toList());
		// アルゴリズム「実行ログ作成処理」を実行する
		this.executionLogCreationProcess(scheduleExecutionLog, scheduleCreateContent, scheduleCreators);
	}

	/**
	 * 実行ログ作成処理
	 * 
	 * @param scheduleExecutionLog
	 * @param scheduleCreateContent
	 * @param scheduleCreators
	 */
	private void executionLogCreationProcess(ScheduleExecutionLog scheduleExecutionLog,
			ScheduleCreateContent scheduleCreateContent, List<ScheduleCreator> scheduleCreators) {
		// ドメインモデル「スケジュール作成実行ログ」を新規登録する
		this.scheduleExecutionLogRepository.add(scheduleExecutionLog);
		// ドメインモデル「スケジュール作成内容」を新規登録する
		this.scheduleCreateContentRepository.add(scheduleCreateContent);
		// ドメインモデル「スケジュール作成対象者」を新規登録する
		this.scheduleCreatorRepository.saveAll(scheduleCreators);
	}
	
	/**
	 * マスタ情報を取得する
	 * 
	 * @param companyId
	 * @param listWorkType
	 * @param listWorkTimeSetting
	 * @param mapFixedWorkSetting
	 * @param listFlowWorkSetting
	 * @param listDiffTimeWorkSetting
	 */
	private CreateScheduleMasterCache acquireData(String companyId, List<String> employeeIds, DatePeriod period) {
		
		// EA No1675
		// Imported(就業)「社員の履歴情報」を取得する
		EmployeeGeneralInfoImported empGeneralInfo = this.scEmpGeneralInfoAdapter.getPerEmpInfo(employeeIds, period);

		// Imported(就業)「社員の在職状態」を取得する
		Map<String, List<EmploymentInfoImported>> mapEmploymentStatus = this.employmentStatusAdapter
				.findListOfEmployee(employeeIds, period).stream().collect(Collectors
						.toMap(EmploymentStatusImported::getEmployeeId, EmploymentStatusImported::getEmploymentInfo));

		// 労働条件情報を取得する
		// EA No1828
		List<WorkCondItemDto> listWorkingConItem = this.acquireWorkingConditionInformation(employeeIds, period);

		// 社員の短時間勤務履歴を取得する
		// EA No2134
		List<ShortWorkTimeDto> listShortWorkTimeDto = this.scShortWorkTimeAdapter.findShortWorkTimes(employeeIds, period);

		// 勤務種別情報を取得する
		// ドメインモデル「社員の勤務種別の履歴」を取得する
		// ドメインモデル「社員の勤務種別」を取得する
		List<BusinessTypeOfEmpDto> listBusTypeOfEmpHis = this.businessTypeOfEmpHisAdaptor
				.findByCidSidBaseDate(companyId, employeeIds, period);
		
		CreateScheduleMasterCache cache = new CreateScheduleMasterCache(
				empGeneralInfo,
				mapEmploymentStatus,
				listWorkingConItem,
				listShortWorkTimeDto,
				listBusTypeOfEmpHis);
		
		
		// ドメインモデル「勤務種類」を取得する
		cache.getListWorkType().addAll(this.workTypeRepository.findNotDeprecateByCompanyId(companyId));
		// ドメインモデル「就業時間帯の設定」を取得する
		cache.getListWorkTimeSetting().addAll(this.workTimeSettingRepository.findActiveItems(companyId));
		// EA修正履歴 No2103
		List<String> listWorkTimeCodeFix = new ArrayList<>();
		List<String> listWorkTimeCodeFlow = new ArrayList<>();
		List<String> listWorkTimeCodeDiff = new ArrayList<>();
		cache.getListWorkTimeSetting().forEach(workTime -> {
			WorkTimeDivision workTimeDivision = workTime.getWorkTimeDivision();
			if (workTimeDivision.getWorkTimeDailyAtr() == WorkTimeDailyAtr.REGULAR_WORK) {
				if (workTimeDivision.getWorkTimeMethodSet() == WorkTimeMethodSet.FIXED_WORK) {
					listWorkTimeCodeFix.add(workTime.getWorktimeCode().v());
				} else if (workTimeDivision.getWorkTimeMethodSet() == WorkTimeMethodSet.FLOW_WORK) {
					listWorkTimeCodeFlow.add(workTime.getWorktimeCode().v());
				} else {
					listWorkTimeCodeDiff.add(workTime.getWorktimeCode().v());
				}
			}
		});
		// ドメインモデル「固定勤務設定」を取得する
		if (!listWorkTimeCodeFix.isEmpty()) {
			Map<WorkTimeCode, List<AmPmWorkTimezone>> mapFixOffdayWorkRestTimezones = this.fixedWorkSettingRepository
					.getFixOffdayWorkRestTimezones(companyId, listWorkTimeCodeFix);
			Map<WorkTimeCode, List<AmPmWorkTimezone>> mapFixHalfDayWorkRestTimezones = this.fixedWorkSettingRepository
					.getFixHalfDayWorkRestTimezones(companyId, listWorkTimeCodeFix);
			this.setDataForMap(cache.getMapFixedWorkSetting(), mapFixOffdayWorkRestTimezones, mapFixHalfDayWorkRestTimezones);
		}
		// ドメインモデル「流動勤務設定」を取得する
		if (!listWorkTimeCodeFlow.isEmpty()) {
			Map<WorkTimeCode, List<AmPmWorkTimezone>> mapFlowOffdayWorkRestTimezones = this.flowWorkSettingRepository
					.getFlowOffdayWorkRestTimezones(companyId, listWorkTimeCodeFlow);
			Map<WorkTimeCode, List<AmPmWorkTimezone>> mapFlowHalfDayWorkRestTimezones = this.flowWorkSettingRepository
					.getFlowHalfDayWorkRestTimezones(companyId, listWorkTimeCodeFlow);
			this.setDataForMap(cache.getMapFlowWorkSetting(), mapFlowOffdayWorkRestTimezones, mapFlowHalfDayWorkRestTimezones);
		}
		// ドメインモデル「時差勤務設定」を取得する
		if (!listWorkTimeCodeDiff.isEmpty()) {
			Map<WorkTimeCode, List<AmPmWorkTimezone>> mapDiffOffdayWorkRestTimezones = this.diffTimeWorkSettingRepository
					.getDiffOffdayWorkRestTimezones(companyId, listWorkTimeCodeDiff);
			Map<WorkTimeCode, List<AmPmWorkTimezone>> mapDiffHalfDayWorkRestTimezones = this.diffTimeWorkSettingRepository
					.getDiffHalfDayWorkRestTimezones(companyId, listWorkTimeCodeDiff);

			this.setDataForMap(cache.getMapDiffTimeWorkSetting(), mapDiffOffdayWorkRestTimezones, mapDiffHalfDayWorkRestTimezones);
		}

		return cache;
	}
	
	/**
	 * 労働条件情報を取得する
	 * 
	 * @param sIds
	 * @param datePeriod
	 * @return
	 */
	private List<WorkCondItemDto> acquireWorkingConditionInformation(List<String> sIds, DatePeriod datePeriod) {
		// EA修正履歴 No1829
		List<WorkingCondition> listWorkingCondition = this.workingConditionRepository.getBySidsAndDatePeriod(sIds,
				datePeriod);

		List<WorkingConditionItem> listWorkingConditionItem = this.workingConditionItemRepository
				.getBySidsAndDatePeriod(sIds, datePeriod);
		Map<String, WorkingConditionItem> mapWorkingCondtionItem = listWorkingConditionItem.stream()
				.collect(Collectors.toMap(WorkingConditionItem::getHistoryId, x -> x));
		List<WorkCondItemDto> listWorkCondItemDto = new ArrayList<>();
		listWorkingCondition.forEach(x -> x.getDateHistoryItem().forEach(y -> {
			WorkingConditionItem workingConditionItem = mapWorkingCondtionItem.get(y.identifier());
			WorkCondItemDto workCondItemDto = new WorkCondItemDto(workingConditionItem);
			workCondItemDto.setDatePeriod(y.span());
			listWorkCondItemDto.add(workCondItemDto);
		}));

		return listWorkCondItemDto;
	}
	
	/**
	 * 
	 * @param map
	 * @param map1
	 * @param map2
	 */
	private void setDataForMap(Map<String, WorkRestTimeZoneDto> map, Map<WorkTimeCode, List<AmPmWorkTimezone>> map1,
			Map<WorkTimeCode, List<AmPmWorkTimezone>> map2) {
		if (map1.size() >= map2.size()) {
			map1.forEach((key, value) -> {
				map.put(key.v(), new WorkRestTimeZoneDto(value, map2.get(key)));
			});
		} else {
			map2.forEach((key, value) -> {
				map.put(key.v(), new WorkRestTimeZoneDto(map1.get(key), value));
			});
		}
	}

	/**
	 * Update status schedule execution log.
	 *
	 * @param domain
	 *            the domain
	 */
	private void updateStatusScheduleExecutionLog(ScheduleExecutionLog domain) {
		List<ScheduleErrorLog> scheduleErrorLogs = this.scheduleErrorLogRepository
				.findByExecutionId(domain.getExecutionId());

		// check exist data schedule error log
		if (CollectionUtil.isEmpty(scheduleErrorLogs)) {
			domain.setCompletionStatus(CompletionStatus.DONE);
		} else {
			domain.setCompletionStatus(CompletionStatus.COMPLETION_ERROR);
		}
		domain.updateExecutionTimeEndToNow();
		this.scheduleExecutionLogRepository.update(domain);
	}


	/**
	 * Update status schedule execution log.
	 *
	 * @param domain
	 *            the domain
	 */
	private void updateStatusScheduleExecutionLog(ScheduleExecutionLog domain, CompletionStatus completionStatus) {
		// check exist data schedule error log
		domain.setCompletionStatus(completionStatus);
		domain.updateExecutionTimeEndToNow();
		this.scheduleExecutionLogRepository.update(domain);
	}
	

	/**
	 * アルゴリズム「対象期間を締め開始日以降に補正する」を実行する
	 * 
	 * @param companyId
	 * @param employeeId
	 * @param dateBeforeCorrection
	 * @param empGeneralInfo
	 * @return
	 */
	private StateAndValueDatePeriod correctTargetPeriodAfterClosingStartDate(String companyId, String employeeId,
			DatePeriod dateBeforeCorrection, EmployeeGeneralInfoImported empGeneralInfo) {
		// EA No1676
		Map<String, List<ExEmploymentHistItemImported>> mapEmploymentHist = empGeneralInfo.getEmploymentDto().stream()
				.collect(Collectors.toMap(ExEmploymentHistoryImported::getEmployeeId,
						ExEmploymentHistoryImported::getEmploymentItems));

		List<ExEmploymentHistItemImported> listEmpHistItem = mapEmploymentHist.get(employeeId);
		Optional<ExEmploymentHistItemImported> optEmpHistItem = Optional.empty();
		if (listEmpHistItem != null) {
			optEmpHistItem = listEmpHistItem.stream()
					.filter(empHistItem -> empHistItem.getPeriod().contains(dateBeforeCorrection.end())).findFirst();
		}

		if (!optEmpHistItem.isPresent()) {
			return new StateAndValueDatePeriod(dateBeforeCorrection, false);
		}

		// ドメインモデル「雇用に紐づく就業締め」を取得
		Optional<ClosureEmployment> optionalClosureEmployment = this.closureEmployment.findByEmploymentCD(companyId,
				optEmpHistItem.get().getEmploymentCode());
		if (!optionalClosureEmployment.isPresent())
			return new StateAndValueDatePeriod(dateBeforeCorrection, false);
		// ドメインモデル「締め」を取得
		Optional<Closure> optionalClosure = this.closureRepository.findById(companyId,
				optionalClosureEmployment.get().getClosureId());
		if (!optionalClosure.isPresent())
			return new StateAndValueDatePeriod(dateBeforeCorrection, false);
		// アルゴリズム「当月の期間を算出する」を実行
		DatePeriod dateP = this.closureService.getClosurePeriod(optionalClosure.get().getClosureId().value,
				optionalClosure.get().getClosureMonth().getProcessingYm());
		// Input「対象開始日」と、取得した「開始年月日」を比較
		DatePeriod dateAfterCorrection = dateBeforeCorrection;
		if (dateBeforeCorrection.start().before(dateP.start())) {
			dateAfterCorrection = dateBeforeCorrection.cutOffWithNewStart(dateP.start());
		}
		// Output「対象開始日(補正後)」に、取得した「締め期間. 開始日年月日」を設定する
		if (dateAfterCorrection.start().beforeOrEquals(dateBeforeCorrection.end())) {
			// Out「対象終了日(補正後)」に、Input「対象終了日」を設定する
			dateAfterCorrection = dateAfterCorrection.cutOffWithNewEnd(dateBeforeCorrection.end());
			return new StateAndValueDatePeriod(dateAfterCorrection, true);
		}

		return new StateAndValueDatePeriod(dateAfterCorrection, false);
	}
}