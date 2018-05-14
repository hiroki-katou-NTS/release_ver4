package nts.uk.screen.at.ws.ktgwidget;

import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import nts.arc.layer.ws.WebService;
import nts.arc.time.YearMonth;
import nts.uk.ctx.at.shared.app.query.workrule.closure.ClosureResultModel;
import nts.uk.screen.at.app.ktgwidget.KTG027QueryProcessor;
import nts.uk.screen.at.app.ktgwidget.find.dto.OvertimeHours;
import nts.uk.screen.at.app.ktgwidget.find.dto.OvertimeHoursDto;
@Path("screen/at/overtimehours")
@Produces("application/json")
public class OvertimeHoursWebService extends WebService{
	@Inject
	private KTG027QueryProcessor finder;
	@POST
	@Path("getlistclosure")
	public List<ClosureResultModel> getListClosure(){
		return finder.getListClosure();
	}
	
	@POST
	@Path("getovertimehours/{targetMonth}")
	public OvertimeHoursDto getOvertimeHours(@PathParam("targetMonth") int targetMonth){
		return finder.initialActivationArocess(targetMonth);
	}
	
	@POST
	@Path("getovertimehours/buttonPressingProcess")
	public OvertimeHours getOvertimeHours(int targetMonth, int closureID){
		return finder.buttonPressingProcess(targetMonth, closureID);
	}
}
