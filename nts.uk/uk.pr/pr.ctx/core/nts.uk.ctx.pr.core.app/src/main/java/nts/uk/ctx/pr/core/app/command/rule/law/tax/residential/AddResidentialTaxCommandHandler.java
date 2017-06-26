package nts.uk.ctx.pr.core.app.command.rule.law.tax.residential;

import java.util.Optional;

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
 * AddResidentalTaxCommandHandler
 * 
 * @author lanlt
 *
 */
@Stateless
@Transactional
public class AddResidentialTaxCommandHandler extends CommandHandler<AddResidentialTaxCommand> {
	@Inject
	private ResidentialTaxRepository repo;

	@Override
	protected void handle(CommandHandlerContext<AddResidentialTaxCommand> context) {
		String companyCode = AppContexts.user().companyCode();
		AddResidentialTaxCommand add = context.getCommand();
		// ERROR5 resiTaxCode duplicate
		Optional<ResidentialTax> resiTax = this.repo.getResidentialTax(companyCode, add.getResiTaxCode().toString());
		if (resiTax.isPresent()) {
			throw new BusinessException( new RawErrorMessage("入力した 住民税納付先コード  は既に存在しています。\r\n 住民税納付先コードを確認してください。"));

		} else {
			this.repo.add(ResidentialTax.createFromJavaType(companyCode, add.getCompanyAccountNo(),
					add.getCompanySpecifiedNo(), add.getCordinatePostOffice(), add.getCordinatePostalCode(),
					add.getMemo(), add.getPrefectureCode(), add.getRegisteredName(), add.getResiTaxAutonomy(),
					add.getResiTaxAutonomyKana(), add.getResiTaxCode(), add.getResiTaxReportCode()));
		}
	}

}
