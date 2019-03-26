package nts.uk.ctx.at.record.dom.adapter.employment;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import nts.arc.time.GeneralDate;
import nts.uk.shr.com.time.calendar.period.DatePeriod;

public interface SyEmploymentAdapter {

	Optional<SyEmploymentImport> findByEmployeeId (String companyId, String employeeId, GeneralDate baseDate);
	
	Map<String, List<SyEmploymentImport>> finds(List<String> employeeId, DatePeriod baseDate);
	
	Map<String, String> getEmploymentMapCodeName(String companyId, List<String> empCodes);
}
