package nts.uk.ctx.at.record.dom.monthlycommon.aggrperiod;

import java.util.List;

import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.shared.dom.workrule.closure.service.ClosureIdHistory;

/**
 * 集計すべき期間を計算
 * @author shuichi_ishida
 */
public interface CalcPeriodForAggregate {
	
	/**
	 * 集計すべき期間を計算
	 * @param employeeId 社員ID
	 * @param aggrEnd 集計終了日
	 */
	List<ClosurePeriod> algorithm(String employeeId, GeneralDate aggrEnd);
	
	/**
	 * 締め履歴から集計期間を生成
	 * @param employeeId 社員ID
	 * @param aggrEnd 集計終了日
	 * @param closureIdHistories 締めID履歴リスト
	 */
	List<ClosurePeriod> fromClosureHistory(String employeeId, GeneralDate aggrEnd,
			List<ClosureIdHistory> closureIdHistories);
}
