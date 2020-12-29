package nts.uk.ctx.at.schedule.dom.shift.management.shifttable;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import mockit.Injectable;
import nts.arc.time.GeneralDate;
import nts.arc.time.calendar.DateInMonth;
import nts.arc.time.calendar.OneMonth;
import nts.uk.ctx.at.schedule.dom.shift.management.workavailability.AssignmentMethod;
import nts.uk.ctx.at.schedule.dom.shift.management.workavailability.WorkAvailability;
import nts.uk.ctx.at.schedule.dom.shift.management.workavailability.WorkAvailabilityMemo;
import nts.uk.ctx.at.schedule.dom.shift.management.workavailability.WorkAvailabilityOfOneDay;
import nts.uk.ctx.at.shared.dom.common.time.TimeSpanForCalc;
import nts.uk.ctx.at.shared.dom.workrule.shiftmaster.ShiftMasterCode;
import nts.uk.shr.com.time.TimeWithDayAttr;

public class WorkAvailabilityRuleDateSettingHelper {
	
	@Injectable static WorkAvailability.Require require;
	
	public static WorkAvailabilityRuleDateSetting defaultCreate() {
		return new WorkAvailabilityRuleDateSetting(
				new OneMonth(DateInMonth.lastDay()), 
				DateInMonth.of(20), 
				new HolidayAvailabilityMaxdays(6));
	}
	
	public static WorkAvailabilityRuleDateSetting createWithParam(int closureDate, int deadline, int maxHolidayDays) {
		return new WorkAvailabilityRuleDateSetting(
				new OneMonth(DateInMonth.of(closureDate)), 
				DateInMonth.of(deadline), 
				new HolidayAvailabilityMaxdays(maxHolidayDays));
	}
	
	public static WorkAvailabilityOfOneDay createExpectation(
			@Injectable WorkAvailability.Require require,
			GeneralDate expectingDate, AssignmentMethod assignmentMethod) {
		
		List<ShiftMasterCode> shiftMasterCodeList = assignmentMethod == AssignmentMethod.SHIFT ? 
				Arrays.asList(new ShiftMasterCode("S01")) : Collections.emptyList();
		
		List<TimeSpanForCalc> timeZoneList = assignmentMethod == AssignmentMethod.TIME_ZONE ?
				Arrays.asList(new TimeSpanForCalc(new TimeWithDayAttr(100), new TimeWithDayAttr(200))) : Collections.emptyList();
		
		return WorkAvailabilityOfOneDay.create(
				require,
				"emp-id", 
				expectingDate,
				new WorkAvailabilityMemo("memo"), 
				assignmentMethod, 
				shiftMasterCodeList, 
				timeZoneList);
	}
	
}
