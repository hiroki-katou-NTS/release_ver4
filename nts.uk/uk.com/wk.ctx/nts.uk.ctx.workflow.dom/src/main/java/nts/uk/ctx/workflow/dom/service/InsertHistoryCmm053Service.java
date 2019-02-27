package nts.uk.ctx.workflow.dom.service;

import java.util.Optional;

import nts.arc.time.GeneralDate;
import nts.uk.ctx.workflow.dom.approvermanagement.workroot.PersonApprovalRoot;

/**
 * @author sang.nv
 *
 */
public interface InsertHistoryCmm053Service {

	/**
	 * Insert History By Manager Setting
	 * 02.履歴追加を登録する
	 * @param companyId
	 * @param historyId
	 * @param employeeId
	 * @param startDate
	 * @param departmentApproverId
	 * @param dailyApproverId
	 */
	void insertHistoryByManagerSetting(String companyId, String historyId, String employeeId, GeneralDate startDate, String departmentApproverId,
			String dailyApproverId, boolean dailyDisplay);

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
	void updateOrInsertDiffStartDate(String companyId, String employeeId, String historyId, GeneralDate startDate,
			String endDate, Optional<PersonApprovalRoot> commonPs, Optional<PersonApprovalRoot> monthlyPs,
			String departmentApproverId, String dailyApproverId, boolean dailyDisplay);

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
	void updateOrInsertHistory(String companyId, String employeeId, String historyId, GeneralDate startDate,
			String endDate, Optional<PersonApprovalRoot> commonPs, Optional<PersonApprovalRoot> monthlyPs,
			String departmentApproverId, String dailyApproverId, boolean dailyDisplay);
}
