package nts.uk.ctx.at.record.app.find.monthly.finder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.arc.time.YearMonth;
import nts.arc.time.calendar.period.DatePeriod;
import nts.uk.ctx.at.record.app.find.monthly.root.MonthlyDayoffRemainDataDto;
import nts.uk.ctx.at.shared.app.util.attendanceitem.ConvertHelper;
import nts.uk.ctx.at.shared.app.util.attendanceitem.MonthlyFinderFacade;
import nts.uk.ctx.at.shared.dom.attendance.util.item.ConvertibleAttendanceItem;
import nts.uk.ctx.at.shared.dom.scherec.monthlyattdcal.monthly.vacation.dayoff.MonthlyDayoffRemainDataRepository;
import nts.uk.ctx.at.shared.dom.workrule.closure.ClosureId;
import nts.uk.shr.com.time.calendar.date.ClosureDate;

@Stateless
public class DayOffRemainMonthFinder extends MonthlyFinderFacade {
	
	@Inject
	private MonthlyDayoffRemainDataRepository repo;

	@Override
	@SuppressWarnings("unchecked")
	public MonthlyDayoffRemainDataDto find(String employeeId, YearMonth yearMonth, ClosureId closureId,
			ClosureDate closureDate) {
		return find(Arrays.asList(employeeId), yearMonth).stream().map(c -> (MonthlyDayoffRemainDataDto) c)
				.filter(c -> c.getClosureID() == closureId.value && c.getClosureDate().getLastDayOfMonth().equals(closureDate.getLastDayOfMonth())
					&& c.getClosureDate().getClosureDay() == closureDate.getClosureDay().v())
				.findFirst().orElse(null);
	}
	
	@Override
	public <T extends ConvertibleAttendanceItem> List<T> find(Collection<String> employeeId, DatePeriod range) {
		return find(employeeId, ConvertHelper.yearMonthsBetween(range));
	}

	@Override
	public <T extends ConvertibleAttendanceItem> List<T> find(Collection<String> employeeId, YearMonth yearMonth) {
		return find(employeeId, Arrays.asList(yearMonth));
	}

	@Override
	@SuppressWarnings("unchecked")
	public <T extends ConvertibleAttendanceItem> List<T> find(Collection<String> employeeId, Collection<YearMonth> yearMonth) {
		return (List<T>) repo.findBySidsAndYearMonths(new ArrayList<>(employeeId), new ArrayList<>(yearMonth))
								.stream().map(d -> MonthlyDayoffRemainDataDto.from(d)).collect(Collectors.toList());
	}
}
