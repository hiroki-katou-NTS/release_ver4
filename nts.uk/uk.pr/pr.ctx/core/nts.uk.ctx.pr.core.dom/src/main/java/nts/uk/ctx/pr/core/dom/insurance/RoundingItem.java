/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.core.dom.insurance;

import lombok.Getter;

/**
 * The Class RoundingItem.
 */
@Getter
public class RoundingItem {

	/** The company round atr. */
	private RoundingMethod companyRoundAtr;

	/** The personal round atr. */
	private RoundingMethod personalRoundAtr;

	/**
	 * Instantiates a new rounding item.
	 *
	 * @param companyRoundAtr the company round atr
	 * @param personalRoundAtr the personal round atr
	 */
	public RoundingItem(RoundingMethod companyRoundAtr, RoundingMethod personalRoundAtr) {
		super();
		this.companyRoundAtr = companyRoundAtr;
		this.personalRoundAtr = personalRoundAtr;
	}

	/**
	 * Instantiates a new rounding item.
	 */
	public RoundingItem() {
		super();
	}
	
}
