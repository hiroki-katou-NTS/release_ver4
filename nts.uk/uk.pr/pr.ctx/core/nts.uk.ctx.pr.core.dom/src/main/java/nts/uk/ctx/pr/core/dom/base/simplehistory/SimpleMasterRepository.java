/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.core.dom.base.simplehistory;

/**
 * The Interface SimpleMasterRepository.
 */
public interface SimpleMasterRepository<M extends Master> {

	/**
	 * Delete.
	 *
	 * @param companyCode the company code
	 * @param masterCode the master code
	 */
	void delete(String companyCode, String masterCode);
}
