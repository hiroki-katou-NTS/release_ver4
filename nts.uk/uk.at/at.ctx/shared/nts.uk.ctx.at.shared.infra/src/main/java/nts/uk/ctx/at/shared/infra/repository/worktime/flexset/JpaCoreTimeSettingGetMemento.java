/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.at.shared.infra.repository.worktime.flexset;

import nts.uk.ctx.at.shared.dom.common.time.AttendanceTime;
import nts.uk.ctx.at.shared.dom.common.usecls.ApplyAtr;
import nts.uk.ctx.at.shared.dom.worktime.flexset.CoreTimeSettingGetMemento;
import nts.uk.ctx.at.shared.dom.worktime.flexset.TimeSheet;
import nts.uk.ctx.at.shared.infra.entity.worktime.KshmtFlexWorkSet;
import nts.uk.ctx.at.shared.infra.entity.worktime.KshmtFlexWorkSetPK;

/**
 * The Class JpaCoreTimeSettingGetMemento.
 */
public class JpaCoreTimeSettingGetMemento implements CoreTimeSettingGetMemento {
	
	/** The entity. */
	private KshmtFlexWorkSet entity;
	
	/**
	 * Instantiates a new jpa core time setting get memento.
	 *
	 * @param entity the entity
	 */
	public JpaCoreTimeSettingGetMemento(KshmtFlexWorkSet entity) {
		super();
		if(entity.getKshmtFlexWorkSetPK() == null){
			entity.setKshmtFlexWorkSetPK(new KshmtFlexWorkSetPK());
		}
		this.entity = entity;
	}

	/**
	 * Gets the core time sheet.
	 *
	 * @return the core time sheet
	 */
	@Override
	public TimeSheet getCoreTimeSheet() {
		return new TimeSheet(new JpaFlexTimeSheetGetMemento(this.entity));
	}

	/**
	 * Gets the timesheet.
	 *
	 * @return the timesheet
	 */
	@Override
	public ApplyAtr getTimesheet() {
//		return ApplyAtr.valueOf(this.entity.getUseAtr());
		// TODO: check again
		return null;
	}

	/**
	 * Gets the min work time.
	 *
	 * @return the min work time
	 */
	@Override
	public AttendanceTime getMinWorkTime() {
		return new AttendanceTime(this.entity.getLeastWorkTime());
	}
	

}
