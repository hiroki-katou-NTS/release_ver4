/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.at.record.dom.optionalitem.calculationformula.disporder;

import nts.uk.ctx.at.record.dom.optionalitem.OptionalItemNo;
import nts.uk.ctx.at.record.dom.optionalitem.calculationformula.OptionalItemFormulaId;
import nts.uk.ctx.at.shared.dom.common.CompanyId;

/**
 * The Interface FormulaDispOrderSetMemento.
 */
public interface FormulaDispOrderSetMemento {

	/**
	 * Sets the company id.
	 *
	 * @param comId the new company id
	 */
	void setCompanyId(CompanyId comId);

	/**
	 * Sets the optional item no.
	 *
	 * @param optNo the new optional item no
	 */
	void setOptionalItemNo(OptionalItemNo optNo);

	/**
	 * Sets the optional item formula id.
	 *
	 * @param formulaId the new optional item formula id
	 */
	void setOptionalItemFormulaId(OptionalItemFormulaId formulaId);

	/**
	 * Sets the disp order.
	 *
	 * @param dispOrder the new disp order
	 */
	void setDispOrder(int dispOrder);
}
