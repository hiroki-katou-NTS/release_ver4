/******************************************************************
 * Copyright (c) 2016 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.core.app.insurance.labor.unemployeerate.command;

import lombok.Getter;
import lombok.Setter;
import nts.uk.ctx.pr.core.app.insurance.labor.unemployeerate.command.dto.UnemployeeInsuranceRateDto;
import nts.uk.ctx.pr.core.dom.insurance.labor.unemployeerate.UnemployeeInsuranceRate;

@Getter
@Setter
public class UnemployeeInsuranceRateUpdateCommand {
	/** The history info dto. */
	private UnemployeeInsuranceRateDto unemployeeInsuranceRate;

	/**
	 * To domain.
	 *
	 * @param companyCode
	 *            the company code
	 * @return the unemployee insurance rate
	 */
	public UnemployeeInsuranceRate toDomain(String companyCode) {
		return unemployeeInsuranceRate.toDomain(companyCode);
	}

}
