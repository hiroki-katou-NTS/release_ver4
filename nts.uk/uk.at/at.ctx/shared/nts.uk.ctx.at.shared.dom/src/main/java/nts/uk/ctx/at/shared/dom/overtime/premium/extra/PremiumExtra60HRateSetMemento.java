/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.at.shared.dom.overtime.premium.extra;

import nts.uk.ctx.at.shared.dom.overtime.OvertimeNo;
import nts.uk.ctx.at.shared.dom.overtime.breakdown.BreakdownItemNo;
import nts.uk.ctx.at.shared.dom.overtime.premium.PremiumRate;

/**
 * The Interface PremiumExtra60HRateSetMemento.
 */
public interface PremiumExtra60HRateSetMemento {
	
	/**
	 * Sets the breakdown item no.
	 *
	 * @param breakdownItemNo the new breakdown item no
	 */
	public void setBreakdownItemNo(BreakdownItemNo breakdownItemNo);
	
	
	/**
	 * Sets the premium rate.
	 *
	 * @param premiumRate the new premium rate
	 */
	public void setPremiumRate(PremiumRate premiumRate);
	
	
	/**
	 * Sets the overtime no.
	 *
	 * @param overtimeNo the new overtime no
	 */
	public void setOvertimeNo(OvertimeNo overtimeNo);

}
