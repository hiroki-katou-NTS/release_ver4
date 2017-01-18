/******************************************************************
 * Copyright (c) 2016 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.core.dom.insurance.social.pensionrate;

import java.util.List;

/**
 * The Interface PensionRateRepository.
 */
public interface PensionRateRepository {

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
	 * @param version the version
	 */
    void remove(String id, Long version);

	/**
	 * Find all.
	 *
	 * @param companyCode the company code
	 * @return the list
	 */
	List<PensionRate> findAll(int companyCode);

	/**
	 * Find by id.
	 *
	 * @param id the id
	 * @return the pension rate
	 */
	PensionRate findById(String id);
}
