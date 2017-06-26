/******************************************************************
 * Copyright (c) 2015 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.core.dom.wagetable.history;

import java.util.List;

import nts.uk.ctx.pr.core.dom.insurance.MonthRange;
import nts.uk.ctx.pr.core.dom.wagetable.WtCode;
import nts.uk.ctx.pr.core.dom.wagetable.history.element.ElementSetting;

/**
 * The Interface WageTableHistorySetMemento.
 */
public interface WtHistorySetMemento {

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
	void setWageTableCode(WtCode code);

	/**
	 * Sets the history id.
	 *
	 * @param historyId the new history id
	 */
	void setHistoryId(String historyId);

	/**
	 * Sets the apply range.
	 *
	 * @param applyRange the new apply range
	 */
	void setApplyRange(MonthRange applyRange);

	/**
	 * Sets the element settings.
	 *
	 * @param elementSettings the new element settings
	 */
	void setElementSettings(List<ElementSetting> elementSettings);

	/**
	 * Sets the value items.
	 *
	 * @param valueItems the new value items
	 */
	void setValueItems(List<WtItem> valueItems);

}
