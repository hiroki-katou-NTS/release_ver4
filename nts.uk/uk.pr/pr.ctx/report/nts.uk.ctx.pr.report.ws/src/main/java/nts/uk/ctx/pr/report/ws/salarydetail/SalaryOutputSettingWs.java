/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.report.ws.salarydetail;

import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import nts.arc.layer.ws.WebService;
import nts.uk.ctx.pr.report.app.salarydetail.outputsetting.command.SalaryOutputSettingRemoveCommand;
import nts.uk.ctx.pr.report.app.salarydetail.outputsetting.command.SalaryOutputSettingRemoveCommandHandler;
import nts.uk.ctx.pr.report.app.salarydetail.outputsetting.command.SalaryOutputSettingSaveCommand;
import nts.uk.ctx.pr.report.app.salarydetail.outputsetting.command.SalaryOutputSettingSaveCommandHandler;
import nts.uk.ctx.pr.report.app.salarydetail.outputsetting.find.SalaryOutputSettingFinder;
import nts.uk.ctx.pr.report.app.salarydetail.outputsetting.find.dto.SalaryOutputSettingDto;
import nts.uk.ctx.pr.report.app.salarydetail.outputsetting.find.dto.SalaryOutputSettingHeaderDto;

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

	@Inject
	private SalaryOutputSettingFinder finder;
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

	/**
	 * Find.
	 *
	 * @param id the id
	 */
	@POST
	@Path("find/{id}")
	public SalaryOutputSettingDto find(@PathParam("id") String id) {
		return finder.find(id);
	}

	/**
	 * Findall.
	 */
	@POST
	@Path("findall")
	public List<SalaryOutputSettingHeaderDto> findall() {
		return finder.findAll();
	}
}
