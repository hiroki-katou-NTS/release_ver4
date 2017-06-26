/******************************************************************
 * Copyright (c) 2016 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.core.app.insurance.labor.unemployeerate.command;

import lombok.Getter;
import lombok.Setter;

/**
 * The Class UnemployeeInsuranceRateCopyCommand.
 */
@Getter
@Setter
public class UnemployeeInsuranceRateCopyCommand {

	/** The history id copy. */
	private String historyIdCopy;

	/** The start month. */
	private Integer startMonth;

	/** The add new. */
	private boolean addNew;

}
