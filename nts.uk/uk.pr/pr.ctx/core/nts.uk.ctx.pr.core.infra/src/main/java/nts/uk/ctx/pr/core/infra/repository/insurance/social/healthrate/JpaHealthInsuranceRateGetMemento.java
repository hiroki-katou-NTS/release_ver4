/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.core.infra.repository.insurance.social.healthrate;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import nts.arc.time.YearMonth;
import nts.uk.ctx.pr.core.dom.insurance.CalculateMethod;
import nts.uk.ctx.pr.core.dom.insurance.CommonAmount;
import nts.uk.ctx.pr.core.dom.insurance.Ins3Rate;
import nts.uk.ctx.pr.core.dom.insurance.MonthRange;
import nts.uk.ctx.pr.core.dom.insurance.OfficeCode;
import nts.uk.ctx.pr.core.dom.insurance.PaymentType;
import nts.uk.ctx.pr.core.dom.insurance.RoundingItem;
import nts.uk.ctx.pr.core.dom.insurance.RoundingMethod;
import nts.uk.ctx.pr.core.dom.insurance.social.healthrate.HealthChargeRateItem;
import nts.uk.ctx.pr.core.dom.insurance.social.healthrate.HealthInsuranceRateGetMemento;
import nts.uk.ctx.pr.core.dom.insurance.social.healthrate.HealthInsuranceRounding;
import nts.uk.ctx.pr.core.dom.insurance.social.healthrate.HealthInsuranceType;
import nts.uk.ctx.pr.core.dom.insurance.social.healthrate.InsuranceRateItem;
import nts.uk.ctx.pr.core.infra.entity.insurance.social.healthrate.QismtHealthInsuRate;

/**
 * The Class JpaHealthInsuranceRateGetMemento.
 */
public class JpaHealthInsuranceRateGetMemento implements HealthInsuranceRateGetMemento {

	/** The type value. */
	protected QismtHealthInsuRate typeValue;

	/**
	 * Instantiates a new jpa health insurance rate get memento.
	 *
	 * @param typeValue
	 *            the type value
	 */
	public JpaHealthInsuranceRateGetMemento(QismtHealthInsuRate typeValue) {
		this.typeValue = typeValue;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.pr.core.dom.insurance.social.healthrate.
	 * HealthInsuranceRateGetMemento#getHistoryId()
	 */
	@Override
	public String getHistoryId() {
		return this.typeValue.getHistId();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.pr.core.dom.insurance.social.healthrate.
	 * HealthInsuranceRateGetMemento#getCompanyCode()
	 */
	@Override
	public String getCompanyCode() {
		return this.typeValue.getCcd();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.pr.core.dom.insurance.social.healthrate.
	 * HealthInsuranceRateGetMemento#getOfficeCode()
	 */
	@Override
	public OfficeCode getOfficeCode() {
		return new OfficeCode(this.typeValue.getSiOfficeCd());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.pr.core.dom.insurance.social.healthrate.
	 * HealthInsuranceRateGetMemento#getApplyRange()
	 */
	@Override
	public MonthRange getApplyRange() {
		return MonthRange.range(new YearMonth(this.typeValue.getStrYm()),
				new YearMonth(this.typeValue.getEndYm()));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.pr.core.dom.insurance.social.healthrate.
	 * HealthInsuranceRateGetMemento#getAutoCalculate()
	 */
	@Override
	public CalculateMethod getAutoCalculate() {
		return CalculateMethod.valueOf(this.typeValue.getKeepEntryFlg());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.pr.core.dom.insurance.social.healthrate.
	 * HealthInsuranceRateGetMemento#getMaxAmount()
	 */
	@Override
	public CommonAmount getMaxAmount() {
		return new CommonAmount(this.typeValue.getBonusHealthMaxMny());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.pr.core.dom.insurance.social.healthrate.
	 * HealthInsuranceRateGetMemento#getRateItems()
	 */
	@Override
	public Set<InsuranceRateItem> getRateItems() {
		List<InsuranceRateItem> listRate = new ArrayList<InsuranceRateItem>();
		InsuranceRateItem generalSalary = new InsuranceRateItem(PaymentType.Salary,
				HealthInsuranceType.General,
				new HealthChargeRateItem(new Ins3Rate(this.typeValue.getCPayGeneralRate()),
						new Ins3Rate(this.typeValue.getPPayGeneralRate())));
		InsuranceRateItem generalBonus = new InsuranceRateItem(PaymentType.Bonus,
				HealthInsuranceType.General,
				new HealthChargeRateItem(new Ins3Rate(this.typeValue.getCBnsGeneralRate()),
						new Ins3Rate(this.typeValue.getPBnsGeneralRate())));
		InsuranceRateItem nursingSalary = new InsuranceRateItem(PaymentType.Salary,
				HealthInsuranceType.Nursing,
				new HealthChargeRateItem(new Ins3Rate(this.typeValue.getCPayNursingRate()),
						new Ins3Rate(this.typeValue.getPPayNursingRate())));
		InsuranceRateItem nursingBonus = new InsuranceRateItem(PaymentType.Bonus,
				HealthInsuranceType.Nursing,
				new HealthChargeRateItem(new Ins3Rate(this.typeValue.getCBnsNursingRate()),
						new Ins3Rate(this.typeValue.getPBnsNursingRate())));
		InsuranceRateItem specialSalary = new InsuranceRateItem(PaymentType.Salary,
				HealthInsuranceType.Special,
				new HealthChargeRateItem(new Ins3Rate(this.typeValue.getCPaySpecificRate()),
						new Ins3Rate(this.typeValue.getPPaySpecificRate())));
		InsuranceRateItem specialBonus = new InsuranceRateItem(PaymentType.Bonus,
				HealthInsuranceType.Special,
				new HealthChargeRateItem(new Ins3Rate(this.typeValue.getCBnsSpecificRate()),
						new Ins3Rate(this.typeValue.getPBnsSpecificRate())));
		InsuranceRateItem basicSalary = new InsuranceRateItem(PaymentType.Salary,
				HealthInsuranceType.Basic,
				new HealthChargeRateItem(new Ins3Rate(this.typeValue.getCPayBasicRate()),
						new Ins3Rate(this.typeValue.getPPayBasicRate())));
		InsuranceRateItem basicBonus = new InsuranceRateItem(PaymentType.Bonus,
				HealthInsuranceType.Basic,
				new HealthChargeRateItem(new Ins3Rate(this.typeValue.getCBnsBasicRate()),
						new Ins3Rate(this.typeValue.getPBnsBasicRate())));

		listRate.add(generalSalary);
		listRate.add(generalBonus);
		listRate.add(nursingSalary);
		listRate.add(nursingBonus);
		listRate.add(specialSalary);
		listRate.add(specialBonus);
		listRate.add(basicSalary);
		listRate.add(basicBonus);
		return new HashSet<InsuranceRateItem>(listRate);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.pr.core.dom.insurance.social.healthrate.
	 * HealthInsuranceRateGetMemento#getRoundingMethods()
	 */
	@Override
	public Set<HealthInsuranceRounding> getRoundingMethods() {
		List<HealthInsuranceRounding> listRounding = new ArrayList<HealthInsuranceRounding>();
		RoundingItem salaryRoundingItem = new RoundingItem(
				RoundingMethod.valueOf(this.typeValue.getCPayHealthRoundAtr()),
				RoundingMethod.valueOf(this.typeValue.getPPayHealthRoundAtr()));

		RoundingItem bonusRoundingItem = new RoundingItem(
				RoundingMethod.valueOf(this.typeValue.getCBnsHealthRoundAtr()),
				RoundingMethod.valueOf(this.typeValue.getPBnsHealthRoundAtr()));

		HealthInsuranceRounding roundSalary = new HealthInsuranceRounding(PaymentType.Salary,
				salaryRoundingItem);
		HealthInsuranceRounding roundBonus = new HealthInsuranceRounding(PaymentType.Bonus,
				bonusRoundingItem);

		listRounding.add(roundSalary);
		listRounding.add(roundBonus);
		return new HashSet<HealthInsuranceRounding>(listRounding);
	}

}
