package nts.uk.ctx.at.function.ac.monthlyattendanceitem;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.uk.ctx.at.function.dom.adapter.monthlyattendanceitem.AttendanceItemValueImport;
import nts.uk.ctx.at.function.dom.adapter.monthlyattendanceitem.MonthlyAttendanceItemAdapter;
import nts.uk.ctx.at.function.dom.adapter.monthlyattendanceitem.MonthlyAttendanceItemFunImport;
import nts.uk.ctx.at.function.dom.adapter.monthlyattendanceitem.MonthlyAttendanceResultImport;
import nts.uk.ctx.at.record.pub.dailyattendanceitem.AttendanceItemService;
import nts.uk.shr.com.time.calendar.period.YearMonthPeriod;

@Stateless
public class MonthlyAttendanceItemAcFinder implements MonthlyAttendanceItemAdapter {

	@Inject
	private AttendanceItemService attendanceItemService;

	@Override
	public List<MonthlyAttendanceItemFunImport> getMonthlyAttendanceItem(String companyId,
			List<Integer> dailyAttendanceItemIds) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * RequestList421
	 */
	@Override
	public List<MonthlyAttendanceResultImport> getMonthlyValueOf(Collection<String> employeeIds, YearMonthPeriod range,
			Collection<Integer> itemIds) {
		return attendanceItemService.getMonthlyValueOf(employeeIds, range, itemIds).stream().map(m -> {
			return new MonthlyAttendanceResultImport(m.getEmployeeId(), m.getYearMonth(), m.getClosureId(),
					m.getClouseDate(), m.isLastDayOfMonth(),
					m.getAttendanceItems().stream().map(item -> {
						return new AttendanceItemValueImport(item.getValueType(), item.getItemId(), item.value());
					}).collect(Collectors.toList()));
		}).collect(Collectors.toList());
	}

	/**
	 * RequestList421
	 */
	@Override
	public List<MonthlyAttendanceResultImport> getMonthlyValueOf(String employeeId, YearMonthPeriod range,
			Collection<Integer> itemIds) {
		return attendanceItemService.getMonthlyValueOf(employeeId, range, itemIds).stream().map(m -> {
			return new MonthlyAttendanceResultImport(m.getEmployeeId(), m.getYearMonth(), m.getClosureId(),
					m.getClouseDate(), m.isLastDayOfMonth(),
					m.getAttendanceItems().stream().map(item -> {
						return new AttendanceItemValueImport(item.getValueType(), item.getItemId(), item.value());
					}).collect(Collectors.toList()));
		}).collect(Collectors.toList());
	}

}
