package nts.uk.ctx.at.shared.dom.attendance.data;

import static java.util.stream.Collectors.*;

import java.util.List;

import lombok.Value;
import nts.arc.time.GeneralDate;
import nts.uk.shr.com.time.calendar.period.DatePeriod;

/**
 * 日別実績のキー
 */
@Value
public final class DailyRecordKey {

	/** 社員ID */
	private final String employeeId;
	
	/** 年月日 */
	private final GeneralDate date;

	public static List<DailyRecordKey> listOf(String employeeId, DatePeriod period) {
		return period.datesBetween().stream()
				.map(date -> new DailyRecordKey(employeeId, date))
				.collect(toList());
	}
	
	public static List<DailyRecordKey> listOf(List<String> employeeIds, GeneralDate date) {
		return employeeIds.stream()
				.map(employeeId -> new DailyRecordKey(employeeId, date))
				.collect(toList());
	}
	
	public static List<String> getEmployeeIds(List<DailyRecordKey> recordKeys) {
		return recordKeys.stream()
				.map(key -> key.getEmployeeId())
				.distinct()
				.collect(toList());
	}
}