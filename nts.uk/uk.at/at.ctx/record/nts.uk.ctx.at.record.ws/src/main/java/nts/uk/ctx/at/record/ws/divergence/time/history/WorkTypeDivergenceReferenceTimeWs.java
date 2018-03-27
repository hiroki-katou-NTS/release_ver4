package nts.uk.ctx.at.record.ws.divergence.time.history;

import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import nts.arc.layer.ws.WebService;
import nts.uk.ctx.at.record.app.command.divergence.time.history.WorkTypeDivergenceRefTimeSaveCommand;
import nts.uk.ctx.at.record.app.command.divergence.time.history.WorkTypeDivergenceRefTimeSaveCommandHandler;
import nts.uk.ctx.at.record.app.find.divergence.time.history.WorkTypeDivergenceReferenceTimeDto;
import nts.uk.ctx.at.record.app.find.divergence.time.history.WorkTypeDivergenceReferenceTimeFinder;

/**
 * The Class WorkTypeDivergenceReferenceTimeWs.
 */
@Path("at/record/divergence/time/workTypeDivergenceRefTime")
@Produces("application/json")
public class WorkTypeDivergenceReferenceTimeWs extends WebService {
	/** The finder. */
	@Inject
	private WorkTypeDivergenceReferenceTimeFinder finder;

	/** The save handler. */
	@Inject
	private WorkTypeDivergenceRefTimeSaveCommandHandler saveHandler;

	/**
	 * Gets the all company divergence reference time.
	 *
	 * @param workTypeCode
	 *            the work type code
	 * @param historyId
	 *            the history id
	 * @return the all company divergence reference time
	 */
	@POST
	@Path("find/{workTypeCode}/{historyId}")
	public List<WorkTypeDivergenceReferenceTimeDto> getAllCompanyDivergenceReferenceTime(
			@PathParam("workTypeCode") String workTypeCode, @PathParam("historyId") String historyId) {
		return this.finder.getDivergenceReferenceTimeItemByHist(historyId, workTypeCode);
	}

	/**
	 * Save.
	 *
	 * @param command
	 *            the command
	 */
	@POST
	@Path("save")
	public void save(WorkTypeDivergenceRefTimeSaveCommand command) {
		this.saveHandler.handle(command);
	}
}
