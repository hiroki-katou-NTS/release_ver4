/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.core.dom.insurance.social.healthrate;

import java.util.List;
import java.util.Optional;

import nts.uk.ctx.pr.core.dom.base.simplehistory.SimpleHistoryRepository;

/**
 * The Interface HealthInsuranceRateRepository.
 */
public interface HealthInsuranceRateRepository extends SimpleHistoryRepository<HealthInsuranceRate>{

	/**
	 * Adds the.
	 *
	 * @param rate the rate
	 */
    void add(HealthInsuranceRate rate);

	/**
	 * Update.
	 *
	 * @param rate the rate
	 */
    void update(HealthInsuranceRate rate);

	/**
	 * Removes the.
	 *
	 * @param rate the rate
	 */
    void remove(String historyId);
    
    /**
     * Removes the by office code.
     *
     * @param companyCode the company code
     * @param officeCode the office code
     */
    void removeByOfficeCode(String companyCode, String officeCode);

	/**
	 * Find all.
	 *
	 * @param companyCode the company code
	 * @param officeCode the office code
	 * @return the list
	 */
	List<HealthInsuranceRate> findAll(String companyCode);

	/**
	 * Find by id.
	 *
	 * @param <Optional> the generic type
	 * @param id the id
	 * @return the health insurance rate
	 */
	Optional<HealthInsuranceRate> findById(String id);


	/**
	 * Find all office.
	 *
	 * @param companyCode the company code
	 * @param officeCode the office code
	 * @return the list
	 */
	List<HealthInsuranceRate> findAllOffice(String companyCode , String officeCode);
}
