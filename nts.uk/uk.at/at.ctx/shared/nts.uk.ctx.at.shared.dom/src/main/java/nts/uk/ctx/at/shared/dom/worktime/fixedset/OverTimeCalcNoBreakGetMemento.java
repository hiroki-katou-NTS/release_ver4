/******************************************************************
 * Copyright (c) 2018 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.at.shared.dom.worktime.fixedset;

import nts.uk.ctx.at.shared.dom.workrule.outsideworktime.overtime.overtimeframe.OverTimeFrameNo;
import nts.uk.ctx.at.shared.dom.worktime.common.CalcMethodNoBreak;

/**
 * The Interface OverTimeCalcNoBreakGetMemento.
 */
public interface OverTimeCalcNoBreakGetMemento {

	/**
	 * Gets the calc method.
	 *
	 * @return the calc method
	 */
	CalcMethodNoBreak getCalcMethod();

	/**
	 * Gets the in law OT.
	 *
	 * @return the in law OT
	 */
	OverTimeFrameNo getInLawOT();
	
	/**
	 * Gets the not in law OT.
	 *
	 * @return the not in law OT
	 */
	OverTimeFrameNo getNotInLawOT();
	
}
