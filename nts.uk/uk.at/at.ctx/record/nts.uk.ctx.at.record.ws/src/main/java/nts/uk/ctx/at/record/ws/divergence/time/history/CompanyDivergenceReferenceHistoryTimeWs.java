package nts.uk.ctx.at.record.ws.divergence.time.history;

import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import nts.arc.layer.ws.WebService;
import nts.uk.ctx.at.record.app.command.divergence.time.history.CompanyDivergenceReferenceTimeHistoryCommand;
import nts.uk.ctx.at.record.app.command.divergence.time.history.CompanyDivergenceReferenceTimeHistoryCommandHandler;
import nts.uk.ctx.at.record.app.find.divergence.time.history.CompanyDivergenceReferenceTimeHistoryDto;
import nts.uk.ctx.at.record.app.find.divergence.time.history.CompanyDivergenceReferenceTimeHistoryFinder;

/**
 * The Class CompanyDivergenceReferenceHistoryTimeWs.
 */
@Path("at/record/divergence/time/history/companyDivergenceRefTime")
@Produces("application/json")
public class CompanyDivergenceReferenceHistoryTimeWs extends WebService{
	
	/** The history finder. */
	@Inject
	private CompanyDivergenceReferenceTimeHistoryFinder historyFinder;
	
	/** The history save handler. */
	@Inject
	private CompanyDivergenceReferenceTimeHistoryCommandHandler historySaveHandler;
	
	/**
	 * Gets the all company divergence reference time history.
	 *
	 * @return the all company divergence reference time history
	 */
	@POST
	@Path("findAll")
	public List<CompanyDivergenceReferenceTimeHistoryDto> getAllCompanyDivergenceReferenceTimeHistory() {
	    return this.historyFinder.getAllHistories();
	}
	
	/**
	 * Gets the company divergence reference time history by hist id.
	 *
	 * @param historyId the history id
	 * @return the company divergence reference time history by hist id
	 */
	@POST
	@Path("find/{historyId}")
	public CompanyDivergenceReferenceTimeHistoryDto getCompanyDivergenceReferenceTimeHistoryByHistId(@PathParam("historyId") String historyId){
		return this.historyFinder.getHistory(historyId);
	}
	
	/**
	 * Save.
	 *
	 * @param command the command
	 */
	@POST
	@Path("save")
	public void save(CompanyDivergenceReferenceTimeHistoryCommand command){
		this.historySaveHandler.handle(command);
	}
}
