package nts.uk.ctx.workflow.dom.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.ejb.Stateless;
import javax.inject.Inject;

import org.apache.logging.log4j.util.Strings;

import nts.gul.collection.CollectionUtil;
import nts.uk.ctx.workflow.dom.approverstatemanagement.ApprovalBehaviorAtr;
import nts.uk.ctx.workflow.dom.approverstatemanagement.ApprovalFrame;
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
	public void doReleaseAllAtOnce(String companyID, String rootStateID) {
		Optional<ApprovalRootState> opApprovalRootState = approvalRootStateRepository.findEmploymentApp(rootStateID);
		if(!opApprovalRootState.isPresent()){
			throw new RuntimeException("状態：承認ルート取得失敗"+System.getProperty("line.separator")+"error: ApprovalRootState, ID: "+rootStateID);
		}
		ApprovalRootState approvalRootState = opApprovalRootState.get();
		approvalRootState.getListApprovalPhaseState().stream().forEach(approvalPhaseState -> {
			List<String> approvers = judgmentApprovalStatusService.getApproverFromPhase(approvalPhaseState);
			if(CollectionUtil.isEmpty(approvers)){
				return;
			}
			Boolean phaseNotApprovalFlag = approvalPhaseState.getApprovalAtr().equals(ApprovalBehaviorAtr.UNAPPROVED);
			for(ApprovalFrame approvalFrame : approvalPhaseState.getListApprovalFrame()){
				phaseNotApprovalFlag = Boolean.logicalAnd(phaseNotApprovalFlag, approvalFrame.getApprovalAtr().equals(ApprovalBehaviorAtr.UNAPPROVED));
			}
			if(phaseNotApprovalFlag.equals(Boolean.TRUE)){
				return;
			}
			approvalPhaseState.getListApprovalFrame().forEach(approvalFrame -> {
				approvalFrame.setApprovalAtr(ApprovalBehaviorAtr.UNAPPROVED);
				approvalFrame.setApproverID("");
				approvalFrame.setRepresenterID("");
			});
			approvalPhaseState.setApprovalAtr(ApprovalBehaviorAtr.UNAPPROVED);
		});
		approvalRootStateRepository.update(approvalRootState);
	}

	@Override
	public ApproverApprovedOutput getApproverApproved(String rootStateID) {
		List<ApproverWithFlagOutput> listApproverWithFlagOutput = new ArrayList<>();
		List<String> listApprover = new ArrayList<>();
		Optional<ApprovalRootState> opApprovalRootState = approvalRootStateRepository.findEmploymentApp(rootStateID);
		if(!opApprovalRootState.isPresent()){
			throw new RuntimeException("状態：承認ルート取得失敗"+System.getProperty("line.separator")+"error: ApprovalRootState, ID: "+rootStateID);
		}
		ApprovalRootState approvalRootState = opApprovalRootState.get();
		for(ApprovalPhaseState approvalPhaseState : approvalRootState.getListApprovalPhaseState()){
			List<String> approvers = judgmentApprovalStatusService.getApproverFromPhase(approvalPhaseState);
			if(CollectionUtil.isEmpty(approvers)){
				continue;
			}
			approvalPhaseState.getListApprovalFrame().forEach(approvalFrame -> {
				if(!approvalFrame.getApprovalAtr().equals(ApprovalBehaviorAtr.UNAPPROVED)){
					return;
				}
				if(Strings.isBlank(approvalFrame.getRepresenterID())){
					listApproverWithFlagOutput.add(new ApproverWithFlagOutput(approvalFrame.getApproverID(), false));
				} else {
					listApproverWithFlagOutput.add(new ApproverWithFlagOutput(approvalFrame.getRepresenterID(), true));
				}
				listApprover.add(approvalFrame.getApproverID());
			});
			break;
		}
		return new ApproverApprovedOutput(listApproverWithFlagOutput, listApprover);
	}

}
