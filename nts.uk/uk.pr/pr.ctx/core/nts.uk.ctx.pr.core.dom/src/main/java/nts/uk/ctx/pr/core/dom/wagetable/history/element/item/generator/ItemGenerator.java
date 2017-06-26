/******************************************************************
 * Copyright (c) 2015 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.core.dom.wagetable.history.element.item.generator;

import java.util.List;

import nts.uk.ctx.pr.core.dom.wagetable.ElementType;
import nts.uk.ctx.pr.core.dom.wagetable.history.element.ElementSetting;
import nts.uk.ctx.pr.core.dom.wagetable.history.element.item.Item;

/**
 * The Interface ItemGenerator.
 */
public interface ItemGenerator {

	/**
	 * Generate.
	 *
	 * @param companyCode the company code
	 * @param elementSetting the element setting
	 * @return the list<? extends item>
	 */
	List<? extends Item> generate(String companyCode, String historyId, ElementSetting elementSetting);

	/**
	 * Gets the handle type.
	 *
	 * @return the handle type
	 */
	boolean canHandle(ElementType type);
}
