package nts.uk.ctx.at.record.app.find.dailyperform.calculationattribute;

import java.util.List;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.record.app.find.dailyperform.calculationattribute.dto.CalcAttrOfDailyPerformanceDto;
import nts.uk.ctx.at.record.dom.calculationattribute.repo.CalAttrOfDailyPerformanceRepository;
import nts.uk.ctx.at.shared.app.util.attendanceitem.FinderFacade;
import nts.uk.ctx.at.shared.dom.attendance.util.item.ConvertibleAttendanceItem;
import nts.uk.shr.com.time.calendar.period.DatePeriod;

@Stateless
public class CalcAttrOfDailyPerformanceFinder extends FinderFacade {

	@Inject
	private CalAttrOfDailyPerformanceRepository repo;

	@SuppressWarnings("unchecked")
	@Override
	public CalcAttrOfDailyPerformanceDto find(String employeeId, GeneralDate baseDate) {
		return CalcAttrOfDailyPerformanceDto.getDto(this.repo.find(employeeId, baseDate));
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public <T extends ConvertibleAttendanceItem> List<T> find(List<String> employeeId, DatePeriod baseDate) {
		return (List<T>) this.repo.finds(employeeId, baseDate).stream()
				.map(c -> CalcAttrOfDailyPerformanceDto.getDto(c)).collect(Collectors.toList());
	}
}
