package nts.uk.ctx.at.record.dom.breakorgoout.repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.record.dom.breakorgoout.OutingTimeOfDailyPerformance;
import nts.uk.ctx.at.record.dom.breakorgoout.primitivevalue.OutingFrameNo;
import nts.uk.shr.com.time.calendar.period.DatePeriod;

public interface OutingTimeOfDailyPerformanceRepository {

	void delete(String employeeId, GeneralDate ymd);
	
	Optional<OutingTimeOfDailyPerformance> findByEmployeeIdAndDate(String employeeId, GeneralDate ymd);
	
	List<OutingTimeOfDailyPerformance> finds(List<String> employeeId, DatePeriod ymd);
	
	List<OutingTimeOfDailyPerformance> finds(Map<String, List<GeneralDate>> param);
	
	void add(OutingTimeOfDailyPerformance outing);
	
	void update(OutingTimeOfDailyPerformance outingTimeOfDailyPerformance);
	
	boolean checkExistData(String employeeId, GeneralDate ymd, OutingFrameNo outingFrameNo);

}
