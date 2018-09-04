package nts.uk.ctx.at.record.dom.monthly.performance;

import java.util.List;

import nts.arc.time.YearMonth;
import nts.uk.ctx.at.shared.dom.workrule.closure.ClosureDate;
import nts.uk.ctx.at.shared.dom.workrule.closure.ClosureId;

/**
 * リポジトリ：月別実績の編集状態
 * @author shuichu_ishida
 */
public interface EditStateOfMonthlyPerRepository {

	/**
	 * 検索　（締め）
	 * @param employeeId 社員ID
	 * @param yearMonth 年月
	 * @param closureId 締めID
	 * @param closureDate 締め日
	 * @return 月別実績の編集状態リスト
	 */
	List<EditStateOfMonthlyPerformance> findByClosure(
			String employeeId, YearMonth yearMonth, ClosureId closureId, ClosureDate closureDate);
}
