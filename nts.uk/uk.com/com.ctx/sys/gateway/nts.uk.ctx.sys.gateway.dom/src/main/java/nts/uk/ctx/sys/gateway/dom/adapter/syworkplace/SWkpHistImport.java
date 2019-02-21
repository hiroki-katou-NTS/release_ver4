/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.sys.gateway.dom.adapter.syworkplace;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * The Class SWkpHistImport.
 */
@Data
@AllArgsConstructor
public class SWkpHistImport {
	/** The workplace id. */
	// 職場ID
	private String workplaceId;
	
	/** The work place name. */
	//職場名称
	private String workPlaceName;
}
