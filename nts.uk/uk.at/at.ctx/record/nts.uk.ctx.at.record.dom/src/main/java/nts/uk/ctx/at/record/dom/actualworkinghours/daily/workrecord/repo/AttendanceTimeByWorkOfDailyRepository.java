package nts.uk.ctx.at.record.dom.actualworkinghours.daily.workrecord.repo;

import java.util.List;
import java.util.Optional;

import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.record.dom.actualworkinghours.daily.workrecord.AttendanceTimeByWorkOfDaily;

public interface AttendanceTimeByWorkOfDailyRepository {

void add(AttendanceTimeByWorkOfDaily attendanceTime);
	
	void update(AttendanceTimeByWorkOfDaily attendanceTime);
	
	Optional<AttendanceTimeByWorkOfDaily> find(String employeeId, GeneralDate ymd);
	
	List<AttendanceTimeByWorkOfDaily> findAllOf(String employeeId, List<GeneralDate> ymd);
}
