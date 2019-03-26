package nts.uk.ctx.at.request.dom.application.common.service.detailscreen.before;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.arc.enums.EnumAdaptor;
import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.request.dom.application.ApplicationRepository_New;
import nts.uk.ctx.at.request.dom.application.Application_New;
import nts.uk.ctx.at.request.dom.application.ReflectPlanPerState;
import nts.uk.ctx.at.request.dom.application.common.adapter.workflow.AgentAdapter;
import nts.uk.ctx.at.request.dom.application.common.adapter.workflow.ApprovalRootStateAdapter;
import nts.uk.ctx.at.request.dom.application.common.adapter.workflow.dto.ApproverPersonImport;
import nts.uk.ctx.at.request.dom.application.common.appapprovalphase.ApprovalAtr;
//import nts.uk.ctx.at.request.dom.application.common.service.detailscreen.InitMode;
import nts.uk.ctx.at.request.dom.application.common.service.detailscreen.output.DetailedScreenPreBootModeOutput;
//import nts.uk.ctx.at.request.dom.application.common.service.detailscreen.output.OutputMode;
import nts.uk.ctx.at.request.dom.application.common.service.detailscreen.output.User;
import nts.uk.ctx.at.request.dom.application.common.service.other.OtherCommonAlgorithm;
import nts.uk.ctx.at.request.dom.application.common.service.other.output.PeriodCurrentMonth;

@Stateless
public class BeforePreBootModeImpl implements BeforePreBootMode {
	/** 14-2.詳細画面起動前モードの判断 */
	@Inject
	OtherCommonAlgorithm otherCommonAlgorithmService;

	@Inject
	AgentAdapter approvalAgencyInformationService;
	
	@Inject
	private ApprovalRootStateAdapter approvalRootStateAdapter;
	
	@Inject
	private ApplicationRepository_New applicationRepository;
	
//	@Inject
//	private InitMode initMode;
	
	@Override
	public DetailedScreenPreBootModeOutput judgmentDetailScreenMode(String companyID, String employeeID, String appID, GeneralDate baseDate) {
		
		Application_New applicationData = applicationRepository.findByID(companyID, appID).get();
		// Output variables
		DetailedScreenPreBootModeOutput outputData = new DetailedScreenPreBootModeOutput(User.OTHER, ReflectPlanPerState.NOTREFLECTED, false, ApprovalAtr.UNAPPROVED, false, false);
		if(applicationData.getEmployeeID().equals(employeeID)||applicationData.getEnteredPersonID().equals(employeeID)) {
			outputData.setLoginInputOrApproval(true);
		}
		//4.社員の当月の期間を算出する
		PeriodCurrentMonth listDate = otherCommonAlgorithmService.employeePeriodCurrentMonthCalculate(companyID,
				applicationData.getEmployeeID(), baseDate);
		GeneralDate startDate = listDate.getStartDate();
		// 締め開始日 >  ドメインモデル「申請」．申請日 がtrue
		if (startDate.after(applicationData.getAppDate())) {
			//ステータス = 過去申請(status= 過去申請)
			outputData.setReflectPlanState(ReflectPlanPerState.PASTAPP);
		} else {
			//ステータス = ドメインモデル「反映情報」．実績反映状態(Set status = 「反映情報」．実績反映状態)
			outputData.setReflectPlanState(EnumAdaptor.valueOf(applicationData.getReflectionInformation().getStateReflectionReal().value, ReflectPlanPerState.class));
		}	
		
		// get User
		// "Application".Applicant = login If employee ID is true
		//ログイン者が承認者かチェックする(Check xem login có phải là người approve ko?)
		
		Boolean isApprover = approvalRootStateAdapter.judgmentTargetPersonIsApprover(companyID, applicationData.getAppID(), employeeID);
		if(isApprover.equals(Boolean.TRUE)){
			if(applicationData.getEmployeeID().equals(employeeID)){
				outputData.setUser(User.APPLICANT_APPROVER);
			} else {
				outputData.setUser(User.APPROVER);
			}
		} else {
			if(applicationData.getEmployeeID().equals(employeeID)){
				outputData.setUser(User.APPLICANT);
			} else {
				outputData.setUser(User.OTHER);
			}
		}
		if(outputData.getUser().equals(User.APPLICANT_APPROVER)||outputData.getUser().equals(User.APPROVER)){
			ApproverPersonImport approverPersonImport = approvalRootStateAdapter.judgmentTargetPersonCanApprove(companyID, applicationData.getAppID(), employeeID);
			outputData.setAuthorizableFlags(approverPersonImport.getAuthorFlag());
			outputData.setApprovalATR(EnumAdaptor.valueOf(approverPersonImport.getApprovalAtr().value, ApprovalAtr.class));
			outputData.setAlternateExpiration(approverPersonImport.getExpirationAgentFlag());
		}
		return outputData;
	}
}