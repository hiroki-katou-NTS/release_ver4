package nts.uk.ctx.at.request.dom.application.common.adapter.schedule.schedule.basicschedule;

import java.util.List;
import java.util.Optional;

import nts.arc.time.GeneralDate;
import nts.uk.shr.com.time.calendar.period.DatePeriod;

/**
 * 
 * @author Doan Duy Hung
 *
 */
public interface ScBasicScheduleAdapter {
	
	public Optional<ScBasicScheduleImport> findByID(String employeeID, GeneralDate date);
	
	public List<ScBasicScheduleImport> findByID(List<String> employeeID, DatePeriod date);
	
}
