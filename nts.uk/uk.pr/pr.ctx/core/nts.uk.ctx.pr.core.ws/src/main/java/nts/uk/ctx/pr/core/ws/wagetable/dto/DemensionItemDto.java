/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.core.ws.wagetable.dto;

import lombok.Data;
import nts.uk.ctx.pr.core.dom.wagetable.ElementType;

/**
 * The Class DemensionItemDto.
 */
@Data
public class DemensionItemDto {

	/** The type. */
	private Integer type;

	/** The code. */
	private String code;

	/** The name. */
	private String name;

	/** The is code mode. */
	public boolean isCodeMode;

	/** The is range mode. */
	public boolean isRangeMode;

	/**
	 * Instantiates a new demension item dto.
	 *
	 * @param type the type
	 * @param code the code
	 * @param name the name
	 */
	public DemensionItemDto(ElementType type, String code, String name) {
		super();
		this.type = type.value;
		this.code = code;
		this.name = name;
		this.isCodeMode = type.isCodeMode;
		this.isRangeMode = type.isRangeMode;
	}

	/**
	 * Instantiates a new demension item dto.
	 *
	 * @param type the type
	 */
	public DemensionItemDto(ElementType type) {
		super();
		this.type = type.value;
		this.code = String.valueOf(type.value);
		this.name = type.displayName;
		this.isCodeMode = type.isCodeMode;
		this.isRangeMode = type.isRangeMode;
	}

}
