/******************************************************************
 * Copyright (c) 2015 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.report.app.itemmaster.query;

import java.util.List;

/**
 * The Interface ItemMaterFinder.
 */
public interface ItemMaterRepository {
	
	/**
	 * Find all.
	 *
	 * @param companyCode the company code
	 * @return the list
	 */
	List<MasterItemDto> findAll(String companyCode);
	
	/**
	 * Find by codes.
	 *
	 * @param companyCode the company code
	 * @param codes the codes
	 * @return the list
	 */
	List<MasterItemDto> findByCodes(String companyCode, List<String> codes);
}
