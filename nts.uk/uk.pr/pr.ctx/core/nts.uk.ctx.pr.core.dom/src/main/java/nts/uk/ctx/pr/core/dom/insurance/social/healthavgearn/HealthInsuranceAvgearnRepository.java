/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.core.dom.insurance.social.healthavgearn;

import java.util.List;
import java.util.Optional;

/**
 * The Interface HealthInsuranceAvgearnRepository.
 */
public interface HealthInsuranceAvgearnRepository {

	/**
	 * Update.
	 *
	 * @param healthInsuranceAvgearns the health insurance avgearns
	 * @param ccd the ccd
	 * @param officeCd the office cd
	 */
	void update(List<HealthInsuranceAvgearn> healthInsuranceAvgearns, String ccd, String officeCd);

	/**
	 * Find by id.
	 *
	 * @param id the id
	 * @return the list
	 */
	List<HealthInsuranceAvgearn> findById(String id);

	/**
	 * Find by office.
	 *
	 * @param companyCode the company code
	 * @param officeCodes the office codes
	 * @return the list
	 */
	List<HealthInsuranceAvgearn> findByOfficeCodes(String companyCode, List<String> officeCodes);

	/**
	 * Find.
	 *
	 * @param ccd the ccd
	 * @param officeCd the office cd
	 * @param historyId the history id
	 * @param grade the level code
	 * @return the optional
	 */
	Optional<HealthInsuranceAvgearn> find(String ccd, String officeCd, String historyId, Integer grade);
}
