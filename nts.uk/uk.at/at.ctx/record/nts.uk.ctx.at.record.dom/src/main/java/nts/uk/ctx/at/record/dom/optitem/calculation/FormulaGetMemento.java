/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.at.record.dom.optitem.calculation;

import nts.uk.ctx.at.record.dom.optitem.OptionalItemNo;
import nts.uk.ctx.at.shared.dom.common.CompanyId;

/**
 * The Interface FormulaGetMemento.
 */
public interface FormulaGetMemento {

	/**
	 * Gets the company id.
	 *
	 * @return the company id
	 */
	CompanyId getCompanyId();

	/**
	 * Gets the formula id.
	 *
	 * @return the formula id
	 */
	FormulaId getFormulaId();

	/**
	 * Gets the optional item no.
	 *
	 * @return the optional item no
	 */
	OptionalItemNo getOptionalItemNo();

	/**
	 * Gets the formula name.
	 *
	 * @return the formula name
	 */
	FormulaName getFormulaName();

	/**
	 * Gets the calc formula setting.
	 *
	 * @return the calc formula setting
	 */
	CalcFormulaSetting getCalcFormulaSetting();

	/**
	 * Gets the calc formula atr.
	 *
	 * @return the calc formula atr
	 */
	FormulaAtr getCalcFormulaAtr();

	/**
	 * Gets the symbol.
	 *
	 * @return the symbol
	 */
	Symbol getSymbol();

	/**
	 * Gets the monthly rounding.
	 *
	 * @return the monthly rounding
	 */
	Rounding getMonthlyRounding();

	/**
	 * Gets the daily rounding.
	 *
	 * @return the daily rounding
	 */
	Rounding getDailyRounding();
}
