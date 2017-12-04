/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.at.shared.app.find.worktime.common.dto;

import lombok.Getter;
import lombok.Setter;
import nts.uk.ctx.at.shared.dom.worktime.common.FlowRestTimezone;
import nts.uk.ctx.at.shared.dom.worktime.common.FlowWorkRestTimezoneSetMemeto;
import nts.uk.ctx.at.shared.dom.worktime.common.TimezoneOfFixedRestTimeSet;

/**
 * The Class FlowWorkRestTimezoneDto.
 */
@Getter
@Setter
public class FlowWorkRestTimezoneDto implements FlowWorkRestTimezoneSetMemeto{
	/** The fix rest time. */
	private boolean fixRestTime;
	
	/** The timezone. */
	private TimezoneOfFixedRestTimeSetDto fixedRestTimezone;
	
	/** The flow rest timezone. */
	private FlowRestTimezoneDto flowRestTimezone;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nts.uk.ctx.at.shared.dom.worktime.common.FlowWorkRestTimezoneSetMemeto#
	 * setFixRestTime(boolean)
	 */
	@Override
	public void setFixRestTime(boolean fixRestTime) {
		this.fixRestTime = fixRestTime;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nts.uk.ctx.at.shared.dom.worktime.common.FlowWorkRestTimezoneSetMemeto#
	 * setFixedRestTimezone(nts.uk.ctx.at.shared.dom.worktime.common.
	 * TimezoneOfFixedRestTimeSet)
	 */
	@Override
	public void setFixedRestTimezone(TimezoneOfFixedRestTimeSet fixedRestTimezone) {
		fixedRestTimezone.saveToMemento(this.fixedRestTimezone);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nts.uk.ctx.at.shared.dom.worktime.common.FlowWorkRestTimezoneSetMemeto#
	 * setFlowRestTimezone(nts.uk.ctx.at.shared.dom.worktime.common.
	 * FlowRestTimezone)
	 */
	@Override
	public void setFlowRestTimezone(FlowRestTimezone flowRestTimezone) {
		flowRestTimezone.saveToMemento(this.flowRestTimezone);
	}


	
}
