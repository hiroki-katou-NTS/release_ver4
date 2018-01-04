package nts.uk.ctx.at.request.ac.workflow.approvalrootstate;

import java.util.List;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EnumType;

import nts.arc.enums.EnumAdaptor;
import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.request.dom.application.common.adapter.workflow.ApprovalRootStateAdapter;
import nts.uk.ctx.at.request.dom.application.common.adapter.workflow.dto.AgentPubImport;
import nts.uk.ctx.at.request.dom.application.common.adapter.workflow.dto.ApprovalBehaviorAtrImport_New;
import nts.uk.ctx.at.request.dom.application.common.adapter.workflow.dto.ApprovalFrameImport_New;
import nts.uk.ctx.at.request.dom.application.common.adapter.workflow.dto.ApprovalPhaseStateImport_New;
import nts.uk.ctx.at.request.dom.application.common.adapter.workflow.dto.ApprovalRootContentImport_New;
import nts.uk.ctx.at.request.dom.application.common.adapter.workflow.dto.ApprovalRootStateImport_New;
import nts.uk.ctx.at.request.dom.application.common.adapter.workflow.dto.ApproverApprovedImport_New;
import nts.uk.ctx.at.request.dom.application.common.adapter.workflow.dto.ApproverPersonImport;
import nts.uk.ctx.at.request.dom.application.common.adapter.workflow.dto.ApproverRepresenterImport;
import nts.uk.ctx.at.request.dom.application.common.adapter.workflow.dto.ApproverStateImport_New;
import nts.uk.ctx.at.request.dom.application.common.adapter.workflow.dto.ApproverWithFlagImport_New;
import nts.uk.ctx.at.request.dom.application.common.adapter.workflow.dto.ErrorFlagImport;
import nts.uk.ctx.at.request.dom.application.common.adapter.workflow.dto.RepresenterInformationImport;
import nts.uk.ctx.workflow.pub.agent.AgentPubExport;
import nts.uk.ctx.workflow.pub.agent.ApproverRepresenterExport;
import nts.uk.ctx.workflow.pub.service.ApprovalRootStatePub;
import nts.uk.ctx.workflow.pub.service.export.ApprovalRootContentExport;
import nts.uk.ctx.workflow.pub.service.export.ApprovalRootStateExport;
import nts.uk.ctx.workflow.pub.service.export.ApproverApprovedExport;
import nts.uk.ctx.workflow.pub.service.export.ApproverPersonExport;
/**
 * 
 * @author Doan Duy Hung
 *
 */
@Stateless
public class ApprovalRootStateAdapterImpl implements ApprovalRootStateAdapter {
	
	@Inject
	private ApprovalRootStatePub approvalRootStatePub;
	
	@Override
	public ApprovalRootContentImport_New getApprovalRootContent(String companyID, String employeeID, Integer appTypeValue, GeneralDate appDate, String appID, Boolean isCreate) {
		ApprovalRootContentExport approvalRootContentExport = approvalRootStatePub.getApprovalRoot(companyID, employeeID, appTypeValue, appDate, appID, isCreate);
		return new ApprovalRootContentImport_New(
					new ApprovalRootStateImport_New(
						approvalRootContentExport.getApprovalRootState().getListApprovalPhaseState().stream()
						.map(x -> {
							return new ApprovalPhaseStateImport_New(
									x.getPhaseOrder(), 
									x.getApprovalAtr(), 
									x.getListApprovalFrame().stream()
									.map(y -> {
										return new ApprovalFrameImport_New(
												y.getPhaseOrder(), 
												y.getFrameOrder(), 
												y.getApprovalAtr(), 
												y.getListApprover().stream().map(z -> new ApproverStateImport_New(z.getApproverID(), z.getRepresenterID())).collect(Collectors.toList()), 
												y.getApproverID(), 
												y.getRepresenterID(), 
												y.getApprovalReason());
									}).collect(Collectors.toList()));
						}).collect(Collectors.toList())),
					EnumAdaptor.valueOf(approvalRootContentExport.getErrorFlag().value, ErrorFlagImport.class));
	}

	@Override
	public void insertByAppType(String companyID, String employeeID, Integer appTypeValue, GeneralDate date, String appID) {
		approvalRootStatePub.insertAppRootType(companyID, employeeID, appTypeValue, date, appID);
	}

	@Override
	public List<String> getNextApprovalPhaseStateMailList(String companyID, String rootStateID,
			Integer approvalPhaseStateNumber, Boolean isCreate, String employeeID, Integer appTypeValue,
			GeneralDate appDate) {
		return approvalRootStatePub.getNextApprovalPhaseStateMailList(companyID, rootStateID, approvalPhaseStateNumber, isCreate, employeeID, appTypeValue, appDate);
	}

	@Override
	public Integer doApprove(String companyID, String rootStateID, String employeeID, Boolean isCreate,
			Integer appTypeValue, GeneralDate appDate) {
		return approvalRootStatePub.doApprove(companyID, rootStateID, employeeID, isCreate, appTypeValue, appDate);
	}

	@Override
	public Boolean isApproveAllComplete(String companyID, String rootStateID, String employeeID, Boolean isCreate,
			Integer appTypeValue, GeneralDate appDate) {
		// TODO Auto-generated method stub
		return approvalRootStatePub.isApproveAllComplete(companyID, rootStateID, employeeID, isCreate, appTypeValue, appDate);
	}

	@Override
	public void doReleaseAllAtOnce(String companyID, String rootStateID) {
		approvalRootStatePub.doReleaseAllAtOnce(companyID, rootStateID);
	}

	@Override
	public ApproverApprovedImport_New getApproverApproved(String rootStateID) {
		ApproverApprovedExport approverApprovedExport = approvalRootStatePub.getApproverApproved(rootStateID);
		return new ApproverApprovedImport_New(
				approverApprovedExport.getListApproverWithFlagOutput().stream()
					.map(x -> new ApproverWithFlagImport_New(x.getEmployeeID(), x.getAgentFlag())).collect(Collectors.toList()), 
				approverApprovedExport.getListApprover());
	}

	@Override
	public AgentPubImport getApprovalAgencyInformation(String companyID, List<String> approver) {
		// TODO Auto-generated method stub
		return convertAgentPubImport(approvalRootStatePub.getApprovalAgentInfor(companyID, approver));
	}
	
	private AgentPubImport convertAgentPubImport(AgentPubExport agentPubExport) {
		return new AgentPubImport(
				agentPubExport.getListApproverAndRepresenterSID().stream()
				.map(x -> this.covertApproverImport(x)).collect(Collectors.toList()),
				agentPubExport.getListRepresenterSID(),
				agentPubExport.isFlag()
				);
		
	}
	
	private ApproverRepresenterImport covertApproverImport(ApproverRepresenterExport approverRepresenterExport) {
		return new  ApproverRepresenterImport(
				approverRepresenterExport.getApprover(),
				new RepresenterInformationImport(approverRepresenterExport.getRepresenter().getValue()) 
				);
	}

	@Override
	public List<String> getMailNotifierList(String companyID, String rootStateID) {
		return approvalRootStatePub.getMailNotifierList(companyID, rootStateID);
	}

	@Override
	public void deleteApprovalRootState(String rootStateID) {
		approvalRootStatePub.deleteApprovalRootState(rootStateID);
		
	}

	@Override
	public Boolean doRelease(String companyID, String rootStateID, String employeeID) {
		return approvalRootStatePub.doRelease(companyID, rootStateID, employeeID);
	}

	@Override
	public Boolean doDeny(String companyID, String rootStateID, String employeeID) {
		return approvalRootStatePub.doDeny(companyID, rootStateID, employeeID);
	}

	@Override
	public Boolean judgmentTargetPersonIsApprover(String companyID, String rootStateID, String employeeID) {
		return approvalRootStatePub.judgmentTargetPersonIsApprover(companyID, rootStateID, employeeID);
	}

	@Override
	public ApproverPersonImport judgmentTargetPersonCanApprove(String companyID, String rootStateID,
			String employeeID) {
		ApproverPersonExport approverPersonExport = approvalRootStatePub.judgmentTargetPersonCanApprove(companyID, rootStateID, employeeID);
		return new ApproverPersonImport(
				approverPersonExport.getAuthorFlag(), 
				EnumAdaptor.valueOf(approverPersonExport.getApprovalAtr().value, ApprovalBehaviorAtrImport_New.class), 
				approverPersonExport.getExpirationAgentFlag());
	}
	
}
