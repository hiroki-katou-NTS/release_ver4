package nts.uk.ctx.at.record.dom.workrecord.actualsituation.approvalstatusmonthly;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

import lombok.val;
import nts.arc.enums.EnumAdaptor;
import nts.arc.time.GeneralDate;
import nts.arc.time.YearMonth;
import nts.uk.ctx.at.record.dom.adapter.company.StatusOfEmployeeExport;
import nts.uk.ctx.at.record.dom.adapter.company.SyCompanyRecordAdapter;
import nts.uk.ctx.at.record.dom.adapter.workflow.service.ApprovalStatusAdapter;
import nts.uk.ctx.at.record.dom.adapter.workflow.service.dtos.AppRootOfEmpMonthImport;
import nts.uk.ctx.at.record.dom.adapter.workflow.service.dtos.AppRootSituationMonth;
import nts.uk.ctx.at.record.dom.adapter.workflow.service.dtos.ApproveRootStatusForEmpImport;
import nts.uk.ctx.at.record.dom.adapter.workflow.service.enums.ApprovalActionByEmpl;
import nts.uk.ctx.at.record.dom.adapter.workflow.service.enums.ApprovalStatusForEmployee;
import nts.uk.ctx.at.record.dom.adapter.workflow.service.enums.ApproverEmployeeState;
import nts.uk.ctx.at.record.dom.adapter.workflow.service.enums.ReleasedProprietyDivision;
import nts.uk.ctx.at.record.dom.approvalmanagement.ApprovalProcessingUseSetting;
import nts.uk.ctx.at.record.dom.dailyperformanceprocessing.confirmationstatus.change.approval.ApprovalStatusInfoEmp;
import nts.uk.ctx.at.record.dom.dailyperformanceprocessing.confirmationstatus.change.confirm.ConfirmInfoResult;
import nts.uk.ctx.at.record.dom.dailyperformanceprocessing.confirmationstatus.change.confirm.InformationMonth;
import nts.uk.ctx.at.record.dom.dailyperformanceprocessing.finddata.IFindDataDCRecord;
import nts.uk.ctx.at.record.dom.workrecord.actualsituation.confirmstatusmonthly.AvailabilityAtr;
import nts.uk.ctx.at.record.dom.workrecord.actualsituation.confirmstatusmonthly.MonthlyModifyResultDto;
import nts.uk.ctx.at.record.dom.workrecord.actualsituation.confirmstatusmonthly.ReleasedAtr;
import nts.uk.ctx.at.record.dom.workrecord.actualsituation.identificationstatus.export.CheckIndentityDayConfirm;
import nts.uk.ctx.at.record.dom.workrecord.identificationstatus.IdentityProcessUseSet;
import nts.uk.ctx.at.record.dom.workrecord.identificationstatus.month.ConfirmationMonth;
import nts.uk.ctx.at.record.dom.workrecord.identificationstatus.repository.ConfirmationMonthRepository;
import nts.uk.ctx.at.record.dom.workrecord.identificationstatus.repository.IdentityProcessUseSetRepository;
import nts.uk.ctx.at.shared.dom.workrule.closure.ClosureId;
import nts.uk.ctx.at.shared.dom.workrule.closure.service.ClosureService;
import nts.uk.shr.com.context.AppContexts;
import nts.uk.shr.com.time.calendar.date.ClosureDate;
import nts.uk.shr.com.time.calendar.period.DatePeriod;

/**
 * 月の実績の承認状況を取得する : RQ587
 * 
 * @author tutk
 *
 */
@Stateless
public class ApprovalStatusMonthly {
	@Inject
	private IFindDataDCRecord iFindDataDCRecord;

	@Inject
	private IdentityProcessUseSetRepository identityProcessUseSetRepo;

	@Inject
	private CheckIndentityDayConfirm checkIndentityDayConfirm;

	@Inject
	private ClosureService closureService;

	@Inject
	private SyCompanyRecordAdapter syCompanyRecordAdapter;

	@Inject
	private ApprovalStatusAdapter approvalStatusAdapter;

	@Inject
	private ConfirmationMonthRepository confirmationMonthRepo;

	@Inject
	private ApprovalStatusInfoEmp approvalStatusInfoEmp;

	public Optional<ApprovalStatusMonth> getApprovalStatusMonthly(String companyId, String approverId,
			Integer closureId, List<String> listEmployeeId, YearMonth yearmonthInput,
			List<MonthlyModifyResultDto> results) {
		iFindDataDCRecord.clearAllStateless();
		// ドメインモデル「承認処理の利用設定」を取得する
		Optional<ApprovalProcessingUseSetting> optApprovalUse = iFindDataDCRecord.findApprovalByCompanyId(companyId);
		if (!optApprovalUse.isPresent()) {
			return Optional.empty();
		}
		if (!optApprovalUse.get().getUseMonthApproverConfirm()) {
			return Optional.empty();
		}
		// ドメインモデル「本人確認処理の利用設定」を取得する
		Optional<IdentityProcessUseSet> identityProcessUseSet = identityProcessUseSetRepo.findByKey(companyId);

		// 承認情報取得処理
		String employeeLogin = AppContexts.user().employeeId();
		List<ConfirmInfoResult> listApprovalInfoResult = this.approvalInfoAcquisitionProcess(companyId, employeeLogin,
				listEmployeeId, true, Optional.empty(), Optional.of(yearmonthInput));
		if (listApprovalInfoResult.isEmpty()) {
			return Optional.empty();
		}
		// チェック処理（月の承認）
		List<ApprovalStatusResult> result = this.checkProcessMonthApprove(listEmployeeId, listApprovalInfoResult,
				identityProcessUseSet.get(), optApprovalUse.get(), closureId);
		return Optional.of(new ApprovalStatusMonth(result));
	}

	/**
	 * 承認情報取得処理
	 * 
	 * @param companyId
	 * @param empIdLogin
	 * @param lstEmpId
	 * @param isCallBy587
	 *            = true (日次月次区分：日次 or 月次)
	 * @param targetYm
	 * @param targetDatePeriod
	 */
	private List<ConfirmInfoResult> approvalInfoAcquisitionProcess(String companyId, String empIdLogin,
			List<String> lstEmpId, boolean isCallBy587, Optional<DatePeriod> optTargetDatePeriod,
			Optional<YearMonth> optTargetYm) {
		// Input「対象年月」「対象期間」をチェックする
		// Input「対象期間」をチェックする
		if (optTargetDatePeriod.isPresent()) {
			// Input「対象社員」をチェックする
			if (lstEmpId.size() == 1) {
				// 社員1件の承認状況情報を取得する
				return approvalStatusInfoEmp.approvalStatusInfoOneEmp(companyId, empIdLogin, lstEmpId.get(0),
						optTargetDatePeriod, optTargetYm, isCallBy587);
			}
			if (lstEmpId.size() > 1) {
				// 複数社員の承認状況情報を取得する
				return approvalStatusInfoEmp.approvalStatusInfoMulEmp(companyId, empIdLogin, lstEmpId,
						optTargetDatePeriod, optTargetYm, isCallBy587);
			}
		}
		// Input「対象年月」をチェックする
		if (optTargetYm.isPresent()) {
			// Input「対象社員」をチェックする
			if (lstEmpId.size() == 1) {
				// 社員1件の承認状況情報を取得する
				return approvalStatusInfoEmp.approvalStatusInfoOneEmp(companyId, empIdLogin, lstEmpId.get(0),
						optTargetDatePeriod, optTargetYm, isCallBy587);
			}
			if (lstEmpId.size() > 1) {
				// 複数社員の承認状況情報を取得する
				return approvalStatusInfoEmp.approvalStatusInfoMulEmp(companyId, empIdLogin, lstEmpId,
						optTargetDatePeriod, optTargetYm, isCallBy587);
			}
		}

		return Collections.emptyList();

	}

	/**
	 * チェック処理（月の承認）
	 */
	private List<ApprovalStatusResult> checkProcessMonthApprove(List<String> listEmployeeId,
			List<ConfirmInfoResult> listApprovalInfoResult, IdentityProcessUseSet identityProcessUseSet,
			ApprovalProcessingUseSetting approvalUse, Integer closId) {
		List<ApprovalStatusResult> approvalStatusResults = new ArrayList<>();
		// Input「対象締め」に一致しないInput「社員の実績の承認状況情報．月の情報」を削除する
		listApprovalInfoResult.forEach(x -> {
			x.getInformationMonths().removeIf(y -> y.getActualClosure().getClosureId().value != closId.intValue());
		});
		// 取得している「承認処理の利用設定．日の承認を利用する」をチェックする- k can cho vao vong loop
		boolean useDayApproverConfirm = approvalUse.getUseDayApproverConfirm() ? true : false;
		for (String employeeId : listEmployeeId) {
			Optional<ConfirmInfoResult> optApprovalInfoResult = listApprovalInfoResult.stream()
					.filter(x -> x.getEmployeeId().equals(employeeId)).findFirst();
			if (!optApprovalInfoResult.isPresent())
				continue;
			ConfirmInfoResult approvalInfoResult = optApprovalInfoResult.get();
			for (InformationMonth infoMonth : approvalInfoResult.getInformationMonths()) {
				// 対象締め
				ClosureId closureId = infoMonth.getActualClosure().getClosureId();
				// 対象年月
				YearMonth yearMonth = infoMonth.getActualClosure().getYearMonth();
				// 承認状況
				Optional<ApproveRootStatusForEmpImport> optApproveRootStatusForEmpImport = infoMonth
						.getLstApprovalMonthStatus().stream().filter(x -> x.getEmployeeID().equals(employeeId))
						.findFirst();
				if (!optApproveRootStatusForEmpImport.isPresent()) {
					continue;
				}
				ApprovalStatusForEmployee normalStatus = optApproveRootStatusForEmpImport.get().getApprovalStatus();

				List<AppRootSituationMonth> approvalRootSituations = new ArrayList<>();
				infoMonth.getLstAppRootOfEmpMonth().stream()
						.forEach(x -> approvalRootSituations.addAll(x.getApprovalRootSituations()));
				Optional<AppRootSituationMonth> optAppRootSituationMonth = approvalRootSituations.stream()
						.filter(x -> x.getTargetID().equals(employeeId) && x.getYearMonth().v() == yearMonth.v())
						.findFirst();
				// 承認状態
				boolean approvalStatus = false;
				// 実施可否
				AvailabilityAtr implementaPropriety = AvailabilityAtr.CAN_NOT_RELEASE;
				// 解除可否
				ReleasedAtr whetherToRelease = ReleasedAtr.CAN_NOT_RELEASE;
				if (optAppRootSituationMonth.isPresent()) {
					AppRootSituationMonth appRootSituationMonth = optAppRootSituationMonth.get();
					// 承認状態
					approvalStatus = (appRootSituationMonth.getApprovalStatus()
							.getApprovalActionByEmpl().value == ApprovalActionByEmpl.APPROVALED.value);
					// 実施可否
					List<Integer> listValueTrue = Arrays.asList(ApproverEmployeeState.COMPLETE.value,
							ApproverEmployeeState.PHASE_LESS.value, ApproverEmployeeState.PHASE_PASS.value);
					implementaPropriety = listValueTrue.contains(appRootSituationMonth.getApprovalAtr().value)
							? AvailabilityAtr.CAN_NOT_RELEASE : AvailabilityAtr.CAN_RELEASE;
					// 解除可否
					whetherToRelease = infoMonth.getLstAppRootOfEmpMonth().isEmpty() ? ReleasedAtr.CAN_NOT_RELEASE
							: EnumAdaptor.valueOf(appRootSituationMonth.getApprovalStatus().getReleaseDivision().value,
									ReleasedAtr.class);
				}

				// 取得した情報からパラメータ「月の実績の承認状況」を生成する
				ApprovalStatusResult approvalStatusResult = new ApprovalStatusResult(employeeId, yearMonth, closureId,
						approvalStatus, normalStatus, implementaPropriety, whetherToRelease);
				// 取得している「本人確認処理の利用設定．月の本人確認を利用する」をチェックする
				// Input「社員の実績の承認状況情報．月の情報．月の本人確認」をチェックする
				if (identityProcessUseSet.isUseIdentityOfMonth() && infoMonth.getLstConfirmMonth().isEmpty()) {
					// パラメータ「月の実績の承認状況」をセットする
					approvalStatusResult.setImplementaPropriety(AvailabilityAtr.CAN_NOT_RELEASE);
				}

				if (useDayApproverConfirm) {
					// Input「社員の実績の承認状況情報．日の情報．日の承認．承認状況」をチェックする
					// lay ra list date can approve
					List<GeneralDate> listDateApprove = new ArrayList<>();
					approvalInfoResult.getStatusOfEmp().getListPeriod().stream().forEach(period -> {
						listDateApprove.addAll(period.datesBetween());
					});
					List<ApproveRootStatusForEmpImport> lstApprovalDayStatus = approvalInfoResult.getInformationDay()
							.getLstApprovalDayStatus();
					// 1件でも「承認中」「未承認」「NULL」の場合
					if (lstApprovalDayStatus.isEmpty()) {
						// パラメータ「月の実績の承認状況」をセットする
						// 実施可否：実施できない
						approvalStatusResult.setImplementaPropriety(AvailabilityAtr.CAN_NOT_RELEASE);
					}
					for (ApproveRootStatusForEmpImport approvalDayStatus : lstApprovalDayStatus) {
						if (listDateApprove.contains(approvalDayStatus.getAppDate()) && (approvalDayStatus
								.getApprovalStatus().value == ApprovalStatusForEmployee.UNAPPROVED.value
								|| approvalDayStatus
										.getApprovalStatus().value == ApprovalStatusForEmployee.DURING_APPROVAL.value)) {
							// パラメータ「月の実績の承認状況」をセットする
							// 実施可否：実施できない
							approvalStatusResult.setImplementaPropriety(AvailabilityAtr.CAN_NOT_RELEASE);
							break;
						}
					}
				}

				// 取得している「本人確認処理の利用設定．日の本人確認を利用する」をチェックする
				if (identityProcessUseSet.isUseConfirmByYourself()) {
					// Input「社員の実績の確認状況情報．日の情報．日の本人確認」をチェックする
					// lay ra list date can confirm
					List<GeneralDate> listDateConfirm = new ArrayList<>();
					approvalInfoResult.getStatusOfEmp().getListPeriod().stream().forEach(period -> {
						listDateConfirm.addAll(period.datesBetween());
					});
					// lay ra listDate da confirm
					List<GeneralDate> listDateConfirmed = approvalInfoResult.getInformationDay().getIndentities()
							.stream().map(x -> x.getProcessingYmd()).collect(Collectors.toList());
					// 締め期間分1件でも存在しない
					if (!listDateConfirmed.containsAll(listDateConfirm)) {
						approvalStatusResult.setImplementaPropriety(AvailabilityAtr.CAN_NOT_RELEASE);
					}
				}
				// Input「ロック状態」をチェックする - Theo nhu Thanhnx thi da lam o xu ly
				// ngoai
				// パラメータ「月の実績の確認状況」をセットする
				approvalStatusResults.add(approvalStatusResult);
			}
		}
		return approvalStatusResults;
	}

	public Optional<ApprovalStatusResult> getApprovalStatus(String companyId, String approverId, String employeeId,
			YearMonth yearMonth, int closureId, ClosureDate closureDate, DatePeriod workPeriod) {
		iFindDataDCRecord.clearAllStateless();
		// ドメインモデル「承認処理の利用設定」を取得する
		Optional<ApprovalProcessingUseSetting> optApprovalUse = iFindDataDCRecord.findApprovalByCompanyId(companyId);
		if (!optApprovalUse.isPresent()) {
			return Optional.empty();
		}
		if (!optApprovalUse.get().getUseMonthApproverConfirm()) {
			return Optional.empty();
		}

		// 指定した年月の期間を算出する
		DatePeriod datePeriod = closureService.getClosurePeriod(closureId, yearMonth);
		// 社員の指定期間中の所属期間を取得する RQ588
		List<StatusOfEmployeeExport> listStatusOfEmployeeExport = syCompanyRecordAdapter
				.getListAffComHistByListSidAndPeriod(Arrays.asList(employeeId), workPeriod);
		if (listStatusOfEmployeeExport.isEmpty())
			return Optional.empty();

		Optional<StatusOfEmployeeExport> statusOfEmployeeExport = listStatusOfEmployeeExport.stream()
				.filter(c -> c.getEmployeeId().equals(employeeId)).findFirst();
		if (!statusOfEmployeeExport.isPresent()) {
			return Optional.empty();
		}

		List<GeneralDate> listDate = new ArrayList<>();
		for (DatePeriod period : statusOfEmployeeExport.get().getListPeriod()) {
			listDate.addAll(period.datesBetween());
		}

		// 対応するImported「（就業．勤務実績）承認対象者の承認状況」をすべて取得する : RQ462
		List<ApproveRootStatusForEmpImport> lstApprovalStatus = approvalStatusAdapter
				.getApprovalByListEmplAndListApprovalRecordDateNew(Arrays.asList(datePeriod.end()),
						Arrays.asList(employeeId), 2); // 2 : 月別確認
		if (lstApprovalStatus.isEmpty())
			return Optional.empty();

		// 対応するImported「基準社員の承認対象者」を取得する RQ463
		// ApprovalRootOfEmployeeImport approvalRootOfEmployeeImport =
		// approvalStatusAdapter.getApprovalRootOfEmloyeeNew(datePeriod.end(),
		// datePeriod.end(), approverId, companyId, 2); // 2 : 月別確認
		// Change 463(call 133) by 534 for Tú bro - if have bug, Tú will fix,
		// don't call Phong
		AppRootOfEmpMonthImport approvalRootOfEmloyee = this.approvalStatusAdapter.getApprovalEmpStatusMonth(
				AppContexts.user().employeeId(), yearMonth, closureId, closureDate, datePeriod.end(),
				optApprovalUse.get().getUseDayApproverConfirm(), datePeriod);

		// 取得した情報からパラメータ「月の実績の承認状況」を生成する
		ApprovalStatusResult approvalStatusResult = new ApprovalStatusResult();
		approvalStatusResult.setEmployeeId(employeeId);
		approvalStatusResult.setYearMonth(yearMonth);
		Optional<AppRootSituationMonth> approvalRootSituation = approvalRootOfEmloyee.getApprovalRootSituations()
				.stream().filter(c -> c.getTargetID().equals(employeeId)).findFirst();
		if (!approvalRootSituation.isPresent()) {
			// ・解除可否：取得した「基準社員の承認対象者．解除可否区分」
			approvalStatusResult.setWhetherToRelease(
					EnumAdaptor.valueOf(ReleasedProprietyDivision.NOT_RELEASE.value, ReleasedAtr.class));
			// 承認状態 : false
			approvalStatusResult.setApprovalStatus(false);
		} else {
			// ・承認状態：取得した「基準社員の承認対象者．基準社員の承認アクション
			if (approvalRootSituation.get().getApprovalStatus()
					.getApprovalActionByEmpl() == ApprovalActionByEmpl.APPROVALED) {
				approvalStatusResult.setApprovalStatus(true);
			} else {
				approvalStatusResult.setApprovalStatus(false);
			}

			// ・解除可否：取得した「基準社員の承認対象者．解除可否区分」
			approvalStatusResult.setWhetherToRelease(EnumAdaptor.valueOf(
					approvalRootSituation.get().getApprovalStatus().getReleaseDivision().value, ReleasedAtr.class));
			// Output「基準社員の承認対象者．ルート状況．基準社員のルート状況」をチェックする
			if (approvalRootSituation.get().getApprovalAtr() != ApproverEmployeeState.PHASE_DURING) {
				approvalStatusResult.setImplementaPropriety(AvailabilityAtr.CAN_NOT_RELEASE);
				return Optional.of(approvalStatusResult);
			}
		}

		// ・承認状況：取得した「承認対象者の承認状況．承認状況」
		approvalStatusResult.setNormalStatus(lstApprovalStatus.get(0).getApprovalStatus());
		// ドメインモデル「本人確認処理の利用設定」を取得する
		Optional<IdentityProcessUseSet> identityProcessUseSet = identityProcessUseSetRepo.findByKey(companyId);
		// 取得した「承認処理の利用設定．日の承認者確認を利用する」をチェックする
		if (optApprovalUse.get().getUseDayApproverConfirm()) {
			// 対応するImported「（就業．勤務実績）承認対象者の承認状況」をすべて取得する
			// fixbug 107181: lay dateperiod theo closure moi (thiet ke k viet,
			// sua theo ý anh Tuấn bảo)
			List<ApproveRootStatusForEmpImport> lstApprovalStatusDay = approvalStatusAdapter
					.getApprovalByListEmplAndListApprovalRecordDateNew(workPeriod.datesBetween(),
							Arrays.asList(employeeId), 1); // 1 : 日別確認
			val checkDataNotApprovalDay = lstApprovalStatusDay.stream()
					.filter(x -> x.getApprovalStatus() != ApprovalStatusForEmployee.APPROVED)
					.collect(Collectors.toList());

			if (lstApprovalStatusDay.isEmpty() || !checkDataNotApprovalDay.isEmpty()) {
				approvalStatusResult.setImplementaPropriety(AvailabilityAtr.CAN_NOT_RELEASE);
				return Optional.of(approvalStatusResult);
			} else {
				approvalStatusResult.setImplementaPropriety(AvailabilityAtr.CAN_RELEASE);
			}
		} else {
			// 実施可否：実施できる
			approvalStatusResult.setImplementaPropriety(AvailabilityAtr.CAN_RELEASE);
		}

		// 取得した「本人確認処理の利用設定．日の本人確認を利用する」をチェックする
		if (!identityProcessUseSet.isPresent() || !identityProcessUseSet.get().isUseConfirmByYourself()) {
			approvalStatusResult.setImplementaPropriety(AvailabilityAtr.CAN_RELEASE);
			approvalStatusResult.setWhetherToRelease(ReleasedAtr.CAN_RELEASE);
		} else {
			// 対象日の本人確認が済んでいるかチェックする
			boolean checkConfirm = checkIndentityDayConfirm.checkIndentityDay(employeeId, listDate);
			// Output「対象日一覧の確認が済んでいる」をチェックする
			if (checkConfirm) {
				// 実施可否：実施できる
				approvalStatusResult.setImplementaPropriety(AvailabilityAtr.CAN_RELEASE);
			} else {
				approvalStatusResult.setImplementaPropriety(AvailabilityAtr.CAN_NOT_RELEASE);
			}
		}

		// 取得した「本人確認処理の利用設定．月の本人確認を利用する」をチェックする
		if (identityProcessUseSet.get().isUseIdentityOfMonth()) {
			// ドメインモデル「月の本人確認」を取得する
			Optional<ConfirmationMonth> confirmationMonth = confirmationMonthRepo.findByKey(companyId, employeeId,
					EnumAdaptor.valueOf(closureId, ClosureId.class), closureDate, yearMonth);
			if (confirmationMonth.isPresent()) {
				approvalStatusResult.setImplementaPropriety(AvailabilityAtr.CAN_RELEASE);
			} else {
				approvalStatusResult.setImplementaPropriety(AvailabilityAtr.CAN_NOT_RELEASE);
			}
		}

		return Optional.of(approvalStatusResult);
	}
}
