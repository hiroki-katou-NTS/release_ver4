package nts.uk.ctx.at.record.app.find.dailyperform.erroralarm;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.record.app.find.dailyperform.erroralarm.dto.EmployeeDailyPerErrorDto;
import nts.uk.ctx.at.record.dom.workrecord.erroralarm.EmployeeDailyPerErrorRepository;
import nts.uk.ctx.at.shared.app.util.attendanceitem.FinderFacade;
import nts.uk.ctx.at.shared.dom.attendance.util.item.ConvertibleAttendanceItem;
import nts.uk.shr.com.time.calendar.period.DatePeriod;

@Stateless
public class EmployeeDailyPerErrorFinder extends FinderFacade {

	@Inject
	private EmployeeDailyPerErrorRepository repo;
	
	@SuppressWarnings("unchecked")
	@Override
	public EmployeeDailyPerErrorDto find(String employeeId, GeneralDate baseDate) {
		return null;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public <T extends ConvertibleAttendanceItem> List<T> finds(String employeeId, GeneralDate baseDate) {
		return (List<T>) this.repo.find(employeeId, baseDate).stream()
				.map(c -> EmployeeDailyPerErrorDto.getDto(c)).collect(Collectors.toList());
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public <T extends ConvertibleAttendanceItem> List<T> find(List<String> employeeId, DatePeriod baseDate) {
		return (List<T>) this.repo.finds(employeeId, baseDate).stream()
				.map(c -> EmployeeDailyPerErrorDto.getDto(c)).collect(Collectors.toList());
	}

	@Override
	@SuppressWarnings("unchecked")
	public <T extends ConvertibleAttendanceItem> List<T> find(Map<String, List<GeneralDate>> param) {
		return (List<T>) this.repo.finds(param).stream()
			.map(c -> EmployeeDailyPerErrorDto.getDto(c)).collect(Collectors.toList());
	}

}
