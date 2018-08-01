/******************************************************************
 * Copyright (c) 2018 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.at.function.app.find.monthlyworkschedule;

import java.util.List;

import lombok.Data;
import nts.uk.ctx.at.function.dom.dailyworkschedule.RemarkInputContent;
import nts.uk.ctx.at.function.dom.monthlyworkschedule.PrintSettingRemarksColumn;

/**
 * The Class OutputItemMonthlyWorkScheduleDto.
 */
@Data
public class OutputItemMonthlyWorkScheduleDto {

	/** The item code. */
	private String itemCode;
	
	/** The item name. */
	private String itemName;
	
	/** The lst displayed attendance. */
	private List<TimeItemTobeDisplayDto> lstDisplayedAttendance;
	
	/** The print setting remarks column. */
	private PrintSettingRemarksColumn printSettingRemarksColumn;
		
	/** The remark input no. */
	private RemarkInputContent remarkInputContent;
}
