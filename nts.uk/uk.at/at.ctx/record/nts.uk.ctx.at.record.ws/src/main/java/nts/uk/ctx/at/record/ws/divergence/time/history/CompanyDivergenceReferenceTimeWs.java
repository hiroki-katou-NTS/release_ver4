package nts.uk.ctx.at.record.ws.divergence.time.history;

import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import nts.arc.layer.ws.WebService;
import nts.uk.ctx.at.record.app.command.divergence.time.history.ComDivergenceRefTimeSaveCommand;
import nts.uk.ctx.at.record.app.command.divergence.time.history.ComDivergenceRefTimeSaveCommandHandler;
import nts.uk.ctx.at.record.app.find.divergence.time.history.CompanyDivergenceReferenceTimeDto;
import nts.uk.ctx.at.record.app.find.divergence.time.history.CompanyDivergenceReferenceTimeFinder;

@Path("at/record/divergence/time/companyDivergenceRefTime")
@Produces("application/json")
public class CompanyDivergenceReferenceTimeWs extends WebService{
	@Inject
	private CompanyDivergenceReferenceTimeFinder finder;
	
	@Inject 
	private ComDivergenceRefTimeSaveCommandHandler saveHandler;
	
	@POST
	@Path("find/{historyId}")
	public List<CompanyDivergenceReferenceTimeDto> getAllCompanyDivergenceReferenceTime(@PathParam("historyId") String historyId) {
	    return this.finder.getDivergenceReferenceTimeItemByHist(historyId);
	}
	
	@POST
	@Path("save")
	public void save(ComDivergenceRefTimeSaveCommand command){
		this.saveHandler.handle(command);
	}
}
