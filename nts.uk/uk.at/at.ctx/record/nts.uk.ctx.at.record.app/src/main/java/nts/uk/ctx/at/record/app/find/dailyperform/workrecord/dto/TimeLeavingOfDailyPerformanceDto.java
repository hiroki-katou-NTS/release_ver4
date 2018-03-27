package nts.uk.ctx.at.record.app.find.dailyperform.workrecord.dto;

import java.util.List;
import java.util.Optional;

import lombok.Data;
import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.record.app.find.dailyperform.common.WithActualTimeStampDto;
import nts.uk.ctx.at.record.dom.worktime.TimeActualStamp;
import nts.uk.ctx.at.record.dom.worktime.TimeLeavingOfDailyPerformance;
import nts.uk.ctx.at.record.dom.worktime.TimeLeavingWork;
import nts.uk.ctx.at.record.dom.worktime.primitivevalue.WorkTimes;
import nts.uk.ctx.at.shared.app.util.attendanceitem.ConvertHelper;
import nts.uk.ctx.at.shared.dom.attendance.util.anno.AttendanceItemLayout;
import nts.uk.ctx.at.shared.dom.attendance.util.anno.AttendanceItemRoot;
import nts.uk.ctx.at.shared.dom.attendance.util.anno.AttendanceItemValue;
import nts.uk.ctx.at.shared.dom.attendance.util.item.AttendanceItemCommon;
import nts.uk.ctx.at.shared.dom.attendance.util.item.ValueType;
import nts.uk.ctx.at.shared.dom.worktime.common.WorkNo;

@AttendanceItemRoot(rootName = "日別実績の出退勤")
@Data
public class TimeLeavingOfDailyPerformanceDto extends AttendanceItemCommon {

	private String employeeId;

	private GeneralDate ymd;

	@AttendanceItemLayout(layout = "A", jpPropertyName = "出退勤", listMaxLength = 2, indexField = "workNo")
	private List<WorkLeaveTimeDto> workAndLeave;

	@AttendanceItemLayout(layout = "B", jpPropertyName = "勤務回数")
	@AttendanceItemValue(type = ValueType.INTEGER)
	private Integer workTimes;

	public static TimeLeavingOfDailyPerformanceDto getDto(TimeLeavingOfDailyPerformance domain) {
		TimeLeavingOfDailyPerformanceDto dto = new TimeLeavingOfDailyPerformanceDto();
		if (domain != null) {
			dto.setEmployeeId(domain.getEmployeeId());
			dto.setYmd(domain.getYmd());
			dto.setWorkTimes(domain.getWorkTimes() != null ? domain.getWorkTimes().v() : null);
			dto.setWorkAndLeave(ConvertHelper.mapTo(domain.getTimeLeavingWorks(),
					(c) -> new WorkLeaveTimeDto(c.getWorkNo().v(),
							WithActualTimeStampDto.toWithActualTimeStamp(c.getAttendanceStamp().orElse(null)),
							WithActualTimeStampDto.toWithActualTimeStamp(c.getLeaveStamp().orElse(null)))));
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
	public TimeLeavingOfDailyPerformance toDomain(String emp, GeneralDate date) {
		if(!this.isHaveData()) {
			return null;
		}
		return new TimeLeavingOfDailyPerformance(emp, new WorkTimes(toWorkTime()),
				ConvertHelper.mapTo(workAndLeave, c -> toTimeLeaveWork(c)), date);
	}

	private int toWorkTime() {
		return workTimes == null ? (workAndLeave == null ? 0 : workAndLeave.size()) : (workTimes);
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
