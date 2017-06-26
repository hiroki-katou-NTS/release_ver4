/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.core.ws.insurance.labor.accidentrate;

import javax.inject.Inject;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import nts.arc.layer.ws.WebService;
import nts.uk.ctx.pr.core.app.insurance.labor.accidentrate.command.AccidentInsuranceRateAddCommand;
import nts.uk.ctx.pr.core.app.insurance.labor.accidentrate.command.AccidentInsuranceRateAddCommandHandler;
import nts.uk.ctx.pr.core.app.insurance.labor.accidentrate.command.AccidentInsuranceRateCopyCommand;
import nts.uk.ctx.pr.core.app.insurance.labor.accidentrate.command.AccidentInsuranceRateCopyCommandHandler;
import nts.uk.ctx.pr.core.app.insurance.labor.accidentrate.command.AccidentInsuranceRateDeleteCommand;
import nts.uk.ctx.pr.core.app.insurance.labor.accidentrate.command.AccidentInsuranceRateDeleteCommandHandler;
import nts.uk.ctx.pr.core.app.insurance.labor.accidentrate.command.AccidentInsuranceRateUpdateCommand;
import nts.uk.ctx.pr.core.app.insurance.labor.accidentrate.command.AccidentInsuranceRateUpdateCommandHandler;
import nts.uk.ctx.pr.core.app.insurance.labor.accidentrate.find.AccidentInsuranceRateFinder;
import nts.uk.ctx.pr.core.app.insurance.labor.accidentrate.find.dto.AccidentInsuranceRateFindDto;

/**
 * The Class AccidentInsuranceRateWs.
 */
@Path("pr/insurance/labor/accidentrate")
@Produces("application/json")
public class AccidentInsuranceRateWs extends WebService {

	/** The add. */
	@Inject
	private AccidentInsuranceRateAddCommandHandler add;

	/** The copy. */
	@Inject
	private AccidentInsuranceRateCopyCommandHandler copy;

	/** The update. */
	@Inject
	private AccidentInsuranceRateUpdateCommandHandler update;

	/** The delete. */
	@Inject
	private AccidentInsuranceRateDeleteCommandHandler delete;

	/** The find. */
	@Inject
	private AccidentInsuranceRateFinder find;

	/**
	 * Find.
	 *
	 * @param historyId
	 *            the history id
	 * @return the accident insurance rate find out
	 */
	@POST
	@Path("find/{historyId}")
	public AccidentInsuranceRateFindDto find(@PathParam("historyId") String historyId) {
		return this.find.find(historyId);
	}

	/**
	 * Adds the.
	 *
	 * @param command
	 *            the command
	 */
	@POST
	@Path("add")
	public void add(AccidentInsuranceRateAddCommand command) {
		this.add.handle(command);
	}

	/**
	 * Copy.
	 *
	 * @param command
	 *            the command
	 */
	@POST
	@Path("copy")
	public void copy(AccidentInsuranceRateCopyCommand command) {
		this.copy.handle(command);
	}

	/**
	 * Update.
	 *
	 * @param command
	 *            the command
	 */
	@POST
	@Path("update")
	public void update(AccidentInsuranceRateUpdateCommand command) {
		this.update.handle(command);
	}

	/**
	 * Delete.
	 *
	 * @param command
	 *            the command
	 */
	@POST
	@Path("delete")
	public void delete(AccidentInsuranceRateDeleteCommand command) {
		this.delete.handle(command);
	}

}
