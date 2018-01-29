/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.at.shared.dom.worktime.flexset;

import lombok.Getter;
import nts.uk.ctx.at.shared.dom.worktime.service.WorkTimeDomainObject;
import nts.uk.shr.com.time.TimeWithDayAttr;

/**
 * The Class TimeSheet.
 */
// 時間帯
@Getter
public class TimeSheet extends WorkTimeDomainObject {

	/** The start time. */
	// 開始時刻
	private TimeWithDayAttr startTime;

	/** The end time. */
	// 終了時刻
	private TimeWithDayAttr endTime;
	
	
	/**
	 * Instantiates a new time sheet.
	 *
	 * @param memento the memento
	 */
	public TimeSheet(TimeSheetGetMemento memento) {
		this.startTime = memento.getStartTime();
		this.endTime = memento.getEndTime();
	}

	/**
	 * Save to memento.
	 *
	 * @param memento the memento
	 */
	public void saveToMemento(TimeSheetSetMemento memento){
		memento.setStartTime(this.startTime);
		memento.setEndTime(this.endTime);
	}
}
