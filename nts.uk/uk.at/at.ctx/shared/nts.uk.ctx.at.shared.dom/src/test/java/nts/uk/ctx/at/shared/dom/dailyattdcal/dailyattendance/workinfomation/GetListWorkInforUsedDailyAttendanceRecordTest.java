package nts.uk.ctx.at.shared.dom.dailyattdcal.dailyattendance.workinfomation;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.Test;

import nts.uk.ctx.at.shared.dom.WorkInformation;
import nts.uk.ctx.at.shared.dom.holidaymanagement.publicholiday.configuration.DayOfWeek;
import nts.uk.ctx.at.shared.dom.scherec.dailyattdcal.dailyattendance.workinfomation.GetWorkInforUsedDailyAttenRecordService;
import nts.uk.ctx.at.shared.dom.scherec.dailyattdcal.dailyattendance.workinfomation.WorkInfoOfDailyAttendance;
import nts.uk.ctx.at.shared.dom.worktime.common.WorkTimeCode;
import nts.uk.ctx.at.shared.dom.worktype.WorkTypeCode;
import static org.assertj.core.api.Assertions.tuple;

public class GetListWorkInforUsedDailyAttendanceRecordTest {

	@Test
	public void testGetListWorkInfo() {
		WorkInformation recordInfo1 = new WorkInformation("ty1", "ti1");
		WorkInformation recordInfo2 = new WorkInformation("ty2", "ti2");
		WorkInformation recordInfo3 = new WorkInformation("ty3", null);
		WorkInformation recordInfo4 = new WorkInformation("ty2", "ti3");
		WorkInformation recordInfo5 = new WorkInformation("ty2", "ti2");
		WorkInformation recordInfo6 = new WorkInformation("ty3", "ti2");
		List<WorkInfoOfDailyAttendance> lstWorkInfoOfDailyAttendance = Arrays.asList(
				WorkInfoOfDailyAttendanceHelper.getData(recordInfo1),
				WorkInfoOfDailyAttendanceHelper.getData(recordInfo2),
				WorkInfoOfDailyAttendanceHelper.getData(recordInfo3),
				WorkInfoOfDailyAttendanceHelper.getData(recordInfo4),
				WorkInfoOfDailyAttendanceHelper.getData(recordInfo5),
				WorkInfoOfDailyAttendanceHelper.getData(recordInfo6));
		
		List<WorkInformation> listWorkInformation= GetWorkInforUsedDailyAttenRecordService.getListWorkInfo(lstWorkInfoOfDailyAttendance);
		assertThat(listWorkInformation).extracting(d->d.getWorkTypeCode(), d->d.getWorkTimeCode())
				.containsExactly(
						tuple(new WorkTypeCode("ty1"), new WorkTimeCode("ti1")),
						tuple(new WorkTypeCode("ty2"), new WorkTimeCode("ti2")),
						tuple(new WorkTypeCode("ty3"), null),
						tuple(new WorkTypeCode("ty2"), new WorkTimeCode("ti3")),
						tuple(new WorkTypeCode("ty3"), new WorkTimeCode("ti2")));

	}
	
}