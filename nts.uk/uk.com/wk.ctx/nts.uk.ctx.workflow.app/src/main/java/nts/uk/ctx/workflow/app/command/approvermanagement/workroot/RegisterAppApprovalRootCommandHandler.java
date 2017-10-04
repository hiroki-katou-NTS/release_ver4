package nts.uk.ctx.workflow.app.command.approvermanagement.workroot;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.arc.layer.app.command.CommandHandler;
import nts.arc.layer.app.command.CommandHandlerContext;
import nts.arc.time.GeneralDate;
import nts.uk.ctx.workflow.app.find.approvermanagement.workroot.ApprovalPhaseDto;
import nts.uk.ctx.workflow.app.find.approvermanagement.workroot.ApproverDto;
import nts.uk.ctx.workflow.dom.approvermanagement.workroot.ApprovalBranch;
import nts.uk.ctx.workflow.dom.approvermanagement.workroot.ApprovalBranchRepository;
import nts.uk.ctx.workflow.dom.approvermanagement.workroot.ApprovalPhase;
import nts.uk.ctx.workflow.dom.approvermanagement.workroot.ApprovalPhaseRepository;
import nts.uk.ctx.workflow.dom.approvermanagement.workroot.Approver;
import nts.uk.ctx.workflow.dom.approvermanagement.workroot.ApproverRepository;
import nts.uk.ctx.workflow.dom.approvermanagement.workroot.CompanyApprovalRoot;
import nts.uk.ctx.workflow.dom.approvermanagement.workroot.CompanyApprovalRootRepository;
import nts.uk.ctx.workflow.dom.approvermanagement.workroot.PersonApprovalRoot;
import nts.uk.ctx.workflow.dom.approvermanagement.workroot.PersonApprovalRootRepository;
import nts.uk.ctx.workflow.dom.approvermanagement.workroot.WorkplaceApprovalRoot;
import nts.uk.ctx.workflow.dom.approvermanagement.workroot.WorkplaceApprovalRootRepository;
import nts.uk.shr.com.context.AppContexts;

@Stateless
public class RegisterAppApprovalRootCommandHandler  extends CommandHandler<RegisterAppApprovalRootCommand>{

	@Inject
	private PersonApprovalRootRepository repoPerson;
	@Inject
	private CompanyApprovalRootRepository repoCom;
	@Inject
	private WorkplaceApprovalRootRepository repoWorkplace;
	@Inject
	private ApprovalPhaseRepository repoAppPhase;
	@Inject
	private ApproverRepository repoApprover;
	@Inject
	private ApprovalBranchRepository repoBranch;
	@Override
	protected void handle(CommandHandlerContext<RegisterAppApprovalRootCommand> context) {
		//____________New__________
		String historyId = UUID.randomUUID().toString();
		RegisterAppApprovalRootCommand data = context.getCommand();
		int rootType = data.getRootType();
		//TH: company
		if(rootType == 0){
			registerCom(data, historyId);
		}
		//TH: work place
		else if(rootType == 1){
			registerWp(data, historyId);
		}
		//TH: person
		else{
			registerPs(data, historyId);
		}
	}

	private void registerCom(RegisterAppApprovalRootCommand data,String historyId){
		String companyId = AppContexts.user().companyId();
		List<CompanyAppRootADto> root = data.getRoot();
		boolean checkAddHist = data.isCheckAddHist();
		String startDate = data.getStartDate().replace("/", "-");
		String endDateOld = data.getEndDate().replace("/", "-");
		GeneralDate sDate = GeneralDate.localDate(LocalDate.parse(startDate));
		GeneralDate eDate = sDate.addDays(-1);
		String endDateNew = eDate.toString().replace("/", "-");
		String endDateS = "9999-12-31";
		GeneralDate endDate = GeneralDate.fromString(endDateS, "yyyy-MM-dd");
		GeneralDate endDateUpdate = GeneralDate.fromString(endDateOld, "yyyy-MM-dd");
		//TH: create history new
		if(checkAddHist){
			//Tạo root có ls mới với appType ở dữ liệu bên phải.
			//Update root có ls trước đó của những root mới được tạo ở trên.
			List<CompanyApprovalRoot> listCom = new ArrayList<>();
			List<CompanyApprovalRoot> listComPre = new ArrayList<>();
			List<ApprovalBranch> lstBranch = new ArrayList<>();
			for (CompanyAppRootADto commonRoot : root) {
				Integer type = commonRoot.getAppTypeValue();
				String branchId = UUID.randomUUID().toString();
				//root right
				CompanyApprovalRoot com = CompanyApprovalRoot.createSimpleFromJavaType(companyId, 
									UUID.randomUUID().toString(), historyId, type, startDate, endDateS,
									branchId, null, null, type == null ? 0 : 1);
				//branch
				ApprovalBranch branch = new ApprovalBranch(companyId,branchId,1);
				lstBranch.add(branch);
				listCom.add(com);
				//get root old by end date and type
				List<CompanyApprovalRoot> comOld = repoCom.getComApprovalRootByEdate(companyId, endDate, type);
				if(!comOld.isEmpty()){
					//update ls cu
					CompanyApprovalRoot comPre = CompanyApprovalRoot.updateSdateEdate(comOld.get(0), endDateNew);
					listComPre.add(comPre);
				}
				//Add approval
				addApproval(commonRoot, 0, branchId, checkAddHist);
			}
			//Add ls new, update ls old, add branch
			repoCom.addAllComApprovalRoot(listCom);
			repoCom.updateAllComApprovalRoot(listComPre);
			repoBranch.addAllBranch(lstBranch);
		}
		//TH: update history old
		else{
			List<Integer> lstAppTypeDb = data.getLstAppType();
			List<Integer> lstAppTypeUi = new ArrayList<>();
			for (CompanyAppRootADto commonRoot : root) {
				lstAppTypeUi.add(commonRoot.getAppTypeValue());
			}
			//delete root not display in screen
			for (Integer type : lstAppTypeDb) {
				if(!lstAppTypeUi.contains(type)){
					List<CompanyApprovalRoot> lstCom = repoCom.getComApprovalRootByEdate(companyId, endDateUpdate, type);
					if(!lstCom.isEmpty()){
						CompanyApprovalRoot com = lstCom.get(0);
						//==========
						deleteAppPh(com.getBranchId());
						//=======
						repoCom.deleteComApprovalRoot(companyId, com.getApprovalId(), com.getHistoryId());
					}
				}
			}
			//update root display in screen
			updateRoot(lstAppTypeUi, root);
		}
	}
	/**
	 * register work place
	 * @param data
	 * @param historyId
	 */
	private void registerWp(RegisterAppApprovalRootCommand data,String historyId){
		String companyId = AppContexts.user().companyId();
		List<CompanyAppRootADto> root = data.getRoot();
		String workplaceId = data.getWorkpplaceId();
		boolean checkAddHist = data.isCheckAddHist();
		String startDate = data.getStartDate().replace("/", "-");
		String endDateOld = data.getEndDate().replace("/", "-");
		GeneralDate sDate = GeneralDate.localDate(LocalDate.parse(startDate));
		GeneralDate eDate = sDate.addDays(-1);
		String endDateNew = eDate.toString().replace("/", "-");
		String endDateS = "9999-12-31";
		GeneralDate endDate = GeneralDate.fromString(endDateS, "yyyy-MM-dd");
		GeneralDate endDateUpdate = GeneralDate.fromString(endDateOld, "yyyy-MM-dd");
		//TH: create history new
		if(checkAddHist){
			//Tạo root có ls mới với appType ở dữ liệu bên phải.
			//Update root có ls trước đó của những root mới được tạo ở trên.
			List<WorkplaceApprovalRoot> listWp = new ArrayList<>();
			List<WorkplaceApprovalRoot> listWpPre = new ArrayList<>();
			List<ApprovalBranch> lstBranch = new ArrayList<>();
			for (CompanyAppRootADto commonRoot : root) {
				Integer type = commonRoot.getAppTypeValue();
				String branchId = UUID.randomUUID().toString();
				WorkplaceApprovalRoot com = WorkplaceApprovalRoot.createSimpleFromJavaType(companyId, 
									UUID.randomUUID().toString(), workplaceId, historyId, type, startDate, endDateS,
									branchId, null, null, type == null ? 0 : 1);
				ApprovalBranch branch = new ApprovalBranch(companyId,branchId,1);
				lstBranch.add(branch);
				listWp.add(com);
				List<WorkplaceApprovalRoot> psOld = repoWorkplace.getWpApprovalRootByEdate(companyId, workplaceId, endDate, type);
				if(!psOld.isEmpty()){
					//update ls cu
					WorkplaceApprovalRoot psPre = WorkplaceApprovalRoot.updateSdateEdate(psOld.get(0), endDateNew);
					listWpPre.add(psPre);
				}
				//Add approval
				addApproval(commonRoot, 0, branchId, checkAddHist);
			}
			//Add ls new, update ls old, add branch
			repoWorkplace.addAllWpApprovalRoot(listWp);
			repoWorkplace.updateAllWpApprovalRoot(listWpPre);
			repoBranch.addAllBranch(lstBranch);
		}
		//TH: update history old
		else{
			List<Integer> lstAppTypeDb = data.getLstAppType();
			List<Integer> lstAppTypeUi = new ArrayList<>();
			for (CompanyAppRootADto commonRoot : root) {
				lstAppTypeUi.add(commonRoot.getAppTypeValue());
			}
			//delete root not display in screen
			for (Integer type : lstAppTypeDb) {
				if(!lstAppTypeUi.contains(type)){
					List<WorkplaceApprovalRoot> lstWp = repoWorkplace.getWpApprovalRootByEdate(companyId, workplaceId, endDateUpdate, type);
					if(!lstWp.isEmpty()){
						WorkplaceApprovalRoot wp = lstWp.get(0);
						//==========
						deleteAppPh(wp.getBranchId());
						//=======
						repoCom.deleteComApprovalRoot(companyId, wp.getApprovalId(), wp.getHistoryId());
					}
				}
			}
			//update root display in screen
			updateRoot(lstAppTypeUi, root);
		}
	}
	/**
	 * register person
	 * @param data
	 * @param historyId
	 */
	private void registerPs(RegisterAppApprovalRootCommand data,String historyId){
		String companyId = AppContexts.user().companyId();
		List<CompanyAppRootADto> root = data.getRoot();
		String employeeId = data.getEmployeeId(); 
		boolean checkAddHist = data.isCheckAddHist();
		String startDate = data.getStartDate().replace("/", "-");
		String endDateOld = data.getEndDate().replace("/", "-");
		GeneralDate sDate = GeneralDate.localDate(LocalDate.parse(startDate));
		GeneralDate eDate = sDate.addDays(-1);
		String endDateNew = eDate.toString().replace("/", "-");
		String endDateS = "9999-12-31";
		GeneralDate endDate = GeneralDate.fromString(endDateS, "yyyy-MM-dd");
		GeneralDate endDateUpdate = GeneralDate.fromString(endDateOld, "yyyy-MM-dd");
		//TH: create history new
		if(checkAddHist){
			//Tạo root có ls mới với appType ở dữ liệu bên phải.
			//Update root có ls trước đó của những root mới được tạo ở trên.
			List<PersonApprovalRoot> listPs = new ArrayList<>();
			List<PersonApprovalRoot> listPsPre = new ArrayList<>();
			List<ApprovalBranch> lstBranch = new ArrayList<>();
			for (CompanyAppRootADto commonRoot : root) {
				Integer type = commonRoot.getAppTypeValue();
				String branchId = UUID.randomUUID().toString();
				PersonApprovalRoot com = PersonApprovalRoot.createSimpleFromJavaType(companyId, 
									UUID.randomUUID().toString(), employeeId, historyId, type, startDate, endDateS,
									branchId, null, null, type == null ? 0 : 1);
				ApprovalBranch branch = new ApprovalBranch(companyId,branchId,1);
				lstBranch.add(branch);
				listPs.add(com);
				List<PersonApprovalRoot> psOld = repoPerson.getPsApprovalRootByEdate(companyId, employeeId, endDate, type);
				if(!psOld.isEmpty()){
					//update ls cu
					PersonApprovalRoot psPre = PersonApprovalRoot.updateSdateEdate(psOld.get(0), endDateNew);
					listPsPre.add(psPre);
				}
				//Add approval
				addApproval(commonRoot, 0, branchId, checkAddHist);
			}
			//Add ls new, update ls old, add branch
			repoPerson.addAllPsApprovalRoot(listPs);
			repoPerson.updateAllPsApprovalRoot(listPsPre);
			repoBranch.addAllBranch(lstBranch);
		}
		//TH: update history old
		else{
			List<Integer> lstAppTypeDb = data.getLstAppType();
			List<Integer> lstAppTypeUi = new ArrayList<>();
			for (CompanyAppRootADto commonRoot : root) {
				lstAppTypeUi.add(commonRoot.getAppTypeValue());
			}
			//delete root not display in screen
			for (Integer type : lstAppTypeDb) {
				if(!lstAppTypeUi.contains(type)){
					List<PersonApprovalRoot> lstPs = repoPerson.getPsApprovalRootByEdate(companyId, employeeId, endDateUpdate, type);
					if(!lstPs.isEmpty()){
						PersonApprovalRoot ps = lstPs.get(0);
						//==========
						deleteAppPh(ps.getBranchId());
						//=======
						repoCom.deleteComApprovalRoot(companyId, ps.getApprovalId(), ps.getHistoryId());
					}
				}
			}
			//update root display in screen
			updateRoot(lstAppTypeUi, root);
		}
	}
	/**
	 * Add new history
	 * @param root
	 * @param lstAddHist
	 * @param rootType
	 * @param startDate
	 * @param endDate
	 * @param branchId
	 */
	private void addApproval(CompanyAppRootADto commonRoot, int rootType, String branchId, boolean checkAddHist){
		if(commonRoot == null){
			return;
		}
		List<ApprovalPhase> listAppPhase = new ArrayList<>();
		deleteAppPh(branchId);
		ApprovalPhase appPhaseN1 = checkAppPh(commonRoot.getAppPhase1(), branchId);
		ApprovalPhase appPhaseN2 = checkAppPh(commonRoot.getAppPhase2(), branchId);
		ApprovalPhase appPhaseN3 = checkAppPh(commonRoot.getAppPhase3(), branchId);
		ApprovalPhase appPhaseN4 = checkAppPh(commonRoot.getAppPhase4(), branchId);
		ApprovalPhase appPhaseN5 = checkAppPh(commonRoot.getAppPhase5(), branchId);
		if(appPhaseN1 != null){
			listAppPhase.add(appPhaseN1);
		}
		if(appPhaseN2 != null){
			listAppPhase.add(appPhaseN2);
		}
		if(appPhaseN3 != null){
			listAppPhase.add(appPhaseN3);
		}
		if(appPhaseN4 != null){
			listAppPhase.add(appPhaseN4);
		}
		if(appPhaseN5 != null){
			listAppPhase.add(appPhaseN5);
		}
		for (ApprovalPhase approvalPhase : listAppPhase) {
			repoApprover.addAllApprover(approvalPhase.getApprovers());
		}
		repoAppPhase.addAllApprovalPhase(listAppPhase);
	}

	/**
	 * TH: update
	 * update root display in screen
	 */
	private void updateRoot(List<Integer> lstAppTypeUi, List<CompanyAppRootADto> root){
		//update root display in screen
		for (Integer type : lstAppTypeUi) {
			CompanyAppRootADto commonRoot = findRoot(root, type);
			String branchId = commonRoot.getBranchId();
			ApprovalPhase appPhaseN1 = checkAppPh(commonRoot.getAppPhase1(), branchId);
			ApprovalPhase appPhaseN2 = checkAppPh(commonRoot.getAppPhase2(), branchId);
			ApprovalPhase appPhaseN3 = checkAppPh(commonRoot.getAppPhase3(), branchId);
			ApprovalPhase appPhaseN4 = checkAppPh(commonRoot.getAppPhase4(), branchId);
			ApprovalPhase appPhaseN5 = checkAppPh(commonRoot.getAppPhase5(), branchId);
			//Xu ly them,sua,xoa appPh and approver
			if(appPhaseN1 != null && appPhaseN1.getApprovalForm().value != 0){
				addAppPhase(appPhaseN1,branchId);
			}
			if(appPhaseN2 != null && appPhaseN2.getApprovalForm().value != 0){
				addAppPhase(appPhaseN2,branchId);
			}
			if(appPhaseN3 != null && appPhaseN3.getApprovalForm().value != 0){
				addAppPhase(appPhaseN3,branchId);
			}
			if(appPhaseN4 != null && appPhaseN4.getApprovalForm().value != 0){
				addAppPhase(appPhaseN4,branchId);
			}
			if(appPhaseN5 != null && appPhaseN5.getApprovalForm().value != 0){
				addAppPhase(appPhaseN5,branchId);
			}
		}
	}
	/**
	 * add appPhase
	 * @param appPhaseN1
	 * @param branchId
	 */
	private void addAppPhase(ApprovalPhase appPhaseN1, String branchId){
		if(appPhaseN1 == null){
			return;
		}
		String companyId = AppContexts.user().companyId();
		Optional<ApprovalPhase> appPh1 = repoAppPhase.getApprovalPhase(companyId, branchId, appPhaseN1.getApprovalPhaseId());
		if(!appPh1.isPresent()){//add new appPh and Approver
			String approvalPhaseId = UUID.randomUUID().toString();
			appPhaseN1.updateAppPhaseId(approvalPhaseId);
			List<Approver>  approvers = appPhaseN1.getApprovers();
			for (Approver approver : approvers) {
				approver.updateApprovalPhaseId(approvalPhaseId);
				approver.updateApproverId(UUID.randomUUID().toString());
			}
			repoApprover.addAllApprover(approvers);
			repoAppPhase.addApprovalPhase(appPhaseN1);
		}else{//update
			repoApprover.deleteAllApproverByAppPhId(companyId, appPhaseN1.getApprovalPhaseId());
			List<Approver>  approvers = appPhaseN1.getApprovers();
			for (Approver approver : approvers) {
				approver.updateApprovalPhaseId(appPhaseN1.getApprovalPhaseId());
				approver.updateApproverId(UUID.randomUUID().toString());
			}
			repoApprover.addAllApprover(approvers);
			repoAppPhase.updateApprovalPhase(appPhaseN1);
		}
	}
	/**
	 * check AppPhase(add or not add)
	 * @param appPhase
	 * @param branchId
	 * @return
	 */
	private ApprovalPhase checkAppPh(ApprovalPhaseDto appPhase, String branchId){
		if(appPhase.getApprovalForm() == null || appPhase.getApprovalForm().intValue() == 0 ){//khong co
			return null;
		}
		String companyId = AppContexts.user().companyId();
		String approvalPhaseId = appPhase.getApprovalPhaseId();
		List<ApproverDto> approver = appPhase.getApprover();
		List<Approver> lstApp = new ArrayList<>();
		for (ApproverDto approverDto : approver) {
			lstApp.add(Approver.createSimpleFromJavaType(companyId, branchId,
					approvalPhaseId, UUID.randomUUID().toString(), approverDto.getJobTitleId(), approverDto.getEmployeeId(), approverDto.getOrderNumber(), approverDto.getApprovalAtr(), approverDto.getConfirmPerson()));
		}
		return ApprovalPhase.createSimpleFromJavaType(companyId, branchId,
				approvalPhaseId , appPhase.getApprovalForm(),
				appPhase.getBrowsingPhase(), appPhase.getOrderNumber(),lstApp);
	}
	/**
	 * find root
	 * @param root
	 * @param appTypeValue
	 * @return
	 */
	private CompanyAppRootADto findRoot(List<CompanyAppRootADto> root, Integer appTypeValue){
		for (CompanyAppRootADto companyAppRootADto : root) {
			if(companyAppRootADto.getAppTypeValue() == appTypeValue){
				return companyAppRootADto;
			}
		}
		return null;
	}
	/**
	 * delete
	 * @param branchId
	 */
	private void deleteAppPh(String branchId){
		String companyId = AppContexts.user().companyId();
		List<ApprovalPhase> lstAppPhase = repoAppPhase.getAllApprovalPhasebyCode(companyId, branchId);
		if(!lstAppPhase.isEmpty()){
			for (ApprovalPhase approvalPhase : lstAppPhase) {
				//delete All Approver By Approval Phase Id
				repoApprover.deleteAllApproverByAppPhId(companyId, approvalPhase.getApprovalPhaseId());
			}
			//delete All Approval Phase By Branch Id
			repoAppPhase.deleteAllAppPhaseByBranchId(companyId, branchId);
		}
	}
}
