package nts.uk.ctx.workflow.dom.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.gul.collection.CollectionUtil;
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
public class CollectMailNotifierImpl implements CollectMailNotifierService {
	
	@Inject
	private ApprovalRootStateRepository approvalRootStateRepository;
	
	@Inject
	private JudgmentApprovalStatusService judgmentApprovalStatusService;
	
	@Inject
	private ApproveService approveService;

	@Inject
	private CollectApprovalAgentInforService collectApprovalAgentInforService;
	
	@Override
	public List<String> getMailNotifierList(String companyID, String rootStateID, Integer rootType) {
		List<String> mailList = new ArrayList<>();
		Optional<ApprovalRootState> opApprovalRootState = approvalRootStateRepository.findByID(rootStateID);
		if(opApprovalRootState.isPresent()){
			throw new RuntimeException("状態：承認ルート取得失敗"+System.getProperty("line.separator")+"error: ApprovalRootState, ID: "+rootStateID);
		}
		ApprovalRootState approvalRootState = opApprovalRootState.get();
		for(ApprovalPhaseState approvalPhaseState : approvalRootState.getListApprovalPhaseState()){
			List<String> listApprover = judgmentApprovalStatusService.getApproverFromPhase(approvalPhaseState);
			if(CollectionUtil.isEmpty(listApprover)){
				continue;
			}
			List<String> listUnapproveApprover = approveService.getUnapproveApproverFromPhase(approvalPhaseState);
			if(CollectionUtil.isEmpty(listUnapproveApprover)){
				continue;
			}
			ApprovalRepresenterOutput approvalRepresenterOutput = collectApprovalAgentInforService.getApprovalAgentInfor(companyID, listUnapproveApprover);
			List<String> destinationList = approveService.judgmentDestination(approvalRepresenterOutput.getListApprovalAgentInfor());
			mailList.addAll(destinationList);
			if(!approvalPhaseState.getApprovalAtr().equals(ApprovalBehaviorAtr.APPROVED)){
				break;
			}
		}
		return mailList.stream().distinct().collect(Collectors.toList());
	}

}
