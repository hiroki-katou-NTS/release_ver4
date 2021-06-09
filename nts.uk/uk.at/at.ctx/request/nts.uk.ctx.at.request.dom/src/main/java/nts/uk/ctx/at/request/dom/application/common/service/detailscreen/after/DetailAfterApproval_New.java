package nts.uk.ctx.at.request.dom.application.common.service.detailscreen.after;

import nts.uk.ctx.at.request.dom.application.Application_New;
import nts.uk.ctx.at.request.dom.application.common.service.other.output.ProcessResult;

/**
 * 8-2.詳細画面承認後の処理
 * @author Doan Duy Hung
 *
 */
public interface DetailAfterApproval_New {
	
	public default ProcessResult doApproval(String companyID, String appID, String employeeID, String memo, String appReason, boolean isUpdateAppReason) {
		
		ApprovalProcessAfterResult approval = doApprovalSimple(companyID, appID, employeeID, memo, appReason, isUpdateAppReason);
		
		ProcessMailResult sendMailResult = processMail(companyID, appID, employeeID, approval.getApplication(), approval.getAllApprovalFlg(), approval.getPhaseNumber());
		
		return new ProcessResult(true, sendMailResult.isAutoSendMail(), sendMailResult.getAutoSuccessMail(),
				sendMailResult.getAutoFailMail(), appID, approval.getReflectAppId());
	}
	
	public ApprovalProcessAfterResult doApprovalSimple(String companyID, String appID, String employeeID, String memo, String appReason, boolean isUpdateAppReason);

	public ProcessMailResult processMail(String companyID, String appID, String employeeID, Application_New application,
			Boolean allApprovalFlg, Integer phaseNumber);

}
