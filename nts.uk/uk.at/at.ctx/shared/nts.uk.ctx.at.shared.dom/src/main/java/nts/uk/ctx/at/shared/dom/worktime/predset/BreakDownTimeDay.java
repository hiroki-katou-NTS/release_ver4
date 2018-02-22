/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.at.shared.dom.worktime.predset;

import lombok.Builder;
import lombok.Getter;
import nts.arc.error.BusinessException;
import nts.uk.ctx.at.shared.dom.common.time.AttendanceTime;
import nts.uk.ctx.at.shared.dom.worktime.service.WorkTimeDomainObject;

/**
 * The Class BreakDownTimeDay.
 */
@Builder
@Getter
//１日の時間内訳
public class BreakDownTimeDay extends WorkTimeDomainObject {

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
	 * @param oneDay the one day
	 * @param morning the morning
	 * @param afternoon the afternoon
	 */
	public BreakDownTimeDay(Integer oneDay, Integer morning, Integer afternoon) {
		super();
		if (morning == null) {
			throw new BusinessException("Msg_518", "KMK003_217");
		}
		if (afternoon == null) {
			throw new BusinessException("Msg_518", "KMK003_218");
		}
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
	
	/**
	 * 所定労働時間の取得
	 * @return　所定労働時間
	 */
	public int getPredetermineWorkTime(){
		return this.morning.valueAsMinutes() + this.afternoon.valueAsMinutes();
	}
	
}
