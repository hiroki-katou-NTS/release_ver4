/******************************************************************
 * Copyright (c) 2016 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.core.dom.rule.employment.unitprice;

import java.util.List;
import java.util.Optional;

/**
 * The Interface UnitPriceRepository.
 */
public interface UnitPriceRepository {

	/**
	 * Adds the.
	 *
	 * @param unitPrice the unit price
	 */
    void add(UnitPrice unitPrice);

	/**
	 * Update.
	 *
	 * @param unitPrice the unit price
	 */
    void update(UnitPrice unitPrice);

	/**
	 * Removes the.
	 *
	 * @param id the id
	 * @param version the version
	 */
    void remove(String companyCode, UnitPriceCode cUnitpriceCd);

	/**
	 * Find all.
	 *
	 * @param companyCode the company code
	 * @param contractCode the contract code
	 * @return the list
	 */
	List<UnitPrice> findAll(String companyCode);

	/**
	 * Find by id.
	 *
	 * @param id the id
	 * @return the unit price
	 */
	Optional<UnitPrice> findByCode(String companyCode, UnitPriceCode cUnitpriceCd);

	/**
	 * Find by codes.
	 *
	 * @param companyCode the company code
	 * @param unitPriceCodes the unit price codes
	 * @return the list
	 */
	List<UnitPrice> findByCodes(String companyCode, List<String> unitPriceCodes);

	/**
	 * Check duplicate code.
	 *
	 * @param code the code
	 * @return true, if successful
	 */
	boolean isDuplicateCode(String companyCode, UnitPriceCode code);

	/**
	 * Gets the company unit price.
	 *
	 * @param baseDate the base date
	 * @return the company unit price
	 */
	List<UnitPrice> getCompanyUnitPrice(Integer baseDate);
}
