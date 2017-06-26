/******************************************************************
 * Copyright (c) 2015 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.core.infra.repository.insurance.labor.unemployeerate;

import java.util.HashSet;
import java.util.Set;

import nts.arc.time.YearMonth;
import nts.uk.ctx.pr.core.dom.insurance.MonthRange;
import nts.uk.ctx.pr.core.dom.insurance.labor.unemployeerate.CareerGroup;
import nts.uk.ctx.pr.core.dom.insurance.labor.unemployeerate.UnemployeeInsuranceRateGetMemento;
import nts.uk.ctx.pr.core.dom.insurance.labor.unemployeerate.UnemployeeInsuranceRateItem;
import nts.uk.ctx.pr.core.infra.entity.insurance.labor.unemployeerate.QismtEmpInsuRate;

/**
 * The Class JpaUnemployeeInsuranceRateGetMemento.
 */
public class JpaUnemployeeInsuranceRateGetMemento implements UnemployeeInsuranceRateGetMemento {

	/** The type value. */
	private QismtEmpInsuRate typeValue;

	/**
	 * Instantiates a new jpa unemployee insurance rate get memento.
	 *
	 * @param typeValue
	 *            the type value
	 */
	public JpaUnemployeeInsuranceRateGetMemento(QismtEmpInsuRate typeValue) {
		this.typeValue = typeValue;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.pr.core.dom.insurance.labor.unemployeerate.
	 * UnemployeeInsuranceRateGetMemento#getHistoryId()
	 */
	@Override
	public String getHistoryId() {
		return this.typeValue.getQismtEmpInsuRatePK().getHistId();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.pr.core.dom.insurance.labor.unemployeerate.
	 * UnemployeeInsuranceRateGetMemento#getCompanyCode()
	 */
	@Override
	public String getCompanyCode() {
		return this.typeValue.getQismtEmpInsuRatePK().getCcd();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.pr.core.dom.insurance.labor.unemployeerate.
	 * UnemployeeInsuranceRateGetMemento#getApplyRange()
	 */
	@Override
	public MonthRange getApplyRange() {
		return MonthRange.range(YearMonth.of(this.typeValue.getStrYm()),
				YearMonth.of(this.typeValue.getEndYm()));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.pr.core.dom.insurance.labor.unemployeerate.
	 * UnemployeeInsuranceRateGetMemento#getRateItems()
	 */
	@Override
	public Set<UnemployeeInsuranceRateItem> getRateItems() {

		Set<UnemployeeInsuranceRateItem> setUnemployeeInsuranceRateItem = new HashSet<>();

		//add Agroforestry
		setUnemployeeInsuranceRateItem
				.add(new UnemployeeInsuranceRateItem(new JpaUnemployeeInsuranceRateItemGetMemento(
						this.typeValue, CareerGroup.Agroforestry)));
		
		//add Other
		setUnemployeeInsuranceRateItem
				.add(new UnemployeeInsuranceRateItem(new JpaUnemployeeInsuranceRateItemGetMemento(
						this.typeValue, CareerGroup.Other)));
		
		//add Contruction
		setUnemployeeInsuranceRateItem.add(new UnemployeeInsuranceRateItem(
				new JpaUnemployeeInsuranceRateItemGetMemento(this.typeValue, CareerGroup.Contruction)));

		return setUnemployeeInsuranceRateItem;
	}

}
