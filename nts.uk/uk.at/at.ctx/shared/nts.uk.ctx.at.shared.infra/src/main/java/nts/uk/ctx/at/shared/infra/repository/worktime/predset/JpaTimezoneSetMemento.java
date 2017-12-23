/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.at.shared.infra.repository.worktime.predset;

import nts.uk.ctx.at.shared.dom.worktime.predset.TimezoneSetMemento;
import nts.uk.ctx.at.shared.dom.worktime.predset.UseSetting;
import nts.uk.ctx.at.shared.infra.entity.worktime.predset.KshmtWorkTimeSheetSet;
import nts.uk.ctx.at.shared.infra.entity.worktime.predset.KshmtWorkTimeSheetSetPK;
import nts.uk.shr.com.time.TimeWithDayAttr;

/**
 * The Class JpaTimezoneSetMemento.
 */
public class JpaTimezoneSetMemento implements TimezoneSetMemento {

	/** The entity. */
	private KshmtWorkTimeSheetSet entity;

	/**
	 * Instantiates a new jpa timezone set memento.
	 *
	 * @param cid
	 *            the cid
	 * @param worktimeCd
	 *            the worktime cd
	 * @param entity
	 *            the entity
	 */
	public JpaTimezoneSetMemento(String cid, String worktimeCd, KshmtWorkTimeSheetSet entity) {
		super();
		if (entity.getKshmtWorkTimeSheetSetPK() == null) {
			KshmtWorkTimeSheetSetPK kshmtWorkTimeSheetSetPK = new KshmtWorkTimeSheetSetPK();
			kshmtWorkTimeSheetSetPK.setCid(cid);
			kshmtWorkTimeSheetSetPK.setWorktimeCd(worktimeCd);
			entity.setKshmtWorkTimeSheetSetPK(kshmtWorkTimeSheetSetPK);
		}
		this.entity = entity;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nts.uk.ctx.at.shared.dom.worktime.predset.TimezoneSetMemento#setWorkNo(
	 * int)
	 */
	@Override
	public void setWorkNo(int workNo) {
		this.entity.getKshmtWorkTimeSheetSetPK().setWorkNo(workNo);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nts.uk.ctx.at.shared.dom.worktime.predset.TimezoneSetMemento#setUseAtr(
	 * nts.uk.ctx.at.shared.dom.worktime.predset.UseSetting)
	 */
	@Override
	public void setUseAtr(UseSetting useAtr) {
		this.entity.setUseAtr(useAtr.value);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nts.uk.ctx.at.shared.dom.worktime.predset.TimezoneSetMemento#setStart(nts
	 * .uk.shr.com.time.TimeWithDayAttr)
	 */
	@Override
	public void setStart(TimeWithDayAttr start) {
		this.entity.setStartTime(start.valueAsMinutes());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nts.uk.ctx.at.shared.dom.worktime.predset.TimezoneSetMemento#setEnd(nts.
	 * uk.shr.com.time.TimeWithDayAttr)
	 */
	@Override
	public void setEnd(TimeWithDayAttr end) {
		this.entity.setEndTime(end.valueAsMinutes());
	}

}
