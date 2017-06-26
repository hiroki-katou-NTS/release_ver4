/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.report.dom.salarydetail.aggregate;

import java.util.List;
import java.util.Optional;

/**
 * The Interface SalaryAggregateItemRepository.
 */
public interface SalaryAggregateItemRepository {

	/**
	 * Adds the.
	 *
	 * @param aggregateItem the aggregate item
	 */
	void add(SalaryAggregateItem aggregateItem);

	/**
	 * Update.
	 *
	 * @param aggregateItem the aggregate item
	 */
	void update(SalaryAggregateItem aggregateItem);

	/**
	 * Removes the.
	 *
	 * @param aggregateItem the aggregate item
	 */
	void remove(SalaryAggregateItem aggregateItem);

	/**
	 * Find by code.
	 *
	 * @param companyCode the company code
	 * @param code the code
	 * @return the salary aggregate item
	 */
	Optional<SalaryAggregateItem> findByCode(String companyCode, String aggregateItemCode, int categoryCode);

	/**
	 * Find all.
	 *
	 * @param companyCode the company code
	 * @return the list
	 */
	List<SalaryAggregateItem> findAll(String companyCode);
}
