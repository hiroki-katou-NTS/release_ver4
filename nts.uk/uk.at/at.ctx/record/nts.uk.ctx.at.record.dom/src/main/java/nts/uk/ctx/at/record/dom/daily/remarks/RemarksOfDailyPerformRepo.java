package nts.uk.ctx.at.record.dom.daily.remarks;

import java.util.List;

import nts.arc.time.GeneralDate;
import nts.uk.shr.com.time.calendar.period.DatePeriod;

public interface RemarksOfDailyPerformRepo {

	public List<RemarksOfDailyPerform> getRemarks(String employeeId, GeneralDate workingDate);
	
	public List<RemarksOfDailyPerform> getRemarks(List<String> employeeId, DatePeriod baseDate);

	public List<RemarksOfDailyPerform> getRemarks(String employeeId, List<GeneralDate> baseDate);
	
	public void update(RemarksOfDailyPerform domain);

	public void update(List<RemarksOfDailyPerform> domain);

	public void add(RemarksOfDailyPerform domain);
	
	public void add(List<RemarksOfDailyPerform> domain);
	
	public void remove(String employeeId, GeneralDate workingDate);
}
