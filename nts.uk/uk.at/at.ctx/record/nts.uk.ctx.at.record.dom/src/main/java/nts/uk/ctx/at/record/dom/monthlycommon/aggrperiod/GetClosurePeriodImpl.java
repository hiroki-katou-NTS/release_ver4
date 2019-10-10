package nts.uk.ctx.at.record.dom.monthlycommon.aggrperiod;

import java.util.List;
import java.util.ListIterator;
import java.util.Optional;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;

import nts.arc.time.GeneralDate;
import nts.arc.time.YearMonth;
import nts.uk.ctx.at.record.dom.workrecord.workperfor.dailymonthlyprocessing.enums.ExecutionType;
import nts.uk.ctx.at.shared.dom.workrule.closure.ClosureId;
import nts.uk.ctx.at.shared.dom.workrule.closure.service.ClosureIdHistory;
import nts.uk.ctx.at.shared.dom.workrule.closure.service.GetClosureIdHistory;
import nts.uk.shr.com.time.calendar.period.DatePeriod;

/**
 * 集計期間を取得する
 * @author shuichi_ishida
 */
@Stateless
@TransactionAttribute(TransactionAttributeType.SUPPORTS)
public class GetClosurePeriodImpl implements GetClosurePeriod {

	/** 集計すべき期間を計算 */
	@Inject
	private CalcPeriodForAggregate calcPeriodForAggregate;
	/** 締め履歴を取得する */
	@Inject
	private GetClosureIdHistory getClosureIdHistory;
	
	/** 集計期間を取得する */
	@Override
	public List<ClosurePeriod> get(String companyId, String employeeId, GeneralDate criteriaDate,
			Optional<YearMonth> yearMonthOpt, Optional<ClosureId> closureIdOpt, Optional<ExecutionType> executionTypeOpt) {
		
		// ※　Optinal引数の組み合わせ不足等エラーは、throw new RuntimeError...で。
		//    現時点では、引数の利用が未設計。（2018.3.15 shuichi_ishida）
		
		// 集計すべき期間を計算
		return this.calcPeriodForAggregate.algorithm(employeeId, criteriaDate);
	}
	
	/** 年月を指定して集計期間を求める */
	@Override
	public List<ClosurePeriod> fromYearMonth(String employeeId, GeneralDate criteriaDate, YearMonth yearMonth) {

		// 指定した年月の社員の締め履歴を取得する
		List<ClosureIdHistory> closureIdHistoryList =
				this.getClosureIdHistory.ofEmployeeFromYearMonth(employeeId, yearMonth);
		
		// 締め履歴から集計期間を生成
		List<ClosurePeriod> closurePeriods = this.calcPeriodForAggregate.fromClosureHistory(
				employeeId, criteriaDate, closureIdHistoryList);
		
		// 集計期間から指定した年月以外を削除する
		ListIterator<ClosurePeriod> itrClosurePeriods = closurePeriods.listIterator();
		while (itrClosurePeriods.hasNext()) {
			ClosurePeriod target = itrClosurePeriods.next();
			ListIterator<AggrPeriodEachActualClosure> itrAggrPeriods = target.getAggrPeriods().listIterator();
			while (itrAggrPeriods.hasNext()) {
				AggrPeriodEachActualClosure aggrPeriod = itrAggrPeriods.next();
				if (!aggrPeriod.getYearMonth().equals(yearMonth)) itrAggrPeriods.remove();
			}
			if (target.getAggrPeriods().size() == 0) itrClosurePeriods.remove();
		}
		
		// 締め処理期間リストを返す
		return closurePeriods;
	}
	
	/** 期間を指定して集計期間を求める */
	@Override
	public List<ClosurePeriod> fromPeriod(String employeeId, GeneralDate criteriaDate, DatePeriod period) {

		// 指定した期間の社員の締め履歴を取得する
		List<ClosureIdHistory> closureIdHistoryList =
				this.getClosureIdHistory.ofEmployeeFromPeriod(employeeId, period);
		
		// 締め履歴から集計期間を生成
		return this.calcPeriodForAggregate.fromClosureHistory(employeeId, criteriaDate, closureIdHistoryList);
	}
}
