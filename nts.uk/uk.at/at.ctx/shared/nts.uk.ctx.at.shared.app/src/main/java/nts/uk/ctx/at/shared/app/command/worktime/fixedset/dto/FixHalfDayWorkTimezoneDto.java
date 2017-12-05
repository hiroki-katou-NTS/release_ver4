/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.at.shared.app.command.worktime.fixedset.dto;

import lombok.Value;
import nts.uk.ctx.at.shared.app.command.worktime.flexset.dto.FixedWorkTimezoneSetDto;
import nts.uk.ctx.at.shared.dom.worktime.common.FixedWorkTimezoneSet;
import nts.uk.ctx.at.shared.dom.worktime.fixedset.FixHalfDayWorkTimezoneGetMemento;
import nts.uk.ctx.at.shared.dom.worktime.fixedset.FixRestTimezoneSet;
import nts.uk.ctx.at.shared.dom.worktime_old.AmPmClassification;

/**
 * The Class FixHalfDayWorkTimezoneDto.
 */
@Value
public class FixHalfDayWorkTimezoneDto implements FixHalfDayWorkTimezoneGetMemento {

	/** The rest timezone. */
	private FixRestTimezoneSetDto restTimezone;

	/** The work timezone. */
	private FixedWorkTimezoneSetDto workTimezone;

	/** The day atr. */
	private Integer dayAtr;

	/**
	 * Gets the rest timezone.
	 *
	 * @return the rest timezone
	 */
	@Override
	public FixRestTimezoneSet getRestTimezone() {
		return new FixRestTimezoneSet(this.restTimezone);
	}

	/**
	 * Gets the work timezone.
	 *
	 * @return the work timezone
	 */
	@Override
	public FixedWorkTimezoneSet getWorkTimezone() {
		return new FixedWorkTimezoneSet(this.workTimezone);
	}

	/**
	 * Gets the day atr.
	 *
	 * @return the day atr
	 */
	@Override
	public AmPmClassification getDayAtr() {
		return AmPmClassification.valueOf(this.dayAtr);
	}

}
