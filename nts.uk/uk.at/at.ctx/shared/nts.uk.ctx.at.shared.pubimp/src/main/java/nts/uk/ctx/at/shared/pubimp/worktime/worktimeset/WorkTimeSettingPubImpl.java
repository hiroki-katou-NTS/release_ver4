/******************************************************************
 * Copyright (c) 2018 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.at.shared.pubimp.worktime.worktimeset;

import java.util.Optional;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.uk.ctx.at.shared.dom.worktime.common.AbolishAtr;
import nts.uk.ctx.at.shared.dom.worktime.worktimeset.WorkTimeSetting;
import nts.uk.ctx.at.shared.dom.worktime.worktimeset.WorkTimeSettingRepository;
import nts.uk.ctx.at.shared.pub.worktime.worktimeset.WorkTimeSettingPub;

/**
 * The Class WorkTimeSettingPubImpl.
 */
@Stateless
public class WorkTimeSettingPubImpl implements WorkTimeSettingPub {

	/** The work time setting repository. */
	@Inject
	private WorkTimeSettingRepository workTimeSettingRepository;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nts.uk.ctx.at.shared.pub.worktime.worktimeset.WorkTimeSettingPub#isFlowWork(
	 * java.lang.String, java.lang.String)
	 */
	@Override
	public boolean isFlowWork(String companyId, String workTimeCode) {

		// Get WorkTimeSetting
		Optional<WorkTimeSetting> opWorkTimeSetting = this.workTimeSettingRepository.findByCode(companyId,
				workTimeCode);
		if (!opWorkTimeSetting.isPresent()) {
			return false;
		}

		return opWorkTimeSetting.get().getWorkTimeDivision().isFlow();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.at.shared.pub.worktime.worktimeset.WorkTimeSettingPub#
	 * getWorkTimeSettingName(java.lang.String, java.lang.String)
	 */
	@Override
	public String getWorkTimeSettingName(String companyId, String workTimeCode) {

		// Get WorkTimeSetting
		Optional<WorkTimeSetting> opWorkTimeSetting = this.workTimeSettingRepository.findByCode(companyId,
				workTimeCode);
		if (!opWorkTimeSetting.isPresent()) {
			return null;
		}

		if (opWorkTimeSetting.get().getAbolishAtr().equals(AbolishAtr.ABOLISH)) {
			return null;
		}
		return opWorkTimeSetting.get().getWorkTimeDisplayName().getWorkTimeName().v();
	}

	@Override
	public boolean isExist(String companyId, String worktimeCode) {
		// Get WorkTimeSetting
		Optional<WorkTimeSetting> opWorkTimeSetting = this.workTimeSettingRepository.findByCode(companyId,
				worktimeCode);
		if (opWorkTimeSetting.isPresent()) {
			return true;
		}
		return false;
	}

}
