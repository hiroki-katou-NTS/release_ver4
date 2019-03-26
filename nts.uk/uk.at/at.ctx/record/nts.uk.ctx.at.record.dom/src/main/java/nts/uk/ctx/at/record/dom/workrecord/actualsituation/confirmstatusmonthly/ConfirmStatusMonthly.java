package nts.uk.ctx.at.record.dom.workrecord.actualsituation.confirmstatusmonthly;

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
import nts.uk.ctx.at.record.dom.adapter.workflow.service.dtos.ApproveRootStatusForEmpImport;
import nts.uk.ctx.at.record.dom.adapter.workflow.service.enums.ApprovalStatusForEmployee;
import nts.uk.ctx.at.record.dom.approvalmanagement.ApprovalProcessingUseSetting;
import nts.uk.ctx.at.record.dom.dailyperformanceprocessing.finddata.IFindDataDCRecord;
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
 * RQ586 : 月の実績の確認状況を取得する
 * @author tutk
 *
 */
@Stateless
public class ConfirmStatusMonthly {
	
	@Inject
	private IdentityProcessUseSetRepository identityProcessUseSetRepo;
	
	@Inject
	private ClosureService closureService;
	
	@Inject
	private SyCompanyRecordAdapter syCompanyRecordAdapter;
	
	@Inject
	private ConfirmationMonthRepository confirmationMonthRepo; 
	
	@Inject
	private CheckIndentityDayConfirm checkIndentityDayConfirm;
	
	@Inject
	private IFindDataDCRecord iFindDataDCRecord;
	
	@Inject
	private ApprovalStatusAdapter approvalStatusAdapter;

	
	public Optional<StatusConfirmMonthDto> getConfirmStatusMonthly(String companyId,Integer closureId,ClosureDate closureDate, List<String> listEmployeeId,YearMonth yearmonthInput) {
		iFindDataDCRecord.clearAllStateless();
		List<ConfirmStatusResult> listConfirmStatus = new ArrayList<>();
		//ドメインモデル「本人確認処理の利用設定」を取得する
		Optional<IdentityProcessUseSet> identityProcessUseSet = identityProcessUseSetRepo.findByKey(companyId);
		if(!identityProcessUseSet.isPresent())
			return Optional.empty();
		//取得した「本人確認処理の利用設定．月の本人確認を利用する」をチェックする
		if(!identityProcessUseSet.get().isUseIdentityOfMonth())
			return Optional.empty();
		//指定した年月の期間を算出する
		DatePeriod datePeriod =closureService.getClosurePeriod(closureId, yearmonthInput);
		//社員の指定期間中の所属期間を取得する RQ588
		List<StatusOfEmployeeExport> listStatusOfEmployeeExport = syCompanyRecordAdapter.getListAffComHistByListSidAndPeriod(listEmployeeId, datePeriod)
				.stream().filter(c->c.getEmployeeId() !=null).collect(Collectors.toList());
		//Output「社員の会社所属状況」をチェックする
		if(listStatusOfEmployeeExport.isEmpty())
			return Optional.empty();
		for(String employeeId :listEmployeeId ) {
			Optional<StatusOfEmployeeExport> statusOfEmployeeExport = listStatusOfEmployeeExport.stream().filter(c -> c.getEmployeeId().equals(employeeId)).findFirst();
			if(!statusOfEmployeeExport.isPresent()) {
				continue;
			}
			List<GeneralDate> listDate = new ArrayList<>();
			for(DatePeriod period : statusOfEmployeeExport.get().getListPeriod()) {
				listDate.addAll(period.datesBetween());
			}
			ConfirmStatusResult confirmStatus = new ConfirmStatusResult();
			//ドメインモデル「月の本人確認」を取得する
			Optional<ConfirmationMonth> confirmationMonth =  confirmationMonthRepo.findByKey(companyId, employeeId,EnumAdaptor.valueOf(closureId, ClosureId.class), closureDate, yearmonthInput);
			confirmStatus.setEmployeeId(employeeId);
			confirmStatus.setYearMonth(yearmonthInput);
			
			if(confirmationMonth.isPresent()) {
				confirmStatus.setConfirmStatus(true);
			}else {
				confirmStatus.setConfirmStatus(false);
			}
			
			//取得した「本人確認処理の利用設定．日の本人確認を利用する」をチェックする
			if(identityProcessUseSet.get().isUseConfirmByYourself()) {
				//対象日の本人確認が済んでいるかチェックする
				boolean checkConfirm = checkIndentityDayConfirm.checkIndentityDay(employeeId, listDate);
				if(checkConfirm) {
					//・実施可否：実施できる
					confirmStatus.setImplementaPropriety(AvailabilityAtr.CAN_RELEASE);
				}else {
					//・実施可否：実施できない
					confirmStatus.setImplementaPropriety(AvailabilityAtr.CAN_NOT_RELEASE);
				}
			}else {
				//実施可否：実施できる
				confirmStatus.setImplementaPropriety(AvailabilityAtr.CAN_RELEASE);
			}
			
			//ドメインモデル「承認処理の利用設定」を取得する
			Optional<ApprovalProcessingUseSetting> optApprovalUse = iFindDataDCRecord.findApprovalByCompanyId(companyId);
			if(!optApprovalUse.isPresent()) {
				//解除可否：解除できる
				confirmStatus.setWhetherToRelease(ReleasedAtr.CAN_RELEASE);
			}else {
				//取得した「承認処理の利用設定．月の承認者確認を利用する」をチェックする
				if(!optApprovalUse.get().getUseMonthApproverConfirm()) {
					confirmStatus.setWhetherToRelease(ReleasedAtr.CAN_RELEASE);
				}else {
					//対応するImported「（就業．勤務実績）承認対象者の承認状況」をすべて取得する : RQ462
					List<ApproveRootStatusForEmpImport> lstApprovalStatus = approvalStatusAdapter
							.getApprovalByListEmplAndListApprovalRecordDateNew(Arrays.asList(datePeriod.end()),
									Arrays.asList(employeeId), 2); // 2 : 月別確認
					if(lstApprovalStatus.isEmpty()) {
						confirmStatus.setWhetherToRelease(ReleasedAtr.CAN_RELEASE);
					}else {
						//取得した「承認対象者の承認状況」をチェックする
						if(lstApprovalStatus.get(0).getApprovalStatus() == ApprovalStatusForEmployee.UNAPPROVED) {
							confirmStatus.setWhetherToRelease(ReleasedAtr.CAN_RELEASE);
						}else {
							confirmStatus.setWhetherToRelease(ReleasedAtr.CAN_NOT_RELEASE);
						}
					}
					
					
				}
			}
			listConfirmStatus.add(confirmStatus);
			
		}
		
		return Optional.of(new StatusConfirmMonthDto(listConfirmStatus));
	}
}
