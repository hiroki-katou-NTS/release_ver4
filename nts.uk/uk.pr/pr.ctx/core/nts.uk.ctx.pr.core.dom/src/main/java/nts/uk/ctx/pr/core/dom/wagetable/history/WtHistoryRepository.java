/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.core.dom.wagetable.history;

import java.util.List;

import nts.uk.ctx.pr.core.dom.base.simplehistory.SimpleHistoryRepository;

/**
 * The Interface WageTableHistoryRepository.
 */
public interface WtHistoryRepository extends SimpleHistoryRepository<WtHistory> {

	/**
	 * Find all.
	 *
	 * @param companyCode the company code
	 * @return the list
	 */
	List<WtHistory> findAll(String companyCode);

	/**
	 * Checks if is valid date range.
	 *
	 * @param companyCode the company code
	 * @param wageTableCode the wage table code
	 * @param startMonth the start month
	 * @return true, if is valid date range
	 */
	boolean isValidDateRange(String companyCode, String wageTableCode, Integer startMonth) ;
}
