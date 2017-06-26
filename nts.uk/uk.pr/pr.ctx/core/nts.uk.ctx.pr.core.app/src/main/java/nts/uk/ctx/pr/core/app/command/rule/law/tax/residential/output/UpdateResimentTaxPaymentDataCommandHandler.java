package nts.uk.ctx.pr.core.app.command.rule.law.tax.residential.output;

import java.util.Optional;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.transaction.Transactional;
import nts.arc.error.BusinessException;
import nts.arc.error.RawErrorMessage;
import nts.arc.layer.app.command.CommandHandler;
import nts.arc.layer.app.command.CommandHandlerContext;
import nts.uk.ctx.pr.core.dom.rule.law.tax.residential.output.ResidentTaxPaymentData;
import nts.uk.ctx.pr.core.dom.rule.law.tax.residential.output.ResidentTaxPaymentDataRepository;
import nts.uk.shr.com.context.AppContexts;
/**
 * 
 * @author phongtq
 *
 */
@Stateless
@Transactional
public class UpdateResimentTaxPaymentDataCommandHandler extends CommandHandler<UpdateResimentTaxPaymentDataCommand> {
	@Inject
	private ResidentTaxPaymentDataRepository repository;

	@Override
	protected void handle(CommandHandlerContext<UpdateResimentTaxPaymentDataCommand> context) {
		String companyCode = AppContexts.user().companyCode();
		UpdateResimentTaxPaymentDataCommand command = context.getCommand();
		// Check exists data
		Optional<ResidentTaxPaymentData> taxtPaymentData = repository.find(
				companyCode, 
				command.getResimentTaxCode(),
				command.getYearMonth());
		if (!taxtPaymentData.isPresent()) {		
			throw new BusinessException(new RawErrorMessage("対象データがありません。"));
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
		repository.update(companyCode, domain);
	}

}
