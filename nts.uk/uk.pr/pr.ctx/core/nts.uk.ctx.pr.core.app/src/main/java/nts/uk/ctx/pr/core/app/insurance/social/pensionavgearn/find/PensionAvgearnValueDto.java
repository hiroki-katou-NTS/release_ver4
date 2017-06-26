/******************************************************************
 * Copyright (c) 2016 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.core.app.insurance.social.pensionavgearn.find;

import java.math.BigDecimal;

import lombok.Data;
import nts.uk.ctx.pr.core.dom.insurance.CommonAmount;
import nts.uk.ctx.pr.core.dom.insurance.social.pensionavgearn.PensionAvgearnValue;

/**
 * The Class PensionAvgearnValueDto.
 */
@Data
public class PensionAvgearnValueDto {

	/** The male amount. */
	private BigDecimal maleAmount;

	/** The female amount. */
	private BigDecimal femaleAmount;

	/** The unknown amount. */
	private BigDecimal unknownAmount;

	/**
	 * Instantiates a new pension avgearn value dto.
	 */
	public PensionAvgearnValueDto() {
		super();
	}

	/**
	 * Instantiates a new pension avgearn value dto.
	 *
	 * @param maleAmount
	 *            the male amount
	 * @param femaleAmount
	 *            the female amount
	 * @param unknownAmount
	 *            the unknown amount
	 */
	public PensionAvgearnValueDto(BigDecimal maleAmount, BigDecimal femaleAmount, BigDecimal unknownAmount) {
		super();
		this.maleAmount = maleAmount;
		this.femaleAmount = femaleAmount;
		this.unknownAmount = unknownAmount;
	}

	/**
	 * From domain.
	 *
	 * @param domain
	 *            the domain
	 * @return the pension avgearn value dto
	 */
	public static PensionAvgearnValueDto fromDomain(PensionAvgearnValue domain) {
		return new PensionAvgearnValueDto(domain.getMaleAmount().v(), domain.getFemaleAmount().v(),
				domain.getUnknownAmount().v());
	}

	/**
	 * To domain.
	 *
	 * @param dto
	 *            the dto
	 * @return the pension avgearn value
	 */
	public static PensionAvgearnValue toDomain(PensionAvgearnValueDto dto) {
		return new PensionAvgearnValue(new CommonAmount(dto.getMaleAmount()), new CommonAmount(dto.getFemaleAmount()),
				new CommonAmount(dto.getUnknownAmount()));
	}

}
