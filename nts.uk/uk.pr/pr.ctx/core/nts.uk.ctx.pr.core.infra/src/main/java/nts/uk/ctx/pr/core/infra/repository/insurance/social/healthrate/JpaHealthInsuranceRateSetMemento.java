/******************************************************************
 * Copyright (c) 2015 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.core.infra.repository.insurance.social.healthrate;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import nts.uk.ctx.pr.core.dom.insurance.CalculateMethod;
import nts.uk.ctx.pr.core.dom.insurance.CommonAmount;
import nts.uk.ctx.pr.core.dom.insurance.MonthRange;
import nts.uk.ctx.pr.core.dom.insurance.OfficeCode;
import nts.uk.ctx.pr.core.dom.insurance.PaymentType;
import nts.uk.ctx.pr.core.dom.insurance.social.healthrate.HealthInsuranceRateSetMemento;
import nts.uk.ctx.pr.core.dom.insurance.social.healthrate.HealthInsuranceRounding;
import nts.uk.ctx.pr.core.dom.insurance.social.healthrate.HealthInsuranceType;
import nts.uk.ctx.pr.core.dom.insurance.social.healthrate.InsuranceRateItem;
import nts.uk.ctx.pr.core.infra.entity.insurance.social.healthrate.QismtHealthInsuRate;

/**
 * The Class JpaHealthInsuranceRateSetMemento.
 */
public class JpaHealthInsuranceRateSetMemento implements HealthInsuranceRateSetMemento {

	/** The type value. */
	private QismtHealthInsuRate typeValue;

	/**
	 * Instantiates a new jpa health insurance rate set memento.
	 *
	 * @param typeValue
	 *            the type value
	 */
	public JpaHealthInsuranceRateSetMemento(QismtHealthInsuRate typeValue) {
		this.typeValue = typeValue;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.pr.core.dom.insurance.social.healthrate.
	 * HealthInsuranceRateSetMemento#setHistoryId(java.lang.String)
	 */
	@Override
	public void setHistoryId(String historyId) {
		this.typeValue.setHistId(historyId);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.pr.core.dom.insurance.social.healthrate.
	 * HealthInsuranceRateSetMemento#setCompanyCode(nts.uk.ctx.core.dom.company.
	 * CompanyCode)
	 */
	@Override
	public void setCompanyCode(String companyCode) {
		this.typeValue.setCcd(companyCode);
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
		this.typeValue.setSiOfficeCd(officeCode.v());
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
		this.typeValue.setStrYm(applyRange.getStartMonth().v());
		this.typeValue.setEndYm(applyRange.getEndMonth().v());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.pr.core.dom.insurance.social.healthrate.
	 * HealthInsuranceRateSetMemento#setAutoCalculate(java.lang.Boolean)
	 */
	@Override
	public void setAutoCalculate(CalculateMethod autoCalculate) {
		this.typeValue.setKeepEntryFlg(autoCalculate.value);
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
		this.typeValue.setBonusHealthMaxMny(maxAmount.v());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.pr.core.dom.insurance.social.healthrate.
	 * HealthInsuranceRateSetMemento#setRateItems(java.util.Set)
	 */
	@Override
	public void setRateItems(Set<InsuranceRateItem> rateItems) {
		List<InsuranceRateItem> list = new ArrayList<InsuranceRateItem>();
		list.addAll(rateItems);
		for (InsuranceRateItem e : list) {
			// general Salary
			if (e.getPayType().equals(PaymentType.Salary)
					&& e.getInsuranceType().equals(HealthInsuranceType.General)) {
				this.typeValue.setCPayGeneralRate(e.getChargeRate().getCompanyRate().v());
				this.typeValue.setPPayGeneralRate(e.getChargeRate().getPersonalRate().v());
			}
			// general Bonus
			if (e.getPayType().equals(PaymentType.Bonus)
					&& e.getInsuranceType().equals(HealthInsuranceType.General)) {
				this.typeValue.setCBnsGeneralRate(e.getChargeRate().getCompanyRate().v());
				this.typeValue.setPBnsGeneralRate(e.getChargeRate().getPersonalRate().v());
			}
			// nursing Salary
			if (e.getPayType().equals(PaymentType.Salary)
					&& e.getInsuranceType().equals(HealthInsuranceType.Nursing)) {
				this.typeValue.setCPayNursingRate(e.getChargeRate().getCompanyRate().v());
				this.typeValue.setPPayNursingRate(e.getChargeRate().getPersonalRate().v());
			}
			// nursing Bonus
			if (e.getPayType().equals(PaymentType.Bonus)
					&& e.getInsuranceType().equals(HealthInsuranceType.Nursing)) {
				this.typeValue.setCBnsNursingRate(e.getChargeRate().getCompanyRate().v());
				this.typeValue.setPBnsNursingRate(e.getChargeRate().getPersonalRate().v());
			}
			// special Salary
			if (e.getPayType().equals(PaymentType.Salary)
					&& e.getInsuranceType().equals(HealthInsuranceType.Special)) {
				this.typeValue.setCPaySpecificRate(e.getChargeRate().getCompanyRate().v());
				this.typeValue.setPPaySpecificRate(e.getChargeRate().getPersonalRate().v());
			}
			// special Bonus
			if (e.getPayType().equals(PaymentType.Bonus)
					&& e.getInsuranceType().equals(HealthInsuranceType.Special)) {
				this.typeValue.setCBnsSpecificRate(e.getChargeRate().getCompanyRate().v());
				this.typeValue.setPBnsSpecificRate(e.getChargeRate().getPersonalRate().v());
			}
			// basic Salary
			if (e.getPayType().equals(PaymentType.Salary)
					&& e.getInsuranceType().equals(HealthInsuranceType.Basic)) {
				this.typeValue.setCPayBasicRate(e.getChargeRate().getCompanyRate().v());
				this.typeValue.setPPayBasicRate(e.getChargeRate().getPersonalRate().v());
			}
			// basic Bonus
			if (e.getPayType().equals(PaymentType.Bonus)
					&& e.getInsuranceType().equals(HealthInsuranceType.Basic)) {
				this.typeValue.setCBnsBasicRate(e.getChargeRate().getCompanyRate().v());
				this.typeValue.setPBnsBasicRate(e.getChargeRate().getPersonalRate().v());
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.pr.core.dom.insurance.social.healthrate.
	 * HealthInsuranceRateSetMemento#setRoundingMethods(java.util.Set)
	 */
	@Override
	public void setRoundingMethods(Set<HealthInsuranceRounding> roundingMethods) {
		List<HealthInsuranceRounding> list = new ArrayList<HealthInsuranceRounding>();
		list.addAll(roundingMethods);
		for (HealthInsuranceRounding e : list) {
			// salary
			if (e.getPayType().equals(PaymentType.Salary)) {
				this.typeValue.setCPayHealthRoundAtr(e.getRoundAtrs().getCompanyRoundAtr().value);
				this.typeValue.setPPayHealthRoundAtr(e.getRoundAtrs().getPersonalRoundAtr().value);
			}
			// bonus
			if (e.getPayType().equals(PaymentType.Bonus)) {
				this.typeValue.setCBnsHealthRoundAtr(e.getRoundAtrs().getCompanyRoundAtr().value);
				this.typeValue.setPBnsHealthRoundAtr(e.getRoundAtrs().getPersonalRoundAtr().value);
			}
		}
	}
}
