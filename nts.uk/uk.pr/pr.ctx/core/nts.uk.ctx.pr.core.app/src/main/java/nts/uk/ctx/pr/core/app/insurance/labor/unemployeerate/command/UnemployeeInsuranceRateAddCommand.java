/******************************************************************
 * Copyright (c) 2016 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.core.app.insurance.labor.unemployeerate.command;

import lombok.Getter;
import lombok.Setter;
import nts.uk.ctx.pr.core.app.insurance.labor.unemployeerate.command.dto.UnemployeeInsuranceRateDto;
import nts.uk.ctx.pr.core.dom.insurance.labor.unemployeerate.UnemployeeInsuranceRate;

/**
 * The Class UnemployeeInsuranceRateAddCommand.
 */
@Getter
@Setter
public class UnemployeeInsuranceRateAddCommand {

	/** The history info dto. */
	private UnemployeeInsuranceRateDto unemployeeInsuranceRate;

	/**
	 * To domain.
	 *
	 * @return the labor insurance office
	 */
	public UnemployeeInsuranceRate toDomain(String companyCode) {
		return this.unemployeeInsuranceRate.toDomain(companyCode);
	}

}
