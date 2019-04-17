package nts.uk.ctx.at.record.dom.daily.attendanceleavinggate.repo;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.record.dom.daily.attendanceleavinggate.PCLogOnInfoOfDaily;
import nts.uk.shr.com.time.calendar.period.DatePeriod;

public interface PCLogOnInfoOfDailyRepo {

	public Optional<PCLogOnInfoOfDaily> find(String employeeId, GeneralDate baseDate);

	public List<PCLogOnInfoOfDaily> find(String employeeId, List<GeneralDate> baseDate);
	
	public List<PCLogOnInfoOfDaily> find(String employeeId);
	
	public List<PCLogOnInfoOfDaily> finds(List<String> employeeId, DatePeriod baseDate);
	
	public List<PCLogOnInfoOfDaily> finds(Map<String, List<GeneralDate>> param);

	public void update(PCLogOnInfoOfDaily domain);

	public void add(PCLogOnInfoOfDaily domain);
	
	public void removeByKey(String employeeId, GeneralDate baseDate);
}
