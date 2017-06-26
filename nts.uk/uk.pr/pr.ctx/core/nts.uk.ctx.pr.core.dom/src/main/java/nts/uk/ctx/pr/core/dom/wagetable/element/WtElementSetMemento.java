/******************************************************************
 * Copyright (c) 2015 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.core.dom.wagetable.element;

import nts.uk.ctx.pr.core.dom.wagetable.DemensionNo;
import nts.uk.ctx.pr.core.dom.wagetable.ElementType;

/**
 * The Interface WageTableElementSetMemento.
 */
public interface WtElementSetMemento {

	/**
	 * Sets the demension no.
	 *
	 * @param demensionNo the new demension no
	 */
	void setDemensionNo(DemensionNo demensionNo);

	/**
	 * Sets the element type.
	 *
	 * @param type the new element type
	 */
	void setElementType(ElementType type);

	/**
	 * Sets the element ref code.
	 *
	 * @param code the new element ref code
	 */
	void setElementRefCode(String code);
}
