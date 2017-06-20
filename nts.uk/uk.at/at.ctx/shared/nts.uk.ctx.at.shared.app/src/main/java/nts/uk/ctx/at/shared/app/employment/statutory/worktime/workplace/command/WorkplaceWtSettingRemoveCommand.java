/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.at.shared.app.employment.statutory.worktime.workplace.command;

import lombok.Data;

/**
 * The Class CompanySettingRemoveCommand.
 */
@Data
public class WorkplaceWtSettingRemoveCommand {

	/** The year. */
	private int year;

	/** The work place id. */
	private String workPlaceId;
}
