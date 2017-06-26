/******************************************************************
 * Copyright (c) 2015 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.report.app.wageledger.command;

import lombok.Getter;
import nts.uk.ctx.pr.report.app.wageledger.command.dto.ItemSubjectDto;

/**
 * The Class AggregateItemRemoveCommand.
 */
@Getter
public class AggregateItemRemoveCommand {
	
	/** The subject. */
	private ItemSubjectDto subject; 
}
