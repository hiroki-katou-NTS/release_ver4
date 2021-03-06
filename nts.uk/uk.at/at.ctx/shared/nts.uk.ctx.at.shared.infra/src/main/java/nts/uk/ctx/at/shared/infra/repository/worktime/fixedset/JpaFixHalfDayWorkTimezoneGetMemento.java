/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.at.shared.infra.repository.worktime.fixedset;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import nts.gul.collection.CollectionUtil;
import nts.uk.ctx.at.shared.dom.worktime.common.AmPmAtr;
import nts.uk.ctx.at.shared.dom.worktime.common.FixedWorkTimezoneSet;
import nts.uk.ctx.at.shared.dom.worktime.common.TimezoneOfFixedRestTimeSet;
import nts.uk.ctx.at.shared.dom.worktime.fixedset.FixHalfDayWorkTimezoneGetMemento;
import nts.uk.ctx.at.shared.infra.entity.worktime.fixedset.KshmtFixedWorkSetPK;
import nts.uk.ctx.at.shared.infra.entity.worktime.fixedset.KshmtWtFix;
import nts.uk.ctx.at.shared.infra.entity.worktime.fixedset.KshmtWtFixBrWekTs;
import nts.uk.ctx.at.shared.infra.entity.worktime.fixedset.KshmtWtFixOverTs;
import nts.uk.ctx.at.shared.infra.entity.worktime.fixedset.KshmtWtFixWorkTs;

/**
 * The Class JpaFixHalfDayWorkTimezoneGetMemento.
 */
public class JpaFixHalfDayWorkTimezoneGetMemento implements FixHalfDayWorkTimezoneGetMemento {

	/** The entity. */
	private KshmtWtFix entity;

	/** The type. */
	private AmPmAtr type;

	/**
	 * Instantiates a new jpa fix half day work timezone get memento.
	 *
	 * @param entity
	 *            the entity
	 * @param type
	 *            the type
	 */
	public JpaFixHalfDayWorkTimezoneGetMemento(KshmtWtFix entity, AmPmAtr type) {
		super();
		if (entity.getKshmtFixedWorkSetPK() == null) {
			entity.setKshmtFixedWorkSetPK(new KshmtFixedWorkSetPK());
		}
		this.entity = entity;
		this.type = type;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.at.shared.dom.worktime.fixedset.
	 * FixHalfDayWorkTimezoneGetMemento#getRestTimezone()
	 */
	@Override
	public TimezoneOfFixedRestTimeSet getRestTimezone() {
		// KSHMT_WT_FIX_BR_WEK_TS
		if (CollectionUtil.isEmpty(this.entity.getKshmtFixedHalfRestSets())) {
			this.entity.setKshmtFixedHalfRestSets(new ArrayList<>());
		}
		List<KshmtWtFixBrWekTs> kshmtFixedHalfRestSets = this.entity.getKshmtFixedHalfRestSets().stream()
				.sorted((item1, item2) -> item1.getStartTime() - item2.getEndTime())
				.filter(entity -> entity.getKshmtFixedHalfRestSetPK().getAmPmAtr() == this.type.value)
				.collect(Collectors.toList());
		return new TimezoneOfFixedRestTimeSet(new JpaFixRestHalfdayTzGetMemento(kshmtFixedHalfRestSets));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.at.shared.dom.worktime.fixedset.
	 * FixHalfDayWorkTimezoneGetMemento#getWorkTimezone()
	 */
	@Override
	public FixedWorkTimezoneSet getWorkTimezone() {
		// KSHMT_WT_FIX_WORK_TS ??????????????????????????????(??????)
		if (CollectionUtil.isEmpty(this.entity.getKshmtFixedWorkTimeSets())) {
			this.entity.setKshmtFixedWorkTimeSets(new ArrayList<>());
		}
		List<KshmtWtFixWorkTs> kshmtFixedWorkTimeSets = this.entity.getKshmtFixedWorkTimeSets().stream()
				.sorted((item1, item2) -> item1.getTimeStr() - item2.getTimeStr())
				.filter(entity -> entity.getKshmtFixedWorkTimeSetPK().getAmPmAtr() == this.type.value)
				.collect(Collectors.toList());
		// KSHMT_WT_FIX_OVER_TS ??????????????????????????????
		if (CollectionUtil.isEmpty(this.entity.getKshmtFixedOtTimeSets())) {
			this.entity.setKshmtFixedOtTimeSets(new ArrayList<>());
		}
		List<KshmtWtFixOverTs> kshmtFixedOtTimeSets = this.entity.getKshmtFixedOtTimeSets().stream()
				.sorted((item1, item2) -> item1.getTimeStr() - item2.getTimeStr())
				.filter(entity -> entity.getKshmtFixedOtTimeSetPK().getAmPmAtr() == this.type.value)
				.collect(Collectors.toList());
		return new FixedWorkTimezoneSet(new JpaFixedWorkTimezoneSetGetMemento(kshmtFixedWorkTimeSets, kshmtFixedOtTimeSets));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.at.shared.dom.worktime.fixedset.
	 * FixHalfDayWorkTimezoneGetMemento#getDayAtr()
	 */
	@Override
	public AmPmAtr getDayAtr() {
		return this.type;
	}

}
