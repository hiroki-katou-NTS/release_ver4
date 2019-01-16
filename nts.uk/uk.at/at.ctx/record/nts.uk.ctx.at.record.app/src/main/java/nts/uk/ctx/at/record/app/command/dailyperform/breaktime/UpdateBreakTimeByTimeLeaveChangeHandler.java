package nts.uk.ctx.at.record.app.command.dailyperform.breaktime;

import java.util.ArrayList;
import java.util.Optional;
import java.util.function.Supplier;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.arc.layer.app.command.CommandHandlerContext;
import nts.arc.layer.app.command.CommandHandlerWithResult;
import nts.uk.ctx.at.record.dom.breakorgoout.BreakTimeOfDailyPerformance;
import nts.uk.ctx.at.record.dom.breakorgoout.enums.BreakType;
import nts.uk.ctx.at.record.dom.dailyprocess.calc.IntegrationOfDaily;
import nts.uk.ctx.at.record.dom.service.event.breaktime.BreakTimeOfDailyService;
import nts.uk.ctx.at.record.dom.service.event.common.EventHandleResult;
import nts.uk.ctx.at.record.dom.service.event.common.EventHandleResult.EventHandleAction;
import nts.uk.ctx.at.record.dom.workinformation.WorkInfoOfDailyPerformance;
import nts.uk.ctx.at.record.dom.workinformation.repository.WorkInformationRepository;
import nts.uk.shr.com.context.AppContexts;

/** Event：休憩時間帯を補正する */
@Stateless
public class UpdateBreakTimeByTimeLeaveChangeHandler extends
		CommandHandlerWithResult<UpdateBreakTimeByTimeLeaveChangeCommand, EventHandleResult<BreakTimeOfDailyPerformance>> {

	@Inject
	private WorkInformationRepository workInfoRepo;

	@Inject
	private BreakTimeOfDailyService eventService;

	@Override
	/** 休憩時間帯を補正する */
	protected EventHandleResult<BreakTimeOfDailyPerformance> handle(
			CommandHandlerContext<UpdateBreakTimeByTimeLeaveChangeCommand> context) {
		UpdateBreakTimeByTimeLeaveChangeCommand command = context.getCommand();
		WorkInfoOfDailyPerformance wi = getWithDefaul(command.cachedWorkInfo, () -> getDefaultWorkInfo(command));
		if (wi == null) {
			return EventHandleResult.withResult(EventHandleAction.ABORT, null);
		}

		String companyId = getWithDefaul(command.companyId, () -> AppContexts.user().companyId());

		IntegrationOfDaily working = new IntegrationOfDaily(wi, null, null, Optional.empty(), Optional.empty(),
				new ArrayList<>(), Optional.empty(), new ArrayList<>(), Optional.empty(), Optional.empty(),
				command.cachedTimeLeave, Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(),
				command.cachedEditState.isPresent() ? command.cachedEditState.get() : new ArrayList<>(),
				Optional.empty(), new ArrayList<>());

		command.cachedBreackTime.ifPresent(b -> {
			working.getBreakTime().add(b);
		});

		EventHandleResult<IntegrationOfDaily> result = eventService.correct(companyId, working, command.cachedWorkType, !command.actionOnCache);

		/** 「日別実績の休憩時間帯」を削除する */
		if (result.getAction() == EventHandleAction.DELETE) {
			return EventHandleResult.withResult(EventHandleAction.DELETE, null);
		}

		/** 「日別実績の休憩時間帯」を更新する */
		if (result.getAction() == EventHandleAction.UPDATE || result.getAction() == EventHandleAction.INSERT) {
			BreakTimeOfDailyPerformance breakTime = result.getData().getBreakTime().stream()
					.filter(b -> b.getBreakType() == BreakType.REFER_WORK_TIME).findFirst().orElse(null);
			return EventHandleResult.withResult(EventHandleAction.UPDATE, breakTime);
		}
		return EventHandleResult.onFail();
	}

	private WorkInfoOfDailyPerformance getDefaultWorkInfo(UpdateBreakTimeByTimeLeaveChangeCommand command) {
		return this.workInfoRepo.find(command.employeeId, command.targetDate).orElse(null);
	}

	private <T> T getWithDefaul(Optional<T> target, Supplier<T> defaultVal) {
		if (target.isPresent()) {
			return target.get();
		}
		return defaultVal.get();
	}
}
