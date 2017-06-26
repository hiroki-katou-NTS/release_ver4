/******************************************************************
 * Copyright (c) 2016 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.core.app.insurance.social.pensionrate.find;

import java.math.BigDecimal;
import java.util.Set;
import java.util.stream.Collectors;

import lombok.Builder;
import nts.uk.ctx.pr.core.dom.insurance.CalculateMethod;
import nts.uk.ctx.pr.core.dom.insurance.CommonAmount;
import nts.uk.ctx.pr.core.dom.insurance.FundInputApply;
import nts.uk.ctx.pr.core.dom.insurance.Ins2Rate;
import nts.uk.ctx.pr.core.dom.insurance.MonthRange;
import nts.uk.ctx.pr.core.dom.insurance.OfficeCode;
import nts.uk.ctx.pr.core.dom.insurance.social.pensionrate.FundRateItem;
import nts.uk.ctx.pr.core.dom.insurance.social.pensionrate.PensionPremiumRateItem;
import nts.uk.ctx.pr.core.dom.insurance.social.pensionrate.PensionRateRounding;
import nts.uk.ctx.pr.core.dom.insurance.social.pensionrate.PensionRateSetMemento;

/**
 * The Class PensionRateDto.
 */
@Builder
public class PensionRateDto implements PensionRateSetMemento {

	/** The history id. */
	public String historyId;

	/** The company code. */
	public String companyCode;

	/** The office code. */
	public String officeCode;

	/** The start month. */
	public String startMonth;

	/** The end month. */
	public String endMonth;

	/** The max amount. */
	public BigDecimal maxAmount;

	/** The fund rate items. */
	public Set<FundRateItemDto> fundRateItems;

	/** The premium rate items. */
	public Set<PensionPremiumRateItemDto> premiumRateItems;

	/** The child contribution rate. */
	public BigDecimal childContributionRate;

	/** The rounding methods. */
	public Set<PensionRateRounding> roundingMethods;

	/** The fund input apply. */
	public Integer fundInputApply;

	/** The auto calculate. */
	public Integer autoCalculate;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nts.uk.ctx.pr.core.dom.insurance.social.pensionrate.PensionRateSetMemento
	 * #setHistoryId(java.lang.String)
	 */
	@Override
	public void setHistoryId(String historyId) {
		this.historyId = historyId;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nts.uk.ctx.pr.core.dom.insurance.social.pensionrate.PensionRateSetMemento
	 * #setCompanyCode(nts.uk.ctx.core.dom.company.CompanyCode)
	 */
	@Override
	public void setCompanyCode(String companyCode) {
		this.companyCode = companyCode;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nts.uk.ctx.pr.core.dom.insurance.social.pensionrate.PensionRateSetMemento
	 * #setOfficeCode(nts.uk.ctx.pr.core.dom.insurance.OfficeCode)
	 */
	@Override
	public void setOfficeCode(OfficeCode officeCode) {
		this.officeCode = officeCode.v();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nts.uk.ctx.pr.core.dom.insurance.social.pensionrate.PensionRateSetMemento
	 * #setApplyRange(nts.uk.ctx.pr.core.dom.insurance.MonthRange)
	 */
	@Override
	public void setApplyRange(MonthRange applyRange) {
		this.startMonth = applyRange.getStartMonth().toString();
		this.endMonth = applyRange.getEndMonth().toString();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nts.uk.ctx.pr.core.dom.insurance.social.pensionrate.PensionRateSetMemento
	 * #setMaxAmount(nts.uk.ctx.pr.core.dom.insurance.CommonAmount)
	 */
	@Override
	public void setMaxAmount(CommonAmount maxAmount) {
		this.maxAmount = maxAmount.v();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nts.uk.ctx.pr.core.dom.insurance.social.pensionrate.PensionRateSetMemento
	 * #setFundRateItems(java.util.List)
	 */
	@Override
	public void setFundRateItems(Set<FundRateItem> fundRateItems) {
		this.fundRateItems = fundRateItems.stream().map(item -> FundRateItemDto.fromDomain(item))
				.collect(Collectors.toSet());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nts.uk.ctx.pr.core.dom.insurance.social.pensionrate.PensionRateSetMemento
	 * #setPremiumRateItems(java.util.List)
	 */
	@Override
	public void setPremiumRateItems(Set<PensionPremiumRateItem> premiumRateItems) {
		this.premiumRateItems = premiumRateItems.stream()
				.map(item -> PensionPremiumRateItemDto.fromDomain(item))
				.collect(Collectors.toSet());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nts.uk.ctx.pr.core.dom.insurance.social.pensionrate.PensionRateSetMemento
	 * #setChildContributionRate(nts.uk.ctx.pr.core.dom.insurance.Ins2Rate)
	 */
	@Override
	public void setChildContributionRate(Ins2Rate childContributionRate) {
		this.childContributionRate = childContributionRate.v();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nts.uk.ctx.pr.core.dom.insurance.social.pensionrate.PensionRateSetMemento
	 * #setRoundingMethods(java.util.List)
	 */
	@Override
	public void setRoundingMethods(Set<PensionRateRounding> roundingMethods) {
		this.roundingMethods = roundingMethods;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nts.uk.ctx.pr.core.dom.insurance.social.pensionrate.PensionRateSetMemento
	 * #setFundInputApply(java.lang.Boolean)
	 */
	@Override
	public void setFundInputApply(FundInputApply fundInputApply) {
		this.fundInputApply = fundInputApply.value;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nts.uk.ctx.pr.core.dom.insurance.social.pensionrate.PensionRateSetMemento
	 * #setAutoCalculate(java.lang.Boolean)
	 */
	@Override
	public void setAutoCalculate(CalculateMethod autoCalculate) {
		this.autoCalculate = autoCalculate.value;
	}

}
