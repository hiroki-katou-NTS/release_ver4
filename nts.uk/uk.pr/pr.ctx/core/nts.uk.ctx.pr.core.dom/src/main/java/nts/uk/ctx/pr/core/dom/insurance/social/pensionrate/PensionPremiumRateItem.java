/******************************************************************
 * Copyright (c) 2016 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.core.dom.insurance.social.pensionrate;

import lombok.Getter;
import lombok.Setter;
import nts.arc.layer.dom.DomainObject;
import nts.uk.ctx.pr.core.dom.insurance.PaymentType;

/**
 * The Class PensionPremiumRateItem.
 */
@Getter
@Setter
public class PensionPremiumRateItem extends DomainObject {

	/** The pay type. */
	private PaymentType payType;

	/** The gender type. */
	private InsuranceGender genderType;

	/** The raise charge rate. */
	private PensionChargeRateItem chargeRate;

	/**
	 * Instantiates a new pension premium rate item.
	 *
	 * @param payType
	 *            the pay type
	 * @param genderType
	 *            the gender type
	 * @param chargeRate
	 *            the charge rate
	 */
	public PensionPremiumRateItem(PaymentType payType, InsuranceGender genderType, PensionChargeRateItem chargeRate) {
		super();
		this.payType = payType;
		this.genderType = genderType;
		this.chargeRate = chargeRate;
	}

}
