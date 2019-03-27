package nts.uk.ctx.at.record.dom.adapter.classification.affiliate;

import java.util.List;
import java.util.Optional;

import nts.arc.time.GeneralDate;
import nts.uk.shr.com.time.calendar.period.DatePeriod;

public interface AffClassificationAdapter {
	
	Optional<AffClassificationSidImport> findByEmployeeId(String companyId, String employeeId, GeneralDate baseDate);
	
	List<AffClassificationSidImport> finds(String companyId, List<String> employeeId, DatePeriod baseDate);
}
