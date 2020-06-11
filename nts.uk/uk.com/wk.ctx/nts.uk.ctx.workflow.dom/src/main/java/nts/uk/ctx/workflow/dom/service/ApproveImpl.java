package nts.uk.ctx.workflow.dom.service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

import org.apache.logging.log4j.util.Strings;

import nts.arc.time.GeneralDate;
import nts.gul.collection.CollectionUtil;
import nts.uk.ctx.workflow.dom.approvermanagement.setting.ApprovalSettingRepository;
import nts.uk.ctx.workflow.dom.approvermanagement.setting.PrincipalApprovalFlg;
import nts.uk.ctx.workflow.dom.approvermanagement.workroot.ApplicationType;
import nts.uk.ctx.workflow.dom.approvermanagement.workroot.ApprovalForm;
import nts.uk.ctx.workflow.dom.approvermanagement.workroot.ConfirmPerson;
import nts.uk.ctx.workflow.dom.approvermanagement.workroot.EmploymentRootAtr;
import nts.uk.ctx.workflow.dom.approverstatemanagement.ApprovalBehaviorAtr;
import nts.uk.ctx.workflow.dom.approverstatemanagement.ApprovalFrame;
import nts.uk.ctx.workflow.dom.approverstatemanagement.ApprovalPhaseState;
import nts.uk.ctx.workflow.dom.approverstatemanagement.ApprovalRootState;
import nts.uk.ctx.workflow.dom.approverstatemanagement.ApprovalRootStateRepository;
import nts.uk.ctx.workflow.dom.service.output.ApprovalRepresenterInforOutput;
import nts.uk.ctx.workflow.dom.service.output.ApprovalRepresenterOutput;
import nts.uk.ctx.workflow.dom.service.output.ApprovalRootContentOutput;
import nts.uk.ctx.workflow.dom.service.output.RepresenterInforOutput;
import nts.uk.shr.com.context.AppContexts;

/**
 * 
 * @author Doan Duy Hung
 *
 */
@Stateless
public class ApproveImpl implements ApproveService {
	
	@Inject
	private ApprovalRootStateRepository approvalRootStateRepository;
	
	@Inject
	private JudgmentApprovalStatusService judgmentApprovalStatusService;
	
	@Inject
	private CollectApprovalAgentInforService collectApprovalAgentInforService;
	
	@Inject
	private CollectApprovalRootService collectApprovalRootService;
	
	@Inject
	private ApprovalSettingRepository repoApprSet;
	
	//承認する
	@Override
	public Integer doApprove(String companyID, String rootStateID, String employeeID, Boolean isCreate, 
			ApplicationType appType, GeneralDate appDate, String memo, Integer rootType) {
		Integer approvalPhaseNumber = 0;
		ApprovalRootState approvalRootState = null;
		if(isCreate.equals(Boolean.TRUE)){
			ApprovalRootContentOutput approvalRootContentOutput = collectApprovalRootService.getApprovalRootOfSubjectRequest(
					companyID, 
					employeeID, 
					EmploymentRootAtr.APPLICATION, 
					appType, 
					appDate);
			approvalRootState = approvalRootContentOutput.getApprovalRootState();
		} else {
			//ドメインモデル「承認ルートインスタンス」を取得する
			List<ApprovalRootState> opApprovalRootState = approvalRootStateRepository.findByID(rootStateID);
			if(opApprovalRootState.isEmpty()){//0件
				throw new RuntimeException("状態：承認ルート取得失敗"+System.getProperty("line.separator")+"error: ApprovalRootState, ID: "+rootStateID);
			}
			approvalRootState = opApprovalRootState.get(0);
		}
		//hoatt 2018.12.14
		//EA修正履歴 No.3013
		//ドメインモデル「承認設定」を取得する
		Optional<PrincipalApprovalFlg> flg = repoApprSet.getPrincipalByCompanyId(companyID);
		if((!flg.isPresent() || flg.get().equals(PrincipalApprovalFlg.NOT_PRINCIPAL)) &&
				approvalRootState.getEmployeeID().equals(AppContexts.user().employeeId())){
			//本人による承認＝false　＆　申請者＝ログイン社員IDの場合
			return approvalPhaseNumber;
		}
		
		approvalRootState.getListApprovalPhaseState().sort(Comparator.comparing(ApprovalPhaseState::getPhaseOrder));
		for(ApprovalPhaseState approvalPhaseState : approvalRootState.getListApprovalPhaseState()){
			if(approvalPhaseState.getApprovalAtr().equals(ApprovalBehaviorAtr.APPROVED)){
				continue;
			}
			approvalPhaseState.getListApprovalFrame().forEach(approvalFrame -> {
				if(approvalFrame.getApprovalAtr().equals(ApprovalBehaviorAtr.APPROVED)){
					return;
				}
				if(approvalFrame.getApprovalAtr().equals(ApprovalBehaviorAtr.UNAPPROVED)){
					List<String> listApprover = approvalFrame.getListApproverState().stream().map(x -> x.getApproverID()).collect(Collectors.toList());
					if(!listApprover.contains(employeeID)){
						ApprovalRepresenterOutput approvalRepresenterOutput = collectApprovalAgentInforService.getApprovalAgentInfor(companyID, listApprover);
						if(approvalRepresenterOutput.getListAgent().contains(employeeID)){
							approvalFrame.setApprovalAtr(ApprovalBehaviorAtr.APPROVED);
							approvalFrame.setApproverID("");
							approvalFrame.setRepresenterID(employeeID);
							approvalFrame.setApprovalDate(GeneralDate.today());
							approvalFrame.setApprovalReason(memo);
							return;
						}
						return;
					}
				} else {
					// if文： ドメインモデル「承認枠」．承認者 == INPUT．社員ID　OR ドメインモデル「承認枠」．代行者 == INPUT．社員ID
					if(!((Strings.isNotBlank(approvalFrame.getApproverID())&&approvalFrame.getApproverID().equals(employeeID))|
						(Strings.isNotBlank(approvalFrame.getRepresenterID())&&approvalFrame.getRepresenterID().equals(employeeID)))){
						return;
					}
				}
				approvalFrame.setApprovalAtr(ApprovalBehaviorAtr.APPROVED);
				approvalFrame.setApproverID(employeeID);
				approvalFrame.setRepresenterID("");
				approvalFrame.setApprovalDate(GeneralDate.today());
				approvalFrame.setApprovalReason(memo);
				return;
			});
			Boolean approveApprovalPhaseStateFlag = this.isApproveApprovalPhaseStateComplete(companyID, approvalPhaseState);
			if(approveApprovalPhaseStateFlag.equals(Boolean.FALSE)){
				approvalPhaseState.setApprovalAtr(ApprovalBehaviorAtr.UNAPPROVED);
				break;
			}
			approvalPhaseState.setApprovalAtr(ApprovalBehaviorAtr.APPROVED);
			approvalPhaseNumber = approvalPhaseState.getPhaseOrder();
		}
		approvalRootStateRepository.update(approvalRootState);
		return approvalPhaseNumber;
	}
	
	@Override
	public Boolean isApproveApprovalPhaseStateComplete(String companyID, ApprovalPhaseState approvalPhaseState) {
		if(approvalPhaseState.getApprovalForm().equals(ApprovalForm.EVERYONE_APPROVED)){
			Optional<ApprovalFrame> opApprovalFrameNotApprove = approvalPhaseState.getListApprovalFrame().stream()
			.filter(x -> !x.getApprovalAtr().equals(ApprovalBehaviorAtr.APPROVED)).findAny();
			if(!opApprovalFrameNotApprove.isPresent()){
				return true;
			}
			List<String> listUnapproveApprover = this.getUnapproveApproverFromPhase(approvalPhaseState);
			ApprovalRepresenterOutput approvalRepresenterOutput = collectApprovalAgentInforService.getApprovalAgentInfor(companyID, listUnapproveApprover);
			if(approvalRepresenterOutput.getAllPathSetFlag().equals(Boolean.TRUE)){
				return true;
			}
			return false;
		}
		Optional<ApprovalFrame> opApprovalFrameIsConfirm = approvalPhaseState.getListApprovalFrame().stream().filter(x -> x.getConfirmAtr().equals(ConfirmPerson.CONFIRM)).findAny();
		if(opApprovalFrameIsConfirm.isPresent()){
			ApprovalFrame approvalFrame = opApprovalFrameIsConfirm.get();
			if(approvalFrame.getApprovalAtr().equals(ApprovalBehaviorAtr.APPROVED)){
				return true;
			}
			List<String> listApprover = approvalFrame.getListApproverState().stream().map(x -> x.getApproverID()).collect(Collectors.toList());
			ApprovalRepresenterOutput approvalRepresenterOutput = collectApprovalAgentInforService.getApprovalAgentInfor(companyID, listApprover);
			if(approvalRepresenterOutput.getAllPathSetFlag().equals(Boolean.TRUE)){
				return true;
			}
			return false;
		}
		Optional<ApprovalFrame> opApprovalFrameIsApprove = approvalPhaseState.getListApprovalFrame().stream()
				.filter(x -> x.getApprovalAtr().equals(ApprovalBehaviorAtr.APPROVED)).findAny();
		if(opApprovalFrameIsApprove.isPresent()){
			return true;
		}
		List<String> listApprover = judgmentApprovalStatusService.getApproverFromPhase(approvalPhaseState);
		ApprovalRepresenterOutput approvalRepresenterOutput = collectApprovalAgentInforService.getApprovalAgentInfor(companyID, listApprover);
		if(approvalRepresenterOutput.getAllPathSetFlag().equals(Boolean.TRUE)){
			return true;
		}
		return false;
	}

	@Override
	public Boolean isApproveAllComplete(String companyID, String rootStateID, String employeeID, 
			Boolean isCreate, ApplicationType appType, GeneralDate appDate, Integer rootType) {
		Boolean approveAllFlag = false;
		ApprovalRootState approvalRootState = null;
		if(isCreate.equals(Boolean.TRUE)){
			ApprovalRootContentOutput approvalRootContentOutput = collectApprovalRootService.getApprovalRootOfSubjectRequest(
					companyID, 
					employeeID, 
					EmploymentRootAtr.APPLICATION, 
					appType, 
					appDate);
			approvalRootState = approvalRootContentOutput.getApprovalRootState();
		} else {
			List<ApprovalRootState> opApprovalRootState = approvalRootStateRepository.findByID(rootStateID);
			if(opApprovalRootState.isEmpty()){
				throw new RuntimeException("状態：承認ルート取得失敗"+System.getProperty("line.separator")+"error: ApprovalRootState, ID: "+rootStateID);
			}
			approvalRootState = opApprovalRootState.get(0);
		}
		approvalRootState.getListApprovalPhaseState().sort(Comparator.comparing(ApprovalPhaseState::getPhaseOrder).reversed());
		for(ApprovalPhaseState approvalPhaseState : approvalRootState.getListApprovalPhaseState()){
			if(approvalPhaseState.getApprovalAtr().equals(ApprovalBehaviorAtr.APPROVED)){
				approveAllFlag = true;
				break;
			}
			List<String> listApprover = judgmentApprovalStatusService.getApproverFromPhase(approvalPhaseState);
			if(CollectionUtil.isEmpty(listApprover)){
				continue;
			}
			ApprovalRepresenterOutput approvalRepresenterOutput = collectApprovalAgentInforService.getApprovalAgentInfor(companyID, listApprover);
			if(approvalRepresenterOutput.getAllPathSetFlag().equals(Boolean.FALSE)){
				break;
			}
		}
		return approveAllFlag;
	}

	@Override
	public List<String> getUnapproveApproverFromPhase(ApprovalPhaseState approvalPhaseState) {
		List<String> listUnapproveApprover = new ArrayList<>();
		approvalPhaseState.getListApprovalFrame().forEach(approvalFrame -> {
			if(approvalFrame.getApprovalAtr().equals(ApprovalBehaviorAtr.UNAPPROVED)){
				List<String> approvers = approvalFrame.getListApproverState().stream().map(x -> x.getApproverID()).collect(Collectors.toList());
				listUnapproveApprover.addAll(approvers);
			}
		});
		return listUnapproveApprover.stream().distinct().collect(Collectors.toList());
	}
	
	@Override
	public List<String> getNextApprovalPhaseStateMailList(String companyID, String rootStateID, Integer approvalPhaseStateNumber, 
			Boolean isCreate, String employeeID, ApplicationType appType, GeneralDate appDate, Integer rootType) {
		List<String> mailList = new ArrayList<>();
		ApprovalRootState approvalRootState = null;
		if(isCreate.equals(Boolean.TRUE)){
			ApprovalRootContentOutput approvalRootContentOutput = collectApprovalRootService.getApprovalRootOfSubjectRequest(
					companyID, 
					employeeID, 
					EmploymentRootAtr.APPLICATION, 
					appType, 
					appDate);
			approvalRootState = approvalRootContentOutput.getApprovalRootState();
		} else {
			List<ApprovalRootState> opApprovalRootState = approvalRootStateRepository.findByID(rootStateID);
			if(opApprovalRootState.isEmpty()){
				throw new RuntimeException("状態：承認ルート取得失敗"+System.getProperty("line.separator")+"error: ApprovalRootState, ID: "+rootStateID);
			}
			approvalRootState = opApprovalRootState.get(0);
		}
		if(!(approvalPhaseStateNumber>=1&&approvalPhaseStateNumber<=5)){
			return mailList;
		}
		Integer i = approvalPhaseStateNumber;
		List<ApprovalPhaseState> afterList = approvalRootState.getListApprovalPhaseState().stream()
				.filter(x -> x.getPhaseOrder() >= approvalPhaseStateNumber).collect(Collectors.toList());
		for(ApprovalPhaseState approvalPhaseState : afterList){
			List<String> listApprover = judgmentApprovalStatusService.getApproverFromPhase(approvalPhaseState);
			if(CollectionUtil.isEmpty(listApprover)){
				i++;
				continue;
			}
			List<String> listUnapproveApprover = this.getUnapproveApproverFromPhase(approvalPhaseState);
			if(CollectionUtil.isEmpty(listUnapproveApprover)){
				break;
			}
			ApprovalRepresenterOutput approvalRepresenterOutput = collectApprovalAgentInforService.getApprovalAgentInfor(companyID, listUnapproveApprover);
			List<String> destinationList = this.judgmentDestination(approvalRepresenterOutput.getListApprovalAgentInfor());
			mailList.addAll(destinationList);
			break;
		}
		return mailList;
	}

	@Override
	public List<String> judgmentDestination(List<ApprovalRepresenterInforOutput> listApprovalRepresenterInforOutput) {
		List<String> destinationList = new ArrayList<>();
		if(CollectionUtil.isEmpty(listApprovalRepresenterInforOutput)){
			return destinationList;
		}
		listApprovalRepresenterInforOutput.stream().forEach(approvalRepresenterInforOutput -> {
			if(approvalRepresenterInforOutput.getRepresenter().getValue().equals(RepresenterInforOutput.None_Information)){
				destinationList.add(approvalRepresenterInforOutput.getApprover());
				return;
			}
			if(approvalRepresenterInforOutput.getRepresenter().getValue().equals(RepresenterInforOutput.Path_Information)){
				return;
			}
			destinationList.add(approvalRepresenterInforOutput.getApprover());
			destinationList.add(approvalRepresenterInforOutput.getRepresenter().getValue());
		});
		return destinationList;
	}
}
