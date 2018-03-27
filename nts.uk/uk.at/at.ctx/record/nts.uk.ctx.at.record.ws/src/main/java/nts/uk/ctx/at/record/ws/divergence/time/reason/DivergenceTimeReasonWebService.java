package nts.uk.ctx.at.record.ws.divergence.time.reason;

import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import nts.arc.layer.ws.WebService;
import nts.uk.ctx.at.record.app.command.divergence.time.reason.DivergenceReasonSelectAddCommand;
import nts.uk.ctx.at.record.app.command.divergence.time.reason.DivergenceReasonSelectAddCommandHandler;
import nts.uk.ctx.at.record.app.command.divergence.time.reason.DivergenceReasonSelectDeleteCommand;
import nts.uk.ctx.at.record.app.command.divergence.time.reason.DivergenceReasonSelectDeleteCommandHandler;
import nts.uk.ctx.at.record.app.command.divergence.time.reason.DivergenceReasonSelectSaveCommand;
import nts.uk.ctx.at.record.app.command.divergence.time.reason.DivergenceReasonSelectSaveCommandHandler;
import nts.uk.ctx.at.record.app.find.divergence.time.reason.DivergenceReasonSelectDto;
import nts.uk.ctx.at.record.app.find.divergence.time.reason.DivergenceReasonSelectFinder;

/**
 * The Class DivergenceTimeReasonWebService.
 */
@Path("at/record/divergencetime/reason")
@Produces("application/json")
public class DivergenceTimeReasonWebService extends WebService {

	/** The div reasonselect finder. */
	@Inject
	private DivergenceReasonSelectFinder divReasonselectFinder;

	/** The div reason select add command handler. */
	@Inject
	private DivergenceReasonSelectAddCommandHandler divReasonSelectAddCommandHandler;

	/** The div reason select save command handler. */
	@Inject
	private DivergenceReasonSelectSaveCommandHandler divReasonSelectSaveCommandHandler;

	/** The div reason select delete command handler. */
	@Inject
	private DivergenceReasonSelectDeleteCommandHandler divReasonSelectDeleteCommandHandler;

	/**
	 * get all divergence reason.
	 *
	 * @param divTimeNo
	 *            the div time no
	 * @return the all div reason
	 */
	@POST
	@Path("getalldivreason/{divTimeId}")
	public List<DivergenceReasonSelectDto> getAllDivReason(@PathParam("divTimeId") String divTimeNo) {
		return this.divReasonselectFinder.getAllReason(Integer.parseInt(divTimeNo));

	}

	/**
	 * add divergence reason.
	 *
	 * @param command
	 *            the command
	 */
	@POST
	@Path("adddivreason")
	public void addDivReason(DivergenceReasonSelectAddCommand command) {
		this.divReasonSelectAddCommandHandler.handle(command);
	}

	/**
	 * update divergence reason.
	 *
	 * @param command
	 *            the command
	 */
	@POST
	@Path("updatedivreason")
	public void updateDivReason(DivergenceReasonSelectSaveCommand command) {
		this.divReasonSelectSaveCommandHandler.handle(command);
	}

	/**
	 * delete divergence reason.
	 *
	 * @param command
	 *            the command
	 */
	@POST
	@Path("deletedivreason")
	public void deleteDivReason(DivergenceReasonSelectDeleteCommand command) {
		this.divReasonSelectDeleteCommandHandler.handle(command);
	}

}
