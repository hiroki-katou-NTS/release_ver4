package nts.uk.ctx.pr.core.app.command.paymentdata;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.transaction.Transactional;

import nts.arc.layer.app.command.CommandHandler;
import nts.arc.layer.app.command.CommandHandlerContext;
import nts.uk.ctx.pr.core.dom.paymentdata.repository.PaymentDataRepository;
import nts.uk.shr.com.context.AppContexts;

@Stateless
@Transactional
public class RemovePaymentDataCommandHandler extends CommandHandler<RemovePaymentDataCommand>{

	/** paymentDataRepository */
	@Inject
	private PaymentDataRepository paymentDataRepository;
	
	@Override
	protected void handle(CommandHandlerContext<RemovePaymentDataCommand> context) {
		String companyCode = AppContexts.user().companyCode();
		
		this.paymentDataRepository.removeDetails(context.getCommand().getPersonId(), context.getCommand().getProcessingYM(), companyCode);
		this.paymentDataRepository.removeHeader(context.getCommand().getPersonId(), context.getCommand().getProcessingYM(), companyCode);
	}

}
