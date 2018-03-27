package nts.uk.ctx.at.record.ws.divergence.time.history;

import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import nts.arc.layer.ws.WebService;
import nts.uk.ctx.at.record.app.command.divergence.time.history.ComDivergenceRefTimeHistDeleteCommand;
import nts.uk.ctx.at.record.app.command.divergence.time.history.ComDivergenceRefTimeHistDeleteCommandHanlder;
import nts.uk.ctx.at.record.app.command.divergence.time.history.ComDivergenceRefTimeHistSaveCommand;
import nts.uk.ctx.at.record.app.command.divergence.time.history.ComDivergenceRefTimeHistSaveCommandHandler;
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
	private ComDivergenceRefTimeHistSaveCommandHandler historySaveHandler;
	
	/** The history delete handler. */
	@Inject
	private ComDivergenceRefTimeHistDeleteCommandHanlder historyDeleteHandler;
	
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
	public void save(ComDivergenceRefTimeHistSaveCommand command){
		this.historySaveHandler.handle(command);
	}
	
	/**
	 * Removes the.
	 *
	 * @param command the command
	 */
	@POST
	@Path("remove")
	public void remove(ComDivergenceRefTimeHistDeleteCommand command){
		this.historyDeleteHandler.handle(command);
	}
}
