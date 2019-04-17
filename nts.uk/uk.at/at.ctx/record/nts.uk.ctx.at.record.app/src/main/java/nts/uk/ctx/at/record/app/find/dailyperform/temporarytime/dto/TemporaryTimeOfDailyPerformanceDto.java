package nts.uk.ctx.at.record.app.find.dailyperform.temporarytime.dto;

import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import lombok.Data;
import lombok.EqualsAndHashCode;
import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.record.app.find.dailyperform.common.WithActualTimeStampDto;
import nts.uk.ctx.at.record.app.find.dailyperform.customjson.CustomGeneralDateSerializer;
import nts.uk.ctx.at.record.app.find.dailyperform.workrecord.dto.WorkLeaveTimeDto;
import nts.uk.ctx.at.record.dom.worktime.TemporaryTimeOfDailyPerformance;
import nts.uk.ctx.at.record.dom.worktime.TimeLeavingWork;
import nts.uk.ctx.at.record.dom.worktime.primitivevalue.WorkTimes;
import nts.uk.ctx.at.shared.app.util.attendanceitem.ConvertHelper;
import nts.uk.ctx.at.shared.dom.attendance.util.ItemConst;
import nts.uk.ctx.at.shared.dom.attendance.util.anno.AttendanceItemLayout;
import nts.uk.ctx.at.shared.dom.attendance.util.anno.AttendanceItemRoot;
import nts.uk.ctx.at.shared.dom.attendance.util.anno.AttendanceItemValue;
import nts.uk.ctx.at.shared.dom.attendance.util.item.AttendanceItemCommon;
import nts.uk.ctx.at.shared.dom.attendance.util.item.ValueType;

@Data
@EqualsAndHashCode(callSuper = false)
@AttendanceItemRoot(rootName = ItemConst.DAILY_TEMPORARY_TIME_NAME)
public class TemporaryTimeOfDailyPerformanceDto extends AttendanceItemCommon {

	/***/
	private static final long serialVersionUID = 1L;
	
	private String employeeId;

	@AttendanceItemLayout(layout = LAYOUT_A, jpPropertyName = COUNT)
	@AttendanceItemValue(type = ValueType.COUNT)
	private Integer workTimes;

	@AttendanceItemLayout(layout = LAYOUT_B, jpPropertyName = TIME_ZONE, 
			listMaxLength = 3, indexField = DEFAULT_INDEX_FIELD_NAME)
	private List<WorkLeaveTimeDto> workLeaveTime;

	@JsonDeserialize(using = CustomGeneralDateSerializer.class)
	private GeneralDate ymd;

	public static TemporaryTimeOfDailyPerformanceDto getDto(TemporaryTimeOfDailyPerformance domain) {
		TemporaryTimeOfDailyPerformanceDto dto = new TemporaryTimeOfDailyPerformanceDto();
		if (domain != null) {
			dto.setEmployeeId(domain.getEmployeeId());
			dto.setYmd(domain.getYmd());
			dto.setWorkTimes(domain.getWorkTimes() == null ? null : domain.getWorkTimes().v());
			dto.setWorkLeaveTime(ConvertHelper.mapTo(domain.getTimeLeavingWorks(), (c) -> newWorkLeaveTime(c)));
			dto.exsistData();
		}
		return dto;
	}

	@Override
	public TemporaryTimeOfDailyPerformanceDto clone() {
		TemporaryTimeOfDailyPerformanceDto dto = new TemporaryTimeOfDailyPerformanceDto();
			dto.setEmployeeId(employeeId());
			dto.setYmd(workingDate());
			dto.setWorkTimes(workTimes);
			dto.setWorkLeaveTime(workLeaveTime == null ? null : workLeaveTime.stream().map(t -> t.clone()).collect(Collectors.toList()));
		if (isHaveData()) {
			dto.exsistData();
		}
		return dto;
	}

	private static WorkLeaveTimeDto newWorkLeaveTime(TimeLeavingWork c) {
		return c == null ? null : new WorkLeaveTimeDto(c.getWorkNo().v(), WithActualTimeStampDto.toWithActualTimeStamp(c.getAttendanceStamp().orElse(null)),
				WithActualTimeStampDto.toWithActualTimeStamp(c.getLeaveStamp().orElse(null)));
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
	public TemporaryTimeOfDailyPerformance toDomain(String emp, GeneralDate date) {
		if(!this.isHaveData()) {
			return null;
		}
		if (employeeId == null) {
			employeeId = this.employeeId();
		}
		if (date == null) {
			date = this.workingDate();
		}
		return new TemporaryTimeOfDailyPerformance(emp, new WorkTimes(toWorkTimes()), 
						ConvertHelper.mapTo(workLeaveTime, (c) -> WorkLeaveTimeDto.toDomain(c)), date);
	}

	private int toWorkTimes() {
		return workTimes == null ? (workLeaveTime == null ? 0 : workLeaveTime.size()) : workTimes;
	}
}
