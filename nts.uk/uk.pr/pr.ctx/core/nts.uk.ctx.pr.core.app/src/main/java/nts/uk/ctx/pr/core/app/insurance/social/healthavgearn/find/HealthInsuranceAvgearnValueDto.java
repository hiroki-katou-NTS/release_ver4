/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.core.app.insurance.social.healthavgearn.find;

import java.math.BigDecimal;

import lombok.Data;
import nts.uk.ctx.pr.core.dom.insurance.CommonAmount;
import nts.uk.ctx.pr.core.dom.insurance.InsuranceAmount;
import nts.uk.ctx.pr.core.dom.insurance.social.healthavgearn.HealthInsuranceAvgearnValue;

/* (non-Javadoc)
 * @see java.lang.Object#toString()
 */
@Data
public class HealthInsuranceAvgearnValueDto {
	
	/** The health basic mny. */
	private BigDecimal healthBasicMny;

	/** The health general mny. */
	private BigDecimal healthGeneralMny;

	/** The health nursing mny. */
	private BigDecimal healthNursingMny;

	/** The health specific mny. */
	private BigDecimal healthSpecificMny;

	/**
	 * Instantiates a new health insurance avgearn value dto.
	 *
	 * @param healthBasicMny the health basic mny
	 * @param healthGeneralMny the health general mny
	 * @param healthNursingMny the health nursing mny
	 * @param healthSpecificMny the health specific mny
	 */
	public HealthInsuranceAvgearnValueDto(BigDecimal healthBasicMny, BigDecimal healthGeneralMny,
			BigDecimal healthNursingMny, BigDecimal healthSpecificMny) {
		super();
		this.healthBasicMny = healthBasicMny;
		this.healthGeneralMny = healthGeneralMny;
		this.healthNursingMny = healthNursingMny;
		this.healthSpecificMny = healthSpecificMny;
	}

	/**
	 * Instantiates a new health insurance avgearn value dto.
	 */
	public HealthInsuranceAvgearnValueDto() {
		super();
	}

	/**
	 * To domain.
	 *
	 * @param dto the dto
	 * @return the health insurance avgearn value
	 */
	public static HealthInsuranceAvgearnValue toDomain(HealthInsuranceAvgearnValueDto dto) {
		return new HealthInsuranceAvgearnValue(new InsuranceAmount(dto.getHealthBasicMny()),
				new CommonAmount(dto.getHealthGeneralMny()), new CommonAmount(dto.getHealthNursingMny()),
				new InsuranceAmount(dto.getHealthSpecificMny()));
	}

	/**
	 * From domain.
	 *
	 * @param domain the domain
	 * @return the health insurance avgearn value dto
	 */
	public static HealthInsuranceAvgearnValueDto fromDomain(HealthInsuranceAvgearnValue domain) {
		return new HealthInsuranceAvgearnValueDto(domain.getHealthBasicMny().v(), domain.getHealthGeneralMny().v(),
				domain.getHealthNursingMny().v(), domain.getHealthSpecificMny().v());
	}

}