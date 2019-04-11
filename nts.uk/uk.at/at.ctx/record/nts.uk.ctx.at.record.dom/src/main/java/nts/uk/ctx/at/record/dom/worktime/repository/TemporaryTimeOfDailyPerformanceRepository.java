package nts.uk.ctx.at.record.dom.worktime.repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.record.dom.worktime.TemporaryTimeOfDailyPerformance;
import nts.uk.shr.com.time.calendar.period.DatePeriod;

public interface TemporaryTimeOfDailyPerformanceRepository {

	void delete(String employeeId, GeneralDate ymd);
	
	Optional<TemporaryTimeOfDailyPerformance> findByKey(String employeeId, GeneralDate ymd);

	List<TemporaryTimeOfDailyPerformance> findbyPeriodOrderByYmd(String employeeId, DatePeriod datePeriod);

	List<TemporaryTimeOfDailyPerformance> finds(List<String> employeeId, DatePeriod ymd);

	List<TemporaryTimeOfDailyPerformance> finds(Map<String, List<GeneralDate>> param);

	void add(TemporaryTimeOfDailyPerformance temporaryTime);

	void update(TemporaryTimeOfDailyPerformance temporaryTimeOfDailyPerformance);

	void insert(TemporaryTimeOfDailyPerformance temporaryTimeOfDailyPerformance);

}
