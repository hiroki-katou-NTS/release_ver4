/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.at.shared.app.command.worktime.common.dto;

import lombok.Value;
import nts.uk.ctx.at.shared.app.find.worktime.common.dto.GoOutTimezoneRoundingSetDto;
import nts.uk.ctx.at.shared.dom.worktime.common.GoOutTimezoneRoundingSet;
import nts.uk.ctx.at.shared.dom.worktime.common.TotalRoundingSet;
import nts.uk.ctx.at.shared.dom.worktime.common.WorkTimezoneGoOutSetGetMemento;

/**
 * The Class WorkTimezoneGoOutSetDto.
 */
@Value
public class WorkTimezoneGoOutSetDto implements WorkTimezoneGoOutSetGetMemento {

	/** The total rounding set. */
	private TotalRoundingSetDto totalRoundingSet;

	/** The diff timezone setting. */
	private GoOutTimezoneRoundingSetDto diffTimezoneSetting;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nts.uk.ctx.at.shared.dom.worktime.common.WorkTimezoneGoOutSetGetMemento#
	 * getTotalRoundingSet()
	 */
	@Override
	public TotalRoundingSet getTotalRoundingSet() {
		return new TotalRoundingSet(this.totalRoundingSet);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nts.uk.ctx.at.shared.dom.worktime.common.WorkTimezoneGoOutSetGetMemento#
	 * getDiffTimezoneSetting()
	 */
	@Override
	public GoOutTimezoneRoundingSet getDiffTimezoneSetting() {
		return new GoOutTimezoneRoundingSet(this.diffTimezoneSetting);
	}

}
