package nts.uk.ctx.at.function.dom.adapter.employment;

import java.util.List;
import java.util.Optional;

import nts.arc.time.GeneralDate;
import nts.uk.shr.com.time.calendar.period.DatePeriod;

public interface EmploymentAdapter {
	String getClosure (String employeeId, GeneralDate baseDate);
	/**
	 * Find by date.
	 *
	 * @param companyId the company id
	 * @param employeeId the employee id
	 * @param baseDate the base date
	 * @return the optional
	 */
	Optional<EmploymentHistoryImported> getEmpHistBySid(String companyId, String employeeId, GeneralDate baseDate);
	
	List<String> getEmploymentBySidsAndEmploymentCds(List<String> sids, List<String> employmentCodes, DatePeriod dateRange);
}
