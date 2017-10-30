/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.at.shared.dom.personallaborcondition;

import java.util.List;
import java.util.Optional;

import lombok.Getter;
import nts.arc.layer.dom.DomainObject;
import nts.uk.ctx.at.shared.dom.worktype.WorkTypeCode;

/**
 * The Class SingleDaySchedule.
 */
// 単一日勤務予定
@Getter
public class SingleDaySchedule extends DomainObject{
	
	/** The work type code. */
	// 勤務種類コード
	private WorkTypeCode workTypeCode; 
	
	/** The working hours. */
	// 勤務時間帯
	private List<TimeZone> workingHours;
	
	/** The work time code. */
	// 就業時間帯コード
	private Optional<WorkTimeCode> workTimeCode;

}
