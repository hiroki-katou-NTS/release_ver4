/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.core.app.wagetable.command.dto;

import java.math.BigDecimal;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

/**
 * The Class ElementSettingDto.
 */
@Getter
@Setter
public class ElementSettingDto {

	/** The demension no. */
	private Integer demensionNo;

	/** The type. */
	private Integer type;

	/** The item list. */
	private List<ElementItemDto> itemList;

	/** The lower limit. */
	private BigDecimal lowerLimit;

	/** The upper limit. */
	private BigDecimal upperLimit;

	/** The interval. */
	private BigDecimal interval;

	/** The demension name. */
	private String demensionName;

}
