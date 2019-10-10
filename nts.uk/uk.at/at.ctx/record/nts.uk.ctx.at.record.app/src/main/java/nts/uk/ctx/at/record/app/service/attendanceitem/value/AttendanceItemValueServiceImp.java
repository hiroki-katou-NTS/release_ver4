package nts.uk.ctx.at.record.app.service.attendanceitem.value;

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
import nts.uk.ctx.at.record.app.find.dailyperform.DailyRecordDto;
import nts.uk.ctx.at.record.app.find.dailyperform.DailyRecordWorkFinder;
import nts.uk.ctx.at.record.app.find.monthly.finder.MonthlyRecordWorkFinder;
import nts.uk.ctx.at.record.app.find.monthly.root.MonthlyRecordWorkDto;
import nts.uk.ctx.at.shared.app.util.attendanceitem.ConvertHelper;
import nts.uk.ctx.at.shared.dom.attendance.util.AttendanceItemUtil;
import nts.uk.ctx.at.shared.dom.attendance.util.AttendanceItemUtil.AttendanceItemType;
import nts.uk.ctx.at.shared.dom.attendance.util.item.ItemValue;
import nts.uk.ctx.at.shared.dom.workrule.closure.ClosureId;
import nts.uk.shr.com.time.calendar.date.ClosureDate;
import nts.uk.shr.com.time.calendar.period.DatePeriod;
import nts.uk.shr.com.time.calendar.period.YearMonthPeriod;

@Stateless
@TransactionAttribute(TransactionAttributeType.SUPPORTS)
public class AttendanceItemValueServiceImp implements AttendanceItemValueService {


	@Inject
	private DailyRecordWorkFinder fullFinder;
	
    @Inject
    private MonthlyRecordWorkFinder monthlyFinder;
    
	@Override
	public Optional<ItemValue> getValueOf(String employeeId, GeneralDate workingDate, int itemId) {
		DailyRecordDto itemDtos = this.fullFinder.find(employeeId, workingDate);
		return AttendanceItemUtil.toItemValues(itemDtos, Arrays.asList(itemId)).stream()
																				.filter(c -> c.itemId() == itemId).findFirst();
	}

	@Override
	public AttendanceItemValueResult getValueOf(String employeeId, GeneralDate workingDate,
			Collection<Integer> itemIds) {
		DailyRecordDto itemDtos = this.fullFinder.find(employeeId, workingDate);
		
		return convertDaily(Arrays.asList(itemDtos) , itemIds).get(0);
	}

	@Override
	public List<AttendanceItemValueResult> getValueOf(Collection<String> employeeId, DatePeriod workingDate,
			Collection<Integer> itemIds) {
		if (employeeId == null || employeeId.isEmpty() || workingDate == null) {
			return new ArrayList<>();
		}
		
		return convertDaily(this.fullFinder.find(new ArrayList<>(employeeId), workingDate), itemIds);
	}

	@Override
	public MonthlyAttendanceItemValueResult getMonthlyValueOf(String employeeId, YearMonth yearMonth, int closureId,
			int clouseDate, boolean lastDayOfMonth, Collection<Integer> itemIds) {
		MonthlyRecordWorkDto itemDtos = monthlyFinder.find(employeeId, yearMonth,
				ConvertHelper.getEnum(closureId, ClosureId.class), new ClosureDate(clouseDate, lastDayOfMonth));
		
		return convertMonthly(Arrays.asList(itemDtos), itemIds).get(0);
	}

	@Override
	public List<MonthlyAttendanceItemValueResult> getMonthlyValueOf(Collection<String> employeeId, DatePeriod range,
			Collection<Integer> itemIds) {
		
		return convertMonthly(monthlyFinder.find(employeeId, range), itemIds);
	}

	@Override
	public List<MonthlyAttendanceItemValueResult> getMonthlyValueOf(String employeeId, DatePeriod range,
			Collection<Integer> itemIds) {
		return getMonthlyValueOf(Arrays.asList(employeeId), range, itemIds);
	}

	@Override
	public List<MonthlyAttendanceItemValueResult> getMonthlyValueOf(Collection<String> employeeId,
			YearMonthPeriod range, Collection<Integer> itemIds) {
		
		return convertMonthly(monthlyFinder.find(employeeId, range), itemIds);
	}

	@Override
	public List<MonthlyAttendanceItemValueResult> getMonthlyValueOf(String employeeId, YearMonthPeriod range,
			Collection<Integer> itemIds) {
		return getMonthlyValueOf(Arrays.asList(employeeId), range, itemIds);
	}

	@Override
	public List<MonthlyAttendanceItemValueResult> getMonthlyValueOf(Collection<String> employeeId, YearMonth ym,
			Collection<Integer> itemIds) {
		
		return convertMonthly(monthlyFinder.find(employeeId, ym), itemIds);
	}

	@Override
	public List<MonthlyAttendanceItemValueResult> getMonthlyValueOf(String employeeId, YearMonth ym,
			Collection<Integer> itemIds) {
		return getMonthlyValueOf(Arrays.asList(employeeId), ym, itemIds);
	}
	
	private List<AttendanceItemValueResult> convertDaily(List<DailyRecordDto> dto, Collection<Integer> itemIds){
		return AttendanceItemUtil.toItemValues(dto, itemIds, AttendanceItemType.DAILY_ITEM).entrySet().stream().map(d -> {
			return AttendanceItemValueResult.builder()
					.employeeId(d.getKey().employeeId())
					.workingDate(d.getKey().workingDate())
					.attendanceItems(d.getValue())
					.build();
		}).collect(Collectors.toList());
	}
	
	private List<MonthlyAttendanceItemValueResult> convertMonthly(List<MonthlyRecordWorkDto> dto, Collection<Integer> itemIds){
		return AttendanceItemUtil.toItemValues(dto, itemIds, AttendanceItemType.MONTHLY_ITEM).entrySet().stream().map(d -> {
			return MonthlyAttendanceItemValueResult.builder()
					.employeeId(d.getKey().getEmployeeId())
					.yearMonth(d.getKey().getYearMonth()).closureId(d.getKey().getClosureID())
					.clouseDate(d.getKey().getClosureDate().getClosureDay()).lastDayOfMonth(d.getKey().getClosureDate().getLastDayOfMonth())
					.attendanceItems(d.getValue())
					.build();
		}).collect(Collectors.toList());
	}
	
}
