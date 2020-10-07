package nts.uk.screen.at.ws.ktgwidget;

import javax.inject.Inject;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import nts.uk.ctx.sys.portal.dom.toppagepart.standardwidget.StandardWidget;
import nts.uk.screen.at.app.ktgwidget.KTG001QueryProcessor_ver04;
import nts.uk.screen.at.app.ktgwidget.find.dto.ApprovedDataExecutionResultDto;

@Path("screen/at/ktg001/")
@Produces("application/json")
public class KTG001WebService_ver4 {

	@Inject
	private KTG001QueryProcessor_ver04 queryProcessor;

	@POST
	@Path("display")
	public ApprovedDataExecutionResultDto checkDisplay(KTG001Param param) {
		return this.queryProcessor.getApprovedDataExecutionResult(param.getYm(), param.getClosureId());
	}
	
	@POST
	@Path("setting")
	public void updateSetting(StandardWidget standardWidget) {
		this.queryProcessor.updateSetting(standardWidget);
	}
	
}
