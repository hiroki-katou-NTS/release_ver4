/******************************************************************
 * Copyright (c) 2015 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.core.dom.rule.employment.unitprice;

import nts.uk.ctx.pr.core.dom.insurance.MonthRange;
import nts.uk.shr.com.primitive.Memo;

/**
 * The Interface UnitPriceHistoryMemento.
 */
public interface UnitPriceHistoryGetMemento {

	/**
	 * Gets the id.
	 *
	 * @return the id
	 */
	String getId();

	/**
	 * Gets the company code.
	 *
	 * @return the company code
	 */
	String getCompanyCode();

	/**
	 * Gets the unit price code.
	 *
	 * @return the unit price code
	 */
	UnitPriceCode getUnitPriceCode();

	/**
	 * Gets the apply range.
	 *
	 * @return the apply range
	 */
	MonthRange getApplyRange();

	/**
	 * Gets the budget.
	 *
	 * @return the budget
	 */
	Money getBudget();

	/**
	 * Gets the fix pay setting type.
	 *
	 * @return the fix pay setting type
	 */
	SettingType getFixPaySettingType();

	/**
	 * Gets the fix pay atr.
	 *
	 * @return the fix pay atr
	 */
	ApplySetting getFixPayAtr();

	/**
	 * Gets the fix pay atr monthly.
	 *
	 * @return the fix pay atr monthly
	 */
	ApplySetting getFixPayAtrMonthly();

	/**
	 * Gets the fix pay atr day month.
	 *
	 * @return the fix pay atr day month
	 */
	ApplySetting getFixPayAtrDayMonth();

	/**
	 * Gets the fix pay atr daily.
	 *
	 * @return the fix pay atr daily
	 */
	ApplySetting getFixPayAtrDaily();

	/**
	 * Gets the fix pay atr hourly.
	 *
	 * @return the fix pay atr hourly
	 */
	ApplySetting getFixPayAtrHourly();

	/**
	 * Gets the memo.
	 *
	 * @return the memo
	 */
	Memo getMemo();

}
