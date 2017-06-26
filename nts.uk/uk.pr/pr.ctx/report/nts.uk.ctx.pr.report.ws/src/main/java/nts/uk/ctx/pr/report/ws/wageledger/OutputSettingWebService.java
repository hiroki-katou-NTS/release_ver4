/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.report.ws.wageledger;

import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import nts.arc.layer.ws.WebService;
import nts.uk.ctx.pr.report.app.wageledger.command.OutputSettingRemoveCommand;
import nts.uk.ctx.pr.report.app.wageledger.command.OutputSettingRemoveCommandHandler;
import nts.uk.ctx.pr.report.app.wageledger.command.OutputSettingSaveCommand;
import nts.uk.ctx.pr.report.app.wageledger.command.OutputSettingSaveCommandHandler;
import nts.uk.ctx.pr.report.app.wageledger.find.OutputSettingFinder;
import nts.uk.ctx.pr.report.app.wageledger.find.dto.HeaderSettingDto;
import nts.uk.ctx.pr.report.app.wageledger.find.dto.OutputSettingDto;

/**
 * The Class OutputSettingWebService.
 */
@Path("ctx/pr/report/wageledger/outputsetting")
@Produces("application/json")
public class OutputSettingWebService extends WebService {

	/** The save handler. */
	@Inject
	private OutputSettingSaveCommandHandler saveHandler;

	/** The remove handler. */
	@Inject
	private OutputSettingRemoveCommandHandler removeHandler;

	/** The finder. */
	@Inject
	private OutputSettingFinder finder;

	/**
	 * Find all.
	 *
	 * @return the list
	 */
	@POST
	@Path("findAll")
	public List<HeaderSettingDto> findAll() {
		return this.finder.findAll();
	}

	/**
	 * Find detail.
	 *
	 * @param code the code
	 * @return the output setting dto
	 */
	@POST
	@Path("find/{code}")
	public OutputSettingDto findDetail(@PathParam("code") String code) {
		return this.finder.find(code);
	}

	/**
	 * Save.
	 *
	 * @param command the command
	 */
	@POST
	@Path("save")
	public void save(OutputSettingSaveCommand command) {
		this.saveHandler.handle(command);
	}

	/**
	 * Removes the.
	 *
	 * @param command the command
	 */
	@POST
	@Path("remove")
	public void remove(OutputSettingRemoveCommand command) {
		this.removeHandler.handle(command);
	}
}
