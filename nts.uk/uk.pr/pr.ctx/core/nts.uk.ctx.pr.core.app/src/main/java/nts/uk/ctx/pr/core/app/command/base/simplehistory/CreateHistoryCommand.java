/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.core.app.command.base.simplehistory;

import lombok.Getter;
import lombok.Setter;

/**
 * The Class CreateHistoryCommand.
 */
@Getter
@Setter
public class CreateHistoryCommand {
	
	/** The master code. */
	private String masterCode;
	
	/** The start year month. */
	private int startYearMonth;
	
	/** The is copy from latest. */
	private boolean copyFromLatest;
}
