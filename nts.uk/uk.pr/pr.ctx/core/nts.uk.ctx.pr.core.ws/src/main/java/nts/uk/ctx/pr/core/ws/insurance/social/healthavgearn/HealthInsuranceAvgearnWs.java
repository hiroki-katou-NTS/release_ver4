/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.core.ws.insurance.social.healthavgearn;

import javax.inject.Inject;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import nts.arc.layer.ws.WebService;
import nts.uk.ctx.pr.core.app.insurance.social.healthavgearn.command.RecalculateHealthInsuAvgearnCommand;
import nts.uk.ctx.pr.core.app.insurance.social.healthavgearn.command.RecalculateHealthInsuAvgearnCommandHandler;
import nts.uk.ctx.pr.core.app.insurance.social.healthavgearn.command.UpdateHealthInsuranceAvgearnCommand;
import nts.uk.ctx.pr.core.app.insurance.social.healthavgearn.command.UpdateHealthInsuranceAvgearnCommandHandler;
import nts.uk.ctx.pr.core.app.insurance.social.healthavgearn.find.HealthInsAvgearnsModel;
import nts.uk.ctx.pr.core.app.insurance.social.healthavgearn.find.HealthInsuranceAvgearnFinder;

/**
 * The Class HealthInsuranceAvgearnWs.
 */
@Path("ctx/pr/core/insurance/social/healthavgearn")
@Produces("application/json")
public class HealthInsuranceAvgearnWs extends WebService {

	/** The health insurance avgearn finder. */
	@Inject
	private HealthInsuranceAvgearnFinder healthInsuranceAvgearnFinder;

	/** The update health insurance avgearn command handler. */
	@Inject
	private UpdateHealthInsuranceAvgearnCommandHandler updateCommandHandler;

	/** The recalculate health insu avgearn command handler. */
	@Inject
	private RecalculateHealthInsuAvgearnCommandHandler recalculateCommandHandler;

	/**
	 * Update.
	 *
	 * @param command
	 *            the command
	 */
	@POST
	@Path("update")
	public void update(UpdateHealthInsuranceAvgearnCommand command) {
		updateCommandHandler.handle(command);
	}

	/**
	 * Find.
	 *
	 * @param id
	 *            the id
	 * @return the list
	 */
	@POST
	@Path("find/{id}")
	public HealthInsAvgearnsModel find(@PathParam("id") String id) {
		return healthInsuranceAvgearnFinder.find(id);
	}

	/**
	 * Re calculate.
	 *
	 * @param command
	 *            the command
	 */
	@POST
	@Path("recalculate")
	public HealthInsAvgearnsModel reCalculate(RecalculateHealthInsuAvgearnCommand command) {
		return recalculateCommandHandler.handle(command);
	}
}
