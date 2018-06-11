package nts.uk.ctx.at.request.ws.application.common;

import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import lombok.Getter;
import nts.arc.layer.ws.WebService;
import nts.uk.ctx.at.request.app.command.application.common.RemandApplicationHandler;
import nts.uk.ctx.at.request.app.command.application.common.RemandCommand;
import nts.uk.ctx.at.request.app.command.application.common.UpdateApplicationApproveHandler;
import nts.uk.ctx.at.request.app.command.application.common.UpdateApplicationCancelHandler;
import nts.uk.ctx.at.request.app.command.application.common.UpdateApplicationCommonCmd;
import nts.uk.ctx.at.request.app.command.application.common.UpdateApplicationDelete;
import nts.uk.ctx.at.request.app.command.application.common.UpdateApplicationDenyHandler;
import nts.uk.ctx.at.request.app.command.application.common.UpdateApplicationReleaseHandler;
import nts.uk.ctx.at.request.app.command.setting.request.ApplicationDeadlineCommand;
import nts.uk.ctx.at.request.app.command.setting.request.UpdateApplicationDeadlineCommandHandler;
import nts.uk.ctx.at.request.app.find.application.common.AppDataDateFinder;
import nts.uk.ctx.at.request.app.find.application.common.AppDateDataDto;
import nts.uk.ctx.at.request.app.find.application.common.ApplicationFinder;
import nts.uk.ctx.at.request.app.find.application.common.ApprovalRootOfSubjectRequestDto;
import nts.uk.ctx.at.request.app.find.application.common.GetDataApprovalRootOfSubjectRequest;
import nts.uk.ctx.at.request.app.find.application.common.GetDataCheckDetail;
import nts.uk.ctx.at.request.app.find.application.common.ObjApprovalRootInput;
import nts.uk.ctx.at.request.app.find.application.common.OutputDetailCheckDto;
import nts.uk.ctx.at.request.app.find.application.common.dto.ApplicationMetaDto;
import nts.uk.ctx.at.request.app.find.application.common.dto.ApplicationPeriodDto;
import nts.uk.ctx.at.request.app.find.application.common.dto.ApplicationRemandDto;
import nts.uk.ctx.at.request.app.find.application.common.dto.ApplicationSendDto;
import nts.uk.ctx.at.request.app.find.application.common.dto.InputCommonData;
import nts.uk.ctx.at.request.app.find.application.requestofearch.GetDataAppCfDetailFinder;
import nts.uk.ctx.at.request.app.find.application.requestofearch.OutputMessageDeadline;
import nts.uk.ctx.at.request.app.find.setting.request.application.ApplicationDeadlineDto;
import nts.uk.ctx.at.request.dom.application.common.service.detailscreen.InputGetDetailCheck;
import nts.uk.ctx.at.request.dom.application.common.service.detailscreen.output.MailSenderResult;
import nts.uk.ctx.at.request.dom.application.common.service.other.output.ProcessResult;

@Path("at/request/application")
@Produces("application/json")
public class ApplicationWebservice extends WebService {
	@Inject 
	private ApplicationFinder finderApp;
	
	@Inject 
	private GetDataApprovalRootOfSubjectRequest getDataApprovalRoot;
	
	@Inject 
	private GetDataAppCfDetailFinder getDataAppCfDetailFinder;
	
	@Inject
	private GetDataCheckDetail getDataCheckDetail; 
	
	@Inject
	private UpdateApplicationApproveHandler approveApp;
	
	@Inject
	private UpdateApplicationDenyHandler denyApp;
	
	@Inject
	private RemandApplicationHandler remandApplicationHandler;
	
	@Inject
	private UpdateApplicationReleaseHandler releaseApp;
	
	@Inject
	private UpdateApplicationCancelHandler cancelApp;
	

	@Inject
	private UpdateApplicationDelete deleteApp;
	
	@Inject
	private AppDataDateFinder appDataDateFinder;
	@Inject
	private UpdateApplicationDeadlineCommandHandler update;

	
	
	/**
	 * approve application
	 * @return
	 */
	@POST
	@Path("approveapp")
	public ProcessResult approveApp(InputCommonData command){
		 return this.approveApp.handle(command);
	}
	
	/**
	 * deny application
	 * @return
	 */
	@POST
	@Path("denyapp")
	public ProcessResult denyApp(InputCommonData command){
		return this.denyApp.handle(command);
	}
	
	/**
	 * remand application
	 * @return
	 */
	@POST
	@Path("remandapp")
	public MailSenderResult remandApp(RemandCommand command){
		return remandApplicationHandler.handle(command);
	}
	
	/**
	 * release application
	 * @return
	 */
	@POST
	@Path("releaseapp")
	public ProcessResult releaseApp(InputCommonData command){
		return this.releaseApp.handle(command);
	}
	
	/**
	 * cancel application
	 * @return
	 */
	@POST
	@Path("cancelapp")
	public void cancelApp(UpdateApplicationCommonCmd command){
		 this.cancelApp.handle(command);
	}
	
	/**
	 * delete application
	 * @return
	 */
	@POST
	@Path("deleteapp")
	public ProcessResult deleteApp(UpdateApplicationCommonCmd command){
		 return this.deleteApp.handle(command);
	}
	
	/**
	 * lấy message và deadline trên màn hình
	 * get message and deadline (getDataConfigDetail)
	 * @return
	 */
	@POST
	@Path("getmessagedeadline")
	public OutputMessageDeadline getDataConfigDetail(ApplicationMetaDto application) {
		return this.getDataAppCfDetailFinder.getDataConfigDetail(application);
	}
	//new InputMessageDeadline("000000000000-0005",null,1,null)
	
	/**
	 * get data  ApprovalRootOfSubjectRequest
	 * @return
	 */
	@POST
	@Path("getdataapprovalroot")
	public List<ApprovalRootOfSubjectRequestDto> getDataApprovalRoot(ObjApprovalRootInput objApprovalRootInput) {
		return this.getDataApprovalRoot.getApprovalRootOfSubjectRequest(objApprovalRootInput);
	}
	
	/**
	 * get getDetailedScreenPreBootMode (check)
	 * @return
	 */
	@POST
	@Path("getdetailcheck")
	public OutputDetailCheckDto getDetailCheck(InputGetDetailCheck inputGetDetailCheck){
		
		return this.getDataCheckDetail.getDataCheckDetail(inputGetDetailCheck);
	}
	
	@POST
	@Path("getApplicationInfo")
	public List<ApplicationMetaDto> getAppInfo(ApplicationPeriodDto periodDate){
		return this.finderApp.getAppbyDate(periodDate);
	}
	
	@POST
	@Path("getAppInfoByAppID")
	public ApplicationMetaDto getAppInfo(String appID){
		return this.finderApp.getAppByID(appID);
	}
	
	@POST
	@Path("getAppInfoByListAppID")
	public List<ApplicationMetaDto> getListAppInfo(List<String> listAppID){
		return this.finderApp.getListAppInfo(listAppID);
	}
	
	
	@POST
	@Path("getAppInfoForRemandByAppId")
	public ApplicationRemandDto getAppInfoByAppIdForRemand(String appID){
		return this.finderApp.getAppByIdForRemand(appID);
	}
	@POST
	@Path("getApplicationForSendByAppID")
	public ApplicationSendDto getApplicationForSendByAppID(String appID){
		return finderApp.getAppByIdForSend(appID);
	}
	
	@POST
	@Path("getAppDataByDate")
	public AppDateDataDto getAppDataByDate(AppDateParam param){
		return appDataDateFinder.getAppDataByDate(param.getAppTypeValue(), param.getAppDate(), param.getIsStartup(), param.getAppID());
	}
	
	/**
	 * @author yennth
	 * @param closureId
	 * @return
	 */
	@POST
    @Path("getalldatabyclosureId")
    public List<ApplicationDeadlineDto> getDeadlineByClosureId(ClosureParam closureId){
        return this.getDataAppCfDetailFinder.findByClosureId(closureId.getClosureId());
    }
	/**
	 * update application deadline
	 * @param command
	 * @author yennth
	 */
	@POST
	@Path("update")
	public void update(List<ApplicationDeadlineCommand> command){
		this.update.handle(command);
	}

}

@Getter
class AppDateParam {
	private Integer appTypeValue; 
	private String appDate;
	private Boolean isStartup;
	private String appID;
}


@Getter
class ClosureParam {
	private List<Integer> closureId;
}
