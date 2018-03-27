package nts.uk.ctx.workflow.dom.service;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

import org.apache.logging.log4j.util.Strings;

import nts.arc.time.GeneralDate;
import nts.gul.collection.CollectionUtil;
import nts.uk.ctx.workflow.dom.approvermanagement.workroot.ConfirmPerson;
import nts.uk.ctx.workflow.dom.approverstatemanagement.ApprovalBehaviorAtr;
import nts.uk.ctx.workflow.dom.approverstatemanagement.ApprovalFrame;
import nts.uk.ctx.workflow.dom.approverstatemanagement.ApprovalPhaseState;
import nts.uk.ctx.workflow.dom.approverstatemanagement.ApprovalRootState;
import nts.uk.ctx.workflow.dom.approverstatemanagement.ApprovalRootStateRepository;
import nts.uk.ctx.workflow.dom.service.output.ApprovalRepresenterOutput;
/**
 * 
 * @author Doan Duy Hung
 *
 */
@Stateless
public class DenyImpl implements DenyService {
	
	@Inject
	private ApprovalRootStateRepository approvalRootStateRepository;
	
	@Inject
	private JudgmentApprovalStatusService judgmentApprovalStatusService;
	
	@Inject
	private CollectApprovalAgentInforService collectApprovalAgentInforService;

	@Override
	public Boolean doDeny(String companyID, String rootStateID, String employeeID, String memo) {
		Boolean executedFlag = false;
		Optional<ApprovalRootState> opApprovalRootState = approvalRootStateRepository.findEmploymentApp(rootStateID);
		if(!opApprovalRootState.isPresent()){
			throw new RuntimeException("状態：承認ルート取得失敗"+System.getProperty("line.separator")+"error: ApprovalRootState, ID: "+rootStateID);
		}
		ApprovalRootState approvalRootState = opApprovalRootState.get();
		approvalRootState.getListApprovalPhaseState().sort(Comparator.comparing(ApprovalPhaseState::getPhaseOrder).reversed());
		for(ApprovalPhaseState approvalPhaseState : approvalRootState.getListApprovalPhaseState()){
			List<String> approvers = judgmentApprovalStatusService.getApproverFromPhase(approvalPhaseState);
			if(CollectionUtil.isEmpty(approvers)){
				continue;
			}
			Boolean allFrameUnapproveFlag = approvalPhaseState.getListApprovalFrame().stream()
				.filter(x -> !x.getApprovalAtr().equals(ApprovalBehaviorAtr.UNAPPROVED)).findAny().map(y -> false).orElse(true);
			Boolean phaseNotApprovalFlag = approvalPhaseState.getApprovalAtr().equals(ApprovalBehaviorAtr.UNAPPROVED)&&allFrameUnapproveFlag;
			if(phaseNotApprovalFlag.equals(Boolean.TRUE)){
				Boolean canDenyCheckFlag = this.canDenyCheck(approvalRootState, approvalPhaseState.getPhaseOrder()-1, employeeID);
				if(canDenyCheckFlag.equals(Boolean.FALSE)){
					continue;
				}
			}
			for(ApprovalFrame approvalFrame : approvalPhaseState.getListApprovalFrame()){
				if(approvalFrame.getApprovalAtr().equals(ApprovalBehaviorAtr.UNAPPROVED)){
					if(!approvalFrame.getListApproverState().stream().map(x -> x.getApproverID()).collect(Collectors.toList()).contains(employeeID)){
						List<String> listApprover = approvalFrame.getListApproverState().stream().map(x -> x.getApproverID()).collect(Collectors.toList());
						ApprovalRepresenterOutput approvalRepresenterOutput = collectApprovalAgentInforService.getApprovalAgentInfor(companyID, listApprover);
						if(approvalRepresenterOutput.getListAgent().contains(employeeID)){
							approvalFrame.setApprovalAtr(ApprovalBehaviorAtr.DENIAL);
							approvalFrame.setApproverID("");
							approvalFrame.setRepresenterID(employeeID);
							approvalFrame.setApprovalDate(GeneralDate.today());
							approvalFrame.setApprovalReason(memo);
							approvalPhaseState.setApprovalAtr(ApprovalBehaviorAtr.DENIAL);
							executedFlag = true;
							continue;
						} else {
							continue;
						}
					}
				} else {
					if(!((Strings.isNotBlank(approvalFrame.getApproverID())&&approvalFrame.getApproverID().equals(employeeID))||
						(Strings.isNotBlank(approvalFrame.getRepresenterID())&&approvalFrame.getRepresenterID().equals(employeeID)))){
						continue;
					}
				}
				approvalFrame.setApprovalAtr(ApprovalBehaviorAtr.DENIAL);
				approvalFrame.setApproverID(employeeID);
				approvalFrame.setRepresenterID("");
				approvalFrame.setApprovalDate(GeneralDate.today());
				approvalFrame.setApprovalReason(memo);
				approvalPhaseState.setApprovalAtr(ApprovalBehaviorAtr.DENIAL);
				executedFlag = true;
			}
			approvalRootStateRepository.update(approvalRootState);
			break;
		}
		return executedFlag;
	}

	@Override
	public Boolean canDenyCheck(ApprovalRootState approvalRootState, Integer order, String employeeID) {
		Boolean canDenyFlag = true;
		if(approvalRootState.getListApprovalPhaseState().size()==1){
			return canDenyFlag;
		}
		if(order<=0){
			return canDenyFlag;
		}
		ApprovalPhaseState lowerPhase = approvalRootState.getListApprovalPhaseState()
				.stream().filter(x -> x.getPhaseOrder()<=order)
				.sorted(Comparator.comparing(ApprovalPhaseState::getPhaseOrder).reversed())
				.findFirst().get();
		if(!lowerPhase.getApprovalAtr().equals(ApprovalBehaviorAtr.APPROVED)){
			canDenyFlag = false;
			return canDenyFlag;
		}
		Optional<ApprovalFrame> opConfirmApprovalFrame = lowerPhase.getListApprovalFrame().stream()
		 	.filter(x -> x.getConfirmAtr().equals(ConfirmPerson.CONFIRM)).findAny();
		if(opConfirmApprovalFrame.isPresent()){
			ApprovalFrame confirmApprovalFrame = opConfirmApprovalFrame.get();
			if(confirmApprovalFrame.getApproverID().equals(employeeID)||confirmApprovalFrame.getRepresenterID().equals(employeeID)){
				canDenyFlag = false;
			} else {
				canDenyFlag = true;
			}
			return canDenyFlag;
		}
		canDenyFlag = lowerPhase.getListApprovalFrame().stream()
			.filter(x -> x.getApproverID().equals(employeeID)||x.getRepresenterID().equals(employeeID))
			.findAny().map(y -> false).orElse(true);
		return canDenyFlag;
	}
}
