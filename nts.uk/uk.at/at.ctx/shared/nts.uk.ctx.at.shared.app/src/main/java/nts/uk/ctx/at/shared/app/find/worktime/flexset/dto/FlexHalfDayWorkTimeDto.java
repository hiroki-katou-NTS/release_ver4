/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.at.shared.app.find.worktime.flexset.dto;

import java.util.List;

import lombok.Getter;
import lombok.Setter;
import nts.uk.ctx.at.shared.app.find.worktime.common.dto.FlowWorkRestTimezoneDto;

/**
 * The Class FlexHalfDayWorkTimeDto.
 */
@Getter
@Setter
public class FlexHalfDayWorkTimeDto {
	
	/** The rest timezone. */
	private List<FlowWorkRestTimezoneDto> restTimezone;
	
	/** The work timezone. */
	private FixedWorkTimezoneSetDto workTimezone;
	
	/** The Am pm classification. */
	private Integer AmPmClassification;

	/**
	 * Instantiates a new flex half day work time dto.
	 *
	 * @param restTimezone the rest timezone
	 * @param workTimezone the work timezone
	 * @param amPmClassification the am pm classification
	 */
	public FlexHalfDayWorkTimeDto(List<FlowWorkRestTimezoneDto> restTimezone, FixedWorkTimezoneSetDto workTimezone,
			Integer amPmClassification) {
		super();
		this.restTimezone = restTimezone;
		this.workTimezone = workTimezone;
		AmPmClassification = amPmClassification;
	}

	
}
