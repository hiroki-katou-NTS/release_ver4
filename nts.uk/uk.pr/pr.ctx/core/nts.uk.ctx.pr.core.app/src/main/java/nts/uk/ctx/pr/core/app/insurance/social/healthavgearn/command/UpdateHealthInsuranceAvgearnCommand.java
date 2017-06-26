/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.core.app.insurance.social.healthavgearn.command;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

/**
 * The Class UpdateHealthInsuranceAvgearnCommand.
 */
@Getter
@Setter
public class UpdateHealthInsuranceAvgearnCommand {

	/** The list health insurance avgearn. */
	private List<HealthInsuranceAvgearnCommandDto> listHealthInsuranceAvgearnDto;

	/** The history id. */
	private String historyId;

	/** The office code. */
	private String officeCode;
}
