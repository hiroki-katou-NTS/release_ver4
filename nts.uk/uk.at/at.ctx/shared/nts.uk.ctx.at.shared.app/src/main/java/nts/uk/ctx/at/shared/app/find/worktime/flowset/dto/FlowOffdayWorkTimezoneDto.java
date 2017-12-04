/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.at.shared.app.find.worktime.flowset.dto;

import java.util.List;
import java.util.stream.Collectors;

import lombok.Getter;
import lombok.Setter;
import nts.uk.ctx.at.shared.app.find.worktime.common.dto.FlowWorkRestTimezoneDto;
import nts.uk.ctx.at.shared.dom.worktime.common.FlowWorkRestTimezone;
import nts.uk.ctx.at.shared.dom.worktime.flowset.FlowOffdayWorkTimezoneSetMemento;
import nts.uk.ctx.at.shared.dom.worktime.flowset.FlowWorkHolidayTimeZone;

/**
 * The Class FlowOffdayWorkTimezoneDto.
 */
@Getter
@Setter
public class FlowOffdayWorkTimezoneDto implements FlowOffdayWorkTimezoneSetMemento {

	/** The rest time zone. */
	private FlowWorkRestTimezoneDto restTimeZone;

	/** The lst work timezone. */
	private List<FlowWorkHolidayTimeZoneDto> lstWorkTimezone;

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.at.shared.dom.worktime.flowset.
	 * FlowOffdayWorkTimezoneSetMemento#setRestTimeZone(nts.uk.ctx.at.shared.dom
	 * .worktime.common.FlowWorkRestTimezone)
	 */
	@Override
	public void setRestTimeZone(FlowWorkRestTimezone tzone) {
		tzone.saveToMemento(this.restTimeZone);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.at.shared.dom.worktime.flowset.
	 * FlowOffdayWorkTimezoneSetMemento#setLstWorkTimezone(java.util.List)
	 */
	@Override
	public void setLstWorkTimezone(List<FlowWorkHolidayTimeZone> listHdtz) {
		this.lstWorkTimezone = listHdtz.stream().map(item -> {
			FlowWorkHolidayTimeZoneDto dto = new FlowWorkHolidayTimeZoneDto();
			item.saveToMement(dto);
			return dto;
		}).collect(Collectors.toList());
	}
}
