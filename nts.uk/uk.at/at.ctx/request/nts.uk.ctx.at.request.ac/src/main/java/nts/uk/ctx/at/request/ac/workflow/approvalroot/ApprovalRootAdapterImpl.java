package nts.uk.ctx.at.request.ac.workflow.approvalroot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;
import nts.arc.enums.EnumAdaptor;
import nts.arc.time.GeneralDate;
import nts.gul.collection.CollectionUtil;
import nts.uk.ctx.at.request.dom.application.common.adapter.bs.EmployeeRequestAdapter;
import nts.uk.ctx.at.request.dom.application.common.adapter.bs.dto.ConcurrentEmployeeRequest;
import nts.uk.ctx.at.request.dom.application.common.adapter.workflow.AgentAdapter;
import nts.uk.ctx.at.request.dom.application.common.adapter.workflow.ApprovalRootAdapter;
import nts.uk.ctx.at.request.dom.application.common.adapter.workflow.dto.AgentPubImport;
import nts.uk.ctx.at.request.dom.application.common.adapter.workflow.dto.ApprovalPhaseImport;
import nts.uk.ctx.at.request.dom.application.common.adapter.workflow.dto.ApprovalRootImport;
import nts.uk.ctx.at.request.dom.application.common.adapter.workflow.dto.ApproverInfoImport;
import nts.uk.ctx.at.request.dom.application.common.adapter.workflow.dto.ApproverRepresenterImport;
import nts.uk.ctx.at.request.dom.application.common.adapter.workflow.dto.ErrorFlagImport;
import nts.uk.ctx.bs.employee.pub.jobtitle.SyJobTitlePub;
import nts.uk.ctx.workflow.pub.agent.AgentPub;
import nts.uk.ctx.workflow.pub.approvalroot.ApprovalRootPub;
import nts.uk.ctx.workflow.pub.approvalroot.export.ApprovalPhaseExport;
import nts.uk.ctx.workflow.pub.approvalroot.export.ApprovalRootExport;
import nts.uk.ctx.workflow.pub.approvalroot.export.ApproverInfoExport;
import nts.uk.shr.com.context.AppContexts;

@Stateless
public class ApprovalRootAdapterImpl implements ApprovalRootAdapter
{

	@Inject
	private ApprovalRootPub approvalRootPub;
	
	@Inject
	private EmployeeRequestAdapter employeeAdapter;
	
	@Inject 
	private SyJobTitlePub syJobTitlePub;
	
	@Inject
	private AgentAdapter agentAdapter;

	@Override
	public List<ApprovalRootImport> getApprovalRootOfSubjectRequest(String cid, String sid, int employmentRootAtr,int appType, GeneralDate standardDate) {
		List<ApprovalRootExport> approvalRootData = this.approvalRootPub.getApprovalRootOfSubjectRequest(cid, sid, employmentRootAtr, appType, standardDate);
		if(CollectionUtil.isEmpty(approvalRootData)){
			return Collections.emptyList();
		}
		 
		List<ApprovalRootImport> approvalRootResult = approvalRootData.stream().map(x -> this.convertApprovalRootImport(x)).collect(Collectors.toList());
		List<String> approverSIDList = new ArrayList<>();
		approvalRootResult.stream().forEach(approvalRootImport -> {
			approvalRootImport.getBeforeApprovers().stream().forEach(approvalPhaseImport -> {
				approvalPhaseImport.getApprovers().stream().forEach(approverInfoImport -> {
					if(approverInfoImport.getSid() != null) {
						approverSIDList.add(approverInfoImport.getSid());
					}else if(approverInfoImport.getJobId() != null) {
						List<ConcurrentEmployeeRequest> lstEmployeeByJob = employeeAdapter.getConcurrentEmployee(cid,approverInfoImport.getJobId(), standardDate);
						if(!lstEmployeeByJob.isEmpty()) {
							for(ConcurrentEmployeeRequest emp : lstEmployeeByJob) {
								approverInfoImport.getApproverSIDList().add(emp.getEmployeeId());
								approverInfoImport.getApproverNameList().add(emp.getPersonName());
								approverSIDList.add(emp.getEmployeeId());
							}
						}
					}
				});
			});
		});
		
		AgentPubImport agentPubImport = agentAdapter.getApprovalAgencyInformation(cid, approverSIDList);
		List<ApproverRepresenterImport> representerList = agentPubImport.getListApproverAndRepresenterSID();
		approvalRootResult.stream().forEach(approvalRootImport -> {
			approvalRootImport.getBeforeApprovers().stream().forEach(approvalPhaseImport -> {
				approvalPhaseImport.getApprovers().stream().forEach(approverInfoImport -> {
					if(approverInfoImport.getSid() != null) {
						representerList.stream().filter(x -> x.getApprover().equals(approverInfoImport.getSid())).findAny()
						.map(y -> {
							if(!y.getRepresenter().equals("Empty")){
								approverInfoImport.addRepresenterSID(y.getRepresenter());
								approverInfoImport.addRepresenterName(employeeAdapter.getEmployeeName(approverInfoImport.getRepresenterSID()));
							}
							return null;
						}).orElse(null);
					}else if(approverInfoImport.getJobId() != null) {
						approverInfoImport.getApproverSIDList().forEach(item -> {
							representerList.stream().filter(x -> x.getApprover().equals(item)).findAny()
							.map(y -> {
								if(!y.getRepresenter().equals("Empty")){
									approverInfoImport.getRepresenterSIDList().add(y.getRepresenter());
									approverInfoImport.getRepresenterNameList().add(employeeAdapter.getEmployeeName(approverInfoImport.getRepresenterSID()));
								}
								return null;
							}).orElse(null);
						});
					}
				});
			});
		});
		return approvalRootResult;
	}
	/**
	 * convert ApprovalRootExport to ApprovalRootImport
	 * @param export
	 * @return
	 */
	private ApprovalRootImport convertApprovalRootImport(ApprovalRootExport export) {
		List<ApprovalPhaseImport> beforeApprovers = new ArrayList<>();
		export.getBeforeApprovers().stream().forEach(x ->{
			ApprovalPhaseImport beforeApprover = new ApprovalPhaseImport(x.getCompanyId(), x.getBranchId(), x.getApprovalPhaseId(), x.getApprovalForm(), x.getBrowsingPhase(), x.getOrderNumber());
			beforeApprovers.add(beforeApprover);
		});
		List<ApprovalPhaseImport> afterApprovers = new ArrayList<>();
		export.getAfterApprovers().stream().forEach(x -> {
			ApprovalPhaseImport afterApprover = new ApprovalPhaseImport(x.getCompanyId(), x.getBranchId(), x.getApprovalPhaseId(), x.getApprovalForm(), x.getBrowsingPhase(), x.getOrderNumber());
			afterApprovers.add(afterApprover);
		});
		ApprovalRootImport temp = new ApprovalRootImport(export.getCompanyId(),
				export.getWorkplaceId(),
				export.getApprovalId(),
				export.getEmployeeId(),
				export.getHistoryId(),
				export.getApplicationType(),
				export.getStartDate(),
				export.getEndDate(),
				export.getBranchId(),
				export.getAnyItemApplicationId(), 
				export.getConfirmationRootType(),
				export.getEmploymentRootAtr(), 
				beforeApprovers, 
				afterApprovers, 
				EnumAdaptor.valueOf(export.getErrorFlag() == null ? 0 : export.getErrorFlag().value, ErrorFlagImport.class));
		temp.addBeforeApprovers(
				export.getBeforeApprovers().stream()
				.map(x -> this.convertApprovalPhaseImport(x)).collect(Collectors.toList())
				);
		temp.addAfterApprovers(
				export.getAfterApprovers().stream()
				.map(x -> this.convertApprovalPhaseImport(x)).collect(Collectors.toList())
				);
		return temp;
		
	}

	/**
	 * covert ApprovalPhaseExport -> ApprovalPhaseImport
	 * @param approvalPhaseExport
	 * @return
	 */
	private ApprovalPhaseImport convertApprovalPhaseImport(ApprovalPhaseExport  approvalPhaseExport) {
		ApprovalPhaseImport temp = new  ApprovalPhaseImport(
				approvalPhaseExport.getCompanyId(),
				approvalPhaseExport.getBranchId(),
				approvalPhaseExport.getApprovalPhaseId(),
				approvalPhaseExport.getApprovalForm(),
				approvalPhaseExport.getBrowsingPhase(),
				approvalPhaseExport.getOrderNumber()
				);
		temp.addApproverList(approvalPhaseExport.getApprovers().stream().map(x -> this.convertApproverInfoImport(x)).collect(Collectors.toList()));
		return temp;
		
	}
	
	
	private ApproverInfoImport convertApproverInfoImport(ApproverInfoExport approverInfoExport) {
		String companyID = AppContexts.user().companyId();
		ApproverInfoImport temp = new  ApproverInfoImport(
				approverInfoExport.getJobId(), // jobID 
				approverInfoExport.getSid(),
				approverInfoExport.getApprovalPhaseId(),
				approverInfoExport.getIsConfirmPerson(),
				approverInfoExport.getOrderNumber(),
				approverInfoExport.getApprovalAtr() // int approvalAtr  = 0,1
				);
		if(approverInfoExport.getApprovalAtr() ==0) {//if pesson
			temp.addEmployeeName(employeeAdapter.getEmployeeName(approverInfoExport.getSid()));
		}
		if(approverInfoExport.getApprovalAtr() ==1) {
			temp.addEmployeeName(syJobTitlePub.findByJobId(companyID, approverInfoExport.getJobId(), GeneralDate.today()).get().getJobTitleName());

		}
		return temp;
		
	}

	@Override
	public List<ApproverInfoImport> convertToApprover(String cid, String sid, GeneralDate baseDate, String jobTitleId) {
		return  this.approvalRootPub.convertToApprover(cid, sid, baseDate, jobTitleId).stream()
				.map(x -> this.convertApproverInfoImport(x)).collect(Collectors.toList());
	}
	
	
}

