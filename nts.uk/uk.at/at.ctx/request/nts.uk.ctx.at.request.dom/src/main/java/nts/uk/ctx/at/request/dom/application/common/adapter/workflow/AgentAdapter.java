package nts.uk.ctx.at.request.dom.application.common.adapter.workflow;

import java.util.List;

import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.request.dom.application.common.adapter.workflow.dto.AgentDataRequestPubImport;
import nts.uk.ctx.at.request.dom.application.common.adapter.workflow.dto.AgentPubImport;
/**
 * 3-1.承認代行情報の取得処理
 * @author tutk
 *
 */
public interface AgentAdapter {
	
	AgentPubImport getApprovalAgencyInformation(String companyID, List<String> approver);
	
	List<AgentDataRequestPubImport> lstAgentData(String companyId, String employeeId, GeneralDate baseDate);
}
