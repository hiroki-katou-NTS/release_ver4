package nts.uk.screen.at.ws.ktgwidget;

import javax.inject.Inject;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import nts.arc.layer.ws.WebService;
import nts.uk.ctx.at.shared.dom.adapter.dailyperformance.PresenceDataApprovedImport;
import nts.uk.screen.at.app.ktgwidget.KTG001QueryProcessor;

@Path("screen/at/ktg001")
@Produces("application/json")
public class KTG001WebService extends WebService {

	@Inject
	private KTG001QueryProcessor queryProcessor; 
	
	@POST
	@Path("checkDisplay")
	public PresenceDataApprovedImport checkDisplay(){
		return queryProcessor.confirmDailyActual();
	}
}
