package nts.uk.ctx.at.record.ws.divergence.time.history;

import javax.inject.Inject;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import nts.arc.layer.ws.WebService;
import nts.uk.ctx.at.record.app.command.divergence.time.history.DivergenceReferenceTimeUsageUnitCommand;
import nts.uk.ctx.at.record.app.command.divergence.time.history.DivergenceReferenceTimeUsageUnitSaveCommandHandler;
import nts.uk.ctx.at.record.app.find.divergence.time.history.DivergenceReferenceTimeUsageUnitDto;
import nts.uk.ctx.at.record.app.find.divergence.time.history.DivergenceReferenceTimeUsageUnitFinder;

/**
 * The Class DivergenceReferenceTimeUsageUnitWs.
 */
@Path("at/record/divergence/time/history/divergenceRefTimeUsageUnit")
@Produces("application/json")
public class DivergenceReferenceTimeUsageUnitWs extends WebService {

	/** The save handler. */
	@Inject
	private DivergenceReferenceTimeUsageUnitSaveCommandHandler saveHandler;

	/** The finder. */
	@Inject
	private DivergenceReferenceTimeUsageUnitFinder finder;

	/**
	 * Save.
	 *
	 * @param command
	 *            the command
	 */
	@POST
	@Path("save")
	public void save(DivergenceReferenceTimeUsageUnitCommand command) {
		this.saveHandler.handle(command);
	}

	/**
	 * Find by company id.
	 *
	 * @return the divergence reference time usage unit dto
	 */
	@POST
	@Path("find")
	public DivergenceReferenceTimeUsageUnitDto findByCompanyId() {
		return this.finder.findByCompanyId();
	}
}
