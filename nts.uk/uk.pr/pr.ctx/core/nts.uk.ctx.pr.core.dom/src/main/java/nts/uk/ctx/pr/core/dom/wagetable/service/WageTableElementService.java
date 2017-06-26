/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.core.dom.wagetable.service;

import java.util.List;

import nts.uk.ctx.pr.core.dom.wagetable.history.element.item.CodeItem;

/**
 * The Interface WageTableElementService.
 */
public interface WageTableElementService {

	/**
	 * Gets the ref mode items.
	 *
	 * @return the ref mode items
	 */
	List<CodeItem> getRefModeItems();

	/**
	 * Gets the step mode items.
	 *
	 * @return the step mode items
	 */
	List<CodeItem> getStepModeItems();

}