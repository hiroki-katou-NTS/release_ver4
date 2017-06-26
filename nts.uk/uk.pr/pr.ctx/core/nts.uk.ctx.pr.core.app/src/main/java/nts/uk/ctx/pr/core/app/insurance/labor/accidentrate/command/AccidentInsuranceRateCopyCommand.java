/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.core.app.insurance.labor.accidentrate.command;

import lombok.Getter;
import lombok.Setter;

/**
 * The Class AccidentInsuranceRateCopyCommand.
 */
@Getter
@Setter
public class AccidentInsuranceRateCopyCommand {

	/** The history id copy. */
	private String historyIdCopy;

	/** The start month. */
	private Integer startMonth;

	/** The add new. */
	private boolean addNew;
}
