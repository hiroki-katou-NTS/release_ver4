/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.at.function.app.find.dailyworkschedule.scrA;

import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.function.app.find.dailyworkschedule.DataInforReturnDto;


/**
 * Sets the lst output item daily work schedule.
 *
 * @param lstOutputItemDailyWorkSchedule the new lst output item daily work schedule
 * @author HoangDD
 */
@Setter
@Getter
@NoArgsConstructor
public class WorkScheduleOutputConditionDto {
	
	/** The start date. */
	private GeneralDate startDate;
	
	/** The end date. */
	private GeneralDate endDate;
	
	/** The str return. */
	private String strReturn;
	
	/** The lst output item daily work schedule. */
	private List<DataInforReturnDto> lstOutputItemDailyWorkSchedule;
	
	/**
	 * Instantiates a new work schedule output condition dto.
	 *
	 * @param startDate the start date
	 * @param endDate the end date
	 * @param strReturn the str return
	 * @param lstOutputItemDailyWorkSchedule the lst output item daily work schedule
	 */
	public WorkScheduleOutputConditionDto(GeneralDate startDate, GeneralDate endDate, String strReturn, List<DataInforReturnDto> lstOutputItemDailyWorkSchedule) {
		super();
		this.startDate = startDate;
		this.endDate = endDate;
		this.strReturn = strReturn;
		this.lstOutputItemDailyWorkSchedule = lstOutputItemDailyWorkSchedule;
	}
}


