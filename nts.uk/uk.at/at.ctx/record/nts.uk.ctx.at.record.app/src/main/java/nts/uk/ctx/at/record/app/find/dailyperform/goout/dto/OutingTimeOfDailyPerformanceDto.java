package nts.uk.ctx.at.record.app.find.dailyperform.goout.dto;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import lombok.Data;
import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.record.app.find.dailyperform.common.WithActualTimeStampDto;
import nts.uk.ctx.at.record.dom.breakorgoout.OutingTimeOfDailyPerformance;
import nts.uk.ctx.at.record.dom.breakorgoout.OutingTimeSheet;
import nts.uk.ctx.at.record.dom.breakorgoout.enums.GoingOutReason;
import nts.uk.ctx.at.record.dom.breakorgoout.primitivevalue.OutingFrameNo;
import nts.uk.ctx.at.record.dom.worktime.TimeActualStamp;
import nts.uk.ctx.at.shared.app.util.attendanceitem.ConvertHelper;
import nts.uk.ctx.at.shared.dom.attendance.util.anno.AttendanceItemLayout;
import nts.uk.ctx.at.shared.dom.attendance.util.anno.AttendanceItemRoot;
import nts.uk.ctx.at.shared.dom.attendance.util.item.ConvertibleAttendanceItem;
import nts.uk.ctx.at.shared.dom.common.time.AttendanceTime;

@AttendanceItemRoot(rootName = "日別実績の外出時間帯")
@Data
public class OutingTimeOfDailyPerformanceDto implements ConvertibleAttendanceItem {

	private String employeeId;
	
	private GeneralDate ymd;
	
	@AttendanceItemLayout(layout = "A", jpPropertyName = "時間帯", listMaxLength = 10, indexField = "workNo")
	private List<OutingTimeZoneDto> timeZone;
	
	public static OutingTimeOfDailyPerformanceDto getDto(OutingTimeOfDailyPerformance domain) {
		OutingTimeOfDailyPerformanceDto dto = new OutingTimeOfDailyPerformanceDto();
		if (domain != null) {
			dto.setEmployeeId(domain.getEmployeeId());
			dto.setYmd(domain.getYmd());
			dto.setTimeZone(ConvertHelper.mapTo(domain.getOutingTimeSheets(),
					(c) -> new OutingTimeZoneDto(
							c.getOutingFrameNo().v(),
							WithActualTimeStampDto.toWithActualTimeStamp(c.getGoOut().orElse(null)),
							WithActualTimeStampDto.toWithActualTimeStamp(c.getComeBack().orElse(null)),
							c.getReasonForGoOut().value, 
							c.getOutingTimeCalculation() == null ? null : c.getOutingTimeCalculation().valueAsMinutes(),
							c.getOutingTime() == null ? null : c.getOutingTime().valueAsMinutes())));
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
	public OutingTimeOfDailyPerformance toDomain(String emp, GeneralDate date) {
		return new OutingTimeOfDailyPerformance(emp, date, 
					timeZone == null ? new ArrayList<>() : ConvertHelper.mapTo(timeZone,
						(c) -> new OutingTimeSheet(new OutingFrameNo(c.getWorkNo()), createTimeActual(c.getOuting()),
								new AttendanceTime(c.getOutTimeCalc()), new AttendanceTime(c.getOutTIme()),
								ConvertHelper.getEnum(c.getReason(), GoingOutReason.class),
								createTimeActual(c.getComeBack()))));
	}

	private Optional<TimeActualStamp> createTimeActual(WithActualTimeStampDto c) {
		return c == null ? Optional.empty() : Optional.of(c.toDomain());
	}
}
