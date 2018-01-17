/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.at.shared.infra.repository.worktime.flowset;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import nts.gul.collection.CollectionUtil;
import nts.uk.ctx.at.shared.dom.worktime.common.FlowWorkRestTimezone;
import nts.uk.ctx.at.shared.dom.worktime.flowset.FlOffdayWtzGetMemento;
import nts.uk.ctx.at.shared.dom.worktime.flowset.FlowWorkHolidayTimeZone;
import nts.uk.ctx.at.shared.infra.entity.worktime.flowset.KshmtFlowWorkSet;

/**
 * The Class JpaFlowOffdayWorkTimezoneGetMemento.
 */
public class JpaFlowOffdayWorkTimezoneGetMemento implements FlOffdayWtzGetMemento {

	/** The entity. */
	private KshmtFlowWorkSet entity;
	
	/**
	 * Instantiates a new jpa flow offday work timezone get memento.
	 *
	 * @param entity the entity
	 */
	public JpaFlowOffdayWorkTimezoneGetMemento(KshmtFlowWorkSet entity) {
		super();
		this.entity = entity;
		if (CollectionUtil.isEmpty(this.entity.getLstKshmtFworkHolidayTime())) {
			this.entity.setLstKshmtFworkHolidayTime(new ArrayList<>());
		}
	}
	
	/* (non-Javadoc)
	 * @see nts.uk.ctx.at.shared.dom.worktime.flowset.FlOffdayWtzGetMemento#getRestTimeZone()
	 */
	@Override
	public FlowWorkRestTimezone getRestTimeZone() {
		return new FlowWorkRestTimezone(new JpaFlowWorkRestTimezoneGetMemento(this.entity.getFlowOffDayWorkRtSet()));
	}

	/* (non-Javadoc)
	 * @see nts.uk.ctx.at.shared.dom.worktime.flowset.FlOffdayWtzGetMemento#getLstWorkTimezone()
	 */
	@Override
	public List<FlowWorkHolidayTimeZone> getLstWorkTimezone() {
		return this.entity.getLstKshmtFworkHolidayTime().stream()
				.map(entity -> new FlowWorkHolidayTimeZone(new JpaFlowWorkHolidayTimeZoneGetMemento(entity)))
				.sorted((item1, item2) -> item1.getWorktimeNo().compareTo(item2.getWorktimeNo()))
				.collect(Collectors.toList());
	}

}
