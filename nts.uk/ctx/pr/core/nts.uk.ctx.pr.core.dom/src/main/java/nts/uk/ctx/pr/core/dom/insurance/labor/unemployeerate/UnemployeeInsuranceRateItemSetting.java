/******************************************************************
 * Copyright (c) 2016 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.core.dom.insurance.labor.unemployeerate;

import lombok.Getter;
import lombok.Setter;
import nts.uk.ctx.pr.core.dom.insurance.RoundingMethod;

/**
 * The Class LaborInsuranceOffice.
 */
@Getter
@Setter
public class UnemployeeInsuranceRateItemSetting {

	/** The company code. */
	private RoundingMethod roundAtr;

	/** The code. */
	private Double rate;

}
