package nts.uk.ctx.at.function.dom.adapter;

import java.util.List;

public interface DailyAttendanceItemAdapter {
	
	List<DailyAttendanceItemAdapterDto> getDailyAttendanceItem(String companyId, List<Integer> dailyAttendanceItemIds);
}
