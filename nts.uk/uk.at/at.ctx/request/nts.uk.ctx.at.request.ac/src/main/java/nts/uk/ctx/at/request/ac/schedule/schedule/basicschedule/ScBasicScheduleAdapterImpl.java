package nts.uk.ctx.at.request.ac.schedule.schedule.basicschedule;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.request.dom.application.common.adapter.schedule.schedule.basicschedule.ScBasicScheduleAdapter;
import nts.uk.ctx.at.request.dom.application.common.adapter.schedule.schedule.basicschedule.ScBasicScheduleImport;
import nts.uk.ctx.at.request.dom.application.common.adapter.schedule.schedule.basicschedule.WorkScheduleTimeZoneImport;
import nts.uk.ctx.at.schedule.pub.schedule.basicschedule.ScBasicScheduleExport;
import nts.uk.ctx.at.schedule.pub.schedule.basicschedule.ScBasicSchedulePub;
import nts.uk.shr.com.time.calendar.period.DatePeriod;
/**
 * 
 * @author Doan Duy Hung
 *
 */
@Stateless
public class ScBasicScheduleAdapterImpl implements ScBasicScheduleAdapter {
	
	@Inject
	private ScBasicSchedulePub scBasicSchedulePub;
	
	@Override
	public Optional<ScBasicScheduleImport> findByID(String employeeID, GeneralDate date) {
		return scBasicSchedulePub.findById(employeeID, date)
				.map(x -> convertTo(x));
	}

	private ScBasicScheduleImport convertTo(ScBasicScheduleExport x) {
		WorkScheduleTimeZoneImport workScheduleTimeZoneImport1 = x.getWorkScheduleTimeZones().stream()
			.filter(y -> y.getScheduleCnt()==1).findAny()
			.map(z -> new WorkScheduleTimeZoneImport(z.getScheduleStartClock(), z.getScheduleEndClock()))
			.orElse(new WorkScheduleTimeZoneImport(-1, -1));
		WorkScheduleTimeZoneImport workScheduleTimeZoneImport2 = x.getWorkScheduleTimeZones().stream()
				.filter(y -> y.getScheduleCnt()==2).findAny()
				.map(z -> new WorkScheduleTimeZoneImport(z.getScheduleStartClock(), z.getScheduleEndClock()))
				.orElse(new WorkScheduleTimeZoneImport(-1, -1));
		return new ScBasicScheduleImport(
				x.getEmployeeId(), 
				x.getDate(), 
				x.getWorkTypeCode(), 
				x.getWorkTimeCode(), 
				workScheduleTimeZoneImport1.getScheduleStartClock(), 
				workScheduleTimeZoneImport1.getScheduleEndClock(), 
				workScheduleTimeZoneImport2.getScheduleStartClock(), 
				workScheduleTimeZoneImport2.getScheduleEndClock());
	}

	@Override
	public List<ScBasicScheduleImport> findByID(List<String> employeeID, DatePeriod date) {
		
		return scBasicSchedulePub.findById(employeeID, date).stream().map(x -> convertTo(x)).collect(Collectors.toList());
	}

}
