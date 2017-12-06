/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.at.shared.app.find.worktime.common.dto;

import java.util.List;
import java.util.stream.Collectors;

import lombok.Getter;
import lombok.Setter;
import nts.uk.ctx.at.shared.dom.worktime.common.PrioritySetting;
import nts.uk.ctx.at.shared.dom.worktime.common.RoundingSet;
import nts.uk.ctx.at.shared.dom.worktime.common.WorkTimezoneStampSetSetMemento;

/**
 * The Class WorkTimezoneStampSetDto.
 */
@Getter
@Setter
public class WorkTimezoneStampSetDto implements WorkTimezoneStampSetSetMemento{
	
	/** The rounding set. */
	private List<RoundingSetDto> roundingSets;
	
	/** The priority set. */
	private List<PrioritySettingDto> prioritySets;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nts.uk.ctx.at.shared.dom.worktime.common.WorkTimezoneStampSetSetMemento#
	 * setRoundingSet(java.util.List)
	 */
	@Override
	public void setRoundingSet(List<RoundingSet> rdSet) {
		this.roundingSets = rdSet.stream().map(domain->{
			RoundingSetDto dto = new RoundingSetDto();
			domain.saveToMemento(dto);
			return dto;
		}).collect(Collectors.toList());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nts.uk.ctx.at.shared.dom.worktime.common.WorkTimezoneStampSetSetMemento#
	 * setPrioritySet(java.util.List)
	 */
	@Override
	public void setPrioritySet(List<PrioritySetting> prSet) {
		this.prioritySets = prSet.stream().map(domain->{
			PrioritySettingDto dto = new PrioritySettingDto();
			domain.saveToMemento(dto);
			return dto;
		}).collect(Collectors.toList());
	}

	
	

}
