package nts.uk.ctx.at.record.app.command.monthly.reserveleave;

import lombok.Getter;
import nts.uk.ctx.at.record.app.command.monthly.MonthlyWorkCommonCommand;
import nts.uk.ctx.at.record.app.find.monthly.root.RsvLeaRemNumEachMonthDto;
import nts.uk.ctx.at.shared.dom.attendance.util.item.ConvertibleAttendanceItem;

public class RsvLeaRemNumEachMonthCommand extends MonthlyWorkCommonCommand{

	@Getter
	private RsvLeaRemNumEachMonthDto data;
	
	@Override
	public void setRecords(ConvertibleAttendanceItem item) {
		this.data = item == null || !item.isHaveData() ? null : (RsvLeaRemNumEachMonthDto) item;
	}

	@Override
	public void updateData(Object data) {
		// TODO Auto-generated method stub
		
	}

}