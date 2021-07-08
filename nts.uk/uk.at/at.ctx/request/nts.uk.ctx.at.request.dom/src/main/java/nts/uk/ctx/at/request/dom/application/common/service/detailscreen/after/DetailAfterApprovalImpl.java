package nts.uk.ctx.at.request.dom.application.common.service.detailscreen.after;

import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.uk.ctx.at.request.dom.application.Application;
import nts.uk.ctx.at.request.dom.application.ApplicationRepository;
import nts.uk.ctx.at.request.dom.application.ReflectedState;
import nts.uk.ctx.at.request.dom.application.ReflectionStatusOfDay;
import nts.uk.ctx.at.request.dom.application.common.adapter.workflow.ApprovalRootStateAdapter;
import nts.uk.ctx.at.request.dom.application.common.service.detailscreen.output.ProcessApprovalOutput;
import nts.uk.ctx.at.request.dom.setting.company.applicationapprovalsetting.applicationsetting.applicationtypesetting.AppTypeSetting;
import nts.uk.ctx.at.request.dom.setting.company.applicationapprovalsetting.applicationsetting.applicationtypesetting.service.ApprovalMailSendCheck;
import nts.uk.shr.com.context.AppContexts;

/**
 * 
 * @author Doan Duy Hung
 *
 */
@Stateless
public class DetailAfterApprovalImpl implements DetailAfterApproval {
	
	@Inject
	private ApprovalRootStateAdapter approvalRootStateAdapter;
	
	/*@Inject
	private AppReflectManager appReflectManager;*/
	
	@Inject
	private ApprovalMailSendCheck approvalMailSendCheck;
	
	@Inject
	private ApplicationRepository applicationRepository;
	
	@Override
	public ProcessApprovalOutput doApproval(String companyID, String appID, Application application, ApprovalProcessParam approvalProcessParam, String memo) {
		ProcessApprovalOutput processApprovalOutput = new ProcessApprovalOutput();
		String loginEmployeeID = AppContexts.user().employeeId();
		// 2.承認する(ApproveService)
		Integer phaseNumber = approvalRootStateAdapter.doApprove(appID, loginEmployeeID, memo);
		// アルゴリズム「承認全体が完了したか」を実行する ( Thực hiện thuật toán ''Đã hoàn thành toàn bộ approve hay chưa"
		Boolean allApprovalFlg = approvalRootStateAdapter.isApproveAllComplete(appID);
		String reflectAppId = "";
		if(allApprovalFlg.equals(Boolean.TRUE)){
			// 反映状態を「反映待ち」に変更する
			for(ReflectionStatusOfDay reflectionStatusOfDay : application.getReflectionStatus().getListReflectionStatusOfDay()) {
				reflectionStatusOfDay.setActualReflectStatus(ReflectedState.WAITREFLECTION);
				reflectionStatusOfDay.setActualReflectStatus(ReflectedState.WAITREFLECTION);
			}
			applicationRepository.update(application);
			reflectAppId = application.getAppID();
			// 反映対象なのかチェックする(check xem có phải đối tượng phản ánh hay k?)
			// Gọi webservice riêng
//			if((application.isPreApp() && (application.isOverTimeApp() || application.isHolidayWorkApp()))
//				|| application.isWorkChangeApp()
//				|| application.isGoReturnDirectlyApp()){
//				// 社員の申請を反映(phản ánh employee application)
//				//appReflectManager.reflectEmployeeOfApp(application, ExecutionTypeExImport.NORMAL_EXECUTION, "", 0);
//			}
		} else {
			// 反映状態を「未反映」に変更する
			for(ReflectionStatusOfDay reflectionStatusOfDay : application.getReflectionStatus().getListReflectionStatusOfDay()) {
				reflectionStatusOfDay.setActualReflectStatus(ReflectedState.NOTREFLECTED);
				reflectionStatusOfDay.setActualReflectStatus(ReflectedState.NOTREFLECTED);
			}
			applicationRepository.update(application);
			// INPUT．申請表示情報．申請表示情報(基準日関係なし)．メールサーバ設定済区分をチェックする
			if(!approvalProcessParam.isMailServerSet()) {
				processApprovalOutput.setAppID(appID);
				processApprovalOutput.setReflectAppId(reflectAppId);
				return processApprovalOutput;
			}
		}
		// アルゴリズム「承認処理後にメールを自動送信するか判定」を実行する ( Thực hiện thuật toán「Xác định có tự động gửi thư sau khi xử lý phê duyệt hay không」 
		AppTypeSetting appTypeSetting = approvalProcessParam.getAppTypeSetting();
		String applicantResult = approvalMailSendCheck.sendMailApplicant(
				appTypeSetting,
				application, 
				allApprovalFlg);
		processApprovalOutput.setApplicant(applicantResult);
		// 承認処理後にメールを承認者に送信判定
		List<String> approverLstResult = approvalMailSendCheck.sendMailApprover(
				appTypeSetting,
				application, 
				phaseNumber);
		processApprovalOutput.setApproverLst(approverLstResult);
		processApprovalOutput.setAppID(appID);
		processApprovalOutput.setReflectAppId(reflectAppId);
		return processApprovalOutput;
	}

}
