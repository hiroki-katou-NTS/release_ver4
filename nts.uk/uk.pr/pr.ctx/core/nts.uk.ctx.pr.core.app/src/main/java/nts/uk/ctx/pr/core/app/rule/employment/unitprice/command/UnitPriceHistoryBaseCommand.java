/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.core.app.rule.employment.unitprice.command;

import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;
import nts.arc.time.YearMonth;
import nts.uk.ctx.pr.core.dom.insurance.MonthRange;
import nts.uk.ctx.pr.core.dom.rule.employment.unitprice.ApplySetting;
import nts.uk.ctx.pr.core.dom.rule.employment.unitprice.Money;
import nts.uk.ctx.pr.core.dom.rule.employment.unitprice.SettingType;
import nts.uk.ctx.pr.core.dom.rule.employment.unitprice.UnitPriceCode;
import nts.uk.ctx.pr.core.dom.rule.employment.unitprice.UnitPriceGetMemento;
import nts.uk.ctx.pr.core.dom.rule.employment.unitprice.UnitPriceHistoryGetMemento;
import nts.uk.ctx.pr.core.dom.rule.employment.unitprice.UnitPriceName;
import nts.uk.shr.com.context.AppContexts;
import nts.uk.shr.com.primitive.Memo;

/**
 * The Class UnitPriceHistoryBaseCommand.
 */
@Getter
@Setter
public class UnitPriceHistoryBaseCommand
		implements UnitPriceGetMemento, UnitPriceHistoryGetMemento {

	/** The id. */
	private String id;

	/** The unit price code. */
	private String unitPriceCode;

	/** The unit price name. */
	private String unitPriceName;

	/** The start month. */
	private int startMonth;

	/** The end month. */
	private int endMonth;

	/** The budget. */
	private BigDecimal budget;

	/** The fix pay setting type. */
	private SettingType fixPaySettingType;

	/** The fix pay atr. */
	private ApplySetting fixPayAtr;

	/** The fix pay atr monthly. */
	private ApplySetting fixPayAtrMonthly;

	/** The fix pay atr day month. */
	private ApplySetting fixPayAtrDayMonth;

	/** The fix pay atr daily. */
	private ApplySetting fixPayAtrDaily;

	/** The fix pay atr hourly. */
	private ApplySetting fixPayAtrHourly;

	/** The memo. */
	private String memo;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nts.uk.ctx.pr.core.dom.rule.employment.unitprice.UnitPriceGetMemento#
	 * getCompanyCode()
	 */
	@Override
	public String getCompanyCode() {
		return AppContexts.user().companyCode();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nts.uk.ctx.pr.core.dom.rule.employment.unitprice.UnitPriceGetMemento#
	 * getCode()
	 */
	@Override
	public UnitPriceCode getCode() {
		return new UnitPriceCode(this.unitPriceCode);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nts.uk.ctx.pr.core.dom.rule.employment.unitprice.UnitPriceGetMemento#
	 * getName()
	 */
	@Override
	public UnitPriceName getName() {
		return new UnitPriceName(this.unitPriceName);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.pr.core.dom.rule.employment.unitprice.
	 * UnitPriceHistoryGetMemento#getUnitPriceCode()
	 */
	@Override
	public UnitPriceCode getUnitPriceCode() {
		return this.getCode();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.pr.core.dom.rule.employment.unitprice.
	 * UnitPriceHistoryGetMemento#getApplyRange()
	 */
	@Override
	public MonthRange getApplyRange() {
		return MonthRange.range(new YearMonth(this.startMonth), new YearMonth(this.endMonth));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.pr.core.dom.rule.employment.unitprice.
	 * UnitPriceHistoryGetMemento#getBudget()
	 */
	@Override
	public Money getBudget() {
		return new Money(this.budget);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.pr.core.dom.rule.employment.unitprice.
	 * UnitPriceHistoryGetMemento#getFixPaySettingType()
	 */
	@Override
	public SettingType getFixPaySettingType() {
		return this.fixPaySettingType;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.pr.core.dom.rule.employment.unitprice.
	 * UnitPriceHistoryGetMemento#getFixPayAtr()
	 */
	@Override
	public ApplySetting getFixPayAtr() {
		return this.fixPayAtr;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.pr.core.dom.rule.employment.unitprice.
	 * UnitPriceHistoryGetMemento#getFixPayAtrMonthly()
	 */
	@Override
	public ApplySetting getFixPayAtrMonthly() {
		return this.fixPayAtrMonthly;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.pr.core.dom.rule.employment.unitprice.
	 * UnitPriceHistoryGetMemento#getFixPayAtrDayMonth()
	 */
	@Override
	public ApplySetting getFixPayAtrDayMonth() {
		return this.fixPayAtrDayMonth;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.pr.core.dom.rule.employment.unitprice.
	 * UnitPriceHistoryGetMemento#getFixPayAtrDaily()
	 */
	@Override
	public ApplySetting getFixPayAtrDaily() {
		return this.fixPayAtrDaily;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.pr.core.dom.rule.employment.unitprice.
	 * UnitPriceHistoryGetMemento#getFixPayAtrHourly()
	 */
	@Override
	public ApplySetting getFixPayAtrHourly() {
		return this.fixPayAtrHourly;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.pr.core.dom.rule.employment.unitprice.
	 * UnitPriceHistoryGetMemento#getMemo()
	 */
	@Override
	public Memo getMemo() {
		if (this.memo != null) {
			return new Memo(this.memo);
		}
		return null;
	}
}
