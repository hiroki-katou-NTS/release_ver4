package nts.uk.ctx.at.function.ac.monthlyattendanceitem;

import java.util.ArrayList;
//import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;

import nts.arc.task.parallel.ManagedParallelWithContext;
import nts.uk.ctx.at.function.dom.adapter.monthlyattendanceitem.AttendanceItemValueImport;
import nts.uk.ctx.at.function.dom.adapter.monthlyattendanceitem.MonthlyAttendanceItemAdapter;
import nts.uk.ctx.at.function.dom.adapter.monthlyattendanceitem.MonthlyAttendanceItemFunImport;
import nts.uk.ctx.at.function.dom.adapter.monthlyattendanceitem.MonthlyAttendanceResultImport;
import nts.uk.ctx.at.record.pub.dailyattendanceitem.AttendanceItemService;
import nts.arc.time.calendar.period.YearMonthPeriod;

@TransactionAttribute(TransactionAttributeType.SUPPORTS)
@Stateless
public class MonthlyAttendanceItemAcFinder implements MonthlyAttendanceItemAdapter {

	@Inject
	private AttendanceItemService attendanceItemService;
	
	@Inject
	private ManagedParallelWithContext parallel;

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
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
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

	@Override
	public List<MonthlyAttendanceResultImport> getMonthlyValueOfParallel(Map<String, YearMonthPeriod> employees, Collection<Integer> itemIds) {
		List<MonthlyAttendanceResultImport> itemValuesSync = Collections.synchronizedList(new ArrayList<>());
		this.parallel.forEach(employees.entrySet(), employee -> {
			itemValuesSync.addAll(this.getMonthlyValueOf(employee.getKey(), employee.getValue(), itemIds));
		});
		return new ArrayList<>(itemValuesSync);
	}

}
