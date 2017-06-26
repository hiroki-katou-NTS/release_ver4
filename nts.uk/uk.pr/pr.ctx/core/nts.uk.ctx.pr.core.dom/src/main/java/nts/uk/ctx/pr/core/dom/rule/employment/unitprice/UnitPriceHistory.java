/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.core.dom.rule.employment.unitprice;

import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;
import nts.arc.layer.dom.AggregateRoot;
import nts.arc.primitive.PrimitiveValue;
import nts.arc.time.YearMonth;
import nts.gul.text.IdentifierUtil;
import nts.uk.ctx.pr.core.dom.base.simplehistory.History;
import nts.uk.ctx.pr.core.dom.insurance.MonthRange;
import nts.uk.shr.com.primitive.Memo;

/**
 * The Class UnitPriceHistory.
 */
@Getter
public class UnitPriceHistory extends AggregateRoot implements History<UnitPriceHistory> {

	/** The id. */
	// UuId
	private String id;

	/** The company code. */
	private String companyCode;

	/** The unit price code. */
	private UnitPriceCode unitPriceCode;

	/** The apply range. */
	@Setter
	private MonthRange applyRange;

	/** The budget. */
	@Setter
	private Money budget;

	/** The fix pay setting type. */
	@Setter
	private SettingType fixPaySettingType;

	/** The fix pay atr. */
	@Setter
	private ApplySetting fixPayAtr;

	/** The fix pay atr monthly. */
	@Setter
	private ApplySetting fixPayAtrMonthly;

	/** The fix pay atr day month. */
	@Setter
	private ApplySetting fixPayAtrDayMonth;

	/** The fix pay atr daily. */
	@Setter
	private ApplySetting fixPayAtrDaily;

	/** The fix pay atr hourly. */
	@Setter
	private ApplySetting fixPayAtrHourly;

	/** The memo. */
	@Setter
	private Memo memo;

	/**
	 * Instantiates a new unit price history.
	 */
	private UnitPriceHistory() {
		this.id = IdentifierUtil.randomUniqueId();
	};

	// =================== Memento State Support Method ===================
	/**
	 * Instantiates a new unit price history.
	 *
	 * @param memento
	 *            the memento
	 */
	public UnitPriceHistory(UnitPriceHistoryGetMemento memento) {
		this.id = memento.getId();
		this.companyCode = memento.getCompanyCode();
		this.unitPriceCode = memento.getUnitPriceCode();
		this.applyRange = memento.getApplyRange();
		this.budget = memento.getBudget();
		this.fixPaySettingType = memento.getFixPaySettingType();
		this.fixPayAtr = memento.getFixPayAtr();
		this.fixPayAtrMonthly = memento.getFixPayAtrMonthly();
		this.fixPayAtrDayMonth = memento.getFixPayAtrDayMonth();
		this.fixPayAtrDaily = memento.getFixPayAtrDaily();
		this.fixPayAtrHourly = memento.getFixPayAtrHourly();
		this.memo = memento.getMemo();
	}

	/**
	 * Save to memento.
	 *
	 * @param memento
	 *            the memento
	 */
	public void saveToMemento(UnitPriceHistorySetMemento memento) {
		memento.setId(this.id);
		memento.setCompanyCode(this.companyCode);
		memento.setUnitPriceCode(this.unitPriceCode);
		memento.setApplyRange(this.applyRange);
		memento.setBudget(this.budget);
		memento.setFixPaySettingType(this.fixPaySettingType);
		memento.setFixPayAtr(this.fixPayAtr);
		memento.setFixPayAtrMonthly(this.fixPayAtrMonthly);
		memento.setFixPayAtrDayMonth(this.fixPayAtrDayMonth);
		memento.setFixPayAtrDaily(this.fixPayAtrDaily);
		memento.setFixPayAtrHourly(this.fixPayAtrHourly);
		memento.setMemo(this.memo);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.pr.core.dom.base.simplehistory.History#getUuid()
	 */
	@Override
	public String getUuid() {
		return this.id;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.pr.core.dom.base.simplehistory.History#getMasterCode()
	 */
	@Override
	public PrimitiveValue<String> getMasterCode() {
		return this.unitPriceCode;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.pr.core.dom.base.simplehistory.History#getStart()
	 */
	@Override
	public YearMonth getStart() {
		return this.applyRange.getStartMonth();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.pr.core.dom.base.simplehistory.History#getEnd()
	 */
	@Override
	public YearMonth getEnd() {
		return this.applyRange.getEndMonth();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nts.uk.ctx.pr.core.dom.base.simplehistory.History#setStart(nts.arc.time.
	 * YearMonth)
	 */
	@Override
	public void setStart(YearMonth yearMonth) {
		this.applyRange = MonthRange.range(yearMonth, this.applyRange.getEndMonth());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nts.uk.ctx.pr.core.dom.base.simplehistory.History#setEnd(nts.arc.time.
	 * YearMonth)
	 */
	@Override
	public void setEnd(YearMonth yearMonth) {
		this.applyRange = MonthRange.range(this.applyRange.getStartMonth(), yearMonth);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nts.uk.ctx.pr.core.dom.base.simplehistory.History#copyWithDate(nts.arc.
	 * time.YearMonth)
	 */
	@Override
	public UnitPriceHistory copyWithDate(YearMonth start) {
		UnitPriceHistory newHistory = new UnitPriceHistory();
		newHistory.companyCode = this.companyCode;
		newHistory.unitPriceCode = this.unitPriceCode;
		newHistory.applyRange = MonthRange.toMaxDate(start);
		newHistory.budget = this.budget;
		newHistory.fixPaySettingType = this.fixPaySettingType;
		newHistory.fixPayAtr = this.fixPayAtr;
		newHistory.fixPayAtrMonthly = this.fixPayAtrMonthly;
		newHistory.fixPayAtrDayMonth = this.fixPayAtrDayMonth;
		newHistory.fixPayAtrDaily = this.fixPayAtrDaily;
		newHistory.fixPayAtrHourly = this.fixPayAtrHourly;
		newHistory.memo = this.memo;
		return newHistory;
	}

	/**
	 * Creates the with intial.
	 *
	 * @param companyCode
	 *            the company code
	 * @param unitPriceCode
	 *            the unit price code
	 * @return the unit price history
	 */
	public static final UnitPriceHistory createWithIntial(String companyCode,
			UnitPriceCode unitPriceCode, YearMonth startYearMonth) {
		UnitPriceHistory history = new UnitPriceHistory();
		history.companyCode = companyCode;
		history.unitPriceCode = unitPriceCode;
		history.applyRange = MonthRange.toMaxDate(startYearMonth);
		history.budget = new Money(BigDecimal.ZERO);
		history.fixPaySettingType = SettingType.Company;
		history.fixPayAtr = ApplySetting.Apply;
		history.fixPayAtrMonthly = ApplySetting.Apply;
		history.fixPayAtrDayMonth = ApplySetting.Apply;
		history.fixPayAtrDaily = ApplySetting.Apply;
		history.fixPayAtrHourly = ApplySetting.Apply;
		history.memo = null;
		return history;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		UnitPriceHistory other = (UnitPriceHistory) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
}
