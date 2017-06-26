/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.core.ws.base.simplehistory.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * The Class HistoryModel.
 */
@Getter
@Setter
@Builder
public class HistoryModel {

	/** The uuid. */
	private String uuid;

	/** The start. */
	private int start;

	/** The end. */
	private int end;
}
