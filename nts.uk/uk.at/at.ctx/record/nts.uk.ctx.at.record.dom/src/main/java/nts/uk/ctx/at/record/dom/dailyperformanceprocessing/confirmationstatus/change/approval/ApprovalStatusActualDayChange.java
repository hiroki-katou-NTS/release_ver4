package nts.uk.ctx.at.record.dom.dailyperformanceprocessing.confirmationstatus.change.approval;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;

import org.apache.commons.lang3.tuple.Pair;

import lombok.val;
import nts.arc.time.GeneralDate;
import nts.arc.time.YearMonth;
import nts.uk.ctx.at.record.dom.adapter.company.StatusOfEmployeeExport;
import nts.uk.ctx.at.record.dom.approvalmanagement.ApprovalProcessingUseSetting;
import nts.uk.ctx.at.record.dom.dailyperformanceprocessing.confirmationstatus.ApprovalStatusActualResult;
import nts.uk.ctx.at.record.dom.dailyperformanceprocessing.confirmationstatus.ConfirmStatusActualResult;
import nts.uk.ctx.at.record.dom.dailyperformanceprocessing.confirmationstatus.ModeData;
import nts.uk.ctx.at.record.dom.dailyperformanceprocessing.confirmationstatus.change.confirm.ConfirmInfoResult;
import nts.uk.ctx.at.record.dom.dailyperformanceprocessing.confirmationstatus.change.confirm.InformationMonth;
import nts.uk.ctx.at.record.dom.dailyperformanceprocessing.finddata.IFindDataDCRecord;
import nts.uk.ctx.at.record.dom.monthlycommon.aggrperiod.AggrPeriodEachActualClosure;
import nts.uk.ctx.at.record.dom.workrecord.approval.daily.ApprovalProgressDaily;
import nts.uk.ctx.at.record.dom.workrecord.approval.daily.ApprovalSubjectiveDailyOnWorkflow;
import nts.uk.ctx.at.record.dom.workrecord.approval.monthly.ApprovalProgressMonthly;
import nts.uk.ctx.at.record.dom.workrecord.identificationstatus.Identification;
import nts.uk.ctx.at.record.dom.workrecord.identificationstatus.IdentityProcessUseSet;
import nts.uk.ctx.at.shared.dom.attendance.data.DailyRecordKey;
import nts.uk.shr.com.time.calendar.period.DatePeriod;

/**
 * @author thanhnx
 *
 */
@Stateless
@TransactionAttribute(TransactionAttributeType.SUPPORTS)
public class ApprovalStatusActualDayChange {

	@Inject
	private IFindDataDCRecord iFindDataDCRecord;

	@Inject
	private ApprovalInfoAcqProcess approvalInfoAcqProcess;

	// [No.585]日の実績の承認状況を取得する（NEW）
	public List<ApprovalStatusActualResult> processApprovalStatus(String companyId, String empTarget,
			List<String> employeeIds, Optional<DatePeriod> periodOpt, Optional<YearMonth> yearMonthOpt, int mode) {
		// ドメインモデル「承認処理の利用設定」を取得する
		Optional<ApprovalProcessingUseSetting> optApprovalUse = iFindDataDCRecord.findApprovalByCompanyId(companyId);
		if (!optApprovalUse.isPresent() || !optApprovalUse.get().getUseDayApproverConfirm())
			return Collections.emptyList();

		List<ConfirmInfoResult> confirmInfoResults = approvalInfoAcqProcess.getApprovalInfoAcp(companyId, empTarget,
				employeeIds, periodOpt, yearMonthOpt);
		if (confirmInfoResults.isEmpty())
			return Collections.emptyList();
		// ドメインモデル「本人確認処理の利用設定」を取得する
		Optional<IdentityProcessUseSet> optIndentity = iFindDataDCRecord.findIdentityByKey(companyId);
		return processApprovalStatusCommon(companyId, empTarget, employeeIds, confirmInfoResults, optIndentity,
				optApprovalUse, mode);
	}

	public List<ApprovalStatusActualResult> processApprovalStatusCommon(String companyId, String tartgetEmp,
			List<String> employeeIds, List<ConfirmInfoResult> approvalInfoResults,
			Optional<IdentityProcessUseSet> optIndentity, Optional<ApprovalProcessingUseSetting> approvalUseSettingOpt,
			int mode) {
		List<ApprovalStatusActualResult> resultResult = new ArrayList<>();
		Optional<ApprovalProcessingUseSetting> optApprovalUse = approvalUseSettingOpt.isPresent()
				? approvalUseSettingOpt
				: iFindDataDCRecord.findApprovalByCompanyId(companyId);
		// 社員の指定期間中の所属期間を取得する
		// List<StatusOfEmployeeExport> statusOfEmps =
		// approvalInfoResultAll.getStatusOfEmps();
		approvalInfoResults.stream().forEach(approvalInfoResult -> {
			// statusOfEmps.stream().forEach(statusOfEmp -> {
			StatusOfEmployeeExport statusOfEmp = approvalInfoResult.getStatusOfEmp();
			List<ApprovalStatusActualResult> resultPart = new ArrayList<>(), resultPart2 = new ArrayList<>();
			List<DatePeriod> lstDatePeriod = statusOfEmp.getListPeriod();
			String employeeId = statusOfEmp.getEmployeeId();
			Map<DailyRecordKey, ApprovalSubjectiveDailyOnWorkflow> mapApprovalRootBySId = new HashMap<>();
			lstDatePeriod.stream().forEach(datePeriod -> {

				// 対応するImported「（就業．勤務実績）承認対象者の承認状況」をすべて取得する
				// List<ApproveRootStatusForEmpImport> lstApprovalStatus = approvalStatusAdapter
				// .getApprovalByListEmplAndListApprovalRecordDateNew(datePeriod.datesBetween(),
				// Arrays.asList(employeeId), 1);
				List<ApprovalProgressDaily> lstApprovalStatus = approvalInfoResult.getInformationDay().getLstApprovalDayStatus();
				// 対応するImported「基準社員の承認対象者」を取得する
				List<ApprovalSubjectiveDailyOnWorkflow> lstApprovalRootDay = approvalInfoResult.getInformationDay().getLstApprovalRootDaily();
				Map<DailyRecordKey, ApprovalSubjectiveDailyOnWorkflow> mapApprovalRoot = lstApprovalRootDay.stream()
						.collect(Collectors.toMap(x -> new DailyRecordKey(x.getTargetEmployeeId(), x.getDate()), x -> x, (x, y) -> x));
				mapApprovalRootBySId.putAll(mapApprovalRoot);

				if (mode == ModeData.NORMAL.value) {
					// set 承認状況： 取得した「承認対象者の承認状況．承認状況」
					lstApprovalStatus.stream().forEach(x -> {
						if (x.isUnapproved()) {
							resultPart2.add(new ApprovalStatusActualResult(employeeId, x.getDate(), false, false));
						} else {
							resultPart2.add(new ApprovalStatusActualResult(employeeId, x.getDate(), false, true));
						}
						;
					});
					// ApprovalActionByEmpl
					resultPart2.stream().forEach(x -> {
						val temp = mapApprovalRoot.get(new DailyRecordKey(x.getEmployeeId(), x.getDate()));
						if (temp != null && temp.getSubjective().isApproved())
							x.updateApprovalStatus(true);
					});

				} else {
					Map<Pair<String, GeneralDate>, ApprovalProgressDaily> mapApprovalStatus = lstApprovalStatus
							.stream().collect(Collectors.toMap(x -> Pair.of(x.getEmployeeId(), x.getDate()), x -> x, (x, y) -> x));
					if (!lstApprovalRootDay.isEmpty()) {
						lstApprovalRootDay.stream()
								.filter(x -> x.getTargetEmployeeId().equals(employeeId)).forEach(x -> {
									if (x.getSubjective().isApproved()) {
										resultPart2.add(new ApprovalStatusActualResult(employeeId, x.getDate(), true,
												false));
									} else {
										resultPart2.add(new ApprovalStatusActualResult(employeeId, x.getDate(),
												false, false));
									}
								});
					}

					resultPart2.stream().forEach(x -> {
						val temp = mapApprovalStatus.get(Pair.of(x.getEmployeeId(), x.getDate()));
						if (temp != null && !temp.isUnapproved())
							x.setStatusNormal(true);
					});

				}

				Optional<IdentityProcessUseSet> identitySetOpt = optIndentity.isPresent() ? optIndentity
						: iFindDataDCRecord.findIdentityByKey(companyId);
				if (!identitySetOpt.isPresent() || !identitySetOpt.get().isUseConfirmByYourself()) {
					resultPart.addAll(updatePermissionCheckWithCondition(resultPart2, true, mapApprovalRoot));
				} else {
					// Acquire the domain model "identification of the day"
					List<Identification> lstIdentity = approvalInfoResult.getInformationDay().getIndentities();
					List<GeneralDate> dateTemps = lstIdentity.stream().map(x -> x.getProcessingYmd())
							.collect(Collectors.toList());

					val temp1 = updatePermissionCheckWithCondition(resultPart2.stream()
							.filter(x -> !dateTemps.contains(x.getDate())).collect(Collectors.toList()), false,
							mapApprovalRoot);
					val temp2 = updatePermissionCheckWithCondition(resultPart2.stream()
							.filter(x -> dateTemps.contains(x.getDate())).collect(Collectors.toList()), true,
							mapApprovalRoot);
					resultPart.addAll(temp1);
					resultPart.addAll(temp2);
				}

			});

			if (!optApprovalUse.isPresent() || !optApprovalUse.get().getUseMonthApproverConfirm()) {
				updatePermissionRelease(resultPart, true);
			} else {
				List<InformationMonth> informationMonths = approvalInfoResult.getInformationMonths();
				List<ApprovalProgressMonthly> lstApprovalStatus = approvalInfoResult.getInformationMonths()
						.stream().flatMap(x -> x.getLstApprovalMonthStatus().stream()).collect(Collectors.toList());
				for(InformationMonth informationMonth : informationMonths) {
//				for (AggrPeriodEachActualClosure mergePeriodClr : approvalInfoResult.getAggrPeriods()) {
					AggrPeriodEachActualClosure mergePeriodClr = informationMonth.getActualClosure();
					DatePeriod mergePeriod = mergePeriodClr.getPeriod();
					// 対応するImported「（就業．勤務実績）承認対象者の承認状況」をすべて取得する
//					List<ApproveRootStatusForEmpImport> lstApprovalStatus = approvalInfoResult
//							.getLstApprovalMonthStatus();
					ApprovalProgressMonthly mapApprovalStatus = lstApprovalStatus.isEmpty() ? null
							: lstApprovalStatus.get(0);
					// mapApprovalRootBySId
					resultPart.stream().forEach(x -> {
						val mapApprovalRoot = mapApprovalRootBySId.get(new DailyRecordKey(x.getEmployeeId(), x.getDate()));
						if (mapApprovalRoot != null && mapApprovalStatus != null
								&& (x.getDate().afterOrEquals(mergePeriod.start())
										&& x.getDate().beforeOrEquals(mergePeriod.end()))) {
							if (mapApprovalRoot.getSubjective().canRelease()
									&& mapApprovalStatus.isUnapproved())
								x.setPermissionRelease(true);
							else
								x.setPermissionRelease(false);
						} else if (mapApprovalStatus == null) {
							x.setPermissionRelease(true);
						}
					});
//				}
			}
			}
			resultResult.addAll(resultPart);
			// });
		});
		return resultResult;
	}

	public <T extends ConfirmStatusActualResult> List<T> updatePermissionCheckWithCondition(List<T> datas,
			boolean checked,
			Map<DailyRecordKey, ApprovalSubjectiveDailyOnWorkflow> mapApprovalRoot) {
		return datas.stream().map(x -> {
			val data = mapApprovalRoot.get(new DailyRecordKey(x.getEmployeeId(), x.getDate()));
			val dataCompleteApproval = (data == null) ? false : checked && data.getSubjective().canExecute();
			x.setPermissionChecked(dataCompleteApproval);
			return x;
		}).collect(Collectors.toList());
	}

	public <T extends ConfirmStatusActualResult> List<T> updatePermissionRelease(List<T> datas, boolean release) {
		return datas.stream().map(x -> {
			x.setPermissionRelease(release);
			return x;
		}).collect(Collectors.toList());
	}

}
