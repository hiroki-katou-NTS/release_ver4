/******************************************************************
 * Copyright (c) 2016 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.core.dom.insurance.labor.accidentrate;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

import lombok.Getter;
import nts.arc.layer.dom.DomainObject;
import nts.arc.time.YearMonth;
import nts.gul.text.IdentifierUtil;
import nts.uk.ctx.pr.core.dom.insurance.MonthRange;
import nts.uk.ctx.pr.core.dom.insurance.RoundingMethod;
import nts.uk.ctx.pr.core.dom.insurance.labor.businesstype.BusinessTypeEnum;

/**
 * The Class AccidentInsuranceRate.
 */

@Getter
public class AccidentInsuranceRate extends DomainObject {

	/** The history id. */
	private String historyId;

	/** The company code. */
	private String companyCode;

	/** The apply range. */
	private MonthRange applyRange;

	/** The rate items. */
	private Set<InsuBizRateItem> rateItems;

	/**
	 * Instantiates a new accident insurance rate.
	 */
	private AccidentInsuranceRate() {
		super();
		this.historyId = IdentifierUtil.randomUniqueId();
	}

	// =================== Memento State Support Method ===================

	/**
	 * Instantiates a new accident insurance rate.
	 *
	 * @param memento the memento
	 */
	public AccidentInsuranceRate(AccidentInsuranceRateGetMemento memento) {
		this.historyId = memento.getHistoryId();
		this.companyCode = memento.getCompanyCode();
		this.applyRange = memento.getApplyRange();
		this.rateItems = memento.getRateItems();
	}

	/**
	 * Save to memento.
	 *
	 * @param memento the memento
	 */
	public void saveToMemento(AccidentInsuranceRateSetMemento memento) {
		memento.setHistoryId(this.historyId);
		memento.setCompanyCode(this.companyCode);
		memento.setApplyRange(this.applyRange);
		memento.setRateItems(this.rateItems);
	}

	/**
	 * Copy with date.
	 *
	 * @param start the start
	 * @return the accident insurance rate
	 */
	public AccidentInsuranceRate copyWithDate(YearMonth start) {
		AccidentInsuranceRate newDomain = new AccidentInsuranceRate();
		newDomain.companyCode = this.companyCode;
		newDomain.applyRange = MonthRange.range(start, this.applyRange.getEndMonth());
		newDomain.rateItems = this.rateItems;
		return newDomain;
	}

	/**
	 * Creates the with intial.
	 *
	 * @param companyCode the company code
	 * @param startYearMonth the start year month
	 * @return the accident insurance rate
	 */
	public static final AccidentInsuranceRate createWithIntial(String companyCode,
			YearMonth startYearMonth) {
		AccidentInsuranceRate domain = new AccidentInsuranceRate();
		domain.companyCode = companyCode;
		domain.applyRange = MonthRange.toMaxDate(startYearMonth);
		domain.rateItems = Arrays
				.asList(AccidentInsuranceRate.valueIntial(BusinessTypeEnum.Biz1St),
						AccidentInsuranceRate.valueIntial(BusinessTypeEnum.Biz2Nd),
						AccidentInsuranceRate.valueIntial(BusinessTypeEnum.Biz3Rd),
						AccidentInsuranceRate.valueIntial(BusinessTypeEnum.Biz4Th),
						AccidentInsuranceRate.valueIntial(BusinessTypeEnum.Biz5Th),
						AccidentInsuranceRate.valueIntial(BusinessTypeEnum.Biz6Th),
						AccidentInsuranceRate.valueIntial(BusinessTypeEnum.Biz7Th),
						AccidentInsuranceRate.valueIntial(BusinessTypeEnum.Biz8Th),
						AccidentInsuranceRate.valueIntial(BusinessTypeEnum.Biz9Th),
						AccidentInsuranceRate.valueIntial(BusinessTypeEnum.Biz10Th))
				.stream().collect(Collectors.toSet());
		return domain;
	}

	/**
	 * Value intial.
	 *
	 * @param businessTypeEnum the business type enum
	 * @return the insu biz rate item
	 */
	public static final InsuBizRateItem valueIntial(BusinessTypeEnum businessTypeEnum) {
		return new InsuBizRateItem(businessTypeEnum, Double.valueOf("0"), RoundingMethod.RoundUp);
	}

	/**
	 * Sets the max date.
	 */
	public void setMaxDate() {
		this.applyRange = MonthRange.toMaxDate(this.getApplyRange().getStartMonth());
	}

	/**
	 * Sets the start.
	 *
	 * @param yearMonth the new start
	 */
	public void setStart(YearMonth yearMonth) {
		this.applyRange = MonthRange.range(yearMonth, this.applyRange.getEndMonth());
	}

	/**
	 * Sets the end.
	 *
	 * @param yearMonth the new end
	 */
	public void setEnd(YearMonth yearMonth) {
		this.applyRange = MonthRange.range(this.applyRange.getStartMonth(), yearMonth);
	}

}
