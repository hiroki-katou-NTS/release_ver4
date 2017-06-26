/******************************************************************
 * Copyright (c) 2016 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.core.app.insurance.labor.accidentrate.find.dto;

import lombok.Getter;
import lombok.Setter;
import nts.uk.ctx.pr.core.dom.insurance.labor.accidentrate.InsuBizRateItem;

/**
 * The Class InsuBizRateItemFindOutDto.
 */
@Getter
@Setter
public class InsuBizRateItemFindDto {

	/** The insu biz type. */
	private Integer insuBizType;

	/** The insu rate. */
	private Double insuRate;

	/** The insu round. */
	private Integer insuRound;

	/** The insurance business type. */
	private String insuranceBusinessType;

	/**
	 * Instantiates a new insu biz rate item find out dto.
	 *
	 * @param item the item
	 */
	public InsuBizRateItemFindDto(InsuBizRateItem item) {
		this.insuBizType = item.getInsuBizType().value;
		this.insuRate = item.getInsuRate();
		this.insuRound = item.getInsuRound().value;
	}

}
