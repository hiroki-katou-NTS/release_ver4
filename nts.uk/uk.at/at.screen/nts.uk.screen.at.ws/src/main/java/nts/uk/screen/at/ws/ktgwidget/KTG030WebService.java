package nts.uk.screen.at.ws.ktgwidget;

import javax.inject.Inject;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import nts.arc.layer.ws.WebService;
import nts.uk.screen.at.app.ktgwidget.KTG030QueryProcessor;
import nts.uk.screen.at.app.ktgwidget.find.KTG001Dto;

@Path("screen/at/ktg030")
@Produces("application/json")
public class KTG030WebService extends WebService {

	@Inject
	private KTG030QueryProcessor queryProcessor;

	@POST
	@Path("checkDisplay/{ym}/{closureId}")
	public KTG001Dto checkDisplay(@PathParam("ym")Integer ym, @PathParam("closureId") int closureId) {
		return queryProcessor.checkDataMonPerConfirm(ym, closureId);
	}
}