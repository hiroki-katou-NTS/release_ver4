/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.report.ws.salarydetail;

import javax.inject.Inject;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import nts.arc.layer.ws.WebService;
import nts.uk.ctx.pr.report.app.salarydetail.command.SalaryOutputSettingRemoveCommand;
import nts.uk.ctx.pr.report.app.salarydetail.command.SalaryOutputSettingRemoveCommandHandler;
import nts.uk.ctx.pr.report.app.salarydetail.command.SalaryOutputSettingSaveCommand;
import nts.uk.ctx.pr.report.app.salarydetail.command.SalaryOutputSettingSaveCommandHandler;

/**
 * The Class SalaryOutputSettingWs.
 */
@Path("ctx/pr/report/salary/outputsetting")
@Produces("application/json")
public class SalaryOutputSettingWs extends WebService {

	/** The save handler. */
	@Inject
	private SalaryOutputSettingSaveCommandHandler saveHandler;

	/** The remove handler. */
	@Inject
	private SalaryOutputSettingRemoveCommandHandler removeHandler;

	/**
	 * Save.
	 *
	 * @param command the command
	 */
	@POST
	@Path("save")
	public void save(SalaryOutputSettingSaveCommand command) {
		this.saveHandler.handle(command);
	}

	/**
	 * Removes the.
	 *
	 * @param command the command
	 */
	@POST
	@Path("remove")
	public void remove(SalaryOutputSettingRemoveCommand command) {
		this.removeHandler.handle(command);
	}
}
