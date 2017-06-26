package nts.uk.ctx.pr.core.ws.paymentdata;

import javax.inject.Inject;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import nts.arc.layer.ws.WebService;
import nts.uk.ctx.pr.core.app.find.paymentdata.CommuteNotaxLimitDto;
import nts.uk.ctx.pr.core.app.find.personalinfo.commute.CommuteFinder;

@Path("pr/proto/paymentdata")
@Produces("application/json")
public class CommuteNotaxLimitWebService extends WebService {
	
	@Inject
	private CommuteFinder commuteNotaxLimitFinder;
	
	@POST
	@Path("findCommuteNotaxLimit/{personId}/{baseYearMonth}")
	public CommuteNotaxLimitDto findCommuteNotaxLimit(@PathParam("personId") String personId,@PathParam("baseYearMonth") int baseYearMonth ){
		return commuteNotaxLimitFinder.find(personId, baseYearMonth);
	}
}
