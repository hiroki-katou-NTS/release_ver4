package nts.uk.ctx.at.record.dom.divergencetime.service.attendance;

import lombok.AllArgsConstructor;
import lombok.Value;

@AllArgsConstructor
@Value
public class AttendanceNameDivergenceDto {
	private int attendanceItemId;

	private String attendanceItemName;

	private int attendanceItemDisplayNumber;
}
