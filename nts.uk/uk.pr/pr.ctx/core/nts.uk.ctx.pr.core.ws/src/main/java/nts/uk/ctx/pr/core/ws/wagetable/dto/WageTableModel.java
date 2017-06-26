/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.core.ws.wagetable.dto;

import lombok.Data;
import nts.uk.ctx.pr.core.app.wagetable.command.dto.WtHeadDto;
import nts.uk.ctx.pr.core.app.wagetable.command.dto.WtHistoryDto;

/**
 * The Class WageTableModel.
 */
@Data
public class WageTableModel {

	/** The head. */
	private WtHeadDto head;

	/** The history. */
	private WtHistoryDto history;
}
