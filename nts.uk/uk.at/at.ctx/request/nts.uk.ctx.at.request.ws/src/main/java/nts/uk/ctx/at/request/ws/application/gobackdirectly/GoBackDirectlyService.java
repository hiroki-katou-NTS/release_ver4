package nts.uk.ctx.at.request.ws.application.gobackdirectly;

import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import nts.arc.layer.ws.WebService;
import nts.uk.ctx.at.request.app.command.application.gobackdirectly.CheckInsertGoBackCommandHandler;
import nts.uk.ctx.at.request.app.command.application.gobackdirectly.CheckUpdateGoBackCommandHandler;
import nts.uk.ctx.at.request.app.command.application.gobackdirectly.InsertApplicationGoBackDirectlyCommand;
import nts.uk.ctx.at.request.app.command.application.gobackdirectly.InsertGoBackDirectlyCommandHandler;
import nts.uk.ctx.at.request.app.command.application.gobackdirectly.UpdateApplicationGoBackDirectlyCommand;
import nts.uk.ctx.at.request.app.command.application.gobackdirectly.UpdateGoBackDirectlyCommandHandler;
import nts.uk.ctx.at.request.app.find.application.gobackdirectly.GoBackDirectDetailDto;
import nts.uk.ctx.at.request.app.find.application.gobackdirectly.GoBackDirectSettingDto;
import nts.uk.ctx.at.request.app.find.application.gobackdirectly.GoBackDirectlyDto;
import nts.uk.ctx.at.request.app.find.application.gobackdirectly.GoBackDirectlyFinder;
import nts.uk.ctx.at.request.app.find.application.gobackdirectly.ParamGetAppGoBack;
import nts.uk.ctx.at.request.dom.application.common.service.other.output.ProcessResult;
import nts.uk.ctx.at.request.dom.application.gobackdirectly.service.GoBackDirectlyRegisterService;
import nts.uk.shr.com.context.AppContexts;

@Path("at/request/application/gobackdirectly")
@Produces("application/json")
public class GoBackDirectlyService extends WebService {
	@Inject
	private GoBackDirectlyFinder goBackDirectlyFinder;
	
	@Inject 
	private InsertGoBackDirectlyCommandHandler insertGoBackHandler;
	
	@Inject 
	private CheckInsertGoBackCommandHandler checkInsertGoBackHandler;
	
	@Inject
	private CheckUpdateGoBackCommandHandler checkUpdateGoBackHandler;

	@Inject 
	private UpdateGoBackDirectlyCommandHandler updateGoBackHandler;
	
	@Inject
	private GoBackDirectlyRegisterService goBackDirectlyRegisterService;

	/**
	 * 
	 * @return
	 */
	@POST
	@Path("getGoBackDirectlyByAppID")
	public GoBackDirectlyDto getGoBackDirectlyByAppID(String appID) {
		return this.goBackDirectlyFinder.getGoBackDirectlyByAppID(appID);
	}
	
	/**
	 * 
	 * @return
	 */
	@POST
	@Path("getGoBackCommonSetting")
	public GoBackDirectSettingDto getGoBackCommonSetting(ParamGetAppGoBack param) {
		return this.goBackDirectlyFinder.getGoBackDirectCommonSetting(param.getEmployeeIDs(), param.getAppDate());
	}

	/**
	 * 
	 * @return
	 */
	@POST
	@Path("getGoBackDirectDetail/{appID}")
	public GoBackDirectDetailDto getGoBackDetailData(@PathParam("appID") String appID) {
		return this.goBackDirectlyFinder.getGoBackDirectDetailByAppId(appID);
	}
	/**
	 * insert
	 * @param command
	 */
	@POST
	@Path("insertGoBackDirectly")
	public ProcessResult insertGoBackData (InsertApplicationGoBackDirectlyCommand command) {
		return insertGoBackHandler.handle(command);
	}
	
	/**
	 * check before insert OR update
	 * @param command
	 */
	@POST
	@Path("checkBeforeChangeGoBackDirectly")
	public void checkBeforeInsertGoBackData (InsertApplicationGoBackDirectlyCommand command) {
		this.checkInsertGoBackHandler.handle(command);
	}
	
	@POST
	@Path("checkBeforeUpdateGoBackData")
	public void checkBeforeUpdateGoBackData (InsertApplicationGoBackDirectlyCommand command) {
		this.checkUpdateGoBackHandler.handle(command);
	}
	
	
	/**
	 * update command
	 * @param command
	 */
	@POST
	@Path("updateGoBackDirectly")
	public ProcessResult updateGoBackData (UpdateApplicationGoBackDirectlyCommand command) {
		return this.updateGoBackHandler.handle(command);
	}
	
	@POST
	@Path("confirmInconsistency")
	public List<String> confirmInconsistency(InsertApplicationGoBackDirectlyCommand command) {
		String companyID = AppContexts.user().companyId();
		return goBackDirectlyRegisterService.inconsistencyCheck(
				companyID, 
				command.getAppCommand().getApplicantSID(), 
				command.getAppCommand().getApplicationDate());
	}
		
}

