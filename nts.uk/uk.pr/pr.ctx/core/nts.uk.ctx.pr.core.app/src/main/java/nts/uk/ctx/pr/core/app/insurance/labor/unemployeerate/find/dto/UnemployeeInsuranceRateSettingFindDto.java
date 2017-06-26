/******************************************************************
 * Copyright (c) 2016 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.core.app.insurance.labor.unemployeerate.find.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * The Class UnemployeeInsuranceRateItemSettingFindOutDto.
 */
@Getter
@Setter
public class UnemployeeInsuranceRateSettingFindDto {
	/** The round atr. */
	private Integer roundAtr;

	/** The code. */
	private Double rate;
}
