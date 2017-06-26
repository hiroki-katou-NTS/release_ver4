/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.core.dom.wagetable.history;

import nts.uk.ctx.pr.core.dom.wagetable.ElementId;
import nts.uk.ctx.pr.core.dom.wagetable.WtValue;

/**
 * The Interface WageTableItemGetMemento.
 */
public interface WtItemGetMemento {

	/**
	 * Gets the element 1 id.
	 *
	 * @return the element 1 id
	 */
	ElementId getElement1Id();

	/**
	 * Gets the element 2 id.
	 *
	 * @return the element 2 id
	 */
	ElementId getElement2Id();

	/**
	 * Gets the element 3 id.
	 *
	 * @return the element 3 id
	 */
	ElementId getElement3Id();

	/**
	 * Gets the amount.
	 *
	 * @return the amount
	 */
	WtValue getAmount();

}
