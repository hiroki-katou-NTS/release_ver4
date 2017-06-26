/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.core.dom.rule.employment.unitprice;

import nts.uk.ctx.pr.core.dom.insurance.MonthRange;
import nts.uk.shr.com.primitive.Memo;

/**
 * The Interface UnitPriceHistoryMemento.
 */
public interface UnitPriceHistorySetMemento {

	/**
	 * Sets the id.
	 *
	 * @param id the new id
	 */
	void setId(String id);

	/**
	 * Sets the company code.
	 *
	 * @param companyCode the new company code
	 */
	void setCompanyCode(String companyCode);

	/**
	 * Sets the unit price code.
	 *
	 * @param unitPriceCode the new unit price code
	 */
	void setUnitPriceCode(UnitPriceCode unitPriceCode);

	/**
	 * Sets the unit price name.
	 *
	 * @param unitPriceName the new unit price name
	 */
	void setUnitPriceName(UnitPriceName unitPriceName);

	/**
	 * Sets the apply range.
	 *
	 * @param applyRange the new apply range
	 */
	void setApplyRange(MonthRange applyRange);

	/**
	 * Sets the budget.
	 *
	 * @param budget the new budget
	 */
	void setBudget(Money budget);

	/**
	 * Sets the fix pay setting type.
	 *
	 * @param fixPaySettingType the new fix pay setting type
	 */
	void setFixPaySettingType(SettingType fixPaySettingType);

	/**
	 * Sets the fix pay atr.
	 *
	 * @param fixPayAtr the new fix pay atr
	 */
	void setFixPayAtr(ApplySetting fixPayAtr);

	/**
	 * Sets the fix pay atr monthly.
	 *
	 * @param fixPayAtrMonthly the new fix pay atr monthly
	 */
	void setFixPayAtrMonthly(ApplySetting fixPayAtrMonthly);

	/**
	 * Sets the fix pay atr day month.
	 *
	 * @param fixPayAtrDayMonth the new fix pay atr day month
	 */
	void setFixPayAtrDayMonth(ApplySetting fixPayAtrDayMonth);

	/**
	 * Sets the fix pay atr daily.
	 *
	 * @param fixPayAtrDaily the new fix pay atr daily
	 */
	void setFixPayAtrDaily(ApplySetting fixPayAtrDaily);

	/**
	 * Sets the fix pay atr hourly.
	 *
	 * @param fixPayAtrHourly the new fix pay atr hourly
	 */
	void setFixPayAtrHourly(ApplySetting fixPayAtrHourly);

	/**
	 * Sets the memo.
	 *
	 * @param memo the new memo
	 */
	void setMemo(Memo memo);

}
