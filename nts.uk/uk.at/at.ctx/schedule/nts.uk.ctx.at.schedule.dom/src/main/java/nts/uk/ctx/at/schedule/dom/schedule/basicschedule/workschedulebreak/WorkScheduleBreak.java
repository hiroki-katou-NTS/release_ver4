/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.at.schedule.dom.schedule.basicschedule.workschedulebreak;

import lombok.AllArgsConstructor;
import lombok.Getter;
import nts.arc.layer.dom.DomainObject;
import nts.uk.shr.com.time.TimeWithDayAttr;

/**
 * The Class WorkScheduleBreak.
 */
@Getter
@AllArgsConstructor
// 勤務予定休憩
public class WorkScheduleBreak extends DomainObject{

	/** The schedule break cnt. */
	// 予定休憩回数
	private ScheduledBreakCnt scheduleBreakCnt;
	
	/** The scheduled start clock. */
	// 予定休憩開始時刻
	private TimeWithDayAttr  scheduledStartClock;
	
	/** The scheduled end clock. */
	// 予定休憩終了時刻
	private TimeWithDayAttr scheduledEndClock;
	
	
	/**
	 * Instantiates a new work schedule break.
	 *
	 * @param memento the memento
	 */
	public WorkScheduleBreak(WorkScheduleBreakGetMemento memento) {
		this.scheduleBreakCnt = memento.getScheduleBreakCnt();
		this.scheduledStartClock = memento.getScheduledStartClock();
		this.scheduledEndClock = memento.getScheduledEndClock();
	}
	
	/**
	 * Save to memento.
	 *
	 * @param memento the memento
	 */
	public void saveToMemento(WorkScheduleBreakSetMemento memento){
		memento.setScheduledBreakCnt(this.scheduleBreakCnt);
		memento.setScheduledStartClock(this.scheduledStartClock);
		memento.setScheduledEndClock(this.scheduledEndClock);
	}
	
	public static WorkScheduleBreak createFromJavaType(int scheduleBreakCnt, int scheduledStartClock, int scheduledEndClock) {
		return new WorkScheduleBreak(
				new ScheduledBreakCnt(scheduleBreakCnt), 
				new TimeWithDayAttr(scheduledStartClock), 
				new TimeWithDayAttr(scheduledEndClock));
	}
}
