package nts.uk.ctx.at.record.app.command.dailyperform;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import lombok.Getter;
import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.record.app.command.dailyperform.affiliationInfor.AffiliationInforOfDailyPerformCommand;
import nts.uk.ctx.at.record.app.command.dailyperform.affiliationInfor.BusinessTypeOfDailyPerformCommand;
import nts.uk.ctx.at.record.app.command.dailyperform.attendanceleavinggate.AttendanceLeavingGateOfDailyCommand;
import nts.uk.ctx.at.record.app.command.dailyperform.attendanceleavinggate.PCLogInfoOfDailyCommand;
import nts.uk.ctx.at.record.app.command.dailyperform.attendancetime.AttendanceTimeOfDailyPerformCommand;
import nts.uk.ctx.at.record.app.command.dailyperform.breaktime.BreakTimeOfDailyPerformanceCommand;
import nts.uk.ctx.at.record.app.command.dailyperform.calculationattribute.CalcAttrOfDailyPerformanceCommand;
import nts.uk.ctx.at.record.app.command.dailyperform.editstate.EditStateOfDailyPerformCommand;
import nts.uk.ctx.at.record.app.command.dailyperform.erroralarm.EmployeeDailyPerErrorCommand;
import nts.uk.ctx.at.record.app.command.dailyperform.goout.OutingTimeOfDailyPerformanceCommand;
import nts.uk.ctx.at.record.app.command.dailyperform.optionalitem.OptionalItemOfDailyPerformCommand;
import nts.uk.ctx.at.record.app.command.dailyperform.remark.RemarkOfDailyCommand;
import nts.uk.ctx.at.record.app.command.dailyperform.shorttimework.ShortTimeOfDailyCommand;
import nts.uk.ctx.at.record.app.command.dailyperform.specificdatetttr.SpecificDateAttrOfDailyCommand;
import nts.uk.ctx.at.record.app.command.dailyperform.temporarytime.TemporaryTimeOfDailyPerformanceCommand;
import nts.uk.ctx.at.record.app.command.dailyperform.workinfo.WorkInformationOfDailyPerformCommand;
import nts.uk.ctx.at.record.app.command.dailyperform.workrecord.AttendanceTimeByWorkOfDailyCommand;
import nts.uk.ctx.at.record.app.command.dailyperform.workrecord.TimeLeavingOfDailyPerformanceCommand;
import nts.uk.ctx.at.record.app.find.dailyperform.DailyRecordDto;
import nts.uk.ctx.at.record.dom.actualworkinghours.AttendanceTimeOfDailyPerformance;
import nts.uk.ctx.at.record.dom.actualworkinghours.daily.workrecord.AttendanceTimeByWorkOfDaily;
import nts.uk.ctx.at.record.dom.breakorgoout.BreakTimeOfDailyPerformance;
import nts.uk.ctx.at.record.dom.breakorgoout.OutingTimeOfDailyPerformance;
import nts.uk.ctx.at.record.dom.daily.attendanceleavinggate.AttendanceLeavingGateOfDaily;
import nts.uk.ctx.at.record.dom.daily.attendanceleavinggate.PCLogOnInfoOfDaily;
import nts.uk.ctx.at.record.dom.daily.optionalitemtime.AnyItemValueOfDaily;
import nts.uk.ctx.at.record.dom.raisesalarytime.SpecificDateAttrOfDailyPerfor;
import nts.uk.ctx.at.record.dom.shorttimework.ShortTimeOfDailyPerformance;
import nts.uk.ctx.at.record.dom.worktime.TemporaryTimeOfDailyPerformance;
import nts.uk.ctx.at.record.dom.worktime.TimeLeavingOfDailyPerformance;
import nts.uk.ctx.at.shared.app.util.attendanceitem.DailyWorkCommonCommand;
import nts.uk.ctx.at.shared.dom.scherec.dailyattdcal.dailyattendance.attendancetime.TemporaryTimeOfDailyAttd;
import nts.uk.ctx.at.shared.dom.scherec.dailyattdcal.dailyattendance.attendancetime.TimeLeavingOfDailyAttd;
import nts.uk.ctx.at.shared.dom.scherec.dailyattdcal.dailyattendance.breakouting.OutingTimeOfDailyAttd;
import nts.uk.ctx.at.shared.dom.scherec.dailyattdcal.dailyattendance.breakouting.breaking.BreakTimeOfDailyAttd;
import nts.uk.ctx.at.shared.dom.scherec.dailyattdcal.dailyattendance.converter.util.item.ConvertibleAttendanceItem;
import nts.uk.ctx.at.shared.dom.scherec.dailyattdcal.dailyattendance.converter.util.item.ItemValue;
import nts.uk.ctx.at.shared.dom.scherec.dailyattdcal.dailyattendance.dailyattendancework.IntegrationOfDaily;
import nts.uk.ctx.at.shared.dom.scherec.dailyattdcal.dailyattendance.entranceandexit.AttendanceLeavingGateOfDailyAttd;
import nts.uk.ctx.at.shared.dom.scherec.dailyattdcal.dailyattendance.entranceandexit.PCLogOnInfoOfDailyAttd;
import nts.uk.ctx.at.shared.dom.scherec.dailyattdcal.dailyattendance.optionalitemvalue.AnyItemValueOfDailyAttd;
import nts.uk.ctx.at.shared.dom.scherec.dailyattdcal.dailyattendance.paytime.SpecificDateAttrOfDailyAttd;
import nts.uk.ctx.at.shared.dom.scherec.dailyattdcal.dailyattendance.shortworktime.ShortTimeOfDailyAttd;
import nts.uk.ctx.at.shared.dom.scherec.dailyattdcal.dailyattendance.worktime.AttendanceTimeOfDailyAttendance;

public class DailyRecordWorkCommand extends DailyWorkCommonCommand {
	
	private final List<ItemValue> itemValues = new ArrayList<>();

	/** 勤務情報： 日別実績の勤務情報 */
	@Getter
	private final WorkInformationOfDailyPerformCommand workInfo = new WorkInformationOfDailyPerformCommand();

	/** 計算区分： 日別実績の計算区分 */
	@Getter
	private final CalcAttrOfDailyPerformanceCommand calcAttr = new CalcAttrOfDailyPerformanceCommand();

	/** 所属情報： 日別実績の所属情報 */
	@Getter
	private final AffiliationInforOfDailyPerformCommand affiliationInfo = new AffiliationInforOfDailyPerformCommand();
	
	/** 所属情報： 日別実績の所属情報 */
	@Getter
	private final BusinessTypeOfDailyPerformCommand businessType = new BusinessTypeOfDailyPerformCommand();

	/** エラー一覧： 社員の日別実績エラー一覧 */
	// TODO: list?
	@Getter
	private final EmployeeDailyPerErrorCommand errors = new EmployeeDailyPerErrorCommand();

	/** 外出時間帯: 日別実績の外出時間帯 */
	@Getter
	private final OutingTimeOfDailyPerformanceCommand outingTime = new OutingTimeOfDailyPerformanceCommand();

	/** 休憩時間帯: 日別実績の休憩時間帯 */
	@Getter
	private final BreakTimeOfDailyPerformanceCommand breakTime = new BreakTimeOfDailyPerformanceCommand();

	/** 勤怠時間: 日別実績の勤怠時間 */
	@Getter
	private final AttendanceTimeOfDailyPerformCommand attendanceTime = new AttendanceTimeOfDailyPerformCommand();

	/** 作業別勤怠時間: 日別実績の作業別勤怠時間 */
	@Getter
	private final AttendanceTimeByWorkOfDailyCommand attendanceTimeByWork = new AttendanceTimeByWorkOfDailyCommand();

	/** 出退勤: 日別実績の出退勤 */
	@Getter
	private final TimeLeavingOfDailyPerformanceCommand timeLeaving = new TimeLeavingOfDailyPerformanceCommand();

	/** 短時間勤務時間帯: 日別実績の短時間勤務時間帯 */
	@Getter
	private final ShortTimeOfDailyCommand shortWorkTime = new ShortTimeOfDailyCommand();

	/** 特定日区分: 日別実績の特定日区分 */
	@Getter
	private final SpecificDateAttrOfDailyCommand specificDateAttr = new SpecificDateAttrOfDailyCommand();

	/** 入退門: 日別実績の入退門 */
	@Getter
	private final AttendanceLeavingGateOfDailyCommand attendanceLeavingGate = new AttendanceLeavingGateOfDailyCommand();

	/** 任意項目: 日別実績の任意項目 */
	@Getter
	private final OptionalItemOfDailyPerformCommand optionalItem = new OptionalItemOfDailyPerformCommand();

	/** 編集状態: 日別実績の編集状態 */
	@Getter
	private final EditStateOfDailyPerformCommand editState = new EditStateOfDailyPerformCommand();

	/** 臨時出退勤: 日別実績の臨時出退勤 */
	@Getter
	private final TemporaryTimeOfDailyPerformanceCommand temporaryTime = new TemporaryTimeOfDailyPerformanceCommand();

	/** PCログオン情報: 日別実績のPCログオン情報 */
	@Getter
	private final PCLogInfoOfDailyCommand pcLogInfo = new PCLogInfoOfDailyCommand();
	
	/** PCログオン情報: 日別実績のPCログオン情報 */
	@Getter
	private final RemarkOfDailyCommand remarks = new RemarkOfDailyCommand();

	public DailyWorkCommonCommand getCommand(String domain) {
		switch (domain) {
		case DAILY_WORK_INFO_NAME:
			return this.workInfo;
		case DAILY_CALCULATION_ATTR_NAME:
			return this.calcAttr;
		case DAILY_AFFILIATION_INFO_NAME:
			return this.affiliationInfo;
		case DAILY_BUSINESS_TYPE_NAME:
			return this.businessType;
		case DAILY_OUTING_TIME_NAME:
			return this.outingTime;
		case DAILY_BREAK_TIME_NAME:
			return this.breakTime;
		case DAILY_ATTENDANCE_TIME_NAME:
			return this.attendanceTime;
		case DAILY_ATTENDANCE_TIME_BY_WORK_NAME:
			return this.attendanceTimeByWork;
		case DAILY_ATTENDACE_LEAVE_NAME:
			return this.timeLeaving;
		case DAILY_SHORT_TIME_NAME:
			return this.shortWorkTime;
		case DAILY_SPECIFIC_DATE_ATTR_NAME:
			return this.specificDateAttr;
		case DAILY_ATTENDANCE_LEAVE_GATE_NAME:
			return this.attendanceLeavingGate;
		case DAILY_OPTIONAL_ITEM_NAME:
			return this.optionalItem;
		case DAILY_EDIT_STATE_NAME:
			return this.editState;
		case DAILY_TEMPORARY_TIME_NAME:
			return this.temporaryTime;
		case DAILY_PC_LOG_INFO_NAME:
			return this.pcLogInfo;
		case DAILY_REMARKS_NAME:
			return this.remarks;
		default:
			return null;
		}
	}
	
	public List<String> getAvailableLayout(){
		return Arrays.asList(DAILY_WORK_INFO_NAME, DAILY_CALCULATION_ATTR_NAME, DAILY_AFFILIATION_INFO_NAME,
				DAILY_BUSINESS_TYPE_NAME, DAILY_OUTING_TIME_NAME, DAILY_BREAK_TIME_NAME, DAILY_ATTENDANCE_TIME_NAME,
				DAILY_ATTENDANCE_TIME_BY_WORK_NAME, DAILY_ATTENDACE_LEAVE_NAME, DAILY_SHORT_TIME_NAME,
				DAILY_SPECIFIC_DATE_ATTR_NAME, DAILY_ATTENDANCE_LEAVE_GATE_NAME, DAILY_OPTIONAL_ITEM_NAME,
				DAILY_EDIT_STATE_NAME, DAILY_TEMPORARY_TIME_NAME, DAILY_PC_LOG_INFO_NAME, DAILY_REMARKS_NAME);
	}

	@Override
	public void setRecords(ConvertibleAttendanceItem item) {
		DailyRecordDto fullDto = (DailyRecordDto) item;
		this.workInfo.setRecords(fullDto.getWorkInfo());
		this.calcAttr.setRecords(fullDto.getCalcAttr());
		this.affiliationInfo.setRecords(fullDto.getAffiliationInfo());
		fullDto.getErrors().stream().forEach(c -> this.errors.setRecords(c));
		this.outingTime.setRecords(fullDto.getOutingTime().orElse(null));
		this.breakTime.setRecords(fullDto.getBreakTime());
		this.attendanceTime.setRecords(fullDto.getAttendanceTime().orElse(null));
		this.attendanceTimeByWork.setRecords(fullDto.getAttendanceTimeByWork().orElse(null));
		this.timeLeaving.setRecords(fullDto.getTimeLeaving().orElse(null));
		this.shortWorkTime.setRecords(fullDto.getShortWorkTime().orElse(null));
		this.specificDateAttr.setRecords(fullDto.getSpecificDateAttr().orElse(null));
		this.attendanceLeavingGate.setRecords(fullDto.getAttendanceLeavingGate().orElse(null));
		this.optionalItem.setRecords(fullDto.getOptionalItem().orElse(null));
		fullDto.getEditStates().stream().forEach(c -> this.editState.setRecords(c));
		this.temporaryTime.setRecords(fullDto.getTemporaryTime().orElse(null));
		this.pcLogInfo.setRecords(fullDto.getPcLogInfo().orElse(null));
		this.remarks.setRecords(fullDto.getRemarks());
	}

	@Override
	public void forEmployee(String employeId) {
		super.forEmployee(employeId);
		this.attendanceTime.forEmployee(employeId);
		this.workInfo.forEmployee(employeId);
		this.calcAttr.forEmployee(employeId);
		this.affiliationInfo.forEmployee(employeId);
		this.businessType.forEmployee(employeId);
		this.errors.forEmployee(employeId);
		this.outingTime.forEmployee(employeId);
		this.breakTime.forEmployee(employeId);
		this.attendanceTimeByWork.forEmployee(employeId);
		this.timeLeaving.forEmployee(employeId);
		this.shortWorkTime.forEmployee(employeId);
		this.specificDateAttr.forEmployee(employeId);
		this.attendanceLeavingGate.forEmployee(employeId);
		this.optionalItem.forEmployee(employeId);
		this.editState.forEmployee(employeId);
		this.temporaryTime.forEmployee(employeId);
		this.pcLogInfo.forEmployee(employeId);
		this.remarks.forEmployee(employeId);
	}

	@Override
	public void withDate(GeneralDate date) {
		super.withDate(date);
		this.attendanceTime.withDate(date);
		this.workInfo.withDate(date);
		this.calcAttr.withDate(date);
		this.affiliationInfo.withDate(date);
		this.businessType.withDate(date);
		this.errors.withDate(date);
		this.outingTime.withDate(date);
		this.breakTime.withDate(date);
		this.attendanceTimeByWork.withDate(date);
		this.timeLeaving.withDate(date);
		this.shortWorkTime.withDate(date);
		this.specificDateAttr.withDate(date);
		this.attendanceLeavingGate.withDate(date);
		this.optionalItem.withDate(date);
		this.editState.withDate(date);
		this.temporaryTime.withDate(date);
		this.pcLogInfo.withDate(date);
		this.remarks.withDate(date);
	}

	public DailyRecordDto toDto() {
		return DailyRecordDto.builder()
				.breakTime(breakTime.toDto())
				.editStates(editState.toDto())
				.attendanceLeavingGateO(attendanceLeavingGate.toDto())
				.attendanceTimeO(attendanceTime.toDto())
				.attendanceTimeByWorkO(attendanceTimeByWork.toDto())
				.employeeId(getEmployeeId())
				.optionalItemsO(optionalItem.toDto())
				.outingTimeO(outingTime.toDto())
				.pcLogInfoO(pcLogInfo.toDto())
				.shortWorkTimeO(shortWorkTime.toDto())
				.remarks(remarks.toDto())
				.specificDateAttrO(specificDateAttr.toDto())
				.temporaryTimeO(temporaryTime.toDto())
				.timeLeavingO(timeLeaving.toDto())
				.withAffiliationInfo(affiliationInfo.toDto())
				.withCalcAttr(calcAttr.toDto())
//				.withErrors(errors.getData())
				.withWorkInfo(workInfo.toDto())
				.workingDate(getWorkDate())
				.withBusinessTypeO(businessType.toDto())
				.complete();
	}

	public static DailyRecordWorkCommand open() {
		return new DailyRecordWorkCommand();
	}

	public DailyRecordWorkCommand forEmployeeId(String employeeId) {
		this.forEmployee(employeeId);
		return this;
	}

	public DailyRecordWorkCommand withWokingDate(GeneralDate workDate) {
		this.withDate(workDate);
		return this;
	}
	
	public DailyRecordWorkCommand forEmployeeIdAndDate(String employeeId, GeneralDate date) {
		this.forEmployee(employeeId);
		this.withDate(date);
		return this;
	}

	public DailyRecordWorkCommand fromItems(List<ItemValue> itemValues) {
		this.itemValues.addAll(itemValues);
		return this;
	}

	public DailyRecordWorkCommand withData(DailyRecordDto data) {
		this.setRecords(data);
		return this;
	}
	
	public IntegrationOfDaily toDomain() {
		Optional<PCLogOnInfoOfDaily> pCLogOnInfoOfDaily = this.getPcLogInfo().toDomain();
		Optional<PCLogOnInfoOfDailyAttd> pCLogOnInfoOfDailyAttd = pCLogOnInfoOfDaily.isPresent()
				? Optional.of(pCLogOnInfoOfDaily.get().getTimeZone())
				: Optional.empty();
				
		Optional<OutingTimeOfDailyPerformance> outingTimeOfDailyPerformance = this.getOutingTime().toDomain();
		Optional<OutingTimeOfDailyAttd> outingTimeOfDailyAttd = outingTimeOfDailyPerformance.isPresent()
				? Optional.of(outingTimeOfDailyPerformance.get().getOutingTime())
				: Optional.empty();
				
		List<BreakTimeOfDailyPerformance> breakTimeOfDailyPerformance = this.getBreakTime().toDomain();
		List<BreakTimeOfDailyAttd> breakTimeOfDailyAttd = breakTimeOfDailyPerformance.stream().map(c->c.getTimeZone()).collect(Collectors.toList());
				
		Optional<AttendanceTimeOfDailyPerformance> attendanceTimeOfDailyPer = this.getAttendanceTime().toDomain();
		Optional<AttendanceTimeOfDailyAttendance> attendanceTimeOfDailyAttd = attendanceTimeOfDailyPer.isPresent()
				? Optional.of(attendanceTimeOfDailyPer.get().getTime())
				: Optional.empty();
				
		Optional<TimeLeavingOfDailyPerformance> timeLeavingOfDailyPerformance = this.getTimeLeaving().toDomain();
		Optional<TimeLeavingOfDailyAttd> timeLeavingOfDailyAttd = timeLeavingOfDailyPerformance.isPresent()
				? Optional.of(timeLeavingOfDailyPerformance.get().getAttendance())
				: Optional.empty();
				
		Optional<ShortTimeOfDailyPerformance> shortTimeOfDailyPerformance = this.getShortWorkTime().toDomain();
		Optional<ShortTimeOfDailyAttd> shortTimeOfDailyAttd = shortTimeOfDailyPerformance.isPresent()
				? Optional.of(shortTimeOfDailyPerformance.get().getTimeZone())
				: Optional.empty();
				
		Optional<SpecificDateAttrOfDailyPerfor> specificDateAttrOfDailyPerfor = this.getSpecificDateAttr().toDomain();
		Optional<SpecificDateAttrOfDailyAttd> specificDateAttrOfDailyAttd = specificDateAttrOfDailyPerfor.isPresent()
				? Optional.of(specificDateAttrOfDailyPerfor.get().getSpecificDay())
				: Optional.empty();
				
		Optional<AttendanceLeavingGateOfDaily> attendanceLeavingGateOfDaily = this.getAttendanceLeavingGate().toDomain();
		Optional<AttendanceLeavingGateOfDailyAttd> attendanceLeavingGateOfDailyAttd = attendanceLeavingGateOfDaily.isPresent()
				? Optional.of(attendanceLeavingGateOfDaily.get().getTimeZone())
				: Optional.empty();
				
		Optional<AnyItemValueOfDaily> anyItemValueOfDaily = this.getOptionalItem().toDomain();
		Optional<AnyItemValueOfDailyAttd> anyItemValueOfDailyAttd = anyItemValueOfDaily.isPresent()
				? Optional.of(anyItemValueOfDaily.get().getAnyItem())
				: Optional.empty();
				
		Optional<TemporaryTimeOfDailyPerformance> temporaryTimeOfDailyPerformance = this.getTemporaryTime().toDomain();
		Optional<TemporaryTimeOfDailyAttd> temporaryTimeOfDailyAttd = temporaryTimeOfDailyPerformance.isPresent()
				? Optional.of(temporaryTimeOfDailyPerformance.get().getAttendance())
				: Optional.empty();
		return new IntegrationOfDaily(
										this.getAffiliationInfo().getEmployeeId(),
										this.getAffiliationInfo().getWorkDate(),
										this.getWorkInfo().toDomain().getWorkInformation(), 
										this.getCalcAttr().toDomain().getCalcategory(), 
										this.getAffiliationInfo().toDomain().getAffiliationInfor(),
//										this.getBusinessType().toDomain(), 
										pCLogOnInfoOfDailyAttd, //pcLogOnInfo
										this.getErrors().toDomain(), //employeeError
										outingTimeOfDailyAttd, //outingTime
										breakTimeOfDailyAttd, //breakTime
										attendanceTimeOfDailyAttd, //attendanceTimeOfDailyPerformance
//										this.getAttendanceTimeByWork().toDomain(), 
										timeLeavingOfDailyAttd, //attendanceLeave
										shortTimeOfDailyAttd, //shortTime
										specificDateAttrOfDailyAttd, //specDateAttr
										attendanceLeavingGateOfDailyAttd, //attendanceLeavingGate
										anyItemValueOfDailyAttd, //anyItemValue
										this.getEditState().toDomain().stream().map(c->c.getEditState()).collect(Collectors.toList()), 
										temporaryTimeOfDailyAttd,
										this.getRemarks().toDomain().stream().map(c->c.getRemarks()).collect(Collectors.toList()));
	}

	public List<ItemValue> itemValues() {
		return new ArrayList<>(this.itemValues);
	}

	@Override
	public void updateData(Object data) {
	}
}
