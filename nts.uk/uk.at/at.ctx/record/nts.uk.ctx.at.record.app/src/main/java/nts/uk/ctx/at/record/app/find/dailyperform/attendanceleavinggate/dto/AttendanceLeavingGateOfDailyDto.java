package nts.uk.ctx.at.record.app.find.dailyperform.attendanceleavinggate.dto;

import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import lombok.Data;
import nts.arc.enums.EnumAdaptor;
import nts.arc.layer.ws.json.serializer.GeneralDateDeserializer;
import nts.arc.layer.ws.json.serializer.GeneralDateSerializer;
import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.record.app.find.dailyperform.common.TimeSheetDto;
import nts.uk.ctx.at.record.app.find.dailyperform.common.TimeStampDto;
import nts.uk.ctx.at.record.app.find.dailyperform.customjson.CustomGeneralDateSerializer;
import nts.uk.ctx.at.record.dom.daily.attendanceleavinggate.AttendanceLeavingGate;
import nts.uk.ctx.at.record.dom.daily.attendanceleavinggate.AttendanceLeavingGateOfDaily;
import nts.uk.ctx.at.record.dom.worklocation.WorkLocationCD;
import nts.uk.ctx.at.record.dom.worktime.WorkStamp;
import nts.uk.ctx.at.record.dom.worktime.enums.StampSourceInfo;
import nts.uk.ctx.at.shared.app.util.attendanceitem.ConvertHelper;
import nts.uk.ctx.at.shared.dom.attendance.util.ItemConst;
import nts.uk.ctx.at.shared.dom.attendance.util.anno.AttendanceItemLayout;
import nts.uk.ctx.at.shared.dom.attendance.util.anno.AttendanceItemRoot;
import nts.uk.ctx.at.shared.dom.attendance.util.item.AttendanceItemCommon;
import nts.uk.ctx.at.shared.dom.worktime.common.WorkNo;
import nts.uk.shr.com.time.TimeWithDayAttr;

@Data
@AttendanceItemRoot(rootName = ItemConst.DAILY_ATTENDANCE_LEAVE_GATE_NAME)
public class AttendanceLeavingGateOfDailyDto extends AttendanceItemCommon {

	private String employeeId;
	
	@JsonDeserialize(using = CustomGeneralDateSerializer.class)
	private GeneralDate ymd;
	
	@AttendanceItemLayout(layout = LAYOUT_A, jpPropertyName = TIME_ZONE, listMaxLength = 3, indexField = DEFAULT_INDEX_FIELD_NAME)
	private List<TimeSheetDto> attendanceLeavingGateTime;
	
	@Override
	public AttendanceLeavingGateOfDailyDto clone() {
		AttendanceLeavingGateOfDailyDto dto = new AttendanceLeavingGateOfDailyDto();
		dto.setAttendanceLeavingGateTime(attendanceLeavingGateTime == null ? null 
				: attendanceLeavingGateTime.stream().map(t -> t.clone()).collect(Collectors.toList()));
		dto.setEmployeeId(employeeId());
		dto.setYmd(workingDate());
		if (isHaveData()) {
			dto.exsistData();
		}
		return dto;
	}
	
	public static AttendanceLeavingGateOfDailyDto getDto(AttendanceLeavingGateOfDaily domain){
		AttendanceLeavingGateOfDailyDto dto = new AttendanceLeavingGateOfDailyDto();
		if (domain != null) {
			dto.setAttendanceLeavingGateTime(ConvertHelper.mapTo(domain.getAttendanceLeavingGates(),
					(c) -> new TimeSheetDto(c.getWorkNo().v(),
											TimeStampDto.createTimeStamp(c.getAttendance().orElse(null)),
											TimeStampDto.createTimeStamp(c.getLeaving().orElse(null)),
											0)));
			dto.setEmployeeId(domain.getEmployeeId());
			dto.setYmd(domain.getYmd());
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
	public AttendanceLeavingGateOfDaily toDomain(String employeeId, GeneralDate ymd) {
		if(!this.isHaveData()) {
			return null;
		}
		if (employeeId == null) {
			employeeId = this.employeeId();
		}
		if (ymd == null) {
			ymd = this.workingDate();
		}
		return new AttendanceLeavingGateOfDaily(employeeId, ymd, ConvertHelper.mapTo(attendanceLeavingGateTime,
						(c) -> new AttendanceLeavingGate(new WorkNo(c.getNo()),
															createWorkStamp(c.getStart()),
															createWorkStamp(c.getEnd()))));
	}

	private WorkStamp createWorkStamp(TimeStampDto c) {
		return c == null ? null : new WorkStamp(
				c.getAfterRoundingTimesOfDay() == null ? null : new TimeWithDayAttr(c.getAfterRoundingTimesOfDay()),
				c.getTimesOfDay() == null ? null : new TimeWithDayAttr(c.getTimesOfDay()),
				c.getPlaceCode() == null ? null : new WorkLocationCD(c.getPlaceCode()),
				c.getStampSourceInfo() == null ? StampSourceInfo.HAND_CORRECTION_BY_MYSELF : EnumAdaptor.valueOf(c.getStampSourceInfo(), StampSourceInfo.class));
	}
}
