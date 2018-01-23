package nts.uk.ctx.at.record.app.find.dailyperform.optionalitem;

import java.util.List;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.record.app.find.dailyperform.optionalitem.dto.OptionalItemOfDailyPerformDto;
import nts.uk.ctx.at.record.dom.daily.optionalitemtime.AnyItemValueOfDailyRepo;
import nts.uk.ctx.at.shared.app.util.attendanceitem.FinderFacade;
import nts.uk.ctx.at.shared.dom.attendance.util.item.ConvertibleAttendanceItem;
import nts.uk.shr.com.time.calendar.period.DatePeriod;

/** 日別実績の任意項目 Finder */
@Stateless
public class OptionalItemOfDailyPerformFinder extends FinderFacade {

	@Inject
	private AnyItemValueOfDailyRepo repo;

	@SuppressWarnings("unchecked")
	@Override
	public OptionalItemOfDailyPerformDto find(String employeeId, GeneralDate baseDate) {
		return OptionalItemOfDailyPerformDto.getDto(this.repo.find(employeeId, baseDate).orElse(null));
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public <T extends ConvertibleAttendanceItem> List<T> find(List<String> employeeId, DatePeriod baseDate) {
		return (List<T>) this.repo.finds(employeeId, baseDate).stream()
				.map(c -> OptionalItemOfDailyPerformDto.getDto(c)).collect(Collectors.toList());
	}

}
