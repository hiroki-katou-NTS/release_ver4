/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.at.shared.app.find.statutory.worktime.employmentNew;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import nts.uk.ctx.at.shared.app.command.statutory.worktime.common.MonthlyUnitDto;
import nts.uk.ctx.at.shared.dom.statutory.worktime.monunit.MonthlyWorkTimeSet;


/**
 * The Class EmpFlexSettingDto.
 */

@Data
@AllArgsConstructor
public class EmpFlexSettingDto{
	
	/** The employment code. */
	/** 社員ID. */
	private String employmentCode;
	
	/** The year. */
	/** 年. */
	private int year;
	
	/** The company id. */
	private String companyId;
	
	/** The statutory setting. */
	/** 法定時間. */
	private List<MonthlyUnitDto> statutorySetting;
	
	/** The specified setting. */
	/** 所定時間. */
	private List<MonthlyUnitDto> specifiedSetting;

	/** 週平均時間. */
	private List<MonthlyUnitDto> weekAvgSetting;
	
	public static <T extends MonthlyWorkTimeSet> EmpFlexSettingDto with (String cid, String employmentCode,
			int year, List<T> workTime) {
		
		EmpFlexSettingDto dto = new EmpFlexSettingDto(employmentCode, year, cid, new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
		
		workTime.stream().forEach(wt -> {
			
			dto.getStatutorySetting().add(new MonthlyUnitDto(wt.getYm().month(), wt.getLaborTime().getLegalLaborTime().v()));
			dto.getSpecifiedSetting().add(new MonthlyUnitDto(wt.getYm().month(), wt.getLaborTime().getWithinLaborTime().get().v()));
			dto.getWeekAvgSetting().add(new MonthlyUnitDto(wt.getYm().month(), wt.getLaborTime().getWeekAvgTime().get().v()));
		});
		
		return dto;
	}
}
