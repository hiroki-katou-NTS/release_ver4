package nts.uk.ctx.at.record.dom.dailyperformanceprocessing.confirmationstatus;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

import org.apache.commons.lang3.tuple.Pair;

import lombok.val;
import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.record.dom.adapter.application.ApplicationRecordAdapter;
import nts.uk.ctx.at.record.dom.adapter.application.ApplicationRecordImport;
import nts.uk.ctx.at.record.dom.adapter.company.StatusOfEmployeeExport;
import nts.uk.ctx.at.record.dom.adapter.workflow.service.ApprovalStatusAdapter;
import nts.uk.ctx.at.record.dom.adapter.workflow.service.dtos.ApproveRootStatusForEmpImport;
import nts.uk.ctx.at.record.dom.adapter.workflow.service.enums.ApprovalStatusForEmployee;
import nts.uk.ctx.at.record.dom.application.realitystatus.RealityStatusService;
import nts.uk.ctx.at.record.dom.application.realitystatus.output.EmployeeErrorOuput;
import nts.uk.ctx.at.record.dom.approvalmanagement.ApprovalProcessingUseSetting;
import nts.uk.ctx.at.record.dom.dailyperformanceprocessing.appreflect.ReflectedStateRecord;
import nts.uk.ctx.at.record.dom.dailyperformanceprocessing.finddata.IFindDataDCRecord;
import nts.uk.ctx.at.record.dom.workrecord.identificationstatus.Identification;
import nts.uk.ctx.at.record.dom.workrecord.identificationstatus.IdentityProcessUseSet;
import nts.uk.ctx.at.record.dom.workrecord.identificationstatus.enums.SelfConfirmError;
import nts.uk.ctx.at.record.dom.workrecord.identificationstatus.month.ConfirmationMonth;
import nts.uk.ctx.at.record.dom.workrecord.identificationstatus.repository.ConfirmationMonthRepository;
import nts.uk.ctx.at.record.dom.workrecord.identificationstatus.repository.IdentificationRepository;
import nts.uk.ctx.at.shared.dom.workrule.closure.Closure;
import nts.uk.ctx.at.shared.dom.workrule.closure.ClosurePeriod;
import nts.uk.ctx.at.shared.dom.workrule.closure.ClosureRepository;
import nts.uk.ctx.at.shared.dom.workrule.closure.UseClassification;
import nts.arc.time.calendar.period.DatePeriod;

/**
 * @author thanhnx
 * 日の実績の確認状況を取得する
 */
@Stateless
public class ConfirmStatusActualDay {

	@Inject
	private IdentificationRepository identificationRepository;

	@Inject
	private ClosureRepository closureRepo;

	@Inject
	private ApprovalStatusAdapter approvalStatusAdapter;

	@Inject
	private ConfirmationMonthRepository confirmationMonthRepository;
	
	@Inject
	private IFindDataDCRecord iFindDataDCRecord;
	
	@Inject
	private RealityStatusService realityStatusService;
	
	@Inject
	private ApplicationRecordAdapter applicationRecordAdapter;
	
	/**
	 * 日の実績の確認状況を取得する
	 */
	public List<ConfirmStatusActualResult> processConfirmStatus(String companyId,
			List<String> employeeIds, DatePeriod period, Integer closureId, Optional<String> keyRandom) {
		List<ConfirmStatusActualResult> lstResult = new ArrayList<>();
		// ドメインモデル「本人確認処理の利用設定」を取得する
		Optional<IdentityProcessUseSet> optIndentity = iFindDataDCRecord.findIdentityByKey(companyId);
		if (!optIndentity.isPresent() || !optIndentity.get().isUseConfirmByYourself() || closureId == null)
			return new ArrayList<>();
		// 社員の指定期間中の所属期間を取得する
		List<StatusOfEmployeeExport> statusOfEmps = iFindDataDCRecord
				.getListAffComHistByListSidAndPeriod(keyRandom, employeeIds, period).stream().filter(x -> x.getEmployeeId() != null).collect(Collectors.toList());
		// ドメインモデル「日の本人確認」を取得する
		statusOfEmps.stream().forEach(statusOfEmp -> {
			List<ConfirmStatusActualResult> lstResultEmpTemp = new ArrayList<>(), 
											lstResultEmpTemp1 = new ArrayList<>(),
											lstResultEmpTemp2 = new ArrayList<>();
			List<DatePeriod> lstPeriod = statusOfEmp.getListPeriod();
			String employeeId = statusOfEmp.getEmployeeId();
			List<GeneralDate> dateTemps = new ArrayList<>();
			lstPeriod.stream().forEach(periodTemp -> {
				// ドメインモデル「日の本人確認」を取得する
				List<Identification> indentities = identificationRepository.findByEmployeeID(employeeId,
						periodTemp.start(), periodTemp.end());
				// set checkbox
				List<ConfirmStatusActualResult> lstResultChild = indentities.stream().map(x -> {
					return new ConfirmStatusActualResult(x.getEmployeeId(), x.getProcessingYmd(), true);
				}).collect(Collectors.toList());

				lstResultEmpTemp.addAll(lstResultChild);
				dateTemps.addAll(lstResultChild.stream().map(y -> y.getDate()).collect(Collectors.toList()));
			});

			// indentities.
			period.datesBetween().stream().filter(dateT -> !dateTemps.contains(dateT)).forEach(z -> {
				lstResultEmpTemp.add(new ConfirmStatusActualResult(employeeId, z, false));
			});

			lstResultEmpTemp2 = lstResultEmpTemp;

			//empHist = empHist.stream().filter(x -> x.getPeriod())
			// 指定した年月日時点の締め期間を取得する
			Optional<ClosurePeriod> closureStartOpt = findPeriodByClosure(companyId, closureId,
					lstPeriod.get(0).start());

			Optional<ClosurePeriod> closureEndOpt = findPeriodByClosure(companyId, closureId,
					lstPeriod.get(lstPeriod.size() - 1).end());

			// ドメインモデル「承認処理の利用設定」を取得する
			Optional<ApprovalProcessingUseSetting> approvalUseSettingOpt = iFindDataDCRecord
					.findApprovalByCompanyId(companyId);

			List<ClosurePeriod> lstMergePeriod = mergePeriod(closureStartOpt, closureEndOpt);
			for (ClosurePeriod mergePeriodClr : lstMergePeriod) {
				if (!approvalUseSettingOpt.isPresent() || !approvalUseSettingOpt.get().getUseMonthApproverConfirm()) {
					lstResultEmpTemp2 = lstResultEmpTemp2.stream().map(x -> {
						x.setPermission(true, true);
						return x;
					}).collect(Collectors.toList());
					// TODO: 取得した「本人確認処理の利用設定．月の本人確認を利用する」をチェックする
					lstResultEmpTemp1.addAll(
							updatePermission(companyId, employeeId, optIndentity.get(), mergePeriodClr, lstResultEmpTemp2));
				} else {

					//Map<Pair<String, GeneralDate>, ApprovalStatusForEmployee> mapApprovalStatus = new HashMap<>();
					DatePeriod mergePeriod = mergePeriodClr.getPeriod();
					// 対応するImported「（就業．勤務実績）承認対象者の承認状況」をすべて取得する
					List<ApproveRootStatusForEmpImport> lstApprovalStatus = approvalStatusAdapter
							.getApprovalByListEmplAndListApprovalRecordDateNew(Arrays.asList(mergePeriod.end()),
									Arrays.asList(employeeId), 2);
//					mapApprovalStatus.putAll(lstApprovalStatus.stream().collect(Collectors
//							.toMap(x -> Pair.of(x.getEmployeeID(), x.getAppDate()), x -> x.getApprovalStatus())));
					// list emp date unApproval
					val approvalStatusMonth = lstApprovalStatus.isEmpty() ? null : lstApprovalStatus.get(0);
					val lstEmpDateUnApproval = lstResultEmpTemp2.stream().filter(x -> {
						if(approvalStatusMonth == null) return true;
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
							lstEmpDateUnApproval));

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
			
			//取得した「承認処理の利用設定．日の承認者確認を利用する」をチェックする true
			if(approvalUseSettingOpt.isPresent() && approvalUseSettingOpt.get().getUseDayApproverConfirm()) {
				lstPeriod.stream().forEach(periodTemp -> {
					
					List<ApproveRootStatusForEmpImport> lstApprovalStatus = approvalStatusAdapter
							.getApprovalByListEmplAndListApprovalRecordDateNew(periodTemp.datesBetween(),
									Arrays.asList(employeeId), 1);
					val mapApprovalStatus = lstApprovalStatus.stream().collect(Collectors
							.toMap(x -> Pair.of(x.getEmployeeID(), x.getAppDate()), x -> x.getApprovalStatus(), (x, y) -> x));
					//lstResultEmpTemp3 = 
					lstResultEmpTemp1.forEach(x ->{
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
		});
		List<ConfirmStatusActualResult> results = new ArrayList<>();
		 Set<Pair<String, GeneralDate>> setErrorAndApp = getErrorAndApplication(employeeIds, period, optIndentity);
		 results = lstResult.stream().map(x ->{
			if(setErrorAndApp.contains(Pair.of(x.getEmployeeId(), x.getDate()))) {
				x.setPermissionChecked(false);
			}
			return x;
		}).collect(Collectors.toList());
		return results;

	}

	public Optional<ClosurePeriod> findPeriodByClosure(String companyId, int closureId, GeneralDate date) {

		Optional<Closure> optClosure = closureRepo.findById(companyId, closureId);

		// Check exist and active
		if (!optClosure.isPresent()
				|| optClosure.get().getUseClassification().equals(UseClassification.UseClass_NotUse)) {
			return Optional.empty();
		}

		Closure closure = optClosure.get();
		Optional<ClosurePeriod> cPeriod = closure.getClosurePeriodByYmd(date);
		return cPeriod;
	}

	public List<ClosurePeriod> mergePeriod(Optional<ClosurePeriod> closureStartOpt,
			Optional<ClosurePeriod> closureEndOpt) {
		if (!closureStartOpt.isPresent() && !closureEndOpt.isPresent())
			return Collections.emptyList();
		if (!closureStartOpt.isPresent())
			return Arrays.asList(closureEndOpt.get());
		if (!closureEndOpt.isPresent())
			return Arrays.asList(closureEndOpt.get());

		ClosurePeriod closureStart = closureStartOpt.get(), closureEnd = closureEndOpt.get();
		if (closureStart.getClosureId() == closureEnd.getClosureId() && closureStart.getPeriod().equals(closureEnd.getPeriod())) {
			return Arrays.asList(closureEndOpt.get());
		} else {
			return Arrays.asList(closureEndOpt.get(), closureStartOpt.get());
		}
	}

	public List<ConfirmStatusActualResult> updatePermission(String companyId, String employeeId,
			IdentityProcessUseSet identityProcessUseSet, ClosurePeriod mergePeriodClr,
			List<ConfirmStatusActualResult> lstResult) {
		lstResult = lstResult.stream().filter(x -> x.getDate().afterOrEquals(mergePeriodClr.getPeriod().start())
				&& x.getDate().beforeOrEquals(mergePeriodClr.getPeriod().end())).collect(Collectors.toList());
		if (!identityProcessUseSet.isUseIdentityOfMonth()) {
			return lstResult.stream().map(x -> {
				x.setPermission(true, true);
				return x;
			}).collect(Collectors.toList());
		} else {
			Optional<ConfirmationMonth> optConfirmMonth = confirmationMonthRepository.findByKey(companyId, employeeId,
					mergePeriodClr.getClosureId(), mergePeriodClr.getClosureDate(), mergePeriodClr.getYearMonth());
			return lstResult.stream().map(x -> {
				x.setPermission(true, !optConfirmMonth.isPresent());
				return x;
			}).collect(Collectors.toList());
		}
	}
	
//  public DatePeriod mergeDateInPeriod(DatePeriod periodNeedMerge, DatePeriod periodCheck) {
//	  GeneralDate startDate = periodNeedMerge.start().afterOrEquals(periodCheck.start()) ? periodNeedMerge.start() : periodCheck.start();
//	  GeneralDate endDate = periodNeedMerge.end().beforeOrEquals(periodCheck.end()) ? periodNeedMerge.end() : periodCheck.end();
//      return new DatePeriod(startDate, endDate);
//  }
	/**
	 * [No.606]社員リストと基準日から日の実績の確認状況を取得する
	 */
	public List<ConfirmStatusActualResult> processConfirmStatus(String companyId, List<String> employeeIds,
			GeneralDate dateRefer, Integer closureId) {
		// ドメインモデル「本人確認処理の利用設定」を取得する
		List<ConfirmStatusActualResult> lstResult = new ArrayList<>();
		Optional<IdentityProcessUseSet> optIndentity = iFindDataDCRecord.findIdentityByKey(companyId);
		if (!optIndentity.isPresent() || !optIndentity.get().isUseConfirmByYourself() || closureId == null)
			return new ArrayList<>();

		List<ConfirmStatusActualResult> lstResultEmpTemp = new ArrayList<>(), lstResultEmpTemp1 = new ArrayList<>(),
				lstResultEmpTemp2 = new ArrayList<>();

		List<Identification> indentities = identificationRepository.findByListEmpDate(employeeIds, dateRefer);
		// set checkbox
		// 取得した情報からパラメータ「日の実績の確認状況」を生成する
		List<ConfirmStatusActualResult> lstResultChild = indentities.stream().map(x -> {
			return new ConfirmStatusActualResult(x.getEmployeeId(), x.getProcessingYmd(), true);
		}).collect(Collectors.toList());

		lstResultEmpTemp.addAll(lstResultChild);

		List<String> lstEmpId = new ArrayList<>();
		lstEmpId.addAll(lstResultChild.stream().map(y -> y.getEmployeeId()).collect(Collectors.toList()));
		// indentities.
		employeeIds.stream().filter(emp -> !lstEmpId.contains(emp)).forEach(z -> {
			lstResultEmpTemp.add(new ConfirmStatusActualResult(z, dateRefer, false));
		});

		// ドメインモデル「締め」を取得する
		Optional<Closure> closure = closureRepo.findById(companyId, closureId);
		if (!closure.isPresent())
			return new ArrayList<>();

		// 指定した年月日時点の締め期間を取得する
		Optional<ClosurePeriod> closurePeriodOpt = closure.get().getClosurePeriodByYmd(dateRefer);
		if (!closurePeriodOpt.isPresent())
			return new ArrayList<>();

		// ドメインモデル「承認処理の利用設定」を取得する
		Optional<ApprovalProcessingUseSetting> approvalUseSettingOpt = iFindDataDCRecord
				.findApprovalByCompanyId(companyId);

		lstResultEmpTemp2 = lstResultEmpTemp;

		if (!approvalUseSettingOpt.isPresent() || !approvalUseSettingOpt.get().getUseMonthApproverConfirm()) {
			lstResultEmpTemp2 = lstResultEmpTemp2.stream().map(x -> {
				x.setPermission(true, true);
				return x;
			}).collect(Collectors.toList());
			// 取得した「本人確認処理の利用設定．月の本人確認を利用する」をチェックする
			lstResultEmpTemp1.addAll(updatePermission(companyId, employeeIds, optIndentity.get(),
					closurePeriodOpt.get(), lstResultEmpTemp2));
		} else {
			DatePeriod mergePeriod = closurePeriodOpt.get().getPeriod();
			// 対応するImported「（就業．勤務実績）承認対象者の承認状況」をすべて取得する
			List<ApproveRootStatusForEmpImport> lstApprovalStatus = approvalStatusAdapter
					.getApprovalByListEmplAndListApprovalRecordDateNew(Arrays.asList(mergePeriod.end()), employeeIds,
							2);
            Map<String, ApproveRootStatusForEmpImport> mapApprovalStatus  = lstApprovalStatus.stream().collect(Collectors.toMap(x -> x.getEmployeeID(), x -> x, (x, y) -> x));
			//val approvalStatusMonth = lstApprovalStatus.isEmpty() ? null : lstApprovalStatus.stream().get(0);
			val lstEmpDateUnApproval = lstResultEmpTemp2.stream().filter(x -> {
				val approvalStatusMonth = mapApprovalStatus.isEmpty() ? null : mapApprovalStatus.get(x.getEmployeeId());
				if (approvalStatusMonth == null)
					return true;
				val value = x.getEmployeeId().equals(approvalStatusMonth.getEmployeeID())
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

			lstResultEmpTemp1.addAll(updatePermission(companyId, employeeIds, optIndentity.get(),
					closurePeriodOpt.get(), lstEmpDateUnApproval));

			// list emp date Approval
			val lstEmpDateApproval = lstResultEmpTemp2.stream().filter(x -> {
				val approvalStatusMonth = mapApprovalStatus.isEmpty() ? null : mapApprovalStatus.get(x.getEmployeeId());
				val value = (approvalStatusMonth != null
						&& x.getEmployeeId().equals(approvalStatusMonth.getEmployeeID()))
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

		// 取得した「承認処理の利用設定．日の承認者確認を利用する」をチェックする true
		if (approvalUseSettingOpt.isPresent() && approvalUseSettingOpt.get().getUseDayApproverConfirm()) {
			List<ApproveRootStatusForEmpImport> lstApprovalStatus = approvalStatusAdapter
					.getApprovalByListEmplAndListApprovalRecordDateNew(
							closurePeriodOpt.get().getPeriod().datesBetween(), employeeIds, 1);
			val mapApprovalStatus = lstApprovalStatus.stream().collect(
					Collectors.toMap(x -> Pair.of(x.getEmployeeID(), x.getAppDate()), x -> x.getApprovalStatus(), (x, y) -> x));
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
		}

		lstResult.addAll(lstResultEmpTemp1);

		return lstResult;
	}

	public List<ConfirmStatusActualResult> updatePermission(String companyId, List<String> employeeIds,
			IdentityProcessUseSet identityProcessUseSet, ClosurePeriod mergePeriodClr,
			List<ConfirmStatusActualResult> lstResult) {
		lstResult = lstResult.stream().filter(x -> x.getDate().afterOrEquals(mergePeriodClr.getPeriod().start())
				&& x.getDate().beforeOrEquals(mergePeriodClr.getPeriod().end())).collect(Collectors.toList());
		if (!identityProcessUseSet.isUseIdentityOfMonth()) {
			return lstResult.stream().map(x -> {
				x.setPermission(true, true);
				return x;
			}).collect(Collectors.toList());
		} else {

			List<ConfirmationMonth> lstConfirmMonth = confirmationMonthRepository.findBySomeProperty(employeeIds,
					mergePeriodClr.getYearMonth().v(), mergePeriodClr.getClosureDate().getClosureDay().v(),
					mergePeriodClr.getClosureDate().getLastDayOfMonth(), mergePeriodClr.getClosureId().value);
			Map<String, ConfirmationMonth> mapConfirmMonth = lstConfirmMonth.stream()
					.collect(Collectors.toMap(x -> x.getEmployeeId(), x -> x, (x, y) -> x));
			return lstResult.stream().map(x -> {
				x.setPermission(true, !mapConfirmMonth.containsKey(x.getEmployeeId()));
				return x;
			}).collect(Collectors.toList());
		}
	}
	
	public Set<Pair<String, GeneralDate>> getErrorAndApplication(List<String> employeeIds, DatePeriod datePeriod, Optional<IdentityProcessUseSet> optIndentity){
		 Set<Pair<String, GeneralDate>> result = new HashSet<>();

		if (optIndentity.isPresent() && optIndentity.get().getYourSelfConfirmError().isPresent() && optIndentity.get()
				.getYourSelfConfirmError().get().value == SelfConfirmError.CAN_NOT_CHECK_WHEN_ERROR.value) {
			employeeIds.stream().forEach(error -> {
				List<EmployeeErrorOuput> lstOut = realityStatusService.checkEmployeeErrorOnProcessingDate(error,
						datePeriod.start(), datePeriod.end());
				result.addAll(lstOut.stream().filter(x -> x.getHasError()).map(x -> Pair.of(error, x.getDate()))
						.collect(Collectors.toSet()));
			});
		}
		List<ApplicationRecordImport> lstAppRecord = applicationRecordAdapter.getApplicationBySID(employeeIds,
				datePeriod.start(), datePeriod.end());
		lstAppRecord.forEach(x -> {
			boolean disable = (x.getReflectState() == ReflectedStateRecord.NOTREFLECTED.value
					|| x.getReflectState() == ReflectedStateRecord.REMAND.value)
					&& x.getAppType() != nts.uk.ctx.at.shared.dom.remainingnumber.algorithm.ApplicationType.OVER_TIME_APPLICATION.value;
			if (disable)
				result.add(Pair.of(x.getEmployeeID(), x.getAppDate()));
		});
		
		return result;
	}
}
