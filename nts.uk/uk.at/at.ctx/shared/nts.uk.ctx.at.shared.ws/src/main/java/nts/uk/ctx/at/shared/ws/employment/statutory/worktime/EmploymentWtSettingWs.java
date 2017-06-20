/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.at.shared.ws.employment.statutory.worktime;

import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import nts.arc.layer.ws.WebService;
import nts.uk.ctx.at.shared.app.employment.statutory.worktime.employment.command.EmploymentWtSettingRemoveCommand;
import nts.uk.ctx.at.shared.app.employment.statutory.worktime.employment.command.EmploymentWtSettingRemoveCommandHandler;
import nts.uk.ctx.at.shared.app.employment.statutory.worktime.employment.command.EmploymentWtSettingSaveCommand;
import nts.uk.ctx.at.shared.app.employment.statutory.worktime.employment.command.EmploymentWtSettingSaveCommandHandler;
import nts.uk.ctx.at.shared.app.employment.statutory.worktime.employment.find.EmploymentWtSettingDto;
import nts.uk.ctx.at.shared.app.employment.statutory.worktime.employment.find.EmploymentWtSettingFinder;
import nts.uk.ctx.at.shared.app.employment.statutory.worktime.employment.find.EmploymentWtSettingRequest;

/**
 * The Class CompanySettingWs.
 */
@Path("ctx/at/shared/employment/statutory/worktime/employment")
@Produces("application/json")
public class EmploymentWtSettingWs extends WebService {

	/** The save handler. */
	@Inject
	private EmploymentWtSettingSaveCommandHandler saveHandler;

	/** The remove handler. */
	@Inject
	private EmploymentWtSettingRemoveCommandHandler removeHandler;

	/** The finder. */
	@Inject
	private EmploymentWtSettingFinder finder;

	/**
	 * Find.
	 *
	 * @param year the year
	 * @return the company setting dto
	 */
	@POST
	@Path("find")
	public EmploymentWtSettingDto find(EmploymentWtSettingRequest request) {
		return this.finder.find(request);
	}

	/**
	 * Find.
	 *
	 * @param year the year
	 * @return the company setting dto
	 */
	@POST
	@Path("findall/{year}")
	public List<String> findall(@PathParam("year") int year) {
		return this.finder.findall(year);
	}

	/**
	 * Removes the.
	 *
	 * @param command the command
	 */
	@POST
	@Path("remove")
	public void remove(EmploymentWtSettingRemoveCommand command) {
		this.removeHandler.handle(command);
	}

	/**
	 * Save.
	 *
	 * @param command the command
	 */
	@POST
	@Path("save")
	public void save(EmploymentWtSettingSaveCommand command) {
		this.saveHandler.handle(command);
	}
}
