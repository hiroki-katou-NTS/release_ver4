/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.at.shared.dom.worktime.common.internal;

import javax.ejb.Stateless;

import nts.arc.error.BusinessException;
import nts.uk.ctx.at.shared.dom.worktime.common.OtherEmTimezoneLateEarlyPolicy;
import nts.uk.ctx.at.shared.dom.worktime.common.OtherEmTimezoneLateEarlySet;
import nts.uk.ctx.at.shared.dom.worktime.predset.PredetemineTimeSetting;

/**
 * The Class OtherEmTimezoneLateEarlyPolicyImpl.
 */
@Stateless
public class OtherEmTimezoneLateEarlyPolicyImpl implements OtherEmTimezoneLateEarlyPolicy {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nts.uk.ctx.at.shared.dom.worktime.common.OtherEmTimezoneLateEarlyPolicy#
	 * validLateTime(nts.uk.ctx.at.shared.dom.worktime.predset.
	 * PredetemineTimeSetting,
	 * nts.uk.ctx.at.shared.dom.worktime.common.OtherEmTimezoneLateEarlySet)
	 */
	@Override
	public void validLateTime(PredetemineTimeSetting predTime, OtherEmTimezoneLateEarlySet otSet) {
		if (otSet.getGraceTimeSet().getGraceTime().v() >= predTime.getRangeTimeDay().v()) {
			throw new BusinessException("Msg_517");
		}
	}

}
