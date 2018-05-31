package nts.uk.ctx.at.record.pubimp.remainnumber.reserveleave;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

import lombok.val;
import nts.arc.time.GeneralDate;
import nts.arc.time.YearMonth;
import nts.uk.ctx.at.record.dom.monthlycommon.aggrperiod.ClosurePeriod;
import nts.uk.ctx.at.record.dom.monthlycommon.aggrperiod.GetClosurePeriod;
import nts.uk.ctx.at.record.dom.remainingnumber.reserveleave.export.param.AggrResultOfReserveLeave;
import nts.uk.ctx.at.record.pub.remainnumber.reserveleave.GetRsvLeaNumAfterCurrentMon;
import nts.uk.ctx.at.record.pub.remainnumber.reserveleave.RsvLeaUsedCurrentMonExport;
import nts.uk.ctx.at.shared.dom.workrule.closure.service.ClosureService;
import nts.uk.shr.com.time.calendar.period.DatePeriod;
import nts.uk.shr.com.time.calendar.period.YearMonthPeriod;

/**
 * 実装：当月以降の積立年休使用数・残数を取得する
 * @author shuichu_ishida
 */
@Stateless
public class GetRsvLeaNumAfterCurrentMonImpl implements GetRsvLeaNumAfterCurrentMon {

	/** 当月の期間を算出する */
	@Inject
	private ClosureService closureService;
	/** 集計期間を取得する */
	@Inject
	private GetClosurePeriod getClosurePeriod;
	/** 期間中の積立年休を取得 */
	//@Inject
	//*****（未）　未実装。2018.5.29 shuichi_ishida
	
	/** 当月以降の積立年休使用数・残数を取得する */
	@Override
	public List<RsvLeaUsedCurrentMonExport> algorithm(String employeeId, YearMonthPeriod period) {
		
		List<RsvLeaUsedCurrentMonExport> results = new ArrayList<>();

		// 社員に対応する処理締めを取得する
		val closure = this.closureService.getClosureDataByEmployee(employeeId, GeneralDate.today());
		if (closure == null) return results;
		
		// 指定した年月の期間をすべて取得する
		List<ClosurePeriod> aggrPeriods = new ArrayList<>();
		val endYMPeriods = closure.getPeriodByYearMonth(period.end());
		for (val endYMPeriod : endYMPeriods){

			// 集計期間を取得する
			aggrPeriods.addAll(this.getClosurePeriod.get(closure.getCompanyId().v(), employeeId,
					endYMPeriod.end(), Optional.empty(), Optional.empty(), Optional.empty()));
		}
		
		// 締め処理期間のうち、同じ年月の期間をまとめる
		Map<YearMonth, DatePeriod> closurePeriods = new HashMap<>();
		for (val aggrPeriod : aggrPeriods){
			YearMonth calcYearMonth = aggrPeriod.getYearMonth();
			for (val detailPeriod : aggrPeriod.getAggrPeriods()){
				DatePeriod calcPeriod = detailPeriod.getPeriod();
				if (closurePeriods.containsKey(calcYearMonth)){
					DatePeriod oldPeriod = closurePeriods.get(calcYearMonth);
					GeneralDate startDate = calcPeriod.start();
					GeneralDate endDate = calcPeriod.end();
					if (startDate.after(oldPeriod.start())) startDate = oldPeriod.start();
					if (endDate.before(oldPeriod.end())) endDate = oldPeriod.end();
					calcPeriod = new DatePeriod(startDate, endDate);
				}
				closurePeriods.put(calcYearMonth, calcPeriod);
			}
		}
		
		List<YearMonth> keys = closurePeriods.keySet().stream().collect(Collectors.toList());
		keys.sort((a, b) -> a.compareTo(b));
		for (val key : keys){
			
			// 期間中の積立年休を取得
			//*****（未）　処理未実装のため、空クラスを仮に返却。2018.5.29 shuichi_ishida
			val aggrResultOfReserveOpt = Optional.of(new AggrResultOfReserveLeave());
			if (aggrResultOfReserveOpt.isPresent()){
				val aggrResultOfReserve = aggrResultOfReserveOpt.get();
				val withMinus =
						aggrResultOfReserve.getAsOfPeriodEnd().getRemainingNumber().getReserveLeaveWithMinus();
				
				results.add(new RsvLeaUsedCurrentMonExport(
						key,
						withMinus.getUsedNumber().getUsedDays(),
						withMinus.getRemainingNumber().getTotalRemainingDays()));
			}
		}
		
		// 年月毎積立年休の集計結果を返す
		return results;
	}
}
