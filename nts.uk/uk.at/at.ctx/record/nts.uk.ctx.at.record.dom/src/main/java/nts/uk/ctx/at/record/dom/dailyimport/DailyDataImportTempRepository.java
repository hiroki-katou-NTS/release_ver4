package nts.uk.ctx.at.record.dom.dailyimport;

import java.util.Collection;
import java.util.List;

import nts.uk.shr.com.time.calendar.period.DatePeriod;

public interface DailyDataImportTempRepository {

	List<DailyDataImportTemp> getDataImport(DatePeriod period, String companyCode);
	
	List<String> getTargetEmpCode(DatePeriod period, String companyCode);
	
	List<DailyDataImportTemp> getDataImport(DatePeriod period, String companyCode, Collection<String> empCode);
}
