package nts.uk.ctx.at.record.dom.adapter.employment;

import java.util.List;

import nts.uk.shr.com.time.calendar.period.DatePeriod;

/**
 * アダプタ：所属雇用履歴
 * @author shuichu_ishida
 */
public interface EmploymentHistAdapter {

	/**
	 * 所属雇用履歴を取得する
	 * @param employeeId 社員ID
	 * @return 所属雇用履歴リスト
	 */
	// RequestList326
	List<EmploymentHistImport> findByEmployeeIdOrderByStartDate(String employeeId);
	
	/**
	 * 社員（List）と期間から雇用履歴項目を取得する
	 * @param list employeeId
	 * @param period
	 * @return
	 */
	List<EmploymentHistImport> findBySidDatePeriod(List<String> employeeIds, DatePeriod period);
}
