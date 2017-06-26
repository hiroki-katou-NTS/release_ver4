/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.core.app.insurance.social.healthrate.find;

import lombok.Builder;
import lombok.Data;

/**
 * The Class HealthInsuranceHistoryItemDto.
 */
@Data
@Builder
public class HealthInsuranceHistoryItemDto {
	/** The history id. */
	public String historyId;

	/** The start month. */
	public String startMonth;

	/** The end month. */
	public String endMonth;
}
