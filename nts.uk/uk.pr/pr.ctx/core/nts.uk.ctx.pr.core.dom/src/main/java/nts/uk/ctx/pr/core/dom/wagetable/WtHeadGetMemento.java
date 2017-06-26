/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.core.dom.wagetable;

import java.util.List;

import nts.uk.ctx.pr.core.dom.wagetable.element.WtElement;
import nts.uk.shr.com.primitive.Memo;

/**
 * The Interface WageTableHeadGetMemento.
 */
public interface WtHeadGetMemento {

	/**
	 * Gets the company code.
	 *
	 * @return the company code
	 */
	String getCompanyCode();

	/**
	 * Gets the code.
	 *
	 * @return the code
	 */
	WtCode getCode();

	/**
	 * Gets the name.
	 *
	 * @return the name
	 */
	WtName getName();

	/**
	 * Gets the mode.
	 *
	 * @return the mode
	 */
	ElementCount getMode();

	/**
	 * Gets the elements.
	 *
	 * @return the elements
	 */
	List<WtElement> getElements();

	/**
	 * Gets the memo.
	 *
	 * @return the memo
	 */
	Memo getMemo();
}
