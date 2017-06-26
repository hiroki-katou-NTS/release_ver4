/******************************************************************
 * Copyright (c) 2016 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.core.dom.insurance.social.healthrate;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import nts.arc.error.BusinessException;
import nts.arc.layer.dom.DomainObject;
import nts.uk.ctx.pr.core.dom.insurance.PaymentType;

/**
 * The Class InsuranceRateItem.
 */
@Getter
@Setter
@EqualsAndHashCode(callSuper = true, of = { "payType", "insuranceType" })
public class InsuranceRateItem extends DomainObject {

	/** The pay type. */
	private PaymentType payType;

	/** The insurance type. */
	private HealthInsuranceType insuranceType;

	/** The charge rate. */
	private HealthChargeRateItem chargeRate;

	/**
	 * Instantiates a new insurance rate item.
	 *
	 * @param payType the pay type
	 * @param insuranceType the insurance type
	 * @param chargeRate the charge rate
	 */
	public InsuranceRateItem(PaymentType payType, HealthInsuranceType insuranceType, HealthChargeRateItem chargeRate) {
		super();

		// Validate required item
		if (payType == null || insuranceType == null || chargeRate == null) {
			throw new BusinessException("ER001");
		}

		this.chargeRate = chargeRate;
		this.payType = payType;
		this.insuranceType = insuranceType;
	}
}
