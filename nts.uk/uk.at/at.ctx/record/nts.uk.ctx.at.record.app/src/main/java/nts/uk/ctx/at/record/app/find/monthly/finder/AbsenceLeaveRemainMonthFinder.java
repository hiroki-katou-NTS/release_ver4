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
import nts.uk.ctx.at.record.app.find.monthly.root.AbsenceLeaveRemainDataDto;
import nts.uk.ctx.at.shared.app.util.attendanceitem.ConvertHelper;
import nts.uk.ctx.at.shared.app.util.attendanceitem.MonthlyFinderFacade;
import nts.uk.ctx.at.shared.dom.attendance.util.item.ConvertibleAttendanceItem;
import nts.uk.ctx.at.shared.dom.monthly.vacation.absenceleave.monthremaindata.AbsenceLeaveRemainDataRepository;
import nts.uk.ctx.at.shared.dom.workrule.closure.ClosureId;
import nts.uk.shr.com.time.calendar.date.ClosureDate;

@Stateless
public class AbsenceLeaveRemainMonthFinder extends MonthlyFinderFacade {
	
	@Inject
	private AbsenceLeaveRemainDataRepository repo;

	@Override
	@SuppressWarnings("unchecked")
	public AbsenceLeaveRemainDataDto find(String employeeId, YearMonth yearMonth, ClosureId closureId,
			ClosureDate closureDate) {
		return find(Arrays.asList(employeeId), yearMonth).stream().map(c -> (AbsenceLeaveRemainDataDto) c)
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
								.stream().map(d -> AbsenceLeaveRemainDataDto.from(d)).collect(Collectors.toList());
	}
}
