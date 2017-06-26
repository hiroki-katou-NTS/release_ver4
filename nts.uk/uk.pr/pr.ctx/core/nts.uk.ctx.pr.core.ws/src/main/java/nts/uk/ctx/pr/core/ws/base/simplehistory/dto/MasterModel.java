/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.core.ws.base.simplehistory.dto;

import java.util.List;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * The Class MasterModel.
 */
@Getter
@Setter
@Builder
public class MasterModel {

	/** The code. */
	private String code;

	/** The name. */
	private String name;

	/** The history model. */
	private List<HistoryModel> historyList;
}
