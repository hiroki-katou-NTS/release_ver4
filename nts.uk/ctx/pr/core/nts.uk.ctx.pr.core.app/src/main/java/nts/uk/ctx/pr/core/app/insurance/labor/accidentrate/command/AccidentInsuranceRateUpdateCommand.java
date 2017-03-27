/******************************************************************
 * Copyright (c) 2016 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.core.app.insurance.labor.accidentrate.command;

import lombok.Getter;
import lombok.Setter;
import nts.uk.ctx.pr.core.app.insurance.labor.accidentrate.command.dto.AccidentInsuranceRateDto;
import nts.uk.ctx.pr.core.dom.insurance.labor.accidentrate.AccidentInsuranceRate;

/**
 * The Class AccidentInsuranceRateUpdateCommand.
 */
@Getter
@Setter
public class AccidentInsuranceRateUpdateCommand {

	/** The accident insurance rate. */
	private AccidentInsuranceRateDto accidentInsuranceRate;

	/**
	 * To domain.
	 *
	 * @param companyCode
	 *            the company code
	 * @return the accident insurance rate
	 */
	public AccidentInsuranceRate toDomain(String companyCode) {
		return this.accidentInsuranceRate.toDomain(companyCode);

	}
}
