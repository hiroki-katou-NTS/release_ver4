/******************************************************************
 * Copyright (c) 2015 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.core.dom.wagetable.history.element.item.generator;

import java.util.List;

import nts.uk.ctx.pr.core.dom.wagetable.history.element.ElementSetting;

/**
 * A factory for creating ElementItem objects.
 */
public interface ElementItemFactory {
	
	/**
	 * Generate.
	 *
	 * @param companyCode the company code
	 * @param elementSetting the element setting
	 * @return the list
	 */
	public List<ElementSetting> generate(String companyCode, String historyId, List<ElementSetting> elementSetting);
}
