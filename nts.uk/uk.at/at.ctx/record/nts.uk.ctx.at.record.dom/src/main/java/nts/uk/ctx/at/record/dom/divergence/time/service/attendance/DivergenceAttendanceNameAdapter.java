package nts.uk.ctx.at.record.dom.divergence.time.service.attendance;

import java.util.List;

/**
 * The Interface DivergenceAttendanceNameAdapter.
 */
public interface DivergenceAttendanceNameAdapter {

	/**
	 * Gets the daily attendance item name.
	 *
	 * @param dailyAttendanceItemIds the daily attendance item ids
	 * @return the daily attendance item name
	 */
	List<DivergenceAttendanceNameDto> getDailyAttendanceItemName(List<Integer> dailyAttendanceItemIds);
}
