package nts.uk.ctx.at.record.dom.dailyperformanceprocessing.confirmationstatus.change.confirm;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.arc.time.GeneralDate;
import nts.arc.time.YearMonth;
import nts.uk.ctx.at.record.dom.adapter.application.ApplicationRecordAdapter;
import nts.uk.ctx.at.record.dom.adapter.application.ApplicationRecordImport;
import nts.uk.ctx.at.record.dom.adapter.company.StatusOfEmployeeExport;
import nts.uk.ctx.at.record.dom.application.realitystatus.RealityStatusService;
import nts.uk.ctx.at.record.dom.dailyperformanceprocessing.confirmationstatus.change.CommonProcess;
import nts.uk.ctx.at.record.dom.dailyperformanceprocessing.finddata.IFindDataDCRecord;
import nts.uk.ctx.at.record.dom.monthlycommon.aggrperiod.AggrPeriodEachActualClosure;
import nts.uk.ctx.at.record.dom.monthlycommon.aggrperiod.ClosurePeriod;
import nts.uk.ctx.at.record.dom.monthlycommon.aggrperiod.ClosurePeriodCacheKey;
import nts.uk.ctx.at.record.dom.monthlycommon.aggrperiod.GetClosurePeriod;
import nts.uk.ctx.at.record.dom.workrecord.approval.daily.ApprovalProgressDaily;
import nts.uk.ctx.at.record.dom.workrecord.approval.daily.ApprovalStatusDailyAdapter;
import nts.uk.ctx.at.record.dom.workrecord.approval.monthly.ApprovalProgressMonthly;
import nts.uk.ctx.at.record.dom.workrecord.approval.monthly.ApprovalStatusMonthlyAdapter;
import nts.uk.ctx.at.record.dom.workrecord.identificationstatus.Identification;
import nts.uk.ctx.at.record.dom.workrecord.identificationstatus.month.ConfirmationMonth;
import nts.uk.ctx.at.record.dom.workrecord.identificationstatus.repository.ConfirmationMonthRepository;
import nts.uk.ctx.at.record.dom.workrecord.identificationstatus.repository.IdentificationRepository;
import nts.uk.shr.com.time.calendar.period.DatePeriod;

/**
 * @author thanhnx
 *
 */
@Stateless
public class ConfirmStatusInfoEmp {

	@Inject
	private IFindDataDCRecord iFindDataDCRecord;

	@Inject
	private IdentificationRepository identificationRepository;

	@Inject
	private ApprovalStatusMonthlyAdapter approvalStatusMonthlyAdapter;

	@Inject
	private ApprovalStatusDailyAdapter approvalStatusDailyAdapter;

	@Inject
	private ApplicationRecordAdapter applicationRecordAdapter;

	@Inject
	private ConfirmationMonthRepository confirmationMonthRepository;

	@Inject
	private RealityStatusService realityStatusService;

	@Inject
	private GetClosurePeriod getClosurePeriod;

	/**
	 * 社員1件の確認状況情報を取得する
	 */
	public List<ConfirmInfoResult> confirmStatusInfoOneEmp(String employeeId,
			Optional<DatePeriod> periodOpt, Optional<YearMonth> yearMonthOpt,
			Map<ClosurePeriodCacheKey, List<ClosurePeriod>> cachedClosurePeriod) {

		List<ClosurePeriod> lstClosure = new ArrayList<>();
		if (periodOpt.isPresent()) {
			// 期間を指定して集計期間を求める
			lstClosure = getClosurePeriod.fromPeriod(employeeId, periodOpt.get().end(), periodOpt.get(), cachedClosurePeriod);
		} else {
			// 年月を指定して集計期間を求める
			GeneralDate dateRefer = GeneralDate.ymd(yearMonthOpt.get().year(), yearMonthOpt.get().month(), yearMonthOpt.get().lastDateInMonth());
			lstClosure = getClosurePeriod.fromYearMonth(employeeId, dateRefer, yearMonthOpt.get(), cachedClosurePeriod);
		}

		// Output「締め処理期間．集計期間．期間」のMAX期間を求める
		List<DatePeriod> periods = lstClosure.stream().flatMap(x -> x.getAggrPeriods().stream()).map(x -> x.getPeriod())
				.collect(Collectors.toList());
		DatePeriod dateMax = CommonProcess.getMaxPeriod(periods);
		if (dateMax == null)
			return new ArrayList<>();
		List<StatusOfEmployeeExport> statusOfEmps = iFindDataDCRecord
				.getListAffComHistByListSidAndPeriod(Optional.empty(), Arrays.asList(employeeId), dateMax).stream()
				.filter(x -> x.getEmployeeId() != null).collect(Collectors.toList());

		if (statusOfEmps.isEmpty())
			return new ArrayList<>();
		// Output「社員の会社所属状況．所属状況．期間」のMAX期間を求める
		DatePeriod dateEmpExport = CommonProcess.getMaxPeriod(statusOfEmps.get(0).getListPeriod());
		// ドメインモデル「日の本人確認」を取得する
		List<Identification> indentities = identificationRepository.findByEmployeeID(employeeId, dateEmpExport.start(),
				dateEmpExport.end());

		// 対応するImported「（就業．勤務実績）承認対象者の承認状況」をすべて取得する
		List<ApprovalProgressDaily> lstApprovalDayStatus = approvalStatusDailyAdapter
				.getProgress(Arrays.asList(employeeId), dateEmpExport);
		
		List<ApplicationRecordImport> lstApplication = applicationRecordAdapter
				.getApplicationBySID(Arrays.asList(employeeId), dateEmpExport.start(), dateEmpExport.end());

		List<AggrPeriodEachActualClosure> aggrPeriods = lstClosure.stream().flatMap(x -> x.getAggrPeriods().stream())
				.collect(Collectors.toList());
		// Output「締め処理期間．実締め毎集計期間」の件数ループする
		List<InformationMonth> inforMonths = new ArrayList<>();
		for (AggrPeriodEachActualClosure mergePeriodClr : aggrPeriods) {

			List<ConfirmationMonth> lstConfirmMonth = confirmationMonthRepository.findBySomeProperty(
					Arrays.asList(employeeId), mergePeriodClr.getClosureMonth());

			// 対応するImported「（就業．勤務実績）承認対象者の承認状況」をすべて取得する
			List<ApprovalProgressMonthly> lstApprovalMonthStatus = approvalStatusMonthlyAdapter
					.getProgress(Arrays.asList(employeeId), mergePeriodClr.getClosureMonth(), mergePeriodClr.getPeriod());

			inforMonths.add(new InformationMonth(mergePeriodClr, lstConfirmMonth, lstApprovalMonthStatus));
		}

		// [No.303]対象期間に日別実績のエラーが発生している年月日を取得する
		List<EmployeeDateErrorOuput> lstOut = realityStatusService
				.checkEmployeeErrorOnProcessingDate(employeeId, dateEmpExport.start(), dateEmpExport.end()).stream()
				.map(x -> {
					return new EmployeeDateErrorOuput(employeeId, x.getDate(), x.getHasError());
				}).collect(Collectors.toList());
		return Arrays.asList(ConfirmInfoResult.builder().employeeId(employeeId).period(dateMax)
				.informationDay(new InformationDay(indentities, lstApprovalDayStatus)).lstApplication(lstApplication)
				.lstOut(lstOut).statusOfEmp(statusOfEmps.get(0)).informationMonths(inforMonths).build());

	}

	// 複数社員の確認状況情報を取得する
	public List<ConfirmInfoResult> confirmStatusInfoMulEmp(List<String> employeeIds,
			Optional<DatePeriod> periodOpt, Optional<YearMonth> yearMonthOpt,
			Map<ClosurePeriodCacheKey, List<ClosurePeriod>> cachedClosurePeriod) {
		List<ConfirmInfoResult> results = new ArrayList<>();
		Map<DatePeriod, Set<String>> groupByMaxPeriodEmpList = new HashMap<>();
		Map<AggrPeriodEachActualClosure, Set<String>> aggrPeriods = new HashMap<>();
		for (String employeeId : employeeIds) {
			List<ClosurePeriod> lstClosure;
			if (periodOpt.isPresent()) {
				// 期間を指定して集計期間を求める
				lstClosure = getClosurePeriod.fromPeriod(employeeId, periodOpt.get().end(), periodOpt.get(), cachedClosurePeriod);
			} else {
				// 年月を指定して集計期間を求める
				GeneralDate dateRefer = GeneralDate.ymd(yearMonthOpt.get().year(), yearMonthOpt.get().month(), yearMonthOpt.get().lastDateInMonth());
				lstClosure = getClosurePeriod.fromYearMonth(employeeId, dateRefer, yearMonthOpt.get(), cachedClosurePeriod);
			}

			// Output「締め処理期間．集計期間．期間」のMAX期間を求める
			List<DatePeriod> periods = lstClosure.stream().flatMap(x -> x.getAggrPeriods().stream())
					.map(x -> x.getPeriod()).collect(Collectors.toList());
			DatePeriod dateMax = CommonProcess.getMaxPeriod(periods);
			if (dateMax == null)
				continue;

			Set<String> emplist;
			if (groupByMaxPeriodEmpList.containsKey(dateMax)) {
				emplist = groupByMaxPeriodEmpList.get(dateMax);
				if (emplist == null) emplist = new HashSet<>();
			}
			else {
				emplist = new HashSet<>();
			}
			emplist.add(employeeId);
			groupByMaxPeriodEmpList.put(dateMax, emplist);

			// Output「締め処理期間．実締め毎集計期間」の件数ループする
			lstClosure.stream()
				.flatMap(c -> c.getAggrPeriods().stream())
				.forEach(c -> {
					Set<String> e = aggrPeriods.get(c);
					if (e == null || e.isEmpty()) {
						e = new HashSet<String>(Arrays.asList(employeeId));
					} else {
						e.add(employeeId);
					}
					aggrPeriods.put(c, e);
				});
		}

		Map<String, InformationMonth> inforMonths = new HashMap<>();
		for (AggrPeriodEachActualClosure mergePeriodClr : aggrPeriods.keySet()) {
			List<String> emplist = new ArrayList<>(aggrPeriods.get(mergePeriodClr));

			// ドメインモデル「月の本人確認」を取得する
			List<ConfirmationMonth> lstConfirmMonth = confirmationMonthRepository.findBySomeProperty(
					emplist, mergePeriodClr.getClosureMonth());

			// 対応するImported「（就業．勤務実績）承認対象者の承認状況」をすべて取得する
			List<ApprovalProgressMonthly> lstApprovalMonthStatus = approvalStatusMonthlyAdapter
					.getProgress(emplist, mergePeriodClr.getClosureMonth(), mergePeriodClr.getPeriod());

			for (String employeeId : emplist) {
				if(!inforMonths.containsKey(employeeId)){
					inforMonths.put(employeeId, new InformationMonth(
							mergePeriodClr,
							lstConfirmMonth.stream().filter(cm -> employeeId.equals(cm.getEmployeeId())).collect(Collectors.toList()),
							lstApprovalMonthStatus.stream().filter(ams -> employeeId.equals(ams.getEmployeeId())).collect(Collectors.toList())));
				}
			}
		}

		Map<DatePeriod, Set<String>> groupByDateEmpExportEmpList = new HashMap<>();
		Map<String, List<StatusOfEmployeeExport>> statusOfEmps = new HashMap<>();
		for (DatePeriod dateMax : groupByMaxPeriodEmpList.keySet()) {
			List<String> employeeList = new ArrayList<>(groupByMaxPeriodEmpList.get(dateMax));

			List<StatusOfEmployeeExport> status = iFindDataDCRecord
					.getListAffComHistByListSidAndPeriod(Optional.empty(), employeeList, dateMax).stream()
					.filter(x -> x.getEmployeeId() != null).collect(Collectors.toList());

			if (status.isEmpty())
			    continue;

			// Output「社員の会社所属状況．所属状況．期間」のMAX期間を求める
			Map<DatePeriod, List<StatusOfEmployeeExport>> empGroup = status.stream()
					.collect(Collectors.groupingBy(e -> CommonProcess.getMaxPeriod(e.getListPeriod())));

			for (DatePeriod dp : empGroup.keySet()) {
				Set<String> emplist = empGroup.get(dp).stream().map(e -> e.getEmployeeId()).collect(Collectors.toSet());
				groupByDateEmpExportEmpList.put(dp, emplist);
			}

			status.stream().forEach(s -> {
				statusOfEmps.merge(s.getEmployeeId(), Arrays.asList(s), (v1, v2) -> { v1.addAll(v2); return v1; });
			});
		}

		for (DatePeriod dateEmpExport : groupByDateEmpExportEmpList.keySet()) {
			List<String> employeeList = new ArrayList<>(groupByDateEmpExportEmpList.get(dateEmpExport));

			// ドメインモデル「日の本人確認」を取得する
			List<Identification> indentities = identificationRepository.findByListEmployeeID(employeeList,
					dateEmpExport.start(), dateEmpExport.end());

			// 対応するImported「（就業．勤務実績）承認対象者の承認状況」をすべて取得する
			List<ApprovalProgressDaily> lstApprovalDayStatus = approvalStatusDailyAdapter
					.getProgress(employeeList, dateEmpExport);

			// [No.26]社員、期間に一致する申請を取得する
			List<ApplicationRecordImport> lstApplication = applicationRecordAdapter
					.getApplicationBySID(employeeList, dateEmpExport.start(), dateEmpExport.end());
			
			for (String employeeId : employeeList) {

				List<Identification> indentifications = indentities.stream()
						.filter(i -> i.getEmployeeId().equals(employeeId)).collect(Collectors.toList());
				List<ApprovalProgressDaily> approvalDayStatus = lstApprovalDayStatus.stream()
						.filter(i -> i.getEmployeeId().equals(employeeId)).collect(Collectors.toList());
				List<ApplicationRecordImport> application = lstApplication.stream()
						.filter(i -> i.getEmployeeID().equals(employeeId)).collect(Collectors.toList());

				InformationMonth inforMonth = inforMonths.get(employeeId);
						
				// [No.303]対象期間に日別実績のエラーが発生している年月日を取得する
				List<EmployeeDateErrorOuput> lstOut = realityStatusService
						.checkEmployeeErrorOnProcessingDate(employeeId, dateEmpExport.start(), dateEmpExport.end()).stream()
						.map(x -> {
							return new EmployeeDateErrorOuput(employeeId, x.getDate(), x.getHasError());
						}).collect(Collectors.toList());

				DatePeriod dateMax = groupByDateEmpExportEmpList.entrySet().stream()
					.filter(es -> es.getValue().contains(employeeId))
					.map(es -> es.getKey())
					.findFirst().orElse(null);

				// 外部でremoveIfなどの操作が実行されるため、変更可能なArrayListで作る必要がある
				List<InformationMonth> inforMonthMutableList = new ArrayList<>();
				inforMonthMutableList.add(inforMonth);
				
				ConfirmInfoResult result = ConfirmInfoResult.builder()
						.employeeId(employeeId)
						.period(dateMax)
						.informationDay(new InformationDay(indentifications, approvalDayStatus))
						.lstApplication(application)
						.lstOut(lstOut)
						.statusOfEmp(statusOfEmps.get(employeeId).get(0))
						.informationMonths(inforMonthMutableList)
						.build();
				results.add(result);
			}
		}
		return results;
	}

	// 複数社員の確認状況情報を取得する
	public List<ConfirmInfoResult> confirmStatusInfoMulEmp(List<String> employeeIds,
			Optional<DatePeriod> periodOpt, Optional<YearMonth> yearMonthOpt) {
		List<ConfirmInfoResult> results = new ArrayList<>();
		for (String employeeId : employeeIds) {
			List<ClosurePeriod> lstClosure = new ArrayList<>();
			Map<ClosurePeriodCacheKey, List<ClosurePeriod>> cachedClosurePeriod = new HashMap<>();
			if (periodOpt.isPresent()) {
				// 期間を指定して集計期間を求める
				lstClosure = getClosurePeriod.fromPeriod(employeeId, periodOpt.get().end(), periodOpt.get(), cachedClosurePeriod);
			} else {
				// 年月を指定して集計期間を求める
				GeneralDate dateRefer = GeneralDate.ymd(yearMonthOpt.get().year(), yearMonthOpt.get().month(), yearMonthOpt.get().lastDateInMonth());
				lstClosure = getClosurePeriod.fromYearMonth(employeeId, dateRefer, yearMonthOpt.get(), cachedClosurePeriod);
			}

			// Output「締め処理期間．集計期間．期間」のMAX期間を求める
			List<DatePeriod> periods = lstClosure.stream().flatMap(x -> x.getAggrPeriods().stream())
					.map(x -> x.getPeriod()).collect(Collectors.toList());
			DatePeriod dateMax = CommonProcess.getMaxPeriod(periods);
			if (dateMax == null)
				continue;

			List<StatusOfEmployeeExport> statusOfEmps = iFindDataDCRecord
					.getListAffComHistByListSidAndPeriod(Optional.empty(), Arrays.asList(employeeId), dateMax).stream()
					.filter(x -> x.getEmployeeId() != null).collect(Collectors.toList());

			if (statusOfEmps.isEmpty())
			    continue;
			// Output「社員の会社所属状況．所属状況．期間」のMAX期間を求める
			DatePeriod dateEmpExport = CommonProcess.getMaxPeriod(statusOfEmps.get(0).getListPeriod());
			// ドメインモデル「日の本人確認」を取得する
			List<Identification> indentities = identificationRepository.findByEmployeeID(employeeId,
					dateEmpExport.start(), dateEmpExport.end());

			// 対応するImported「（就業．勤務実績）承認対象者の承認状況」をすべて取得する
			List<ApprovalProgressDaily> lstApprovalDayStatus = approvalStatusDailyAdapter
					.getProgress(Arrays.asList(employeeId), dateEmpExport);

			// [No.26]社員、期間に一致する申請を取得する
			List<ApplicationRecordImport> lstApplication = applicationRecordAdapter
					.getApplicationBySID(Arrays.asList(employeeId), dateEmpExport.start(), dateEmpExport.end());

			// [No.303]対象期間に日別実績のエラーが発生している年月日を取得する
			List<EmployeeDateErrorOuput> lstOut = realityStatusService
					.checkEmployeeErrorOnProcessingDate(employeeId, dateEmpExport.start(), dateEmpExport.end()).stream()
					.map(x -> {
						return new EmployeeDateErrorOuput(employeeId, x.getDate(), x.getHasError());
					}).collect(Collectors.toList());

			// Output「締め処理期間．実締め毎集計期間」の件数ループする
			List<AggrPeriodEachActualClosure> aggrPeriods = lstClosure.stream()
					.flatMap(x -> x.getAggrPeriods().stream()).collect(Collectors.toList());
			List<InformationMonth> inforMonths = new ArrayList<>();
			for (AggrPeriodEachActualClosure mergePeriodClr : aggrPeriods) {

				// ドメインモデル「月の本人確認」を取得する
				List<ConfirmationMonth> lstConfirmMonth = confirmationMonthRepository.findBySomeProperty(
						Arrays.asList(employeeId), mergePeriodClr.getClosureMonth());

				// 対応するImported「（就業．勤務実績）承認対象者の承認状況」をすべて取得する
				List<ApprovalProgressMonthly> lstApprovalMonthStatus = approvalStatusMonthlyAdapter
						.getProgress(Arrays.asList(employeeId), mergePeriodClr.getClosureMonth(), mergePeriodClr.getPeriod());
				inforMonths.add(new InformationMonth(mergePeriodClr, lstConfirmMonth, lstApprovalMonthStatus));
			}

			ConfirmInfoResult result = ConfirmInfoResult.builder().employeeId(employeeId).period(dateMax)
					.informationDay(new InformationDay(indentities, lstApprovalDayStatus))
					.lstApplication(lstApplication).lstOut(lstOut).statusOfEmp(statusOfEmps.get(0))
					.informationMonths(inforMonths).build();
			results.add(result);

		}
		return results;
	}
}
