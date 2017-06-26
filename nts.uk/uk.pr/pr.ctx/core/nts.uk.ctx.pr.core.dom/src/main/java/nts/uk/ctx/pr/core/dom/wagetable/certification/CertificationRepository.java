/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.core.dom.wagetable.certification;

import java.util.List;

/**
 * The Interface CertificationReponsitory.
 */
public interface CertificationRepository {

	/**
	 * Find all.
	 *
	 * @param companyCode the company code
	 * @return the list
	 */
	List<Certification> findAll(String companyCode);

	/**
	 * Find all none of group.
	 *
	 * @param companyCode the company code
	 * @return the list
	 */
	List<Certification> findAllNoneOfGroup(String companyCode);

}
