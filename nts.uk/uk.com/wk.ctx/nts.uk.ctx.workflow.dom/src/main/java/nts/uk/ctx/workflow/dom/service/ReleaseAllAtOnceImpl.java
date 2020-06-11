package nts.uk.ctx.workflow.dom.service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

import org.apache.logging.log4j.util.Strings;

import nts.gul.collection.CollectionUtil;
import nts.uk.ctx.workflow.dom.approverstatemanagement.ApprovalBehaviorAtr;
import nts.uk.ctx.workflow.dom.approverstatemanagement.ApprovalPhaseState;
import nts.uk.ctx.workflow.dom.approverstatemanagement.ApprovalRootState;
import nts.uk.ctx.workflow.dom.approverstatemanagement.ApprovalRootStateRepository;
import nts.uk.ctx.workflow.dom.service.output.ApproverApprovedOutput;
import nts.uk.ctx.workflow.dom.service.output.ApproverWithFlagOutput;

/**
 * 
 * @author Doan Duy Hung
 *
 */
@Stateless
public class ReleaseAllAtOnceImpl implements ReleaseAllAtOnceService {
	
	@Inject
	private ApprovalRootStateRepository approvalRootStateRepository;
	
	@Inject
	private JudgmentApprovalStatusService judgmentApprovalStatusService;

	@Override
	public void doReleaseAllAtOnce(String companyID, String rootStateID, Integer rootType) {
		List<ApprovalRootState> opApprovalRootState = approvalRootStateRepository.findByID(rootStateID);
		if(opApprovalRootState.isEmpty()){
			throw new RuntimeException("状態：承認ルート取得失敗"+System.getProperty("line.separator")+"error: ApprovalRootState, ID: "+rootStateID);
		}
		ApprovalRootState approvalRootState = opApprovalRootState.get(0);
		approvalRootState.getListApprovalPhaseState().sort(Comparator.comparing(ApprovalPhaseState::getPhaseOrder).reversed());
		approvalRootState.getListApprovalPhaseState().stream().forEach(approvalPhaseState -> {
			approvalPhaseState.getListApprovalFrame().forEach(approvalFrame -> {
				approvalFrame.setApprovalAtr(ApprovalBehaviorAtr.UNAPPROVED);
				approvalFrame.setApproverID("");
				approvalFrame.setRepresenterID("");
				approvalFrame.setApprovalDate(null);
				approvalFrame.setApprovalReason("");
			});
			approvalPhaseState.setApprovalAtr(ApprovalBehaviorAtr.UNAPPROVED);
		});
		approvalRootStateRepository.update(approvalRootState);
	}

	@Override
	public ApproverApprovedOutput getApproverApproved(String rootStateID, Integer rootType) {
		List<ApproverWithFlagOutput> listApproverWithFlagOutput = new ArrayList<>();
		List<String> listApprover = new ArrayList<>();
		List<ApprovalRootState> opApprovalRootState = approvalRootStateRepository.findByID(rootStateID);
		if(opApprovalRootState.isEmpty()){
			throw new RuntimeException("状態：承認ルート取得失敗"+System.getProperty("line.separator")+"error: ApprovalRootState, ID: "+rootStateID);
		}
		ApprovalRootState approvalRootState = opApprovalRootState.get(0);
		for(ApprovalPhaseState approvalPhaseState : approvalRootState.getListApprovalPhaseState()){
			List<String> approvers = judgmentApprovalStatusService.getApproverFromPhase(approvalPhaseState);
			if(CollectionUtil.isEmpty(approvers)){
				continue;
			}
			approvalPhaseState.getListApprovalFrame().forEach(approvalFrame -> {
				if(approvalFrame.getApprovalAtr().equals(ApprovalBehaviorAtr.UNAPPROVED)){
					return;
				}
				if(Strings.isBlank(approvalFrame.getRepresenterID())){
					listApproverWithFlagOutput.add(new ApproverWithFlagOutput(approvalFrame.getApproverID(), false));
				} else {
					listApproverWithFlagOutput.add(new ApproverWithFlagOutput(approvalFrame.getRepresenterID(), true));
				}
				listApprover.addAll(approvalFrame.getListApproverState().stream().map(x -> x.getApproverID()).collect(Collectors.toList()));
			});
			break;
		}
		return new ApproverApprovedOutput(listApproverWithFlagOutput, listApprover);
	}

}
