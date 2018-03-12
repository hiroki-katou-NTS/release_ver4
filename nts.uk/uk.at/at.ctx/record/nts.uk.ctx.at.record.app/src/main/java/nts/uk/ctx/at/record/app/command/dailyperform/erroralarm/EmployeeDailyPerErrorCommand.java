package nts.uk.ctx.at.record.app.command.dailyperform.erroralarm;

import lombok.Getter;
import nts.uk.ctx.at.record.app.find.dailyperform.erroralarm.dto.EmployeeDailyPerErrorDto;
import nts.uk.ctx.at.record.dom.workrecord.erroralarm.EmployeeDailyPerError;
import nts.uk.ctx.at.shared.app.util.attendanceitem.DailyWorkCommonCommand;
import nts.uk.ctx.at.shared.dom.attendance.util.item.AttendanceItemCommon;

public class EmployeeDailyPerErrorCommand extends DailyWorkCommonCommand {

	@Getter
	private EmployeeDailyPerError data;

	@Override
	public void setRecords(AttendanceItemCommon item) {
		this.data = item == null || !item.isHaveData() ? null : ((EmployeeDailyPerErrorDto) item).toDomain(getEmployeeId(), getWorkDate());
	}

	@Override
	public void updateData(Object data) {
		this.data = (EmployeeDailyPerError) data;
	}
}
