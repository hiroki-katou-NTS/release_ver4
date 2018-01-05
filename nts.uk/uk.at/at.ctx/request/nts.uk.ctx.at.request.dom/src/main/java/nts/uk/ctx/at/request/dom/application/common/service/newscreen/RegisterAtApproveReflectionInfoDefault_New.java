package nts.uk.ctx.at.request.dom.application.common.service.newscreen;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.uk.ctx.at.request.dom.application.ApplicationRepository_New;
import nts.uk.ctx.at.request.dom.application.Application_New;
import nts.uk.ctx.at.request.dom.application.ReflectedState_New;
import nts.uk.ctx.at.request.dom.application.common.adapter.workflow.ApprovalRootStateAdapter;

/**
 * 2-2.新規画面登録時承認反映情報の整理
 * 
 * @author ducpm
 *
 */
@Stateless
public class RegisterAtApproveReflectionInfoDefault_New implements RegisterAtApproveReflectionInfoService_New {
	
	@Inject
	private ApprovalRootStateAdapter approvalRootStateAdapter;
	
	@Inject
	private ApplicationRepository_New applicationRepository;
	
	@Override
	public void newScreenRegisterAtApproveInfoReflect(String SID, Application_New application) {
		// アルゴリズム「承認情報の整理」を実行する
		approvalRootStateAdapter.doApprove(
				application.getCompanyID(), 
				application.getAppID(), 
				application.getEmployeeID(), 
				false, 
				application.getAppType().value, 
				application.getAppDate());
		// アルゴリズム「実績反映状態の判断」を実行する
		Boolean approvalCompletionFlag = approvalRootStateAdapter.isApproveAllComplete(
				application.getCompanyID(), 
				application.getAppID(), 
				application.getEmployeeID(), 
				false, 
				application.getAppType().value, 
				application.getAppDate());
		//承認完了フラグがtrue
		if (approvalCompletionFlag.equals(Boolean.TRUE)) {
			// 「反映情報」．実績反映状態を「反映待ち」にする
			application.getReflectionInformation().setStateReflectionReal(ReflectedState_New.WAITREFLECTION);
			applicationRepository.update(application);
		}
	}
}
