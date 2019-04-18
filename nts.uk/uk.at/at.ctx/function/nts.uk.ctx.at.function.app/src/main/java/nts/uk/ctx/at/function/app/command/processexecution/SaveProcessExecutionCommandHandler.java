package nts.uk.ctx.at.function.app.command.processexecution;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.arc.enums.EnumAdaptor;
import nts.arc.error.BusinessException;
import nts.arc.layer.app.command.CommandHandlerContext;
import nts.arc.layer.app.command.CommandHandlerWithResult;
import nts.uk.ctx.at.function.dom.alarm.AlarmPatternCode;
import nts.uk.ctx.at.function.dom.processexecution.AlarmExtraction;
import nts.uk.ctx.at.function.dom.processexecution.AppRouteUpdateDaily;
import nts.uk.ctx.at.function.dom.processexecution.ExecutionCode;
import nts.uk.ctx.at.function.dom.processexecution.ExecutionName;
import nts.uk.ctx.at.function.dom.processexecution.ExecutionScopeClassification;
import nts.uk.ctx.at.function.dom.processexecution.LastExecDateTime;
import nts.uk.ctx.at.function.dom.processexecution.ProcessExecType;
import nts.uk.ctx.at.function.dom.processexecution.ProcessExecution;
import nts.uk.ctx.at.function.dom.processexecution.ProcessExecutionScope;
import nts.uk.ctx.at.function.dom.processexecution.ProcessExecutionScopeItem;
import nts.uk.ctx.at.function.dom.processexecution.ProcessExecutionSetting;
import nts.uk.ctx.at.function.dom.processexecution.dailyperformance.DailyPerformanceCreation;
import nts.uk.ctx.at.function.dom.processexecution.dailyperformance.DailyPerformanceItem;
import nts.uk.ctx.at.function.dom.processexecution.dailyperformance.TargetGroupClassification;
import nts.uk.ctx.at.function.dom.processexecution.executionlog.CurrentExecutionStatus;
import nts.uk.ctx.at.function.dom.processexecution.executionlog.EndStatus;
import nts.uk.ctx.at.function.dom.processexecution.executionlog.ProcessExecutionLogManage;
import nts.uk.ctx.at.function.dom.processexecution.personalschedule.CreateScheduleYear;
import nts.uk.ctx.at.function.dom.processexecution.personalschedule.CreationPeriod;
import nts.uk.ctx.at.function.dom.processexecution.personalschedule.PersonalScheduleCreation;
import nts.uk.ctx.at.function.dom.processexecution.personalschedule.PersonalScheduleCreationPeriod;
import nts.uk.ctx.at.function.dom.processexecution.personalschedule.PersonalScheduleCreationTarget;
import nts.uk.ctx.at.function.dom.processexecution.personalschedule.TargetClassification;
import nts.uk.ctx.at.function.dom.processexecution.personalschedule.TargetDate;
import nts.uk.ctx.at.function.dom.processexecution.personalschedule.TargetMonth;
import nts.uk.ctx.at.function.dom.processexecution.personalschedule.TargetSetting;
import nts.uk.ctx.at.function.dom.processexecution.repository.ExecutionScopeItemRepository;
import nts.uk.ctx.at.function.dom.processexecution.repository.LastExecDateTimeRepository;
import nts.uk.ctx.at.function.dom.processexecution.repository.ProcessExecutionLogManageRepository;
import nts.uk.ctx.at.function.dom.processexecution.repository.ProcessExecutionRepository;
import nts.uk.ctx.at.shared.dom.ot.frame.NotUseAtr;
import nts.uk.shr.com.context.AppContexts;
import nts.uk.shr.com.time.calendar.MonthDay;

@Stateless
public class SaveProcessExecutionCommandHandler extends CommandHandlerWithResult<SaveProcessExecutionCommand, String> {

	@Inject
	private ProcessExecutionRepository procExecRepo;

	@Inject
	private LastExecDateTimeRepository lastDateTimeRepo;
	
	@Inject
	private ExecutionScopeItemRepository scopeItemRepo;
	@Inject
	private ProcessExecutionLogManageRepository processExecLogManRepo;

	//登録ボタン押下時処理
	@Override
	protected String handle(CommandHandlerContext<SaveProcessExecutionCommand> context) {
		String companyId = AppContexts.user().companyId();

		SaveProcessExecutionCommand command = context.getCommand();
		
		List<ProcessExecutionScopeItem> workplaceIdList = command.getWorkplaceList().stream()
				.map(workplaceId -> ProcessExecutionScopeItem.createSimpleFromJavaType(
																		companyId,
																		command.getExecItemCd(),
																		workplaceId)
				).collect(Collectors.toList());
		
		ProcessExecutionScope execScope =
				new ProcessExecutionScope(EnumAdaptor.valueOf(command.getExecScopeCls(), ExecutionScopeClassification.class),
						command.getRefDate(),
						workplaceIdList);
		
		AlarmExtraction alarmExtraction = new AlarmExtraction(command.isAlarmAtr(), new AlarmPatternCode(command.getAlarmCode()),
				command.getMailPrincipal(),
				command.getMailAdministrator()
				);
		
		PersonalScheduleCreationPeriod period = new PersonalScheduleCreationPeriod(
										command.getCreationPeriod() == null ? null : new CreationPeriod(command.getCreationPeriod()),
										command.getTargetDate() == null ? null : new TargetDate(command.getTargetDate()),
										EnumAdaptor.valueOf(command.getTargetMonth(), TargetMonth.class),
										command.getDesignatedYear() == null ? null : EnumAdaptor.valueOf(command.getDesignatedYear(), CreateScheduleYear.class),
										command.getStartMonthDay() == null ? null : new MonthDay(command.getStartMonthDay()/100, command.getStartMonthDay()%100),
										command.getEndMonthDay() == null ? null : new MonthDay(command.getEndMonthDay()/100, command.getEndMonthDay()%100)
				);
		
		PersonalScheduleCreationTarget target = new PersonalScheduleCreationTarget(
				EnumAdaptor.valueOf(command.getCreationTarget(), TargetClassification.class),
				new TargetSetting(command.isRecreateWorkType(), command.isManualCorrection(), command.isCreateEmployee(), command.isRecreateTransfer()));
		PersonalScheduleCreation perSchCreation = new PersonalScheduleCreation(period, command.isPerScheduleCls(), target);
		DailyPerformanceCreation dailyPerfCreation =
								new DailyPerformanceCreation(command.isDailyPerfCls(),
										EnumAdaptor.valueOf(command.getDailyPerfItem(), DailyPerformanceItem.class)
										,new TargetGroupClassification( command.isRecreateWorkType(), command.isMidJoinEmployee(),command.isRecreateTransfer()));

		ProcessExecutionSetting execSetting = 
				new ProcessExecutionSetting(alarmExtraction, perSchCreation, dailyPerfCreation, command.isReflectResultCls(), command.isMonthlyAggCls(),
						new AppRouteUpdateDaily(
								EnumAdaptor.valueOf(command.isAppRouteUpdateAtr()?1:0, NotUseAtr.class),
								command.getCreateNewEmp()==null?null:EnumAdaptor.valueOf((command.getCreateNewEmp().booleanValue()?1:0), NotUseAtr.class)
								),
						EnumAdaptor.valueOf(command.isAppRouteUpdateMonthly()?1:0, NotUseAtr.class)
						);
		
		ProcessExecution procExec =
				new ProcessExecution(companyId,
						new ExecutionCode(command.getExecItemCd()),
						new ExecutionName(command.getExecItemName()), execScope, execSetting,
						EnumAdaptor.valueOf(command.getProcessExecType(), ProcessExecType.class));
		
		
		procExec.validateVer2();
		if (command.isNewMode()) {
			//新規登録処理
			Optional<ProcessExecution> procExecOpt = this.procExecRepo.getProcessExecutionByCidAndExecCd(companyId, command.getExecItemCd());
			if (procExecOpt.isPresent()) {
				throw new BusinessException("Msg_3");
			}
			// ドメインモデル「更新処理自動実行」に新規登録する
			procExecRepo.insert(procExec);
			
			//ドメインモデル「更新処理自動実行管理」に新規登録する
			ProcessExecutionLogManage processExecutionLogManage = new ProcessExecutionLogManage(new ExecutionCode(command.getExecItemCd()),companyId,EndStatus.NOT_IMPLEMENT,CurrentExecutionStatus.WAITING);
			this.processExecLogManRepo.insert(processExecutionLogManage);
			this.lastDateTimeRepo.insert(new LastExecDateTime(companyId,
																new ExecutionCode(command.getExecItemCd()),
																null));
		} else {
			this.scopeItemRepo.removeAllByCidAndExecCd(procExec.getCompanyId(), procExec.getExecItemCd().v());
			procExecRepo.update(procExec);
			this.scopeItemRepo.insert(procExec.getCompanyId(),
										procExec.getExecItemCd().v(),
										procExec.getExecScope().getWorkplaceIdList());
		}
		return procExec.getExecItemCd().v();
//		return null;
	}
}
