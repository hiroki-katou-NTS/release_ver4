/******************************************************************
 * Copyright (c) 2015 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.report.app.itemmaster.query;

import lombok.Builder;

/**
 * The Class ItemMasterDto.
 */
@Builder
public class MasterItemDto {
	
	/** The code. */
	public String code;
	
	/** The name. */
	public String name;
	
	/** The category. */
	public ItemMasterCategory category;
}
