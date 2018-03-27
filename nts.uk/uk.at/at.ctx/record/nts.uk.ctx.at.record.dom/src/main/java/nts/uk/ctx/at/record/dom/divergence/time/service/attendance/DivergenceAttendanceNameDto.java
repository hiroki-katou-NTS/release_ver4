package nts.uk.ctx.at.record.dom.divergence.time.service.attendance;

import lombok.AllArgsConstructor;
import lombok.Value;

/**
 * The Class DivergenceAttendanceNameDto.
 */
@AllArgsConstructor
@Value
public class DivergenceAttendanceNameDto {
	
	/** The attendance item id. */
	private int attendanceItemId;

	/** The attendance item name. */
	private String attendanceItemName;

	/** The attendance item display number. */
	private int attendanceItemDisplayNumber;
}
