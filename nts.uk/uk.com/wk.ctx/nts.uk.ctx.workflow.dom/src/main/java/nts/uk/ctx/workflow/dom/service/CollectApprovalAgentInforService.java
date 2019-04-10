package nts.uk.ctx.workflow.dom.service;

import java.util.List;

import nts.uk.ctx.workflow.dom.service.output.ApprovalRepresenterOutput;
/**
 * 承認代行情報の取得処理
 * @author Doan Duy Hung
 *
 */
public interface CollectApprovalAgentInforService {
	
	/**
	 * 承認代行情報の取得処理
	 * @param companyID 会社ID
	 * @param listApprover 承認者リスト
　							※[承認者の社員ID]の一覧
	 * @return
	 */
	public ApprovalRepresenterOutput getApprovalAgentInfor(String companyID, List<String> listApprover);
	
}
