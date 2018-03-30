package nts.uk.ctx.workflow.dom.service;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.arc.time.GeneralDate;
import nts.gul.collection.CollectionUtil;
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
public class RemandImpl implements RemandService {
	
	@Inject
	private ApprovalRootStateRepository approvalRootStateRepository;
	
	@Inject
	private JudgmentApprovalStatusService judgmentApprovalStatusService;
	
	@Inject
	private ReleaseAllAtOnceService releaseAllAtOnceService;
	
	@Inject
	private CollectApprovalAgentInforService collectApprovalAgentInforService;

	@Override
	public List<String> doRemandForApprover(String companyID, String rootStateID, Integer order) {
		Optional<ApprovalRootState> opApprovalRootState = approvalRootStateRepository.findEmploymentApp(rootStateID);
		if(!opApprovalRootState.isPresent()){
			throw new RuntimeException("状態：承認ルート取得失敗"+System.getProperty("line.separator")+"error: ApprovalRootState, ID: "+rootStateID);
		}
		ApprovalRootState approvalRootState = opApprovalRootState.get();
		List<ApprovalPhaseState> listApprovalPhase = approvalRootState.getListApprovalPhaseState();
		listApprovalPhase.sort(Comparator.comparing(ApprovalPhaseState::getPhaseOrder).reversed());
		listApprovalPhase.forEach(approvalPhaseState -> {
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
				approvalFrame.setApprovalDate(GeneralDate.today());
				approvalFrame.setApprovalReason("");
			});
			approvalPhaseState.setApprovalAtr(ApprovalBehaviorAtr.UNAPPROVED);
		});
		approvalRootStateRepository.update(approvalRootState);
		// 送信者ID＝送信先リスト
		ApprovalPhaseState approvalPhaseState = approvalRootState.getListApprovalPhaseState().get(5-order);
		List<String> approvers = judgmentApprovalStatusService.getApproverFromPhase(approvalPhaseState);
		ApprovalRepresenterOutput approvalRepresenterOutput = collectApprovalAgentInforService.getApprovalAgentInfor(companyID, approvers);
		approvers.addAll(approvalRepresenterOutput.getListAgent());
		return approvers.stream().distinct().collect(Collectors.toList());
	}

	@Override
	public void doRemandForApplicant(String companyID, String rootStateID) {
		releaseAllAtOnceService.doReleaseAllAtOnce(companyID, rootStateID);
	}

}
