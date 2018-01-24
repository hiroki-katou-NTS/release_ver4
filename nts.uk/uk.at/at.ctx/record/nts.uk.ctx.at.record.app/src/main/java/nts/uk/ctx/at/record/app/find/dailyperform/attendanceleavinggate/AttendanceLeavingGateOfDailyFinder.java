package nts.uk.ctx.at.record.app.find.dailyperform.attendanceleavinggate;

import java.util.List;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.record.app.find.dailyperform.attendanceleavinggate.dto.AttendanceLeavingGateOfDailyDto;
import nts.uk.ctx.at.record.dom.daily.attendanceleavinggate.repo.AttendanceLeavingGateOfDailyRepo;
import nts.uk.ctx.at.shared.app.util.attendanceitem.FinderFacade;
import nts.uk.ctx.at.shared.dom.attendance.util.item.ConvertibleAttendanceItem;
import nts.uk.shr.com.time.calendar.period.DatePeriod;

@Stateless
public class AttendanceLeavingGateOfDailyFinder extends FinderFacade {

	@Inject
	private AttendanceLeavingGateOfDailyRepo repo;

	@SuppressWarnings("unchecked")
	@Override
	public AttendanceLeavingGateOfDailyDto find(String employeeId, GeneralDate baseDate) {
		return AttendanceLeavingGateOfDailyDto.getDto(this.repo.find(employeeId, baseDate).orElse(null));
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public <T extends ConvertibleAttendanceItem> List<T> find(List<String> employeeId, DatePeriod baseDate) {
		return (List<T>) this.repo.finds(employeeId, baseDate).stream()
				.map(c -> AttendanceLeavingGateOfDailyDto.getDto(c)).collect(Collectors.toList());
	}

}
