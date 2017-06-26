/******************************************************************
 * Copyright (c) 2015 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.core.dom.rule.employment.unitprice;

/**
 * The Interface UnitPriceMemento.
 */
public interface UnitPriceSetMemento {

	/**
	 * Sets the company code.
	 *
	 * @param companyCode the new company code
	 */
	void setCompanyCode(String companyCode);

	/**
	 * Sets the code.
	 *
	 * @param code the new code
	 */
	void setCode(UnitPriceCode code);

	/**
	 * Sets the name.
	 *
	 * @param name the new name
	 */
	void setName(UnitPriceName name);

}
