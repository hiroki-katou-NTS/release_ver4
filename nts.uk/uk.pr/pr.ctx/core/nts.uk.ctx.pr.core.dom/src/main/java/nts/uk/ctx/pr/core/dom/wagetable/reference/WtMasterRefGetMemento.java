/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.core.dom.wagetable.reference;

import nts.uk.ctx.pr.core.dom.wagetable.WtElementRefNo;

/**
 * The Interface WageTableMasterRefGetMemento.
 */
public interface WtMasterRefGetMemento {

	/**
	 * Gets the company code.
	 *
	 * @return the company code
	 */
	 String getCompanyCode();

	/**
	 * Gets the ref no.
	 *
	 * @return the ref no
	 */
	 WtElementRefNo getRefNo();

	/**
	 * Gets the ref name.
	 *
	 * @return the ref name
	 */
	 String getRefName();

	/**
	 * Gets the wage ref table.
	 *
	 * @return the wage ref table
	 */
	 String getWageRefTable();

	/**
	 * Gets the wage ref field.
	 *
	 * @return the wage ref field
	 */
	 String getWageRefField();

	/**
	 * Gets the wage ref disp field.
	 *
	 * @return the wage ref disp field
	 */
	 String getWageRefDispField();

	/**
	 * Gets the wage person table.
	 *
	 * @return the wage person table
	 */
	 String getWagePersonTable();

	/**
	 * Gets the wage person field.
	 *
	 * @return the wage person field
	 */
	 String getWagePersonField();

	/**
	 * Gets the wage ref query.
	 *
	 * @return the wage ref query
	 */
	 String getWageRefQuery();

	/**
	 * Gets the wage person query.
	 *
	 * @return the wage person query
	 */
	 String getWagePersonQuery();

}
