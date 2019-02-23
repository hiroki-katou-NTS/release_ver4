package nts.uk.ctx.workflow.pubimp.spr;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.arc.time.GeneralDate;
import nts.uk.ctx.workflow.dom.agent.Agent;
import nts.uk.ctx.workflow.dom.agent.AgentRepository;
import nts.uk.ctx.workflow.dom.approvermanagement.workroot.ApprovalPhaseRepository;
import nts.uk.ctx.workflow.dom.approvermanagement.workroot.CompanyApprovalRootRepository;
import nts.uk.ctx.workflow.dom.approvermanagement.workroot.PersonApprovalRootRepository;
import nts.uk.ctx.workflow.dom.approvermanagement.workroot.WorkplaceApprovalRootRepository;
import nts.uk.ctx.workflow.dom.approverstatemanagement.ApprovalRootState;
import nts.uk.ctx.workflow.dom.approverstatemanagement.ApprovalRootStateRepository;
import nts.uk.ctx.workflow.dom.service.JudgmentApprovalStatusService;
import nts.uk.ctx.workflow.dom.service.output.ApproverPersonOutput;
import nts.uk.ctx.workflow.pub.spr.SprApprovalSearchPub;
import nts.uk.ctx.workflow.pub.spr.export.ApprovalComSprExport;
import nts.uk.ctx.workflow.pub.spr.export.ApprovalPersonSprExport;
import nts.uk.ctx.workflow.pub.spr.export.ApprovalPhaseSprExport;
import nts.uk.ctx.workflow.pub.spr.export.ApprovalRootStateSprExport;
import nts.uk.ctx.workflow.pub.spr.export.ApprovalWorkplaceSprExport;
import nts.uk.ctx.workflow.pub.spr.export.ApproverSprExport;
import nts.uk.ctx.workflow.pub.spr.export.JudgmentSprExport;
import nts.uk.shr.com.time.calendar.period.DatePeriod;
/**
 * 
 * @author Doan Duy Hung
 *
 */
@Stateless
public class SprApprovalSearchPubImpl implements SprApprovalSearchPub {
	
	@Inject
	private CompanyApprovalRootRepository companyApprovalRootRepository;
	
	@Inject
	private WorkplaceApprovalRootRepository workplaceApprovalRootRepository; 
	
	@Inject
	private PersonApprovalRootRepository personApprovalRootRepository; 
	
	@Inject
	private ApprovalPhaseRepository approvalPhaseRepository;
	
	@Inject
	private ApprovalRootStateRepository approvalRootStateRepository;
	
	@Inject
	private JudgmentApprovalStatusService judgmentApprovalStatusService;
	
	@Inject
	private AgentRepository agentRepository;

	@Override
	public List<ApprovalComSprExport> getApprovalRootCom(String companyID, GeneralDate date, Integer employmentRootAtr,
			Integer confirmRootAtr) {
		return companyApprovalRootRepository.getComAppRoot(companyID, date, employmentRootAtr, confirmRootAtr)
				.stream().map(x -> new ApprovalComSprExport(
						companyID, 
						x.getApprovalId(), 
						x.getApplicationType() == null ? null : x.getApplicationType().value, 
						x.getBranchId(), 
						x.getAnyItemApplicationId(), 
						confirmRootAtr, 
						employmentRootAtr))
				.collect(Collectors.toList());
	}

	@Override
	public List<ApprovalPhaseSprExport> getAllIncludeApprovers(String companyId, String branchId) {
		return approvalPhaseRepository.getAllIncludeApprovers(companyId, branchId)
			.stream().map(x -> ApprovalPhaseSprExport.createFromJavaType(
					x.getCompanyId(), 
					x.getBranchId(), 
					x.getApprovalPhaseId(), 
					x.getApprovalForm().value, 
					x.getBrowsingPhase(), 
					x.getOrderNumber(), 
					x.getApprovers().stream().map(y -> ApproverSprExport.createFromJavaType(
							y.getCompanyId(), 
							y.getBranchId(), 
							y.getApprovalPhaseId(), 
							y.getApproverId(), 
							y.getJobTitleId(), 
							y.getEmployeeId(), 
							y.getOrderNumber(), 
							y.getApprovalAtr().value, 
							y.getConfirmPerson().value)).collect(Collectors.toList())))
			.collect(Collectors.toList());
	}

	@Override
	public List<ApprovalWorkplaceSprExport> getApprovalRootWp(String companyID, GeneralDate date, Integer employmentRootAtr,
			Integer confirmRootAtr) {
		return workplaceApprovalRootRepository.getWpAppRoot(companyID, date, employmentRootAtr, confirmRootAtr)
				.stream().map(x -> new ApprovalWorkplaceSprExport(
						x.getCompanyId(), 
						x.getApprovalId(), 
						x.getWorkplaceId(), 
						x.getBranchId(), 
						x.getAnyItemApplicationId(), 
						confirmRootAtr, 
						employmentRootAtr, 
						x.getApplicationType() == null ? null : x.getApplicationType().value))
				.collect(Collectors.toList());
	}

	@Override
	public List<ApprovalPersonSprExport> getApprovalRootPs(String companyID, GeneralDate date, Integer employmentRootAtr,
			Integer confirmRootAtr) {
		return personApprovalRootRepository.getPsAppRoot(companyID, date, employmentRootAtr, confirmRootAtr)
				.stream().map(x -> new ApprovalPersonSprExport(
						x.getCompanyId(), 
						x.getApprovalId(), 
						x.getEmployeeId(), 
						x.getApplicationType() == null ? null : x.getApplicationType().value, 
						x.getBranchId(), 
						x.getAnyItemApplicationId(), 
						confirmRootAtr, 
						employmentRootAtr))
				.collect(Collectors.toList());
	}
	
	@Override
	public List<ApprovalRootStateSprExport> getAppByApproverDate(String companyID, String approverID, GeneralDate date) {
		List<ApprovalRootState> result =  new ArrayList<>();
		List<ApprovalRootState> approverLst = approvalRootStateRepository.getByApproverPeriod(companyID, approverID, new DatePeriod(date, date));
		result.addAll(approverLst);
		List<Agent> agentInfoOutputs = agentRepository.findAgentForSpr(companyID, approverID, date, date);
		agentInfoOutputs.forEach(agent -> {
			List<ApprovalRootState> approverAgentLst = approvalRootStateRepository.getByApproverAgentPeriod(companyID, approverID, 
					new DatePeriod(date, date), 
					new DatePeriod(agent.getStartDate(), agent.getEndDate()));
			result.addAll(approverAgentLst);
		});
		return result.stream().map(x -> new ApprovalRootStateSprExport(
					x.getRootStateID(), 
					x.getRootType().value, 
					x.getHistoryID(), 
					x.getApprovalRecordDate(), 
					x.getEmployeeID()))
			.collect(Collectors.toList());
	}

	@Override
	public JudgmentSprExport judgmentTargetPersonCanApprove(String companyID, String rootStateID, String employeeID, Integer rootType) {
		ApproverPersonOutput approverPersonOutput = judgmentApprovalStatusService
				.judgmentTargetPersonCanApprove(companyID, rootStateID, employeeID, rootType);
		return new JudgmentSprExport(
				approverPersonOutput.getAuthorFlag(), 
				approverPersonOutput.getApprovalAtr().value, 
				approverPersonOutput.getExpirationAgentFlag());
	}
	
}
