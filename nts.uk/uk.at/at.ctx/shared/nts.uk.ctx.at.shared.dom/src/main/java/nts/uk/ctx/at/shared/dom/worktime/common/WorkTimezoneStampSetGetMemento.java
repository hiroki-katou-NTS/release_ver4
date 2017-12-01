/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.at.shared.dom.worktime.common;

import java.util.List;

/**
 * The Interface WorkTimezoneStampSetGetMemento.
 */
public interface WorkTimezoneStampSetGetMemento {

	/**
 	 * Gets the rounding set.
 	 *
 	 * @return the rounding set
 	 */
 	List<RoundingSet> getRoundingSet();

	/**
	 * Gets the priority set.
	 *
	 * @return the priority set
	 */
	 List<PrioritySet> getPrioritySet();
}
