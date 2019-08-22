package nts.uk.screen.at.app.dailyperformance.correction.identitymonth;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.record.dom.adapter.company.AffComHistItemImport;
import nts.uk.ctx.at.record.dom.adapter.company.AffCompanyHistImport;
import nts.uk.ctx.at.record.dom.adapter.company.SyCompanyRecordAdapter;
import nts.uk.ctx.at.record.dom.monthlycommon.aggrperiod.AggrPeriodEachActualClosure;
import nts.uk.ctx.at.record.dom.monthlycommon.aggrperiod.ClosurePeriod;
import nts.uk.ctx.at.record.dom.monthlycommon.aggrperiod.GetClosurePeriod;
import nts.uk.ctx.at.record.dom.workrecord.actualsituation.identificationstatus.export.CheckIndentityDayConfirm;
import nts.uk.ctx.at.record.dom.workrecord.identificationstatus.month.ConfirmationMonth;
import nts.uk.ctx.at.record.dom.workrecord.identificationstatus.repository.ConfirmationMonthRepository;
import nts.uk.ctx.at.shared.pub.workrule.closure.PresentClosingPeriodExport;
import nts.uk.ctx.at.shared.pub.workrule.closure.ShClosurePub;
import nts.uk.shr.com.context.AppContexts;
import nts.uk.shr.com.time.calendar.period.DatePeriod;

/**
 * @author thanhnx
 *
 *
 */
@Stateless
public class CheckIndentityMonth {

	@Inject
	private GetClosurePeriod getClosurePeriod;

	@Inject
	private ConfirmationMonthRepository confirmationMonthRepository;

	@Inject
	private SyCompanyRecordAdapter syCompanyRecordAdapter;

	@Inject
	private CheckIndentityDayConfirm checkIndentityDayConfirm;
	
	@Inject
	private ShClosurePub shClosurePub;

	// 月の本人確認をチェックする
	public IndentityMonthResult checkIndenityMonth(IndentityMonthParam param) {
		String empLogin = AppContexts.user().employeeId();
		if (!empLogin.equals(param.getEmployeeId()) || param.getDisplayFormat() != 0
				|| !param.getIndentityUseSetOpt().isPresent()
				|| !param.getIndentityUseSetOpt().get().isUseIdentityOfMonth()) {
			return new IndentityMonthResult(false, false, true);
		}

		Optional<PresentClosingPeriodExport> clsPeriodOpt = shClosurePub.find(param.getCompanyId(), param.getClosureId());
		if (!clsPeriodOpt.isPresent()
				|| clsPeriodOpt.get().getClosureStartDate().after(param.getDateRefer())
				|| clsPeriodOpt.get().getClosureEndDate().before(param.getDateRefer())) {
			return new IndentityMonthResult(false, false, true);
		}
		

		// 集計期間
		List<ClosurePeriod> closurePeriods = getClosurePeriod.get(param.companyId, param.employeeId, clsPeriodOpt.get().getClosureEndDate(),
				Optional.empty(), Optional.empty(), Optional.empty());
		//closurePeriods  = closurePeriods
		if (closurePeriods.isEmpty())
			return new IndentityMonthResult(false, false, false);

//		Pair<DatePeriod, ClosurePeriod> pairClosure = getDatePeriodOldest(closurePeriods);
//		ClosurePeriod closurePeriodOldest = pairClosure.getRight();
		//「対象締め」に対応する締め期間が取得できているかチェックする
		AggrPeriodEachActualClosure closurePeriodOldest = closurePeriods.stream().flatMap(x -> x.getAggrPeriods().stream())
				.filter(x -> x.getClosureId().value == param.getClosureId()).findFirst()
				.orElse(null);
		DatePeriod datePeriodOldest = closurePeriodOldest == null ? null : closurePeriodOldest.getPeriod();
		if(datePeriodOldest == null) {
			return new IndentityMonthResult(false, false, true);
		}
		// 月の本人確認を取得する
		Optional<ConfirmationMonth> optCMonth = confirmationMonthRepository.findByKey(param.companyId, param.employeeId,
				closurePeriodOldest.getClosureId(),
				closurePeriodOldest.getClosureDate(),
				closurePeriodOldest.getYearMonth());
		// 取得できたかチェックする
		if (optCMonth.isPresent()) {
			// A2_6 hide, A2_7 show
			return new IndentityMonthResult(false, true, false);
		}

		DatePeriod period = datePeriodOldest;
		// 社員ID（List）と指定期間から所属会社履歴項目を取得
		List<AffCompanyHistImport> affCompanyHis = syCompanyRecordAdapter
				.getAffCompanyHistByEmployee(Arrays.asList(param.employeeId), period);

		List<DatePeriod> datePeriods = new ArrayList<>();
		if (!affCompanyHis.isEmpty()) {
			for (AffComHistItemImport his : affCompanyHis.get(0).getLstAffComHistItem()) {
				DatePeriod datePeriodHis = his.getDatePeriod();
				GeneralDate endDateB = datePeriodHis.end();
				GeneralDate startDateA = datePeriodHis.start();
				// 「所属会社履歴項目．退職日」がパラメータ「当月の期間」に含まれているかチェックする
				if (endDateB != null
						&& (endDateB.afterOrEquals(period.start()) && startDateA.beforeOrEquals(period.end()))) {
					datePeriods
							.add(new DatePeriod(period.start().afterOrEquals(startDateA) ? period.start() : startDateA,
									period.end().beforeOrEquals(endDateB) ? period.end() : endDateB));
				}
			}
		}
		if (datePeriods.isEmpty())
			return new IndentityMonthResult(true, true, false);
		List<GeneralDate> lstDate = datePeriods.stream().flatMap(x -> x.datesBetween().stream())
				.collect(Collectors.toList());

		boolean checkIndentityDay = checkIndentityDayConfirm.checkIndentityDay(param.employeeId, lstDate);
		// A2_6 show, A2_7 hide
		return new IndentityMonthResult(true, checkIndentityDay, false);
	}
	
}
