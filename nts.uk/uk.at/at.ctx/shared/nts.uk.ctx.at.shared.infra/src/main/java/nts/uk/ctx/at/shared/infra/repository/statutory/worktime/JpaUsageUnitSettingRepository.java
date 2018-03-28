/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.at.shared.infra.repository.statutory.worktime;

import java.util.Optional;

import javax.ejb.Stateless;

import nts.arc.layer.infra.data.JpaRepository;
import nts.uk.ctx.at.shared.dom.statutory.worktime.UsageUnitSetting;
import nts.uk.ctx.at.shared.dom.statutory.worktime.UsageUnitSettingRepository;
import nts.uk.ctx.at.shared.infra.entity.statutory.worktime_new.KuwstUsageUnitWtSet;

/**
 * The Class JpaUsageUnitSettingRepository.
 */
@Stateless
public class JpaUsageUnitSettingRepository extends JpaRepository implements UsageUnitSettingRepository {

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.at.shared.dom.employment.statutory.worktime.
	 * UsageUnitSettingRepository#update(nts.uk.ctx.at.shared.dom.employment.
	 * statutory.worktime.UsageUnitSetting)
	 */
	@Override
	public void update(UsageUnitSetting setting) {
		this.commandProxy().update(this.updateEntity(this.toEntity(setting)));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.at.shared.dom.employment.statutory.worktime.
	 * UsageUnitSettingRepository#create(nts.uk.ctx.at.shared.dom.employment.
	 * statutory.worktime.UsageUnitSetting)
	 */
	@Override
	public void create(UsageUnitSetting setting) {
		this.commandProxy().insert(this.toEntity(setting));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.at.shared.dom.employment.statutory.worktime.
	 * UsageUnitSettingRepository#find(java.lang.String)
	 */
	@Override
	public Optional<UsageUnitSetting> findByCompany(String companyId) {
		return this.queryProxy().find(companyId, KuwstUsageUnitWtSet.class).map(setting -> this.toDomain(setting));
	}

	/**
	 * To domain.
	 *
	 * @param entity the entity
	 * @return the usage unit setting
	 */
	private UsageUnitSetting toDomain(KuwstUsageUnitWtSet entity) {
		return new UsageUnitSetting(new JpaUsageUnitSettingGetMemento(entity));
	}

	/**
	 * To entitỵ̣̣.
	 *
	 * @param domain the domain
	 * @return the juuwtst usage unit wt set
	 */
	private KuwstUsageUnitWtSet toEntity(UsageUnitSetting domain) {
		KuwstUsageUnitWtSet entity = new KuwstUsageUnitWtSet();
		domain.saveToMemento(new JpaUsageUnitSettingSetMemento(entity));
		return entity;
	}

	/**
	 * Update entity.
	 *
	 * @param entity the entity
	 * @return the juuwtst usage unit wt set
	 */
	private KuwstUsageUnitWtSet updateEntity(KuwstUsageUnitWtSet entity) {
		KuwstUsageUnitWtSet updatedEntity = this.queryProxy().find(entity.getCid(), KuwstUsageUnitWtSet.class)
				.get();
		updatedEntity.setIsEmp(entity.getIsEmp());
		updatedEntity.setIsEmpt(entity.getIsEmpt());
		updatedEntity.setIsWkp(entity.getIsWkp());
		return updatedEntity;
	}

}
