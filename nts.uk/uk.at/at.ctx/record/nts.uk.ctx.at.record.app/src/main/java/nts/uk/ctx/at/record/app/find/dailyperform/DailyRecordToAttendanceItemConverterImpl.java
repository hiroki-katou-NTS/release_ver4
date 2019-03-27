package nts.uk.ctx.at.record.app.find.dailyperform;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.record.app.find.dailyperform.affiliationInfor.dto.AffiliationInforOfDailyPerforDto;
import nts.uk.ctx.at.record.app.find.dailyperform.affiliationInfor.dto.BusinessTypeOfDailyPerforDto;
import nts.uk.ctx.at.record.app.find.dailyperform.attendanceleavinggate.dto.AttendanceLeavingGateOfDailyDto;
import nts.uk.ctx.at.record.app.find.dailyperform.calculationattribute.dto.CalcAttrOfDailyPerformanceDto;
import nts.uk.ctx.at.record.app.find.dailyperform.dto.AttendanceTimeDailyPerformDto;
import nts.uk.ctx.at.record.app.find.dailyperform.editstate.EditStateOfDailyPerformanceDto;
import nts.uk.ctx.at.record.app.find.dailyperform.erroralarm.dto.EmployeeDailyPerErrorDto;
import nts.uk.ctx.at.record.app.find.dailyperform.goout.dto.OutingTimeOfDailyPerformanceDto;
import nts.uk.ctx.at.record.app.find.dailyperform.optionalitem.dto.OptionalItemOfDailyPerformDto;
import nts.uk.ctx.at.record.app.find.dailyperform.pclogoninfor.dto.PCLogOnInforOfDailyPerformDto;
import nts.uk.ctx.at.record.app.find.dailyperform.remark.dto.RemarksOfDailyDto;
import nts.uk.ctx.at.record.app.find.dailyperform.resttime.dto.BreakTimeDailyDto;
import nts.uk.ctx.at.record.app.find.dailyperform.shorttimework.dto.ShortTimeOfDailyDto;
import nts.uk.ctx.at.record.app.find.dailyperform.specificdatetttr.dto.SpecificDateAttrOfDailyPerforDto;
import nts.uk.ctx.at.record.app.find.dailyperform.temporarytime.dto.TemporaryTimeOfDailyPerformanceDto;
import nts.uk.ctx.at.record.app.find.dailyperform.workinfo.dto.WorkInformationOfDailyDto;
import nts.uk.ctx.at.record.app.find.dailyperform.workrecord.dto.AttendanceTimeByWorkOfDailyDto;
import nts.uk.ctx.at.record.app.find.dailyperform.workrecord.dto.TimeLeavingOfDailyPerformanceDto;
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
import nts.uk.ctx.at.record.dom.dailyprocess.calc.converter.DailyRecordToAttendanceItemConverter;
import nts.uk.ctx.at.record.dom.editstate.EditStateOfDailyPerformance;
import nts.uk.ctx.at.record.dom.optitem.OptionalItem;
//import nts.uk.ctx.at.record.dom.optitem.OptionalItemAtr;
import nts.uk.ctx.at.record.dom.optitem.OptionalItemRepository;
import nts.uk.ctx.at.record.dom.raisesalarytime.SpecificDateAttrOfDailyPerfor;
import nts.uk.ctx.at.record.dom.shorttimework.ShortTimeOfDailyPerformance;
import nts.uk.ctx.at.record.dom.workinformation.WorkInfoOfDailyPerformance;
import nts.uk.ctx.at.record.dom.workrecord.erroralarm.EmployeeDailyPerError;
import nts.uk.ctx.at.record.dom.worktime.TemporaryTimeOfDailyPerformance;
import nts.uk.ctx.at.record.dom.worktime.TimeLeavingOfDailyPerformance;
import nts.uk.ctx.at.shared.dom.attendance.util.AttendanceItemUtil;
import nts.uk.ctx.at.shared.dom.attendance.util.item.ItemValue;
import nts.uk.shr.com.context.AppContexts;

//@Stateless
public class DailyRecordToAttendanceItemConverterImpl implements DailyRecordToAttendanceItemConverter {

	private final DailyRecordDto dailyRecord;
	
	private final Map<Integer, OptionalItem> optionalItems;
	
	private DailyRecordToAttendanceItemConverterImpl(Map<Integer, OptionalItem> optionalItems) {
		this.dailyRecord = new DailyRecordDto();
		this.optionalItems = optionalItems;
	}

	@Override
	public Optional<ItemValue> convert(int attendanceItemId) {
		List<ItemValue> items = AttendanceItemUtil.toItemValues(this.dailyRecord, Arrays.asList(attendanceItemId));
		return items.isEmpty() ? Optional.empty() : Optional.of(items.get(0));
	}

	@Override
	public List<ItemValue> convert(Collection<Integer> attendanceItemIds) {
		return AttendanceItemUtil.toItemValues(this.dailyRecord, attendanceItemIds);
	}

	
	@Override
	public void merge(ItemValue value) {
		AttendanceItemUtil.fromItemValues(dailyRecord, Arrays.asList(value));
	}

	@Override
	public void merge(Collection<ItemValue> values) {
		AttendanceItemUtil.fromItemValues(dailyRecord, values);
	}

	@Override
	public IntegrationOfDaily toDomain() {
		return (IntegrationOfDaily) dailyRecord.toDomain();
	}

	@Override
	public DailyRecordToAttendanceItemConverter setData(IntegrationOfDaily domain) {
		this.employeeId(domain.getAffiliationInfor().getEmployeeId());
		this.workingDate(domain.getAffiliationInfor().getYmd());
		this.withWorkInfo(domain.getWorkInformation());
		this.withCalcAttr(domain.getCalAttr());
		this.withAffiliationInfo(domain.getAffiliationInfor());
		this.withBusinessType(domain.getBusinessType().orElse(null));
		if(domain.getEmployeeError() != null && !domain.getEmployeeError().isEmpty()) {
			this.withEmployeeErrors(domain.getEmployeeError());
		}
		this.withOutingTime(domain.getOutingTime().orElse(null));
		this.withBreakTime(domain.getBreakTime());
		this.withAttendanceTime(domain.getAttendanceTimeOfDailyPerformance().orElse(null));
		this.withAttendanceTimeByWork(domain.getAttendancetimeByWork().orElse(null));
		this.withTimeLeaving(domain.getAttendanceLeave().orElse(null));
		this.withShortTime(domain.getShortTime().orElse(null));
		this.withSpecificDateAttr(domain.getSpecDateAttr().orElse(null));
		this.withAttendanceLeavingGate(domain.getAttendanceLeavingGate().orElse(null));
		this.withAnyItems(domain.getAnyItemValue().orElse(null));
		this.withEditStates(domain.getEditState());
		this.withTemporaryTime(domain.getTempTime().orElse(null));
		this.withPCLogInfo(domain.getPcLogOnInfo().orElse(null));
		this.withRemarks(domain.getRemarks());
		return this;
	}
	
	public static DailyRecordToAttendanceItemConverter builder(OptionalItemRepository optionalItem) {
		String compId = AppContexts.user().companyId();
		return new DailyRecordToAttendanceItemConverterImpl(
				optionalItem.findAll(compId).stream()
					.collect(Collectors.toMap(c -> c.getOptionalItemNo().v(), c -> c)));
	}
	
	public static DailyRecordToAttendanceItemConverter builder(Map<Integer, OptionalItem> optionalItems) {
		return new DailyRecordToAttendanceItemConverterImpl(optionalItems);
	}

	public DailyRecordToAttendanceItemConverter withWorkInfo(WorkInfoOfDailyPerformance domain) {
		this.dailyRecord.withWorkInfo(WorkInformationOfDailyDto.getDto(domain));
		return this;
	}

	public DailyRecordToAttendanceItemConverter withCalcAttr(CalAttrOfDailyPerformance domain) {
		this.dailyRecord.withCalcAttr(CalcAttrOfDailyPerformanceDto.getDto(domain));
		return this;
	}

	public DailyRecordToAttendanceItemConverter withBusinessType(WorkTypeOfDailyPerformance domain) {
		this.dailyRecord.withBusinessType(BusinessTypeOfDailyPerforDto.getDto(domain));
		return this;
	}

	public DailyRecordToAttendanceItemConverter withAffiliationInfo(AffiliationInforOfDailyPerfor domain) {
		this.dailyRecord.withAffiliationInfo(AffiliationInforOfDailyPerforDto.getDto(domain));
		return this;
	}

	public DailyRecordToAttendanceItemConverter withEmployeeErrors(List<EmployeeDailyPerError> domain) {
		this.dailyRecord.withErrors(domain.stream().map(x -> EmployeeDailyPerErrorDto.getDto(x)).collect(Collectors.toList()));
		return this;
	}

	public DailyRecordToAttendanceItemConverter withOutingTime(OutingTimeOfDailyPerformance domain) {
		this.dailyRecord.outingTime(OutingTimeOfDailyPerformanceDto.getDto(domain));
		return this;
	}

	public DailyRecordToAttendanceItemConverter withBreakTime(BreakTimeOfDailyPerformance domain) {
		this.dailyRecord.addBreakTime(BreakTimeDailyDto.getDto(domain));
		return this;
	}
	
	public DailyRecordToAttendanceItemConverter withBreakTime(List<BreakTimeOfDailyPerformance> domain) {
		this.dailyRecord.breakTime(domain.stream().map(c -> BreakTimeDailyDto.getDto(c)).collect(Collectors.toList()));
		return this;
	}

	public DailyRecordToAttendanceItemConverter withAttendanceTime(AttendanceTimeOfDailyPerformance domain) {
		this.dailyRecord.attendanceTime(AttendanceTimeDailyPerformDto.getDto(domain));
		return this;
	}

	public DailyRecordToAttendanceItemConverter withAttendanceTimeByWork(AttendanceTimeByWorkOfDaily domain) {
		this.dailyRecord.attendanceTimeByWork(AttendanceTimeByWorkOfDailyDto.getDto(domain));
		return this;
	}

	public DailyRecordToAttendanceItemConverter withTimeLeaving(TimeLeavingOfDailyPerformance domain) {
		this.dailyRecord.timeLeaving(TimeLeavingOfDailyPerformanceDto.getDto(domain));
		return this;
	}

	public DailyRecordToAttendanceItemConverter withShortTime(ShortTimeOfDailyPerformance domain) {
		this.dailyRecord.shortWorkTime(ShortTimeOfDailyDto.getDto(domain));
		return this;
	}

	public DailyRecordToAttendanceItemConverter withSpecificDateAttr(SpecificDateAttrOfDailyPerfor domain) {
		this.dailyRecord.specificDateAttr(SpecificDateAttrOfDailyPerforDto.getDto(domain));
		return this;
	}

	public DailyRecordToAttendanceItemConverter withAttendanceLeavingGate(AttendanceLeavingGateOfDaily domain) {
		this.dailyRecord.attendanceLeavingGate(AttendanceLeavingGateOfDailyDto.getDto(domain));
		return this;
	}

	public DailyRecordToAttendanceItemConverter withAnyItems(AnyItemValueOfDaily domain) {
		this.dailyRecord.optionalItems(OptionalItemOfDailyPerformDto.getDto(domain, this.optionalItems));
		return this;
	}

	public DailyRecordToAttendanceItemConverter withEditStates(EditStateOfDailyPerformance domain) {
		this.dailyRecord.addEditStates(EditStateOfDailyPerformanceDto.getDto(domain));
		return this;
	}

	public DailyRecordToAttendanceItemConverter withEditStates(List<EditStateOfDailyPerformance> domain) {
//		List<Integer> current = this.dailyRecord.getEditStates().stream().map(c -> c.getAttendanceItemId()).collect(Collectors.toList());
		this.dailyRecord.editStates(domain.stream().map(c -> EditStateOfDailyPerformanceDto.getDto(c)).collect(Collectors.toList()));
		return this;
	}

	public DailyRecordToAttendanceItemConverter withTemporaryTime(TemporaryTimeOfDailyPerformance domain) {
		this.dailyRecord.temporaryTime(TemporaryTimeOfDailyPerformanceDto.getDto(domain));
		return this;
	}

	public DailyRecordToAttendanceItemConverter withPCLogInfo(PCLogOnInfoOfDaily domain) {
		this.dailyRecord.pcLogInfo(PCLogOnInforOfDailyPerformDto.from(domain));
		return this;
	}

	@Override
	public DailyRecordToAttendanceItemConverter withRemark(RemarksOfDailyPerform domain) {
		this.dailyRecord.addRemarks(RemarksOfDailyDto.getDto(domain));
		return this;
	}

	@Override
	public DailyRecordToAttendanceItemConverter withRemarks(List<RemarksOfDailyPerform> domain) {
		this.dailyRecord.remarks(domain.stream().map(c -> RemarksOfDailyDto.getDto(c)).collect(Collectors.toList()));
		return this;
	}
	
	public DailyRecordToAttendanceItemConverter employeeId(String employeeId) {
		this.dailyRecord.employeeId(employeeId);
		return this;
	}

	public DailyRecordToAttendanceItemConverter workingDate(GeneralDate workingDate) {
		this.dailyRecord.workingDate(workingDate);
		return this;
	}


	public DailyRecordToAttendanceItemConverter completed(){
		return this;
	}

	@Override
	public WorkInfoOfDailyPerformance workInfo() {
		return this.dailyRecord.getWorkInfo().toDomain(this.dailyRecord.employeeId(), this.dailyRecord.workingDate());
	}

	@Override
	public CalAttrOfDailyPerformance calcAttr() {
		return this.dailyRecord.getCalcAttr().toDomain(this.dailyRecord.employeeId(), this.dailyRecord.workingDate());
	}

	@Override
	public Optional<WorkTypeOfDailyPerformance> businessType() {
		return this.dailyRecord.getBusinessType().map(d -> d.toDomain(this.dailyRecord.employeeId(), this.dailyRecord.workingDate()));
	}

	@Override
	public AffiliationInforOfDailyPerfor affiliationInfo() {
		return this.dailyRecord.getAffiliationInfo().toDomain(this.dailyRecord.employeeId(), this.dailyRecord.workingDate());
	}

	@Override
	public Optional<OutingTimeOfDailyPerformance> outingTime() {
		return this.dailyRecord.getOutingTime().map(d -> d.toDomain(this.dailyRecord.employeeId(), this.dailyRecord.workingDate()));
	}

	@Override
	public List<BreakTimeOfDailyPerformance> breakTime() {
		return this.dailyRecord.getBreakTime().stream().map(d ->
				d.toDomain(this.dailyRecord.employeeId(), this.dailyRecord.workingDate())).collect(Collectors.toList());
	}

	@Override
	public Optional<AttendanceTimeOfDailyPerformance> attendanceTime() {
		return this.dailyRecord.getAttendanceTime().map(d -> d.toDomain(this.dailyRecord.employeeId(), this.dailyRecord.workingDate()));
	}

	@Override
	public Optional<AttendanceTimeByWorkOfDaily> attendanceTimeByWork() {
		return this.dailyRecord.getAttendanceTimeByWork().map(d -> d.toDomain(this.dailyRecord.employeeId(), this.dailyRecord.workingDate()));
	}

	@Override
	public Optional<TimeLeavingOfDailyPerformance> timeLeaving() {
		return this.dailyRecord.getTimeLeaving().map(d -> d.toDomain(this.dailyRecord.employeeId(), this.dailyRecord.workingDate()));
	}

	@Override
	public Optional<ShortTimeOfDailyPerformance> shortTime() {
		return this.dailyRecord.getShortWorkTime().map(d -> d.toDomain(this.dailyRecord.employeeId(), this.dailyRecord.workingDate()));
	}

	@Override
	public Optional<SpecificDateAttrOfDailyPerfor> specificDateAttr() {
		return this.dailyRecord.getSpecificDateAttr().map(d -> d.toDomain(this.dailyRecord.employeeId(), this.dailyRecord.workingDate()));
	}

	@Override
	public Optional<AttendanceLeavingGateOfDaily> attendanceLeavingGate() {
		return this.dailyRecord.getAttendanceLeavingGate().map(d -> d.toDomain(this.dailyRecord.employeeId(), this.dailyRecord.workingDate()));
	}

	@Override
	public Optional<AnyItemValueOfDaily> anyItems() {
		this.dailyRecord.getOptionalItem().ifPresent(c -> {
			if(optionalItems != null) {
				c.correctItems(optionalItems);
			}
		});
		return this.dailyRecord.getOptionalItem().map(d -> d.toDomain(this.dailyRecord.employeeId(), this.dailyRecord.workingDate()));
	}

	@Override
	public List<EditStateOfDailyPerformance> editStates() {
		return this.dailyRecord.getEditStates().stream().map(d ->
						d.toDomain(this.dailyRecord.employeeId(), this.dailyRecord.workingDate())).collect(Collectors.toList());
	}

	@Override
	public Optional<TemporaryTimeOfDailyPerformance> temporaryTime() {
		return this.dailyRecord.getTemporaryTime().map(d -> d.toDomain(this.dailyRecord.employeeId(), this.dailyRecord.workingDate()));
	}

	@Override
	public Optional<PCLogOnInfoOfDaily> pcLogInfo() {
		return this.dailyRecord.getPcLogInfo().map(d -> d.toDomain(this.dailyRecord.employeeId(), this.dailyRecord.workingDate()));
	}

	@Override
	public List<RemarksOfDailyPerform> remarks() {
		return this.dailyRecord.getRemarks().stream().map(d ->
						d.toDomain(this.dailyRecord.employeeId(), this.dailyRecord.workingDate())).collect(Collectors.toList());
	}
}
