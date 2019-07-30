package nts.uk.ctx.at.record.app.command.dailyperform.calculationattribute;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.arc.layer.app.command.CommandHandlerContext;
import nts.uk.ctx.at.record.dom.daily.DailyRecordAdUpService;
import nts.uk.ctx.at.shared.app.util.attendanceitem.CommandFacade;

@Stateless
public class CalcAttrOfDailyPerformanceCommandUpdateHandler extends CommandFacade<CalcAttrOfDailyPerformanceCommand> {

	/*@Inject
	private CalAttrOfDailyPerformanceRepository repo;*/
	
	@Inject
	private DailyRecordAdUpService adUpRepo;

	@Override
	protected void handle(CommandHandlerContext<CalcAttrOfDailyPerformanceCommand> context) {
		if(context.getCommand().getData() == null) { return; }
		adUpRepo.adUpCalAttr(context.getCommand().toDomain());
	}

	
}
