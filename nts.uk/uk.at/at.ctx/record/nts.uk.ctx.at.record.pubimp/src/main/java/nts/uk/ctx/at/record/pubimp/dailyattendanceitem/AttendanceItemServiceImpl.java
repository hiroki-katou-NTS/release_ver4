package nts.uk.ctx.at.record.pubimp.dailyattendanceitem;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.record.app.find.dailyperform.DailyRecordDto;
import nts.uk.ctx.at.record.app.find.dailyperform.DailyRecordWorkFinder;
import nts.uk.ctx.at.record.pub.dailyattendanceitem.AttendanceItemService;
import nts.uk.ctx.at.record.pub.dailyattendanceitem.AttendanceItemValue;
import nts.uk.ctx.at.record.pub.dailyattendanceitem.AttendanceResult;
import nts.uk.ctx.at.shared.dom.attendance.util.AttendanceItemUtil;
import nts.uk.shr.com.time.calendar.period.DatePeriod;

@Stateless
public class AttendanceItemServiceImpl implements AttendanceItemService {

	@Inject
	private DailyRecordWorkFinder fullFinder;

	@Override
	public Optional<AttendanceItemValue> getValueOf(String employeeId, GeneralDate workingDate, int itemId) {
		DailyRecordDto itemDtos = this.fullFinder.find(employeeId, workingDate);
		return AttendanceItemUtil.toItemValues(itemDtos, Arrays.asList(itemId)).stream()
				.filter(c -> c.itemId() == itemId).findFirst()
				.map(c -> new AttendanceItemValue(c.getValueType().value, c.itemId(), c.value()));
	}

	@Override
	public AttendanceResult getValueOf(String employeeId, GeneralDate workingDate, List<Integer> itemIds) {
		DailyRecordDto itemDtos = this.fullFinder.find(employeeId, workingDate);
		return getAndConvert(employeeId, workingDate, itemDtos, itemIds);
	}

	@Override
	public List<AttendanceResult> getValueOf(List<String> employeeId, DatePeriod workingDate, List<Integer> itemIds) {
		if(employeeId == null || employeeId.isEmpty() || workingDate == null){
			return new ArrayList<>();
		}
		return this.fullFinder.find(employeeId, workingDate).stream()
				.map(c -> getAndConvert(c.employeeId(), c.workingDate(),(DailyRecordDto)  c, itemIds))
				.collect(Collectors.toList());
	}
	
	private AttendanceResult getAndConvert(String employeeId, GeneralDate workingDate, DailyRecordDto data, List<Integer> itemIds){
		return AttendanceResult.builder().employeeId(employeeId).workingDate(workingDate)
				.attendanceItems(AttendanceItemUtil.toItemValues(data, itemIds).stream()
						.map(c -> new AttendanceItemValue(c.getValueType().value, c.itemId(), c.value()))
						.collect(Collectors.toList())).build();
	}

}
