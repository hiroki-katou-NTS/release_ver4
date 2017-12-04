/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.at.shared.dom.worktime.fixedset;

import java.util.List;

/**
 * The Interface FixOffdayWorkTimezoneGetMemento.
 */
public interface FixOffdayWorkTimezoneGetMemento {

	/**
	 * Gets the rest timezone.
	 *
	 * @return the rest timezone
	 */
	FixRestTimezoneSet getRestTimezone();

	/**
	 * Gets the lst work timezone.
	 *
	 * @return the lst work timezone
	 */
	List<HDWorkTimeSheetSetting> getLstWorkTimezone();
	
}
