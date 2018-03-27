package nts.uk.ctx.at.record.ws.divergence.time.history;

import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import nts.uk.ctx.at.record.app.command.divergence.time.history.WorkTypeDivergenceRefTimeHistDeleteCommand;
import nts.uk.ctx.at.record.app.command.divergence.time.history.WorkTypeDivergenceRefTimeHistDeleteCommandHanlder;
import nts.uk.ctx.at.record.app.command.divergence.time.history.WorkTypeDivergenceRefTimeHistSaveCommand;
import nts.uk.ctx.at.record.app.command.divergence.time.history.WorkTypeDivergenceRefTimeHistSaveCommandHandler;
import nts.uk.ctx.at.record.app.find.divergence.time.history.WorkTypeDivergenceReferenceTimeHistoryDto;
import nts.uk.ctx.at.record.app.find.divergence.time.history.WorkTypeDivergenceReferenceTimeHistoryFinder;

/**
 * The Class WorkTypeDivergenceReferenceTimeHistoryWs.
 */
@Path("at/record/divergence/time/history/workTypeDivergenceRefTime")
@Produces("application/json")
public class WorkTypeDivergenceReferenceTimeHistoryWs {
	
	/** The history finder. */
	@Inject
	private WorkTypeDivergenceReferenceTimeHistoryFinder historyFinder;
	
	/** The history save handler. */
	@Inject
	private WorkTypeDivergenceRefTimeHistSaveCommandHandler historySaveHandler;
	
	/** The history delete handler. */
	@Inject
	private WorkTypeDivergenceRefTimeHistDeleteCommandHanlder historyDeleteHandler;
	
	/**
	 * Gets the all work type divergence reference time history.
	 *
	 * @param workTypeCode the work type code
	 * @return the all work type divergence reference time history
	 */
	@POST
	@Path("findAll/{workTypeCode}")
	public List<WorkTypeDivergenceReferenceTimeHistoryDto> getAllWorkTypeDivergenceReferenceTimeHistory(@PathParam("workTypeCode") String workTypeCode) {
		return this.historyFinder.getAllHistories(workTypeCode);
	}

	/**
	 * Gets the company divergence reference time history by hist id.
	 *
	 * @param historyId the history id
	 * @return the company divergence reference time history by hist id
	 */
	@POST
	@Path("find/{historyId}")
	public WorkTypeDivergenceReferenceTimeHistoryDto getWkTypeDivergenceReferenceTimeHistoryByHistId(
			@PathParam("historyId") String historyId) {
		return this.historyFinder.getHistory(historyId);
	}

	/**
	 * Save.
	 *
	 * @param command
	 *            the command
	 */
	@POST
	@Path("save")
	public void save(WorkTypeDivergenceRefTimeHistSaveCommand command) {
		this.historySaveHandler.handle(command);
	}

	/**
	 * Removes the.
	 *
	 * @param command
	 *            the command
	 */
	@POST
	@Path("remove")
	public void remove(WorkTypeDivergenceRefTimeHistDeleteCommand command) {
		this.historyDeleteHandler.handle(command);
	}
}
