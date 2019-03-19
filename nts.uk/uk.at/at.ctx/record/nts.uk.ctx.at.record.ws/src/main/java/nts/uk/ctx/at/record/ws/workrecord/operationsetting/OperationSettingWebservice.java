/**
 * 
 */
package nts.uk.ctx.at.record.ws.workrecord.operationsetting;

import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import nts.arc.enums.EnumConstant;
import nts.arc.layer.ws.WebService;
import nts.uk.ctx.at.record.app.command.workrecord.operationsetting.ApprovalProcessCommand;
import nts.uk.ctx.at.record.app.command.workrecord.operationsetting.DaiPerformanceFunCommand;
import nts.uk.ctx.at.record.app.command.workrecord.operationsetting.FormatPerformanceCommand;
import nts.uk.ctx.at.record.app.command.workrecord.operationsetting.IdentityProcessCommand;
import nts.uk.ctx.at.record.app.command.workrecord.operationsetting.MonPerformanceFunCommand;
import nts.uk.ctx.at.record.app.command.workrecord.operationsetting.UpdateApprovalProcessCommandHandler;
import nts.uk.ctx.at.record.app.command.workrecord.operationsetting.UpdateDaiPerformanceFunCommandHandler;
import nts.uk.ctx.at.record.app.command.workrecord.operationsetting.UpdateFormatPerformanceCommandHandler;
import nts.uk.ctx.at.record.app.command.workrecord.operationsetting.UpdateIdentityProcessCommandHandler;
import nts.uk.ctx.at.record.app.command.workrecord.operationsetting.UpdateMonPerformanceFunCommandHandler;
import nts.uk.ctx.at.record.app.find.workrecord.operationsetting.ApplicationTypeFinder;
import nts.uk.ctx.at.record.app.find.workrecord.operationsetting.ApprovalProcessDto;
import nts.uk.ctx.at.record.app.find.workrecord.operationsetting.ApprovalProcessFinder;
import nts.uk.ctx.at.record.app.find.workrecord.operationsetting.DaiPerformanceFunDto;
import nts.uk.ctx.at.record.app.find.workrecord.operationsetting.DaiPerformanceFunFinder;
import nts.uk.ctx.at.record.app.find.workrecord.operationsetting.FormatPerformanceDto;
import nts.uk.ctx.at.record.app.find.workrecord.operationsetting.FormatPerformanceFinder;
import nts.uk.ctx.at.record.app.find.workrecord.operationsetting.IdentityProcessDto;
import nts.uk.ctx.at.record.app.find.workrecord.operationsetting.IdentityProcessFinder;
import nts.uk.ctx.at.record.app.find.workrecord.operationsetting.MonPerformanceFunDto;
import nts.uk.ctx.at.record.app.find.workrecord.operationsetting.MonPerformanceFunFinder;
import nts.uk.shr.com.context.AppContexts;

/**
 * @author binhhx
 *
 */
@Path("at/record/workrecord/operationsetting/")
@Produces("application/json")
public class OperationSettingWebservice extends WebService {
	@Inject
	private FormatPerformanceFinder  formatPerformanceFinder;
	
	@Inject
	private MonPerformanceFunFinder  monPerformanceFunFinder;
	
	@Inject
	private DaiPerformanceFunFinder  daiPerformanceFunFinder;
	
	@Inject 
	private UpdateFormatPerformanceCommandHandler updateFormatPerformanceCommandHandler;
	
	@Inject 
	private UpdateMonPerformanceFunCommandHandler updateMonPerformanceFunCommandHandler;
	
	@Inject
	private UpdateDaiPerformanceFunCommandHandler updateDaiPerformanceFunCommandHandler;
	
	@Inject
	private ApprovalProcessFinder approvalProcessFinder;
	
	@Inject
	private IdentityProcessFinder identityProcessFinder;
	
	@Inject
	private UpdateApprovalProcessCommandHandler updateApprovalProcessCommandHandler;
	
	@Inject
	private UpdateIdentityProcessCommandHandler updateIdentityProcessCommandHandler;
	
	@Inject
	private ApplicationTypeFinder appTypeFinder; 
	
	@POST 
	@Path("getFormat")
	public FormatPerformanceDto getFormatPerformanceById() {
		String companyId = AppContexts.user().companyId();
		FormatPerformanceDto dto =  formatPerformanceFinder.getAllFormatPerformanceById(companyId);
		
		return dto;
	}
	
	@POST
	@Path("getdaily")
	public DaiPerformanceFunDto getDaiPerformanceFunById() {
		String companyId = AppContexts.user().companyId();
		DaiPerformanceFunDto dto =  daiPerformanceFunFinder.getDaiPerformanceFunById(companyId);
		return dto;
	}
	
	@POST
	@Path("getMonthy")
	public MonPerformanceFunDto getMonPerformanceFunById() {
		String companyId = AppContexts.user().companyId();
		MonPerformanceFunDto dto =  monPerformanceFunFinder.getAllMonPerformanceFunById(companyId);
		
		return dto;
	}
	
	@POST
	@Path("getIdentity")
	public IdentityProcessDto getIdentityProcessById() {
		String companyId = AppContexts.user().companyId();
		IdentityProcessDto dto =  identityProcessFinder.getAllIdentityProcessById(companyId);
		
		return dto;
	}
	
	@POST
	@Path("getApproval")
	public ApprovalProcessDto getApprovalProcessById() {
		String companyId = AppContexts.user().companyId();
		ApprovalProcessDto dto =  approvalProcessFinder.getApprovalProcessById(companyId);
		
		return dto;
	}
	
	@POST
	@Path("updateFormat")
	public void updateFormatPerformance(FormatPerformanceCommand command){
		updateFormatPerformanceCommandHandler.handle(command);
	}
	
	@POST
	@Path("updateMonthly")
	public void updateMonPerformanceFun(MonPerformanceFunCommand command){
		updateMonPerformanceFunCommandHandler.handle(command);
	}
	
	@POST
	@Path("updateDaily")
	public void updateDaiPerformanceFun(DaiPerformanceFunCommand command){
		updateDaiPerformanceFunCommandHandler.handle(command);
	}
	
	@POST
	@Path("updateApproval")
	public void updateApprovalProcess(ApprovalProcessCommand command){
		updateApprovalProcessCommandHandler.handle(command);
	}
	
	@POST
	@Path("updateIdentity")
	public void updateIdentityProcess(IdentityProcessCommand command){
		updateIdentityProcessCommandHandler.handle(command);
	}
	
	@POST
	@Path("findApplicationType")
	public List<EnumConstant> getApplicationType() {
		return appTypeFinder.getAppWithOvertimeInfo();
	}
}
