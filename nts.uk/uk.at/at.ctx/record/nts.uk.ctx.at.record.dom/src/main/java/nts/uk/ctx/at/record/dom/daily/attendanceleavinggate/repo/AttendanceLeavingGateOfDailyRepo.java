package nts.uk.ctx.at.record.dom.daily.attendanceleavinggate.repo;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.record.dom.daily.attendanceleavinggate.AttendanceLeavingGateOfDaily;
import nts.uk.shr.com.time.calendar.period.DatePeriod;

public interface AttendanceLeavingGateOfDailyRepo {

	public Optional<AttendanceLeavingGateOfDaily> find(String employeeId, GeneralDate baseDate);

	public List<AttendanceLeavingGateOfDaily> find(String employeeId, List<GeneralDate> baseDate);
	
	public List<AttendanceLeavingGateOfDaily> finds(List<String> employeeId, DatePeriod baseDate);
	
	public List<AttendanceLeavingGateOfDaily> finds(Map<String, List<GeneralDate>> param);
	
	public List<AttendanceLeavingGateOfDaily> find(String employeeId);

	public void update(AttendanceLeavingGateOfDaily domain);

	public void add(AttendanceLeavingGateOfDaily domain);
	
	public void removeByKey(String employeeId, GeneralDate baseDate);
}
