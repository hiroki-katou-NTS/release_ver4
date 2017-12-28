package nts.uk.ctx.at.record.dom.worktime.repository;

import java.util.List;
import java.util.Optional;

import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.record.dom.worktime.TemporaryTimeOfDailyPerformance;

public interface TemporaryTimeOfDailyPerformanceRepository {

	void delete(String employeeId, GeneralDate ymd);
	
	void deleteByListEmployeeId(List<String> employeeIds, List<GeneralDate> ymds);
	
	Optional<TemporaryTimeOfDailyPerformance> findByKey(String employeeId, GeneralDate ymd);
	
	void add(TemporaryTimeOfDailyPerformance temporaryTime);
	void update(TemporaryTimeOfDailyPerformance temporaryTime);
}
