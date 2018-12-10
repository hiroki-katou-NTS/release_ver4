package nts.uk.ctx.at.record.pubimp.remainnumber.reserveleave;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

import lombok.val;
import nts.arc.task.parallel.ManagedParallelWithContext;
import nts.arc.time.GeneralDate;
import nts.arc.time.YearMonth;
import nts.gul.util.value.MutableValue;
import nts.uk.ctx.at.record.dom.monthlycommon.aggrperiod.ClosurePeriod;
import nts.uk.ctx.at.record.dom.monthlycommon.aggrperiod.GetClosurePeriod;
import nts.uk.ctx.at.record.dom.remainingnumber.annualleave.export.GetAnnAndRsvRemNumWithinPeriod;
import nts.uk.ctx.at.record.dom.remainingnumber.annualleave.export.InterimRemainMngMode;
import nts.uk.ctx.at.record.dom.remainingnumber.annualleave.export.param.AggrResultOfAnnAndRsvLeave;
import nts.uk.ctx.at.record.dom.remainingnumber.annualleave.export.param.AggrResultOfAnnualLeave;
import nts.uk.ctx.at.record.dom.remainingnumber.reserveleave.export.param.AggrResultOfReserveLeave;
import nts.uk.ctx.at.record.pub.remainnumber.reserveleave.GetRsvLeaNumAfterCurrentMon;
import nts.uk.ctx.at.record.pub.remainnumber.reserveleave.RsvLeaUsedCurrentMonExport;
import nts.uk.ctx.at.shared.dom.workrule.closure.service.ClosureService;
import nts.uk.shr.com.time.calendar.period.DatePeriod;
import nts.uk.shr.com.time.calendar.period.YearMonthPeriod;

/**
 * 実装：当月以降の積立年休使用数・残数を取得する
 * @author shuichi_ishida
 */
@Stateless
public class GetRsvLeaNumAfterCurrentMonImpl implements GetRsvLeaNumAfterCurrentMon {

	/** 当月の期間を算出する */
	@Inject
	private ClosureService closureService;
	/** 集計期間を取得する */
	@Inject
	private GetClosurePeriod getClosurePeriod;
	/** 期間中の年休積休残数を取得 */
	@Inject
	private GetAnnAndRsvRemNumWithinPeriod getAnnAndRsvRemNumWithinPeriod;
	@Inject
	private ManagedParallelWithContext parallel;
	
	/** 当月以降の積立年休使用数・残数を取得する */
	@Override
	public List<RsvLeaUsedCurrentMonExport> algorithm(String employeeId, YearMonthPeriod period) {
		// 社員に対応する処理締めを取得する
		val closure = this.closureService.getClosureDataByEmployee(employeeId, GeneralDate.today());
		if (closure == null) return new ArrayList<>();
		
		// 指定した年月の期間をすべて取得する
		val endYMPeriods = closure.getPeriodByYearMonth(period.end());
		List<ClosurePeriod> aggrTmp = Collections.synchronizedList(new ArrayList<>());
		parallel.forEach(endYMPeriods, endYMPeriod -> {
			// 集計期間を取得する
			aggrTmp.addAll(this.getClosurePeriod.get(closure.getCompanyId().v(), employeeId,
					endYMPeriod.end(), Optional.empty(), Optional.empty(), Optional.empty()));
		});
		List<ClosurePeriod> aggrPeriods = new ArrayList<>();
		aggrPeriods.addAll(aggrTmp);
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
		MutableValue<AggrResultOfAnnualLeave> prevAnnualLeave = new MutableValue<>();
		MutableValue<AggrResultOfReserveLeave> prevReserveLeave = new MutableValue<>();
		List<RsvLeaUsedCurrentMonExport> tmp = Collections.synchronizedList(new ArrayList<>());
		parallel.forEach (keys, key -> {
			val closurePeriod = closurePeriods.get(key);
			// 期間中の年休積休残数を取得
			AggrResultOfAnnAndRsvLeave aggrResult = this.getAnnAndRsvRemNumWithinPeriod.algorithm(
					closure.getCompanyId().v(),
					employeeId,
					closurePeriod,
					InterimRemainMngMode.OTHER,
					closurePeriod.end(),
					false,
					false,
					Optional.empty(),
					Optional.empty(),
					Optional.empty(),
					Optional.empty(),
					Optional.empty(),
					prevAnnualLeave.optional(),
					prevReserveLeave.optional());
			prevAnnualLeave.set(aggrResult.getAnnualLeave().isPresent() ? aggrResult.getAnnualLeave().get() : null);
			prevReserveLeave.set(aggrResult.getReserveLeave().isPresent() ? aggrResult.getReserveLeave().get() : null);
			
			// 結果をListに追加
			val aggrResultOfReserveOpt = aggrResult.getReserveLeave();
			if (aggrResultOfReserveOpt.isPresent()){
				val aggrResultOfReserve = aggrResultOfReserveOpt.get();
				val withMinus =
						aggrResultOfReserve.getAsOfPeriodEnd().getRemainingNumber().getReserveLeaveWithMinus();
				
				tmp.add(new RsvLeaUsedCurrentMonExport(
						key,
						withMinus.getUsedNumber().getUsedDays(),
						withMinus.getRemainingNumber().getTotalRemainingDays()));
			}
		});
		List<RsvLeaUsedCurrentMonExport> results = new ArrayList<>();
		results.addAll(tmp);
		// 年月毎積立年休の集計結果を返す
		return results;
	}
}
