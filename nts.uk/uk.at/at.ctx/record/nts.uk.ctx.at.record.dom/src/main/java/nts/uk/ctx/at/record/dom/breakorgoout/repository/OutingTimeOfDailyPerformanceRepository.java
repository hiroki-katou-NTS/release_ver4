package nts.uk.ctx.at.record.dom.breakorgoout.repository;

import java.util.List;
import java.util.Optional;

import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.record.dom.breakorgoout.OutingTimeOfDailyPerformance;
import nts.uk.ctx.at.record.dom.breakorgoout.primitivevalue.OutingFrameNo;

public interface OutingTimeOfDailyPerformanceRepository {

	void delete(String employeeId, GeneralDate ymd);
	
	void deleteByListEmployeeId(List<String> employeeIds, List<GeneralDate> ymds);
	
	Optional<OutingTimeOfDailyPerformance> findByEmployeeIdAndDate(String employeeId, GeneralDate ymd);
	
	void add(OutingTimeOfDailyPerformance outing);
	
	void insert(OutingTimeOfDailyPerformance outingTimeOfDailyPerformance);
	
	void update(OutingTimeOfDailyPerformance outingTimeOfDailyPerformance);
	
	boolean checkExistData(String employeeId, GeneralDate ymd, OutingFrameNo outingFrameNo);

}
