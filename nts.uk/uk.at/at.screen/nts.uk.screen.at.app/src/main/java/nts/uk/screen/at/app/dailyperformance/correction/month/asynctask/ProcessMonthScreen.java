package nts.uk.screen.at.app.dailyperformance.correction.month.asynctask;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.shared.dom.workrule.closure.service.ClosureService;
import nts.uk.screen.at.app.dailyperformance.correction.DailyPerformanceCorrectionProcessor;
import nts.uk.screen.at.app.dailyperformance.correction.DailyPerformanceScreenRepo;
import nts.uk.screen.at.app.dailyperformance.correction.dto.DailyPerformanceCorrectionDto;
import nts.uk.screen.at.app.dailyperformance.correction.dto.OperationOfDailyPerformanceDto;
import nts.uk.screen.at.app.dailyperformance.correction.identitymonth.CheckIndentityMonth;
import nts.uk.screen.at.app.dailyperformance.correction.identitymonth.IndentityMonthParam;
import nts.uk.screen.at.app.dailyperformance.correction.identitymonth.IndentityMonthResult;
import nts.uk.screen.at.app.dailyperformance.correction.monthflex.DPMonthFlexParam;
import nts.uk.screen.at.app.dailyperformance.correction.monthflex.DPMonthFlexProcessor;
import nts.uk.shr.com.context.AppContexts;
import nts.uk.shr.com.time.calendar.period.DatePeriod;

@Stateless
public class ProcessMonthScreen {

	@Inject
	private DailyPerformanceScreenRepo repo;

	@Inject
	private DPMonthFlexProcessor monthFlexProcessor;

	@Inject
	private DailyPerformanceCorrectionProcessor processor;

	@Inject
	private ClosureService closureService;

	@Inject
	private CheckIndentityMonth checkIndentityMonth;

	public DailyPerformanceCorrectionDto processMonth(ParamCommonAsync param) {
		DailyPerformanceCorrectionDto screenDto = new DailyPerformanceCorrectionDto();
		System.out.println("start month");
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
			if (param.employeeTarget.equals(sId)) {
				// 社員に対応する締め期間を取得する
				DatePeriod period = closureService.findClosurePeriod(param.employeeTarget, param.dateRange.getEndDate());

				// パラメータ「日別実績の修正の状態．対象期間．終了日」がパラメータ「締め期間」に含まれているかチェックする
				if (period == null || !period.contains(param.dateRange.getEndDate())) {
					screenDto.setIndentityMonthResult(new IndentityMonthResult(false, true, true));
					// 対象日の本人確認が済んでいるかチェックする
					// screenDto.checkShowTighProcess(displayFormat, true);
				} else {
					// checkIndenityMonth
					screenDto.setIndentityMonthResult(checkIndentityMonth
							.checkIndenityMonth(new IndentityMonthParam(companyId, sId, GeneralDate.today(), param.getClosureId())));
					// 対象日の本人確認が済んでいるかチェックする
					screenDto.checkShowTighProcess(param.displayFormat, true);
				}
			} else {
				screenDto.getIndentityMonthResult().setHideAll(true);
			}
			// screenDto.setFlexShortage(null);
		}
		System.out.println("end month"+ (System.currentTimeMillis() - startTime));
		return screenDto;
	}
}
