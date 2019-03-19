package nts.uk.ctx.workflow.ws.approvermanagement.workroot;

import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import nts.arc.enums.EnumAdaptor;
import nts.arc.enums.EnumConstant;
import nts.arc.layer.ws.WebService;
import nts.arc.time.GeneralDate;
import nts.uk.ctx.workflow.app.command.approvermanagement.workroot.DeleteHistoryCmm053CmdHandler;
import nts.uk.ctx.workflow.app.command.approvermanagement.workroot.HistoryCmm053Command;
import nts.uk.ctx.workflow.app.command.approvermanagement.workroot.InsertHistoryCmm053CmdHandler;
import nts.uk.ctx.workflow.app.command.approvermanagement.workroot.RegisterAppApprovalRootCommand;
import nts.uk.ctx.workflow.app.command.approvermanagement.workroot.RegisterAppApprovalRootCommandHandler;
import nts.uk.ctx.workflow.app.command.approvermanagement.workroot.UpdateHistoryCmm053CmdHandler;
import nts.uk.ctx.workflow.app.command.approvermanagement.workroot.UpdateWorkAppApprovalRByHistCommand;
import nts.uk.ctx.workflow.app.command.approvermanagement.workroot.UpdateWorkAppApprovalRByHistCommandHandler;
import nts.uk.ctx.workflow.app.find.approvermanagement.workroot.Cmm053EmployeeSearchParam;
import nts.uk.ctx.workflow.app.find.approvermanagement.workroot.CommonApprovalRootDto;
import nts.uk.ctx.workflow.app.find.approvermanagement.workroot.CommonApprovalRootFinder;
import nts.uk.ctx.workflow.app.find.approvermanagement.workroot.DataFullDto;
import nts.uk.ctx.workflow.app.find.approvermanagement.workroot.EmployeeRegisterApprovalRootDto;
import nts.uk.ctx.workflow.app.find.approvermanagement.workroot.EmployeeSearch;
import nts.uk.ctx.workflow.app.find.approvermanagement.workroot.EmployeeUnregisterFinder;
import nts.uk.ctx.workflow.app.find.approvermanagement.workroot.MasterApproverRootDto;
import nts.uk.ctx.workflow.app.find.approvermanagement.workroot.OutputCheckRegCmm053;
import nts.uk.ctx.workflow.app.find.approvermanagement.workroot.ParamCheckRegCmm053;
import nts.uk.ctx.workflow.app.find.approvermanagement.workroot.ParamDto;
import nts.uk.ctx.workflow.app.find.approvermanagement.workroot.PastHistoryDto;
import nts.uk.ctx.workflow.dom.adapter.bs.EmployeeAdapter;
import nts.uk.ctx.workflow.dom.adapter.bs.PersonAdapter;
import nts.uk.ctx.workflow.dom.adapter.bs.SyJobTitleAdapter;
import nts.uk.ctx.workflow.dom.adapter.bs.dto.EmployeeImport;
import nts.uk.ctx.workflow.dom.adapter.bs.dto.JobTitleImport;
import nts.uk.ctx.workflow.dom.adapter.bs.dto.PersonImport;
import nts.uk.ctx.workflow.dom.adapter.employee.EmployeeWithRangeLoginImport;
import nts.uk.ctx.workflow.dom.adapter.workplace.WorkplaceImport;
import nts.uk.ctx.workflow.dom.approvermanagement.workroot.ApplicationType;
import nts.uk.ctx.workflow.dom.approvermanagement.workroot.ConfirmationRootType;
import nts.uk.ctx.workflow.dom.approvermanagement.workroot.service.output.EmployeeUnregisterOutput;
import nts.uk.ctx.workflow.dom.approvermanagement.workroot.service.output.MasterApproverRootOutput;
import nts.uk.ctx.workflow.dom.approvermanagement.workroot.service.registerapproval.EmployeeRegisterApprovalRoot;
import nts.uk.shr.com.context.AppContexts;
@Path("workflow/approvermanagement/workroot")
@Produces("application/json")
public class WorkAppApprovalRootWebService extends WebService{

	@Inject
	private CommonApprovalRootFinder comFinder;
	@Inject
	private UpdateWorkAppApprovalRByHistCommandHandler updateHist;
	@Inject
	private EmployeeUnregisterFinder empUnregister;
	@Inject
	private RegisterAppApprovalRootCommandHandler updateRoot;
	@Inject
	private PersonAdapter psInfo;
	@Inject
	private EmployeeAdapter employeeAdapter;
	@Inject
	private SyJobTitleAdapter jobTitle;
	@Inject
	private EmployeeRegisterApprovalRoot registerApprovalRoot;
	@Inject
	private InsertHistoryCmm053CmdHandler insertByManager;
	@Inject
	private UpdateHistoryCmm053CmdHandler updateByManager;
	@Inject
	private DeleteHistoryCmm053CmdHandler deleteByManager;
	
	@POST
	@Path("getbycom")
	public DataFullDto getAllByCom(ParamDto param) {
		return this.comFinder.getAllCommonApprovalRoot(param);
	}
	
	@POST
	@Path("getbyprivate")
	public CommonApprovalRootDto getAllByPrivate(ParamDto param) {
		return this.comFinder.getPrivate(param);
	}
	@POST
	@Path("getEmployeesInfo")
	public List<EmployeeImport> findByWpkIds(EmployeeSearch employeeSearch){
		return employeeAdapter.findByWpkIdsWithParallel(AppContexts.user().companyId(), employeeSearch.getWorkplaceIds(), employeeSearch.getBaseDate());		
	}
	 @POST
	 @Path("updateHistory")
	 public void updateHistory(UpdateWorkAppApprovalRByHistCommand command){
		 this.updateHist.handle(command);
	 }
		
	/**
	 * Enum 申請種類.
	 *
	 * @return the list
	 */
	@POST
	@Path("find/applicationType")
	public List<EnumConstant> findApplicationType() {
		return EnumAdaptor.convertToValueNameList(ApplicationType.class);
	}
	
	@POST
	@Path("testInUnregistry")
	public List<EmployeeUnregisterOutput> lstEmployeeUnregister(GeneralDate baseDate){
		//GeneralDate date = GeneralDate.fromString(baseDate, "yyyy-mm-dd");
		List<EmployeeUnregisterOutput> data =empUnregister.lstEmployeeUnregister(baseDate); 
		return data;
	}
	
	@POST
	@Path("testMasterDat")
	public MasterApproverRootOutput masterInfor(MasterApproverRootDto dto) {
		MasterApproverRootOutput data = empUnregister.masterInfors(dto);
		return data;
	}
	@POST
	@Path("updateRoot")
	public void updateRoot(RegisterAppApprovalRootCommand command){
		updateRoot.handle(command);
	}
	@POST
	@Path("getInforPerson")
	public PersonImport getPsInfor(String SID) {
		return psInfo.getPersonInfo(SID);
	}
	@POST
	@Path("getInforPsLogin")
	public PersonImport getPsInforLogin() {
		String sId = AppContexts.user().employeeId();
		return psInfo.getPersonInfo(sId);
	}
	@POST
	@Path("getInforJobTitle")
	public List<JobTitleImport> findAllJobTitle(GeneralDate baseDate){
		String companyId = AppContexts.user().companyId();
		return jobTitle.findAll(companyId, baseDate);
	}
	/**
	 * 
	 * @param baseDate
	 * @param lstEmpIds
	 * @param rootAtr:　申請共通を選んだとrootAtr：０、以外：１
	 * @param lstApps
	 * @return
	 */
	@POST
	@Path("getEmployeeRegisterApprovalRoot")
	public void lstEmps(EmployeeRegisterApprovalRootDto data){
		String companyId = AppContexts.user().companyId();
		registerApprovalRoot.lstEmps(companyId, data.getBaseDate(), data.getLstEmpIds(), data.getLstApps());
	}
	@POST
	@Path("getJobtitleName")
	public JobTitleImport findJobTitleByPositionId(JobTitleImport jobTitleInfo) {
		String companyId = AppContexts.user().companyId();
		return jobTitle.findJobTitleByPositionId(companyId, jobTitleInfo.getPositionId(), jobTitleInfo.getStartDate());
	}
	/**
	 * Enum 確認ルート種類.
	 *
	 * @return the list
	 */
	@POST
	@Path("find/confirmRootType")
	public List<EnumConstant> findConfirmRootType() {
		return EnumAdaptor.convertToValueNameList(ConfirmationRootType.class);
	}
	@POST
	@Path("find/wpInfo")
	public WorkplaceImport getWpInfo(String workplaceId){
		return comFinder.getWpInfo(workplaceId);
	}

	@POST
	@Path("find/getEmployeeByCode")
	public EmployeeWithRangeLoginImport getEmployeeByCode(Cmm053EmployeeSearchParam param){
		return comFinder.getEmployeeInfoByCode(param);
	}

	@POST
	@Path("find/settingOfManager/getPastHistory/{employeeId}")
	public List<PastHistoryDto> getPastHistory(@PathParam("employeeId") String employeeId) {
		return comFinder.getPastHistory(employeeId);
	}

	@POST
	@Path("managersetting/insert")
	public void insertHistoryByManagerSetting(HistoryCmm053Command command) {
		this.insertByManager.handle(command);
	}
	
	@POST
	@Path("managersetting/update")
	public void updateHistoryByManagerSetting(HistoryCmm053Command command) {
		this.updateByManager.handle(command);
	}
	
	@POST
	@Path("managersetting/delete")
	public void deleteHistoryByManagerSetting(HistoryCmm053Command command) {
		this.deleteByManager.handle(command);
	}
	@POST
	@Path("find-wpInfo-login")
	public WorkplaceImport getWpInfoLogin(){
		return comFinder.getWpInfoLogin();
	}
	
	@POST
	@Path("checkBfRegCMM053")
	public OutputCheckRegCmm053 checkBfResCmm053(ParamCheckRegCmm053 param){
		return comFinder.checkReg(param);
	}
}