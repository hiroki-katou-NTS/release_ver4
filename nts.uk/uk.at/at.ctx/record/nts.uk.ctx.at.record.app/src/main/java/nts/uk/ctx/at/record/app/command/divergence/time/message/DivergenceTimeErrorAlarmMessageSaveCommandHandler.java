package nts.uk.ctx.at.record.app.command.divergence.time.message;

import java.util.Optional;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.transaction.Transactional;

import nts.arc.layer.app.command.CommandHandler;
import nts.arc.layer.app.command.CommandHandlerContext;
import nts.uk.ctx.at.record.dom.divergence.time.message.DivergenceTimeErrorAlarmMessage;
import nts.uk.ctx.at.record.dom.divergence.time.message.DivergenceTimeErrorAlarmMessageRepository;
import nts.uk.ctx.at.record.dom.divergence.time.message.ErrorAlarmMessage;
import nts.uk.ctx.at.shared.dom.common.CompanyId;
import nts.uk.shr.com.context.AppContexts;

/**
 * The Class DivergenceTimeErrorAlarmMessageSaveCommandHandler.
 */
@Stateless
public class DivergenceTimeErrorAlarmMessageSaveCommandHandler
		extends CommandHandler<DivergenceTimeErrorAlarmMessageCommand> {

	/** The repository. */
	@Inject
	private DivergenceTimeErrorAlarmMessageRepository repository;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nts.arc.layer.app.command.CommandHandler#handle(nts.arc.layer.app.command
	 * .CommandHandlerContext)
	 */
	@Override
	@Transactional
	protected void handle(CommandHandlerContext<DivergenceTimeErrorAlarmMessageCommand> context) {

		/* Get company ID */
		CompanyId companyId = new CompanyId(AppContexts.user().companyId());

		// get command
		DivergenceTimeErrorAlarmMessageCommand command = context.getCommand();

		// find Divergence Time Error Alarm Message
		Optional<DivergenceTimeErrorAlarmMessage> opt = this.repository.findByDivergenceTimeNo(companyId,
				command.getDivergenceTimeNo());

		// save
		if (opt.isPresent()) {
			DivergenceTimeErrorAlarmMessage domain = opt.get();
			domain.setAlarmMessage(Optional.of(new ErrorAlarmMessage(command.getAlarmMessage())));
			domain.setErrorMessage(Optional.of(new ErrorAlarmMessage(command.getErrorMessage())));
			this.repository.update(domain);
		} else {
			DivergenceTimeErrorAlarmMessage domain = new DivergenceTimeErrorAlarmMessage();
			domain.setCId(companyId);
			domain.setDivergenceTimeNo(command.getDivergenceTimeNo());
			domain.setAlarmMessage(Optional.of(new ErrorAlarmMessage(command.getAlarmMessage())));
			domain.setErrorMessage(Optional.of(new ErrorAlarmMessage(command.getErrorMessage())));
			this.repository.add(domain);
		}
	}

}
