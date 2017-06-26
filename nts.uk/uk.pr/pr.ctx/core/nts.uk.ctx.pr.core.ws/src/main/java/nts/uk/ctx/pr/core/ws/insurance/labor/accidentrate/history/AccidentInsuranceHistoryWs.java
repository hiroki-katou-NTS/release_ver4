/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.core.ws.insurance.labor.accidentrate.history;

import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import nts.arc.layer.ws.WebService;
import nts.uk.ctx.pr.core.app.insurance.labor.accidentrate.find.dto.AccidentInsuranceRateHistoryFindDto;
import nts.uk.ctx.pr.core.app.insurance.labor.accidentrate.history.command.AccidentInsuranceHistoryUpdateCommand;
import nts.uk.ctx.pr.core.app.insurance.labor.accidentrate.history.command.AccidentInsuranceHistoryUpdateCommandHandler;
import nts.uk.ctx.pr.core.app.insurance.labor.accidentrate.history.find.AccidentInsuranceHistoryFinder;

/**
 * The Class AccidentInsuranceHistoryWs.
 */
@Path("pr/insurance/labor/accidentrate/history")
@Produces("application/json")
public class AccidentInsuranceHistoryWs extends WebService {

	/** The find. */
	@Inject
	private AccidentInsuranceHistoryFinder find;

	/** The update. */
	@Inject
	private AccidentInsuranceHistoryUpdateCommandHandler update;

	/**
	 * Find all.
	 *
	 * @return the list
	 */
	@POST
	@Path("findall")
	public List<AccidentInsuranceRateHistoryFindDto> findAll() {
		return this.find.findAll();
	}

	/**
	 * Find.
	 *
	 * @param historyId
	 *            the history id
	 * @return the accident insurance rate history find out dto
	 */
	@POST
	@Path("find/{historyId}")
	public AccidentInsuranceRateHistoryFindDto find(@PathParam("historyId") String historyId) {
		return this.find.find(historyId);
	}

	/**
	 * Update.
	 *
	 * @param command
	 *            the command
	 */
	@POST
	@Path("update")
	public void update(AccidentInsuranceHistoryUpdateCommand command) {
		this.update.handle(command);
	}

}
