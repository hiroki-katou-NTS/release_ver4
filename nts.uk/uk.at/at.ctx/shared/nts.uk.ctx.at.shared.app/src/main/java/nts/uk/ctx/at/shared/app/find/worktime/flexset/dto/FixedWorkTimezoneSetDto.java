/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.at.shared.app.find.worktime.flexset.dto;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import lombok.Getter;
import lombok.Setter;
import nts.gul.collection.CollectionUtil;
import nts.uk.ctx.at.shared.app.find.worktime.common.dto.EmTimeZoneSetDto;
import nts.uk.ctx.at.shared.dom.worktime.common.EmTimeZoneSet;
import nts.uk.ctx.at.shared.dom.worktime.fixedset.FixedWorkTimezoneSetSetMemento;
import nts.uk.ctx.at.shared.dom.worktime.fixedset.OverTimeOfTimeZoneSet;

/**
 * The Class FixedWorkTimezoneSetDto.
 */
@Getter
@Setter
public class FixedWorkTimezoneSetDto implements FixedWorkTimezoneSetSetMemento {
	
	/** The lst working time zone. */
	private List<EmTimeZoneSetDto> lstWorkingTimezone;

	/** The lst OT timezone. */
	private List<OverTimeOfTimeZoneSetDto> lstOTTimezone;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nts.uk.ctx.at.shared.dom.worktime.fixedset.FixedWorkTimezoneSetSetMemento
	 * #setLstWorkingTimezone(java.util.List)
	 */
	@Override
	public void setLstWorkingTimezone(List<EmTimeZoneSet> lstWorkingTimezone) {
		if (CollectionUtil.isEmpty(lstWorkingTimezone)) {
			this.lstWorkingTimezone = new ArrayList<>();
		} else {
			this.lstWorkingTimezone = lstWorkingTimezone.stream().map(domain -> {
				EmTimeZoneSetDto dto = new EmTimeZoneSetDto();
				domain.saveToMemento(dto);
				return dto;
			}).collect(Collectors.toList());
		}
	}

	@Override
	public void setLstOTTimezone(List<OverTimeOfTimeZoneSet> lstOTTimezone) {
		if (CollectionUtil.isEmpty(lstOTTimezone)) {
			this.lstOTTimezone = new ArrayList<>();
		} else {
			this.lstOTTimezone = lstOTTimezone.stream().map(domain -> {
				OverTimeOfTimeZoneSetDto dto = new OverTimeOfTimeZoneSetDto();
				domain.saveToMemento(dto);
				return dto;
			}).collect(Collectors.toList());
		}
	}


}
