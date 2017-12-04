/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.at.shared.app.find.worktime.difftimeset.dto;

import java.util.List;

import lombok.Getter;
import nts.uk.ctx.at.shared.app.find.worktime.common.dto.EmTimeZoneSetDto;
import nts.uk.ctx.at.shared.dom.worktime.common.EmTimeZoneSet;
import nts.uk.ctx.at.shared.dom.worktime.difftimeset.DiffTimeOTTimezoneSet;
import nts.uk.ctx.at.shared.dom.worktime.difftimeset.DiffTimezoneSettingSetMemento;

/**
 * The Class DiffTimezoneSetting.
 */
@Getter
public class DiffTimezoneSettingDto implements DiffTimezoneSettingSetMemento{

	/** The employment timezone. */
	private List<EmTimeZoneSetDto> employmentTimezone;

	/** The OT timezone. */
	private List<DiffTimeOTTimezoneSetDto> OTTimezone;

	@Override
	public void setEmploymentTimezone(List<EmTimeZoneSet> employmentTimezone) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setOTTimezone(List<DiffTimeOTTimezoneSet> OTTimezone) {
		// TODO Auto-generated method stub
		
	}

}
