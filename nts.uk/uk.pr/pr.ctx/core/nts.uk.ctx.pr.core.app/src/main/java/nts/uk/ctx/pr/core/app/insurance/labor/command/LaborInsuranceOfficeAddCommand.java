/******************************************************************
 * Copyright (c) 2016 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.core.app.insurance.labor.command;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;
import nts.uk.ctx.pr.core.app.insurance.labor.command.dto.LaborInsuranceOfficeDto;
import nts.uk.ctx.pr.core.dom.insurance.labor.LaborInsuranceOffice;

/**
 * The Class AddLaborInsuranceOfficeCommand.
 */
@Getter
@Setter
public class LaborInsuranceOfficeAddCommand implements Serializable {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The labor insurance office. */
	private LaborInsuranceOfficeDto laborInsuranceOfficeDto;

	/**
	 * To domain.
	 *
	 * @return the labor insurance office
	 */
	public LaborInsuranceOffice toDomain(String companyCode) {
		return laborInsuranceOfficeDto.toDomain(companyCode);
	}
}
