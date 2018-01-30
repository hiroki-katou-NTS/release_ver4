/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.at.schedule.infra.repository.shift.estimate.aggregateset;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import nts.arc.layer.infra.data.JpaRepository;
import nts.gul.collection.CollectionUtil;
import nts.uk.ctx.at.schedule.dom.shift.estimate.aggregateset.AggregateSettingSetMemento;
import nts.uk.ctx.at.schedule.dom.shift.estimate.aggregateset.ExtraTimeItemNo;
import nts.uk.ctx.at.schedule.dom.shift.estimate.aggregateset.MonthlyWorkingDaySetting;
import nts.uk.ctx.at.schedule.infra.entity.shift.estimate.aggregateset.KscstEstAggregateSet;
import nts.uk.ctx.at.schedule.infra.entity.shift.estimate.aggregateset.KscstPerCostExtraItem;
import nts.uk.ctx.at.schedule.infra.entity.shift.estimate.aggregateset.KscstPerCostExtraItemPK;
import nts.uk.ctx.at.shared.dom.common.CompanyId;

/**
 * The Class JpaAggregateSettingSetMemento.
 */
public class JpaAggregateSettingSetMemento extends JpaRepository implements AggregateSettingSetMemento {

	/** The kscst est aggregate set. */
	private KscstEstAggregateSet kscstEstAggregateSet;

	/**
	 * Instantiates a new jpa aggregate setting set memento.
	 *
	 * @param entity the entity
	 */
	public JpaAggregateSettingSetMemento(KscstEstAggregateSet entity) {
		if (CollectionUtil.isEmpty(entity.getKscstPerCostExtraItem())) {
			entity.setKscstPerCostExtraItem(new ArrayList<>());
		}
		this.kscstEstAggregateSet = entity;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.at.schedule.dom.shift.estimate.aggregateset.
	 * AggregateSettingSetMemento#setCompanyId(nts.uk.ctx.at.shared.dom.common.
	 * CompanyId)
	 */
	@Override
	public void setCompanyId(CompanyId companyId) {
		this.kscstEstAggregateSet.setCid(companyId.v());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.at.schedule.dom.shift.estimate.aggregateset.
	 * AggregateSettingSetMemento#setPremiumNo(java.util.List)
	 */
	@Override
	public void setPremiumNo(List<ExtraTimeItemNo> premiumNo) {
		String companyId = this.kscstEstAggregateSet.getCid();

		// convert map entity
		Map<KscstPerCostExtraItemPK, KscstPerCostExtraItem> mapEntity = this.kscstEstAggregateSet
				.getKscstPerCostExtraItem().stream().collect(Collectors.toMap(
						item -> ((KscstPerCostExtraItem) item).getKscstPerCostExtraItemPK(), Function.identity()));

		// set item list
		this.kscstEstAggregateSet.setKscstPerCostExtraItem(premiumNo.stream().map(item -> {
			KscstPerCostExtraItemPK pk = new KscstPerCostExtraItemPK(companyId, item.v());
			if (mapEntity.containsKey(pk)) {
				return mapEntity.get(pk);
			}
			return new KscstPerCostExtraItem(pk);
		}).collect(Collectors.toList()));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.at.schedule.dom.shift.estimate.aggregateset.
	 * AggregateSettingSetMemento#setMonthlyWorkingDaySetting(nts.uk.ctx.at.
	 * schedule.dom.shift.estimate.aggregateset.MonthlyWorkingDaySetting)
	 */
	@Override
	public void setMonthlyWorkingDaySetting(MonthlyWorkingDaySetting monthlyWorkingDaySetting) {
		this.kscstEstAggregateSet.setHalfDayAtr(monthlyWorkingDaySetting.getHalfDayAtr().value);
		this.kscstEstAggregateSet.setYearHdAtr(monthlyWorkingDaySetting.getYearHdAtr().value);
		this.kscstEstAggregateSet.setSphdAtr(monthlyWorkingDaySetting.getSphdAtr().value);
		this.kscstEstAggregateSet.setHavyHdAtr(monthlyWorkingDaySetting.getHavyHdAtr().value);
	}

}
