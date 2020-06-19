package nts.uk.ctx.at.record.app.command.dailyperform.calculationattribute;

//import java.util.Optional;

import lombok.Getter;
import nts.uk.ctx.at.record.app.find.dailyperform.calculationattribute.dto.CalcAttrOfDailyPerformanceDto;
import nts.uk.ctx.at.shared.app.util.attendanceitem.DailyWorkCommonCommand;
import nts.uk.ctx.at.shared.dom.attendance.util.item.ConvertibleAttendanceItem;
import nts.uk.ctx.at.shared.dom.calculationattribute.CalAttrOfDailyPerformance;

public class CalcAttrOfDailyPerformanceCommand extends DailyWorkCommonCommand {

	@Getter
	private CalAttrOfDailyPerformance data;

	@Override
	public void setRecords(ConvertibleAttendanceItem item) {
		this.data = item == null || !item.isHaveData() ? null : ((CalcAttrOfDailyPerformanceDto) item).toDomain(getEmployeeId(), getWorkDate());
	}

	@Override
	public void updateData(Object data) {
		if(data == null){ return; }
		this.data = (CalAttrOfDailyPerformance) data;
	}

	@Override
	public CalAttrOfDailyPerformance toDomain() {
		return data;
	}
	
	@Override
	public CalcAttrOfDailyPerformanceDto toDto() {
		return CalcAttrOfDailyPerformanceDto.getDto(getData());
	}
}
