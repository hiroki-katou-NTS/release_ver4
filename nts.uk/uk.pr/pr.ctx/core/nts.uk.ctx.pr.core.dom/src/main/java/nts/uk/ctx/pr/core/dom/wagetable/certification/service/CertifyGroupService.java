/******************************************************************
 * Copyright (c) 2016 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.core.dom.wagetable.certification.service;

import nts.uk.ctx.pr.core.dom.wagetable.certification.CertifyGroup;

/**
 * The Interface CertifyGroupService.
 */
public interface CertifyGroupService {

	/**
	 * Validate required item.
	 *
	 * @param office the office
	 */
	void validateRequiredItem(CertifyGroup certifyGroup);

	/**
	 * Check duplicate code.
	 *
	 * @param office the office
	 */
	void checkDuplicateCode(CertifyGroup certifyGroup);

	/**
	 * Check dulicate certification.
	 *
	 * @param certifyGroup the certify group
	 * @param certifyGroupCode the certify group code
	 */
	void checkCertificationIsBelong(CertifyGroup certifyGroup);

}