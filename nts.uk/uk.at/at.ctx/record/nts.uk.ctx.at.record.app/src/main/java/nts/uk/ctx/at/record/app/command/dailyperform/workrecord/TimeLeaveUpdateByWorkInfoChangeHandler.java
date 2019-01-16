package nts.uk.ctx.at.record.app.command.dailyperform.workrecord;

import java.util.ArrayList;
import java.util.Optional;
import java.util.function.Supplier;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.arc.layer.app.command.CommandHandlerContext;
import nts.arc.layer.app.command.CommandHandlerWithResult;
import nts.uk.ctx.at.record.dom.dailyprocess.calc.IntegrationOfDaily;
import nts.uk.ctx.at.record.dom.service.event.common.EventHandleResult;
import nts.uk.ctx.at.record.dom.service.event.common.EventHandleResult.EventHandleAction;
import nts.uk.ctx.at.record.dom.service.event.timeleave.TimeLeavingOfDailyService;
import nts.uk.ctx.at.record.dom.workinformation.WorkInfoOfDailyPerformance;
import nts.uk.ctx.at.record.dom.workinformation.repository.WorkInformationRepository;
import nts.uk.ctx.at.record.dom.worktime.TimeLeavingOfDailyPerformance;
import nts.uk.shr.com.context.AppContexts;

/** Event：出退勤時刻を補正する */
@Stateless
public class TimeLeaveUpdateByWorkInfoChangeHandler extends CommandHandlerWithResult<TimeLeaveUpdateByWorkInfoChangeCommand, EventHandleResult<TimeLeavingOfDailyPerformance>> {

	@Inject
	private WorkInformationRepository workInfoRepo;

	@Inject
	private TimeLeavingOfDailyService eventService;
	
	@Override
	protected EventHandleResult<TimeLeavingOfDailyPerformance> handle(CommandHandlerContext<TimeLeaveUpdateByWorkInfoChangeCommand> context) {
		TimeLeaveUpdateByWorkInfoChangeCommand command = context.getCommand();

		WorkInfoOfDailyPerformance wi = getWithDefaul(command.cachedWorkInfo, () -> getDefaultWorkInfo(command));
		if(wi == null) {
			return EventHandleResult.onFail();
		}
		
		String companyId = getWithDefaul(command.companyId, () -> AppContexts.user().companyId());
		IntegrationOfDaily working = new IntegrationOfDaily(wi, null,
				null, Optional.empty(),
				Optional.empty(),
				new ArrayList<>(), Optional.empty(),
				new ArrayList<>(),
				Optional.empty(),
				Optional.empty(),
				command.cachedTimeLeave, Optional.empty(),
				Optional.empty(),
				Optional.empty(), Optional.empty(),
				command.cachedEditState.isPresent() ? command.cachedEditState.get() : new ArrayList<>(), Optional.empty(),
				new ArrayList<>());
		EventHandleResult<IntegrationOfDaily> result = eventService.correct(companyId, working, command.cachedWorkCondition, command.cachedWorkType, !command.actionOnCache);
		
		if(command.isTriggerRelatedEvent && result.getAction() != EventHandleAction.ABORT) {
			/** <<Event>> 実績の出退勤が変更されたイベントを発行する　*/
			result.getData().getAttendanceLeave().ifPresent(tl -> {
				tl.timeLeavesChanged();
			});
		}
		
		return EventHandleResult.withResult(result.getAction(), result.getData().getAttendanceLeave().orElse(null));
	}

	private WorkInfoOfDailyPerformance getDefaultWorkInfo(TimeLeaveUpdateByWorkInfoChangeCommand command) {
		return this.workInfoRepo.find(command.employeeId, command.targetDate).orElse(null);
	}

	private <T> T getWithDefaul(Optional<T> target, Supplier<T> defaultVal){
		if(target.isPresent()){
			return target.get();
		}
		return defaultVal.get();
	}
}
