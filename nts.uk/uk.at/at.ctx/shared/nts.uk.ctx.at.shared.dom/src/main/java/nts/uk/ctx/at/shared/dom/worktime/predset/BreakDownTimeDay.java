/******************************************************************
 * Copyright (c) 2018 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.at.shared.dom.worktime.predset;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
//import nts.arc.error.BusinessException;
import nts.uk.ctx.at.shared.dom.common.time.AttendanceTime;
import nts.uk.ctx.at.shared.dom.worktime.service.WorkTimeDomainObject;

/**
 * The Class BreakDownTimeDay.
 */
@Getter
@NoArgsConstructor
@Builder
// １日の時間内訳
public class BreakDownTimeDay extends WorkTimeDomainObject implements Cloneable{

	/** The one day. */
	// 1日
	private AttendanceTime oneDay;

	/** The morning. */
	// 午前
	private AttendanceTime morning;

	/** The afternoon. */
	// 午後
	private AttendanceTime afternoon;

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.arc.layer.dom.DomainObject#validate()
	 */
	@Override
	public void validate() {
		// if 1日<午前 => Msg_518
		if (this.oneDay.lessThan(this.morning)) {
			this.bundledBusinessExceptions.addMessage("Msg_518", "KMK003_217");
		}

		// 1日<午後 => Msg_518
		if (this.oneDay.lessThan(this.afternoon)) {
			this.bundledBusinessExceptions.addMessage("Msg_518", "KMK003_218");
		}

		super.validate();
	}

	/**
	 * Instantiates a new break down time day.
	 *
	 * @param oneDay
	 *            the one day
	 * @param morning
	 *            the morning
	 * @param afternoon
	 *            the afternoon
	 */
	public BreakDownTimeDay(Integer oneDay, Integer morning, Integer afternoon) {
		super();
		// case not input
		if (morning == null) {
			morning = 0;
		}
		if (afternoon == null) {
			afternoon = 0;
		}
		this.oneDay = new AttendanceTime(oneDay);
		this.morning = new AttendanceTime(morning);
		this.afternoon = new AttendanceTime(afternoon);
	}

	/**
	 * Instantiates a new break down time day.
	 *
	 * @param oneDay
	 *            the one day
	 * @param morning
	 *            the morning
	 * @param afternoon
	 *            the afternoon
	 */
	public BreakDownTimeDay(AttendanceTime oneDay, AttendanceTime morning, AttendanceTime afternoon) {
		super();
		this.oneDay = oneDay;
		this.morning = morning;
		this.afternoon = afternoon;
	}

	/**
	 * New instance.
	 *
	 * @return the break down time day
	 */
	static public BreakDownTimeDay newInstance() {
		return new BreakDownTimeDay(0, 0, 0);
	}

	/**
	 * Gets the predetermine work time.
	 *
	 * @return the predetermine work time
	 */
	public int getPredetermineWorkTime() {
		return this.morning.valueAsMinutes() + this.afternoon.valueAsMinutes();
	}

	@Override
	public BreakDownTimeDay clone() {
		BreakDownTimeDay cloned = new BreakDownTimeDay();
		try {
			cloned.oneDay = new AttendanceTime(this.oneDay.valueAsMinutes());
			cloned.morning = new AttendanceTime(this.morning.valueAsMinutes());
			cloned.afternoon = new AttendanceTime(this.afternoon.valueAsMinutes());
			
		}
		catch (Exception e){
			throw new RuntimeException("BreakDownTimeDay clone error.");
		}
		return cloned;
	}
}
