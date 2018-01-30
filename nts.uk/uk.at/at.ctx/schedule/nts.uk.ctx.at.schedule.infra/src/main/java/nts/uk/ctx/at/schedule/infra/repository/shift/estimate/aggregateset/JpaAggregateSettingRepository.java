/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.at.schedule.infra.repository.shift.estimate.aggregateset;

import java.util.Optional;

import javax.ejb.Stateless;

import nts.arc.layer.infra.data.JpaRepository;
import nts.uk.ctx.at.schedule.dom.shift.estimate.aggregateset.AggregateSetting;
import nts.uk.ctx.at.schedule.dom.shift.estimate.aggregateset.AggregateSettingRepository;
import nts.uk.ctx.at.schedule.infra.entity.shift.estimate.aggregateset.KscstEstAggregateSet;
import nts.uk.ctx.at.shared.dom.common.CompanyId;

/**
 * The Class JpaAggregateSettingRepository.
 */
@Stateless
public class JpaAggregateSettingRepository extends JpaRepository implements AggregateSettingRepository {

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.at.schedule.dom.shift.estimate.aggregateset.
	 * AggregateSettingRepository#findByCID(nts.uk.ctx.at.shared.dom.common.
	 * CompanyId)
	 */
	@Override
	public Optional<AggregateSetting> findByCID(CompanyId companyId) {
		return this.queryProxy().find(companyId, KscstEstAggregateSet.class).map(e -> toDomain(e));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.at.schedule.dom.shift.estimate.aggregateset.
	 * AggregateSettingRepository#update(nts.uk.ctx.at.schedule.dom.shift.
	 * estimate.aggregateset.AggregateSetting)
	 */
	@Override
	public void save(AggregateSetting domain) {
		// find entity
		Optional<KscstEstAggregateSet> opt = this.queryProxy().find(domain.getCompanyId().v(),
				KscstEstAggregateSet.class);

		// update mode
		if (opt.isPresent()) {
			this.commandProxy().update(toEntity(domain, opt.get()));
		}
		// add mode
		else {
			this.commandProxy().insert(toEntity(domain, new KscstEstAggregateSet()));
		}
	}

	/**
	 * To entity.
	 *
	 * @param domain the domain
	 * @param entity the entity
	 * @return the kscst est aggregate set
	 */
	private KscstEstAggregateSet toEntity(AggregateSetting domain, KscstEstAggregateSet entity) {
		domain.saveToMemento(new JpaAggregateSettingSetMemento(entity));
		return entity;
	}

	/**
	 * To domain.
	 *
	 * @param entity the entity
	 * @return the aggregate setting
	 */
	private AggregateSetting toDomain(KscstEstAggregateSet entity) {
		AggregateSetting domain = new AggregateSetting(new JpaAggregateSettingGetMemento(entity));
		return domain;
	}
}
