package nts.uk.ctx.at.record.app.command.divergence.time;

import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.arc.layer.app.command.CommandHandler;
import nts.arc.layer.app.command.CommandHandlerContext;
import nts.uk.ctx.at.record.app.find.divergence.time.DivergenceTypeDto;
import nts.uk.ctx.at.record.dom.divergence.time.DivergenceReasonInputMethod;
import nts.uk.ctx.at.record.dom.divergence.time.DivergenceReasonInputMethodRepository;
import nts.uk.ctx.at.record.dom.divergence.time.DivergenceTime;
import nts.uk.ctx.at.record.dom.divergence.time.DivergenceTimeRepository;

/**
 * The Class DivergenceTimeInputMethodSaveCommandHandler.
 */
@Stateless
public class DivergenceTimeInputMethodSaveCommandHandler extends CommandHandler<DivergenceTimeInputMethodSaveCommand> {

	/** The divergence time repo. */
	@Inject
	private DivergenceTimeRepository divTimeRepo;

	/** The div reason input repo. */
	@Inject
	private DivergenceReasonInputMethodRepository divReasonInputRepo;

	/* (non-Javadoc)
	 * @see nts.arc.layer.app.command.CommandHandler#handle(nts.arc.layer.app.command.CommandHandlerContext)
	 */
	@Override
	protected void handle(CommandHandlerContext<DivergenceTimeInputMethodSaveCommand> context) {
		// get command
		DivergenceTimeInputMethodSaveCommand command = context.getCommand();
		int divType = DivergenceTypeDto.valueOfString(command.getDivergenceType()).value;

		// Convert to DivergenceTimeSaveCommand
		DivergenceTimeSaveCommand divTimeCommand = new DivergenceTimeSaveCommand(command.getDivergenceTimeNo(),
				command.getDivergenceTimeUseSet(), command.getDivergenceTimeName(), divType, command.isReasonInput(),
				command.isReasonSelect(),
				command.getTargetItems().stream().map(e -> e.doubleValue()).collect(Collectors.toList()));

		// Convert to DivergenceReasonInputMethosSaveCommand

		DivergenceReasonInputMethodSaveCommand divReasonCommand = new DivergenceReasonInputMethodSaveCommand(
				command.getDivergenceTimeNo(), command.isDivergenceReasonInputed(),
				command.isDivergenceReasonSelected());
		// convert to domain

		DivergenceTime divTime = new DivergenceTime(divTimeCommand);
		DivergenceReasonInputMethod divReasonInput = new DivergenceReasonInputMethod(divReasonCommand);
		// update
		this.divTimeRepo.update(divTime);
		if (command.getDivergenceTimeUseSet() == 1) {
			this.divReasonInputRepo.update(divReasonInput);
		}
	}

}
