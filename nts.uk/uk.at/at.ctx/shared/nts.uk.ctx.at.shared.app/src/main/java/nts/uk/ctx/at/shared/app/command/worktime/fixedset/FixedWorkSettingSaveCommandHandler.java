/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.at.shared.app.command.worktime.fixedset;

import java.util.Optional;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.arc.layer.app.command.CommandHandler;
import nts.arc.layer.app.command.CommandHandlerContext;
import nts.uk.ctx.at.shared.app.command.worktime.common.WorkTimeCommonSaveCommandHandler;
import nts.uk.ctx.at.shared.dom.worktime.fixedset.FixedWorkSetting;
import nts.uk.ctx.at.shared.dom.worktime.fixedset.FixedWorkSettingPolicy;
import nts.uk.ctx.at.shared.dom.worktime.fixedset.FixedWorkSettingRepository;
import nts.uk.shr.com.context.AppContexts;

/**
 * The Class FixedWorkSettingSaveCommandHandler.
 */
@Stateless
public class FixedWorkSettingSaveCommandHandler extends CommandHandler<FixedWorkSettingSaveCommand> {

	/** The fixed work setting repository. */
	@Inject
	private FixedWorkSettingRepository fixedWorkSettingRepository;
	
	/** The common handler. */
	@Inject
	private WorkTimeCommonSaveCommandHandler commonHandler;
	
	/** The fixed policy. */
	@Inject
	private FixedWorkSettingPolicy fixedPolicy;
	
	/* (non-Javadoc)
	 * @see nts.arc.layer.app.command.CommandHandler#handle(nts.arc.layer.app.command.CommandHandlerContext)
	 */
	@Override
	protected void handle(CommandHandlerContext<FixedWorkSettingSaveCommand> context) {

		String companyId = AppContexts.user().companyId();

		// get command
		FixedWorkSettingSaveCommand command = context.getCommand();

		// get domain fixed work setting by client send
		FixedWorkSetting fixedWorkSetting = command.toDomainFixedWorkSetting();

		// Validate
		this.fixedPolicy.canRegister(fixedWorkSetting, command.toDomainPredetemineTimeSetting());

		// common handler
		this.commonHandler.handle(command);

		// call repository save flex work setting
		Optional<FixedWorkSetting> opFixedWorkSetting = this.fixedWorkSettingRepository.findByKey(companyId,
				command.getWorktimeSetting().worktimeCode);
		if (opFixedWorkSetting.isPresent()) {
			fixedWorkSetting.restoreData(command.getScreenMode(), null, opFixedWorkSetting.get());
			this.fixedWorkSettingRepository.update(fixedWorkSetting);
			return;
		}
		this.fixedWorkSettingRepository.add(fixedWorkSetting);
	}

}
