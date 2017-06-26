package nts.uk.ctx.pr.core.app.command.rule.employment.unitprice.personal;

import java.util.Optional;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;

import nts.arc.error.BusinessException;
import nts.arc.layer.app.command.CommandHandler;
import nts.arc.layer.app.command.CommandHandlerContext;
import nts.uk.ctx.pr.core.dom.rule.employment.unitprice.personal.PersonalUnitPrice;
import nts.uk.ctx.pr.core.dom.rule.employment.unitprice.personal.PersonalUnitPriceRepository;
import nts.uk.shr.com.context.AppContexts;

@RequestScoped
@Transactional
public class RemovePersonalUnitPriceCommandHander extends CommandHandler<RemovePersonalUnitPriceCommand> {

	@Inject
	private PersonalUnitPriceRepository personalUnitPriceRepository;
	
	@Override
	protected void handle(CommandHandlerContext<RemovePersonalUnitPriceCommand> context) {
		RemovePersonalUnitPriceCommand command = context.getCommand();
		String companyCode = AppContexts.user().companyCode();
		
		Optional<PersonalUnitPrice> unitPrice = personalUnitPriceRepository.find(companyCode, command.getPersonalUnitPriceCode());
		if (!unitPrice.isPresent()) {
			throw new RuntimeException("Unit price not found");
		}
		
	    personalUnitPriceRepository.remove(companyCode, command.getPersonalUnitPriceCode());
	}

}
