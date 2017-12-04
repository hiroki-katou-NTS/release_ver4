/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.at.shared.dom.worktime.fixedset;

import java.util.List;

/**
 * The Interface FixOffdayWorkTimezoneSetMemento.
 */
public interface FixOffdayWorkTimezoneSetMemento {

	/**
	 * Sets the rest timezone.
	 *
	 * @param restTimezone the new rest timezone
	 */
	void setRestTimezone(FixRestTimezoneSet restTimezone);

	/**
	 * Sets the lst work timezone.
	 *
	 * @param lstWorkTimezone the new lst work timezone
	 */
	void setLstWorkTimezone(List<HDWorkTimeSheetSetting> lstWorkTimezone);
	
}
