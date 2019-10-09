package nts.uk.ctx.at.record.app.find.dailyperform.editstate;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;

import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.record.dom.editstate.repository.EditStateOfDailyPerformanceRepository;
import nts.uk.ctx.at.shared.app.util.attendanceitem.ConvertHelper;
import nts.uk.ctx.at.shared.app.util.attendanceitem.FinderFacade;
import nts.uk.ctx.at.shared.dom.attendance.util.item.ConvertibleAttendanceItem;
import nts.uk.shr.com.time.calendar.period.DatePeriod;

@Stateless
@TransactionAttribute(TransactionAttributeType.SUPPORTS)
public class EditStateOfDailyPerformanceFinder extends FinderFacade {

	@Inject
	private EditStateOfDailyPerformanceRepository repo;

	@SuppressWarnings("unchecked")
	@Override
	public EditStateOfDailyPerformanceDto find(String employeeId, GeneralDate baseDate) {
		EditStateOfDailyPerformanceDto result = new EditStateOfDailyPerformanceDto();
		return result;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<EditStateOfDailyPerformanceDto> finds(String employeeId, GeneralDate baseDate) {
		return ConvertHelper.mapTo(this.repo.findByKey(employeeId, baseDate),
				(c) -> EditStateOfDailyPerformanceDto.getDto(c));
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public <T extends ConvertibleAttendanceItem> List<T> find(List<String> employeeId, DatePeriod baseDate) {
		return (List<T>) this.repo.finds(employeeId, baseDate).stream()
				.map(c -> EditStateOfDailyPerformanceDto.getDto(c)).collect(Collectors.toList());
	}

	@Override
	@SuppressWarnings("unchecked")
	public <T extends ConvertibleAttendanceItem> List<T> find(Map<String, List<GeneralDate>> param) {
		return (List<T>) this.repo.finds(param).stream()
			.map(c -> EditStateOfDailyPerformanceDto.getDto(c)).collect(Collectors.toList());
	}

	@Override
	public Object getDomain(String employeeId, GeneralDate baseDate) {
		return repo.findByKey(employeeId, baseDate);
	}

}
