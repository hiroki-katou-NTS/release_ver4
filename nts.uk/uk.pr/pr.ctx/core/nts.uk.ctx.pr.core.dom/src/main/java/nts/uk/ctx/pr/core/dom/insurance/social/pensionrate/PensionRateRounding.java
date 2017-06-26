/******************************************************************
 * Copyright (c) 2016 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.core.dom.insurance.social.pensionrate;

import lombok.Data;
import nts.uk.ctx.pr.core.dom.insurance.PaymentType;
import nts.uk.ctx.pr.core.dom.insurance.RoundingItem;

/**
 * The Class PensionRateRounding.
 */
@Data
public class PensionRateRounding {

	/** The pay type. */
	private PaymentType payType;

	/** The round atrs. */
	private RoundingItem roundAtrs;

	/**
	 * Instantiates a new pension rate rounding.
	 */
	public PensionRateRounding() {
		super();
	}

	/**
	 * Instantiates a new pension rate rounding.
	 *
	 * @param payType the pay type
	 * @param roundAtrs the round atrs
	 */
	public PensionRateRounding(PaymentType payType, RoundingItem roundAtrs) {
		super();
		this.payType = payType;
		this.roundAtrs = roundAtrs;
	}
	
}
