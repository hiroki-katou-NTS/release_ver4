/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.core.ws.insurance.labor.unemployeerate.history;

import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import nts.arc.layer.ws.WebService;
import nts.uk.ctx.pr.core.app.insurance.labor.unemployeerate.find.UnemployeeInsuranceHistoryFinder;
import nts.uk.ctx.pr.core.app.insurance.labor.unemployeerate.find.dto.UnemployeeInsuranceHistoryFindDto;
import nts.uk.ctx.pr.core.app.insurance.labor.unemployeerate.history.command.UnemployeeInsuranceHistoryUpdateCommand;
import nts.uk.ctx.pr.core.app.insurance.labor.unemployeerate.history.command.UnemployeeInsuranceHistoryUpdateCommandHandler;

/**
 * The Class UnemployeeInsuranceHistoryWs.
 */
@Path("pr/insurance/labor/unemployeerate/history")
@Produces("application/json")
public class UnemployeeInsuranceHistoryWs extends WebService {

	/** The find. */
	@Inject
	private UnemployeeInsuranceHistoryFinder find;

	/** The update. */
	@Inject
	private UnemployeeInsuranceHistoryUpdateCommandHandler update;

	/**
	 * Find all.
	 *
	 * @return the list
	 */
	@POST
	@Path("findall")
	public List<UnemployeeInsuranceHistoryFindDto> findAll() {
		return this.find.findAll();
	}

	/**
	 * Find history.
	 *
	 * @param historyId
	 *            the history id
	 * @return the unemployee insurance history find out dto
	 */
	@POST
	@Path("find/{historyId}")
	public UnemployeeInsuranceHistoryFindDto findHistory(
			@PathParam("historyId") String historyId) {
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
	public void update(UnemployeeInsuranceHistoryUpdateCommand command) {
		this.update.handle(command);
	}
}
