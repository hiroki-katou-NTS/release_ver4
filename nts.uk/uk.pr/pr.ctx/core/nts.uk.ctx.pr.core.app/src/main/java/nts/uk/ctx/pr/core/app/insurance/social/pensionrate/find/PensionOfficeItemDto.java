/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.core.app.insurance.social.pensionrate.find;

import java.util.List;

import lombok.Builder;
import lombok.Data;


@Data
@Builder
public class PensionOfficeItemDto {
	
	/** The office code. */
	public String officeCode;

	/** The office name. */
	public String officeName;

	/** The list history. */
	List<PensionHistoryItemDto> listHistory;

}
