package nts.uk.ctx.workflow.dom.service;

import nts.arc.time.GeneralDate;
import nts.uk.ctx.workflow.dom.approvermanagement.workroot.PersonApprovalRoot;

/**
 * @author sang.nv
 *
 */
public interface UpdateHistoryCmm053Service {
	/**
	 * Update History By Manager Setting
	 * 03.履歴更新を登録する
	 * @param companyId
	 * @param historyId
	 * @param employeeId
	 * @param startDate
	 * @param departmentApproverId
	 * @param dailyApproverId
	 */
	void updateHistoryByManagerSetting(String companyId, String historyId, String employeeId, GeneralDate startDate,
			String departmentApproverId, String dailyApproverId, boolean dailyDisplay);

	/**
	 * Update approver first phase
	 * @param companyId
	 * @param employeeIdApprover
	 * @param psAppRoot
	 */
	void updateApproverFirstPhase(String companyId, String employeeIdApprover, PersonApprovalRoot psAppRoot);
	
	void updateRootCMM053(String companyId, String a27, String a210,PersonApprovalRoot commonRoot, 
			PersonApprovalRoot monthlyRoot, boolean dailyDisplay);
}
