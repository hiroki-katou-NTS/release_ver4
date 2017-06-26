/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.core.dom.insurance.labor.unemployeerate;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

import lombok.Getter;
import lombok.Setter;
import nts.arc.layer.dom.DomainObject;
import nts.arc.time.YearMonth;
import nts.gul.text.IdentifierUtil;
import nts.uk.ctx.pr.core.dom.insurance.MonthRange;

/**
 * The Class UnemployeeInsuranceRate.
 */
@Getter
public class UnemployeeInsuranceRate extends DomainObject {

	/** The history id. */
	private String historyId;

	/** The company code. */
	private String companyCode;

	/** The apply range. */
	@Setter
	private MonthRange applyRange;

	/** The rate items. */
	@Setter
	private Set<UnemployeeInsuranceRateItem> rateItems;

	/**
	 * Instantiates a new unemployee insurance rate.
	 */
	private UnemployeeInsuranceRate() {
		super();
		this.historyId = IdentifierUtil.randomUniqueId();
	}

	// =================== Memento State Support Method ===================
	/**
	 * Instantiates a new unemployee insurance rate.
	 *
	 * @param memento
	 *            the memento
	 */
	public UnemployeeInsuranceRate(UnemployeeInsuranceRateGetMemento memento) {
		this.historyId = memento.getHistoryId();
		this.companyCode = memento.getCompanyCode();
		this.applyRange = memento.getApplyRange();
		this.rateItems = memento.getRateItems();
	}

	/**
	 * Save to memento.
	 *
	 * @param memento
	 *            the memento
	 */
	public void saveToMemento(UnemployeeInsuranceRateSetMemento memento) {
		memento.setHistoryId(this.historyId);
		memento.setCompanyCode(this.companyCode);
		memento.setApplyRange(this.applyRange);
		memento.setRateItems(this.rateItems);
	}

	/**
	 * Copy with date.
	 *
	 * @param start
	 *            the start
	 * @return the unemployee insurance rate
	 */
	public UnemployeeInsuranceRate copyWithDate(YearMonth start) {
		UnemployeeInsuranceRate newDomain = new UnemployeeInsuranceRate();
		newDomain.companyCode = this.companyCode;
		newDomain.applyRange = MonthRange.range(start, this.applyRange.getEndMonth());
		newDomain.rateItems = this.rateItems;
		return newDomain;
	}

	/**
	 * Creates the with intial.
	 *
	 * @param companyCode
	 *            the company code
	 * @param startYearMonth
	 *            the start year month
	 * @return the unemployee insurance rate
	 */
	public static final UnemployeeInsuranceRate createWithIntial(String companyCode,
			YearMonth startYearMonth) {
		// Create domain
		UnemployeeInsuranceRate domain = new UnemployeeInsuranceRate();

		// Set data
		domain.companyCode = companyCode;
		domain.applyRange = MonthRange.toMaxDate(startYearMonth);
		domain.rateItems = Arrays
				.asList(UnemployeeInsuranceRateItem.valueIntial(CareerGroup.Agroforestry),
						UnemployeeInsuranceRateItem.valueIntial(CareerGroup.Contruction),
						UnemployeeInsuranceRateItem.valueIntial(CareerGroup.Other))
				.stream().collect(Collectors.toSet());

		// Return
		return domain;
	}

	/**
	 * Gets the start.
	 *
	 * @return the start
	 */
	public YearMonth getStart() {
		return this.applyRange.getStartMonth();
	}

	/**
	 * Gets the end.
	 *
	 * @return the end
	 */
	public YearMonth getEnd() {
		return this.applyRange.getEndMonth();
	}

	/**
	 * Sets the start.
	 *
	 * @param yearMonth
	 *            the new start
	 */
	public void setStart(YearMonth yearMonth) {
		this.applyRange = MonthRange.range(yearMonth, this.applyRange.getEndMonth());
	}

	/**
	 * Sets the end.
	 *
	 * @param yearMonth
	 *            the new end
	 */
	public void setEnd(YearMonth yearMonth) {
		this.applyRange = MonthRange.range(this.applyRange.getStartMonth(), yearMonth);
	}

	/**
	 * Sets the max date.
	 */
	public void setMaxDate() {
		this.applyRange = MonthRange.toMaxDate(this.getApplyRange().getStartMonth());
	}

}
