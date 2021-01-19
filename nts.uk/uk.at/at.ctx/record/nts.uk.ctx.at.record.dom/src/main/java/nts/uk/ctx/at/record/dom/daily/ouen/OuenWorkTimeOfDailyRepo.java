package nts.uk.ctx.at.record.dom.daily.ouen;

import java.util.List;
import java.util.Optional;

import nts.arc.time.GeneralDate;
import nts.arc.time.calendar.period.DatePeriod;

public interface OuenWorkTimeOfDailyRepo {

	public Optional<OuenWorkTimeOfDaily> find(String empId, GeneralDate ymd);
	
	public List<OuenWorkTimeOfDaily> find(List<String> employeeId, DatePeriod datePeriod);
	
	public void update(OuenWorkTimeOfDaily domain);
	
	public void insert(OuenWorkTimeOfDaily domain);
	
	public void delete(OuenWorkTimeOfDaily domain);
}
