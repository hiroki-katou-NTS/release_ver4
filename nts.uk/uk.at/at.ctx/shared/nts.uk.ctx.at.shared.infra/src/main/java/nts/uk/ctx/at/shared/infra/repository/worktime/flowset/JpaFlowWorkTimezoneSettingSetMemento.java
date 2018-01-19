/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.at.shared.infra.repository.worktime.flowset;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import nts.gul.collection.CollectionUtil;
import nts.uk.ctx.at.shared.dom.common.timerounding.TimeRoundingSetting;
import nts.uk.ctx.at.shared.dom.worktime.flowset.FlWtzSettingSetMemento;
import nts.uk.ctx.at.shared.dom.worktime.flowset.FlowOTTimezone;
import nts.uk.ctx.at.shared.infra.entity.worktime.flowset.KshmtFlowWorkSet;
import nts.uk.ctx.at.shared.infra.entity.worktime.flowset.KshmtOtTimeZone;
import nts.uk.ctx.at.shared.infra.entity.worktime.flowset.KshmtOtTimeZonePK;

/**
 * The Class JpaFlowWorkTimezoneSettingSetMemento.
 */
public class JpaFlowWorkTimezoneSettingSetMemento implements FlWtzSettingSetMemento {

	/** The entity. */
	private KshmtFlowWorkSet entity;

	/** The company id. */
	private String companyId;

	/** The work time cd. */
	private String workTimeCd;

	/**
	 * Instantiates a new jpa flow work timezone setting set memento.
	 *
	 * @param entity
	 *            the entity
	 */
	public JpaFlowWorkTimezoneSettingSetMemento(KshmtFlowWorkSet entity) {
		super();
		this.entity = entity;
		if (CollectionUtil.isEmpty(this.entity.getLstKshmtOtTimeZone())) {
			this.entity.setLstKshmtOtTimeZone(new ArrayList<>());
		}
		this.companyId = this.entity.getKshmtFlowWorkSetPK().getCid();
		this.workTimeCd = this.entity.getKshmtFlowWorkSetPK().getWorktimeCd();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.at.shared.dom.worktime.flowset.FlWtzSettingSetMemento#
	 * setWorkTimeRounding(nts.uk.ctx.at.shared.dom.common.timerounding.
	 * TimeRoundingSetting)
	 */
	@Override
	public void setWorkTimeRounding(TimeRoundingSetting trSet) {
		this.entity.getKshmtFlowTimeZone().setUnit(trSet.getRoundingTime().value);
		this.entity.getKshmtFlowTimeZone().setRounding(trSet.getRounding().value);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.at.shared.dom.worktime.flowset.FlWtzSettingSetMemento#
	 * setLstOTTimezone(java.util.List)
	 */
	@Override
	public void setLstOTTimezone(List<FlowOTTimezone> lstTzone) {
		if (CollectionUtil.isEmpty(lstTzone)) {
			this.entity.setLstKshmtOtTimeZone(new ArrayList<>());
			return;
		}

		List<KshmtOtTimeZone> lstEntity = this.entity.getLstKshmtOtTimeZone();
		if (CollectionUtil.isEmpty(lstEntity)) {
			lstEntity = new ArrayList<>();
		}

		// convert map entity
		Map<KshmtOtTimeZonePK, KshmtOtTimeZone> mapEntity = lstEntity.stream()
				.collect(Collectors.toMap(KshmtOtTimeZone::getKshmtOtTimeZonePK, Function.identity()));

		// set list entity
		this.entity.setLstKshmtOtTimeZone(lstTzone.stream().map(domain -> {
			// newPk
			KshmtOtTimeZonePK pk = new KshmtOtTimeZonePK();
			pk.setCid(companyId);
			pk.setWorktimeCd(workTimeCd);
			pk.setWorktimeNo(domain.getWorktimeNo());

			// find entity if existed, else new entity
			KshmtOtTimeZone entity = mapEntity.get(pk);
			if (entity == null) {
				entity = new KshmtOtTimeZone();
				entity.setKshmtOtTimeZonePK(pk);
			}

			// save to memento
			domain.saveToMemento(new JpaFlowOTTimezoneSetMemento(entity));

			return entity;
		}).collect(Collectors.toList()));
	}
}
