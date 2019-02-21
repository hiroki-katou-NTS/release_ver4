package nts.uk.ctx.at.request.dom.application.common.service.detailscreen.after;

import nts.uk.ctx.at.request.dom.application.common.service.other.output.ProcessResult;

/**
 * 8-2.詳細画面承認後の処理
 * @author Doan Duy Hung
 *
 */
public interface DetailAfterApproval_New {
	
	public ProcessResult doApproval(String companyID, String appID, String employeeID, String memo, String appReason, boolean isUpdateAppReason);
	
}
