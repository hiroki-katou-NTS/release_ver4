package nts.uk.ctx.at.request.dom.application.common.service.detailscreen.after;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.uk.ctx.at.request.dom.application.common.Application;
import nts.uk.ctx.at.request.dom.application.common.ApplicationRepository;
import nts.uk.ctx.at.request.dom.application.common.ReflectPlanPerState;
import nts.uk.ctx.at.request.dom.application.common.appapprovalphase.AppApprovalPhase;
import nts.uk.ctx.at.request.dom.application.common.appapprovalphase.AppApprovalPhaseRepository;
import nts.uk.ctx.at.request.dom.application.common.appapprovalphase.ApprovalAtr;
import nts.uk.ctx.at.request.dom.application.common.approvalframe.ApprovalFrame;
import nts.uk.ctx.at.request.dom.application.common.approvalframe.ApprovalFrameRepository;
import nts.uk.ctx.at.request.dom.application.common.service.other.ApprovalAgencyInformationService;
import nts.uk.ctx.at.request.dom.application.common.service.other.DestinationJudgmentProcessService;
import nts.uk.ctx.at.request.dom.application.common.service.other.dto.ObjApproverRepresenter;
import nts.uk.ctx.at.request.dom.setting.request.application.apptypediscretesetting.AppTypeDiscreteSetting;
import nts.uk.ctx.at.request.dom.setting.request.application.apptypediscretesetting.AppTypeDiscreteSettingRepository;
import nts.uk.ctx.at.request.dom.setting.request.application.common.AppCanAtr;

/**
 * 
 * @author tutk
 *
 */
@Stateless
public class ProcessAfterReturnDefault implements ProcessAfterReturnService {

	@Inject
	private ApplicationRepository appRepo;
	@Inject
	private AppApprovalPhaseRepository phaseRepo;
	@Inject
	private ApprovalFrameRepository frameRepo;
	@Inject
	private AppTypeDiscreteSettingRepository appTypeSettingRepo;
	@Inject
	private ApprovalAgencyInformationService  approvalAgencyInformationServiceRepo;
	@Inject
	private DestinationJudgmentProcessService destinationJudgmentProcessServiceRepo;
	
	@Override
	public void detailScreenProcessAfterReturn(Application application, boolean checkApplicant, int orderPhase) {
		//String companyID = AppContexts.user().companyId();
		// if 差し戻し先が申請本人の場合(the applicant )
		
		//list application phase
		List<AppApprovalPhase> listPhase = phaseRepo.findPhaseByAppID(application.getCompanyID(), application.getApplicationID());
		// get information application setting by companyID  and app type
		Optional<AppTypeDiscreteSetting> appSetting = appTypeSettingRepo.getAppTypeDiscreteSettingByAppType(
				application.getCompanyID(), 
				application.getApplicationType().value);
		//list send email
		List<String> listDestination = new ArrayList<>(); 
		if(checkApplicant){
			for(AppApprovalPhase appApprovalPhase:listPhase) {
				appApprovalPhase.setApprovalForm(null);
				appApprovalPhase.setApprovalATR(null);
				// set value setApprovalForm and setApprovalATR = null
				phaseRepo.update(appApprovalPhase);
				List<ApprovalFrame> listFrame = frameRepo.getAllApproverByPhaseID(appApprovalPhase.getCompanyID(), appApprovalPhase.getPhaseID());
				for(ApprovalFrame approvalFrame:listFrame) {
					approvalFrame.setApprovalATR(null);
					approvalFrame.setApprovalDate(null);
					approvalFrame.setApproverSID(null);
					approvalFrame.setReason(null);
					approvalFrame.setRepresenterSID(null);
					// set value frame =  null
					frameRepo.update(approvalFrame);
				}
			}
			
			
			//update stateReflectionReal
			application.setReflectPerState(ReflectPlanPerState.REMAND);
			appRepo.updateApplication(application);
			
			//check : send mail when approval = can
			if(appSetting.get().getSendMailWhenApprovalFlg() == AppCanAtr.CAN) {
				//send mail 
			}
			
		}else {//else 差し戻し先が承認者の場合(the approver)
			for(int i=0;i<orderPhase;i++) {
				listPhase.get(i).setApprovalATR(ApprovalAtr.UNAPPROVED);
				listPhase.get(i).setApprovalForm(null);
				phaseRepo.update(listPhase.get(i));
				List<ApprovalFrame> listFrame = frameRepo.getAllApproverByPhaseID(
						listPhase.get(i).getCompanyID(), 
						listPhase.get(i).getPhaseID());
				for(ApprovalFrame approvalFrame:listFrame) {
					approvalFrame.setApprovalATR(null);
					approvalFrame.setApprovalDate(null);
					approvalFrame.setApproverSID(null);
					approvalFrame.setReason(null);
					approvalFrame.setRepresenterSID(null);
					// set value frame =  null
					frameRepo.update(approvalFrame);
				}
			}
			//check : send mail when register = can
			if(appSetting.get().getSendMailWhenRegisterFlg() == AppCanAtr.CAN) {
				//get 8.2 -> 3 -> 3.1
				List<String> listApprover = new ArrayList<>();
				//get List Approver And RepresenterSID
				List<ObjApproverRepresenter> getListApproverRep = approvalAgencyInformationServiceRepo
						.getApprovalAgencyInformation(
						application.getCompanyID(), 
						listApprover).getListApproverAndRepresenterSID();
				List<String> listSendMail = destinationJudgmentProcessServiceRepo
						.getDestinationJudgmentProcessService(getListApproverRep);
				// check duplicate and delete for list
				 listDestination = listSendMail.stream()
						.distinct()
						.collect(Collectors.toList());
				//send mail
			}
		}
		//show messeger 223
		if(listDestination.size()>0) {
			//Imported(就業)「社員」を取得する
			//情報メッセージに（Msg_392）を表示する
		}
			
		
	}


}
