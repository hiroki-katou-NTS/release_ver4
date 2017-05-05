package nts.uk.ctx.at.record.app.command.divergencetime;

import java.util.Optional;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.arc.error.BusinessException;
import nts.arc.layer.app.command.CommandHandler;
import nts.arc.layer.app.command.CommandHandlerContext;
import nts.uk.ctx.at.record.dom.divergencetime.DivergenceReason;
import nts.uk.ctx.at.record.dom.divergencetime.DivergenceTimeRepository;
import nts.uk.shr.com.context.AppContexts;
@Stateless
public class AddDivergenceReasonCommandHandler extends CommandHandler<AddDivergenceReasonCommand>{

	@Inject
	private DivergenceTimeRepository divTimeRepo;
	@Override
	protected void handle(CommandHandlerContext<AddDivergenceReasonCommand> context) {
		String companyId = AppContexts.user().companyCode();
		DivergenceReason divReason = DivergenceReason.createSimpleFromJavaType(companyId,
				context.getCommand().getDivTimeId(),
				context.getCommand().getDivReasonCode(),
				context.getCommand().getDivReasonContent(),
				context.getCommand().getRequiredAtr());
		Optional<DivergenceReason> divReasonF = divTimeRepo.getDivReason(companyId,
													context.getCommand().getDivTimeId(),
													context.getCommand().getDivReasonCode());
		if(divReasonF.isPresent()){
			throw new BusinessException("du lieu da khong con ton tai trong db");
		}else{
			divTimeRepo.addDivReason(divReason);
		}
	}

}
