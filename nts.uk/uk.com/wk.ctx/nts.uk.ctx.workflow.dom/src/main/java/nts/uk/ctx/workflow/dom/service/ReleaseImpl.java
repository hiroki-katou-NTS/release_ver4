package nts.uk.ctx.workflow.dom.service;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import javax.ejb.Stateless;
import javax.inject.Inject;

import org.apache.logging.log4j.util.Strings;

import nts.arc.time.GeneralDate;
import nts.gul.collection.CollectionUtil;
import nts.uk.ctx.workflow.dom.approvermanagement.workroot.ApprovalForm;
import nts.uk.ctx.workflow.dom.approvermanagement.workroot.ConfirmPerson;
import nts.uk.ctx.workflow.dom.approverstatemanagement.ApprovalBehaviorAtr;
import nts.uk.ctx.workflow.dom.approverstatemanagement.ApprovalFrame;
import nts.uk.ctx.workflow.dom.approverstatemanagement.ApprovalPhaseState;
import nts.uk.ctx.workflow.dom.approverstatemanagement.ApprovalRootState;
import nts.uk.ctx.workflow.dom.approverstatemanagement.ApprovalRootStateRepository;

/**
 * 
 * @author Doan Duy Hung
 *
 */
@Stateless
public class ReleaseImpl implements ReleaseService {
	
	@Inject
	private ApprovalRootStateRepository approvalRootStateRepository;
	
	@Inject
	private JudgmentApprovalStatusService judgmentApprovalStatusService;

	@Override
	public Boolean doRelease(String companyID, String rootStateID, String employeeID, Integer rootType) {
		Boolean executedFlag = false;
		Optional<ApprovalRootState> opApprovalRootState = approvalRootStateRepository.findByID(rootStateID);
		if(!opApprovalRootState.isPresent()){
			throw new RuntimeException("状態：承認ルート取得失敗"+System.getProperty("line.separator")+"error: ApprovalRootState, ID: "+rootStateID);
		}
		ApprovalRootState approvalRootState = opApprovalRootState.get();
		approvalRootState.getListApprovalPhaseState().sort(Comparator.comparing(ApprovalPhaseState::getPhaseOrder).reversed());
		for(ApprovalPhaseState approvalPhaseState : approvalRootState.getListApprovalPhaseState()){
			List<String> listApprover = judgmentApprovalStatusService.getApproverFromPhase(approvalPhaseState);
			if(CollectionUtil.isEmpty(listApprover)){
				continue;
			}
			Boolean allFrameUnapproveFlag = approvalPhaseState.getListApprovalFrame().stream()
					.filter(x -> !x.getApprovalAtr().equals(ApprovalBehaviorAtr.UNAPPROVED)).findAny().map(y -> false).orElse(true);
			Boolean phaseNotApprovalFlag = approvalPhaseState.getApprovalAtr().equals(ApprovalBehaviorAtr.UNAPPROVED)&&allFrameUnapproveFlag;
			if(phaseNotApprovalFlag.equals(Boolean.TRUE)){
				continue;
			}
			Boolean canRelease = this.canReleaseCheck(approvalPhaseState, employeeID);
			if(canRelease.equals(Boolean.FALSE)){
				break;
			}
			approvalPhaseState.getListApprovalFrame().forEach(approvalFrame -> {
				if((Strings.isNotBlank(approvalFrame.getApproverID()) && approvalFrame.getApproverID().equals(employeeID)) ||
						(Strings.isNotBlank(approvalFrame.getRepresenterID()) && approvalFrame.getRepresenterID().equals(employeeID))){
					approvalFrame.setApprovalAtr(ApprovalBehaviorAtr.UNAPPROVED);
					approvalFrame.setApproverID("");
					approvalFrame.setRepresenterID("");
					approvalFrame.setApprovalDate(GeneralDate.today());
					approvalFrame.setApprovalReason("");
				}
			});
			approvalPhaseState.setApprovalAtr(ApprovalBehaviorAtr.UNAPPROVED);
			approvalRootStateRepository.update(approvalRootState);
			executedFlag = true;
		}
		return executedFlag;
	}

	@Override
	public Boolean canReleaseCheck(ApprovalPhaseState approvalPhaseState, String employeeID) {
		if(approvalPhaseState.getApprovalForm().equals(ApprovalForm.EVERYONE_APPROVED)){
			return approvalPhaseState.getListApprovalFrame().stream()
				.filter(x -> (Strings.isNotBlank(x.getApproverID()) && x.getApproverID().equals(employeeID))||
						(Strings.isNotBlank(x.getRepresenterID()) && x.getRepresenterID().equals(employeeID)))
				.findAny().map(y ->true).orElse(false);
		}
		Optional<ApprovalFrame> opConfirmFrame = approvalPhaseState.getListApprovalFrame().stream().filter(x -> x.getConfirmAtr().equals(ConfirmPerson.CONFIRM)).findAny();
		if(opConfirmFrame.isPresent()){
			ApprovalFrame approvalFrame = opConfirmFrame.get();
			if((Strings.isNotBlank(approvalFrame.getApproverID()) && approvalFrame.getApproverID().equals(employeeID)) ||
					(Strings.isNotBlank(approvalFrame.getRepresenterID()) && approvalFrame.getRepresenterID().equals(employeeID))){
				return true;
			}
			return false;
		}
		return approvalPhaseState.getListApprovalFrame().stream()
				.filter(x -> (Strings.isNotBlank(x.getApproverID()) && x.getApproverID().equals(employeeID))||
						(Strings.isNotBlank(x.getRepresenterID()) && x.getRepresenterID().equals(employeeID)))
				.findAny().map(y ->true).orElse(false);
	}

}
