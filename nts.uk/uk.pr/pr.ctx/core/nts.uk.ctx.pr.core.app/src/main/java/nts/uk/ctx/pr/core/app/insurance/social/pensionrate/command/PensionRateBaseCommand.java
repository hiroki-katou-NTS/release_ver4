/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.core.app.insurance.social.pensionrate.command;

import java.math.BigDecimal;
import java.util.Set;
import java.util.stream.Collectors;

import lombok.Getter;
import lombok.Setter;
import nts.uk.ctx.core.dom.util.PrimitiveUtil;
import nts.uk.ctx.pr.core.app.insurance.social.pensionrate.find.FundRateItemDto;
import nts.uk.ctx.pr.core.app.insurance.social.pensionrate.find.PensionPremiumRateItemDto;
import nts.uk.ctx.pr.core.dom.insurance.CalculateMethod;
import nts.uk.ctx.pr.core.dom.insurance.CommonAmount;
import nts.uk.ctx.pr.core.dom.insurance.FundInputApply;
import nts.uk.ctx.pr.core.dom.insurance.Ins2Rate;
import nts.uk.ctx.pr.core.dom.insurance.MonthRange;
import nts.uk.ctx.pr.core.dom.insurance.OfficeCode;
import nts.uk.ctx.pr.core.dom.insurance.social.pensionrate.FundRateItem;
import nts.uk.ctx.pr.core.dom.insurance.social.pensionrate.PensionChargeRateItem;
import nts.uk.ctx.pr.core.dom.insurance.social.pensionrate.PensionPremiumRateItem;
import nts.uk.ctx.pr.core.dom.insurance.social.pensionrate.PensionRateGetMemento;
import nts.uk.ctx.pr.core.dom.insurance.social.pensionrate.PensionRateRounding;

/**
 * The Class PensionBaseCommand.
 */
@Getter
@Setter
public class PensionRateBaseCommand implements PensionRateGetMemento {

	/** The history id. */
	private String historyId;

	/** The company code. */
	private String companyCode;

	/** The office code. */
	private String officeCode;

	/** The start month. */
	private String startMonth;

	/** The end month. */
	private String endMonth;

	/** The fund input apply. */
	private Integer fundInputApply;

	/** The auto calculate. */
	private Integer autoCalculate;

	/** The premium rate items. */
	private Set<PensionPremiumRateItemDto> premiumRateItems;

	/** The fund rate items. */
	private Set<FundRateItemDto> fundRateItems;

	/** The rounding methods. */
	private Set<PensionRateRounding> roundingMethods;

	/** The max amount. */
	private BigDecimal maxAmount;

	/** The child contribution rate. */
	private BigDecimal childContributionRate;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nts.uk.ctx.pr.core.dom.insurance.social.pensionrate.PensionRateGetMemento
	 * #getRoundingMethods()
	 */
	@Override
	public Set<PensionRateRounding> getRoundingMethods() {
		return this.roundingMethods;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nts.uk.ctx.pr.core.dom.insurance.social.pensionrate.PensionRateGetMemento
	 * #getPremiumRateItems()
	 */
	@Override
	public Set<PensionPremiumRateItem> getPremiumRateItems() {
		return this.premiumRateItems.stream().map(dto -> {
			PensionChargeRateItem pensionChargeRateItem = new PensionChargeRateItem(
					new Ins2Rate(dto.getCompanyRate()), new Ins2Rate(dto.getPersonalRate()));
			return new PensionPremiumRateItem(dto.getPayType(), dto.getGenderType(),
					pensionChargeRateItem);
		}).collect(Collectors.toSet());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nts.uk.ctx.pr.core.dom.insurance.social.pensionrate.PensionRateGetMemento
	 * #getOfficeCode()
	 */
	@Override
	public OfficeCode getOfficeCode() {
		return new OfficeCode(this.officeCode);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nts.uk.ctx.pr.core.dom.insurance.social.pensionrate.PensionRateGetMemento
	 * #getMaxAmount()
	 */
	@Override
	public CommonAmount getMaxAmount() {
		return new CommonAmount(this.maxAmount);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nts.uk.ctx.pr.core.dom.insurance.social.pensionrate.PensionRateGetMemento
	 * #getHistoryId()
	 */
	@Override
	public String getHistoryId() {
		return this.historyId;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nts.uk.ctx.pr.core.dom.insurance.social.pensionrate.PensionRateGetMemento
	 * #getFundRateItems()
	 */
	@Override
	public Set<FundRateItem> getFundRateItems() {
		return this.fundRateItems.stream().map(dto -> {
			PensionChargeRateItem burdenPensionChargeRateItem = new PensionChargeRateItem(
					new Ins2Rate(dto.getBurdenChargeCompanyRate()),
					new Ins2Rate(dto.getBurdenChargePersonalRate()));

			PensionChargeRateItem exemptionPensionChargeRateItem = new PensionChargeRateItem(
					new Ins2Rate(dto.getExemptionChargeCompanyRate()),
					new Ins2Rate(dto.getExemptionChargePersonalRate()));

			return new FundRateItem(dto.getPayType(), dto.getGenderType(),
					burdenPensionChargeRateItem, exemptionPensionChargeRateItem);
		}).collect(Collectors.toSet());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nts.uk.ctx.pr.core.dom.insurance.social.pensionrate.PensionRateGetMemento
	 * #getCompanyCode()
	 */
	@Override
	public String getCompanyCode() {
		return companyCode;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nts.uk.ctx.pr.core.dom.insurance.social.pensionrate.PensionRateGetMemento
	 * #getChildContributionRate()
	 */
	@Override
	public Ins2Rate getChildContributionRate() {
		return new Ins2Rate(this.childContributionRate);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nts.uk.ctx.pr.core.dom.insurance.social.pensionrate.PensionRateGetMemento
	 * #getApplyRange()
	 */
	@Override
	public MonthRange getApplyRange() {
		return MonthRange.range(PrimitiveUtil.toYearMonth(this.startMonth, "/"),
				PrimitiveUtil.toYearMonth(this.endMonth, "/"));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nts.uk.ctx.pr.core.dom.insurance.social.pensionrate.PensionRateGetMemento
	 * #getFundInputApply()
	 */
	@Override
	public FundInputApply getFundInputApply() {
		return FundInputApply.valueOf(this.fundInputApply);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nts.uk.ctx.pr.core.dom.insurance.social.pensionrate.PensionRateGetMemento
	 * #getAutoCalculate()
	 */
	@Override
	public CalculateMethod getAutoCalculate() {
		return CalculateMethod.valueOf(this.autoCalculate);
	}
}
