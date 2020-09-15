package nts.uk.ctx.at.request.dom.application.common.service.detailscreen.after;

import java.util.ArrayList;
//import java.util.Arrays;
import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.uk.ctx.at.request.dom.application.Application;
import nts.uk.ctx.at.request.dom.application.ApplicationRepository;
import nts.uk.ctx.at.request.dom.application.ReflectedState;
import nts.uk.ctx.at.request.dom.application.ReflectionStatusOfDay;
import nts.uk.ctx.at.request.dom.application.common.adapter.workflow.ApprovalRootStateAdapter;
import nts.uk.ctx.at.request.dom.application.common.service.other.output.ProcessResult;
import nts.uk.ctx.at.request.dom.application.common.service.setting.output.AppDispInfoStartupOutput;
import nts.uk.ctx.at.request.dom.setting.company.applicationapprovalsetting.applicationsetting.applicationtypesetting.service.ApprovalMailSendCheck;
import nts.uk.ctx.at.request.dom.setting.company.applicationapprovalsetting.applicationsetting.applicationtypesetting.service.NewRegisterMailSendCheck;
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
	private NewRegisterMailSendCheck newRegisterMailSendCheck;
	
	@Inject
	private ApplicationRepository applicationRepository;
	
	@Override
	public ProcessResult doApproval(String companyID, String appID, Application application, AppDispInfoStartupOutput appDispInfoStartupOutput, String memo) {
		boolean isProcessDone = true;
		boolean isAutoSendMail = false;
		List<String> autoSuccessMail = new ArrayList<>();
		List<String> autoFailMail = new ArrayList<>();
		List<String> autoFailServer = new ArrayList<>();
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
			if((application.isPreApp() && (application.isOverTimeApp() || application.isHolidayWorkApp()))
				|| application.isWorkChangeApp()
				|| application.isGoReturnDirectlyApp()){
				// 社員の申請を反映(phản ánh employee application)
				//appReflectManager.reflectEmployeeOfApp(application);
			}
		} else {
			// 反映状態を「未反映」に変更する
			for(ReflectionStatusOfDay reflectionStatusOfDay : application.getReflectionStatus().getListReflectionStatusOfDay()) {
				reflectionStatusOfDay.setActualReflectStatus(ReflectedState.NOTREFLECTED);
				reflectionStatusOfDay.setActualReflectStatus(ReflectedState.NOTREFLECTED);
			}
			applicationRepository.update(application);
			// INPUT．申請表示情報．申請表示情報(基準日関係なし)．メールサーバ設定済区分をチェックする
			if(!appDispInfoStartupOutput.getAppDispInfoNoDateOutput().isMailServerSet()) {
				return new ProcessResult(isProcessDone, isAutoSendMail, autoSuccessMail, autoFailMail, autoFailServer, appID, reflectAppId);
			}
		}
//		isAutoSendMail = true;
//		// アルゴリズム「承認処理後にメールを自動送信するか判定」を実行する ( Thực hiện thuật toán「Xác định có tự động gửi thư sau khi xử lý phê duyệt hay không」
//		// TODO: 申請設定 domain has changed!
//		AppTypeSetting appTypeSetting = appDispInfoStartupOutput.getAppDispInfoNoDateOutput().getApplicationSetting().getAppTypeSettings()
//				.stream().filter(x -> x.getAppType()==application.getAppType()).findAny().orElse(null);
//		ProcessResult processResult1 = approvalMailSendCheck.sendMail(
//				appTypeSetting,
//				application, 
//				allApprovalFlg);
//		autoSuccessMail.addAll(processResult1.getAutoSuccessMail());
//		autoFailMail.addAll(processResult1.getAutoFailMail());
//		autoFailServer.addAll(processResult1.getAutoFailServer());
//		// アルゴリズム「新規登録時のメール送信判定」を実行する ( Thực hiện thuật toán 「 Xác định gửi mail khi đăng ký mới」
//		// TODO: 申請設定 domain has changed!
//		ProcessResult processResult2 = newRegisterMailSendCheck.sendMail(
//				appTypeSetting,
//				application, 
//				phaseNumber);
//		autoSuccessMail.addAll(processResult2.getAutoSuccessMail());
//		autoFailMail.addAll(processResult2.getAutoFailMail());
//		autoFailServer.addAll(processResult2.getAutoFailServer());
		return new ProcessResult(isProcessDone, isAutoSendMail, autoSuccessMail, autoFailMail, autoFailServer, appID, reflectAppId);
	}

}
