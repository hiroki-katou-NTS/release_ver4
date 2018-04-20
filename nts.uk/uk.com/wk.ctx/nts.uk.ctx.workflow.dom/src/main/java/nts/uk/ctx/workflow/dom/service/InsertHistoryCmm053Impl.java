package nts.uk.ctx.workflow.dom.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.arc.error.BusinessException;
import nts.arc.time.GeneralDate;
import nts.uk.ctx.workflow.dom.approvermanagement.workroot.ApprovalBranch;
import nts.uk.ctx.workflow.dom.approvermanagement.workroot.ApprovalBranchRepository;
import nts.uk.ctx.workflow.dom.approvermanagement.workroot.ApprovalPhase;
import nts.uk.ctx.workflow.dom.approvermanagement.workroot.ApprovalPhaseRepository;
import nts.uk.ctx.workflow.dom.approvermanagement.workroot.Approver;
import nts.uk.ctx.workflow.dom.approvermanagement.workroot.ApproverRepository;
import nts.uk.ctx.workflow.dom.approvermanagement.workroot.PersonApprovalRoot;
import nts.uk.ctx.workflow.dom.approvermanagement.workroot.PersonApprovalRootRepository;

/**
 * @author sang.nv
 *
 */
@Stateless
public class InsertHistoryCmm053Impl implements InsertHistoryCmm053Service {

	@Inject
	private PersonApprovalRootRepository repoPerson;
	@Inject
	private ApprovalPhaseRepository repoAppPhase;
	@Inject
	private ApproverRepository repoApprover;
	@Inject
	private ApprovalBranchRepository repoBranch;

	/**
	 * Add history
	 * @param command
	 * @param historyId
	 * @param departmentApproverId
	 * @param dailyApproverId
	 */
	@Override
	public void insertHistoryByManagerSetting(String companyId, String historyId, String employeeId, GeneralDate startDate, String departmentApproverId,
			String dailyApproverId) {
		String endDate = "9999-12-31";
		List<PersonApprovalRoot> psOlds = this.repoPerson.getPsAppRootLastest(companyId, employeeId, GeneralDate.fromString("9999/12/31", "yyyy/MM/dd"));

		// 追加する履歴を最新の履歴の開始年月日と比較する
		if (!psOlds.isEmpty() && startDate.before(psOlds.get(0).getEmploymentAppHistoryItems().get(0).start())) {
			throw new BusinessException("Msg_153");
		}

		List<PersonApprovalRoot> listPsPre = this.repoPerson.getPsApprovalRootBySdate(companyId, employeeId, startDate);
		List<ApprovalPhase> listApprovalPhase = new ArrayList<>();
		if (!listPsPre.isEmpty()) {
			for (PersonApprovalRoot psApp : listPsPre) {
				Optional<ApprovalPhase> approvalPhase = this.repoAppPhase.getApprovalFirstPhase(companyId,
						psApp.getBranchId());
				if (approvalPhase.isPresent()) {
					listApprovalPhase.add(approvalPhase.get());
				}
			}
		}
		// ドメインモデル「個人別就業承認ルート」．「承認フェーズ」．「順序」をチェックする
		if (!listApprovalPhase.isEmpty()) {
			// ドメインモデル「個人別就業承認ルート」．「承認フェーズ」．「順序」を追加する
			Optional<PersonApprovalRoot> commonPs  = this.repoPerson.getNewestCommonPsAppRoot(companyId, employeeId);
			Optional<PersonApprovalRoot> monthlyPs = this.repoPerson.getNewestMonthlyPsAppRoot(companyId, employeeId);
			this.updateOrInsertHistory(companyId, employeeId, historyId, startDate, endDate, commonPs, monthlyPs,
					departmentApproverId, dailyApproverId);
		} else {
			if (!psOlds.isEmpty()) {
				GeneralDate endDatePrevious = startDate.addDays(-1);
				// 取得したドメインモデル「就業承認ルート履歴」を更新する。（履歴の終了日を追加する履歴の開始日-1日した日付にする）
				for (PersonApprovalRoot updateItem : psOlds) {
					PersonApprovalRoot psAppRoot = PersonApprovalRoot.updateEdate(updateItem,
							endDatePrevious.toString().replace("/", "-"));
					repoPerson.updatePsApprovalRoot(psAppRoot);
				}
			}

			// 条件： １．就業ルート区分：申請 AND 申請種類：共通ルート ２．承認フェーズ.順序 ＝ 1
			PersonApprovalRoot common = PersonApprovalRoot.createSimpleFromJavaType(companyId,
					UUID.randomUUID().toString(), employeeId, historyId, null, startDate.toString().replace("/", "-"),
					endDate, UUID.randomUUID().toString(), null, null, 0);

			// 条件： 1．就業ルート区分：確認 AND 確認ルート種類：月次確認 2．承認フェーズ.順序 ＝ 1
			PersonApprovalRoot monthly = PersonApprovalRoot.createSimpleFromJavaType(companyId,
					UUID.randomUUID().toString(), employeeId, historyId, null, startDate.toString().replace("/", "-"),
					endDate, UUID.randomUUID().toString(), null, 1, 2);

			List<PersonApprovalRoot> insertPersonApproval = new ArrayList<>();
			if (!Objects.isNull(dailyApproverId)) {
				insertPersonApproval.add(common);
			}
			insertPersonApproval.add(monthly);

			// ドメインモデル「就業承認ルート」と紐付きドメインモデル「分岐」「承認ルート」をINSERTする(INSERT
			// domain「就業承認ルート」và domain 「分岐」「承認ルート」 liên kết)
			this.addHistoryByListPersonApprovalRoot(companyId, departmentApproverId, dailyApproverId, insertPersonApproval);
		}
	}
	
	/**
	 * AddistoryByListPersonApprovalRoot
	 * @param companyId
	 * @param departmentApproverId
	 * @param dailyApproverId
	 * @param insertPersonApproval
	 */
	private void addHistoryByListPersonApprovalRoot(String companyId, String departmentApproverId, String dailyApproverId,
			List<PersonApprovalRoot> insertPersonApproval) {
		int orderNumber         = 1;
		int approverOrderNumber = 0;
		int approvalAtr         = 0;
		int confirmPerson       = 0;
		int approvalForm        = 1;
		int browsingPhase       = 0;
		String jobTitleId       = null;
		List<ApprovalBranch> lstBranch = new ArrayList<>();

		for (PersonApprovalRoot psAppRoot : insertPersonApproval) {
			String approverId      = UUID.randomUUID().toString();
			String approvalPhaseId = UUID.randomUUID().toString();
			String branchId        = psAppRoot.getBranchId();

			// 承認者
			List<Approver> listApprover = new ArrayList<>();
			String employeeIdApprover   = PersonApprovalRoot.isCommonPsApprovalRoot(psAppRoot) ? dailyApproverId : departmentApproverId;
			listApprover.add(Approver.createSimpleFromJavaType(companyId, branchId, approvalPhaseId, approverId,
					jobTitleId, employeeIdApprover, approverOrderNumber, approvalAtr, confirmPerson));
			repoApprover.addAllApprover(listApprover);

			// 承認フェーズ
			repoAppPhase.addApprovalPhase(ApprovalPhase.createSimpleFromJavaType(companyId, branchId, approvalPhaseId,
					approvalForm, browsingPhase, orderNumber, listApprover));

			ApprovalBranch branch = new ApprovalBranch(companyId, branchId, 1);
			lstBranch.add(branch);
		}

		// 分岐
		repoBranch.addAllBranch(lstBranch);

		// ドメインモデル「個人別就業承認ルート」をINSERTする
		this.repoPerson.addAllPsApprovalRoot(insertPersonApproval);
	}

	/**
	 * １．バラバラ履歴の場合
	 *     1.1　最新履歴の承認者を更新する
	 *     1.2　前の履歴を変更して新しい履歴を追加する
	 * @param companyId
	 * @param employeeId
	 * @param historyId
	 * @param startDate
	 * @param endDate
	 * @param commonPs
	 * @param monthlyPs
	 * @param departmentApproverId
	 * @param dailyApproverId
	 */
	@Override
	public void updateOrInsertDiffStartDate(String companyId, String employeeId, String historyId, GeneralDate startDate,
			String endDate, Optional<PersonApprovalRoot> commonPs, Optional<PersonApprovalRoot> monthlyPs,
			String departmentApproverId, String dailyApproverId){

		PersonApprovalRoot newestPsAppRoot = null;
		PersonApprovalRoot olderPsAppRoot  = null;
		GeneralDate endDatePrevious        = startDate.addDays(-1);
		List<PersonApprovalRoot> insertPsAppRoot = new ArrayList<>();

		if (commonPs.get().getEmploymentAppHistoryItems().get(0).start()
				.after(monthlyPs.get().getEmploymentAppHistoryItems().get(0).start())) {
			newestPsAppRoot = commonPs.get();
			olderPsAppRoot  = monthlyPs.get();
		} else {
			newestPsAppRoot = monthlyPs.get();
			olderPsAppRoot  = commonPs.get();
		}

		//1.1　最新履歴の承認者を更新する
		String employeeIdApprover = PersonApprovalRoot.isCommonPsApprovalRoot(newestPsAppRoot) ? dailyApproverId : departmentApproverId;
		this.updateApproverFirstPhase(companyId, employeeIdApprover, newestPsAppRoot);

		//1.2　前の履歴を変更して新しい履歴を追加する
		PersonApprovalRoot updateOlderPsAppRoot = PersonApprovalRoot.updateEdate(olderPsAppRoot, endDatePrevious.toString().replace("/", "-"));
		repoPerson.updatePsApprovalRoot(updateOlderPsAppRoot);
		
		if (PersonApprovalRoot.isCommonPsApprovalRoot(olderPsAppRoot)) {
			// 条件： １．就業ルート区分：申請 AND 申請種類：共通ルート ２．承認フェーズ.順序 ＝ 1
			PersonApprovalRoot common = PersonApprovalRoot.createSimpleFromJavaType(companyId,
					UUID.randomUUID().toString(), employeeId, historyId, null,
					startDate.toString().replace("/", "-"), endDate, UUID.randomUUID().toString(), null, null, 0);
			if(!Objects.isNull(dailyApproverId)){
				insertPsAppRoot.add(common);
			}
		} else {
			// 条件： 1．就業ルート区分：確認 AND 確認ルート種類：月次確認 2．承認フェーズ.順序 ＝ 1
			PersonApprovalRoot monthly = PersonApprovalRoot.createSimpleFromJavaType(companyId,
					UUID.randomUUID().toString(), employeeId, historyId, null,
					startDate.toString().replace("/", "-"), endDate, UUID.randomUUID().toString(), null, 1, 2);
			insertPsAppRoot.add(monthly);
		}
		this.addHistoryByListPersonApprovalRoot(companyId, departmentApproverId, dailyApproverId, insertPsAppRoot);
	}

	/**
	 * ２．一個履歴の場合 
	 *     1.1　履歴の承認者を変更する 
	 *     1.2　新しい履歴を追加する
	 * @param companyId
	 * @param employeeId
	 * @param historyId
	 * @param startDate
	 * @param endDate
	 * @param commonPs
	 * @param monthlyPs
	 * @param dailyApproverId
	 * @param departmentApproverId
	 */
	@Override
	public void updateOrInsertHistory(String companyId, String employeeId, String historyId, GeneralDate startDate,
			String endDate, Optional<PersonApprovalRoot> commonPs, Optional<PersonApprovalRoot> monthlyPs,
			String departmentApproverId, String dailyApproverId) {
		List<PersonApprovalRoot> personApproval       = new ArrayList<>();
		List<PersonApprovalRoot> insertPersonApproval = new ArrayList<>();
		if (commonPs.isPresent()) {
			personApproval.add(commonPs.get());
		} else {
			// 条件： １．就業ルート区分：申請 AND 申請種類：共通ルート ２．承認フェーズ.順序 ＝ 1
			PersonApprovalRoot common = PersonApprovalRoot.createSimpleFromJavaType(companyId,
					UUID.randomUUID().toString(), employeeId, historyId, null, startDate.toString().replace("/", "-"),
					endDate, UUID.randomUUID().toString(), null, null, 0);
			if(!Objects.isNull(dailyApproverId)){
				insertPersonApproval.add(common);
			}
		}

		if (monthlyPs.isPresent()) {
			personApproval.add(monthlyPs.get());
		} else {
			// 条件： 1．就業ルート区分：確認 AND 確認ルート種類：月次確認 2．承認フェーズ.順序 ＝ 1
			PersonApprovalRoot monthly = PersonApprovalRoot.createSimpleFromJavaType(companyId,
					UUID.randomUUID().toString(), employeeId, historyId, null, startDate.toString().replace("/", "-"),
					endDate, UUID.randomUUID().toString(), null, 1, 2);
			insertPersonApproval.add(monthly);
		}

		if (!personApproval.isEmpty()) {
			for (PersonApprovalRoot psAppRoot : personApproval) {
				String employeeIdApprover = PersonApprovalRoot.isCommonPsApprovalRoot(psAppRoot) ? dailyApproverId : departmentApproverId;
				this.updateApproverFirstPhase(companyId, employeeIdApprover, psAppRoot);
			}
		}

		if (!insertPersonApproval.isEmpty()) {
			this.addHistoryByListPersonApprovalRoot(companyId, departmentApproverId, dailyApproverId, insertPersonApproval);
		}
	}

	/**
	 * Update approver first phase
	 * @param companyId
	 * @param employeeIdApprover
	 * @param psAppRoot
	 */
	private void updateApproverFirstPhase(String companyId, String employeeIdApprover, PersonApprovalRoot psAppRoot) {
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
