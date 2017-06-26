/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.core.dom.insurance.labor.businesstype;

import java.util.List;

/**
 * The Interface InsuranceBusinessTypeRepository.
 */
public interface InsuranceBusinessTypeRepository {

	List<InsuranceBusinessType> findAll(String companyCode);

	/**
	 * Update.
	 *
	 * @param lstInsuranceBusinessType the lst insurance business type
	 */
	void update(List<InsuranceBusinessType> lstInsuranceBusinessType);
}
