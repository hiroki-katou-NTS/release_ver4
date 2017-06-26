/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.core.app.insurance.social.healthavgearn.find;

import java.util.List;

import lombok.Builder;
import lombok.Data;

/**
 * The Class ListHealthInsuranceAvgearnModel.
 */
@Builder
@Data
public class HealthInsAvgearnsModel {

	/** The history id. */
	private String historyId;

	/** The list health insurance avgearn dto. */
	private List<HealthInsuranceAvgearnDto> listHealthInsuranceAvgearnDto;
}
