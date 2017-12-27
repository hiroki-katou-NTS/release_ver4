/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.at.shared.infra.repository.worktime.common;

import nts.uk.ctx.at.shared.dom.worktime.common.GoOutTimezoneRoundingSet;
import nts.uk.ctx.at.shared.dom.worktime.common.TotalRoundingSet;
import nts.uk.ctx.at.shared.dom.worktime.common.WorkTimezoneGoOutSetSetMemento;
import nts.uk.ctx.at.shared.infra.entity.worktime.common.KshmtWorktimeCommonSet;

/**
 * The Class JpaWorkTimezoneGoOutSetSetMemento.
 */
public class JpaWorkTimezoneGoOutSetSetMemento implements WorkTimezoneGoOutSetSetMemento {

	/** The entity. */
	private KshmtWorktimeCommonSet entity;

	/**
	 * Instantiates a new jpa work timezone go out set set memento.
	 *
	 * @param entity
	 *            the entity
	 */
	public JpaWorkTimezoneGoOutSetSetMemento(KshmtWorktimeCommonSet entity) {
		super();
		this.entity = entity;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nts.uk.ctx.at.shared.dom.worktime.common.WorkTimezoneGoOutSetSetMemento#
	 * setTotalRoundingSet(nts.uk.ctx.at.shared.dom.worktime.common.
	 * TotalRoundingSet)
	 */
	@Override
	public void setTotalRoundingSet(TotalRoundingSet set) {
		this.entity.getKshmtWorktimeGoOutSet().setRoundingSameFrame(set.getSetSameFrameRounding().value);
		this.entity.getKshmtWorktimeGoOutSet().setRoundingCrossFrame(set.getFrameStraddRoundingSet().value);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nts.uk.ctx.at.shared.dom.worktime.common.WorkTimezoneGoOutSetSetMemento#
	 * setDiffTimezoneSetting(nts.uk.ctx.at.shared.dom.worktime.common.
	 * GoOutTimezoneRoundingSet)
	 */
	@Override
	public void setDiffTimezoneSetting(GoOutTimezoneRoundingSet set) {
		set.saveToMememto(new JpaGoOutTimezoneRoundingSetSetMemento(this.entity));
	}

}
