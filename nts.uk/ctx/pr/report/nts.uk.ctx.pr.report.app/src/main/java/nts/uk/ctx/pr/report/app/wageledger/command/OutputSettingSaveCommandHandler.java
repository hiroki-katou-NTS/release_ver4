/******************************************************************
 * Copyright (c) 2015 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.report.app.wageledger.command;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.transaction.Transactional;

import nts.arc.error.BusinessException;
import nts.arc.layer.app.command.CommandHandler;
import nts.arc.layer.app.command.CommandHandlerContext;
import nts.uk.ctx.pr.report.dom.company.CompanyCode;
import nts.uk.ctx.pr.report.dom.wageledger.outputsetting.WLOutputSetting;
import nts.uk.ctx.pr.report.dom.wageledger.outputsetting.WLOutputSettingCode;
import nts.uk.ctx.pr.report.dom.wageledger.outputsetting.WLOutputSettingRepository;
import nts.uk.shr.com.context.AppContexts;

/**
 * The Class OutputSettingSaveCommandHandler.
 */
@Stateless
public class OutputSettingSaveCommandHandler extends CommandHandler<OutputSettingSaveCommand> {
	
	/** The repository. */
	@Inject
	private WLOutputSettingRepository repository;

	/* (non-Javadoc)
	 * @see nts.arc.layer.app.command.CommandHandler#handle(nts.arc.layer.app.command.CommandHandlerContext)
	 */
	@Override
	@Transactional
	protected void handle(CommandHandlerContext<OutputSettingSaveCommand> context) {
		OutputSettingSaveCommand command = context.getCommand();
		String companyCode = AppContexts.user().companyCode();
		
		if (command.isCreateMode()) {
			// Check exist.
			if (this.repository.isExist(new CompanyCode(companyCode), new WLOutputSettingCode(command.getCode()))) {
				throw new BusinessException("入力したコードは既に存在しています。\r\nコードを確認してください。");
			}
			
			// Convert To Domain and save.
			WLOutputSetting outputSetting = command.toDomain(companyCode);
			outputSetting.validate();
			this.repository.create(outputSetting);
			return;
		}
		
		// Case update.
		WLOutputSetting outputSetting = this.repository.findByCode(new CompanyCode(companyCode), 
				new WLOutputSettingCode(command.getCode()));
		if (outputSetting == null) {
			throw new IllegalStateException("Output Setting is not found");
		}
		outputSetting = command.toDomain(companyCode);
		outputSetting.validate();
		this.repository.update(outputSetting);
	}

}
