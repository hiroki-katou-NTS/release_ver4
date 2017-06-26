/******************************************************************
 * Copyright (c) 2016 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.core.dom.wagetable.certification;

import java.util.List;
import java.util.Optional;

/**
 * The Interface CertifyGroupRepository.
 */
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
    void remove(String companyCode, String groupCode);

	/**
	 * Find all.
	 *
	 * @param companyCode the company code
	 * @return the list
	 */
	List<CertifyGroup> findAll(String companyCode);

	/**
	 * Find by id.
	 *
	 * @param companyCode the company code
	 * @param code the code
	 * @return the optional
	 */
	Optional<CertifyGroup> findById(String companyCode, String code);

	/**
	 * Checks if is belong to exist group.
	 *
	 * @param companyCode the company code
	 * @param groupCode the group code
	 * @param certificationCode the certification code
	 * @return the boolean
	 */
	Boolean isBelongToExistGroup(String companyCode, String groupCode, String certificationCode);

}
