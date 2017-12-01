/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.at.shared.app.find.worktime.flexset.dto;

import lombok.Getter;
import lombok.Setter;
import nts.uk.ctx.at.shared.dom.common.time.AttendanceTime;
import nts.uk.ctx.at.shared.dom.common.usecls.ApplyAtr;
import nts.uk.ctx.at.shared.dom.worktime.flexset.CoreTimeSettingSetMemento;
import nts.uk.ctx.at.shared.dom.worktime.flexset.TimeSheet;

/**
 * The Class CoreTimeSettingDto.
 */
@Getter
@Setter
public class CoreTimeSettingDto implements CoreTimeSettingSetMemento{

	/** The core time sheet. */
	private TimeSheetDto coreTimeSheet;
	
	/** The timesheet. */
	private Integer timesheet;
	
	/** The min work time. */
	private Integer minWorkTime;


	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.at.shared.dom.worktime.flexset.CoreTimeSettingSetMemento#
	 * setCoreTimeSheet(nts.uk.ctx.at.shared.dom.worktime.flexset.TimeSheet)
	 */
	@Override
	public void setCoreTimeSheet(TimeSheet coreTimeSheet) {
		coreTimeSheet.saveToMemento(this.coreTimeSheet);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.at.shared.dom.worktime.flexset.CoreTimeSettingSetMemento#
	 * setTimesheet(nts.uk.ctx.at.shared.dom.common.usecls.ApplyAtr)
	 */
	@Override
	public void setTimesheet(ApplyAtr timesheet) {
		this.timesheet = timesheet.value;
		
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.at.shared.dom.worktime.flexset.CoreTimeSettingSetMemento#
	 * setMinWorkTime(nts.uk.ctx.at.shared.dom.common.time.AttendanceTime)
	 */
	@Override
	public void setMinWorkTime(AttendanceTime minWorkTime) {
		this.minWorkTime = minWorkTime.valueAsMinutes();
	}
	
	
}
