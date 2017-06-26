/******************************************************************
 * Copyright (c) 2016 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.core.app.insurance.social.pensionrate.find;

import java.math.BigDecimal;

import lombok.Data;
import nts.uk.ctx.pr.core.dom.insurance.PaymentType;
import nts.uk.ctx.pr.core.dom.insurance.social.pensionrate.InsuranceGender;
import nts.uk.ctx.pr.core.dom.insurance.social.pensionrate.PensionPremiumRateItem;

/**
 * The Class PensionPremiumRateItemDto.
 */
@Data
public class PensionPremiumRateItemDto {

	/** The pay type. */
	private PaymentType payType;

	/** The gender type. */
	private InsuranceGender genderType;

	/** The personal rate. */
	private BigDecimal personalRate;

	/** The company rate. */
	private BigDecimal companyRate;

	/**
	 * Instantiates a new pension premium rate item dto.
	 */
	public PensionPremiumRateItemDto() {
		super();
	}
	
	/**
	 * Instantiates a new pension premium rate item dto.
	 *
	 * @param payType
	 *            the pay type
	 * @param genderType
	 *            the gender type
	 * @param personalRate
	 *            the personal rate
	 * @param companyRate
	 *            the company rate
	 */
	public PensionPremiumRateItemDto(PaymentType payType, InsuranceGender genderType, BigDecimal personalRate,
			BigDecimal companyRate) {
		super();
		this.payType = payType;
		this.genderType = genderType;
		this.personalRate = personalRate;
		this.companyRate = companyRate;
	}

	/**
	 * From domain.
	 *
	 * @param domain
	 *            the domain
	 * @return the pension premium rate item dto
	 */
	public static PensionPremiumRateItemDto fromDomain(PensionPremiumRateItem domain) {
		return new PensionPremiumRateItemDto(domain.getPayType(), domain.getGenderType(),
				domain.getChargeRate().getPersonalRate().v(), domain.getChargeRate().getCompanyRate().v());
	}

}
