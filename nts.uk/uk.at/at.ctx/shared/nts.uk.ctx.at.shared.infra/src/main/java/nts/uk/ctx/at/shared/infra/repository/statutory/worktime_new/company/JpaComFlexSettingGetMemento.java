/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.at.shared.infra.repository.statutory.worktime_new.company;

import java.util.ArrayList;
import java.util.List;

import nts.uk.ctx.at.shared.dom.common.CompanyId;
import nts.uk.ctx.at.shared.dom.common.Month;
import nts.uk.ctx.at.shared.dom.common.MonthlyEstimateTime;
import nts.uk.ctx.at.shared.dom.common.Year;
import nts.uk.ctx.at.shared.dom.statutory.worktime.companyNew.ComFlexSettingGetMemento;
import nts.uk.ctx.at.shared.dom.statutory.worktime.sharedNew.MonthlyUnit;
import nts.uk.ctx.at.shared.infra.entity.statutory.worktime_new.company.KshstComFlexSet;

/**
 * The Class JpaComFlexSettingGetMemento.
 */
public class JpaComFlexSettingGetMemento implements ComFlexSettingGetMemento {
	
	/** The entity. */
	private KshstComFlexSet entity;

	/**
	 * Instantiates a new jpa com flex setting get memento.
	 *
	 * @param entity the entity
	 */
	public JpaComFlexSettingGetMemento(KshstComFlexSet entity) {
		this.entity = entity;
	}

	/* (non-Javadoc)
	 * @see nts.uk.ctx.at.shared.dom.statutory.worktime.sharedNew.FlexSettingGetMemento#getYear()
	 */
	@Override
	public Year getYear() {
		return new Year(this.entity.getKshstComFlexSetPK().getYear());
	}

	/* (non-Javadoc)
	 * @see nts.uk.ctx.at.shared.dom.statutory.worktime.companyNew.ComFlexSettingGetMemento#getCompanyId()
	 */
	@Override
	public CompanyId getCompanyId() {
		return new CompanyId(this.entity.getKshstComFlexSetPK().getCid());
	}

	/* (non-Javadoc)
	 * @see nts.uk.ctx.at.shared.dom.statutory.worktime.sharedNew.FlexSettingGetMemento#getStatutorySetting()
	 */
	@Override
	public List<MonthlyUnit> getStatutorySetting() {
		return this.toStatutorySetting();
	}

	/* (non-Javadoc)
	 * @see nts.uk.ctx.at.shared.dom.statutory.worktime.sharedNew.FlexSettingGetMemento#getSpecifiedSetting()
	 */
	@Override
	public List<MonthlyUnit> getSpecifiedSetting() {
		return this.toSpecSetting();
	}
	
	/**
	 * To statutory setting.
	 *
	 * @return the list
	 */
	private List<MonthlyUnit> toStatutorySetting() {
		List<MonthlyUnit> monthlyUnits = new ArrayList<>();
		monthlyUnits.add(new MonthlyUnit( new Month(Month.JANUARY), new MonthlyEstimateTime(this.entity.getStatJanTime())));
		monthlyUnits.add(new MonthlyUnit( new Month(Month.FEBRUARY), new MonthlyEstimateTime(this.entity.getStatFebTime())));
		monthlyUnits.add(new MonthlyUnit( new Month(Month.MARCH), new MonthlyEstimateTime(this.entity.getStatMarTime())));
		monthlyUnits.add(new MonthlyUnit( new Month(Month.APRIL), new MonthlyEstimateTime(this.entity.getStatAprTime())));
		monthlyUnits.add(new MonthlyUnit( new Month(Month.MAY), new MonthlyEstimateTime(this.entity.getStatMayTime())));
		monthlyUnits.add(new MonthlyUnit( new Month(Month.JUNE), new MonthlyEstimateTime(this.entity.getStatJunTime())));
		monthlyUnits.add(new MonthlyUnit( new Month(Month.JULY), new MonthlyEstimateTime(this.entity.getStatJulTime())));
		monthlyUnits.add(new MonthlyUnit( new Month(Month.AUGUST), new MonthlyEstimateTime(this.entity.getStatAugTime())));
		monthlyUnits.add(new MonthlyUnit( new Month(Month.SEPTEMBER), new MonthlyEstimateTime(this.entity.getStatSepTime())));
		monthlyUnits.add(new MonthlyUnit( new Month(Month.OCTOBER), new MonthlyEstimateTime(this.entity.getStatOctTime())));
		monthlyUnits.add(new MonthlyUnit( new Month(Month.NOVEMBER), new MonthlyEstimateTime(this.entity.getStatNovTime())));
		monthlyUnits.add(new MonthlyUnit( new Month(Month.DECEMBER), new MonthlyEstimateTime(this.entity.getStatDecTime())));
		return monthlyUnits;
	}
	
	/**
	 * To spec setting.
	 *
	 * @return the list
	 */
	private List<MonthlyUnit> toSpecSetting() {
		List<MonthlyUnit> monthlyUnits = new ArrayList<>();
		monthlyUnits.add(new MonthlyUnit( new Month(Month.JANUARY), new MonthlyEstimateTime(this.entity.getSpecJanTime())));
		monthlyUnits.add(new MonthlyUnit( new Month(Month.FEBRUARY), new MonthlyEstimateTime(this.entity.getSpecFebTime())));
		monthlyUnits.add(new MonthlyUnit( new Month(Month.MARCH), new MonthlyEstimateTime(this.entity.getSpecMarTime())));
		monthlyUnits.add(new MonthlyUnit( new Month(Month.APRIL), new MonthlyEstimateTime(this.entity.getSpecAprTime())));
		monthlyUnits.add(new MonthlyUnit( new Month(Month.MAY), new MonthlyEstimateTime(this.entity.getSpecMayTime())));
		monthlyUnits.add(new MonthlyUnit( new Month(Month.JUNE), new MonthlyEstimateTime(this.entity.getSpecJunTime())));
		monthlyUnits.add(new MonthlyUnit( new Month(Month.JULY), new MonthlyEstimateTime(this.entity.getSpecJulTime())));
		monthlyUnits.add(new MonthlyUnit( new Month(Month.AUGUST), new MonthlyEstimateTime(this.entity.getSpecAugTime())));
		monthlyUnits.add(new MonthlyUnit( new Month(Month.SEPTEMBER), new MonthlyEstimateTime(this.entity.getSpecSepTime())));
		monthlyUnits.add(new MonthlyUnit( new Month(Month.OCTOBER), new MonthlyEstimateTime(this.entity.getSpecOctTime())));
		monthlyUnits.add(new MonthlyUnit( new Month(Month.NOVEMBER), new MonthlyEstimateTime(this.entity.getSpecNovTime())));
		monthlyUnits.add(new MonthlyUnit( new Month(Month.DECEMBER), new MonthlyEstimateTime(this.entity.getSpecDecTime())));
		return monthlyUnits;
	}
}
