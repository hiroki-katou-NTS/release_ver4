package nts.uk.ctx.at.request.dom.application.common.service.newscreen;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.request.dom.application.ApplicationRepository_New;
import nts.uk.ctx.at.request.dom.application.ApplicationType;
import nts.uk.ctx.at.request.dom.application.Application_New;
import nts.uk.ctx.at.request.dom.application.PrePostAtr;
import nts.uk.ctx.at.request.dom.application.ReflectedState_New;
import nts.uk.ctx.at.request.dom.application.common.adapter.workflow.ApprovalRootStateAdapter;
import nts.uk.ctx.at.request.dom.applicationreflect.service.AppReflectManagerFromRecord;
import nts.uk.ctx.at.request.dom.applicationreflect.service.InformationSettingOfAppForReflect;
import nts.uk.ctx.at.request.dom.applicationreflect.service.InformationSettingOfEachApp;
import nts.uk.ctx.at.request.dom.applicationreflect.service.workrecord.dailymonthlyprocessing.ExecutionTypeExImport;
import nts.uk.shr.com.time.calendar.period.DatePeriod;

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
	@Inject
	private AppReflectManagerFromRecord appReflectManager;
	@Inject
	private InformationSettingOfAppForReflect appSetting;
	@Override
	public void newScreenRegisterAtApproveInfoReflect(String SID, Application_New application) {
		// アルゴリズム「承認情報の整理」を実行する
		approvalRootStateAdapter.doApprove(
				application.getCompanyID(), 
				application.getAppID(), 
				application.getEmployeeID(), 
				false, 
				application.getAppType().value, 
				application.getAppDate(),
				"");
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
			Application_New application_New = applicationRepository.findByID(application.getCompanyID(), application.getAppID()).get();
			// 「反映情報」．実績反映状態を「反映待ち」にする
			application_New.getReflectionInformation().setStateReflectionReal(ReflectedState_New.WAITREFLECTION);
			applicationRepository.update(application_New);
			if((application.getPrePostAtr().equals(PrePostAtr.PREDICT)&&
					application.getAppType().equals(ApplicationType.OVER_TIME_APPLICATION))
				|| application.getAppType().equals(ApplicationType.BREAK_TIME_APPLICATION)
				|| application.getAppType().equals(ApplicationType.GO_RETURN_DIRECTLY_APPLICATION)
				|| application.getAppType().equals(ApplicationType.WORK_CHANGE_APPLICATION)
				|| application.getAppType().equals(ApplicationType.ABSENCE_APPLICATION)
				|| application.getAppType().equals(ApplicationType.COMPLEMENT_LEAVE_APPLICATION)){
				InformationSettingOfEachApp reflectSetting = appSetting.getSettingOfEachApp();
				GeneralDate startDate = application.getStartDate().isPresent() ? application.getStartDate().get() : application.getAppDate();
				GeneralDate endDate = application.getEndDate().isPresent() ? application.getEndDate().get() : application.getAppDate();
				appReflectManager.reflectAppOfAppDate("",
						application.getEmployeeID(),
						ExecutionTypeExImport.RERUN,
						reflectSetting,
						new DatePeriod(startDate, endDate));
			}
		}
	}
}
