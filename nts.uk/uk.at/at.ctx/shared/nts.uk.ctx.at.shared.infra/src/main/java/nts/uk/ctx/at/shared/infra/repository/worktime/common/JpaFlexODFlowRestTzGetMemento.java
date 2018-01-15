/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.at.shared.infra.repository.worktime.common;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import nts.gul.collection.CollectionUtil;
import nts.uk.ctx.at.shared.dom.worktime.common.BooleanGetAtr;
import nts.uk.ctx.at.shared.dom.worktime.common.FlowRestSetting;
import nts.uk.ctx.at.shared.dom.worktime.common.FlowRestTimezoneGetMemento;
import nts.uk.ctx.at.shared.infra.entity.worktime.KshmtFlexOdRtSet;

/**
 * The Class JpaFlexODFlowRestTzGetMemento.
 */
public class JpaFlexODFlowRestTzGetMemento implements FlowRestTimezoneGetMemento{
	
	/** The entity. */
	private KshmtFlexOdRtSet entity;


	/**
	 * Instantiates a new jpa flex OD flow rest tz get memento.
	 *
	 * @param entity the entity
	 */
	public JpaFlexODFlowRestTzGetMemento(KshmtFlexOdRtSet entity) {
		super();
		this.entity = entity;
	}

	/* (non-Javadoc)
	 * @see nts.uk.ctx.at.shared.dom.worktime.common.FlowRestTimezoneGetMemento#getFlowRestSet()
	 */
	@Override
	public List<FlowRestSetting> getFlowRestSet() {
		if (CollectionUtil.isEmpty(this.entity.getKshmtFlexOdRestSets())) {
			return new ArrayList<>();
		}
		return this.entity.getKshmtFlexOdRestSets().stream()
				.map(entity -> new FlowRestSetting(new JpaFlexODFlowRestGetMemento(entity)))
				.collect(Collectors.toList());
	}

	/* (non-Javadoc)
	 * @see nts.uk.ctx.at.shared.dom.worktime.common.FlowRestTimezoneGetMemento#getUseHereAfterRestSet()
	 */
	@Override
	public boolean getUseHereAfterRestSet() {
		return BooleanGetAtr.getAtrByInteger(this.entity.getUseRestAfterSet());
	}

	/* (non-Javadoc)
	 * @see nts.uk.ctx.at.shared.dom.worktime.common.FlowRestTimezoneGetMemento#getHereAfterRestSet()
	 */
	@Override
	public FlowRestSetting getHereAfterRestSet() {
		return new FlowRestSetting(new JpaFlexODFlowRestSettingGetMemento(entity));
	}
	

}
