package nts.uk.ctx.workflow.app.command.approvermanagement.workroot;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.arc.error.BusinessException;
import nts.arc.layer.app.command.CommandHandler;
import nts.arc.layer.app.command.CommandHandlerContext;
import nts.arc.time.GeneralDate;
import nts.gul.text.StringUtil;
import nts.uk.ctx.workflow.app.find.approvermanagement.workroot.ApprovalPhaseDto;
import nts.uk.ctx.workflow.app.find.approvermanagement.workroot.ApproverDto;
import nts.uk.ctx.workflow.dom.adapter.workplace.WorkplaceApproverAdapter;
import nts.uk.ctx.workflow.dom.adapter.workplace.WorkplaceImport;
import nts.uk.ctx.workflow.dom.approvermanagement.workroot.ApprovalBranch;
import nts.uk.ctx.workflow.dom.approvermanagement.workroot.ApprovalBranchRepository;
import nts.uk.ctx.workflow.dom.approvermanagement.workroot.ApprovalPhase;
import nts.uk.ctx.workflow.dom.approvermanagement.workroot.ApprovalPhaseRepository;
import nts.uk.ctx.workflow.dom.approvermanagement.workroot.Approver;
import nts.uk.ctx.workflow.dom.approvermanagement.workroot.ApproverRepository;
import nts.uk.ctx.workflow.dom.approvermanagement.workroot.CompanyApprovalRoot;
import nts.uk.ctx.workflow.dom.approvermanagement.workroot.CompanyApprovalRootRepository;
import nts.uk.ctx.workflow.dom.approvermanagement.workroot.EmploymentRootAtr;
import nts.uk.ctx.workflow.dom.approvermanagement.workroot.PersonApprovalRoot;
import nts.uk.ctx.workflow.dom.approvermanagement.workroot.PersonApprovalRootRepository;
import nts.uk.ctx.workflow.dom.approvermanagement.workroot.WorkplaceApprovalRoot;
import nts.uk.ctx.workflow.dom.approvermanagement.workroot.WorkplaceApprovalRootRepository;
import nts.uk.ctx.workflow.dom.resultrecord.RecordRootType;
import nts.uk.ctx.workflow.dom.resultrecord.service.CreateDailyApprover;
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
	@Inject
	private WorkplaceApproverAdapter adapterWp;
	@Inject
	private CreateDailyApprover creDailyAppr;
	private static final int COMPANY = 0;
	private static final int WORKPLACE = 1;
	@Override
	protected void handle(CommandHandlerContext<RegisterAppApprovalRootCommand> context) {
		//____________New__________
		String historyId = UUID.randomUUID().toString();
		RegisterAppApprovalRootCommand data = context.getCommand();
		int rootType = data.getRootType();
		//TH: company
		if(rootType == COMPANY){
			registerCom(data, historyId);
		}
		//TH: work place
		else if(rootType == WORKPLACE){
			registerWp(data, historyId);
		}
		//TH: person
		else{
			registerPs(data, historyId);
		}
	}
	/**
	 * register domain Company
	 * @param data
	 * @param historyId
	 */
	private void registerCom(RegisterAppApprovalRootCommand data,String historyId){
		String companyId = AppContexts.user().companyId();
		List<CompanyAppRootADto> root = data.getRoot();
		List<CompanyAppRootADto> rootInsert = new ArrayList<>();
		boolean checkAddHist = data.isCheckAddHist();
		String startDate = data.getStartDate().replace("/", "-");
		String endDateOld = data.getEndDate().replace("/", "-");
		GeneralDate sDate = GeneralDate.localDate(LocalDate.parse(startDate));
		GeneralDate eDate = sDate.addDays(-1);
		String endDateNew = eDate.toString().replace("/", "-");
		String endDateS = "9999-12-31";
		GeneralDate endDateUpdate = GeneralDate.fromString(endDateOld, "yyyy-MM-dd");
		//loai bo nhung root chua duoc setting
		for (CompanyAppRootADto commonRoot : root) {
			if(commonRoot.isColor()){
				rootInsert.add(commonRoot);
			}
		}
		//TH: create history new
		if(checkAddHist){
			//Tạo root có ls mới với appType ở dữ liệu bên phải.
			//Update root có ls trước đó của những root mới được tạo ở trên.
			List<CompanyApprovalRoot> listCom = new ArrayList<>();
			List<CompanyApprovalRoot> listComPre = new ArrayList<>();
			List<ApprovalBranch> lstBranch = new ArrayList<>();
			for (CompanyAppRootADto commonRoot : rootInsert) {
				Integer type = commonRoot.getAppTypeValue();
				int employRootAtr = commonRoot.getEmployRootAtr();
				String branchId = UUID.randomUUID().toString();
				//root right
				CompanyApprovalRoot com = CompanyApprovalRoot.createSimpleFromJavaType(companyId, 
									UUID.randomUUID().toString(), historyId, type, startDate, endDateS,
									branchId, null, employRootAtr == EmploymentRootAtr.CONFIRMATION.value ? type : null, employRootAtr);
				//branch
				ApprovalBranch branch = new ApprovalBranch(companyId,branchId,1);
				lstBranch.add(branch);
				if(!CompanyApprovalRoot.checkValidate(startDate.toString(), endDateS)){
					throw new BusinessException("Msg_156");
				}
				listCom.add(com);
				//Add approval
				addApproval(commonRoot, branchId);
			}
			//find history by type and EmployRootAtr
			if(data.getCheckMode() == 1){
				// xử lí update phần riêng
				if(!rootInsert.isEmpty()){
					List<CompanyApprovalRoot> comOld = repoCom.getComApprovalRootByType(companyId, rootInsert.get(0).getAppTypeValue(), rootInsert.get(0).getEmployRootAtr());
					if(!comOld.isEmpty()){
						//update ls cu
						CompanyApprovalRoot comPre = CompanyApprovalRoot.updateEdate(comOld.get(0), endDateNew);
						listComPre.add(comPre);
					}
				}
			}else{
				// xử lí update phần chung
				List<CompanyApprovalRoot> comOlds = repoCom.getComAppRootLast(companyId, GeneralDate.fromString("9999/12/31", "yyyy/MM/dd"));
					for(CompanyApprovalRoot comAppRoot : comOlds){
						CompanyApprovalRoot comPre = CompanyApprovalRoot.updateEdate(comAppRoot, endDateNew);
						listComPre.add(comPre);
					}
				
			}
			//Add ls new, update ls old, add branch
			repoCom.addAllComApprovalRoot(listCom);
			repoCom.updateAllComApprovalRoot(listComPre);
			repoBranch.addAllBranch(lstBranch);
		}
		//TH: update history old
		else{
			List<AppType> lstAppTypeDb = data.getLstAppType();
			List<AppType> lstAppTypeUi = new ArrayList<>();
			for (CompanyAppRootADto commonRoot : rootInsert) {
				lstAppTypeUi.add(new AppType(commonRoot.getAppTypeValue(), commonRoot.getEmployRootAtr()));
			}
			//delete root not display in screen
			for (AppType type : lstAppTypeDb) {
				if(!lstAppTypeUi.contains(type)){
					List<CompanyApprovalRoot> lstCom = repoCom.getComApprovalRootByEdate(companyId, endDateUpdate, type.getValue(), type.getEmployRootAtr());
					if(!lstCom.isEmpty()){
						CompanyApprovalRoot com = lstCom.get(0);
						//==========
						deleteAppPh(com.getBranchId());
						//=======
						//delete root
						repoCom.deleteComApprovalRoot(companyId, com.getApprovalId(), com.getEmploymentAppHistoryItems().get(0).getHistoryId());
						//delete branch
						repoBranch.deleteBranch(companyId, com.getBranchId());
					}
				}
			}
			List<CompanyApprovalRoot> listCom = new ArrayList<>();
			List<ApprovalBranch> lstBranch = new ArrayList<>();
			for (AppType type : lstAppTypeUi) {
				CompanyAppRootADto commonRoot = findRoot(root, type);
				String branchId = commonRoot.getBranchId();
				//root create new
				if(StringUtil.isNullOrEmpty(branchId, true)){
					branchId = UUID.randomUUID().toString();
					Integer typeCom = commonRoot.getAppTypeValue();
					int employRootAtr = commonRoot.getEmployRootAtr();
					//root right
					CompanyApprovalRoot com = CompanyApprovalRoot.createSimpleFromJavaType(companyId, 
										UUID.randomUUID().toString(), historyId, type.getValue(), startDate, endDateOld,
										branchId, null, employRootAtr == EmploymentRootAtr.CONFIRMATION.value ? typeCom : null, employRootAtr);
					//branch
					ApprovalBranch branch = new ApprovalBranch(companyId,branchId,1);
					lstBranch.add(branch);
					if(!CompanyApprovalRoot.checkValidate(startDate.toString(), endDateOld)){
						throw new BusinessException("Msg_156");
					}
					listCom.add(com);
					addApproval(commonRoot,branchId);
				}
			}
			//Add ls new
			repoCom.addAllComApprovalRoot(listCom);
			//add branch
			repoBranch.addAllBranch(lstBranch);
			//update root display in screen
			updateRoot(lstAppTypeUi, rootInsert);
		}
	}
	/**
	 * register domain work place
	 * @param data
	 * @param historyId
	 */
	private void registerWp(RegisterAppApprovalRootCommand data,String historyId){
		String companyId = AppContexts.user().companyId();
		//list display in screen
		List<CompanyAppRootADto> root = data.getRoot();
		//list insert in db
		List<CompanyAppRootADto> rootInsert = new ArrayList<>();
		String workplaceId = data.getWorkpplaceId();
		boolean checkAddHist = data.isCheckAddHist();
		String startDate = data.getStartDate().replace("/", "-");
		String endDateOld = data.getEndDate().replace("/", "-");
		GeneralDate sDate = GeneralDate.localDate(LocalDate.parse(startDate));
		GeneralDate eDate = sDate.addDays(-1);
		String endDateNew = eDate.toString().replace("/", "-");
		String endDateS = "9999-12-31";
		GeneralDate endDateUpdate = GeneralDate.fromString(endDateOld, "yyyy-MM-dd");
		//loai bo nhung root chua duoc setting
		for (CompanyAppRootADto commonRoot : root) {
			if(commonRoot.isColor()){
				rootInsert.add(commonRoot);
			}
		}
		if(workplaceId.compareTo("") == 0){
			GeneralDate baseDate = GeneralDate.today();
			WorkplaceImport workplace = adapterWp.findBySid(AppContexts.user().employeeId(), baseDate);
			workplaceId = workplace.getWkpId();
		}
		//TH: create history new
		if(checkAddHist){
			if(rootInsert.size() == 0){//TH tao moi ls nhung k co don nao duoc setting
				return;
			}
			//Tạo root có ls mới với appType ở dữ liệu bên phải.
			//Update root có ls trước đó của những root mới được tạo ở trên.
			List<WorkplaceApprovalRoot> listWp = new ArrayList<>();
			List<WorkplaceApprovalRoot> listWpPre = new ArrayList<>();
			List<ApprovalBranch> lstBranch = new ArrayList<>();
			for (CompanyAppRootADto commonRoot : rootInsert) {
				Integer type = commonRoot.getAppTypeValue();
				int employRootAtr = commonRoot.getEmployRootAtr();
				String branchId = UUID.randomUUID().toString();
				WorkplaceApprovalRoot com = WorkplaceApprovalRoot.createSimpleFromJavaType(companyId, 
									UUID.randomUUID().toString(), workplaceId, historyId, type, startDate, endDateS,
									branchId, null, employRootAtr == EmploymentRootAtr.CONFIRMATION.value ? type : null, employRootAtr);
				ApprovalBranch branch = new ApprovalBranch(companyId,branchId,1);
				lstBranch.add(branch);
				if(!WorkplaceApprovalRoot.checkValidate(startDate.toString(), endDateS)){
					throw new BusinessException("Msg_156");
				}
				listWp.add(com);
				
				//Add approval
				addApproval(commonRoot, branchId);
			}
			//find history by type and 
			if(data.getCheckMode() == 1){
				List<WorkplaceApprovalRoot> psOld = repoWorkplace.getWpApprovalRootByType(companyId, workplaceId, rootInsert.get(0).getAppTypeValue(), rootInsert.get(0).getEmployRootAtr());
				if(!psOld.isEmpty()){
					//update ls cu
					WorkplaceApprovalRoot psPre = WorkplaceApprovalRoot.updateEdate(psOld.get(0), endDateNew);
					listWpPre.add(psPre);
				}
			}else{
				// xu li update phan chung
				List<WorkplaceApprovalRoot> psOlds = repoWorkplace.getWpAppRootLast(companyId, workplaceId, GeneralDate.fromString("9999/12/31", "yyyy/MM/dd"));
				for(WorkplaceApprovalRoot wpAppRoot : psOlds){
					WorkplaceApprovalRoot psPre = WorkplaceApprovalRoot.updateEdate(wpAppRoot, endDateNew);
					listWpPre.add(psPre);
				}
			}
			
			
			//Add ls new, update ls old, add branch
			repoWorkplace.addAllWpApprovalRoot(listWp);
			repoWorkplace.updateAllWpApprovalRoot(listWpPre);
			repoBranch.addAllBranch(lstBranch);
		}
		//TH: update history old
		else{
			List<AppType> lstAppTypeDb = data.getLstAppType();
			List<AppType> lstAppTypeUi = new ArrayList<>();
			for (CompanyAppRootADto commonRoot : rootInsert) {
				lstAppTypeUi.add(new AppType(commonRoot.getAppTypeValue(), commonRoot.getEmployRootAtr()));
			}
			//delete root not display in screen
			for (AppType type : lstAppTypeDb) {
				if(!lstAppTypeUi.contains(type)){
					List<WorkplaceApprovalRoot> lstWp = repoWorkplace.getWpApprovalRootByEdate(companyId, workplaceId, endDateUpdate, type.getValue(), type.getEmployRootAtr());
					if(!lstWp.isEmpty()){
						WorkplaceApprovalRoot wp = lstWp.get(0);
						//==========
						deleteAppPh(wp.getBranchId());
						//=======
						repoWorkplace.deleteWpApprovalRoot(companyId, wp.getApprovalId(), workplaceId, wp.getEmploymentAppHistoryItems().get(0).getHistoryId());
						//delete branch
						repoBranch.deleteBranch(companyId, wp.getBranchId());
					}
				}
			}
			List<WorkplaceApprovalRoot> listWp = new ArrayList<>();
			List<ApprovalBranch> lstBranch = new ArrayList<>();
			for (AppType type : lstAppTypeUi) {
				CompanyAppRootADto commonRoot = findRoot(root, type);
				String branchId = commonRoot.getBranchId();
				if(StringUtil.isNullOrEmpty(branchId, true)){
					branchId = UUID.randomUUID().toString();
					int employRootAtr = commonRoot.getEmployRootAtr();
					//root right
					WorkplaceApprovalRoot wp = WorkplaceApprovalRoot.createSimpleFromJavaType(companyId, UUID.randomUUID().toString(),workplaceId,
							historyId, employRootAtr == EmploymentRootAtr.APPLICATION.value ? type.getValue() : null, startDate, endDateOld,
							branchId, null,employRootAtr == EmploymentRootAtr.CONFIRMATION.value ? type.getValue() : null, employRootAtr);
					//branch
					ApprovalBranch branch = new ApprovalBranch(companyId,branchId,1);
					lstBranch.add(branch);
					if(!WorkplaceApprovalRoot.checkValidate(startDate.toString(), endDateOld)){
						throw new BusinessException("Msg_156");
					}
					listWp.add(wp);
					addApproval(commonRoot,branchId);
				}
			}
			//Add ls new
			repoWorkplace.addAllWpApprovalRoot(listWp);
			//add branch
			repoBranch.addAllBranch(lstBranch);
			//update root display in screen
			updateRoot(lstAppTypeUi, root);
		}
	}
	/**
	 * register domain person
	 * @param data
	 * @param historyId
	 */
	private void registerPs(RegisterAppApprovalRootCommand data,String historyId){
		String companyId = AppContexts.user().companyId();
		//list display in screen
		List<CompanyAppRootADto> root = data.getRoot();
		//list insert in db
		List<CompanyAppRootADto> rootInsert = new ArrayList<>();
		String employeeId = data.getEmployeeId(); 
		boolean checkAddHist = data.isCheckAddHist();
		String startDate = data.getStartDate().replace("/", "-");
		String endDateOld = data.getEndDate().replace("/", "-");
		GeneralDate sDate = GeneralDate.localDate(LocalDate.parse(startDate));
		GeneralDate eDate = sDate.addDays(-1);
		String endDateNew = eDate.toString().replace("/", "-");
		String endDateS = "9999-12-31";
		GeneralDate endDateUpdate = GeneralDate.fromString(endDateOld, "yyyy-MM-dd");
		//loai bo nhung root chua duoc setting
		for (CompanyAppRootADto commonRoot : root) {
			if(commonRoot.isColor()){
				rootInsert.add(commonRoot);
			}
		}
		//TH: create history new
		//履歴追加を実行する
		if(checkAddHist){
			//Tạo root có ls mới với appType ở dữ liệu bên phải.
			//Update root có ls trước đó của những root mới được tạo ở trên.
			List<PersonApprovalRoot> listPs = new ArrayList<>();
			List<PersonApprovalRoot> listPsPre = new ArrayList<>();
			List<ApprovalBranch> lstBranch = new ArrayList<>();
			for (CompanyAppRootADto commonRoot : rootInsert) {
				Integer type = commonRoot.getAppTypeValue();
				int employRootAtr = commonRoot.getEmployRootAtr();
				String branchId = UUID.randomUUID().toString();
				PersonApprovalRoot com = PersonApprovalRoot.createSimpleFromJavaType(companyId, 
									UUID.randomUUID().toString(), employeeId, historyId, type, startDate, endDateS,
									branchId, null, employRootAtr == EmploymentRootAtr.CONFIRMATION.value ? type : null, employRootAtr);
				ApprovalBranch branch = new ApprovalBranch(companyId,branchId,1);
				lstBranch.add(branch);
				if(!PersonApprovalRoot.checkValidate(startDate.toString(), endDateS)){
					throw new BusinessException("Msg_156");
				}
				listPs.add(com);
				
				//Add approval
				addApproval(commonRoot, branchId);
			}
			//find history by type and
			if(data.getCheckMode() == 1){
				List<PersonApprovalRoot> psOld = repoPerson.getPsApprovalRootByType(companyId, employeeId, rootInsert.get(0).getAppTypeValue(), rootInsert.get(0).getEmployRootAtr());
				if(!psOld.isEmpty()){
					//update ls cu
					PersonApprovalRoot psPre = PersonApprovalRoot.updateEdate(psOld.get(0), endDateNew);
					listPsPre.add(psPre);
				}
			}else{
				// xu li phan chung 
				List<PersonApprovalRoot> psOlds = repoPerson.getPsAppRootLastest(companyId, employeeId, GeneralDate.fromString("9999/12/31", "yyyy/MM/dd"));
				for(PersonApprovalRoot psAppRoot : psOlds){
					PersonApprovalRoot psPre = PersonApprovalRoot.updateEdate(psAppRoot, endDateNew);
					listPsPre.add(psPre);
				}
			}
			
			//Add ls new, update ls old, add branch
			repoPerson.addAllPsApprovalRoot(listPs);
			repoPerson.updateAllPsApprovalRoot(listPsPre);
			repoBranch.addAllBranch(lstBranch);
		}
		//TH: update history old
		//承認ルートの更新を実行する
		else{
			List<AppType> lstAppTypeDb = data.getLstAppType();
			List<AppType> lstAppTypeUi = new ArrayList<>();
			for (CompanyAppRootADto commonRoot : rootInsert) {
				lstAppTypeUi.add(new AppType(commonRoot.getAppTypeValue(),commonRoot.getEmployRootAtr()));
			}
			//delete root not display in screen
			for (AppType type : lstAppTypeDb) {
				if(!lstAppTypeUi.contains(type)){
					List<PersonApprovalRoot> lstPs = repoPerson.getPsApprovalRootByEdate(companyId, employeeId, endDateUpdate, type.getValue(), type.getEmployRootAtr());
					if(!lstPs.isEmpty()){
						PersonApprovalRoot ps = lstPs.get(0);
						//==========
						deleteAppPh(ps.getBranchId());
						//=======
						repoPerson.deletePsApprovalRoot(companyId, ps.getApprovalId(),employeeId, ps.getEmploymentAppHistoryItems().get(0).getHistoryId());
						//delete branch
						repoBranch.deleteBranch(companyId, ps.getBranchId());
					}
				}
			}
			List<PersonApprovalRoot> listPs = new ArrayList<>();
			List<ApprovalBranch> lstBranch = new ArrayList<>();
			for (AppType type : lstAppTypeUi) {
				CompanyAppRootADto commonRoot = findRoot(root, type);
				String branchId = commonRoot.getBranchId();
				if(StringUtil.isNullOrEmpty(branchId, true)){
					branchId = UUID.randomUUID().toString();
					int employRootAtr = commonRoot.getEmployRootAtr();
					//root right
					PersonApprovalRoot ps = PersonApprovalRoot.createSimpleFromJavaType(companyId, UUID.randomUUID().toString(),employeeId,
							historyId, employRootAtr == EmploymentRootAtr.APPLICATION.value ? type.getValue() : null, startDate, endDateOld,
							branchId, null,employRootAtr == EmploymentRootAtr.CONFIRMATION.value ? type.getValue() : null, employRootAtr);
					//branch
					ApprovalBranch branch = new ApprovalBranch(companyId,branchId,1);
					lstBranch.add(branch);
					if(!PersonApprovalRoot.checkValidate(startDate.toString(), endDateOld)){
						throw new BusinessException("Msg_156");
					}
					listPs.add(ps);
					addApproval(commonRoot,branchId);
				}
			}
			//Add ls new
			repoPerson.addAllPsApprovalRoot(listPs);
			//add branch
			repoBranch.addAllBranch(lstBranch);
			//update root display in screen
			updateRoot(lstAppTypeUi, root);
		}
		//EA修正履歴 No.3267
		//EA修正履歴 No.3269
		//EA修正履歴 No.3270
		//EA修正履歴 No.3272
		//追加する履歴の開始日とシステム日付をチェックする
		if(sDate.after(GeneralDate.today())){//履歴の開始日＞システム日付
			return;
		}
		//指定社員の中間データを作成する（日別）
		creDailyAppr.createDailyApprover(data.getEmployeeId(), RecordRootType.CONFIRM_WORK_BY_DAY, sDate, sDate);
		//指定社員の中間データを作成する（月別）
		creDailyAppr.createDailyApprover(data.getEmployeeId(), RecordRootType.CONFIRM_WORK_BY_MONTH, sDate, sDate);
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
	private void addApproval(CompanyAppRootADto commonRoot, String branchId){
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
			appPhaseN1.updateAppPhaseId(UUID.randomUUID().toString());
			listAppPhase.add(appPhaseN1);
		}
		if(appPhaseN2 != null){
			appPhaseN2.updateAppPhaseId(UUID.randomUUID().toString());
			listAppPhase.add(appPhaseN2);
		}
		if(appPhaseN3 != null){
			appPhaseN3.updateAppPhaseId(UUID.randomUUID().toString());
			listAppPhase.add(appPhaseN3);
		}
		if(appPhaseN4 != null){
			appPhaseN4.updateAppPhaseId(UUID.randomUUID().toString());
			listAppPhase.add(appPhaseN4);
		}
		if(appPhaseN5 != null){
			appPhaseN5.updateAppPhaseId(UUID.randomUUID().toString());
			listAppPhase.add(appPhaseN5);
		}
		for (ApprovalPhase approvalPhase : listAppPhase) {
			String approvalPhaseId = approvalPhase.getApprovalPhaseId();
			for (Approver approver : approvalPhase.getApprovers()) {
				approver.updateApprovalPhaseId(approvalPhaseId);
			}
			repoApprover.addAllApprover(approvalPhase.getApprovers());
		}
		repoAppPhase.addAllApprovalPhase(listAppPhase);
	}

	/**
	 * TH: update
	 * update root display in screen
	 */
	private void updateRoot(List<AppType> lstAppTypeUi, List<CompanyAppRootADto> root){
		//update root display in screen
		for (AppType type : lstAppTypeUi) {
			CompanyAppRootADto commonRoot = findRoot(root, type);
			String branchId = commonRoot.getBranchId();
			if(StringUtil.isNullOrEmpty(branchId, true)){
				continue;
			}
			//xoa app Phase
			deleteAppPh(branchId);
			ApprovalPhase appPhaseN1 = checkAppPh(commonRoot.getAppPhase1(), branchId);
			ApprovalPhase appPhaseN2 = checkAppPh(commonRoot.getAppPhase2(), branchId);
			ApprovalPhase appPhaseN3 = checkAppPh(commonRoot.getAppPhase3(), branchId);
			ApprovalPhase appPhaseN4 = checkAppPh(commonRoot.getAppPhase4(), branchId);
			ApprovalPhase appPhaseN5 = checkAppPh(commonRoot.getAppPhase5(), branchId);
			//Xu ly them,sua,xoa appPh and approver
			if(appPhaseN1 != null){
				addAppPhase(appPhaseN1,branchId);
			}
			if(appPhaseN2 != null && appPhaseN2.getApprovalForm().value != 0){
				addAppPhase(appPhaseN2,branchId);
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
		}
	}
	/**
	 * check AppPhase(add or not add)
	 * @param appPhase
	 * @param branchId
	 * @return
	 */
	private ApprovalPhase checkAppPh(ApprovalPhaseDto appPhase, String branchId){
		if(appPhase.getApprovalForm() == null || appPhase.getApprovalForm().intValue() == 0){//khong co
			return null;
		}
		List<Approver> lstApp = new ArrayList<>();
		String companyId = AppContexts.user().companyId();
		String approvalPhaseId = appPhase.getApprovalPhaseId();
		List<ApproverDto> approver = appPhase.getApprover();
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
	private CompanyAppRootADto findRoot(List<CompanyAppRootADto> root, AppType appTypeValue){
		for (CompanyAppRootADto companyAppRootADto : root) {
			if(companyAppRootADto.getAppTypeValue() == appTypeValue.getValue() && companyAppRootADto.getEmployRootAtr() == appTypeValue.getEmployRootAtr()){
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
