package nts.uk.ctx.at.record.dom.editstate.repository;

import java.util.List;

import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.record.dom.editstate.EditStateOfDailyPerformance;
import nts.uk.shr.com.time.calendar.period.DatePeriod;

public interface EditStateOfDailyPerformanceRepository {

	void delete(String employeeId, GeneralDate ymd);
	
	void deleteByListEmployeeId(List<String> employeeIds, List<GeneralDate> processingYmds);
	
	void add(List<EditStateOfDailyPerformance> editStates);
	
	List<EditStateOfDailyPerformance> findByKey(String employeeId, GeneralDate ymd);
	
	List<EditStateOfDailyPerformance> finds(List<String> employeeId, DatePeriod ymd);
	
	void updateByKey(List<EditStateOfDailyPerformance> editStates);
}
