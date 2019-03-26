package nts.uk.ctx.workflow.dom.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import javax.ejb.Stateless;
import javax.inject.Inject;

import org.apache.logging.log4j.util.Strings;

import nts.arc.error.BusinessException;
import nts.arc.time.GeneralDate;
import nts.uk.ctx.workflow.dom.approvermanagement.workroot.ApprovalForm;
import nts.uk.ctx.workflow.dom.approvermanagement.workroot.ApprovalPhase;
import nts.uk.ctx.workflow.dom.approvermanagement.workroot.ApprovalPhaseRepository;
import nts.uk.ctx.workflow.dom.approvermanagement.workroot.Approver;
import nts.uk.ctx.workflow.dom.approvermanagement.workroot.ApproverRepository;
import nts.uk.ctx.workflow.dom.approvermanagement.workroot.PersonApprovalRoot;
import nts.uk.ctx.workflow.dom.approvermanagement.workroot.PersonApprovalRootRepository;
import nts.uk.ctx.workflow.dom.resultrecord.RecordRootType;
import nts.uk.ctx.workflow.dom.resultrecord.service.AppRootInstanceContent;
import nts.uk.ctx.workflow.dom.resultrecord.service.CreateDailyApprover;
import nts.uk.ctx.workflow.dom.service.output.ErrorFlag;

/**
 * @author sang.nv
 *
 */
@Stateless
public class UpdateHistoryCmm053Impl implements UpdateHistoryCmm053Service {

	@Inject
	private PersonApprovalRootRepository repoPerson;
	@Inject
	private InsertHistoryCmm053Service insertHistoryCmm053Service;
	@Inject
	private ApprovalPhaseRepository repoAppPhase;
	@Inject
	private ApproverRepository repoApprover;
	@Inject
	private CreateDailyApprover createDailyApprover;
	
	@Override
	//03.履歴更新を登録する
	public void updateHistoryByManagerSetting(String companyId, String historyId, String employeeId, GeneralDate startDate,
			String departmentApproverId, String dailyApproverId, boolean dailyDisplay) {
		String endDate    = "9999-12-31";
		Optional<PersonApprovalRoot> commonPs  = this.repoPerson.getNewestCommonPsAppRoot(companyId, employeeId);
		Optional<PersonApprovalRoot> monthlyPs = this.repoPerson.getNewestMonthlyPsAppRoot(companyId, employeeId);

		if (commonPs.isPresent() && monthlyPs.isPresent()) {
			// １．バラバラ履歴の場合: TH 2 loai deu co ls
			if (commonPs.get().getEmploymentAppHistoryItems().get(0).start()
					.compareTo(monthlyPs.get().getEmploymentAppHistoryItems().get(0).start()) != 0) {
				//TH 2 lich su khong giong nhau
				this.insertHistoryCmm053Service.updateOrInsertDiffStartDate(companyId, employeeId, historyId, startDate,
						endDate, commonPs, monthlyPs, departmentApproverId, dailyApproverId, dailyDisplay);
			} else{
				//TH 2 lich su giong nhau
				this.updateRootCMM053(companyId, departmentApproverId, dailyApproverId, commonPs.get(), monthlyPs.get(), dailyDisplay);
			}
		} else {
			// ２．一個履歴の場合: TH chi co 1 loai co lich su
			this.insertHistoryCmm053Service.updateOrInsertHistory(companyId, employeeId, historyId, startDate, endDate,
					commonPs, monthlyPs, departmentApproverId, dailyApproverId, dailyDisplay);
		}
		//履歴の開始日とシステム日付をチェックする
		GeneralDate systemDate = GeneralDate.today();
		if(startDate.beforeOrEquals(systemDate)){
			//指定社員の中間データを作成する（日別）
			AppRootInstanceContent result =  createDailyApprover.createDailyApprover(employeeId, RecordRootType.CONFIRM_WORK_BY_DAY, startDate, startDate);
			if(!result.getErrorFlag().equals(ErrorFlag.NO_ERROR)){
				throw new BusinessException(result.getErrorMsgID());
			}
			//指定社員の中間データを作成する（月別）
			result = createDailyApprover.createDailyApprover(employeeId, RecordRootType.CONFIRM_WORK_BY_MONTH, startDate, startDate);
			if(!result.getErrorFlag().equals(ErrorFlag.NO_ERROR)){
				throw new BusinessException(result.getErrorMsgID());
			}
		}
		
	}

	@Override
	public void updateApproverFirstPhase(String companyId, String employeeIdApprover, PersonApprovalRoot psAppRoot) {
		if(Strings.isNotBlank(employeeIdApprover)){
			Optional<ApprovalPhase> approvalPhase = this.repoAppPhase.getApprovalFirstPhase(companyId,
					psAppRoot.getBranchId());
			if (approvalPhase.isPresent()) {
				ApprovalPhase updateApprovalPhase = approvalPhase.get();
				List<Approver> approverOlds       = updateApprovalPhase.getApprovers();
				Optional<Approver> firstApprover  = approverOlds.stream().filter(x -> x.getOrderNumber() == 0).findFirst();
				if (firstApprover.isPresent()) {
					firstApprover.get().setEmployeeId(employeeIdApprover);
					this.repoApprover.updateEmployeeIdApprover(firstApprover.get());
				}
			}
		}
	}
	@Override
	public void updateRootCMM053(String companyId, String a27, String a210,PersonApprovalRoot commonRoot, PersonApprovalRoot monthlyRoot, boolean dailyDisplay) {
		List<Approver> lstApprNew = new ArrayList<>();
		if(commonRoot != null){
			Optional<ApprovalPhase> commonPhase = repoAppPhase.getApprovalFirstPhase(companyId, commonRoot.getBranchId());
			//common
			if(commonPhase.isPresent()){
				//承認フェーズ・承認形態　＝　誰か一人
				ApprovalPhase phase = commonPhase.get();
				if(phase.getApprovalForm().equals(ApprovalForm.EVERYONE_APPROVED)){
					phase.setApprovalForm(ApprovalForm.SINGLE_APPROVED);
					repoAppPhase.updateApprovalPhase(phase);
				}
				String phaseId = phase.getApprovalPhaseId();
				//delete approver old
				repoApprover.deleteAllApproverByAppPhId(companyId, phaseId);
				//insert approver moi
				String branchId = commonRoot.getBranchId();
				//common
				if(dailyDisplay){//common insert 2 record
					//a210
					lstApprNew.add(Approver.createSimpleFromJavaType(companyId,  branchId, phaseId, UUID.randomUUID().toString(), null, a210, 0, 0, 0));
					//a27
					lstApprNew.add(Approver.createSimpleFromJavaType(companyId,  branchId, phaseId, UUID.randomUUID().toString(), null, a27, 1, 0, 0));
				}else{//common insert 1 record
					lstApprNew.add(Approver.createSimpleFromJavaType(companyId,  branchId, phaseId, UUID.randomUUID().toString(), null, a27, 0, 0, 0));
				}
	 		}
		}
		if(monthlyRoot != null){
			//monthly A27
			Optional<ApprovalPhase> monthlyPhase = repoAppPhase.getApprovalFirstPhase(companyId, monthlyRoot.getBranchId());
			if(monthlyPhase.isPresent()){
				ApprovalPhase mphase = monthlyPhase.get();
				//承認フェーズ・承認形態　＝　誰か一人
				if(mphase.getApprovalForm().equals(ApprovalForm.EVERYONE_APPROVED)){
					mphase.setApprovalForm(ApprovalForm.SINGLE_APPROVED);
					repoAppPhase.updateApprovalPhase(mphase);
				}
				String mphaseId = mphase.getApprovalPhaseId();
				//delete approver old
				repoApprover.deleteAllApproverByAppPhId(companyId, mphaseId);
				lstApprNew.add(Approver.createSimpleFromJavaType(companyId,  monthlyRoot.getBranchId(), mphaseId, UUID.randomUUID().toString(), null, a27, 0, 0, 0));
			}
		}
		if(!lstApprNew.isEmpty()){
			repoApprover.addAllApprover(lstApprNew);
		}
	}
}
