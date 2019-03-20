package nts.uk.ctx.at.record.dom.workrecord.actualsituation.approvalstatusmonthly;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.arc.enums.EnumAdaptor;
import nts.arc.time.GeneralDate;
import nts.arc.time.YearMonth;
import nts.uk.ctx.at.record.dom.adapter.company.StatusOfEmployeeExport;
import nts.uk.ctx.at.record.dom.adapter.company.SyCompanyRecordAdapter;
import nts.uk.ctx.at.record.dom.adapter.workflow.service.ApprovalStatusAdapter;
import nts.uk.ctx.at.record.dom.adapter.workflow.service.dtos.ApprovalRootOfEmployeeImport;
import nts.uk.ctx.at.record.dom.adapter.workflow.service.dtos.ApprovalRootSituation;
import nts.uk.ctx.at.record.dom.adapter.workflow.service.dtos.ApproveRootStatusForEmpImport;
import nts.uk.ctx.at.record.dom.adapter.workflow.service.enums.ApprovalActionByEmpl;
import nts.uk.ctx.at.record.dom.adapter.workflow.service.enums.ApprovalStatusForEmployee;
import nts.uk.ctx.at.record.dom.adapter.workflow.service.enums.ApproverEmployeeState;
import nts.uk.ctx.at.record.dom.approvalmanagement.ApprovalProcessingUseSetting;
import nts.uk.ctx.at.record.dom.dailyperformanceprocessing.finddata.IFindDataDCRecord;
import nts.uk.ctx.at.record.dom.workrecord.actualsituation.confirmstatusmonthly.AvailabilityAtr;
import nts.uk.ctx.at.record.dom.workrecord.actualsituation.confirmstatusmonthly.ReleasedAtr;
import nts.uk.ctx.at.record.dom.workrecord.actualsituation.identificationstatus.export.CheckIndentityDayConfirm;
import nts.uk.ctx.at.record.dom.workrecord.identificationstatus.IdentityProcessUseSet;
import nts.uk.ctx.at.record.dom.workrecord.identificationstatus.month.ConfirmationMonth;
import nts.uk.ctx.at.record.dom.workrecord.identificationstatus.repository.ConfirmationMonthRepository;
import nts.uk.ctx.at.record.dom.workrecord.identificationstatus.repository.IdentityProcessUseSetRepository;
import nts.uk.ctx.at.shared.dom.workrule.closure.ClosureId;
import nts.uk.ctx.at.shared.dom.workrule.closure.service.ClosureService;
import nts.uk.shr.com.time.calendar.date.ClosureDate;
import nts.uk.shr.com.time.calendar.period.DatePeriod;

/**
 * 月の実績の承認状況を取得する : RQ587
 * @author tutk
 *
 */
@Stateless
public class ApprovalStatusMonthly {
	@Inject
	private IFindDataDCRecord iFindDataDCRecord;
	
	@Inject
	private ClosureService closureService;
	
	@Inject
	private SyCompanyRecordAdapter syCompanyRecordAdapter;
	
	@Inject
	private ApprovalStatusAdapter approvalStatusAdapter;
	
	@Inject
	private IdentityProcessUseSetRepository identityProcessUseSetRepo;
	
	@Inject
	private CheckIndentityDayConfirm checkIndentityDayConfirm;
	
	@Inject
	private ConfirmationMonthRepository confirmationMonthRepo; 
	
	public Optional<ApprovalStatusMonth> getApprovalStatusMonthly(String companyId,String approverId,Integer closureId,ClosureDate closureDate, List<String> listEmployeeId,YearMonth yearmonthInput) {
		//ドメインモデル「承認処理の利用設定」を取得する
		Optional<ApprovalProcessingUseSetting> optApprovalUse = iFindDataDCRecord.findApprovalByCompanyId(companyId);
		if(!optApprovalUse.isPresent()) {
			return Optional.empty();
		}
		if(!optApprovalUse.get().getUseMonthApproverConfirm()) {
			return Optional.empty();
		}
		//指定した年月の期間を算出する
		DatePeriod datePeriod =closureService.getClosurePeriod(closureId, yearmonthInput);
		//社員の指定期間中の所属期間を取得する RQ588
		List<StatusOfEmployeeExport> listStatusOfEmployeeExport = syCompanyRecordAdapter.getListAffComHistByListSidAndPeriod(listEmployeeId, datePeriod)
				.stream().filter(c->c.getEmployeeId() !=null).collect(Collectors.toList());
		if(listStatusOfEmployeeExport.isEmpty())
			return Optional.empty();
		//対応するImported「基準社員の承認対象者」を取得する RQ643
		ApprovalRootOfEmployeeImport  approvalRootOfEmployeeImport =  approvalStatusAdapter.getApprovalRootOfEmloyeeNew(
				datePeriod.end(), datePeriod.end(), approverId, companyId, 2); // 2 : 月別確認
		List<ApprovalStatusResult> listApprovalStatusResult  =new ArrayList<>(); 
		for(String employeeId :listEmployeeId ) {
			Optional<StatusOfEmployeeExport> statusOfEmployeeExport = listStatusOfEmployeeExport.stream().filter(c -> c.getEmployeeId().equals(employeeId)).findFirst();
			if(!statusOfEmployeeExport.isPresent()) {
				continue;
			}
			Optional<ApprovalRootSituation> approvalRootSituation =  approvalRootOfEmployeeImport.getApprovalRootSituations().stream().filter(c ->c.getTargetID().equals(employeeId)).findFirst();
			if(!approvalRootSituation.isPresent()) {
				continue;
			}
			List<GeneralDate> listDate = new ArrayList<>();
			for(DatePeriod period : statusOfEmployeeExport.get().getListPeriod()) {
				listDate.addAll(period.datesBetween());
			}
			//対応するImported「（就業．勤務実績）承認対象者の承認状況」をすべて取得する : RQ462
			List<ApproveRootStatusForEmpImport> lstApprovalStatus = approvalStatusAdapter
					.getApprovalByListEmplAndListApprovalRecordDateNew(Arrays.asList(datePeriod.end()),
							Arrays.asList(employeeId), 2); // 2 : 月別確認
			if(lstApprovalStatus.isEmpty()) {
				continue;
			}
			ApprovalStatusResult approvalStatusResult = new ApprovalStatusResult();
			approvalStatusResult.setEmployeeId(employeeId);
			approvalStatusResult.setYearMonth(yearmonthInput);
			
			//・承認状態：取得した「基準社員の承認対象者．基準社員の承認アクション
			if(approvalRootSituation.get().getApprovalStatus().getApprovalActionByEmpl() == ApprovalActionByEmpl.APPROVALED ) {
				approvalStatusResult.setApprovalStatus(true);
			}else {
				approvalStatusResult.setApprovalStatus(false);
			}
			//・承認状況：取得した「承認対象者の承認状況．承認状況」
			approvalStatusResult.setNormalStatus(lstApprovalStatus.get(0).getApprovalStatus());
			//・解除可否：取得した「基準社員の承認対象者．解除可否区分」
			approvalStatusResult.setWhetherToRelease(EnumAdaptor.valueOf(approvalRootSituation.get().getApprovalStatus().getReleaseDivision().value, ReleasedAtr.class) );
			
			//Output「基準社員の承認対象者．ルート状況．基準社員のルート状況」をチェックする
			if(approvalRootSituation.get().getApprovalAtr() != ApproverEmployeeState.PHASE_DURING ) {
				approvalStatusResult.setImplementaPropriety(AvailabilityAtr.CAN_NOT_RELEASE);
			}
			
			//ドメインモデル「本人確認処理の利用設定」を取得する
			Optional<IdentityProcessUseSet> identityProcessUseSet = identityProcessUseSetRepo.findByKey(companyId);
			if(optApprovalUse.get().getUseDayApproverConfirm()) {
				//対応するImported「（就業．勤務実績）承認対象者の承認状況」をすべて取得する
				List<ApproveRootStatusForEmpImport> lstApprovalStatusDay = approvalStatusAdapter
						.getApprovalByListEmplAndListApprovalRecordDateNew(Arrays.asList(datePeriod.end()),
								Arrays.asList(employeeId), 1); // 1 : 日別確認
				//取得した「承認対象者の承認状況」をチェックする
				if(lstApprovalStatusDay.isEmpty()) {
					continue;
				}
				if(lstApprovalStatusDay.get(0).getApprovalStatus() == ApprovalStatusForEmployee.DURING_APPROVAL 
					||  lstApprovalStatusDay.get(0).getApprovalStatus() == ApprovalStatusForEmployee.UNAPPROVED ) {
					approvalStatusResult.setImplementaPropriety(AvailabilityAtr.CAN_NOT_RELEASE);
				}else {
					approvalStatusResult.setImplementaPropriety(AvailabilityAtr.CAN_RELEASE);
					//取得した「本人確認処理の利用設定．日の本人確認を利用する」をチェックする
					if(!identityProcessUseSet.isPresent()) {
						approvalStatusResult.setImplementaPropriety(AvailabilityAtr.CAN_RELEASE);
						approvalStatusResult.setWhetherToRelease(ReleasedAtr.CAN_RELEASE);
					}else {
						if(identityProcessUseSet.get().isUseConfirmByYourself()) {
							//対象日の本人確認が済んでいるかチェックする
							boolean checkConfirm = checkIndentityDayConfirm.checkIndentityDay(employeeId, listDate);
							//Output「対象日一覧の確認が済んでいる」をチェックする
							if(checkConfirm) {
								//実施可否：実施できる
								approvalStatusResult.setImplementaPropriety(AvailabilityAtr.CAN_RELEASE);
								//取得した「本人確認処理の利用設定．月の本人確認を利用する」をチェックする
								if(identityProcessUseSet.get().isUseIdentityOfMonth()) {
									//ドメインモデル「月の本人確認」を取得する
									Optional<ConfirmationMonth> confirmationMonth =  confirmationMonthRepo.findByKey(companyId, employeeId,EnumAdaptor.valueOf(closureId, ClosureId.class), closureDate, yearmonthInput);
									if(confirmationMonth.isPresent()) {
										approvalStatusResult.setImplementaPropriety(AvailabilityAtr.CAN_RELEASE);
									}else {
										approvalStatusResult.setImplementaPropriety(AvailabilityAtr.CAN_NOT_RELEASE);
									}
								}
								
							}else {
								approvalStatusResult.setImplementaPropriety(AvailabilityAtr.CAN_NOT_RELEASE);
							}
								
						}
					}
				}
				
			}else {
				//実施可否：実施できる
				approvalStatusResult.setImplementaPropriety(AvailabilityAtr.CAN_RELEASE);
			}
			listApprovalStatusResult.add(approvalStatusResult);
		}
		
		return Optional.of(new ApprovalStatusMonth());
	}
}
