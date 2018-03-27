package nts.uk.ctx.at.record.app.command.divergence.time.message;

import java.util.Optional;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.transaction.Transactional;

import nts.arc.layer.app.command.CommandHandler;
import nts.arc.layer.app.command.CommandHandlerContext;
import nts.uk.ctx.at.record.dom.dailyperformanceformat.primitivevalue.BusinessTypeCode;
import nts.uk.ctx.at.record.dom.divergence.time.message.ErrorAlarmMessage;
import nts.uk.ctx.at.record.dom.divergence.time.message.WorkTypeDivergenceTimeErrorAlarmMessage;
import nts.uk.ctx.at.record.dom.divergence.time.message.WorkTypeDivergenceTimeErrorAlarmMessageRepository;
import nts.uk.ctx.at.shared.dom.common.CompanyId;
import nts.uk.shr.com.context.AppContexts;

/**
 * The Class WorkTypeDivergenceTimeErrorAlarmMessageSaveCommandHandler.
 */
@Stateless
public class WorkTypeDivergenceTimeErrorAlarmMessageSaveCommandHandler
		extends CommandHandler<WorkTypeDivergenceTimeErrorAlarmMessageCommand> {

	/** The repository. */
	@Inject
	private WorkTypeDivergenceTimeErrorAlarmMessageRepository repository;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nts.arc.layer.app.command.CommandHandler#handle(nts.arc.layer.app.command
	 * .CommandHandlerContext)
	 */
	@Override
	@Transactional
	protected void handle(CommandHandlerContext<WorkTypeDivergenceTimeErrorAlarmMessageCommand> context) {

		/* Get company ID */
		CompanyId companyId = new CompanyId(AppContexts.user().companyId());

		// Get command
		WorkTypeDivergenceTimeErrorAlarmMessageCommand command = context.getCommand();

		// Get workTypeCode
		BusinessTypeCode workTypeCode = new BusinessTypeCode(command.getWorkTypeCode());

		// Find WorkType divergence Time Error Alarm Message
		Optional<WorkTypeDivergenceTimeErrorAlarmMessage> opt = this.repository
				.getByDivergenceTimeNo(command.getDivergenceTimeNo(), companyId, workTypeCode);

		// Update
		if (opt.isPresent()) {
			WorkTypeDivergenceTimeErrorAlarmMessage domain = opt.get();
			domain.setAlarmMessage(Optional.of(new ErrorAlarmMessage(command.getAlarmMessage())));
			domain.setErrorMessage(Optional.of(new ErrorAlarmMessage(command.getErrorMessage())));
			this.repository.update(domain);
		} else {
			WorkTypeDivergenceTimeErrorAlarmMessage domain = new WorkTypeDivergenceTimeErrorAlarmMessage();
			domain.setCId(companyId);
			domain.setWorkTypeCode(new BusinessTypeCode(command.getWorkTypeCode()));
			domain.setDivergenceTimeNo(command.getDivergenceTimeNo());
			domain.setAlarmMessage(Optional.of(new ErrorAlarmMessage(command.getAlarmMessage())));
			domain.setErrorMessage(Optional.of(new ErrorAlarmMessage(command.getErrorMessage())));
			this.repository.add(domain);
		}
	}

}
