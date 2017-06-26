package nts.uk.ctx.basic.app.command.organization.payclassification;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.arc.error.BusinessException;
import nts.arc.error.RawErrorMessage;
import nts.arc.layer.app.command.CommandHandler;
import nts.arc.layer.app.command.CommandHandlerContext;
import nts.uk.ctx.basic.dom.organization.payclassification.PayClassificationRepository;
import nts.uk.shr.com.context.AppContexts;

@Stateless
public class RemovePayClassificationCommandHandler extends CommandHandler<RemovePayClassificationCommand> {

	@Inject
	private PayClassificationRepository payClassificationRepository;

	@Override
	protected void handle(CommandHandlerContext<RemovePayClassificationCommand> context) {
		String companyCode = AppContexts.user().companyCode();
		if (!payClassificationRepository.isExisted(companyCode, context.getCommand().getPayClassificationCode())) {

			throw new BusinessException(new RawErrorMessage("対象データがありません。"));}

		payClassificationRepository.remove(companyCode, context.getCommand().getPayClassificationCode());

	}

}
