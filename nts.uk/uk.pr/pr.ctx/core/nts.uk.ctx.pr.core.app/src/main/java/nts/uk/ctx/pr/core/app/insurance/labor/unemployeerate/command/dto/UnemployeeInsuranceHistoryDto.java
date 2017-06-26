/******************************************************************
 * Copyright (c) 2016 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.core.app.insurance.labor.unemployeerate.command.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * The Class UnemployeeInsuranceHistoryDto.
 */

/**
 * Instantiates a new unemployee insurance history dto.
 */
@Getter
@Setter
public class UnemployeeInsuranceHistoryDto {

	/** The history id. */
	private String historyId;

	/** The start month. */
	private int startMonth;

	/** The end month. */
	private int endMonth;

}
