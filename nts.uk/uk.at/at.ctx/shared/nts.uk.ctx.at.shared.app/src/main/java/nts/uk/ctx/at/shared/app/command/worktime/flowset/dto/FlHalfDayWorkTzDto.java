/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.at.shared.app.command.worktime.flowset.dto;

import lombok.Value;
import nts.uk.ctx.at.shared.app.command.worktime.common.dto.FlowWorkRestTimezoneDto;
import nts.uk.ctx.at.shared.dom.worktime.common.FlowWorkRestTimezone;
import nts.uk.ctx.at.shared.dom.worktime.flowset.FlHalfDayWtzGetMemento;
import nts.uk.ctx.at.shared.dom.worktime.flowset.FlWtzSetting;

/**
 * The Class FlHalfDayWorkTzDto.
 */
@Value
public class FlHalfDayWorkTzDto implements FlHalfDayWtzGetMemento {

	/** The rest timezone. */
	private FlowWorkRestTimezoneDto restTimezone;

	/** The work time zone. */
	private FlWorkTzSettingDto workTimeZone;

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.at.shared.dom.worktime.flowset.FlHalfDayWtzGetMemento#
	 * getRestTimezone()
	 */
	@Override
	public FlowWorkRestTimezone getRestTimezone() {
		return new FlowWorkRestTimezone(this.restTimezone);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.at.shared.dom.worktime.flowset.FlHalfDayWtzGetMemento#
	 * getWorkTimeZone()
	 */
	@Override
	public FlWtzSetting getWorkTimeZone() {
		return new FlWtzSetting(this.workTimeZone);
	}

}
