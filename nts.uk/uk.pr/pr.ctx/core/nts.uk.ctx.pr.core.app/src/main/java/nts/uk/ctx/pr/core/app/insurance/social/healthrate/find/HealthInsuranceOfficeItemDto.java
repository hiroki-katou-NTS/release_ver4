/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.core.app.insurance.social.healthrate.find;

import java.util.List;

import lombok.Builder;
import lombok.Data;

/**
 * The Class HealthInsuranceOfficeItemDto.
 */
@Data
@Builder
public class HealthInsuranceOfficeItemDto {

	/** The office code. */
	public String officeCode;

	/** The office name. */
	public String officeName;

	/** The list history. */
	List<HealthInsuranceHistoryItemDto> listHistory;

}
