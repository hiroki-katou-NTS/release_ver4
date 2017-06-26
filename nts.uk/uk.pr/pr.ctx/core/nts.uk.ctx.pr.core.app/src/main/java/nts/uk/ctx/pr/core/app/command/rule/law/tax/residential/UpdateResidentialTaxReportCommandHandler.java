package nts.uk.ctx.pr.core.app.command.rule.law.tax.residential;

import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.transaction.Transactional;

import nts.arc.error.BusinessException;
import nts.arc.error.RawErrorMessage;
import nts.arc.layer.app.command.CommandHandler;
import nts.arc.layer.app.command.CommandHandlerContext;
import nts.uk.ctx.pr.core.dom.rule.law.tax.residential.ResidentialTaxRepository;
import nts.uk.ctx.pr.core.dom.rule.law.tax.residential.input.PersonResiTaxRepository;
import nts.uk.shr.com.context.AppContexts;

/**
 * UpdateResidentialTaxReportCommandHandler
 * 
 * @author lanlt
 *
 */
@Stateless
@Transactional
public class UpdateResidentialTaxReportCommandHandler extends CommandHandler<UpdateResidentialTaxReportCommand> {
	@Inject
	private ResidentialTaxRepository resiTaxRepository;
	@Inject
	private PersonResiTaxRepository personTaxRepository;

	@Override
	protected void handle(CommandHandlerContext<UpdateResidentialTaxReportCommand> context) {
		UpdateResidentialTaxReportCommand update = context.getCommand();
		String companyCode = AppContexts.user().companyCode();
		List<String> lstResidentaxCode = update.getResiTaxCodes();
		List<?> lstResiTaxCheck = this.resiTaxRepository.getAllResidentialTaxCode(companyCode,
				update.getResiTaxReportCode());
		// error 001
		if (update.getYearKey() == 0) {
			throw new BusinessException(new RawErrorMessage("対象年度  が入力されていません。。"));
		}
		// error009
		if (lstResiTaxCheck.isEmpty()) {
			throw new BusinessException(new RawErrorMessage("統合元と統合先で同じコードの が選択されています。\r\n ＊を確認してください。"));
		}

		// error 007
		if (lstResidentaxCode.isEmpty()) {
			throw new BusinessException(new RawErrorMessage("住民税納付先コード が選択されていません。"));
		}
		lstResidentaxCode.stream().forEach(c -> {
			List<?> lstResiTaxUpdate = this.resiTaxRepository.getAllResidentialTaxCode(companyCode, c.toString());
			lstResiTaxUpdate.stream().forEach(resiTaxUpdate -> {
				this.resiTaxRepository.update(companyCode, resiTaxUpdate.toString(), update.getResiTaxReportCode());
			});
			List<?> lstPersonId = this.personTaxRepository.findByResidenceCode(companyCode, c, update.getYearKey());
			lstPersonId.stream().forEach(personId -> {
				this.personTaxRepository.updateResendence(companyCode, update.getResiTaxReportCode(),
						personId.toString(), update.getYearKey());
			});
		});
	}
}