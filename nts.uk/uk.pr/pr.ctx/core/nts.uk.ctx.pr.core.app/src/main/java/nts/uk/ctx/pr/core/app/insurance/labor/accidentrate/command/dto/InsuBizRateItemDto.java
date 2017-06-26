/******************************************************************
 * Copyright (c) 2016 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.core.app.insurance.labor.accidentrate.command.dto;

import lombok.Getter;
import lombok.Setter;
import nts.uk.ctx.pr.core.dom.insurance.RoundingMethod;
import nts.uk.ctx.pr.core.dom.insurance.labor.accidentrate.InsuBizRateItem;
import nts.uk.ctx.pr.core.dom.insurance.labor.businesstype.BusinessTypeEnum;

/**
 * Instantiates a new insu biz rate item dto.
 */
@Getter
@Setter
public class InsuBizRateItemDto {
	
	/** The insu biz type. */
	private Integer insuBizType;

	/** The insu rate. */
	private Double insuRate;

	/** The insu round. */
	private Integer insuRound;

	/**
	 * To domain.
	 *
	 * @return the insu biz rate item
	 */
	public InsuBizRateItem toDomain() {
		InsuBizRateItem insuBizRateItem = new InsuBizRateItem();
		insuBizRateItem.setInsuBizType(BusinessTypeEnum.valueOf(this.insuBizType));
		insuBizRateItem.setInsuRate(this.insuRate);
		insuBizRateItem.setInsuRound(RoundingMethod.valueOf(this.insuRound));
		return insuBizRateItem;
	}
}
