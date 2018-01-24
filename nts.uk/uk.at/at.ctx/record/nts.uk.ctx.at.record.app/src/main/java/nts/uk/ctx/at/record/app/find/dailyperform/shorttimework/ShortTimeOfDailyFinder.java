package nts.uk.ctx.at.record.app.find.dailyperform.shorttimework;

import java.util.List;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.record.app.find.dailyperform.shorttimework.dto.ShortTimeOfDailyDto;
import nts.uk.ctx.at.record.dom.shorttimework.repo.ShortTimeOfDailyPerformanceRepository;
import nts.uk.ctx.at.shared.app.util.attendanceitem.FinderFacade;
import nts.uk.ctx.at.shared.dom.attendance.util.item.ConvertibleAttendanceItem;
import nts.uk.shr.com.time.calendar.period.DatePeriod;

@Stateless
public class ShortTimeOfDailyFinder extends FinderFacade {

	@Inject
	private ShortTimeOfDailyPerformanceRepository repo;

	@SuppressWarnings("unchecked")
	@Override
	public ShortTimeOfDailyDto find(String employeeId, GeneralDate baseDate) {
		return ShortTimeOfDailyDto.getDto(this.repo.find(employeeId, baseDate).orElse(null));
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public <T extends ConvertibleAttendanceItem> List<T> find(List<String> employeeId, DatePeriod baseDate) {
		return (List<T>) this.repo.finds(employeeId, baseDate).stream()
				.map(c -> ShortTimeOfDailyDto.getDto(c)).collect(Collectors.toList());
	}

}
