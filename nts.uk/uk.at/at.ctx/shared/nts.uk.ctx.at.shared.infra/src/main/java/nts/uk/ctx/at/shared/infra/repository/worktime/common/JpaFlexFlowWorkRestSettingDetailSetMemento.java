/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.at.shared.infra.repository.worktime.common;

import nts.uk.ctx.at.shared.dom.worktime.common.BooleanGetAtr;
import nts.uk.ctx.at.shared.dom.worktime.flowset.FlowFixedRestSet;
import nts.uk.ctx.at.shared.dom.worktime.flowset.FlowRestSet;
import nts.uk.ctx.at.shared.dom.worktime.flowset.FlowWorkRestSettingDetailSetMemento;
import nts.uk.ctx.at.shared.infra.entity.worktime.flexset.KshmtFlexRestSet;
import nts.uk.ctx.at.shared.infra.entity.worktime.flexset.KshmtFlexRestSetPK;

/**
 * The Class JpaFlexFlowWorkRestSettingDetailSetMemento.
 */
public class JpaFlexFlowWorkRestSettingDetailSetMemento implements FlowWorkRestSettingDetailSetMemento {
	

	/** The entity. */
	private KshmtFlexRestSet entity;
	
	/**
	 * Instantiates a new jpa flex flow work rest setting detail set memento.
	 *
	 * @param entity the entity
	 */
	public JpaFlexFlowWorkRestSettingDetailSetMemento(KshmtFlexRestSet entity) {
		super();
		if(entity.getKshmtFlexRestSetPK() == null){
			entity.setKshmtFlexRestSetPK(new KshmtFlexRestSetPK());
		}
		this.entity = entity;
	}



	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.at.shared.dom.worktime.common.
	 * FlowWorkRestSettingDetailSetMemento#setFlowRestSetting(nts.uk.ctx.at.
	 * shared.dom.worktime.common.FlowRestSet)
	 */
	@Override
	public void setFlowRestSetting(FlowRestSet set) {
		set.saveToMemento(new JpaFlexFlowRestSetSetMemento(this.entity));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.at.shared.dom.worktime.common.
	 * FlowWorkRestSettingDetailSetMemento#setFlowFixedRestSetting(nts.uk.ctx.at
	 * .shared.dom.worktime.common.FlowFixedRestSet)
	 */
	@Override
	public void setFlowFixedRestSetting(FlowFixedRestSet set) {
		set.saveToMemento(new JpaFlexFlowFixedRestSetSetMemento(this.entity));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.at.shared.dom.worktime.common.
	 * FlowWorkRestSettingDetailSetMemento#setUsePluralWorkRestTime(boolean)
	 */
	@Override
	public void setUsePluralWorkRestTime(boolean val) {
		this.entity.setUsePluralWorkRestTime(BooleanGetAtr.getAtrByBoolean(val));
	}

}
