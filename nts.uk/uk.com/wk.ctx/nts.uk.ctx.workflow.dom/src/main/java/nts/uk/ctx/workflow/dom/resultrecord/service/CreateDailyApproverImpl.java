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
	public AppRootInstanceContent createDailyApprover(String employeeID, RecordRootType rootType, GeneralDate recordDate, GeneralDate closureStartDate) {
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
				new DatePeriod(GeneralDate.fromString("1900/01/01", "yyyy/MM/dd"), GeneralDate.fromString("9999/12/31", "yyyy/MM/dd")), 
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
		case ABNORMAL_TERMINATION:
			errorMsgID = "Msg_1339";
			break;
		default:
			break;
		}
		if(errorFlag!=ErrorFlag.NO_ERROR){
			return new AppRootInstanceContent(appRootInstance, errorFlag, errorMsgID);
		}
		// ドメインモデル「承認ルート中間データ」を削除する
		List<AppRootInstance> opAppRootInstanceOverLst = appRootInstanceRepository.findByEmpFromDate(companyID, employeeID, recordDate, rootType);
		for(AppRootInstance appRootInstanceOver : opAppRootInstanceOverLst){
			appRootInstanceRepository.delete(appRootInstanceOver);
		}
		// ドメインモデル「承認ルート中間データ」を取得する
		Optional<AppRootInstance> opAppRootInstanceConflict = appRootInstanceRepository.findByEmpDateNewestBelow(companyID, employeeID, recordDate, rootType);
		if(opAppRootInstanceConflict.isPresent()){
			// 履歴期間．開始日が一番新しいドメインモデル「承認ルート中間データ」を取得する
			AppRootInstance appRootInstanceConflict = opAppRootInstanceConflict.get();
			// output．承認ルートの内容は取得したドメインモデル「承認ルート中間データ」を比較する
			boolean isSame = compareAppRootContent(appRootInstanceConflict, appRootInstance)
					&& compareAppRootContent(appRootInstance, appRootInstanceConflict);
			if(isSame){
				// 履歴期間．開始日が一番新しいドメインモデル「承認ルート中間データ」をUPDATEする
				DatePeriod oldPeriod = appRootInstanceConflict.getDatePeriod();
				appRootInstanceConflict.setDatePeriod(new DatePeriod(oldPeriod.start(), GeneralDate.fromString("9999/12/31", "yyyy/MM/dd")));
				appRootInstanceRepository.update(appRootInstanceConflict);
				return new AppRootInstanceContent(appRootInstanceConflict, errorFlag, errorMsgID);
			} else {
				// 履歴の開始日を取得する
				GeneralDate startHistDate = this.getHistoryStartDate(companyID, employeeID, rootType, recordDate, closureStartDate, appRootInstanceConflict);
				// ドメインモデル「承認ルート中間データ」を削除する
				List<AppRootInstance> opAppRootIns = appRootInstanceRepository.findByEmpFromDate(companyID, employeeID, startHistDate, rootType);
				for(AppRootInstance appRootInstanceOver : opAppRootIns){
					appRootInstanceRepository.delete(appRootInstanceOver);
				}
				appRootInstance.setDatePeriod(new DatePeriod(startHistDate, GeneralDate.fromString("9999/12/31", "yyyy/MM/dd")));
				// 履歴期間．開始日が一番新しいドメインモデル「承認ルート中間データ」を取得する
				AppRootInstance appRootInstNewest = appRootInstanceRepository.findByEmpDateNewestBelow(companyID, employeeID, startHistDate, rootType).get();
				// 履歴期間．開始日が一番新しいドメインモデル「承認ルート中間データ」をUPDATEする
				DatePeriod oldPeriod = appRootInstNewest.getDatePeriod();
				appRootInstNewest.setDatePeriod(new DatePeriod(oldPeriod.start(), startHistDate.addDays(-1)));
				appRootInstanceRepository.update(appRootInstNewest);
				//承認状態をクリアする
				appRootConfirmRepository.clearStatusFromDate(companyID, employeeID, recordDate, rootType);
			}
		}
		// 取得した承認ルートをドメインモデル「承認ルート中間データ」にINSERTする
		appRootInstanceRepository.insert(appRootInstance);
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

	/**
	 * 履歴の開始日を取得する
	 * @param companyID
	 * @param employeeID
	 * @param rootType
	 * @param date
	 * @param closureStartDate
	 * @param appRootInstance
	 * @return
	 */
	private GeneralDate getHistoryStartDate(String companyID, String employeeID, RecordRootType rootType, GeneralDate date, 
			GeneralDate closureStartDate, AppRootInstance appRootInstance){
		if(rootType==RecordRootType.CONFIRM_WORK_BY_MONTH){
			return date;
		}
		// input．年月日－１日～締め開始日まで－１日ずつループする
		GeneralDate loopDate = date.addDays(-1);
		do {
			AppRootInstance compareAppIns = appRootInstance;
			// 承認ルートを取得する（確認）
			ApprovalRootContentOutput approvalRootContentOutput = collectApprovalRootService.getApprovalRootConfirm(
					companyID, 
					employeeID, 
					EnumAdaptor.valueOf(rootType.value-1, ConfirmationRootType.class), 
					loopDate);
			AppRootInstance appRootInsRs = new AppRootInstance(
					approvalRootContentOutput.getApprovalRootState().getRootStateID(), 
					companyID, 
					employeeID, 
					new DatePeriod(GeneralDate.fromString("1900/01/01", "yyyy/MM/dd"), GeneralDate.fromString("9999/12/31", "yyyy/MM/dd")), 
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
			if(appRootInstance.getDatePeriod().start().after(loopDate)){
				// ドメインモデル「承認ルート中間データ」を取得する
				Optional<AppRootInstance> opAppIns = appRootInstanceRepository.findByContainDate(companyID, employeeID, loopDate, rootType);
				if(opAppIns.isPresent()){
					compareAppIns = opAppIns.get();
				}
			}
			// output．承認ルートの内容は取得したドメインモデル「承認ルート中間データ」を比較する
			boolean isSame = compareAppRootContent(appRootInsRs, compareAppIns)
					&& compareAppRootContent(compareAppIns, appRootInsRs);
			if(isSame){
				// 履歴開始日＝ループ中の年月日+1日
				return loopDate.addDays(1);
			}
			// ループ中の年月日 ← ループ中の年月日－1日
			loopDate = loopDate.addDays(-1);
		} while(loopDate.afterOrEquals(closureStartDate));
		// 履歴開始日＝締め開始日
		return closureStartDate;
	}
}
