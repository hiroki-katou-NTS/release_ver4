package nts.uk.ctx.at.record.dom.dailyperformanceprocessing.confirmationstatus.change.confirm;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

import org.apache.commons.lang3.tuple.Pair;

import lombok.val;
import nts.arc.time.GeneralDate;
import nts.arc.time.YearMonth;
import nts.uk.ctx.at.record.dom.adapter.application.ApplicationRecordImport;
import nts.uk.ctx.at.record.dom.adapter.company.StatusOfEmployeeExport;
import nts.uk.ctx.at.record.dom.adapter.workflow.service.dtos.ApproveRootStatusForEmpImport;
import nts.uk.ctx.at.record.dom.adapter.workflow.service.enums.ApprovalStatusForEmployee;
import nts.uk.ctx.at.record.dom.approvalmanagement.ApprovalProcessingUseSetting;
import nts.uk.ctx.at.record.dom.dailyperformanceprocessing.appreflect.ReflectedStateRecord;
import nts.uk.ctx.at.record.dom.dailyperformanceprocessing.confirmationstatus.ConfirmStatusActualResult;
import nts.uk.ctx.at.record.dom.dailyperformanceprocessing.confirmationstatus.ReleasedAtr;
import nts.uk.ctx.at.record.dom.dailyperformanceprocessing.confirmationstatus.change.CommonProcess;
import nts.uk.ctx.at.record.dom.dailyperformanceprocessing.finddata.IFindDataDCRecord;
import nts.uk.ctx.at.record.dom.monthlycommon.aggrperiod.AggrPeriodEachActualClosure;
import nts.uk.ctx.at.record.dom.workrecord.identificationstatus.Identification;
import nts.uk.ctx.at.record.dom.workrecord.identificationstatus.IdentityProcessUseSet;
import nts.uk.ctx.at.record.dom.workrecord.identificationstatus.enums.SelfConfirmError;
import nts.uk.ctx.at.record.dom.workrecord.identificationstatus.month.ConfirmationMonth;
import nts.uk.shr.com.time.calendar.period.DatePeriod;

/**
 * @author thanhnx
 * 
 */
@Stateless
public class ConfirmStatusActualDayChange {

	@Inject
	private IFindDataDCRecord iFindDataDCRecord;

	@Inject
	private ConfirmInfoAcqProcess confirmInfoAcqProcess;

	// [No.584]日の実績の確認状況を取得する（NEW）
	public List<ConfirmStatusActualResult> processConfirmStatus(String companyId, String empTarget,
			List<String> employeeIds, Optional<DatePeriod> periodOpt, Optional<YearMonth> yearMonthOpt) {
		// ドメインモデル「本人確認処理の利用設定」を取得する
		Optional<IdentityProcessUseSet> optIndentity = iFindDataDCRecord.findIdentityByKey(companyId);
		if (!optIndentity.isPresent() || !optIndentity.get().isUseConfirmByYourself())
			return Collections.emptyList();

		// ドメインモデル「承認処理の利用設定」を取得する
		Optional<ApprovalProcessingUseSetting> approvalUseSettingOpt = iFindDataDCRecord
				.findApprovalByCompanyId(companyId);

		// 確認情報取得処理
		List<ConfirmInfoResult> confirmInfoResults = confirmInfoAcqProcess.getConfirmInfoAcp(companyId, employeeIds, periodOpt,
				yearMonthOpt);
		if (confirmInfoResults.isEmpty())
			return Collections.emptyList();
		return checkProcess(companyId, empTarget, employeeIds, confirmInfoResults, optIndentity, approvalUseSettingOpt);
	}

	private List<ConfirmStatusActualResult> checkProcess(String companyId, String empTarget, List<String> employeeIds,
			List<ConfirmInfoResult> confirmInfoResults, Optional<IdentityProcessUseSet> optIndentity,
			Optional<ApprovalProcessingUseSetting> approvalUseSettingOpt) {
		List<ConfirmStatusActualResult> lstResult = new ArrayList<>();
		confirmInfoResults.stream().forEach(data ->{
//			confirmInfoResultAll.getStatusOfEmps().stream().forEach(statusOfEmp -> {
			    StatusOfEmployeeExport statusOfEmp = data.getStatusOfEmp();
				ConfirmInfoResult confirmInfoResult = data;
				List<ConfirmStatusActualResult> lstResultEmpTemp = new ArrayList<>(), lstResultEmpTemp1 = new ArrayList<>(),
						lstResultEmpTemp2 = new ArrayList<>();
				List<DatePeriod> lstPeriod = statusOfEmp.getListPeriod();
				String employeeId = statusOfEmp.getEmployeeId();
				List<GeneralDate> dateTemps = new ArrayList<>();
				lstPeriod.stream().forEach(periodTemp -> {
					// ドメインモデル「日の本人確認」を取得する
					List<Identification> indentities = confirmInfoResult.getInformationDay().getIndentities().stream()
							.filter(x -> CommonProcess.inRange(x.getProcessingYmd(), periodTemp))
							.collect(Collectors.toList());
					// set checkbox
					List<ConfirmStatusActualResult> lstResultChild = indentities.stream().map(x -> {
						return new ConfirmStatusActualResult(x.getEmployeeId(), x.getProcessingYmd(), true);
					}).collect(Collectors.toList());

					lstResultEmpTemp.addAll(lstResultChild);
					dateTemps.addAll(lstResultChild.stream().map(y -> y.getDate()).collect(Collectors.toList()));

					// indentities.
					periodTemp.datesBetween().stream().filter(dateT -> !dateTemps.contains(dateT)).forEach(z -> {
						lstResultEmpTemp.add(new ConfirmStatusActualResult(employeeId, z, false));
					});
				});

				lstResultEmpTemp2 = lstResultEmpTemp;
				List<InformationMonth> informationMonths = data.getInformationMonths();
				
				for (InformationMonth infoMonth : informationMonths) {
					AggrPeriodEachActualClosure mergePeriodClr = infoMonth.getActualClosure();
					if (!approvalUseSettingOpt.isPresent() || !approvalUseSettingOpt.get().getUseMonthApproverConfirm()) {
						lstResultEmpTemp2 = lstResultEmpTemp2.stream().map(x -> {
							x.setPermission(true, true);
							return x;
						}).collect(Collectors.toList());
						// TODO: 取得した「本人確認処理の利用設定．月の本人確認を利用する」をチェックする
						lstResultEmpTemp1.addAll(updatePermission(companyId, employeeId, optIndentity.get(), mergePeriodClr,
								lstResultEmpTemp2, infoMonth.getLstConfirmMonth()));
					} else {

						// Map<Pair<String, GeneralDate>, ApprovalStatusForEmployee> mapApprovalStatus =
						// new HashMap<>();
						DatePeriod mergePeriod = mergePeriodClr.getPeriod();
						List<ApproveRootStatusForEmpImport> lstApprovalStatus = infoMonth.getLstApprovalMonthStatus();
						val approvalStatusMonth = lstApprovalStatus.isEmpty() ? null : lstApprovalStatus.get(0);
						val lstEmpDateUnApproval = lstResultEmpTemp2.stream().filter(x -> {
							if (approvalStatusMonth == null)
								return true;
							val value = ((x.getDate().afterOrEquals(mergePeriod.start())
									&& x.getDate().beforeOrEquals(mergePeriod.end())))
											? approvalStatusMonth.getApprovalStatus()
											: null;
							if (value != null && value == ApprovalStatusForEmployee.UNAPPROVED)
								return true;
							else
								return false;
						}).map(x -> {
							x.setPermission(true, true);
							return x;
						}).collect(Collectors.toList());

						lstResultEmpTemp1.addAll(updatePermission(companyId, employeeId, optIndentity.get(), mergePeriodClr,
								lstEmpDateUnApproval, infoMonth.getLstConfirmMonth()));

						// list emp date Approval
						val lstEmpDateApproval = lstResultEmpTemp2.stream().filter(x -> {
							val value = (approvalStatusMonth != null && (x.getDate().afterOrEquals(mergePeriod.start())
									&& x.getDate().beforeOrEquals(mergePeriod.end())))
											? approvalStatusMonth.getApprovalStatus()
											: null;
							if (value != null && value != ApprovalStatusForEmployee.UNAPPROVED)
								return true;
							else
								return false;
						}).map(x -> {
							x.setPermission(true, false);
							return x;
						}).collect(Collectors.toList());

						lstResultEmpTemp1.addAll(lstEmpDateApproval);
					}
				}

			List<ApproveRootStatusForEmpImport> lstApprovalStatus = data.getInformationDay().getLstApprovalDayStatus().stream()
					.collect(Collectors.toList());
			// 取得した「承認処理の利用設定．日の承認者確認を利用する」をチェックする true
				if (approvalUseSettingOpt.isPresent() && approvalUseSettingOpt.get().getUseDayApproverConfirm()) {
					lstPeriod.stream().forEach(periodTemp -> {

						// List<ApproveRootStatusForEmpImport> lstApprovalStatus = approvalStatusAdapter
						// .getApprovalByListEmplAndListApprovalRecordDateNew(periodTemp.datesBetween(),
						// Arrays.asList(employeeId), 1);
						//List<ApproveRootStatusForEmpImport> lstApprovalStatus = confirmInfoResult.getLstApprovalDayStatus();
						// .getApprovalByListEmplAndListApprovalRecordDateNew(periodTemp.datesBetween(),
						// Arrays.asList(employeeId), 1);
						val mapApprovalStatus = lstApprovalStatus.stream().collect(Collectors
								.toMap(x -> Pair.of(x.getEmployeeID(), x.getAppDate()), x -> x.getApprovalStatus(), (x, y) -> x));
						// lstResultEmpTemp3 =
						lstResultEmpTemp1.forEach(x -> {
							if (mapApprovalStatus == null) {
								x.setPermission(true, true);
							} else {
								val temp = mapApprovalStatus.get(Pair.of(x.getEmployeeId(), x.getDate()));
								if (temp != null) {
									if (x.getPermissionRelease() == ReleasedAtr.CAN_IMPLEMENT
											&& temp == ApprovalStatusForEmployee.UNAPPROVED) {
										x.setPermission(true, true);
									} else {
										x.setPermission(true, false);
									}
								}
							}
						});

					});
				}

				lstResult.addAll(lstResultEmpTemp1);
//			});
		});

		List<ConfirmStatusActualResult> results = new ArrayList<>();
		Set<Pair<String, GeneralDate>> setErrorAndApp = getErrorAndApplication(confirmInfoResults, optIndentity);
		results = lstResult.stream().map(x -> {
			if (setErrorAndApp.contains(Pair.of(x.getEmployeeId(), x.getDate()))) {
				x.setPermissionChecked(false);
			}
			return x;
		}).collect(Collectors.toList());
		return results;
	}

	public List<ConfirmStatusActualResult> updatePermission(String companyId, String employeeId,
			IdentityProcessUseSet identityProcessUseSet, AggrPeriodEachActualClosure mergePeriodClr,
			List<ConfirmStatusActualResult> lstResult, List<ConfirmationMonth> lstConfirmMonth) {
		lstResult = lstResult.stream().filter(x -> x.getDate().afterOrEquals(mergePeriodClr.getPeriod().start())
				&& x.getDate().beforeOrEquals(mergePeriodClr.getPeriod().end())).collect(Collectors.toList());
		if (!identityProcessUseSet.isUseIdentityOfMonth()) {
			return lstResult.stream().map(x -> {
				x.setPermission(true, true);
				return x;
			}).collect(Collectors.toList());
		} else {
			Optional<ConfirmationMonth> optConfirmMonth = lstConfirmMonth.stream()
					.filter(x -> x.getClosureId().value == mergePeriodClr.getClosureId().value
							&& x.getProcessYM().equals(mergePeriodClr.getYearMonth())
							&& x.getClosureDate().getLastDayOfMonth().booleanValue() == mergePeriodClr.getClosureDate()
									.getLastDayOfMonth().booleanValue()
							&& x.getClosureDate().getClosureDay().v() == mergePeriodClr.getClosureDate().getClosureDay()
									.v())
					.findFirst();
			return lstResult.stream().map(x -> {
				x.setPermission(true, !optConfirmMonth.isPresent());
				return x;
			}).collect(Collectors.toList());
		}
	}

	public Set<Pair<String, GeneralDate>> getErrorAndApplication(List<ConfirmInfoResult> confirmInfoResults,  Optional<IdentityProcessUseSet> optIndentity) {
		Set<Pair<String, GeneralDate>> result = new HashSet<>();
		boolean checkError = optIndentity.isPresent() && optIndentity.get().getYourSelfConfirmError().isPresent() && optIndentity.get()
				.getYourSelfConfirmError().get().value == SelfConfirmError.CAN_NOT_CHECK_WHEN_ERROR.value;
				
		confirmInfoResults.stream().forEach(confirmInfoResult -> {
			if (checkError) {
				result.addAll(confirmInfoResult.getLstOut().stream().filter(x -> x.getHasError())
						.map(x -> Pair.of(x.getEmployeeId(), x.getDate())).collect(Collectors.toSet()));
			}
			List<ApplicationRecordImport> lstAppRecord = confirmInfoResult.getLstApplication();
			lstAppRecord.forEach(x -> {
				boolean disable = (x.getReflectState() == ReflectedStateRecord.NOTREFLECTED.value
						|| x.getReflectState() == ReflectedStateRecord.REMAND.value)
						&& x.getAppType() != nts.uk.ctx.at.shared.dom.remainingnumber.algorithm.ApplicationType.OVER_TIME_APPLICATION.value;
				if (disable)
					result.add(Pair.of(x.getEmployeeID(), x.getAppDate()));
			});
		});
		return result;
	}
}
