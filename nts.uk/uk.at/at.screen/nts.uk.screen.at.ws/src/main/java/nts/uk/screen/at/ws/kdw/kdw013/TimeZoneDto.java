package nts.uk.screen.at.ws.kdw.kdw013;

import java.util.Optional;

import lombok.Data;
import lombok.NoArgsConstructor;
import nts.uk.ctx.at.record.app.find.dailyattendance.timesheet.ouen.dto.ReasonTimeChangeDto;
import nts.uk.ctx.at.record.app.find.dailyattendance.timesheet.ouen.dto.WorkTimeInformationDto;
import nts.uk.ctx.at.record.dom.daily.ouen.TimeZone;
import nts.uk.ctx.at.shared.dom.common.time.AttendanceTime;
import nts.uk.ctx.at.shared.dom.scherec.dailyattdcal.dailyattendance.common.timestamp.EngravingMethod;
import nts.uk.ctx.at.shared.dom.scherec.dailyattdcal.dailyattendance.common.timestamp.ReasonTimeChange;
import nts.uk.ctx.at.shared.dom.scherec.dailyattdcal.dailyattendance.common.timestamp.TimeChangeMeans;
import nts.uk.ctx.at.shared.dom.scherec.dailyattdcal.dailyattendance.common.timestamp.WorkTimeInformation;
import nts.uk.shr.com.time.TimeWithDayAttr;

/**
 * 
 * @author tutt
 *
 */

@Data
@NoArgsConstructor
public class TimeZoneDto {

	// 開始時刻: 時刻(日区分付き)
	private WorkTimeInformationDto start;

	// 終了時刻: 時刻(日区分付き)
	private WorkTimeInformationDto end;

	// 作業時間: 勤怠時間
	private Integer workingHours;

	public static TimeZoneDto toDto(TimeZone timeZone) {
		TimeZoneDto timeZoneDto = new TimeZoneDto();
		WorkTimeInformationDto startWorkTimeInformationDto = new WorkTimeInformationDto();
		startWorkTimeInformationDto.setReasonTimeChange(
				new ReasonTimeChangeDto(timeZone.getStart().getReasonTimeChange().getTimeChangeMeans().value,
						timeZone.getStart().getReasonTimeChange().getEngravingMethod().map(m -> m.value).orElse(null)));
		startWorkTimeInformationDto.setTimeWithDay(timeZone.getStart().getTimeWithDay().map(m -> m.v()).orElse(null));

		WorkTimeInformationDto endWorkTimeInformationDto = new WorkTimeInformationDto();
		startWorkTimeInformationDto.setReasonTimeChange(
				new ReasonTimeChangeDto(timeZone.getEnd().getReasonTimeChange().getTimeChangeMeans().value,
						timeZone.getEnd().getReasonTimeChange().getEngravingMethod().map(m -> m.value).orElse(null)));
		startWorkTimeInformationDto.setTimeWithDay(timeZone.getEnd().getTimeWithDay().map(m -> m.v()).orElse(null));

		timeZoneDto.setWorkingHours(timeZone.getWorkingHours().map(m -> m.v()).orElse(null));
		timeZoneDto.setStart(startWorkTimeInformationDto);
		timeZoneDto.setEnd(endWorkTimeInformationDto);

		return timeZoneDto;
	}

	public TimeZone toDomain() {

		return new TimeZone(
				new WorkTimeInformation(
						new ReasonTimeChange(
								TimeChangeMeans.valueOf(this.start.getReasonTimeChange().getTimeChangeMeans()),
								Optional.of(EngravingMethod
										.valueOf(this.start.getReasonTimeChange().getEngravingMethod()))),
						new TimeWithDayAttr(this.start.getTimeWithDay())),
				new WorkTimeInformation(
						new ReasonTimeChange(
								TimeChangeMeans.valueOf(this.end.getReasonTimeChange().getTimeChangeMeans()),
								Optional.of(EngravingMethod
										.valueOf(this.end.getReasonTimeChange().getEngravingMethod()))),
						new TimeWithDayAttr(this.end.getTimeWithDay())),
				Optional.of(new AttendanceTime(this.workingHours)));
	}
}
