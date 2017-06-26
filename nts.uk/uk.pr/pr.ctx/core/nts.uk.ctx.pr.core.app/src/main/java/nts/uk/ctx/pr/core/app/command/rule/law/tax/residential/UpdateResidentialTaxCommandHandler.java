package nts.uk.ctx.pr.core.app.command.rule.law.tax.residential;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.transaction.Transactional;

import nts.arc.error.BusinessException;
import nts.arc.error.RawErrorMessage;
import nts.arc.layer.app.command.CommandHandler;
import nts.arc.layer.app.command.CommandHandlerContext;
import nts.uk.ctx.pr.core.dom.rule.law.tax.residential.ResidentialTax;
import nts.uk.ctx.pr.core.dom.rule.law.tax.residential.ResidentialTaxRepository;
import nts.uk.shr.com.context.AppContexts;
/**
 * UpdateResidentalTaxCommandHandler
 * @author lanlt
 *
 */
@Stateless
@Transactional
public class UpdateResidentialTaxCommandHandler extends CommandHandler<UpdateResidentialTaxCommand> {

	 @Inject
	 private ResidentialTaxRepository resiTaxRepository;
	@Override
	protected void handle(CommandHandlerContext<UpdateResidentialTaxCommand> context) {
		 UpdateResidentialTaxCommand update = context.getCommand();
		 String companyCode = AppContexts.user().companyCode();
		 if(update.getResiTaxCode().isEmpty() || update.getResiTaxAutonomy().isEmpty()){
			 throw new BusinessException(new RawErrorMessage(" 明細書名が入力されていません。")); 
		 }	 
    ResidentialTax resiTax = ResidentialTax.createFromJavaType(
				 companyCode, update.getCompanyAccountNo(), update.getCompanySpecifiedNo(),
					update.getCordinatePostOffice(), update.getCordinatePostalCode(), update.getMemo(), 
					update.getPrefectureCode(), update.getRegisteredName(), update.getResiTaxAutonomy(), 
					update.getResiTaxAutonomyKana(),update.getResiTaxCode(), 
					update.getResiTaxReportCode());
		 this.resiTaxRepository.update(resiTax);
	}
}
