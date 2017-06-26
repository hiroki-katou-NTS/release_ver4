/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.core.app.wagetable.certification.command;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;
import nts.uk.ctx.pr.core.app.wagetable.certification.command.dto.CertifyGroupDto;
import nts.uk.ctx.pr.core.dom.wagetable.certification.CertifyGroup;

/**
 * The Class CertifyGroupUpdateCommand.
 */
@Setter
@Getter
public class CertifyGroupUpdateCommand implements Serializable {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The certify group dto. */
	private CertifyGroupDto certifyGroupDto;

	/**
	 * To domain.
	 *
	 * @param companyCode the company code
	 * @return the certify group
	 */
	public CertifyGroup toDomain(String companyCode) {
		return certifyGroupDto.toDomain(companyCode);
	}

}
