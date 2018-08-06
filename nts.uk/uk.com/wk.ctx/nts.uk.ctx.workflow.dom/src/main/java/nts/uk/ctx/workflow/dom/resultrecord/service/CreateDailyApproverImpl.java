package nts.uk.ctx.workflow.dom.resultrecord.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.arc.enums.EnumAdaptor;
import nts.arc.time.GeneralDate;
import nts.uk.ctx.workflow.dom.approvermanagement.workroot.ConfirmationRootType;
import nts.uk.ctx.workflow.dom.resultrecord.AppFrameInstance;
import nts.uk.ctx.workflow.dom.resultrecord.AppPhaseInstance;
import nts.uk.ctx.workflow.dom.resultrecord.AppRootInstance;
import nts.uk.ctx.workflow.dom.resultrecord.AppRootInstanceRepository;
import nts.uk.ctx.workflow.dom.resultrecord.AppRootConfirmRepository;
import nts.uk.ctx.workflow.dom.resultrecord.RecordRootType;
import nts.uk.ctx.workflow.dom.service.CollectApprovalRootService;
import nts.uk.ctx.workflow.dom.service.output.ApprovalRootContentOutput;
import nts.uk.ctx.workflow.dom.service.output.ErrorFlag;
import nts.uk.shr.com.context.AppContexts;
import nts.uk.shr.com.time.calendar.period.DatePeriod;
/**
 * 
 * @author Doan Duy Hung
 *
 */
@Stateless
public class CreateDailyApproverImpl implements CreateDailyApprover {
	
	@Inject
	private AppRootInstanceRepository appRootInstanceRepository;
	
	@Inject
	private AppRootConfirmRepository appRootConfirmRepository;
	
	@Inject
	private CollectApprovalRootService collectApprovalRootService;

	@Override
	public AppRootInstanceContent createDailyApprover(String employeeID, RecordRootType rootType, GeneralDate recordDate) {
		String companyID = AppContexts.user().companyId();
		// 承認ルートを取得する（確認）
		ApprovalRootContentOutput approvalRootContentOutput = collectApprovalRootService.getApprovalRootConfirm(
																companyID, 
																employeeID, 
																EnumAdaptor.valueOf(rootType.value-1, ConfirmationRootType.class), 
																recordDate);
		AppRootInstance appRootInstance = new AppRootInstance(
				approvalRootContentOutput.getApprovalRootState().getRootStateID(), 
				companyID, 
				employeeID, 
				new DatePeriod(recordDate, GeneralDate.fromString("9999/12/31", "yyyy/MM/dd")), 
				rootType, 
				approvalRootContentOutput.getApprovalRootState().getListApprovalPhaseState().stream()
					.map(x -> new AppPhaseInstance(
							x.getPhaseOrder(), 
							x.getApprovalForm(), 
							x.getListApprovalFrame().stream()
								.map(y -> new AppFrameInstance(
										y.getFrameOrder(), 
										y.getConfirmAtr().value==1?true:false, 
										y.getListApproverState().stream().map(z -> z.getApproverID())
								.collect(Collectors.toList())))
					.collect(Collectors.toList())))
				.collect(Collectors.toList()));
		ErrorFlag errorFlag = approvalRootContentOutput.getErrorFlag();
		String errorMsgID = "";
		switch (errorFlag) {
		case NO_APPROVER:
			errorMsgID = "Msg_324";
			break;
		case NO_CONFIRM_PERSON:
			errorMsgID = "Msg_326";
			break;
		case APPROVER_UP_10:
			errorMsgID = "Msg_325";
			break;
		default:
			break;
		}
		if(errorFlag!=ErrorFlag.NO_ERROR){
			// create log
		}
		// ドメインモデル「承認ルート中間データ」を取得する
		Optional<AppRootInstance> opAppRootInstanceConflict = appRootInstanceRepository.findByEmpDate(companyID, employeeID, recordDate, rootType);
		if(opAppRootInstanceConflict.isPresent()){
			// 履歴期間．開始日が一番新しいドメインモデル「承認ルート中間データ」を取得する
			AppRootInstance appRootInstanceNewest = appRootInstanceRepository.findByEmpDateNewest(companyID, employeeID, rootType).get();
			// output．承認ルートの内容は取得したドメインモデル「承認ルート中間データ」を比較する
			boolean isSame = compareAppRootContent(appRootInstanceNewest, appRootInstance);
			if(isSame){
				return new AppRootInstanceContent(appRootInstanceNewest, errorFlag, errorMsgID);
			}
		}
		// 取得した承認ルートをドメインモデル「承認ルート中間データ」にINSERTする
		appRootInstanceRepository.insert(appRootInstance);
		// 履歴期間．開始日が一番新しいドメインモデル「承認ルート中間データ」をUPDATEする
		appRootInstanceRepository.findByEmpDateNewest(companyID, employeeID, rootType).ifPresent(x -> {
			GeneralDate start = x.getDatePeriod().start();
			GeneralDate end = x.getDatePeriod().end().addDays(-1);
			x.setDatePeriod(new DatePeriod(start, end));
			appRootInstanceRepository.update(x);
		});
		//承認状態をクリアする
		appRootConfirmRepository.clearStatus(companyID, employeeID, recordDate, rootType);
		return new AppRootInstanceContent(appRootInstance, errorFlag, errorMsgID);
	}
	
	private boolean compareAppRootContent(AppRootInstance oldAppRoot, AppRootInstance newAppRoot){
		for(AppPhaseInstance oldAppPhase : oldAppRoot.getListAppPhase()){
			Optional<AppPhaseInstance> opNewAppPhaseLoop = 
					newAppRoot.getListAppPhase().stream().filter(x -> x.getPhaseOrder()==oldAppPhase.getPhaseOrder()).findAny();
			if(!opNewAppPhaseLoop.isPresent()){
				return false;
			}
			boolean isSame = compareAppPhaseContent(oldAppPhase, opNewAppPhaseLoop.get());
			if(!isSame){
				return false;
			}
		}
		return true;
	}
	
	private boolean compareAppPhaseContent(AppPhaseInstance oldAppPhase, AppPhaseInstance newAppPhase){
		for(AppFrameInstance oldAppFrame : oldAppPhase.getListAppFrame()){
			Optional<AppFrameInstance> opNewAppFrameLoop = 
					newAppPhase.getListAppFrame().stream().filter(x -> x.getFrameOrder()==oldAppFrame.getFrameOrder()).findAny();
			if(!opNewAppFrameLoop.isPresent()){
				return false;
			}
			boolean isSame = compareAppFrameContent(oldAppFrame, opNewAppFrameLoop.get());
			if(!isSame){
				return false;
			}
		}
		return true;
	}
	
	private boolean compareAppFrameContent(AppFrameInstance oldAppFrame, AppFrameInstance newAppFrame){
		List<String> oldList = oldAppFrame.getListApprover();
		List<String> newList = newAppFrame.getListApprover();
		return oldList.containsAll(newList) && newList.containsAll(oldList);
	}

}
