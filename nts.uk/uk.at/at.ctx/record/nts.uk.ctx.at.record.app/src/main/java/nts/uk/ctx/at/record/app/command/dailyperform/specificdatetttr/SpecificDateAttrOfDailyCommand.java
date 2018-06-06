package nts.uk.ctx.at.record.app.command.dailyperform.specificdatetttr;

import java.util.Optional;

import lombok.Getter;
import nts.uk.ctx.at.record.app.find.dailyperform.specificdatetttr.dto.SpecificDateAttrOfDailyPerforDto;
import nts.uk.ctx.at.record.dom.raisesalarytime.SpecificDateAttrOfDailyPerfor;
import nts.uk.ctx.at.shared.app.util.attendanceitem.DailyWorkCommonCommand;
import nts.uk.ctx.at.shared.dom.attendance.util.item.ConvertibleAttendanceItem;

public class SpecificDateAttrOfDailyCommand extends DailyWorkCommonCommand {

	@Getter
	private Optional<SpecificDateAttrOfDailyPerfor> data;

	@Override
	public void setRecords(ConvertibleAttendanceItem item) {
		this.data = item == null || !item.isHaveData() ? Optional.empty() 
				: Optional.of(((SpecificDateAttrOfDailyPerforDto) item).toDomain(getEmployeeId(), getWorkDate()));
	}

	@Override
	public void updateData(Object item) {
		if(data == null){ return; }
		this.data = Optional.of((SpecificDateAttrOfDailyPerfor) item);
	}
	
	@Override
	public Optional<SpecificDateAttrOfDailyPerfor> toDomain() {
		return this.data;
	}

	@Override
	public Optional<SpecificDateAttrOfDailyPerforDto> toDto() {
		return getData().map(b -> SpecificDateAttrOfDailyPerforDto.getDto(b));
	}

	@Override
	@SuppressWarnings("unchecked")
	public void updateDataO(Optional<?> data) {
		this.data = (Optional<SpecificDateAttrOfDailyPerfor>) data;
	}
}
