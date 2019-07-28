package nts.uk.ctx.at.shared.dom.workrule.closure.service;

import java.util.List;

import nts.arc.time.YearMonth;
import nts.uk.shr.com.time.calendar.period.DatePeriod;

/**
 * 締め履歴を取得する
 * @author shuichi_ishida
 */
public interface GetClosureIdHistory {

	/**
	 * 指定した年月の社員の締め履歴を取得する
	 * @param employeeId 対象社員
	 * @param yearMonth 対象年月
	 * @return 締めID履歴リスト
	 */
	List<ClosureIdHistory> ofEmployeeFromYearMonth(String employeeId, YearMonth yearMonth);
	
	/**
	 * 指定した期間の社員の締め履歴を取得する
	 * @param employeeId 対象社員
	 * @param period 対象期間
	 * @return 締めID履歴リスト
	 */
	List<ClosureIdHistory> ofEmployeeFromPeriod(String employeeId, DatePeriod period);
}
