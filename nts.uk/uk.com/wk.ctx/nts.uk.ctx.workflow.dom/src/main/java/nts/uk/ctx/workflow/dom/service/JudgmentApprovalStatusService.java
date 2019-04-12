package nts.uk.ctx.workflow.dom.service;

import java.util.List;

import nts.uk.ctx.workflow.dom.approverstatemanagement.ApprovalBehaviorAtr;
import nts.uk.ctx.workflow.dom.approverstatemanagement.ApprovalPhaseState;
import nts.uk.ctx.workflow.dom.approverstatemanagement.ApprovalRootState;
import nts.uk.ctx.workflow.dom.service.output.ApprovalStatusOutput;
import nts.uk.ctx.workflow.dom.service.output.ApproverPersonOutput;
import nts.uk.ctx.workflow.dom.service.output.ApproverPersonOutputNew;

/**
 * 承認ステータスの判断
 * @author Doan Duy Hung
 *
 */
public interface JudgmentApprovalStatusService {
	
	/**
	 * 1.指定した社員が承認者であるかの判断
	 * @param companyID 会社ID
	 * @param rootStateID インスタンスID
	 * @param employeeID 社員ID
	 * @return 承認者フラグ(true、false)
	 			true：承認者である
	 			false：承認者でない
	 */
	public Boolean judgmentTargetPersonIsApprover(String companyID, String rootStateID, String employeeID, Integer rootType);
	
	/**
	 * 2.承認ステータスの判断
	 * @param companyID 会社ID
	 * @param rootStateID インスタンスID
	 * @return ステータス：（否認、承認済、未承認、差し戻し）
	 */
	public ApprovalBehaviorAtr determineApprovalStatus(String companyID, String rootStateID, Integer rootType);
	
	/**
	 * 3.指定した社員が承認できるかの判断
	 * @param companyID 会社ID
	 * @param rootStateID インスタンスID
	 * @param employeeID 社員ID
	 * @return
	 */
	public ApproverPersonOutputNew judgmentTargetPersonCanApprove(String companyID, String rootStateID, String employeeID, Integer rootType);
	
	/**
	 * 1.承認フェーズ毎の承認者を取得する
	 * lấy người xác nhận trong Phase
	 * @param approvalPhaseState Approval Phase
	 * @return list Approver
	 */
	public List<String> getApproverFromPhase(ApprovalPhaseState approvalPhaseState);
	
	/**
	 * 1.承認状況の判断
	 * @param approvalPhaseState
	 * @param employeeID
	 */
	public ApprovalStatusOutput judmentApprovalStatus(String companyID, ApprovalPhaseState approvalPhaseState, String employeeID);
	
	/**
	 * 1.承認状況の判断(NodataDatabaseAcess)
	 * @param approvalPhaseState
	 * @param employeeID
	 */
	public ApprovalStatusOutput judmentApprovalStatusNodataDatabaseAcess(String companyID, ApprovalPhaseState approvalPhaseState, String employeeID,List<String> agents);
	
	/**
	 * 2.指定した社員が指定した承認者リストの代行承認者かの判断
	 * @param companyID
	 * @param employeeID
	 * @param listApprover
	 * @return
	 */
	public Boolean judgmentAgentListByEmployee(String companyID, String employeeID, List<String> listApprover);
	
	/**
	 * 4.ループ中の承認フェーズが承認中のフェーズかチェックする
	 * @param approvalRootState 承認ルートインスタンス（ApprovalRootInstance）
	 * @param currentPhase ループ中の承認フェーズのNo（current phase no）（5～1）
	 * @param pastPhaseFlg 過去フェーズフラグ
	 * @return true/false
	 */
	public Boolean judgmentLoopApprovalPhase(ApprovalRootState approvalRootState, ApprovalPhaseState currentPhase, Boolean pastPhaseFlg);
	
	/**
	 * 3.指定した社員が承認できるかの判断(NoDBACCESS)
	 * @param approvalRootState
	 * @return
	 */
	public ApproverPersonOutput judgmentTargetPerCanApproveNoDB(ApprovalRootState approvalRootState, String approverID);
}
