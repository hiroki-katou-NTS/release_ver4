/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.at.schedule.dom.schedule.basicschedule.workscheduletimezone;

import lombok.Getter;
import nts.arc.layer.dom.DomainObject;
import nts.uk.shr.com.time.TimeWithDayAttr;

/**
 * The Class WorkScheduleTimeZone.
 */
@Getter
// 勤務予定時間帯
public class WorkScheduleTimeZone extends DomainObject{
	
	/** The schedule cnt. */
	// 予定勤務回数
	private int scheduleCnt;
	
	/** The schedule start clock. */
	// 予定開始時刻
	private TimeWithDayAttr scheduleStartClock;

	/** The schedule end clock. */
	// 予定終了時刻
	private TimeWithDayAttr scheduleEndClock;
	
	/** The bounce atr. */
	// 直行直帰区分
	private BounceAtr bounceAtr;
	
	
	/**
	 * Instantiates a new work schedule time zone.
	 *
	 * @param memento the memento
	 */
	public WorkScheduleTimeZone(WorkScheduleTimeZoneGetMemento memento){
		this.scheduleCnt = memento.getScheduleCnt();
		this.scheduleStartClock = memento.getScheduleStartClock();
		this.scheduleEndClock = memento.getScheduleEndClock();
		this.bounceAtr = memento.getBounceAtr();
	}
	
	/**
	 * Save to memento.
	 *
	 * @param memento the memento
	 */
	public void saveToMemento(WorkScheduleTimeZoneSetMemento memento){
		memento.setScheduleCnt(this.scheduleCnt);
		memento.setScheduleStartClock(this.scheduleStartClock);
		memento.setScheduleEndClock(this.scheduleEndClock);
		memento.setBounceAtr(this.bounceAtr);
	}
}
