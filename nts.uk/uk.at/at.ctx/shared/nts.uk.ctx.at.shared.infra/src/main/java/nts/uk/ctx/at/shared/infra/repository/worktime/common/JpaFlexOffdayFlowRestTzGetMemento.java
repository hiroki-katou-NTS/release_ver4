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
import nts.uk.ctx.at.shared.infra.entity.worktime.flexset.KshmtFlexOdRestSet;
import nts.uk.ctx.at.shared.infra.entity.worktime.flexset.KshmtFlexOdRestTime;

/**
 * The Class JpaFlexOffdayFlowRestTzGetMemento.
 */
public class JpaFlexOffdayFlowRestTzGetMemento implements FlowRestTimezoneGetMemento{
	
	/** The entity. */
	private KshmtFlexOdRestTime entity;
	
	/** The entity flow rests. */
	private List<KshmtFlexOdRestSet> entityFlowRests;
	

	/**
	 * Instantiates a new jpa flex offday flow rest tz get memento.
	 *
	 * @param entity the entity
	 * @param entityFlowRests the entity flow rests
	 */
	public JpaFlexOffdayFlowRestTzGetMemento(KshmtFlexOdRestTime entity, List<KshmtFlexOdRestSet> entityFlowRests) {
		super();
		this.entity = entity;
		this.entityFlowRests = entityFlowRests;
	}


	/* (non-Javadoc)
	 * @see nts.uk.ctx.at.shared.dom.worktime.common.FlowRestTimezoneGetMemento#getFlowRestSet()
	 */
	@Override
	public List<FlowRestSetting> getFlowRestSet() {
		if(CollectionUtil.isEmpty(this.entityFlowRests)){
			return new ArrayList<>();
		}
		return this.entityFlowRests.stream()
				.map(entity -> new FlowRestSetting(new JpaFlexOffdayFlowRestGetMemento(entity)))
				.collect(Collectors.toList());
	}


	/* (non-Javadoc)
	 * @see nts.uk.ctx.at.shared.dom.worktime.common.FlowRestTimezoneGetMemento#getUseHereAfterRestSet()
	 */
	@Override
	public boolean getUseHereAfterRestSet() {
		return BooleanGetAtr.getAtrByInteger(this.entity.getUseRestAfterSet());
	}


	@Override
	public FlowRestSetting getHereAfterRestSet() {
		return new FlowRestSetting(new JpaFlexOffdayFlowRestSettingGetMemento(this.entity));
	}
	

}
