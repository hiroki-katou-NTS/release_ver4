package nts.uk.ctx.workflow.dom.agent;

import java.util.List;

import nts.uk.ctx.workflow.dom.agent.output.ApprovalAgencyInfoOutput;

/**
 * 3-1.承認代行情報の取得処理
 * 
 * @author tutk
 *
 */
public interface ApprovalAgencyInfoService {
	ApprovalAgencyInfoOutput getApprovalAgencyInformation(String companyID, List<String> approver);

}
