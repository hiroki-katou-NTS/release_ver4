package nts.uk.ctx.at.record.dom.daily.optionalitemtime;

import java.util.List;
import java.util.Optional;

import nts.arc.time.GeneralDate;
import nts.uk.shr.com.time.calendar.period.DatePeriod;

public interface AnyItemValueOfDailyRepo {

	public Optional<AnyItemValueOfDaily> find(String employeeId, GeneralDate baseDate);

	public List<AnyItemValueOfDaily> finds(List<String> employeeId, DatePeriod baseDate);

	public List<AnyItemValueOfDaily> find(String employeeId, List<GeneralDate> baseDate);
	
	public List<AnyItemValueOfDaily> find(String employeeId);

	public void update(AnyItemValueOfDaily domain);

	public void add(AnyItemValueOfDaily domain);
}
