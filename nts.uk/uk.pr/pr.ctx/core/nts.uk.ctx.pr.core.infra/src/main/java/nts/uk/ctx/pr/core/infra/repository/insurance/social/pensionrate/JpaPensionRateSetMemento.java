/******************************************************************
 * Copyright (c) 2015 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.core.infra.repository.insurance.social.pensionrate;

import java.util.Set;

import nts.uk.ctx.pr.core.dom.insurance.CalculateMethod;
import nts.uk.ctx.pr.core.dom.insurance.CommonAmount;
import nts.uk.ctx.pr.core.dom.insurance.FundInputApply;
import nts.uk.ctx.pr.core.dom.insurance.Ins2Rate;
import nts.uk.ctx.pr.core.dom.insurance.MonthRange;
import nts.uk.ctx.pr.core.dom.insurance.OfficeCode;
import nts.uk.ctx.pr.core.dom.insurance.PaymentType;
import nts.uk.ctx.pr.core.dom.insurance.social.pensionrate.FundRateItem;
import nts.uk.ctx.pr.core.dom.insurance.social.pensionrate.InsuranceGender;
import nts.uk.ctx.pr.core.dom.insurance.social.pensionrate.PensionPremiumRateItem;
import nts.uk.ctx.pr.core.dom.insurance.social.pensionrate.PensionRateRounding;
import nts.uk.ctx.pr.core.dom.insurance.social.pensionrate.PensionRateSetMemento;
import nts.uk.ctx.pr.core.infra.entity.insurance.social.pensionrate.QismtPensionRate;

/**
 * The Class JpaPensionRateSetMemento.
 */
public class JpaPensionRateSetMemento implements PensionRateSetMemento {

	/** The type value. */
	private QismtPensionRate typeValue;

	/**
	 * Instantiates a new jpa pension rate set memento.
	 *
	 * @param typeValue
	 *            the type value
	 */
	public JpaPensionRateSetMemento(QismtPensionRate typeValue) {
		this.typeValue = typeValue;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nts.uk.ctx.pr.core.dom.insurance.social.pensionrate.PensionRateSetMemento
	 * #setHistoryId(java.lang.String)
	 */
	@Override
	public void setHistoryId(String historyId) {
		this.typeValue.setHistId(historyId);
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
		this.typeValue.setCcd(companyCode);
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
		this.typeValue.setSiOfficeCd(officeCode.v());
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
		this.typeValue.setStrYm(applyRange.getStartMonth().v());
		this.typeValue.setEndYm(applyRange.getEndMonth().v());
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
		this.typeValue.setBonusPensionMaxMny(maxAmount.v());
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
		for (FundRateItem e : fundRateItems) {
			if (e.getPayType().equals(PaymentType.Salary)
					&& e.getGenderType().equals(InsuranceGender.Male)) {
				this.typeValue.setCPayFundMaleRate(e.getBurdenChargeRate().getCompanyRate().v());
				this.typeValue.setPPayFundMaleRate(e.getBurdenChargeRate().getPersonalRate().v());
				this.typeValue
						.setCPayFundExMaleRate(e.getExemptionChargeRate().getCompanyRate().v());
				this.typeValue
						.setPPayFundExMaleRate(e.getExemptionChargeRate().getPersonalRate().v());
			}
			if (e.getPayType().equals(PaymentType.Salary)
					&& e.getGenderType().equals(InsuranceGender.Female)) {
				this.typeValue.setCPayFundFemRate(e.getBurdenChargeRate().getCompanyRate().v());
				this.typeValue.setPPayFundFemRate(e.getBurdenChargeRate().getPersonalRate().v());
				this.typeValue
						.setCPayFundExFemRate(e.getExemptionChargeRate().getCompanyRate().v());
				this.typeValue
						.setPPayFundExFemRate(e.getExemptionChargeRate().getPersonalRate().v());
			}
			if (e.getPayType().equals(PaymentType.Salary)
					&& e.getGenderType().equals(InsuranceGender.Unknow)) {
				this.typeValue.setCPayFundMinerRate(e.getBurdenChargeRate().getCompanyRate().v());
				this.typeValue.setPPayFundMinerRate(e.getBurdenChargeRate().getPersonalRate().v());
				this.typeValue
						.setCPayFundExMinerRate(e.getExemptionChargeRate().getCompanyRate().v());
				this.typeValue
						.setPPayFundExMinerRate(e.getExemptionChargeRate().getPersonalRate().v());
			}
			if (e.getPayType().equals(PaymentType.Bonus)
					&& e.getGenderType().equals(InsuranceGender.Male)) {
				this.typeValue.setCBnsFundManRate(e.getBurdenChargeRate().getCompanyRate().v());
				this.typeValue.setPBnsFundManRate(e.getBurdenChargeRate().getPersonalRate().v());
				this.typeValue
						.setCBnsFundExMaleRate(e.getExemptionChargeRate().getCompanyRate().v());
				this.typeValue
						.setPBnsFundExMaleRate(e.getExemptionChargeRate().getPersonalRate().v());
			}
			if (e.getPayType().equals(PaymentType.Bonus)
					&& e.getGenderType().equals(InsuranceGender.Female)) {
				this.typeValue.setCBnsFundFemRate(e.getBurdenChargeRate().getCompanyRate().v());
				this.typeValue.setPBnsFundFemRate(e.getBurdenChargeRate().getPersonalRate().v());
				this.typeValue
						.setCBnsFundExFemRate(e.getExemptionChargeRate().getCompanyRate().v());
				this.typeValue
						.setPBnsFundExFemRate(e.getExemptionChargeRate().getPersonalRate().v());
			}
			if (e.getPayType().equals(PaymentType.Bonus)
					&& e.getGenderType().equals(InsuranceGender.Unknow)) {
				this.typeValue.setCBnsFundMinerRate(e.getBurdenChargeRate().getCompanyRate().v());
				this.typeValue.setPBnsFundMinerRate(e.getBurdenChargeRate().getPersonalRate().v());
				this.typeValue
						.setCBnsFundExMinerRate(e.getExemptionChargeRate().getCompanyRate().v());
				this.typeValue
						.setPBnsFundExMinerRate(e.getExemptionChargeRate().getPersonalRate().v());
			}
		}
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
		for (PensionPremiumRateItem e : premiumRateItems) {
			if (e.getPayType().equals(PaymentType.Salary)
					&& e.getGenderType().equals(InsuranceGender.Male)) {
				this.typeValue.setCPayPensionMaleRate(e.getChargeRate().getCompanyRate().v());
				this.typeValue.setPPayPensionMaleRate(e.getChargeRate().getPersonalRate().v());
			}
			if (e.getPayType().equals(PaymentType.Salary)
					&& e.getGenderType().equals(InsuranceGender.Female)) {
				this.typeValue.setCPayPensionFemRate(e.getChargeRate().getCompanyRate().v());
				this.typeValue.setPPayPensionFemRate(e.getChargeRate().getPersonalRate().v());
			}
			if (e.getPayType().equals(PaymentType.Salary)
					&& e.getGenderType().equals(InsuranceGender.Unknow)) {
				this.typeValue.setCPayPensionMinerRate(e.getChargeRate().getCompanyRate().v());
				this.typeValue.setPPayPensionMinerRate(e.getChargeRate().getPersonalRate().v());
			}
			if (e.getPayType().equals(PaymentType.Bonus)
					&& e.getGenderType().equals(InsuranceGender.Male)) {
				this.typeValue.setCBnsPensionMaleRate(e.getChargeRate().getCompanyRate().v());
				this.typeValue.setPBnsPensionMaleRate(e.getChargeRate().getPersonalRate().v());
			}
			if (e.getPayType().equals(PaymentType.Bonus)
					&& e.getGenderType().equals(InsuranceGender.Female)) {
				this.typeValue.setCBnsPensionFemRate(e.getChargeRate().getCompanyRate().v());
				this.typeValue.setPBnsPensionFemRate(e.getChargeRate().getPersonalRate().v());
			}
			if (e.getPayType().equals(PaymentType.Bonus)
					&& e.getGenderType().equals(InsuranceGender.Unknow)) {
				this.typeValue.setCBnsPensionMinerRate(e.getChargeRate().getCompanyRate().v());
				this.typeValue.setPBnsPensionMinerRate(e.getChargeRate().getPersonalRate().v());
			}
		}
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
		this.typeValue.setChildContributionRate(childContributionRate.v());
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
		for (PensionRateRounding e : roundingMethods) {
			// salary
			if (e.getPayType().equals(PaymentType.Salary)) {
				this.typeValue.setCPayPensionRoundAtr(e.getRoundAtrs().getCompanyRoundAtr().value);
				this.typeValue.setPPayPensionRoundAtr(e.getRoundAtrs().getPersonalRoundAtr().value);
			}
			// bonus
			if (e.getPayType().equals(PaymentType.Bonus)) {
				this.typeValue.setCBnsPensionRoundAtr(e.getRoundAtrs().getCompanyRoundAtr().value);
				this.typeValue.setPBnsPensionRoundAtr(e.getRoundAtrs().getPersonalRoundAtr().value);
			}
		}
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
		this.typeValue.setPensionFundJoinAtr(fundInputApply.value);
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
		this.typeValue.setKeepEntryFlg(autoCalculate.value);
	}

}
