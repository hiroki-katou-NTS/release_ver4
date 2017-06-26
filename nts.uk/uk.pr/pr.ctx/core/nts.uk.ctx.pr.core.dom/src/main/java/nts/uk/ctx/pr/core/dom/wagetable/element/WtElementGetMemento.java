/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.core.dom.wagetable.element;

import nts.uk.ctx.pr.core.dom.wagetable.DemensionNo;
import nts.uk.ctx.pr.core.dom.wagetable.ElementType;

/**
 * The Interface WageTableElementGetMemento.
 */
public interface WtElementGetMemento {

	/**
	 * Gets the demension no.
	 *
	 * @return the demension no
	 */
	DemensionNo getDemensionNo();

	/**
	 * Gets the element type.
	 *
	 * @return the element type
	 */
	ElementType getElementType();

	/**
	 * Gets the ref code.
	 *
	 * @return the ref code
	 */
	String getRefCode();
}
