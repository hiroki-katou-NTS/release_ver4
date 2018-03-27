package nts.uk.ctx.at.record.dom.breakorgoout.repository;

import java.util.List;
import java.util.Optional;

import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.record.dom.breakorgoout.BreakTimeOfDailyPerformance;
import nts.uk.shr.com.time.calendar.period.DatePeriod;

public interface BreakTimeOfDailyPerformanceRepository {

	void delete(String employeeId, GeneralDate ymd);
	
	void deleteByListEmployeeId(List<String> employeeIds, List<GeneralDate> ymds);
	
	List<BreakTimeOfDailyPerformance> findByKey(String employeeId, GeneralDate ymd);
	
	List<BreakTimeOfDailyPerformance> finds(List<String> employeeId, DatePeriod ymd);

	void insert(BreakTimeOfDailyPerformance breakTimes);
	
	void insert(List<BreakTimeOfDailyPerformance> breakTimes);
	
	void update(BreakTimeOfDailyPerformance breakTimes);
	
	void update(List<BreakTimeOfDailyPerformance> breakTimes);
	
	Optional<BreakTimeOfDailyPerformance> find(String employeeId, GeneralDate ymd, int breakType);
}
