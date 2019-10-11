package nts.uk.ctx.at.record.app.find.dailyperform.calculationattribute;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;

import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.record.app.find.dailyperform.calculationattribute.dto.CalcAttrOfDailyPerformanceDto;
import nts.uk.ctx.at.record.dom.calculationattribute.repo.CalAttrOfDailyPerformanceRepository;
import nts.uk.ctx.at.shared.app.util.attendanceitem.FinderFacade;
import nts.uk.ctx.at.shared.dom.attendance.util.item.ConvertibleAttendanceItem;
import nts.uk.shr.com.time.calendar.period.DatePeriod;

@Stateless
@TransactionAttribute(TransactionAttributeType.SUPPORTS)
public class CalcAttrOfDailyPerformanceFinder extends FinderFacade {

	@Inject
	private CalAttrOfDailyPerformanceRepository repo;

	@SuppressWarnings("unchecked")
	@Override
	public CalcAttrOfDailyPerformanceDto find(String employeeId, GeneralDate baseDate) {
		return CalcAttrOfDailyPerformanceDto.getDto(this.repo.find(employeeId, baseDate));
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public <T extends ConvertibleAttendanceItem> List<T> find(List<String> employeeId, DatePeriod baseDate) {
		return (List<T>) this.repo.finds(employeeId, baseDate).stream()
				.map(c -> CalcAttrOfDailyPerformanceDto.getDto(c)).collect(Collectors.toList());
	}

	@Override
	@SuppressWarnings("unchecked")
	public <T extends ConvertibleAttendanceItem> List<T> find(Map<String, List<GeneralDate>> param) {
		return (List<T>) this.repo.finds(param).stream()
				.map(c -> CalcAttrOfDailyPerformanceDto.getDto(c)).collect(Collectors.toList());
	}

	@Override
	public Object getDomain(String employeeId, GeneralDate baseDate) {
		return repo.find(employeeId, baseDate);
	}
}
