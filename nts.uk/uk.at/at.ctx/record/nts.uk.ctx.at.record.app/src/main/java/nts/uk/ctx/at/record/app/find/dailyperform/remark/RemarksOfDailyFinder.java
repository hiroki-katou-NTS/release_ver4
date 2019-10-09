package nts.uk.ctx.at.record.app.find.dailyperform.remark;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;

import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.record.app.find.dailyperform.remark.dto.RemarksOfDailyDto;
import nts.uk.ctx.at.record.dom.daily.remarks.RemarksOfDailyPerform;
import nts.uk.ctx.at.record.dom.daily.remarks.RemarksOfDailyPerformRepo;
import nts.uk.ctx.at.shared.app.util.attendanceitem.FinderFacade;
import nts.uk.ctx.at.shared.dom.attendance.util.item.ConvertibleAttendanceItem;
import nts.uk.shr.com.time.calendar.period.DatePeriod;

@Stateless
@TransactionAttribute(TransactionAttributeType.SUPPORTS)
/** 日別実績の備考 Finder */
public class RemarksOfDailyFinder extends FinderFacade {

	@Inject
	private RemarksOfDailyPerformRepo repo;

	@SuppressWarnings("unchecked")
	@Override
	public RemarksOfDailyDto find(String employeeId, GeneralDate baseDate) {
		List<RemarksOfDailyPerform> domains = this.repo.getRemarks(employeeId, baseDate);
		if (!domains.isEmpty()) {
			return RemarksOfDailyDto.getDto(domains.get(0));
		}
		return new RemarksOfDailyDto();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<RemarksOfDailyDto> finds(String employeeId, GeneralDate baseDate) {
		return this.repo.getRemarks(employeeId, baseDate).stream().map(x -> {
			return RemarksOfDailyDto.getDto(x);
		}).collect(Collectors.toList());
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public <T extends ConvertibleAttendanceItem> List<T> find(List<String> employeeId, DatePeriod baseDate) {
		return (List<T>) this.repo.getRemarks(employeeId, baseDate).stream()
				.map(c -> RemarksOfDailyDto.getDto(c)).collect(Collectors.toList());
	}

	@Override
	@SuppressWarnings("unchecked")
	public <T extends ConvertibleAttendanceItem> List<T> find(Map<String, List<GeneralDate>> param) {
		return (List<T>) this.repo.getRemarks(param).stream()
			.map(c -> RemarksOfDailyDto.getDto(c)).collect(Collectors.toList());
	}

	@Override
	public Object getDomain(String employeeId, GeneralDate baseDate) {
		return repo.getRemarks(employeeId, baseDate);
	}
}
