package nts.uk.file.at.app.export.schedule;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import nts.arc.time.GeneralDate;
import nts.uk.shr.com.time.calendar.period.DatePeriod;
import nts.uk.shr.com.time.calendar.period.YearMonthPeriod;

public interface FileService {

	Optional<GeneralDate> getProcessingYM(String cId, int closureId);
	
	Map<String, YearMonthPeriod> getAffiliationPeriod(List<String> listSid , YearMonthPeriod period , GeneralDate baseDate);
	
	Map<String, DatePeriod> getAffiliationDatePeriod(List<String> listSid , YearMonthPeriod period , GeneralDate baseDate);
}
