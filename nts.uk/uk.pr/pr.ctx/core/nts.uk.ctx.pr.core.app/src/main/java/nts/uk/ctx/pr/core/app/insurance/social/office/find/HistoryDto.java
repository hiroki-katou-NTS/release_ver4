/******************************************************************
 * Copyright (c) 2016 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.core.app.insurance.social.office.find;

import lombok.Data;

/**
 * Instantiates a new history dto.
 */
@Data
public class HistoryDto {
	
	/** The company code. */
	private String companyCode;
	
	/** The office code. */
	private String officeCode;
	
	/** The start. */
	private String start;
	
	/** The end. */
	private String end;
	
	/** The code. */
	private String code;

	/** The name. */
	private String name;
}
