package nts.uk.ctx.workflow.dom.service;

import java.util.List;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;

import nts.arc.time.GeneralDate;
import nts.uk.ctx.workflow.dom.approvermanagement.workroot.ApplicationType;
import nts.uk.ctx.workflow.dom.approvermanagement.workroot.EmploymentRootAtr;
import nts.uk.ctx.workflow.dom.approverstatemanagement.ApprovalRootState;
import nts.uk.ctx.workflow.dom.approverstatemanagement.ApprovalRootStateRepository;
import nts.uk.ctx.workflow.dom.approverstatemanagement.RootType;
import nts.uk.ctx.workflow.dom.service.output.ApprovalRootContentOutput;
import nts.uk.shr.com.time.calendar.period.DatePeriod;

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
		approvalRootStateRepository.insert(ApprovalRootState.createFromFirst(companyID, appID,  employeeID, appDate, approvalRootState));
	}

	@Override
	public void delete(String rootStateID, Integer rootType) {
		approvalRootStateRepository.delete(rootStateID);
	}

	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	@Override
	public List<ApprovalRootState> getByPeriod(String employeeID, GeneralDate startDate, GeneralDate endDate, Integer rootType) {
		return approvalRootStateRepository.findAppApvRootStateByEmployee(new DatePeriod(startDate, endDate), employeeID);
	}
}
