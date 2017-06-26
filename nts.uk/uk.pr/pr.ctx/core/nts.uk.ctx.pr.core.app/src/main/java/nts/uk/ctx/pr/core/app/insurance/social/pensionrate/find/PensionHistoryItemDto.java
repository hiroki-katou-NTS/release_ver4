/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.core.app.insurance.social.pensionrate.find;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PensionHistoryItemDto {

	/** The history id. */
	public String historyId;

	/** The start month. */
	public String startMonth;

	/** The end month. */
	public String endMonth;
}
