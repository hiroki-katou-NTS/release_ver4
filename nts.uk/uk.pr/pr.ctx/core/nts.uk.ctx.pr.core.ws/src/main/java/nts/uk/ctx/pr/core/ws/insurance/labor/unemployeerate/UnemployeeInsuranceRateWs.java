/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.core.ws.insurance.labor.unemployeerate;

import javax.inject.Inject;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import nts.uk.ctx.pr.core.app.insurance.labor.unemployeerate.command.UnemployeeInsuranceRateAddCommand;
import nts.uk.ctx.pr.core.app.insurance.labor.unemployeerate.command.UnemployeeInsuranceRateAddCommandHandler;
import nts.uk.ctx.pr.core.app.insurance.labor.unemployeerate.command.UnemployeeInsuranceRateCopyCommand;
import nts.uk.ctx.pr.core.app.insurance.labor.unemployeerate.command.UnemployeeInsuranceRateCopyCommandHandler;
import nts.uk.ctx.pr.core.app.insurance.labor.unemployeerate.command.UnemployeeInsuranceRateDeleteCommand;
import nts.uk.ctx.pr.core.app.insurance.labor.unemployeerate.command.UnemployeeInsuranceRateDeleteCommandHandler;
import nts.uk.ctx.pr.core.app.insurance.labor.unemployeerate.command.UnemployeeInsuranceRateUpdateCommand;
import nts.uk.ctx.pr.core.app.insurance.labor.unemployeerate.command.UnemployeeInsuranceRateUpdateCommandHandler;
import nts.uk.ctx.pr.core.app.insurance.labor.unemployeerate.find.UnemployeeInsuranceFinder;
import nts.uk.ctx.pr.core.app.insurance.labor.unemployeerate.find.dto.UnemployeeInsuranceRateFindDto;

/**
 * The Class UnemployeeInsuranceRateWs.
 */
@Path("pr/insurance/labor/unemployeerate")
@Produces("application/json")
public class UnemployeeInsuranceRateWs {

	/** The find. */
	@Inject
	private UnemployeeInsuranceFinder find;

	/** The copy. */
	@Inject
	private UnemployeeInsuranceRateCopyCommandHandler copy;

	/** The add. */
	@Inject
	private UnemployeeInsuranceRateAddCommandHandler add;

	/** The update. */
	@Inject
	private UnemployeeInsuranceRateUpdateCommandHandler update;

	/** The delete. */
	@Inject
	private UnemployeeInsuranceRateDeleteCommandHandler delete;

	/**
	 * Detail.
	 *
	 * @param historyId
	 *            the history id
	 * @return the unemployee insurance rate find out dto
	 */
	@POST
	@Path("detail/{historyId}")
	public UnemployeeInsuranceRateFindDto detail(@PathParam("historyId") String historyId) {
		return this.find.findById(historyId);
	}

	/**
	 * Copy.
	 *
	 * @param command
	 *            the command
	 */
	@POST
	@Path("copy")
	public void copy(UnemployeeInsuranceRateCopyCommand command) {
		this.copy.handle(command);
	}

	/**
	 * Adds the.
	 *
	 * @param command
	 *            the command
	 */
	@POST
	@Path("add")
	public void add(UnemployeeInsuranceRateAddCommand command) {
		this.add.handle(command);
	}

	/**
	 * Update.
	 *
	 * @param command
	 *            the command
	 */
	@POST
	@Path("update")
	public void update(UnemployeeInsuranceRateUpdateCommand command) {
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
	public void delete(UnemployeeInsuranceRateDeleteCommand command) {
		this.delete.handle(command);
	}

}
