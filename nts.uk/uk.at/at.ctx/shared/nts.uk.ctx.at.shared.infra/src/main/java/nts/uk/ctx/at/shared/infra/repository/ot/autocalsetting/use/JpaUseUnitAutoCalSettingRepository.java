/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.at.shared.infra.repository.ot.autocalsetting.use;

import java.util.Optional;

import javax.ejb.Stateless;

import nts.arc.layer.infra.data.JpaRepository;
import nts.uk.ctx.at.shared.dom.ot.autocalsetting.use.UseUnitAutoCalSetting;
import nts.uk.ctx.at.shared.dom.ot.autocalsetting.use.UseUnitAutoCalSettingRepository;
import nts.uk.ctx.at.shared.infra.entity.ot.autocalsetting.use.KshmtAutoUseUnitSet;

/**
 * The Class JpaUseUnitAutoCalSettingRepository.
 */
@Stateless
public class JpaUseUnitAutoCalSettingRepository  extends JpaRepository implements UseUnitAutoCalSettingRepository {

	/* (non-Javadoc)
	 * @see nts.uk.ctx.at.schedule.dom.shift.autocalsetting.UseUnitAutoCalSettingRepository#update(nts.uk.ctx.at.schedule.dom.shift.autocalsetting.UseUnitAutoCalSetting)
	 */
	@Override
	public void update(UseUnitAutoCalSetting useUnitAutoCalSetting) {
		Optional<KshmtAutoUseUnitSet> optional = this.queryProxy().find(useUnitAutoCalSetting.getCompanyId().v(), KshmtAutoUseUnitSet.class);

		if (!optional.isPresent()) {
			throw new RuntimeException("Unit Auto not existed.");
		}

		KshmtAutoUseUnitSet entity = optional.get();
		useUnitAutoCalSetting.saveToMemento(new JpaUseUnitAutoCalSettingSetMemento(entity));
		this.commandProxy().update(entity);		
		
	}

	/* (non-Javadoc)
	 * @see nts.uk.ctx.at.schedule.dom.shift.autocalsetting.UseUnitAutoCalSettingRepository#getAllUseUnitAutoCalSetting(java.lang.String)
	 */
	@Override
	public Optional<UseUnitAutoCalSetting> getAllUseUnitAutoCalSetting(String companyId) {

		Optional<KshmtAutoUseUnitSet> optKshmtAutoUseUnitSet = this.queryProxy().find(companyId, KshmtAutoUseUnitSet.class);

		if (!optKshmtAutoUseUnitSet.isPresent()) {
			return Optional.empty();
		}
		return Optional.of(new UseUnitAutoCalSetting(new JpaUseUnitAutoCalSettingGetMemento(optKshmtAutoUseUnitSet.get())));
	}
	
}
