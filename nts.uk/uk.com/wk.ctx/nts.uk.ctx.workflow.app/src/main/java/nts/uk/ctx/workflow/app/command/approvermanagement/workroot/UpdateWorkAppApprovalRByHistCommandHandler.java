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
import nts.uk.ctx.workflow.dom.approvermanagement.workroot.ApprovalPhase;
import nts.uk.ctx.workflow.dom.approvermanagement.workroot.ApprovalPhaseRepository;
import nts.uk.ctx.workflow.dom.approvermanagement.workroot.ApproverRepository;
import nts.uk.ctx.workflow.dom.approvermanagement.workroot.CompanyApprovalRoot;
import nts.uk.ctx.workflow.dom.approvermanagement.workroot.CompanyApprovalRootRepository;
import nts.uk.ctx.workflow.dom.approvermanagement.workroot.PersonApprovalRoot;
import nts.uk.ctx.workflow.dom.approvermanagement.workroot.PersonApprovalRootRepository;
import nts.uk.ctx.workflow.dom.approvermanagement.workroot.WorkplaceApprovalRoot;
import nts.uk.ctx.workflow.dom.approvermanagement.workroot.WorkplaceApprovalRootRepository;
import nts.uk.shr.com.context.AppContexts;
/**
 * 
 * @author hoatt
 *
 */
@Stateless
public class UpdateWorkAppApprovalRByHistCommandHandler extends CommandHandler<UpdateWorkAppApprovalRByHistCommand>{
	@Inject
	private PersonApprovalRootRepository repo;
	@Inject
	private CompanyApprovalRootRepository repoCom;
	@Inject
	private WorkplaceApprovalRootRepository repoWorkplace;
	@Inject
	private ApprovalPhaseRepository repoAppPhase;
	@Inject
	private ApproverRepository repoApprover;
	
	@Override
	protected void handle(CommandHandlerContext<UpdateWorkAppApprovalRByHistCommand> context) {
		String companyId = AppContexts.user().companyId();
		UpdateWorkAppApprovalRByHistCommand  objUpdateItem = context.getCommand();
		List<UpdateHistoryDto> lstHist = objUpdateItem.getLstUpdate();
		//history current
		String startDate = objUpdateItem.getStartDate();
		GeneralDate sDate = GeneralDate.fromString(startDate, "yyyy-MM-dd");
		GeneralDate eDate = sDate.addDays(-1);
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		String endDateUpdate = eDate.localDate().format(formatter);//Edate: edit
		//history previous
		String startDatePrevious = objUpdateItem.getStartDatePrevious();
		GeneralDate sDatePrevious = GeneralDate.localDate(LocalDate.parse(startDatePrevious));
		GeneralDate eDatePrevious = sDatePrevious.addDays(-1);//Edate to find history Previous 
		String endDateDelete = "9999-12-31";//Edate: delete
		//** For
		for (UpdateHistoryDto updateItem : lstHist) {
			//TH: company - domain 会社別就業承認ルート
			if(objUpdateItem.getCheck()==1){
				Optional<CompanyApprovalRoot> comAppRootDb = repoCom.getComApprovalRoot(companyId, updateItem.getApprovalId(), updateItem.getHistoryId());
				if(!comAppRootDb.isPresent()){
					continue;
				}
				//item update
				CompanyApprovalRoot comAppRoot = CompanyApprovalRoot.updateSdateEdate(comAppRootDb.get(), objUpdateItem.getStartDate(), objUpdateItem.getEndDate());
				//find history previous
				List<CompanyApprovalRoot> lstOld= repoCom.getComApprovalRootByEdate(companyId, eDatePrevious, comAppRoot.getApplicationType()== null ? null : comAppRoot.getApplicationType().value);
				if(lstOld.isEmpty()){// history previous is not exist
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
					}
				}else{// history previous is exist
					CompanyApprovalRoot com = lstOld.get(0);
					//check 編集後の履歴の開始年月日 > 取得した履歴の開始年月日 が falseの場合
					if(!checkStartDate(com.getPeriod().getStartDate().toString(),objUpdateItem.getStartDate())){
						throw new BusinessException("Msg_156");
					}
					if(objUpdateItem.getEditOrDelete()==1){//edit
						//history previous 
						String startDateUpdate = com.getPeriod().getStartDate().localDate().format(formatter);
						CompanyApprovalRoot comAppRootUpdate= CompanyApprovalRoot.updateSdateEdate(com, startDateUpdate, endDateUpdate);
						//update history previous
						repoCom.updateComApprovalRoot(comAppRootUpdate);
						//update history current
						repoCom.updateComApprovalRoot(comAppRoot);
					}else{//delete 
						String startDateUpdate = com.getPeriod().getStartDate().localDate().format(formatter);
						CompanyApprovalRoot comAppRootUpdate = CompanyApprovalRoot.updateSdateEdate(com, startDateUpdate, endDateDelete);
						//update history previous
						repoCom.updateComApprovalRoot(comAppRootUpdate);
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
					}
				}
			}
			//TH: workplace - domain 職場別就業承認ルート
			if(objUpdateItem.getCheck()==2){
				Optional<WorkplaceApprovalRoot> wpAppRootDb = repoWorkplace.getWpApprovalRoot(companyId, updateItem.getApprovalId(), objUpdateItem.getWorkplaceId(), updateItem.getHistoryId());
				if(!wpAppRootDb.isPresent()){
					continue;
				}
				WorkplaceApprovalRoot wpAppRoot = WorkplaceApprovalRoot.updateSdateEdate(wpAppRootDb.get(), objUpdateItem.getStartDate(), objUpdateItem.getEndDate());
				//find history previous
				List<WorkplaceApprovalRoot> lstOld= repoWorkplace.getWpApprovalRootByEdate(companyId, wpAppRoot.getWorkplaceId(), eDatePrevious, wpAppRoot.getApplicationType() == null ? null : wpAppRoot.getApplicationType().value);
				if(lstOld.isEmpty()){// history previous is not exist
					if(objUpdateItem.getEditOrDelete()==1){//TH: edit
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
					}
				}else{// history previous is exist
					WorkplaceApprovalRoot wp = lstOld.get(0);
					//check 編集後の履歴の開始年月日 > 取得した履歴の開始年月日 が falseの場合
					if(!checkStartDate(wp.getPeriod().getStartDate().toString(),objUpdateItem.getStartDate())){
						throw new BusinessException("Msg_156");
					}
					if(objUpdateItem.getEditOrDelete()==1){//edit
						//history previous 
						String startDateUpdate = wp.getPeriod().getStartDate().localDate().format(formatter);
						WorkplaceApprovalRoot wpAppRootUpdate = WorkplaceApprovalRoot.updateSdateEdate(wp, startDateUpdate, endDateUpdate);
						//update history previous
						repoWorkplace.updateWpApprovalRoot(wpAppRootUpdate);
						//update history current
						repoWorkplace.updateWpApprovalRoot(wpAppRoot);
					}else{//delete 
						String startDateUpdate = wp.getPeriod().getStartDate().localDate().format(formatter);
						WorkplaceApprovalRoot wpAppRootUpdate = WorkplaceApprovalRoot.updateSdateEdate(wp, startDateUpdate, endDateDelete);
						//update history previous
						repoWorkplace.updateWpApprovalRoot(wpAppRootUpdate);
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
					}
				}
			}
			//TH: person - domain 個人別就業承認ルート
			if(objUpdateItem.getCheck()==3){
				Optional<PersonApprovalRoot> psAppRootDb = repo.getPsApprovalRoot(companyId, updateItem.getApprovalId(), objUpdateItem.getEmployeeId(), updateItem.getHistoryId());
				if(!psAppRootDb.isPresent()){
					continue;
				}
				PersonApprovalRoot psAppRoot = PersonApprovalRoot.updateSdateEdate(psAppRootDb.get(), objUpdateItem.getStartDate(), objUpdateItem.getEndDate());
				//find history previous
				List<PersonApprovalRoot> lstOld= repo.getPsApprovalRootByEdate(companyId, psAppRoot.getEmployeeId(),  eDatePrevious, psAppRoot.getApplicationType() == null ? null : psAppRoot.getApplicationType().value);
				if(lstOld.isEmpty()){// history previous is not exist
					if(objUpdateItem.getEditOrDelete()==1){//TH: edit
						repo.updatePsApprovalRoot(psAppRoot);
					}else{//TH: delete
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
						repo.deletePsApprovalRoot(companyId, updateItem.getApprovalId(), psAppRoot.getEmployeeId(), updateItem.getHistoryId());
					}
				}else{// history previous is exist
					PersonApprovalRoot ps = lstOld.get(0);
					//check 編集後の履歴の開始年月日 > 取得した履歴の開始年月日 が falseの場合
					if(!checkStartDate(ps.getPeriod().getStartDate().toString(),objUpdateItem.getStartDate())){
						throw new BusinessException("Msg_156");
					}
					if(objUpdateItem.getEditOrDelete()==1){//edit
						//history previous 
						String startDateUpdate = ps.getPeriod().getStartDate().localDate().format(formatter);
						PersonApprovalRoot psAppRootUpdate= PersonApprovalRoot.updateSdateEdate(ps, startDateUpdate, endDateUpdate);
						//update history previous
						repo.updatePsApprovalRoot(psAppRootUpdate);
						//update history current
						repo.updatePsApprovalRoot(psAppRoot);
					}else{//delete 
						String startDateUpdate = ps.getPeriod().getStartDate().localDate().format(formatter);
						PersonApprovalRoot psAppRootUpdate= PersonApprovalRoot.updateSdateEdate(ps, startDateUpdate, endDateDelete);
						//update history previous
						repo.updatePsApprovalRoot(psAppRootUpdate);
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
						repo.deletePsApprovalRoot(companyId, updateItem.getApprovalId(), psAppRoot.getEmployeeId(),  psAppRoot.getHistoryId());
					}
				}
			}
		}
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
