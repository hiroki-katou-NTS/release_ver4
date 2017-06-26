/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.core.dom.wagetable.reference;

import nts.uk.ctx.pr.core.dom.wagetable.WtElementRefNo;

/**
 * The Interface WageTableCodeRefGetMemento.
 */
public interface WtCodeRefGetMemento {

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
	 * Gets the wage ref value.
	 *
	 * @return the wage ref value
	 */
	String getWageRefValue();

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
	 * Gets the wage person query.
	 *
	 * @return the wage person query
	 */
	String getWagePersonQuery();

}
