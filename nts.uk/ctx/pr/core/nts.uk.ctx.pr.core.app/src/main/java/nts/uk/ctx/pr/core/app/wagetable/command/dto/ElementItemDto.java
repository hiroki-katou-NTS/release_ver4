/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.core.app.wagetable.command.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * The Class CodeItem.
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ElementItemDto {

	/** The uuid. */
	private String uuid;

	/** The reference code. */
	private String referenceCode;

	/** The order number. */
	private Integer orderNumber;

	/** The start val. */
	private Double startVal;

	/** The end val. */
	private Double endVal;

}
