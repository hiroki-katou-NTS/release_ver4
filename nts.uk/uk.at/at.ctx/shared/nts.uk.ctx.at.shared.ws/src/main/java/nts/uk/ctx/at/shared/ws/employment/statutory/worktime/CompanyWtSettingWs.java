/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.at.shared.ws.employment.statutory.worktime;

import javax.inject.Inject;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import nts.arc.layer.ws.WebService;
import nts.uk.ctx.at.shared.app.employment.statutory.worktime.company.command.CompanyWtSettingRemoveCommand;
import nts.uk.ctx.at.shared.app.employment.statutory.worktime.company.command.CompanyWtSettingRemoveCommandHandler;
import nts.uk.ctx.at.shared.app.employment.statutory.worktime.company.command.CompanyWtSettingSaveCommand;
import nts.uk.ctx.at.shared.app.employment.statutory.worktime.company.command.CompanyWtSettingSaveCommandHandler;
import nts.uk.ctx.at.shared.app.employment.statutory.worktime.company.find.CompanyWtSettingDto;
import nts.uk.ctx.at.shared.app.employment.statutory.worktime.company.find.CompanyWtSettingFinder;

/**
 * The Class CompanySettingWs.
 */
@Path("ctx/at/shared/employment/statutory/worktime/company")
@Produces("application/json")
public class CompanyWtSettingWs extends WebService {

	/** The save handler. */
	@Inject
	private CompanyWtSettingSaveCommandHandler saveHandler;

	/** The remove handler. */
	@Inject
	private CompanyWtSettingRemoveCommandHandler removeHandler;

	/** The finder. */
	@Inject
	private CompanyWtSettingFinder finder;

	/**
	 * Find.
	 *
	 * @param year the year
	 * @return the company setting dto
	 */
	@POST
	@Path("find/{year}")
	public CompanyWtSettingDto find(@PathParam("year") int year) {
		return this.finder.find(year);
	}

	/**
	 * Removes the.
	 *
	 * @param command the command
	 */
	@POST
	@Path("remove")
	public void remove(CompanyWtSettingRemoveCommand command) {
		this.removeHandler.handle(command);
	}

	/**
	 * Save.
	 *
	 * @param command the command
	 */
	@POST
	@Path("save")
	public void save(CompanyWtSettingSaveCommand command) {
		this.saveHandler.handle(command);
	}
}
