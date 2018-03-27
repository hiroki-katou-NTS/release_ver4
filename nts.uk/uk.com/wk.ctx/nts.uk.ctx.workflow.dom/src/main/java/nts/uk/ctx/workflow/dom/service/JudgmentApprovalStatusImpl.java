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
import nts.uk.ctx.workflow.dom.approvermanagement.workroot.ConfirmPerson;
import nts.uk.ctx.workflow.dom.approverstatemanagement.ApprovalBehaviorAtr;
import nts.uk.ctx.workflow.dom.approverstatemanagement.ApprovalFrame;
import nts.uk.ctx.workflow.dom.approverstatemanagement.ApprovalPhaseState;
import nts.uk.ctx.workflow.dom.approverstatemanagement.ApprovalRootState;
import nts.uk.ctx.workflow.dom.approverstatemanagement.ApprovalRootStateRepository;
import nts.uk.ctx.workflow.dom.service.output.ApprovalRepresenterOutput;
import nts.uk.ctx.workflow.dom.service.output.ApprovalStatusOutput;
import nts.uk.ctx.workflow.dom.service.output.ApproverPersonOutput;

/**
 * 
 * @author Doan Duy Hung
 *
 */
@Stateless
public class JudgmentApprovalStatusImpl implements JudgmentApprovalStatusService {
	
	@Inject
	private ApprovalRootStateRepository approvalRootStateRepository;
	
	@Inject
	private CollectApprovalAgentInforService collectApprovalAgentInforService;

	@Override
	public Boolean judgmentTargetPersonIsApprover(String companyID, String rootStateID, String employeeID) {
		Boolean approverFlag = false;
		Optional<ApprovalRootState> opApprovalRootState = approvalRootStateRepository.findEmploymentApp(rootStateID);
		if(!opApprovalRootState.isPresent()){
			throw new RuntimeException("状態：承認ルート取得失敗"+System.getProperty("line.separator")+"error: ApprovalRootState, ID: "+rootStateID);
		}
		ApprovalRootState approvalRootState = opApprovalRootState.get();
		List<String> listApprover = new ArrayList<>();
		approvalRootState.getListApprovalPhaseState().stream().forEach(approvalPhaseState -> {
			List<String> approvers = this.getApproverFromPhase(approvalPhaseState);
			listApprover.addAll(approvers);
		});
		List<String> newListApprover = listApprover.stream().distinct().collect(Collectors.toList());
		if(newListApprover.contains(employeeID)){
			approverFlag = true;
			return approverFlag;
		} 
		ApprovalRepresenterOutput approvalAgentOutput = collectApprovalAgentInforService.getApprovalAgentInfor(companyID, newListApprover);
		if(approvalAgentOutput.getListAgent().contains(employeeID)){
			approverFlag = true;
			return approverFlag;
		}
		for(ApprovalPhaseState approvalPhaseState : approvalRootState.getListApprovalPhaseState()){
			for(ApprovalFrame approvalFrame : approvalPhaseState.getListApprovalFrame()){
				if(Strings.isNotBlank(approvalFrame.getRepresenterID()) && approvalFrame.getRepresenterID().equals(employeeID)){
					approverFlag = true;
					break;
				}
			}
			if(approverFlag.equals(Boolean.TRUE)){
				break;
			}
		}
		return approverFlag;
	}

	@Override
	public ApprovalBehaviorAtr determineApprovalStatus(String companyID, String rootStateID) {
		ApprovalBehaviorAtr approvalAtr = ApprovalBehaviorAtr.UNAPPROVED;
		Optional<ApprovalRootState> opApprovalRootState = approvalRootStateRepository.findEmploymentApp(rootStateID);
		if(!opApprovalRootState.isPresent()){
			throw new RuntimeException("状態：承認ルート取得失敗"+System.getProperty("line.separator")+"error: ApprovalRootState, ID: "+rootStateID);
		}
		ApprovalRootState approvalRootState = opApprovalRootState.get();
		approvalRootState.getListApprovalPhaseState().sort(Comparator.comparing(ApprovalPhaseState::getPhaseOrder).reversed());
		for(ApprovalPhaseState approvalPhaseState : approvalRootState.getListApprovalPhaseState()){
			List<String> approvers = this.getApproverFromPhase(approvalPhaseState);
			if(CollectionUtil.isEmpty(approvers)){
				continue;
			}
			if(approvalPhaseState.getApprovalAtr().equals(ApprovalBehaviorAtr.APPROVED)){
				approvalAtr = ApprovalBehaviorAtr.APPROVED;
				break;
			}
			Optional<ApprovalPhaseState> previousPhaseResult = approvalRootState.getListApprovalPhaseState().stream()
				.filter(x -> x.getPhaseOrder() < approvalPhaseState.getPhaseOrder())
				.filter(x -> !x.getApprovalAtr().equals(ApprovalBehaviorAtr.APPROVED))
				.findAny();
			Optional<ApprovalPhaseState> afterPhaseResult = approvalRootState.getListApprovalPhaseState().stream()
					.filter(x -> x.getPhaseOrder() > approvalPhaseState.getPhaseOrder())
					.filter(x -> !x.getApprovalAtr().equals(ApprovalBehaviorAtr.UNAPPROVED))
					.findAny();
			if(!previousPhaseResult.isPresent()&&(!afterPhaseResult.isPresent()||approvalPhaseState.getPhaseOrder()==1)){
				approvalAtr = approvalPhaseState.getApprovalAtr();
				break;
			}
		}
		return approvalAtr;
	}

	@Override
	public ApproverPersonOutput judgmentTargetPersonCanApprove(String companyID, String rootStateID, String employeeID) {
		Boolean authorFlag = false;
		ApprovalBehaviorAtr approvalAtr = ApprovalBehaviorAtr.UNAPPROVED;
		Boolean expirationAgentFlag = false; 
		Optional<ApprovalRootState> opApprovalRootState = approvalRootStateRepository.findEmploymentApp(rootStateID);
		if(!opApprovalRootState.isPresent()){
			throw new RuntimeException("状態：承認ルート取得失敗"+System.getProperty("line.separator")+"error: ApprovalRootState, ID: "+rootStateID);
		}
		ApprovalRootState approvalRootState = opApprovalRootState.get();
		approvalRootState.getListApprovalPhaseState().sort(Comparator.comparing(ApprovalPhaseState::getPhaseOrder).reversed());
		for(ApprovalPhaseState approvalPhaseState : approvalRootState.getListApprovalPhaseState()){
			Boolean pastPhaseFlag = false;
			List<String> approvers = this.getApproverFromPhase(approvalPhaseState);
			if(CollectionUtil.isEmpty(approvers)){
				continue;
			}
			// ループ中の承認フェーズが承認中のフェーズかチェックする
			Boolean judgmentResult = this.judgmentLoopApprovalPhase(approvalRootState, approvalPhaseState, pastPhaseFlag);
			if(judgmentResult){
				ApprovalStatusOutput approvalStatusOutput = this.judmentApprovalStatus(companyID, approvalPhaseState, employeeID);
				authorFlag = approvalStatusOutput.getApprovableFlag();
				approvalAtr = approvalStatusOutput.getApprovalAtr();
				expirationAgentFlag = approvalStatusOutput.getSubExpFlag(); 
				pastPhaseFlag = true;
			} else {
				if(pastPhaseFlag.equals(Boolean.FALSE)){
					continue;
				}
				ApprovalStatusOutput approvalStatusOutput = this.judmentApprovalStatus(companyID, approvalPhaseState, employeeID);
				authorFlag = approvalStatusOutput.getApprovableFlag();
				approvalAtr = approvalStatusOutput.getApprovalAtr();
			}
			if(pastPhaseFlag.equals(Boolean.TRUE)){
				break;
			}
			
		}
		return new ApproverPersonOutput(authorFlag, approvalAtr, expirationAgentFlag);
	}

	@Override
	public List<String> getApproverFromPhase(ApprovalPhaseState approvalPhaseState) {
		List<String> listApprover = new ArrayList<>();
		approvalPhaseState.getListApprovalFrame().forEach(approvalFrame -> {
			List<String> approvers = approvalFrame.getListApproverState().stream().map(x -> x.getApproverID()).collect(Collectors.toList());
			listApprover.addAll(approvers);
		});
		List<String> newListApprover = listApprover.stream().distinct().collect(Collectors.toList());
		return newListApprover;
	}
	
	@Override
	public ApprovalStatusOutput judmentApprovalStatus(String companyID, ApprovalPhaseState approvalPhaseState, String employeeID) {
		Boolean approvalFlag = false;
		ApprovalBehaviorAtr approvalAtr = ApprovalBehaviorAtr.UNAPPROVED;
		Boolean approvableFlag = false;
		Boolean subExpFlag = false;
		for(ApprovalFrame approvalFrame : approvalPhaseState.getListApprovalFrame()){
			if(Strings.isNotBlank(approvalFrame.getApproverID()) && approvalFrame.getApproverID().equals(employeeID)){
				approvalFlag = true;
				approvalAtr = approvalFrame.getApprovalAtr();
				approvableFlag = true;
				subExpFlag = false;
				continue;
			}
			List<String> listApprover = approvalFrame.getListApproverState().stream().map(x -> x.getApproverID()).collect(Collectors.toList());
			if(Strings.isNotBlank(approvalFrame.getRepresenterID()) && approvalFrame.getRepresenterID().equals(employeeID)){
				approvalFlag = true;
				approvalAtr = approvalFrame.getApprovalAtr();
				approvableFlag = true;
				subExpFlag = !this.judgmentAgentListByEmployee(companyID, employeeID, listApprover);
				continue;
			}
			if(!listApprover.contains(employeeID)){
				ApprovalRepresenterOutput approvalRepresenterOutput = collectApprovalAgentInforService.getApprovalAgentInfor(companyID, listApprover);
				if(!approvalRepresenterOutput.getListAgent().contains(employeeID)){
					continue;
				}
			}
			approvalFlag = true;
			approvalAtr = approvalFrame.getApprovalAtr();
			approvableFlag = true;
			subExpFlag = false;
		};
		Optional<ApprovalFrame> opApprovalFrameConfirm = approvalPhaseState.getListApprovalFrame().stream().filter(x -> x.getConfirmAtr().equals(ConfirmPerson.CONFIRM)).findAny();
		if(opApprovalFrameConfirm.isPresent()){
			ApprovalFrame approvalFrameConfirm = opApprovalFrameConfirm.get();
			if(!approvalFrameConfirm.getApprovalAtr().equals(ApprovalBehaviorAtr.UNAPPROVED)&&
				(Strings.isNotBlank(approvalFrameConfirm.getApproverID()) && !approvalFrameConfirm.getApproverID().equals(employeeID))&&
				(Strings.isNotBlank(approvalFrameConfirm.getRepresenterID()) && !approvalFrameConfirm.getRepresenterID().equals(employeeID))){
				approvableFlag = false;
			}
		}
		return new ApprovalStatusOutput(approvalFlag, approvalAtr, approvableFlag, subExpFlag);
	}

	@Override
	public Boolean judgmentAgentListByEmployee(String companyID, String employeeID, List<String> listApprover) {
		ApprovalRepresenterOutput approvalRepresenterOutput = collectApprovalAgentInforService.getApprovalAgentInfor(companyID, listApprover);
		if(approvalRepresenterOutput.getListAgent().contains(employeeID)){
			return true;
		}
		return false;
	}

	@Override
	public Boolean judgmentLoopApprovalPhase(ApprovalRootState approvalRootState, ApprovalPhaseState currentPhase, Boolean pastPhaseFlg) {
		// 過去フェーズフラグ＝trueの場合
		if(pastPhaseFlg) {
			return false;
		}
		
		// パラメータのループ中のフェーズ番号をチェックする
		if(approvalRootState.getListApprovalPhaseState().size()==1) {
			return true;
		}
		if(currentPhase.getPhaseOrder()==1){
			return true;
		}
		
		// ループ中のフェーズの番号-１から、降順にループする
		ApprovalPhaseState lowerPhase = approvalRootState.getListApprovalPhaseState()
				.stream().filter(x -> x.getPhaseOrder()<currentPhase.getPhaseOrder())
				.sorted(Comparator.comparing(ApprovalPhaseState::getPhaseOrder).reversed())
				.findFirst().get();
		if(lowerPhase.getApprovalAtr().equals(ApprovalBehaviorAtr.APPROVED)){
			return true;
		}
		return false;
	}

	

}
