/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.core.dom.wagetable;

import java.util.List;
import java.util.Optional;

/**
 * The Interface WageTableHeadRepository.
 */
public interface WtHeadRepository {

	/**
	 * Adds the.
	 *
	 * @param wageTableHead the wage table head
	 */
    void add(WtHead wageTableHead);

	/**
	 * Update.
	 *
	 * @param wageTableHead the wage table head
	 */
    void update(WtHead wageTableHead);

	/**
	 * Removes the.
	 *
	 * @param companyCode the company code
	 * @param wageTableCode the wage table code
	 */
    void remove(String companyCode, String wageTableCode);

	/**
	 * Find all.
	 *
	 * @param companyCode the company code
	 * @return the list
	 */
	List<WtHead> findAll(String companyCode);

	/**
	 * Find by id.
	 *
	 * @param companyCode the company code
	 * @param code the code
	 * @return the optional
	 */
	Optional<WtHead> findByCode(String companyCode, String code);

	/**
	 * Checks if is duplicate code.
	 *
	 * @param companyCode the company code
	 * @param code the code
	 * @return true, if is duplicate code
	 */
	boolean isExistCode(String companyCode, String code);

	/**
	 * Gets the wage table by base date.
	 *
	 * @param baseDate the base date
	 * @return the wage table by base date
	 */
	List<WtHead> getWageTableByBaseMonth(Integer baseMonth);

	/**
	 * Gets the wage table by codes.
	 *
	 * @param codes the codes
	 * @return the wage table by codes
	 */
	List<WtHead> getWageTableByCodes(List<String> codes);

}
