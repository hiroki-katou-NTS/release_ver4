package nts.uk.ctx.at.record.app.command.monthly.dayoff;

import lombok.Getter;
import nts.uk.ctx.at.record.app.command.monthly.MonthlyWorkCommonCommand;
import nts.uk.ctx.at.record.app.find.monthly.root.MonthlyDayoffRemainDataDto;
import nts.uk.ctx.at.shared.dom.attendance.util.item.ConvertibleAttendanceItem;
import nts.uk.ctx.at.shared.dom.monthly.vacation.dayoff.monthremaindata.MonthlyDayoffRemainData;

public class DayOffRemainMonthlyCommand extends MonthlyWorkCommonCommand{

	@Getter
	private MonthlyDayoffRemainDataDto data;
	
	@Override
	public void setRecords(ConvertibleAttendanceItem item) {
		this.data = item == null || !item.isHaveData() ? null : (MonthlyDayoffRemainDataDto) item;
	}

	@Override
	public void updateData(Object data) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public MonthlyDayoffRemainData toDomain() {
		return data.toDomain(getEmployeeId(), getYearMonth(), getClosureId(), getClosureDate());
	}
	
	

}