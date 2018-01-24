package nts.uk.ctx.at.record.app.find.dailyperform.temporarytime;

import java.util.List;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.record.app.find.dailyperform.temporarytime.dto.TemporaryTimeOfDailyPerformanceDto;
import nts.uk.ctx.at.record.dom.worktime.repository.TemporaryTimeOfDailyPerformanceRepository;
import nts.uk.ctx.at.shared.app.util.attendanceitem.FinderFacade;
import nts.uk.ctx.at.shared.dom.attendance.util.item.ConvertibleAttendanceItem;
import nts.uk.shr.com.time.calendar.period.DatePeriod;

@Stateless
public class TemporaryTimeOfDailyPerformanceFinder extends FinderFacade {

	@Inject
	private TemporaryTimeOfDailyPerformanceRepository repo;

	@SuppressWarnings("unchecked")
	@Override
	public TemporaryTimeOfDailyPerformanceDto find(String employeeId, GeneralDate baseDate) {
		return TemporaryTimeOfDailyPerformanceDto.getDto(this.repo.findByKey(employeeId, baseDate).orElse(null));
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public <T extends ConvertibleAttendanceItem> List<T> find(List<String> employeeId, DatePeriod baseDate) {
		return (List<T>) this.repo.finds(employeeId, baseDate).stream()
				.map(c -> TemporaryTimeOfDailyPerformanceDto.getDto(c)).collect(Collectors.toList());
	}

}
