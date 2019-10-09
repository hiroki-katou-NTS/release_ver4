package nts.uk.ctx.at.record.app.find.dailyperform;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;

import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.record.app.find.dailyperform.dto.AttendanceTimeDailyPerformDto;
import nts.uk.ctx.at.record.dom.actualworkinghours.repository.AttendanceTimeRepository;
import nts.uk.ctx.at.shared.app.util.attendanceitem.FinderFacade;
import nts.uk.ctx.at.shared.dom.attendance.util.item.ConvertibleAttendanceItem;
import nts.uk.shr.com.time.calendar.period.DatePeriod;

/** 日別実績の勤怠時間 finder */
@Stateless
@TransactionAttribute(TransactionAttributeType.SUPPORTS)
public class AttendanceTimeOfDailyPerformFinder extends FinderFacade {

	@Inject
	private AttendanceTimeRepository attendanceTimeRepo;

	@Override
	@SuppressWarnings("unchecked")
	public AttendanceTimeDailyPerformDto find(String employeeId, GeneralDate baseDate) {
		return AttendanceTimeDailyPerformDto.getDto(attendanceTimeRepo.find(employeeId, baseDate).orElse(null));
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public <T extends ConvertibleAttendanceItem> List<T> find(List<String> employeeId, DatePeriod baseDate) {
		return (List<T>) this.attendanceTimeRepo.finds(employeeId, baseDate).stream()
				.map(c -> AttendanceTimeDailyPerformDto.getDto(c)).collect(Collectors.toList());
	}

	@Override
	@SuppressWarnings("unchecked")
	public <T extends ConvertibleAttendanceItem> List<T> find(Map<String, List<GeneralDate>> param) {
		return (List<T>) this.attendanceTimeRepo.finds(param).stream()
			.map(c -> AttendanceTimeDailyPerformDto.getDto(c)).collect(Collectors.toList());
	}

	@Override
	public Object getDomain(String employeeId, GeneralDate baseDate) {
		return attendanceTimeRepo.find(employeeId, baseDate);
	}

}
