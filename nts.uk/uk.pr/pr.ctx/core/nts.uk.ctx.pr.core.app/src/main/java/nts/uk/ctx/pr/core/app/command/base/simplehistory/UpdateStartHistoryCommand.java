/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.core.app.command.base.simplehistory;

import lombok.Getter;
import lombok.Setter;

/**
 * The Class DeleteHistoryCommand.
 */
@Getter
@Setter
public class UpdateStartHistoryCommand {
	/** The history id. */
	private String historyId;

	/** The new year month. */
	private int newYearMonth;
}