/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.report.ws.salarydetail;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import nts.uk.ctx.pr.report.app.salarydetail.printsetting.command.SalaryPrintSettingSaveCommand;
import nts.uk.ctx.pr.report.app.salarydetail.printsetting.command.SalaryPrintSettingSaveCommandHandler;
import nts.uk.ctx.pr.report.app.salarydetail.printsetting.find.SalaryPrintSettingFinder;
import nts.uk.ctx.pr.report.app.salarydetail.printsetting.find.dto.SalaryPrintSettingDto;

/**
 * The Class SalaryPrintSettingWs.
 */
@Path("ctx/pr/report/salarydetail/printsetting")
@Produces("application/json")
@Stateless
public class SalaryPrintSettingWs {

	/** The save salary print setting command handler. */
	@Inject
	private SalaryPrintSettingSaveCommandHandler saveSalaryPrintSettingCommandHandler;

	/** The finder. */
	@Inject
	private SalaryPrintSettingFinder finder;

	/**
	 * Find.
	 */
	@POST
	@Path("find")
	public SalaryPrintSettingDto find() {
		return this.finder.find();
	}

	/**
	 * Save.
	 *
	 * @param command the command
	 */
	@POST
	@Path("save")
	public void save(SalaryPrintSettingSaveCommand command) {
		saveSalaryPrintSettingCommandHandler.handle(command);
	}
}
