/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.core.dom.wagetable.history;

import java.util.List;

import nts.uk.ctx.pr.core.dom.insurance.MonthRange;
import nts.uk.ctx.pr.core.dom.wagetable.WtCode;
import nts.uk.ctx.pr.core.dom.wagetable.history.element.ElementSetting;

/**
 * The Interface WageTableHistoryGetMemento.
 */
public interface WtHistoryGetMemento {

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
	WtCode getWageTableCode();

	/**
	 * Gets the history id.
	 *
	 * @return the history id
	 */
	String getHistoryId();

	/**
	 * Gets the apply range.
	 *
	 * @return the apply range
	 */
	MonthRange getApplyRange();

	/**
	 * Gets the demension items.
	 *
	 * @return the demension items
	 */
	List<ElementSetting> getElementSettings();

	/**
	 * Gets the elements.
	 *
	 * @return the elements
	 */
	List<WtItem> getValueItems();

}
