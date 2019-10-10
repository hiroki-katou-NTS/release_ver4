package nts.uk.ctx.at.record.app.find.monthly.finder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;

import nts.arc.time.YearMonth;
import nts.uk.ctx.at.record.app.find.monthly.root.MonthlyRemarksDto;
import nts.uk.ctx.at.record.dom.monthly.remarks.RemarksMonthlyRecordRepository;
import nts.uk.ctx.at.shared.app.util.attendanceitem.ConvertHelper;
import nts.uk.ctx.at.shared.app.util.attendanceitem.MonthlyFinderFacade;
import nts.uk.ctx.at.shared.dom.attendance.util.item.ConvertibleAttendanceItem;
import nts.uk.ctx.at.shared.dom.workrule.closure.ClosureId;
import nts.uk.shr.com.time.calendar.date.ClosureDate;
import nts.uk.shr.com.time.calendar.period.DatePeriod;

@Stateless
@TransactionAttribute(TransactionAttributeType.SUPPORTS)
public class MonthlyRemarksFinder extends MonthlyFinderFacade {
	
	@Inject
	private RemarksMonthlyRecordRepository repo;

	@Override
	@SuppressWarnings("unchecked")
	public MonthlyRemarksDto find(String employeeId, YearMonth yearMonth, ClosureId closureId,
			ClosureDate closureDate) {
		return MonthlyRemarksDto.from(this.repo.find(employeeId, closureId, null, yearMonth, closureDate).orElse(null));
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<MonthlyRemarksDto> finds(String employeeId, YearMonth yearMonth, ClosureId closureId,
			ClosureDate closureDate) {
		return repo.findByYearMonthOrderByStartYmd(employeeId, yearMonth).stream()
				.filter(c -> c.getClosureId() == closureId && c.getClosureDate().getLastDayOfMonth() == closureDate.getLastDayOfMonth()
							&& c.getClosureDate().getClosureDay() == closureDate.getClosureDay())
				.map(c -> MonthlyRemarksDto.from(c)).collect(Collectors.toList());
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
				.stream().map(d -> MonthlyRemarksDto.from(d)).collect(Collectors.toList());
	}
}
