package nts.uk.ctx.at.record.app.find.dailyperform.goout.dto;

import java.util.List;
import java.util.Optional;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import lombok.Data;
import lombok.EqualsAndHashCode;
import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.record.app.find.dailyperform.common.TimeStampDto;
import nts.uk.ctx.at.record.app.find.dailyperform.common.WithActualTimeStampDto;
import nts.uk.ctx.at.record.app.find.dailyperform.customjson.CustomGeneralDateSerializer;
import nts.uk.ctx.at.record.dom.breakorgoout.OutingTimeOfDailyPerformance;
import nts.uk.ctx.at.shared.app.util.attendanceitem.ConvertHelper;
import nts.uk.ctx.at.shared.dom.attendance.util.item.AttendanceItemDataGate;
import nts.uk.ctx.at.shared.dom.common.time.AttendanceTime;
import nts.uk.ctx.at.shared.dom.scherec.dailyattdcal.dailyattendance.breakouting.OutingFrameNo;
import nts.uk.ctx.at.shared.dom.scherec.dailyattdcal.dailyattendance.breakouting.OutingTimeOfDailyAttd;
import nts.uk.ctx.at.shared.dom.scherec.dailyattdcal.dailyattendance.breakouting.OutingTimeSheet;
import nts.uk.ctx.at.shared.dom.scherec.dailyattdcal.dailyattendance.common.TimeActualStamp;
import nts.uk.ctx.at.shared.dom.scherec.dailyattdcal.dailyattendance.common.timestamp.WorkStamp;
import nts.uk.ctx.at.shared.dom.scherec.dailyattdcal.dailyattendance.converter.util.ItemConst;
import nts.uk.ctx.at.shared.dom.scherec.dailyattdcal.dailyattendance.converter.util.anno.AttendanceItemLayout;
import nts.uk.ctx.at.shared.dom.scherec.dailyattdcal.dailyattendance.converter.util.anno.AttendanceItemRoot;
import nts.uk.ctx.at.shared.dom.scherec.dailyattdcal.dailyattendance.converter.util.item.AttendanceItemCommon;

@Data
@EqualsAndHashCode(callSuper = false)
@AttendanceItemRoot(rootName = ItemConst.DAILY_OUTING_TIME_NAME)
public class OutingTimeOfDailyPerformanceDto extends AttendanceItemCommon {

	@Override
	public String rootName() { return DAILY_OUTING_TIME_NAME; }
	/***/
	private static final long serialVersionUID = 1L;
	
	private String employeeId;
	
	@JsonDeserialize(using = CustomGeneralDateSerializer.class)
	private GeneralDate ymd;
	
	@AttendanceItemLayout(layout = LAYOUT_A, jpPropertyName = TIME_ZONE, 
			listMaxLength = 10, indexField = DEFAULT_INDEX_FIELD_NAME)
	private List<OutingTimeZoneDto> timeZone;
	
	public static OutingTimeOfDailyPerformanceDto getDto(OutingTimeOfDailyPerformance domain) {
		OutingTimeOfDailyPerformanceDto dto = new OutingTimeOfDailyPerformanceDto();
		if (domain != null) {
			dto.setEmployeeId(domain.getEmployeeId());
			dto.setYmd(domain.getYmd());
			dto.setTimeZone(ConvertHelper.mapTo(domain.getOutingTime().getOutingTimeSheets(),
					(c) -> new OutingTimeZoneDto(
							c.getOutingFrameNo().v(),
							TimeStampDto.createTimeStamp(c.getGoOut() != null ? c.getGoOut().orElse(null) : null),
							TimeStampDto.createTimeStamp(c.getComeBack() != null ? c.getComeBack().orElse(null) : null),
							c.getReasonForGoOut() == null ? 0 : c.getReasonForGoOut().value, 
							c.getOutingTimeCalculation() == null ? 0 : c.getOutingTimeCalculation().valueAsMinutes(),
							c.getOutingTime() == null ? 0 : c.getOutingTime().valueAsMinutes())));
			dto.exsistData();
		}
		return dto;
	}
	public static OutingTimeOfDailyPerformanceDto getDto(String employeeID,GeneralDate ymd,OutingTimeOfDailyAttd domain) {
		OutingTimeOfDailyPerformanceDto dto = new OutingTimeOfDailyPerformanceDto();
		if (domain != null) {
			dto.setEmployeeId(employeeID);
			dto.setYmd(ymd);
			dto.setTimeZone(ConvertHelper.mapTo(domain.getOutingTimeSheets(),
					(c) -> new OutingTimeZoneDto(
							c.getOutingFrameNo().v(),
							TimeStampDto.createTimeStamp(c.getGoOut() != null ? c.getGoOut().orElse(null) : null),
							TimeStampDto.createTimeStamp(c.getComeBack() != null ? c.getComeBack().orElse(null) : null),
							c.getReasonForGoOut() == null ? 0 : c.getReasonForGoOut().value, 
							c.getOutingTimeCalculation() == null ? 0 : c.getOutingTimeCalculation().valueAsMinutes(),
							c.getOutingTime() == null ? 0 : c.getOutingTime().valueAsMinutes())));
			dto.exsistData();
		}
		return dto;
	}
	
	@Override
	public OutingTimeOfDailyPerformanceDto clone() {
		OutingTimeOfDailyPerformanceDto dto = new OutingTimeOfDailyPerformanceDto();
		dto.setEmployeeId(employeeId());
		dto.setYmd(workingDate());
		dto.setTimeZone(ConvertHelper.mapTo(timeZone,t -> t.clone()));
		if (isHaveData()) {
			dto.exsistData();
		}
		return dto;
	}

	@Override
	public String employeeId() {
		return this.employeeId;
	}

	@Override
	public GeneralDate workingDate() {
		return this.ymd;
	}

	@Override
	public boolean isRoot() { return true; }
	
	

	@Override
	public OutingTimeOfDailyAttd toDomain(String emp, GeneralDate date) {
		if(!this.isHaveData()) {
			return null;
		}
		if (employeeId == null) {
			employeeId = this.employeeId();
		}
		if (date == null) {
			date = this.workingDate();
		}
		OutingTimeOfDailyPerformance domain =  new OutingTimeOfDailyPerformance(emp, date, ConvertHelper.mapTo(timeZone, (c) -> 
											new OutingTimeSheet(new OutingFrameNo(c.getNo()), createTimeActual(c.getOuting()),
													new AttendanceTime(c.getOutTimeCalc()), new AttendanceTime(c.getOutTIme()),
													c.reason(), createTimeActual(c.getComeBack()))));
		return domain.getOutingTime();
	}

	private Optional<WorkStamp> createTimeActual(TimeStampDto c) {
		return c == null ? Optional.empty() : Optional.of(TimeStampDto.toDomain(c));
	}

	@Override
	public AttendanceItemDataGate newInstanceOf(String path) {
		if (path.equals(TIME_ZONE)) {
			return new OutingTimeZoneDto();
		}
		return super.newInstanceOf(path);
	}

	@Override
	@SuppressWarnings("unchecked")
	public <T extends AttendanceItemDataGate> List<T> gets(String path) {
		if (path.equals(TIME_ZONE)) {
			return (List<T>) this.timeZone;
		}
		
		return super.gets(path);
	}

	@Override
	@SuppressWarnings("unchecked")
	public <T extends AttendanceItemDataGate> void set(String path, List<T> value) {
		
		if (path.equals(TIME_ZONE)) {
			this.timeZone = (List<OutingTimeZoneDto>) value;
		}
	}
	
	@Override
	public int size(String path) {
		if (path.equals(TIME_ZONE)) {
			return 10;
		}
		return 0;
	}

	@Override
	public PropType typeOf(String path) {
		if (path.equals(TIME_ZONE)) {
			return PropType.IDX_LIST;
		}
		
		return PropType.OBJECT;
	}
	
}
