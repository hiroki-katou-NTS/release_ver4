package nts.uk.ctx.at.record.app.find.dailyperform.goout.dto;

import java.util.List;
import java.util.Optional;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import lombok.Data;
import lombok.EqualsAndHashCode;
import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.record.app.find.dailyperform.common.WithActualTimeStampDto;
import nts.uk.ctx.at.record.app.find.dailyperform.customjson.CustomGeneralDateSerializer;
import nts.uk.ctx.at.record.dom.breakorgoout.OutingTimeOfDailyPerformance;
import nts.uk.ctx.at.record.dom.breakorgoout.OutingTimeSheet;
import nts.uk.ctx.at.record.dom.breakorgoout.primitivevalue.OutingFrameNo;
import nts.uk.ctx.at.record.dom.worktime.TimeActualStamp;
import nts.uk.ctx.at.shared.app.util.attendanceitem.ConvertHelper;
import nts.uk.ctx.at.shared.dom.attendance.util.ItemConst;
import nts.uk.ctx.at.shared.dom.attendance.util.anno.AttendanceItemLayout;
import nts.uk.ctx.at.shared.dom.attendance.util.anno.AttendanceItemRoot;
import nts.uk.ctx.at.shared.dom.attendance.util.item.AttendanceItemCommon;
import nts.uk.ctx.at.shared.dom.common.time.AttendanceTime;

@Data
@EqualsAndHashCode(callSuper = false)
@AttendanceItemRoot(rootName = ItemConst.DAILY_OUTING_TIME_NAME)
public class OutingTimeOfDailyPerformanceDto extends AttendanceItemCommon {

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
			dto.setTimeZone(ConvertHelper.mapTo(domain.getOutingTimeSheets(),
					(c) -> new OutingTimeZoneDto(
							c.getOutingFrameNo().v(),
							WithActualTimeStampDto.toWithActualTimeStamp(c.getGoOut() != null ? c.getGoOut().orElse(null) : null),
							WithActualTimeStampDto.toWithActualTimeStamp(c.getComeBack() != null ? c.getComeBack().orElse(null) : null),
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
	public OutingTimeOfDailyPerformance toDomain(String emp, GeneralDate date) {
		if(!this.isHaveData()) {
			return null;
		}
		if (employeeId == null) {
			employeeId = this.employeeId();
		}
		if (date == null) {
			date = this.workingDate();
		}
		return new OutingTimeOfDailyPerformance(emp, date, ConvertHelper.mapTo(timeZone, (c) -> 
											new OutingTimeSheet(new OutingFrameNo(c.getNo()), createTimeActual(c.getOuting()),
													new AttendanceTime(c.getOutTimeCalc()), new AttendanceTime(c.getOutTIme()),
													c.reason(), createTimeActual(c.getComeBack()))));
	}

	private Optional<TimeActualStamp> createTimeActual(WithActualTimeStampDto c) {
		return c == null ? Optional.empty() : Optional.of(c.toDomain());
	}
}
