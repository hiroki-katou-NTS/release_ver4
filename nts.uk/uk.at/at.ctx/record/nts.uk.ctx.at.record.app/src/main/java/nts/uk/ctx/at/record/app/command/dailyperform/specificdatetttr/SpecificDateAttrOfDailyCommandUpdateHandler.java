package nts.uk.ctx.at.record.app.command.dailyperform.specificdatetttr;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.arc.layer.app.command.CommandHandlerContext;
import nts.uk.ctx.at.record.dom.daily.DailyRecordAdUpService;
import nts.uk.ctx.at.shared.app.util.attendanceitem.CommandFacade;

@Stateless
public class SpecificDateAttrOfDailyCommandUpdateHandler extends CommandFacade<SpecificDateAttrOfDailyCommand> {

	@Inject
	private DailyRecordAdUpService adUpRepo;

	@Override
	protected void handle(CommandHandlerContext<SpecificDateAttrOfDailyCommand> context) {
		SpecificDateAttrOfDailyCommand command = context.getCommand();
		if(command.getData().isPresent()){
			adUpRepo.adUpSpecificDate(command.toDomain());
		}
	}

}
