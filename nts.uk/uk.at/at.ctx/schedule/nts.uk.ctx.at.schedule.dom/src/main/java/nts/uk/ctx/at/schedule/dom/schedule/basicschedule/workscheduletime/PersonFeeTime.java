/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.at.schedule.dom.schedule.basicschedule.workscheduletime;

import lombok.Getter;
import nts.arc.layer.dom.DomainObject;
import nts.uk.ctx.at.schedule.dom.schedule.basicschedule.personalfee.ExtraTimeItemNo;
import nts.uk.ctx.at.shared.dom.common.time.AttendanceTime;

/**
 * The Class PersonFeeTime.
 */
//勤務予定人件費時間
@Getter
public class PersonFeeTime extends DomainObject {
	
	/** The no. */
	//NO
	private ExtraTimeItemNo no;
	
	/** The person fee time. */
	//人件費時間
	private AttendanceTime personFeeTime; 
}
