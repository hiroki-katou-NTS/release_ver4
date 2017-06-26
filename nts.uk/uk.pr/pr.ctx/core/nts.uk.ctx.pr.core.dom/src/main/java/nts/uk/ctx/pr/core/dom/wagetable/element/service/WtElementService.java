/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.core.dom.wagetable.element.service;

import nts.uk.ctx.pr.core.dom.wagetable.ElementType;

/**
 * The Interface WageTableElementRepository.
 */
public interface WtElementService {

	/**
	 * Gets the demension name.
	 *
	 * @param companyCode the company code
	 * @param referenceCode the reference code
	 * @param type the type
	 * @return the demension name
	 */
	String getDemensionName(String companyCode, String referenceCode, ElementType type);

}
