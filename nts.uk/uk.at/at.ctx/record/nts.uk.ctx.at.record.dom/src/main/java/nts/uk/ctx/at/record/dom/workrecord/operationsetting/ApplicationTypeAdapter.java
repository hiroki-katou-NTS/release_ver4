package nts.uk.ctx.at.record.dom.workrecord.operationsetting;

import java.util.List;
import java.util.Map;

import nts.uk.shr.com.time.calendar.period.DatePeriod;

public interface ApplicationTypeAdapter {
	
	List<AppWithDetailExporAdp> getAppWithOvertimeInfo(String companyID);
	
	Map<String, List<DatePeriod>> getListAffComHistByListSidAndPeriod(List<String> sid, DatePeriod datePeriod);
}
