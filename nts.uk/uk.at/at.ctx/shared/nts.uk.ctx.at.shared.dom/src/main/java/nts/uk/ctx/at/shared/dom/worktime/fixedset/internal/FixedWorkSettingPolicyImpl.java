/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.at.shared.dom.worktime.fixedset.internal;

import javax.ejb.Stateless;

import nts.uk.ctx.at.shared.dom.worktime.fixedset.FixedWorkSetting;
import nts.uk.ctx.at.shared.dom.worktime.fixedset.FixedWorkSettingPolicy;
import nts.uk.ctx.at.shared.dom.worktime.predset.PredetemineTimeSet;

/**
 * The Class FixedWorkSettingPolicyImpl.
 */
@Stateless
public class FixedWorkSettingPolicyImpl implements FixedWorkSettingPolicy {

	// @Inject
	// private PredeteminePolicyService predService;

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.at.shared.dom.worktime.fixedset.FixedWorkSettingPolicy#
	 * canRegister(nts.uk.ctx.at.shared.dom.worktime.fixedset.FixedWorkSetting,
	 * nts.uk.ctx.at.shared.dom.worktime.predset.PredetemineTimeSet)
	 */
	@Override
	public void canRegister(FixedWorkSetting fixedWork, PredetemineTimeSet predTime) {
		// TODO
	}

}
