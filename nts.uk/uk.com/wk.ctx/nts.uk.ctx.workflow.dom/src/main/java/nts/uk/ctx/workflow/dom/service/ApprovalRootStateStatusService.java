package nts.uk.ctx.workflow.dom.service;

import java.util.List;

import nts.arc.time.GeneralDate;
import nts.uk.ctx.workflow.dom.approverstatemanagement.ApprovalRootState;
import nts.uk.ctx.workflow.dom.approverstatemanagement.DailyConfirmAtr;
import nts.uk.ctx.workflow.dom.service.output.ApprovalRootStateStatus;
/**
 * 
 * @author Doan Duy Hung
 *
 */
public interface ApprovalRootStateStatusService {
	
	/**
	 * 承認対象者と期間から承認状況を取得する
	 * @param employeeID
	 * @param startDate
	 * @param endDate
	 * @param rootType
	 * @return
	 */
	public List<ApprovalRootStateStatus> getStatusByEmpAndDate(String employeeID, GeneralDate startDate, GeneralDate endDate, Integer rootType);
	/**
	 * 承認ルート状況を取得する
	 * @param approvalRootStateList
	 * @return
	 */
	public List<ApprovalRootStateStatus> getApprovalRootStateStatus(List<ApprovalRootState> approvalRootStateList);
	
	/**
	 * 日別・月別確認のステータスを判断する
	 * @param approvalRootState
	 * @return
	 */
	public DailyConfirmAtr determineDailyConfirm(ApprovalRootState approvalRootState);
	
	/**
	 * 承認フェーズインスタンスの承認状態が未承認か判断
	 * @param rootStateID 申請ID
	 * @return 　・未承認以外フラグ：
　　			False：全て承認枠の承認状態が未承認　　　　　（誰も承認していない）
　　			True:　全て承認枠の承認状態が未承認ではない　（１人でも承認処理していた）
	 */
	public boolean determinePhaseApproval(String rootStateID);
}
