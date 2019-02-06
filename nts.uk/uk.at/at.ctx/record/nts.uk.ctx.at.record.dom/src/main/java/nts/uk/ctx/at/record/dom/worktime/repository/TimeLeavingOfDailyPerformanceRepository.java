package nts.uk.ctx.at.record.dom.worktime.repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.record.dom.worktime.TimeLeavingOfDailyPerformance;
import nts.uk.shr.com.time.calendar.period.DatePeriod;

public interface TimeLeavingOfDailyPerformanceRepository {
	
	void delete(String employeeId, GeneralDate ymd);
	
	Optional<TimeLeavingOfDailyPerformance> findByKey(String employeeId, GeneralDate ymd);

	List<TimeLeavingOfDailyPerformance> findbyPeriodOrderByYmd(String employeeId, DatePeriod datePeriod);
	
	List<TimeLeavingOfDailyPerformance> finds(List<String> employeeIds, DatePeriod ymd);
	
	List<TimeLeavingOfDailyPerformance> finds(Map<String, List<GeneralDate>> param);
	
	void add(TimeLeavingOfDailyPerformance timeLeaving);
	
	void update(TimeLeavingOfDailyPerformance timeLeavingOfDailyPerformance);
	
	void update(List<TimeLeavingOfDailyPerformance> domains);

	void insert(TimeLeavingOfDailyPerformance timeLeavingOfDailyPerformance);

	void updateFlush(TimeLeavingOfDailyPerformance timeLeavingOfDailyPerformance);
	
	void deleteTimeNoBy(List<String> employeeIds, List<GeneralDate> processingYmds, int no);
	
	void deleteScheStamp(List<String> employeeIds, List<GeneralDate> processingYmds);
}
