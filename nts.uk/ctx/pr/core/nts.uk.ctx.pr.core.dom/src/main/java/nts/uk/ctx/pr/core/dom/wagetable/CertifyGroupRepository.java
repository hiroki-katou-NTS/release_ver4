/******************************************************************
 * Copyright (c) 2016 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.core.dom.wagetable;

import java.util.List;
import java.util.Optional;

import nts.uk.ctx.core.dom.company.Company;
import nts.uk.ctx.core.dom.company.CompanyCode;

public interface CertifyGroupRepository {

	/**
	 * Adds the.
	 *
	 * @param certifyGroup the certify group
	 */
    void add(CertifyGroup certifyGroup);

	/**
	 * Update.
	 *
	 * @param certifyGroup the certify group
	 */
    void update(CertifyGroup certifyGroup);

	/**
	 * Removes the.
	 *
	 * @param companyCode the company code
	 * @param groupCode the group code
	 * @param version the version
	 */
    void remove(CompanyCode companyCode, String groupCode, Long version);

	/**
	 * Find all.
	 *
	 * @return the list
	 */
	List<CertifyGroup> findAll(CompanyCode companyCode);

	/**
	 * Find by id.
	 *
	 * @param companyCode the company code
	 * @param code the code
	 * @return the certify group
	 */
	Optional<CertifyGroup> findById(CompanyCode companyCode, String code);

}
