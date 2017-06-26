/******************************************************************
 * Copyright (c) 2015 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.core.infra.repository.rule.employment.unitprice;

import nts.arc.time.YearMonth;
import nts.uk.ctx.pr.core.dom.insurance.MonthRange;
import nts.uk.ctx.pr.core.dom.rule.employment.unitprice.ApplySetting;
import nts.uk.ctx.pr.core.dom.rule.employment.unitprice.Money;
import nts.uk.ctx.pr.core.dom.rule.employment.unitprice.SettingType;
import nts.uk.ctx.pr.core.dom.rule.employment.unitprice.UnitPriceCode;
import nts.uk.ctx.pr.core.dom.rule.employment.unitprice.UnitPriceHistoryGetMemento;
import nts.uk.ctx.pr.core.infra.entity.rule.employment.unitprice.QupmtCUnitpriceDetail;
import nts.uk.shr.com.primitive.Memo;

/**
 * The Class JpaUnitPriceHistoryGetMemento.
 */
public class JpaUnitPriceHistoryGetMemento implements UnitPriceHistoryGetMemento {

	/** The type value. */
	private QupmtCUnitpriceDetail typeValue;

	/**
	 * Instantiates a new jpa unit price history get memento.
	 *
	 * @param typeValue
	 *            the type value
	 */
	public JpaUnitPriceHistoryGetMemento(QupmtCUnitpriceDetail typeValue) {
		this.typeValue = typeValue;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.pr.core.dom.rule.employment.unitprice.
	 * UnitPriceHistoryGetMemento#getId()
	 */
	@Override
	public String getId() {
		return this.typeValue.getQupmtCUnitpriceDetailPK().getHistId();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.pr.core.dom.rule.employment.unitprice.
	 * UnitPriceHistoryGetMemento#getCompanyCode()
	 */
	@Override
	public String getCompanyCode() {
		return this.typeValue.getQupmtCUnitpriceDetailPK().getCcd();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.pr.core.dom.rule.employment.unitprice.
	 * UnitPriceHistoryGetMemento#getUnitPriceCode()
	 */
	@Override
	public UnitPriceCode getUnitPriceCode() {
		return new UnitPriceCode(this.typeValue.getQupmtCUnitpriceDetailPK().getCUnitpriceCd());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.pr.core.dom.rule.employment.unitprice.
	 * UnitPriceHistoryGetMemento#getApplyRange()
	 */
	@Override
	public MonthRange getApplyRange() {
		return MonthRange.range(new YearMonth(this.typeValue.getStrYm()),
				new YearMonth(this.typeValue.getEndYm()));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.pr.core.dom.rule.employment.unitprice.
	 * UnitPriceHistoryGetMemento#getBudget()
	 */
	@Override
	public Money getBudget() {
		return new Money(this.typeValue.getCUnitprice());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.pr.core.dom.rule.employment.unitprice.
	 * UnitPriceHistoryGetMemento#getFixPaySettingType()
	 */
	@Override
	public SettingType getFixPaySettingType() {
		return SettingType.valueOf(this.typeValue.getFixPaySet());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.pr.core.dom.rule.employment.unitprice.
	 * UnitPriceHistoryGetMemento#getFixPayAtr()
	 */
	@Override
	public ApplySetting getFixPayAtr() {
		return ApplySetting.valueOf(this.typeValue.getFixPayAtr());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.pr.core.dom.rule.employment.unitprice.
	 * UnitPriceHistoryGetMemento#getFixPayAtrMonthly()
	 */
	@Override
	public ApplySetting getFixPayAtrMonthly() {
		return ApplySetting.valueOf(this.typeValue.getFixPayAtrMonthly());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.pr.core.dom.rule.employment.unitprice.
	 * UnitPriceHistoryGetMemento#getFixPayAtrDayMonth()
	 */
	@Override
	public ApplySetting getFixPayAtrDayMonth() {
		return ApplySetting.valueOf(this.typeValue.getFixPayAtrDaymonth());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.pr.core.dom.rule.employment.unitprice.
	 * UnitPriceHistoryGetMemento#getFixPayAtrDaily()
	 */
	@Override
	public ApplySetting getFixPayAtrDaily() {
		return ApplySetting.valueOf(this.typeValue.getFixPayAtrDaily());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.pr.core.dom.rule.employment.unitprice.
	 * UnitPriceHistoryGetMemento#getFixPayAtrHourly()
	 */
	@Override
	public ApplySetting getFixPayAtrHourly() {
		return ApplySetting.valueOf(this.typeValue.getFixPayAtrHourly());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.pr.core.dom.rule.employment.unitprice.
	 * UnitPriceHistoryGetMemento#getMemo()
	 */
	@Override
	public Memo getMemo() {
		return new Memo(this.typeValue.getMemo());
	}

}
