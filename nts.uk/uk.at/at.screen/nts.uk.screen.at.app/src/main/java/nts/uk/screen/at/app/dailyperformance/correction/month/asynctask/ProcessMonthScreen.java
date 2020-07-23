package nts.uk.screen.at.app.dailyperformance.correction.month.asynctask;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;

import nts.gul.collection.CollectionUtil;
import nts.uk.ctx.at.function.app.find.dailyperformanceformat.MonthlyPerfomanceAuthorityFinder;
import nts.uk.screen.at.app.dailyperformance.correction.DailyPerformanceCorrectionProcessor;
import nts.uk.screen.at.app.dailyperformance.correction.DailyPerformanceScreenRepo;
import nts.uk.screen.at.app.dailyperformance.correction.dto.DailyPerformanceCorrectionDto;
import nts.uk.screen.at.app.dailyperformance.correction.dto.OperationOfDailyPerformanceDto;
import nts.uk.screen.at.app.dailyperformance.correction.identitymonth.CheckIndentityMonth;
import nts.uk.screen.at.app.dailyperformance.correction.identitymonth.IndentityMonthParam;
import nts.uk.screen.at.app.dailyperformance.correction.monthflex.DPMonthFlexParam;
import nts.uk.screen.at.app.dailyperformance.correction.monthflex.DPMonthFlexProcessor;
import nts.uk.shr.com.context.AppContexts;

@Stateless
@TransactionAttribute(TransactionAttributeType.SUPPORTS)
public class ProcessMonthScreen {

	@Inject
	private DailyPerformanceScreenRepo repo;

	@Inject
	private DPMonthFlexProcessor monthFlexProcessor;

	@Inject
	private DailyPerformanceCorrectionProcessor processor;

//	@Inject
//	private ClosureService closureService;

	@Inject
	private CheckIndentityMonth checkIndentityMonth;
	
	@Inject
	private MonthlyPerfomanceAuthorityFinder monthlyPerfomanceAuthorityFinder;

	public DailyPerformanceCorrectionDto processMonth(ParamCommonAsync param) {
		DailyPerformanceCorrectionDto screenDto = new DailyPerformanceCorrectionDto();

		long startTime = System.currentTimeMillis();
		String companyId = AppContexts.user().companyId();
		String sId = AppContexts.user().employeeId();
		screenDto.setIdentityProcessDto(param.getIdentityUseSetDto());
		//アルゴリズム「実績修正画面で利用するフォーマットを取得する」を実行する(thực hiện xử lý 「実績修正画面で利用するフォーマットを取得する」)
		OperationOfDailyPerformanceDto dailyPerformanceDto = repo.findOperationOfDailyPerformance();
		if (param.displayFormat == 0) {
			// フレックス情報を表示する
			screenDto
					.setMonthResult(
							monthFlexProcessor.getDPMonthFlex(
									new DPMonthFlexParam(companyId, param.employeeTarget, param.dateRange.getEndDate(),
											param.employeeTarget.equals(sId) ? param.employmentCode
													: processor.getEmploymentCode(companyId,
															param.dateRange.getEndDate(), param.employeeTarget),
											dailyPerformanceDto, param.autBussCode)));
			if(screenDto.getMonthResult() != null && !CollectionUtil.isEmpty(screenDto.getMonthResult().getResults())) {
				List<Integer> items = screenDto.getMonthResult().getResults().get(0).getItems().stream().map(x -> x.getItemId()).collect(Collectors.toList());
				screenDto.getMonthResult().setItemNameMonths(this.monthlyPerfomanceAuthorityFinder.getListAttendanceItemName(items));
			}
			
			screenDto.setIndentityMonthResult(checkIndentityMonth.checkIndenityMonth(
					new IndentityMonthParam(companyId, sId, param.dateRange.getEndDate(), param.getClosureId(),
							param.displayFormat, Optional.ofNullable(screenDto.getIdentityProcessDto()))));
			// 対象日の本人確認が済んでいるかチェックする
			// }
			// screenDto.setFlexShortage(null);
		}

		return screenDto;
	}
}
