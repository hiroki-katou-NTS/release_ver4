/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.core.dom.insurance.social.pensionrate;

import lombok.Getter;
import nts.uk.ctx.pr.core.dom.insurance.Ins2Rate;

/**
 * The Class ChargeRateItem.
 */
@Getter
public class PensionChargeRateItem {

	/** The company rate. */
	private Ins2Rate companyRate;

	/** The personal rate. */
	private Ins2Rate personalRate;

	/**
	 * Instantiates a new charge rate item.
	 */
	public PensionChargeRateItem() {
		super();
	}

	/**
	 * Instantiates a new pension charge rate item.
	 *
	 * @param companyRate the company rate
	 * @param personalRate the personal rate
	 */
	public PensionChargeRateItem(Ins2Rate companyRate, Ins2Rate personalRate) {
		super();
		this.companyRate = companyRate;
		this.personalRate = personalRate;
	}

}
