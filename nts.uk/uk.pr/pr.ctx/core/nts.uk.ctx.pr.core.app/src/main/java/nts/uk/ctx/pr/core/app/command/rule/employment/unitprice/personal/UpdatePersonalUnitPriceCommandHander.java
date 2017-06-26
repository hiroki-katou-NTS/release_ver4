package nts.uk.ctx.pr.core.app.command.rule.employment.unitprice.personal;

import java.util.Optional;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;

import nts.arc.layer.app.command.CommandHandler;
import nts.arc.layer.app.command.CommandHandlerContext;
import nts.uk.ctx.pr.core.dom.rule.employment.unitprice.personal.PersonalUnitPrice;
import nts.uk.ctx.pr.core.dom.rule.employment.unitprice.personal.PersonalUnitPriceRepository;
import nts.uk.shr.com.context.AppContexts;

@RequestScoped
@Transactional
public class UpdatePersonalUnitPriceCommandHander extends CommandHandler<UpdatePersonalUnitPriceCommand> {
	@Inject
	private PersonalUnitPriceRepository personalUnitPriceRepository;
	
	@Override
	protected void handle(CommandHandlerContext<UpdatePersonalUnitPriceCommand> context) {
		UpdatePersonalUnitPriceCommand command = context.getCommand();
		String companyCode = AppContexts.user().companyCode();
		
		Optional<PersonalUnitPrice> personalUnitPriceOp = personalUnitPriceRepository.find(companyCode, command.getPersonalUnitPriceCode());
		if (!personalUnitPriceOp.isPresent()) {
			throw new RuntimeException("Personal unit price not found");
		}
		
		PersonalUnitPrice domain = PersonalUnitPrice.createFromJavaType(
				companyCode,
				command.getPersonalUnitPriceCode().trim(),
				command.getPersonalUnitPriceName(),
				command.getPersonalUnitPriceShortName(),
				command.getUniteCode(),
				command.getMemo(),
				command.getFixPaymentAtr(),
				command.getFixPaymentMonthly(),
				command.getFixPaymentDayMonth(),
				command.getFixPaymentDaily(),
				command.getFixPaymentHoursly(),
				command.getDisplaySet(),
				command.getPaymentSettingType(),
				command.getUnitPriceAtr());
	    domain.validate();
	    personalUnitPriceRepository.update(domain);	
	}

}
