/******************************************************************
 * Copyright (c) 2016 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.core.dom.insurance.social.healthrate;

import lombok.Getter;
import lombok.Setter;
import nts.uk.ctx.pr.core.dom.insurance.Ins3Rate;

/**
 * The Class ChargeRateItem.
 */
@Getter
@Setter
public class HealthChargeRateItem {

	/** The company rate. */
	private Ins3Rate companyRate;

	/** The personal rate. */
	private Ins3Rate personalRate;

	/**
	 * Instantiates a new charge rate item.
	 */
	public HealthChargeRateItem() {
		super();
	}

	/**
	 * Instantiates a new health charge rate item.
	 *
	 * @param companyRate the company rate
	 * @param personalRate the personal rate
	 */
	public HealthChargeRateItem(Ins3Rate companyRate, Ins3Rate personalRate) {
		super();
		this.companyRate = companyRate;
		this.personalRate = personalRate;
	}

}
