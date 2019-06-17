/******************************************************************
 * Copyright (c) 2018 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.at.shared.dom.worktime.predset;

import lombok.Getter;
import lombok.NoArgsConstructor;
import nts.uk.ctx.at.shared.dom.worktime.common.TimeZone;
import nts.uk.shr.com.time.TimeWithDayAttr;

/**
 * The Class TimezoneUse.
 */
//時間帯(使用区分付き)
@Getter
@NoArgsConstructor
public class TimezoneUse extends TimeZone implements Cloneable{

	/** The use atr. */
	// 使用区分
	private UseSetting useAtr;

	/** The work no. */
	// 勤務NO
	private int workNo;

	/** The shift one. */
	public static Integer SHIFT_ONE = 1;

	/** The shift two. */
	public static Integer SHIFT_TWO = 2;

	/**
	 * Instantiates a new timezone use.
	 *
	 * @param start
	 *            the start
	 * @param end
	 *            the end
	 * @param useAtr
	 *            the use atr
	 * @param workNo
	 *            the work no
	 */
	public TimezoneUse(TimeWithDayAttr start, TimeWithDayAttr end, UseSetting useAtr, int workNo) {
		super(start, end);
		this.useAtr = useAtr;
		this.workNo = workNo;
	}

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
	 * Disable.
	 */
	public void disable() {
		this.useAtr = UseSetting.NOT_USE;
	}

	/**
	 * Reset time.
	 */
	public void resetTime() {
		this.start = null;
		this.end = null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.at.shared.dom.worktime.common.TimeZone#validate()
	 */
	@Override
	public void validate() {
		if (this.isUsed()) {
			// Validate
			if (this.workNo == SHIFT_TWO && (this.start == null || this.end == null)) {
				this.bundledBusinessExceptions.addMessage("Msg_516", "KMK003_216");
			}
			this.validateRange("KMK003_216");
		}

		super.validate();
	}

	/**
	 * Correct data.
	 *
	 * @param oldDomain
	 *            the old domain
	 */
	public void correctData(TimezoneUse oldDomain) {
		this.useAtr = oldDomain.getUseAtr();
		this.start = oldDomain.getStart();
		this.end = oldDomain.getEnd();
	}

	/**
	 * Restore time.
	 *
	 * @param oldDomain
	 *            the old domain
	 */
	public void restoreTime(TimezoneUse oldDomain) {
		this.start = oldDomain.getStart();
		this.end = oldDomain.getEnd();
	}

	/**
	 * Correct default data.
	 */
	public void correctDefaultData() {
		this.useAtr = UseSetting.NOT_USE;
		this.start = TimeWithDayAttr.THE_PRESENT_DAY_0000;
		this.end = TimeWithDayAttr.THE_PRESENT_DAY_0000;
	}

	/**
	 * Instantiates a new timezone use.
	 *
	 * @param memento
	 *            the memento
	 */
	public TimezoneUse(TimezoneGetMemento memento) {
		this.useAtr = memento.getUseAtr();
		this.workNo = memento.getWorkNo();
		this.start = memento.getStart();
		this.end = memento.getEnd();

		// If Work No is 1, useAtr allway is USE.
		if (this.workNo == SHIFT_ONE) {
			this.useAtr = UseSetting.USE;
		}
	}

	/**
	 * Save to memento.
	 *
	 * @param memento
	 *            the memento
	 */
	public void saveToMemento(TimezoneSetMemento memento) {
		memento.setUseAtr(this.useAtr);
		memento.setWorkNo(this.workNo);
		memento.setStart(this.start);
		memento.setEnd(this.end);
	}

	/**
	 * Checks if is used.
	 *
	 * @return true, if is used
	 */
	public boolean isUsed() {
		return this.useAtr == UseSetting.USE;
	}

	@Override
	public TimezoneUse clone() {
		TimezoneUse cloned = new TimezoneUse();
		try {
			cloned.start = new TimeWithDayAttr(this.start.valueAsMinutes());
			cloned.end = new TimeWithDayAttr(this.end.valueAsMinutes());;
			cloned.useAtr = UseSetting.valueOf(this.useAtr.value);
			cloned.workNo = this.workNo;
		}
		catch (Exception e){
			throw new RuntimeException("TimezoneUse clone error.");
		}
		return cloned;
	}
}
