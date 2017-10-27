/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.at.schedule.dom.shift.totaltimes;

import nts.uk.ctx.at.schedule.dom.shift.pattern.WorkTypeCode;

/**
 * The Interface TotalSubjectsSetMemento.
 */
public interface TotalSubjectsSetMemento {

	/**
	 * Sets the work type code.
	 *
	 * @param setWorkTypeCode the new work type code
	 */
	void  setWorkTypeCode(WorkTypeCode setWorkTypeCode);

	/**
	 * Sets the work type atr.
	 *
	 * @param setWorkTypeAtr the new work type atr
	 */
	void  setWorkTypeAtr(WorkTypeAtr setWorkTypeAtr);
}
