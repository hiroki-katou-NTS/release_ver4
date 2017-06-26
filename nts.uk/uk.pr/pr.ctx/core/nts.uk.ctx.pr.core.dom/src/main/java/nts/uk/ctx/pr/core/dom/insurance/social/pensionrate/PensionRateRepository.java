/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.core.dom.insurance.social.pensionrate;

import java.util.List;
import java.util.Optional;

import nts.uk.ctx.pr.core.dom.base.simplehistory.SimpleHistoryRepository;

/**
 * The Interface PensionRateRepository.
 */
public interface PensionRateRepository extends SimpleHistoryRepository<PensionRate>{

	/**
	 * Adds the.
	 *
	 * @param rate the rate
	 */
    void add(PensionRate rate);

	/**
	 * Update.
	 *
	 * @param rate the rate
	 */
    void update(PensionRate rate);

	/**
	 * Removes the.
	 *
	 * @param id the id
	 */
    void remove(String id);
    
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
	List<PensionRate> findAll(String companyCode);

	/**
	 * Find all office.
	 *
	 * @param companyCode the company code
	 * @param officeCode the office code
	 * @return the list
	 */
	List<PensionRate> findAllOffice(String companyCode,String officeCode);

	/**
	 * Find by id.
	 *
	 * @param id the id
	 * @return the pension rate
	 */
	Optional<PensionRate> findById(String id);
}
