/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.at.shared.app.find.statutory.worktime.shared;

import java.util.List;
import java.util.stream.Collectors;

import lombok.Data;
import nts.uk.ctx.at.shared.dom.statutory.worktime.monunit.MonthlyWorkTimeSet;
import nts.uk.ctx.at.shared.dom.statutory.worktime.week.WorkingTimeSetting;

/**
 * The Class DeformationLaborSettingDto.
 */
@Data
public class DeformationLaborSettingDto {

	/** The statutory setting. */
	private WorkingTimeSettingDto statutorySetting;

	/** The week start. */
	private int weekStart;

	public static <T extends WorkingTimeSetting, U extends MonthlyWorkTimeSet> 
		DeformationLaborSettingDto with (T week, List<U> workTime) {
		
		DeformationLaborSettingDto dto = new DeformationLaborSettingDto();
		
		dto.setStatutorySetting(new WorkingTimeSettingDto(
				week.getDailyTime().getDailyTime().valueAsMinutes(),
				workTime.stream()
							.map(c -> new MonthlyDto(c.getYm().month(), c.getLaborTime().getLegalLaborTime().v()))
							.collect(Collectors.toList()), 
				week.getWeeklyTime().getTime().valueAsMinutes()));
		
		return dto;
	}
}
