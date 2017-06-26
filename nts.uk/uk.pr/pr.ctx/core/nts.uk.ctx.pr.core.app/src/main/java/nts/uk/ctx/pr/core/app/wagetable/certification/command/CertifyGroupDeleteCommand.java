/******************************************************************
 * Copyright (c) 2016 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.core.app.wagetable.certification.command;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;
import nts.uk.ctx.pr.core.app.wagetable.certification.command.dto.CertifyGroupDeleteDto;

/**
 * The Class CertifyGroupDeleteCommand.
 */
@Setter
@Getter
public class CertifyGroupDeleteCommand implements Serializable {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The certify group dto. */
	private CertifyGroupDeleteDto certifyGroupDeleteDto;

}
