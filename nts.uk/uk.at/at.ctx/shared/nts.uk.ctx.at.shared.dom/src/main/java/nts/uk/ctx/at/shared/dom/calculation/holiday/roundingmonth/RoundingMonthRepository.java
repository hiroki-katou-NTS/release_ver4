/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.at.shared.dom.calculation.holiday.roundingmonth;

import java.util.List;
import java.util.Optional;

/**
 * The Interface RoundingMonthRepository.
 */
public interface RoundingMonthRepository {

	/**
	 * Find by company id.
	 *
	 * @param companyId the company id
	 * @param itemTimeId the item time id
	 * @return the list
	 */
	List<RoundingMonth> findByCompanyId(String companyId, String itemTimeId);

	/**
	 * Adds the.
	 *
	 * @param month the month
	 */
	void add(RoundingMonth month);

	/**
	 * Update.
	 *
	 * @param month the month
	 */
	void update(RoundingMonth month);

	/**
	 * Find by C id.
	 *
	 * @param companyId the company id
	 * @param timeItemId the time item id
	 * @return the optional
	 */
	Optional<RoundingMonth> findByCId(String companyId, String timeItemId);

}
