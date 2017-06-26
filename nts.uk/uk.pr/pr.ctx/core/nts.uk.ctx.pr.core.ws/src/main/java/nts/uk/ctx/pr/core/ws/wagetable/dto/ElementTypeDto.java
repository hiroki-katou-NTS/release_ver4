/******************************************************************
 * Copyright (c) 2015 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.core.ws.wagetable.dto;

import lombok.Builder;
import lombok.Data;

/**
 * The Class ElementTypeDto.
 */
@Data
@Builder
public class ElementTypeDto {
	/** The value. */
	public int value;

	/** The is code mode. */
	public boolean isCodeMode;

	/** The is range mode. */
	public boolean isRangeMode;

	/** The display name. */
	public String displayName;
}
