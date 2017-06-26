/******************************************************************
 * Copyright (c) 2016 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.core.app.insurance.social.healthrate.find;

import lombok.Getter;
import lombok.Setter;
import nts.uk.ctx.pr.core.dom.insurance.PaymentType;
import nts.uk.ctx.pr.core.dom.insurance.social.healthrate.HealthInsuranceType;
import nts.uk.ctx.pr.core.dom.insurance.social.healthrate.InsuranceRateItem;

/**
 * The Class InsuranceRateItemDto.
 */
@Getter
@Setter
public class InsuranceRateItemDto {

	/** The pay type. */
	private PaymentType payType;

	/** The insurance type. */
	private HealthInsuranceType insuranceType;

	/** The charge rate. */
	private HealthChargeRateItemDto chargeRate;

	/**
	 * Instantiates a new insurance rate item dto.
	 */
	public InsuranceRateItemDto() {
		super();
	}
	
	/**
	 * Instantiates a new insurance rate item dto.
	 *
	 * @param payType
	 *            the pay type
	 * @param insuranceType
	 *            the insurance type
	 * @param chargeRate
	 *            the charge rate
	 */
	public InsuranceRateItemDto(PaymentType payType, HealthInsuranceType insuranceType,
			HealthChargeRateItemDto chargeRate) {
		super();
		this.payType = payType;
		this.insuranceType = insuranceType;
		this.chargeRate = chargeRate;
	}

	/**
	 * From domain.
	 *
	 * @param domain
	 *            the domain
	 * @return the insurance rate item dto
	 */
	public static InsuranceRateItemDto fromDomain(InsuranceRateItem domain) {
		return new InsuranceRateItemDto(domain.getPayType(), domain.getInsuranceType(),
				HealthChargeRateItemDto.fromDomain(domain.getChargeRate()));
	}

}
