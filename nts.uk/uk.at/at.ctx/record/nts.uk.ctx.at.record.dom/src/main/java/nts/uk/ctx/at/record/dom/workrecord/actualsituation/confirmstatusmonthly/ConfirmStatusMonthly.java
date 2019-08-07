package nts.uk.ctx.at.record.dom.workrecord.actualsituation.confirmstatusmonthly;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.arc.time.GeneralDate;
import nts.arc.time.YearMonth;
import nts.uk.ctx.at.record.dom.adapter.workflow.service.enums.ApprovalStatusForEmployee;
import nts.uk.ctx.at.record.dom.approvalmanagement.ApprovalProcessingUseSetting;
import nts.uk.ctx.at.record.dom.dailyperformanceprocessing.confirmationstatus.change.confirm.ConfirmInfoAcqProcess;
import nts.uk.ctx.at.record.dom.dailyperformanceprocessing.confirmationstatus.change.confirm.ConfirmInfoResult;
import nts.uk.ctx.at.record.dom.dailyperformanceprocessing.confirmationstatus.change.confirm.InformationMonth;
import nts.uk.ctx.at.record.dom.dailyperformanceprocessing.finddata.IFindDataDCRecord;
import nts.uk.ctx.at.record.dom.workrecord.identificationstatus.IdentityProcessUseSet;
import nts.uk.ctx.at.record.dom.workrecord.identificationstatus.repository.IdentityProcessUseSetRepository;
import nts.uk.ctx.at.shared.dom.workrule.closure.ClosureId;
import nts.uk.shr.com.time.calendar.date.ClosureDate;

/**
 * RQ586 : 月の実績の確認状況を取得する
 * 
 * @author tutk
 *
 */
@Stateless
public class ConfirmStatusMonthly {

	@Inject
	private IdentityProcessUseSetRepository identityProcessUseSetRepo;

	@Inject
	private IFindDataDCRecord iFindDataDCRecord;

	@Inject
	private ConfirmInfoAcqProcess confirmInfoAcqProcess;

	public Optional<StatusConfirmMonthDto> getConfirmStatusMonthly(String companyId, Integer closureId,
			ClosureDate closureDate, List<String> listEmployeeId, YearMonth yearmonthInput,
			List<MonthlyModifyResultDto> results) {
		iFindDataDCRecord.clearAllStateless();
		// ドメインモデル「本人確認処理の利用設定」を取得する
		Optional<IdentityProcessUseSet> identityProcessUseSet = identityProcessUseSetRepo.findByKey(companyId);
		if (!identityProcessUseSet.isPresent())
			return Optional.empty();
		// 取得した「本人確認処理の利用設定．月の本人確認を利用する」をチェックする
		if (!identityProcessUseSet.get().isUseIdentityOfMonth()) {
			return Optional.empty();
		}
		// ドメインモデル「承認処理の利用設定」を取得する
		Optional<ApprovalProcessingUseSetting> optApprovalUse = iFindDataDCRecord.findApprovalByCompanyId(companyId);
		// 確認情報取得処理
		List<ConfirmInfoResult> confirmInfoResults = this.confirmInfoAcqProcess.getConfirmInfoAcp(companyId,
				listEmployeeId, Optional.empty(), Optional.of(yearmonthInput));
		if(confirmInfoResults.isEmpty()){
			return Optional.empty();
		}
		// チェック処理（月の確認）
		Optional<StatusConfirmMonthDto> result = this.checkProcessMonthConfirm(listEmployeeId, confirmInfoResults,
				optApprovalUse, identityProcessUseSet.get());

		return result;
	}

	/**
	 * チェック処理（月の確認）
	 * 
	 * @param listEmployeeId
	 * @param confirmInfoResults
	 * @param optApprovalUse
	 * @param identityProcessUseSet
	 * @return
	 */
	private Optional<StatusConfirmMonthDto> checkProcessMonthConfirm(List<String> listEmployeeId,
			List<ConfirmInfoResult> confirmInfoResults, Optional<ApprovalProcessingUseSetting> optApprovalUse,
			IdentityProcessUseSet identityProcessUseSet) {
		List<ConfirmStatusResult> confirmStatusResults = new ArrayList<>();
		// 取得している「承認処理の利用設定．月の承認者確認を利用する」をチェックする- k can cho vao vong loop
		boolean useMonthApproverConfirm = (optApprovalUse.isPresent()
				&& optApprovalUse.get().getUseMonthApproverConfirm()) ? true : false;
		// Input「社員一覧」でループ
		for (String employeeId : listEmployeeId) {
			Optional<ConfirmInfoResult> optConfirmInfoResult = confirmInfoResults.stream()
					.filter(x -> x.getEmployeeId().equals(employeeId)).findFirst();
			if(!optConfirmInfoResult.isPresent()) continue;
			ConfirmInfoResult confirmInfoResult = optConfirmInfoResult.get();
			for (InformationMonth infoMonth : confirmInfoResult.getInformationMonths()) {
				// 対象締め
				ClosureId closureId = infoMonth.getActualClosure().getClosureId();
				// 対象年月
				YearMonth yearMonth = infoMonth.getActualClosure().getYearMonth();
				// 確認状況
				boolean confirmStatus = !infoMonth.getLstConfirmMonth().isEmpty();
				// 取得した情報からパラメータ「月の実績の確認状況」を生成する
				ConfirmStatusResult confirmStatusResult = new ConfirmStatusResult(employeeId, yearMonth, closureId,
						confirmStatus, AvailabilityAtr.CAN_RELEASE, ReleasedAtr.CAN_RELEASE);

				// Input「社員の実績の確認状況情報．月の情報．締め期間」の件数ループ
				// trong EA ghi la loop theo 締め期間 nhung hien tai chi loop dc theo 月の情報
				// Input「社員の実績の確認状況情報．月の情報．月の承認」をチェックする
				int approvalStatus = infoMonth.getLstApprovalMonthStatus().get(0).getApprovalStatus().value;
				if (useMonthApproverConfirm && (approvalStatus == ApprovalStatusForEmployee.APPROVED.value
						|| approvalStatus == ApprovalStatusForEmployee.DURING_APPROVAL.value)) {
					// パラメータ「月の実績の確認状況」をセットする
					confirmStatusResult.setWhetherToRelease(ReleasedAtr.CAN_NOT_RELEASE);
				}
				// 取得している「本人確認処理の利用設定．日の本人確認を利用する」をチェックする
				if (identityProcessUseSet.isUseConfirmByYourself()) {
					// Input「社員の実績の確認状況情報．日の情報．日の本人確認」をチェックする
					// lay ra list date can confirm
					List<GeneralDate> listDateConfirm = new ArrayList<>();
					confirmInfoResult.getStatusOfEmp().getListPeriod().stream().forEach(period -> {
						listDateConfirm.addAll(period.datesBetween());
					});
					// lay ra listDate da confirm
					List<GeneralDate> listDateConfirmed = confirmInfoResult.getInformationDay().getIndentities()
							.stream().map(x -> x.getProcessingYmd()).collect(Collectors.toList());
					// 締め期間分1件でも存在しない
					if (!listDateConfirmed.containsAll(listDateConfirm)) {
						confirmStatusResult.setImplementaPropriety(AvailabilityAtr.CAN_NOT_RELEASE);
					}
				}

				// Input「ロック状態」をチェックする - Theo nhu Thanhnx thi da lam o xu ly ngoai
				// パラメータ「月の実績の確認状況」をセットする
				confirmStatusResults.add(confirmStatusResult);
			}
		}

		return Optional.of(new StatusConfirmMonthDto(confirmStatusResults));
	}
}
