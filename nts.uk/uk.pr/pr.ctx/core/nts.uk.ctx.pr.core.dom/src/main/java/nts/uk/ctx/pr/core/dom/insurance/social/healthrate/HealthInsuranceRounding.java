/******************************************************************
 * Copyright (c) 2016 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.core.dom.insurance.social.healthrate;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import nts.uk.ctx.pr.core.dom.insurance.PaymentType;
import nts.uk.ctx.pr.core.dom.insurance.RoundingItem;

/**
 * The Class HealthInsuranceRounding.
 */
@Getter
@Setter
@EqualsAndHashCode(of = { "payType" })
public class HealthInsuranceRounding {

	/** The pay type. */
	private PaymentType payType;

	/** The round atrs. */
	private RoundingItem roundAtrs;

	/**
	 * Instantiates a new health insurance rounding.
	 */
	public HealthInsuranceRounding() {
		super();
	}

	/**
	 * Instantiates a new health insurance rounding.
	 *
	 * @param payType the pay type
	 * @param roundAtrs the round atrs
	 */
	public HealthInsuranceRounding(PaymentType payType, RoundingItem roundAtrs) {
		super();
		this.payType = payType;
		this.roundAtrs = roundAtrs;
	}

}
