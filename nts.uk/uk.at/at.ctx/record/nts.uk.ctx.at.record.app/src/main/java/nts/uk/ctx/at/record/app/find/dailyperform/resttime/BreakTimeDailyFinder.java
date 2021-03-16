package nts.uk.ctx.at.record.app.find.dailyperform.resttime;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;

import nts.arc.time.GeneralDate;
import nts.arc.time.calendar.period.DatePeriod;
import nts.uk.ctx.at.record.app.find.dailyperform.resttime.dto.BreakTimeDailyDto;
import nts.uk.ctx.at.record.dom.breakorgoout.repository.BreakTimeOfDailyPerformanceRepository;
import nts.uk.ctx.at.shared.app.util.attendanceitem.FinderFacade;
import nts.uk.ctx.at.shared.dom.scherec.dailyattdcal.dailyattendance.converter.util.item.ConvertibleAttendanceItem;

@Stateless
@TransactionAttribute(TransactionAttributeType.SUPPORTS)
/** 日別実績の休憩時間帯 Finder */
public class BreakTimeDailyFinder extends FinderFacade {

	@Inject
	private BreakTimeOfDailyPerformanceRepository repo;

	@SuppressWarnings("unchecked")
	@Override
	public BreakTimeDailyDto find(String employeeId, GeneralDate baseDate) {
//		Optional<BreakTimeOfDailyPerformance> domains = this.repo.findByKey(employeeId, baseDate);
//		if (!domains.isEmpty()) {
//			return BreakTimeDailyDto.getDto(domains.get());
//		}
		return this.repo.findByKey(employeeId, baseDate).map(c -> BreakTimeDailyDto.getDto(c))
				.orElse(new BreakTimeDailyDto());
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public <T extends ConvertibleAttendanceItem> List<T> find(List<String> employeeId, DatePeriod baseDate) {
		return (List<T>) this.repo.finds(employeeId, baseDate).stream()
				.map(c -> BreakTimeDailyDto.getDto(c)).collect(Collectors.toList());
	}

	@Override
	@SuppressWarnings("unchecked")
	public <T extends ConvertibleAttendanceItem> List<T> find(Map<String, List<GeneralDate>> param) {
		return (List<T>) this.repo.finds(param).stream()
			.map(c -> BreakTimeDailyDto.getDto(c)).collect(Collectors.toList());
	}

	@Override
	public Object getDomain(String employeeId, GeneralDate baseDate) {
		return repo.findByKey(employeeId, baseDate);
	}
}
