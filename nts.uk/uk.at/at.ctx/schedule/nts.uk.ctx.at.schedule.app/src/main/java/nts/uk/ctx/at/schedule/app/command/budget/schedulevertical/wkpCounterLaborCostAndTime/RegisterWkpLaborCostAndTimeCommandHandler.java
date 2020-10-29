package nts.uk.ctx.at.schedule.app.command.budget.schedulevertical.wkpCounterLaborCostAndTime;

import nts.arc.enums.EnumAdaptor;
import nts.arc.layer.app.command.CommandHandler;
import nts.arc.layer.app.command.CommandHandlerContext;
import nts.uk.ctx.at.schedule.dom.shift.management.schedulecounter.laborcostandtime.LaborCostAndTime;
import nts.uk.ctx.at.schedule.dom.shift.management.schedulecounter.laborcostandtime.LaborCostAndTimeType;
import nts.uk.ctx.at.schedule.dom.shift.management.schedulecounter.laborcostandtime.WorkplaceCounterLaborCostAndTime;
import nts.uk.ctx.at.schedule.dom.shift.management.schedulecounter.laborcostandtime.WorkplaceCounterLaborCostAndTimeRepo;
import nts.uk.shr.com.context.AppContexts;
import nts.uk.shr.com.enumcommon.NotUseAtr;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.transaction.Transactional;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * 職場計の人件費・時間情報を登録する
 */
@Transactional
@Stateless
public class RegisterWkpLaborCostAndTimeCommandHandler extends CommandHandler<List<RegisterWkpLaborCostAndTimeCommand>> {
	@Inject
	private WorkplaceCounterLaborCostAndTimeRepo repository;

	@Override
	protected void handle(CommandHandlerContext<List<RegisterWkpLaborCostAndTimeCommand>> context) {
		List<RegisterWkpLaborCostAndTimeCommand> commands = context.getCommand();
		Map<LaborCostAndTimeType, LaborCostAndTime> laborCostAndTimeList = new HashMap<>();
		commands.stream().map(x -> laborCostAndTimeList.put(
			EnumAdaptor.valueOf(x.getLaborCostAndTimeType(), LaborCostAndTimeType.class),
			new LaborCostAndTime(
				NotUseAtr.valueOf(x.getUseClassification()),
				NotUseAtr.valueOf(x.getTime()),
				NotUseAtr.valueOf(x.getLaborCost()),
				x.getBudget() == null ? Optional.empty() : Optional.of(NotUseAtr.valueOf(x.getBudget())))
			));
		WorkplaceCounterLaborCostAndTime workplaceCounterLaborCostAndTime = new WorkplaceCounterLaborCostAndTime(laborCostAndTimeList);

		Optional<WorkplaceCounterLaborCostAndTime> wokpLaborCostAndTime = repository.get(AppContexts.user().companyId());
		if (wokpLaborCostAndTime.isPresent()){
			repository.update(AppContexts.user().companyId(), workplaceCounterLaborCostAndTime);
		}else {
			repository.insert(AppContexts.user().companyId(), workplaceCounterLaborCostAndTime);
		}
	}

}
