package nts.uk.ctx.at.record.dom.worktime.repository;

import java.util.Optional;

import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.record.dom.worktime.TimeLeavingOfDailyPerformance;

public interface TimeLeavingOfDailyPerformanceRepository {
	
	void delete(String employeeId, GeneralDate ymd);
	
	Optional<TimeLeavingOfDailyPerformance> findByKey(String employeeId, GeneralDate ymd);
	
	void add(TimeLeavingOfDailyPerformance timeLeaving);
	
	void update(TimeLeavingOfDailyPerformance timeLeavingOfDailyPerformance);

	void insert(TimeLeavingOfDailyPerformance timeLeavingOfDailyPerformance);


}
