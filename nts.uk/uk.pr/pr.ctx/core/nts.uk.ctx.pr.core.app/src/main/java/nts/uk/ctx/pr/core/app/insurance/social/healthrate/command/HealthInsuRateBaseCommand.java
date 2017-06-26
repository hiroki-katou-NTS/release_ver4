/******************************************************************
 * Copyright (c) 2016 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.core.app.insurance.social.healthrate.command;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import lombok.Getter;
import lombok.Setter;
import nts.uk.ctx.core.dom.util.PrimitiveUtil;
import nts.uk.ctx.pr.core.app.insurance.social.healthrate.find.InsuranceRateItemDto;
import nts.uk.ctx.pr.core.dom.insurance.CalculateMethod;
import nts.uk.ctx.pr.core.dom.insurance.CommonAmount;
import nts.uk.ctx.pr.core.dom.insurance.Ins3Rate;
import nts.uk.ctx.pr.core.dom.insurance.MonthRange;
import nts.uk.ctx.pr.core.dom.insurance.OfficeCode;
import nts.uk.ctx.pr.core.dom.insurance.social.healthrate.HealthChargeRateItem;
import nts.uk.ctx.pr.core.dom.insurance.social.healthrate.HealthInsuranceRateGetMemento;
import nts.uk.ctx.pr.core.dom.insurance.social.healthrate.HealthInsuranceRounding;
import nts.uk.ctx.pr.core.dom.insurance.social.healthrate.InsuranceRateItem;

/**
 * The Class HealthInsuranceBaseCommand.
 */
@Getter
@Setter
public class HealthInsuRateBaseCommand implements HealthInsuranceRateGetMemento {

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

	/** The auto calculate. */
	private Integer autoCalculate;

	/** The max amount. */
	private BigDecimal maxAmount;

	/** The rate items. */
	private List<InsuranceRateItemDto> rateItems;

	/** The rounding methods. */
	private List<HealthInsuranceRounding> roundingMethods;

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.pr.core.dom.insurance.social.healthrate.
	 * HealthInsuranceRateGetMemento#getRoundingMethods()
	 */
	@Override
	public Set<HealthInsuranceRounding> getRoundingMethods() {
		return new HashSet<HealthInsuranceRounding>(this.roundingMethods);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.pr.core.dom.insurance.social.healthrate.
	 * HealthInsuranceRateGetMemento#getRateItems()
	 */
	@Override
	public Set<InsuranceRateItem> getRateItems() {
		return new HashSet<InsuranceRateItem>(this.rateItems
				.stream().map(
						dto -> new InsuranceRateItem(dto.getPayType(), dto.getInsuranceType(),
								new HealthChargeRateItem(new Ins3Rate(dto.getChargeRate().getCompanyRate()),
										new Ins3Rate(dto.getChargeRate().getPersonalRate()))))
				.collect(Collectors.toList()));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.pr.core.dom.insurance.social.healthrate.
	 * HealthInsuranceRateGetMemento#getOfficeCode()
	 */
	@Override
	public OfficeCode getOfficeCode() {
		return new OfficeCode(this.officeCode);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.pr.core.dom.insurance.social.healthrate.
	 * HealthInsuranceRateGetMemento#getMaxAmount()
	 */
	@Override
	public CommonAmount getMaxAmount() {
		return new CommonAmount(this.maxAmount);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.pr.core.dom.insurance.social.healthrate.
	 * HealthInsuranceRateGetMemento#getHistoryId()
	 */
	@Override
	public String getHistoryId() {
		return historyId;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.pr.core.dom.insurance.social.healthrate.
	 * HealthInsuranceRateGetMemento#getCompanyCode()
	 */
	@Override
	public String getCompanyCode() {
		return companyCode;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.pr.core.dom.insurance.social.healthrate.
	 * HealthInsuranceRateGetMemento#getAutoCalculate()
	 */
	@Override
	public CalculateMethod getAutoCalculate() {
		return CalculateMethod.valueOf(this.autoCalculate);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.pr.core.dom.insurance.social.healthrate.
	 * HealthInsuranceRateGetMemento#getApplyRange()
	 */
	@Override
	public MonthRange getApplyRange() {
		return MonthRange.range(PrimitiveUtil.toYearMonth(this.startMonth, "/"),
				PrimitiveUtil.toYearMonth(this.endMonth, "/"));
	}
}
