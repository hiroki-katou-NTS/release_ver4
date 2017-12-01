/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.at.shared.dom.worktime.difftimeset;

import java.util.List;

/**
 * The Interface DiffTimeDayOffWorkTimezoneGetMemento.
 */
public interface DiffTimeDayOffWorkTimezoneGetMemento {

	/**
	 * Gets the rest timezone.
	 *
	 * @return the rest timezone
	 */
	public DiffTimeRestTimezone getRestTimezone() ;

	/**
	 * Gets the work timezone.
	 *
	 * @return the work timezone
	 */
	public List<DayOffTimezoneSetting> getWorkTimezone();
}
