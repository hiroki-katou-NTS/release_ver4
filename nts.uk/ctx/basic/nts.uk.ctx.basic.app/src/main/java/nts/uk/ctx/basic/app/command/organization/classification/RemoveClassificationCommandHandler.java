package nts.uk.ctx.basic.app.command.organization.classification;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.arc.layer.app.command.CommandHandler;
import nts.arc.layer.app.command.CommandHandlerContext;
import nts.uk.ctx.basic.dom.organization.classification.ClassificationCode;
import nts.uk.ctx.basic.dom.organization.classification.ClassificationRepository;
import nts.uk.shr.com.context.AppContexts;

@Stateless
public class RemoveClassificationCommandHandler extends CommandHandler<RemoveClassificationCommand> {

	@Inject
	private ClassificationRepository classificationRepository;

	@Override
	protected void handle(CommandHandlerContext<RemoveClassificationCommand> context) {
		String companyCode = AppContexts.user().companyCode();
		classificationRepository.remove(companyCode,
				new ClassificationCode(context.getCommand().getClassificationCode()));
	}

}
