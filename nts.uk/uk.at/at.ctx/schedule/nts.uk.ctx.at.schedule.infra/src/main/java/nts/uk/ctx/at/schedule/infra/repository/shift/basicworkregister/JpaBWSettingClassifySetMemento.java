/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.at.schedule.infra.repository.shift.basicworkregister;

import nts.uk.ctx.at.schedule.dom.shift.basicworkregister.BasicWorkSettingSetMemento;
import nts.uk.ctx.at.schedule.dom.shift.basicworkregister.WorkdayDivision;
import nts.uk.ctx.at.schedule.dom.shift.basicworkregister.WorkingCode;
import nts.uk.ctx.at.schedule.dom.shift.basicworkregister.WorktypeCode;
import nts.uk.ctx.at.schedule.infra.entity.shift.basicworkregister.KcbmtClassifyWorkSet;
import nts.uk.ctx.at.schedule.infra.entity.shift.basicworkregister.KcbmtClassifyWorkSetPK;

/**
 * The Class JpaBWSettingClassifySetMemento.
 */
public class JpaBWSettingClassifySetMemento implements BasicWorkSettingSetMemento {

	/** The type value. */
	private KcbmtClassifyWorkSet typeValue;
	
	

	/**
	 * Instantiates a new jpa BW setting classify set memento.
	 *
	 * @param typeValue the type value
	 */
	public JpaBWSettingClassifySetMemento(KcbmtClassifyWorkSet typeValue) {
		super();
		if(typeValue.getKcbmtClassifyWorkSetPK() == null){
			typeValue.setKcbmtClassifyWorkSetPK(new KcbmtClassifyWorkSetPK());
		}
		this.typeValue = typeValue;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.at.schedule.dom.shift.basicworkregister.
	 * BasicWorkSettingSetMemento#setWorkTypeCode(nts.uk.ctx.at.schedule.dom.
	 * shift.basicworkregister.WorktypeCode)
	 */
	@Override
	public void setWorkTypeCode(WorktypeCode worktypeCode) {
		this.typeValue.setWorktypeCode(worktypeCode.v());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.at.schedule.dom.shift.basicworkregister.
	 * BasicWorkSettingSetMemento#setSiftCode(nts.uk.ctx.at.schedule.dom.shift.
	 * basicworkregister.WorkingCode)
	 */
	@Override
	public void setSiftCode(WorkingCode workingCode) {
		this.typeValue.setWorkingCode(workingCode.v());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.at.schedule.dom.shift.basicworkregister.
	 * BasicWorkSettingSetMemento#setWorkDayDivision(nts.uk.ctx.at.schedule.dom.
	 * shift.basicworkregister.WorkdayDivision)
	 */
	@Override
	public void setWorkDayDivision(WorkdayDivision workdayDivision) {
		this.typeValue.getKcbmtClassifyWorkSetPK().setWorkdayDivision(workdayDivision.value);
	}
}
