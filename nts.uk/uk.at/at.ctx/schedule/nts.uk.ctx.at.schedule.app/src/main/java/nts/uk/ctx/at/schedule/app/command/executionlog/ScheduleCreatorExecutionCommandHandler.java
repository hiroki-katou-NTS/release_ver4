/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.at.schedule.app.command.executionlog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;

import lombok.val;
import nts.arc.layer.app.command.AsyncCommandHandler;
import nts.arc.layer.app.command.CommandHandlerContext;
import nts.arc.task.AsyncTask;
import nts.arc.task.parallel.ManagedParallelWithContext;
import nts.arc.task.parallel.ParallelWithContext;
import nts.arc.time.GeneralDate;
import nts.gul.collection.CollectionUtil;
import nts.uk.ctx.at.schedule.app.command.executionlog.internal.BasicScheduleResetCommand;
import nts.uk.ctx.at.schedule.app.command.executionlog.internal.ScheCreExeBasicScheduleHandler;
import nts.uk.ctx.at.schedule.app.command.executionlog.internal.ScheCreExeMonthlyPatternHandler;
import nts.uk.ctx.at.schedule.app.command.executionlog.internal.ScheCreExeWorkTypeHandler;
import nts.uk.ctx.at.schedule.dom.adapter.ScTimeAdapter;
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
import nts.uk.ctx.at.schedule.dom.executionlog.CreateMethodAtr;
import nts.uk.ctx.at.schedule.dom.executionlog.ExecutionAtr;
import nts.uk.ctx.at.schedule.dom.executionlog.ExecutionStatus;
import nts.uk.ctx.at.schedule.dom.executionlog.ImplementAtr;
import nts.uk.ctx.at.schedule.dom.executionlog.ProcessExecutionAtr;
import nts.uk.ctx.at.schedule.dom.executionlog.ReCreateAtr;
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
import nts.uk.ctx.at.schedule.dom.schedule.basicschedule.ConfirmedAtr;
import nts.uk.ctx.at.schedule.dom.schedule.basicschedule.service.DateRegistedEmpSche;
import nts.uk.ctx.at.schedule.dom.schedule.basicschedule.service.RegistrationListDateSchedule;
import nts.uk.ctx.at.schedule.dom.schedule.schedulemaster.ScheMasterInfo;
import nts.uk.ctx.at.shared.dom.dailyperformanceformat.businesstype.BusinessTypeOfEmpDto;
import nts.uk.ctx.at.shared.dom.dailyperformanceformat.businesstype.BusinessTypeOfEmpHisAdaptor;
import nts.uk.ctx.at.shared.dom.remainingnumber.algorithm.InterimRemainDataMngRegisterDateChange;
import nts.uk.ctx.at.shared.dom.workingcondition.ManageAtr;
import nts.uk.ctx.at.shared.dom.workingcondition.WorkScheduleBasicCreMethod;
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
import nts.uk.ctx.at.shared.dom.worktime.worktimeset.WorkTimeSetting;
import nts.uk.ctx.at.shared.dom.worktime.worktimeset.WorkTimeSettingRepository;
import nts.uk.ctx.at.shared.dom.worktype.WorkType;
import nts.uk.ctx.at.shared.dom.worktype.WorkTypeRepository;
import nts.uk.shr.com.context.AppContexts;
import nts.uk.shr.com.context.LoginUserContext;
import nts.uk.shr.com.time.calendar.period.DatePeriod;
import nts.uk.shr.infra.i18n.resource.I18NResourcesForUK;

/**
 * The Class ScheduleCreatorExecutionCommandHandler.
 */
@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
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

	/** The sche cre exe work type handler. */
	@Inject
	private ScheCreExeWorkTypeHandler scheCreExeWorkTypeHandler;

	/** The sche cre exe basic schedule handler. */
	@Inject
	private ScheCreExeBasicScheduleHandler scheCreExeBasicScheduleHandler;

	@Inject
	private ScheCreExeMonthlyPatternHandler scheCreExeMonthlyPatternHandler;

	/** The create content repository. */
	@Inject
	private ScheduleCreateContentRepository scheduleCreateContentRepository;

	@Inject
	private I18NResourcesForUK internationalization;

	@Inject
	private ClosureEmploymentRepository closureEmployment;

	@Inject
	private ClosureRepository closureRepository;

	@Inject
	private ClosureService closureService;

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
	private InterimRemainDataMngRegisterDateChange interimRemainDataMngRegisterDateChange;
	
	@Inject
	private ScTimeAdapter scTimeAdapter;


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

	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	@Override
	public void handle(CommandHandlerContext<ScheduleCreatorExecutionCommand> context) {
		LoginUserContext loginUserContext = AppContexts.user();

		// get company id
		String companyId = loginUserContext.companyId();

		// get command
		ScheduleCreatorExecutionCommand command = context.getCommand();

		if (!command.isAutomatic()) {
			ScheduleExecutionLog scheduleExecutionLog = new ScheduleExecutionLog();

			// update command
			command.setCompanyId(companyId);
			command.setIsDeleteBeforInsert(false);

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
		this.registerPersonalSchedule(command, scheduleExecutionLogAuto, context, companyId);
	}

	/**
	 * Reset schedule.
	 *
	 * @param command
	 *            the command
	 * @param creator
	 *            the creator
	 * @param domain
	 *            the domain
	 */
	// スケジュールを再設定する
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	private void resetScheduleWithMultiThread(BasicScheduleResetCommand command,
			CommandHandlerContext<ScheduleCreatorExecutionCommand> context, List<GeneralDate> betweenDates,
			EmployeeGeneralInfoImported empGeneralInfo, List<BusinessTypeOfEmpDto> listBusTypeOfEmpHis,
			List<BasicSchedule> listBasicSchedule, RegistrationListDateSchedule registrationListDateSchedule) {
		
		/**************************************************/
		
//		ExecutorService executorService = Executors.newFixedThreadPool(20);
//		CountDownLatch countDownLatch = new CountDownLatch(betweenDates.size());
//
//		betweenDates.forEach(dateInPeriod -> {
//			AsyncTask task = AsyncTask.builder().withContexts().keepsTrack(false).threadName(this.getClass().getName())
//					.build(() -> {
//						// get info by context
//						val asyncTask = context.asAsync();
//
//						// 中断フラグを判断
//						if (asyncTask.hasBeenRequestedToCancel()) {
//							// ドメインモデル「スケジュール作成実行ログ」を更新する
//							// TODO - hinh nhu chua lam
//							asyncTask.finishedAsCancelled();
//							return;
//						}
//						// ドメインモデル「勤務予定基本情報」を取得する
//						Optional<BasicSchedule> optionalBasicSchedule = this.basicScheduleRepository
//								.find(command.getEmployeeId(), dateInPeriod);
//						if (optionalBasicSchedule.isPresent()) {
//							command.setWorkingCode(optionalBasicSchedule.get().getWorkTimeCode());
//							command.setWorkTypeCode(optionalBasicSchedule.get().getWorkTypeCode());
//							// 入力パラメータ「再作成区分」を判断
//							// 取得したドメインモデル「勤務予定基本情報」の「予定確定区分」を判断
//							if (command.getReCreateAtr() == ReCreateAtr.ALL_CASE.value
//									|| optionalBasicSchedule.get().getConfirmedAtr() == ConfirmedAtr.UNSETTLED) {
//								// 再設定する情報を取得する
//								this.scheCreExeBasicScheduleHandler.resetAllDataToCommandSave(command, dateInPeriod,
//										empGeneralInfo, listBusTypeOfEmpHis);
//							}
//						}
//
//						// Count down latch.
//						countDownLatch.countDown();
//					});
//			executorService.submit(task);
//		});
//
//		// Wait for latch until finish.
//		try {
//			countDownLatch.await();
//		} catch (InterruptedException ie) {
//			throw new RuntimeException(ie);
//		} finally {
//			// // Force shut down executor services.
//			executorService.shutdown();
//		}
		
		/**************************************************/

		// get info by context
		val asyncTask = context.asAsync();
		
		DateRegistedEmpSche dateRegistedEmpSche = new DateRegistedEmpSche(command.getEmployeeId(), new ArrayList<>());
		// loop start period date => end period date
		for(val toDate : betweenDates) {
			// 中断フラグを判断
			if (asyncTask.hasBeenRequestedToCancel()) {
				// ドメインモデル「スケジュール作成実行ログ」を更新する
				// TODO - hinh nhu chua lam
				asyncTask.finishedAsCancelled();
				break;
			}
			// ドメインモデル「勤務予定基本情報」を取得する
			// fix for response
//			Optional<BasicSchedule> optionalBasicSchedule = this.basicScheduleRepository.find(command.getEmployeeId(),
//					toDate);
			Optional<BasicSchedule> optionalBasicSchedule = listBasicSchedule.stream().filter(
					x -> (x.getEmployeeId().equals(command.getEmployeeId()) && x.getDate().compareTo(toDate) == 0))
					.findFirst();
			if (optionalBasicSchedule.isPresent()) {
				command.setWorkingCode(optionalBasicSchedule.get().getWorkTimeCode());
				command.setWorkTypeCode(optionalBasicSchedule.get().getWorkTypeCode());
				// 入力パラメータ「再作成区分」を判断
				// 取得したドメインモデル「勤務予定基本情報」の「予定確定区分」を判断
				if (command.getReCreateAtr() == ReCreateAtr.ALL_CASE.value
						|| optionalBasicSchedule.get().getConfirmedAtr() == ConfirmedAtr.UNSETTLED) {
					// 再設定する情報を取得する
					this.scheCreExeBasicScheduleHandler.resetAllDataToCommandSave(command, toDate, empGeneralInfo,
							listBusTypeOfEmpHis, listBasicSchedule, dateRegistedEmpSche);
				}
			}
		}
		
		if(dateRegistedEmpSche.getListDate().size() > 0){
			registrationListDateSchedule.getRegistrationListDateSchedule().add(dateRegistedEmpSche);
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

	private void createScheduleBasedPersonOneDate(ScheduleCreatorExecutionCommand command, ScheduleCreator creator,
			ScheduleExecutionLog domain, CommandHandlerContext<ScheduleCreatorExecutionCommand> context,
			GeneralDate dateInPeriod, EmployeeGeneralInfoImported empGeneralInfo,
			Map<String, List<EmploymentInfoImported>> mapEmploymentStatus, List<WorkCondItemDto> listWorkingConItem,
			List<WorkType> listWorkType, List<WorkTimeSetting> listWorkTimeSetting,
			List<BusinessTypeOfEmpDto> listBusTypeOfEmpHis, Map<String, WorkRestTimeZoneDto> mapFixedWorkSetting,
			Map<String, WorkRestTimeZoneDto> mapFlowWorkSetting,
			Map<String, WorkRestTimeZoneDto> mapDiffTimeWorkSetting, List<ShortWorkTimeDto> listShortWorkTimeDto,
			List<BasicSchedule> listBasicSchedule, DateRegistedEmpSche dateRegistedEmpSche) {

		// get info by context
		val asyncTask = context.asAsync();

		// check is client submit cancel ［中断］(Interrupt)
		if (asyncTask.hasBeenRequestedToCancel()) {
			asyncTask.finishedAsCancelled();
			// ドメインモデル「スケジュール作成実行ログ」を更新する(update domain 「スケジュール作成実行ログ」)
			this.updateStatusScheduleExecutionLog(domain, CompletionStatus.INTERRUPTION);
			return;
		}

		// 労働条件情報からパラメータ.社員ID、ループ中の対象日から該当する労働条件項目を取得する
		// EA修正履歴 No1830
		Optional<WorkCondItemDto> _workingConditionItem = listWorkingConItem.stream().filter(
				x -> x.getDatePeriod().contains(dateInPeriod) && creator.getEmployeeId().equals(x.getEmployeeId()))
				.findFirst();

		if (!_workingConditionItem.isPresent()) {
			String errorContent = this.internationalization.localize("Msg_602", "#KSC001_87").get();
			// ドメインモデル「スケジュール作成エラーログ」を登録する
			ScheduleErrorLog scheduleErrorLog = new ScheduleErrorLog(errorContent, command.getExecutionId(),
					dateInPeriod, creator.getEmployeeId());
			this.scheduleErrorLogRepository.add(scheduleErrorLog);
			return;
		}

		WorkCondItemDto workingConditionItem = _workingConditionItem.get();

		if (workingConditionItem.getScheduleManagementAtr() == ManageAtr.NOTUSE) {
			return;
		}

		if (!workingConditionItem.getScheduleMethod().isPresent()) {
			return;
		}

		WorkScheduleBasicCreMethod basicCreateMethod = workingConditionItem.getScheduleMethod().get()
				.getBasicCreateMethod();
		switch (basicCreateMethod) {
		case BUSINESS_DAY_CALENDAR:
			// アルゴリズム「営業日カレンダーで勤務予定を作成する」を実行する
			this.createWorkScheduleByBusinessDayCalenda(command, dateInPeriod, workingConditionItem, empGeneralInfo,
					mapEmploymentStatus, listWorkingConItem, listWorkType, listWorkTimeSetting, listBusTypeOfEmpHis,
					mapFixedWorkSetting, mapFlowWorkSetting, mapDiffTimeWorkSetting, listShortWorkTimeDto, listBasicSchedule, dateRegistedEmpSche);
			break;
		case MONTHLY_PATTERN:
			// アルゴリズム「月間パターンで勤務予定を作成する」を実行する
			// create schedule by monthly pattern
			this.scheCreExeMonthlyPatternHandler.createScheduleWithMonthlyPattern(command, dateInPeriod,
					workingConditionItem, empGeneralInfo, mapEmploymentStatus, listWorkingConItem, listWorkType,
					listWorkTimeSetting, listBusTypeOfEmpHis, mapFixedWorkSetting, mapFlowWorkSetting,
					mapDiffTimeWorkSetting, listShortWorkTimeDto, listBasicSchedule, dateRegistedEmpSche);
			break;
		case PERSONAL_DAY_OF_WEEK:
			// アルゴリズム「個人曜日別で勤務予定を作成する」を実行する
			// TODO
			// 対象外
			break;
		}
		System.out.println(creator.getEmployeeId() + " " + dateInPeriod.toString());
	}

	/**
	 * 個人情報をもとにスケジュールを作成する-Creates the schedule based person.
	 * 
	 * @param command
	 * @param creator
	 * @param domain
	 * @param context
	 * @param dateAfterCorrection
	 * @param empGeneralInfo
	 * @param mapEmploymentStatus
	 * @param listWorkingConItem
	 * @param listWorkType
	 * @param listWorkTimeSetting
	 * @param listBusTypeOfEmpHis
	 * @param allData
	 * @param mapFixedWorkSetting
	 * @param mapFlowWorkSetting
	 * @param mapDiffTimeWorkSetting
	 */
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	private void createScheduleBasedPersonWithMultiThread(ScheduleCreatorExecutionCommand command,
			ScheduleCreator creator, ScheduleExecutionLog domain,
			CommandHandlerContext<ScheduleCreatorExecutionCommand> context, List<GeneralDate> betweenDates,
			EmployeeGeneralInfoImported empGeneralInfo, Map<String, List<EmploymentInfoImported>> mapEmploymentStatus,
			List<WorkCondItemDto> listWorkingConItem, List<WorkType> listWorkType,
			List<WorkTimeSetting> listWorkTimeSetting, List<BusinessTypeOfEmpDto> listBusTypeOfEmpHis,
			Map<String, WorkRestTimeZoneDto> mapFixedWorkSetting, Map<String, WorkRestTimeZoneDto> mapFlowWorkSetting,
			Map<String, WorkRestTimeZoneDto> mapDiffTimeWorkSetting, List<ShortWorkTimeDto> listShortWorkTimeDto,
			List<BasicSchedule> listBasicSchedule, RegistrationListDateSchedule registrationListDateSchedule) {

//		ExecutorService executorService = Executors.newFixedThreadPool(20);
//		CountDownLatch countDownLatch = new CountDownLatch(betweenDates.size());
		DateRegistedEmpSche dateRegistedEmpSche = new DateRegistedEmpSche(command.getEmployeeId(), new ArrayList<>());
		
		betweenDates.forEach(dateInPeriod -> {
//			AsyncTask task = AsyncTask.builder().withContexts().keepsTrack(false).threadName(this.getClass().getName())
//					.build(() -> {
						createScheduleBasedPersonOneDate(command, creator, domain, context, dateInPeriod,
								empGeneralInfo, mapEmploymentStatus, listWorkingConItem, listWorkType,
								listWorkTimeSetting, listBusTypeOfEmpHis, mapFixedWorkSetting, mapFlowWorkSetting,
								mapDiffTimeWorkSetting, listShortWorkTimeDto, listBasicSchedule, dateRegistedEmpSche);

						// // Count down latch.
//						countDownLatch.countDown();
		});

		if(dateRegistedEmpSche.getListDate().size() > 0){
			registrationListDateSchedule.getRegistrationListDateSchedule().add(dateRegistedEmpSche);
		}
//			executorService.submit(task);
//		});
//
//		// Wait for latch until finish.
//		try {
//			countDownLatch.await();
//		} catch (InterruptedException ie) {
//			throw new RuntimeException(ie);
//		} finally {
//			// // Force shut down executor services.
//			executorService.shutdown();
//		}

	}

	/**
	 * 営業日カレンダーで勤務予定を作成する
	 * 
	 * Creates the work schedule by business day calendar.
	 * 
	 * @param command
	 * @param workingConditionItem
	 * @param empGeneralInfo
	 * @param mapEmploymentStatus
	 * @param listWorkingConItem
	 */
	private void createWorkScheduleByBusinessDayCalenda(ScheduleCreatorExecutionCommand command,
			GeneralDate dateInPeriod, WorkCondItemDto workingConditionItem, EmployeeGeneralInfoImported empGeneralInfo,
			Map<String, List<EmploymentInfoImported>> mapEmploymentStatus, List<WorkCondItemDto> listWorkingConItem,
			List<WorkType> listWorkType, List<WorkTimeSetting> listWorkTimeSetting,
			List<BusinessTypeOfEmpDto> listBusTypeOfEmpHis, Map<String, WorkRestTimeZoneDto> mapFixedWorkSetting,
			Map<String, WorkRestTimeZoneDto> mapFlowWorkSetting,
			Map<String, WorkRestTimeZoneDto> mapDiffTimeWorkSetting, List<ShortWorkTimeDto> listShortWorkTimeDto,
			List<BasicSchedule> listBasicSchedule, DateRegistedEmpSche dateRegistedEmpSche) {
		// 「社員の在職状態」から該当社員、該当日の在職状態を取得する
		// EA No1689
		List<EmploymentInfoImported> listEmploymentInfo = mapEmploymentStatus.get(workingConditionItem.getEmployeeId());
		Optional<EmploymentInfoImported> optEmploymentInfo = Optional.empty();
		if (listEmploymentInfo != null) {
			optEmploymentInfo = listEmploymentInfo.stream()
					.filter(employmentInfo -> employmentInfo.getStandardDate().equals(dateInPeriod)).findFirst();
		}

		// status employment equal RETIREMENT (退職)
		if (!optEmploymentInfo.isPresent() || optEmploymentInfo.get().getEmploymentState() == RETIREMENT) {
			return;
		}

		// status employment not equal BEFORE_JOINING (入社前)
		if (optEmploymentInfo.get().getEmploymentState() != BEFORE_JOINING) {
			// ドメインモデル「勤務予定基本情報」を取得する(lấy dữ liệu domain 「勤務予定基本情報」)
//			Optional<BasicSchedule> optionalBasicSchedule = this.basicScheduleRepository
//					.find(workingConditionItem.getEmployeeId(), dateInPeriod);
			// fix for response
			Optional<BasicSchedule> optionalBasicSchedule = listBasicSchedule.stream()
					.filter(x -> (x.getEmployeeId().equals(workingConditionItem.getEmployeeId())
							&& x.getDate().compareTo(dateInPeriod) == 0))
					.findFirst();

			if (optionalBasicSchedule.isPresent()) {
				BasicSchedule basicSchedule = optionalBasicSchedule.get();
				// checked2018
				// 登録前削除区分をTrue（削除する）とする
				command.setIsDeleteBeforInsert(true); // FIX BUG #87113
				// check parameter implementAtr recreate (入力パラメータ「実施区分」を判断)
				// 入力パラメータ「実施区分」を判断(kiểm tra parameter 「実施区分」)
				if (command.getContent().getImplementAtr().value == ImplementAtr.RECREATE.value) {
					this.createWorkScheduleByRecreate(command, dateInPeriod, basicSchedule, workingConditionItem,
							optEmploymentInfo, empGeneralInfo, mapEmploymentStatus, listWorkingConItem, listWorkType,
							listWorkTimeSetting, listBusTypeOfEmpHis, mapFixedWorkSetting, mapFlowWorkSetting,
							mapDiffTimeWorkSetting, listShortWorkTimeDto, listBasicSchedule, dateRegistedEmpSche);
				}
			} else {
				// EA No1841
				ScheMasterInfo scheMasterInfo = new ScheMasterInfo(null);
				BasicSchedule basicSche = new BasicSchedule(null, scheMasterInfo);
				if (ImplementAtr.RECREATE == command.getContent().getImplementAtr()
						&& !this.scheCreExeMonthlyPatternHandler.scheduleCreationDeterminationProcess(command,
								dateInPeriod, basicSche, optEmploymentInfo, workingConditionItem, empGeneralInfo,
								listBusTypeOfEmpHis, listShortWorkTimeDto)) {
					return;
				}

				// 登録前削除区分をTrue（削除する）とする
				// checked2018
				command.setIsDeleteBeforInsert(false); // FIX BUG #87113

				// not exist data basic schedule
				this.scheCreExeWorkTypeHandler.createWorkSchedule(command, dateInPeriod, workingConditionItem,
						empGeneralInfo, mapEmploymentStatus, listWorkingConItem, listWorkType, listWorkTimeSetting,
						listBusTypeOfEmpHis, mapFixedWorkSetting, mapFlowWorkSetting, mapDiffTimeWorkSetting,
						listShortWorkTimeDto, listBasicSchedule, dateRegistedEmpSche);
			}
		}
	}

	/**
	 * Creates the work schedule by recreate.
	 * 
	 * @param command
	 * @param basicSchedule
	 * @param workingConditionItem
	 * @param optEmploymentInfo
	 * @param empGeneralInfo
	 * @param mapEmploymentStatus
	 * @param listWorkingConItem
	 */
	private void createWorkScheduleByRecreate(ScheduleCreatorExecutionCommand command, GeneralDate dateInPeriod,
			BasicSchedule basicSchedule, WorkCondItemDto workingConditionItem,
			Optional<EmploymentInfoImported> optEmploymentInfo, EmployeeGeneralInfoImported empGeneralInfo,
			Map<String, List<EmploymentInfoImported>> mapEmploymentStatus, List<WorkCondItemDto> listWorkingConItem,
			List<WorkType> listWorkType, List<WorkTimeSetting> listWorkTimeSetting,
			List<BusinessTypeOfEmpDto> listBusTypeOfEmpHis, Map<String, WorkRestTimeZoneDto> mapFixedWorkSetting,
			Map<String, WorkRestTimeZoneDto> mapFlowWorkSetting,
			Map<String, WorkRestTimeZoneDto> mapDiffTimeWorkSetting, List<ShortWorkTimeDto> listShortWorkTimeDto,
			List<BasicSchedule> listBasicSchedule, DateRegistedEmpSche dateRegistedEmpSche) {
		// 入力パラメータ「再作成区分」を判断 - check parameter ReCreateAtr onlyUnconfirm
		// 取得したドメインモデル「勤務予定基本情報」の「予定確定区分」を判断
		// (kiểm tra thông tin 「予定確定区分」 của domain 「勤務予定基本情報」)
		if (command.getContent().getReCreateContent().getReCreateAtr() == ReCreateAtr.ALL_CASE
				|| basicSchedule.getConfirmedAtr().equals(ConfirmedAtr.UNSETTLED)) {
			// アルゴリズム「スケジュール作成判定処理」を実行する
			if (this.scheCreExeMonthlyPatternHandler.scheduleCreationDeterminationProcess(command, dateInPeriod,
					basicSchedule, optEmploymentInfo, workingConditionItem, empGeneralInfo, listBusTypeOfEmpHis,
					listShortWorkTimeDto)) {
				this.scheCreExeWorkTypeHandler.createWorkSchedule(command, dateInPeriod, workingConditionItem,
						empGeneralInfo, mapEmploymentStatus, listWorkingConItem, listWorkType, listWorkTimeSetting,
						listBusTypeOfEmpHis, mapFixedWorkSetting, mapFlowWorkSetting, mapDiffTimeWorkSetting,
						listShortWorkTimeDto, listBasicSchedule, dateRegistedEmpSche);
			}
		}
	}

	/**
	 * 個人スケジュールを登録する: register Personal Schedule
	 * 
	 * @param command
	 * @param scheduleExecutionLog
	 * @param context
	 */
	private void registerPersonalSchedule(ScheduleCreatorExecutionCommand command,
			ScheduleExecutionLog scheduleExecutionLog, CommandHandlerContext<ScheduleCreatorExecutionCommand> context,
			String companyId) {

		String exeId = command.getExecutionId();
		DatePeriod period = scheduleExecutionLog.getPeriod();

		// パラメータ実施区分を判定
		if (scheduleExecutionLog.getExeAtr() == ExecutionAtr.AUTOMATIC) {
			createExcutionLog(command, scheduleExecutionLog);
		}

		DatePeriod dateBeforeCorrection = new DatePeriod(period.start(), period.end());

		// get all data creator
		List<ScheduleCreator> scheduleCreators = this.scheduleCreatorRepository.findAll(exeId);
		List<String> employeeIds = scheduleCreators.stream().map(item -> item.getEmployeeId())
				.collect(Collectors.toList());
		// EA No1675
		// Imported(就業)「社員の履歴情報」を取得する
		EmployeeGeneralInfoImported empGeneralInfo = this.scEmpGeneralInfoAdapter.getPerEmpInfo(employeeIds, period);

		// Imported(就業)「社員の在職状態」を取得する
		Map<String, List<EmploymentInfoImported>> mapEmploymentStatus = this.employmentStatusAdapter
				.findListOfEmployee(employeeIds, dateBeforeCorrection).stream().collect(Collectors
						.toMap(EmploymentStatusImported::getEmployeeId, EmploymentStatusImported::getEmploymentInfo));

		// 労働条件情報を取得する
		// EA No1828
		List<WorkCondItemDto> listWorkingConItem = this.acquireWorkingConditionInformation(employeeIds,
				dateBeforeCorrection);
		List<WorkType> listWorkType = new ArrayList<>();
		List<WorkTimeSetting> listWorkTimeSetting = new ArrayList<>();
		Map<String, WorkRestTimeZoneDto> mapFixedWorkSetting = new HashMap<>();
		Map<String, WorkRestTimeZoneDto> mapFlowWorkSetting = new HashMap<>();
		Map<String, WorkRestTimeZoneDto> mapDiffTimeWorkSetting = new HashMap<>();

		// 社員の短時間勤務履歴を取得する
		// EA No2134
		List<ShortWorkTimeDto> listShortWorkTimeDto = this.acquireEmployeeShortTimeWorkHistory(employeeIds, period);

		// EA No2017
		// マスタ情報を取得する
		this.acquireData(companyId, listWorkType, listWorkTimeSetting, mapFixedWorkSetting, mapFlowWorkSetting,
				mapDiffTimeWorkSetting);

		// 勤務種別情報を取得する
		// ドメインモデル「社員の勤務種別の履歴」を取得する
		// ドメインモデル「社員の勤務種別」を取得する
		List<BusinessTypeOfEmpDto> listBusTypeOfEmpHis = this.businessTypeOfEmpHisAdaptor
				.findByCidSidBaseDate(companyId, employeeIds, dateBeforeCorrection);

		// find all data BasicSchedule
		// List<BasicSchedule> listBasicSchedule =
		// this.basicScheduleRepository.findAllBetweenDate(employeeIds,
		// scheduleExecutionLog.getPeriod().start(),
		// scheduleExecutionLog.getPeriod().end());
		
		List<BasicSchedule> listBasicSchedule = this.basicScheduleRepository.findSomePropertyWithJDBC(employeeIds, scheduleExecutionLog.getPeriod());
		RegistrationListDateSchedule registrationListDateSchedule = new RegistrationListDateSchedule(new ArrayList<>());

		// get info by context
		val asyncTask = context.asAsync();
		
		// at.recordの計算処理で使用する共通の会社設定は、ここで取得しキャッシュしておく
		Object companySetting = scTimeAdapter.getCompanySettingForCalculation();

		this.parallel.forEach(scheduleCreators, scheduleCreator -> {
			
			// check is client submit cancel
			if (asyncTask.hasBeenRequestedToCancel()) {
				asyncTask.finishedAsCancelled();
				// ドメインモデル「スケジュール作成実行ログ」を更新する(update domain 「スケジュール作成実行ログ」)
				this.updateStatusScheduleExecutionLog(scheduleExecutionLog, CompletionStatus.INTERRUPTION);
				return;
			}
		
			// アルゴリズム「対象期間を締め開始日以降に補正する」を実行する
			StateAndValueDatePeriod stateAndValueDatePeriod = this.correctTargetPeriodAfterClosingStartDate(
					command.getCompanyId(), scheduleCreator.getEmployeeId(), dateBeforeCorrection,
					empGeneralInfo);
			if (stateAndValueDatePeriod.state) {
				DatePeriod dateAfterCorrection = stateAndValueDatePeriod.getValue();
				ScheduleCreateContent content = command.getContent();
				List<GeneralDate> betweenDates = dateAfterCorrection.datesBetween();
				// 実施区分を判断, 処理実行区分を判断
				// EA No2115
				if (content.getImplementAtr() == ImplementAtr.RECREATE && content.getReCreateContent()
						.getProcessExecutionAtr() == ProcessExecutionAtr.RECONFIG) {
					BasicScheduleResetCommand commandReset = new BasicScheduleResetCommand();
					commandReset.setCompanyId(command.getCompanyId());
					commandReset.setConfirm(content.getConfirm());
					commandReset.setEmployeeId(scheduleCreator.getEmployeeId());
					commandReset.setExecutionId(exeId);
					commandReset.setReCreateAtr(content.getReCreateContent().getReCreateAtr().value);
					commandReset.setResetAtr(content.getReCreateContent().getResetAtr());
					commandReset.setTargetStartDate(period.start());
					commandReset.setTargetEndDate(period.end());
					commandReset.setCompanySetting(companySetting);
					// スケジュールを再設定する (Thiết lập lại schedule)
					this.resetScheduleWithMultiThread(commandReset, context, betweenDates,
							empGeneralInfo, listBusTypeOfEmpHis, listBasicSchedule, registrationListDateSchedule);
				} else {
					// 入力パラメータ「作成方法区分」を判断-check parameter
					// CreateMethodAtr
					if (content.getCreateMethodAtr() == CreateMethodAtr.PERSONAL_INFO) {
						command.setCompanySetting(companySetting);
						this.createScheduleBasedPersonWithMultiThread(command, scheduleCreator,
								scheduleExecutionLog, context, betweenDates, empGeneralInfo,
								mapEmploymentStatus, listWorkingConItem, listWorkType, listWorkTimeSetting,
								listBusTypeOfEmpHis, mapFixedWorkSetting, mapFlowWorkSetting,
								mapDiffTimeWorkSetting, listShortWorkTimeDto, listBasicSchedule, registrationListDateSchedule);
					}
				}

				scheduleCreator.updateToCreated();
				this.scheduleCreatorRepository.update(scheduleCreator);
			} else {
				scheduleCreator.updateToCreated();
				this.scheduleCreatorRepository.update(scheduleCreator);
				// EA修正履歴　No2378
				// ドメインモデル「スケジュール作成実行ログ」を取得する find execution log by id
				ScheduleExecutionLog scheExeLog = this.scheduleExecutionLogRepository
						.findById(command.getCompanyId(), scheduleExecutionLog.getExecutionId()).get();
				if (scheExeLog.getCompletionStatus() != CompletionStatus.INTERRUPTION) {
					this.updateStatusScheduleExecutionLog(scheduleExecutionLog);
				}
			}
		});
		
//		for (val scheduleCreator : scheduleCreators) {
//
//			// アルゴリズム「対象期間を締め開始日以降に補正する」を実行する
//			StateAndValueDatePeriod stateAndValueDatePeriod = this.correctTargetPeriodAfterClosingStartDate(
//					command.getCompanyId(), scheduleCreator.getEmployeeId(), dateBeforeCorrection, empGeneralInfo);
//			if (stateAndValueDatePeriod.state) {
//				DatePeriod dateAfterCorrection = stateAndValueDatePeriod.getValue();
//				ScheduleCreateContent content = command.getContent();
//				List<GeneralDate> betweenDates = dateAfterCorrection.datesBetween();
//				// 実施区分を判断, 処理実行区分を判断
//				// EA No2115
//				if (content.getImplementAtr() == ImplementAtr.RECREATE
//						&& content.getReCreateContent().getProcessExecutionAtr() == ProcessExecutionAtr.RECONFIG) {
//					BasicScheduleResetCommand commandReset = new BasicScheduleResetCommand();
//					commandReset.setCompanyId(command.getCompanyId());
//					commandReset.setConfirm(content.getConfirm());
//					commandReset.setEmployeeId(scheduleCreator.getEmployeeId());
//					commandReset.setExecutionId(exeId);
//					commandReset.setReCreateAtr(content.getReCreateContent().getReCreateAtr().value);
//					commandReset.setResetAtr(content.getReCreateContent().getResetAtr());
//					commandReset.setTargetStartDate(period.start());
//					commandReset.setTargetEndDate(period.end());
//					// スケジュールを再設定する (Thiết lập lại schedule)
//					this.resetScheduleWithMultiThread(commandReset, context, betweenDates, empGeneralInfo,
//							listBusTypeOfEmpHis);
//				} else {
//					// 入力パラメータ「作成方法区分」を判断-check parameter
//					// CreateMethodAtr
//					if (content.getCreateMethodAtr() == CreateMethodAtr.PERSONAL_INFO) {
//						this.createScheduleBasedPersonWithMultiThread(command, scheduleCreator, scheduleExecutionLog,
//								context, betweenDates, empGeneralInfo, mapEmploymentStatus, listWorkingConItem,
//								listWorkType, listWorkTimeSetting, listBusTypeOfEmpHis, mapFixedWorkSetting,
//								mapFlowWorkSetting, mapDiffTimeWorkSetting, listShortWorkTimeDto);
//					}
//				}
//
//				scheduleCreator.updateToCreated();
//				this.scheduleCreatorRepository.update(scheduleCreator);
//			}
//			
//			if (asyncTask.hasBeenRequestedToCancel()) {
//				asyncTask.finishedAsCancelled();
//				// ドメインモデル「スケジュール作成実行ログ」を更新する(update domain 「スケジュール作成実行ログ」)
//				this.updateStatusScheduleExecutionLog(scheduleExecutionLog, CompletionStatus.INTERRUPTION);
//				break;
//			}
//			
//		}
		
		// 暫定データを作成する (Tạo data tạm)
		registrationListDateSchedule.getRegistrationListDateSchedule().stream().forEach(x -> {
			// アルゴリズム「暫定データの登録」を実行する(Thực hiện thuật toán [đăng ký data tạm]) 
			this.interimRemainDataMngRegisterDateChange.registerDateChange(companyId, x.getEmployeeId(), x.getListDate());
		});

		// ドメインモデル「スケジュール作成実行ログ」を取得する find execution log by id
		ScheduleExecutionLog scheExeLog = this.scheduleExecutionLogRepository
				.findById(command.getCompanyId(), scheduleExecutionLog.getExecutionId()).get();
		if (scheExeLog.getCompletionStatus() != CompletionStatus.INTERRUPTION) {
			System.out.println("not hasBeenRequestedToCancel: " + asyncTask.hasBeenRequestedToCancel() + "&exeid="
					+ scheduleExecutionLog.getExecutionId());
			this.updateStatusScheduleExecutionLog(scheduleExecutionLog);
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

	// private void insertUpdateAllBasicSchedule(List<BasicSchedule>
	// listBasicSchedule, List<BasicSchedule> allData) {
	// List<BasicSchedule> listUpdates = new ArrayList<>();
	// List<BasicSchedule> listInsert = new ArrayList<>();
	// allData.forEach(x -> {
	// Optional<BasicSchedule> opt = listBasicSchedule.stream()
	// .filter(y -> (y.getEmployeeId().equals(x.getEmployeeId()) &&
	// y.getDate().equals(x.getDate())))
	// .findFirst();
	// if (opt.isPresent()) {
	// listUpdates.add(x);
	// } else {
	// listInsert.add(x);
	// }
	// });
	//
	// if (listInsert.size() > 0) {
	// this.basicScheduleRepository.insertAll(listInsert);
	// }
	// if (listUpdates.size() > 0) {
	// this.basicScheduleRepository.updateAll(listUpdates);
	// }
	//
	// }

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
	 * マスタ情報を取得する
	 * 
	 * @param companyId
	 * @param listWorkType
	 * @param listWorkTimeSetting
	 * @param mapFixedWorkSetting
	 * @param listFlowWorkSetting
	 * @param listDiffTimeWorkSetting
	 */
	private void acquireData(String companyId, List<WorkType> listWorkType, List<WorkTimeSetting> listWorkTimeSetting,
			Map<String, WorkRestTimeZoneDto> mapFixedWorkSetting, Map<String, WorkRestTimeZoneDto> mapFlowWorkSetting,
			Map<String, WorkRestTimeZoneDto> mapDiffTimeWorkSetting) {
		// ドメインモデル「勤務種類」を取得する
		listWorkType.addAll(this.workTypeRepository.findNotDeprecateByCompanyId(companyId));
		// ドメインモデル「就業時間帯の設定」を取得する
		listWorkTimeSetting.addAll(this.workTimeSettingRepository.findActiveItems(companyId));
		// EA修正履歴 No2103
		List<String> listWorkTimeCodeFix = new ArrayList<>();
		List<String> listWorkTimeCodeFlow = new ArrayList<>();
		List<String> listWorkTimeCodeDiff = new ArrayList<>();
		listWorkTimeSetting.forEach(workTime -> {
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
			this.setDataForMap(mapFixedWorkSetting, mapFixOffdayWorkRestTimezones, mapFixHalfDayWorkRestTimezones);
		}
		// ドメインモデル「流動勤務設定」を取得する
		if (!listWorkTimeCodeFlow.isEmpty()) {
			Map<WorkTimeCode, List<AmPmWorkTimezone>> mapFlowOffdayWorkRestTimezones = this.flowWorkSettingRepository
					.getFlowOffdayWorkRestTimezones(companyId, listWorkTimeCodeFlow);
			Map<WorkTimeCode, List<AmPmWorkTimezone>> mapFlowHalfDayWorkRestTimezones = this.flowWorkSettingRepository
					.getFlowHalfDayWorkRestTimezones(companyId, listWorkTimeCodeFlow);
			this.setDataForMap(mapFlowWorkSetting, mapFlowOffdayWorkRestTimezones, mapFlowHalfDayWorkRestTimezones);
		}
		// ドメインモデル「時差勤務設定」を取得する
		if (!listWorkTimeCodeDiff.isEmpty()) {
			Map<WorkTimeCode, List<AmPmWorkTimezone>> mapDiffOffdayWorkRestTimezones = this.diffTimeWorkSettingRepository
					.getDiffOffdayWorkRestTimezones(companyId, listWorkTimeCodeDiff);
			Map<WorkTimeCode, List<AmPmWorkTimezone>> mapDiffHalfDayWorkRestTimezones = this.diffTimeWorkSettingRepository
					.getDiffHalfDayWorkRestTimezones(companyId, listWorkTimeCodeDiff);

			this.setDataForMap(mapDiffTimeWorkSetting, mapDiffOffdayWorkRestTimezones, mapDiffHalfDayWorkRestTimezones);
		}

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
	 * 
	 * @param employeeIds
	 * @param period
	 */
	private List<ShortWorkTimeDto> acquireEmployeeShortTimeWorkHistory(List<String> employeeIds, DatePeriod period) {
		return this.scShortWorkTimeAdapter.findShortWorkTimes(employeeIds, period);
	}
}