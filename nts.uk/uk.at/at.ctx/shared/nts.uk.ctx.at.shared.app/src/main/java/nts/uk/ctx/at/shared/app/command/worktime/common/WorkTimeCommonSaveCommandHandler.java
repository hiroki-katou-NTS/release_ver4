/******************************************************************
 * Copyright (c) 2018 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.at.shared.app.command.worktime.common;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.arc.error.BundledBusinessException;
import nts.arc.error.BusinessException;
import nts.uk.ctx.at.shared.dom.worktime.predset.PredetemineTimeSetting;
import nts.uk.ctx.at.shared.dom.worktime.predset.PredetemineTimeSettingRepository;
import nts.uk.ctx.at.shared.dom.worktime.worktimeset.WorkTimeSetting;
import nts.uk.ctx.at.shared.dom.worktime.worktimeset.WorkTimeSettingPolicy;
import nts.uk.ctx.at.shared.dom.worktime.worktimeset.WorkTimeSettingRepository;

/**
 * The Class WorkTimeCommonSaveCommandHandler.
 */
@Stateless
public class WorkTimeCommonSaveCommandHandler {

	/** The work time setting repository. */
	@Inject
	private WorkTimeSettingRepository workTimeSettingRepository;

	/** The predetemine time setting repository. */
	@Inject
	private PredetemineTimeSettingRepository predetemineTimeSettingRepository;

	/** The work time set policy. */
	@Inject
	private WorkTimeSettingPolicy workTimeSetPolicy;

	/**
	 * Handle.
	 *
	 * @param command
	 *            the command
	 */
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nts.arc.layer.app.command.CommandHandler#handle(nts.arc.layer.app.command
	 * .CommandHandlerContext)
	 */
	public void handle(WorkTimeCommonSaveCommand command) {

		// get work time setting by client send
		WorkTimeSetting workTimeSetting = command.toDomainWorkTimeSetting();

		// get pred setting by client send
		PredetemineTimeSetting predseting = command.toDomainPredetemineTimeSetting();

		// check is add mode
		if (command.isAddMode()) {
			// Validate
			this.validate(command, workTimeSetting, predseting);

			// call repository add work time setting
			this.workTimeSettingRepository.add(workTimeSetting);

			// call repository add predetemine time setting
			this.predetemineTimeSettingRepository.add(predseting);
		} else {
			// call repository update work time setting
			this.workTimeSettingRepository.update(workTimeSetting);

			// call repository update predetemine time setting
			this.predetemineTimeSettingRepository.update(predseting);
		}
	}

	/**
	 * Validate.
	 *
	 * @param command
	 *            the command
	 * @param workTimeSetting
	 *            the work time setting
	 */
	private void validate(WorkTimeCommonSaveCommand command, WorkTimeSetting workTimeSetting, PredetemineTimeSetting predseting) {
		BundledBusinessException bundledBusinessExceptions = BundledBusinessException.newInstance();

		// Check workTimeSetting domain
		try {
			workTimeSetting.validate();
		} catch (BundledBusinessException e) {
			bundledBusinessExceptions.addMessage(e.cloneExceptions());
		} catch (BusinessException e) {
			bundledBusinessExceptions.addMessage(e);
		} 

		// Check predSetting domain
		try {
			predseting.validate();
		} catch (BundledBusinessException e) {
			bundledBusinessExceptions.addMessage(e.cloneExceptions());
		} catch (BusinessException e) {
			bundledBusinessExceptions.addMessage(e);
		}

		// check register
		this.workTimeSetPolicy.validateExist(bundledBusinessExceptions, workTimeSetting);

		// Throw exceptions if exist
		if (!bundledBusinessExceptions.cloneExceptions().isEmpty()) {
			throw bundledBusinessExceptions;
		}
	}
}
