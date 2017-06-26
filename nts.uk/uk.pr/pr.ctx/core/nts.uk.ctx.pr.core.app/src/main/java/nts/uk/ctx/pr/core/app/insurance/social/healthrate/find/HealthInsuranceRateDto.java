/******************************************************************
 * Copyright (c) 2016 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.core.app.insurance.social.healthrate.find;

import java.math.BigDecimal;
import java.util.Set;
import java.util.stream.Collectors;

import lombok.Builder;
import nts.uk.ctx.pr.core.dom.insurance.CalculateMethod;
import nts.uk.ctx.pr.core.dom.insurance.CommonAmount;
import nts.uk.ctx.pr.core.dom.insurance.MonthRange;
import nts.uk.ctx.pr.core.dom.insurance.OfficeCode;
import nts.uk.ctx.pr.core.dom.insurance.social.healthrate.HealthInsuranceRateSetMemento;
import nts.uk.ctx.pr.core.dom.insurance.social.healthrate.HealthInsuranceRounding;
import nts.uk.ctx.pr.core.dom.insurance.social.healthrate.InsuranceRateItem;

/* (non-Javadoc)
 * @see java.lang.Object#toString()
 */
@Builder
public class HealthInsuranceRateDto implements HealthInsuranceRateSetMemento {

	/** The history id. */
	public String historyId;

	/** The company code. */
	public String companyCode;

	/** The office code. */
	public String officeCode;

	/** The office name. */
	public String officeName;

	/** The start month. */
	public String startMonth;

	/** The end month. */
	public String endMonth;

	/** The auto calculate. */
	public Integer autoCalculate;

	/** The max amount. */
	public BigDecimal maxAmount;

	/** The rate items. */
	public Set<InsuranceRateItemDto> rateItems;

	/** The rounding methods. */
	public Set<HealthInsuranceRounding> roundingMethods;

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.pr.core.dom.insurance.social.healthrate.
	 * HealthInsuranceRateSetMemento#setCompanyCode(nts.uk.ctx.core.dom.company.
	 * CompanyCode)
	 */
	@Override
	public void setCompanyCode(String companyCode) {
		this.companyCode = companyCode;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.pr.core.dom.insurance.social.healthrate.
	 * HealthInsuranceRateSetMemento#setOfficeCode(nts.uk.ctx.pr.core.dom.
	 * insurance.OfficeCode)
	 */
	@Override
	public void setOfficeCode(OfficeCode officeCode) {
		this.officeCode = officeCode.v();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.pr.core.dom.insurance.social.healthrate.
	 * HealthInsuranceRateSetMemento#setApplyRange(nts.uk.ctx.pr.core.dom.
	 * insurance.MonthRange)
	 */
	@Override
	public void setApplyRange(MonthRange applyRange) {
		this.startMonth = applyRange.getStartMonth().toString();
		this.endMonth = applyRange.getEndMonth().toString();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.pr.core.dom.insurance.social.healthrate.
	 * HealthInsuranceRateSetMemento#setMaxAmount(nts.uk.ctx.pr.core.dom.
	 * insurance.CommonAmount)
	 */
	@Override
	public void setMaxAmount(CommonAmount maxAmount) {
		this.maxAmount = maxAmount.v();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.pr.core.dom.insurance.social.healthrate.
	 * HealthInsuranceRateSetMemento#setRateItems(java.util.Set)
	 */
	@Override
	public void setRateItems(Set<InsuranceRateItem> rateItems) {
		this.rateItems = rateItems.stream().map(
				insuranceRateItemDomain -> InsuranceRateItemDto.fromDomain(insuranceRateItemDomain))
				.collect(Collectors.toSet());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.pr.core.dom.insurance.social.healthrate.
	 * HealthInsuranceRateSetMemento#setHistoryId(java.lang.String)
	 */
	@Override
	public void setHistoryId(String historyId) {
		this.historyId = historyId;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.pr.core.dom.insurance.social.healthrate.
	 * HealthInsuranceRateSetMemento#setAutoCalculate(java.lang.Boolean)
	 */
	@Override
	public void setAutoCalculate(CalculateMethod autoCalculate) {
		this.autoCalculate = autoCalculate.value;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.pr.core.dom.insurance.social.healthrate.
	 * HealthInsuranceRateSetMemento#setRoundingMethods(java.util.Set)
	 */
	@Override
	public void setRoundingMethods(Set<HealthInsuranceRounding> roundingMethods) {
		this.roundingMethods = roundingMethods;
	}

}
