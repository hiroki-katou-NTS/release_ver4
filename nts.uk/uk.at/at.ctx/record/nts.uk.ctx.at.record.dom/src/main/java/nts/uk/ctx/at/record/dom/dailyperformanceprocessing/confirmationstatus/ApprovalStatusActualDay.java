package nts.uk.ctx.at.record.dom.dailyperformanceprocessing.confirmationstatus;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

import org.apache.commons.lang3.tuple.Pair;

import lombok.val;
import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.record.dom.adapter.company.StatusOfEmployeeExport;
import nts.uk.ctx.at.record.dom.adapter.workflow.service.ApprovalStatusAdapter;
import nts.uk.ctx.at.record.dom.adapter.workflow.service.dtos.ApprovalRootOfEmployeeImport;
import nts.uk.ctx.at.record.dom.adapter.workflow.service.dtos.ApprovalStatus;
import nts.uk.ctx.at.record.dom.adapter.workflow.service.dtos.ApproveRootStatusForEmpImport;
import nts.uk.ctx.at.record.dom.adapter.workflow.service.enums.ApprovalActionByEmpl;
import nts.uk.ctx.at.record.dom.adapter.workflow.service.enums.ApprovalStatusForEmployee;
import nts.uk.ctx.at.record.dom.adapter.workflow.service.enums.ReleasedProprietyDivision;
import nts.uk.ctx.at.record.dom.approvalmanagement.ApprovalProcessingUseSetting;
import nts.uk.ctx.at.record.dom.dailyperformanceprocessing.finddata.IFindDataDCRecord;
import nts.uk.ctx.at.record.dom.workrecord.identificationstatus.Identification;
import nts.uk.ctx.at.record.dom.workrecord.identificationstatus.IdentityProcessUseSet;
import nts.uk.ctx.at.record.dom.workrecord.identificationstatus.repository.IdentificationRepository;
import nts.uk.ctx.at.shared.dom.workrule.closure.ClosurePeriod;
import nts.uk.shr.com.context.AppContexts;
import nts.uk.shr.com.time.calendar.period.DatePeriod;

/**
 * @author thanhnx 日の実績の承認状況を取得する
 */
@Stateless
public class ApprovalStatusActualDay {

	@Inject
	private ApprovalStatusAdapter approvalStatusAdapter;

	@Inject
	private IFindDataDCRecord iFindDataDCRecord;

	@Inject
	private IdentificationRepository identificationRepository;

	@Inject
	private ConfirmStatusActualDay confirmStatusActualDay;

	/**
	 * 日の実績の承認状況を取得する
	 */
	public List<ApprovalStatusActualResult> processApprovalStatus(String companyId, List<String> employeeIds,
			DatePeriod period, Integer closureId, Optional<String> keyRandom) {

		List<ApprovalStatusActualResult> resultResult = new ArrayList<>();
		// ドメインモデル「承認処理の利用設定」を取得する
		Optional<ApprovalProcessingUseSetting> optApprovalUse = iFindDataDCRecord.findApprovalByCompanyId(companyId);
		if (!optApprovalUse.isPresent() || !optApprovalUse.get().getUseDayApproverConfirm() || closureId == null)
			return Collections.emptyList();

		// 社員の指定期間中の所属期間を取得する
		List<StatusOfEmployeeExport> statusOfEmps = iFindDataDCRecord
				.getListAffComHistByListSidAndPeriod(keyRandom, employeeIds, period).stream()
				.filter(x -> x.getEmployeeId() != null).collect(Collectors.toList());

		statusOfEmps.stream().forEach(statusOfEmp -> {
			List<ApprovalStatusActualResult> resultPart = new ArrayList<>(), resultPart2 = new ArrayList<>();
			List<DatePeriod> lstDatePeriod = statusOfEmp.getListPeriod().stream().map(x -> confirmStatusActualDay.mergeDateInPeriod(x, period)).collect(Collectors.toList());
			String employeeId = statusOfEmp.getEmployeeId();
			Map<Pair<String, GeneralDate>, ApprovalStatus> mapApprovalRootBySId = new HashMap<>();
			lstDatePeriod.stream().forEach(datePeriod -> {

				List<ApproveRootStatusForEmpImport> lstApprovalStatus = approvalStatusAdapter
						.getApprovalByListEmplAndListApprovalRecordDate(datePeriod.datesBetween(),
								Arrays.asList(employeeId), 1);

				ApprovalRootOfEmployeeImport approvalRoot = approvalStatusAdapter.getApprovalRootOfEmloyeeNew(
						datePeriod.start(), datePeriod.end(), AppContexts.user().employeeId(), companyId, 1);
				Map<Pair<String, GeneralDate>, ApprovalStatus> mapApprovalRoot = approvalRoot
						.getApprovalRootSituations().stream()
						.collect(Collectors.toMap(x -> Pair.of(x.getTargetID(), x.getAppDate()),
								x -> x.getApprovalStatus()));
				mapApprovalRootBySId.putAll(mapApprovalRoot);

				// set 承認状況： 取得した「承認対象者の承認状況．承認状況」
				lstApprovalStatus.stream().forEach(x -> {
					if (x.getApprovalStatus() == ApprovalStatusForEmployee.UNAPPROVED) {
						resultPart2.add(new ApprovalStatusActualResult(employeeId, x.getAppDate(), false, false));
					} else {
						resultPart2.add(new ApprovalStatusActualResult(employeeId, x.getAppDate(), false, true));
					}
					;
				});

				// ApprovalActionByEmpl
				resultPart2.stream().forEach(x -> {
					val temp = mapApprovalRoot.get(Pair.of(x.getEmployeeId(), x.getDate()));
					if (temp != null && temp.getApprovalActionByEmpl() == ApprovalActionByEmpl.APPROVALED)
						x.updateApprovalStatus(true);
				});

				Optional<IdentityProcessUseSet> identitySetOpt = iFindDataDCRecord.findIdentityByKey(companyId);
				if (!identitySetOpt.isPresent() || !identitySetOpt.get().isUseConfirmByYourself()) {
					resultPart.addAll(updatePermissionCheck(resultPart2, true));
				} else {
					List<Identification> lstIdentity = identificationRepository.findByEmployeeID(employeeId,
							datePeriod.start(), datePeriod.end());
					List<GeneralDate> dateTemps = lstIdentity.stream().map(x -> x.getProcessingYmd())
							.collect(Collectors.toList());
					val temp1 = updatePermissionCheck(resultPart2.stream().filter(x -> !dateTemps.contains(x.getDate()))
							.collect(Collectors.toList()), false);
					val temp2 = updatePermissionCheck(resultPart2.stream().filter(x -> dateTemps.contains(x.getDate()))
							.collect(Collectors.toList()), true);
					resultPart.addAll(temp1);
					resultPart.addAll(temp2);
				}

			});

			if (!optApprovalUse.isPresent() || !optApprovalUse.get().getUseDayApproverConfirm()) {
				updatePermissionRelease(resultPart, true);
			} else {
				// 指定した年月日時点の締め期間を取得する
				Optional<ClosurePeriod> closureStartOpt = confirmStatusActualDay.findPeriodByClosure(companyId,
						closureId, lstDatePeriod.get(0).start());

				Optional<ClosurePeriod> closureEndOpt = confirmStatusActualDay.findPeriodByClosure(companyId, closureId,
						lstDatePeriod.get(lstDatePeriod.size() - 1).end());

				List<ClosurePeriod> lstMergePeriod = confirmStatusActualDay.mergePeriod(closureStartOpt, closureEndOpt);
				for (ClosurePeriod mergePeriodClr : lstMergePeriod) {
					DatePeriod mergePeriod = mergePeriodClr.getPeriod();
					// 対応するImported「（就業．勤務実績）承認対象者の承認状況」をすべて取得する
					List<ApproveRootStatusForEmpImport> lstApprovalStatus = approvalStatusAdapter
							.getApprovalByListEmplAndListApprovalRecordDateNew(mergePeriod.datesBetween(),
									Arrays.asList(employeeId), 2);
					val mapApprovalStatus = lstApprovalStatus.stream().collect(Collectors
							.toMap(x -> Pair.of(x.getEmployeeID(), x.getAppDate()), x -> x.getApprovalStatus()));
					// mapApprovalRootBySId 
					resultPart.stream().forEach(x -> {
						val tempApprovalStatus = mapApprovalStatus.get(Pair.of(x.getEmployeeId(), x.getDate()));
						val mapApprovalRoot = mapApprovalRootBySId.get(Pair.of(x.getEmployeeId(), x.getDate())); 
						if (mapApprovalRoot != null && tempApprovalStatus != null) {
							if (mapApprovalRoot.getReleaseDivision() == ReleasedProprietyDivision.RELEASE && tempApprovalStatus != ApprovalStatusForEmployee.UNAPPROVED)
								x.setPermissionRelease(true);
							else
								x.setPermissionRelease(false);
						}
					});
				}
			}
			resultResult.addAll(resultPart);
		});
		return resultResult;
	}

	public <T extends ConfirmStatusActualResult> List<T> updatePermissionCheck(List<T> datas, boolean checked) {
		return datas.stream().map(x -> {
			x.setPermissionChecked(checked);
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
