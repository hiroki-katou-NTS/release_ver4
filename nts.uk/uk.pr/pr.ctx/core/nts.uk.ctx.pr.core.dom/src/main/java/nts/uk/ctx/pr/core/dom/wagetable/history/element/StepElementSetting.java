/******************************************************************
 * Copyright (c) 2015 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.core.dom.wagetable.history.element;

import java.util.List;

import lombok.Getter;
import nts.uk.ctx.pr.core.dom.wagetable.DemensionNo;
import nts.uk.ctx.pr.core.dom.wagetable.ElementType;
import nts.uk.ctx.pr.core.dom.wagetable.history.element.item.Item;

/**
 * The Class StepElementSetting.
 */
@Getter
public class StepElementSetting extends ElementSetting {
	
	/** The upper limit. */
	private RangeLimit upperLimit;

	/** The lower limit. */
	private RangeLimit lowerLimit;

	/** The interval. */
	private RangeLimit interval;

	/**
	 * Instantiates a new step element setting.
	 *
	 * @param demensionNo
	 *            the demension no
	 * @param type
	 *            the type
	 * @param itemList
	 *            the item list
	 */
	public StepElementSetting(DemensionNo demensionNo, ElementType type,
			List<? extends Item> itemList) {
		super(demensionNo, type, itemList);
	}

	/**
	 * Sets the setting.
	 *
	 * @param upperLimit
	 *            the upper limit
	 * @param lowerLimit
	 *            the lower limit
	 * @param interval
	 *            the interval
	 */
	public void setSetting(RangeLimit lowerLimit, RangeLimit upperLimit, RangeLimit interval) {
		this.upperLimit = upperLimit;
		this.lowerLimit = lowerLimit;
		this.interval = interval;
	}

}
