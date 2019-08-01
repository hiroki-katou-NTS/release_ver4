package nts.uk.ctx.at.record.dom.daily;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.record.dom.actualworkinghours.AttendanceTimeOfDailyPerformance;
import nts.uk.ctx.at.record.dom.actualworkinghours.daily.workrecord.AttendanceTimeByWorkOfDaily;
import nts.uk.ctx.at.record.dom.affiliationinformation.AffiliationInforOfDailyPerfor;
import nts.uk.ctx.at.record.dom.affiliationinformation.WorkTypeOfDailyPerformance;
import nts.uk.ctx.at.record.dom.breakorgoout.BreakTimeOfDailyPerformance;
import nts.uk.ctx.at.record.dom.breakorgoout.OutingTimeOfDailyPerformance;
import nts.uk.ctx.at.record.dom.calculationattribute.CalAttrOfDailyPerformance;
import nts.uk.ctx.at.record.dom.daily.attendanceleavinggate.AttendanceLeavingGateOfDaily;
import nts.uk.ctx.at.record.dom.daily.attendanceleavinggate.PCLogOnInfoOfDaily;
import nts.uk.ctx.at.record.dom.daily.optionalitemtime.AnyItemValueOfDaily;
import nts.uk.ctx.at.record.dom.daily.remarks.RemarksOfDailyPerform;
import nts.uk.ctx.at.record.dom.dailyprocess.calc.IntegrationOfDaily;
import nts.uk.ctx.at.record.dom.editstate.EditStateOfDailyPerformance;
import nts.uk.ctx.at.record.dom.raisesalarytime.SpecificDateAttrOfDailyPerfor;
import nts.uk.ctx.at.record.dom.shorttimework.ShortTimeOfDailyPerformance;
import nts.uk.ctx.at.record.dom.workinformation.WorkInfoOfDailyPerformance;
import nts.uk.ctx.at.record.dom.workrecord.erroralarm.EmployeeDailyPerError;
import nts.uk.ctx.at.record.dom.worktime.TemporaryTimeOfDailyPerformance;
import nts.uk.ctx.at.record.dom.worktime.TimeLeavingOfDailyPerformance;

public interface DailyRecordAdUpService {
	
	public void adUpWorkInfo(WorkInfoOfDailyPerformance workInfo);
	
	public void adUpAffilicationInfo(AffiliationInforOfDailyPerfor affiliationInfor);
	
	public void adUpCalAttr(CalAttrOfDailyPerformance calAttr);
	
	public void adUpWorkType(Optional<WorkTypeOfDailyPerformance> businessType);
	
	public void adUpTimeLeaving(Optional<TimeLeavingOfDailyPerformance> attendanceLeave);
	
	public void adUpBreakTime(List<BreakTimeOfDailyPerformance> breakTime);
	
	public void adUpOutTime(Optional<OutingTimeOfDailyPerformance> outingTime);
	
	public void adUpShortTime(Optional<ShortTimeOfDailyPerformance> shortTime);
	
	public void adUpTemporaryTime(Optional<TemporaryTimeOfDailyPerformance> tempTime);
	
	public void adUpAttendanceLeavingGate( Optional<AttendanceLeavingGateOfDaily> attendanceLeavingGate);
	
	public void adUpAttendanceTime(Optional<AttendanceTimeOfDailyPerformance> attendanceTimeOfDailyPerformance);
	
	public void adUpSpecificDate(Optional<SpecificDateAttrOfDailyPerfor> specDateAttr);
	
	public void adUpEditState(List<EditStateOfDailyPerformance> editState);
	
	public void adUpAnyItem( Optional<AnyItemValueOfDaily> anyItemValue);
	
	public void adUpAttendanceTimeByWork(Optional<AttendanceTimeByWorkOfDaily> attendancetimeByWork);
	
	public void adUpPCLogOn(Optional<PCLogOnInfoOfDaily> pcLogOnInfo);
	
	public void adUpRemark(List<RemarksOfDailyPerform> remarks);
	
	/**
	 * @param errors domain error
	 * @param mapEmpDateError map<employeeId, list date>
	 * @param hasRemoveError has remove error
	 */
	public void adUpEmpError(List<EmployeeDailyPerError> errors, Map<String, List<GeneralDate>> mapEmpDateError, boolean hasRemoveError);
	
	public List<IntegrationOfDaily> adTimeAndAnyItemAdUp(List<IntegrationOfDaily> dailys);
}
