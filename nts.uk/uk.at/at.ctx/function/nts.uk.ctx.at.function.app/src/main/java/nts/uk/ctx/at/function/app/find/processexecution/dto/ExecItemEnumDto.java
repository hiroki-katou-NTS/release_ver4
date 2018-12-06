/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.at.function.app.find.processexecution.dto;

import java.util.List;

import lombok.Getter;
import lombok.Setter;
import nts.arc.enums.EnumAdaptor;
import nts.arc.enums.EnumConstant;
import nts.uk.ctx.at.function.dom.processexecution.dailyperformance.DailyPerformanceItem;
import nts.uk.ctx.at.function.dom.processexecution.personalschedule.CreateScheduleYear;
import nts.uk.ctx.at.function.dom.processexecution.personalschedule.TargetMonth;
import nts.uk.ctx.at.function.dom.processexecution.tasksetting.detail.RepeatMonthDaysSelect;
import nts.uk.ctx.at.function.dom.processexecution.tasksetting.enums.RepeatContentItem;
import nts.uk.shr.infra.i18n.resource.I18NResourcesForUK;

@Setter
@Getter
public class ExecItemEnumDto {

	private List<EnumConstant> targetMonth;

	private List<EnumConstant> dailyPerfItems;
	
	private List<EnumConstant> repeatContentItems;
	
	private List<EnumConstant> monthDays;
	
	private List<EnumConstant> designatedYear;
	
	public static ExecItemEnumDto init(I18NResourcesForUK i18n) {
		ExecItemEnumDto dto = new ExecItemEnumDto();
		dto.setTargetMonth(EnumAdaptor.convertToValueNameList(TargetMonth.class, i18n));
		dto.setDailyPerfItems(EnumAdaptor.convertToValueNameList(DailyPerformanceItem.class, i18n));
		dto.setRepeatContentItems(EnumAdaptor.convertToValueNameList(RepeatContentItem.class, i18n));
		dto.setMonthDays(EnumAdaptor.convertToValueNameList(RepeatMonthDaysSelect.class, i18n));
		dto.setDesignatedYear(EnumAdaptor.convertToValueNameList(CreateScheduleYear.class, i18n));
		return dto;
	}
}
