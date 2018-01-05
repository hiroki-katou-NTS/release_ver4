/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.at.shared.dom.worktime.predset;

import lombok.Builder;
import lombok.Getter;
import nts.arc.error.BundledBusinessException;
import nts.arc.layer.dom.DomainObject;
import nts.uk.ctx.at.shared.dom.common.time.AttendanceTime;

/**
 * The Class BreakDownTimeDay.
 */
@Builder
@Getter
//１日の時間内訳
public class BreakDownTimeDay extends DomainObject{

	/** The one day. */
	// 1日
	private AttendanceTime oneDay;

	/** The morning. */
	// 午前
	private AttendanceTime morning;

	/** The afternoon. */
	// 午後
	private AttendanceTime afternoon;
	
	
	/* (non-Javadoc)
	 * @see nts.arc.layer.dom.DomainObject#validate()
	 */
	@Override
	public void validate() {
		super.validate();
		
		// if 1日<午前 => Msg_518 
		// 1日<午後 => Msg_518
		if (this.oneDay.lessThan(this.morning) || this.oneDay.lessThan(this.afternoon)) {
			BundledBusinessException be = BundledBusinessException.newInstance();
			be.addMessage("Msg_518");
			be.throwExceptions();
		}
		
	}


	/**
	 * Instantiates a new break down time day.
	 *
	 * @param oneDay the one day
	 * @param morning the morning
	 * @param afternoon the afternoon
	 */
	public BreakDownTimeDay(int oneDay, int morning, int afternoon) {
		super();
		this.oneDay = new AttendanceTime(oneDay);
		this.morning = new AttendanceTime(morning);
		this.afternoon = new AttendanceTime(afternoon);
	}


	/**
	 * Instantiates a new break down time day.
	 *
	 * @param oneDay the one day
	 * @param morning the morning
	 * @param afternoon the afternoon
	 */
	public BreakDownTimeDay(AttendanceTime oneDay, AttendanceTime morning, AttendanceTime afternoon) {
		super();
		this.oneDay = oneDay;
		this.morning = morning;
		this.afternoon = afternoon;
	}
	
}
