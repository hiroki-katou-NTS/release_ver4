/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.at.shared.dom.worktime.common.internal;

import javax.ejb.Stateless;

import nts.arc.error.BusinessException;
import nts.uk.ctx.at.shared.dom.worktime.common.SubHolTransferSet;
import nts.uk.ctx.at.shared.dom.worktime.common.SubHolTransferSetPolicy;
import nts.uk.ctx.at.shared.dom.worktime.predset.PredetemineTimeSetting;

/**
 * The Class SubHolTransferSetPolicyImpl.
 */
@Stateless
public class SubHolTransferSetPolicyImpl implements SubHolTransferSetPolicy {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nts.uk.ctx.at.shared.dom.worktime.common.SubHolTransferSetPolicy#validate
	 * (nts.uk.ctx.at.shared.dom.worktime.predset.PredetemineTimeSetting,
	 * nts.uk.ctx.at.shared.dom.worktime.common.SubHolTransferSet)
	 */
	@Override
	public void validate(PredetemineTimeSetting predSet, SubHolTransferSet subHdSet) {
		if (subHdSet.getCertainTime().greaterThanOrEqualTo(predSet.getRangeTimeDay())) {
			throw new BusinessException("Msg_781");
		}
	}
}
