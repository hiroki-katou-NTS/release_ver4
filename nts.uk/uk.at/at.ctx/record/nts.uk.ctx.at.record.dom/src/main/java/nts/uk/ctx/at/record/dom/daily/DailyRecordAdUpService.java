package nts.uk.ctx.at.record.dom.daily;

import java.util.List;
import java.util.Optional;

import org.apache.commons.lang3.tuple.Pair;

import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.record.dom.actualworkinghours.AttendanceTimeOfDailyPerformance;
import nts.uk.ctx.at.record.dom.actualworkinghours.daily.workrecord.AttendanceTimeByWorkOfDaily;
import nts.uk.ctx.at.record.dom.affiliationinformation.AffiliationInforOfDailyPerfor;
import nts.uk.ctx.at.record.dom.affiliationinformation.WorkTypeOfDailyPerformance;
import nts.uk.ctx.at.record.dom.approvalmanagement.ApprovalProcessingUseSetting;
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
import nts.uk.ctx.at.record.dom.workrecord.identificationstatus.IdentityProcessUseSet;
import nts.uk.ctx.at.record.dom.worktime.TemporaryTimeOfDailyPerformance;
import nts.uk.ctx.at.record.dom.worktime.TimeLeavingOfDailyPerformance;

public interface DailyRecordAdUpService {
	//ドメインモデル「日別実績の勤務情報」を更新する
	public void adUpWorkInfo(WorkInfoOfDailyPerformance workInfo);
	
	//ドメインモデル「日別実績の所属情報」を更新する
	public void adUpAffilicationInfo(AffiliationInforOfDailyPerfor affiliationInfor);
	
	//ドメインモデル「日別実績の計算区分」を更新する 
	public void adUpCalAttr(CalAttrOfDailyPerformance calAttr);
	
	//ドメインモデル「日別実績の勤務種別」を更新する
	public void adUpWorkType(Optional<WorkTypeOfDailyPerformance> businessType);
	
	//ドメインモデル「日別実績の出退勤」を更新する 
	public void adUpTimeLeaving(Optional<TimeLeavingOfDailyPerformance> attendanceLeave);
	
	//ドメインモデル「日別実績の休憩時間帯」を更新する
	public void adUpBreakTime(List<BreakTimeOfDailyPerformance> breakTime);
	
	//ドメインモデル「日別実績の外出時間帯」を更新する
	public void adUpOutTime(Optional<OutingTimeOfDailyPerformance> outingTime);

	//ドメインモデル「日別実績の短時間勤務時間帯」を更新する
	public void adUpShortTime(Optional<ShortTimeOfDailyPerformance> shortTime);
	
	//ドメインモデル「日別実績の臨時出退勤」を更新する
	public void adUpTemporaryTime(Optional<TemporaryTimeOfDailyPerformance> tempTime);
	
	// ドメインモデル「日別実績の入退門」を更新する
	public void adUpAttendanceLeavingGate( Optional<AttendanceLeavingGateOfDaily> attendanceLeavingGate);
	
	// ドメインモデル「日別実績の勤怠時間」を更新する
	public void adUpAttendanceTime(Optional<AttendanceTimeOfDailyPerformance> attendanceTimeOfDailyPerformance);
	
	//ドメインモデル「日別実績の特定日区分」を更新する
	public void adUpSpecificDate(Optional<SpecificDateAttrOfDailyPerfor> specDateAttr);
	
	// ドメインモデル「日別実績の編集状態」を更新する
	public void adUpEditState(List<EditStateOfDailyPerformance> editState);
	
	public void clearExcludeEditState(List<EditStateOfDailyPerformance> editState);
	
	//ドメインモデル「日別実績の任意項目」を更新する
	public void adUpAnyItem( Optional<AnyItemValueOfDaily> anyItemValue);
	
	////ドメインモデル「日別実績の作業別勤怠時間」を更新する
	public void adUpAttendanceTimeByWork(Optional<AttendanceTimeByWorkOfDaily> attendancetimeByWork);
	
	// ドメインモデル「日別実績のPCログオン情報」を更新する
	public void adUpPCLogOn(Optional<PCLogOnInfoOfDaily> pcLogOnInfo);
	
	//ドメインモデル「日別実績の備考」を更新する
	public void adUpRemark(List<RemarksOfDailyPerform> remarks);
	
	/**
	 * ドメインモデル「社員の日別実績エラー一覧」を更新する
	 * @param errors domain error
	 * @param lstPairRemove List<Pair<employeeId, date>>
	 * @param hasRemoveError has remove error
	 */
	public void adUpEmpError(List<EmployeeDailyPerError> errors, List<Pair<String, GeneralDate>> lstPairRemove, boolean hasRemoveError);
	
	public List<IntegrationOfDaily> adTimeAndAnyItemAdUp(List<IntegrationOfDaily> dailys);
	
	public void removeConfirmApproval( List<IntegrationOfDaily> domainDaily, Optional<IdentityProcessUseSet> iPUSOpt, Optional<ApprovalProcessingUseSetting>  approvalSet);
}
