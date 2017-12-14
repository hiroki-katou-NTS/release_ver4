/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.at.shared.dom.workingcondition;

import lombok.Getter;
import nts.arc.layer.dom.DomainObject;
import nts.uk.shr.com.time.TimeWithDayAttr;

/**
 * The Class TimeZone.
 */
// 時間帯
@Getter
public class TimeZone extends DomainObject {

	/** The use atr. */
	// 使用区分
	private NotUseAtr useAtr;

	/** The cnt. */
	// 勤務NO
	private int cnt;

	/** The start. */
	// 開始
	private TimeWithDayAttr start;

	/** The end. */
	// 終了
	private TimeWithDayAttr end;

	/**
	 * Update start time.
	 *
	 * @param start
	 *            the start
	 */
	public void updateStartTime(TimeWithDayAttr start) {
		this.start = start;
	}

	/**
	 * Update end time.
	 *
	 * @param end
	 *            the end
	 */
	public void updateEndTime(TimeWithDayAttr end) {
		this.end = end;
	}

	/**
	 * Instantiates a new time zone.
	 *
	 * @param memento
	 *            the memento
	 */
	public TimeZone(TimezoneGetMemento memento) {
		this.useAtr = memento.getUseAtr();
		this.cnt = memento.getCnt();
		this.start = memento.getStart();
		this.end = memento.getEnd();
	}

	/**
	 * Save to memento.
	 *
	 * @param memento
	 *            the memento
	 */
	public void saveToMemento(TimezoneSetMemento memento) {
		memento.setUseAtr(this.useAtr);
		memento.setCnt(this.cnt);
		memento.setStart(this.start);
		memento.setEnd(this.end);
	}
}
