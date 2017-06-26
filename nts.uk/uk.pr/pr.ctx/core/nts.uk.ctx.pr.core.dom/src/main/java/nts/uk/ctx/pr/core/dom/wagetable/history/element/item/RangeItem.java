/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.core.dom.wagetable.history.element.item;

import java.math.BigDecimal;

import lombok.Getter;
import nts.uk.ctx.pr.core.dom.wagetable.ElementId;

/**
 * The Class RangeItem.
 */
@Getter
public class RangeItem extends BaseItem {

	/** The order number. */
	private Integer orderNumber;

	/** The start val. */
	private BigDecimal startVal;

	/** The end val. */
	private BigDecimal endVal;

	/**
	 * Instantiates a new range item.
	 *
	 * @param orderNumber
	 *            the order number
	 * @param startVal
	 *            the start val
	 * @param endVal
	 *            the end val
	 * @param uuid
	 *            the uuid
	 */
	public RangeItem(Integer orderNumber, BigDecimal startVal, BigDecimal endVal, ElementId uuid) {
		super(uuid);
		this.orderNumber = orderNumber;
		this.startVal = startVal;
		this.endVal = endVal;
	}
}
