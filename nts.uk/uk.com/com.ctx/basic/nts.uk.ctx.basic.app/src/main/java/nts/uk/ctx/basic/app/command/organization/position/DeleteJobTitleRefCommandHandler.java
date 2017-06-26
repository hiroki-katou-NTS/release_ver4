package nts.uk.ctx.basic.app.command.organization.position;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.arc.layer.app.command.CommandHandler;
import nts.arc.layer.app.command.CommandHandlerContext;
import nts.uk.ctx.basic.dom.organization.position.PositionRepository;
import nts.uk.shr.com.context.AppContexts;

@Stateless
public class DeleteJobTitleRefCommandHandler extends CommandHandler<DeleteJobTitleRefCommand> {
	@Inject
	private PositionRepository positionRepository;

	@Override
	protected void handle(CommandHandlerContext<DeleteJobTitleRefCommand> context) {

		String companyCode = AppContexts.user().companyCode();
		positionRepository.deleteJobTitleRef(companyCode, context.getCommand().getHistoryId(),context.getCommand().getJobCode());

	}
}
