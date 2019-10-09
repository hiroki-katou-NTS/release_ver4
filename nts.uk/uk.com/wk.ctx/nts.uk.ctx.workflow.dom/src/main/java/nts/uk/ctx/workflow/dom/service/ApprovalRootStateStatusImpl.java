package nts.uk.ctx.workflow.dom.service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;

import nts.arc.time.GeneralDate;
import nts.uk.ctx.workflow.dom.approverstatemanagement.ApprovalBehaviorAtr;
import nts.uk.ctx.workflow.dom.approverstatemanagement.ApprovalFrame;
import nts.uk.ctx.workflow.dom.approverstatemanagement.ApprovalPhaseState;
import nts.uk.ctx.workflow.dom.approverstatemanagement.ApprovalRootState;
import nts.uk.ctx.workflow.dom.approverstatemanagement.DailyConfirmAtr;
import nts.uk.ctx.workflow.dom.service.output.ApprovalRootStateStatus;
/**
 * 
 * @author Doan Duy Hung
 *
 */
@Stateless
public class ApprovalRootStateStatusImpl implements ApprovalRootStateStatusService{
	
	@Inject
	private ApprovalRootStateService approvalRootStateService;
	
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	@Override
	public List<ApprovalRootStateStatus> getStatusByEmpAndDate(String employeeID, GeneralDate startDate, GeneralDate endDate,
			Integer rootType) {
		List<ApprovalRootState> approvalRootStateList = approvalRootStateService.getByPeriod(
				employeeID, 
				startDate, 
				endDate, 
				rootType);
		return this.getApprovalRootStateStatus(approvalRootStateList);
	}
	
	@Override
	public List<ApprovalRootStateStatus> getApprovalRootStateStatus(List<ApprovalRootState> approvalRootStateList) {
		List<ApprovalRootStateStatus> resultList = new ArrayList<>();
		approvalRootStateList.forEach(x -> {
			resultList.add(new ApprovalRootStateStatus(
					x.getApprovalRecordDate(), 
					x.getEmployeeID(), 
					this.determineDailyConfirm(x)));
		});
		return resultList;
	}

	@Override
	public DailyConfirmAtr determineDailyConfirm(ApprovalRootState approvalRootState) {
		// ステータス＝未承認, 未承認フェーズあり＝false
		DailyConfirmAtr dailyConfirmAtr = DailyConfirmAtr.UNAPPROVED;
		Boolean currentPhaseUnapproved = false;
		// ドメインモデル「承認フェーズインスタンス」．順序5～1の順でループする
		approvalRootState.getListApprovalPhaseState().sort(Comparator.comparing(ApprovalPhaseState::getPhaseOrder).reversed());
		for(ApprovalPhaseState approvalPhaseState : approvalRootState.getListApprovalPhaseState()){
			// ループ中のドメインモデル「承認フェーズインスタンス」．承認区分をチェックする
			if(!approvalPhaseState.getApprovalAtr().equals(ApprovalBehaviorAtr.APPROVED)){
				currentPhaseUnapproved = true;
				// ループ中の承認フェーズの承認枠をチェックする
				Optional<ApprovalFrame> anyAppovedFrame = approvalPhaseState.getListApprovalFrame()
						.stream().filter(x -> x.getApprovalAtr().equals(ApprovalBehaviorAtr.APPROVED)).findAny();
				if(anyAppovedFrame.isPresent()){
					dailyConfirmAtr = DailyConfirmAtr.ON_APPROVED;
					break;
				}
				continue;
			} 
			if(currentPhaseUnapproved){
				dailyConfirmAtr = DailyConfirmAtr.ON_APPROVED;
			} else {
				dailyConfirmAtr = DailyConfirmAtr.ALREADY_APPROVED;
			}
			break;
		}
		return dailyConfirmAtr;
	}
}
