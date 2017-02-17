package nts.uk.ctx.pr.core.app.command.rule.law.tax.residential.output;

import java.util.Optional;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;

import nts.arc.error.BusinessException;
import nts.arc.layer.app.command.CommandHandler;
import nts.arc.layer.app.command.CommandHandlerContext;
import nts.uk.ctx.pr.core.dom.rule.law.tax.residential.output.ResidentTaxPaymentData;
import nts.uk.ctx.pr.core.dom.rule.law.tax.residential.output.ResidentTaxPaymentDataRepository;
import nts.uk.shr.com.context.AppContexts;

@RequestScoped
@Transactional
public class AddResimentTaxPaymentDataCommandHandler extends CommandHandler<AddResimentTaxPaymentDataCommand> {
	@Inject
	private ResidentTaxPaymentDataRepository repository;
	
	@Override
	protected void handle(CommandHandlerContext<AddResimentTaxPaymentDataCommand> context) {
		String companyCode = AppContexts.user().companyCode();
		AddResimentTaxPaymentDataCommand command = context.getCommand();
		
		// check exists data
		Optional<ResidentTaxPaymentData> taxtPaymentData = repository.find(companyCode, command.getResimentTaxCode(), command.getYearMonth());
		if (taxtPaymentData.isPresent()) {
			throw new BusinessException("Data exists!");
		}
		
		
		ResidentTaxPaymentData domain = ResidentTaxPaymentData.createFromJavaType(
				command.getResimentTaxCode(), 
				command.getTaxPayRollMoney(), 
				command.getTaxBonusMoney(), 
				command.getTaxOverDueMoney(),
				command.getTaxDemandChargeMoyney(), 
				command.getAddress(), 
				command.getDueDate(), 
				command.getHeadcount(), 
				command.getRetirementBonusAmout(), 
				command.getCityTaxMoney(), 
				command.getPrefectureTaxMoney(),
				command.getYearMonth());
		
		domain.validate();
		
		repository.add(companyCode, domain);
	}

}
