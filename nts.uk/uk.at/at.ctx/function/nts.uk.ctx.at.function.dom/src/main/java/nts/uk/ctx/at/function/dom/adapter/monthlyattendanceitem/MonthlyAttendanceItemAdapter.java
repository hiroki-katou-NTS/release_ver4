package nts.uk.ctx.at.function.dom.adapter.monthlyattendanceitem;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import nts.uk.shr.com.time.calendar.period.YearMonthPeriod;

public interface MonthlyAttendanceItemAdapter {

	List<MonthlyAttendanceItemFunImport> getMonthlyAttendanceItem(String companyId, List<Integer> dailyAttendanceItemIds);

	/** RequestList421 */
	public List<MonthlyAttendanceResultImport> getMonthlyValueOf(Collection<String> employeeId, YearMonthPeriod range,
			Collection<Integer> itemIds);

	/** RequestList421 */
	public List<MonthlyAttendanceResultImport> getMonthlyValueOf(String employeeId, YearMonthPeriod range,
			Collection<Integer> itemIds);
	
	public List<MonthlyAttendanceResultImport> getMonthlyValueOfParallel(Map<String, YearMonthPeriod> employeeIds, Collection<Integer> itemIds);
}
