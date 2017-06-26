/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.core.ws.rule.employment.unitprice.dto;

import java.math.BigDecimal;

import lombok.Builder;
import lombok.Data;
import nts.uk.ctx.pr.core.dom.insurance.MonthRange;
import nts.uk.ctx.pr.core.dom.rule.employment.unitprice.ApplySetting;
import nts.uk.ctx.pr.core.dom.rule.employment.unitprice.Money;
import nts.uk.ctx.pr.core.dom.rule.employment.unitprice.SettingType;
import nts.uk.ctx.pr.core.dom.rule.employment.unitprice.UnitPriceCode;
import nts.uk.ctx.pr.core.dom.rule.employment.unitprice.UnitPriceHistorySetMemento;
import nts.uk.ctx.pr.core.dom.rule.employment.unitprice.UnitPriceName;
import nts.uk.shr.com.primitive.Memo;

/**
 * The Class UnitPriceHistoryDto.
 */
@Builder
@Data
public class UnitPriceHistoryModel implements UnitPriceHistorySetMemento {

	/** The id. */
	public String id;

	/** The unit price code. */
	public String unitPriceCode;

	/** The unit price name. */
	public String unitPriceName;

	/** The start month. */
	public Integer startMonth;

	/** The end month. */
	public Integer endMonth;

	/** The budget. */
	public BigDecimal budget;

	/** The fix pay setting type. */
	public SettingType fixPaySettingType;

	/** The fix pay atr. */
	public ApplySetting fixPayAtr;

	/** The fix pay atr monthly. */
	public ApplySetting fixPayAtrMonthly;

	/** The fix pay atr day month. */
	public ApplySetting fixPayAtrDayMonth;

	/** The fix pay atr daily. */
	public ApplySetting fixPayAtrDaily;

	/** The fix pay atr hourly. */
	public ApplySetting fixPayAtrHourly;

	/** The memo. */
	public String memo;

	/*
	 * (non-Javadoc)
	 *
	 * @see nts.uk.ctx.pr.core.dom.rule.employment.unitprice.
	 * UnitPriceHistorySetMemento#setId(java.lang.String)
	 */
	@Override
	public void setId(String id) {
		this.id = id;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see nts.uk.ctx.pr.core.dom.rule.employment.unitprice.
	 * UnitPriceHistorySetMemento#setCompanyCode(nts.uk.ctx.core.dom.company.
	 * CompanyCode)
	 */
	@Override
	public void setCompanyCode(String companyCode) {
		// Do nothing.
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see nts.uk.ctx.pr.core.dom.rule.employment.unitprice.
	 * UnitPriceHistorySetMemento#setUnitPriceCode(nts.uk.ctx.pr.core.dom.rule.
	 * employment.unitprice.UnitPriceCode)
	 */
	@Override
	public void setUnitPriceCode(UnitPriceCode unitPriceCode) {
		this.unitPriceCode = unitPriceCode.v();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see nts.uk.ctx.pr.core.dom.rule.employment.unitprice.
	 * UnitPriceHistorySetMemento#setApplyRange(nts.uk.ctx.pr.core.dom.insurance
	 * .MonthRange)
	 */
	@Override
	public void setApplyRange(MonthRange applyRange) {
		this.startMonth = applyRange.getStartMonth().v();
		this.endMonth = applyRange.getEndMonth().v();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see nts.uk.ctx.pr.core.dom.rule.employment.unitprice.
	 * UnitPriceHistorySetMemento#setBudget(nts.uk.ctx.pr.core.dom.rule.
	 * employment.unitprice.Money)
	 */
	@Override
	public void setBudget(Money budget) {
		this.budget = budget.v();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see nts.uk.ctx.pr.core.dom.rule.employment.unitprice.
	 * UnitPriceHistorySetMemento#setFixPaySettingType(nts.uk.ctx.pr.core.dom.
	 * rule.employment.unitprice.SettingType)
	 */
	@Override
	public void setFixPaySettingType(SettingType fixPaySettingType) {
		this.fixPaySettingType = fixPaySettingType;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see nts.uk.ctx.pr.core.dom.rule.employment.unitprice.
	 * UnitPriceHistorySetMemento#setFixPayAtr(nts.uk.ctx.pr.core.dom.rule.
	 * employment.unitprice.ApplySetting)
	 */
	@Override
	public void setFixPayAtr(ApplySetting fixPayAtr) {
		this.fixPayAtr = fixPayAtr;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see nts.uk.ctx.pr.core.dom.rule.employment.unitprice.
	 * UnitPriceHistorySetMemento#setFixPayAtrMonthly(nts.uk.ctx.pr.core.dom.
	 * rule.employment.unitprice.ApplySetting)
	 */
	@Override
	public void setFixPayAtrMonthly(ApplySetting fixPayAtrMonthly) {
		this.fixPayAtrMonthly = fixPayAtrMonthly;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see nts.uk.ctx.pr.core.dom.rule.employment.unitprice.
	 * UnitPriceHistorySetMemento#setFixPayAtrDayMonth(nts.uk.ctx.pr.core.dom.
	 * rule.employment.unitprice.ApplySetting)
	 */
	@Override
	public void setFixPayAtrDayMonth(ApplySetting fixPayAtrDayMonth) {
		this.fixPayAtrDayMonth = fixPayAtrDayMonth;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see nts.uk.ctx.pr.core.dom.rule.employment.unitprice.
	 * UnitPriceHistorySetMemento#setFixPayAtrDaily(nts.uk.ctx.pr.core.dom.rule.
	 * employment.unitprice.ApplySetting)
	 */
	@Override
	public void setFixPayAtrDaily(ApplySetting fixPayAtrDaily) {
		this.fixPayAtrDaily = fixPayAtrDaily;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see nts.uk.ctx.pr.core.dom.rule.employment.unitprice.
	 * UnitPriceHistorySetMemento#setFixPayAtrHourly(nts.uk.ctx.pr.core.dom.rule
	 * .employment.unitprice.ApplySetting)
	 */
	@Override
	public void setFixPayAtrHourly(ApplySetting fixPayAtrHourly) {
		this.fixPayAtrHourly = fixPayAtrHourly;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see nts.uk.ctx.pr.core.dom.rule.employment.unitprice.
	 * UnitPriceHistorySetMemento#setMemo(nts.uk.shr.com.primitive.Memo)
	 */
	@Override
	public void setMemo(Memo memo) {
		this.memo = memo.v();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see nts.uk.ctx.pr.core.dom.rule.employment.unitprice.
	 * UnitPriceHistorySetMemento#setUnitPriceName(nts.uk.ctx.pr.core.dom.rule.
	 * employment.unitprice.UnitPriceName)
	 */
	@Override
	public void setUnitPriceName(UnitPriceName unitPriceName) {
		this.unitPriceName = unitPriceName.v();
	}

}
