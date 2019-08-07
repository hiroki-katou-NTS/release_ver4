package nts.uk.screen.at.app.dailyperformance.correction.loadupdate.onlycheckbox;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

import org.apache.commons.lang3.tuple.Pair;

import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.record.app.find.dailyperform.DailyRecordDto;
import nts.uk.ctx.at.record.dom.dailyperformanceprocessing.confirmationstatus.ApprovalStatusActualResult;
import nts.uk.ctx.at.record.dom.dailyperformanceprocessing.confirmationstatus.ConfirmStatusActualResult;
import nts.uk.ctx.at.record.dom.dailyperformanceprocessing.confirmationstatus.change.approval.ApprovalStatusActualDayChange;
import nts.uk.ctx.at.record.dom.dailyperformanceprocessing.confirmationstatus.change.confirm.ConfirmStatusActualDayChange;
import nts.uk.ctx.at.shared.dom.workrule.closure.service.ClosureService;
import nts.uk.screen.at.app.dailymodify.query.DailyModifyQueryProcessor;
import nts.uk.screen.at.app.dailymodify.query.DailyModifyResult;
import nts.uk.screen.at.app.dailyperformance.correction.GetDataDaily;
import nts.uk.screen.at.app.dailyperformance.correction.dto.ApprovalConfirmCache;
import nts.uk.screen.at.app.dailyperformance.correction.dto.EmpAndDate;
import nts.uk.screen.at.app.dailyperformance.correction.identitymonth.CheckIndentityMonth;
import nts.uk.screen.at.app.dailyperformance.correction.identitymonth.IndentityMonthParam;
import nts.uk.screen.at.app.dailyperformance.correction.identitymonth.IndentityMonthResult;
import nts.uk.shr.com.context.AppContexts;
import nts.uk.shr.com.time.calendar.period.DatePeriod;

@Stateless
public class DPLoadVerProcessor {

	@Inject
	private DailyModifyQueryProcessor dailyModifyQueryProcessor;

	@Inject
	private CheckIndentityMonth checkIndentityMonth;

	@Inject
	private ClosureService closureService;
	
	@Inject
	private ApprovalStatusActualDayChange approvalStatusActualDayChange;
	
	@Inject
	private ConfirmStatusActualDayChange confirmStatusActualDayChange;

	public LoadVerDataResult loadVerAfterCheckbox(LoadVerData loadVerData) {
		LoadVerDataResult result = new LoadVerDataResult();
		List<EmpAndDate> lstDataChange = loadVerData.getLstDataChange();
		String companyId = AppContexts.user().companyId();
		String sId = AppContexts.user().employeeId();
		Map<String, List<GeneralDate>> mapDate = lstDataChange.stream()
				.collect(Collectors.groupingBy(x -> x.getEmployeeId(), Collectors.collectingAndThen(Collectors.toList(),
						list -> list.stream().map(c -> c.getDate()).collect(Collectors.toList()))));

		Pair<List<DailyModifyResult>, List<DailyRecordDto>> resultPair = new GetDataDaily(mapDate,
				dailyModifyQueryProcessor).getDataRow();
		// List<DailyRecordDto> dailyRow = resultPair.getRight();
		Map<Pair<String, GeneralDate>, DailyRecordDto> mapDtoChange = resultPair.getRight().stream()
				.collect(Collectors.toMap(x -> Pair.of(x.getEmployeeId(), x.getDate()), x -> x));
		List<Pair<String, GeneralDate>> lstPairChange = lstDataChange.stream()
				.map(x -> Pair.of(x.getEmployeeId(), x.getDate())).collect(Collectors.toList());
		result.setLstDomainOld(loadVerData.getLstDomainOld().stream().map(x -> {
			return lstPairChange.contains(Pair.of(x.getEmployeeId(), x.getDate()))
					? mapDtoChange.getOrDefault(Pair.of(x.getEmployeeId(), x.getDate()), x)
					: x;
		}).collect(Collectors.toList()));
		List<String> emp = lstDataChange.stream().map(x -> x.getEmployeeId()).distinct().collect(Collectors.toList());
		if (!emp.isEmpty() && emp.get(0).equals(sId) && loadVerData.getDisplayFormat() == 0) {
			// 社員に対応する締め期間を取得する
			DatePeriod period = closureService.findClosurePeriod(sId, loadVerData.getDateRange().getEndDate());

			// パラメータ「日別実績の修正の状態．対象期間．終了日」がパラメータ「締め期間」に含まれているかチェックする
			if (!period.contains(loadVerData.getDateRange().getEndDate())) {
				result.setIndentityMonthResult(new IndentityMonthResult(false, true, true));
				// 対象日の本人確認が済んでいるかチェックする
				// screenDto.checkShowTighProcess(displayFormat, true);
			} else {
				// checkIndenityMonth
				result.setIndentityMonthResult(checkIndentityMonth
						.checkIndenityMonth(new IndentityMonthParam(companyId, sId, GeneralDate.today())));
				// 対象日の本人確認が済んでいるかチェックする
			}
		} else {
			result.setIndentityMonthResult(new IndentityMonthResult(false, false, true));
		}
		
		ApprovalConfirmCache cache = loadVerData.getApprovalConfirmCache();
		List<ConfirmStatusActualResult> confirmResults = confirmStatusActualDayChange.processConfirmStatus(companyId, sId, cache.getEmployeeIds(), Optional.of(cache.getPeriod()), Optional.empty());

		List<ApprovalStatusActualResult> approvalResults = approvalStatusActualDayChange.processApprovalStatus(companyId, sId, cache.getEmployeeIds(), Optional.of(cache.getPeriod()), Optional.empty(), cache.getMode());
        result.setApprovalConfirmCache(new ApprovalConfirmCache(sId,  cache.getEmployeeIds(), cache.getPeriod(), cache.getMode(), confirmResults, approvalResults));
		return result;
	}
}
