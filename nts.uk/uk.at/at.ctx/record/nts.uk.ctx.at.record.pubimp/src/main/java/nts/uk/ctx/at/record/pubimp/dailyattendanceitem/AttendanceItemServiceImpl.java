package nts.uk.ctx.at.record.pubimp.dailyattendanceitem;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;

import nts.arc.time.GeneralDate;
import nts.arc.time.YearMonth;
import nts.uk.ctx.at.record.app.service.attendanceitem.value.AttendanceItemValueService;
import nts.uk.ctx.at.record.app.service.attendanceitem.value.AttendanceItemValueService.AttendanceItemValueResult;
import nts.uk.ctx.at.record.app.service.attendanceitem.value.AttendanceItemValueService.MonthlyAttendanceItemValueResult;
import nts.uk.ctx.at.record.pub.dailyattendanceitem.AttendanceItemService;
import nts.uk.ctx.at.record.pub.dailyattendanceitem.AttendanceItemValue;
import nts.uk.ctx.at.record.pub.dailyattendanceitem.AttendanceResult;
import nts.uk.ctx.at.record.pub.dailyattendanceitem.MonthlyAttendanceResult;
import nts.uk.ctx.at.shared.dom.attendance.util.item.ItemValue;
import nts.uk.shr.com.time.calendar.period.DatePeriod;
import nts.uk.shr.com.time.calendar.period.YearMonthPeriod;

@Stateless
@TransactionAttribute(TransactionAttributeType.SUPPORTS)
public class AttendanceItemServiceImpl implements AttendanceItemService {

	@Inject
	private AttendanceItemValueService service;

	@Override
	public Optional<AttendanceItemValue> getValueOf(String employeeId, GeneralDate workingDate, int itemId) {
		return service.getValueOf(employeeId, workingDate, itemId).map(c -> convert1(c));
	}

	@Override
	public AttendanceResult getValueOf(String employeeId, GeneralDate workingDate, Collection<Integer> itemIds) {
		return convert2(this.service.getValueOf(employeeId, workingDate, itemIds));
	}

	@Override
	public List<AttendanceResult> getValueOf(Collection<String> employeeId, DatePeriod workingDate,
			Collection<Integer> itemIds) {
		if (employeeId == null || employeeId.isEmpty() || workingDate == null) {
			return new ArrayList<>();
		}
		return this.service.getValueOf(employeeId, workingDate, itemIds)
				.stream().map(t -> convert2(t)).collect(Collectors.toList());
	}

	@Override
	public List<MonthlyAttendanceResult> getMonthlyValueOf(Collection<String> employeeId, YearMonth yearMonth,
			int closureId, int clouseDate, boolean lastDayOfMonth, Collection<Integer> itemIds) {
		return getMonthlyValueOf(employeeId, yearMonth, itemIds).stream()
			.filter(c -> c.getClosureId() == closureId && c.getClouseDate() == clouseDate && c.isLastDayOfMonth() == lastDayOfMonth)
			.collect(Collectors.toList());
	}

	@Override
	public MonthlyAttendanceResult getMonthlyValueOf(String employeeId, YearMonth yearMonth, int closureId,
			int clouseDate, boolean lastDayOfMonth, Collection<Integer> itemIds) {
		MonthlyAttendanceItemValueResult result = this.service.getMonthlyValueOf(employeeId, yearMonth, closureId, clouseDate, lastDayOfMonth, itemIds);
		return convert3(result);
	}

	@Override
	public List<MonthlyAttendanceResult> getMonthlyValueOf(Collection<String> employeeId, DatePeriod range,
			Collection<Integer> itemIds) {
		return this.service.getMonthlyValueOf(employeeId, range, itemIds)
				.stream().map(t -> convert3(t)).collect(Collectors.toList());
	}

	@Override
	public List<MonthlyAttendanceResult> getMonthlyValueOf(String employeeId, DatePeriod range,
			Collection<Integer> itemIds) {
		return getMonthlyValueOf(Arrays.asList(employeeId), range, itemIds);
	}

	@Override
	public List<MonthlyAttendanceResult> getMonthlyValueOf(Collection<String> employeeId, YearMonthPeriod range,
			Collection<Integer> itemIds) {
		return this.service.getMonthlyValueOf(employeeId, range, itemIds)
				.stream().map(t -> convert3(t)).collect(Collectors.toList());
	}

	@Override
	public List<MonthlyAttendanceResult> getMonthlyValueOf(String employeeId, YearMonthPeriod range,
			Collection<Integer> itemIds) {
		return getMonthlyValueOf(Arrays.asList(employeeId), range, itemIds);
	}

	@Override
	public List<MonthlyAttendanceResult> getMonthlyValueOf(Collection<String> employeeId, YearMonth range,
			Collection<Integer> itemIds) {
		return this.service.getMonthlyValueOf(employeeId, range, itemIds)
				.stream().map(t -> convert3(t)).collect(Collectors.toList());
	}

	@Override
	public List<MonthlyAttendanceResult> getMonthlyValueOf(String employeeId, YearMonth range,
			Collection<Integer> itemIds) {
		return getMonthlyValueOf(Arrays.asList(employeeId), range, itemIds);
	}

	private AttendanceItemValue convert1(ItemValue c) {
		return new AttendanceItemValue(c.getValueType().value, c.itemId(), c.value());
	}

	private AttendanceResult convert2(AttendanceItemValueResult result) {
		return AttendanceResult.builder().employeeId(result.getEmployeeId()).workingDate(result.getWorkingDate())
				.attendanceItems(result.getAttendanceItems().stream().map(t -> convert1(t)).collect(Collectors.toList()))
				.build();
	}

	private MonthlyAttendanceResult convert3(MonthlyAttendanceItemValueResult result) {
		return MonthlyAttendanceResult.builder().employeeId(result.getEmployeeId())
				.attendanceItems(result.getAttendanceItems().stream().map(t -> convert1(t)).collect(Collectors.toList()))
				.closureId(result.getClosureId())
				.clouseDate(result.getClouseDate())
				.lastDayOfMonth(result.isLastDayOfMonth())
				.yearMonth(result.getYearMonth())
				.build();
	}
}
