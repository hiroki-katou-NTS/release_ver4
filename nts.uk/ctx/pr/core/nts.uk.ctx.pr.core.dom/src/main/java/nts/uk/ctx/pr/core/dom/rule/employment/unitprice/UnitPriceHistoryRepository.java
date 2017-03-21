/******************************************************************
 * Copyright (c) 2016 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.core.dom.rule.employment.unitprice;

import java.util.List;

import nts.uk.ctx.core.dom.company.CompanyCode;
import nts.uk.ctx.pr.core.dom.base.simplehistory.SimpleHistoryRepository;

/**
 * The Interface UnitPriceHistoryRepository.
 */
public interface UnitPriceHistoryRepository extends SimpleHistoryRepository<UnitPriceHistory> {
	
	/**
	 * Find all.
	 *
	 * @param companyCode the company code
	 * @return the list
	 */
	List<UnitPriceHistory> findAll(CompanyCode companyCode);

	/**
	 * Checks if is duplicate code.
	 *
	 * @param companyCode the company code
	 * @param unitPriceCode the unit price code
	 * @return true, if is duplicate code
	 */
	boolean isDuplicateCode(CompanyCode companyCode, UnitPriceCode unitPriceCode);
}
