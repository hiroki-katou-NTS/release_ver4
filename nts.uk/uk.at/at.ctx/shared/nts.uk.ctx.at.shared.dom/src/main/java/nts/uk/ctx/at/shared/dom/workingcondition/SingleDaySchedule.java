/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.at.shared.dom.workingcondition;

import java.util.List;

import lombok.Getter;
import nts.arc.layer.dom.DomainObject;
import nts.uk.ctx.at.shared.dom.worktype.WorkTypeCode;

/**
 * The Class SingleDaySchedule.
 */
// 単一日勤務予定
@Getter
public class SingleDaySchedule extends DomainObject{
	
	/** The work type code. */
	// 勤務種類コード
	private WorkTypeCode workTypeCode; 
	
	/** The working hours. */
	// 勤務時間帯
	private List<TimeZone> workingHours;
	
	/** The work time code. */
	// 就業時間帯コード
	private WorkTimeCode workTimeCode;
	
	
	/**
	 * Instantiates a new single day schedule.
	 *
	 * @param memento the memento
	 */
	public SingleDaySchedule(SingleDayScheduleGetMemento memento) {
		this.workTypeCode = memento.getWorkTypeCode();
		this.workingHours = memento.getWorkingHours();
		this.workTimeCode = memento.getWorkTimeCode();
	}

	/**
	 * Save to memento.
	 *
	 * @param memento the memento
	 */
	public void saveToMemento(SingleDayScheduleSetMemento memento){
		memento.setWorkTypeCode(this.workTypeCode);
		memento.setWorkTimeCode(this.workTimeCode);
		memento.setWorkingHours(workingHours);
	}
	
}
