package nts.uk.ctx.workflow.dom.service;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.arc.time.GeneralDate;
import nts.uk.ctx.workflow.dom.approvermanagement.workroot.ApprovalBranchRepository;
import nts.uk.ctx.workflow.dom.approvermanagement.workroot.ApprovalPhase;
import nts.uk.ctx.workflow.dom.approvermanagement.workroot.ApprovalPhaseRepository;
import nts.uk.ctx.workflow.dom.approvermanagement.workroot.ApproverRepository;
import nts.uk.ctx.workflow.dom.approvermanagement.workroot.PersonApprovalRoot;
import nts.uk.ctx.workflow.dom.approvermanagement.workroot.PersonApprovalRootRepository;
import nts.uk.ctx.workflow.dom.resultrecord.RecordRootType;
import nts.uk.ctx.workflow.dom.resultrecord.service.CreateDailyApprover;

@Stateless
public class DeleteHistoryCmm053Impl implements DeleteHistoryCmm053Service {

	@Inject
	private PersonApprovalRootRepository repoPerson;
	@Inject
	private ApprovalPhaseRepository repoAppPhase;
	@Inject
	private ApproverRepository repoApprover;
	@Inject
	private ApprovalBranchRepository repoBranch;
	
	@Inject
	private CreateDailyApprover createDailyApprover;

	@Override
	public void deleteHistoryByManagerSetting(GeneralDate startDate, GeneralDate endDate, String companyId,
			String employeeId) {
		GeneralDate endDatePrevious = startDate.addDays(-1);
		List<PersonApprovalRoot> getPastHistory = this.repoPerson.getPastHistory(companyId, employeeId);
		Map<GeneralDate, List<PersonApprovalRoot>> groupedPsApproval = getPastHistory.stream()
				.collect(Collectors.groupingBy(item -> item.getEmploymentAppHistoryItems().get(0).end()));

		if (groupedPsApproval.containsKey(endDate)) {
			List<PersonApprovalRoot> deletePersonApproval = groupedPsApproval.get(endDate);
			for (PersonApprovalRoot deleteItem : deletePersonApproval) {
				String approvalId = deleteItem.getApprovalId();
				String historyId  = deleteItem.getEmploymentAppHistoryItems().get(0).getHistoryId();
				String branchId   = deleteItem.getBranchId();
				Optional<ApprovalPhase> approvalPhase = this.repoAppPhase.getApprovalFirstPhase(companyId, branchId);
				if (approvalPhase.isPresent()) {
					String phaseId = approvalPhase.get().getApprovalPhaseId();
					// 「個人別就業承認ルート」に紐付く「分岐」「承認ルート」を削除する
					this.repoApprover.deleteAllApproverByAppPhId(companyId, phaseId);
					this.repoAppPhase.deleteAllAppPhaseByBranchId(companyId, branchId);
					this.repoBranch.deleteBranch(companyId, branchId);
				}
				// 「個人別就業承認ルート」を削除する
				this.repoPerson.deletePsApprovalRoot(companyId, approvalId, employeeId, historyId);
			}
		}

		List<PersonApprovalRoot> getAllPsApprovalRoot = this.repoPerson.getAllPsApprovalRoot(companyId, employeeId);
		List<PersonApprovalRoot> currentApprovalRoot  = this.getPersonApprovalRootByEndDate(getAllPsApprovalRoot, endDate);
		List<PersonApprovalRoot> previousApprovalRoot = this.getPersonApprovalRootByEndDate(getAllPsApprovalRoot, endDatePrevious);
		// 削除した履歴の直前の「個人別就業承認ルート」が存在するかチェックする
		if (currentApprovalRoot.isEmpty() && !previousApprovalRoot.isEmpty()) {
			for (PersonApprovalRoot updateItem : previousApprovalRoot) {
				// 削除した履歴の直前のドメインモデル「就業承認ルート履歴」を更新する。
				PersonApprovalRoot psAppRoot = PersonApprovalRoot.updateEdate(updateItem, endDate.toString().replace("/", "-"));
				repoPerson.updatePsApprovalRoot(psAppRoot);
			}
		}
		createDailyApprover.createApprover(employeeId, RecordRootType.CONFIRM_WORK_BY_DAY, startDate, startDate);
		createDailyApprover.createApprover(employeeId, RecordRootType.CONFIRM_WORK_BY_MONTH, startDate, startDate);
	}

	/**
	 * Get person approval root by end date
	 * 
	 * @param personAppRootList
	 * @param endDate
	 * @return
	 */
	private List<PersonApprovalRoot> getPersonApprovalRootByEndDate(List<PersonApprovalRoot> personAppRootList,
			GeneralDate endDate) {
		if (personAppRootList.isEmpty())
			return Collections.emptyList();
		return personAppRootList.stream()
				.filter(x -> x.getEmploymentAppHistoryItems().get(0).end().compareTo(endDate) == 0)
				.collect(Collectors.toList());
	}
}
