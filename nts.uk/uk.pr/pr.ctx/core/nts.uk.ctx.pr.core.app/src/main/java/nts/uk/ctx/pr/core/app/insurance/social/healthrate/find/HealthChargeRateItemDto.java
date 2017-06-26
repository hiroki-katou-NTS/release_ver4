/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.core.app.insurance.social.healthrate.find;

import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;
import nts.uk.ctx.pr.core.dom.insurance.social.healthrate.HealthChargeRateItem;

/**
 * The Class HealthChargeRateItemDto.
 */
@Getter
@Setter
public class HealthChargeRateItemDto {
	
	/** The company rate. */
	private BigDecimal companyRate;

	/** The personal rate. */
	private BigDecimal personalRate;

	/**
	 * Instantiates a new health charge rate item dto.
	 */
	public HealthChargeRateItemDto() {
		super();
	}
	
	/**
	 * Instantiates a new health charge rate item dto.
	 *
	 * @param companyRate the company rate
	 * @param personalRate the personal rate
	 */
	public HealthChargeRateItemDto(BigDecimal companyRate, BigDecimal personalRate) {
		super();
		this.companyRate = companyRate;
		this.personalRate = personalRate;
	}

	/**
	 * From domain.
	 *
	 * @param domain the domain
	 * @return the health charge rate item dto
	 */
	public static HealthChargeRateItemDto fromDomain(HealthChargeRateItem domain) {
		return new HealthChargeRateItemDto(domain.getCompanyRate().v(), domain.getPersonalRate().v());
	}
}
