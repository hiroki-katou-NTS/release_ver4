/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.at.shared.infra.repository.statutory.worktime_new.workplace;

import java.util.ArrayList;
import java.util.List;

import nts.uk.ctx.at.shared.dom.common.CompanyId;
import nts.uk.ctx.at.shared.dom.common.Month;
import nts.uk.ctx.at.shared.dom.common.MonthlyEstimateTime;
import nts.uk.ctx.at.shared.dom.common.WorkplaceId;
import nts.uk.ctx.at.shared.dom.common.Year;
import nts.uk.ctx.at.shared.dom.statutory.worktime.sharedNew.MonthlyUnit;
import nts.uk.ctx.at.shared.dom.statutory.worktime.workplaceNew.WkpNormalSettingGetMemento;
import nts.uk.ctx.at.shared.infra.entity.statutory.worktime_new.workingplace.KshstWkpNormalSet;

/**
 * The Class JpaWkpNormalSettingGetMemento.
 */
public class JpaWkpNormalSettingGetMemento implements WkpNormalSettingGetMemento {

	/** The entity. */
	private KshstWkpNormalSet entity;

	/**
	 * Instantiates a new jpa com normal setting get memento.
	 *
	 * @param entity
	 *            the entity
	 */
	public JpaWkpNormalSettingGetMemento(KshstWkpNormalSet entity) {
		this.entity = entity;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.at.shared.dom.statutory.worktime.sharedNew.
	 * NormalSettingGetMemento#getYear()
	 */
	@Override
	public Year getYear() {
		return new Year(this.entity.getKshstWkpNormalSetPK().getYear());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.at.shared.dom.statutory.worktime.companyNew.
	 * WkpNormalSettingGetMemento#getCompanyId()
	 */
	@Override
	public CompanyId getCompanyId() {
		return new CompanyId(this.entity.getKshstWkpNormalSetPK().getCid());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.at.shared.dom.statutory.worktime.workplaceNew.
	 * WkpNormalSettingGetMemento#getWorkplaceId()
	 */
	@Override
	public WorkplaceId getWorkplaceId() {
		return new WorkplaceId(this.entity.getKshstWkpNormalSetPK().getWkpId());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.at.shared.dom.statutory.worktime.sharedNew.
	 * NormalSettingGetMemento#getStatutorySetting()
	 */
	@Override
	public List<MonthlyUnit> getStatutorySetting() {
		List<MonthlyUnit> monthlyUnits = new ArrayList<>();
		monthlyUnits.add(new MonthlyUnit(new Month(Month.JANUARY),
				new MonthlyEstimateTime(this.entity.getJanTime())));
		monthlyUnits.add(new MonthlyUnit(new Month(Month.FEBRUARY),
				new MonthlyEstimateTime(this.entity.getFebTime())));
		monthlyUnits.add(new MonthlyUnit(new Month(Month.MARCH),
				new MonthlyEstimateTime(this.entity.getMarTime())));
		monthlyUnits.add(new MonthlyUnit(new Month(Month.APRIL),
				new MonthlyEstimateTime(this.entity.getAprTime())));
		monthlyUnits.add(new MonthlyUnit(new Month(Month.MAY),
				new MonthlyEstimateTime(this.entity.getMayTime())));
		monthlyUnits.add(new MonthlyUnit(new Month(Month.JUNE),
				new MonthlyEstimateTime(this.entity.getJunTime())));
		monthlyUnits.add(new MonthlyUnit(new Month(Month.JULY),
				new MonthlyEstimateTime(this.entity.getJulTime())));
		monthlyUnits.add(new MonthlyUnit(new Month(Month.AUGUST),
				new MonthlyEstimateTime(this.entity.getAugTime())));
		monthlyUnits.add(new MonthlyUnit(new Month(Month.SEPTEMBER),
				new MonthlyEstimateTime(this.entity.getSepTime())));
		monthlyUnits.add(new MonthlyUnit(new Month(Month.OCTOBER),
				new MonthlyEstimateTime(this.entity.getOctTime())));
		monthlyUnits.add(new MonthlyUnit(new Month(Month.NOVEMBER),
				new MonthlyEstimateTime(this.entity.getNovTime())));
		monthlyUnits.add(new MonthlyUnit(new Month(Month.DECEMBER),
				new MonthlyEstimateTime(this.entity.getDecTime())));
		return monthlyUnits;
	}

}