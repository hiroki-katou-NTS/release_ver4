/******************************************************************
 * Copyright (c) 2016 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.core.app.insurance.labor.unemployeerate.command.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UnemployeeInsuranceRateItemSettingDto {
	
	/** The round atr. */
	private Integer roundAtr;

	/** The code. */
	private Double rate;
}
