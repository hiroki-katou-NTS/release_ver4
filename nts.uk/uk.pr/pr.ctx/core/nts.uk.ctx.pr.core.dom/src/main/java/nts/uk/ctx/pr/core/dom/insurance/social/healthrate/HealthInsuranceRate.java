/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.core.dom.insurance.social.healthrate;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

import lombok.Getter;
import lombok.Setter;
import nts.arc.error.BusinessException;
import nts.arc.layer.dom.DomainObject;
import nts.arc.primitive.PrimitiveValue;
import nts.arc.time.YearMonth;
import nts.gul.text.IdentifierUtil;
import nts.uk.ctx.pr.core.dom.base.simplehistory.History;
import nts.uk.ctx.pr.core.dom.insurance.CalculateMethod;
import nts.uk.ctx.pr.core.dom.insurance.CommonAmount;
import nts.uk.ctx.pr.core.dom.insurance.Ins3Rate;
import nts.uk.ctx.pr.core.dom.insurance.MonthRange;
import nts.uk.ctx.pr.core.dom.insurance.OfficeCode;
import nts.uk.ctx.pr.core.dom.insurance.PaymentType;
import nts.uk.ctx.pr.core.dom.insurance.RoundingItem;
import nts.uk.ctx.pr.core.dom.insurance.RoundingMethod;

/**
 * The Class HealthInsuranceRate.
 */
@Getter
public class HealthInsuranceRate extends DomainObject implements History<HealthInsuranceRate> {

	/** The history id. */
	private String historyId;

	/** The company code. */
	private String companyCode;

	/** The office code. */
	private OfficeCode officeCode;

	/** The apply range. */
	private MonthRange applyRange;

	/** The auto calculate. */
	@Setter
	private CalculateMethod autoCalculate;

	/** The max amount. */
	@Setter
	private CommonAmount maxAmount;

	/** The rate items. */
	@Setter
	private Set<InsuranceRateItem> rateItems;

	/** The rounding methods. */
	@Setter
	private Set<HealthInsuranceRounding> roundingMethods;

	/**
	 * Instantiates a new health insurance rate.
	 */
	private HealthInsuranceRate() {
		this.historyId = IdentifierUtil.randomUniqueId();
	};

	// =================== Memento State Support Method ===================
	/**
	 * Instantiates a new health insurance rate.
	 *
	 * @param memento
	 *            the memento
	 */
	public HealthInsuranceRate(HealthInsuranceRateGetMemento memento) {
		this.historyId = memento.getHistoryId();
		this.companyCode = memento.getCompanyCode();
		this.officeCode = memento.getOfficeCode();
		this.applyRange = memento.getApplyRange();
		this.autoCalculate = memento.getAutoCalculate();
		this.maxAmount = memento.getMaxAmount();
		this.rateItems = memento.getRateItems();
		this.roundingMethods = memento.getRoundingMethods();
	}

	/**
	 * Save to memento.
	 *
	 * @param memento
	 *            the memento
	 */
	public void saveToMemento(HealthInsuranceRateSetMemento memento) {
		memento.setHistoryId(this.historyId);
		memento.setCompanyCode(this.companyCode);
		memento.setOfficeCode(this.officeCode);
		memento.setApplyRange(this.applyRange);
		memento.setAutoCalculate(this.autoCalculate);
		memento.setMaxAmount(this.maxAmount);
		memento.setRateItems(this.rateItems);
		memento.setRoundingMethods(this.roundingMethods);
	}

	/**
	 * Creates the with intial.
	 *
	 * @param companyCode
	 *            the company code
	 * @param officeCode
	 *            the office code
	 * @param startYearMonth
	 *            the start year month
	 * @return the health insurance rate
	 */
	public static final HealthInsuranceRate createWithIntial(String companyCode,
			OfficeCode officeCode, YearMonth startYearMonth) {
		// Create domain
		HealthInsuranceRate domain = new HealthInsuranceRate();

		// Set data
		domain.companyCode = companyCode;
		domain.officeCode = officeCode;
		domain.applyRange = MonthRange.toMaxDate(startYearMonth);
		domain.autoCalculate = CalculateMethod.Auto;
		domain.maxAmount = new CommonAmount(BigDecimal.ZERO);
		domain.rateItems = setDefaultRateItems();
		domain.roundingMethods = setDefaultRounding();

		// Return
		return domain;
	}

	/**
	 * Sets the default rate items.
	 *
	 * @return the sets the
	 */
	private static Set<InsuranceRateItem> setDefaultRateItems() {
		Ins3Rate insRate = new Ins3Rate(BigDecimal.ZERO);
		HealthChargeRateItem chargeRate = new HealthChargeRateItem(insRate, insRate);

		// Return
		return Arrays.asList(
				new InsuranceRateItem(PaymentType.Salary, HealthInsuranceType.Basic, chargeRate),
				new InsuranceRateItem(PaymentType.Salary, HealthInsuranceType.General, chargeRate),
				new InsuranceRateItem(PaymentType.Salary, HealthInsuranceType.Nursing, chargeRate),
				new InsuranceRateItem(PaymentType.Salary, HealthInsuranceType.Special, chargeRate),
				new InsuranceRateItem(PaymentType.Bonus, HealthInsuranceType.Basic, chargeRate),
				new InsuranceRateItem(PaymentType.Bonus, HealthInsuranceType.General, chargeRate),
				new InsuranceRateItem(PaymentType.Bonus, HealthInsuranceType.Nursing, chargeRate),
				new InsuranceRateItem(PaymentType.Bonus, HealthInsuranceType.Special, chargeRate))
				.stream().collect(Collectors.toSet());
	}

	/**
	 * Sets the default rounding.
	 *
	 * @return the sets the
	 */
	private static Set<HealthInsuranceRounding> setDefaultRounding() {
		RoundingItem defRoundingItem = new RoundingItem(RoundingMethod.Truncation,
				RoundingMethod.Truncation);

		// Return
		return Arrays
				.asList(new HealthInsuranceRounding(PaymentType.Salary, defRoundingItem),
						new HealthInsuranceRounding(PaymentType.Bonus, defRoundingItem))
				.stream().collect(Collectors.toSet());
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
		result = prime * result + ((historyId == null) ? 0 : historyId.hashCode());
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
		if (!(obj instanceof HealthInsuranceRate))
			return false;
		HealthInsuranceRate other = (HealthInsuranceRate) obj;
		if (historyId == null) {
			if (other.historyId != null)
				return false;
		} else if (!historyId.equals(other.historyId))
			return false;
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.pr.core.dom.base.simplehistory.History#getUuid()
	 */
	@Override
	public String getUuid() {
		return this.historyId;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.pr.core.dom.base.simplehistory.History#getMasterCode()
	 */
	@Override
	public PrimitiveValue<String> getMasterCode() {
		return this.officeCode;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.pr.core.dom.base.simplehistory.History#getStart()
	 */
	@Override
	public YearMonth getStart() {
		return this.getApplyRange().getStartMonth();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.pr.core.dom.base.simplehistory.History#getEnd()
	 */
	@Override
	public YearMonth getEnd() {
		return this.getApplyRange().getEndMonth();
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
	public HealthInsuranceRate copyWithDate(YearMonth start) {
		// check if duplicate start date
		if (this.applyRange.getStartMonth().equals(start)) {
			throw new BusinessException("ER011");
		}

		// Create domain
		HealthInsuranceRate domain = new HealthInsuranceRate();

		// Set data
		domain.applyRange = MonthRange.toMaxDate(start);
		domain.autoCalculate = this.autoCalculate;
		domain.companyCode = this.companyCode;
		domain.maxAmount = this.maxAmount;
		domain.officeCode = this.officeCode;
		domain.rateItems = this.rateItems;
		domain.roundingMethods = this.roundingMethods;

		// Return
		return domain;
	}
}
