package nts.uk.ctx.at.request.dom.application.common.service.detailscreen.after;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.request.dom.application.ApplicationRepository_New;
import nts.uk.ctx.at.request.dom.application.Application_New;
import nts.uk.ctx.at.request.dom.application.ReflectedState_New;
import nts.uk.ctx.at.request.dom.application.common.adapter.workflow.ApprovalRootStateAdapter;
import nts.uk.ctx.at.request.dom.application.common.service.other.OtherCommonAlgorithm;
import nts.uk.ctx.at.request.dom.application.common.service.other.output.MailResult;
import nts.uk.ctx.at.request.dom.application.common.service.other.output.ProcessResult;
import nts.uk.ctx.at.request.dom.setting.request.application.apptypediscretesetting.AppTypeDiscreteSetting;
import nts.uk.ctx.at.request.dom.setting.request.application.apptypediscretesetting.AppTypeDiscreteSettingRepository;
import nts.uk.ctx.at.request.dom.setting.request.application.common.AppCanAtr;
import nts.uk.ctx.at.shared.dom.remainingnumber.algorithm.InterimRemainDataMngRegisterDateChange;

/**
 * 
 * @author Doan Duy Hung
 *
 */
@Stateless
public class DetailAfterDenyImpl implements DetailAfterDeny {
	
	@Inject
	private ApprovalRootStateAdapter approvalRootStateAdapter;
	
	@Inject
	private ApplicationRepository_New applicationRepository;
	
	@Inject
	private AppTypeDiscreteSettingRepository discreteRepo;
	
	@Inject
	private OtherCommonAlgorithm otherCommonAlgorithm;
	
	@Inject
	private InterimRemainDataMngRegisterDateChange interimRemainDataMngRegisterDateChange;

	@Override
	public ProcessResult doDeny(String companyID, String appID, String employeeID, String memo) {
		boolean isProcessDone = false;
		boolean isAutoSendMail = false;
		List<String> autoSuccessMail = new ArrayList<>();
		List<String> autoFailMail = new ArrayList<>();
		Application_New application = applicationRepository.findByID(companyID, appID).get();
		Boolean releaseFlg = approvalRootStateAdapter.doDeny(companyID, appID, employeeID, memo);
		if(releaseFlg.equals(Boolean.TRUE)){
			isProcessDone = true;
			application.getReflectionInformation().setStateReflectionReal(ReflectedState_New.DENIAL);
			applicationRepository.updateWithVersion(application);
			
			// 暫定データの登録
			List<GeneralDate> dateLst = new ArrayList<>();
			GeneralDate startDate = application.getStartDate().orElse(application.getAppDate());
			GeneralDate endDate = application.getEndDate().orElse(application.getAppDate());
			for(GeneralDate loopDate = startDate; loopDate.beforeOrEquals(endDate); loopDate = loopDate.addDays(1)){
				dateLst.add(loopDate);
			}
			interimRemainDataMngRegisterDateChange.registerDateChange(companyID, application.getEmployeeID(), dateLst);
			
			AppTypeDiscreteSetting discreteSetting = discreteRepo.getAppTypeDiscreteSettingByAppType(companyID, application.getAppType().value).get();
			if (discreteSetting.getSendMailWhenApprovalFlg().equals(AppCanAtr.CAN)) {
				isAutoSendMail = true;
				MailResult mailResult = otherCommonAlgorithm.sendMailApplicantDeny(application); 
				autoSuccessMail = mailResult.getSuccessList();
				autoFailMail = mailResult.getFailList();
			}
		}
		return new ProcessResult(isProcessDone, isAutoSendMail, autoSuccessMail, autoFailMail, appID,"");
	}

}
