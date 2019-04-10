package nts.uk.ctx.workflow.dom.service;

import java.util.List;

import nts.arc.time.GeneralDate;
import nts.uk.ctx.workflow.dom.approvermanagement.workroot.ApplicationType;
import nts.uk.ctx.workflow.dom.approvermanagement.workroot.ApprovalPhase;
import nts.uk.ctx.workflow.dom.approvermanagement.workroot.ConfirmationRootType;
import nts.uk.ctx.workflow.dom.approvermanagement.workroot.EmploymentRootAtr;
import nts.uk.ctx.workflow.dom.service.output.ApprovalRootContentOutput;
import nts.uk.ctx.workflow.dom.service.output.ApproverInfo;
import nts.uk.ctx.workflow.dom.service.output.ErrorFlag;
/**
 * 承認ルートを取得する
 * @author Doan Duy Hung
 *
 */
public interface CollectApprovalRootService {
	
	/**
	 * 1.社員の対象申請の承認ルートを取得する
	 * 
	 * @param cid 会社ID
	 * @param sid 社員ID（申請本人の社員ID）
	 * @param employmentRootAtr 就業ルート区分
	 * @param subjectRequest 対象申請
	 * @param standardDate 基準日
	 */
	public ApprovalRootContentOutput getApprovalRootOfSubjectRequest(
			String companyID, 
			String employeeID, 
			EmploymentRootAtr rootAtr, 
			ApplicationType appType, 
			GeneralDate standardDate);
	
	/**
	 * 2.承認ルートを整理する
	 * 
	 * @param cid 会社ID
	 * @param sid 社員ID（申請本人の社員ID）
	 * @param baseDate 基準日
	 * @param appPhases 承認フーズ
	 */
	public List<ApprovalPhase> adjustmentData(String companyID, String employeeID, GeneralDate baseDate,  List<ApprovalPhase> listApprovalPhase);
	
	/**
	 * 3.職位から承認者へ変換する
	 * @param cid 会社ID
	 * @param sid 社員ID
	 * @param baseDate 基準日
	 * @param jobTitleId 職位ID（承認者）
	 * @return
	 */
	public List<ApproverInfo> convertPositionToApprover(String companyID, String employeeID, GeneralDate baseDate, String jobTitleId);
	
	/**
	 * 4.申請者の職位の序列は承認者のと比較する
	 * @param companyID 会社ID
	 * @param targetPersonID 社員ID（申請本人の社員ID）
	 * @param approverID 職位ID（承認者）
	 * @param date 基準日
	 * @return
	 */
	public Boolean compareHierarchyTargetPerson(String companyID, String targetPersonID, String positionID, GeneralDate date);
	
	/**
	 * 6.職場に指定する職位の対象者を取得する
	 * @param cid 会社ID
	 * @param wkpId 職場ID（申請者の所属職場）
	 * @param baseDate 基準日
	 * @param jobTitleId 職位ID（承認者）
	 * @return
	 */
	public List<ApproverInfo> getPersonByWorkplacePosition(String cid, String wkpId, GeneralDate baseDate, String jobTitleId);
	
	/**
	 * 7.承認ルートの異常チェック
	 * @param listApprovalPhaseBefore 取得した承認ルート（マスタ設定）
	 * @param listApprovalPhaseAfter 整理後の承認ルート
	 * @return エラーフラグ
	 */
	public ErrorFlag checkApprovalRoot(List<ApprovalPhase> listApprovalPhaseBefore, List<ApprovalPhase> listApprovalPhaseAfter);
	
	// 8.社員の３６申請の承認ルートを取得する
	public void getApprovalRootBy36AppEmployee(String companyID, String employeeID, GeneralDate date);
	
	/**
	 * 10.閲覧フェーズを整理する
	 * @param companyID 会社ID
	 * @param employeeID 社員ID（申請本人の社員ID）
	 * @param date 基準日
	 * @param approvalPhase 整理前の承認ルート
	 * @return
	 */
	public List<String> organizeBrowsingPhase(String companyID, String employeeID, GeneralDate date, ApprovalPhase approvalPhase);
	
	/**
	 * 承認ルートを取得する（確認）
	 * @param companyID
	 * @param employeeID
	 * @param confirmAtr
	 * @param standardDate
	 * @return
	 */
	public ApprovalRootContentOutput getApprovalRootConfirm(
			String companyID, 
			String employeeID, 
			ConfirmationRootType confirmAtr, 
			GeneralDate standardDate);
	
}
