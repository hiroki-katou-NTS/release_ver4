package nts.uk.ctx.at.record.ws.realitystatus;

import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import nts.arc.layer.ws.WebService;
import nts.uk.ctx.at.record.app.find.realitystatus.RealityStatusActivityParam;
import nts.uk.ctx.at.record.app.find.realitystatus.RealityStatusActivityDto;
import nts.uk.ctx.at.record.app.find.realitystatus.RealityStatusFinder;
import nts.uk.ctx.at.record.app.find.realitystatus.ExeSendUnconfirmMailParam;
import nts.uk.ctx.at.record.app.find.realitystatus.WkpIdMailCheckParam;

@Path("at/record/realitystatus")
@Produces("application/json")
public class RealityStatusWebService extends WebService {
	@Inject
	RealityStatusFinder realityStatusFinder;

	@POST
	@Path("getStatusActivity")
	public List<RealityStatusActivityDto> getStatusActivity(RealityStatusActivityParam wkpInfoDto) {
		return realityStatusFinder.getStatusActivity(wkpInfoDto);
	}

	@POST
	@Path("checkSendUnconfirmedMail")
	public void checkSendUnconfirmedMail(List<WkpIdMailCheckParam> listWkp) {
		realityStatusFinder.checkSendUnconfirmedMail(listWkp);
	}
	
	@POST
	@Path("exeSendUnconfirmedMail")
	public void exeSendUnconfirmedMail(ExeSendUnconfirmMailParam obj) {
		realityStatusFinder.exeSendUnconfirmMail(obj);
	}
}
