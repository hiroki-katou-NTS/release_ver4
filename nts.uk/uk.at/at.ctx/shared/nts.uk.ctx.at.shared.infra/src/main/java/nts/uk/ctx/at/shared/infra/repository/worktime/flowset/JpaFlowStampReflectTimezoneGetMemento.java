/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.at.shared.infra.repository.worktime.flowset;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import nts.gul.collection.CollectionUtil;
import nts.uk.ctx.at.shared.dom.worktime.common.StampReflectTimezone;
import nts.uk.ctx.at.shared.dom.worktime.flowset.FlStampReflectTzGetMemento;
import nts.uk.ctx.at.shared.dom.worktime.flowset.ReflectReferenceTwoWorkTime;
import nts.uk.ctx.at.shared.infra.entity.worktime.flowset.KshmtFstampReflectTime;

/**
 * The Class JpaFlowStampReflectTimezoneGetMemento.
 */
public class JpaFlowStampReflectTimezoneGetMemento implements FlStampReflectTzGetMemento {

	/** The entity. */
	private KshmtFstampReflectTime entity;
	
	/**
	 * Instantiates a new jpa flow stamp reflect timezone get memento.
	 *
	 * @param entity the entity
	 */
	public JpaFlowStampReflectTimezoneGetMemento(KshmtFstampReflectTime entity) {
		super();
		this.entity = entity;
		if (CollectionUtil.isEmpty(this.entity.getLstKshmtFlowStampReflect())) {
			this.entity.setLstKshmtFlowStampReflect(new ArrayList<>());
		}
	}
	
	/* (non-Javadoc)
	 * @see nts.uk.ctx.at.shared.dom.worktime.flowset.FlStampReflectTzGetMemento#getTwoTimesWorkReflectBasicTime()
	 */
	@Override
	public ReflectReferenceTwoWorkTime getTwoTimesWorkReflectBasicTime() {
		return new ReflectReferenceTwoWorkTime(this.entity.getTwoReflectBasicTime());
	}

	/* (non-Javadoc)
	 * @see nts.uk.ctx.at.shared.dom.worktime.flowset.FlStampReflectTzGetMemento#getStampReflectTimezone()
	 */
	@Override
	public List<StampReflectTimezone> getStampReflectTimezone() {
		return this.entity.getLstKshmtFlowStampReflect().stream()
				.map(entity -> new StampReflectTimezone(new JpaStampReflectTimezoneGetMemento(entity)))
				.sorted((item1, item2) -> item1.getStartTime().compareTo(item2.getStartTime()))
				.collect(Collectors.toList());
	}

}
