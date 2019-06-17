/******************************************************************
 * Copyright (c) 2018 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.at.shared.dom.worktime.flexset;

import lombok.Getter;
import lombok.NoArgsConstructor;
import nts.uk.ctx.at.shared.dom.worktime.service.WorkTimeDomainObject;
import nts.uk.shr.com.time.TimeWithDayAttr;

/**
 * The Class TimeSheet.
 */
// 時間帯
@Getter
@NoArgsConstructor
public class TimeSheet extends WorkTimeDomainObject implements Cloneable{

	/** The start time. */
	// 開始時刻
	private TimeWithDayAttr startTime;

	/** The end time. */
	// 終了時刻
	private TimeWithDayAttr endTime;

	/**
	 * Instantiates a new time sheet.
	 *
	 * @param memento
	 *            the memento
	 */
	public TimeSheet(TimeSheetGetMemento memento) {
		this.startTime = memento.getStartTime();
		this.endTime = memento.getEndTime();
	}

	/**
	 * Instantiates a new time sheet.
	 *
	 * @param startTime
	 *            the start time
	 * @param endTime
	 *            the end time
	 */
	public TimeSheet(TimeWithDayAttr startTime, TimeWithDayAttr endTime) {
		this.startTime = startTime;
		this.endTime = endTime;
	}

	/**
	 * Save to memento.
	 *
	 * @param memento
	 *            the memento
	 */
	public void saveToMemento(TimeSheetSetMemento memento) {
		memento.setStartTime(this.startTime);
		memento.setEndTime(this.endTime);
	}

	/**
	 * Creates the default.
	 *
	 * @return the time sheet
	 */
	public static TimeSheet createDefault() {
		return new TimeSheet(TimeWithDayAttr.THE_PRESENT_DAY_0000, TimeWithDayAttr.THE_PRESENT_DAY_0000);
	}
	
	@Override
	public TimeSheet clone() {
		TimeSheet cloned = new TimeSheet();
		try {
			cloned.startTime = new TimeWithDayAttr(this.startTime.valueAsMinutes());
			cloned.endTime = new TimeWithDayAttr(this.endTime.valueAsMinutes());
		}
		catch (Exception e){
			throw new RuntimeException("TimeSheet clone error.");
		}
		return cloned;
	}
}
