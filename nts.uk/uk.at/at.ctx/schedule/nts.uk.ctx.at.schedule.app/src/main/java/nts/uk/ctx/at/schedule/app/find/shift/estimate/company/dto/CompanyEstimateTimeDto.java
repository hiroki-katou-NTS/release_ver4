/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.at.schedule.app.find.shift.estimate.company.dto;

import java.io.Serializable;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
import nts.uk.ctx.at.schedule.app.find.shift.estimate.dto.EstimateTimeDto;

/**
 * The Class CompanyEstimateTimeDto.
 */
@Getter
@Setter
public class CompanyEstimateTimeDto implements Serializable {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The monthly estimates. */
	private List<EstimateTimeDto> monthlyEstimates;

	/** The yearly estimate. */
	private EstimateTimeDto yearlyEstimate;
}
