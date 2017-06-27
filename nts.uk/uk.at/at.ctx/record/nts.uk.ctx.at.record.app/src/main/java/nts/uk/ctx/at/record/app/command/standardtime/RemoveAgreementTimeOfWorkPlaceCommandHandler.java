package nts.uk.ctx.at.record.app.command.standardtime;

import java.util.Optional;

import javax.inject.Inject;

import nts.arc.enums.EnumAdaptor;
import nts.arc.layer.app.command.CommandHandler;
import nts.arc.layer.app.command.CommandHandlerContext;
import nts.uk.ctx.at.record.dom.standardtime.enums.LaborSystemtAtr;
import nts.uk.ctx.at.record.dom.standardtime.repository.AgreementTimeOfWorkPlaceDomainService;
import nts.uk.ctx.at.record.dom.standardtime.repository.AgreementTimeOfWorkPlaceRepository;

public class RemoveAgreementTimeOfWorkPlaceCommandHandler
		extends CommandHandler<RemoveAgreementTimeOfWorkPlaceCommand> {
	
	@Inject
	private AgreementTimeOfWorkPlaceDomainService agreementTimeOfWorkPlaceDomainService;

	@Inject
	private AgreementTimeOfWorkPlaceRepository agreementTimeOfWorkPlaceRepository;

	@Override
	protected void handle(CommandHandlerContext<RemoveAgreementTimeOfWorkPlaceCommand> context) {
		RemoveAgreementTimeOfWorkPlaceCommand command = context.getCommand();

		Optional<String> basicSettingId = this.agreementTimeOfWorkPlaceRepository.find(command.getWorkPlaceId(),
				EnumAdaptor.valueOf(command.getLaborSystemAtr(), LaborSystemtAtr.class));
		
		if(basicSettingId.isPresent()){
			this.agreementTimeOfWorkPlaceDomainService.remove(basicSettingId.get(), command.getWorkPlaceId(),command.getLaborSystemAtr());
		}
	}

}
