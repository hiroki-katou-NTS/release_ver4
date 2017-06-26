/******************************************************************
 * Copyright (c) 2016 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.report.app.insurance.command;

import java.util.Optional;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.transaction.Transactional;

import nts.arc.error.BusinessException;
import nts.arc.layer.app.command.CommandHandler;
import nts.arc.layer.app.command.CommandHandlerContext;
import nts.uk.ctx.pr.report.dom.insurance.ChecklistPrintSetting;
import nts.uk.ctx.pr.report.dom.insurance.ChecklistPrintSettingRepository;
import nts.uk.shr.com.context.AppContexts;

/**
 * The Class CheckListPrintSettingSaveCommandHandler.
 */
@Stateless
@Transactional
public class CheckListPrintSettingSaveCommandHandler
	extends CommandHandler<CheckListPrintSettingSaveCommand> {

	/** The checklist print setting repository. */
	@Inject
	private ChecklistPrintSettingRepository checklistPrintSettingRepository;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nts.arc.layer.app.command.CommandHandler#handle(nts.arc.layer.app.command
	 * .CommandHandlerContext)
	 */
	@Override
	protected void handle(CommandHandlerContext<CheckListPrintSettingSaveCommand> context) {
		CheckListPrintSettingSaveCommand command = context.getCommand();
		String companyCode = AppContexts.user().companyCode();

		// Validate command.
		// If 4 Select not check => throw error ER007 .
		if (!command.getShowDeliveryNoticeAmount() && !command.getShowDetail()
			&& !command.getShowOffice() && !command.getShowTotal()) {
			throw new BusinessException("出力設定が選択されていません。");
		}

		// Check is create or update.
		Optional<ChecklistPrintSetting> domainOptional = this.checklistPrintSettingRepository
			.findByCompanyCode(companyCode);
		if (domainOptional.isPresent()) {
			// Update.
			ChecklistPrintSetting domain = command.toDomain(companyCode);
			this.checklistPrintSettingRepository.update(domain);
			return;
		}

		// Create.
		ChecklistPrintSetting checklistPrintSetting = command.toDomain(companyCode);
		this.checklistPrintSettingRepository.create(checklistPrintSetting);
	}

}
