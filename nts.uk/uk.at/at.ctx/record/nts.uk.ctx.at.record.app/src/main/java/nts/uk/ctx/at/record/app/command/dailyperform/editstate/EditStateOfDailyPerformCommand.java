package nts.uk.ctx.at.record.app.command.dailyperform.editstate;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
//import java.util.Optional;
import java.util.stream.Collectors;

import lombok.Getter;
import nts.uk.ctx.at.record.app.find.dailyperform.editstate.EditStateOfDailyPerformanceDto;
import nts.uk.ctx.at.record.dom.editstate.EditStateOfDailyPerformance;
import nts.uk.ctx.at.shared.app.util.attendanceitem.DailyWorkCommonCommand;
import nts.uk.ctx.at.shared.dom.attendance.util.item.ConvertibleAttendanceItem;

public class EditStateOfDailyPerformCommand extends DailyWorkCommonCommand {

	@Getter
	private List<EditStateOfDailyPerformance> data = new ArrayList<>();

	@Getter
	private Set<Integer> changedItem = new HashSet<>();

	@Override
	public void setRecords(ConvertibleAttendanceItem item) {
		if(item != null && item.isHaveData()){
			updateData(((EditStateOfDailyPerformanceDto) item).toDomain(getEmployeeId(), getWorkDate()));
		}
	}

	@Override
	public void updateData(Object data) {
		if(data != null){
			EditStateOfDailyPerformance d = (EditStateOfDailyPerformance) data;
			this.data.removeIf(es -> es.getAttendanceItemId() == d.getAttendanceItemId());
			this.data.add(d);
		}
	}
	
	public void itemChanged(Collection<Integer> items){
		changedItem.addAll(items);
	}

	@Override
	public List<EditStateOfDailyPerformance> toDomain() {
		return data;
	}

	@Override
	public List<EditStateOfDailyPerformanceDto> toDto() {
		return getData().stream().map(b -> EditStateOfDailyPerformanceDto.getDto(b)).collect(Collectors.toList());
	}
}
