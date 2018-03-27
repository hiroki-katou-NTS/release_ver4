package nts.uk.ctx.at.record.dom.workinformation.repository;

import java.util.List;
import java.util.Optional;

import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.record.dom.workinformation.WorkInfoOfDailyPerformance;
import nts.uk.shr.com.time.calendar.period.DatePeriod;

public interface WorkInformationRepository {
	
	/** Find an Employee WorkInformation of Daily Performance */
	Optional<WorkInfoOfDailyPerformance> find(String employeeId, GeneralDate ymd);
	
	List<WorkInfoOfDailyPerformance> findByPeriodOrderByYmd(String employeeId, DatePeriod datePeriod);
	
	List<WorkInfoOfDailyPerformance> findByPeriodOrderByYmdDesc(String employeeId, DatePeriod datePeriod);
	
	List<WorkInfoOfDailyPerformance> findByListEmployeeId(List<String> employeeIds, DatePeriod ymds);
	
	void delete(String employeeId, GeneralDate ymd);
	
	void updateByKey(WorkInfoOfDailyPerformance workInfoOfDailyPerformance);
	
	void insert(WorkInfoOfDailyPerformance workInfoOfDailyPerformance);
	
	void updateByKeyFlush(WorkInfoOfDailyPerformance workInfoOfDailyPerformance);
}