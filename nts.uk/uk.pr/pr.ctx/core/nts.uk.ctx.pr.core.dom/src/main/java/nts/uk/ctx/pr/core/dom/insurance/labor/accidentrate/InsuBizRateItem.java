/******************************************************************
 * Copyright (c) 2016 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.core.dom.insurance.labor.accidentrate;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import nts.uk.ctx.pr.core.dom.insurance.RoundingMethod;
import nts.uk.ctx.pr.core.dom.insurance.labor.businesstype.BusinessTypeEnum;

/**
 * The Class InsuBizRateItem.
 */
@Getter
@Setter
@EqualsAndHashCode(of = { "insuBizType" })
public class InsuBizRateItem {

	/** The insu biz type. */
	private BusinessTypeEnum insuBizType;

	/** The insu rate. */
	private Double insuRate;

	/** The insu round. */
	private RoundingMethod insuRound;

	/**
	 * Instantiates a new insu biz rate item.
	 */
	public InsuBizRateItem() {
		super();
	}

	/**
	 * Instantiates a new insu biz rate item.
	 *
	 * @param insuBizType the insu biz type
	 * @param insuRate the insu rate
	 * @param insuRound the insu round
	 */
	public InsuBizRateItem(BusinessTypeEnum insuBizType, Double insuRate, RoundingMethod insuRound) {
		super();
		this.insuBizType = insuBizType;
		this.insuRate = insuRate;
		this.insuRound = insuRound;
	}

}
