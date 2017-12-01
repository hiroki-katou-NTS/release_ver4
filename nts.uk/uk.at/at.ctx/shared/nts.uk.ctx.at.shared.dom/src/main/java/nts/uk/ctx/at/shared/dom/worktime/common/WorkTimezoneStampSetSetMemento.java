/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.at.shared.dom.worktime.common;

import java.util.List;

/**
 * The Interface WorkTimezoneStampSetSetMemento.
 */
public interface WorkTimezoneStampSetSetMemento {

	/**
	 * Sets the rounding set.
	 *
	 * @param rdSet the new rounding set
	 */
 	void setRoundingSet(List<RoundingSet> rdSet);

	/**
	 * Sets the priority set.
	 *
	 * @param prSet the new priority set
	 */
	 void setPrioritySet(List<PrioritySet> prSet);
}
