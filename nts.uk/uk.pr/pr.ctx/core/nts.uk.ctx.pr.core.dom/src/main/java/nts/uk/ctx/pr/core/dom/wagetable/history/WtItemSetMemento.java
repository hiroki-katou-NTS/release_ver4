/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.core.dom.wagetable.history;

import nts.uk.ctx.pr.core.dom.wagetable.ElementId;
import nts.uk.ctx.pr.core.dom.wagetable.WtValue;

/**
 * The Interface WageTableItemSetMemento.
 */
public interface WtItemSetMemento {

	/**
	 * Sets the element 1 id.
	 *
	 * @param element1Id the new element 1 id
	 */
	void setElement1Id(ElementId element1Id);

	/**
	 * Sets the element 2 id.
	 *
	 * @param element2Id the new element 2 id
	 */
	void setElement2Id(ElementId element2Id);

	/**
	 * Sets the element 3 id.
	 *
	 * @param element3Id the new element 3 id
	 */
	void setElement3Id(ElementId element3Id);

	/**
	 * Sets the amount.
	 *
	 * @param amount the new amount
	 */
	void setAmount(WtValue amount);

}
