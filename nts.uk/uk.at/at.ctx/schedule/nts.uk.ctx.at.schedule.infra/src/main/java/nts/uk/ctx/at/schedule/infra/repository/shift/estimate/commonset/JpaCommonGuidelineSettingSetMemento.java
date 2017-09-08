/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.at.schedule.infra.repository.shift.estimate.commonset;

import java.util.List;
import java.util.stream.Collectors;

import nts.uk.ctx.at.schedule.dom.shift.estimate.commonset.CommonGuidelineSettingSetMemento;
import nts.uk.ctx.at.schedule.dom.shift.estimate.commonset.EstimatedAlarmColor;
import nts.uk.ctx.at.schedule.dom.shift.estimate.commonset.ReferenceCondition;
import nts.uk.ctx.at.schedule.infra.entity.shift.estimate.commonset.KscstEstAlarmColor;
import nts.uk.ctx.at.schedule.infra.entity.shift.estimate.commonset.KscstEstAlarmColorPK;
import nts.uk.ctx.at.schedule.infra.entity.shift.estimate.commonset.KscstEstComSet;
import nts.uk.ctx.at.shared.dom.common.CompanyId;

/**
 * The Class JpaCommonGuidelineSettingSetMemento.
 */
public class JpaCommonGuidelineSettingSetMemento implements CommonGuidelineSettingSetMemento {

	/** The entity. */
	private KscstEstComSet entity;

	/**
	 * Instantiates a new jpa common guideline setting set memento.
	 *
	 * @param entity
	 *            the entity
	 */
	public JpaCommonGuidelineSettingSetMemento(KscstEstComSet entity) {
		this.entity = entity;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.at.schedule.dom.shift.estimate.guideline.
	 * CommonGuidelineSettingSetMemento#setCompanyId(nts.uk.ctx.at.shared.dom.
	 * common.CompanyId)
	 */
	@Override
	public void setCompanyId(CompanyId companyId) {
		this.entity.setCid(companyId.v());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.at.schedule.dom.shift.estimate.guideline.
	 * CommonGuidelineSettingSetMemento#setAlarmColors(java.util.List)
	 */
	@Override
	public void setAlarmColors(List<EstimatedAlarmColor> alarmColors) {
		this.entity.setKscstEstAlarmColors(alarmColors.stream()
				.map(item -> new KscstEstAlarmColor(new KscstEstAlarmColorPK(this.entity.getCid(),
						item.getGuidelineCondition().value), item.getColor().v()))
				.collect(Collectors.toList()));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.at.schedule.dom.shift.estimate.guideline.
	 * CommonGuidelineSettingSetMemento#setEstimateTime(nts.uk.ctx.at.schedule.
	 * dom.shift.estimate.guideline.ReferenceCondition)
	 */
	@Override
	public void setEstimateTime(ReferenceCondition estimateTime) {
		this.entity.setTimeYearlyDispCond(estimateTime.getYearlyDisplayCondition().value);
		this.entity.setTimeMonthlyDispCond(estimateTime.getMonthlyDisplayCondition().value);
		this.entity.setTimeAlarmCheckCond(estimateTime.getAlarmCheckCondition().value);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.at.schedule.dom.shift.estimate.guideline.
	 * CommonGuidelineSettingSetMemento#setEstimatePrice(nts.uk.ctx.at.schedule.
	 * dom.shift.estimate.guideline.ReferenceCondition)
	 */
	@Override
	public void setEstimatePrice(ReferenceCondition estimatePrice) {
		this.entity.setPriceYearlyDispCond(estimatePrice.getYearlyDisplayCondition().value);
		this.entity.setPriceMonthlyDispCond(estimatePrice.getMonthlyDisplayCondition().value);
		this.entity.setPriceAlarmCheckCond(estimatePrice.getAlarmCheckCondition().value);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.at.schedule.dom.shift.estimate.guideline.
	 * CommonGuidelineSettingSetMemento#setEstimateNumberOfDays(nts.uk.ctx.at.
	 * schedule.dom.shift.estimate.guideline.ReferenceCondition)
	 */
	@Override
	public void setEstimateNumberOfDays(ReferenceCondition estimateNumberOfDays) {
		this.entity.setNumOfDayYearlyDispCond(estimateNumberOfDays.getYearlyDisplayCondition().value);
		this.entity.setNumOfDayMonthlyDispCond(estimateNumberOfDays.getMonthlyDisplayCondition().value);
		this.entity.setNumOfDayAlarmCheckCond(estimateNumberOfDays.getAlarmCheckCondition().value);
	}

}
