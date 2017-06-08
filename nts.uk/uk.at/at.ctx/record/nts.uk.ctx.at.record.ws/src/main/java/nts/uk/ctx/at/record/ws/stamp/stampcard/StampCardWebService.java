package nts.uk.ctx.at.record.ws.stamp.stampcard;

import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import nts.arc.layer.ws.WebService;
import nts.uk.ctx.at.record.app.find.stamp.stampcard.StampCardDto;
import nts.uk.ctx.at.record.app.find.stamp.stampcard.StampCardFinder;

@Path("at/record/stamp/stampcard")
@Produces("application/json")
public class StampCardWebService extends WebService {
	@Inject
	private StampCardFinder getStampCard;

	@POST
	@Path("getstampnumberbyempcode/{personId}")

	public List<StampCardDto> getStampNumber(@PathParam("personId") String personId) {
		return this.getStampCard.findByPersonID(personId);
	}
}
