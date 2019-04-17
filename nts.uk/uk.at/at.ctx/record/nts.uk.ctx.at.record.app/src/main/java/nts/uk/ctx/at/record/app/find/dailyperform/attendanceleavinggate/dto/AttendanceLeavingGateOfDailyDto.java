package nts.uk.ctx.at.record.app.find.dailyperform.attendanceleavinggate.dto;

import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import lombok.Data;
import lombok.EqualsAndHashCode;
import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.record.app.find.dailyperform.common.TimeSheetDto;
import nts.uk.ctx.at.record.app.find.dailyperform.common.TimeStampDto;
import nts.uk.ctx.at.record.app.find.dailyperform.customjson.CustomGeneralDateSerializer;
import nts.uk.ctx.at.record.dom.daily.attendanceleavinggate.AttendanceLeavingGate;
import nts.uk.ctx.at.record.dom.daily.attendanceleavinggate.AttendanceLeavingGateOfDaily;
import nts.uk.ctx.at.shared.app.util.attendanceitem.ConvertHelper;
import nts.uk.ctx.at.shared.dom.attendance.util.ItemConst;
import nts.uk.ctx.at.shared.dom.attendance.util.anno.AttendanceItemLayout;
import nts.uk.ctx.at.shared.dom.attendance.util.anno.AttendanceItemRoot;
import nts.uk.ctx.at.shared.dom.attendance.util.item.AttendanceItemCommon;
import nts.uk.ctx.at.shared.dom.worktime.common.WorkNo;

@Data
@EqualsAndHashCode(callSuper = false)
@AttendanceItemRoot(rootName = ItemConst.DAILY_ATTENDANCE_LEAVE_GATE_NAME)
public class AttendanceLeavingGateOfDailyDto extends AttendanceItemCommon {

	/***/
	private static final long serialVersionUID = 1L;
	
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
													TimeStampDto.toDomain(c.getStart()),
													TimeStampDto.toDomain(c.getEnd()))));
	}
}
