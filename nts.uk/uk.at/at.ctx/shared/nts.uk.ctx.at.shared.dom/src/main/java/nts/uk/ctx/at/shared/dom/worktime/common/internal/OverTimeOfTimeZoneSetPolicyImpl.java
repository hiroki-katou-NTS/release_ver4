/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.at.shared.dom.worktime.common.internal;

import javax.ejb.Stateless;
import javax.inject.Inject;

import lombok.val;
import nts.arc.error.BusinessException;
import nts.uk.ctx.at.shared.dom.worktime.common.EmTimeZoneSet;
import nts.uk.ctx.at.shared.dom.worktime.common.OverTimeOfTimeZoneSet;
import nts.uk.ctx.at.shared.dom.worktime.common.OverTimeOfTimeZoneSetPolicy;
import nts.uk.ctx.at.shared.dom.worktime.common.TimeZoneRoundingPolicy;
import nts.uk.ctx.at.shared.dom.worktime.predset.PredetemineTimeSetting;

/**
 * The Class OverTimeOfTimeZoneSetPolicyImpl.
 */
@Stateless
public class OverTimeOfTimeZoneSetPolicyImpl implements OverTimeOfTimeZoneSetPolicy {

	/** The tzr policy. */
	@Inject
	private TimeZoneRoundingPolicy tzrPolicy;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nts.uk.ctx.at.shared.dom.worktime.common.EmTimeZoneSetPolicy#validate(nts
	 * .uk.ctx.at.shared.dom.worktime.predset.PredetemineTimeSetting,
	 * nts.uk.ctx.at.shared.dom.worktime.common.EmTimeZoneSet)
	 */
	@Override
	public void validate(PredetemineTimeSetting predTime, OverTimeOfTimeZoneSet otSet, EmTimeZoneSet emTimezone) {
		val otTimezone = otSet.getTimezone();
		val shift1Timezone = predTime.getPrescribedTimezoneSetting().getTimezoneShiftOne();
		val shift2Timezone = predTime.getPrescribedTimezoneSetting().getTimezoneShiftTwo();

		// validate msg_516
		this.tzrPolicy.validateRange(predTime, otSet.getTimezone());

		// validate msg_519
		if (predTime.isPredetermine() && predTime.getPrescribedTimezoneSetting().getLstTimezone().stream()
				.anyMatch(shift -> otTimezone.isBetweenOrEqual(shift))) {
			throw new BusinessException("Msg_519");
		}

		// validate msg_779
		boolean isNotEarlyAndPred = !otSet.isEarlyOTUse() && !predTime.isPredetermine();
		boolean condition1 = !shift2Timezone.isUsed() && (otTimezone.getStart().lessThan(shift1Timezone.getEnd())
				|| otTimezone.getEnd().lessThan(shift1Timezone.getEnd()));
		boolean condition2 = shift2Timezone.isUsed() && (otTimezone.getStart().lessThan(shift2Timezone.getEnd())
				|| otTimezone.getEnd().lessThan(shift2Timezone.getEnd()));
		if (isNotEarlyAndPred && (condition1 || condition2)) {
			throw new BusinessException("Msg_779");
		}

		// validate msg_780
		if (!predTime.isPredetermine() && otSet.isEarlyOTUse()
				&& (otTimezone.getStart().greaterThan(shift1Timezone.getStart())
						|| otTimezone.getEnd().greaterThan(shift1Timezone.getStart()))) {
			throw new BusinessException("Msg_780");
		}
	}
}
