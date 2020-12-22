package nts.uk.ctx.workflow.dom.service.resultrecord;

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
import nts.uk.ctx.workflow.dom.approvermanagement.workroot.ApprovalForm;
import nts.uk.ctx.workflow.dom.approvermanagement.workroot.ConfirmPerson;
import nts.uk.ctx.workflow.dom.approverstatemanagement.ApprovalBehaviorAtr;
import nts.uk.ctx.workflow.dom.approverstatemanagement.ApprovalFrame;
import nts.uk.ctx.workflow.dom.approverstatemanagement.ApprovalPhaseState;
import nts.uk.ctx.workflow.dom.approverstatemanagement.ApproverInfor;
import nts.uk.ctx.workflow.dom.resultrecord.AppFrameConfirm;
import nts.uk.ctx.workflow.dom.resultrecord.AppFrameInstance;
import nts.uk.ctx.workflow.dom.resultrecord.AppPhaseConfirm;
import nts.uk.ctx.workflow.dom.resultrecord.AppPhaseInstance;
import nts.uk.ctx.workflow.dom.resultrecord.AppRootConfirm;
import nts.uk.ctx.workflow.dom.resultrecord.AppRootConfirmRepository;
import nts.uk.ctx.workflow.dom.resultrecord.AppRootInstance;
import nts.uk.ctx.workflow.dom.service.ApproveService;
import nts.uk.ctx.workflow.dom.service.CollectApprovalAgentInforService;
import nts.uk.ctx.workflow.dom.service.output.ApprovalRepresenterOutput;
import nts.uk.shr.com.context.AppContexts;
/**
 * 
 * @author Doan Duy Hung
 *
 */
@Stateless
public class AppRootConfirmServiceImpl implements AppRootConfirmService {
	
	@Inject
	private CollectApprovalAgentInforService collectApprovalAgentInforService;
	
	@Inject
	private ApproveService approveService;
	
	@Inject
	private AppRootConfirmRepository appRootConfirmRepository;

	@Override
	public void approve(String approverID, String employeeID, GeneralDate date, AppRootInstance appRootInstance, AppRootConfirm appRootConfirm) {
		String companyID = AppContexts.user().companyId();
		// INPUT．ドメインモデル「就業実績確認状態」．承認済フェーズ．順序を1～5の順でループする
		List<AppPhaseInstance> appPhaseInstanceLst = appRootInstance.getListAppPhase().stream()
				.sorted(Comparator.comparing(AppPhaseInstance::getPhaseOrder)).collect(Collectors.toList());
		for(AppPhaseInstance appPhaseInstance : appPhaseInstanceLst){
			AppPhaseConfirm appPhaseConfirm = new AppPhaseConfirm(appPhaseInstance.getPhaseOrder(), ApprovalBehaviorAtr.UNAPPROVED, new ArrayList<>());
			// ループする順の「承認済フェーズ」が承認済かチェックする
			Optional<AppPhaseConfirm> opAppPhaseConfirm = appRootConfirm.getListAppPhase().stream()
					.filter(phaseConfirm -> phaseConfirm.getPhaseOrder()==appPhaseInstance.getPhaseOrder()).findAny();
			if(opAppPhaseConfirm.isPresent()){
				appPhaseConfirm = opAppPhaseConfirm.get();
				if(appPhaseConfirm.getAppPhaseAtr()==ApprovalBehaviorAtr.APPROVED){
					continue;
				}
			}
			// 承認したフラグ=false(初期化)
			ApprovalBehaviorAtr approvalFlg = ApprovalBehaviorAtr.UNAPPROVED;
			// ループ中の「承認済承認者」．順序を1～5の順でループする
			for(AppFrameInstance frameInstance : appPhaseInstance.getListAppFrame()){
				// ループする順の「承認済承認者」が承認済かチェックする
				Optional<AppFrameConfirm> opAppFrameConfirm = appPhaseConfirm.getListAppFrame().stream()
						.filter(frameConfirm -> frameConfirm.getFrameOrder()==frameInstance.getFrameOrder()).findAny();
				if(opAppFrameConfirm.isPresent()){
					continue;
				}
				// 指定する承認者が該当承認枠の承認者かチェックする
				String approverIDParam = null, representerIDParam = null;
				if(!frameInstance.getListApprover().contains(approverID)){
					ApprovalRepresenterOutput approvalRepresenterOutput = collectApprovalAgentInforService.getApprovalAgentInfor(companyID, frameInstance.getListApprover());
					if(!approvalRepresenterOutput.getListAgent().contains(approverID)){
						continue;
					} else {
						representerIDParam = approverID;
					}
				} else {
					approverIDParam = approverID;
				}
				// ドメインモデル「承認済承認者」を追加する
				AppFrameConfirm appFrameConfirm = new AppFrameConfirm(
						frameInstance.getFrameOrder(), 
						Optional.ofNullable(approverIDParam), 
						Optional.ofNullable(representerIDParam), 
						GeneralDate.today());
				appPhaseConfirm.getListAppFrame().add(appFrameConfirm);
				// 承認したフラグ=true
				approvalFlg = ApprovalBehaviorAtr.APPROVED;
			}
			// 承認したフラグをチェックする
			if(approvalFlg==ApprovalBehaviorAtr.APPROVED){
				// ループする順の「承認済フェーズ」が存在するかチェックする
				if(!opAppPhaseConfirm.isPresent()){
					// ドメインモデル「承認済フェーズ」を追加する
					appRootConfirm.getListAppPhase().add(appPhaseConfirm);
				} else {
					AppPhaseConfirm oldAppPhaseConfirm = opAppPhaseConfirm.get();
					appRootConfirm.getListAppPhase().remove(oldAppPhaseConfirm);
					appRootConfirm.getListAppPhase().add(appPhaseConfirm);
				}
			}
			// 中間データから承認フェーズインスタンスに変換する
			ApprovalPhaseState approvalPhaseState = this.convertPhaseInsToPhaseState(appPhaseInstance, appPhaseConfirm);
			// 指定する承認フェーズの承認が完了したか
			boolean phaseComplete = approveService.isApproveApprovalPhaseStateComplete(companyID, approvalPhaseState);
			// ループする順のドメインモデル「承認済フェーズ」を更新する
			if(phaseComplete){
				appPhaseConfirm.setAppPhaseAtr(ApprovalBehaviorAtr.APPROVED);
			} else {
				break;
			}
		}
		appRootConfirmRepository.update(appRootConfirm);
	}

	@Override
	public boolean cleanStatus(String approverID, String employeeID, GeneralDate date, AppRootInstance appRootInstance, AppRootConfirm appRootConfirm) {
		// 解除を実行したかフラグ=false(初期化)
		boolean cleanComplete = false;
		// ループ終了フラグ=false(初期化)
		boolean loopCompleteFlg = false;
		// INPUT．ドメインモデル「承認ルート中間データ」．承認フェーズ中間データ．順序を5～1の順でループする
		List<AppPhaseInstance> appPhaseInstanceLst = appRootInstance.getListAppPhase().stream()
				.sorted(Comparator.comparing(AppPhaseInstance::getPhaseOrder).reversed()).collect(Collectors.toList());
		for(AppPhaseInstance appPhaseInstance : appPhaseInstanceLst){
			// (中間データ版)承認フェーズ中間データ毎の承認者を取得する
			List<String> approverLst = this.getApproverFromPhase(appPhaseInstance);
			if(approverLst.isEmpty()){
				// ループ終了フラグをチェックする
				if(loopCompleteFlg){
					break;
				}
				continue;
			}
			// ループ中の承認フェーズには承認を行ったか
			Optional<AppPhaseConfirm> opAppPhaseConfirm = appRootConfirm.getListAppPhase().stream()
				.filter(phaseConfirm -> phaseConfirm.getPhaseOrder()==appPhaseInstance.getPhaseOrder()).findAny();
			if(!opAppPhaseConfirm.isPresent()){
				// ループ終了フラグをチェックする
				if(loopCompleteFlg){
					break;
				}
				continue;
			}
			// 中間データから承認フェーズインスタンスに変換する
			AppPhaseConfirm appPhaseConfirm = opAppPhaseConfirm.get();
			ApprovalPhaseState approvalPhaseState = this.convertPhaseInsToPhaseState(appPhaseInstance, appPhaseConfirm);
			// 解除できるかチェックする
			if(!this.canCancelCheck(approvalPhaseState, approverID)) {
				break;
			}
			List<ApprovalFrame> confirmFrameLst = approvalPhaseState.getListApprovalFrame().stream().filter(x -> x.getConfirmAtr()==ConfirmPerson.CONFIRM).collect(Collectors.toList());
			// 承認形態と確定区分をチェックする
			if((approvalPhaseState.getApprovalForm()==ApprovalForm.SINGLE_APPROVED)&&confirmFrameLst.isEmpty()){
				appRootConfirm.getListAppPhase().remove(appPhaseConfirm);
				// 解除を実行したかフラグ=true
				cleanComplete = true;
				// ループ終了フラグをチェックする
				if(loopCompleteFlg){
					break;
				} 
				continue;
			}
			// ループ終了フラグ=false(初期化)
			loopCompleteFlg = false;
			for(AppFrameInstance appFrameInstance : appPhaseInstance.getListAppFrame()){
				Optional<AppFrameConfirm> opAppFrameConfirm = appPhaseConfirm.getListAppFrame().stream()
						.filter(frameConfirm -> frameConfirm.getFrameOrder()==appFrameInstance.getFrameOrder()).findAny();
				if(!opAppFrameConfirm.isPresent()){
					continue;
				}
				AppFrameConfirm appFrameConfirm = opAppFrameConfirm.get();
				// 指定する承認者が承認を行った承認者かチェックする
				if(appFrameInstance.getListApprover().contains(approverID) || appFrameConfirm.getRepresenterID().orElse("").equals(approverID)){
					// ループする枠番のドメインモデル「承認済承認者」を削除する
					appPhaseConfirm.getListAppFrame().remove(appFrameConfirm);
					cleanComplete = true;
				}
			} 
			// ループする順の「承認済承認者」があるかチェックする
			if(CollectionUtil.isEmpty(appPhaseConfirm.getListAppFrame())){
				// ループする順の「承認済フェーズ」を削除する	
				appRootConfirm.getListAppPhase().remove(appPhaseConfirm);
			} else {
				// ループする順の「承認済フェーズ」．承認区分=未承認
				appPhaseConfirm.setAppPhaseAtr(ApprovalBehaviorAtr.UNAPPROVED);
				// ループ終了フラグ=true
				loopCompleteFlg = true;
			}
			// ループ終了フラグをチェックする
			if(loopCompleteFlg){
				break;
			}
		}
		appRootConfirmRepository.update(appRootConfirm);
		return cleanComplete;
	}

	@Override
	public ApprovalPhaseState convertPhaseInsToPhaseState(AppPhaseInstance appPhaseInstance, AppPhaseConfirm appPhaseConfirm) {
		// output「承認フェーズインスタンス」を初期化
		ApprovalPhaseState approvalPhaseState = new ApprovalPhaseState();
		approvalPhaseState.setApprovalAtr(appPhaseConfirm.getAppPhaseAtr());
		approvalPhaseState.setPhaseOrder(appPhaseInstance.getPhaseOrder());
		approvalPhaseState.setApprovalForm(appPhaseInstance.getApprovalForm());
		approvalPhaseState.setListApprovalFrame(new ArrayList<>());
		appPhaseInstance.getListAppFrame().forEach(frameInstance -> {
			ApprovalFrame approvalFrame = new ApprovalFrame();
			approvalFrame.setFrameOrder(frameInstance.getFrameOrder());
			approvalFrame.setConfirmAtr(frameInstance.isConfirmAtr() ? ConfirmPerson.CONFIRM : ConfirmPerson.NOT_CONFIRM);
			approvalFrame.setLstApproverInfo(new ArrayList<>());
			frameInstance.getListApprover().forEach(approver -> {
				ApproverInfor approverState = new ApproverInfor();
				approverState.setApprovalAtr(ApprovalBehaviorAtr.UNAPPROVED);
				approverState.setApproverID(approver);
				approvalFrame.getLstApproverInfo().add(approverState);
			});
			approvalPhaseState.getListApprovalFrame().add(approvalFrame);
		});
		approvalPhaseState.getListApprovalFrame().forEach(frame -> {
			Optional<AppFrameConfirm> opAppFrameConfirm = appPhaseConfirm.getListAppFrame().stream()
					.filter(frameConfirm -> frameConfirm.getFrameOrder()==frame.getFrameOrder()).findAny();
			if(opAppFrameConfirm.isPresent()){
				AppFrameConfirm appFrameConfirm = opAppFrameConfirm.get();
				if(!CollectionUtil.isEmpty(frame.getLstApproverInfo())) {
					frame.getLstApproverInfo().get(0).setApprovalAtr(ApprovalBehaviorAtr.APPROVED);
					frame.getLstApproverInfo().get(0).setApproverID(appFrameConfirm.getApproverID().orElse(""));
					frame.getLstApproverInfo().get(0).setAgentID(appFrameConfirm.getRepresenterID().orElse(""));
				}
				frame.setAppDate(appFrameConfirm.getApprovalDate());
			}
		});
		return approvalPhaseState;
	}

	@Override
	public List<String> getApproverFromPhase(AppPhaseInstance appPhaseInstance) {
		// 承認者社員ID一覧をクリアする（初期化）
		List<String> result = new ArrayList<>();
		// 承認枠１～５ループする(loop approve frame １～５)
		appPhaseInstance.getListAppFrame().forEach(appFrame -> {
			// ループ中の「承認枠」．承認者リストを承認者社員ID一覧に追加する
			result.addAll(appFrame.getListApprover());
		});
		// 社員ID重複な承認者を消す
		return result.stream().distinct().collect(Collectors.toList());
	}

	@Override
	public boolean canCancelCheck(ApprovalPhaseState approvalPhaseState, String employeeID) {
		// 解除できるフラグ = false(初期化)
		boolean canCancel = false;
		// 「承認フェーズインスタンス」．承認形態をチェックする
		if(approvalPhaseState.getApprovalForm()==ApprovalForm.EVERYONE_APPROVED){
			// 指定する社員が承認を行った承認者かチェックする
			Optional<ApproverInfor> opApproverInfor = Optional.empty();
			List<ApprovalFrame> approvalFrameApprovedLst = approvalPhaseState.getListApprovalFrame().stream()
					.filter(frame -> frame.isApproved(approvalPhaseState.getApprovalForm())).collect(Collectors.toList());
			for(ApprovalFrame approvalFrame : approvalFrameApprovedLst) {
				Optional<ApproverInfor> opApproverInforApproved = approvalFrame.getLstApproverInfo().stream()
						.filter(approverInfor -> approverInfor.isApproved() && 
								((Strings.isNotBlank(approverInfor.getApproverID())&&approverInfor.getApproverID().equals(employeeID)) || 
								(Strings.isNotBlank(approverInfor.getAgentID())&&approverInfor.getAgentID().equals(employeeID))))
						.findAny();
				if(opApproverInforApproved.isPresent()) {
					opApproverInfor = opApproverInforApproved;
				}
			}
			if(opApproverInfor.isPresent()){
				// 解除できるフラグ = true
				canCancel = true;
			}
		} else {
			// 確定者を設定したかチェックする(kiểm tra có cài đặt 確定者 hay không)
			Optional<ApprovalFrame> opFrameConfirm = approvalPhaseState.getListApprovalFrame().stream()
					.filter(frame -> frame.getConfirmAtr()==ConfirmPerson.CONFIRM).findAny();
			if(opFrameConfirm.isPresent()){
				// 指定する社員が確定者として承認を行ったかチェックする
				ApprovalFrame approvalFrame = opFrameConfirm.get();
				Optional<ApproverInfor> opApproverInforApproved = approvalFrame.getLstApproverInfo().stream()
						.filter(approverInfor -> approverInfor.isApproved() && 
								((Strings.isNotBlank(approverInfor.getApproverID())&&approverInfor.getApproverID().equals(employeeID)) || 
								(Strings.isNotBlank(approverInfor.getAgentID())&&approverInfor.getAgentID().equals(employeeID))))
						.findAny();
				if(opApproverInforApproved.isPresent()) {
					// 解除できるフラグ = true
					canCancel = true;
				}
			} else {
				// 指定する社員が承認を行った承認者かチェックする
				Optional<ApproverInfor> opApproverInfor = Optional.empty();
				List<ApprovalFrame> approvalFrameApprovedLst = approvalPhaseState.getListApprovalFrame().stream()
						.filter(frame -> frame.isApproved(approvalPhaseState.getApprovalForm())).collect(Collectors.toList());
				for(ApprovalFrame approvalFrame : approvalFrameApprovedLst) {
					Optional<ApproverInfor> opApproverInforApproved = approvalFrame.getLstApproverInfo().stream()
							.filter(approverInfor -> approverInfor.isApproved() && 
									((Strings.isNotBlank(approverInfor.getApproverID())&&approverInfor.getApproverID().equals(employeeID)) || 
									(Strings.isNotBlank(approverInfor.getAgentID())&&approverInfor.getAgentID().equals(employeeID))))
							.findAny();
					if(opApproverInforApproved.isPresent()) {
						opApproverInfor = opApproverInforApproved;
					}
				}
				if(opApproverInfor.isPresent()){
					// 解除できるフラグ = true
					canCancel = true;
				}
			}
		}
		return canCancel;
	}

}
