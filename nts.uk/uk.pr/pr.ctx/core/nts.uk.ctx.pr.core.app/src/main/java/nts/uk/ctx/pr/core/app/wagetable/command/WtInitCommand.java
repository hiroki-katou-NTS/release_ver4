/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.core.app.wagetable.command;

import lombok.Getter;
import lombok.Setter;
import nts.uk.ctx.pr.core.app.wagetable.command.dto.WtHeadDto;

/**
 * The Class WageTableHistoryAddCommand.
 */
@Setter
@Getter
public class WtInitCommand {

	/** The wage table head dto. */
	private WtHeadDto wageTableHeadDto;

	/** The start month. */
	private Integer startMonth;
	
}
