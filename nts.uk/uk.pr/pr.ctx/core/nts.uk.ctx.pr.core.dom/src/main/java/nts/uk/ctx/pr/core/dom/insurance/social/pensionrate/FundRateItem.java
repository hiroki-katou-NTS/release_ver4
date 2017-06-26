/******************************************************************
 * Copyright (c) 2016 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.core.dom.insurance.social.pensionrate;

import lombok.Data;
import nts.uk.ctx.pr.core.dom.insurance.PaymentType;

/**
 * The Class FundRateItem.
 */
@Data
public class FundRateItem {

	/** The pay type. */
	private PaymentType payType;

	/** The gender type. */
	private InsuranceGender genderType;

	/** The burden charge rate. */
	private PensionChargeRateItem burdenChargeRate;

	/** The exemption charge rate. */
	private PensionChargeRateItem exemptionChargeRate;

	/**
	 * Instantiates a new fund rate item.
	 */
	public FundRateItem() {
		super();
	}

	/**
	 * Instantiates a new fund rate item.
	 *
	 * @param payType
	 *            the pay type
	 * @param genderType
	 *            the gender type
	 * @param burdenChargeRate
	 *            the burden charge rate
	 * @param exemptionChargeRate
	 *            the exemption charge rate
	 */
	public FundRateItem(PaymentType payType, InsuranceGender genderType, PensionChargeRateItem burdenChargeRate,
			PensionChargeRateItem exemptionChargeRate) {
		super();
		this.payType = payType;
		this.genderType = genderType;
		this.burdenChargeRate = burdenChargeRate;
		this.exemptionChargeRate = exemptionChargeRate;
	}

}
