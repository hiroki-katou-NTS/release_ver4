/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.core.ws.insurance.social.pensionavgearn;

import javax.inject.Inject;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import nts.arc.layer.ws.WebService;
import nts.uk.ctx.pr.core.app.insurance.social.healthavgearn.command.RecalculateHealthInsuAvgearnCommand;
import nts.uk.ctx.pr.core.app.insurance.social.pensionavgearn.command.RecalculatePensionAvgearnCommandHandler;
import nts.uk.ctx.pr.core.app.insurance.social.pensionavgearn.command.UpdatePensionAvgearnCommand;
import nts.uk.ctx.pr.core.app.insurance.social.pensionavgearn.command.UpdatePensionAvgearnCommandHandler;
import nts.uk.ctx.pr.core.app.insurance.social.pensionavgearn.find.ListPensionAvgearnModel;
import nts.uk.ctx.pr.core.app.insurance.social.pensionavgearn.find.PensionAvgearnFinder;

/**
 * The Class PensionAvgearnWs.
 */
@Path("ctx/pr/core/insurance/social/pensionavgearn")
@Produces("application/json")
public class PensionAvgearnWs extends WebService {

	/** The pension avgearn finder. */
	@Inject
	private PensionAvgearnFinder pensionAvgearnFinder;

	/** The update pension avgearn command handler. */
	@Inject
	private UpdatePensionAvgearnCommandHandler updatePensionAvgearnCommandHandler;

	/** The recalculate pension avgearn command handler. */
	@Inject
	private RecalculatePensionAvgearnCommandHandler recalculatePensionAvgearnCommandHandler;

	/**
	 * Find.
	 *
	 * @param id
	 *            the id
	 * @return the list
	 */
	@POST
	@Path("find/{id}")
	public ListPensionAvgearnModel find(@PathParam("id") String id) {
		return pensionAvgearnFinder.find(id);
	}

	/**
	 * Update.
	 *
	 * @param command
	 *            the command
	 */
	@POST
	@Path("update")
	public void update(UpdatePensionAvgearnCommand command) {
		updatePensionAvgearnCommandHandler.handle(command);
	}

	/**
	 * Re calculate.
	 *
	 * @param command
	 *            the command
	 */
	@POST
	@Path("recalculate")
	public ListPensionAvgearnModel reCalculate(RecalculateHealthInsuAvgearnCommand command) {
		return recalculatePensionAvgearnCommandHandler.handle(command);
	}
}
