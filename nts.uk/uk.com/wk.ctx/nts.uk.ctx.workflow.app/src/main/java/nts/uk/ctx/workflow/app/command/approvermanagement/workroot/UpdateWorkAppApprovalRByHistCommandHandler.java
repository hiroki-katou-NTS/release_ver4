package nts.uk.ctx.workflow.app.command.approvermanagement.workroot;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.arc.error.BusinessException;
import nts.arc.layer.app.command.CommandHandler;
import nts.arc.layer.app.command.CommandHandlerContext;
import nts.arc.time.GeneralDate;
import nts.uk.ctx.workflow.dom.approvermanagement.workroot.ApprovalBranchRepository;
import nts.uk.ctx.workflow.dom.approvermanagement.workroot.ApprovalPhase;
import nts.uk.ctx.workflow.dom.approvermanagement.workroot.ApprovalPhaseRepository;
import nts.uk.ctx.workflow.dom.approvermanagement.workroot.ApproverRepository;
import nts.uk.ctx.workflow.dom.approvermanagement.workroot.CompanyApprovalRoot;
import nts.uk.ctx.workflow.dom.approvermanagement.workroot.CompanyApprovalRootRepository;
import nts.uk.ctx.workflow.dom.approvermanagement.workroot.PersonApprovalRoot;
import nts.uk.ctx.workflow.dom.approvermanagement.workroot.PersonApprovalRootRepository;
import nts.uk.ctx.workflow.dom.approvermanagement.workroot.WorkplaceApprovalRoot;
import nts.uk.ctx.workflow.dom.approvermanagement.workroot.WorkplaceApprovalRootRepository;
import nts.uk.ctx.workflow.dom.resultrecord.RecordRootType;
import nts.uk.ctx.workflow.dom.resultrecord.service.CreateDailyApprover;
import nts.uk.shr.com.context.AppContexts;
/**
 * 
 * @author hoatt
 *
 */
@Stateless
public class UpdateWorkAppApprovalRByHistCommandHandler extends CommandHandler<UpdateWorkAppApprovalRByHistCommand>{
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
	private CreateDailyApprover creDailyAppr;
	private static final int COMPANY = 0;
	private static final int WORKPLACE = 1;
	private static final int EDIT = 1;
	private static final int COMMON = 0;
	private static final int PRIVATE = 1;
	@Override
	protected void handle(CommandHandlerContext<UpdateWorkAppApprovalRByHistCommand> context) {
		UpdateWorkAppApprovalRByHistCommand  objUpdateItem = context.getCommand();
		//TH: company - domain 会社別就業承認ルート
		if(objUpdateItem.getCheck() == COMPANY){
			this.updateHistoryCom(objUpdateItem);
		}
		//TH: work place - domain 職場別就業承認ルート
		else if(objUpdateItem.getCheck() == WORKPLACE){
			this.updateHistoryWorkplace(objUpdateItem);
		}
		//TH: person - domain 個人別就業承認ルート
		else{
			this.updateHistoryPerson(objUpdateItem);
		}
	}
	/**
	 * edit history
	 * domain 会社別就業承認ルート (Company)
	 * @param objUpdateItem
	 */
	private void updateHistoryCom(UpdateWorkAppApprovalRByHistCommand  objUpdateItem){
		String companyId = AppContexts.user().companyId();
		List<UpdateHistoryDto> lstHist = objUpdateItem.getLstUpdate();
		//history current
		String startDate = objUpdateItem.getStartDate();
		GeneralDate sDate = GeneralDate.fromString(startDate, "yyyy/MM/dd");
		GeneralDate eDate = sDate.addDays(-1);
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		String endDateUpdate = eDate.localDate().format(formatter);//Edate: edit
		String endDateDelete = "9999-12-31";//Edate: delete
		//history previous
		String startDatePrevious = objUpdateItem.getStartDatePrevious();
		GeneralDate sDatePrevious = GeneralDate.localDate(LocalDate.parse(startDatePrevious.replace("/","-")));
		GeneralDate eDatePrevious = sDatePrevious.addDays(-1);//Edate to find history Previous
		for (UpdateHistoryDto updateItem : lstHist) {
			//find history by type and EmployRootAtr
			List<CompanyApprovalRoot> lstComByApp = repoCom.getComApprovalRootByType(companyId, updateItem.getApplicationType(), updateItem.getEmployRootAtr());
			Optional<CompanyApprovalRoot> comAppRootDb = repoCom.getComApprovalRoot(companyId, updateItem.getApprovalId(), updateItem.getHistoryId());
			if(!comAppRootDb.isPresent()){
				continue;
			}
			//item update (New)
			CompanyApprovalRoot comAppRoot = CompanyApprovalRoot.updateSdate(comAppRootDb.get(), startDate.replace("/","-"));
			if(lstComByApp.size() < 2){// history previous is not exist
				if(objUpdateItem.getEditOrDelete()==1){//TH: edit
					repoCom.updateComApprovalRoot(comAppRoot);
				}else{//TH: delete
					//get all  ApprovalPhase by BranchId
					List<ApprovalPhase> lstAPhase = repoAppPhase.getAllApprovalPhasebyCode(companyId, comAppRoot.getBranchId());
					for (ApprovalPhase approvalPhase : lstAPhase) {
						//delete All Approver By Approval Phase Id
						repoApprover.deleteAllApproverByAppPhId(companyId, approvalPhase.getApprovalPhaseId());
					}
					//delete All Approval Phase By Branch Id
					repoAppPhase.deleteAllAppPhaseByBranchId(companyId, comAppRoot.getBranchId());
					//delete ComApprovalRoot
					repoCom.deleteComApprovalRoot(companyId, updateItem.getApprovalId(), updateItem.getHistoryId());
					//delete branch
					repoBranch.deleteBranch(companyId, comAppRoot.getBranchId());
				}
			}else{// history previous is exist
				if(objUpdateItem.getEditOrDelete( )== EDIT){//edit
					CompanyApprovalRoot com = lstComByApp.get(1);
					String sDatePre = com.getEmploymentAppHistoryItems().get(0).start().toString();
					//check 編集後の履歴の開始年月日 > 取得した履歴の開始年月日 が falseの場合
					if(startDate.compareTo(sDatePre) <= 0){
						//エラーメッセージ(Msg_156)(error message (Msg_156))
						throw new BusinessException("Msg_156",sDatePre);
					}
//					//history previous 
//					CompanyApprovalRoot comAppRootUpdate= CompanyApprovalRoot.updateEdate(com, endDateUpdate);
//					//update history previous
//					repoCom.updateComApprovalRoot(comAppRootUpdate);
					//update history current
					repoCom.updateComApprovalRoot(comAppRoot);
				}else{//delete
					//find history previous
//				    List<CompanyApprovalRoot> lstOld= repoCom.getComApprovalRootByEdate(companyId, eDatePrevious, comAppRoot.getApplicationType()== null ? null : comAppRoot.getApplicationType().value, comAppRoot.getEmploymentRootAtr().value);
//				    if(!lstOld.isEmpty()){
//				    	CompanyApprovalRoot comold = lstOld.get(0);
//					    CompanyApprovalRoot comAppRootUpdate = CompanyApprovalRoot.updateEdate(comold, endDateDelete);
//						//update history previous
//						repoCom.updateComApprovalRoot(comAppRootUpdate);
//				    }
					//get all  ApprovalPhase by BranchId
					List<ApprovalPhase> lstAPhase = repoAppPhase.getAllApprovalPhasebyCode(companyId, comAppRoot.getBranchId());
					//check: if data(lstAPhase) > 0: delete
					if(!lstAPhase.isEmpty()){
						for (ApprovalPhase approvalPhase : lstAPhase) {
							//delete All Approver By Approval Phase Id
							repoApprover.deleteAllApproverByAppPhId(companyId, approvalPhase.getApprovalPhaseId());
						}
						//delete All Approval Phase By Branch Id
						repoAppPhase.deleteAllAppPhaseByBranchId(companyId, comAppRoot.getBranchId());
					}
					//delete history current
					repoCom.deleteComApprovalRoot(companyId, updateItem.getApprovalId(), updateItem.getHistoryId());
					//delete branch
					repoBranch.deleteBranch(companyId, comAppRoot.getBranchId());
				}
			}
		}
		if(objUpdateItem.getCheckMode() == 0){
			// xu li mode chung
			List<CompanyApprovalRoot> lstComByApp = repoCom.getComAppRootLast(companyId, eDatePrevious);
			
				if(objUpdateItem.getEditOrDelete( )== EDIT){
					for(CompanyApprovalRoot appRoot : lstComByApp){
						//history previous 
						CompanyApprovalRoot comAppRootUpdate= CompanyApprovalRoot.updateEdate(appRoot, endDateUpdate);
						//update history previous
						repoCom.updateComApprovalRoot(comAppRootUpdate);
					}
				}else{
					for(CompanyApprovalRoot appRoot : lstComByApp){
						//history previous 
						CompanyApprovalRoot comAppRootUpdate= CompanyApprovalRoot.updateEdate(appRoot, endDateDelete);
						//update history previous
						repoCom.updateComApprovalRoot(comAppRootUpdate);
					}
				}
		}else{
			// xu li mode rieng
			List<CompanyApprovalRoot> lstComByApp = repoCom.getComApprovalRootByType(companyId, lstHist.get(0).getApplicationType(), lstHist.get(0).getEmployRootAtr());
			if(objUpdateItem.getEditOrDelete( )== EDIT){
				if(!lstComByApp.isEmpty() && lstComByApp.size() > 1){
					//history previous 
					CompanyApprovalRoot comAppRootUpdate= CompanyApprovalRoot.updateEdate(lstComByApp.get(1), endDateUpdate);
					//update history previous
					repoCom.updateComApprovalRoot(comAppRootUpdate);
				}
			}else{
				//delete
				if(!lstComByApp.isEmpty()){
					//history previous 
					CompanyApprovalRoot comAppRootUpdate= CompanyApprovalRoot.updateEdate(lstComByApp.get(0), endDateDelete);
					//update history previous
					repoCom.updateComApprovalRoot(comAppRootUpdate);
				}
				
			}
			
		}
	}
		
	/**
	 * edit history
	 * domain 職場別就業承認ルート (Work place)
	 * @param objUpdateItem
	 */
	private void updateHistoryWorkplace(UpdateWorkAppApprovalRByHistCommand  objUpdateItem){
		String companyId = AppContexts.user().companyId();
		List<UpdateHistoryDto> lstHist = objUpdateItem.getLstUpdate();
		//history current
		String startDate = objUpdateItem.getStartDate();
		GeneralDate sDate = GeneralDate.fromString(startDate, "yyyy/MM/dd");
		GeneralDate eDate = sDate.addDays(-1);
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		String endDateUpdate = eDate.localDate().format(formatter);//Edate: edit
		String endDateDelete = "9999-12-31";//Edate: delete
		//history previous
		String startDatePrevious = objUpdateItem.getStartDatePrevious();
		GeneralDate sDatePrevious = GeneralDate.localDate(LocalDate.parse(startDatePrevious.replace("/","-")));
		GeneralDate eDatePrevious = sDatePrevious.addDays(-1);//Edate to find history Previous
		for (UpdateHistoryDto updateItem : lstHist) {
			//find history by type and 
			List<WorkplaceApprovalRoot> lstWpByApp = repoWorkplace.getWpApprovalRootByType(companyId, objUpdateItem.getWorkplaceId(), updateItem.getApplicationType(), updateItem.getEmployRootAtr());
			//find history current
			Optional<WorkplaceApprovalRoot> wpAppRootDb = repoWorkplace.getWpApprovalRoot(companyId, updateItem.getApprovalId(), objUpdateItem.getWorkplaceId(), updateItem.getHistoryId());
			if(!wpAppRootDb.isPresent()){
				continue;
			}
			WorkplaceApprovalRoot wpAppRoot = WorkplaceApprovalRoot.updateSdate(wpAppRootDb.get(), startDate.replace("/","-"));
			if(lstWpByApp.size() < 2){// history previous is not exist
				if(objUpdateItem.getEditOrDelete() == EDIT){//TH: edit
					repoWorkplace.updateWpApprovalRoot(wpAppRoot);
				}else{//TH: delete
					//get all  ApprovalPhase by BranchId
					List<ApprovalPhase> lstAPhase = repoAppPhase.getAllApprovalPhasebyCode(companyId, wpAppRoot.getBranchId());
					//check: if data(lstAPhase) > 0: delete
					if(!lstAPhase.isEmpty()){
						for (ApprovalPhase approvalPhase : lstAPhase) {
							//delete All Approver By Approval Phase Id
							repoApprover.deleteAllApproverByAppPhId(companyId, approvalPhase.getApprovalPhaseId());
						}
						//delete All Approval Phase By Branch Id
						repoAppPhase.deleteAllAppPhaseByBranchId(companyId, wpAppRoot.getBranchId());
					}
					//delete WpApprovalRoot
					repoWorkplace.deleteWpApprovalRoot(companyId, updateItem.getApprovalId(), wpAppRoot.getWorkplaceId(), updateItem.getHistoryId());
					//delete branch
					repoBranch.deleteBranch(companyId, wpAppRoot.getBranchId());
				}
			}else{// history previous is exist
				if(objUpdateItem.getEditOrDelete() == EDIT){//edit
					//history previous
					WorkplaceApprovalRoot wp = lstWpByApp.get(1);
					String sDatePre = wp.getEmploymentAppHistoryItems().get(0).start().toString();
					//check 編集後の履歴の開始年月日 > 取得した履歴の開始年月日 が falseの場合
					if(startDate.compareTo(sDatePre) <= 0){
						//エラーメッセージ(Msg_156)(error message (Msg_156))
						throw new BusinessException("Msg_156",sDatePre);
					}
					//history previous 
//					WorkplaceApprovalRoot wpAppRootUpdate = WorkplaceApprovalRoot.updateEdate(wp, endDateUpdate);
//					//update history previous
//					repoWorkplace.updateWpApprovalRoot(wpAppRootUpdate);
					//update history current
					repoWorkplace.updateWpApprovalRoot(wpAppRoot);
				}else{//delete 
					//find history previous (lien ke)
//					List<WorkplaceApprovalRoot> lstOld= repoWorkplace.getWpApprovalRootByEdate(companyId, wpAppRoot.getWorkplaceId(), eDatePrevious, wpAppRoot.getApplicationType()== null ? null : wpAppRoot.getApplicationType().value, wpAppRoot.getEmploymentRootAtr().value);
//					if(!lstOld.isEmpty()){
//						//history previous
//						WorkplaceApprovalRoot wpOld = lstOld.get(0);
//						WorkplaceApprovalRoot wpAppRootUpdate = WorkplaceApprovalRoot.updateEdate(wpOld, endDateDelete);
//						//update history previous
//						repoWorkplace.updateWpApprovalRoot(wpAppRootUpdate);
//					}
					//get all  ApprovalPhase by BranchId
					List<ApprovalPhase> lstAPhase = repoAppPhase.getAllApprovalPhasebyCode(companyId, wpAppRoot.getBranchId());
					//check: if data(lstAPhase) > 0: delete
					if(!lstAPhase.isEmpty()){
						for (ApprovalPhase approvalPhase : lstAPhase) {
							//delete All Approver By Approval Phase Id
							repoApprover.deleteAllApproverByAppPhId(companyId, approvalPhase.getApprovalPhaseId());
						}
						//delete All Approval Phase By Branch Id
						repoAppPhase.deleteAllAppPhaseByBranchId(companyId, wpAppRoot.getBranchId());
					}
					//delete history current
					repoWorkplace.deleteWpApprovalRoot(companyId, updateItem.getApprovalId(), wpAppRoot.getWorkplaceId(), updateItem.getHistoryId());
					//delete branch
					repoBranch.deleteBranch(companyId, wpAppRoot.getBranchId());
				}
			}
		}
		if(objUpdateItem.getCheckMode() == 0){
			// xu li mode chung
			List<WorkplaceApprovalRoot> lstWpByApp = repoWorkplace.getWpAppRootLast(companyId, objUpdateItem.getWorkplaceId(), eDatePrevious);
			
				if(objUpdateItem.getEditOrDelete( )== EDIT){
					for(WorkplaceApprovalRoot appRoot : lstWpByApp){
						//history previous 
						WorkplaceApprovalRoot wpAppRootUpdate = WorkplaceApprovalRoot.updateEdate(appRoot, endDateUpdate);
						//update history previous
						repoWorkplace.updateWpApprovalRoot(wpAppRootUpdate);
					}
				}else{
					for(WorkplaceApprovalRoot appRoot : lstWpByApp){
						//history previous 
						WorkplaceApprovalRoot wpAppRootUpdate= WorkplaceApprovalRoot.updateEdate(appRoot, endDateDelete);
						//update history previous
						repoWorkplace.updateWpApprovalRoot(wpAppRootUpdate);
					}
				}
		}else{
			//find history by type and 
			List<WorkplaceApprovalRoot> lstWpByApp = repoWorkplace.getWpApprovalRootByType(companyId, objUpdateItem.getWorkplaceId(), lstHist.get(0).getApplicationType(), lstHist.get(0).getEmployRootAtr());
			if(objUpdateItem.getEditOrDelete( )== EDIT){
				if(!lstWpByApp.isEmpty() && lstWpByApp.size() > 1){
					//history previous 
					WorkplaceApprovalRoot wpAppRootUpdate = WorkplaceApprovalRoot.updateEdate(lstWpByApp.get(1), endDateUpdate);
					//update history previous
					repoWorkplace.updateWpApprovalRoot(wpAppRootUpdate);
				}
			}else{
				//delete
				if(!lstWpByApp.isEmpty()){
					//history previous 
					WorkplaceApprovalRoot wpAppRootUpdate = WorkplaceApprovalRoot.updateEdate(lstWpByApp.get(0), endDateDelete);
					//update history previous
					repoWorkplace.updateWpApprovalRoot(wpAppRootUpdate);
				}
				
			}
			
		}
	}
	/**
	 * edit history
	 * domain 個人別就業承認ルート (Person)
	 * @param objUpdateItem
	 */
	private void updateHistoryPerson(UpdateWorkAppApprovalRByHistCommand  objUpdateItem){
		String companyId = AppContexts.user().companyId();
		List<UpdateHistoryDto> lstHist = objUpdateItem.getLstUpdate();
		//history current
		String startDate = objUpdateItem.getStartDate();
		GeneralDate sDate = GeneralDate.fromString(startDate, "yyyy/MM/dd");
		GeneralDate eDate = sDate.addDays(-1);
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		String endDateUpdate = eDate.localDate().format(formatter);//Edate: edit
		String endDateDelete = "9999-12-31";//Edate: delete
		//history previous
		String startDatePrevious = objUpdateItem.getStartDatePrevious();
		GeneralDate sDatePrevious = GeneralDate.localDate(LocalDate.parse(startDatePrevious.replace("/","-")));
		GeneralDate eDatePrevious = sDatePrevious.addDays(-1);//Edate to find history Previous
		String employeeId = objUpdateItem.getEmployeeId();
		GeneralDate dateLastest = null;
		//ドメインモデル「就業承認ルート履歴」を取得する
		if(objUpdateItem.getEditOrDelete() == EDIT && objUpdateItem.getCheckMode() == COMMON){//07.履歴編集を実行する(まとめて設定モード)
			Optional<PersonApprovalRoot> histL = repoPerson.getHistLastestCom(companyId, employeeId);
			if(histL.isPresent()) dateLastest = histL.get().getEmploymentAppHistoryItems().get(0).getDatePeriod().start();
		}
		if(objUpdateItem.getEditOrDelete() == EDIT && objUpdateItem.getCheckMode() == PRIVATE){//10.履歴編集を実行する(申請個別設定モード)
			Integer employmentRootAtr = objUpdateItem.getLstUpdate().get(0).getEmployRootAtr();
			Integer applicationType = objUpdateItem.getLstUpdate().get(0).getApplicationType();
			Optional<PersonApprovalRoot> histL = repoPerson.getHistLastestPri(companyId, employeeId, employmentRootAtr, applicationType);
			if(histL.isPresent()) dateLastest = histL.get().getEmploymentAppHistoryItems().get(0).getDatePeriod().start();
		}
		for (UpdateHistoryDto updateItem : lstHist) {
			//find history by type and 
			List<PersonApprovalRoot> lstPsByApp = repoPerson.getPsApprovalRootByType(companyId, objUpdateItem.getEmployeeId(), updateItem.getApplicationType(), updateItem.getEmployRootAtr());
			//find history current
			Optional<PersonApprovalRoot> psAppRootDb = repoPerson.getPsApprovalRoot(companyId, updateItem.getApprovalId(), objUpdateItem.getEmployeeId(), updateItem.getHistoryId());
			if(!psAppRootDb.isPresent()){
				continue;
			}
			PersonApprovalRoot psAppRoot = PersonApprovalRoot.updateSdate(psAppRootDb.get(), startDate.replace("/","-"));
			if(lstPsByApp.size() < 2){// history previous is not exist
				if(objUpdateItem.getEditOrDelete() == EDIT){//TH: edit hist (J)
					repoPerson.updatePsApprovalRoot(psAppRoot);
				}else{//TH: delete hist (J)
					//get all  ApprovalPhase by BranchId
					List<ApprovalPhase> lstAPhase = repoAppPhase.getAllApprovalPhasebyCode(companyId, psAppRoot.getBranchId());
					//check: if data(lstAPhase) > 0: delete
					if(!lstAPhase.isEmpty()){
						for (ApprovalPhase approvalPhase : lstAPhase) {
							//delete All Approver By Approval Phase Id
							repoApprover.deleteAllApproverByAppPhId(companyId, approvalPhase.getApprovalPhaseId());
						}
						//delete All Approval Phase By Branch Id
						repoAppPhase.deleteAllAppPhaseByBranchId(companyId, psAppRoot.getBranchId());
					}
					//delete PsApprovalRoot
					repoPerson.deletePsApprovalRoot(companyId, updateItem.getApprovalId(), psAppRoot.getEmployeeId(), updateItem.getHistoryId());
					//delete branch
					repoBranch.deleteBranch(companyId, psAppRoot.getBranchId());
				}
			}else{// history previous is exist
				if(objUpdateItem.getEditOrDelete() == EDIT){//edit
					//history previous
					PersonApprovalRoot ps = lstPsByApp.get(1);
					String sDatePre = ps.getEmploymentAppHistoryItems().get(0).start().toString();
					//check 編集後の履歴の開始年月日 > 取得した履歴の開始年月日 が falseの場合
					if(startDate.compareTo(sDatePre) <= 0){
						//エラーメッセージ(Msg_156)(error message (Msg_156))
						throw new BusinessException("Msg_156",sDatePre);
					}
					//update history current
					repoPerson.updatePsApprovalRoot(psAppRoot);
				}else{//delete 
					//get all  ApprovalPhase by BranchId
					List<ApprovalPhase> lstAPhase = repoAppPhase.getAllApprovalPhasebyCode(companyId, psAppRoot.getBranchId());
					//check: if data(lstAPhase) > 0: delete
					if(!lstAPhase.isEmpty()){
						for (ApprovalPhase approvalPhase : lstAPhase) {
							//delete All Approver By Approval Phase Id
							repoApprover.deleteAllApproverByAppPhId(companyId, approvalPhase.getApprovalPhaseId());
						}
						//delete All Approval Phase By Branch Id
						repoAppPhase.deleteAllAppPhaseByBranchId(companyId, psAppRoot.getBranchId());
					}
					//delete history current
					repoPerson.deletePsApprovalRoot(companyId, updateItem.getApprovalId(), psAppRoot.getEmployeeId(),  psAppRoot.getEmploymentAppHistoryItems().get(0).getHistoryId());
					//delete branch
					repoBranch.deleteBranch(companyId, psAppRoot.getBranchId());
				}
			}
		}
		//xu li update hist old
		if(objUpdateItem.getCheckMode() == 0){
			// xu li mode chung
			List<PersonApprovalRoot> lstPsByApp = repoPerson.getPsAppRootLastest(companyId,  objUpdateItem.getEmployeeId(), eDatePrevious);
			
				if(objUpdateItem.getEditOrDelete( )== EDIT){
					for(PersonApprovalRoot appRoot : lstPsByApp){
						//history previous 
						PersonApprovalRoot psAppRootUpdate= PersonApprovalRoot.updateEdate(appRoot,  endDateUpdate);
						//update history previous
						repoPerson.updatePsApprovalRoot(psAppRootUpdate);
					}
				}else{
					for(PersonApprovalRoot appRoot : lstPsByApp){
						PersonApprovalRoot psAppRootUpdate= PersonApprovalRoot.updateEdate(appRoot, endDateDelete);
						//update history previous
						repoPerson.updatePsApprovalRoot(psAppRootUpdate);
					}
				}
		}else{
			//find history by type and 
			List<PersonApprovalRoot> lstPsByApp = repoPerson.getPsApprovalRootByType(companyId, objUpdateItem.getEmployeeId(), lstHist.get(0).getApplicationType(), lstHist.get(0).getEmployRootAtr());
			if(objUpdateItem.getEditOrDelete( )== EDIT){
				if(!lstPsByApp.isEmpty() && lstPsByApp.size() > 1){
					//history previous 
					PersonApprovalRoot psAppRootUpdate= PersonApprovalRoot.updateEdate(lstPsByApp.get(1), endDateUpdate);
					//update history previous
					repoPerson.updatePsApprovalRoot(psAppRootUpdate);
				}
			}else{
				//delete
				if(!lstPsByApp.isEmpty()){
					//history previous 
					PersonApprovalRoot psAppRootUpdate= PersonApprovalRoot.updateEdate(lstPsByApp.get(0), endDateDelete);
					//update history previous
					repoPerson.updatePsApprovalRoot(psAppRootUpdate);
				}
				
			}
			
		}
		//EA修正履歴 No.3268
		//EA修正履歴 No.3271
		//EA修正履歴 No.3273
		//EA修正履歴 No.3274
		//EA修正履歴 No.3290
		if(objUpdateItem.getEditOrDelete() == EDIT){//Edit
			//履歴の開始日とシステム日付をチェックする
			if(sDate.after(GeneralDate.today()) || dateLastest == null){//履歴の開始日＞システム日付
				return;
			}
			if(dateLastest.before(sDate)){//取得した開始日(1)＜新しい履歴の開始日
				//指定社員の中間データを作成する（日別）
				creDailyAppr.createDailyApprover(objUpdateItem.getEmployeeId(), RecordRootType.CONFIRM_WORK_BY_DAY, dateLastest, dateLastest);
				//指定社員の中間データを作成する（月別）
				creDailyAppr.createDailyApprover(objUpdateItem.getEmployeeId(), RecordRootType.CONFIRM_WORK_BY_MONTH, dateLastest, dateLastest);
			}
		}
		//指定社員の中間データを作成する（日別）
		creDailyAppr.createDailyApprover(objUpdateItem.getEmployeeId(), RecordRootType.CONFIRM_WORK_BY_DAY, sDate, sDate);
		//指定社員の中間データを作成する（月別）
		creDailyAppr.createDailyApprover(objUpdateItem.getEmployeeId(), RecordRootType.CONFIRM_WORK_BY_MONTH, sDate, sDate);
	}
	/**
	 * check 編集後の履歴の開始年月日 > 取得した履歴の開始年月日 が falseの場合
	 * @param sDatePre
	 * @param sDateCur
	 * @return
	 */
	public boolean checkStartDate(String sDatePre, String sDateCur){
		if(sDateCur.compareTo(sDatePre)>0){
			return true;
		}
		return false;
	}
}
