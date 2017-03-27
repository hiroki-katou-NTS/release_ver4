/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.core.ws.wagetable.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * The Class WtElementDto.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WtElementDto {

	/** The demension no. */
	private Integer demensionNo;

	/** The demension name. */
	private String demensionName;

	/** The type. */
	private Integer type;

	/** The reference code. */
	private String referenceCode;

}
