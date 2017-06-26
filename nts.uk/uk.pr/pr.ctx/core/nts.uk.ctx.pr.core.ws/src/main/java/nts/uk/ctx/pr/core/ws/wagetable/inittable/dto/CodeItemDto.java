/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.core.ws.wagetable.inittable.dto;

import lombok.Builder;
import lombok.Data;

/**
 * The Class CertifyItemDto.
 */
@Data
@Builder
public class CodeItemDto {

	/** The code. */
	private String code;

	/** The name. */
	private String name;

}
