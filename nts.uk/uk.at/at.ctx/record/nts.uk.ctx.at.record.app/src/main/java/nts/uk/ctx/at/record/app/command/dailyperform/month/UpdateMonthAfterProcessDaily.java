package nts.uk.ctx.at.record.app.command.dailyperform.month;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

import org.apache.commons.lang3.tuple.Pair;

import lombok.val;
import nts.arc.time.GeneralDate;
import nts.arc.time.YearMonth;
import nts.uk.ctx.at.record.app.command.dailyperform.DailyRecordWorkCommand;
import nts.uk.ctx.at.record.app.service.dailycheck.CheckCalcMonthService;
import nts.uk.ctx.at.record.dom.dailyprocess.calc.IntegrationOfDaily;
import nts.uk.ctx.at.record.dom.monthlyprocess.aggr.IntegrationOfMonthly;
import nts.uk.ctx.at.record.dom.monthlyprocess.aggr.export.AggregateSpecifiedDailys;
import nts.uk.ctx.at.shared.dom.workrule.closure.ClosureId;
import nts.uk.shr.com.context.AppContexts;

/**
 * @author thanhnx
 * 
 */
@Stateless
public class UpdateMonthAfterProcessDaily {

	@Inject
	private CheckCalcMonthService checkCalcMonthService;

	@Inject
	private AggregateSpecifiedDailys aggregateSpecifiedDailys;
	
	public List<IntegrationOfMonthly> updateMonth(List<DailyRecordWorkCommand> commandNew,
			List<IntegrationOfDaily> domainDailyNew, Optional<IntegrationOfMonthly> monthlyWork, UpdateMonthDailyParam month) {
		String companyId = AppContexts.user().companyId();
		List<IntegrationOfMonthly> result = new ArrayList<>();
		if (!monthlyWork.isPresent()){
		List<Pair<String, GeneralDate>> lstDaily = commandNew.stream()
				.map(x -> Pair.of(x.getEmployeeId(), x.getWorkDate())).collect(Collectors.toList());

		Map<String, List<GeneralDate>> groupByEmp = lstDaily.stream()
				.collect(Collectors.groupingBy(x -> x.getLeft(), Collectors.collectingAndThen(Collectors.toList(),
						x -> x.stream().map(y -> y.getRight()).collect(Collectors.toList()))));
		groupByEmp.forEach((key, value) -> {
			//月次集計を実施する必要があるかチェックする
			val needCalc = checkCalcMonthService.isNeedCalcMonth(companyId, key, value);
			if (needCalc.getLeft()) {
				List<IntegrationOfDaily> domainDailyGroupEmp = domainDailyNew.stream()
						.filter(x -> x.getWorkInformation().getEmployeeId().equals(key)).collect(Collectors.toList());
				needCalc.getRight().forEach(data -> {
					//月の実績を集計する
					Optional<IntegrationOfMonthly> monthDomainOpt = aggregateSpecifiedDailys.algorithm(companyId, key,
							data.getYearMonth(), data.getClosureId(), data.getClosureDate(), data.getPeriod(), Optional.empty(), domainDailyGroupEmp,
							monthlyWork);
					if (monthDomainOpt.isPresent())
						result.add(monthDomainOpt.get());
				});
			}
		});
		}
		// insert domain month
		for(IntegrationOfMonthly monthResult : result){
			if(!monthResult.getEmployeeMonthlyPerErrorList().isEmpty()) return result;
		}
		
		if (!domainDailyNew.isEmpty()) {
			//updateAllDomainMonthService.insertUpdateAll(result);
		} else if (monthlyWork.isPresent()) {
			List<IntegrationOfDaily> domainDailyGroupEmp = commandNew.stream().map(x -> x.toDomain()).collect(Collectors.toList());
			long time = System.currentTimeMillis();
			Optional<IntegrationOfMonthly> monthDomainOpt = aggregateSpecifiedDailys.algorithm(companyId, month.getEmployeeId(),
					new YearMonth(month.getYearMonth()), ClosureId.valueOf(month.getClosureId()), month.getClosureDate().toDomain(), month.getDatePeriod(), Optional.empty(), domainDailyGroupEmp,
					monthlyWork);
			if(monthDomainOpt.isPresent()) result.add(monthDomainOpt.get());
			System.out.println("tg tinh toan thang : "+ (System.currentTimeMillis() - time));
			if(monthDomainOpt.isPresent() && !monthDomainOpt.get().getEmployeeMonthlyPerErrorList().isEmpty()) return result;
			//updateAllDomainMonthService.insertUpdateAll(Arrays.asList(monthDomainOpt.get()));
		}
		
		return result;
	}

}
