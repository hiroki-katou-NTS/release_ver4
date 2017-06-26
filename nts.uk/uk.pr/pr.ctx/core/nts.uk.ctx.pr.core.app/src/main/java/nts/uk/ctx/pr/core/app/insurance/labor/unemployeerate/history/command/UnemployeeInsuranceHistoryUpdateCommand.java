/******************************************************************
 * Copyright (c) 2016 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.core.app.insurance.labor.unemployeerate.history.command;

import lombok.Getter;
import lombok.Setter;

/**
 * The Class UnemployeeInsuranceHistoryUpdateCommand.
 */
@Getter
@Setter
public class UnemployeeInsuranceHistoryUpdateCommand {

	/** The history id copy. */
	private String historyId;

	/** The start month. */
	private Integer startMonth;

	/** The end month. */
	private Integer endMonth;

}
