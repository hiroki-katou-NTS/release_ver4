package nts.uk.ctx.at.record.dom.dailyprocess.calc.converter;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.record.dom.actualworkinghours.AttendanceTimeOfDailyPerformance;
import nts.uk.ctx.at.record.dom.actualworkinghours.daily.workrecord.AttendanceTimeByWorkOfDaily;
import nts.uk.ctx.at.record.dom.affiliationinformation.AffiliationInforOfDailyPerfor;
import nts.uk.ctx.at.record.dom.breakorgoout.BreakTimeOfDailyPerformance;
import nts.uk.ctx.at.record.dom.breakorgoout.OutingTimeOfDailyPerformance;
import nts.uk.ctx.at.record.dom.calculationattribute.CalAttrOfDailyPerformance;
import nts.uk.ctx.at.record.dom.daily.attendanceleavinggate.AttendanceLeavingGateOfDaily;
import nts.uk.ctx.at.record.dom.daily.optionalitemtime.AnyItemValueOfDaily;
import nts.uk.ctx.at.record.dom.dailyprocess.calc.IntegrationOfDaily;
import nts.uk.ctx.at.record.dom.editstate.EditStateOfDailyPerformance;
import nts.uk.ctx.at.record.dom.raisesalarytime.SpecificDateAttrOfDailyPerfor;
import nts.uk.ctx.at.record.dom.shorttimework.ShortTimeOfDailyPerformance;
import nts.uk.ctx.at.record.dom.workinformation.WorkInfoOfDailyPerformance;
import nts.uk.ctx.at.record.dom.workrecord.erroralarm.EmployeeDailyPerError;
import nts.uk.ctx.at.record.dom.worktime.TemporaryTimeOfDailyPerformance;
import nts.uk.ctx.at.record.dom.worktime.TimeLeavingOfDailyPerformance;
import nts.uk.ctx.at.shared.dom.attendance.util.item.ItemValue;

public interface DailyRecordToAttendanceItemConverter {

	Optional<ItemValue> convert(int attendanceItemId);

	List<ItemValue> convert(Collection<Integer> attendanceItemIds);
	
	void merge(ItemValue value);
	
	void merge(Collection<ItemValue> values);
		
	IntegrationOfDaily toDomain();
	
	DailyRecordToAttendanceItemConverter setData(IntegrationOfDaily domain);

	DailyRecordToAttendanceItemConverter withWorkInfo(WorkInfoOfDailyPerformance domain);

	DailyRecordToAttendanceItemConverter withCalcAttr(CalAttrOfDailyPerformance domain);

	DailyRecordToAttendanceItemConverter withAffiliationInfo(AffiliationInforOfDailyPerfor domain);

	DailyRecordToAttendanceItemConverter withEmployeeErrors(EmployeeDailyPerError domain);

	DailyRecordToAttendanceItemConverter withOutingTime(OutingTimeOfDailyPerformance domain);

	DailyRecordToAttendanceItemConverter withBreakTime(BreakTimeOfDailyPerformance domain);

	DailyRecordToAttendanceItemConverter withBreakTime(List<BreakTimeOfDailyPerformance> domain);

	DailyRecordToAttendanceItemConverter withAttendanceTime(AttendanceTimeOfDailyPerformance domain);

	DailyRecordToAttendanceItemConverter withAttendanceTimeByWork(AttendanceTimeByWorkOfDaily domain);

	DailyRecordToAttendanceItemConverter withTimeLeaving(TimeLeavingOfDailyPerformance domain);

	DailyRecordToAttendanceItemConverter withShortTime(ShortTimeOfDailyPerformance domain);

	DailyRecordToAttendanceItemConverter withSpecificDateAttr(SpecificDateAttrOfDailyPerfor domain);

	DailyRecordToAttendanceItemConverter withAttendanceLeavingGate(AttendanceLeavingGateOfDaily domain);

	DailyRecordToAttendanceItemConverter withAnyItems(AnyItemValueOfDaily domain);

	DailyRecordToAttendanceItemConverter withEditStates(EditStateOfDailyPerformance domain);

	DailyRecordToAttendanceItemConverter withEditStates(List<EditStateOfDailyPerformance> domain);

	DailyRecordToAttendanceItemConverter withTemporaryTime(TemporaryTimeOfDailyPerformance domain);

	DailyRecordToAttendanceItemConverter employeeId(String employeeId);

	DailyRecordToAttendanceItemConverter workingDate(GeneralDate workingDate);

	DailyRecordToAttendanceItemConverter completed();

}