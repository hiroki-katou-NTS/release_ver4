package nts.uk.ctx.at.shared.dom.scherec.monthlyattdcal.monthly.information.care;

import java.util.List;
import java.util.Optional;

import nts.arc.time.YearMonth;
import nts.uk.ctx.at.shared.dom.workrule.closure.ClosureId;
import nts.uk.shr.com.time.calendar.date.ClosureDate;

public interface MonCareHdRemainRepository {
	
	/**
	 * Find by YearMonth, ClosureId & ClosureDate
	 * @param employeeId
	 * @param yearMonth
	 * @param closureId
	 * @param closureDate
	 * @return
	 */
	Optional<MonCareHdRemain> find(String employeeId, YearMonth yearMonth,
			ClosureId closureId, ClosureDate closureDate);
	
	/**
	 * Find by YearMonth
	 * @param employeeId
	 * @param yearMonth
	 * @return
	 */
	List<MonCareHdRemain> findByYearMonthOrderByStartYmd(String employeeId, YearMonth yearMonth);
	
	/**
	 * 
	 * @param employeeId
	 * @param yearMonth
	 * @param closureId
	 * @return
	 */
	List<MonCareHdRemain> findByYMAndClosureIdOrderByStartYmd(
			String employeeId, YearMonth yearMonth, ClosureId closureId);
	
	/**
	 * 
	 * @param employeeIds
	 * @param yearMonth
	 * @param closureId
	 * @param closureDate
	 * @return
	 */
	List<MonCareHdRemain> findByEmployees(List<String> employeeIds, YearMonth yearMonth,
			ClosureId closureId, ClosureDate closureDate);

	/**
	 * 
	 * @param employeeIds
	 * @param yearMonths
	 * @return
	 */
	List<MonCareHdRemain> findBySidsAndYearMonths(List<String> employeeIds, List<YearMonth> yearMonths);

	/**
	 * 
	 * @param attendanceTimeOfMonthly
	 */
	void persistAndUpdate(MonCareHdRemain remarksMonthlyRecord);

	/**
	 * 
	 * @param employeeId
	 * @param yearMonth
	 * @param closureId
	 * @param closureDate
	 */
	void remove(String employeeId, YearMonth yearMonth, ClosureId closureId, ClosureDate closureDate);

	/**
	 * 
	 * @param employeeId
	 * @param yearMonth
	 */
	void removeByYearMonth(String employeeId, YearMonth yearMonth);
}
