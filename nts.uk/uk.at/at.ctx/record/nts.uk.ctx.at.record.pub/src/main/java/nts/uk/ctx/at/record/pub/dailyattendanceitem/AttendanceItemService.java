package nts.uk.ctx.at.record.pub.dailyattendanceitem;

import java.util.List;
import java.util.Optional;

import nts.arc.time.GeneralDate;
import nts.uk.shr.com.time.calendar.period.DatePeriod;

public interface AttendanceItemService {

	public Optional<AttendanceItemValue> getValueOf(String employeeId, GeneralDate workingDate, int itemId);
	
	public AttendanceResult getValueOf(String employeeId, GeneralDate workingDate, List<Integer> itemIds);
	
	public List<AttendanceResult> getValueOf(List<String> employeeId, DatePeriod workingDate, List<Integer> itemIds);
}
