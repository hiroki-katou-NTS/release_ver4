package nts.uk.ctx.at.record.app.command.dailyperform.temporarytime;

import java.util.Optional;

import lombok.Getter;
import nts.uk.ctx.at.record.app.find.dailyperform.temporarytime.dto.TemporaryTimeOfDailyPerformanceDto;
import nts.uk.ctx.at.record.dom.worktime.TemporaryTimeOfDailyPerformance;
import nts.uk.ctx.at.shared.app.util.attendanceitem.DailyWorkCommonCommand;
import nts.uk.ctx.at.shared.dom.attendance.util.item.AttendanceItemCommon;

public class TemporaryTimeOfDailyPerformanceCommand extends DailyWorkCommonCommand {

	@Getter
	private Optional<TemporaryTimeOfDailyPerformanceDto> data;

	@Override
	public void setRecords(AttendanceItemCommon item) {
		this.data = item == null || !item.isHaveData() ? Optional.empty() : Optional.of((TemporaryTimeOfDailyPerformanceDto) item);
	}

	@Override
	public void updateData(Object data) {
		if(data == null){ return; }
		setRecords(TemporaryTimeOfDailyPerformanceDto.getDto((TemporaryTimeOfDailyPerformance) data));
	}
	
	@Override
	public Optional<TemporaryTimeOfDailyPerformance> toDomain() {
		return data == null ? null : data.map(c -> c.toDomain(getEmployeeId(), getWorkDate()));
	}
}
