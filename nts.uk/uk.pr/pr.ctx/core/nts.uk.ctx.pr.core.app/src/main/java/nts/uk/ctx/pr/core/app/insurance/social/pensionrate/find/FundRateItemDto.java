/******************************************************************
 * Copyright (c) 2016 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.core.app.insurance.social.pensionrate.find;

import java.math.BigDecimal;

import lombok.Data;
import nts.uk.ctx.pr.core.dom.insurance.PaymentType;
import nts.uk.ctx.pr.core.dom.insurance.social.pensionrate.FundRateItem;
import nts.uk.ctx.pr.core.dom.insurance.social.pensionrate.InsuranceGender;

/**
 * The Class FundRateItemDto.
 */
@Data
public class FundRateItemDto {
	/** The pay type. */
	private PaymentType payType;

	/** The gender type. */
	private InsuranceGender genderType;

	/** The burden charge rate. */
	private BigDecimal burdenChargePersonalRate;

	/** The burden charge rate. */
	private BigDecimal burdenChargeCompanyRate;

	/** The exemption charge rate. */
	private BigDecimal exemptionChargePersonalRate;

	/** The exemption charge rate. */
	private BigDecimal exemptionChargeCompanyRate;

	/**
	 * Instantiates a new fund rate item dto.
	 */
	public FundRateItemDto() {
		super();
	}
	
	/**
	 * Instantiates a new fund rate item dto.
	 *
	 * @param payType
	 *            the pay type
	 * @param genderType
	 *            the gender type
	 * @param burdenChargePersonalRate
	 *            the burden charge personal rate
	 * @param burdenChargeCompanyRate
	 *            the burden charge company rate
	 * @param exemptionChargePersonalRate
	 *            the exemption charge personal rate
	 * @param exemptionChargeCompanyRate
	 *            the exemption charge company rate
	 */
	public FundRateItemDto(PaymentType payType, InsuranceGender genderType, BigDecimal burdenChargePersonalRate,
			BigDecimal burdenChargeCompanyRate, BigDecimal exemptionChargePersonalRate,
			BigDecimal exemptionChargeCompanyRate) {
		super();
		this.payType = payType;
		this.genderType = genderType;
		this.burdenChargePersonalRate = burdenChargePersonalRate;
		this.burdenChargeCompanyRate = burdenChargeCompanyRate;
		this.exemptionChargePersonalRate = exemptionChargePersonalRate;
		this.exemptionChargeCompanyRate = exemptionChargeCompanyRate;
	}

	/**
	 * From domain.
	 *
	 * @param domain
	 *            the domain
	 * @return the fund rate item dto
	 */
	public static FundRateItemDto fromDomain(FundRateItem domain) {
		return new FundRateItemDto(domain.getPayType(), domain.getGenderType(),
				domain.getBurdenChargeRate().getPersonalRate().v(), domain.getBurdenChargeRate().getCompanyRate().v(),
				domain.getExemptionChargeRate().getPersonalRate().v(),
				domain.getExemptionChargeRate().getCompanyRate().v());
	}

}
