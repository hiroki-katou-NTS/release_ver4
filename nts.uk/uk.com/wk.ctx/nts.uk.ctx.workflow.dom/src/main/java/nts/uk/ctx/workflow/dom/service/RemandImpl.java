package nts.uk.ctx.workflow.dom.service;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.uk.ctx.workflow.dom.approverstatemanagement.ApprovalBehaviorAtr;
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
	public List<String> doRemandForApprover(String companyID, String rootStateID, Integer order, Integer rootType) {
		List<ApprovalRootState> lstApprovalRootState = approvalRootStateRepository.findByID(rootStateID);
		if(lstApprovalRootState.isEmpty()){
			throw new RuntimeException("状態：承認ルート取得失敗"+System.getProperty("line.separator")+"error: ApprovalRootState, ID: "+rootStateID);
		}
		ApprovalRootState approvalRootState = lstApprovalRootState.get(0);
		List<ApprovalPhaseState> listApprovalPhase = approvalRootState.getListApprovalPhaseState();
		listApprovalPhase.sort(Comparator.comparing(ApprovalPhaseState::getPhaseOrder).reversed());
		List<ApprovalPhaseState> listUpperPhase = listApprovalPhase.stream().filter(x -> x.getPhaseOrder() >= order).collect(Collectors.toList());
		listUpperPhase.forEach(approvalPhaseState -> {
			approvalPhaseState.getListApprovalFrame().forEach(approvalFrame -> {
				approvalFrame.setApprovalAtr(ApprovalBehaviorAtr.UNAPPROVED);
				approvalFrame.setApproverID("");
				approvalFrame.setRepresenterID("");
				approvalFrame.setApprovalDate(null);
				approvalFrame.setApprovalReason("");
			});
			approvalPhaseState.setApprovalAtr(ApprovalBehaviorAtr.UNAPPROVED);
		});
		ApprovalPhaseState currentPhase = listApprovalPhase.stream().filter(x -> x.getPhaseOrder()==order).findFirst().get();
		currentPhase.setApprovalAtr(ApprovalBehaviorAtr.REMAND);
		approvalRootStateRepository.update(approvalRootState);
		// 送信者ID＝送信先リスト
		List<String> approvers = judgmentApprovalStatusService.getApproverFromPhase(currentPhase);
		ApprovalRepresenterOutput approvalRepresenterOutput = collectApprovalAgentInforService.getApprovalAgentInfor(companyID, approvers);
		approvers.addAll(approvalRepresenterOutput.getListAgent());
		return approvers.stream().distinct().collect(Collectors.toList());
	}

	@Override
	public void doRemandForApplicant(String companyID, String rootStateID, Integer rootType) {
		// アルゴリズム「一括解除する」を実行する
		releaseAllAtOnceService.doReleaseAllAtOnce(companyID, rootStateID, rootType);
		// ドメインモデル「承認ルートインスタンス」．承認フェーズ１．承認区分をupdateする
		List<ApprovalRootState> lsAppRoot = approvalRootStateRepository.findByID(rootStateID);
		if(!lsAppRoot.isEmpty()){
			ApprovalRootState appRoot = lsAppRoot.get(0);
			appRoot.getListApprovalPhaseState().sort(Comparator.comparing(ApprovalPhaseState::getPhaseOrder));
			appRoot.getListApprovalPhaseState().get(0).setApprovalAtr(ApprovalBehaviorAtr.ORIGINAL_REMAND);
			approvalRootStateRepository.update(appRoot);
		}
	}

	@Override
	public Integer getCurrentApprovePhase(String rootStateID, Integer rootType) {
		List<ApprovalRootState> lstApprovalRootState = approvalRootStateRepository.findByID(rootStateID);
		if(lstApprovalRootState.isEmpty()){
			throw new RuntimeException("状態：承認ルート取得失敗"+System.getProperty("line.separator")+"error: ApprovalRootState, ID: "+rootStateID);
		}
		ApprovalRootState approvalRootState = lstApprovalRootState.get(0);
		List<ApprovalPhaseState> listApprovalPhase = approvalRootState.getListApprovalPhaseState();
		listApprovalPhase.sort(Comparator.comparing(ApprovalPhaseState::getPhaseOrder).reversed());
		List<ApprovalPhaseState> lstApprover = listApprovalPhase.stream()
				.filter(x -> x.getApprovalAtr().equals(ApprovalBehaviorAtr.APPROVED)).collect(Collectors.toList());
		if(lstApprover.isEmpty()){
			return 1;
		}
		return lstApprover.get(0).getPhaseOrder() + 1;
	}

}
