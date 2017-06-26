/******************************************************************
 * Copyright (c) 2016 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.core.app.insurance.social.pensionavgearn.command;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

/**
 * The Class UpdatePensionAvgearnCommand.
 */
@Getter
@Setter
public class UpdatePensionAvgearnCommand {

	/** The list pension avgearn. */
	private List<PensionAvgearnCommandDto> listPensionAvgearnDto;

	/** The history id. */
	private String historyId;

	/** The office code. */
	private String officeCode;
}
