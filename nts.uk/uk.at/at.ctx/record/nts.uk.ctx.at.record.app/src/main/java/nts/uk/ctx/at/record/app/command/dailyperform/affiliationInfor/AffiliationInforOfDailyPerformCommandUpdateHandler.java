package nts.uk.ctx.at.record.app.command.dailyperform.affiliationInfor;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.arc.layer.app.command.CommandHandlerContext;
import nts.uk.ctx.at.record.dom.affiliationinformation.repository.AffiliationInforOfDailyPerforRepository;
import nts.uk.ctx.at.shared.app.util.attendanceitem.CommandFacade;

@Stateless
public class AffiliationInforOfDailyPerformCommandUpdateHandler extends CommandFacade<AffiliationInforOfDailyPerformCommand> {

	@Inject
	private AffiliationInforOfDailyPerforRepository repo;

	@Override
	protected void handle(CommandHandlerContext<AffiliationInforOfDailyPerformCommand> context) {
		if(context.getCommand().getData() == null) { return; }
		repo.updateByKey(context.getCommand().toDomain());
	}

}
