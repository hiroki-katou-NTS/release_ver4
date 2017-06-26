package nts.uk.ctx.pr.core.app.command.rule.employment.unitprice.personal;

import java.util.Optional;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;

import nts.arc.error.BusinessException;
import nts.arc.layer.app.command.CommandHandler;
import nts.arc.layer.app.command.CommandHandlerContext;
import nts.gul.text.StringUtil;
import nts.uk.ctx.pr.core.dom.rule.employment.unitprice.personal.PersonalUnitPrice;
import nts.uk.ctx.pr.core.dom.rule.employment.unitprice.personal.PersonalUnitPriceRepository;
import nts.uk.shr.com.context.AppContexts;

@RequestScoped
@Transactional
public class AddPersonalUnitPriceCommandHandler extends CommandHandler<AddPersonalUnitPriceCommand> {
	@Inject
	private PersonalUnitPriceRepository personalUnitPriceRepository;

	@Override
	protected void handle(CommandHandlerContext<AddPersonalUnitPriceCommand> context) {
		AddPersonalUnitPriceCommand command = context.getCommand();
		String companyCode = AppContexts.user().companyCode();

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
	    
		// validate
		domain.validate();
		
	    Optional<PersonalUnitPrice> unitPrice = personalUnitPriceRepository.find(companyCode, command.getPersonalUnitPriceCode());
		if (unitPrice.isPresent()) {
			throw new BusinessException("ER005");
		}
	    
		personalUnitPriceRepository.add(domain);
	}
	

}
