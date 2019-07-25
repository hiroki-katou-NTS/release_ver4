package nts.uk.ctx.workflow.dom.service;

import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.arc.time.GeneralDate;
import nts.uk.ctx.workflow.dom.approvermanagement.workroot.ApplicationType;
import nts.uk.ctx.workflow.dom.approvermanagement.workroot.EmploymentRootAtr;
import nts.uk.ctx.workflow.dom.approverstatemanagement.ApprovalRootState;
import nts.uk.ctx.workflow.dom.approverstatemanagement.ApprovalRootStateRepository;
import nts.uk.ctx.workflow.dom.approverstatemanagement.RootType;
import nts.uk.ctx.workflow.dom.service.output.ApprovalRootContentOutput;

/**
 * 
 * @author Doan Duy Hung
 *
 */
@Stateless
public class ApprovalRootStateImpl implements ApprovalRootStateService {
	
	@Inject
	private CollectApprovalRootService collectApprovalRootService;
	
	@Inject
	private ApprovalRootStateRepository approvalRootStateRepository;

	@Override
	public void insertAppRootType(String companyID, String employeeID, ApplicationType appType, 
			GeneralDate appDate, String appID, Integer rootType, GeneralDate baseDate) {
		ApprovalRootContentOutput approvalRootContentOutput = collectApprovalRootService.getApprovalRootOfSubjectRequest(companyID, employeeID, EmploymentRootAtr.APPLICATION, appType, baseDate);
		ApprovalRootState approvalRootState = approvalRootContentOutput.getApprovalRootState();
		approvalRootStateRepository.insert(companyID, ApprovalRootState.createFromFirst(
				companyID,
				appID,  
				RootType.EMPLOYMENT_APPLICATION, 
				approvalRootState.getHistoryID(), 
				appDate, 
				employeeID, 
				approvalRootState),
				rootType);
	}

	@Override
	public void delete(String rootStateID, Integer rootType) {
		approvalRootStateRepository.delete(rootStateID, rootType);
	}

	@Override
	public List<ApprovalRootState> getByPeriod(String employeeID, GeneralDate startDate, GeneralDate endDate, Integer rootType) {
		return approvalRootStateRepository.findAppByEmployeeIDRecordDate(
				startDate, 
				endDate, 
				employeeID, 
				rootType);
	}
}
