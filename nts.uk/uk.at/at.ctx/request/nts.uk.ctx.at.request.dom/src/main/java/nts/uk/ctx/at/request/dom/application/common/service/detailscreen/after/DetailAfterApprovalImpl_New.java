package nts.uk.ctx.at.request.dom.application.common.service.detailscreen.after;

import java.util.ArrayList;
//import java.util.Arrays;
import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.uk.ctx.at.request.dom.application.AppReason;
import nts.uk.ctx.at.request.dom.application.ApplicationRepository_New;
//import nts.uk.ctx.at.request.dom.application.ApplicationType;
import nts.uk.ctx.at.request.dom.application.Application_New;
//import nts.uk.ctx.at.request.dom.application.PrePostAtr;
import nts.uk.ctx.at.request.dom.application.ReflectedState_New;
import nts.uk.ctx.at.request.dom.application.common.adapter.workflow.ApprovalRootStateAdapter;
import nts.uk.ctx.at.request.dom.application.common.service.other.OtherCommonAlgorithm;
import nts.uk.ctx.at.request.dom.application.common.service.other.output.MailResult;
import nts.uk.ctx.at.request.dom.application.common.service.other.output.ProcessResult;
//import nts.uk.ctx.at.request.dom.applicationreflect.service.AppReflectManager;
import nts.uk.ctx.at.request.dom.setting.request.application.apptypediscretesetting.AppTypeDiscreteSetting;
import nts.uk.ctx.at.request.dom.setting.request.application.apptypediscretesetting.AppTypeDiscreteSettingRepository;
import nts.uk.ctx.at.request.dom.setting.request.application.common.AppCanAtr;

/**
 * 
 * @author Doan Duy Hung
 *
 */
@Stateless
public class DetailAfterApprovalImpl_New implements DetailAfterApproval_New {
	
	@Inject
	private ApprovalRootStateAdapter approvalRootStateAdapter;
	
	@Inject
	private AppTypeDiscreteSettingRepository discreteRepo;
	
	@Inject
	private ApplicationRepository_New applicationRepository;
	
//	@Inject
//	private AppReflectManager appReflectManager;
	
	@Inject
	private OtherCommonAlgorithm otherCommonAlgorithm;
	
	@Override
	public ProcessResult doApproval(String companyID, String appID, String employeeID, String memo, String appReason, boolean isUpdateAppReason) {
		boolean isProcessDone = true;
		boolean isAutoSendMail = false;
		List<String> autoSuccessMail = new ArrayList<>();
		List<String> autoFailMail = new ArrayList<>();
		Application_New application = applicationRepository.findByID(companyID, appID).get();
		if(isUpdateAppReason){
			application.setAppReason(new AppReason(appReason));
		}
		Integer phaseNumber = approvalRootStateAdapter.doApprove(
				companyID, 
				appID, 
				employeeID, 
				false, 
				application.getAppType().value, 
				application.getAppDate(), 
				memo);
		Boolean allApprovalFlg = approvalRootStateAdapter.isApproveAllComplete(
				companyID, 
				appID, 
				employeeID, 
				false, 
				application.getAppType().value, 
				application.getAppDate());
		applicationRepository.updateWithVersion(application);
		String reflectAppId = "";
		if(allApprovalFlg.equals(Boolean.TRUE)){
			// 実績反映状態 = 反映状態．反映待ち
			application.getReflectionInformation().setStateReflectionReal(ReflectedState_New.WAITREFLECTION);
			//予定反映状態 = 反映状態．反映待ち
			application.getReflectionInformation().setStateReflection(ReflectedState_New.WAITREFLECTION);
			applicationRepository.update(application);
			reflectAppId = application.getAppID();
//			if((application.getPrePostAtr().equals(PrePostAtr.PREDICT)&&
//					(application.getAppType().equals(ApplicationType.OVER_TIME_APPLICATION)
//					|| application.getAppType().equals(ApplicationType.BREAK_TIME_APPLICATION)))
//				|| application.getAppType().equals(ApplicationType.GO_RETURN_DIRECTLY_APPLICATION)
//				|| application.getAppType().equals(ApplicationType.WORK_CHANGE_APPLICATION)
//				|| application.getAppType().equals(ApplicationType.ABSENCE_APPLICATION)
//				|| application.getAppType().equals(ApplicationType.COMPLEMENT_LEAVE_APPLICATION)){
//				appReflectManager.reflectEmployeeOfApp(application);
//			}
		} else {
			// ドメインモデル「申請」と紐付き「反映情報」．実績反映状態 = 反映状態．未反映
			application.getReflectionInformation().setStateReflectionReal(ReflectedState_New.NOTREFLECTED);
			applicationRepository.update(application);
		}
		AppTypeDiscreteSetting discreteSetting = discreteRepo.getAppTypeDiscreteSettingByAppType(companyID, application.getAppType().value).get();
		// 承認処理時に自動でメールを送信するが trueの場合
		if (discreteSetting.getSendMailWhenApprovalFlg().equals(AppCanAtr.CAN)) {
			isAutoSendMail = true;
			if(allApprovalFlg.equals(Boolean.TRUE)){
				MailResult applicantResult = otherCommonAlgorithm.sendMailApplicantApprove(application);
				autoSuccessMail.addAll(applicantResult.getSuccessList());
				autoFailMail.addAll(applicantResult.getFailList());
			}
		}
		
		if (discreteSetting.getSendMailWhenRegisterFlg().equals(AppCanAtr.CAN)) {
			isAutoSendMail = true;
			boolean phaseComplete = approvalRootStateAdapter.isApproveApprovalPhaseStateComplete(companyID, appID, phaseNumber);
			if(phaseComplete){
				List<String> destination = approvalRootStateAdapter.getNextApprovalPhaseStateMailList(
						companyID, 
						application.getAppID(), 
						phaseNumber + 1, 
						false, 
						employeeID, 
						application.getAppType().value, 
						application.getAppDate());
				MailResult applicantResult = otherCommonAlgorithm.sendMailApproverApprove(destination, application);
				autoSuccessMail.addAll(applicantResult.getSuccessList());
				autoFailMail.addAll(applicantResult.getFailList());
			}
		}
		return new ProcessResult(isProcessDone, isAutoSendMail, autoSuccessMail, autoFailMail, appID, reflectAppId);
	}

}
