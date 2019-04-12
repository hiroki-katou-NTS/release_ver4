package nts.uk.ctx.workflow.pub.service;

import java.util.List;
import java.util.Map;

import nts.arc.time.GeneralDate;
import nts.uk.ctx.workflow.pub.agent.AgentPubExport;
import nts.uk.ctx.workflow.pub.service.export.AppRootStateConfirmExport;
import nts.uk.ctx.workflow.pub.service.export.ApprovalPhaseStateExport;
import nts.uk.ctx.workflow.pub.service.export.ApprovalPhaseStateParam;
import nts.uk.ctx.workflow.pub.service.export.ApprovalRootContentExport;
import nts.uk.ctx.workflow.pub.service.export.ApprovalRootOfEmployeeExport;
import nts.uk.ctx.workflow.pub.service.export.ApproveRootStatusForEmpExport;
import nts.uk.ctx.workflow.pub.service.export.ApproverApprovedExport;
import nts.uk.ctx.workflow.pub.service.export.ApproverPersonExportNew;
import nts.uk.ctx.workflow.pub.service.export.ApproverRemandExport;
/**
 * 
 * @author Doan Duy Hung
 *
 */
public interface ApprovalRootStatePub {
	/**
	 * RequestList356
	 * 実績の承認を解除する
	 * @param approverID
	 * @param approvalRecordDates
	 * @param employeeID
	 * @param rootType
	 * @return
	 */
	public boolean releaseApproval(String approverID, List<GeneralDate> approvalRecordDates, List<String> employeeID,Integer rootType,String companyID);
	/**
	 * RequestList347
	 * 実績の承認を登録する
	 * @param approverID
	 * @param approvalRecordDates
	 * @param employeeID
	 * @param rootType
	 * @return
	 */
	public void registerApproval(String approverID, List<GeneralDate> approvalRecordDates, List<String> employeeID,Integer rootType,String companyID);
	/**
	 * RequestList155
	 * [No.155]承認対象者リストと日付リストから承認状況を取得する
	 * getApprovalByListEmplAndListApprovalRecordDate
	 * @param approvalRecordDates
	 * @param employeeID
	 * @param companyID
	 * @param rootType
	 * @return
	 */
	public List<ApproveRootStatusForEmpExport> getApprovalByListEmplAndListApprovalRecordDate(List<GeneralDate> approvalRecordDates, List<String> employeeID,Integer rootType);
	/**
	 * RequestList229
	 * 承認対象者（複数）と期間から承認状況を取得する
	 * @param startDate
	 * @param endDate
	 * @param employeeID
	 * @param companyID
	 * @param rootType
	 * @return
	 */
	public List<ApproveRootStatusForEmpExport> getApprovalByListEmplAndDate(GeneralDate startDate, GeneralDate endDate, List<String> employeeID,String companyID,Integer rootType);
	/**
	 * 承認すべきデータ有無を取得する
	 * RequestList190 - 191
	 * @param startDate
	 * @param endDate
	 * @param approverID
	 * @param rootType
	 * @param companyID
	 * @return
	 */
	public boolean checkDataApproveed(GeneralDate startDate, GeneralDate endDate, String approverID,Integer rootType,String companyID);
	
	/**
	 * 承認対象者と期間から承認状況を取得する
	 * RequestList113
	 * @param startDate
	 * @param endDate
	 * @param employeeID
	 * @param companyID
	 * @param rootType
	 * @return
	 */
	public List<ApproveRootStatusForEmpExport> getApprovalByEmplAndDate(GeneralDate startDate, GeneralDate endDate, String employeeID,String companyID,Integer rootType); 
	
	/**
	 * RequestList133
	 * @param startDate
	 * @param endDate
	 * @param approverID
	 * @param companyID
	 * @param rootType
	 * @return
	 */
	public ApprovalRootOfEmployeeExport getApprovalRootOfEmloyee(GeneralDate startDate, GeneralDate endDate, String approverID,String companyID,Integer rootType);
	/**
	 * 承認ルートを取得する
	 * @param appID
	 * @param companyID
	 * @return
	 */
	public Map<String,List<ApprovalPhaseStateExport>> getApprovalRoots(List<String> appID,String companyID);
	
	public ApprovalRootContentExport getApprovalRoot(String companyID, String employeeID, Integer appTypeValue, GeneralDate date, String appID, Boolean isCreate);
	
	public void insertAppRootType(String companyID, String employeeID, Integer appTypeValue, GeneralDate date, String appID, Integer rootType);
	
	/**
	 * 4.次の承認の番の承認者を取得する(メール通知用)
	 * @param companyID 会社ID
	 * @param rootStateID インスタンスID
	 * @param approvalPhaseStateNumber ドメインモデル「承認フェーズインスタンス」・順序
	 * @return
	 */
	public List<String> getNextApprovalPhaseStateMailList(String companyID, String rootStateID, Integer approvalPhaseStateNumber, 
			Boolean isCreate, String employeeID, Integer appTypeValue, GeneralDate appDate, Integer rootType);
	
	/**
	 * 承認する
	 * @param companyID 会社ID
	 * @param rootStateID インスタンスID
	 * @param employeeID 社員ID
	 * @return 承認フェーズ枠番
	 */
	public Integer doApprove(String companyID, String rootStateID, String employeeID, Boolean isCreate, 
			Integer appTypeValue, GeneralDate appDate, String memo, Integer rootType);
	
	/**
	 * 2.承認全体が完了したか
	 * @param companyID 会社ID
	 * @param rootStateID インスタンスID
	 * @return
	 */
	public Boolean isApproveAllComplete(String companyID, String rootStateID, String employeeID, Boolean isCreate, 
			Integer appTypeValue, GeneralDate appDate, Integer rootType);
	
	/**
	 * 一括解除する 
	 * @param companyID 会社ID
	 * @param rootStateID インスタンスID
	 */
	public void doReleaseAllAtOnce(String companyID, String rootStateID, Integer rootType);
	
	/**
	 * 1.承認を行った承認者を取得する
	 * @param rootStateID
	 * @return
	 */
	public ApproverApprovedExport getApproverApproved(String rootStateID, Integer rootType); 
	
	/**
	 * RequestList No.484
	 * 承認代行情報の取得処理
	 * @param companyID 会社ID
	 * @param listApprover 承認者リスト-[承認者の社員ID]の一覧
	 * @return
	 */
	public AgentPubExport getApprovalAgentInfor(String companyID, List<String> listApprover);
	
	/**
	 * 削除時のメール通知者を取得する
	 * @param companyID 会社ID
	 * @param rootStateID インスタンスID
	 * @return
	 */
	public List<String> getMailNotifierList(String companyID, String rootStateID, Integer rootType);
	
	public void deleteApprovalRootState(String rootStateID, Integer rootType);
	
	/**
	 * 解除する
	 * @param companyID 会社ID
	 * @param rootStateID インスタンスID
	 * @param employeeID 社員ID
	 */
	public Boolean doRelease(String companyID, String rootStateID, String employeeID, Integer rootType);
	
	/**
	 * 否認する
	 * @param companyID 会社ID
	 * @param rootStateID インスタンスID
	 * @param employeeID 社員ID
	 * @return 否認を実行したかフラグ(true, false)
				true：否認を実行した
				false：否認を実行しなかった
	 */
	public Boolean doDeny(String companyID, String rootStateID, String employeeID, String memo, Integer rootType);
	
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
	 * 3.指定した社員が承認できるかの判断
	 * @param companyID 会社ID
	 * @param rootStateID インスタンスID
	 * @param employeeID 社員ID
	 * @return
	 */
	public ApproverPersonExportNew judgmentTargetPersonCanApprove(String companyID, String rootStateID, String employeeID, Integer rootType);
	
	/**
	 * RequestList No.482
	 * 差し戻しする(承認者まで)
	 * @param companyID
	 * @param rootStateID
	 * @param order
	 */
	public List<String> doRemandForApprover(String companyID, String rootStateID, Integer order, Integer rootType);
	
	/**
	 * RequestList No.480
	 * 差し戻しする(本人まで)
	 * @param companyID
	 * @param rootStateID
	 */
	public void doRemandForApplicant(String companyID, String rootStateID, Integer rootType);
	
	/**
	 * RequestList #136
	 * 承認ルートインスタンスを生成する
	 * @param companyID
	 * @param employeeID
	 * @param confirmAtr
	 * @param appType
	 * @param date
	 * @return
	 */
	public AppRootStateConfirmExport getApprovalRootState(String companyID, String employeeID, 
			Integer confirmAtr, Integer appType, GeneralDate date);
	
	/**
	 * RequestList 403
	 * 承認状態をすべてクリアする
	 * @param rootStateID
	 */
	public void cleanApprovalRootState(String rootStateID, Integer rootType);
	
	public void deleteConfirmDay(String employeeID, GeneralDate date);
	/**
	 * RequestList No.483
	 * 1.承認フェーズ毎の承認者を取得する
	 * @param phase
	 * @return
	 */
	public List<String> getApproverFromPhase(ApprovalPhaseStateParam phase);
	/**
	 * RequestList 479
	 * 差し戻し対象者一覧を取得
	 * @param appID
	 * @return
	 */
	public List<ApproverRemandExport> getListApproverRemand(String appID);
	
	/**
	 * 1.指定する承認フェーズの承認が完了したか
	 * @param approvalPhaseState ドメインモデル「承認フェーズインスタンス」
	 * @return 承認完了フラグ(true, false)
　				true：指定する承認フェーズの承認が完了
　				false：指定する承認フェーズの承認がまだ未完了
	 */
	public Boolean isApproveApprovalPhaseStateComplete(String companyID, String rootStateID, Integer phaseNumber);
}
