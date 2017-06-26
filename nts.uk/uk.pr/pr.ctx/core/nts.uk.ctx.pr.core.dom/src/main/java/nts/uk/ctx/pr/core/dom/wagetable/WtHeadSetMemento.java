/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.core.dom.wagetable;

import java.util.List;

import nts.uk.ctx.pr.core.dom.wagetable.element.WtElement;
import nts.uk.shr.com.primitive.Memo;

/**
 * The Interface WageTableHeadSetMemento.
 */
public interface WtHeadSetMemento {

	/**
	 * Sets the company code.
	 *
	 * @param companyCode the new company code
	 */
	void setCompanyCode(String companyCode);

	/**
	 * Sets the code.
	 *
	 * @param code the new code
	 */
	void setCode(WtCode code);

	/**
	 * Sets the name.
	 *
	 * @param name the new name
	 */
	void setName(WtName name);

	/**
	 * Sets the memo.
	 *
	 * @param memo the new memo
	 */
	void setMemo(Memo memo);

	/**
	 * Gets the mode.
	 *
	 * @return the mode
	 */
	void setMode(ElementCount mode);

	/**
	 * Gets the elements.
	 *
	 * @return the elements
	 */
	void setElements(List<WtElement> elements);


}
