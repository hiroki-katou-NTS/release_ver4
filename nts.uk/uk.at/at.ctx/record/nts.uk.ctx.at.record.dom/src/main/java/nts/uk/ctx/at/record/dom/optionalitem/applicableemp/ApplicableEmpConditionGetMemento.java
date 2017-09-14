/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.at.record.dom.optionalitem.applicableemp;

import java.util.List;

import nts.uk.ctx.at.record.dom.optionalitem.OptionalItemNo;
import nts.uk.ctx.at.shared.dom.common.CompanyId;

/**
 * The Interface ApplicableEmpConditionGetMemento.
 */
public interface ApplicableEmpConditionGetMemento {

	/**
	 * Gets the company id.
	 *
	 * @return the company id
	 */
	CompanyId getCompanyId();

	/**
	 * Gets the optional item no.
	 *
	 * @return the optional item no
	 */
	OptionalItemNo getOptionalItemNo();

	/**
	 * Gets the employment conditions.
	 *
	 * @return the employment conditions
	 */
	List<EmploymentCondition> getEmploymentConditions();
}
