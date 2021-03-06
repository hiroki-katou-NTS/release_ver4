package nts.uk.ctx.at.request.dom.application.common.adapter.record.dailyattendancetime;

import java.util.List;
import java.util.Map;

import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.shared.dom.worktime.common.TimeZone;

public interface DailyAttendanceTimeCaculation {
	DailyAttendanceTimeCaculationImport getCalculation(
			String employeeID,
			GeneralDate ymd,
			String workTypeCode,
			String workTimeCode,
			Map<Integer, TimeZone> mapTimeZone,
			List<Integer> breakStartTimes,
			List<Integer> breakEndTime,
			List<OutingTimeZoneExport> outingTimeSheets,
			List<ChildCareTimeZoneExport> shortWorkingTimeSheets);
	
	DailyAttenTimeLateLeaveImport calcDailyLateLeave(DailyAttenTimeParam dailyAttenTimeParam);
}
