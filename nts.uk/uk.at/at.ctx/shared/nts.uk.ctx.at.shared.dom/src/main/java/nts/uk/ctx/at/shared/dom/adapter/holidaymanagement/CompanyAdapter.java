package nts.uk.ctx.at.shared.dom.adapter.holidaymanagement;

import nts.arc.time.YearMonth;

/**
 * The Interface CompanyAdapter.
 */
public interface CompanyAdapter {
	
	/**
	 * Gets the first month.
	 *
	 * @param companyId the company id
	 * @return the first month
	 */
	CompanyDto getFirstMonth(String companyId);
	
	/**
	 * 暦上の年月を渡して、年度に沿った年月を取得する
	 * @param companyId 会社ID
	 * @param yearMonth 年月
	 * @return 年月
	 */
	// RequestList557
	YearMonth getYearMonthFromCalenderYM(String companyId, YearMonth yearMonth);
}
