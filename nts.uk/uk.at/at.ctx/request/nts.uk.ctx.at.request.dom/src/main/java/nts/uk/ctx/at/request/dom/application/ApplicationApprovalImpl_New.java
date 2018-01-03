package nts.uk.ctx.at.request.dom.application;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.transaction.Transactional;

import nts.uk.ctx.at.request.dom.application.common.adapter.workflow.ApprovalRootStateAdapter;
import nts.uk.ctx.at.request.dom.application.gobackdirectly.GoBackDirectlyRepository;
import nts.uk.ctx.at.request.dom.application.lateorleaveearly.LateOrLeaveEarlyRepository;
import nts.uk.ctx.at.request.dom.application.overtime.OvertimeRepository;
import nts.uk.ctx.at.request.dom.application.stamp.AppStampRepository;
import nts.uk.ctx.at.request.dom.application.workchange.IAppWorkChangeRepository;

/**
 * 
 * @author Doan Duy Hung
 *
 */
@Stateless
@Transactional
public class ApplicationApprovalImpl_New implements ApplicationApprovalService_New {

	@Inject
	private ApplicationRepository_New applicationRepository;
	
	@Inject
	private ApprovalRootStateAdapter approvalRootStateAdapter;
	
	@Inject
	private AppStampRepository appStampRepository;
	
	@Inject
	private OvertimeRepository overtimeRepository;
	
	@Inject
	private GoBackDirectlyRepository goBackDirectlyRepository;
	
	@Inject
	private IAppWorkChangeRepository workChangeRepository;
	
	@Inject
	private LateOrLeaveEarlyRepository lateOrLeaveEarlyRepository;
	
	@Override
	public void insert(Application_New application) {
		applicationRepository.insert(application);
		approvalRootStateAdapter.insertByAppType(
				application.getCompanyID(), 
				application.getEmployeeID(), 
				application.getAppType().value, 
				application.getAppDate(),
				application.getAppID());
	}

	@Override
	public void delete(String companyID, String appID, Long version, ApplicationType appType) {
		switch (appType) {
		case STAMP_APPLICATION:
			appStampRepository.delete(companyID, appID);
			break;
		case OVER_TIME_APPLICATION:
			overtimeRepository.delete(companyID, appID);
			break;
		case GO_RETURN_DIRECTLY_APPLICATION:
			goBackDirectlyRepository.delete(companyID, appID);
			break;
		case WORK_CHANGE_APPLICATION:
			workChangeRepository.delete(companyID, appID);
			break;
		case EARLY_LEAVE_CANCEL_APPLICATION: 
			lateOrLeaveEarlyRepository.remove(companyID, appID);
			break;
		default:
			break;
		}
		applicationRepository.delete(companyID, appID);
		approvalRootStateAdapter.deleteApprovalRootState(appID);
		
		
	}

}
