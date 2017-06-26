/******************************************************************
 * Copyright (c) 2016 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.core.dom.insurance.labor.unemployeerate;

import java.util.List;
import java.util.Optional;

import nts.arc.time.YearMonth;

/**
 * The Interface UnemployeeInsuranceRateRepository.
 */
public interface UnemployeeInsuranceRateRepository {

	/**
	 * Adds the.
	 *
	 * @param rate the rate
	 */
    void add(UnemployeeInsuranceRate rate);

	/**
	 * Update.
	 *
	 * @param rate the rate
	 */
    void update(UnemployeeInsuranceRate rate);

	/**
	 * Removes the.
	 *
	 * @param id the id
	 * @param version the version
	 */
    void remove(String companyCode, String historyId, Long version);

	/**
	 * Find all.
	 *
	 * @param companyCode the company code
	 * @return the list
	 */
	List<UnemployeeInsuranceRate> findAll(String companyCode);

	/**
	 * Find by id.
	 *
	 * @param id the id
	 * @return the unemployee insurance rate
	 */
	Optional<UnemployeeInsuranceRate> findById(String companyCode,String historyId);

	/**
	 * Find between update.
	 *
	 * @param companyCode the company code
	 * @param yearMonth the year month
	 * @return the optional
	 */
	Optional<UnemployeeInsuranceRate> findBetweenUpdate(String companyCode, YearMonth yearMonth,String historyId);

	/**
	 * Find fisrt data.
	 *
	 * @param companyCode the company code
	 * @return the optional
	 */
	Optional<UnemployeeInsuranceRate> findFirstData(String companyCode);

}
