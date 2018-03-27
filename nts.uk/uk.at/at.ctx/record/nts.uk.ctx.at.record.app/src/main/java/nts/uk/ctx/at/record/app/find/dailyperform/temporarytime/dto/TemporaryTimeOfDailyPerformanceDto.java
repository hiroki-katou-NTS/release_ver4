package nts.uk.ctx.at.record.app.find.dailyperform.temporarytime.dto;

import java.util.List;
import java.util.Optional;

import lombok.Data;
import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.record.app.find.dailyperform.common.WithActualTimeStampDto;
import nts.uk.ctx.at.record.app.find.dailyperform.workrecord.dto.WorkLeaveTimeDto;
import nts.uk.ctx.at.record.dom.worktime.TemporaryTimeOfDailyPerformance;
import nts.uk.ctx.at.record.dom.worktime.TimeActualStamp;
import nts.uk.ctx.at.record.dom.worktime.TimeLeavingWork;
import nts.uk.ctx.at.record.dom.worktime.primitivevalue.WorkTimes;
import nts.uk.ctx.at.shared.app.util.attendanceitem.ConvertHelper;
import nts.uk.ctx.at.shared.dom.attendance.util.anno.AttendanceItemLayout;
import nts.uk.ctx.at.shared.dom.attendance.util.anno.AttendanceItemRoot;
import nts.uk.ctx.at.shared.dom.attendance.util.anno.AttendanceItemValue;
import nts.uk.ctx.at.shared.dom.attendance.util.item.AttendanceItemCommon;
import nts.uk.ctx.at.shared.dom.attendance.util.item.ValueType;
import nts.uk.ctx.at.shared.dom.worktime.common.WorkNo;

@Data
@AttendanceItemRoot(rootName = "日別実績の臨時出退勤")
public class TemporaryTimeOfDailyPerformanceDto extends AttendanceItemCommon {

	private String employeeId;

	@AttendanceItemLayout(layout = "A", jpPropertyName = "勤務回数")
	@AttendanceItemValue(type = ValueType.INTEGER)
	private Integer workTimes;

	@AttendanceItemLayout(layout = "B", jpPropertyName = "出退勤", listMaxLength = 3, indexField = "workNo")
	private List<WorkLeaveTimeDto> workLeaveTime;

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
		return new TemporaryTimeOfDailyPerformance(emp, new WorkTimes(toWorkTimes()), 
						ConvertHelper.mapTo(workLeaveTime, (c) -> toTimeLeaveWork(c)), date);
	}

	private int toWorkTimes() {
		return workTimes == null ? (workLeaveTime == null ? 0 : workLeaveTime.size()) : workTimes;
	}

	private TimeLeavingWork toTimeLeaveWork(WorkLeaveTimeDto c) {
		return c == null ? null
				: new TimeLeavingWork(new WorkNo(c.getWorkNo()), toTimeActualStamp(c.getWorking()),
						toTimeActualStamp(c.getLeave()));
	}

	private Optional<TimeActualStamp> toTimeActualStamp(WithActualTimeStampDto c) {
		return c == null ? Optional.empty() : Optional.of(c.toDomain());
	}
}
