/******************************************************************
 * Copyright (c) 2016 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.core.app.wagetable.certification.command;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;
import nts.uk.ctx.pr.core.app.wagetable.certification.command.dto.CertifyGroupDto;
import nts.uk.ctx.pr.core.dom.wagetable.certification.CertifyGroup;

/**
 * The Class CertifyGroupAddCommand.
 */
@Setter
@Getter
public class CertifyGroupAddCommand implements Serializable {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The certify group dto. */
	private CertifyGroupDto certifyGroupDto;

	/**
	 * To domain.
	 *
	 * @return the certify group
	 */
	public CertifyGroup toDomain(String companyCode) {
		return certifyGroupDto.toDomain(companyCode);
	}
}
