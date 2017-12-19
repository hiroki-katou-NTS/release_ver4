/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.at.shared.infra.repository.worktime.common;

import nts.uk.ctx.at.shared.dom.worktime.common.BooleanGetAtr;
import nts.uk.ctx.at.shared.dom.worktime.common.FlowFixedRestSet;
import nts.uk.ctx.at.shared.dom.worktime.common.FlowRestSet;
import nts.uk.ctx.at.shared.dom.worktime.common.FlowWorkRestSettingDetailGetMemento;
import nts.uk.ctx.at.shared.infra.entity.worktime.flexset.KshmtFlexRestSet;

/**
 * The Class JpaFlexFlowWorkRestSettingDetailGetMemento.
 */
public class JpaFlexFlowWorkRestSettingDetailGetMemento implements FlowWorkRestSettingDetailGetMemento {
	

	/** The entity. */
	private KshmtFlexRestSet entity;
	
	/**
	 * Instantiates a new jpa flex flow work rest setting detail get memento.
	 *
	 * @param entity the entity
	 */
	public JpaFlexFlowWorkRestSettingDetailGetMemento(KshmtFlexRestSet entity) {
		super();
		this.entity = entity;
	}

	/* (non-Javadoc)
	 * @see nts.uk.ctx.at.shared.dom.worktime.common.FlowWorkRestSettingDetailGetMemento#getFlowRestSetting()
	 */
	@Override
	public FlowRestSet getFlowRestSetting() {
		return new FlowRestSet(new JpaFlexFlowRestSetGetMemento(this.entity));
	}

	/* (non-Javadoc)
	 * @see nts.uk.ctx.at.shared.dom.worktime.common.FlowWorkRestSettingDetailGetMemento#getFlowFixedRestSetting()
	 */
	@Override
	public FlowFixedRestSet getFlowFixedRestSetting() {
		return new FlowFixedRestSet(new JpaFlexFlowFixedRestSetGetMemento(this.entity));
	}

	/* (non-Javadoc)
	 * @see nts.uk.ctx.at.shared.dom.worktime.common.FlowWorkRestSettingDetailGetMemento#getUsePluralWorkRestTime()
	 */
	@Override
	public boolean getUsePluralWorkRestTime() {
		return BooleanGetAtr.getAtrByInteger(this.entity.getUsePluralWorkRestTime());
	}

}
