/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.at.shared.dom.worktime.common;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import nts.uk.ctx.at.shared.dom.worktime.difftimeset.DiffTimeDeductTimezone;
import nts.uk.ctx.at.shared.dom.worktime.service.WorkTimeDomainObject;
import nts.uk.shr.com.time.TimeWithDayAttr;

/**
 * The Class EmTimeZoneSet.
 */
// 就業時間の時間帯設定
@Getter
@Setter
@NoArgsConstructor
public class EmTimeZoneSet extends WorkTimeDomainObject implements Cloneable{

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

	/**
	 * 開始と終了だけ変更した新しいインスタンスを作る
	 * @param start 開始時刻
	 * @param end 終了時刻
	 * @return 新しいインスタンス
	 */
	public EmTimeZoneSet newSpanWith(TimeWithDayAttr start, TimeWithDayAttr end) {
		return new EmTimeZoneSet(this.employmentTimeFrameNo, new TimeZoneRounding(start, end, this.timezone.getRounding()));
	}

	public boolean checkRestTime(DiffTimeDeductTimezone item) {
		return this.timezone.getStart().v() <= item.getStart().v() && this.timezone.getEnd().v() >= item.getEnd().v();
	}
	
	@Override
	public EmTimeZoneSet clone() {
		EmTimeZoneSet cloned = new EmTimeZoneSet();
		try {
			cloned.employmentTimeFrameNo = new EmTimeFrameNo(this.employmentTimeFrameNo.v());
			cloned.timezone = this.getTimezone().clone();
		}
		catch (Exception e){
			throw new RuntimeException("EmTimeZoneSet clone error.");
		}
		return cloned;
	}
	
}
