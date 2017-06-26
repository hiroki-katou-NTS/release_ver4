/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.core.ws.wagetable.dto;

import lombok.Builder;
import lombok.Data;

/**
 * The Class WageTableItemModel.
 */
@Data
@Builder
public class WageTableItemModel {

	/** The code. */
	private String code;

	/** The name. */
	private String name;

}
