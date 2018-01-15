/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.at.shared.dom.worktime.common;

import lombok.Getter;
import nts.arc.layer.dom.DomainObject;
import nts.uk.shr.com.time.TimeWithDayAttr;

/**
 * The Class EmTimeZoneSet.
 */
// 就業時間の時間帯設定
@Getter
public class EmTimeZoneSet extends DomainObject {

	/** The Employment time frame no. */
	//就業時間枠NO
	private EmTimeFrameNo employmentTimeFrameNo;
	
	/** The timezone. */
	//時間帯
	private TimeZoneRounding timezone;

	/**
	 * Instantiates a new em time zone set.
	 *
	 * @param employmentTimeFrameNo the employment time frame no
	 * @param timezone the timezone
	 */
	public EmTimeZoneSet(EmTimeFrameNo employmentTimeFrameNo, TimeZoneRounding timezone) {
		super();
		this.employmentTimeFrameNo = employmentTimeFrameNo;
		this.timezone = timezone;
	}
	
	/**
	 * Instantiates a new em time zone set.
	 *
	 * @param memento the memento
	 */
	public EmTimeZoneSet(EmTimeZoneSetGetMemento memento) {
		this.employmentTimeFrameNo = memento.getEmploymentTimeFrameNo();
		this.timezone = memento.getTimezone();
	}

	/**
	 * Save to memento.
	 *
	 * @param memento the memento
	 */
	public void saveToMemento(EmTimeZoneSetSetMemento memento) {
		memento.setEmploymentTimeFrameNo(this.employmentTimeFrameNo);
		memento.setTimezone(this.timezone);
	}
	
	/**
	 * Restore data.
	 *
	 * @param other the other
	 */
	public void restoreData(EmTimeZoneSet other) {
		this.employmentTimeFrameNo = other.employmentTimeFrameNo;
		this.timezone = other.getTimezone();
	}
	
	/* (non-Javadoc)
	 * @see nts.arc.layer.dom.DomainObject#validate()
	 */
	@Override
	public void validate() {
		super.validate();

	}

	/**
	 * 開始と終了だけ変更した新しいインスタンスを作る
	 * @param start 開始時刻
	 * @param end 終了時刻
	 * @return 新しいインスタンス
	 */
	public EmTimeZoneSet newSpanWith(TimeWithDayAttr start, TimeWithDayAttr end) {
		return new EmTimeZoneSet(this.employmentTimeFrameNo, new TimeZoneRounding(start, end, this.timezone.getRounding()));
	}
	
}
