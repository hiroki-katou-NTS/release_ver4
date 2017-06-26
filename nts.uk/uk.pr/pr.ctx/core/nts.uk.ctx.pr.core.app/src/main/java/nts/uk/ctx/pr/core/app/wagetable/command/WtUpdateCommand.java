/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.core.app.wagetable.command;

import lombok.Getter;
import lombok.Setter;
import nts.uk.ctx.pr.core.app.wagetable.command.dto.WtHistoryDto;

/**
 * The Class WageTableHistoryUpdateCommand.
 */
@Setter
@Getter
public class WtUpdateCommand {

	// Header
	/** The name. */
	private String code;
	
	/** The name. */
	private String name;

	/** The memo. */
	private String memo;

	// History
	private WtHistoryDto wtHistoryDto;
}
